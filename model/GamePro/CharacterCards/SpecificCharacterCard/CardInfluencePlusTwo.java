package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.Game.AggIsland;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardInfluencePlusTwoDecorator;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardInfluencePlusTwo extends CharacterCard {
    public CardInfluencePlusTwo(ModelPro model){
        super("In this turn, in the calculation of influence you have additional 2 points of influence",
                2,
                4,
                model);
    }

    /**
     *
     * @param parameters parameters of activation
     * @return true
     */
    @Override
    public boolean checkParameters(String parameters) {
        return true;
    }

    /**
     *
     * @return this model with AggIsland of type CardInfluencePlusTwoDecorator
     */
    public ModelPro decorate() {
        System.out.println("decoration "+getIdCard());
        ArrayList<AggIsland> islands = new ArrayList<>();
        for(int i=0; i<model.getIslands().size(); i++){
            islands.add(new CardInfluencePlusTwoDecorator(model.getIslands().get(i), this));
            if(model.getIslands().get(i).hasMotherNature())
                model.getMotherNature().setIsland(islands.get(i));
        }
        model.setIslands(islands);
        return model;
    }

    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        return "";
    }

}
