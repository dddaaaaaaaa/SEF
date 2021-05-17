package controller.user;

import domain.PersonalEvent;
import domain.User;
import domain.UserHolder;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
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
    private Button AddButton, DeleteButton, AttendButton;
    protected static ObservableList<PersonalEvent> events;
    private User currentUser;


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
        UserHolder userHolder;
        userHolder = UserHolder.getInstance();
        currentUser = userHolder.getUser();

        if (currentUser.getUser().equals("Basic User")) {
            AddButton.setVisible(false);
            DeleteButton.setVisible(false);
        }
        if(currentUser.getUser().equals("Event Organizer User"))
        {
            AttendButton.setVisible(false);
        }
       // TableView.setEditable(true);


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

    public void AttendButtonOnAction(ActionEvent actionEvent) throws IOException {
        ObservableList<PersonalEvent> eventSelected;
        eventSelected = TableView.getSelectionModel().getSelectedItems();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/PersonalEventsList.fxml"));
        ObservableList<PersonalEvent> event = eventSelected;

        //events.add((PersonalEvent) event);
    }
}
