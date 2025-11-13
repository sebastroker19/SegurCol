package co.edu.uniquindio.poo.segurcol.model;
public class Vigilante extends Empleado {
    private String numeroPuesto;
    private String tipoArma; // "no letal", "letal", "sin arma"

    public Vigilante(String nombre, String documento, String turno, double salarioBase,
                     String numeroPuesto, String tipoArma) {
        super(nombre, documento, turno, salarioBase);
        this.numeroPuesto = numeroPuesto;
        this.tipoArma = tipoArma;
    }

    @Override
    public double calcularSalarioTotal() {
        // Lógica específica para vigilante (horas extras, bonificaciones)
        return getSalarioBase();
    }

    // Getters y Setters
    public String getNumeroPuesto() { return numeroPuesto; }
    public void setNumeroPuesto(String numeroPuesto) { this.numeroPuesto = numeroPuesto; }

    public String getTipoArma() { return tipoArma; }
    public void setTipoArma(String tipoArma) { this.tipoArma = tipoArma; }
}