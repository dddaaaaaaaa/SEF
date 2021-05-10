package controller.event;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AddEventController {
    @FXML
    private Button createButton;

    public void createButtonOnAction(ActionEvent actionEvent)
    {
        //Debug
        System.out.println("Add event button clicked!");
    }
}
