package co.edu.uniquindio.poo.segurcol.model;
public abstract class Empleado {
    private String nombre;
    private String documento;
    private String turno; // "día" o "noche"
    private double salarioBase;

    public Empleado(String nombre, String documento, String turno, double salarioBase) {
        this.nombre = nombre;
        this.documento = documento;
        this.turno = turno;
        this.salarioBase = salarioBase;
    }

    public abstract double calcularSalarioTotal();

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) { this.salarioBase = salarioBase; }
}