package it.polimi.ingsw;

import it.polimi.ingsw.client.MyClient;

import java.io.IOException;

public class ClientAppCli {

    public static void main(String[] args) throws  ClassNotFoundException {
        try {

            MyClient client1 = new MyClient("127.0.0.1", 6789);
        }catch (IOException e){
            System.out.println("Not Connected to the server");
        }
    }
}
