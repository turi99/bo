package it.polimi.ingsw.client.GUI.handlers;

import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.SceneHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

/**
 * Allow players to crate Lobbies with GUI
 */
public class CreateLobbyHandler implements SceneHandler {
    private ClientGUI clientGUI;

    private ObservableList<Integer> numberOfPlayersList = FXCollections.observableArrayList(2,3,4);
    private ObservableList<String> gameTypeList = FXCollections.observableArrayList("Normal", "Pro");

    @FXML
    private ChoiceBox<Integer> numberOfPlayersChoiceBox;
    @FXML
    private ChoiceBox<String> gameTypeChoiceBox;

    @Override
    public void setClientGui(ClientGUI c) {
        clientGUI = c;
    }

    @Override
    public void setParameters(SceneHandler v) {
    }

    /**
     * Initialises the parameters to show to players
     */
    @Override
    public void init() {
        numberOfPlayersChoiceBox.setValue(2);
        numberOfPlayersChoiceBox.setItems(numberOfPlayersList);

        gameTypeChoiceBox.setValue("Normal");
        gameTypeChoiceBox.setItems(gameTypeList);
    }

    /**
     * Handles the press of the OkMenuButton
     * @param event buttonPressed
     */
    @FXML
    private void okMenuButtonPress(ActionEvent event) {
        if (!(numberOfPlayersChoiceBox.getValue() >= 2 && numberOfPlayersChoiceBox.getValue() <= 4) ||
                (!gameTypeChoiceBox.getValue().equals("Normal") && !gameTypeChoiceBox.getValue().equals("Pro"))
        ) {
            System.out.println("Invalid Values");
        } else {
            String gameType;
            switch (gameTypeChoiceBox.getValue()) {
                case "Normal":
                    gameType = "n";
                    break;
                case "Pro":
                    gameType = "p";
                    break;
                default:
                    gameType = "n";
            }
            System.out.println(numberOfPlayersChoiceBox.getValue());
            System.out.println(gameTypeChoiceBox.getValue());

            clientGUI.createLobby(numberOfPlayersChoiceBox.getValue(), gameType);
        }
    }
}
