package co.edu.uniquindio.poo.segurcol.model;
public abstract class Servicio {
    private String codigoContrato;
    private String cliente;
    private double tarifaMensual;
    private String estado; // "activo", "suspendido", "finalizado"

    public Servicio(String codigoContrato, String cliente, double tarifaMensual, String estado) {
        this.codigoContrato = codigoContrato;
        this.cliente = cliente;
        this.tarifaMensual = tarifaMensual;
        this.estado = estado;
    }

    public abstract double calcularCostoMensual();

    // Getters y Setters
    public String getCodigoContrato() { return codigoContrato; }
    public void setCodigoContrato(String codigoContrato) { this.codigoContrato = codigoContrato; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public double getTarifaMensual() { return tarifaMensual; }
    public void setTarifaMensual(double tarifaMensual) { this.tarifaMensual = tarifaMensual; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}