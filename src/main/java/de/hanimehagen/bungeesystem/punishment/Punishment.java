package de.hanimehagen.bungeesystem.punishment;

public class Punishment {
    private final int id;
    private final String uuid;
    private final String name;
    private final String operatorUuid;
    private final String operatorName;
    private final Reason reason;

    public Punishment(int id, String uuid, String name, String operatorUuid, String operatorName, Reason reason) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.operatorUuid = operatorUuid;
        this.operatorName = operatorName;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getOperatorUuid() {
        return operatorUuid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public Reason getReason() {
        return reason;
    }

}
