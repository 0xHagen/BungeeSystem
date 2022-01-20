package de.hanimehagen.bungeesystem.punishment;

public class Punishment {
    private final String id;
    private final String uuid;
    private final String name;
    private final String operatorUuid;
    private final String operatorName;
    private final String reason;
    private final PunishmentType type;
    private final long startTime;
    private final long endTime;

    public Punishment(String id, String uuid, String name, String operatorUuid, String operatorName, String reason, PunishmentType type, long startTime, long endTime) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.operatorUuid = operatorUuid;
        this.operatorName = operatorName;
        this.reason = reason;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
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

    public String getReason() {
        return reason;
    }

    public PunishmentType getType() {
        return type;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
