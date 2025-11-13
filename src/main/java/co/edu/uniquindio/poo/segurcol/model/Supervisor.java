package co.edu.uniquindio.poo.segurcol.model;
import java.util.List;
import java.util.ArrayList;

public class Supervisor extends Empleado {
    private double bonoCoordinacion;
    private List<Vigilante> vigilantesSupervisados;

    public Supervisor(String nombre, String documento, String turno, double salarioBase,
                      double bonoCoordinacion) {
        super(nombre, documento, turno, salarioBase);
        this.bonoCoordinacion = bonoCoordinacion;
        this.vigilantesSupervisados = new ArrayList<>();
    }

    @Override
    public double calcularSalarioTotal() {
        return getSalarioBase() + bonoCoordinacion;
    }

    public void agregarVigilante(Vigilante vigilante) {
        vigilantesSupervisados.add(vigilante);
    }

    // Getters y Setters
    public double getBonoCoordinacion() { return bonoCoordinacion; }
    public void setBonoCoordinacion(double bonoCoordinacion) { this.bonoCoordinacion = bonoCoordinacion; }

    public List<Vigilante> getVigilantesSupervisados() { return vigilantesSupervisados; }
    public void setVigilantesSupervisados(List<Vigilante> vigilantesSupervisados) {
        this.vigilantesSupervisados = vigilantesSupervisados;
    }
}