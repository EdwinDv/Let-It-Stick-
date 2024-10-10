package Let.It.Stick;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.util.ArrayList;

public class DictionaryWindow{
	
	private static final long serialVersionUID = 1L;
	
	private JScrollPane buttonWords;
	private JPanel words;
	
	private ArrayList<JButton> listOfWords = new ArrayList<JButton>();
		
	public DictionaryWindow() {
		JFrame frame = new JFrame("Let It Stick!");
        
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        
        JLayeredPane wordWindow = new JLayeredPane();
        
        words = new JPanel();
        words.setLayout(new BoxLayout(words, BoxLayout.Y_AXIS));
        
        JButton topButton = new JButton("Your added words will appear here!");
        words.add(topButton);
        
        JButton search = new JButton("Search for a word");
        search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				String word = JOptionPane.showInputDialog(null , "Word searcher", "Enter a word.");
				JButton button = new JButton(word);
				button.setBounds(0, 50 + (listOfWords.size() * 100), 200, 100);
				listOfWords.add(button);
				
				words.add(button);
				buttonWords.revalidate();
				buttonWords.repaint();
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("hi");
					}
				});
			}
        });
        
        
        buttonWords = new JScrollPane(words);
        
        JButton leftButton = new JButton("<");
        leftButton.setBounds(topButton.WIDTH, 384, 50, 100);
        wordWindow.add(leftButton, Integer.valueOf(2));
        
        JButton rightButton = new JButton(">");
        rightButton.setBounds(topButton.WIDTH + 920, 384, 50, 100);
        wordWindow.add(rightButton, Integer.valueOf(2));
        
        JButton mainWord = new JButton("fe");
        mainWord.setBounds(topButton.WIDTH, 0, topButton.WIDTH + 970, 768);
        wordWindow.add(mainWord, Integer.valueOf(1));

        content.add(search, BorderLayout.EAST);
        content.add(buttonWords, BorderLayout.WEST);
        content.add(wordWindow, BorderLayout.CENTER);
        
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1366, 768);
        frame.setVisible(true);
        
		//label.setText(dict.getDefinition(0, "adjective").get(0));
				
	}
	
	public static void main(String[] args) {
		//new DictionaryWindow();
		/*String word = "sad";
		Dictionary dict = new Dictionary(word);
			
		//System.out.println(dict.getPartsOfSpeech(0));
		System.out.println(dict.getDefinition(0,"adjective"));
		//System.out.println(dict.getSynonyms(0, 0, "adjective"));
		*/
		
		new DictionaryWindow();
		
	}
	
}
	
	
	
	

