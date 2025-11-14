package co.edu.uniquindio.poo.segurcol.viewcontroller;

import co.edu.uniquindio.poo.segurcol.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class PrimaryViewController {

    @FXML private Button btnEmpleados;
    @FXML private Button btnServicios;
    @FXML private Button btnEquipos;
    @FXML private Button btnNovedades;
    @FXML private Button btnAgenda;

    private App app;

    public PrimaryViewController() {
        // Constructor vacío requerido por FXML
    }

    @FXML
    void initialize() {
        System.out.println("=== INICIALIZANDO MENÚ PRINCIPAL ===");
        System.out.println("Todos los botones cargados correctamente");
    }

    @FXML
    void onEmpleados() {
        System.out.println("Abriendo gestión de empleados...");
        try {
            app.openEmpleadoView();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la gestión de empleados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onServicios() {
        System.out.println("Abriendo gestión de servicios...");
        try {
            app.openServicioView();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la gestión de servicios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onEquipos() {
        System.out.println("Abriendo gestión de equipos...");
        try {
            app.openEquipoView();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la gestión de equipos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onNovedades() {
        System.out.println("Abriendo gestión de novedades...");
        try {
            app.openNovedadView();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la gestión de novedades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onAgenda() {
        System.out.println("Abriendo gestión de agenda...");
        try {
            app.openAgendaView();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la gestión de agenda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setApp(App app) {
        this.app = app;
        System.out.println("App configurada en PrimaryViewController");
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}