package it.polimi.ingsw.model.Game.State;

import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Model;

public class StatePlayCard extends State{


    public StatePlayCard(Model model) {
        super(model, "playCard");
    }

    /**
     * the current player play a card
     * @param valCard value of the card you want to play
     */
    @Override
    public void playCard(int valCard) {
        try {

            model.playCard(valCard);
            model.setState(nextState());

            model.notify(model.getState());


        } catch (Exception e) {
            model.notify(
                    ""+model.getCurrentPlayerPos()+","+
                            e.getMessage()
            );
        }
    }

    /**
     * method do nothing
     */
    public void moveStudentToLane(Col_Pawn colorStudent) {
        System.out.println("impossible move Student to Lane now!");
    }
    /**
     * method do nothing
     */
    public void moveStudentToIsland(int indexIsland, Col_Pawn colorStudent) {
        System.out.println("impossible move Student to Island now!");

    }
    /**
     * method do nothing
     */
    public void moveMotherNature(int newPos) {
        System.out.println("impossible move Mother Nature now!");

    }
    /**
     * method do nothing
     */
    public void chooseCloud(int cloud) {
        System.out.println("impossible choose Cloud now!");
    }

    /**
     *
     * @return if the current player is the last, StateTurn. else, this
     */
    public State nextState(){
        State nextState;

        if(model.isThisLastPlayer()){
            model.newOrder();
            nextState = new StateTurn(model);
        }else{
            model.nextPlayer();
            nextState = this;
        }

        return  nextState;

    }

    /**
     *
     * @return "0"
     */
    @Override
    public String toString() {
        return String.valueOf(0);
    }


}
