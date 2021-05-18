package controller.user;

import domain.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class PersonalEventsListController extends UserViewInterface implements Initializable {
    //@FXML
    //private ImageView eventImageView, NotificationImageView;
    @FXML
    private TableColumn<PersonalEvent, String> EventColumn;
    @FXML
    private TableColumn<PersonalEvent, Date> DateColumn;
    @FXML
    private TableColumn<PersonalEvent, String> ObservationsColumn, LocationColumn, HostColumn;
    @FXML
    private TableView<PersonalEvent> TableView;
    @FXML
    private Button AddButton, AddRelativeButton, DeleteButton, PushButton, SetNotificationButton;
    protected static ObservableList<PersonalEvent> events;
    private User currentUser;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //File eventFile = new File("src\\main\\resources\\images\\calendar.png");
        //Image eventImage = new Image(eventFile.toURI().toString());
        //eventImageView.setImage(eventImage);

        //File file = new File("src\\main\\resources\\images\\notification.png");
        //Image image = new Image(file.toURI().toString());
       // NotificationImageView.setImage(image);
        //SetNotificationButton.setGraphic(NotificationImageView);

        //eventImageView.setImage(eventImage);
        EventColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("eventName"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, Date>("date"));
        ObservationsColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("observations"));
        HostColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("host"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<PersonalEvent, String>("location"));

        //get ref to user
        UserHolder userHolder;
        userHolder = UserHolder.getInstance();
        currentUser = userHolder.getUser();

        if (currentUser instanceof EventOrganizerUser) {
            System.out.println("User is event organizer!");
            PushButton.setDisable(false);
        }

        //query database;
        String myusername = currentUser.getUsername();
        String queryString = "SELECT * FROM \"personalEvents\" WHERE username = '" + myusername + "';";

        try {
            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("SELECT * FROM \"personalEvents\" WHERE username = ?;");
            ps.setString(1, currentUser.getUsername());

            ResultSet queryResult = ps.executeQuery();

            //data available here
            while (queryResult.next()) {
                String username = queryResult.getString(2);
                String eventname = queryResult.getString(3);
                long duedate = queryResult.getLong(4);
                String extra = queryResult.getString(5);
                String eventloc = queryResult.getString(6);
                String hostname = queryResult.getString(7);

                if (username.equals(hostname)) {
                    hostname += " (Myself)";
                }
                Date date = new Date();
                date.setTime(duedate * 1000);

                //System.out.println("Adding event " + eventname + " happening at " + duedate);
                PersonalEvent ev = new PersonalEvent(date, eventname, extra, hostname, eventloc);
                TableView.getItems().add(ev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
            System.out.println("Querying user events failed!");
        }
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
            Scene scene = new Scene(root, 400, 420);
            AddEventStage.setScene(scene);
            AddEventStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    //create button on action
    public void AddButtonOnAction(javafx.event.ActionEvent actionEvent) {
        createAddEventStage();
    }


    //create relative button on action
    public void AddRelativeButtonOnAction(javafx.event.ActionEvent actionEvent) {
        try {
            Stage addEventStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddRelativeEvent.fxml"));
            Parent root = loader.load();

            AddRelativeEventController addRelativeEventController = loader.getController();
            addRelativeEventController.setTableEvents(TableView.getItems());

            addEventStage.setResizable(false);
            addEventStage.initStyle(StageStyle.DECORATED);
            addEventStage.setScene(new Scene(root));
            addEventStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    //Delete button on Action
    public void DeleteButtonOnAction(javafx.event.ActionEvent actionEvent) {
        ObservableList<PersonalEvent> eventSelected, allEvents;
        allEvents = TableView.getItems();
        eventSelected = TableView.getSelectionModel().getSelectedItems();

        if (eventSelected.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select events to delete!", ButtonType.OK).showAndWait();
            return;
        }

        //delete from the database
        try {

            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("DELETE from \"personalEvents\" WHERE username = ? " +
                    "AND eventname = ? AND duedate = ? AND extra = ? AND location = ?;");
            for (PersonalEvent ev : eventSelected) {
                ps.setString(1, currentUser.getUsername());
                ps.setString(2, ev.getEventName());
                ps.setLong(3, ev.getDate().getTime() / 1000);
                ps.setString(4, ev.getObservations());
                ps.setString(5, ev.getLocation());

                ps.executeUpdate();
            }

            //all deletions succeeded, remove from local
            eventSelected.forEach(allEvents::remove);
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
            new Alert(Alert.AlertType.ERROR, "Delete failed - database error!", ButtonType.OK).showAndWait();
        }
    }

    //push button on action - only for event organizers!
    public void PushButtonOnAction(javafx.event.ActionEvent actionEvent) {
        ObservableList<PersonalEvent> eventSelected = TableView.getSelectionModel().getSelectedItems();

        try {
            Connection connectDB = new DatabaseConnection().getConnection();
            PreparedStatement ps = connectDB.prepareStatement("INSERT INTO \"globalEvents\" ( \"username\", \"eventname\", \"duedate\", \"extra\", \"location\") VALUES (?, ?, ?, ?, ?);");

            for (PersonalEvent pe : eventSelected) {
                ps.setString(1, currentUser.getUsername());
                ps.setString(2, pe.getEventName());
                ps.setLong(3, pe.getDate().getTime() / 1000);
                ps.setString(4, pe.getObservations());
                ps.setString(5, pe.getLocation());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
            new Alert(Alert.AlertType.ERROR, "Push to global list failed - database error!", ButtonType.OK).showAndWait();
        }
    }

    public void SetNotificationButtonOnAction(ActionEvent actionEvent) {

        ObservableList<PersonalEvent> eventSelected;
        eventSelected = TableView.getSelectionModel().getSelectedItems();

        TablePosition pos = TableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        PersonalEvent item = TableView.getItems().get(row);
        java.util.Date currentDate = new java.util.Date();

        TableColumn col = TableView.getColumns().get(1);
        TableColumn col2 = TableView.getColumns().get(0);
        Date selectedDate = (Date) col.getCellObservableValue(item).getValue();
        String selectedEvent = (String) col2.getCellObservableValue(item).getValue();
        File file = new File("src\\main\\resources\\images\\notification.png");
        Image image = new Image(file.toURI().toString());


        Notifications notificationsBuilder = Notifications.create()
                .title("Remind me!")
                .text("Event " + selectedEvent + " \n" + selectedDate + "\n" + "15 minutes prior reminder ON!")
                .graphic(new ImageView(image))
                .hideAfter(Duration.minutes(1))
                .position(Pos.TOP_RIGHT)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked on notification!");
                    }
                });
        notificationsBuilder.darkStyle();
        notificationsBuilder.show();


        long delay = selectedDate.getTime() - currentDate.getTime() - 900000;

        Timer timer = new Timer();
        if (delay > 0) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    File file = new File("src\\main\\resources\\images\\notification.png");
                    Image image = new Image(file.toURI().toString());


                    Notifications notificationsBuilder = Notifications.create()
                            .title("Remind me!")
                            .text("Event " + selectedEvent + " \n" + selectedDate + "\n" + " is happening in 15 minutes!")
                            .graphic(new ImageView(image))
                            .hideAfter(Duration.minutes(1))
                            .position(Pos.TOP_RIGHT)
                            .onAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    System.out.println("Clicked on notification!");
                                }
                            });
                    notificationsBuilder.darkStyle();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            notificationsBuilder.show();
                        }
                    });
                    System.out.println("Executed at:" + (new Date()));
                }
            }, delay);
        }
    }


}
