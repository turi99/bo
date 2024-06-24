package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;
import it.polimi.ingsw.model.Game.AggIsland;
import it.polimi.ingsw.model.Game.Team;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public class CardStartConquer extends CharacterCard {

    public CardStartConquer(ModelPro model) {
        super(
                "By activating the card the conquest on an island of your choice is started",
                3,
                8,
                model
        );
    }

    /**
     *
     * @param parameters parameters of activation
     * @return parameters=="x", where x is and integer representing index of an island
     */
    @Override
    public boolean checkParameters(String parameters) {
        boolean result = false;
        try{
            result = ( 0 <= Integer.parseInt(parameters) && Integer.parseInt(parameters) < model.getIslands().size() );
        }catch (NumberFormatException e){
            return false;
        }
        return result;
    }

    /**
     *
     * @return this model
     */
    @Override
    public ModelPro decorate() {
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

            AggIsland island = model.getIslands().get(Integer.parseInt(parameters));


            Optional<Team> conquerTeam = island.getTeamMaxInfluence(model.getTeams());
            if (conquerTeam.isPresent())
                island.conquer(conquerTeam.get().drawTowers(island.getNumIslands()));
        }
    }

    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        String parameters;
        System.out.println("Insert the number of the island where you wont to start the conquer (0-"+(model.getIslands().size()-1)+")");

        while (!bufferedReader.ready()){
            if(thread.isInterrupted())return "";
        }
        parameters=bufferedReader.readLine();
        return parameters;

    }

}
