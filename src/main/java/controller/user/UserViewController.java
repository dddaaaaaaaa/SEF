package controller.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class UserViewController implements Initializable {
    @FXML
    private ListView<String> UserListView;

    @FXML
    private Button createNewButton;

    public void createNewButtonOnAction(ActionEvent actionEvent)
    {
        //quick debug print
        System.out.println("DEBUG (uview controller line 31) - button press, adding new event");

        //spawn new scene
        try {
            Stage addEventStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AddEvent.fxml"));
            addEventStage.setResizable(false);
            addEventStage.initStyle(StageStyle.DECORATED);
            //addEventStage.setScene(new Scene(root, 500, 400));
            addEventStage.setScene(new Scene(root));
            addEventStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    ObservableList<String> List = FXCollections.observableArrayList();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try
        {
            UserListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
            {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
                {
                    switch (newValue)
                    {

                    }
                }
            });

            LoadIntoUserList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void LoadIntoUserList() throws InterruptedException {
        List.add("Global Events");
        List.add("Personal Events");
        List.add("Account Info");
        UserListView.getItems().addAll(List);
    }
}