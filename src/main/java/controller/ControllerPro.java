package controller;

import it.polimi.ingsw.model.Exceptions.*;
import it.polimi.ingsw.model.ModelPro;

public class ControllerPro extends Controller {

    public ControllerPro(ModelPro modelPro) {
        super(modelPro);
    }

    /**
     * check if the parameters contains right type of input to call model method
     * @param parameters parameters of activation
     */
    public void instructionActivateCard(String parameters) {
        if(parameters == null || parameters.equals("")) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument e) {
                model.notify(
                        "" + model.getCurrentPlayerPos() + "," +
                                e.getMessage()
                );
                return;
            }
        }

        String[] split = parameters.split(",");

        int idCard = -1;
        try {
            idCard=Integer.parseInt(split[0]);
        }catch (NumberFormatException e) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument ex) {
                model.notify(
                        ""+model.getCurrentPlayerPos()+","+
                                e.getMessage()
                 );
            }
        }

        int shift = idCard<10 ? 2 : 3;
        String cardParameters = split.length==1 ? null : parameters.substring(shift);

        getModel().actionActivateCard(idCard,cardParameters);

    }

    /**
     *
     * @return this model
     */
    public ModelPro getModel(){
        return (ModelPro) model;
    }

    /**
     * check if the instruction came from the current player, in that case call the request instruction
     * @param instruction instruction parameter
     * @throws ExceptionIllegalArgument if the instruction is null or empty or wrong format
     */
    public void switchInstruction(String instruction) throws ExceptionIllegalArgument {
        if(instruction == null || instruction.equals(""))
            throw new ExceptionIllegalArgument();

        String[] s=instruction.split(";");
        if(s.length < 2) {
            throw new ExceptionIllegalArgument();
        }
        String[] r=s[0].split(",");
        if(r.length < 2) {
            throw new ExceptionIllegalArgument();

        }

        int choose=-1;
        try {
            choose = Integer.parseInt(r[1]);
        }catch(NumberFormatException e){
            throw new ExceptionIllegalArgument();
        }

        if(isCurrentPlayer(r[0])){
            switch (choose){
                case 0: {
                    instructionPlayCard(s[1]);
                    break;
                }
                case 1: {
                    instructionChooseCloud(s[1]);
                    break;

                }
                case 2: {
                    instructionMoveMotherNature(s[1]);
                    break;
                }
                case 3: {
                    instructionMoveStudentToIsland(s[1]);
                    break;
                }
                case 4: {
                    instructionMoveStudentToLane(s[1]);
                    break;
                }
                case 5: {
                    instructionActivateCard(s[1]);
                    break;
                }
            }
        }
    }




/*
    public static void main(String[] args){

        ControllerPro.startTest1(2);

    }


    public static void startTest1(int numberOfPlayers) {
        ModelPro model = null;
        try {
            model = new ModelPro(numberOfPlayers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Player p : model.getPlayers()){
            ((PlayerPro) p).addCoins(10);
        }

        model = model.initializeModelPro(model);

        System.out.println(model.getClass());

        ControllerPro game = new ControllerPro(model);

        Scanner stdin = new Scanner(System.in);

        while (true){
            game.printActivableCard();


            String stato = game.getModel().getState().getName();

            System.out.println("STATO DELLA PARTITA : "+stato);

            game.print(stato);

            switch (stato){
                case "playCard" :
                {
                    System.out.println();

                    System.out.print("Seleziona una carta : ");
                    game.instructionPlayCard(stdin.nextLine());

                    System.out.println();
                    break;
                }
                case "turn" :
                {
                    System.out.println();

                    System.out.print("isola o lane o attiva? ");
                    String input = stdin.nextLine();
                    switch (input){
                        case "lane":
                        {
                            System.out.print("studente da mettere in lane : ");
                            game.instructionMoveStudentToLane(stdin.nextLine());
                            break;
                        }
                        case "isola":
                        {
                            System.out.print("idIsola,studente : ");
                            game.instructionMoveStudentToIsland(stdin.nextLine());
                            break;
                        }
                        case "attiva":
                        {
                            System.out.print("idCarta : ");
                            game.instructionActivateCard(stdin.nextLine());
                            break;
                        }
                    }

                    System.out.println();
                    break;
                }
                case "moveMotherNature" :
                {
                    System.out.println();

                    System.out.print("in quale isola spostare madre natura? ");
                    game.instructionMoveMotherNature(stdin.nextLine());

                    game.printIsole();

                    System.out.println();
                    break;
                }
                case "selectCloud" :
                {
                    System.out.println();

                    System.out.print("quale nuvola? ");
                    game.instructionChooseCloud(stdin.nextLine());

                    System.out.println();
                    break;
                }
            }

        }

    }


    public void print(String stato){
        printTurno();
        switch (stato){
            case "playCard" :
            {
                printPlayCard();
                break;
            }
            case "turn" :
            {
                printActivableCard();
                printBoard();
                printIsole();
                break;
            }
            case "moveMotherNature" :
            {
                printBoard();
                printIsole();
                break;
            }
            case "selectCloud" :
            {
                printCloud();
                break;
            }
        }
    }

    public void printPlayCard(){

        //DI CHI è IL TURNO?
        System.out.println("Turno giocatore "+getModel().getCurrentPlayer().getID()+" : ");

        //CARTE DEL GIOCATORE
        System.out.print("CARTE -> ");
        for(Card c : getModel().getCurrentPlayer().getOriginalDeck().getCards()){
            System.out.print("("+c.getVal()+")"+c.getMov()+" , ");
        }
        System.out.println();

    }

    public void printTurno(){

        //NUOVO ORDINE TURNI
        System.out.print("ORDINE TURNI -> ");
        for(int i=0; i<getModel().getPlayedCards().size(); i++){
            System.out.print(getModel().getPlayedCards().get(i).getPlayer().getID()+",");
        }
        System.out.println();

        //DI CHI è IL TURNO?
        System.out.println("Turno giocatore "+getModel().getCurrentPlayer().getID()+" : ");

    }

    public void printBoard(){
        Board board = getModel().getCurrentPlayer().getOwnBoard();

        System.out.print("INGRESSO -> ");
        for(Col_Pawn c : Col_Pawn.values()){
            int num = (int) board.getStudents().stream().filter(student -> student.getColor() == c).count();
            if(num>0){
                System.out.print(num+" "+c+", ");
            }
        }

        System.out.println();

        try {
            System.out.println("TORRI -> "+getModel().getTeamByPlayer(getModel().getCurrentPlayer()).getNumOfTower()+" "+getModel().getTeamByPlayer(model.getCurrentPlayer()).getTeamColor());
        } catch (ExceptionTeamNotFound e) {
            getModel().getErrorObserver().notify(
                    ""+getModel().getCurrentPlayerPos()+","+
                            e.getMessage()
            );
        }


        System.out.print("LANE -> ");
        for(Lane l : board.getLanes()){
            if(l.getNumStudents()>0)
                System.out.print(l.getNumStudents()+" "+l.getColor()+", ");
        }

        System.out.println();

        System.out.print("PROF -> ");
        for(Professor p : board.getProfessors()){
            System.out.print(p.getColor()+", ");
        }

        System.out.println();

        System.out.print("COINS -> "+((PlayerPro) getModel().getCurrentPlayer()).getCoins());

        System.out.println();


        System.out.println("codice colori : YELLOW 0, BLUE 1, GREEN 2, RED 3, PINK 4");
    }

    public void printIsole() {
        System.out.println("ISOLE : ");
        AggIsland[] islands = getModel().getIslands().toArray(new AggIsland[0]);


        for (int i=0; i<islands.length; i++){
            System.out.print("("+i+") ");

            System.out.print("Student: ");
            for (Col_Pawn col : Col_Pawn.values()){
                if(islands[i].getNumStudents(col)>0)
                    System.out.print(""+islands[i].getNumStudents(col)+" "+col+", ");
            }

            if(islands[i].isConquered()){
                try {
                    System.out.print(" TOWER: "+islands[i].getNumOfTower()+""+islands[i].getConquer().get()+" ");
                } catch (ExceptionIslandNotConquered e) {
                    getModel().getErrorObserver().notify(
                            ""+getModel().getCurrentPlayerPos()+","+
                                    e.getMessage()
                    );
                }
            }


            if(islands[i].hasMotherNature()){
                System.out.print(" - MOTHERNATURE - ");
            }


            System.out.println();
        }


    }

    public void printCloud(){
        Cloud[] clouds = getModel().getClouds();

        System.out.print("NUVOLE : ");
        for(int i=0; i<clouds.length; i++){
            System.out.print("("+i+") ");
            for (Col_Pawn col : Col_Pawn.values()){
                int num = (int) clouds[i].getStudents().stream().filter(student -> student.getColor() == col).count();
                if(num>0)
                    System.out.print(""+num+" "+col+", ");
            }
        }

        System.out.println();

    }

    public void printActivableCard(){
        ArrayList<CharacterCard> activeGameCard = getModel().getActiveGameCards();

        System.out.println("CARTE SPECIALI : ");

        for(CharacterCard c : activeGameCard){
            c.print();
            System.out.println("---STATO--- "+c.isActive());
        }
    }

 */
}
