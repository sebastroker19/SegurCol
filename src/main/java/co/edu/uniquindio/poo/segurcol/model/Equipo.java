package co.edu.uniquindio.poo.segurcol.model;
public class Equipo {
    private String codigo;
    private String tipo; // "radio", "arma", "vehículo", "uniforme"
    private String estado; // "activo", "mantenimiento", "retirado"
    private double valorReposicion;

    public Equipo(String codigo, String tipo, String estado, double valorReposicion) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.estado = estado;
        this.valorReposicion = valorReposicion;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getValorReposicion() { return valorReposicion; }
    public void setValorReposicion(double valorReposicion) { this.valorReposicion = valorReposicion; }
}