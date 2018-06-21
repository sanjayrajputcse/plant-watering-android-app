package com.moksha.plant_watering;

import java.util.HashMap;
import java.util.Map;

public class APIUtil {

    public static Map<String, String> getCommonHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put( "Content-Type", "application/json");
        headers.put( "APP_ID", "plant-watering-system");
        return headers;
    }
}
