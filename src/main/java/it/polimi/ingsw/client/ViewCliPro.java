package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Game.AggIsland;
import it.polimi.ingsw.model.Game.Cloud;
import it.polimi.ingsw.model.Game.Player;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.PlayerPro;
import it.polimi.ingsw.model.ModelViewPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

public class ViewCliPro extends ViewCli{
    public ViewCliPro(PrintStream socketOut, ObjectInputStream in, BufferedReader bufferedReader) {
        super(socketOut, in,bufferedReader);
    }

    /**
     * show to the user the play menu and allow him to play
     * @throws IOException error while reading user input
     */
    @Override
    public void playMenu() throws IOException {

        if (getModel().getId() == getModel().getCurrentPlayer()) {
            String choose ="";
            while (!choose.equals("6")) {
                if(getModel().getState().getName().equals("turn")) {
                    System.out.println(
                            "1) Show info about other players\n" +
                                    "2) Show info about islands\n" +
                                    "3) Show info about cloud\n" +
                                    "4) Show info about me\n" +
                                    "5) Show cards\n" +
                                    "6) play\n" +
                                    "7) activate card\n"
                    );
                }else{System.out.println(
                        "1) Show info about other players\n" +
                                "2) Show info about islands\n" +
                                "3) Show info about cloud\n" +
                                "4) Show info about me\n" +
                                "5) Show cards\n" +
                                "6) play\n"
                );

                } while (!getBufferedReader().ready()){
                    if(getMenu().isInterrupted())break;
                }
                if(getMenu().isInterrupted())break;
                System.out.println();
                synchronized (getBufferedReader()) {

                    choose =getBufferedReader().readLine();


                }
                if (choose.equals("quit")){
                    getReadObj().interrupt();
                    getSocketOut().println(choose);
                    getMenu().interrupt();
                    break;
                }
                switch (choose) {
                    case "1" -> {
                        for (Player p : getModel().getOtherPlayers()) {
                            System.out.println(p);
                            System.out.println();
                        }
                    }
                    case "2" -> {
                        int id = 0;
                        for (AggIsland a : getModel().getAggIslands()) {
                            System.out.println("id : " + id);
                            System.out.println(a);
                            System.out.println();
                            id += 1;
                        }
                    }
                    case "3" -> {
                        int id = 0;
                        for (Cloud c : getModel().getClouds()) {
                            System.out.println("id : " + id);
                            System.out.println(c);
                            System.out.println();
                            id += 1;
                        }
                    }
                    case "4" -> System.out.println(getModel().getPlayer());
                    case "5" -> {
                        for(CharacterCard c: ((ModelViewPro) getModel()).getCards()){
                            System.out.println(c+"\n");
                        }
                    }
                    case "6" -> readCommand();
                    case "7"-> {
                        if (getModel().getState().getName().equals("turn")) {
                            activateCard();
                        }
                    }
                }
            }
        }
    }

    private void activateCard() throws IOException {
        boolean exit=false;
        while (!exit) {
            for (CharacterCard c : ((ModelViewPro) getModel()).getCards()) {
                System.out.println(c + "\n");
            }
            System.out.println("choose by id ");

            while (!getBufferedReader().ready()){
                if(getReadObj().isInterrupted())return;
            }
            String id =getBufferedReader().readLine();
            for (CharacterCard c : ((ModelViewPro) getModel()).getCards()) {
                if (id.equals(String.valueOf(c.getIdCard()))) {
                    if(!((PlayerPro) getModel().getPlayer()).canPay(c.getCost())) {
                        System.out.println("You don't have enough coins!!");
                    }else {
                        String parameters = c.activationParameters(getBufferedReader(),getReadObj() );
                        if(getReadObj().isInterrupted())return;
                        getSocketOut().println("5;" + id + "," + parameters);
                    }
                    exit=true;
                }
            }
        }
    }

    /**
     * show to the user a menu that allow to show infos about the game
     * @throws IOException error while reading user input
     */
    @Override
    public void infoMenu() throws IOException {
        String choose ;
        while (true) {
            System.out.println(
                    "1) Show info about other players\n" +
                            "2) Show info about islands\n" +
                            "3) Show info about cloud\n" +
                            "4) Show info about me\n"+
                            "5) Show cards"
                    );
            while (!getBufferedReader().ready()){
                if(getMenu().isInterrupted())break;
            }
            if(getMenu().isInterrupted())break;
            synchronized (getBufferedReader()) {

                choose =getBufferedReader().readLine();

            }
            if (choose.equals("quit")){
                getReadObj().interrupt();
                getSocketOut().println(choose);
                getMenu().interrupt();
                break;
            }

            switch (choose) {
                case "1" -> {
                    for (Player p : getModel().getOtherPlayers()) {
                        System.out.println(p);
                        System.out.println();
                    }
                }
                case "2" -> {
                    int id = 0;
                    for (AggIsland a : getModel().getAggIslands()) {
                        System.out.println("id : " + id);
                        System.out.println(a);
                        System.out.println();
                        id += 1;
                    }
                }
                case "3" -> {
                    int id = 0;
                    for (Cloud c : getModel().getClouds()) {
                        System.out.println("id : " + id);
                        System.out.println(c);
                        System.out.println();
                        id += 1;
                    }
                }
                case "4" -> System.out.println(getModel().getPlayer());
                case "5" -> {
                    for(CharacterCard c: ((ModelViewPro) getModel()).getCards()){
                        System.out.println(c+"\n");
                    }
                }
            }
        }

    }


}
