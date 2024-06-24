package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.Exceptions.ExceptionIllegalArgument;
import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;
import it.polimi.ingsw.model.Exceptions.ExceptionWrongStudentColor;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.Student;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardMoveLane extends CharacterCard {
    private ArrayList<Student> students=new ArrayList<>();

    public CardMoveLane(ModelPro model) {
        super("Move one student from card to lane",
                2,
                6,
                model);

    }

    /**
     *
     * @param parameters parameters of activation
     * @return parameters=="x", where x is a color of student in the card
     * @throws ExceptionLaneNotFound
     */
    @Override
    public boolean checkParameters(String parameters) throws ExceptionLaneNotFound {
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
        boolean checkColor=false;

        int x= -1;
        try {
            x = Integer.parseInt(parameters);
        }catch (NumberFormatException e){
            return false;
        }
        if(x<0 || x>= Col_Pawn.values().length){
            return false;
        }

        for(Col_Pawn c: Col_Pawn.values()){
            if(x==c.ordinal() && students.contains(new Student(c))){
                checkColor= true;
                break;
            }
        }
        if(checkColor)return !model.getCurrentPlayer().getOwnBoard().selectLaneByColor(Col_Pawn.values()[x]).full();
        return false;
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
            Col_Pawn color = Col_Pawn.values()[Integer.parseInt(parameters)];
            try {
                model.getCurrentPlayer().getOwnBoard().selectLaneByColor(color).addStudent(new Student(color));
                students.remove(new Student(color));
                students.addAll(model.getBag().pickRandomStudent(1));
                model.checkProf(color);
            } catch (ExceptionWrongStudentColor e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        String parameters;
        System.out.println("Insert the color of student to move");
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

        while (!bufferedReader.ready()){
            if(thread.isInterrupted())return "";
        }
        parameters=bufferedReader.readLine();
        return parameters;

    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
