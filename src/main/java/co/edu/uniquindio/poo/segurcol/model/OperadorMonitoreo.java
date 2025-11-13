package co.edu.uniquindio.poo.segurcol.model;
public class OperadorMonitoreo extends Empleado {
    private int numeroZonasMonitoreo;

    public OperadorMonitoreo(String nombre, String documento, String turno, double salarioBase,
                             int numeroZonasMonitoreo) {
        super(nombre, documento, turno, salarioBase);
        this.numeroZonasMonitoreo = numeroZonasMonitoreo;
    }

    @Override
    public double calcularSalarioTotal() {
        // Bono por zonas monitoreadas
        double bonoZonas = numeroZonasMonitoreo * 50000;
        return getSalarioBase() + bonoZonas;
    }

    // Getters y Setters
    public int getNumeroZonasMonitoreo() { return numeroZonasMonitoreo; }
    public void setNumeroZonasMonitoreo(int numeroZonasMonitoreo) {
        this.numeroZonasMonitoreo = numeroZonasMonitoreo;
    }
}