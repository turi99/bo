package it.polimi.ingsw.model.Game;

import java.io.Serializable;

public class PlayedCard implements  Comparable<PlayedCard>, Serializable {

    Player player;
    Card card;

    public PlayedCard(Player player, Card card){
        this.player = player;
        this.card = card;
    }

    public Player getPlayer() {
        return player;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }


    /**
     *
     * @param o PlayedCard to compare with
     * @return if(card.getVal ()<o.card.getVal ( ) ) return - 1. if(card . getVal()>o.card.getVal()) return 1. else return 0
     */
    @Override
    public int compareTo(PlayedCard o) {
        if(card.getVal()<o.card.getVal())return -1;
        else if(card.getVal()>o.card.getVal()) return 1;
        else return 0;
    }

    @Override
    public String toString(){
        return "id : "+String.valueOf(player.getID())+" ; "+card;
    }
}
