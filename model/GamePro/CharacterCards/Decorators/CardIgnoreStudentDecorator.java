package it.polimi.ingsw.model.GamePro.CharacterCards.Decorators;

import it.polimi.ingsw.model.Exceptions.ExceptionsNoSuchTowers;
import it.polimi.ingsw.model.Game.Island;
import it.polimi.ingsw.model.Game.Professor;
import it.polimi.ingsw.model.Game.Team;
import it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard.CardIgnoreStudent;

import java.util.ArrayList;

public class CardIgnoreStudentDecorator extends IslandDecorator{

    public CardIgnoreStudentDecorator(Island island, CardIgnoreStudent card) {
        super(island, card);
    }


    /**
     * return the influence without student of one color
     */
    public int getInfluenceTeamMod(Team team){
        long influence=0;
        ArrayList<Professor> x=team.getProfessors();
        for (Professor p: x){
            if(p.getColor()!=((CardIgnoreStudent)card).getStudent()){
                influence=influence+island.getNumStudents(p.getColor());
            }
        }
        if(island.isConquered()){
            try {
                if(island.getTower().getColor()==team.getTeamColor()) influence=+1;
            } catch (ExceptionsNoSuchTowers e) {
                return (int)influence;
            }
        }
        return (int)influence;
    }

    /**
     * if the card is active the influence don't count student of a specific color, else same as Model
     */
    @Override
    public int getInfluenceTeam(Team team) {
        if(card.isActive())
            return getInfluenceTeamMod(team);
        return island.getInfluenceTeam(team);
    }
}
