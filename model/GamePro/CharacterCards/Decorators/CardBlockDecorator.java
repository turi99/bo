package it.polimi.ingsw.model.GamePro.CharacterCards.Decorators;

import it.polimi.ingsw.model.Game.AggIsland;
import it.polimi.ingsw.model.Game.Tower;
import it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard.CardBlock;

import java.util.ArrayList;

public class CardBlockDecorator extends AggIslandsDecorator{
    private boolean ban;

    public CardBlockDecorator(AggIsland aggIsland, CardBlock card) {
        super(aggIsland, card);
        ban = false;
    }

    /**
     *
     * @return true if the aggregation can't be conquered, false else
     */
    public boolean getBan() {
        return ban;
    }

    /**
     * set the aggregation ban
     */
    public void setBan(boolean ban) {
        this.ban = ban;
    }

    /**
     * if the aggregation is baned, remove the ban. else, same as Model
     */
    @Override
    public ArrayList<Tower> conquer(ArrayList<Tower> towers) {
        if(ban) {
            setBan(false);
            ((CardBlock)card).setBlockCard(((CardBlock)card).getBlockCard()+1);
            return new ArrayList<>();
        }
        return aggIsland.conquer(towers);
    }

    @Override
    public String toString() {
        return aggIsland.toString()+"\n" +
                "block tail : "+getBan();
    }
}
