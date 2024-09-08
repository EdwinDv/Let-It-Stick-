package Let.It.Stick;


public class DictionaryWindow {
	
	public static void main(String args[]){
		String word = "sad";
		Dictionary dict = new Dictionary(word);
		
		System.out.println(dict.getDefinition(0,"adjective"));
		
	}

}
