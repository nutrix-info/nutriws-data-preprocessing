package net.cokkee.nutrix.util;

import com.google.gson.Gson;

/**
 *
 * @author drupalex
 */
public class NutrixDataUtil {

    //--------------------------------------------------------------------------

    public static final String UUID_PATTERN =
            "^[a-fA-F0-9]{8}-?[a-fA-F0-9]{4}-?[a-fA-F0-9]{4}-?[a-fA-F0-9]{4}-?[a-fA-F0-9]{12}$";

    public static boolean verifyUUID(String uuid) {
        if (uuid == null) return false;
        return uuid.matches(UUID_PATTERN);
    }
    
    //--------------------------------------------------------------------------

    public static String convertObjectToJson(Object entity) {
        Gson gson = new Gson();
        return gson.toJson(entity);
    }

    public static <T> T convertJsonToObject(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static String convertObjectToString(Object value) {
        if (value == null) return null;
        return value.toString();
    }
}
