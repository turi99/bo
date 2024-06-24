package it.polimi.ingsw.model.GamePro.CharacterCards.Decorators;

import it.polimi.ingsw.model.Exceptions.ExceptionIslandNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;
import it.polimi.ingsw.model.Exceptions.ExceptionMotherNatureIllegalMovement;
import it.polimi.ingsw.model.Exceptions.ExceptionsNoSuchTowers;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.ModelPro;

public class CardTwoAdditionalMovementDecorator extends ModelProDecorator {


    public CardTwoAdditionalMovementDecorator(ModelPro model, CharacterCard card) {
        super(model, card);
    }

    /**
     *
     * @return if the card is active, max move of the player + 2. else, max move of the payer
     */
    @Override
    public int getMaxMov(){
        int max=modelPro.getMaxMov();
        if (card.isActive()) {
            max += 2;
        }
        return max;
    }

    /**
     * same of Model
     */
    @Override
    public void moveMotherNature(int newPos) throws ExceptionNoTowerInBoard, ExceptionIslandNotFound, ExceptionMotherNatureIllegalMovement, ExceptionsNoSuchTowers {
        int currentPos = modelPro.getPosMotherNature();
        int maxMov = this.getMaxMov();
        int move;

        if (newPos >= currentPos) {
            move = newPos - currentPos;
        } else {
            move = modelPro.getIslands().size() + newPos - currentPos;
        }

        if(move<=maxMov){
            modelPro.moveMotherNature(newPos, modelPro.getTeams());
            modelPro.notify(modelPro.getMotherNature());
            modelPro.notify(arrayToArrayList(modelPro.getPlayers()));
            modelPro.notify(modelPro.getIslands());
        }else{
            throw new ExceptionMotherNatureIllegalMovement();
        }

    }
}
