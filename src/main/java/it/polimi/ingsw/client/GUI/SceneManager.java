package it.polimi.ingsw.client.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Helper class that manages the change of the current scene
 */
public class SceneManager {
    private static ClientGUI clientGui;
    private static Scene activeScene;
    private static SceneHandler activeHandler;

    /**
     *
     * @param c clientGui to set
     */
    public static void setClientGUI(ClientGUI c) {
        clientGui = c;
    }

    /**
     *
     * @param s Scene to set
     */
    public static void setActiveScene(Scene s) {
        activeScene = s;
    }

    /**
     *
     * @return current clientGui
     */
    public static ClientGUI getClientGUI() {
        return clientGui;
    }

    /**
     *
     * @return current active Scene
     */
    public static Scene getActiveScene() {
        return activeScene;
    }

    /**
     *
     * @return current sceneHandler
     */
    public static SceneHandler getActiveHandler() {
        return activeHandler;
    }

    /**
     * Changes the current scene with another one
     *
     * @param passedController the handler from which takes the values
     * @param fxml the fxml file scene to set
     * @return used sceneHandler
     */
    public static SceneHandler changeScene(SceneHandler passedController, String fxml) {
        SceneHandler tempHandler = null;
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            tempHandler = loader.getController();

            tempHandler.setParameters(passedController);
            activeHandler = tempHandler;

            activeScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        activeHandler.setClientGui(clientGui);
        activeHandler.init();
        return tempHandler;
    }

    /**
     * Changes the current scene with another one
     *
     * @param scene scene to use
     * @param fxml the fxml file scene to set
     * @return used SceneHandler
     */
    private static SceneHandler changeScene(Scene scene, String fxml) {
        SceneHandler handler = null;

        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            handler = loader.getController();

            activeHandler = handler;
            activeScene = scene;
            activeScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        activeHandler.setClientGui(clientGui);
        activeHandler.init();
        return handler;
    }

    /**
     * Changes the current scene with another one
     *
     * @param fxml the fxml file scene to set
     * @return used SceneHandler
     */
    public static SceneHandler changeScene(String fxml) {
        return changeScene(activeScene, fxml);
    }
}
