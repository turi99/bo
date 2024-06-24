package it.polimi.ingsw.model.GamePro.StatePro;

import it.polimi.ingsw.model.Exceptions.ExceptionIslandNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionStudentNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionWrongStudentColor;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.State.State;
import it.polimi.ingsw.model.Game.State.StateMoveMotherNature;
import it.polimi.ingsw.model.Game.State.StateTurn;
import it.polimi.ingsw.model.ModelPro;

public class StateProTurn extends StateTurn implements StatePro {

    protected boolean characterActivable;

    public StateProTurn(ModelPro model) {
        super(model);

        characterActivable = true;
    }


    /**
     * method to allow the current player to activate a character card
     * @param idCard id of the card you want to activate
     * @param parameters string of activation
     */
    public void activateCharacterCard(int idCard, String parameters) {

        System.out.println(characterActivable);
        if( characterActivable ){
            try {

                ((ModelPro) model).activateCard(idCard, parameters);

                characterActivable = false;

                model.setState(nextState());
                model.notify(model.getPlayers());
                model.notify(model.getIslands());
                model.notify(model.getState());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            model.notify(model.getCurrentPlayerPos()+",Impossible activate now!");
        }
    }

    /**
     *
     * @return if numOfPawnPlayed < maxPawnPlayable, this. else, StateProMoveMotherNature
     */
    @Override
    public State nextState(){
        if(numOfPawnPlayed < maxPawnPlayable){
            return this;
        }else{
            ((ModelPro)model).deactivateAllActiveGameCards();
            model.notify(((ModelPro)model).getActiveGameCards());
            return new StateProMoveMotherNature((ModelPro) model);
        }

    }
}
