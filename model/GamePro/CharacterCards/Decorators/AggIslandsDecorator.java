package it.polimi.ingsw.model.GamePro.CharacterCards.Decorators;

import it.polimi.ingsw.model.Exceptions.ExceptionIslandNotConquered;
import it.polimi.ingsw.model.Exceptions.ExceptionsNoSuchTowers;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;

import java.util.ArrayList;
import java.util.Optional;

public abstract class AggIslandsDecorator extends AggIsland {
    protected AggIsland aggIsland;
    protected CharacterCard card;

    public AggIslandsDecorator(AggIsland aggIsland, CharacterCard card) {
        this.aggIsland = aggIsland;
        this.card = card;
    }

    public AggIsland getAggIsland() {
        return aggIsland;
    }

    /**
     * call the same method of the internal object
     */
    @Override
    public void setMotherNature(MotherNature motherNature) {
        aggIsland.setMotherNature(motherNature);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public boolean hasMotherNature() {
        return aggIsland.hasMotherNature();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public ArrayList<Student> getStudents() {
        return aggIsland.getStudents();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public long getNumStudents(Col_Pawn colorStudent) {
        return aggIsland.getNumStudents(colorStudent);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public ArrayList<Island> getIslands() {
        return aggIsland.getIslands();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setIslands(ArrayList<Island> islands) {
        aggIsland.setIslands(islands);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public boolean isConquered() {
        return aggIsland.isConquered();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Optional<Col_Tower> getConquer() {
        return aggIsland.getConquer();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Tower getTower() throws ExceptionsNoSuchTowers {
        return aggIsland.getTower();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public int getNumOfTower() {
        return aggIsland.getNumOfTower();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public ArrayList<Tower> conquer(ArrayList<Tower> towers) {
        return aggIsland.conquer(towers);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public boolean merge(AggIsland aggIsland) {
        return this.aggIsland.merge(aggIsland);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public int getNumIslands() {
        return aggIsland.getNumIslands();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void addStudent(Student student) {
        aggIsland.addStudent(student);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public int getInfluenceTeam(Team team) {
        return aggIsland.getInfluenceTeam(team);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public int[] getAllInfluenceTeams(Team[] teams) {
        return aggIsland.getAllInfluenceTeams(teams);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Optional<Team> getTeamMaxInfluence(Team[] teams) {
        return aggIsland.getTeamMaxInfluence(teams);
    }


    /**
     * call the same method of the internal object
     */
    @Override
    public String toString() {
        return aggIsland.toString();
    }
}
