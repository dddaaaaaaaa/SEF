package controller.user;

import domain.PersonalEvent;
import domain.UserViewInterface;
import javafx.beans.binding.When;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class GlobalEventsListController extends UserViewInterface implements Initializable {
    @FXML
    private ImageView eventImageView;
    @FXML
    private TableColumn<PersonalEvent, String> EventColumn;
    @FXML
    private TableColumn<PersonalEvent, Date> DateColumn;
    @FXML
    private TableColumn<PersonalEvent, String> ObservationsColumn, LocationColumn, HostColumn;
    @FXML
    private javafx.scene.control.TableView<PersonalEvent> TableView;
    @FXML
    private Button AddButton, DeleteButton;
    protected static ObservableList<PersonalEvent> events;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        File globalFile = new File("src\\main\\resources\\images\\calendar.png");
        Image globalImage = new Image(globalFile.toURI().toString());
        eventImageView.setImage(globalImage);

        EventColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("eventName"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, Date>("date"));
        ObservationsColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("observations"));
        HostColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("host"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("location"));

        TableView.setEditable(true);


    }
    public void setTableEvents(ObservableList<PersonalEvent> events) {
        this.events = events;
    }

    public void createAddEventStage() {
        try {
            Stage AddEventStage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddEvent.fxml"));
            Parent root = loader.load();

            AddEventController addEventController = loader.getController();
            addEventController.setTableEvents(TableView.getItems());


            AddEventStage.initStyle(StageStyle.DECORATED);
            Scene scene = new Scene(root, 400, 400);
            AddEventStage.setScene(scene);
            AddEventStage.showAndWait();
            AddButton.setOnAction(e -> AddButton.setVisible(!AddButton.isVisible()));

            if(user.user.equals("Basic User"))
                AddButton.textProperty().bind(
                        new When(AddButton.visibleProperty()).then("Invisible").otherwise(
                                "Visible"));

            AddButton.managedProperty().bind(AddButton.visibleProperty());
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    public void AddButtonOnAction(ActionEvent actionEvent) {
        createAddEventStage();
    }

    public void DeleteButtonOnAction(ActionEvent actionEvent) {
        //deletion from database also needed
        ObservableList<PersonalEvent> eventSelected, allEvents;
        allEvents = TableView.getItems();
        eventSelected = TableView.getSelectionModel().getSelectedItems();
        eventSelected.forEach(allEvents::remove);

    }
}
