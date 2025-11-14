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
import java.time.LocalDate;

public class AgendaViewController {

    private SistemaController sistemaController;
    private ObservableList<AgendaItem> listaAgenda;

    // Campos de entrada
    @FXML private DatePicker dpFecha;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<Empleado> cbEmpleado;
    @FXML private ComboBox<Servicio> cbServicio;
    @FXML private ComboBox<String> cbTipoEvento;

    // Tabla y columnas
    @FXML private TableView<AgendaItem> tblAgenda;
    @FXML private TableColumn<AgendaItem, String> colFecha;
    @FXML private TableColumn<AgendaItem, String> colDescripcion;
    @FXML private TableColumn<AgendaItem, String> colEmpleado;
    @FXML private TableColumn<AgendaItem, String> colServicio;
    @FXML private TableColumn<AgendaItem, String> colTipoEvento;

    // Botones
    @FXML private Button btnProgramar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnFiltrar;
    @FXML private Button btnVolverMenu;

    private App app;

    public AgendaViewController() {
        // Constructor vacío requerido por FXML
    }

    @FXML
    void initialize() {
        System.out.println("=== INICIALIZANDO AGENDA VIEW CONTROLLER ===");

        // Inicializar la lista observable
        listaAgenda = FXCollections.observableArrayList();

        configurarComboBox();
        configurarTabla();

        // Cargar datos de prueba temporalmente
        cargarDatosDePrueba();

        System.out.println("Agenda configurada correctamente");
    }

    public void setApp(App app) {
        this.app = app;
        this.sistemaController = app.getSistemaController();
        System.out.println("SistemaController configurado en AgendaViewController");

        // Forzar recarga cuando se configura la app
        cargarEmpleadosYServiciosEnComboBox();
        cargarAgenda();
    }

    private void configurarComboBox() {
        // Limpiar y cargar combobox
        cbTipoEvento.getItems().clear();
        cbEmpleado.getItems().clear();
        cbServicio.getItems().clear();

        cbTipoEvento.getItems().addAll("turno", "ronda", "chequeo", "custodia");

        // DEBUG
        System.out.println("ComboBox configurados:");
        System.out.println(" - Tipo Evento: " + cbTipoEvento.getItems());

        // Configurar los ComboBox para mostrar nombres
        configurarComboBoxEmpleados();
        configurarComboBoxServicios();

        // Cargar empleados y servicios si el sistemaController está disponible
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
                System.out.println("=== EMPLEADOS CARGADOS EN AGENDA ===");
                for (Empleado emp : empleados) {
                    System.out.println(" - " + emp.getNombre() + " (" + emp.getDocumento() + ")");
                }

                System.out.println("=== SERVICIOS CARGADOS EN AGENDA ===");
                for (Servicio serv : servicios) {
                    System.out.println(" - " + serv.getCliente() + " (" + serv.getCodigoContrato() + ")");
                }

            } else {
                System.out.println("ERROR: SistemaController es null en AgendaViewController");
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
        System.out.println("Configurando tabla de agenda...");

        // Configurar columnas
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

        System.out.println("Tabla configurada con " + listaAgenda.size() + " eventos");
    }

    private void cargarAgenda() {
        if (listaAgenda != null && sistemaController != null) {
            listaAgenda.clear();
            listaAgenda.addAll(sistemaController.getAgendaController().obtenerTodaAgenda());
            System.out.println("Agenda cargada: " + listaAgenda.size() + " eventos");
        }
    }

    private void cargarDatosDePrueba() {
        // Datos de prueba temporalmente
        System.out.println("Cargando datos de prueba para agenda");
    }

    // === MÉTODOS DE ACCIÓN ===

    @FXML
    void onProgramarEvento() {
        System.out.println("=== PROGRAMANDO NUEVO EVENTO ===");
        programarEvento();
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

    private void programarEvento() {
        try {
            if (!validarCampos()) {
                mostrarAlerta("Error", "Complete todos los campos obligatorios", Alert.AlertType.ERROR);
                return;
            }

            LocalDate fecha = dpFecha.getValue();
            String descripcion = txtDescripcion.getText();
            Empleado empleado = cbEmpleado.getValue(); // Puede ser null
            Servicio servicio = cbServicio.getValue(); // Puede ser null
            String tipoEvento = cbTipoEvento.getValue();

            System.out.println("Programando evento:");
            System.out.println(" - Fecha: " + fecha);
            System.out.println(" - Descripción: " + descripcion);
            System.out.println(" - Empleado: " + (empleado != null ? empleado.getNombre() : "N/A"));
            System.out.println(" - Servicio: " + (servicio != null ? servicio.getCliente() : "N/A"));
            System.out.println(" - Tipo Evento: " + tipoEvento);

            // Si hay sistemaController, usar la lógica del sistema
            if (sistemaController != null) {
                sistemaController.programarEvento(fecha, descripcion, empleado, servicio, tipoEvento);
                cargarAgenda();
            } else {
                // Lógica temporal sin sistemaController
                mostrarAlerta("Éxito", "Evento programado correctamente (modo prueba)", Alert.AlertType.INFORMATION);
            }

            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al programar evento: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (dpFecha.getValue() == null) {
            mostrarAlerta("Error", "Seleccione una fecha", Alert.AlertType.ERROR);
            return false;
        }
        if (txtDescripcion.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "Ingrese una descripción", Alert.AlertType.ERROR);
            return false;
        }
        if (cbTipoEvento.getValue() == null) {
            mostrarAlerta("Error", "Seleccione un tipo de evento", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        dpFecha.setValue(null);
        txtDescripcion.clear();
        cbEmpleado.setValue(null);
        cbServicio.setValue(null);
        cbTipoEvento.setValue(null);
        System.out.println("Campos de agenda limpiados");
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