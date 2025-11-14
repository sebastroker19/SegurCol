package co.edu.uniquindio.poo.segurcol.viewcontroller;

import co.edu.uniquindio.poo.segurcol.App;
import co.edu.uniquindio.poo.segurcol.controller.SistemaController;
import co.edu.uniquindio.poo.segurcol.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class EmpleadoViewController {

    private SistemaController sistemaController;
    private ObservableList<Empleado> listaEmpleados;
    private Empleado empleadoSeleccionado;

    // Campos de entrada
    @FXML private TextField txtDocumento;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cbTurno;
    @FXML private TextField txtSalarioBase;
    @FXML private ComboBox<String> cbTipoEmpleado;
    @FXML private TextField txtNumeroPuesto;
    @FXML private ComboBox<String> cbTipoArma;
    @FXML private TextField txtBonoCoordinacion;
    @FXML private TextField txtNumeroZonas;

    // Tabla y columnas
    @FXML private TableView<Empleado> tblEmpleados;
    @FXML private TableColumn<Empleado, String> colDocumento;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colTurno;
    @FXML private TableColumn<Empleado, String> colTipo;
    @FXML private TableColumn<Empleado, String> colSalario;

    // Contenedores de campos específicos
    @FXML private VBox boxVigilante;
    @FXML private VBox boxSupervisor;
    @FXML private VBox boxOperador;

    // Botones
    @FXML private Button btnAgregar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnVolverMenu;

    private App app;

    public EmpleadoViewController() {
        // Constructor vacío requerido por FXML
    }

    @FXML
    void initialize() {
        System.out.println("=== INICIALIZANDO EMPLEADO VIEW CONTROLLER ===");

        // DEBUG: Verificar que los elementos FXML se cargaron
        System.out.println("boxVigilante: " + (boxVigilante != null ? "CARGADO" : "NULL"));
        System.out.println("boxSupervisor: " + (boxSupervisor != null ? "CARGADO" : "NULL"));
        System.out.println("boxOperador: " + (boxOperador != null ? "CARGADO" : "NULL"));
        System.out.println("cbTipoEmpleado: " + (cbTipoEmpleado != null ? "CARGADO" : "NULL"));
        System.out.println("btnAgregar: " + (btnAgregar != null ? "CARGADO" : "NULL"));

        // Inicializar la lista observable
        listaEmpleados = FXCollections.observableArrayList();

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
        cargarEmpleadosIniciales();
    }

    private void configurarComboBox() {
        // Limpiar y cargar combobox
        cbTurno.getItems().clear();
        cbTipoEmpleado.getItems().clear();
        cbTipoArma.getItems().clear();

        cbTurno.getItems().addAll("día", "noche");
        cbTipoEmpleado.getItems().addAll("Vigilante", "Supervisor", "Operador de Monitoreo");
        cbTipoArma.getItems().addAll("no letal", "letal", "sin arma");

        // DEBUG
        System.out.println("ComboBox configurados:");
        System.out.println(" - Turno: " + cbTurno.getItems());
        System.out.println(" - Tipo Empleado: " + cbTipoEmpleado.getItems());
        System.out.println(" - Tipo Arma: " + cbTipoArma.getItems());

        // Listener para mostrar campos específicos - CORREGIDO
        cbTipoEmpleado.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Tipo de empleado seleccionado: " + newVal);
            if (newVal != null) {
                mostrarCamposEspecificos(newVal);
            } else {
                ocultarTodosLosCamposEspecificos();
            }
        });
    }

    private void configurarTabla() {
        System.out.println("Configurando tabla...");

        // Configurar columnas
        colDocumento.setCellValueFactory(cellData -> {
            String documento = cellData.getValue().getDocumento();
            return new SimpleStringProperty(documento);
        });

        colNombre.setCellValueFactory(cellData -> {
            String nombre = cellData.getValue().getNombre();
            return new SimpleStringProperty(nombre);
        });

        colTurno.setCellValueFactory(cellData -> {
            String turno = cellData.getValue().getTurno();
            return new SimpleStringProperty(turno);
        });

        colTipo.setCellValueFactory(cellData -> {
            String tipo = obtenerTipoEmpleado(cellData.getValue());
            return new SimpleStringProperty(tipo);
        });

        colSalario.setCellValueFactory(cellData -> {
            double salario = cellData.getValue().calcularSalarioTotal();
            return new SimpleStringProperty(String.format("$%,.0f", salario));
        });

        // Asignar la lista observable a la tabla
        tblEmpleados.setItems(listaEmpleados);

        System.out.println("Tabla configurada con " + listaEmpleados.size() + " empleados");
    }

    private void configurarListeners() {
        tblEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            empleadoSeleccionado = newSelection;
            if (newSelection != null) {
                mostrarInformacionEmpleado(newSelection);
            }
        });
    }

    private void cargarEmpleadosIniciales() {
        try {
            if (sistemaController != null) {
                // Obtener empleados del sistema
                var empleadosDelSistema = sistemaController.obtenerTodosEmpleados();
                System.out.println("Empleados en sistema: " + empleadosDelSistema.size());

                // Limpiar y cargar la lista observable
                listaEmpleados.clear();
                listaEmpleados.addAll(empleadosDelSistema);

                // Forzar actualización de la tabla
                tblEmpleados.refresh();

                System.out.println("Empleados cargados en tabla: " + listaEmpleados.size());
            }
        } catch (Exception e) {
            System.err.println("Error cargando empleados iniciales: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDatosDePrueba() {
        // Datos de prueba para verificar que la interfaz funciona
        listaEmpleados.add(new Vigilante("Carlos López", "12345678", "día", 2000000, "Puesto-1", "no letal"));
        listaEmpleados.add(new Vigilante("Ana Martínez", "87654321", "noche", 2200000, "Puesto-2", "letal"));
        listaEmpleados.add(new Supervisor("María García", "11223344", "día", 3000000, 500000));
        listaEmpleados.add(new OperadorMonitoreo("Pedro Rodríguez", "44332211", "noche", 2500000, 5));

        System.out.println("Datos de prueba cargados: " + listaEmpleados.size() + " empleados");
    }

    private String obtenerTipoEmpleado(Empleado empleado) {
        if (empleado instanceof Vigilante) return "Vigilante";
        if (empleado instanceof Supervisor) return "Supervisor";
        if (empleado instanceof OperadorMonitoreo) return "Operador";
        return "Desconocido";
    }

    private void ocultarTodosLosCamposEspecificos() {
        if (boxVigilante != null) {
            boxVigilante.setVisible(false);
            boxVigilante.setManaged(false);
        }
        if (boxSupervisor != null) {
            boxSupervisor.setVisible(false);
            boxSupervisor.setManaged(false);
        }
        if (boxOperador != null) {
            boxOperador.setVisible(false);
            boxOperador.setManaged(false);
        }
        System.out.println("Todos los campos específicos ocultos");
    }

    private void mostrarCamposEspecificos(String tipoEmpleado) {
        System.out.println("Mostrando campos para: " + tipoEmpleado);

        ocultarTodosLosCamposEspecificos();

        switch (tipoEmpleado) {
            case "Vigilante":
                if (boxVigilante != null) {
                    boxVigilante.setVisible(true);
                    boxVigilante.setManaged(true);
                    System.out.println("Campos de Vigilante mostrados");
                }
                break;
            case "Supervisor":
                if (boxSupervisor != null) {
                    boxSupervisor.setVisible(true);
                    boxSupervisor.setManaged(true);
                    System.out.println("Campos de Supervisor mostrados");
                }
                break;
            case "Operador de Monitoreo":
                if (boxOperador != null) {
                    boxOperador.setVisible(true);
                    boxOperador.setManaged(true);
                    System.out.println("Campos de Operador mostrados");
                }
                break;
        }
    }

    // === MÉTODOS DE ACCIÓN ===

    @FXML
    void onAgregarEmpleado() {
        System.out.println("=== AGREGANDO NUEVO EMPLEADO ===");
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
    void onVolverMenu() {
        volverAlMenuPrincipal();
    }

    private void agregarEmpleado() {
        try {
            // 1. Validar campos básicos
            if (!validarCamposBasicos()) {
                mostrarAlerta("Error", "Complete todos los campos obligatorios", Alert.AlertType.ERROR);
                return;
            }

            // 2. Validar campos específicos
            String tipo = cbTipoEmpleado.getValue();
            if (!validarCamposEspecificos(tipo)) {
                mostrarAlerta("Error", "Complete los campos específicos para " + tipo, Alert.AlertType.ERROR);
                return;
            }

            // 3. Construir empleado
            Empleado nuevoEmpleado = construirEmpleado();
            if (nuevoEmpleado == null) {
                mostrarAlerta("Error", "No se pudo crear el empleado", Alert.AlertType.ERROR);
                return;
            }

            System.out.println("Empleado creado: " + nuevoEmpleado.getNombre() + " - " + nuevoEmpleado.getDocumento());

            // 4. Registrar en el sistema
            if (sistemaController != null && sistemaController.registrarEmpleado(nuevoEmpleado)) {
                // 5. AGREGAR DIRECTAMENTE A LA LISTA OBSERVABLE
                listaEmpleados.add(nuevoEmpleado);

                // 6. FORZAR ACTUALIZACIÓN VISUAL
                tblEmpleados.refresh();

                System.out.println("Empleado agregado a la tabla. Total: " + listaEmpleados.size());

                // 7. Mostrar éxito y limpiar
                mostrarAlerta("Éxito", "Empleado agregado correctamente", Alert.AlertType.INFORMATION);
                limpiarCampos();

            } else {
                // Si no hay sistemaController, agregar directamente a la lista
                listaEmpleados.add(nuevoEmpleado);
                tblEmpleados.refresh();
                mostrarAlerta("Éxito", "Empleado agregado correctamente", Alert.AlertType.INFORMATION);
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
        return !txtDocumento.getText().trim().isEmpty() &&
                !txtNombre.getText().trim().isEmpty() &&
                cbTurno.getValue() != null &&
                !txtSalarioBase.getText().trim().isEmpty() &&
                cbTipoEmpleado.getValue() != null;
    }

    private boolean validarCamposEspecificos(String tipo) {
        switch (tipo) {
            case "Vigilante":
                return !txtNumeroPuesto.getText().trim().isEmpty() && cbTipoArma.getValue() != null;
            case "Supervisor":
                return !txtBonoCoordinacion.getText().trim().isEmpty();
            case "Operador de Monitoreo":
                return !txtNumeroZonas.getText().trim().isEmpty();
            default:
                return false;
        }
    }

    private Empleado construirEmpleado() {
        String documento = txtDocumento.getText().trim();
        String nombre = txtNombre.getText().trim();
        String turno = cbTurno.getValue();
        double salarioBase = Double.parseDouble(txtSalarioBase.getText().trim());
        String tipo = cbTipoEmpleado.getValue();

        switch (tipo) {
            case "Vigilante":
                return new Vigilante(nombre, documento, turno, salarioBase,
                        txtNumeroPuesto.getText().trim(), cbTipoArma.getValue());
            case "Supervisor":
                double bono = Double.parseDouble(txtBonoCoordinacion.getText().trim());
                return new Supervisor(nombre, documento, turno, salarioBase, bono);
            case "Operador de Monitoreo":
                int zonas = Integer.parseInt(txtNumeroZonas.getText().trim());
                return new OperadorMonitoreo(nombre, documento, turno, salarioBase, zonas);
            default:
                return null;
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
                Vigilante v = (Vigilante) empleado;
                txtNumeroPuesto.setText(v.getNumeroPuesto());
                cbTipoArma.setValue(v.getTipoArma());
            } else if (empleado instanceof Supervisor) {
                cbTipoEmpleado.setValue("Supervisor");
                Supervisor s = (Supervisor) empleado;
                txtBonoCoordinacion.setText(String.valueOf(s.getBonoCoordinacion()));
            } else if (empleado instanceof OperadorMonitoreo) {
                cbTipoEmpleado.setValue("Operador de Monitoreo");
                OperadorMonitoreo o = (OperadorMonitoreo) empleado;
                txtNumeroZonas.setText(String.valueOf(o.getNumeroZonasMonitoreo()));
            }
        }
    }

    private void actualizarEmpleado() {
        if (empleadoSeleccionado != null) {
            // Implementar lógica de actualización
            mostrarAlerta("Información", "Funcionalidad de actualización en desarrollo", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "Seleccione un empleado para actualizar", Alert.AlertType.ERROR);
        }
    }

    private void eliminarEmpleado() {
        if (empleadoSeleccionado != null) {
            // Implementar lógica de eliminación
            listaEmpleados.remove(empleadoSeleccionado);
            tblEmpleados.refresh();
            limpiarCampos();
            mostrarAlerta("Éxito", "Empleado eliminado correctamente", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "Seleccione un empleado para eliminar", Alert.AlertType.ERROR);
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
        ocultarTodosLosCamposEspecificos();

        System.out.println("Campos limpiados");
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