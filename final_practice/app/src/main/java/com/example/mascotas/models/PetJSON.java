package com.example.mascotas.models;

import com.example.mascotas.interfaces.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PetJSON {

    String ID;
    String nMASCOTA;
    String nPROPIETARIO;
    String ADOPCION;
    String TIPO;
    String VACUNADO;

    public PetJSON(String ID, String nMASCOTA, String nPROPIETARIO, String ADOPCION,
                   String TIPO, String VACUNADO) {
        this.ID = ID;
        this.nMASCOTA = nMASCOTA;
        this.nPROPIETARIO = nPROPIETARIO;
        this.ADOPCION = ADOPCION;
        this.TIPO = TIPO;
        this.VACUNADO = VACUNADO;
    }

    private Date getDateFromString(){
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(this.ADOPCION);
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean getVaccinationFromJSON(){
        return this.VACUNADO.equals("Si");
    }

    private String getTypeFromJSON(){
        switch (this.TIPO){
            case "Gato":
                return Constants.CAT_VALUE;
            case "Pájaro":
                return Constants.BIRD_VALUE;
            case "Perro":
                return  Constants.DOG_VALUE;
            case "Otro":
                return  Constants.OTHER_VALUE;
            default:
                return Constants.OTHER_VALUE;
        }
    }

    public Pet parseToPet(){
        return new Pet(this.ID, this.nMASCOTA, this.nPROPIETARIO, this.getTypeFromJSON(), this.getDateFromString(), this.getVaccinationFromJSON());
    }
}
