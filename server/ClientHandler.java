package it.polimi.ingsw.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler{
    private Lobby lobby;
    private String userName;
    private final Socket socket;
    private BufferedReader in;
    private ObjectOutputStream out;

    public ClientHandler(Socket socket){
        this.socket = socket;
        try {
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return lobby
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     * replace the lobby
     * @param lobby new lobby
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * replace the userName
     * @param userName new userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return lobby
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @return socket
     */
    public Socket getSocket() {
        
        return socket;
    }

    /**
     *
     * @return in
     */
    public BufferedReader getIn() {
        return in;
    }

    /**
     *
     * @return out
     */
    public ObjectOutputStream getOut(){
        return out;
    }

    /**
     * close the connection with the client
     * @throws IOException error while closing connection
     */
    public void closeConnection() throws IOException {
        try{
            in.close();
        }catch (Exception e){
            System.out.println("connection already closed");
        }
        try{
            out.close();
        }catch (Exception e){
            System.out.println("connection already closed");
        }
        try{
            socket.close();
        }catch (Exception e){
            System.out.println("connection already closed");
        }
    }

}