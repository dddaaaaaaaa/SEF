package controller.user;

import controller.user.UserViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class FxmlLoader {
    FXMLLoader loader = new FXMLLoader();
    public Pane view = new Pane();

    public Pane getPage(String fileName) {
        try {
            System.out.println(fileName + "\n");
            URL fileURL = getClass().getClassLoader().getResource("fxml/" + fileName + ".fxml");
            System.out.println("file url " + fileURL + "\n");
           // if (fileURL == null) {
            //    throw new java.io.FileNotFoundException("FXML file can't be found!");
          //  }
            loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Settings.fxml"));
            //view = FXMLLoader(fileURL).load();
            view = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return view;
    }
}
