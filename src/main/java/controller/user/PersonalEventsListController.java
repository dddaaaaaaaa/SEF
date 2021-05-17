package controller.user;

import domain.*;
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
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;


public class PersonalEventsListController extends UserViewInterface implements Initializable {
    @FXML
    private ImageView eventImageView;
    @FXML
    private TableColumn<PersonalEvent, String> EventColumn;
    @FXML
    private TableColumn<PersonalEvent, Date> DateColumn;
    @FXML
    private TableColumn<PersonalEvent, String> ObservationsColumn, LocationColumn, HostColumn;
    @FXML
    private TableView<PersonalEvent> TableView;
    @FXML
    private Button AddButton, AddRelativeButton, DeleteButton;
    protected static ObservableList<PersonalEvent> events;
    private User currentUser;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File eventFile = new File("src\\main\\resources\\images\\calendar.png");
        Image eventImage = new Image(eventFile.toURI().toString());
        eventImageView.setImage(eventImage);
        EventColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("eventName"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, Date>("date"));
        ObservationsColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("observations"));
        HostColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("host"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("location"));

       // TableView.setEditable(true);
        UserHolder userHolder;
        userHolder = UserHolder.getInstance();
        currentUser = userHolder.getUser();

       // System.out.println(user.username);
        //query database
        String queryString = "SELECT * FROM \"personalEvents\"";    //TODO query only my events

        try {
            Connection connectDB = new DatabaseConnection().getConnection();
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(queryString);

            //data available here
            while (queryResult.next())
            {
                String username = queryResult.getString(2);
                String eventname = queryResult.getString(3);
                long duedate = queryResult.getLong(4);
                String extra = queryResult.getString(5);
                String eventloc = queryResult.getString(6);

                Date date = new Date();
                date.setTime(duedate * 1000);

                System.out.println("Adding event " + eventname + " happening at " + duedate);
                PersonalEvent ev = new PersonalEvent(date, eventname, extra, username, eventloc);
                TableView.getItems().add(ev);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            e.getCause();
            System.out.println("Querying user events failed!");
        }
    }

    public void setTableEvents(ObservableList<PersonalEvent> events) {
        this.events = events;
    }


    public void ImportGlobalEvents() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/GlobalListEvents.fxml"));
        Parent root = loader.load();

        GlobalEventsListController globalEventsListController = loader.getController();
        globalEventsListController.setTableEvents(TableView.getItems());
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

    //create button on action
    public void AddButtonOnAction(javafx.event.ActionEvent actionEvent)
    {
        createAddEventStage();
    }


    //create relative button on action
    public void AddRelativeButtonOnAction(javafx.event.ActionEvent actionEvent)
    {
        try {
            Stage addEventStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddRelativeEvent.fxml"));
            Parent root = loader.load();

            AddRelativeEventController addRelativeEventController = loader.getController();
            addRelativeEventController.setTableEvents(TableView.getItems());

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

    //Delete button on Action
    public void DeleteButtonOnAction(javafx.event.ActionEvent actionEvent) {
        //deletion from database also needed
        ObservableList<PersonalEvent> eventSelected, allEvents;
        allEvents = TableView.getItems();
        eventSelected = TableView.getSelectionModel().getSelectedItems();
        eventSelected.forEach(allEvents::remove);


    }
}
