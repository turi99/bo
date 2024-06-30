package it.polimi.ingsw.test;

import it.polimi.ingsw.model.Exceptions.ExceptionsNoSuchTowers;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class IslandTest {



    @Test
    public void getStudentTest() {
        ArrayList<Student> students= new ArrayList<>();
        for(int i=0;i<5;i++){
            students.add(new Student(Col_Pawn.BLUE));
        }
        Island island=new Island(students);
        Assertions.assertEquals(island.getStudents(),students);
    }



@Test
    public void getConquerTest() {
    Island island = new Island();
    Assertions.assertEquals(island.getConquer(), Optional.empty());
    island.conquer(new Tower(Col_Tower.BLACK));
    Assertions.assertEquals(island.getConquer().orElse(null), Col_Tower.BLACK);
    island.conquer(new Tower(Col_Tower.WHITE));
    Assertions.assertEquals(island.getConquer().orElse(null), Col_Tower.WHITE);

}
@Test
    public void conquerTest(){
        Island island=new Island();
        Tower tower=new Tower(Col_Tower.BLACK);
        Tower tower2=new Tower(Col_Tower.WHITE);
        island.conquer(tower);
        Assertions.assertEquals(island.getConquer().orElse(null),tower.getColor());
        Tower tower1=island.conquer(tower2).orElse(null);
        Assertions.assertEquals(island.getConquer().orElse(null),tower2.getColor());
        Assertions.assertEquals(tower1,tower);
}

@Test
    public void influenceTest(){
    Model model=new Model(2);
    Random random=new Random();
    int influence=0;
    AggIsland island=null;
    Team team=null;
    int num=random.nextInt(0,model.getIslands().size());
    try {
        team = model.getTeamByColor(Col_Tower.BLACK);
        team.getPlayers()[0].addProfessor(new Professor(Col_Pawn.values()[0]));
        team.getPlayers()[0].addProfessor(new Professor(Col_Pawn.values()[1]));
        team.getPlayers()[0].addProfessor(new Professor(Col_Pawn.values()[2]));
        team.getPlayers()[0].addProfessor(new Professor(Col_Pawn.values()[3]));
        team.getPlayers()[0].addProfessor(new Professor(Col_Pawn.values()[4]));
        island= model.getIslands().get(num);
        influence=island.getInfluenceTeam(model.getTeamByColor(Col_Tower.BLACK));
    }catch (Exception e){
        System.out.println(e);
    }
    int influence2=0;
    ArrayList<Professor> x=team.getProfessors();
    for (Professor p: x){
        influence2= (int) (influence2+ island.getNumStudents(p.getColor()));
    }
    if(island.isConquered()){
        try {
            if (island.getTower().getColor() == team.getTeamColor()) {
                influence2 += 1;
            }
        } catch (ExceptionsNoSuchTowers e) {
            e.printStackTrace();
        }
    }
    System.out.println(influence2);
    Assertions.assertEquals(influence,influence2);
    }
}
