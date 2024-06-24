package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.handlers.MainMenuHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Helper class that initialises the GUI values and launches the GUI
 */
public class GUILauncher extends Application {
    /*public static void main(String[] args){
            ViewGui.main(args);
        }
     */

    /**
     * start the GUI
     *
     * @param stage base GUI stage
     * @throws IOException
     */
    public void start(Stage stage) throws IOException {
        System.out.println("init");

        //Normal launch
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
        Parent root = loader.load();
        ((MainMenuHandler)(loader.getController())).setClientGui(SceneManager.getClientGUI());
        Scene scene = new Scene(root);
        SceneManager.setActiveScene(scene);

        Image icon = new Image("/Images/EriantysImg.jpg");
        stage.setTitle("Eriantys");
        stage.getIcons().add(icon);
        //stage.setFullScreen(true);

        stage.setWidth(1500.0);     //1500  max:1540
        stage.setHeight(780.0);     //780   max:800
        stage.setMaximized(true);

        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
