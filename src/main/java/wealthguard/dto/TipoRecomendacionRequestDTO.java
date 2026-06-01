package wealthguard.dto;

public class TipoRecomendacionRequestDTO {

    private String nombre;
    private String mensaje;

    public TipoRecomendacionRequestDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
