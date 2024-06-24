package it.polimi.ingsw.model.GamePro.StatePro;

import it.polimi.ingsw.model.Exceptions.ExceptionCardNotFound;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.State.State;
import it.polimi.ingsw.model.Game.State.StatePlayCard;
import it.polimi.ingsw.model.Game.State.StateTurn;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelPro;

public class StateProPlayCard extends StatePlayCard implements StatePro {


    public StateProPlayCard(ModelPro model) {
        super(model);
    }


    /**
     * the method do nothing
     */
    public void activateCharacterCard(int idCard, String parameters) {
        model.notify(model.getCurrentPlayerPos()+",Impossible activate now!");
    }

    /**
     *
     * @return if the current player is the last, StateProTurn. else, this
     */
    @Override
    public State nextState(){
        State nextState;

        if(model.isThisLastPlayer()){
            model.newOrder();
            nextState = new StateProTurn((ModelPro) model);
        }else{
            model.nextPlayer();
            nextState = this;
        }

        return  nextState;

    }

}
