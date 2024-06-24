package it.polimi.ingsw.client.GUI.handlers;

import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.SceneHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

import java.util.Objects;

public class WaitingForPlayersHandler implements SceneHandler {
    private ClientGUI clientGUI;
    private int currNumPlayers;
    private int totNumPlayers;
    private String gameKind;

    @FXML
    Text numOfPlayersText;
    @FXML
    Text gameKindText;
    @FXML
    Text loadingGameText;

    /**
     * Sets the current number of players in this Lobby
     * @param num current number of players in this Lobby
     */
    public void setCurrNumOfPlayers(int num) {
        currNumPlayers = num;
    };

    /**
     * Sets the wanted final number of players in this Lobby
     * @param num wanted final number of players in this Lobby
     */
    public void setTotNumPlayers(int num) {
        totNumPlayers = num;
    };

    /**
     * Sets the type of game
     * @param kind String indicating the type of Game: (Normal or Pro)
     */
    public void setGameKind(String kind) {
        this.gameKind = kind;
    }

    @Override
    public void setClientGui(ClientGUI c) {
        clientGUI = c;
    }

    /**
     * Sets the parameters used by the Handler to show current number of players, wanted number of player and game type
     * @param v WaitingForPlayersHandler this handler takes parameters from
     */
    @Override
    public void setParameters(SceneHandler v) {
        WaitingForPlayersHandler t = (WaitingForPlayersHandler) v;
        this.currNumPlayers = t.currNumPlayers;
        this.totNumPlayers = t.totNumPlayers;
        this.gameKind = (t.gameKind.equals("p")||t.gameKind.equals("Pro"))?"Pro":"Normal";
    }

    /**
     *  Initialises the parameters to show to current number of players, wanted number of player and game type
     */
    @Override
    public void init() {
        numOfPlayersText.setText(currNumPlayers + "/" + totNumPlayers + " number of players.");
        gameKindText.setText(gameKind + " game");
        new Thread(() -> clientGUI.createGame()).start();
    }

    /**
     * Updates the current number of players and show the Loading text when the game is loading
     * @param n new current number of players
     */
    public void updateCurrPlayers(int n) {
        this.currNumPlayers = n;
        numOfPlayersText.setText(currNumPlayers + "/" + totNumPlayers + " number of players.");

        if(currNumPlayers == totNumPlayers)
            loadingGameText.setVisible(true);
    }
}
