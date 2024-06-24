package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.ExceptionIslandNotConquered;
import it.polimi.ingsw.model.Exceptions.ExceptionsNoSuchTowers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Island implements Serializable {
    private ArrayList<Student> students=new ArrayList<Student>();
    private Tower tower;


    public Island(ArrayList<Student> students ) {
        this.students = students;
        this.tower=null;
    }

    public Island() {
    }

    /**
     *
     * @return listof all students on the island
     */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     *
     * @param color student color of interest
     * @return number of student on this island with the same color of the given one
     */
    public long getNumStudents(Col_Pawn color){
        return students.stream().filter(a -> a.getColor()==color ).count();
    }

    /**
     *
     * @return if it's present, return the tower on this island
     * @throws ExceptionsNoSuchTowers if the tower is not present
     */
    public Tower getTower() throws ExceptionsNoSuchTowers {
        Tower x;
        if(this.isConquered()){
            x= new Tower(this.tower);
        }
        else throw new ExceptionsNoSuchTowers();
        return x;
    }

    /**
     * method to conquer the island and place your tower
     * @param tower tower to place on the island
     * @return the old tower, if it was present. else, empty
     */
    public Optional<Tower> conquer(Tower tower){
        Optional<Tower> x;
        if(isConquered()) {
            x = Optional.of(changeTower(tower));
        }
        else {
            addTower(tower);
            x = Optional.empty();
        }
        return x;
    }

    private void addTower(Tower tower){
        this.tower=tower;
    }

    private Tower changeTower(Tower tower){
        Tower x= new Tower(this.tower);
        this.tower=tower;
        return x;

    }

    /**
     *
     * @return if island is conquered, return the color of the tower. Else, empty
     */
    public Optional<Col_Tower> getConquer(){

        if(isConquered()) {
            try {
                return Optional.of(getTower().getColor());
            } catch (ExceptionsNoSuchTowers e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    /**
     *
     * @return tower!=null
     */
    public boolean isConquered(){
        return this.tower!=null;
    }

    /**
     *
     * @param student student you want to add
     */
    public void addStudent(Student student){
        this.students.add(student);
    }

    /**
     *
     * @param team team of interest
     * @return influence of the given team on this island
     */
    public int getInfluenceTeam(Team team){
        long influence=0;
        ArrayList<Professor> x = team.getProfessors();
        for (Professor p: x){
            influence=influence+ getNumStudents(p.getColor());
        }
        if(isConquered()){
            try {
                if (getTower().getColor() == team.getTeamColor()) {
                    influence += 1;
                }
            } catch (ExceptionsNoSuchTowers e) {
                e.printStackTrace();
                return (int)influence;
            }
        }
        return (int)influence;
    }

}
