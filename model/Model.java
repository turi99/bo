package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Exceptions.*;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.Game.State.State;
import it.polimi.ingsw.model.Game.State.StatePlayCard;
import it.polimi.ingsw.utils.observer.Observable;

import java.io.Serializable;
import java.util.*;


public class Model extends Observable<Object> implements Serializable {

    private static final int NUM_OF_ISLAND = 12;
    private static final int NUM_OF_STUDENT_PER_COLOR = 26;

    private State state;
    private Team[] teams;
    private Cloud[] clouds;
    private ArrayList<AggIsland> islands;
    private Bag bag;
    private MotherNature motherNature;
    private Box box;
    private Deck[] decks;
    private Player[] players;
    private ArrayList<PlayedCard> playedCards;
    private int posCurrentPlayer;
    private Team teamWinner=null;
    private transient boolean flagCard=false;
    private transient boolean flagBagEmpty=false;
    private transient boolean flagTowerOrIsland=false;



    public Model(int num) {
        Random rand = new Random();
        //posCurrentPlayer = rand.nextInt(num);
        posCurrentPlayer = 1;
        this.box=new Box();
        this.initializeCloudsStart(num);
        this.initializeBagStart();
        this.initializeDecksStart(num);
        this.initializePlayers(num);
        this.initializeTeam(num);
        this.initializeIslandStart();
        this.initializeIsland();


        fillCloud();

        state = new StatePlayCard(this);


    }

    public Model() {
    }


    //getter Observable


    /**
     *
     * @return position of the current player in the current players order
     */
    public int getPosCurrentPlayer() {
        return posCurrentPlayer;
    }
    //-----------  metodi inizializzazione -----------//

    private void initializeTeam(int num){

        int numOfTowers = (num==3) ? 6 : 8;

        if (num == 4) {
            teams = new Team[2];
            Player[] t1 = new Player[2];
            t1[0] = players[0];
            t1[1] = players[2];
            teams[0] = new Team(Col_Tower.BLACK, t1, numOfTowers);
            players[0].setTeam(teams[0]);
            players[2].setTeam(teams[0]);
            Player[] t2 = new Player[2];
            t2[0] = players[1];
            t2[1] = players[3];
            teams[1] = new Team(Col_Tower.WHITE, t2, numOfTowers);
            players[1].setTeam(teams[1]);
            players[3].setTeam(teams[1]);
        } else {
            teams = new Team[num];
            for (int i = 0; i < num; i++) {
                Player[] player = new Player[1];
                player[0] = players[i];
                teams[i] = new Team(Col_Tower.values()[i], player, numOfTowers);
                players[i].setTeam(teams[i]);
            }
        }
    }

    private void initializeIslandStart(){
        this.islands = new ArrayList<>();
        for(int i=0;i<NUM_OF_ISLAND;i++){
            this.islands.add(new AggIsland());
        }
    }

    private void initializeCloudsStart(int num){
        int dim;
        if(num==3)dim =4;
        else dim=3;
        this.clouds=new Cloud[num];
        for(int i=0;i<num;i++){
            this.clouds[i]= new Cloud(dim);
        }
    }

    /**
     * @param flagBagEmpty new value of the flagBagEmpty
     */
    public void setFlagBagEmpty(boolean flagBagEmpty) {
        this.flagBagEmpty = flagBagEmpty;
    }

    private void initializeBagStart(){
        ArrayList<Student> x= new ArrayList<>();
        for (int i = 0; i < NUM_OF_STUDENT_PER_COLOR-2; i++) {
            x.add(new Student(Col_Pawn.BLUE));
            x.add(new Student(Col_Pawn.YELLOW));
            x.add(new Student(Col_Pawn.GREEN));
            x.add(new Student(Col_Pawn.RED));
            x.add(new Student(Col_Pawn.PINK));
        }

        this.bag=new Bag(x);
    }

    private void initializeDecksStart(int num){
        this.decks=new Deck[num];
        for(int i=0;i< num;i++){
            this.decks[i]= new Deck();
        }
    }

    private void initializePlayers(int num){

        System.out.println(posCurrentPlayer);
        playedCards = new ArrayList<>();
        players=new Player[num];
        for(int i=0;i<num;i++){
            players[i]=new Player(i,decks[i],initializeBoard(num));

            playedCards.add(new PlayedCard(players[i], new Card(0,0)));
        }

        ArrayList<PlayedCard> temp = new ArrayList<>();

        for(int i=posCurrentPlayer; i<num; i++){
            temp.add(playedCards.get(i));
        }
        for(int i=0; i<posCurrentPlayer; i++){
            temp.add(playedCards.get(i));
        }

        playedCards = temp;

        for(int i=0; i<num; i++)
            System.out.println("id player -------- "+playedCards.get(i).getPlayer().getID());
    }

    private Board initializeBoard(int num){
        switch (num){
            case 2 : return new Board(bag.pickRandomStudent(7));
            case 3 : return new Board(bag.pickRandomStudent(9));
            case 4 : return new Board(bag.pickRandomStudent(7));
        }
        return new Board(new ArrayList<>());
    }

    private void initializeIsland() {
        ArrayList<Student> x = new ArrayList<>();
        x.add(new Student(Col_Pawn.BLUE));
        x.add(new Student(Col_Pawn.BLUE));
        x.add(new Student(Col_Pawn.YELLOW));
        x.add(new Student(Col_Pawn.YELLOW));
        x.add(new Student(Col_Pawn.GREEN));
        x.add(new Student(Col_Pawn.GREEN));
        x.add(new Student(Col_Pawn.RED));
        x.add(new Student(Col_Pawn.RED));
        x.add(new Student(Col_Pawn.PINK));
        x.add(new Student(Col_Pawn.PINK));
        int j = (int) (Math.random() * (NUM_OF_ISLAND-1));
        motherNature = new MotherNature(this.islands.get(j));
        this.islands.get(j).setMotherNature(motherNature);
        for (int i = 0; i < NUM_OF_ISLAND; i++) {
            if (i != j && i != (j + NUM_OF_ISLAND/2) % NUM_OF_ISLAND) {
                int y = (int) (Math.random() * (x.size()-1));
                islands.get(i).addStudent(x.get(y));
                x.remove(y);
            }
        }

    }

    //------- fine metodi inizializzazione -----------//

    /**
     *
     * @param state new state
     */
    public void setState(State state) {
        this.state = state;


    }

    /**
     *
     * @return the winner team
     */
    public Team getTeamWinner() {
        return teamWinner;
    }

    /**
     *
     * @return current state
     */
    public State getState() {
        return state;
    }

    /**
     *
     * @return array of team
     */
    public Team[] getTeams() {
        return teams;
    }

    /**
     *
     * @param player player of the team of interest
     * @return team that contains the given player
     * @throws ExceptionTeamNotFound if there isn't a team with the given player
     */
    public Team getTeamByPlayer(Player player) throws ExceptionTeamNotFound {
        for (Team t : teams) {
            if( t.containsPlayer(player)) return t;
        }
        throw new ExceptionTeamNotFound();
    }

    /**
     *
     * @param colTeam color of the team of interest
     * @return team with same color of colTeam
     * @throws ExceptionTeamNotFound if there isn't a team with same color of colTeam
     */
    public Team getTeamByColor(Col_Tower colTeam) throws ExceptionTeamNotFound {
        for (Team t : teams) {
            if( t.getTeamColor() == colTeam) return t;
        }
        throw new ExceptionTeamNotFound();
    }

    /**
     *
     * @return array of clouds
     */
    public Cloud[] getClouds() {
        return clouds;
    }

    /**
     *
     * @param indexCloud index of the cloud
     * @return cloud in the given index
     * @throws ExceptionCloudNotFound if there isn't a cloud in the given index
     */
    public Cloud getCloud(int indexCloud) throws ExceptionCloudNotFound {
        if(indexCloud < 0 || indexCloud >= clouds.length)
            throw new ExceptionCloudNotFound();

        return clouds[indexCloud];
    }

    /**
     *
     * @return list of all aggregation in the game
     */
    public ArrayList<AggIsland> getIslands() {
        return islands;
    }

    /**
     *
     * @param indexIsland index of the aggregation of interest
     * @return the aggregation in the specific index
     * @throws ExceptionIslandNotFound if there isn't n aggregation in the given index
     */
    public AggIsland getIsland(int indexIsland) throws ExceptionIslandNotFound {
        if(indexIsland < 0 || indexIsland >= islands.size())
            throw new ExceptionIslandNotFound();

        return islands.get(indexIsland);
    }

    /**
     *
     * @return the bag
     */
    public Bag getBag() {
        return bag;
    }

    /**
     *
     * @return the mother nature
     */
    public MotherNature getMotherNature() {
        return motherNature;
    }

    /**
     *
     * @return list of the unowned professors
     */
    public ArrayList<Professor> getProfessors() {
        return box.getProfessors();
    }

    /**
     *
     * @return array of all decks
     */
    public Deck[] getDecks() {
        return decks;
    }

    /**
     *
     * @return array of all players
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     *
     * @return list of current played cards
     */
    public ArrayList<PlayedCard> getPlayedCards() {
        return playedCards;
    }

    /**
     *
     * @return position of the current player in the current players order
     */
    public int getCurrentPlayerPos() {
        return posCurrentPlayer;
    }

    /**
     *
     * @return the current player
     */
    public Player getCurrentPlayer(){
        return this.players[this.posCurrentPlayer];
    }

    /**
     *
     * @param player player of interest
     * @return played card of the given player
     */
    public PlayedCard selectPlayedCardByPlayer(Player player) {
        int i;

        for(i=0; i<playedCards.size() && !playedCards.get(i).getPlayer().equals(player) ;i++){}

        if(i==playedCards.size()){
            //todo playedCardNotFound
            return null;
        }

        return playedCards.get(i);
    }

    /**
     *
     * @param idPlayer id of the player of interest
     * @return played card of the player with same id as idPlayer
     */
    public PlayedCard selectPlayedCardByIdPlayer(int idPlayer) {
        int i;

        for(i=0; i<playedCards.size() && playedCards.get(i).getPlayer().getID()!=idPlayer ;i++){}

        if(i==playedCards.size()){
            //todo playedCardNotFound
            return null;
        }

        System.out.println("selectId" + i);

        return playedCards.get(i);
    }

    /**
     * update the order of players
     */
    public void newOrder(){
        Collections.sort(playedCards);          //playedCards.sort(Collections.reverseOrder());
        posCurrentPlayer = playedCards.get(0).getPlayer().getID();
        notify(posCurrentPlayer);
    }

    /**
     *
     * @return return the max move of mother nature allowed to the current player
     */
    public int getMaxMov() {
        return this.selectPlayedCardByPlayer(getCurrentPlayer()).getCard().getMov();
    }

    /**
     * fill the clouds with students from the bag
     */
    public void fillCloud(){
        for(Cloud c: clouds){
            try{
                c.addStudents( this.bag.pickRandomStudent(c.getDim()));
            }catch (ExceptionWrongNumOfStud e){
                notify(
                        ""+getCurrentPlayerPos()+","+
                    e.getMessage()
                );
            }
        }
        if(bag.getNumberOfStudents()==0)flagBagEmpty=true;
    }

    /**
     *
     * @return flagCard || flagBagEmpty
     */
    public boolean isLastTurn(){
        return flagCard || flagBagEmpty;
    }

    /**
     *
     * @return return the index of the aggregation that has mother nature
     */
    public int getPosMotherNature(){
        int i=0;
        while (!this.islands.get(i).equals(this.motherNature.getIsland())){
            i++;
        }
        return i;

    }

    /**
     * if a player deserves the professor, the method give it to him
     * @param colProf color of the professor you want to check
     */
    public void checkProf(Col_Pawn colProf){
        for(Professor p : box.getProfessors()){
            if(p.getColor()==colProf){
                moveProf(p);
                break;
            }
        }
        for(Player p: players){
            if(p.containsProfessor(colProf)){
                try {
                    if(compareNumberStudent(getTeamByPlayer(getCurrentPlayer()).getNumStudLane(colProf), getTeamByPlayer(p).getNumStudLane(colProf))){
                        moveProf(p,colProf);
                    }
                } catch (ExceptionLaneNotFound e)  {
                    notify(
                            ""+getCurrentPlayerPos()+","+
                        e.getMessage()
                    );
                } catch (ExceptionTeamNotFound e) {
                    notify(
                            ""+getCurrentPlayerPos()+","+
                        e.getMessage()
                    );
                }
                break;
            }
        }
    }

    /**
     *
     * @param x first integer
     * @param y second integer
     * @return x > y
     */
    public boolean compareNumberStudent(int x, int y){
        return x > y;
    }

    /**
     * give the professor to the current player
     * @param prof professor of interest
     */
    public void moveProf(Professor prof){
        getCurrentPlayer().addProfessor(prof);
        this.box.getProfessors().remove(prof);
    }

    /**
     * give the professor to the given player
     * @param player player that receive the professor
     * @param colProf color of the professor of interest
     */
    public void moveProf(Player player, Col_Pawn colProf){
        try {
            getCurrentPlayer().addProfessor(player.removeProfessor(colProf));
        } catch (ExceptionProfessorNotFound e) {
            notify(
                    ""+getCurrentPlayerPos()+","+
                e.getMessage()
            );
        }
    }

    /**
     * allow the current player to move mother nature on a new island
     * @param indexIsland index of the destination island
     * @param teams array of team, to calculate who will conquer the destination island
     * @throws ExceptionNoTowerInBoard the conquer team don't have enough tower
     * @throws ExceptionIslandNotFound the destination island doesn't exist
     * @throws ExceptionsNoSuchTowers a tower is not found
     */
    public void moveMotherNature(int indexIsland, Team[] teams) throws ExceptionNoTowerInBoard, ExceptionIslandNotFound, ExceptionsNoSuchTowers {

        ArrayList<Tower> towersChanged = motherNature.move(getIsland(indexIsland), teams);

        if(!towersChanged.isEmpty()){
            try {
                getTeamByColor(towersChanged.get(0).getColor()).addTowers(towersChanged);
            } catch (ExceptionTeamNotFound e) {
                notify(
                        ""+getCurrentPlayerPos()+","+
                    e.getMessage()
                );
            }
        }

        if(getCurrentPlayer().getTeam().getNumOfTower() == 0)
            win(getCurrentPlayer().getTeam());

        mergeIsland(indexIsland);

    }

    private void mergeIsland(int indexIsland) throws ExceptionsNoSuchTowers {
        int indexOfNextIsland = (indexIsland+1)%islands.size();
        int indexOfPreviousIsland = indexIsland==0 ? islands.size()-1 : indexIsland-1;

        ArrayList<AggIsland> toBeRemove = new ArrayList<>();

        if( islands.get(indexIsland).merge(islands.get(indexOfNextIsland)) )
            toBeRemove.add(islands.get(indexOfNextIsland));

        if( islands.get(indexIsland).merge(islands.get(indexOfPreviousIsland)) )
            toBeRemove.add(islands.get(indexOfPreviousIsland));

        if( !toBeRemove.isEmpty() )
            islands.removeAll(toBeRemove);

        if(islands.size()<=3)flagTowerOrIsland=true;
    }


    /**
     *
     * @return list of all the island in the game
     */
    public ArrayList<Island> getAllIslands(){
        ArrayList<Island> x=new ArrayList<>();
        for(AggIsland i: islands){
            x.addAll(i.getIslands());
        }
        return x;
    }

    /**
     *
     * @return the box
     */
    public Box getBox() {
        return box;
    }

    /**
     * update the posCurrentPlayer
     */
    public void nextPlayer(){

        posCurrentPlayer =
                playedCards.get(
                        ( playedCards.indexOf(
                                selectPlayedCardByIdPlayer(getCurrentPlayerPos())
                        )+1 ) % playedCards.size()
                ).getPlayer().getID();

        notify(posCurrentPlayer);
    }

    /**
     *
     * @return if the posCurrentPlayer is the last in the players order
     */
    public boolean isThisLastPlayer() {
        /*
        int indexOfCurrentPlayer =
                getPlayedCards().indexOf(
                        selectPlayedCardByIdPlayer(
                                getCurrentPlayerPos()
                        )
                );
        int indexOfLastPlayer = getPlayedCards().size() - 1;


        return indexOfCurrentPlayer == indexOfLastPlayer;

         */

        for(int i=0; i<playedCards.size(); i++)
            System.out.println("id player -------- "+playedCards.get(i).getPlayer().getID());

        return posCurrentPlayer == playedCards.get(playedCards.size()-1).getPlayer().getID();
    }


    /**
     *
     * @return a new ModelPro with same number of player of this
     */
    public ModelPro upgradeToTablePro() throws Exception {
        return new ModelPro(players.length);
    }

    /**
     * replace the teams
     * @param teams new teams
     */
    public void setTeams(Team[] teams) {
        this.teams = teams;
    }

    /**
     * replace the clouds
     * @param clouds new clouds
     */
    public void setClouds(Cloud[] clouds) {
        this.clouds = clouds;
    }

    /**
     * replace the aggregations
     * @param islands new aggregations
     */
    public void setIslands(ArrayList<AggIsland> islands) {
        this.islands = islands;
    }

    /**
     * replace the bag
     * @param bag new bag
     */
    public void setBag(Bag bag) {
        this.bag = bag;
    }

    /**
     * replace the mother nature
     * @param motherNature new mother nature
     */
    public void setMotherNature(MotherNature motherNature) {
        this.motherNature = motherNature;
    }

    /**
     * replace the professors
     * @param professors new professors
     */
    public void setProfessors(ArrayList<Professor> professors) {
        this.box.setProfessors( professors );
    }

    /**
     * replace the decks
     * @param decks new decks
     */
    public void setDecks(Deck[] decks) {
        this.decks = decks;
    }

    /**
     * replace the players
     * @param players new players
     */
    public void setPlayers(Player[] players) {
        this.players = players;
    }

    /**
     * replace the posCurrentPlayer
     * @param posCurrentPlayer new posCurrentPlayer
     */
    public void setPosCurrentPlayer(int posCurrentPlayer) {
        this.posCurrentPlayer = posCurrentPlayer;
    }

    /**
     * replace the played cards
     * @param playedCards new played cards
     */
    public void setPlayedCards(ArrayList<PlayedCard> playedCards){
        this.playedCards = playedCards;
    }

    /**
     * set the box to a new BoxPro
     */
    public void boxToPro(){
        box=new BoxPro();
    }

    /**
     * convert an array of objects in a list with the same objects
     * @param objects array to convert
     * @return list with the same objects of the given array
     */
    public ArrayList<Object> arrayToArrayList(Object[] objects){
        ArrayList<Object> arrayList=new ArrayList();
        for(Object o : objects){
            arrayList.add(o);
        }
        return arrayList;
    }
    //-----------  metodi partita -----------//

    /**
     * allow the current player to use a card
     * @param valCard value of the card you want to play
     * @throws Exception if in this turn another player used the same card
     */
    public void playCard(int valCard) throws Exception {

        for(PlayedCard p : playedCards){
            if(p.getCard()!=null && valCard == p.getCard().getVal())
                throw new Exception("Card already played\n");
        }


            selectPlayedCardByPlayer(getCurrentPlayer()).setCard(getCurrentPlayer().useCard(valCard));
        notify(arrayToArrayList(players));
        notify(playedCards);
        if(getCurrentPlayer().getOriginalDeck().getCards().size()==0)flagCard=true;

    }

    /**
     * allow the current player to move a student from the board to the lane
     * @param colorStudent color of the student you want to move
     * @throws ExceptionLaneNotFound if there isn't a lane with same color of colorStudent
     * @throws ExceptionStudentNotFound if the current player doesn't have a player with same color of colorStudent in the board
     */
    public void moveStudentToLane(Col_Pawn colorStudent) throws ExceptionLaneNotFound, ExceptionStudentNotFound, ExceptionPlayerCantPay {

        getCurrentPlayer().moveStudentToLane(new Student(colorStudent));
        checkProf(colorStudent);
        notify(box);
        notify(arrayToArrayList(players));
    }

    /**
     * allow the current player to move a student from the board to an island
     * @param indexIsland index of the destination island
     * @param colorStudent color of the student of interest
     * @throws ExceptionStudentNotFound if the current player doesn't have a player with same color of colorStudent in the board
     * @throws ExceptionIslandNotFound if there isn't an island in the given index
     */
    public void moveStudentToIsland(int indexIsland, Col_Pawn colorStudent) throws ExceptionStudentNotFound, ExceptionIslandNotFound {

        getIsland(indexIsland).addStudent(getCurrentPlayer().removeStudent(new Student(colorStudent)));
        notify(islands);
        notify(arrayToArrayList(players));

    }

    /**
     * allow the current player to move mother nature on a new island
     * @param newPos index of the new island
     * @throws ExceptionNoTowerInBoard the conquer team doesn't have enough towers
     * @throws ExceptionIslandNotFound if there isn't an island in the given index
     * @throws ExceptionsNoSuchTowers if there isn't the specific tower
     * @throws ExceptionMotherNatureIllegalMovement if there are too much hop between current pos of mather nature and the destination island
     */
    public void moveMotherNature(int newPos) throws ExceptionNoTowerInBoard, ExceptionIslandNotFound, ExceptionsNoSuchTowers, ExceptionMotherNatureIllegalMovement {
        int currentPos= getPosMotherNature();
        int maxMov= getMaxMov();
        int move;

        if (newPos >= currentPos) {
            move = newPos - currentPos;
        } else {
            move = getIslands().size() + newPos - currentPos;
        }

        if(move<=maxMov && move>0){
            moveMotherNature(newPos, getTeams());
            notify(motherNature);
            notify(arrayToArrayList(players));
            notify(islands);
        }else{
            throw new ExceptionMotherNatureIllegalMovement();
        }

        if(getCurrentPlayer().getNumOfTower()==0){
            flagTowerOrIsland = true;
        }
    }

    /**
     * allow the current player to choose a cloud and get the students on it
     * @param indexCloud index of the chosen cloud
     * @throws ExceptionCloudYetChoose if the cloud is already been chosen
     * @throws ExceptionCloudNotFound if there isn't a cloud in the given index
     */
    public void chooseCloud(int indexCloud) throws ExceptionCloudYetChoose, ExceptionCloudNotFound {

        getCurrentPlayer().addStudentsToDoorWay(getCloud(indexCloud).removeStudents());

        notify(arrayToArrayList(players));
        notify(arrayToArrayList(clouds));

    }

    private void win(Team team){
        teamWinner=team;
        notify(teamWinner);
        System.out.println(" THE WINNER IS: "+getTeamWinner().getTeamColor());
    }

    /**
     * calculate if a team has won the game
     * @throws ExceptionsNoSuchTowers if there isn't the specific tower
     */
    public void win() throws ExceptionsNoSuchTowers {
        int[] towers = new int[teams.length];
        for (Team t : teams) {
            towers[t.getTeamColor().ordinal()] = t.getNumOfTower();
        }
            int max = 13;
            for (int i = 0; i < towers.length; i++) {
                if (max > towers[i]) {
                    max = towers[i];
                }
            }
            ArrayList<Integer> IDs = new ArrayList<>();
            for (int i = 0; i < towers.length; i++) {
                if (max == towers[i]) IDs.add(i);
            }
            if (IDs.size() == 1) win(teams[IDs.get(0)]);
            else {
                int[] professors = new int[IDs.size()];
                for (int i = 0; i < IDs.size(); i++) {
                    professors[i] = teams[IDs.get(i)].getProfessors().size();
                }
                int maxP = 0;
                for (int i = 0; i < professors.length; i++) {
                    if (maxP < professors[i]) {
                        maxP = professors[i];
                    }
                }
                int count = 0;
                int id = 0;
                for (int i = 0; i < professors.length; i++) {
                    if (maxP == professors[i]) {
                        count++;
                        id = i;
                    }
                }
                if (count == 1) win(teams[IDs.get(id)]);

            }


        }




    //-----------  metodi stato -----------//

    /**
     * method for the controller to let the current player play a card
     * @param valCard value of the card to play
     */
    public void actionPlayCard(int valCard) {
        state.playCard(valCard);
    }

    /**
     * method for the controller to let the current player move a student from the board to the lane
     * @param colorStudent color of the student of interest
     */
    public void actionMoveStudentToLane(Col_Pawn colorStudent){
        state.moveStudentToLane(colorStudent);
    }

    /**
     * method for the controller to let the current player move a student from the board to an island
     * @param indexIsland destination island
     * @param colorStudent color student of interest
     */
    public void actionMoveStudentToIsland(int indexIsland, Col_Pawn colorStudent){
        state.moveStudentToIsland(indexIsland, colorStudent);
    }

    /**
     * method for the controller to let the current player move mother nature
     * @param newPos destination island
     */
    public void actionMoveMotherNature(int newPos){
        state.moveMotherNature(newPos);
    }

    /**
     * method for the controller to let the current player to choose a cloud and get its content
     * @param indexCloud index of the cloud of interest
     */
    public void actionChooseCloud(int indexCloud){
        state.chooseCloud(indexCloud);
    }



    /**
     *
     * @return flagCard
     */
    public boolean isFlagCard() {
        return flagCard;
    }

    /**
     *
     * @return flagBagEmpty
     */
    public boolean isFlagBagEmpty() {
        return flagBagEmpty;
    }

    /**
     *
     * @return flagTower
     */
    public boolean isFlagTowerOrIsland() {
        return flagTowerOrIsland;
    }
}


