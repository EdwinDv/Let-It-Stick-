package Let.It.Stick;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import java.util.ArrayList;

public class Dictionary {
	
	public static String call(String word) {
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
	

	public static ArrayList<String> getDefinition(String word, String partOfSpeech) {
		
		String response = call(word);
		ArrayList<String> def = new ArrayList<String>();
		
		JsonParser parser = Json.createParser(new StringReader(response));
		
		Event event = parser.next(); 
		
		// It seems like call(word).length isn't giving me the actual length. I have to loop to a reasonable number
		for (int i = 0; i < 5000; i++) {
			if (parser.hasNext()) {
				event = parser.next();
				if (event == Event.KEY_NAME && parser.getString().equals("partOfSpeech")) {
					event = parser.next();
					event = parser.next();
					event = parser.next();
					event = parser.next();
					event = parser.next();
					event = parser.next();
					def.add(parser.getString());
			}
		}
			else {
				break;
			}
		
		
		}
	return def;
	}
	public static void main(String args[]) {
		ArrayList<String> min = getDefinition("peer", "noun");
		
		for (int i = 0; i < min.size(); i++) {
			System.out.println(min.get(i));
		}
		
		//System.out.println(call("peer").length());
	}
		

}
