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

public class CardStudentOnIsland extends CharacterCard{

    ArrayList<Student> students = new ArrayList<>(); //contenute 4, movimento 1

    public CardStudentOnIsland(ModelPro model) {
        super(
                "Choose a student from the card and place him on an island of your choice",
                1,
                9,
                model
        );

    }

    // tipologia stringa richiesta : coloreStudente,numeroIsola
    /**
     *
     * @param parameters parameters of activation
     * @return parameters == "x,y" ,
     * where x is an integer representing a color of a student in the card
     * and, y is ann integer representing index of an island
     */
    @Override
    public boolean checkParameters(String parameters) {
        // tipologia stringa richiesta : coloreStudente,numeroIsola
        boolean trueColor = false;
        boolean trueIsland= false;

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

        if(strings.length != 2)
            return false;


        int color = -1;
        try {
            color = Integer.parseInt(strings[0]);
        }catch (NumberFormatException e){
            return false;
        }
        if(color<0 || color>=Col_Pawn.values().length){
            return false;
        }

        for(Col_Pawn c : Col_Pawn.values()){
            if( c.ordinal() == color  &&  students.contains(new Student(Col_Pawn.values()[color]))) {
                trueColor = true;
                break;
            }
        }

        try {
            if (0 <= Integer.parseInt(strings[1]) && Integer.parseInt(strings[1]) < model.getIslands().size())
                trueIsland = true;
        }catch (NumberFormatException e){
            return false;
        }

        return trueColor && trueIsland;
    }

    /**
     * add 4 student to students
     * @return this model
     */
    @Override
    public ModelPro decorate() {
        System.out.println("decoration "+getIdCard());


        students.addAll(model.getBag().pickRandomStudent(4));

        return model;
    }

    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        String parameters = "";
        int[] stud =new int[5];
        for(int i=0;i<5;i++){
            stud[i]=0;
        }
        for(Student s:students){
            stud[s.getColor().ordinal()]+=1;
        }
        for(int i=0;i<5;i++){
            System.out.println(i+")"+Col_Pawn.values()[i]+" : "+stud[i]);
        }
        System.out.println("Insert the color of student");

        while (!bufferedReader.ready()){
            if(thread.isInterrupted())return "";
        }
        parameters=bufferedReader.readLine();
        System.out.println("Insert the number oh the island (0-"+(model.getIslands().size()-1)+")");

        while (!bufferedReader.ready()){
            if(thread.isInterrupted())return "";
        }
        parameters=parameters+","+bufferedReader.readLine();
        return parameters;

    }


    // tipologia stringa richiesta : coloreStudente,numeroIsola
    /**
     * if checkParameters is true and the player can pay, the card is activated
     * @param parameters parameters of activation
     * @throws ExceptionNoTowerInBoard
     * @throws ExceptionLaneNotFound
     */
    @Override
    public void activate(String parameters) throws ExceptionNoTowerInBoard, ExceptionLaneNotFound {
        // tipologia stringa richiesta : coloreStudente,numeroIsola

        if(checkParameters(parameters)) {
            super.activate(parameters);

            String[] strings = parameters.split(",");
            int color = Integer.parseInt(strings[0]);
            int isl = Integer.parseInt(strings[1]);

            for (Student s : students) {
                if (s.getColor() == Col_Pawn.values()[color]) {
                    model.getIslands().get(isl).addStudent(s);
                    students.remove(s);
                    students.addAll(model.getBag().pickRandomStudent(1));
                    break;
                }

            }
        }
    }


    public ArrayList<Student> getStudents() {
        return students;
    }
}
