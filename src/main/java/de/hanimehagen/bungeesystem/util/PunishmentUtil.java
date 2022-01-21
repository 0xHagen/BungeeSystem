package de.hanimehagen.bungeesystem.util;

import de.hanimehagen.bungeesystem.mysql.PunishmentQuerys;

import java.util.Random;

public class PunishmentUtil {

    private static final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public static String generateId() {
        StringBuilder id = new StringBuilder();
        generateString(id);
        while (PunishmentQuerys.existsId(id.toString())) {
            generateString(id);
        }
        return id.toString();
    }

    private static void generateString(StringBuilder id) {
        for(int i = 0; i < 8; i++) {
            Random r = new Random();
            int low = 0;
            int high = 62;
            int result = r.nextInt(high-low) + low;
            id.append(chars[result]);
        }
    }

}
