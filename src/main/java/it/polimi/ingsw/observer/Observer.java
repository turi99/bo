package it.polimi.ingsw.observer;

public interface Observer<T> {

    public void update(T message);

}