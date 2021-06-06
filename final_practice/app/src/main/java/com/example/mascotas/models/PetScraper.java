package com.example.mascotas.models;

import android.util.Log;

import com.example.mascotas.interfaces.ScrapperConstants;
import com.example.mascotas.utils.Utils;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PetScraper {

    public ArrayList<Pet> getPetList(int minLimit, int maxLimit) throws InterruptedException {
        ArrayList<Pet> scrapedPets = new ArrayList<>();

        // Create thread to make the request in the background without stopping the program
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Send HTTP POST request to retrieve HTML content from URL
                String html = null;
                html = PetScraper.this.sendPostRequestToListPets(ScrapperConstants.PET_LIST_POST_FORM_URL,
                        ScrapperConstants.PET_FORM_DEFAULT_USER, minLimit, maxLimit);

                // Parse content with Jsoup
                Document doc = Jsoup.parse(html);
                Element link = doc.select("a").first(); // Get <a> tag
                String url = link.attr("href"); // Get 'href' attribute from <a> tag

                // Format and parse json content
                String jsonHtml = null;
                jsonHtml = PetScraper.this.sendGetRequestForJson(url);

                // Get JSON content from HTML
                doc = Jsoup.parse(jsonHtml);
                String json  = Jsoup.clean(doc.select("body").first().text(),
                        Whitelist.basic()).replace("&quot;","\"");

                // Get JSON
                Log.d("d", "resultJSON string: " + json);
                PetJSON[] resultJSON = Utils.fromJSONToPetJSON(json, PetJSON[].class);

                if (null != resultJSON) {
                    // Add people from list of url
                    for(int i = 0; i < resultJSON.length; i++) {
                        scrapedPets.add(resultJSON[i].parseToPet());
                    }
                }
            }
        });

        t.start();
        t.join();

        return scrapedPets;
    }

    public Pet addPet(Pet pet, String comment) throws InterruptedException {
        final Pet[] petAdded = {new Pet()};

        // Create thread to make the request in the background without stopping the program
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // make post to get HTML
                String html = PetScraper.this.sendPostRequestToAddPet(ScrapperConstants.PET_ADD_POST_FORM_URL,
                        ScrapperConstants.PET_FORM_DEFAULT_USER, pet, comment);

                // Parse content with Jsoup
                Document doc = Jsoup.parse(html);
                String docBody = doc.select("body").first().toString();
                String [] docBodyParts = docBody.split(" <br />");
                String csvLine = docBodyParts[docBodyParts.length-1].replace(" \n</body>","");
                String [] csvLineAttrs = csvLine.split(" ");

                Log.d("d", "ATRIBUTES: " + String.join(",", csvLineAttrs));

                // Get values
                String id = csvLineAttrs[0];
                String name = csvLineAttrs[1];
                String owner = csvLineAttrs[2];
                String dateString = csvLineAttrs[3];
                String type = csvLineAttrs[4];
                boolean isVaccinated = csvLineAttrs[5].equals("Si");

                // Parse date
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                petAdded[0] = new Pet(id, name, owner, type, date, isVaccinated);
            }
        });
        t.start();
        t.join();

        return petAdded[0];
    }

    private String sendPostRequest(String url, byte[] postData) {
        int postDataLength = postData.length;

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder sb = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postData);
            }

            // Send request
            connection.connect();

            // Read response to get HTML in UTF-8
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

            // Read content from response
            String line;
            sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (ProtocolException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String sendPostRequestToListPets(String url, String userName, int minLimit, int maxLimit) {

        // Prepare POST request data
        String uriParamsString = "usuario=%s&limit_inf=%s&limit_sup=%s&data_format=%s";
        String params = String.format(uriParamsString, userName, Integer.valueOf(minLimit).toString(),
                Integer.valueOf(maxLimit).toString(), ScrapperConstants.DEFAULT_FILE_FORMAT);

        // Parse to bytes
        byte[] postData = params.getBytes(StandardCharsets.UTF_8);

        // Establish connection
        return this.sendPostRequest(url, postData);
    }

    private String sendGetRequestForJson(String url) {
        HttpURLConnection connection = null;
        StringBuilder sb = null;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

            String line;
            sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String sendPostRequestToAddPet(String url, String user, Pet p, String comment) {

        // Format parameters in POST request data
        String uriParamString = "usuario=%s&nMascota=%s&nPropietario=%s&Fecha_adop=%s&Tipo_mascota=%s&comentario=%s";
        String params = String.format(uriParamString, user, p.getName(), p.getOwner(),
                new SimpleDateFormat("yyyy-MM-dd").format(p.getDate()), p.getType(), comment);
        if (p.isVaccinated()) {
            params += "&vacuna=Vacunado";
        }

        Log.d("d", "The uri param string sent to ADD is: " + params);

        // Parse to bytes
        byte[] postData = params.getBytes(StandardCharsets.UTF_8);

        // Establish connection
        return this.sendPostRequest(url, postData);
    }
}
