package wealthguard.dto;

public class PreguntaSeguridadResponseDTO {
    private String pregunta;

    public PreguntaSeguridadResponseDTO() {
    }

    public PreguntaSeguridadResponseDTO(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
}