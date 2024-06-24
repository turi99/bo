package it.polimi.ingsw.model.GamePro.CharacterCards.Decorators;

import it.polimi.ingsw.model.Game.AggIsland;
import it.polimi.ingsw.model.Game.Team;
import it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard.CardInfluencePlusTwo;

import java.util.Optional;

public class CardInfluencePlusTwoDecorator extends AggIslandsDecorator {


    public CardInfluencePlusTwoDecorator(AggIsland island, CardInfluencePlusTwo card) {
        super(island,card);
    }

    /**
     * the team with the character card active has +2 influence
     */
    private int[] getAllInfluenceTeamsMod(Team[] teams){
        int[] x = new int[teams.length];
        for(int i=0;i<teams.length-1;i++){
            if (card.getModel().getCurrentPlayer().getTeam().getTeamColor() == teams[i].getTeamColor()) {
                x[i] = aggIsland.getInfluenceTeam(teams[i]) + 2;
            } else {
                x[i] = aggIsland.getInfluenceTeam(teams[i]);
            }
        }
        return x;
    }

    /**
     * the team with the character card active has +2 influence, else same as Model
     */
    @Override
    public int[] getAllInfluenceTeams(Team[] teams){
        if(card.isActive())return getAllInfluenceTeamsMod(teams);
        else return aggIsland.getAllInfluenceTeams(teams);
    }

    /**
     * same as Model
     */
    @Override
    public Optional<Team> getTeamMaxInfluence(Team[] teams){
        int[] x = getAllInfluenceTeams(teams);
        int j = 0;

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
