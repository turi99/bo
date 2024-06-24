package it.polimi.ingsw.observer;


import it.polimi.ingsw.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Observable<T>{

    private List<it.polimi.ingsw.utils.observer.Observer<T>> observers = new ArrayList<>();

    public void addObservers(it.polimi.ingsw.utils.observer.Observer<T> observer){
        observers.add(observer);
    }

    public void notify(T message){
        for(it.polimi.ingsw.utils.observer.Observer<T> observer: observers){
            observer.update(message);
        }
    }

    public void specificNotify(Observer<T> observer, T message){
        observer.update(message);
    }

}