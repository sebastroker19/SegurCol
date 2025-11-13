package co.edu.uniquindio.poo.segurcol.controller;

import co.edu.uniquindio.poo.segurcol.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SistemaController {
    private EmpleadoController empleadoController;
    private ServicioController servicioController;
    private EquipoController equipoController;
    private NovedadController novedadController;
    private AgendaController agendaController;

    public SistemaController() {
        this.empleadoController = new EmpleadoController();
        this.servicioController = new ServicioController();
        this.equipoController = new EquipoController();
        this.novedadController = new NovedadController();
        this.agendaController = new AgendaController();
    }

    // === MÉTODOS DE COORDINACIÓN GENERAL ===

    public double calcularGastoTotalEmpresa() {
        double totalNomina = empleadoController.calcularTotalNomina();
        double totalEquipos = equipoController.calcularValorTotalEquipos();
        return totalNomina + totalEquipos;
    }

    public String generarInformeGeneral() {
        StringBuilder informe = new StringBuilder();
        informe.append("=== INFORME GENERAL SEGURCOL S.A. ===\n");
        informe.append("Total Empleados: ").append(empleadoController.obtenerTodosEmpleados().size()).append("\n");
        informe.append("Total Servicios Activos: ").append(servicioController.obtenerTodosServicios().size()).append("\n");
        informe.append("Total Equipos: ").append(equipoController.obtenerTodosEquipos().size()).append("\n");
        informe.append("Total Nómina: $").append(empleadoController.calcularTotalNomina()).append("\n");
        informe.append("Total Costo Servicios: $").append(servicioController.calcularCostoTotalMensual()).append("\n");
        informe.append("Gasto Total Empresa: $").append(calcularGastoTotalEmpresa()).append("\n");
        return informe.toString();
    }

    // === MÉTODOS DE EMPLEADO ===
    public boolean registrarEmpleado(Empleado empleado) {
        return empleadoController.registrarEmpleado(empleado);
    }

    public List<Empleado> obtenerTodosEmpleados() {
        return empleadoController.obtenerTodosEmpleados();
    }

    // === MÉTODOS DE SERVICIO ===
    public boolean registrarServicio(Servicio servicio) {
        return servicioController.registrarServicio(servicio);
    }

    public boolean asignarEmpleadoAServicio(String codigoContrato, Empleado empleado) {
        return servicioController.asignarEmpleadoAServicio(codigoContrato, empleado);
    }

    // === MÉTODOS DE EQUIPO ===
    public boolean registrarEquipo(Equipo equipo) {
        return equipoController.registrarEquipo(equipo);
    }

    public boolean asignarEquipoAEmpleado(Empleado empleado, Equipo equipo) {
        return equipoController.asignarEquipoAEmpleado(empleado, equipo);
    }

    public String generarInformeDotacionEmpleado(Empleado empleado) {
        StringBuilder informe = new StringBuilder();
        informe.append("=== INFORME DOTACIÓN ===\n");
        informe.append("Empleado: ").append(empleado.getNombre()).append("\n");
        informe.append("Documento: ").append(empleado.getDocumento()).append("\n");

        List<Equipo> equipos = equipoController.obtenerEquiposPorEmpleado(empleado);
        informe.append("Equipos Asignados:\n");
        for (Equipo equipo : equipos) {
            informe.append("- ").append(equipo.getTipo())
                    .append(" (").append(equipo.getCodigo()).append("): $")
                    .append(equipo.getValorReposicion()).append("\n");
        }
        informe.append("Valor Total Dotación: $").append(equipoController.calcularValorEquiposPorEmpleado(empleado));
        return informe.toString();
    }

    // === MÉTODOS DE NOVEDAD ===
    public void registrarNovedad(RegistroNovedad novedad) {
        novedadController.registrarNovedad(novedad);
    }

    public void registrarNovedadSimple(LocalDateTime fechaHora, String tipo, String descripcion,
                                       Empleado empleado, Servicio servicio) {
        RegistroNovedad novedad = new RegistroNovedad(fechaHora, tipo, descripcion, empleado, servicio);
        registrarNovedad(novedad);
    }

    // === MÉTODOS DE AGENDA ===
    public void programarEvento(LocalDate fecha, String descripcion, Empleado empleado,
                                Servicio servicio, String tipoEvento) {
        agendaController.programar(fecha, descripcion, empleado, servicio, tipoEvento);
    }

    public List<AgendaItem> obtenerAgendaRango(LocalDate desde, LocalDate hasta) {
        return agendaController.obtenerAgenda(desde, hasta);
    }

    // === GETTERS PARA LOS CONTROLADORES ESPECÍFICOS ===
    public EmpleadoController getEmpleadoController() {
        return empleadoController;
    }

    public ServicioController getServicioController() {
        return servicioController;
    }

    public EquipoController getEquipoController() {
        return equipoController;
    }

    public NovedadController getNovedadController() {
        return novedadController;
    }

    public AgendaController getAgendaController() {
        return agendaController;
    }
}