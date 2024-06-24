package it.polimi.ingsw.model;

import it.polimi.ingsw.client.ViewCli;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.Game.State.*;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.AggIslandsDecorator;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardBlockDecorator;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardInfluencePlusTwoDecorator;
import it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard.*;
import it.polimi.ingsw.model.GamePro.PlayerPro;
import it.polimi.ingsw.model.GamePro.StatePro.StateProMoveMotherNature;
import it.polimi.ingsw.model.GamePro.StatePro.StateProPlayCard;
import it.polimi.ingsw.model.GamePro.StatePro.StateProSelectCloud;
import it.polimi.ingsw.model.GamePro.StatePro.StateProTurn;

import java.util.ArrayList;

public class ModelViewPro extends ModelView {
    ArrayList<CharacterCard> cards;

    public ModelViewPro(int id, ModelPro model){
        super(id,model);
        cards=model.getActiveGameCards();
    }

    /**
     *
     * @return cards
     */
    public ArrayList<CharacterCard> getCards() {
        return cards;
    }


    @Override
    public void update(Object message) {
        if(message.getClass()==ArrayList.class){
            Object o=((ArrayList<?>) message).get(0);
            if(o.getClass()==Cloud.class){
                setClouds((ArrayList<Cloud>) message);
            }
            if(o.getClass()==AggIsland.class || o.getClass()== CardBlockDecorator.class || o.getClass()== CardInfluencePlusTwoDecorator.class){
                setAggIslands((ArrayList<AggIsland>)message);
            }
            if(o.getClass()==Player.class || o.getClass()== PlayerPro.class){
                setPlayer(((ArrayList<Player>) message).get(getId()));
                ArrayList<Player> pl=new ArrayList<>();
                for(int i=0;i<((ArrayList<Player>) message).size();i++){
                    if(i==getId())continue;
                    pl.add((Player) ((ArrayList<?>) message).get(i));
                }
                setOtherPlayers(pl);
            }
            if(o.getClass()== CardBlock.class ||o.getClass()== CardIgnoreStudent.class ||o.getClass()== CardIgnoreTowers.class
                    ||o.getClass()== CardInfluencePlusTwo.class ||o.getClass()== CardLoseStudentFromLane.class
                    ||o.getClass()== CardMoveLane.class ||o.getClass()== CardRuleProfessor.class  ||o.getClass()== CardStartConquer.class
                    ||o.getClass()== CardStudentOnIsland.class ||o.getClass()== CardSwitchBoard.class  ||o.getClass()== CardSwitchBoardLane.class
                    ||o.getClass()== CardTwoAdditionalMovement.class  ){
                cards=(ArrayList<CharacterCard>) message;
            }
            if(o.getClass()==PlayedCard.class){
                setPlayedCard((ArrayList<PlayedCard>) message);
            }
        }else{
            if(message.getClass()==Integer.class){
                setCurrentPlayerPos((Integer)message);
            }
            if(message.getClass()==StatePlayCard.class || message.getClass()==StateTurn.class
                    || message.getClass()==StateSelectCloud.class || message.getClass()==StateMoveMotherNature.class
                    || message.getClass()==StateWin.class || message.getClass()== StateProPlayCard.class || message.getClass()== StateProTurn.class
                    || message.getClass()== StateProSelectCloud.class || message.getClass()== StateProMoveMotherNature.class ){
                setState((State) message);
                notify(null);
            }
            if(message.getClass()==Team.class){
                setWinner((Team) message);
            }
            if(message.getClass()==MotherNature.class){
                setMotherNature((MotherNature) message);
            }
            if(message.getClass()==String.class) {
                String s = (String) message;
                if (s.contains(",")) {
                    String[] error = ((String) message).split(",");
                    try {
                        int i = Integer.parseInt(error[0]);
                        if (i == getId()) {
                            System.out.println(error[1]);
                            notify(null);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(s);
                    if (((String) message).contains("lost")) {
                        notify(true);
                    } else{
                        System.out.println(s);
                        notify(null);
                    }
                }
            }


            if(message.getClass()==Box.class || message.getClass()==BoxPro.class){
                setBox((Box) message);
            }
        }

    }






}
