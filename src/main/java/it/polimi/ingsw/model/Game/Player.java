package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.*;
import it.polimi.ingsw.model.GamePro.PlayerPro;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private String userName;
    private int ID;
    private Team team;
    private Deck ownDeck;
    private Board ownBoard;
    //private static final long serialVersionUID = 42L;


    public Player(int id, Deck deck, Board board) {
        ID = id;
        ownDeck = deck;
        ownBoard = board;
    }


    /**
     *
     * @return name of the player
     */
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString(){
        return "Id : "+getID()+"\n\nUserName : "+userName+"\n\nTeam : "+getTeam().getTeamColor()+"\n\nDeck :\n"+getOriginalDeck()+"\n2\nBoard :\n"+getOwnBoard();
    }

    public Player(Player originPlayer) {
        ID = originPlayer.ID;
        team=originPlayer.team;
        ownDeck = originPlayer.ownDeck;
        ownBoard = originPlayer.ownBoard;
    }

    /**
     *
     * @return this player with deck=null
     */
    public Player withoutDeck(){
        this.ownDeck=null;
        return this;
    }

    /**
     *
     * @return board of the player
     */
    public Board getOwnBoard() {
        return ownBoard;
    }

    /**
     * replace the team
     * @param team new team
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     *
     * @return player team
     */
    public Team getTeam() {
        return team;
    }

    /**
     *
     * @return player id
     */
    public int getID() {
        return ID;
    }

    /**
     * replace the id
     * @param arg_ID new id
     */
    public void setID(int arg_ID) {
        ID = arg_ID;
    }

    /**
     *
     * @return deck ofthe player
     */
    public Deck getOriginalDeck() {
        return ownDeck;
    }

    /**
     * remove the card with same value of valCard, and return it
     * @param valCard value of the card ypu want to use
     * @return the card with same value of valCard, if it's present
     * @throws ExceptionCardNotFound if there isn't a card with same value of valCard
     */
    public Card useCard(int valCard) throws ExceptionCardNotFound {
        Card card=ownDeck.useCard(valCard);
        return card;
    }

    /**
     * add a student to the board
     * @param student student you want to add to the board
     */
    public void addStudentToDoorWay(Student student) {
        ownBoard.addStudent(student);
    }

    /**
     * add a list of students to the board
     * @param students list of students you want to add to the board
     */
    public void addStudentsToDoorWay(ArrayList<Student> students) {
        ownBoard.addStudents(students);
    }

    /**
     * move a student from the board to a lane
     * @param student student you want to move
     * @throws ExceptionLaneNotFound if there isn't a lane with the same color of the given student
     * @throws ExceptionStudentNotFound if the given student is not present in the board
     */
    public void moveStudentToLane(Student student) throws ExceptionLaneNotFound, ExceptionStudentNotFound {
        ownBoard.moveStudentToLane(student);
    }

    /**
     *
     * @return list of all professor in the board
     */
    public ArrayList<Professor> getAllProfessors(){
        return ownBoard.getProfessors();
    }

    /**
     * add a professor on the board
     * @param prof professor you want to add
     */
    public void addProfessor(Professor prof) {
        ownBoard.addProfessor(prof);
    }

    /**
     *
     * @param colProf color of the professor you want to remove
     * @return professor with the same color of colProf, if there is one in the board
     * @throws ExceptionProfessorNotFound if there isn't a professor with same color of colProf in the board
     */
    public Professor removeProfessor(Col_Pawn colProf) throws ExceptionProfessorNotFound {
        return ownBoard.removeProfessor(colProf);
    }

    /**
     *
     * @param student student you want to remove
     * @return student, if is in the board
     * @throws ExceptionProfessorNotFound if student is not in the board
     */
    public Student removeStudent(Student student) throws ExceptionStudentNotFound {
        return ownBoard.removeStudent(student);
    }

    /**
     *
     * @param tower tower you want to add to the board
     */
    public void addTower(Tower tower) {
        ownBoard.addTower(tower);
    }

    /**
     *
     * @param towers list of towers you want to add in the board
     */
    public void addTowers(ArrayList<Tower> towers) {
        ownBoard.addTowers(towers);
    }

    /**
     * remove a tower from the board and return it
     * @return tower removed from the board
     * @throws ExceptionNoTowerInBoard if there aren't towers in the board
     */
    public Tower removeTower() throws ExceptionNoTowerInBoard {
        return ownBoard.removeTower();
    }

    /**
     *
     * @return list of all towers in the board
     */
    public ArrayList<Tower> getAllTowers() {
        return ownBoard.getTowers();
    }

    /**
     *
     * @return clone of this player, of class PlayerPro
     */
    public PlayerPro upgradeToPro(){
        return new PlayerPro(this);
    }
//---------------------------------------------------------------------------

    /**
     *
     * @param colStudent student color of interest
     * @return number of student in the lane with color equals to colStudent
     * @throws ExceptionLaneNotFound there isn't a lane with same color of colStudent
     */
    public int getNumStudLane(Col_Pawn colStudent) throws ExceptionLaneNotFound {
        return this.ownBoard.getNumStudLaneByColor(colStudent);
    }

    /**
     *
     * @param colProfessor professor color of interest
     * @return true, if there is a professor with color equals to colProfessor in board. else, false
     */
    public boolean containsProfessor(Col_Pawn colProfessor){
        ArrayList<Professor> x=this.getAllProfessors();
        for(Professor p: x){
            if(p.getColor()==colProfessor) return true;
        }
        return false;
    }

    /**
     *
     * @return number of towers in board
     */
    public int getNumOfTower(){
        return this.ownBoard.getNumTower();
    }
}