package com.example.datospersonalistalmacen.constants;

public interface IOConstants {

    // ----------------- FILENAMES IO
    String DEFAULT_OUTPUT_XML_FILENAME = "Datos_Persona_List_Almacen_XML_Data.xml";
    String DEFAULT_OUTPUT_JSON_FILENAME = "Datos_Persona_List_Almacen_XML_Data.json";

    // JSON KEYS
    String AGENDA_URL_KEY = "agenda";
    String JSON_URL_NAME_PARAMETER = "nombre";
    String JSON_URL_SURENAMES_PARAMETER = "apellidos";
    String JSON_URL_PHONE_PARAMETER = "tfno";
    String JSON_URL_DRIVING_LICENSE_PARAMETER = "conduce";
    String JSON_URL_ENGLISH_LEVEL_PARAMETER = "ingles";
    String JSON_URL_REGISTRY_DATE_PARAMETER = "registro";

    // XML KEYS
    String PERSONA_URL_KEY = "persona";
    String XML_URL_NAME_PARAMETER = "nombre";
    String XML_URL_SURENAMES_PARAMETER = "apellidos";
    String XML_URL_PHONE_PARAMETER = "telefono";
    String XML_URL_DRIVING_LICENSE_PARAMETER = "conduce";
    String XML_URL_ENGLISH_LEVEL_PARAMETER = "nivel_ingles";
    String XML_URL_REGISTRY_DATE_PARAMETER = "datereg";

    // FILE RESULT CODES
    enum FILE_CODES {
        FAILURE,
        SUCCESS,
        INCORRECT_FILENAME,
        IO_FAILURE
    };

}




