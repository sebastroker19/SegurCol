package co.edu.uniquindio.poo.segurcol.model;
import java.time.LocalDateTime;

public class RegistroNovedad {
    private LocalDateTime fechaHora;
    private String tipo; // "incidente", "asignacion", "alerta", "devolucion"
    private String descripcion;
    private Empleado empleado;
    private Servicio servicio;

    public RegistroNovedad(LocalDateTime fechaHora, String tipo, String descripcion,
                           Empleado empleado, Servicio servicio) {
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.empleado = empleado;
        this.servicio = servicio;
    }

    // Getters (record es inmutable, solo getters)
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public Empleado getEmpleado() { return empleado; }
    public Servicio getServicio() { return servicio; }
}