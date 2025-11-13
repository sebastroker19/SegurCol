package co.edu.uniquindio.poo.segurcol.viewcontroller;

import co.edu.uniquindio.poo.segurcol.App;
import co.edu.uniquindio.poo.segurcol.controller.SistemaController;
import co.edu.uniquindio.poo.segurcol.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDateTime;

public class NovedadViewController {

    private SistemaController sistemaController;
    private ObservableList<RegistroNovedad> listaNovedades;

    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<String> cbTipoNovedad;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private ComboBox<Empleado> cbEmpleado;
    @FXML
    private ComboBox<Servicio> cbServicio;
    @FXML
    private TableView<RegistroNovedad> tblNovedades;
    @FXML
    private TableColumn<RegistroNovedad, String> colFecha;
    @FXML
    private TableColumn<RegistroNovedad, String> colTipo;
    @FXML
    private TableColumn<RegistroNovedad, String> colDescripcion;
    @FXML
    private TableColumn<RegistroNovedad, String> colEmpleado;
    @FXML
    private TableColumn<RegistroNovedad, String> colServicio;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnVolverMenu; // BOTÓN VOLVER AGREGADO

    private App app;

    @FXML
    void onRegistrarNovedad() {
        registrarNovedad();
    }

    @FXML
    void onLimpiar() {
        limpiarCampos();
    }

    @FXML
    void onFiltrar() {
        // Implementar filtrado por fecha
    }

    @FXML
    void onVolverMenu() { // MÉTODO VOLVER AGREGADO
        volverAlMenuPrincipal();
    }

    public void setApp(App app) {
        this.app = app;
        this.sistemaController = app.getSistemaController();
        this.listaNovedades = FXCollections.observableArrayList();
        cargarNovedades();
    }

    @FXML
    void initialize() {
        configurarComboBox();
        configurarTabla();
    }

    private void configurarComboBox() {
        cbTipoNovedad.getItems().addAll("incidente", "asignacion", "alerta", "devolucion");

        if (sistemaController != null) {
            cbEmpleado.getItems().addAll(sistemaController.obtenerTodosEmpleados());
            cbServicio.getItems().addAll(sistemaController.getServicioController().obtenerTodosServicios());
        }
    }

    private void configurarTabla() {
        colFecha.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaHora().toString()));
        colTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipo()));
        colDescripcion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescripcion()));
        colEmpleado.setCellValueFactory(cellData -> {
            Empleado emp = cellData.getValue().getEmpleado();
            return new SimpleStringProperty(emp != null ? emp.getNombre() : "N/A");
        });
        colServicio.setCellValueFactory(cellData -> {
            Servicio serv = cellData.getValue().getServicio();
            return new SimpleStringProperty(serv != null ? serv.getCliente() : "N/A");
        });

        tblNovedades.setItems(listaNovedades);
    }

    private void cargarNovedades() {
        if (listaNovedades != null && sistemaController != null) {
            listaNovedades.clear();
            listaNovedades.addAll(sistemaController.getNovedadController().obtenerTodasNovedades());
        }
    }

    private void registrarNovedad() {
        try {
            if (dpFecha.getValue() != null) {
                LocalDateTime fechaHora = dpFecha.getValue().atStartOfDay();
                String tipo = cbTipoNovedad.getValue();
                String descripcion = txtDescripcion.getText();
                Empleado empleado = cbEmpleado.getValue();
                Servicio servicio = cbServicio.getValue();

                sistemaController.registrarNovedadSimple(fechaHora, tipo, descripcion, empleado, servicio);
                cargarNovedades();
                limpiarCampos();
                mostrarAlerta("Éxito", "Novedad registrada correctamente", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al registrar novedad: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        dpFecha.setValue(null);
        cbTipoNovedad.setValue(null);
        txtDescripcion.clear();
        cbEmpleado.setValue(null);
        cbServicio.setValue(null);
    }

    private void volverAlMenuPrincipal() { // MÉTODO VOLVER AGREGADO
        try {
            app.abrirVistaPrincipal();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo volver al menú principal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}