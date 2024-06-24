package it.polimi.ingsw.client.GUI.handlers;

import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.SceneHandler;
import it.polimi.ingsw.client.GUI.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuHandler implements SceneHandler {
    private ClientGUI clientGui;

    private final int MAX_TEXT_LENGTH = 20;
    private boolean reusedUserName = false;

    @FXML
    private Text userNameInfo;

    @FXML
    private TextField nameInput;

    /**
     * Set the reusedUserName parameter
     * @param info says if the name is already used by a player
     */
    public void setReusedUserName(boolean info) {
        reusedUserName = info;
    }


    public void setClientGui(ClientGUI c) {
        clientGui = c;
    }

    /**
     * Sets the parameters used by the Handler
     * @param v MainMenuHandler this handler takes parameters from
     */
    @Override
    public void setParameters(SceneHandler v) {
        this.reusedUserName = ((MainMenuHandler) v).reusedUserName;
    }

    /**
     * Initialises the parameters to tell players if their used UserName is already used
     */
    @Override
    public void init() {
        userNameInfo.setVisible(reusedUserName);
    }

    /**
     * Handles the press of the OkNameButton, check if name is too long and sends the name chosen by players
     * @param event buttonPressed
     */
    public void onOkNameButtonPress(ActionEvent event) {
        try {
            if(nameInput.getText().length()>MAX_TEXT_LENGTH) {
                userNameInfo.setText("Name too long");
                userNameInfo.setVisible(true);
            } else
                clientGui.setUserName(nameInput.getText());
        } catch (IOException | ClassNotFoundException | InterruptedException exception) {
            exception.printStackTrace();
        }
        //SceneManager.changeScene("lobbyView.fxml");
    }
}
