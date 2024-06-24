package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.handlers.MainGameHandlerPro;
import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.PrintStream;

public class ViewGUIPro extends ViewGUI{

    /**
     * Builds ViewGuiPro
     * @param socketOut Socket outputStream
     * @param in Socket inputStream
     */
    public ViewGUIPro(PrintStream socketOut, ObjectInputStream in) {
        super(socketOut, in);
    }

    /**
     * Sends to the Server the messages of the activated Characters Cards
     * @param cardId Id of the activated card
     * @param parameters String containing the parameters for the Character card
     */
    public void activateCard(int cardId, String parameters) {
        getSocketOut().println("5;" + cardId + "," + parameters);
    }

}
