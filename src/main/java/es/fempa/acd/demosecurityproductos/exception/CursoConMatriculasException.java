package es.fempa.acd.demosecurityproductos.exception;

public class CursoConMatriculasException extends IllegalStateException {
    private final Long cursoId;
    private final int cantidadMatriculas;

    public CursoConMatriculasException(Long cursoId, int cantidadMatriculas) {
        super("No se puede eliminar el curso porque tiene " + cantidadMatriculas +
              " matrícula(s) registrada(s). Por favor, cancele las matrículas primero.");
        this.cursoId = cursoId;
        this.cantidadMatriculas = cantidadMatriculas;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public int getCantidadMatriculas() {
        return cantidadMatriculas;
    }
}
