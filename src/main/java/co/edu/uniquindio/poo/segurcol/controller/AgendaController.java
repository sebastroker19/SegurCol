package co.edu.uniquindio.poo.segurcol.controller;

import co.edu.uniquindio.poo.segurcol.model.AgendaItem;
import co.edu.uniquindio.poo.segurcol.model.Empleado;
import co.edu.uniquindio.poo.segurcol.model.Servicio;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class AgendaController {
    private List<AgendaItem> agenda;

    public AgendaController() {
        this.agenda = new ArrayList<>();
    }

    public void programar(LocalDate fecha, String descripcion, Empleado empleado,
                          Servicio servicio, String tipoEvento) {
        AgendaItem item = new AgendaItem(fecha, descripcion, empleado, servicio, tipoEvento);
        agenda.add(item);
    }

    public List<AgendaItem> obtenerAgenda(LocalDate desde, LocalDate hasta) {
        List<AgendaItem> resultado = new ArrayList<>();
        for (AgendaItem item : agenda) {
            if (!item.getFecha().isBefore(desde) && !item.getFecha().isAfter(hasta)) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    public List<AgendaItem> obtenerTodaAgenda() {
        return new ArrayList<>(agenda);
    }

    public List<AgendaItem> obtenerAgendaPorEmpleado(Empleado empleado) {
        List<AgendaItem> resultado = new ArrayList<>();
        for (AgendaItem item : agenda) {
            if (item.getEmpleado() != null && item.getEmpleado().equals(empleado)) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    public List<AgendaItem> obtenerAgendaPorServicio(Servicio servicio) {
        List<AgendaItem> resultado = new ArrayList<>();
        for (AgendaItem item : agenda) {
            if (item.getServicio() != null && item.getServicio().equals(servicio)) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    public List<AgendaItem> obtenerAgendaPorTipoEvento(String tipoEvento) {
        List<AgendaItem> resultado = new ArrayList<>();
        for (AgendaItem item : agenda) {
            if (item.getTipoEvento().equals(tipoEvento)) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    public boolean eliminarEventoAgenda(AgendaItem evento) {
        return agenda.remove(evento);
    }
}