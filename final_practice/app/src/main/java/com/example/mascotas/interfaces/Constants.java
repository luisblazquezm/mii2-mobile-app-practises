package com.example.mascotas.interfaces;

import android.widget.RemoteViews;

public interface Constants {
    // ####### NOTIFICATIONS
    String CONNECTION_ESTABLISHED_STATE = "CONNECTED";

    // ####### KEYS
    String PET_LIST_STATE_KEY = "pet_list_key";
    String SERVICE_STARTED_FLAG_STATE_KEY = "service_started_flag_state_key";

    // ####### URL
    String SENTINEL_JSON_PET_URL = "http://avellano.usal.es/~alsl/DAM/centinelaJSON.txt";
    String DEFAULT_JSON_PET_URL = "http://avellano.usal.es/~alsl/DAM/inicialJSON.txt";

    // ####### OTHERS
    String UNKNOWN_DEFAULT_VALUE = "unknown";

    // ####### ANIMALS VALUES
    String CAT_VALUE = "Gato";
    String BIRD_VALUE = "Pájaro";
    String DOG_VALUE = "Perro";
    String OTHER_VALUE = "Otro";
}
