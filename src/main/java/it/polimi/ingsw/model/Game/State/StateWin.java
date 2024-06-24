package it.polimi.ingsw.model.Game.State;

import it.polimi.ingsw.model.Exceptions.ExceptionsNoSuchTowers;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Model;

public class StateWin extends State{
    public StateWin(Model model) {
        super(model, "win");
        try {
            model.win();
        } catch (ExceptionsNoSuchTowers e) {
            e.printStackTrace();
        }
    }

    /**
     * the method do nothing
     */
    @Override
    public void playCard(int valCard) {
        System.out.println("the game is finished!!");
    }
    /**
     * the method do nothing
     */
    @Override
    public void moveStudentToLane(Col_Pawn colorStudent) {
        System.out.println("the game is finished!!");

    }
    /**
     * the method do nothing
     */
    @Override
    public void moveStudentToIsland(int indexIsland, Col_Pawn colorStudent) {
        System.out.println("the game is finished!!");

    }
    /**
     * the method do nothing
     */
    @Override
    public void moveMotherNature(int newPos) {
        System.out.println("the game is finished!!");

    }
    /**
     * the method do nothing
     */
    @Override
    public void chooseCloud(int cloud) {
        System.out.println("the game is finished!!");
    }

    /**
     *
     * @return null
     */
    @Override
    public State nextState() {
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
