package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class MotherNature  implements Serializable {
    private AggIsland island;

    public MotherNature(AggIsland island) {
        this.island = island;
    }

    /**
     *
     * @return the associated aggregation
     */
    public AggIsland getIsland() {
        return island;
    }

    public void setIsland(AggIsland island){
        this.island = island;
    }

    /**
     * move motherNature on the given island and calculate which team earn the conquest
     * @param island destination island
     * @param teams array of teams
     * @return list of tower already present on the destination island
     * @throws ExceptionNoTowerInBoard
     */
    public ArrayList<Tower> move(AggIsland island, Team[] teams) throws ExceptionNoTowerInBoard {

        this.island.setMotherNature(null);

        this.island=island;

        this.island.setMotherNature(this);

        ArrayList<Tower> towersChanged = new ArrayList<>();

        Optional<Team> conquerTeam = this.island.getTeamMaxInfluence(teams);
        if( conquerTeam.isPresent() )
        {
            towersChanged = this.island.conquer(conquerTeam.get().drawTowers(this.island.getNumIslands()));

        }


        return towersChanged;
    }





}
