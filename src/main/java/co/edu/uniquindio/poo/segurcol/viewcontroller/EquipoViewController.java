package co.edu.uniquindio.poo.segurcol.viewcontroller;

import co.edu.uniquindio.poo.segurcol.App;
import co.edu.uniquindio.poo.segurcol.controller.SistemaController;
import co.edu.uniquindio.poo.segurcol.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EquipoViewController {

    private SistemaController sistemaController;
    private ObservableList<Equipo> listaEquipos;
    private ObservableList<Empleado> listaEmpleados;
    private Equipo equipoSeleccionado;

    @FXML
    private TextField txtCodigo;
    @FXML
    private ComboBox<String> cbTipo;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private TextField txtValorReposicion;
    @FXML
    private ComboBox<Empleado> cbEmpleadoAsignar;
    @FXML
    private TableView<Equipo> tblEquipos;
    @FXML
    private TableColumn<Equipo, String> colCodigo;
    @FXML
    private TableColumn<Equipo, String> colTipo;
    @FXML
    private TableColumn<Equipo, String> colEstado;
    @FXML
    private TableColumn<Equipo, String> colValor;
    @FXML
    private TableColumn<Equipo, String> colAsignadoA;

    @FXML
    @SuppressWarnings("unused")
    private Button btnAgregar;

    @FXML
    @SuppressWarnings("unused")
    private Button btnAsignar;

    @FXML
    @SuppressWarnings("unused")
    private Button btnRetirar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnGenerarInforme;
    @FXML
    private Button btnVolverMenu;

    private App app;

    @FXML
    void onAgregarEquipo() {
        agregarEquipo();
    }

    @FXML
    void onAsignarEquipo() {
        asignarEquipo();
    }

    @FXML
    void onRetirarEquipo() {
        retirarEquipo();
    }

    @FXML
    void onLimpiar() {
        limpiarCampos();
    }

    @FXML
    void onGenerarInforme() {
        generarInformeDotacion();
    }

    @FXML
    void onVolverMenu() { // MÉTODO VOLVER AGREGADO
        volverAlMenuPrincipal();
    }

    public void setApp(App app) {
        this.app = app;
        this.sistemaController = app.getSistemaController();
        this.listaEquipos = FXCollections.observableArrayList();
        this.listaEmpleados = FXCollections.observableArrayList();
        cargarDatos();
    }

    @FXML
    void initialize() {
        configurarComboBox();
        configurarTabla();
        configurarListeners();
    }


    private void configurarComboBox() {
        cbTipo.getItems().addAll("radio", "arma", "vehículo", "uniforme");
        cbEstado.getItems().addAll("activo", "mantenimiento", "retirado");

        if (sistemaController != null) {
            listaEmpleados.addAll(sistemaController.obtenerTodosEmpleados());
            cbEmpleadoAsignar.setItems(listaEmpleados);
        }
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCodigo()));
        colTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipo()));
        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado()));
        colValor.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValorReposicion())));
        colAsignadoA.setCellValueFactory(cellData ->
                new SimpleStringProperty(obtenerEmpleadoAsignado(cellData.getValue())));

        tblEquipos.setItems(listaEquipos);
    }

    private String obtenerEmpleadoAsignado(Equipo equipo) {
        for (Empleado empleado : listaEmpleados) {
            if (sistemaController.getEquipoController().obtenerEquiposPorEmpleado(empleado).contains(equipo)) {
                return empleado.getNombre();
            }
        }
        return "No asignado";
    }

    private void configurarListeners() {
        tblEquipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            equipoSeleccionado = newSelection;
            mostrarInformacionEquipo(newSelection);
        });
    }

    private void cargarDatos() {
        if (listaEquipos != null && sistemaController != null) {
            listaEquipos.clear();
            listaEquipos.addAll(sistemaController.getEquipoController().obtenerTodosEquipos());
        }
    }

    private void mostrarInformacionEquipo(Equipo equipo) {
        if (equipo != null) {
            txtCodigo.setText(equipo.getCodigo());
            cbTipo.setValue(equipo.getTipo());
            cbEstado.setValue(equipo.getEstado());
            txtValorReposicion.setText(String.valueOf(equipo.getValorReposicion()));
        }
    }

    private void agregarEquipo() {
        try {
            Equipo equipo = construirEquipo();
            if (equipo != null && sistemaController.registrarEquipo(equipo)) {
                listaEquipos.add(equipo);
                limpiarCampos();
                mostrarAlerta("Éxito", "Equipo agregado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo agregar el equipo", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos inválidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void asignarEquipo() {
        if (equipoSeleccionado != null && cbEmpleadoAsignar.getValue() != null) {
            if (sistemaController.asignarEquipoAEmpleado(cbEmpleadoAsignar.getValue(), equipoSeleccionado)) {
                tblEquipos.refresh();
                mostrarAlerta("Éxito", "Equipo asignado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo asignar el equipo", Alert.AlertType.ERROR);
            }
        }
    }

    private void retirarEquipo() {
        if (equipoSeleccionado != null && cbEmpleadoAsignar.getValue() != null) {
            if (sistemaController.getEquipoController().retirarEquipoDeEmpleado(cbEmpleadoAsignar.getValue(), equipoSeleccionado)) {
                tblEquipos.refresh();
                mostrarAlerta("Éxito", "Equipo retirado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo retirar el equipo", Alert.AlertType.ERROR);
            }
        }
    }

    private void generarInformeDotacion() {
        if (cbEmpleadoAsignar.getValue() != null) {
            String informe = sistemaController.generarInformeDotacionEmpleado(cbEmpleadoAsignar.getValue());

            TextArea textArea = new TextArea(informe);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefWidth(600);    // ✅ Tamaño directo en TextArea
            textArea.setPrefHeight(400);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informe de Dotación");
            alert.setHeaderText("Dotación de " + cbEmpleadoAsignar.getValue().getNombre());
            alert.getDialogPane().setContent(textArea);  // ✅ TextArea directamente
            alert.showAndWait();
        }
    }

    private void volverAlMenuPrincipal() { // MÉTODO VOLVER AGREGADO
        try {
            app.abrirVistaPrincipal();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo volver al menú principal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Equipo construirEquipo() {
        return new Equipo(
                txtCodigo.getText(),
                cbTipo.getValue(),
                cbEstado.getValue(),
                Double.parseDouble(txtValorReposicion.getText())
        );
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        cbTipo.setValue(null);
        cbEstado.setValue(null);
        txtValorReposicion.clear();
        cbEmpleadoAsignar.setValue(null);
        equipoSeleccionado = null;
        tblEquipos.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}