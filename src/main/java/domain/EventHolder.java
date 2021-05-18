package domain;

public class EventHolder {
    private Event event;
    private final static EventHolder INSTANCE = new EventHolder();
    private EventHolder() {}
    public static EventHolder getInstance() {
        return INSTANCE;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }
}
