package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ControllerPro;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelPro;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Lobby{
    private boolean stated=false;
    private final Server server;
    private final ArrayList<ClientHandler> client=new ArrayList<>();
    private final int num;
    private final String kind;
    private final int id;
    private RemoteView[] remoteViews;
    private Controller controller;
    private Model model;

    /**
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return client
     */
    public ArrayList<ClientHandler> getClient() {
        return client;
    }

    /**
     *
     * @return num
     */
    public int getNum() {
        return num;
    }

    public Lobby(int num, String kind,int id,Server server) throws Exception {
        System.out.println("building lobby...");
        this.id=id;
        this.num=num;
        this.kind=kind;
        this.server=server;
        if(Objects.equals(kind, "n")){
            model=new Model(num);
            controller=new Controller(model);
            remoteViews=new RemoteView[num];



        }
        else if(Objects.equals(kind, "p")){
            model=new ModelPro(num);
            model=((ModelPro)model).initializeModelPro((ModelPro) model);
            controller=new ControllerPro((ModelPro) model);
            remoteViews=new RemoteView[num];
        }


    }

    /**
     *
     * @return client.size()==num
     */
    public boolean isFull(){
        return client.size()==num;
    }

    /**
     *
     * @return client.size()+"/"+num
     */
    public String infoPlayer(){
        return client.size()+"/"+num;
    }

    /**
     *
     * @return id+" "+infoPlayer()+" "+kind
     */
    public String info(){
        return id+" "+infoPlayer()+" "+kind;
    }

    /**
     *
     * @return stated
     */
    public boolean isStrated() {
        return stated;
    }

    /**
     * add the client to the lobby, if the lobby is full the game start
     * @param newClient client to add to the lobby
     * @throws Exception error while sending information to the clients in the lobby
     */
    public void addClient(ClientHandler newClient) throws Exception {
        if(client.size()<num){
            int i=client.size();
            client.add(newClient);
            newClient.setLobby(this);
            model.getPlayers()[i].setUserName(newClient.getUserName());
            System.out.println("infoPlayer : "+infoPlayer());
            remoteViews[i]=new RemoteView(i,client.get(i),controller,server,this);
            model.addObservers(remoteViews[i]);
            Thread t=new Thread(remoteViews[i]);
            t.start();
            sandAll(infoPlayer());
            if(isFull()){
                stated=true;
                for(RemoteView r:remoteViews){
                    r.update("start");
                    r.sendId();
                    r.update(model);
                }
            }
        } else {
            throw new Exception("the lobby is full");
        }

    }

    /**
     * for all client in the lobby, send s message
     * @param s message
     * @throws IOException error while writing on c.getOut()
     */
    public void sandAll(String s) throws IOException {
        for(ClientHandler c: client){
            c.getOut().writeObject(s);
        }
    }

    /**
     * remove the given client
     * @param clientHandler client of interest
     */
    public void removeClientHandler(ClientHandler clientHandler){
        this.client.remove(clientHandler);
    }

    /**
     *
     * @return remoteViews
     */
    public RemoteView[] getRemoteViews() {
        return remoteViews;
    }

}
