package it.polimi.ingsw.model.GamePro.StatePro;

import it.polimi.ingsw.model.Exceptions.ExceptionCloudNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionCloudYetChoose;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.PlayedCard;
import it.polimi.ingsw.model.Game.State.*;
import it.polimi.ingsw.model.ModelPro;

public class StateProSelectCloud extends StateSelectCloud implements StatePro {


    public StateProSelectCloud(ModelPro model) {
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
     * @return if the current player is the last, StateProPlayCard. else, StateProTurn
     */
    @Override
    public State nextState() {
        State nextState;

        if(model.isThisLastPlayer()){
            if(model.isLastTurn()){
                nextState=new StateWin(model);
            }
            else {
                nextState = new StateProPlayCard((ModelPro) model);
                if(model.getBag().getNumberOfStudents()>=model.getClouds().length*model.getClouds()[0].getDim()){
                    model.fillCloud();
                }else model.setFlagBagEmpty(true);
                model.notify(model.arrayToArrayList(model.getClouds()));
                for(PlayedCard p:model.getPlayedCards()){
                    p.setCard(null);
                }
                model.notify(model.getPlayedCards());
            }
        }
        else{
            nextState = new StateProTurn((ModelPro) model);
        }
        model.nextPlayer();

        return  nextState;
    }
}


