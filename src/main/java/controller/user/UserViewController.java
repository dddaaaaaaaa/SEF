package controller.user;


import domain.User;
import domain.UserHolder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class UserViewController extends FxmlLoader implements Initializable {
    @FXML
    private ListView<String> UserListView;
    @FXML
    private BorderPane mainUserPane;
    @FXML
    private Label dateTimeLabel, UsernameLabel;
    //@FXML
    //private ImageView siglaImageView, clockImageView, logoutImageView;
    @FXML
    private Button logoutButton;

    ObservableList<String> List = FXCollections.observableArrayList();
    private User currentUser;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //File file = new File("src\\main\\resources\\images\\sigla.png");
        //Image image = new Image(file.toURI().toString());
        //siglaImageView.setImage(image);


        //File clockFile = new File("src\\main\\resources\\images\\clock.png");
        //image = new Image(clockFile.toURI().toString());
        //clockImageView.setImage(image);

        //file = new File("src\\main\\resources\\images\\logout.png");
        //image = new Image(file.toURI().toString());
        //logoutImageView.setImage(image);

        UserHolder userHolder;
        userHolder = UserHolder.getInstance();
        currentUser = userHolder.getUser();
        UsernameLabel.setText(currentUser.getUsername());
        showSettingsStage();

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

                mainUserPane.setPrefSize(620, 650);
                mainUserPane.setCenter(view);

                //mainUserPane.
            }
        });
        initClock();
    }
    private void initClock() {

        Timeline clock = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dateTimeLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void LoadIntoUserList() throws InterruptedException {
        List.add("Settings");
        List.add("Personal Events");
        List.add("Global Events");

        UserListView.getItems().addAll(List);
    }

    public void showSettingsStage() {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("Settings");
        try {
            view = loader.load(getClass().getClassLoader().getResource("fxml/Settings.fxml"));
            ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainUserPane.setPrefSize(620, 650);
        mainUserPane.setCenter(view);


    }

    public void logoutButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(new Scene(root, 520, 400));
        primaryStage.show();
    }
}
