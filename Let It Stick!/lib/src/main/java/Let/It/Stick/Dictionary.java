package Let.It.Stick;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import java.util.ArrayList;


public class Dictionary {
	
	private String words;
	private JsonParser parser;
	private ArrayList<String> data;
	private JsonReader jsonReader;
	
	public Dictionary(String word) {
		words = call(word);
		//parser = Json.createParser(new StringReader(words));
		jsonReader = Json.createReader(new StringReader(words));
		
	}
	
	public JsonArray details() {
		JsonArray ray = jsonReader.readArray();
		
		JsonObject object = ray.getJsonObject(0);
		
		JsonArray r = object.getJsonArray("meanings");
		
		return r;
		
		//return object.getJsonArray(0);
		
	}
	
	public String call(String word) {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.dictionaryapi.dev/api/v2/entries/en/" + word)).method("GET", HttpRequest.BodyPublishers.noBody()).build();
		
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return response.body().toString();
	}

	public ArrayList<String> getData(String word, int position) {

		ArrayList<String> definition = new ArrayList<String>();
		
		Event event = parser.next(); 
		
		// It seems like call(word).length isn't giving me the actual length. I have to loop to a reasonable number
		for (int i = 0; i < 5000; i++) {
			if (parser.hasNext()) {
				event = parser.next();
				if (event == Event.KEY_NAME && parser.getString().equals("partOfSpeech")) {
					event = parser.next();
					definition.add(parser.getString());
					for (int u = 0; u < position; u++) {
						event = parser.next();	
					}
					if (event == Event.VALUE_STRING)
					definition.add(parser.getString());
					else
						definition.add("It seems as if " + word + " doesn't have that attribute!");
				}
			}
			else {
				break;
			}
		}
		parser.close();
		return definition;
	}
	
	
	
	
	 

}
