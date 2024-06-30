package it.polimi.ingsw.model.GamePro.CharacterCards.Decorators;

import it.polimi.ingsw.model.Exceptions.*;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.Player;
import it.polimi.ingsw.model.Game.Professor;
import it.polimi.ingsw.model.Game.Student;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.PlayerPro;
import it.polimi.ingsw.model.ModelPro;

public class CardRuleProfDecorator extends ModelProDecorator {

    public CardRuleProfDecorator(ModelPro model, CharacterCard card){
        super(model,card);
    }

    private boolean thisCompareNumStud(int x, int y) {
        return x >= y;
    }

    /**
     *
     * @param x first integer
     * @param y second integer
     * @return if the cad is active, x >= y. else, x > y
     */
    @Override
    public boolean compareNumberStudent(int x, int y) {
        return card.isActive() ? thisCompareNumStud(x,y) : modelPro.compareNumberStudent(x, y);
    }

    /**
     * same of Model
     */
    @Override
    public void checkProf(Col_Pawn colProf){
        for(Professor p : modelPro.getProfessors()){
            if(p.getColor()==colProf){
                modelPro.moveProf(p);
                break;
            }
        }
        for(Player p: modelPro.getPlayers()){
            if(p.containsProfessor(colProf)){
                try {
                    if(compareNumberStudent(modelPro.getTeamByPlayer(modelPro.getCurrentPlayer()).getNumStudLane(colProf), modelPro.getTeamByPlayer(p).getNumStudLane(colProf))){
                        modelPro.moveProf(p,colProf);
                    }
                } catch (ExceptionLaneNotFound | ExceptionTeamNotFound e)  {
                    modelPro.notify(
                            ""+modelPro.getCurrentPlayerPos()+","+
                                    e.getMessage()
                     );
                }
                break;
            }
        }
    }

    /**
     * same of Model
     */
    @Override
    public void moveStudentToLane(Col_Pawn colorStudent) throws ExceptionLaneNotFound, ExceptionStudentNotFound, ExceptionPlayerCantPay {

        getCurrentPlayer().moveStudentToLane(new Student(colorStudent));
        checkProf(colorStudent);
        modelPro.notify(modelPro.getBox());
        modelPro.notify(arrayToArrayList(modelPro.getPlayers()));
        notify(arrayToArrayList(getPlayers()));
        if( ((PlayerPro)getCurrentPlayer()).checkStudentsForCoin(colorStudent) ) {
            giveCoinsToPlayer((PlayerPro) getCurrentPlayer(), 1) ;
            notify(arrayToArrayList(getPlayers()));
            notify(getBox());
        }
    }
}
