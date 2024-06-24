package it.polimi.ingsw.client.GUI.handlers;

import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.SceneHandler;
import it.polimi.ingsw.client.GUI.SceneManager;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

public class VictoryScreenHandler implements SceneHandler {
    private ClientGUI clientGUI;

    @FXML
    private Label victoryMessage;
    @FXML
    private Label congratulationMessage;

    @Override
    public void setClientGui(ClientGUI c) {
        clientGUI = c;
    }

    @Override
    public void setParameters(SceneHandler v) {
    }

    /**
     * Initializes the parameters to show thw player if they have won, lost or if someone disconnected
     */
    public void init() {
        if (clientGUI.getViewGui().getModel().getWinner() == null) {
            victoryMessage.setText("Game finished");
            congratulationMessage.setText("Someone left the game");
        } else {
            if (clientGUI.getViewGui().getModel().getColorTeamWinner() == clientGUI.getViewGui().getModel().getMyColor()) {
                victoryMessage.setText("Victory!");
                congratulationMessage.setText("Your team has won, congratulations!");
            } else {
                victoryMessage.setText("You lose");
                congratulationMessage.setText("Your team has lost");
            }
    }}

    /**
     * Handles the press of the Overing of the mouse when it enters on elements and move them slightly
     * @param e element overed
     */
    @FXML
    private void mouseOverEnter(MouseEvent e) {
        ((Node) e.getSource()).setTranslateY(-2);
    }

    /**
     * Handles the press of the Overing of the mouse when it exits from elements and move them slightly
     * @param e element overed
     */
    @FXML
    private void mouseOverExit(MouseEvent e) {
        ((Node) e.getSource()).setTranslateY(2);
    }

    /**
     * Handles the pressing of the exitText and sends the right parameters for the Server
     * @param e textPressed
     */
    @FXML
    private void exitGame(MouseEvent e) {
        clientGUI.playAgain("n");
        Platform.exit();
    }

    /**
     * Handles the pressing of the playAgainText and sends the right parameters for the Server
     * @param e textPressed
     */
    @FXML
    private void replayGame(MouseEvent e) {
        clientGUI.playAgain("y");
    }
}
