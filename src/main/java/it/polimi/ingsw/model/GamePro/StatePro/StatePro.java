package it.polimi.ingsw.model.GamePro.StatePro;

import it.polimi.ingsw.model.Game.State.State;
import it.polimi.ingsw.model.ModelPro;

public interface StatePro{

    /**
     * method that allow the current player to activate a character card
     * @param idCard id of the character card of interest
     * @param parameters string with the activation parameters
     */
    public abstract void activateCharacterCard(int idCard, String parameters);

}
