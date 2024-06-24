package it.polimi.ingsw.model.GamePro.CharacterCards.Decorators;

import it.polimi.ingsw.model.Game.Island;
import it.polimi.ingsw.model.Game.Professor;
import it.polimi.ingsw.model.Game.Team;
import it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard.CardIgnoreTowers;

import java.util.ArrayList;

public class CardIgnoreTowerDecorator extends IslandDecorator {

    public CardIgnoreTowerDecorator(Island island, CardIgnoreTowers cardIgnoreTowers) {
        super(island,cardIgnoreTowers);
    }

    /**
     * return the influence without the towers
     */
    public int getInfluenceTeamMod(Team team){
        long influence=0;
        ArrayList<Professor> x = team.getProfessors();
        for (Professor p: x){
            influence=influence+island.getNumStudents(p.getColor());
        }
        return (int)influence;
    }

    /**
     * if the card is active the influence don't count the towers, else same as Model
     */
    @Override
    public int getInfluenceTeam(Team team) {
        if(card.isActive()) return getInfluenceTeamMod(team);
        return island.getInfluenceTeam(team);
    }
}
