package it.polimi.ingsw.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements Runnable {

    private final ServerSocket socket;
    private final ArrayList<Lobby> lobby;
    private final ArrayList<Thread> threads = new ArrayList<>();

    public Server() throws IOException {
        lobby = new ArrayList<>();
        socket = new ServerSocket(6789);
        System.out.println("Server ready");
    }

    /**
     * create a new thread with of this class, add to the pull of threads and start it
     */
    public void startServer() {
        Thread t = new Thread(this);
        threads.add(t);
        t.start();


    }

    /**
     * ask the client to create or choose a lobby
     * @param clientHandler client of interest
     * @throws Exception error while reading or writing
     */
    public void createOrChoose(ClientHandler clientHandler) throws Exception {
        int i = Integer.parseInt(clientHandler.getIn().readLine());
        if (i == 0) {
            System.out.println(clientHandler.getUserName() + " want to choose a lobby");
            chooseLobby(clientHandler);
        } else {
            System.out.println(clientHandler.getUserName() + " want to create a lobby");
            createLobby(clientHandler);
        }
    }

    /**
     *
     * @return info about existing lobby
     */
    public String allLobbyInfo() {
        String s = lobby.get(0).info();
        for (int i = 1; i < lobby.size(); i++) {
            s = lobby.get(i).info() + "," + s;
        }
        return s;
    }

    /**
     * allow the client to choose one of the lobby present on the server
     * @param clientHandler client of interest
     * @throws Exception error while reading or writing
     */
    public void chooseLobby(ClientHandler clientHandler) throws Exception {
        System.out.println(allLobbyInfo());
        boolean correct = false;
        while (!correct) {
            clientHandler.getOut().writeObject(allLobbyInfo());
            int i = Integer.parseInt(clientHandler.getIn().readLine());
            if (i < lobby.size() && i >= 0) {
                correct = true;
                System.out.println(clientHandler.getUserName() + " choose lobby number " + i);
                clientHandler.getOut().writeObject("Lobby chosen successful");
                lobby.get(i).addClient(clientHandler);
                if (lobby.get(i).isFull()) lobby.remove(lobby.get(i));
            } else clientHandler.getOut().writeObject("ERROR :        MAX ID OF LOBBY : " + (lobby.size() - 1));
        }
    }


    private boolean thereAreLobby() {
        return lobby.size() != 0;
    }

    /**
     *  allow the client to create a new lobby
     * @param clientHandler client of interest
     * @throws Exception error while reading or writing
     */
    public void createLobby(ClientHandler clientHandler) throws Exception {
        boolean correct = false;
        if (lobby.size() == 0) {
            clientHandler.getOut().writeObject(lobby.size() + " lobby, you must create once");
        } else clientHandler.getOut().writeObject(lobby.size());
        while (!correct) {

            String[] s = clientHandler.getIn().readLine().split(",");
            int num = Integer.parseInt(s[0]);
            String kind = s[1];
            if (num <= 4 && num >= 2 && (kind.equals("p") || kind.equals("n"))) {
                correct = true;
                clientHandler.getOut().writeObject("Lobby created successful ");
                System.out.println(clientHandler.getUserName() + " create a lobby for " + s[0] + " players of kind " + s[1]);
                Lobby lobby = new Lobby(num, kind, this.lobby.size(), this);
                lobby.addClient(clientHandler);
                this.lobby.add(lobby);

            } else if (num > 4 || num < 2) {
                clientHandler.getOut().writeObject("ERROR :    NUMBER OF PLAYER BETWEEN 2 AND 4 ");

            } else {

                clientHandler.getOut().writeObject("ERROR :    THE KIND OF GAME MUST BE P OR N ");

            }
        }
    }

    /**
     *
     * @param clientHandler client of interest
     * @throws IOException read or write error
     */
    public void clientEnterInLobby(ClientHandler clientHandler) throws IOException {

        if (thereAreLobby()) {
            clientHandler.getOut().writeObject(1);
            try {
                createOrChoose(clientHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            clientHandler.getOut().writeObject(0);
            try {
                createLobby(clientHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * wait for new connection, when a client is connected start a new thread
     */
    @Override
    public void run() {
        ClientHandler clientHandler = null;
        System.out.println("Ready to connect...");
        while (clientHandler == null) {
            try {
                clientHandler = new ClientHandler(socket.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Thread t = new Thread(this);
        threads.add(t);
        t.start();

        String name = "";
        boolean taken;
        do {
            taken = false;
            try {
                while (!clientHandler.getIn().ready()) {

                }
                name = clientHandler.getIn().readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Lobby l : lobby) {
                for (ClientHandler c : l.getClient()) {
                    if (name.equals(c.getUserName())) {
                        taken = true;
                        try {
                            clientHandler.getOut().writeObject("User Name already taken");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        } while (taken);
        clientHandler.setUserName(name);
        try {
            clientHandler.getOut().writeObject("Accepted");
        } catch (IOException e) {
            e.printStackTrace();
        }



        System.out.println("connected to " + clientHandler.getUserName() + " on socket " + clientHandler.getSocket().getInetAddress());

        try {
            clientEnterInLobby(clientHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }


        }


}