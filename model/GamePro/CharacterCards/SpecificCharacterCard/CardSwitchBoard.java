package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.Exceptions.ExceptionIllegalArgument;
import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.Student;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardSwitchBoard extends CharacterCard{

    ArrayList<Student> students = new ArrayList<>(); //contenute 6, muovi 3

    public CardSwitchBoard(ModelPro model) {
        super(
                "choose up to three students from the certain one and exchange them with the same number from the card",
                1,
                10,
                model
        );
    }


    //formato stringa: 3coloriFromCarta,3coloriFromBoard

    /**
     *
     * @param parameters parameters of activation
     * @return parameters == "x,y" || parameters == "x,k,y,z" || parameters == "x,k,m,y,z,n",
     * where x and k and m are integer representing color of student on the card
     * and, y and z and n are integer representing color of student in board
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
        System.out.println("split lenght " + strings.length);
        if(strings.length%2 != 0 || strings.length==0 || strings.length>6)
            return false;


        int[] colorStud = new int[strings.length];

        for( int i=0; i<strings.length; i++){
            try {
                colorStud[i] = Integer.parseInt(strings[i]);
            }catch (NumberFormatException e){
                return false;
            }
        }

        for( int color : colorStud){
            if( ! (color>=0 && color<Col_Pawn.values().length) ) return false;
        }

        for(int i=0; i<colorStud.length/2; i++){
            if( !(students.contains(new Student(Col_Pawn.values()[colorStud[i]]))) ) return false;
        }
        for(int i=colorStud.length/2; i<colorStud.length; i++){
            if( ! (model.getCurrentPlayer().getOwnBoard().getStudents().contains(new Student(Col_Pawn.values()[colorStud[i]]))) ) return false;
        }


        return true;
    }

    /**
     * add 6 student to students
     * @return this model
     */
    @Override
    public ModelPro decorate() {
        System.out.println("decoration "+getIdCard());


        students.addAll(model.getBag().pickRandomStudent(6));

        return model;
    }


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

            ArrayList<Student> studCard = new ArrayList<Student>();
            ArrayList<Student> studBoard = new ArrayList<Student>();

            int[] colorStud = new int[strings.length];
            for (int i = 0; i < strings.length; i++) {
                colorStud[i] = Integer.parseInt(strings[i]);
            }
            for (int i = 0; i < colorStud.length / 2; i++) {
                studCard.add(new Student(Col_Pawn.values()[colorStud[i]]));
            }
            for (int i = colorStud.length / 2; i < colorStud.length; i++) {
                studBoard.add(new Student(Col_Pawn.values()[colorStud[i]]));
            }

            for (Student s : studBoard)
                model.getCurrentPlayer().getOwnBoard().getStudents().remove(s);
            model.getCurrentPlayer().getOwnBoard().getStudents().addAll(studCard);

            for (Student s : studCard)
                students.remove(s);
            students.addAll(studBoard);

        }
    }


    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        String parameters="";
        int[] studH=new int[5];
        for (int i = 0; i < 5; i++) {
            studH[i] = 0;
        }

        for (Student s : model.getCurrentPlayer().getOwnBoard().getStudents()) {
            studH[s.getColor().ordinal()] += 1;
        }
        int[] stud = new int[5];
        for (int i = 0; i < 5; i++) {
            stud[i] = 0;
        }
        for (Student s : students) {
            stud[s.getColor().ordinal()] += 1;
        }
        System.out.println("How many students do you wont to switch? (MAX 3)");
        int num=0;
        boolean exit = false;
        while (!exit) {

            while (!bufferedReader.ready()){
                if(thread.isInterrupted())return "";
            }
            String in = bufferedReader.readLine();
            try {
                num = Integer.parseInt(in);
                exit = ( num >=0 && num<=3 );
            }catch (Exception e){
                exit = false;
            }
        }
        String[] students=new String[num];
        String[] hall =new String[num];
        for(int i=0;i<num;i++) {
            for (int j = 0; j < 5; j++) {
                System.out.println(j + ")" + Col_Pawn.values()[j] + " : " + stud[j]);
            }
            System.out.println("Insert the color of student from thr card");
            String color = null;
            exit = false;
            while (!exit){
                exit = true;

                while (!bufferedReader.ready()){
                    if(thread.isInterrupted())return "";
                }
                color = bufferedReader.readLine();
                try {
                    stud[Integer.parseInt(color)] -= 1;
                }catch (Exception e){
                    exit = false;
                }
            }
            students[i]=color;

            for (int j = 0; j < 5; j++) {
                System.out.println(j + ")" + Col_Pawn.values()[j] + " : " + studH[j]);
            }
            System.out.println("Insert the color of student of hall");
             exit = false;
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
            hall[i] = color;

        }
        parameters=students[0];
        for(int i=1;i<num;i++){
            parameters=parameters+","+students[i];
        }
        for(int i=1;i<num;i++){
            parameters=parameters+","+ hall[i];
        }
        return parameters;

    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
