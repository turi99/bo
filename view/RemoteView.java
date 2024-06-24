package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Exceptions.ExceptionIllegalArgument;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utils.observer.Observer;

import java.io.IOException;
import java.util.Objects;

public class RemoteView extends Thread implements  Observer<Object> {
    private final Lobby lobby;
    private final Server server;
    private final int id;
    private final ClientHandler clientHandler;
    private final Controller controller ;

    public RemoteView(int id,ClientHandler clientHandler,Controller controller,Server server,Lobby lobby){
        this.id=id;
        this.clientHandler=clientHandler;
        this.controller=controller;
        this.server=server;
        this.lobby=lobby;
    }

    /**
     * read the input from the client and actuate on the controller
     */
    @Override
    public void run() {
        String s="";
        while (!isInterrupted()){
            try {
                s=clientHandler.getIn().readLine();
                System.out.println(s);
                if(((Objects.equals(s, "y")|| s.equals("n")) && (controller.getModel().getState().getName().equals("win") || this.isInterrupted()))|| s.equals("quit")) break;
                String instruction=id+","+s;
                if(lobby.isStrated()) {
                    System.out.println(instruction);
                    controller.switchInstruction(instruction);
                }
            } catch (IOException e) {
                if (lobby.isStrated()) {
                    System.out.println(clientHandler.getUserName() + " disconnected");
                    controller.getModel().notify(clientHandler.getUserName() + " lost\nGame closed");
                }else {
                    lobby.removeClientHandler(clientHandler);
                    return;
                }
                for (RemoteView r : lobby.getRemoteViews()){
                    r.interrupt();
                    try {
                        r.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            }catch (ExceptionIllegalArgument e){
                controller.getModel().notify(controller.getModel().getCurrentPlayerPos()+","+e.getMessage());
            }
        }
        if(s.equals("y")) {
            try {
                server.clientEnterInLobby(clientHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(s.equals("quit")){
            System.out.println(clientHandler.getUserName()+" lost\nGame closed");
            controller.getModel().notify(clientHandler.getUserName()+" lost\nGame closed");
            for (RemoteView r : lobby.getRemoteViews()){
                r.interrupt();
                try {
                    r.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            String m="";
            try {
                m=clientHandler.getIn().readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }if(m.equals("y")) {
                try {
                    server.clientEnterInLobby(clientHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else closeConnection();
        }
        else closeConnection();
    }

    /**
     * close the connection with the client
     */
    public void closeConnection(){
        try {
            clientHandler.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send id to the client
     */
    public void sendId(){
        try {
            clientHandler.getOut().writeObject(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void update(Object message) {
        try {
            System.out.println(message.getClass());
            clientHandler.getOut().reset();
            clientHandler.getOut().writeObject(message);
            clientHandler.getOut().flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }


}
