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
        this.primaryStage.setTitle("SEGURCOL S.A. - Sistema de Gestión de Seguridad");
        inicializarDatosPrueba();
        abrirVistaPrincipal();
    }

    // CORREGIDO: Cambiado de private a public
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // === MÉTODOS CORREGIDOS - NOMBRES ACTUALIZADOS ===

    public void openEmpleadoView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("empleado.fxml"));
            AnchorPane rootLayout = loader.load();

            EmpleadoViewController empleadoController = loader.getController();
            empleadoController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openServicioView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("servicio.fxml"));
            AnchorPane rootLayout = loader.load();

            ServicioViewController servicioController = loader.getController();
            servicioController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openEquipoView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("equipo.fxml"));
            AnchorPane rootLayout = loader.load();

            EquipoViewController equipoController = loader.getController();
            equipoController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openNovedadView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("novedad.fxml"));
            AnchorPane rootLayout = loader.load();

            NovedadViewController novedadController = loader.getController();
            novedadController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openAgendaView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("agenda.fxml"));
            AnchorPane rootLayout = loader.load();

            AgendaViewController agendaController = loader.getController();
            agendaController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inicializarDatosPrueba() {
        // (Mantener el mismo código de inicialización de datos)
        // Crear empleados de prueba
        Vigilante vigilante1 = new Vigilante("Carlos López", "12345678", "día", 1500000, "Puesto-01", "no letal");
        Vigilante vigilante2 = new Vigilante("Ana Martínez", "87654321", "noche", 1600000, "Puesto-02", "letal");
        Supervisor supervisor1 = new Supervisor("María García", "11223344", "día", 2500000, 300000);
        OperadorMonitoreo operador1 = new OperadorMonitoreo("Pedro Rodríguez", "44332211", "noche", 1800000, 5);

        sistemaController.registrarEmpleado(vigilante1);
        sistemaController.registrarEmpleado(vigilante2);
        sistemaController.registrarEmpleado(supervisor1);
        sistemaController.registrarEmpleado(operador1);

        // Crear servicios de prueba
        CustodiaFija custodia1 = new CustodiaFija("CONT-001", "Empresa ABC", 5000000, "activo");
        PatrullajeMovil patrullaje1 = new PatrullajeMovil("CONT-002", "Conjunto Residencial XYZ", 3000000, "activo", 3, 150.5);
        MonitoreoRemoto monitoreo1 = new MonitoreoRemoto("CONT-003", "Centro Comercial Mall", 4000000, "activo", 25);

        sistemaController.registrarServicio(custodia1);
        sistemaController.registrarServicio(patrullaje1);
        sistemaController.registrarServicio(monitoreo1);

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
    }

    public static void main(String[] args) {
        launch();
    }

    // Método para obtener el controlador del sistema (útil para los viewcontrollers)
    public SistemaController getSistemaController() {
        return sistemaController;
    }

    // Método adicional para obtener la instancia principal (opcional, pero útil)
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}