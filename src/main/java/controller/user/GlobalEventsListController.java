package controller.user;

import domain.*;
import javafx.beans.binding.When;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
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

        if(currentUser instanceof EventOrganizerUser)
        {
            AddButton.setDisable(false);
            DeleteButton.setDisable(false);
        }

        //query database

        try {
            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("SELECT * FROM \"globalEvents\";");

            ResultSet queryResult = ps.executeQuery();

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

                PersonalEvent ev = new PersonalEvent(date, eventname, extra, username, eventloc);
                TableView.getItems().add(ev);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            e.getCause();
            new Alert(Alert.AlertType.ERROR, "Querying global events failed - database error!", ButtonType.OK).showAndWait();
        }
    }

    public void setTableEvents(ObservableList<PersonalEvent> events) {
        this.events = events;
    }

    public void createAddEventStage() {
        try {
            Stage AddEventStage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddGlobalEvent.fxml"));
            Parent root = loader.load();

            AddGlobalEventController addEventController = loader.getController();
            addEventController.setTableEvents(TableView.getItems());


            AddEventStage.initStyle(StageStyle.DECORATED);
            Scene scene = new Scene(root, 400, 420);
            AddEventStage.setScene(scene);
            AddEventStage.showAndWait();
            //AddButton.setOnAction(e -> AddButton.setVisible(!AddButton.isVisible()));



            //AddButton.managedProperty().bind(AddButton.visibleProperty());
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void AddButtonOnAction(ActionEvent actionEvent) {
        createAddEventStage();
    }

    public void DeleteButtonOnAction(ActionEvent actionEvent)
    {
        ObservableList<PersonalEvent> eventSelected, allEvents;
        allEvents = TableView.getItems();
        eventSelected = TableView.getSelectionModel().getSelectedItems();

        if(eventSelected.isEmpty())
        {
            new Alert(Alert.AlertType.ERROR, "Please select events to delete!", ButtonType.OK).showAndWait();
            return;
        }

        //delete from the database
        try {

            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("DELETE from \"globalEvents\" WHERE username = ? " +
                    "AND eventname = ? AND duedate = ? AND extra = ? AND location = ?;");
            for(PersonalEvent ev : eventSelected)
            {
                if(!ev.getHost().equals(currentUser.getUsername()))
                {
                    new Alert(Alert.AlertType.ERROR, "Can't delete others' events!", ButtonType.OK).showAndWait();
                    return;
                }

                ps.setString(1, currentUser.getUsername());
                ps.setString(2, ev.getEventName());
                ps.setLong(3, ev.getDate().getTime() / 1000);
                ps.setString(4, ev.getObservations());
                ps.setString(5, ev.getLocation());

                ps.executeUpdate();
            }

            //all deletions succeeded, remove from local
            eventSelected.forEach(allEvents::remove);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            e.getCause();
            new Alert(Alert.AlertType.ERROR, "Delete failed - database error!", ButtonType.OK).showAndWait();
        }

    }

    public void AttendButtonOnAction(ActionEvent actionEvent)
    {
        ObservableList<PersonalEvent> eventSelected = TableView.getSelectionModel().getSelectedItems();

        try
        {
            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("INSERT INTO \"personalEvents\" ( \"username\", \"eventname\", \"duedate\", \"extra\", \"location\", \"host\") VALUES (?, ?, ?, ?, ?, ?);");

            for (PersonalEvent pe : eventSelected)
            {
                ps.setString(1, currentUser.getUsername());
                ps.setString(2, pe.getEventName());
                ps.setLong(3, pe.getDate().getTime() / 1000);
                ps.setString(4, pe.getObservations());
                ps.setString(5, pe.getLocation());
                ps.setString(6, pe.getHost());

                ps.executeUpdate();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            e.getCause();
            new Alert(Alert.AlertType.ERROR, "Import to local list failed - database error!", ButtonType.OK).showAndWait();
        }
    }
}
