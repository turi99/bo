package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionPlayerCantPay;
import it.polimi.ingsw.model.Exceptions.ExceptionStudentNotFound;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.CharactersDeck;
import it.polimi.ingsw.model.GamePro.PlayerPro;
import it.polimi.ingsw.model.GamePro.StatePro.StatePro;
import it.polimi.ingsw.model.GamePro.StatePro.StateProPlayCard;
import it.polimi.ingsw.utils.observer.Observer;

import java.util.ArrayList;

public class ModelPro extends Model implements Observer<Object> {
    private transient CharactersDeck gameCard;
    private ArrayList<CharacterCard> activeGameCard;


    public ModelPro(int num) throws Exception {
        super(num);
        boxToPro();
        upgradePlayer();
        gameCard=new CharactersDeck(this);
        activeGameCard=gameCard.getRandomCards(1);
        activeGameCard.add(gameCard.getAllCharacterCards().get(7));
        activeGameCard.add(gameCard.getAllCharacterCards().get(9));

        for(Player p : getPlayers()){
            giveCoinsToPlayer((PlayerPro) p,3);
        }


        setState(new StateProPlayCard(this));
    }

    public ModelPro() {
    }

    /**
     * decorate the given model like the character cards requires
     * @param model model to decorate
     * @return ModelPro decorate like the character cards requires
     */
    public ModelPro initializeModelPro(ModelPro model){
        //ModelPro model = this;

        for(CharacterCard c : activeGameCard){
            c.setModel(model);
            model = c.decorate();
        }

        return model ;
    }

    /**
     * upgrade the players to PlayerPro class
     */
    private void upgradePlayer(){
        int dim = getPlayers().length;

        Player[] players = new Player[dim];
        ArrayList<PlayedCard> playedCards = new ArrayList<>();

        for(int i=0; i<dim; i++){
            players[i] = getPlayers()[i].upgradeToPro();
            playedCards.add(new PlayedCard(players[i], new Card(0,0)));
        }
        setPlayers(players);

        ArrayList<PlayedCard> temp = new ArrayList<>();

        for(int i=getPosCurrentPlayer(); i<dim; i++){
            temp.add(playedCards.get(i));
        }
        for(int i=0; i<getPosCurrentPlayer(); i++){
            temp.add(playedCards.get(i));
        }

        playedCards = temp;

        for(int i=0; i<dim; i++)
            System.out.println("id player -------- "+playedCards.get(i).getPlayer().getID());

        setPlayedCards(playedCards);

        updateTeams(dim);
    }

    /**
     * update the players in the teams to PlayerPro class
     * @param num number of teams
     */
    private void updateTeams(int num){
        if (num == 4) {
            Player[] t1 = new Player[2];
            t1[0] = getPlayers()[0];
            t1[1] = getPlayers()[2];
            getTeams()[0].setPlayers(t1);

            Player[] t2 = new Player[2];
            t2[0] = getPlayers()[1];
            t2[1] = getPlayers()[3];
            getTeams()[1].setPlayers(t2);
        } else {
            for (int i = 0; i < num; i++) {
                Player[] player = new Player[1];
                player[0] = getPlayers()[i];
                getTeams()[i].setPlayers(player);
            }
        }
    }

    /**
     *
     * @param idCard id of the character card of interest
     * @return character card with same id of idCard
     * @throws Exception if there isn't a character card with same id of idCard
     */
    public CharacterCard getActiveGameCard(int idCard) throws Exception {
        CharacterCard card=null;
        for (CharacterCard c: activeGameCard) {
            if(c.getIdCard()==idCard)card=c;
        }
        if(card == null)
            throw new Exception("character card not found");

        return card;
    }

    /**
     *
     * @return the number of coins unowned
     */
    public int getCoins(){
        return ((BoxPro)getBox()).getCoins();
    }

    /**
     * add coins to the unowned coins
     * @param coins number of coins to add
     */
    public void addCoins(int coins){
        ((BoxPro)getBox()).addCoins(coins);
    }

    /**
     * remove coins from the unowned coins and return them
     * @param coins number of coins to remove
     * @return coins removed
     * @throws ExceptionPlayerCantPay there aren't enough unowned coins
     */
    public int removeCoins(int coins) throws ExceptionPlayerCantPay {
        return  ((BoxPro)getBox()).removeCoins(coins);
    }

    /**
     *
     * @return list of all character card
     */
    public ArrayList<CharacterCard> getActiveGameCards() {
        return activeGameCard;
    }

    /**
     * remove coins from the current player and add to unowned coins
     * @param coins coins to remove from the current player
     */
    public void playerPayCoins(int coins){
        addCoins(
                ((PlayerPro) getCurrentPlayer()).payCoins(coins)
        );
    }

    /**
     *
     * @param player player that receive the coins
     * @param coins coins for the given player
     * @throws ExceptionPlayerCantPay if there aren't enough unowned coins
     */
    public void giveCoinsToPlayer(PlayerPro player, int coins) throws ExceptionPlayerCantPay {
        ((BoxPro)getBox()).removeCoins(coins);
        player.addCoins(coins);
    }

    /**
     * deactivate all the character card
     */
    public void deactivateAllActiveGameCards(){
        for(CharacterCard c : activeGameCard)
            c.deactivate();
    }


    /**
     * allow the current player to move a student from the board to the lane
     * @param colorStudent color of the student you want to move
     * @throws ExceptionLaneNotFound if the current player doesn't have a lane with same color of colorStudent
     * @throws ExceptionStudentNotFound if the current player doesn't have a student with same color of colorStudent in the board
     * @throws ExceptionPlayerCantPay if the current player earns more coins than the number of unowned coins
     */
    @Override
    public void moveStudentToLane(Col_Pawn colorStudent) throws ExceptionLaneNotFound, ExceptionStudentNotFound, ExceptionPlayerCantPay {
        super.moveStudentToLane(colorStudent);
        notify(arrayToArrayList(getPlayers()));
        if( ((PlayerPro)getCurrentPlayer()).checkStudentsForCoin(colorStudent) ) {
            giveCoinsToPlayer((PlayerPro) getCurrentPlayer(), 1) ;
            notify(arrayToArrayList(getPlayers()));
            notify(getBox());
        }

    }


    /**
     * allow the current player to activate a character card
     * @param idCard id of the character card of interest
     * @param parameters string of activation of the character card of interest
     * @throws Exception if there isn't a character card with same id of idCard
     */
    public void activateCard(int idCard, String parameters) throws Exception {
        getActiveGameCard(idCard).activate(parameters);
        notify(activeGameCard);
    }

    /**
     * method that let the current player to activate a character card
     * @param idCard id of the character card of interest
     * @param parameters string of activation of the character card of interest
     */
    public void actionActivateCard(int idCard, String parameters){
        ((StatePro) getState()).activateCharacterCard(idCard, parameters);
    }

    @Override
    public void update(Object message) {
        notify(message);
    }
}
