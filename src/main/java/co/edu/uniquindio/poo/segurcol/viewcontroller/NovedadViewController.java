package co.edu.uniquindio.poo.segurcol.viewcontroller;

import co.edu.uniquindio.poo.segurcol.App;
import co.edu.uniquindio.poo.segurcol.controller.SistemaController;
import co.edu.uniquindio.poo.segurcol.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import java.time.LocalDateTime;

public class NovedadViewController {

    private SistemaController sistemaController;
    private ObservableList<RegistroNovedad> listaNovedades;

    // Campos de entrada
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbTipoNovedad;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<Empleado> cbEmpleado;
    @FXML private ComboBox<Servicio> cbServicio;

    // Tabla y columnas
    @FXML private TableView<RegistroNovedad> tblNovedades;
    @FXML private TableColumn<RegistroNovedad, String> colFecha;
    @FXML private TableColumn<RegistroNovedad, String> colTipo;
    @FXML private TableColumn<RegistroNovedad, String> colDescripcion;
    @FXML private TableColumn<RegistroNovedad, String> colEmpleado;
    @FXML private TableColumn<RegistroNovedad, String> colServicio;

    // Botones
    @FXML private Button btnRegistrar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnFiltrar;
    @FXML private Button btnVolverMenu;

    private App app;

    public NovedadViewController() {
        // Constructor vacío requerido por FXML
    }

    @FXML
    void initialize() {
        System.out.println("=== INICIALIZANDO NOVEDAD VIEW CONTROLLER ===");

        // Inicializar la lista observable
        listaNovedades = FXCollections.observableArrayList();

        configurarComboBox();
        configurarTabla();

        // Cargar datos de prueba temporalmente
        cargarDatosDePrueba();

        System.out.println("Configuración completada");
    }

    public void setApp(App app) {
        this.app = app;
        this.sistemaController = app.getSistemaController();
        System.out.println("SistemaController configurado en NovedadViewController");

        // Forzar recarga cuando se configura la app
        cargarEmpleadosYServiciosEnComboBox();
        cargarNovedades();
    }

    private void configurarComboBox() {
        // Limpiar y cargar combobox
        cbTipoNovedad.getItems().clear();
        cbEmpleado.getItems().clear();
        cbServicio.getItems().clear();

        cbTipoNovedad.getItems().addAll("incidente", "asignacion", "alerta", "devolucion");

        // DEBUG
        System.out.println("ComboBox configurados:");
        System.out.println(" - Tipo Novedad: " + cbTipoNovedad.getItems());

        // Configurar los ComboBox para mostrar nombres
        configurarComboBoxEmpleados();
        configurarComboBoxServicios();

        // Cargar empleados y servicios - CORREGIDO
        cargarEmpleadosYServiciosEnComboBox();
    }

    private void configurarComboBoxEmpleados() {
        // Configurar cómo se muestran los empleados en el ComboBox
        cbEmpleado.setCellFactory(new Callback<ListView<Empleado>, ListCell<Empleado>>() {
            @Override
            public ListCell<Empleado> call(ListView<Empleado> param) {
                return new ListCell<Empleado>() {
                    @Override
                    protected void updateItem(Empleado empleado, boolean empty) {
                        super.updateItem(empleado, empty);
                        if (empty || empleado == null) {
                            setText(null);
                        } else {
                            setText(empleado.getNombre() + " - " + empleado.getDocumento() + " (" + obtenerTipoEmpleado(empleado) + ")");
                        }
                    }
                };
            }
        });

        // Configurar cómo se muestra el elemento seleccionado
        cbEmpleado.setButtonCell(new ListCell<Empleado>() {
            @Override
            protected void updateItem(Empleado empleado, boolean empty) {
                super.updateItem(empleado, empty);
                if (empty || empleado == null) {
                    setText(null);
                } else {
                    setText(empleado.getNombre() + " - " + empleado.getDocumento());
                }
            }
        });
    }

    private void configurarComboBoxServicios() {
        // Configurar cómo se muestran los servicios en el ComboBox
        cbServicio.setCellFactory(new Callback<ListView<Servicio>, ListCell<Servicio>>() {
            @Override
            public ListCell<Servicio> call(ListView<Servicio> param) {
                return new ListCell<Servicio>() {
                    @Override
                    protected void updateItem(Servicio servicio, boolean empty) {
                        super.updateItem(servicio, empty);
                        if (empty || servicio == null) {
                            setText(null);
                        } else {
                            setText(servicio.getCliente() + " - " + servicio.getCodigoContrato() + " (" + obtenerTipoServicio(servicio) + ")");
                        }
                    }
                };
            }
        });

        // Configurar cómo se muestra el elemento seleccionado
        cbServicio.setButtonCell(new ListCell<Servicio>() {
            @Override
            protected void updateItem(Servicio servicio, boolean empty) {
                super.updateItem(servicio, empty);
                if (empty || servicio == null) {
                    setText(null);
                } else {
                    setText(servicio.getCliente() + " - " + servicio.getCodigoContrato());
                }
            }
        });
    }

    private String obtenerTipoEmpleado(Empleado empleado) {
        if (empleado instanceof Vigilante) return "Vigilante";
        if (empleado instanceof Supervisor) return "Supervisor";
        if (empleado instanceof OperadorMonitoreo) return "Operador";
        return "Desconocido";
    }

    private String obtenerTipoServicio(Servicio servicio) {
        if (servicio instanceof CustodiaFija) return "Custodia Fija";
        if (servicio instanceof PatrullajeMovil) return "Patrullaje Móvil";
        if (servicio instanceof MonitoreoRemoto) return "Monitoreo Remoto";
        return "Desconocido";
    }

    private void cargarEmpleadosYServiciosEnComboBox() {
        try {
            if (sistemaController != null) {
                // Cargar empleados
                var empleados = sistemaController.obtenerTodosEmpleados();
                System.out.println("Empleados encontrados: " + empleados.size());
                cbEmpleado.getItems().clear();
                cbEmpleado.getItems().addAll(empleados);

                // Cargar servicios
                var servicios = sistemaController.getServicioController().obtenerTodosServicios();
                System.out.println("Servicios encontrados: " + servicios.size());
                cbServicio.getItems().clear();
                cbServicio.getItems().addAll(servicios);

                // DEBUG
                System.out.println("=== EMPLEADOS CARGADOS ===");
                for (Empleado emp : empleados) {
                    System.out.println(" - " + emp.getNombre() + " (" + emp.getDocumento() + ")");
                }

                System.out.println("=== SERVICIOS CARGADOS ===");
                for (Servicio serv : servicios) {
                    System.out.println(" - " + serv.getCliente() + " (" + serv.getCodigoContrato() + ")");
                }

            } else {
                System.out.println("ERROR: SistemaController es null en NovedadViewController");
                // Cargar datos de prueba
                cargarDatosDePruebaEnComboBox();
            }
        } catch (Exception e) {
            System.err.println("Error cargando datos en ComboBox: " + e.getMessage());
            e.printStackTrace();
            // Cargar datos de prueba como fallback
            cargarDatosDePruebaEnComboBox();
        }
    }

    private void cargarDatosDePruebaEnComboBox() {
        System.out.println("Cargando datos de prueba en ComboBox...");

        // Empleados de prueba
        cbEmpleado.getItems().clear();
        cbEmpleado.getItems().addAll(
                new Vigilante("Carlos López", "12345678", "día", 1500000, "Puesto-01", "no letal"),
                new Vigilante("Ana Martínez", "87654321", "noche", 1600000, "Puesto-02", "letal"),
                new Supervisor("María García", "11223344", "día", 2500000, 300000),
                new OperadorMonitoreo("Pedro Rodríguez", "44332211", "noche", 1800000, 5)
        );

        // Servicios de prueba
        cbServicio.getItems().clear();
        cbServicio.getItems().addAll(
                new CustodiaFija("CONT-001", "Empresa ABC", 5000000, "activo"),
                new PatrullajeMovil("CONT-002", "Conjunto Residencial XYZ", 3000000, "activo", 3, 150.5),
                new MonitoreoRemoto("CONT-003", "Centro Comercial Mall", 4000000, "activo", 25)
        );

        System.out.println("Datos de prueba cargados: " +
                cbEmpleado.getItems().size() + " empleados, " +
                cbServicio.getItems().size() + " servicios");
    }

    private void configurarTabla() {
        System.out.println("Configurando tabla de novedades...");

        // Configurar columnas
        colFecha.setCellValueFactory(cellData -> {
            String fecha = cellData.getValue().getFechaHora().toString();
            return new SimpleStringProperty(fecha);
        });

        colTipo.setCellValueFactory(cellData -> {
            String tipo = cellData.getValue().getTipo();
            return new SimpleStringProperty(tipo);
        });

        colDescripcion.setCellValueFactory(cellData -> {
            String descripcion = cellData.getValue().getDescripcion();
            return new SimpleStringProperty(descripcion);
        });

        colEmpleado.setCellValueFactory(cellData -> {
            Empleado emp = cellData.getValue().getEmpleado();
            return new SimpleStringProperty(emp != null ? emp.getNombre() : "N/A");
        });

        colServicio.setCellValueFactory(cellData -> {
            Servicio serv = cellData.getValue().getServicio();
            return new SimpleStringProperty(serv != null ? serv.getCliente() : "N/A");
        });

        // Asignar la lista observable a la tabla
        tblNovedades.setItems(listaNovedades);

        System.out.println("Tabla configurada con " + listaNovedades.size() + " novedades");
    }

    private void cargarNovedades() {
        try {
            if (sistemaController != null) {
                // Obtener novedades del sistema
                var novedadesDelSistema = sistemaController.getNovedadController().obtenerTodasNovedades();
                System.out.println("Novedades en sistema: " + novedadesDelSistema.size());

                // Limpiar y cargar la lista observable
                listaNovedades.clear();
                listaNovedades.addAll(novedadesDelSistema);

                // Forzar actualización de la tabla
                tblNovedades.refresh();

                System.out.println("Novedades cargadas en tabla: " + listaNovedades.size());
            }
        } catch (Exception e) {
            System.err.println("Error cargando novedades iniciales: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDatosDePrueba() {
        // Datos de prueba para verificar que la interfaz funciona
        System.out.println("Cargando datos de prueba para novedades");
    }

    // === MÉTODOS DE ACCIÓN ===

    @FXML
    void onRegistrarNovedad() {
        System.out.println("=== REGISTRANDO NUEVA NOVEDAD ===");
        registrarNovedad();
    }

    @FXML
    void onLimpiar() {
        limpiarCampos();
    }

    @FXML
    void onFiltrar() {
        // Implementar filtrado por fecha
        mostrarAlerta("Información", "Funcionalidad de filtrado en desarrollo", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onVolverMenu() {
        volverAlMenuPrincipal();
    }

    private void registrarNovedad() {
        try {
            // 1. Validar campos
            if (!validarCampos()) {
                return;
            }

            LocalDateTime fechaHora = dpFecha.getValue().atStartOfDay();
            String tipo = cbTipoNovedad.getValue();
            String descripcion = txtDescripcion.getText();
            Empleado empleado = cbEmpleado.getValue(); // Puede ser null
            Servicio servicio = cbServicio.getValue(); // Puede ser null

            System.out.println("Registrando novedad:");
            System.out.println(" - Fecha: " + fechaHora);
            System.out.println(" - Tipo: " + tipo);
            System.out.println(" - Descripción: " + descripcion);
            System.out.println(" - Empleado: " + (empleado != null ? empleado.getNombre() : "N/A"));
            System.out.println(" - Servicio: " + (servicio != null ? servicio.getCliente() : "N/A"));

            // 2. Registrar en el sistema
            if (sistemaController != null) {
                sistemaController.registrarNovedadSimple(fechaHora, tipo, descripcion, empleado, servicio);
                cargarNovedades();
            } else {
                // Lógica temporal sin sistemaController
                System.out.println("Registro en modo prueba (sin sistemaController)");
            }

            // 3. Mostrar éxito y limpiar
            mostrarAlerta("Éxito", "Novedad registrada correctamente", Alert.AlertType.INFORMATION);
            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al registrar novedad: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (dpFecha.getValue() == null) {
            mostrarAlerta("Error", "Seleccione una fecha", Alert.AlertType.ERROR);
            return false;
        }
        if (cbTipoNovedad.getValue() == null) {
            mostrarAlerta("Error", "Seleccione un tipo de novedad", Alert.AlertType.ERROR);
            return false;
        }
        if (txtDescripcion.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "Ingrese una descripción", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        dpFecha.setValue(null);
        cbTipoNovedad.setValue(null);
        txtDescripcion.clear();
        cbEmpleado.setValue(null);
        cbServicio.setValue(null);

        System.out.println("Campos de novedad limpiados");
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