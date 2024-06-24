package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.ExceptionIslandNotConquered;
import it.polimi.ingsw.model.Exceptions.ExceptionsNoSuchTowers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class AggIsland  implements Serializable {
    private ArrayList<Island> islands;
    private MotherNature motherNature;


    public AggIsland() {
        Island island=new Island();
        if(this.islands==null)this.islands=new ArrayList<Island>();
        this.islands.add(island);
        motherNature = null;
    }

    /**
     * update motherNature
     * @param motherNature new motherNature
     */
    public void setMotherNature(MotherNature motherNature){
        this.motherNature = motherNature;
    }

    /**
     * @return motherNature != null
     */
    public boolean hasMotherNature(){
        return motherNature != null;
    }


    /**
     * @return ArrayList<Student> that contains all student from every island
     */
    public ArrayList<Student> getStudents() {
        ArrayList<Student> students= new ArrayList<Student>();
        for(Island i: islands){
            students=(ArrayList<Student>) i.getStudents();
        }
        return students;
    }

    /**
     *
     * @param colorStudent color student of interest
     * @return number of student in getStudents(), with the same color of colorStudent
     */
    public long getNumStudents(Col_Pawn colorStudent){
        long x=0;
        for (Island i:islands){
            x=x+i.getNumStudents(colorStudent);
        }
        return x;
    }

    @Override
    public String toString() {
        int[] ints=new int[5];
        ArrayList<Student> students=new ArrayList<>();
        for(Island i: islands){
            students.addAll(i.getStudents());
        }
        for(Student s:students){
            ints[s.getColor().ordinal()]+= 1;
        }
        String s=Col_Pawn.values()[0]+" : "+ints[0];
        for(int i=1;i<5;i++){
            s=Col_Pawn.values()[i]+" : "+ints[i]+"\n"+s;
        }
        s="Students : \n\n"+s;
        if(islands.get(0).isConquered()){
            try {
                s="Tower : \nSize : "+islands.size()+" Color : "+islands.get(0).getTower().getColor()+"\n"+s;
            } catch (ExceptionsNoSuchTowers e) {
                e.printStackTrace();
            }

        }
        else s="Tower : null\n"+s;
        if(motherNature==null){
            s="Mother nature : FALSE\n"+s;
        }
        else s="Mother nature : TRUE *\n"+s;
        return s;
    }

    /**
     *
     * @return islands
     */
    public ArrayList<Island> getIslands() {
        return  islands;
    }

    /**
     * replace islands
     * @param islands new islands
     */
    public void setIslands(ArrayList<Island> islands) {
        this.islands = islands;
    }

    /**
     * if the island has towers, it is conquered
     * @return true if it is conquered, else false
     */
    public boolean isConquered(){
        return this.islands.get(0).isConquered();
    }

    /**
     *
     * @return color of the conquer team if it's conquered, else empty
     * @throws ExceptionIslandNotConquered
     */
    public Optional<Col_Tower> getConquer() {
        return islands.get(0).getConquer();
    }

    /**
     *
     * @return if it's conquered, return a tower from one of the island
     * @throws ExceptionsNoSuchTowers if it's not conquered
     */
    public Tower getTower() throws ExceptionsNoSuchTowers {
        Tower x;
        if(this.isConquered()){
            x= new Tower(this.islands.get(0).getTower());
        }
        else throw new ExceptionsNoSuchTowers();
        return x;
    }

    /**
     * @return total number of towers from every island
     */
    public int getNumOfTower(){
        int result=0;

        if (isConquered()) result=getNumIslands();

        return result;
    }

    /**
     * method to conquer the aggregation
     * @param towers towers to place on the aggregation, islands.size()==towers.size()
     * @return if it's already conquered, list of old towers. Else, an empty list
     */
    public ArrayList<Tower> conquer(ArrayList<Tower> towers){
        final ArrayList<Tower> x=new ArrayList<Tower>();
        if(this.islands.size()==towers.size()){
            for(Island i : this.islands){
                i.conquer(towers.get(0)).ifPresent(a -> x.add(a));
                towers.remove(0);
            }
        }
        return x;
    }

    /**
     * add the island of the given aggregation into this one, if they are both conquered by the same team
     * @param aggIsland aggregation you want to merge this one with
     * @return if merge has been done, true. Else, false
     */
    public boolean merge(AggIsland aggIsland){
        boolean merge = mergeCheck(aggIsland);

        if(merge)
            this.islands.addAll(aggIsland.getIslands());

        return merge;
    }

    private boolean mergeCheck(AggIsland aggIsland){

        if(!isConquered() || !aggIsland.isConquered())
            return false;

        if(getConquer().get() != aggIsland.getConquer().get())
            return false;


        return true;
    }


    /**
     *
     * @return number of island in this aggregation
     */
    public int getNumIslands(){
        return  this.islands.size();
    }

    /**
     * add the given student on one of the island in this aggregation
     * @param student student to place on the aggregation
     */
    public void addStudent(Student student){
       int j = (int)( Math.random()*this.getNumIslands() );
        this.islands.get(j).addStudent(student);
    }

    /**
     *
     * @param team team of interest
     * @return influence of the given team on this aggregation
     */
    public int getInfluenceTeam(Team team){
        long influence=0;
        for(Island i: this.islands){
            influence=influence+i.getInfluenceTeam(team);
        }
        return (int)influence;
    }

    /**
     *
     * @param teams teams of interest
     * @return influence of every team on this aggregation, ordered like the given array team
     */
    public int[] getAllInfluenceTeams(Team[] teams){
        int[] x = new int[teams.length];
        for(int i=0;i<teams.length;i++){
            x[i]=this.getInfluenceTeam(teams[i]);
        }
        return x;
    }

    /**
     *
     * @param teams teams of interest
     * @return team that has max influence on this aggregation
     */
    public Optional<Team> getTeamMaxInfluence(Team[] teams){
        int[] x= this.getAllInfluenceTeams(teams);
        int j=0;

        boolean even = false;

        for(int i=1;i<teams.length;i++){
            if (x[j] >= x[i]) {
                if(x[j] == x[i]) even=true;
            } else {
                j = i;
                even=false;
            }
        }


        return even ? Optional.empty() : Optional.of(teams[j]);
    }


}

