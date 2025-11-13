package co.edu.uniquindio.poo.segurcol.model;
import java.util.List;
import java.util.ArrayList;

public class CustodiaFija extends Servicio {
    private List<Vigilante> vigilantesAsignados;

    public CustodiaFija(String codigoContrato, String cliente, double tarifaMensual, String estado) {
        super(codigoContrato, cliente, tarifaMensual, estado);
        this.vigilantesAsignados = new ArrayList<>();
    }

    @Override
    public double calcularCostoMensual() {
        // Costo base + costo adicional por vigilantes
        double costoVigilantes = vigilantesAsignados.size() * 200000;
        return getTarifaMensual() + costoVigilantes;
    }

    public void agregarVigilante(Vigilante vigilante) {
        vigilantesAsignados.add(vigilante);
    }

    // Getters y Setters
    public List<Vigilante> getVigilantesAsignados() { return vigilantesAsignados; }
    public void setVigilantesAsignados(List<Vigilante> vigilantesAsignados) {
        this.vigilantesAsignados = vigilantesAsignados;
    }
}