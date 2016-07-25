package draft;

import dataManager.support.Pair;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by George on 19.07.2016.
 */
public class MicrosoftSpellCheckAPI {
    public static void main(String[] args) {
        String subscriptionKey = "b6603c026f0445fbace4d14b54bda184";
        String requestBody = "Text=it is god to sea you again";
        HttpClient httpClient = HttpClients.createDefault();
        HashMap<String, Pair<String, Integer>> proposedCorrections = new HashMap<>();
        try {
            URIBuilder builder = new URIBuilder("https://api.cognitive.microsoft.com/bing/v5.0/spellcheck/");
            builder.setParameter("mode", "spell");
            URI uri = builder.build();
            HttpPost postRequest = new HttpPost(uri);
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            postRequest.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
            StringEntity requestEntity = new StringEntity(requestBody);
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);
            HttpEntity entity = response.getEntity();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(EntityUtils.toString(entity));
            JSONArray misspelledTokens = (JSONArray) jsonObject.get("flaggedTokens");
            Iterator detectedMistakes = misspelledTokens.iterator();
            while (detectedMistakes.hasNext()){
                JSONObject innerObject = (JSONObject) detectedMistakes.next();
                JSONArray correctionsCandidates = (JSONArray) innerObject.get("suggestions");
                Iterator suggestions = correctionsCandidates.iterator();
                JSONObject deepObject = (JSONObject) suggestions.next();
                String mostLikelyCorrection = deepObject.get("suggestion").toString();
                proposedCorrections.put(innerObject.get("token").toString(),new Pair<>(mostLikelyCorrection,
                        Integer.valueOf(innerObject.get("offset").toString())));
            }
            Set<Entry<String, Pair<String,Integer>>> allCorrections = proposedCorrections.entrySet();
            allCorrections.forEach(e -> {
                System.out.println("for token : \"" + e.getKey() +"\" correction is \" : \""
                + e.getValue().getMainValue() + "\"");
            });
            //if (entity != null) {
            //    System.out.println(EntityUtils.toString(entity));
            //}
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
}
