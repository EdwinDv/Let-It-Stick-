package Let.It.Stick;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

public class Dictionary {
	
	private String data;
	private JsonReader jsonReader;
	
	public Dictionary(String word){
		data = call(word);
		jsonReader = Json.createReader(new StringReader(data));
	}
	
	public String call(String word){
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
	
	public JsonObject getDefinition(int definition, String partOfSpeech) throws JsonParsingException{
		JsonArray array = null;
		JsonObject mainObject = null;
		
		try {
			array = jsonReader.readArray();
		}
		
		catch (JsonParsingException e) {
			System.out.println("This word either doesn't exist, or isn't in the database!");
			jsonReader.close();
			System.exit(0);
		}
		
		JsonObject object = array.getJsonObject(definition);
		JsonArray secondArray = object.getJsonArray("meanings");
		
		for (int i = 0; i < secondArray.size(); i++) {
			if (secondArray.getJsonObject(i).getString("partOfSpeech").equalsIgnoreCase(partOfSpeech)) {
				mainObject = secondArray.getJsonObject(i);
			}
		}
		
		if (mainObject == null) {
			System.out.println("This part of speech doesn't exist for this word!");
			System.exit(0);
		}
		
		return mainObject;
				
	}
	
	public JsonArray getSynonyms(int definition, int type, String partOfSpeech) throws JsonParsingException{
		JsonArray array = null;
		JsonObject mainObject = null;
		
		try {
			array = jsonReader.readArray();
		}
		
		catch (JsonParsingException e) {
			System.out.println("This word either doesn't exist, or isn't in the database!");
			jsonReader.close();
			System.exit(0);
		}
		
		JsonObject object = array.getJsonObject(definition);
		JsonArray secondArray = object.getJsonArray("meanings");
		
		for (int i = 0; i < secondArray.size(); i++) {
			if (secondArray.getJsonObject(i).getString("partOfSpeech").equalsIgnoreCase(partOfSpeech)) {
				mainObject = secondArray.getJsonObject(i);
			}
		}
		
		if (mainObject == null) {
			System.out.println("The part of speech of this word doesn't have a synonym/antonym");
			System.exit(0);
		}
		
		JsonArray data = null;
		if (type == 0)
			data = mainObject.getJsonArray("synonyms");
		else
			data = mainObject.getJsonArray("antonyms");

		return data;
				
	}
	
	public ArrayList<String> getPartsOfSpeech(int definiton) {
		ArrayList<String> parts = new ArrayList<String>();
		
		JsonArray array = null;
		
		try {
			array = jsonReader.readArray();
		}
		
		catch (JsonParsingException e) {
			System.out.println("This word either doesn't exist, or isn't in the database!");
			System.exit(0);
		}
		
		JsonObject object = array.getJsonObject(definiton);
		JsonArray secondArray = object.getJsonArray("meanings");

		for (int i = 0; i < secondArray.size(); i++) {
			parts.add(secondArray.getJsonObject(i).getString("partOfSpeech"));
		}
		
		return parts;
	}
 

}
