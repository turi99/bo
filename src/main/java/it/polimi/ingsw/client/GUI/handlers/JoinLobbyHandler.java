package it.polimi.ingsw.client.GUI.handlers;

import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.SceneHandler;
import it.polimi.ingsw.client.GUI.SceneManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

/**
 * Allow players to join Lobbies with GUI
 */
public class JoinLobbyHandler implements SceneHandler {
    private ClientGUI clientGUI;
    private PrintStream socketOut;
    private ObjectInputStream in;

    private ObservableList<String> numberOfPlayersList = FXCollections.observableArrayList();
    private String currentLobby;
    @FXML
    private ListView<String> listViewOfLobbies;
    @FXML
    private Button joinLobbyButton;

    @Override
    public void setClientGui(ClientGUI c) {
        clientGUI = c;
    }

    @Override
    public void setParameters (SceneHandler v) {
    }

    /**
     * Initialises the parameters to show to players and the current active lobbies
     */
    public void init() {
        joinLobbyButton.setDisable(true);
        in = clientGUI.getInInputStream();
        try {
            String[] s = ((String) in.readObject()).split(",");
            for (String lobby:s) {
                String[] lobbyInfo = lobby.split(" ");
                numberOfPlayersList.add("ID:" + lobbyInfo[0] + "  |  Players:" + lobbyInfo[1] + "  |  Kind of game:" +  (lobbyInfo[2].equals("p")?"Pro":"normal"));
                System.out.println(lobby);
            }
                listViewOfLobbies.setItems(numberOfPlayersList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        listViewOfLobbies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                currentLobby = listViewOfLobbies.getSelectionModel().getSelectedItem();
                //if currNumOfPlayers != totNumberOfPlayers (lobby not full)...
                if(!(currentLobby.split(" ")[4].split(":")[1].split("/")[1].equals(currentLobby.split(" ")[4].split(":")[1].split("/")[0])))
                    joinLobbyButton.setDisable(false);
                else
                    joinLobbyButton.setDisable(true);
            }
        });
    }

    /**
     * Handles the press of the OkMenuButton
     * @param actionEvent buttonPressed
     */
    @FXML
    private void joinLobbyButtonPress(ActionEvent actionEvent) {
        //if currNumOfPlayers != totNumberOfPlayers (lobby not full)...
        if(!(currentLobby.split(" ")[4].split(":")[1].split("/")[1].equals(currentLobby.split(" ")[4].split(":")[1].split("/")[0])))
            clientGUI.joinLobby(currentLobby);
    }
}
