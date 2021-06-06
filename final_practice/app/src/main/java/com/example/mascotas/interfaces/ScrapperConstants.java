package com.example.mascotas.interfaces;

public interface ScrapperConstants {

    // ############ URL
    String PET_LIST_POST_FORM_URL = "http://avellano.usal.es/alsl/listaMascotas.jsp";
    String PET_ADD_POST_FORM_URL = "http://avellano.usal.es/alsl/subeUnaMascota.jsp";

    // ############ KEYS
    String PET_SCRAPED_LIST_STATE_KEY = "pet_scraped_list_state_key";
    String NAME_STATE_KEY = "NAME_STATE_KEY";
    String OWNER_STATE_KEY = "OWNER_STATE_KEY";
    String TYPE_STATE_KEY = "TYPE_STATE_KEY";
    String DATE_STATE_KEY = "DATE_STATE_KEY";
    String COMMENT_STATE_KEY = "COMMENT_STATE_KEY";
    String IS_VACCINATED_KEY = "IS_VACCINATED_KEY";

    // ############ OTHERS
    String PET_FORM_DEFAULT_USER = "pepe";
    String DEFAULT_FILE_FORMAT = "JSON";

}
