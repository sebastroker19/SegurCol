package co.edu.uniquindio.poo.segurcol.viewcontroller;

import co.edu.uniquindio.poo.segurcol.App;
import co.edu.uniquindio.poo.segurcol.controller.SistemaController;
import co.edu.uniquindio.poo.segurcol.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class ServicioViewController {

    private SistemaController sistemaController;
    private ObservableList<Servicio> listaServicios;
    private Servicio servicioSeleccionado;

    // Campos de entrada
    @FXML private TextField txtCodigoContrato;
    @FXML private TextField txtCliente;
    @FXML private TextField txtTarifaMensual;
    @FXML private ComboBox<String> cbEstado;
    @FXML private ComboBox<String> cbTipoServicio;
    @FXML private TextField txtNumeroDispositivos;
    @FXML private TextField txtCantidadRutas;
    @FXML private TextField txtKilometrosRecorridos;

    // Tabla y columnas
    @FXML private TableView<Servicio> tblServicios;
    @FXML private TableColumn<Servicio, String> colCodigo;
    @FXML private TableColumn<Servicio, String> colCliente;
    @FXML private TableColumn<Servicio, String> colTipo;
    @FXML private TableColumn<Servicio, String> colEstado;
    @FXML private TableColumn<Servicio, String> colCosto;

    // Contenedores de campos específicos
    @FXML private HBox boxMonitoreo;
    @FXML private HBox boxPatrullaje1;
    @FXML private HBox boxPatrullaje2;

    // Botones
    @FXML private Button btnAgregar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnVolverMenu;

    private App app;

    public ServicioViewController() {
        // Constructor vacío requerido por FXML
    }

    @FXML
    void initialize() {
        System.out.println("=== INICIALIZANDO SERVICIO VIEW CONTROLLER ===");

        // DEBUG: Verificar que los elementos FXML se cargaron
        System.out.println("boxMonitoreo: " + (boxMonitoreo != null ? "CARGADO" : "NULL"));
        System.out.println("boxPatrullaje1: " + (boxPatrullaje1 != null ? "CARGADO" : "NULL"));
        System.out.println("boxPatrullaje2: " + (boxPatrullaje2 != null ? "CARGADO" : "NULL"));
        System.out.println("cbTipoServicio: " + (cbTipoServicio != null ? "CARGADO" : "NULL"));

        // Inicializar la lista observable
        listaServicios = FXCollections.observableArrayList();

        configurarComboBox();
        configurarTabla();
        configurarListeners();
        ocultarTodosLosCamposEspecificos();

        // Cargar datos de prueba temporalmente
        cargarDatosDePrueba();

        System.out.println("Configuración completada");
    }

    public void setApp(App app) {
        this.app = app;
        this.sistemaController = app.getSistemaController();
        cargarServicios();
    }

    private void configurarComboBox() {
        // Limpiar y cargar combobox
        cbEstado.getItems().clear();
        cbTipoServicio.getItems().clear();

        cbEstado.getItems().addAll("activo", "suspendido", "finalizado");
        cbTipoServicio.getItems().addAll("Custodia Fija", "Patrullaje Móvil", "Monitoreo Remoto");

        // DEBUG
        System.out.println("ComboBox configurados:");
        System.out.println(" - Estado: " + cbEstado.getItems());
        System.out.println(" - Tipo Servicio: " + cbTipoServicio.getItems());

        // Listener para mostrar campos específicos - CORREGIDO
        cbTipoServicio.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Tipo de servicio seleccionado: " + newVal);
            if (newVal != null) {
                mostrarCamposEspecificos(newVal);
            } else {
                ocultarTodosLosCamposEspecificos();
            }
        });
    }

    private void configurarTabla() {
        System.out.println("Configurando tabla de servicios...");

        // Configurar columnas
        colCodigo.setCellValueFactory(cellData -> {
            String codigo = cellData.getValue().getCodigoContrato();
            return new SimpleStringProperty(codigo);
        });

        colCliente.setCellValueFactory(cellData -> {
            String cliente = cellData.getValue().getCliente();
            return new SimpleStringProperty(cliente);
        });

        colTipo.setCellValueFactory(cellData -> {
            String tipo = obtenerTipoServicio(cellData.getValue());
            return new SimpleStringProperty(tipo);
        });

        colEstado.setCellValueFactory(cellData -> {
            String estado = cellData.getValue().getEstado();
            return new SimpleStringProperty(estado);
        });

        colCosto.setCellValueFactory(cellData -> {
            double costo = cellData.getValue().calcularCostoMensual();
            return new SimpleStringProperty(String.format("$%,.0f", costo));
        });

        // Asignar la lista observable a la tabla
        tblServicios.setItems(listaServicios);

        System.out.println("Tabla configurada con " + listaServicios.size() + " servicios");
    }

    private void configurarListeners() {
        tblServicios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            servicioSeleccionado = newSelection;
            if (newSelection != null) {
                mostrarInformacionServicio(newSelection);
            }
        });
    }

    private void cargarServicios() {
        try {
            if (sistemaController != null) {
                // Obtener servicios del sistema
                var serviciosDelSistema = sistemaController.getServicioController().obtenerTodosServicios();
                System.out.println("Servicios en sistema: " + serviciosDelSistema.size());

                // Limpiar y cargar la lista observable
                listaServicios.clear();
                listaServicios.addAll(serviciosDelSistema);

                // Forzar actualización de la tabla
                tblServicios.refresh();

                System.out.println("Servicios cargados en tabla: " + listaServicios.size());
            }
        } catch (Exception e) {
            System.err.println("Error cargando servicios iniciales: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDatosDePrueba() {
        // Datos de prueba para verificar que la interfaz funciona
        listaServicios.add(new CustodiaFija("CONT-001", "Empresa ABC", 5000000, "activo"));
        listaServicios.add(new PatrullajeMovil("CONT-002", "Centro Comercial XYZ", 3500000, "activo", 5, 150.5));
        listaServicios.add(new MonitoreoRemoto("CONT-003", "Condominio Las Lomas", 2800000, "activo", 12));

        System.out.println("Datos de prueba cargados: " + listaServicios.size() + " servicios");
    }

    private String obtenerTipoServicio(Servicio servicio) {
        if (servicio instanceof CustodiaFija) return "Custodia Fija";
        if (servicio instanceof PatrullajeMovil) return "Patrullaje Móvil";
        if (servicio instanceof MonitoreoRemoto) return "Monitoreo Remoto";
        return "Desconocido";
    }

    private void ocultarTodosLosCamposEspecificos() {
        if (boxMonitoreo != null) {
            boxMonitoreo.setVisible(false);
            boxMonitoreo.setManaged(false);
        }
        if (boxPatrullaje1 != null) {
            boxPatrullaje1.setVisible(false);
            boxPatrullaje1.setManaged(false);
        }
        if (boxPatrullaje2 != null) {
            boxPatrullaje2.setVisible(false);
            boxPatrullaje2.setManaged(false);
        }
        System.out.println("Todos los campos específicos ocultos");
    }

    private void mostrarCamposEspecificos(String tipoServicio) {
        System.out.println("Mostrando campos para: " + tipoServicio);

        ocultarTodosLosCamposEspecificos();

        switch (tipoServicio) {
            case "Monitoreo Remoto":
                if (boxMonitoreo != null) {
                    boxMonitoreo.setVisible(true);
                    boxMonitoreo.setManaged(true);
                    System.out.println("Campos de Monitoreo Remoto mostrados");
                }
                break;
            case "Patrullaje Móvil":
                if (boxPatrullaje1 != null) {
                    boxPatrullaje1.setVisible(true);
                    boxPatrullaje1.setManaged(true);
                }
                if (boxPatrullaje2 != null) {
                    boxPatrullaje2.setVisible(true);
                    boxPatrullaje2.setManaged(true);
                }
                System.out.println("Campos de Patrullaje Móvil mostrados");
                break;
            case "Custodia Fija":
                // No tiene campos específicos
                System.out.println("Custodia Fija - sin campos específicos");
                break;
        }
    }

    // === MÉTODOS DE ACCIÓN ===

    @FXML
    void onAgregarServicio() {
        System.out.println("=== AGREGANDO NUEVO SERVICIO ===");
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
    void onVolverMenu() {
        volverAlMenuPrincipal();
    }

    private void agregarServicio() {
        try {
            // 1. Validar campos básicos
            if (!validarCamposBasicos()) {
                mostrarAlerta("Error", "Complete todos los campos obligatorios", Alert.AlertType.ERROR);
                return;
            }

            // 2. Validar campos específicos
            String tipo = cbTipoServicio.getValue();
            if (!validarCamposEspecificos(tipo)) {
                mostrarAlerta("Error", "Complete los campos específicos para " + tipo, Alert.AlertType.ERROR);
                return;
            }

            // 3. Construir servicio
            Servicio nuevoServicio = construirServicio();
            if (nuevoServicio == null) {
                mostrarAlerta("Error", "No se pudo crear el servicio", Alert.AlertType.ERROR);
                return;
            }

            System.out.println("Servicio creado: " + nuevoServicio.getCliente() + " - " + nuevoServicio.getCodigoContrato());

            // 4. Registrar en el sistema
            if (sistemaController != null && sistemaController.registrarServicio(nuevoServicio)) {
                // 5. AGREGAR DIRECTAMENTE A LA LISTA OBSERVABLE
                listaServicios.add(nuevoServicio);

                // 6. FORZAR ACTUALIZACIÓN VISUAL
                tblServicios.refresh();

                System.out.println("Servicio agregado a la tabla. Total: " + listaServicios.size());

                // 7. Mostrar éxito y limpiar
                mostrarAlerta("Éxito", "Servicio agregado correctamente", Alert.AlertType.INFORMATION);
                limpiarCampos();

            } else {
                // Si no hay sistemaController, agregar directamente a la lista
                listaServicios.add(nuevoServicio);
                tblServicios.refresh();
                mostrarAlerta("Éxito", "Servicio agregado correctamente", Alert.AlertType.INFORMATION);
                limpiarCampos();
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Verifique los valores numéricos", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean validarCamposBasicos() {
        return !txtCodigoContrato.getText().trim().isEmpty() &&
                !txtCliente.getText().trim().isEmpty() &&
                !txtTarifaMensual.getText().trim().isEmpty() &&
                cbEstado.getValue() != null &&
                cbTipoServicio.getValue() != null;
    }

    private boolean validarCamposEspecificos(String tipo) {
        switch (tipo) {
            case "Monitoreo Remoto":
                return !txtNumeroDispositivos.getText().trim().isEmpty();
            case "Patrullaje Móvil":
                return !txtCantidadRutas.getText().trim().isEmpty() &&
                        !txtKilometrosRecorridos.getText().trim().isEmpty();
            case "Custodia Fija":
                return true; // No tiene campos específicos
            default:
                return false;
        }
    }

    private Servicio construirServicio() {
        String codigo = txtCodigoContrato.getText().trim();
        String cliente = txtCliente.getText().trim();
        double tarifa = Double.parseDouble(txtTarifaMensual.getText().trim());
        String estado = cbEstado.getValue();
        String tipoServicio = cbTipoServicio.getValue();

        switch (tipoServicio) {
            case "Monitoreo Remoto":
                int dispositivos = Integer.parseInt(txtNumeroDispositivos.getText().trim());
                return new MonitoreoRemoto(codigo, cliente, tarifa, estado, dispositivos);
            case "Patrullaje Móvil":
                int rutas = Integer.parseInt(txtCantidadRutas.getText().trim());
                double kilometros = Double.parseDouble(txtKilometrosRecorridos.getText().trim());
                return new PatrullajeMovil(codigo, cliente, tarifa, estado, rutas, kilometros);
            case "Custodia Fija":
                return new CustodiaFija(codigo, cliente, tarifa, estado);
            default:
                return null;
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

    private void actualizarServicio() {
        if (servicioSeleccionado != null) {
            mostrarAlerta("Información", "Funcionalidad de actualización en desarrollo", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "Seleccione un servicio para actualizar", Alert.AlertType.ERROR);
        }
    }

    private void eliminarServicio() {
        if (servicioSeleccionado != null) {
            listaServicios.remove(servicioSeleccionado);
            tblServicios.refresh();
            limpiarCampos();
            mostrarAlerta("Éxito", "Servicio eliminado correctamente", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "Seleccione un servicio para eliminar", Alert.AlertType.ERROR);
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
        ocultarTodosLosCamposEspecificos();

        System.out.println("Campos de servicio limpiados");
    }

    private void volverAlMenuPrincipal() {
        try {
            if (app != null) {
                app.abrirVistaPrincipal();
            } else {
                mostrarAlerta("Error", "No se puede volver al menú principal", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo volver al menú: " + e.getMessage(), Alert.AlertType.ERROR);
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