package co.edu.uniquindio.poo.segurcol.viewcontroller;

import co.edu.uniquindio.poo.segurcol.App;
import co.edu.uniquindio.poo.segurcol.controller.SistemaController;
import co.edu.uniquindio.poo.segurcol.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EmpleadoViewController {

    private SistemaController sistemaController;
    private ObservableList<Empleado> listaEmpleados;
    private Empleado empleadoSeleccionado;

    @FXML
    private TextField txtDocumento;
    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<String> cbTurno;
    @FXML
    private TextField txtSalarioBase;
    @FXML
    private ComboBox<String> cbTipoEmpleado;
    @FXML
    private TextField txtNumeroPuesto;
    @FXML
    private ComboBox<String> cbTipoArma;
    @FXML
    private TextField txtBonoCoordinacion;
    @FXML
    private TextField txtNumeroZonas;
    @FXML
    private TableView<Empleado> tblEmpleados;
    @FXML
    private TableColumn<Empleado, String> colDocumento;
    @FXML
    private TableColumn<Empleado, String> colNombre;
    @FXML
    private TableColumn<Empleado, String> colTurno;
    @FXML
    private TableColumn<Empleado, String> colTipo;
    @FXML
    private TableColumn<Empleado, String> colSalario;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnVolverMenu; // BOTÓN VOLVER AGREGADO

    private App app;

    @FXML
    void onAgregarEmpleado() {
        agregarEmpleado();
    }

    @FXML
    void onActualizarEmpleado() {
        actualizarEmpleado();
    }

    @FXML
    void onEliminarEmpleado() {
        eliminarEmpleado();
    }

    @FXML
    void onLimpiar() {
        limpiarCampos();
    }

    @FXML
    void onVolverMenu() { // MÉTODO VOLVER AGREGADO
        volverAlMenuPrincipal();
    }

    public void setApp(App app) {
        this.app = app;
        this.sistemaController = app.getSistemaController();
        this.listaEmpleados = FXCollections.observableArrayList();
        cargarEmpleados();
    }

    @FXML
    void initialize() {
        configurarComboBox();
        configurarTabla();
        configurarListeners();
    }

    private void configurarComboBox() {
        cbTurno.getItems().addAll("día", "noche");
        cbTipoEmpleado.getItems().addAll("Vigilante", "Supervisor", "Operador de Monitoreo");
        cbTipoArma.getItems().addAll("no letal", "letal", "sin arma");

        cbTipoEmpleado.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            mostrarCamposEspecificos(newVal);
        });
    }

    private void configurarTabla() {
        colDocumento.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDocumento()));
        colNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombre()));
        colTurno.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTurno()));
        colTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));
        colSalario.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().calcularSalarioTotal())));

        tblEmpleados.setItems(listaEmpleados);
    }

    private void configurarListeners() {
        tblEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            empleadoSeleccionado = newSelection;
            mostrarInformacionEmpleado(newSelection);
        });
    }

    private void cargarEmpleados() {
        if (listaEmpleados != null && sistemaController != null) {
            listaEmpleados.clear();
            listaEmpleados.addAll(sistemaController.obtenerTodosEmpleados());
        }
    }

    private void mostrarCamposEspecificos(String tipoEmpleado) {
        txtNumeroPuesto.setVisible(false);
        cbTipoArma.setVisible(false);
        txtBonoCoordinacion.setVisible(false);
        txtNumeroZonas.setVisible(false);

        switch (tipoEmpleado) {
            case "Vigilante":
                txtNumeroPuesto.setVisible(true);
                cbTipoArma.setVisible(true);
                break;
            case "Supervisor":
                txtBonoCoordinacion.setVisible(true);
                break;
            case "Operador de Monitoreo":
                txtNumeroZonas.setVisible(true);
                break;
        }
    }

    private void mostrarInformacionEmpleado(Empleado empleado) {
        if (empleado != null) {
            txtDocumento.setText(empleado.getDocumento());
            txtNombre.setText(empleado.getNombre());
            cbTurno.setValue(empleado.getTurno());
            txtSalarioBase.setText(String.valueOf(empleado.getSalarioBase()));

            if (empleado instanceof Vigilante) {
                cbTipoEmpleado.setValue("Vigilante");
                Vigilante vigilante = (Vigilante) empleado;
                txtNumeroPuesto.setText(vigilante.getNumeroPuesto());
                cbTipoArma.setValue(vigilante.getTipoArma());
            } else if (empleado instanceof Supervisor) {
                cbTipoEmpleado.setValue("Supervisor");
                Supervisor supervisor = (Supervisor) empleado;
                txtBonoCoordinacion.setText(String.valueOf(supervisor.getBonoCoordinacion()));
            } else if (empleado instanceof OperadorMonitoreo) {
                cbTipoEmpleado.setValue("Operador de Monitoreo");
                OperadorMonitoreo operador = (OperadorMonitoreo) empleado;
                txtNumeroZonas.setText(String.valueOf(operador.getNumeroZonasMonitoreo()));
            }
        }
    }

    private void agregarEmpleado() {
        try {
            Empleado empleado = construirEmpleado();
            if (empleado != null && sistemaController.registrarEmpleado(empleado)) {
                listaEmpleados.add(empleado);
                limpiarCampos();
                mostrarAlerta("Éxito", "Empleado agregado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo agregar el empleado", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos inválidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void actualizarEmpleado() {
        mostrarAlerta("Info", "Funcionalidad en desarrollo", Alert.AlertType.INFORMATION);
    }

    private void eliminarEmpleado() {
        if (empleadoSeleccionado != null) {
            mostrarAlerta("Info", "Funcionalidad en desarrollo", Alert.AlertType.INFORMATION);
        }
    }

    private void volverAlMenuPrincipal() { // MÉTODO VOLVER AGREGADO
        try {
            app.abrirVistaPrincipal();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo volver al menú principal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Empleado construirEmpleado() {
        String documento = txtDocumento.getText();
        String nombre = txtNombre.getText();
        String turno = cbTurno.getValue();
        double salarioBase = Double.parseDouble(txtSalarioBase.getText());
        String tipoEmpleado = cbTipoEmpleado.getValue();

        switch (tipoEmpleado) {
            case "Vigilante":
                return new Vigilante(nombre, documento, turno, salarioBase,
                        txtNumeroPuesto.getText(), cbTipoArma.getValue());
            case "Supervisor":
                return new Supervisor(nombre, documento, turno, salarioBase,
                        Double.parseDouble(txtBonoCoordinacion.getText()));
            case "Operador de Monitoreo":
                return new OperadorMonitoreo(nombre, documento, turno, salarioBase,
                        Integer.parseInt(txtNumeroZonas.getText()));
            default:
                return null;
        }
    }

    private void limpiarCampos() {
        txtDocumento.clear();
        txtNombre.clear();
        cbTurno.setValue(null);
        txtSalarioBase.clear();
        cbTipoEmpleado.setValue(null);
        txtNumeroPuesto.clear();
        cbTipoArma.setValue(null);
        txtBonoCoordinacion.clear();
        txtNumeroZonas.clear();
        empleadoSeleccionado = null;
        tblEmpleados.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}