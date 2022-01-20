package de.hanimehagen.bungeesystem.punishment;

public class Reason {

    private final int id;
    private final String name;
    private final long duration;

    public Reason(int id, String name, long duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getDuration() {
        return duration;
    }

    public Reason getbyId(int id) {
        if(id == this.id) {
            return this;
        }
        return null;
    }



}
