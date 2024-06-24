package it.polimi.ingsw.model.Game.State;

import it.polimi.ingsw.model.Exceptions.ExceptionCloudNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionCloudYetChoose;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.PlayedCard;
import it.polimi.ingsw.model.Model;

public class StateSelectCloud extends State{


    public StateSelectCloud(Model model) {
        super(model, "selectCloud");
    }

    /**
     * method do nothing
     */
    @Override
    public void playCard(int valCard) {
        System.out.println("impossible play a Card now!");
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
     * the current player choose a cloud
     * @param cloud chosen cloud
     */
    public void chooseCloud(int cloud) {
        try {

            model.chooseCloud(cloud);
            model.setState(nextState());
            model.notify(model.getState());

        } catch (ExceptionCloudYetChoose | ExceptionCloudNotFound e) {
            model.notify(
                    ""+model.getCurrentPlayerPos()+","+
                            e.getMessage()
             );
        }
    }

    /**
     *
     * @return if the game is finished, StateWin. if the current player is the last, StatePlayCard. else StateTurn
     */
    @Override
    public State nextState() {
        State nextState;

        if(model.isThisLastPlayer()){
            if(model.isLastTurn()){
                nextState=new StateWin(model);
            }
            else {
                nextState = new StatePlayCard(model);
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
            nextState = new StateTurn(model);
        }
        model.nextPlayer();

        return  nextState;
    }

    @Override
    public String toString() {
        return String.valueOf(1);
    }
}


