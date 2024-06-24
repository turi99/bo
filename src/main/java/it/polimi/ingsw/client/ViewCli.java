package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.ModelView;
import it.polimi.ingsw.utils.observer.Observer;

import java.io.*;
import java.util.Objects;

public class ViewCli implements  Observer<Object>{

    private ModelView model;
    private final PrintStream socketOut;
    private final ObjectInputStream in;
    private Thread readObj;
    private final BufferedReader bufferedReader;
    private Thread menu;


    public ViewCli( PrintStream socketOut,ObjectInputStream in,BufferedReader bufferedReader){
        this.socketOut=socketOut;
        this.in=in;
        this.bufferedReader=bufferedReader;
        readObj=readInObj(this.in);
        readObj.setPriority(Thread.MAX_PRIORITY);
    }



    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void print() throws InterruptedException {
            menu=new Thread(() -> {
                try {
                    menu();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            });



        System.out.println("|**************************************|");
        System.out.println("If you want to exit from the current game type \"quit\"");
        System.out.println("State : " + model.getState().getName());
        if (!Objects.equals(model.getState().getName(), "win")) {
            System.out.println("Id : " + model.getId());
            System.out.println("Current Player : " + model.getCurrentPlayer());
        }
        System.out.println("|**************************************|");
        if (model.getState().getName().equals("win")) {
            readObj.interrupt();
            System.out.println("End game");
            if(model.getColorTeamWinner()==null) System.out.println("Par");
            else if (model.getColorTeamWinner() == model.getMyColor()) {
                System.out.println("You win!!!");
            } else System.out.println("You lose");
        } else {
            menu.start();
        }
    }

    /**
     * show to the user the play menu and allow him to play
     * @throws IOException error while reading user input
     */
    public void playMenu() throws IOException {
        String choose="";
        while (!choose.equals("5")) {
        System.out.println(
                "1) Show info about other players\n" +
                        "2) Show info about islands\n" +
                        "3) Show info about cloud\n" +
                        "4) Show info about me\n" +
                        "5) play\n");
            while (!bufferedReader.ready()){
                if(menu.isInterrupted())break;
            }
            if(menu.isInterrupted())break;
            synchronized (bufferedReader){
                choose =bufferedReader.readLine();
            }
            if (choose.equals("quit")){
                readObj.interrupt();
                socketOut.println(choose);
                getMenu().interrupt();
                break;
            }
            System.out.println();

            switch (choose) {
                case "1" -> {
                    for (Player p : model.getOtherPlayers()) {
                        System.out.println(p);
                        System.out.println();
                    }
                }
                case "2" -> {
                    int id = 0;
                    for (AggIsland a : model.getAggIslands()) {
                        System.out.println("id : " + id);
                        System.out.println(a);
                        System.out.println();
                        id += 1;
                    }
                }
                case "3" -> {
                    int id = 0;
                    for (Cloud c : model.getClouds()) {
                        System.out.println("id : " + id);
                        System.out.println(c);
                        System.out.println();
                        id += 1;
                    }
                }
                case "4" -> System.out.println(model.getPlayer());
                case "5" -> readCommand();
            }

    }

    }

    /**
     * start the readObj thread
     * @param model
     * @throws InterruptedException
     */
    public synchronized void startViewCli(ModelView model) throws InterruptedException {
        this.model=model;
        readObj.start();
        print();
        wait();
    }

    /**
     *
     * @return readObj
     */
    public Thread getReadObj() {
        return readObj;
    }

    /**
     * create the thread readObj, it will read from the socketIn and update the model with the objects received
     * @param socketIn source of data
     * @return readObj with
     */
    public Thread readInObj(final ObjectInputStream socketIn){
         readObj = new Thread(() -> {
             synchronized (this) {
                 while (!readObj.isInterrupted()) {
                         try {
                             Object o = socketIn.readObject();
                             model.update(o);
                         } catch (IOException e) {
                             e.printStackTrace();
                             break;
                         } catch (ClassNotFoundException e) {
                             e.printStackTrace();
                         }
                     }
                     notifyAll();
                 }
         });
        return readObj;

    }

    /**
     *
     * @return menu
     */
    public Thread getMenu() {
        return menu;
    }

    /**
     * show to the user a menu that allow to show infos about the game
     * @throws IOException error while reading user input
     */
    public void infoMenu() throws IOException {
            String choose ;
            while (true) {
                System.out.println(
                        "1) Show info about other players\n" +
                                "2) Show info about islands\n" +
                                "3) Show info about cloud\n" +
                                "4) Show info about me\n");
                while (!bufferedReader.ready()){
                    if(menu.isInterrupted())break;
                }
                if(menu.isInterrupted())break;
                synchronized (bufferedReader) {

                    choose =bufferedReader.readLine();

                }
                if (choose.equals("quit")) {
                    readObj.interrupt();
                    socketOut.println(choose);
                    getMenu().interrupt();
                    break;
                }

                switch (choose) {
                    case "1" -> {
                        for (Player p : model.getOtherPlayers()) {
                            System.out.println(p);
                            System.out.println();
                        }
                    }
                    case "2" -> {
                        int id = 0;
                        for (AggIsland a : model.getAggIslands()) {
                            System.out.println("id : " + id);
                            System.out.println(a);
                            System.out.println();
                            id += 1;
                        }
                    }
                    case "3" -> {
                        int id = 0;
                        for (Cloud c : model.getClouds()) {
                            System.out.println("id : " + id);
                            System.out.println(c);
                            System.out.println();
                            id += 1;
                        }
                    }
                    case "4" -> System.out.println(model.getPlayer());
                }
            }

    }


    /**
     * if is this player turn show playMenu(), else infoMenu()
     * @throws InterruptedException error in thread
     * @throws IOException error while reading user input
     */
    public void menu() throws InterruptedException, IOException {
            if (model.getId() == model.getCurrentPlayer())playMenu();
            else infoMenu();
    }

    /**
     *
     * @return model
     */
    public ModelView getModel() {
        return model;
    }

    /**
     *
     * @return socketOut
     */
    public PrintStream getSocketOut() {
        return socketOut;
    }

    /**
     *
     * @return in
     */
    public ObjectInputStream getIn() {
        return in;
    }

    /**
     * request the user to insert data for a specific state
     * @throws IOException error while reading user input
     */
    public  void readCommand() throws IOException {
        System.out.println(model.getState().getName());
        switch (model.getState().getName()) {
            case "playCard" -> {
                if (model.thereIPlayedCard()) {
                    for (PlayedCard p : model.getPlayedCard()) {
                        System.out.println(p);
                    }
                    System.out.println();
                } else {
                    System.out.println("There aren't played card,you are the first\n");
                }
                System.out.println(model.getPlayer().getOriginalDeck() + "\n");
                System.out.println("Choose a card by value\n");
                String card;
                while (!bufferedReader.ready()){
                    if(readObj.isInterrupted())return;
                }
                card =bufferedReader.readLine();

                System.out.println();
                socketOut.println("0;" + card);
            }
            case "selectCloud" -> {
                int id = 0;
                for (Cloud c : model.getClouds()) {
                    System.out.println("id : " + id);
                    System.out.println(c + "\n");
                    id++;
                }
                System.out.println("Choose a claud\n");

                while (!bufferedReader.ready()){
                    if(readObj.isInterrupted())return;
                }
                String claud =bufferedReader.readLine();
                System.out.println();
                socketOut.println("1;" + claud);
            }
            case "moveMotherNature" -> {
                int id = 0;
                int idM = 0;
                for (AggIsland i : model.getAggIslands()) {
                    System.out.println("Id : " + id + "\n" + i + "\n");
                    if (i.hasMotherNature()) idM = id;
                    id++;
                }
                System.out.println("Current position of mother nature is : " + idM + "\nChoose next island (MAX INCREMENT = " + model.getPlayedCardPlayer().getCard().getMov() + " )\n");

                while (!bufferedReader.ready()){
                    if(readObj.isInterrupted())return;
                }
                String movement =bufferedReader.readLine();
                System.out.println();
                socketOut.println("2;" + movement);
            }
            case "turn" -> {
                String choose="";
                while (!choose.equals("0") && !choose.equals("1")) {
                    System.out.println("island or lane? ( island : 0 | lane : 1 )\n");

                    while (!bufferedReader.ready()){
                        if(readObj.isInterrupted())return;
                    }
                    choose=bufferedReader.readLine();
                }
                System.out.println();
                switch (choose) {
                    case "1" -> {
                        int[] stud = new int[5];
                        String s;
                        for (Student studO : model.getPlayer().getOwnBoard().getStudents()) {
                            stud[studO.getColor().ordinal()] += 1;
                        }
                        s = "(" + Col_Pawn.values()[0].ordinal() + ") " + Col_Pawn.values()[0] + " : " + stud[0];
                        for (int i = 1; i < 5; i++) {
                            s = "(" + Col_Pawn.values()[i].ordinal() + ") " + Col_Pawn.values()[i] + " : " + stud[i] + "\n" + s;
                        }
                        System.out.println(s);
                        System.out.println("Insert student's color\n");

                        while (!bufferedReader.ready()){
                            if(readObj.isInterrupted())return;
                        }
                        String color =bufferedReader.readLine();
                        socketOut.println("4;" + color);
                    }
                    case "0" -> {
                        System.out.println("Insert id island (0-" + (model.getAggIslands().size() - 1) + ")\n");
                        String id =bufferedReader.readLine();
                        System.out.println();
                        int[] stud = new int[5];
                        String s;
                        for (Student studO : model.getPlayer().getOwnBoard().getStudents()) {
                            stud[studO.getColor().ordinal()] += 1;
                        }
                        s = "(" + Col_Pawn.values()[0].ordinal() + ")" + Col_Pawn.values()[0] + "," + stud[0];
                        for (int i = 1; i < 5; i++) {
                            s = "(" + Col_Pawn.values()[i].ordinal() + ")" + Col_Pawn.values()[i] + "," + stud[i] + "\n" + s;
                        }
                        System.out.println(s);
                        System.out.println("Insert student's color\n");

                        while (!bufferedReader.ready()){
                            if(readObj.isInterrupted())return;
                        }
                        String color =bufferedReader.readLine();
                        socketOut.println("3;" + id + "," + color);
                    }
                }
            }
        }

    }

    @Override
    public void update(Object message){
        if(message != null && message.getClass() == Boolean.class){
            if((Boolean)message){
                getReadObj().interrupt();
                getMenu().interrupt();
            }
        }else {
            try {
                if (menu != null) {
                    menu.interrupt();
                    menu.join();
                }
                print();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
