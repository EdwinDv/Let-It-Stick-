package Let.It.Stick;

import java.util.ArrayList;

public class DictionaryWindow {
	
	public static void main(String args[]) {
		Dictionary dict = new Dictionary("pretty");
		ArrayList<String> data = dict.getData("pretty", 5);
		
		for (int i = 0; i < data.size(); i++) {
			System.out.println(data.get(i));
		}
		
	}

}
