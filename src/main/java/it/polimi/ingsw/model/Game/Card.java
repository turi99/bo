package it.polimi.ingsw.model.Game;


import java.io.Serializable;

public class Card implements Serializable {
    private final int valCard;
    private final int mov;

    public Card(int valCard, int mov) {
        this.valCard = valCard;
        this.mov = mov;
    }

    public Card(Card card) {
        this.valCard = card.valCard;
        this.mov = card.mov;
    }

    /**
     *
     * @return value of the card
     */
    public int getVal() {
        return valCard;
    }

    /**
     *
     * @return movement of the card
     */
    public int getMov() {
        return mov;
    }

    /**
     *
     * @return valCard == o.valCard && mov == o.mov
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return valCard == card.valCard && mov == card.mov;
    }

    @Override
    public String toString(){
        return "Value : "+ valCard +" , move : "+mov;
    }
}
