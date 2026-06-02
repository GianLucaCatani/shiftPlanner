package com.shiftplanner.engineering.observer;

import java.util.ArrayList;
import java.util.List;

/*CLASSE ASTRATTA - rappresenta l'oggetto che viene OSSERVATO.
 * Mantiene la lista degli observer e li notifica quando cambia.
 */
public abstract class Subject {
    
    private final List<Observer> observers = new ArrayList<>();
    
    public void attach(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void detach(Observer observer) {
        observers.remove(observer);
    }
    
    protected void notifyObservers() {
        List<Observer> copy = new ArrayList<>(observers);
        for (Observer observer : copy) {
            observer.update(this);  // Chiama il metodo dell'interfaccia Observer
        }
    }
}