package com.example.datospersonalistalmacen.utils;

public interface Constants {

    // Intents
    // ---------------- INTENT KEYS
    String INTENT_PERSON_OBJECT_KEY = "PERSON_OBJECT";
    String INTENT_ELEMENT_POSITION_TO_MODIFY_KEY = "PERSON_POSITION_ITEM_LISTVIEW";
    String INTENT_ELEMENT_DATA_TO_MODIFY_KEY = "PERSON_DATA_ITEM_LISTVIEW";
    String INTENT_REQUEST_CODE_KEY = "REQUEST_CODE";
    String INTENT_ELEMENT_NEW_PERSON_KEY = "NEW_PERSON_OBJECT";

    // ----------------- INTENT REQUEST CODES
    int LAUNCH_SECOND_ACTIVITY_TO_ADD = 1;
    int LAUNCH_SECOND_ACTIVITY_TO_MODIFY = 2;

    enum FILE_CODES {
        FAILURE,
        SUCCESS,
        INCORRECT_FILENAME,
        IO_FAILURE
    };

    // ----------------- FILENAMES IO
    String DEFAULT_OUTPUT_XML_FILENAME = "Datos_Persona_List_Almacen_XML_Data.xml";
    String DEFAULT_OUTPUT_JSON_FILENAME = "Datos_Persona_List_Almacen_XML_Data.json";

    // ----------------- SETTINGS
    String URL_SETTINGS_KEY = "URL";
    String FORMAT_SETTINGS_KEY = "FORMAT";
    String STORAGE_TYPE_SETTINGS_KEY = "STORAGE";
    String COMMUNICATION_TYPE_SETTINGS_KEY = "COMMUNICATION";
    String AGE_VISUALIZATION_SETTINGS_KEY = "AGE_VISUALIZATION";
    String DRIVING_LICENSE_VISUALIZATION_SETTINGS_KEY = "DRIVING_LICENSE_VISUALIZATION";
    String REGISTRY_DATE_VISUALIZATION_SETTINGS_KEY = "REGISTRY_DATE_VISUALIZATION";
    String PHONE_VISUALIZATION_SETTINGS_KEY = "PHONE_VISUALIZATION";
    String ENGLISH_LEVEL_VISUALIZATION_SETTINGS_KEY = "ENGLISH_LEVEL_VISUALIZATION";
    enum FORMAT_KEY_VALUES {
        XML,
        JSON
    };
    enum STORAGE_TYPE_KEY_VALUES {
        INTERNAL_MEMORY,
        CONTENT_PROVIDER
    };
    enum COMMUNICATION_KEY_VALUES {
        ASYNC_TASKS
    }

    // ----------------- DATABASE AND CONTENT PROVIDER
    String CP_DATABASE_NAME = "db";
    int CP_DATABASE_VERSION = 1;
    String CP_TABLE_NAME = "users";

    // ----------------- COLUMNS
    String ID_COLUMN = "id";
    String NAME_COLUMN = "name";
    String SURENNAMES_COLUMN = "surenames";
    String AGE_COLUMN = "age";
    String PHONE_COLUMN = "phone";
    String DRIVING_LICENSE_COLUMN = "driving_license";
    String ENGLISH_LEVEL_COLUMN = "english_level";
    String DATE_COLUMN = "date";

    // States
    String PEOPLE_LIST_STATE_KEY = "PEOPLE_LIST_STATE";
    String NAME_STATE_KEY = "NAME_STATE";
    String SURENAMES_STATE_KEY = "SURENAMES_STATE";
    String AGE_STATE_KEY = "AGE_STATE";
    String PHONE_STATE_KEY = "PHONE_STATE";
    String DRIVING_LICENSE_STATE_KEY = "DRIVING_LICENSE_STATE";
    String ENGLISH_LEVEL_STATE_KEY = "ENGLISH_LEVEL_STATE";
    String DATE_STATE_KEY = "DATE_STATE";

    // Deafult
    String UNKNOWN_DEFAULT_VALUE = "desconocido";

    // Other
    String SHARED_SETTINGS_KEY = "SHARED_SETTINGS";

    String AGENDA_URL_KEY = "agenda";
    String JSON_URL_NAME_PARAMETER = "nombre";
    String JSON_URL_SURENAMES_PARAMETER = "apellidos";
    String JSON_URL_PHONE_PARAMETER = "tfno";
    String JSON_URL_DRIVING_LICENSE_PARAMETER = "conduce";
    String JSON_URL_ENGLISH_LEVEL_PARAMETER = "ingles";
    String JSON_URL_REGISTRY_DATE_PARAMETER = "registro";

    String PERSONA_URL_KEY = "persona";
    String XML_URL_NAME_PARAMETER = "nombre";
    String XML_URL_SURENAMES_PARAMETER = "apellidos";
    String XML_URL_PHONE_PARAMETER = "telefono";
    String XML_URL_DRIVING_LICENSE_PARAMETER = "conduce";
    String XML_URL_ENGLISH_LEVEL_PARAMETER = "nivel_ingles";
    String XML_URL_REGISTRY_DATE_PARAMETER = "datereg";

    String HIGH_LEVEL_VALUE = "ALTO";
    String MEDIUM_LEVEL_VALUE = "MEDIO";
    String LOW_LEVEL_VALUE = "BAJO";

}




