package co.edu.uniquindio.poo.segurcol;

import co.edu.uniquindio.poo.segurcol.controller.SistemaController;
import co.edu.uniquindio.poo.segurcol.model.*;
import co.edu.uniquindio.poo.segurcol.viewcontroller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private Stage primaryStage;
    public static SistemaController sistemaController = new SistemaController();

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SEGURCOL S.A.S - Sistema Integral de Seguridad");
        inicializarDatosPrueba();
        abrirVistaPrincipal();
    }

    // CORREGIDO: Método público para volver al menú principal
    public void abrirVistaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("primary.fxml"));
            VBox rootLayout = loader.load();

            PrimaryViewController primaryController = loader.getController();
            primaryController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("=== MENÚ PRINCIPAL ABIERTO ===");
        } catch (IOException e) {
            System.err.println("Error al abrir vista principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // === MÉTODOS DE NAVEGACIÓN ===

    public void openEmpleadoView() {
        try {
            System.out.println("Abriendo Gestión de Empleados...");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("empleado.fxml"));
            AnchorPane rootLayout = loader.load();

            EmpleadoViewController empleadoController = loader.getController();
            empleadoController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("Gestión de Empleados abierta correctamente");
        } catch (IOException e) {
            System.err.println("Error al abrir gestión de empleados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void openServicioView() {
        try {
            System.out.println("Abriendo Gestión de Servicios...");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("servicio.fxml"));
            AnchorPane rootLayout = loader.load();

            ServicioViewController servicioController = loader.getController();
            servicioController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("Gestión de Servicios abierta correctamente");
        } catch (IOException e) {
            System.err.println("Error al abrir gestión de servicios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void openEquipoView() {
        try {
            System.out.println("Abriendo Gestión de Equipos...");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("equipo.fxml"));
            AnchorPane rootLayout = loader.load();

            EquipoViewController equipoController = loader.getController();
            equipoController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("Gestión de Equipos abierta correctamente");
        } catch (IOException e) {
            System.err.println("Error al abrir gestión de equipos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void openNovedadView() {
        try {
            System.out.println("Abriendo Registro de Novedades...");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("novedad.fxml"));
            AnchorPane rootLayout = loader.load();

            NovedadViewController novedadController = loader.getController();
            novedadController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("Registro de Novedades abierto correctamente");
        } catch (IOException e) {
            System.err.println("Error al abrir registro de novedades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void openAgendaView() {
        try {
            System.out.println("Abriendo Agenda y Programación...");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("agenda.fxml"));
            AnchorPane rootLayout = loader.load();

            AgendaViewController agendaController = loader.getController();
            agendaController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("Agenda y Programación abierta correctamente");
        } catch (IOException e) {
            System.err.println("Error al abrir agenda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void inicializarDatosPrueba() {
        System.out.println("=== INICIALIZANDO DATOS DE PRUEBA ===");

        // Crear empleados de prueba
        Vigilante vigilante1 = new Vigilante("Carlos López", "12345678", "día", 1500000, "Puesto-01", "no letal");
        Vigilante vigilante2 = new Vigilante("Ana Martínez", "87654321", "noche", 1600000, "Puesto-02", "letal");
        Supervisor supervisor1 = new Supervisor("María García", "11223344", "día", 2500000, 300000);
        OperadorMonitoreo operador1 = new OperadorMonitoreo("Pedro Rodríguez", "44332211", "noche", 1800000, 5);

        sistemaController.registrarEmpleado(vigilante1);
        sistemaController.registrarEmpleado(vigilante2);
        sistemaController.registrarEmpleado(supervisor1);
        sistemaController.registrarEmpleado(operador1);

        System.out.println("Empleados de prueba creados: 4");

        // Crear servicios de prueba
        CustodiaFija custodia1 = new CustodiaFija("CONT-001", "Empresa ABC", 5000000, "activo");
        PatrullajeMovil patrullaje1 = new PatrullajeMovil("CONT-002", "Conjunto Residencial XYZ", 3000000, "activo", 3, 150.5);
        MonitoreoRemoto monitoreo1 = new MonitoreoRemoto("CONT-003", "Centro Comercial Mall", 4000000, "activo", 25);

        sistemaController.registrarServicio(custodia1);
        sistemaController.registrarServicio(patrullaje1);
        sistemaController.registrarServicio(monitoreo1);

        System.out.println("Servicios de prueba creados: 3");

        // Asignar vigilantes a custodia
        sistemaController.asignarEmpleadoAServicio("CONT-001", vigilante1);
        sistemaController.asignarEmpleadoAServicio("CONT-001", vigilante2);

        // Crear equipos de prueba
        Equipo radio1 = new Equipo("EQ-001", "radio", "activo", 250000);
        Equipo arma1 = new Equipo("EQ-002", "arma", "activo", 800000);
        Equipo vehiculo1 = new Equipo("EQ-003", "vehículo", "activo", 25000000);
        Equipo uniforme1 = new Equipo("EQ-004", "uniforme", "activo", 150000);

        sistemaController.registrarEquipo(radio1);
        sistemaController.registrarEquipo(arma1);
        sistemaController.registrarEquipo(vehiculo1);
        sistemaController.registrarEquipo(uniforme1);

        System.out.println("Equipos de prueba creados: 4");

        // Asignar equipos a empleados
        sistemaController.asignarEquipoAEmpleado(vigilante1, radio1);
        sistemaController.asignarEquipoAEmpleado(vigilante1, arma1);
        sistemaController.asignarEquipoAEmpleado(vigilante1, uniforme1);
        sistemaController.asignarEquipoAEmpleado(supervisor1, vehiculo1);

        // Registrar algunas novedades de prueba
        sistemaController.registrarNovedadSimple(
                java.time.LocalDateTime.now().minusDays(2),
                "incidente",
                "Intento de ingreso no autorizado en puesto 01",
                vigilante1,
                custodia1
        );

        sistemaController.registrarNovedadSimple(
                java.time.LocalDateTime.now().minusDays(1),
                "asignacion",
                "Nueva asignación de equipo de radio",
                vigilante2,
                null
        );

        System.out.println("Novedades de prueba creadas: 2");

        // Programar eventos en agenda
        sistemaController.programarEvento(
                java.time.LocalDate.now().plusDays(1),
                "Ronda de verificación nocturna",
                vigilante2,
                patrullaje1,
                "ronda"
        );

        sistemaController.programarEvento(
                java.time.LocalDate.now().plusDays(2),
                "Cambio de turno supervisor",
                supervisor1,
                null,
                "turno"
        );

        System.out.println("Eventos de agenda creados: 2");
        System.out.println("=== DATOS DE PRUEBA INICIALIZADOS CORRECTAMENTE ===");
    }

    public static void main(String[] args) {
        System.out.println("=== INICIANDO APLICACIÓN SEGURCOL ===");
        launch();
    }

    // Método para obtener el controlador del sistema
    public SistemaController getSistemaController() {
        return sistemaController;
    }

    // Método para obtener la instancia principal del Stage
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}