package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.Exceptions.*;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.Lane;
import it.polimi.ingsw.model.Game.Student;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardSwitchBoardLane  extends CharacterCard {

    //muovi 2

    public CardSwitchBoardLane(ModelPro model) {
        super(
                "Exchange 2 students from the lane with 2 as many from the hall",
                1,
                11,
                model
        );
    }

    //formato stringa: 2coloriFromBoard,2coloriFromLane

    /**
     *
     * @param parameters parameters of activation
     * @return parameters == "x,y" || parameters == "x,k,y,z",
     * where x and k are integer representing color of student in board
     * and y e z are integer representing color of student in lane
     */
    @Override
    public boolean checkParameters(String parameters) {
        if(parameters == null || parameters.equals("")) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument e) {
                model.notify(
                        "" + model.getCurrentPlayerPos() + "," +
                                e.getMessage()
                );
                return false;
            }
        }

        String[] strings = parameters.split(",");

        if(strings.length%2 != 0 || strings.length==0 || strings.length>6) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument e) {
                model.notify(
                        "" + model.getCurrentPlayerPos() + "," +
                                e.getMessage()
                );
                return false;
            }
        }


        int[] colorStud = new int[strings.length];

        for( int i=0; i<strings.length; i++){
            try {
                colorStud[i] = Integer.parseInt(strings[i]);
            }catch(NumberFormatException e){
                model.notify(
                        "" + model.getCurrentPlayerPos() + "," +
                                e.getMessage()
                );
                return false;
            }
        }

        for( int color : colorStud){
            if( ! (color>=0 && color< Col_Pawn.values().length) ) return false;
        }

        for(int i=0; i<colorStud.length/2; i++){
            if( ! (model.getCurrentPlayer().getOwnBoard().getStudents().contains(new Student(Col_Pawn.values()[colorStud[i]]))) ) return false;
        }



        //studenti presenti nelle lane
        int[] studPerLane = new int[Col_Pawn.values().length];

        for(int i=colorStud.length/2; i<colorStud.length; i++){
            studPerLane[colorStud[i]] ++ ;
        }

        for(int i=0; i<studPerLane.length; i++) {
            try {
                if( ! (model.getCurrentPlayer().getOwnBoard().selectLaneByColor(Col_Pawn.values()[i]).getNumStudents() >= studPerLane[i]) ){
                    return false;
                }
            } catch (ExceptionLaneNotFound e) {
                e.printStackTrace();
                return false;
            }
        }



        return true;
    }

    /**
     *
     * @return this model
     */
    @Override
    public ModelPro decorate() {
        System.out.println("decoration "+getIdCard());

        return model;
    }


    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        String parameters="";
        int[] studL = new int[5];
        int[] studH = new int[5];
        for (int i = 0; i < 5; i++) {
            studL[i] = 0;
        }
        for (int i = 0; i < 5; i++) {
            studH[i] = 0;
        }
        for (Student s : model.getCurrentPlayer().getOwnBoard().getStudents()) {
            studH[s.getColor().ordinal()] += 1;
        }
        for (Lane l : model.getCurrentPlayer().getOwnBoard().getLanes()) {
            studL[l.getColor().ordinal()]=l.getNumStudents();
        }
        String[] lane=new String[2];
        String[] board=new String[2];
        for(int i=0;i<2;i++) {
            for (int j = 0; j < 5; j++) {
                System.out.println(j + ")" + Col_Pawn.values()[j] + " : " + studH[j]);
            }
            System.out.println("Insert the color of student of hall");
            String color = null;
            boolean exit = false;
            while (!exit){
                exit = true;

                while (!bufferedReader.ready()){
                    if(thread.isInterrupted())return "";
                }
                color = bufferedReader.readLine();
                try{
                    studH[Integer.parseInt(color)]-=1;
                }catch (Exception e){
                    System.out.println();
                    exit = false;
                }
            }
            board[i]=color;
            for (int j = 0; j < 5; j++) {
                System.out.println(j + ")" + Col_Pawn.values()[j] + " : " + studL[j]);
            }
            System.out.println("Insert the color of student of lane");
            String color2 = null;
            exit = false;
            while (!exit){
                exit = true;

                while (!bufferedReader.ready()){
                    if(thread.isInterrupted())return "";
                }
                color2 = bufferedReader.readLine();
                try{
                    studL[Integer.parseInt(color2)]-=1;
                }catch (Exception e){
                    exit = false;
                }
            }
            lane[i]=color2;
        }
        parameters=board[0];
        for(int i=1;i<2;i++){
            parameters=parameters+","+board[i];
        }
        for(int i=0;i<2;i++){
            parameters=parameters+","+lane[i];
        }
        return parameters;
    }

    //formato stringa: 2coloriFromBoard,2coloriFromLane
    /**
     * if checkParameters is true and the player can pay, the card is activated
     * @param parameters parameters of activation
     * @throws ExceptionNoTowerInBoard
     * @throws ExceptionLaneNotFound
     */
    @Override
    public void activate(String parameters) throws ExceptionNoTowerInBoard, ExceptionLaneNotFound {
       if(checkParameters(parameters)) {
           super.activate(parameters);

           String[] strings = parameters.split(",");

           ArrayList<Student> studBoard = new ArrayList<Student>();
           ArrayList<Student> studLane = new ArrayList<Student>();

           int[] colorStud = new int[strings.length];
           for (int i = 0; i < strings.length; i++) {
               colorStud[i] = Integer.parseInt(strings[i]);
           }
           for (int i = 0; i < colorStud.length / 2; i++) {
               studBoard.add(new Student(Col_Pawn.values()[colorStud[i]]));
           }
           for (int i = colorStud.length / 2; i < colorStud.length; i++) {
               studLane.add(new Student(Col_Pawn.values()[colorStud[i]]));
           }


           //togli dalla board
           //metti nella lane
           for (Student s : studBoard) {
               try {
                   model.getCurrentPlayer().getOwnBoard().moveStudentToLane(s);
               } catch (ExceptionStudentNotFound e) {
                   e.printStackTrace();
               }
           }

           //togli dalla lane
           for (Student s : studLane) {
               model.getCurrentPlayer().getOwnBoard().selectLaneByColor(s.getColor()).getStudents().remove(s);
           }

           //metti nella board
           model.getCurrentPlayer().getOwnBoard().getStudents().addAll(studLane);

       }
    }

}
