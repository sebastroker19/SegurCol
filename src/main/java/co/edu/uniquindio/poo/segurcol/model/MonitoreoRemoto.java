package co.edu.uniquindio.poo.segurcol.model;
public class MonitoreoRemoto extends Servicio {
    private int numeroDispositivos;

    public MonitoreoRemoto(String codigoContrato, String cliente, double tarifaMensual, String estado,
                           int numeroDispositivos) {
        super(codigoContrato, cliente, tarifaMensual, estado);
        this.numeroDispositivos = numeroDispositivos;
    }

    @Override
    public double calcularCostoMensual() {
        // Costo base + costo por dispositivos
        double costoDispositivos = numeroDispositivos * 50000;
        return getTarifaMensual() + costoDispositivos;
    }

    // Getters y Setters
    public int getNumeroDispositivos() { return numeroDispositivos; }
    public void setNumeroDispositivos(int numeroDispositivos) {
        this.numeroDispositivos = numeroDispositivos;
    }
}