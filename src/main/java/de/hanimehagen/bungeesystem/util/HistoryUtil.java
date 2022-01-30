package de.hanimehagen.bungeesystem.util;

import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.punishment.Punishment;

import java.sql.Timestamp;

public class HistoryUtil {
    public static String punishmentToHistoryString(Punishment punishment) {
        return punishment.getName() + "§" +
                punishment.getReason() + "§" +
                punishment.getOperatorName() + "§" +
                punishment.getOperatorUuid() + "§" +
                punishment.getType().toString() + "§" +
                Data.DATE_FORMAT.format(new Timestamp(punishment.getStartTime())) + "§" +
                DurationUtil.getEndDurationString(punishment.getEndTime());
    }
}
