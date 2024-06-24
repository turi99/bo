package it.polimi.ingsw.model.GamePro.StatePro;

import it.polimi.ingsw.model.Game.State.*;
import it.polimi.ingsw.model.ModelPro;

public class StateProMoveMotherNature extends StateMoveMotherNature implements StatePro {

    public StateProMoveMotherNature(ModelPro model) {
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
     * @return StateProSelectCloud
     */
    @Override
    public State nextState() {
        if(model.isFlagTowerOrIsland())
            return new StateWin(model);
        if(model.isFlagBagEmpty()){
            if(model.isThisLastPlayer()){
                return new StateWin(model);
            }else {
                model.nextPlayer();
                return new StateProTurn((ModelPro) model);
            }
        }
        return new StateProSelectCloud((ModelPro) model);
    }



}
