package wealthguard.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import wealthguard.dto.PresupuestoRequestDTO;
import wealthguard.dto.PresupuestoResponseDTO;
import wealthguard.entity.PresupuestoEntity;
import wealthguard.service.IPresupuestoService;

public class PresupuestoServiceImpl implements IPresupuestoService {

    @Override
    public PresupuestoResponseDTO crearPresupuesto(PresupuestoRequestDTO presupuestoRequest) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'crearPresupuesto'");
        return null;
    }

    @Override
    public boolean eliminarPresupuesto(int idPresupuesto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarPresupuesto'");
    }

    @Override
    public boolean editarPresupuesto(int idPresupuesto, int idCategoria, double limite, LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editarPresupuesto'");
    }

    @Override
    public List<PresupuestoEntity> obtenerPresupuestos(int idUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerPresupuestos'");
    }

}

