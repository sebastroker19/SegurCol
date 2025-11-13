package co.edu.uniquindio.poo.segurcol.controller;

import co.edu.uniquindio.poo.segurcol.model.RegistroNovedad;
import co.edu.uniquindio.poo.segurcol.model.Empleado;
import co.edu.uniquindio.poo.segurcol.model.Servicio;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class NovedadController {
    private List<RegistroNovedad> novedades;

    public NovedadController() {
        this.novedades = new ArrayList<>();
    }

    public void registrarNovedad(RegistroNovedad novedad) {
        novedades.add(novedad);
    }

    public List<RegistroNovedad> obtenerNovedades(LocalDateTime desde, LocalDateTime hasta) {
        List<RegistroNovedad> resultado = new ArrayList<>();
        for (RegistroNovedad novedad : novedades) {
            if (!novedad.getFechaHora().isBefore(desde) && !novedad.getFechaHora().isAfter(hasta)) {
                resultado.add(novedad);
            }
        }
        return resultado;
    }

    public List<RegistroNovedad> obtenerTodasNovedades() {
        return new ArrayList<>(novedades);
    }

    public List<RegistroNovedad> obtenerNovedadesPorTipo(String tipo) {
        List<RegistroNovedad> resultado = new ArrayList<>();
        for (RegistroNovedad novedad : novedades) {
            if (novedad.getTipo().equals(tipo)) {
                resultado.add(novedad);
            }
        }
        return resultado;
    }

    public List<RegistroNovedad> obtenerNovedadesPorEmpleado(Empleado empleado) {
        List<RegistroNovedad> resultado = new ArrayList<>();
        for (RegistroNovedad novedad : novedades) {
            if (novedad.getEmpleado() != null && novedad.getEmpleado().equals(empleado)) {
                resultado.add(novedad);
            }
        }
        return resultado;
    }

    public List<RegistroNovedad> obtenerNovedadesPorServicio(Servicio servicio) {
        List<RegistroNovedad> resultado = new ArrayList<>();
        for (RegistroNovedad novedad : novedades) {
            if (novedad.getServicio() != null && novedad.getServicio().equals(servicio)) {
                resultado.add(novedad);
            }
        }
        return resultado;
    }
}