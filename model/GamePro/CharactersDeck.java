package it.polimi.ingsw.model.GamePro;

import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard.*;
import it.polimi.ingsw.model.ModelPro;

import java.io.Serializable;
import java.util.ArrayList;


public class CharactersDeck implements Serializable {
    ArrayList<CharacterCard> allCharacterCards;

    public CharactersDeck(ModelPro modelPro) {
        allCharacterCards=new ArrayList<>();



        allCharacterCards.add(new CardInfluencePlusTwo(modelPro));

        allCharacterCards.add(new CardTwoAdditionalMovement(modelPro));

        allCharacterCards.add(new CardIgnoreTowers(modelPro));

        allCharacterCards.add(new CardIgnoreStudent(modelPro));

        allCharacterCards.add(new CardStudentOnIsland(modelPro));

        allCharacterCards.add(new CardSwitchBoard(modelPro));

        allCharacterCards.add(new CardBlock(modelPro));

        allCharacterCards.add(new CardLoseStudentFromLane(modelPro));

        allCharacterCards.add(new CardMoveLane(modelPro));

        allCharacterCards.add(new CardRuleProfessor(modelPro));   // XXX

        allCharacterCards.add(new CardStartConquer(modelPro));      // XXX


        allCharacterCards.add(new CardSwitchBoardLane(modelPro));


    }

    /**
     *
     * @param num size of the returned list
     * @return list of num CharacterCard
     * @throws Exception if there aren't enough character card
     */
    public ArrayList<CharacterCard> getRandomCards(int num) throws Exception {
        if(num > allCharacterCards.size()) throw new Exception();

        ArrayList<CharacterCard> returnedList = new ArrayList<>();
        ArrayList<CharacterCard> allCardsListCopy = new ArrayList<>(allCharacterCards);

        for (int i = 0; i < num; i++) {
            int randomIndex = Math.round(Math.round(Math.random() * (allCardsListCopy.size()-1)));
            returnedList.add(allCardsListCopy.remove(randomIndex));
        }

        return returnedList;
    }
}
