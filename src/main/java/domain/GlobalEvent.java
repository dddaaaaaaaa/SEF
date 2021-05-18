package domain;


import java.util.Date;

public class GlobalEvent extends Event{
    public GlobalEvent(Date date, String eventName, String observations, String host, String location) {
        super(date, eventName, observations, host, location);
    }
}
