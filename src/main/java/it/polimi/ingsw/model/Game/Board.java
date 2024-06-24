package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.*;
import it.polimi.ingsw.utils.observer.Observable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private ArrayList<Student> students;
    private ArrayList<Tower> towers;
    private ArrayList<Professor> professors;
    private ArrayList<Lane> lanes;

    public Board(ArrayList<Student> students) {

        lanes = new ArrayList<>();
        this.students = new ArrayList<>(students);
        towers = new ArrayList<>();
        professors = new ArrayList<>();

        lanes.add(new Lane(Col_Pawn.YELLOW));
        lanes.add(new Lane(Col_Pawn.BLUE));
        lanes.add(new Lane(Col_Pawn.GREEN));
        lanes.add(new Lane(Col_Pawn.RED));
        lanes.add(new Lane(Col_Pawn.PINK));

    }

    /**
     *
     * @return list of all lanes
     */
    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    @Override
    public String toString(){
        String s="Hall :\n"+studentsToString()+"\n\nTowers : "+towers.size()+"\n\nProfessor :\n"+professorsToString()+"\n\nLanes :\n"+lanesToString();
        return s;

    }
    /**
     *
     * @return list of all students
     */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     *
     * @return list of all towers
     */
    public ArrayList<Tower> getTowers() {
        return  towers;
    }

    /**
     *
     * @return list of all professors
     */
    public ArrayList<Professor> getProfessors() {
        return  professors;
    }

    /**
     *
     * @return number of tower in the board
     */
    public int getNumTower(){
        return towers.size();
    }

    //----------------------------------------------------------//

    /**
     *
     * @param student student you want to in the board
     */
    public void addStudent(Student student){
        students.add(student);
    }

    /**
     *
     * @param students list of students you want to in the board
     */
    public void addStudents(ArrayList<Student> students){
        this.students.addAll(students);
    }

    /**
     *
     * @param student student you want to remove from the board
     * @return if the given student is present, return that student
     * @throws ExceptionStudentNotFound if the given student is not present
     */
    public Student removeStudent(@NotNull Student student) throws ExceptionStudentNotFound {
        int i;

        System.out.println(student.getColor());

        for(i=0; i<students.size() && students.get(i).getColor()!=student.getColor() ;i++);

        if(i==students.size()){
            throw new ExceptionStudentNotFound();
        }

        Student s = students.get(i);
        students.remove(i);

        return s;

    }

    /**
     * move the given student to the lane with the same color
     * @param student student you want to move
     * @throws ExceptionLaneNotFound if there isn't a lane with same color of the given student
     * @throws ExceptionStudentNotFound if the given student isn't present in the board
     */
    public void moveStudentToLane(@NotNull Student student) throws ExceptionLaneNotFound, ExceptionStudentNotFound {

        try {
            selectLaneByColor(student.getColor()).addStudent(removeStudent(student));
        } catch (ExceptionWrongStudentColor e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param color lane color of interest
     * @return lane with same color of the given one
     * @throws ExceptionLaneNotFound if there isn't a lane with same color of the given student
     */
    public Lane selectLaneByColor(Col_Pawn color) throws ExceptionLaneNotFound {
        int i;

        for(i=0; i<lanes.size() && lanes.get(i).getColor() != color; i++){}



        if(i==lanes.size()) throw new ExceptionLaneNotFound();

        return lanes.get(i);
    }

    /**
     *
     * @param professor professor you want to add
     */
    public void addProfessor(Professor professor){
        professors.add(professor);
    }

    /**
     *
     * @param colorProfessor color of the professor you want to remove
     * @return professor of the same color of the given one
     * @throws ExceptionProfessorNotFound if a professor with the same color of colorProfessor isn't present
     */
    public Professor removeProfessor(Col_Pawn colorProfessor) throws ExceptionProfessorNotFound {
        int i;

        for(i=0; i<professors.size() && professors.get(i).getColor() != colorProfessor ;i++){}

        if(i==professors.size()){
            throw new ExceptionProfessorNotFound();
        }

        Professor p = professors.get(i);
        professors.remove(i);

        return p;

    }

    /**
     *
     * @param tower tower you want to add
     */
    public void addTower(Tower tower){
        towers.add(tower);
    }

    /**
     *
     * @param towers list of towers you want to add
     */
    public void addTowers(ArrayList<Tower> towers) {
        this.towers.addAll(towers);
    }
    public Tower removeTower() throws ExceptionNoTowerInBoard {

        if(getNumTower()==0){
            throw new ExceptionNoTowerInBoard();
        }

        Tower t = towers.get(0);
        towers.remove(0);

        return t;
    }

//-----------------------------------------------------------------

    /**
     * give the number of student in a specific lane
     * @param colStud  student color of interest
     * @return number of student in the lane with same color of colStud
     * @throws ExceptionLaneNotFound if there isn't a lane with the same color of colStud
     */
    public int getNumStudLaneByColor(Col_Pawn colStud) throws ExceptionLaneNotFound {

        return this.selectLaneByColor(colStud).getNumStudents();

    }


    public String studentsToString(){
        int[] stud=new int[5];
        for (Student s: students){
            stud[s.getColor().ordinal()]+=1;
        }
        String s="\t"+ Col_Pawn.values()[0]+" : "+stud[0] ;
        for(int i=1;i<5;i++){
            s= "\t"+Col_Pawn.values()[i]+" : "+stud[i] +"\n"+s;
        }
        return s;
    }

    private String professorsToString() {
        if (professors.size()==0)return "\tnull";
        String s="\t"+professors.get(0).getColor().name();
        for(int i=1; i<professors.size();i++){
            s="\t"+professors.get(i).getColor()+"\n"+s;
        }
        return s;
    }


    private String lanesToString() {
        String s="\t"+lanes.get(0).toString();
        for(int i=1;i<lanes.size();i++){
            s="\t"+lanes.get(i)+"\n"+s;
        }
        return s;
    }

}
