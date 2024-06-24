package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.handlers.MainGameHandler;
import it.polimi.ingsw.client.GUI.handlers.VictoryScreenHandler;
import it.polimi.ingsw.model.Game.AggIsland;
import it.polimi.ingsw.model.Game.Cloud;
import it.polimi.ingsw.model.Game.Player;
import it.polimi.ingsw.model.ModelView;
import it.polimi.ingsw.utils.observer.Observer;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

public class ViewGUI implements Observer<Object> {
    private ModelView model;
    private final PrintStream socketOut;
    private final ObjectInputStream in;
    private Thread readObj;

    public Thread getReadObj() {
        return readObj;
    }

    private MainGameHandler handler;

    public ModelView getModel() {
        return model;
    }

    public PrintStream getSocketOut() {
        return socketOut;
    }

    public void setHandler(MainGameHandler handler) {
        this.handler = handler;
    }

    /**
     *Creates ViewGUI
     *
     * @param socketOut
     * @param in
     */
    public ViewGUI(PrintStream socketOut, ObjectInputStream in) {
        this.socketOut = socketOut;
        this.in = in;

        readObj=readInObj(this.in);
        readObj.setPriority(Thread.MAX_PRIORITY);
    }

    /**
     * Set the Client ModelView and starts the thread which listen to Server messages
     *
     * @param model the modelView of the client
     * @throws InterruptedException
     */
    public synchronized void startViewGui(ModelView model) throws InterruptedException {
        this.model=model;
        readObj.start();
        wait();
    }

    /**
     * Sees if someone has won, if so shows the winning view, otherwise shows the game view
     *
     * @throws InterruptedException
     */
    public synchronized void print() throws InterruptedException {
        if (Objects.equals(model.getState().getName(), "win")) {
            System.out.println("the game is finished\n");
            readObj.interrupt();
            //Platform.runLater(()->SceneManager.changeScene("victoryScreenView.fxml"));
            //MainGameHandler m = new MainGameHandler();
        } else {
            Platform.runLater(() -> handler.update());
        }
    }

    /**
     * Reads the Server messages
     *
     * @param socketIn InputStream to read from
     */
    public Thread readInObj(final ObjectInputStream socketIn){
        readObj = new Thread(() -> {
            synchronized (this) {
                while (!readObj.isInterrupted()) {
                    try {
                        Object o = socketIn.readObject();
                        model.update(o);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                notifyAll();
            }
        });
        return readObj;
    }

    /**
     * Sends the Server the message to play a Card
     *
     * @param value value of the card played
     */
    public void playCard(int value) {
        socketOut.println("0;"+value);
    }

    /**
     * Sends the Server the message to move a student to lane
     *
     * @param color the color of the student to move
     */
    public void studentToLane(int color) {
        socketOut.println("4;"+color);
    }

    /**
     * Sends the Server the message to move a student to an Island
     *
     * @param id id of the island to move to
     * @param color color of the student to move
     */
    public void studentToIsland(int id, int color) {
        socketOut.println("3;" + id+","+color);
    }

    /**
     * Sends the Server the message to move a motherNature to an Island
     *
     * @param destination position of the destination island
     */
    public void moveMotherNature(int destination) {
        socketOut.println("2;" + destination);
    }

    /**
     * Sends the Server the message to select a cloud
     *
     * @param cloud id of the cloud to select
     */
    public void selectCloud(int cloud) {
        socketOut.println("1;" + cloud);
    }


    /**
     * Updates client view with messages from Server
     *
     * @param message Message sent by Server
     */
    @Override
    public void update(Object message) {
        if(message != null) {
            if((Boolean)message) {
                System.out.println("-------------------------------");
                readObj.interrupt();
            }
        }
        try {
            System.out.println("Update: " + message);
            System.out.println("State: " + model.getState().getName());
            print();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
