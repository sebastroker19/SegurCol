package co.edu.uniquindio.poo.segurcol.viewcontroller;

import co.edu.uniquindio.poo.segurcol.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class PrimaryViewController {

    @FXML
    private Button btnEmpleados;

    @FXML
    private Button btnServicios;

    @FXML
    private Button btnEquipos;

    @FXML
    private Button btnNovedades;

    @FXML
    private Button btnAgenda;

    private App app;

    @FXML
    void onEmpleados() {
        try {
            app.openEmpleadoView();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la gestión de empleados: " + e.getMessage());
        }
    }

    @FXML
    void onServicios() {
        try {
            app.openServicioView();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la gestión de servicios: " + e.getMessage());
        }
    }

    @FXML
    void onEquipos() {
        try {
            app.openEquipoView();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la gestión de equipos: " + e.getMessage());
        }
    }

    @FXML
    void onNovedades() {
        try {
            app.openNovedadView();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la gestión de novedades: " + e.getMessage());
        }
    }

    @FXML
    void onAgenda() {
        try {
            app.openAgendaView();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la gestión de agenda: " + e.getMessage());
        }
    }

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    void initialize() {
        // Inicialización de componentes si es necesario
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}