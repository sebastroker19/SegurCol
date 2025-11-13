package co.edu.uniquindio.poo.segurcol.viewcontroller;

import co.edu.uniquindio.poo.segurcol.App;
import co.edu.uniquindio.poo.segurcol.controller.SistemaController;
import co.edu.uniquindio.poo.segurcol.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class AgendaViewController {

    private SistemaController sistemaController;
    private ObservableList<AgendaItem> listaAgenda;

    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private ComboBox<Empleado> cbEmpleado;
    @FXML
    private ComboBox<Servicio> cbServicio;
    @FXML
    private ComboBox<String> cbTipoEvento;
    @FXML
    private TableView<AgendaItem> tblAgenda;
    @FXML
    private TableColumn<AgendaItem, String> colFecha;
    @FXML
    private TableColumn<AgendaItem, String> colDescripcion;
    @FXML
    private TableColumn<AgendaItem, String> colEmpleado;
    @FXML
    private TableColumn<AgendaItem, String> colServicio;
    @FXML
    private TableColumn<AgendaItem, String> colTipoEvento;
    @FXML
    private Button btnProgramar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnVolverMenu; // BOTÓN VOLVER AGREGADO

    private App app;

    @FXML
    void onProgramarEvento() {
        programarEvento();
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
        this.listaAgenda = FXCollections.observableArrayList();
        cargarAgenda();
    }

    @FXML
    void initialize() {
        configurarComboBox();
        configurarTabla();
    }

    private void configurarComboBox() {
        cbTipoEvento.getItems().addAll("turno", "ronda", "chequeo", "custodia");

        if (sistemaController != null) {
            cbEmpleado.getItems().addAll(sistemaController.obtenerTodosEmpleados());
            cbServicio.getItems().addAll(sistemaController.getServicioController().obtenerTodosServicios());
        }
    }

    private void configurarTabla() {
        colFecha.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFecha().toString()));
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
        colTipoEvento.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipoEvento()));

        tblAgenda.setItems(listaAgenda);
    }

    private void cargarAgenda() {
        if (listaAgenda != null && sistemaController != null) {
            listaAgenda.clear();
            listaAgenda.addAll(sistemaController.getAgendaController().obtenerTodaAgenda());
        }
    }

    private void programarEvento() {
        try {
            if (dpFecha.getValue() != null) {
                LocalDate fecha = dpFecha.getValue();
                String descripcion = txtDescripcion.getText();
                Empleado empleado = cbEmpleado.getValue();
                Servicio servicio = cbServicio.getValue();
                String tipoEvento = cbTipoEvento.getValue();

                sistemaController.programarEvento(fecha, descripcion, empleado, servicio, tipoEvento);
                cargarAgenda();
                limpiarCampos();
                mostrarAlerta("Éxito", "Evento programado correctamente", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al programar evento: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        dpFecha.setValue(null);
        txtDescripcion.clear();
        cbEmpleado.setValue(null);
        cbServicio.setValue(null);
        cbTipoEvento.setValue(null);
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