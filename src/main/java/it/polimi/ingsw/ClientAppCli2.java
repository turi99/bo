package it.polimi.ingsw;

import it.polimi.ingsw.client.MyClient;

import java.io.IOException;

public class ClientAppCli2 {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        MyClient client1 = new MyClient("127.0.0.1", 6789);
    }
}
