package org.lanaeus.weeklycalendarwithsqlite;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class Event {

    public static String EVENT_EDIT_EXTRA = "eventEdit";

    public static ArrayList<Event> eventsList = new ArrayList<>();
    public static ArrayList<Event> eventsForDate(LocalDate date){
        ArrayList<Event> events = new ArrayList<>();
        for(Event event: eventsList){
            if(event.getDate().equals(date) && event.getDeleted() == null){
                events.add(event);
            }
        }

        return events;
    }

    private String name;
    private LocalDate date;
    private LocalTime time;
    private int id;
    private Date deleted;


    public Event(int id, String name, LocalDate date, LocalTime time, Date deleted) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.deleted = deleted;
    }

    public Event(int id, String name, LocalDate date, LocalTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.deleted = null;
    }

    public static Event getEventForID(int passedEventID) {
        for (Event event : eventsList){
            if (event.getId() == passedEventID)
                return event;
        }
        return null;
    }

    public static ArrayList<Event> nonDeletedEvents(){
        ArrayList<Event> nonDeleted = new ArrayList<>();
        for (Event event : eventsList){
            if (event.getDeleted() == null){
                nonDeleted.add(event);
            }
        }
        return nonDeleted;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Date getDeleted() { return deleted; }

    public void setDeleted(Date deleted) { this.deleted = deleted; }
}
