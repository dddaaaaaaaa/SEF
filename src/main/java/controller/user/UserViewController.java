package controller.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class UserViewController extends FxmlLoader implements Initializable {
    @FXML
    private ListView<String> UserListView;
    @FXML
    private BorderPane mainUserPane;

    ObservableList<String> List = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            LoadIntoUserList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UserListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                FxmlLoader object = new FxmlLoader();
                Pane view = new Pane();
                System.out.println("This is " + newValue + "\n");
                switch (newValue) {
                    case "Personal Events":
                        view = object.getPage("PersonalEventsList");
                        break;
                    case "Settings":
                        view = object.getPage("Settings");
                        break;
                    case "Global Events":
                        view = object.getPage("GlobalEventsList");
                        break;
                    default:
                        System.out.println("Fxml loader cannot find file!");
                }
                System.out.println("Ceva!");
                mainUserPane.setPrefSize(325,284);
                mainUserPane.setCenter(view);
                //mainUserPane.
            }
        });
    }

    public void LoadIntoUserList() throws InterruptedException {
        List.add("Global Events");
        List.add("Personal Events");
        List.add("Settings");
        UserListView.getItems().addAll(List);
    }

    public void showListStage() {


    }
}
