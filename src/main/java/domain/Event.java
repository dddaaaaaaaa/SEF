package domain;

import java.util.Date;

public abstract class Event {
    Date date;
    String eventName;
    String observations;

    public Event(Date date, String eventName, String observations) {
        this.date = date;
        this.eventName = eventName;
        this.observations = observations;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
