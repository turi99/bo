package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.Game.Island;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardIgnoreTowerDecorator;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardIgnoreTowers extends CharacterCard {

    public CardIgnoreTowers(ModelPro model) {
        super("when counting the influence on an Island or a group of islands,the towers present are not calculated",
                3,
                3,
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
     * @return this model with Island of type CardIgnoreTowerDecorator
     */
    public ModelPro decorate() {

        System.out.println("decoration "+getIdCard());
        for(int i=0; i<model.getIslands().size(); i++){
            ArrayList<Island> islands = new ArrayList<>();
            islands.add(new CardIgnoreTowerDecorator(model.getIslands().get(i).getIslands().get(0), this));
            model.getIslands().get(i).setIslands(islands);
        }

        return model;
    }

    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        return "";

    }
}
