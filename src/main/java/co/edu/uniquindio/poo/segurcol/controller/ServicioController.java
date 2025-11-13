package co.edu.uniquindio.poo.segurcol.controller;

import co.edu.uniquindio.poo.segurcol.model.*;
import java.util.List;
import java.util.ArrayList;

public class ServicioController {
    private List<Servicio> servicios;

    public ServicioController() {
        this.servicios = new ArrayList<>();
    }

    public boolean registrarServicio(Servicio servicio) {
        if (buscarServicioPorContrato(servicio.getCodigoContrato()) == null) {
            servicios.add(servicio);
            return true;
        }
        return false;
    }

    public Servicio buscarServicioPorContrato(String codigoContrato) {
        for (Servicio servicio : servicios) {
            if (servicio.getCodigoContrato().equals(codigoContrato)) {
                return servicio;
            }
        }
        return null;
    }

    public List<Servicio> obtenerTodosServicios() {
        return new ArrayList<>(servicios);
    }

    public List<CustodiaFija> obtenerCustodiasFijas() {
        List<CustodiaFija> custodias = new ArrayList<>();
        for (Servicio servicio : servicios) {
            if (servicio instanceof CustodiaFija) {
                custodias.add((CustodiaFija) servicio);
            }
        }
        return custodias;
    }

    public List<PatrullajeMovil> obtenerPatrullajesMoviles() {
        List<PatrullajeMovil> patrullajes = new ArrayList<>();
        for (Servicio servicio : servicios) {
            if (servicio instanceof PatrullajeMovil) {
                patrullajes.add((PatrullajeMovil) servicio);
            }
        }
        return patrullajes;
    }

    public List<MonitoreoRemoto> obtenerMonitoreosRemotos() {
        List<MonitoreoRemoto> monitoreos = new ArrayList<>();
        for (Servicio servicio : servicios) {
            if (servicio instanceof MonitoreoRemoto) {
                monitoreos.add((MonitoreoRemoto) servicio);
            }
        }
        return monitoreos;
    }

    public double calcularCostoTotalMensual() {
        double total = 0;
        for (Servicio servicio : servicios) {
            if ("activo".equals(servicio.getEstado())) {
                total += servicio.calcularCostoMensual();
            }
        }
        return total;
    }

    public boolean asignarEmpleadoAServicio(String codigoContrato, Empleado empleado) {
        Servicio servicio = buscarServicioPorContrato(codigoContrato);
        if (servicio != null && servicio instanceof CustodiaFija && empleado instanceof Vigilante) {
            CustodiaFija custodia = (CustodiaFija) servicio;
            custodia.agregarVigilante((Vigilante) empleado);
            return true;
        }
        return false;
    }
}