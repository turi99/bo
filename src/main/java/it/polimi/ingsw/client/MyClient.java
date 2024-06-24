package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelPro;
import it.polimi.ingsw.model.ModelView;
import it.polimi.ingsw.model.ModelViewPro;

import java.io.*;
import java.net.Socket;


public class MyClient {
    private final PrintStream out;
    private final ObjectInputStream in;
    private final BufferedReader bufferedReader;
    private final Socket socket;
    private String kind;


    public MyClient(String ip, int port) throws IOException, ClassNotFoundException{
        System.out.println("Waiting server...");
        socket = new Socket(ip, port);
        System.out.println("Connection established");
        out =new PrintStream(new DataOutputStream(socket.getOutputStream()),false);
        in = new ObjectInputStream(socket.getInputStream());
        bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        String message="";
        String userName;
        while (!message.equals("Accepted")){
            System.out.println("Insert User Name\n");
            userName=bufferedReader.readLine();
            out.println(userName);
            message=(String) in.readObject();
            System.out.println(message);
        }

        lobby();

    }

    /**
     * allow the user to create or join a lobby on the server
     * @throws IOException error while reading input
     * @throws ClassNotFoundException error while reading object
     */
    public void lobby() throws IOException, ClassNotFoundException {

        System.out.println("Waiting server message...");
        Object oIn =  in.readObject();
        System.out.println(oIn.getClass());
        if(oIn.getClass()!= Integer.class)
            oIn =  in.readObject();
        int chooseLobby = (int) oIn;
        if (chooseLobby == 1) {
            String c="";
            while (!c.equals("0") && !c.equals("1")) {
                System.out.println("Do you want create or choose a lobby? \n ( 1 : create | 0 : choose ) ");
                c = bufferedReader.readLine();
            }
            out.println(c);
            if (c.equals("0")) chooseLobby();
            else {
                System.out.println("Number of lobby : "+ in.readObject()+"\n");
                createLobby();
            }
        } else {
            System.out.println((String) in.readObject());
            createLobby();
        }

        try {
            String message = (String) in.readObject();
            while (!message.equals("start")) {
                System.out.println(message);
                message = (String) in.readObject();

            }
            int id = (int) in.readObject();
            ViewCli viewCli;
            if (kind.equals("n")) {
                viewCli =new ViewCli(out,in,bufferedReader);
                ModelView model = new ModelView(id, (Model) in.readObject());
                model.addObservers(viewCli);
                viewCli.startViewCli(model);
            }
            else if(kind.equals("p")){
                viewCli =new ViewCliPro(out,in,bufferedReader);
                ModelViewPro model=new ModelViewPro(id,(ModelPro) in.readObject());
                model.addObservers(viewCli);
                viewCli.startViewCli(model);
            }
        }
        catch(Exception e){
            System.out.println("Server lost");
            e.printStackTrace();
            return;
        }
        String choose="";
        while (!choose.equals("y") && !choose.equals("n")){
            System.out.println("Do you want to play again? (y/n)");
            choose=bufferedReader.readLine();
            out.println(choose);
        }
    if(choose.equals("y"))lobby();
    else {
        socket.close();
        bufferedReader.close();
    }
    }

    private void createLobby() throws IOException {
        String message="";
        int num=0;
        while (!message.equals("Lobby created successful ")) {
            System.out.println("insert number of players (2-4) and kind of game (n/p)");
            System.out.println("insert number of players (2-4):");
            num = Integer.parseInt(bufferedReader.readLine());
            System.out.println("insert kind of game (n/p):");
            kind = bufferedReader.readLine();
            out.println(num + "," + kind);
            try {
                message=(String) in.readObject();
                System.out.println(message);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("You create a game of kind " + kind + " and players " + num);
    }

    private void chooseLobby() throws IOException, ClassNotFoundException {
        String message="";
        int lobby=0;
        String[] s = ((String) in.readObject()).split(",");
        String[] s1=new String[s.length];
        int i=1;
        for(String v:s){
            s1[s.length-i]=v;
            i++;
        }
        while (!message.equals("Lobby chosen successful")) {
            for (String c : s1) {
                System.out.println(c);
            }
            lobby = Integer.parseInt(bufferedReader.readLine());
            out.println(lobby);
            message=(String) in.readObject();
            System.out.println(message);
        }
        System.out.println(in.readObject());
        System.out.println("you choose the lobby number " + lobby);
        kind = s1[lobby].split(" ")[2];

    }






}