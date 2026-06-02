package com.shiftplanner.engineering.observer;

/**
 * INTERFACCIA - rappresenta l'oggetto che OSSERVA.
 * Definisce il contratto che tutti gli osservatori devono rispettare.
 */
public interface Observer {
    void update(Subject subject);
}