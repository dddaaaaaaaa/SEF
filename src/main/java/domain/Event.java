package domain;

import java.util.Date;

public abstract class Event {
    Date date;
    String eventName;
    String observations;
    String host;
    String location;


    public Event(Date date, String eventName, String observations, String host, String location) {
        this.date = date;
        this.eventName = eventName;
        this.observations = observations;
        this.host = host;
        this.location = location;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
