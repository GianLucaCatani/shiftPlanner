package com.shiftplanner.engineering;

import com.shiftplanner.engineering.observer.Observer;
import com.shiftplanner.model.Schedule;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/*Verifica del Pattern Observer.
Verifica che quando Schedule.publish() viene chiamato,tutti gli Observer registrati vengano notificati.*/
class ScheduleObserverTest {

    @Test
    void publish_notifiesRegisteredObserver() {
        // GIVEN: un calendario e un Observer "spia" che conta le notifiche
        Schedule schedule = new Schedule(1L,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 7));

        // Usiamo una variabile array per aggirare il limite "effectively final" di Java
        int[] contatoreNotifiche = {0};

        Observer observerSpia = (subject) -> contatoreNotifiche[0]++;

        schedule.attach(observerSpia);

        // WHEN: pubblichiamo il calendario
        schedule.publish();

        // THEN: l'observer deve essere stato chiamato esattamente 1 volta
        assertEquals(1, contatoreNotifiche[0],
                "L'Observer deve essere notificato esattamente una volta dopo publish()");
    }

    @Test
    void publish_alreadyPublished_throwsException() {
        // GIVEN: un calendario già pubblicato
        Schedule schedule = new Schedule(1L,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 7));
        schedule.publish();

        // WHEN + THEN: pubblicare di nuovo deve lanciare IllegalStateException
        assertThrows(IllegalStateException.class, schedule::publish,
                "Non si può pubblicare un calendario già pubblicato");
    }
}