package controller.user;

import domain.PersonalEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class PersonalEventsListController implements Initializable {
    @FXML
    private ImageView eventImageView;
    @FXML
    private TreeTableColumn EventColumn;
    @FXML
    private TreeTableColumn DateColumn;
    @FXML
    private TreeTableColumn ObservationsColumn;
    @FXML
    private TreeTableView treeTableView;
    ObservableList<PersonalEvent> data;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File eventFile = new File("src\\main\\resources\\images\\sigla.png");
        Image eventImage = new Image(eventFile.toURI().toString());
        eventImageView.setImage(eventImage);

        data = FXCollections.observableArrayList(
                new PersonalEvent("02.10.2021", "Raul b-day", "Celebration needed!")
        );
        addTreeTableItem();
    }


    public void addTreeTableItem() {

        EventColumn.setCellFactory(
                new TreeItemPropertyValueFactory<PersonalEvent, String>("eventName")
        );
        DateColumn.setCellFactory(
                new TreeItemPropertyValueFactory<PersonalEvent, String>("date")
        );
        ObservationsColumn.setCellFactory(
                new TreeItemPropertyValueFactory<PersonalEvent, String>("observations")
        );

        TreeItem<PersonalEvent> root = new TreeItem<>(new PersonalEvent("02.10.2021", "Raul b-day", "Celebration needed!"));
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);
    }
}
