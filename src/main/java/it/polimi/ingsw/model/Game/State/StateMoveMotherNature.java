package it.polimi.ingsw.model.Game.State;

import it.polimi.ingsw.model.Exceptions.ExceptionIslandNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;
import it.polimi.ingsw.model.Exceptions.ExceptionMotherNatureIllegalMovement;
import it.polimi.ingsw.model.Exceptions.ExceptionsNoSuchTowers;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Model;

public class StateMoveMotherNature extends State{

    public StateMoveMotherNature(Model model) {
        super(model, "moveMotherNature");
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
     *
     * @param newPos index of the destination island
     */
    public void moveMotherNature(int newPos) {
        try {
            System.out.println(model.getClass());

            model.moveMotherNature(newPos);
            model.setState(nextState());
            model.notify(model.getState());

        } catch (ExceptionNoTowerInBoard | ExceptionIslandNotFound | ExceptionMotherNatureIllegalMovement e) {
            model.notify(
                    ""+model.getCurrentPlayerPos()+","+
                            e.getMessage()
             );
        } catch (ExceptionsNoSuchTowers e) {
            e.printStackTrace();
        }
    }
    /**
     * method do nothing
     */
    public void chooseCloud(int cloud) {
        System.out.println("impossible choose Cloud now!");
    }

    /**
     *
     * @return
     * if the bag is empty and the current player is the last, StateWin.
     * if the bag is empty and the current player isn't the last, StateTurn.
     * if the bag isn't empty, StateSelectCloud.
     */
    @Override
    public State nextState() {
        if(model.isFlagTowerOrIsland())
            return new StateWin(model);
        if(model.isFlagBagEmpty()){
            if(model.isThisLastPlayer()){
                return new StateWin(model);
            }else {
                model.nextPlayer();
                return new StateTurn(model);
            }
        }
        return new StateSelectCloud(model);
    }

    /**
     *
     * @return "2"
     */
    @Override
    public String toString() {
        return String.valueOf(2);
    }

}
