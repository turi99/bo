package it.polimi.ingsw.model.GamePro.CharacterCards.Decorators;

import it.polimi.ingsw.model.Exceptions.ExceptionIslandNotConquered;
import it.polimi.ingsw.model.Exceptions.ExceptionsNoSuchTowers;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;

import java.util.ArrayList;
import java.util.Optional;

public abstract class IslandDecorator extends Island {
    protected Island island;
    protected CharacterCard card;

    public IslandDecorator(Island island, CharacterCard card) {
        this.island=island;
        this.card=card;
    }

    /**
     * call the same method of the internal object
     */
    @Override
    public ArrayList<Student> getStudents() {
        return island.getStudents();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public long getNumStudents(Col_Pawn color) {
        return island.getNumStudents(color);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Tower getTower() throws ExceptionsNoSuchTowers {
        return island.getTower();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Optional<Tower> conquer(Tower tower) {
        return island.conquer(tower);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Optional<Col_Tower> getConquer() {
        return island.getConquer();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public boolean isConquered() {
        return island.isConquered();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void addStudent(Student student) {
        island.addStudent(student);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public int getInfluenceTeam(Team team) {
        return island.getInfluenceTeam(team);
    }


    /**
     * call the same method of the internal object
     */
    @Override
    public String toString() {
        return island.toString();
    }
}
