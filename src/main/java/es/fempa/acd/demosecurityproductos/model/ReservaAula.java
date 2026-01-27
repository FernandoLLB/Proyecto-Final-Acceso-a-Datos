package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva_aula", indexes = {
    @Index(name = "idx_reserva_academia", columnList = "academia_id"),
    @Index(name = "idx_reserva_aula", columnList = "aula_id"),
    @Index(name = "idx_reserva_fechas", columnList = "fecha_inicio, fecha_fin"),
    @Index(name = "idx_reserva_estado", columnList = "estado"),
    @Index(name = "idx_reserva_aula_fechas", columnList = "aula_id, fecha_inicio, fecha_fin, estado")
})
public class ReservaAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La academia es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academia_id", nullable = false)
    private Academia academia;

    @NotNull(message = "El aula es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    @NotNull(message = "El usuario creador es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creada_por", nullable = false)
    private Usuario creadaPor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cancelada_por")
    private Usuario canceladaPor;

    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    public ReservaAula() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoReserva.ACTIVA;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Academia getAcademia() {
        return academia;
    }

    public void setAcademia(Academia academia) {
        this.academia = academia;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getCreadaPor() {
        return creadaPor;
    }

    public void setCreadaPor(Usuario creadaPor) {
        this.creadaPor = creadaPor;
    }

    public Usuario getCanceladaPor() {
        return canceladaPor;
    }

    public void setCanceladaPor(Usuario canceladaPor) {
        this.canceladaPor = canceladaPor;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }
}
