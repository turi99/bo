package it.polimi.ingsw;
//testtttttt
import it.polimi.ingsw.client.MyClient;
import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.GUILauncher;
import it.polimi.ingsw.client.GUI.SceneManager;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        boolean launchGui = false;

        for (String arg : args) {
            if (arg.equals("--gui") || arg.equals("-g")) {
                launchGui = true;
                break;
            }
        }

        if (launchGui) {
            try {
                ClientGUI clientGUI = new ClientGUI("127.0.0.1", 6789);  //192.168.43.25
                SceneManager.setClientGUI(clientGUI);
                GUILauncher.main(args);
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            try {
                MyClient client1 = new MyClient("127.0.0.1", 6789);
            }catch (IOException e){
                System.out.println("Not Connected to the server");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
