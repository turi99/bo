package it.polimi.ingsw.model.Game.State;

import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Model;

import java.io.Serializable;

public abstract class State implements Serializable {

    protected Model model;
    protected final String name;

    public State(Model model, String name){
        this.model = model;
        this.name = name;
    }

    /**
     * replace the model
     * @param model new model
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * method to allow the current player to play a card
     * @param valCard value of the card you want to play
     */
    public abstract void playCard(int valCard) ;

    /**
     * method to allow the current player to move a student in the lane
     * @param colorStudent color of the student you want to move
     */
    public abstract void moveStudentToLane(Col_Pawn colorStudent);

    /**
     * method to allow the current player to move a student on island
     * @param indexIsland index of the destination island
     * @param colorStudent color of the student you want to move
     */
    public abstract void moveStudentToIsland(int indexIsland, Col_Pawn colorStudent);

    /**
     * method to allow the current player to move mother nature
     * @param newPos index of the destination island
     */
    public abstract void moveMotherNature(int newPos);

    /**
     * method to allow the current player to choose a cloud and get its student
     * @param cloud index of the chosen cloud
     */
    public abstract void chooseCloud(int cloud);

    /**
     *
     * @return the next state
     */
    public abstract State nextState();

    @Override
    public abstract String toString();

    /**
     *
     * @return name of the state
     */
    public String getName() {
        return name;
    }
}
