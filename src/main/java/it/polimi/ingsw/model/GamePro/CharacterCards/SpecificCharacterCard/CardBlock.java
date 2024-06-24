package it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard;

import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;
import it.polimi.ingsw.model.Game.AggIsland;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.AggIslandsDecorator;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardBlockDecorator;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardBlock extends CharacterCard {
    int blockCard=4;

    public CardBlock(ModelPro model) {
        super("placing a block card on an island prevents the influence calculation until mother nature stops on the island ",
                2,
                1,
                model);
    }


    /**
     *
     * @return this blockCard
     */
    public int getBlockCard() {
        return blockCard;
    }

    /**
     *
     * @return this model with AggIsland of type CardBlockDecorator
     */
    @Override
    public ModelPro decorate() {
        System.out.println("decoration "+getIdCard());

        ArrayList<AggIsland> islands = new ArrayList<>();
        for(int i=0; i<model.getIslands().size(); i++){
            islands.add(new CardBlockDecorator(model.getIslands().get(i), this));
            if(model.getIslands().get(i).hasMotherNature())
                model.getMotherNature().setIsland(islands.get(i));
        }
        model.setIslands(islands);
        return model;
    }

    @Override
    public String activationParameters(BufferedReader bufferedReader, Thread thread) throws IOException {
        String parameters;
        System.out.println("Insert the number of the island that you wont to block (0-"+(model.getIslands().size()-1)+")");

        while (!bufferedReader.ready()){
            if(thread.isInterrupted())return "";
        }
        parameters=bufferedReader.readLine();
        return parameters;

    }


    /**
     *
     * @param blockCard new value of blockCard
     */
    public void setBlockCard(int blockCard) {
        this.blockCard = blockCard;
    }

    /**
     * if checkParameters is true and the player can pay, the card is activated
     * @param instruction parameters of activation
     * @throws ExceptionNoTowerInBoard
     * @throws ExceptionLaneNotFound
     */
    @Override
    public void activate(String instruction) throws ExceptionNoTowerInBoard, ExceptionLaneNotFound {
        if(checkParameters(instruction)) {
            super.activate(instruction);
            if(model.getIslands().get(Integer.parseInt(instruction)).getClass() == CardBlockDecorator.class){
                ((CardBlockDecorator) model.getIslands().get(Integer.parseInt(instruction))).setBan(true);
            }else{
                ((CardBlockDecorator)((AggIslandsDecorator) model.getIslands().get(Integer.parseInt(instruction))).getAggIsland()).setBan(true);
            }
            blockCard -= 1;
        }
    }

    /**
     *
     * @param parameters parameters of activation
     * @return parameters=="x" where x is an integer representing index of an island
     */
    @Override
    public boolean checkParameters(String parameters) {
        int x = 0;
        try {
            x = Integer.parseInt(parameters);
        }catch (Exception e){
            return false;
        }
        if (x >= 0 && x < model.getIslands().size() && blockCard>0){
            if(model.getIslands().get(x).getClass() == (CardBlockDecorator.class)){
                return !((CardBlockDecorator) model.getIslands().get(x)).getBan();
            }else{
                return !((CardBlockDecorator)((AggIslandsDecorator) model.getIslands().get(x)).getAggIsland()).getBan();
            }
        }
        return false;
    }

}
