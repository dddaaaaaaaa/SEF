package controller.user;

import domain.PersonalEvent;
import javafx.collections.ObservableList;
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
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;


public class PersonalEventsListController implements Initializable {
    @FXML
    private ImageView eventImageView;
    @FXML
    private TableColumn<PersonalEvent, String> EventColumn;
    @FXML
    private TableColumn<PersonalEvent, Date> DateColumn;
    @FXML
    private TableColumn<PersonalEvent, String> ObservationsColumn;
    @FXML
    private TableView<PersonalEvent> TableView;
    @FXML
    private Button AddButton, DeleteButton;
    protected ObservableList<PersonalEvent> events;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File eventFile = new File("src\\main\\resources\\images\\calendar.png");
        Image eventImage = new Image(eventFile.toURI().toString());
        eventImageView.setImage(eventImage);

        EventColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("eventName"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, Date>("date"));
        ObservationsColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("observations"));

        // TableView = new TableView<PersonalEvent>();
        ObservableList<PersonalEvent> tableItems;


    }

    public void setTableEvents(ObservableList<PersonalEvent> events) {
        this.events = events;
    }

    public void createAddEventStage() {
        try {
            Stage AddEventStage = new Stage();
            /*Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AddEvent.fxml"));
            AddEventStage.setResizable(false);
            */
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddEvent.fxml"));
            Parent root = loader.load();

            AddEventController addEventController = loader.getController();
            addEventController.setTableEvents(TableView.getItems());


            AddEventStage.initStyle(StageStyle.DECORATED);
            Scene scene = new Scene(root, 400, 400);
            AddEventStage.setScene(scene);
            AddEventStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void addTreeTableItem(ObservableList<PersonalEvent> events) throws ParseException {
        //Some loading from the database needed here in order to keep the events
        //events = TableView.getItems();
        TableView.setItems(events);

    }

    public void AddButtonOnAction(javafx.event.ActionEvent actionEvent) {
        createAddEventStage();
    }

    //Delete button on Action
    public void DeleteButtonOnAction(javafx.event.ActionEvent actionEvent) {
        //deletion from database also needed
        ObservableList<PersonalEvent> eventSelected, allEvents;
        allEvents = TableView.getItems();
        eventSelected = TableView.getSelectionModel().getSelectedItems();

        eventSelected.forEach(allEvents::remove);


    }
}