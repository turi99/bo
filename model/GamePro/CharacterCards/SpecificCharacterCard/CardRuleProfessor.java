package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardRuleProfDecorator;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;

public class CardRuleProfessor extends CharacterCard {

    public CardRuleProfessor(ModelPro model) {
        super(
                "when this card is active, the professor is taken even in the event of a tie",
                2,
                7,
                model
        );
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
     * @return CardRuleProfDecorator
     */
    @Override
    public ModelPro decorate() {
        System.out.println("decoration "+getIdCard());

        model = new CardRuleProfDecorator(model, this);

        model.getState().setModel(model);

        return model;
    }

    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        return "";
    }


}
