package com.shiftplanner.bean;

import com.shiftplanner.exceptions.PeriodValidationException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/*Validazione dei Bean di Input.
Verifica che ScheduleBean lanci PeriodValidationException quando la data fine è precedente alla data inizio.*/
class ScheduleBeanTest {
    @Test
    void setEndDate_validDate_noException() {
        // GIVEN
        ScheduleBean bean = new ScheduleBean();
        // WHEN + THEN: periodo valido → nessuna eccezione
        assertDoesNotThrow(() -> {
            bean.setStartDate(LocalDate.of(2026, 6, 1));
            bean.setEndDate(LocalDate.of(2026, 6, 7));
        });
    }

    @Test
    void setEndDate_endBeforeStart_throwsException() {
        // GIVEN: data inizio = 10 Giugno
        ScheduleBean bean = new ScheduleBean();
        assertDoesNotThrow(() -> bean.setStartDate(LocalDate.of(2026, 6, 10)));

        // WHEN + THEN: data fine = 5 Giugno → DEVE lanciare PeriodValidationException
        assertThrows(PeriodValidationException.class, () ->
            bean.setEndDate(LocalDate.of(2026, 6, 5))
        );
    }
}