module co.edu.uniquindio.poo.segurcol {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.uniquindio.poo.segurcol to javafx.fxml;
    opens co.edu.uniquindio.poo.segurcol.viewcontroller to javafx.fxml;
    opens co.edu.uniquindio.poo.segurcol.controller to javafx.fxml;
    opens co.edu.uniquindio.poo.segurcol.model to javafx.fxml;

    exports co.edu.uniquindio.poo.segurcol;
    exports co.edu.uniquindio.poo.segurcol.viewcontroller;
    exports co.edu.uniquindio.poo.segurcol.controller;
    exports co.edu.uniquindio.poo.segurcol.model;
}