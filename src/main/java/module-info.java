module co.edu.uniquindio.poo.segurcol {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.poo.segurcol to javafx.fxml;
    exports co.edu.uniquindio.poo.segurcol;
}