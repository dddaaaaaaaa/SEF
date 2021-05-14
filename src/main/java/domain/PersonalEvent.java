package domain;

import javafx.scene.control.DatePicker;

import java.util.Date;

public class PersonalEvent extends Event{
    public PersonalEvent(Date date, String eventName, String observations) {
        super(date, eventName, observations);
    }
}
