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
import javafx.util.Callback;

public class EquipoViewController {

    private SistemaController sistemaController;
    private ObservableList<Equipo> listaEquipos;
    private ObservableList<Empleado> listaEmpleados;
    private Equipo equipoSeleccionado;

    // Campos de entrada
    @FXML private TextField txtCodigo;
    @FXML private ComboBox<String> cbTipo;
    @FXML private ComboBox<String> cbEstado;
    @FXML private TextField txtValorReposicion;
    @FXML private ComboBox<Empleado> cbEmpleadoAsignar;

    // Tabla y columnas
    @FXML private TableView<Equipo> tblEquipos;
    @FXML private TableColumn<Equipo, String> colCodigo;
    @FXML private TableColumn<Equipo, String> colTipo;
    @FXML private TableColumn<Equipo, String> colEstado;
    @FXML private TableColumn<Equipo, String> colValor;
    @FXML private TableColumn<Equipo, String> colAsignadoA;

    // Botones
    @FXML private Button btnAgregar;
    @FXML private Button btnAsignar;
    @FXML private Button btnRetirar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnGenerarInforme;
    @FXML private Button btnVolverMenu;

    private App app;

    public EquipoViewController() {
        // Constructor vacío requerido por FXML
    }

    @FXML
    void initialize() {
        System.out.println("=== INICIALIZANDO EQUIPO VIEW CONTROLLER ===");

        // Inicializar las listas observables
        listaEquipos = FXCollections.observableArrayList();
        listaEmpleados = FXCollections.observableArrayList();

        configurarComboBox();
        configurarTabla();
        configurarListeners();

        // Cargar datos de prueba temporalmente
        cargarDatosDePrueba();

        System.out.println("Configuración completada");
    }

    public void setApp(App app) {
        this.app = app;
        this.sistemaController = app.getSistemaController();
        System.out.println("SistemaController configurado en EquipoViewController");

        // Forzar recarga de empleados cuando se configura la app
        cargarEmpleadosEnComboBox();
        cargarDatos();
    }

    private void configurarComboBox() {
        // Limpiar y cargar combobox
        cbTipo.getItems().clear();
        cbEstado.getItems().clear();
        cbEmpleadoAsignar.getItems().clear();

        cbTipo.getItems().addAll("radio", "arma", "vehículo", "uniforme");
        cbEstado.getItems().addAll("activo", "mantenimiento", "retirado");

        // DEBUG
        System.out.println("ComboBox configurados:");
        System.out.println(" - Tipo: " + cbTipo.getItems());
        System.out.println(" - Estado: " + cbEstado.getItems());

        // Configurar el ComboBox de empleados para mostrar nombres
        configurarComboBoxEmpleados();

        // Cargar empleados - CORREGIDO: Usar directamente del sistemaController
        cargarEmpleadosEnComboBox();
    }

    private void configurarComboBoxEmpleados() {
        // Configurar cómo se muestran los empleados en el ComboBox
        cbEmpleadoAsignar.setCellFactory(new Callback<ListView<Empleado>, ListCell<Empleado>>() {
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
        cbEmpleadoAsignar.setButtonCell(new ListCell<Empleado>() {
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

    private void cargarEmpleadosEnComboBox() {
        try {
            if (sistemaController != null) {
                // Obtener empleados directamente del sistema
                var empleados = sistemaController.obtenerTodosEmpleados();
                System.out.println("Empleados encontrados en sistema: " + empleados.size());

                cbEmpleadoAsignar.getItems().clear();
                cbEmpleadoAsignar.getItems().addAll(empleados);

                // DEBUG: Mostrar empleados cargados
                System.out.println("=== EMPLEADOS CARGADOS EN COMBOBOX ===");
                for (Empleado emp : empleados) {
                    System.out.println(" - " + emp.getNombre() + " (" + emp.getDocumento() + ") - " + obtenerTipoEmpleado(emp));
                }
            } else {
                System.out.println("ERROR: SistemaController es null en EquipoViewController");
                // Cargar datos de prueba en el ComboBox
                cargarEmpleadosDePruebaEnComboBox();
            }
        } catch (Exception e) {
            System.err.println("Error cargando empleados en ComboBox: " + e.getMessage());
            e.printStackTrace();
            // Cargar datos de prueba como fallback
            cargarEmpleadosDePruebaEnComboBox();
        }
    }

    private void cargarEmpleadosDePruebaEnComboBox() {
        System.out.println("Cargando empleados de prueba en ComboBox...");
        cbEmpleadoAsignar.getItems().clear();

        // Crear empleados de prueba
        Empleado vigilante1 = new Vigilante("Carlos López", "12345678", "día", 1500000, "Puesto-01", "no letal");
        Empleado vigilante2 = new Vigilante("Ana Martínez", "87654321", "noche", 1600000, "Puesto-02", "letal");
        Empleado supervisor1 = new Supervisor("María García", "11223344", "día", 2500000, 300000);
        Empleado operador1 = new OperadorMonitoreo("Pedro Rodríguez", "44332211", "noche", 1800000, 5);

        cbEmpleadoAsignar.getItems().addAll(vigilante1, vigilante2, supervisor1, operador1);
        System.out.println("Empleados de prueba cargados: " + cbEmpleadoAsignar.getItems().size());
    }

    private void configurarTabla() {
        System.out.println("Configurando tabla de equipos...");

        // Configurar columnas
        colCodigo.setCellValueFactory(cellData -> {
            String codigo = cellData.getValue().getCodigo();
            return new SimpleStringProperty(codigo);
        });

        colTipo.setCellValueFactory(cellData -> {
            String tipo = cellData.getValue().getTipo();
            return new SimpleStringProperty(tipo);
        });

        colEstado.setCellValueFactory(cellData -> {
            String estado = cellData.getValue().getEstado();
            return new SimpleStringProperty(estado);
        });

        colValor.setCellValueFactory(cellData -> {
            double valor = cellData.getValue().getValorReposicion();
            return new SimpleStringProperty(String.format("$%,.0f", valor));
        });

        colAsignadoA.setCellValueFactory(cellData -> {
            String asignadoA = obtenerEmpleadoAsignado(cellData.getValue());
            return new SimpleStringProperty(asignadoA);
        });

        // Asignar la lista observable a la tabla
        tblEquipos.setItems(listaEquipos);

        System.out.println("Tabla configurada con " + listaEquipos.size() + " equipos");
    }

    private void configurarListeners() {
        tblEquipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            equipoSeleccionado = newSelection;
            if (newSelection != null) {
                mostrarInformacionEquipo(newSelection);
            }
        });
    }

    private void cargarDatos() {
        try {
            if (sistemaController != null) {
                // Obtener equipos del sistema
                var equiposDelSistema = sistemaController.getEquipoController().obtenerTodosEquipos();
                System.out.println("Equipos en sistema: " + equiposDelSistema.size());

                // Limpiar y cargar la lista observable
                listaEquipos.clear();
                listaEquipos.addAll(equiposDelSistema);

                // Forzar actualización de la tabla
                tblEquipos.refresh();

                System.out.println("Equipos cargados en tabla: " + listaEquipos.size());
            }
        } catch (Exception e) {
            System.err.println("Error cargando equipos iniciales: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDatosDePrueba() {
        // Datos de prueba para verificar que la interfaz funciona
        listaEquipos.add(new Equipo("EQ-001", "radio", "activo", 500000));
        listaEquipos.add(new Equipo("EQ-002", "arma", "activo", 1200000));
        listaEquipos.add(new Equipo("EQ-003", "vehículo", "mantenimiento", 45000000));
        listaEquipos.add(new Equipo("EQ-004", "uniforme", "activo", 150000));

        System.out.println("Datos de prueba cargados: " + listaEquipos.size() + " equipos");
    }

    private String obtenerEmpleadoAsignado(Equipo equipo) {
        // Lógica temporal para datos de prueba
        if (sistemaController != null) {
            for (Empleado empleado : cbEmpleadoAsignar.getItems()) {
                if (sistemaController.getEquipoController().obtenerEquiposPorEmpleado(empleado).contains(equipo)) {
                    return empleado.getNombre() + " (" + obtenerTipoEmpleado(empleado) + ")";
                }
            }
        }
        return "No asignado";
    }

    // === MÉTODOS DE ACCIÓN ===

    @FXML
    void onAgregarEquipo() {
        System.out.println("=== AGREGANDO NUEVO EQUIPO ===");
        agregarEquipo();
    }

    @FXML
    void onAsignarEquipo() {
        System.out.println("=== ASIGNANDO EQUIPO ===");
        asignarEquipo();
    }

    @FXML
    void onRetirarEquipo() {
        System.out.println("=== RETIRANDO EQUIPO ===");
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
    void onVolverMenu() {
        volverAlMenuPrincipal();
    }

    private void agregarEquipo() {
        try {
            // 1. Validar campos
            if (!validarCampos()) {
                mostrarAlerta("Error", "Complete todos los campos obligatorios", Alert.AlertType.ERROR);
                return;
            }

            // 2. Construir equipo
            Equipo nuevoEquipo = construirEquipo();
            if (nuevoEquipo == null) {
                mostrarAlerta("Error", "No se pudo crear el equipo", Alert.AlertType.ERROR);
                return;
            }

            System.out.println("Equipo creado: " + nuevoEquipo.getCodigo() + " - " + nuevoEquipo.getTipo());

            // 3. Registrar en el sistema
            if (sistemaController != null && sistemaController.registrarEquipo(nuevoEquipo)) {
                // 4. AGREGAR DIRECTAMENTE A LA LISTA OBSERVABLE
                listaEquipos.add(nuevoEquipo);

                // 5. FORZAR ACTUALIZACIÓN VISUAL
                tblEquipos.refresh();

                System.out.println("Equipo agregado a la tabla. Total: " + listaEquipos.size());

                // 6. Mostrar éxito y limpiar
                mostrarAlerta("Éxito", "Equipo agregado correctamente", Alert.AlertType.INFORMATION);
                limpiarCampos();

            } else {
                // Si no hay sistemaController, agregar directamente a la lista
                listaEquipos.add(nuevoEquipo);
                tblEquipos.refresh();
                mostrarAlerta("Éxito", "Equipo agregado correctamente", Alert.AlertType.INFORMATION);
                limpiarCampos();
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Verifique el valor de reposición (debe ser numérico)", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        return !txtCodigo.getText().trim().isEmpty() &&
                cbTipo.getValue() != null &&
                cbEstado.getValue() != null &&
                !txtValorReposicion.getText().trim().isEmpty();
    }

    private void asignarEquipo() {
        if (equipoSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione un equipo de la tabla", Alert.AlertType.ERROR);
            return;
        }

        if (cbEmpleadoAsignar.getValue() == null) {
            mostrarAlerta("Error", "Seleccione un empleado para asignar", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Lógica de asignación
            if (sistemaController != null) {
                if (sistemaController.asignarEquipoAEmpleado(cbEmpleadoAsignar.getValue(), equipoSeleccionado)) {
                    tblEquipos.refresh();
                    mostrarAlerta("Éxito",
                            "Equipo '" + equipoSeleccionado.getCodigo() + "' asignado correctamente a " +
                                    cbEmpleadoAsignar.getValue().getNombre(),
                            Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Error", "No se pudo asignar el equipo", Alert.AlertType.ERROR);
                }
            } else {
                // Lógica temporal sin sistemaController
                tblEquipos.refresh();
                mostrarAlerta("Éxito",
                        "Equipo asignado correctamente (modo prueba) a " +
                                cbEmpleadoAsignar.getValue().getNombre(),
                        Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al asignar equipo: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void retirarEquipo() {
        if (equipoSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione un equipo de la tabla", Alert.AlertType.ERROR);
            return;
        }

        if (cbEmpleadoAsignar.getValue() == null) {
            mostrarAlerta("Error", "Seleccione un empleado para retirar el equipo", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Lógica de retiro
            if (sistemaController != null) {
                if (sistemaController.getEquipoController().retirarEquipoDeEmpleado(cbEmpleadoAsignar.getValue(), equipoSeleccionado)) {
                    tblEquipos.refresh();
                    mostrarAlerta("Éxito",
                            "Equipo '" + equipoSeleccionado.getCodigo() + "' retirado correctamente de " +
                                    cbEmpleadoAsignar.getValue().getNombre(),
                            Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Error", "No se pudo retirar el equipo", Alert.AlertType.ERROR);
                }
            } else {
                // Lógica temporal sin sistemaController
                tblEquipos.refresh();
                mostrarAlerta("Éxito",
                        "Equipo retirado correctamente (modo prueba) de " +
                                cbEmpleadoAsignar.getValue().getNombre(),
                        Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al retirar equipo: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void generarInformeDotacion() {
        if (cbEmpleadoAsignar.getValue() == null) {
            mostrarAlerta("Error", "Seleccione un empleado para generar el informe", Alert.AlertType.ERROR);
            return;
        }

        try {
            String informe;
            Empleado empleado = cbEmpleadoAsignar.getValue();

            if (sistemaController != null) {
                informe = sistemaController.generarInformeDotacionEmpleado(empleado);
            } else {
                // Informe de prueba
                informe = "=== INFORME DE DOTACIÓN (MODO PRUEBA) ===\n\n" +
                        "Empleado: " + empleado.getNombre() + "\n" +
                        "Documento: " + empleado.getDocumento() + "\n" +
                        "Tipo: " + obtenerTipoEmpleado(empleado) + "\n" +
                        "Turno: " + empleado.getTurno() + "\n\n" +
                        "EQUIPOS ASIGNADOS:\n" +
                        "- Radio comunicador (EQ-001) - $500,000\n" +
                        "- Arma de servicio (EQ-002) - $1,200,000\n" +
                        "- Uniforme corporativo (EQ-004) - $150,000\n\n" +
                        "Total equipos: 3\n" +
                        "Valor total dotación: $1,850,000\n\n" +
                        "Fecha generación: " + java.time.LocalDate.now();
            }

            TextArea textArea = new TextArea(informe);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefWidth(600);
            textArea.setPrefHeight(400);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informe de Dotación");
            alert.setHeaderText("Dotación de " + empleado.getNombre());
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al generar informe: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private String obtenerTipoEmpleado(Empleado empleado) {
        if (empleado instanceof Vigilante) return "Vigilante";
        if (empleado instanceof Supervisor) return "Supervisor";
        if (empleado instanceof OperadorMonitoreo) return "Operador de Monitoreo";
        return "Desconocido";
    }

    private void mostrarInformacionEquipo(Equipo equipo) {
        if (equipo != null) {
            txtCodigo.setText(equipo.getCodigo());
            cbTipo.setValue(equipo.getTipo());
            cbEstado.setValue(equipo.getEstado());
            txtValorReposicion.setText(String.valueOf(equipo.getValorReposicion()));
            System.out.println("Información del equipo cargada: " + equipo.getCodigo());
        }
    }

    private Equipo construirEquipo() {
        return new Equipo(
                txtCodigo.getText().trim(),
                cbTipo.getValue(),
                cbEstado.getValue(),
                Double.parseDouble(txtValorReposicion.getText().trim())
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

        System.out.println("Campos de equipo limpiados");
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