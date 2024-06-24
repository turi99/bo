package it.polimi.ingsw.model.Game.State;

import it.polimi.ingsw.model.Exceptions.*;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelPro;

public class StateTurn extends State{

    protected int numOfPawnPlayed;
    protected int maxPawnPlayable;

    public StateTurn(Model model) {
        super(model, "turn");

        maxPawnPlayable = model.getClouds()[0].getDim();
        numOfPawnPlayed = 0;
    }

    /**
     * the method do nothing
     */
    @Override
    public void playCard(int valCard) {
        System.out.println("impossible now!");
    }

    /**
     * method for move a student of the current player from board to lane
     * @param colorStudent student color you want to move
     */
    @Override
    public void moveStudentToLane(Col_Pawn colorStudent) {
        try {

            if(numOfPawnPlayed < maxPawnPlayable){
                model.moveStudentToLane(colorStudent);
                numOfPawnPlayed++;
            }
            model.setState(nextState());
            model.notify(model.getState());

        } catch (ExceptionLaneNotFound | ExceptionStudentNotFound |  ExceptionPlayerCantPay e) {
            model.notify(
                    ""+model.getCurrentPlayerPos()+","+
                            e.getMessage()
             );
        }
    }

    /**
     * method for move a student of the current player from board to an island
     * @param indexIsland destination island
     * @param colorStudent student color you want to move
     */
    @Override
    public void moveStudentToIsland(int indexIsland, Col_Pawn colorStudent) {
        try {

            if(numOfPawnPlayed < maxPawnPlayable){
                model.moveStudentToIsland(indexIsland, colorStudent);
                numOfPawnPlayed++;
            }
            model.setState(nextState());
            model.notify(model.getState());

        } catch (ExceptionStudentNotFound | ExceptionIslandNotFound e) {
            model.notify(
                    e.getMessage()
             );
        }
    }

    /**
     * the method do nothing
     */
    @Override
    public void moveMotherNature(int newPos) {
        System.out.println("impossible now!");
    }

    /**
     * the method do nothing
     */
    @Override
    public void chooseCloud(int cloud) {
        System.out.println("impossible now!");
    }

    /**
     *
     * @return if numOfPawnPlayed < maxPawnPlayable, this. else, StateMoveMotherNature
     */
    public State nextState(){
        if(numOfPawnPlayed < maxPawnPlayable){
            return this;
        }else{
            ((ModelPro)model).deactivateAllActiveGameCards();
            model.notify(((ModelPro)model).getActiveGameCards());
            return new StateMoveMotherNature(model);
        }

    }

    @Override
    public String toString() {
        return String.valueOf(3);
    }

}
