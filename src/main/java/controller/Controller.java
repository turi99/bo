package controller;

import it.polimi.ingsw.model.Exceptions.ExceptionIllegalArgument;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.Model;

import java.util.EventListener;
import java.util.EventObject;

public class Controller implements EventListener {

    protected Model model;

    public Controller(int num){
        model = new Model(num);
    }

    public Controller(Model model){
        this.model = model;
    }

    /**
     *
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * replace the model
     * @param model new model
     */
    public void setModel(Model model) {
        this.model = model;
    }
    /*----------------------------------------------------------------------------*/

    /**
     *
     * @param player string containing the id player of interest
     * @return true if the current player has the same id of the given one
     */
    boolean isCurrentPlayer(String player){
        if(player == null || player.equals("")) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument e) {
                model.notify(
                        "" + model.getCurrentPlayerPos() + "," +
                                e.getMessage()
                );
                return false;
            }
        }
        int pos = -1;
        try {
            pos = Integer.parseInt(player);
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
        return pos==model.getCurrentPlayerPos();
    }

    /**
     * check if the instruction came from the current player, in that case call the request instruction
     * @param instruction instruction parameter
     * @throws ExceptionIllegalArgument if the instruction is null or empty or wrong format
     */
    public void switchInstruction(String instruction) throws ExceptionIllegalArgument {
        if(instruction == null || instruction == ""){
            throw new ExceptionIllegalArgument();
        }
        String[] s=instruction.split(";");
        if(s.length < 2) {
                throw new ExceptionIllegalArgument();
        }
        String[] r=s[0].split(",");
        if(r.length < 2) {
                throw new ExceptionIllegalArgument();

        }

        int choose;
        try {
            choose=Integer.parseInt(r[1]);
        }catch (NumberFormatException e) {
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
            }
        }
    }

    /**
     * check if the parameters contains right type of input to call model method
     * @param parameters parameters of activation
     */
    public void instructionPlayCard( String parameters) {
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

        int valCard = -1;
        try {
            valCard = Integer.parseInt(parameters);
        }catch (NumberFormatException e) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument ex) {
                model.notify(
                        ""+model.getCurrentPlayerPos()+","+
                                e.getMessage()
                 );
                return;
            }
        }

        model.actionPlayCard(valCard);
    }

    /**
     * check if the parameters contains right type of input to call model method
     * @param parameters parameters of activation
     */
    public void instructionMoveStudentToLane( String parameters) {
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

        int color = -1;
        try {
            color = Integer.parseInt(parameters);
        }catch (NumberFormatException e) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument ex) {
                model.notify(
                        ""+model.getCurrentPlayerPos()+","+
                                e.getMessage()
                );
                return;
            }
        }

        Col_Pawn colorStudent= null;
        try {
            colorStudent=Col_Pawn.values()[color];
        }catch (Exception e) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument ex) {
                model.notify(
                        ""+model.getCurrentPlayerPos()+","+
                                ex.getMessage()
                );
                return;
            }
        }

        model.actionMoveStudentToLane(colorStudent);
    }

    /**
     * check if the parameters contains right type of input to call model method
     * @param parameters parameters of activation
     */
    public void instructionMoveStudentToIsland( String parameters) {
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

        String[] par=parameters.split(",");

        if(par.length < 2) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument e) {
                model.notify(
                        "" + model.getCurrentPlayerPos() + "," +
                                e.getMessage()
                );
            }
        }


        Col_Pawn colorStudent = null;
        int idIsland = -1;
        try {
            idIsland=Integer.parseInt(par[0]);
            colorStudent=Col_Pawn.values()[Integer.parseInt(par[1])];
        }catch (Exception e) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument ex) {
                model.notify(
                        ""+model.getCurrentPlayerPos()+","+
                                ex.getMessage()
                );
                return;
            }
        }

        model.actionMoveStudentToIsland(idIsland,colorStudent);
    }

    /**
     * check if the parameters contains right type of input to call model method
     * @param parameters parameters of activation
     */
    public void instructionMoveMotherNature( String parameters) {
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

        int newPos = -1;
        try {
            newPos=Integer.parseInt(parameters);
        }catch (NumberFormatException e) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument ex) {
                model.notify(
                        ""+model.getCurrentPlayerPos()+","+
                                ex.getMessage()
                 );
                return;
            }
        }

        model.actionMoveMotherNature(newPos);
    }

    /**
     * check if the parameters contains right type of input to call model method
     * @param parameters parameters of activation
     */
    public void instructionChooseCloud( String parameters) {
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

        int cloud = -1;
        try {
            cloud=Integer.parseInt(parameters);
        }catch (NumberFormatException e) {
            try {
                throw new ExceptionIllegalArgument();
            } catch (ExceptionIllegalArgument ex) {
                model.notify(
                        ""+model.getCurrentPlayerPos()+","+
                                ex.getMessage()
                 );
                return;
            }
        }

        model.actionChooseCloud(cloud);
    }


    /*----------------------------------------------------------------------------*/



    public void eventPerformed(EventObject evt) {
        System.out.println("Event Performed: " + evt.getSource().toString());
    }


    /*----------------------------------------------------------------------------*/

    //MAIN DI TEST



/*
    public static void main(String[] args){

        Controller.startTest1(2);

    }


    public static void startTest1(int numberOfPlayers){
        Model model = new Model(numberOfPlayers);
        Controller game = new Controller(model);

        Scanner stdin = new Scanner(System.in);

        while (true){
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

                    System.out.print("isola o lane? ");
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
        System.out.println("Turno giocatore "+model.getCurrentPlayer().getID()+" : ");

        //CARTE DEL GIOCATORE
        System.out.print("CARTE -> ");
        for(Card c : model.getCurrentPlayer().getOriginalDeck().getCards()){
            System.out.print("("+c.getVal()+")"+c.getMov()+" , ");
        }
        System.out.println();

    }

    public void printTurno(){

        //NUOVO ORDINE TURNI
        System.out.print("ORDINE TURNI -> ");
        for(int i=0; i<model.getPlayedCards().size(); i++){
            System.out.print(model.getPlayedCards().get(i).getPlayer().getID()+",");
        }
        System.out.println();

        //DI CHI è IL TURNO?
        System.out.println("Turno giocatore "+model.getCurrentPlayer().getID()+" : ");

    }

    public void printBoard(){
        Board board = model.getCurrentPlayer().getOwnBoard();

        System.out.print("INGRESSO -> ");
        for(Col_Pawn c : Col_Pawn.values()){
            int num = (int) board.getStudents().stream().filter(student -> student.getColor() == c).count();
            if(num>0){
                System.out.print(num+" "+c+", ");
            }
        }

        System.out.println();

        try {
            System.out.println("TORRI -> "+model.getTeamByPlayer(model.getCurrentPlayer()).getNumOfTower()+" "+model.getTeamByPlayer(model.getCurrentPlayer()).getTeamColor());
        } catch (ExceptionTeamNotFound e) {
            model.getErrorObserver().notify(
                    ""+model.getCurrentPlayerPos()+","+
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

        System.out.println("codice colori : YELLOW 0, BLUE 1, GREEN 2, RED 3, PINK 4");
    }

    public void printIsole() {
        System.out.println("ISOLE : ");
        AggIsland[] islands = model.getIslands().toArray(new AggIsland[0]);


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
                    model.getErrorObserver().notify(
                            ""+model.getCurrentPlayerPos()+","+
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
        Cloud[] clouds = model.getClouds();

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

 */
 

}


