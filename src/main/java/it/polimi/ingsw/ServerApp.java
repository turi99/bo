package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;

import java.io.IOException;

public class ServerApp {

    public static void main( String[] args ) throws Exception {
        Server server = new Server();
        server.startServer();
    }



}
