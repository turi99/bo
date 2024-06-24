package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardTwoAdditionalMovementDecorator;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;

public class CardTwoAdditionalMovement extends CharacterCard {

    public CardTwoAdditionalMovement(ModelPro model) {
        super(
                "you can move mother nature up to two additional islands than indicated on the assistant card you played",
                1,
                12,
                model
        );
    }

    /**
     *
     * @return CardTwoAdditionalMovementDecorator
     */
    public ModelPro decorate() {
        System.out.println("decoration "+getIdCard());

        model = new CardTwoAdditionalMovementDecorator(model, this);

        model.getState().setModel(model);

        return model;
    }


    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        return "";
    }

    /**
     *
     * @return true
     */
    @Override
    public boolean checkParameters(String parameters) {
        return true;
    }




}
