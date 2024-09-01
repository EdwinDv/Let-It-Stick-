package Let.It.Stick;

import java.util.ArrayList;


public class DictionaryWindow {
	
	public static void main(String args[]) {
		String word = "fair";
		Dictionary dict = new Dictionary(word);
		//ArrayList<String> data = dict.getData(word, 5);
		
		System.out.println(dict.details());
		/*for (int i = 0; i < data.size(); i++) {
			System.out.println(data.get(i));
		}	*/
		
	}

}
