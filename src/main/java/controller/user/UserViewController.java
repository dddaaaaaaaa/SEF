package controller.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class UserViewController implements Initializable {
    @FXML
    private ListView<String> UserListView;

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
