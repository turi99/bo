package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.Island;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardIgnoreStudentDecorator;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardIgnoreStudent extends CharacterCard {
    private Col_Pawn student;

    public CardIgnoreStudent(ModelPro model) {
        super("for one turn the students of the selected color will be ignored",
                3,
                2,
                model);
    }

    /**
     *
     * @return this model with Island of type CardIgnoreStudentDecorator
     */
    @Override
    public ModelPro decorate() {

        System.out.println("decoration "+getIdCard());
        for(int i=0; i<model.getIslands().size(); i++){
            ArrayList<Island> islands = new ArrayList<>();
            islands.add(new CardIgnoreStudentDecorator(model.getIslands().get(i).getIslands().get(0), this));
            model.getIslands().get(i).setIslands(islands);
        }

        return model;
    }

    /**
     *
     * @return this student
     */
    public Col_Pawn getStudent() {
        return student;
    }

    /**
     *
     * @param parameters parameters of activation
     * @return parameters="x" where x is an integer representing a color of a student
     */
    @Override
    public boolean checkParameters(String parameters) {
        int x=Integer.parseInt(parameters);
        for(Col_Pawn c: Col_Pawn.values()){
            if(c.ordinal()==x)return true;
        }
        return false;
    }

    public Col_Pawn traduce(String parameters){
        return Col_Pawn.values()[Integer.parseInt(parameters)];
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
            student = traduce(parameters);
        }
    }

    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        String parameters;
        System.out.println("Insert the color of student that you want to ignore");
        for(Col_Pawn c:Col_Pawn.values()){
            System.out.println(c.ordinal()+")"+c);
        }

        while (!bufferedReader.ready()){
            if(thread.isInterrupted())return "";
        }
        parameters=bufferedReader.readLine();
        return parameters;

    }



}

