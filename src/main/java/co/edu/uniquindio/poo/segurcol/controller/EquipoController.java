package co.edu.uniquindio.poo.segurcol.controller;

import co.edu.uniquindio.poo.segurcol.model.Equipo;
import co.edu.uniquindio.poo.segurcol.model.Empleado;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EquipoController {
    private List<Equipo> equipos;
    private Map<Empleado, List<Equipo>> asignaciones;

    public EquipoController() {
        this.equipos = new ArrayList<>();
        this.asignaciones = new HashMap<>();
    }

    public boolean registrarEquipo(Equipo equipo) {
        if (buscarEquipoPorCodigo(equipo.getCodigo()) == null) {
            equipos.add(equipo);
            return true;
        }
        return false;
    }

    public Equipo buscarEquipoPorCodigo(String codigo) {
        for (Equipo equipo : equipos) {
            if (equipo.getCodigo().equals(codigo)) {
                return equipo;
            }
        }
        return null;
    }

    public List<Equipo> obtenerTodosEquipos() {
        return new ArrayList<>(equipos);
    }

    public boolean asignarEquipoAEmpleado(Empleado empleado, Equipo equipo) {
        if ("activo".equals(equipo.getEstado())) {
            if (!asignaciones.containsKey(empleado)) {
                asignaciones.put(empleado, new ArrayList<>());
            }
            asignaciones.get(empleado).add(equipo);
            return true;
        }
        return false;
    }

    public boolean retirarEquipoDeEmpleado(Empleado empleado, Equipo equipo) {
        if (asignaciones.containsKey(empleado)) {
            return asignaciones.get(empleado).remove(equipo);
        }
        return false;
    }

    public List<Equipo> obtenerEquiposPorEmpleado(Empleado empleado) {
        return asignaciones.getOrDefault(empleado, new ArrayList<>());
    }

    public double calcularValorTotalEquipos() {
        double total = 0;
        for (Equipo equipo : equipos) {
            if ("activo".equals(equipo.getEstado())) {
                total += equipo.getValorReposicion();
            }
        }
        return total;
    }

    public double calcularValorEquiposPorEmpleado(Empleado empleado) {
        double total = 0;
        List<Equipo> equiposEmpleado = obtenerEquiposPorEmpleado(empleado);
        for (Equipo equipo : equiposEmpleado) {
            total += equipo.getValorReposicion();
        }
        return total;
    }
}