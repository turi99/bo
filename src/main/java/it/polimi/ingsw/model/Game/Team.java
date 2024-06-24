package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;
import it.polimi.ingsw.utils.observer.Observable;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {
    private Col_Tower teamColor;
    private Player[] players;

    public Team() {
        teamColor = null;
        players = null;
    }

    public Team(Col_Tower col, Player[] pl, int numOfTowers) {
        this.teamColor = col;
        this.players = pl;

        for(int i=0; i<numOfTowers; i++){
            addTower(new Tower(col));
        }

    }

    /**
     * replace the players with the given ones
     * @param players new players
     */
    public void setPlayers(Player[] players) {
        this.players = players;
    }

    /**
     *
     * @return list of all towers hold by the players
     */
    public ArrayList<Tower> getTowers(){
        return players[0].getAllTowers();
    }

    /**
     *
     * @return number of all towers hold by the players
     */
    public int getNumOfTower(){
        return getTowers().size();
    }

    /**
     *
     * @return color of the team
     */
    public Col_Tower getTeamColor() {
        return teamColor;
    }

    /**
     *
     * @return players of the team
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * add a tower to one of the player
     * @param tower tower you want to add
     */
    public void addTower(Tower tower) {
        players[0].addTower(tower);
    }

    /**
     * add a list of towers to one of the player
     * @param towers list of towers you want to add
     */
    public void addTowers(ArrayList<Tower> towers) {
        players[0].addTowers(towers);
    }


    /**
     *
     * @return list of all professors hold by all players
     */
    public ArrayList<Professor> getProfessors() {
        ArrayList<Professor> professorsCopy =new ArrayList<>();
        for(Player p : players) {
            professorsCopy.addAll(p.getAllProfessors());
        }
        return professorsCopy;
    }

    //-------------------------------------

    @Override
    public String toString(){
        String s= String.valueOf(teamColor.ordinal());
        return s;
    }


    /**
     * remove a list of towers from one of the players, and return it
     * @param num number of towers to remove
     * @return list of towers
     */
    public ArrayList<Tower> drawTowers(int num)  {
        ArrayList<Tower> x=new ArrayList<>();
        for(int i=0;i<num;i++){
            try {
                x.add(this.players[0].removeTower());
            } catch (ExceptionNoTowerInBoard e) {
                e.printStackTrace();
            }
        }
        return x;
    }

    /**
     *
     * @param player player of interest
     * @return team.contains(player)
     */
    public boolean containsPlayer(Player player){
        for( int i=0; i< players.length ;i++){
            if(player.equals(players[i]))
                return true;
        }
        return false;
    }

    /**
     *
     * @param colStud color student of interest
     * @return sum of the number of student in the lane with same color of colStud of every player
     * @throws ExceptionLaneNotFound
     */
    public int getNumStudLane(Col_Pawn colStud) throws ExceptionLaneNotFound {
        int num = 0;
        for (Player p : players) {
            num += p.getNumStudLane(colStud);
        }
        return num;
    }
}
