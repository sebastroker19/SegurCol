package co.edu.uniquindio.poo.segurcol.controller;

import co.edu.uniquindio.poo.segurcol.model.*;
import java.util.List;
import java.util.ArrayList;

public class EmpleadoController {
    private List<Empleado> empleados;

    public EmpleadoController() {
        this.empleados = new ArrayList<>();
    }

    public boolean registrarEmpleado(Empleado empleado) {
        if (buscarEmpleadoPorDocumento(empleado.getDocumento()) == null) {
            empleados.add(empleado);
            return true;
        }
        return false;
    }

    public Empleado buscarEmpleadoPorDocumento(String documento) {
        for (Empleado empleado : empleados) {
            if (empleado.getDocumento().equals(documento)) {
                return empleado;
            }
        }
        return null;
    }

    public List<Empleado> obtenerTodosEmpleados() {
        return new ArrayList<>(empleados);
    }

    public List<Vigilante> obtenerVigilantes() {
        List<Vigilante> vigilantes = new ArrayList<>();
        for (Empleado empleado : empleados) {
            if (empleado instanceof Vigilante) {
                vigilantes.add((Vigilante) empleado);
            }
        }
        return vigilantes;
    }

    public List<Supervisor> obtenerSupervisores() {
        List<Supervisor> supervisores = new ArrayList<>();
        for (Empleado empleado : empleados) {
            if (empleado instanceof Supervisor) {
                supervisores.add((Supervisor) empleado);
            }
        }
        return supervisores;
    }

    public List<OperadorMonitoreo> obtenerOperadores() {
        List<OperadorMonitoreo> operadores = new ArrayList<>();
        for (Empleado empleado : empleados) {
            if (empleado instanceof OperadorMonitoreo) {
                operadores.add((OperadorMonitoreo) empleado);
            }
        }
        return operadores;
    }

    public double calcularTotalNomina() {
        double total = 0;
        for (Empleado empleado : empleados) {
            total += empleado.calcularSalarioTotal();
        }
        return total;
    }
}