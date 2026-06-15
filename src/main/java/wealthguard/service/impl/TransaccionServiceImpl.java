package wealthguard.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import wealthguard.dto.TransaccionRequestDTO;
import wealthguard.dto.TransaccionResponseDTO;
import wealthguard.entity.CategoriaEntity;
import wealthguard.entity.ObjetivoEntity;
import wealthguard.entity.TransaccionEntity;
import wealthguard.mapper.TransaccionMapper;
import wealthguard.repository.CategoriaRepository;
import wealthguard.repository.ObjetivoRepository;
import wealthguard.repository.TransaccionRepository;
import wealthguard.service.ITransaccionService;

@Service
public class TransaccionServiceImpl implements ITransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private ObjetivoRepository objetivoRepository;

    @Autowired
    private TransaccionMapper transaccionMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Metodo para listar transacciones con filtros dinamicos
    @Override
    public List<TransaccionResponseDTO> listarTransacciones(Integer idUsuario, LocalDateTime fechaInicio,
            LocalDateTime fechaFin, Integer idCategoria, Boolean tipo, Double cantidad, String descripcion) {

        if (fechaFin == null)
            fechaFin = LocalDateTime.now();
        if (fechaInicio == null)
            fechaInicio = fechaFin.minusDays(7);

        List<TransaccionEntity> transacciones = transaccionRepository.buscarConFiltros(idUsuario, fechaInicio, fechaFin,
                idCategoria, tipo, cantidad, descripcion);

        // Convertir de lista de entidades a lista de DTOs
        return transacciones.stream()
                .map(transaccion -> transaccionMapper.convertirADTO(transaccion)).collect(Collectors.toList());

    }

    // Metodo para crear una nueva transaccion
    @Override
    public TransaccionResponseDTO crearTransaccion(TransaccionRequestDTO transaccionRequestDTO) {

        // Convertimos de DTO a entidad
        TransaccionEntity nuevaEntidad = transaccionMapper.convertirAEntity(transaccionRequestDTO);

        if (nuevaEntidad.getCategoria() != null) {
            CategoriaEntity categoriaReal = categoriaRepository.findById(nuevaEntidad.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            nuevaEntidad.setCategoria(categoriaReal);

        }
        // Guardamos la entidad en la base de datos
        TransaccionEntity entidadGuardada = transaccionRepository.save(nuevaEntidad);
        // Convertimos de entidad a DTO y lo devolvemos
        return transaccionMapper.convertirADTO(entidadGuardada);

    }

    // Metodo para editar una transaccion
    @Override
    public TransaccionResponseDTO editarTransaccion(Integer idTransaccion,
            TransaccionRequestDTO transaccionRequestDTO) {

        // Buscamos la transaccion a editar en la base de datos
        TransaccionEntity transaccionExistente = transaccionRepository.findById(idTransaccion)
                .orElseThrow(() -> new RuntimeException("Transaccion no encontrada"));

        // Comprobamos que la transaccion a editar no tenga mas de 3 meses de antiguedad
        LocalDateTime limite = LocalDateTime.now().minusMonths(3);
        if (transaccionExistente.getFecha().isBefore(limite)) {
            throw new RuntimeException("No puedes editar transacciones con más de 3 meses de antigüedad.");
        }

        // Convertimos de DTO a entidad
        TransaccionEntity transaccionActualizada = transaccionMapper.convertirAEntity(transaccionRequestDTO);
        transaccionActualizada.setId(idTransaccion);

        // Guardamos la entidad actualizada en la base de datos
        TransaccionEntity entidadActualizada = transaccionRepository.save(transaccionActualizada);
        return transaccionMapper.convertirADTO(entidadActualizada);

    }

    // Metodo para eliminar una transaccion
    @Override
    public boolean eliminarTransaccion(Integer idTransaccion) {
        // Buscamos la transacción para verificar su fecha
        TransaccionEntity transaccion = transaccionRepository.findById(idTransaccion)
                .orElse(null);

        if (transaccion == null) {
            return false;
        }

        // Comprobamos la restricción de los 3 meses
        LocalDateTime limite = LocalDateTime.now().minusMonths(3);
        if (transaccion.getFecha().isBefore(limite)) {
            return false;
        }

        try {
            transaccionRepository.deleteById(idTransaccion);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Metodo para obtener la tendencia de gastos de un usuario comparando el % del
    // mes actual con el anterior
    @Override
    public double obtenerTendencia(int idUsuario) {

        YearMonth mesActual = YearMonth.now();
        YearMonth mesAnterior = mesActual.minusMonths(1);

        // Obtenemos las fechas del mes actual
        LocalDateTime inicioMesActual = mesActual.atDay(1).atStartOfDay();
        LocalDateTime finMesActual = mesActual.atEndOfMonth().atTime(LocalTime.MAX);

        // Obtenemos las fechas del mes anterior
        LocalDateTime inicioMesAnterior = mesAnterior.atDay(1).atStartOfDay();
        LocalDateTime finMesAnterior = mesAnterior.atEndOfMonth().atTime(LocalTime.MAX);

        // Obtenemos el balance actual y el anterior
        Double balanceActual = transaccionRepository.obtenerBalanceEntreFechas(idUsuario, inicioMesActual,
                finMesActual);
        Double balanceAnterior = transaccionRepository.obtenerBalanceEntreFechas(idUsuario, inicioMesAnterior,
                finMesAnterior);

        // Manejamos los nulos, si no hay transacciones el balance será 0
        if (balanceActual == null) {
            balanceActual = 0.0;
        }
        if (balanceAnterior == null) {
            balanceAnterior = 0.0;
        }

        // Si el mes anterior era 0, la tendencia es 100% si subio o 0% si bajo o si se
        // quedo igual
        if (balanceAnterior == 0.0) {
            if (balanceActual > 0)
                return 100.0;
            else
                return 0.0;
        }

        // Formula de % de crecimiento
        return ((balanceActual - balanceAnterior) / Math.abs(balanceAnterior)) * 100;

    }

    // Metodo para obtener la categoria principal de un usuario
    @Override
    public String[] obtenerCategoriaPrincipal(int idUsuario) {
        // Calculamos las fechas de este mes
        java.time.YearMonth mesActual = java.time.YearMonth.now();
        LocalDateTime inicioMes = mesActual.atDay(1).atStartOfDay();
        LocalDateTime finMes = mesActual.atEndOfMonth().atTime(LocalTime.MAX);

        // Buscamos las transacciones del usuario
        List<TransaccionEntity> transacciones = transaccionRepository.findByUsuarioId(idUsuario);

        // Creamos un map para ir sumando el total de gasto por categoria
        java.util.Map<String, Double> sumasPorCategoria = new java.util.HashMap<>();
        double totalGastos = 0.0;

        // Recorremos las transacciones para ir sumando el gasto por categoria
        for (TransaccionEntity t : transacciones) {

            // Comprobamos si es un gasto y si es de este mes
            boolean esGasto = t.getTipoTransaccion() != null && !t.getTipoTransaccion();
            boolean esDeEsteMes = t.getFecha() != null && !t.getFecha().isBefore(inicioMes)
                    && !t.getFecha().isAfter(finMes);

            // Si cumple las dos condiciones, hacemos las sumas
            if (esGasto && esDeEsteMes) {
                String nombreCat = t.getCategoria().getNombre();
                double cantidad = t.getCantidad();

                totalGastos += cantidad;

                // Sumamos el gasto a la categoria correspondiente
                double sumaAnterior = sumasPorCategoria.getOrDefault(nombreCat, 0.0);
                sumasPorCategoria.put(nombreCat, sumaAnterior + cantidad);
            }
        }

        // Si después de mirar todo no hay gastos este mes, salimos
        if (totalGastos == 0.0) {
            return new String[] { "Sin datos", "0.0" };
        }

        // Sacamos la categoria con mas gasto y su cantidad
        String categoriaPrincipal = "";
        double maximoGasto = 0.0;

        for (java.util.Map.Entry<String, Double> entrada : sumasPorCategoria.entrySet()) {
            if (entrada.getValue() > maximoGasto) {
                maximoGasto = entrada.getValue();
                categoriaPrincipal = entrada.getKey();
            }
        }

        // Calculamos el porcentaje
        double porcentaje = (maximoGasto / totalGastos) * 100;

        return new String[] { categoriaPrincipal, String.format("%.2f", porcentaje) };

    }

    @Override
    @Transactional
    public double[] obtenerMeta(int idUsuario) {

        // Buscamos los objetivos activos del usuario
        Optional<ObjetivoEntity> objetivos = objetivoRepository.findFirstByUsuarioIdOrderByFechaInicioDesc(idUsuario);

        // Realizamos la validación inicial
        if (objetivos.isEmpty()) {
            return new double[] { 0.0, 0.0 };
        }

        // Obtenemos la meta actual
        ObjetivoEntity metaActual = objetivos.get();

        // Comprobamos si la meta ha caducado
        if (metaActual.getFechaFin().isBefore(LocalDateTime.now())) {
            objetivoRepository.delete(metaActual);
            return new double[] { 0.0, 0.0 };
        }

        double cantidadMeta = metaActual.getCantidadObjetivo();

        // Calculamos el rango del mes actual
        YearMonth mesActual = YearMonth.now();
        LocalDateTime inicioMesActual = mesActual.atDay(1).atStartOfDay();
        LocalDateTime finMesActual = mesActual.atEndOfMonth().atTime(LocalTime.MAX);

        // Buscamos las transacciones de la meta actual
        List<TransaccionEntity> transaccionesMeta = transaccionRepository.buscarConFiltros(idUsuario,
                inicioMesActual,
                finMesActual, null, null, null, null);

        // Si la base de datos nos devolvio transacciones validas, las sumamos
        double progresoActual = 0.0;
        if (transaccionesMeta != null) {
            for (TransaccionEntity transaccion : transaccionesMeta) {
                if (transaccion.getTipoTransaccion() != null && transaccion.getTipoTransaccion()) {
                    // Si es true (Ingreso), lo sumamos
                    progresoActual += transaccion.getCantidad();
                } else {
                    // Si es false (Gasto), lo restamos
                    progresoActual -= transaccion.getCantidad();
                }
            }
        }

        // Si la meta es mayor que 0, se divide el progreso entre la meta y se
        // multiplica por 100 para obtener el %
        // Si es 0 me negativo se devuelve 0
        double porcentaje;
        if (cantidadMeta > 0) {
            porcentaje = (progresoActual / cantidadMeta) * 100;
        } else {
            porcentaje = 0.0;

        }

        // Ponemos que el maximo sea 100%
        if (porcentaje > 100.0) {
            porcentaje = 100.0;
        }

        return new double[] { progresoActual, cantidadMeta };
    }

    // Metodo para listar todas las transacciones de un usuario
    @Override
    public List<TransaccionResponseDTO> listarTodasPorUsuario(Integer idUsuario) {

        List<TransaccionEntity> transacciones = transaccionRepository.findByUsuarioId(idUsuario);
        return transacciones.stream()
                .map(transaccionMapper::convertirADTO).toList();
    }
}