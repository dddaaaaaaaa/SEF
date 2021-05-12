package domain;

public abstract class Event {
    String date;
    String eventName;
    String observations;

    public Event(String date, String eventName, String observations) {
        this.date = date;
        this.eventName = eventName;
        this.observations = observations;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
