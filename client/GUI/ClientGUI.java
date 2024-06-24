package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.handlers.CreateLobbyHandler;
import it.polimi.ingsw.client.GUI.handlers.LobbyConnectionHandler;
import it.polimi.ingsw.client.GUI.handlers.MainMenuHandler;
import it.polimi.ingsw.client.GUI.handlers.WaitingForPlayersHandler;
import it.polimi.ingsw.client.ViewCli;
import it.polimi.ingsw.client.ViewCliPro;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelPro;
import it.polimi.ingsw.model.ModelView;
import it.polimi.ingsw.model.ModelViewPro;
import javafx.application.Platform;
import javafx.scene.Parent;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientGUI {
    private ViewGUI viewGui;
    private final PrintStream out;
    private final ObjectInputStream in;
    private Socket socket;
    private String kind;
    private int num;

    private String userName;


    /**
     * Creates ClientGUI
     *
     * @param ip
     * @param port
     * @throws IOException
     */
    public ClientGUI (String ip, int port) throws IOException {
        System.out.println("Waiting server...");
        socket = new Socket(ip, port);
        System.out.println("Connection established");
        out =new PrintStream(new DataOutputStream(socket.getOutputStream()),false);
        in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Sets userName of the player if no one else is using it otherwise sends "UserName already taken" as message
     *
     * @param userName the userName of the player
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public void setUserName(String userName) throws IOException, ClassNotFoundException, InterruptedException {
        String message = "";

        this.userName = userName;
        out.println(userName);
        boolean accepted = false;
        while (!message.equals("Accepted")){
            message = (String) in.readObject();
            if(message.equals("User Name already taken")){
                MainMenuHandler menu = new MainMenuHandler();
                menu.setReusedUserName(true);
                Platform.runLater(()->SceneManager.changeScene(menu, "mainMenu.fxml"));
                break;
            } else {
                accepted = true;
            }
        }
        if(accepted)
            lobby();
    }

    /**
     *
     * @return the out PrintStream
     */
    public PrintStream getOutPrintStream() {
        return out;
    }

    /**
     *
     * @return the in InputStream
     */
    public ObjectInputStream getInInputStream() {
        return in;
    }

    /**
     *
     * @return the number of players of the game
     */
    public int getNum() {
        return num;
    }

    /**
     *
     * @return the viewGui
     */
    public ViewGUI getViewGui() {
        return viewGui;
    }

    /**
     *Sees if there are already open lobbies, if so let the player choose to join one or create one with the gui view, otherwise force a player create a lobby with the gui view
     *
     */
    public void lobby() throws IOException, ClassNotFoundException, InterruptedException {
        Object o = in.readObject();
        if(o.getClass()!= Integer.class)
            o =  in.readObject();
        int chooseLobby = (int) o;
        LobbyConnectionHandler lobbyConnectionHandler = new LobbyConnectionHandler();
        lobbyConnectionHandler.setCanJoinLobbies(chooseLobby == 1);
        Platform.runLater(() -> SceneManager.changeScene(lobbyConnectionHandler, "lobbyView.fxml"));
    }

    /**
     * Let the player create a lobby or join one based on what they want
     *
     * @param i 0 choose lobby;
     *          1 create lobby
     */
    public void createOrChooseLobby(int i) {
        //i=1 create | i=0 choose
        out.println(i);
        if(i==0)
            Platform.runLater(() -> SceneManager.changeScene("chooseLobbyView.fxml"));
        else
            Platform.runLater(() -> SceneManager.changeScene("createLobbyView.fxml"));
    }

    /**
     * Creates a new lobby with teh chosen number of players and the chosen gameType
     *
     * @param numOfPlayers the final number of players for the game
     * @param gameKind the type of game ("p" or "Pro" for pro gameType;   "n" or "Normal" for normal gameType)
     */
    public void createLobby(int numOfPlayers, String gameKind) {
        kind = gameKind;
        num = numOfPlayers;

        //read connection messages
        try {
            System.out.println("numOfLobbies:" + in.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //send lobby info to server and wait for response
        String message="";
        out.println(numOfPlayers + "," + gameKind);
        while(!message.equals("Lobby created successful ")) {
            try {
                message = (String) in.readObject();
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        WaitingForPlayersHandler w = new WaitingForPlayersHandler();
        w.setCurrNumOfPlayers(1);
        w.setTotNumPlayers(numOfPlayers);
        w.setGameKind(gameKind);

        Platform.runLater(() -> SceneManager.changeScene(w, "waitingForPlayers.fxml"));
    }

    /**
     * Makes a player join the chosen lobby
     *
     * @param lobby String containing the id of the chosen lobby to join, the current number of players in lobby and the total number of players wanted fo the game
     */
    public void joinLobby(String lobby) {
        //get lobby info from string
        int lobbyId = Integer.parseInt(lobby.split(" ")[0].split(":")[1]);
        int currNumOfPlayers = Integer.parseInt(lobby.split(" ")[4].split(":")[1].split("/")[0])+1;
        int totNumOfPlayers = Integer.parseInt(lobby.split(" ")[4].split(":")[1].split("/")[1]);
        String gameKind = lobby.split(" ")[10].split(":")[1];

        kind = gameKind;
        num = totNumOfPlayers;

        String message = "";
        out.println(lobbyId);
        while(!message.equals("Lobby chosen successful")) {
            try {
                Object o = in.readObject();
                message = (String) o;
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        WaitingForPlayersHandler w = new WaitingForPlayersHandler();
        w.setCurrNumOfPlayers(currNumOfPlayers);
        w.setTotNumPlayers(totNumOfPlayers);
        w.setGameKind(gameKind);

        Platform.runLater(() -> SceneManager.changeScene(w, "waitingForPlayers.fxml"));
    }

    /**
     * Awaits listening to server messages until all players join the lobby, then creates the game
     *
     */
    public  void createGame() {
        System.out.println("In createGame");
        boolean hasStarted = false;
        WaitingForPlayersHandler s = (WaitingForPlayersHandler) SceneManager.getActiveHandler();
        Object o = null;
        try {
            String message = "";
            while (!message.equals("start")) {
                //System.out.println("cilco while");
                o = in.readObject();
                message = (String) o;
                System.out.println(message);

                //String st = message.split("/")[0];
                //System.out.println("s = " + st);

                int i = 0;
                if (!message.equals("start")) {
                    try {
                        i = Integer.parseInt(message.split("/")[0]);
                        //System.out.println("Normal: " + i);
                    } catch (NumberFormatException e) {
                        System.err.println("Cannot parse int");
                        e.printStackTrace();
                    }
                    //System.out.println("i = " + i);
                    s.updateCurrPlayers(i);
                } else {
                    hasStarted = true;
                }
            }
            if (hasStarted) {
                o = in.readObject();
                int id = (int) o;
                buildAndStartGame(id);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            //System.err.println("CastException");
            try {
                int id = (int) o;
                buildAndStartGame(id);
                //System.out.println("ok");
            } catch (Exception ex) {
                System.err.println("Exception building client");
                ex.printStackTrace();
            }
        }

    }

    /**
     *Build the game elements (viewGui and modelView) for the player based on the type of Game
     *
     * @param id the id given to the player
     */
    public void buildAndStartGame(int id) {
        try {
            if (kind.equals("n")||kind.equals("normal")||kind.equals("Normal")) {
                viewGui =new ViewGUI(out,in);
                ModelView model = new ModelView(id, (Model) in.readObject());
                model.addObservers(viewGui);
                Platform.runLater(() -> SceneManager.changeScene("mainGameView.fxml"));
                viewGui.startViewGui(model);
            }
            else if(kind.equals("p")||kind.equals("pro")||kind.equals("Pro")){
                viewGui =new ViewGUIPro(out,in);
                ModelView model=new ModelViewPro(id,(ModelPro) in.readObject());
                model.addObservers(viewGui);
                Platform.runLater(() -> SceneManager.changeScene("mainGameViewPro.fxml"));
                viewGui.startViewGui(model);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        //stay alive until someone exits the game, then notifies players
        while(!viewGui.getReadObj().isInterrupted()){
        }
        System.out.println("-------------");
        Platform.runLater(() -> SceneManager.changeScene( "victoryScreenView.fxml"));
    }

    /**
     * Let the players decide to play again if they want to after a game has finished or a player disconnected
     *
     * @param value
     */
    public void playAgain(String value) {
        out.println(value);
        if(value.equals("n")) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                //Platform.runLater(()-> SceneManager.changeScene("joinLobby.fxml"));
                lobby();
            }catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
