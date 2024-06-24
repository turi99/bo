package it.polimi.ingsw.client.GUI.handlers;

import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.SceneHandler;
import it.polimi.ingsw.client.GUI.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Allow players choose if to create lobbies or join lobbies if they can
 */
public class LobbyConnectionHandler implements SceneHandler {
    private ClientGUI clientGUI;
    private PrintStream socketOut;
    private ObjectInputStream in;

    private boolean canJoinLobbies;

    /**
     * Set the canJoinLobbies parameter
     * @param canJoinLobbies says if the player can decide to join lobbies or not
     */
    public void setCanJoinLobbies(boolean canJoinLobbies) {
        this.canJoinLobbies = canJoinLobbies;
    }

    @Override
    public void setClientGui(ClientGUI c) {
        clientGUI = c;
    }

    /**
     * Sets the parameters used by the Handler
     * @param v LobbyConnectionHandler this handler takes parameters from
     */
    @Override
    public void setParameters (SceneHandler v) {
        LobbyConnectionHandler t = (LobbyConnectionHandler) v;
        this.canJoinLobbies = t.canJoinLobbies;
    }

    /**
     * Initialises the parameters to show to players and forces a Player to create a Lobby if there are none active
     */
    public void init() {
        in = clientGUI.getInInputStream();
        if(!canJoinLobbies)
            SceneManager.changeScene("createLobbyView.fxml");
    }

    /**
     * Let a player create a new Lobby
     *
     * @param actionEvent buttonPressed
     */
    @FXML
    private void newLobbyButtonPress(ActionEvent actionEvent) {
        clientGUI.createOrChooseLobby(1);
    }

    /**
     * Let a player join an existing Lobby
     * @param actionEvent buttonPressed
     */
    @FXML
    private void joinLobbyButtonPress(ActionEvent actionEvent) {
        clientGUI.createOrChooseLobby(0);
    }
}
