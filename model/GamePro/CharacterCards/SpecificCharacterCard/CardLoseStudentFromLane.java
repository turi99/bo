package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.Exceptions.ExceptionIllegalArgument;
import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.Player;
import it.polimi.ingsw.model.Game.Student;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardLoseStudentFromLane extends CharacterCard {

    public CardLoseStudentFromLane(ModelPro model) {
        super("All players lose 3 students of the chosen color from the lane",
                3,
                5,
                model);
    }

    /**
     *
     * @param parameters parameters of activation
     * @return parameters == "x", where x is an integer that represent the color of students you want to remove from the lane
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
        int x=-1;
        try {
            x = Integer.parseInt(parameters);
        }catch (NumberFormatException e){
            return false;
        }
        for(Col_Pawn c: Col_Pawn.values()){
            if(x==c.ordinal())return true;
        }
        return false;
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
            Col_Pawn student = Col_Pawn.values()[Integer.parseInt(parameters)];
            for (Player p : model.getPlayers()) {
                ArrayList<Student> x = p.getOwnBoard().selectLaneByColor(student).getStudents();
                for (int i = 0; i < 3; i++) {
                    if (!x.isEmpty()) {
                        model.getBag().addStudent(x.get(0));
                        x.remove(0);
                    }
                }
            }
        }
    }

    /**
     *
     * @return this model
     */
    @Override
    public ModelPro decorate() {
        return model;
    }


    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        String parameters;
        System.out.println("Insert the color of student");
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
