package co.edu.uniquindio.poo.segurcol.viewcontroller;

import co.edu.uniquindio.poo.segurcol.App;
import co.edu.uniquindio.poo.segurcol.controller.SistemaController;
import co.edu.uniquindio.poo.segurcol.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ServicioViewController {

    private SistemaController sistemaController;
    private ObservableList<Servicio> listaServicios;
    private Servicio servicioSeleccionado;

    @FXML
    private TextField txtCodigoContrato;
    @FXML
    private TextField txtCliente;
    @FXML
    private TextField txtTarifaMensual;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private ComboBox<String> cbTipoServicio;
    @FXML
    private TextField txtNumeroDispositivos;
    @FXML
    private TextField txtCantidadRutas;
    @FXML
    private TextField txtKilometrosRecorridos;
    @FXML
    private TableView<Servicio> tblServicios;
    @FXML
    private TableColumn<Servicio, String> colCodigo;
    @FXML
    private TableColumn<Servicio, String> colCliente;
    @FXML
    private TableColumn<Servicio, String> colTipo;
    @FXML
    private TableColumn<Servicio, String> colEstado;
    @FXML
    private TableColumn<Servicio, String> colCosto;
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
    void onAgregarServicio() {
        agregarServicio();
    }

    @FXML
    void onActualizarServicio() {
        actualizarServicio();
    }

    @FXML
    void onEliminarServicio() {
        eliminarServicio();
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
        this.listaServicios = FXCollections.observableArrayList();
        cargarServicios();
    }

    @FXML
    void initialize() {
        configurarComboBox();
        configurarTabla();
        configurarListeners();
    }

    private void configurarComboBox() {
        cbEstado.getItems().addAll("activo", "suspendido", "finalizado");
        cbTipoServicio.getItems().addAll("Custodia Fija", "Patrullaje Móvil", "Monitoreo Remoto");

        cbTipoServicio.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            mostrarCamposEspecificos(newVal);
        });
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCodigoContrato()));
        colCliente.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCliente()));
        colTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));
        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado()));
        colCosto.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().calcularCostoMensual())));

        tblServicios.setItems(listaServicios);
    }

    private void configurarListeners() {
        tblServicios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            servicioSeleccionado = newSelection;
            mostrarInformacionServicio(newSelection);
        });
    }

    private void cargarServicios() {
        if (listaServicios != null && sistemaController != null) {
            listaServicios.clear();
            listaServicios.addAll(sistemaController.getServicioController().obtenerTodosServicios());
        }
    }

    private void mostrarCamposEspecificos(String tipoServicio) {
        txtNumeroDispositivos.setVisible(false);
        txtCantidadRutas.setVisible(false);
        txtKilometrosRecorridos.setVisible(false);

        switch (tipoServicio) {
            case "Monitoreo Remoto":
                txtNumeroDispositivos.setVisible(true);
                break;
            case "Patrullaje Móvil":
                txtCantidadRutas.setVisible(true);
                txtKilometrosRecorridos.setVisible(true);
                break;
            case "Custodia Fija":
                break;
        }
    }

    private void mostrarInformacionServicio(Servicio servicio) {
        if (servicio != null) {
            txtCodigoContrato.setText(servicio.getCodigoContrato());
            txtCliente.setText(servicio.getCliente());
            txtTarifaMensual.setText(String.valueOf(servicio.getTarifaMensual()));
            cbEstado.setValue(servicio.getEstado());

            if (servicio instanceof MonitoreoRemoto) {
                cbTipoServicio.setValue("Monitoreo Remoto");
                MonitoreoRemoto monitoreo = (MonitoreoRemoto) servicio;
                txtNumeroDispositivos.setText(String.valueOf(monitoreo.getNumeroDispositivos()));
            } else if (servicio instanceof PatrullajeMovil) {
                cbTipoServicio.setValue("Patrullaje Móvil");
                PatrullajeMovil patrullaje = (PatrullajeMovil) servicio;
                txtCantidadRutas.setText(String.valueOf(patrullaje.getCantidadRutas()));
                txtKilometrosRecorridos.setText(String.valueOf(patrullaje.getKilometrosRecorridos()));
            } else if (servicio instanceof CustodiaFija) {
                cbTipoServicio.setValue("Custodia Fija");
            }
        }
    }

    private void agregarServicio() {
        try {
            Servicio servicio = construirServicio();
            if (servicio != null && sistemaController.registrarServicio(servicio)) {
                listaServicios.add(servicio);
                limpiarCampos();
                mostrarAlerta("Éxito", "Servicio agregado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo agregar el servicio", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos inválidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void actualizarServicio() {
        mostrarAlerta("Info", "Funcionalidad en desarrollo", Alert.AlertType.INFORMATION);
    }

    private void eliminarServicio() {
        if (servicioSeleccionado != null) {
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

    private Servicio construirServicio() {
        String codigo = txtCodigoContrato.getText();
        String cliente = txtCliente.getText();
        double tarifa = Double.parseDouble(txtTarifaMensual.getText());
        String estado = cbEstado.getValue();
        String tipoServicio = cbTipoServicio.getValue();

        switch (tipoServicio) {
            case "Monitoreo Remoto":
                return new MonitoreoRemoto(codigo, cliente, tarifa, estado,
                        Integer.parseInt(txtNumeroDispositivos.getText()));
            case "Patrullaje Móvil":
                return new PatrullajeMovil(codigo, cliente, tarifa, estado,
                        Integer.parseInt(txtCantidadRutas.getText()),
                        Double.parseDouble(txtKilometrosRecorridos.getText()));
            case "Custodia Fija":
                return new CustodiaFija(codigo, cliente, tarifa, estado);
            default:
                return null;
        }
    }

    private void limpiarCampos() {
        txtCodigoContrato.clear();
        txtCliente.clear();
        txtTarifaMensual.clear();
        cbEstado.setValue(null);
        cbTipoServicio.setValue(null);
        txtNumeroDispositivos.clear();
        txtCantidadRutas.clear();
        txtKilometrosRecorridos.clear();
        servicioSeleccionado = null;
        tblServicios.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}