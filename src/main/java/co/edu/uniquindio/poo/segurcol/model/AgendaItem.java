package co.edu.uniquindio.poo.segurcol.model;
import java.time.LocalDate;


public class AgendaItem {
    private LocalDate fecha;
    private String descripcion;
    private Empleado empleado;
    private Servicio servicio;
    private String tipoEvento; // "turno", "ronda", "chequeo", "custodia"

    public AgendaItem(LocalDate fecha, String descripcion, Empleado empleado,
                      Servicio servicio, String tipoEvento) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.empleado = empleado;
        this.servicio = servicio;
        this.tipoEvento = tipoEvento;
    }

    // Getters y Setters
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
}