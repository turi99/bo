package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.ExceptionCardNotFound;
import it.polimi.ingsw.utils.observer.Observable;

import java.io.Serializable;
import java.util.ArrayList;

public class Deck implements Serializable {
    private ArrayList<Card> cards;
    private String name;

    public Deck() {
        cards=new ArrayList<>();
        cards.add(new Card(1,1));
        cards.add(new Card(2,1));
        cards.add(new Card(3,2));
        cards.add(new Card(4,2));
        cards.add(new Card(5,3));
        cards.add(new Card(6,3));
        cards.add(new Card(7,4));
        cards.add(new Card(8,4));
        cards.add(new Card(9,5));
        cards.add(new Card(10,5));

        name = "name";
    }

    /**
     *
     * @return name of the deck
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return list of all card in the deck
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * return the given card and remove it from the deck
     * @param card card of interest
     * @return the given card, if it's present
     * @throws ExceptionCardNotFound if the given card it isn't present
     */
    public Card useCard(Card card) throws ExceptionCardNotFound {
        int i;

        for(i=0; i<cards.size() && !cards.get(i).equals(card) ;i++){}

        if(i==cards.size()){
            throw new ExceptionCardNotFound();
        }

        Card c = cards.get(i);
        cards.remove(i);

        return c;

    }

    /**
     * return a card with the same value of valCard and remove it from the deck
     * @param valCard card of interest
     * @return return a card with the same value of valCard, if it's present
     * @throws ExceptionCardNotFound if there isn't a card with the same value of valCard
     */
    public Card useCard(int valCard) throws ExceptionCardNotFound {

        int i;

        for(i=0; i<cards.size() && cards.get(i).getVal() != valCard ;i++){}

        if(i==cards.size()){
            throw new ExceptionCardNotFound();
        }

        Card c = cards.get(i);
        cards.remove(i);

        return c;
    }


    @Override
    public String toString(){
        if(cards.size()==0)return "empty";
        else {
            String s = "\t" + cards.get(0).toString();
            for (int i = 1; i < cards.size(); i++) {
                s = "\t" + cards.get(i) + "\n" + s;
            }
            return s;
        }

    }


    /**
     *
     * @return cards.size()==0
     */
    public boolean thereAreCards() {
        return cards.size()==0;
    }
}

