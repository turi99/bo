package it.polimi.ingsw.utils.observer;

public interface Observer<T> {

    public void update(T message);

}