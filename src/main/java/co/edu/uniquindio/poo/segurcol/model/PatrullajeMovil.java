package co.edu.uniquindio.poo.segurcol.model;
public class PatrullajeMovil extends Servicio {
    private int cantidadRutas;
    private double kilometrosRecorridos;

    public PatrullajeMovil(String codigoContrato, String cliente, double tarifaMensual, String estado,
                           int cantidadRutas, double kilometrosRecorridos) {
        super(codigoContrato, cliente, tarifaMensual, estado);
        this.cantidadRutas = cantidadRutas;
        this.kilometrosRecorridos = kilometrosRecorridos;
    }

    @Override
    public double calcularCostoMensual() {
        // Costo base + costo por kilómetros
        double costoKilometros = kilometrosRecorridos * 1500;
        return getTarifaMensual() + costoKilometros;
    }

    // Getters y Setters
    public int getCantidadRutas() { return cantidadRutas; }
    public void setCantidadRutas(int cantidadRutas) { this.cantidadRutas = cantidadRutas; }

    public double getKilometrosRecorridos() { return kilometrosRecorridos; }
    public void setKilometrosRecorridos(double kilometrosRecorridos) {
        this.kilometrosRecorridos = kilometrosRecorridos;
    }
}