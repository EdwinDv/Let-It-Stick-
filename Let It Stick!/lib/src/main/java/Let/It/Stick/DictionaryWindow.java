package Let.It.Stick;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader; 
import java.io.BufferedWriter; 



import javax.swing.*;

import java.util.ArrayList;

public class DictionaryWindow{
	
	private static final long serialVersionUID = 1L;
	
	private JScrollPane buttonWords;
	private JPanel words;
	private JTextArea mainWord;
	
	private int currentWord = 0;
	private int def = 0;
	private int currentDefinition = 0;
	private int currentPartOfSpeech = 0;
	
	private ArrayList<JButton> listOfWords = new ArrayList<JButton>();
	
	private BufferedWriter writer;
		
	public DictionaryWindow() {
		String home = System.getProperty("user.home");
		String fileName = "words.txt";
		String filePath = home + File.separator + fileName;
		
		File data = new File(filePath);
		
		if (data.exists()) {
		    try (BufferedReader reader = new BufferedReader(new FileReader(data))) {
		        String line;
		        while ((line = reader.readLine()) != null) {
		            listOfWords.add(new JButton(line));
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		else {
			try {
				data.createNewFile();
			}catch (IOException e) {
			    e.printStackTrace();
			}
		}
		
		try {
			writer = new BufferedWriter(new FileWriter(data, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
				try {
					writer.write(word + "\n");
					writer.flush();
					} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				words.add(button);
				buttonWords.revalidate();
				buttonWords.repaint();
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Dictionary dict = new Dictionary(word);
						if (currentWord != listOfWords.indexOf(button)) {
							def = 0;
							currentDefinition = 0;
							currentPartOfSpeech = 0;
						}
						
						currentWord = listOfWords.indexOf(button);
						
						String sLoop = "";
						String aLoop = "";
						
						ArrayList<String> partsOfSpeech = dict.getPartsOfSpeech(def);
						ArrayList<String> definition = dict.getDefinition(def, partsOfSpeech.get(currentPartOfSpeech));
						
						if (currentDefinition > definition.size() - 1) {
							currentDefinition = 0;
							currentPartOfSpeech++;
							if (currentPartOfSpeech > partsOfSpeech.size() - 1) {
								def++;
								currentPartOfSpeech = 0;
								partsOfSpeech = dict.getPartsOfSpeech(def);
							}
							definition = dict.getDefinition(def, partsOfSpeech.get(currentPartOfSpeech));
							
						}
						
						ArrayList<String> synonyms = dict.getSynonyms(def, 0, partsOfSpeech.get(currentPartOfSpeech));
						ArrayList<String> antonyms = dict.getSynonyms(def, 1, partsOfSpeech.get(currentPartOfSpeech));
						
						for (int i = 0; i < synonyms.size(); i++) {
							sLoop += synonyms.get(i) + ", ";
						}
						
						for (int i = 0; i < antonyms.size(); i++) {
							aLoop += antonyms.get(i) + ", ";
						}
						
						if (sLoop.equals(""))
							sLoop = "Unfourtunately, this word doesn't have a synonym in the database.";
						
						if (aLoop.equals(""))
							aLoop = "Unfourtunately, this word doesn't have an antonym in the database.";
						
						mainWord.setText("\t" + "\t" + "        " + word + "\n" + "\n" + partsOfSpeech.get(currentPartOfSpeech) + ": " + definition.get(currentDefinition) + "\n" + "Synonyms: " + sLoop + "\n" + "Antonyms:" + aLoop);
					}
				});
			}
        });
               
        buttonWords = new JScrollPane(words);
        
        JButton leftButton = new JButton("<");
        
        leftButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (currentDefinition == 0) {
        			if (currentPartOfSpeech > 0) {
        				currentPartOfSpeech--;
        				listOfWords.get(currentWord).doClick();
        	        	mainWord.revalidate();
        				buttonWords.repaint();
        			}
        		}
        else {
        	currentDefinition--;
        	listOfWords.get(currentWord).doClick();
        	mainWord.revalidate();
			buttonWords.repaint();
        }
        	}
        });
    
        leftButton.setBounds(topButton.WIDTH, 384, 50, 100);
        wordWindow.add(leftButton, Integer.valueOf(2));
        
        JButton rightButton = new JButton(">");
        rightButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		currentDefinition++;
        		listOfWords.get(currentWord).doClick();
        		mainWord.revalidate();
				buttonWords.repaint();
        	}
        });
        rightButton.setBounds(topButton.WIDTH + 920, 384, 50, 100);
        wordWindow.add(rightButton, Integer.valueOf(2));
        
        mainWord = new JTextArea("Search for a word and click on it to get started...");
        mainWord.setLineWrap(true);
        mainWord.setWrapStyleWord(true);
        mainWord.setEditable(false);
        mainWord.setFont(mainWord.getFont().deriveFont(25f));
        mainWord.setBounds(topButton.WIDTH, 0, topButton.WIDTH + 970, 768);
        
        wordWindow.add(mainWord, Integer.valueOf(1));

        content.add(search, BorderLayout.EAST);
        content.add(buttonWords, BorderLayout.WEST);
        content.add(wordWindow, BorderLayout.CENTER);
        
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1366, 768);
        
        if (!listOfWords.isEmpty()) {
        	for (int i = 0; i < listOfWords.size(); i++) {
        		JButton thisButton = listOfWords.get(i);
        		words.add(thisButton);
        		thisButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Dictionary dict = new Dictionary(thisButton.getText());
						if (currentWord != listOfWords.indexOf(thisButton)) {
							def = 0;
							currentDefinition = 0;
							currentPartOfSpeech = 0;
						}
						
						currentWord = listOfWords.indexOf(thisButton);
						
						String sLoop = "";
						String aLoop = "";
						
						ArrayList<String> partsOfSpeech = dict.getPartsOfSpeech(def);
						ArrayList<String> definition = dict.getDefinition(def, partsOfSpeech.get(currentPartOfSpeech));
						
						if (currentDefinition > definition.size() - 1) {
							currentDefinition = 0;
							currentPartOfSpeech++;
							if (currentPartOfSpeech > partsOfSpeech.size() - 1) {
								def++;
								currentPartOfSpeech = 0;
								partsOfSpeech = dict.getPartsOfSpeech(def);
							}
							definition = dict.getDefinition(def, partsOfSpeech.get(currentPartOfSpeech));
							
						}
						
						ArrayList<String> synonyms = dict.getSynonyms(def, 0, partsOfSpeech.get(currentPartOfSpeech));
						ArrayList<String> antonyms = dict.getSynonyms(def, 1, partsOfSpeech.get(currentPartOfSpeech));
						
						for (int i = 0; i < synonyms.size(); i++) {
							sLoop += synonyms.get(i) + ", ";
						}
						
						for (int i = 0; i < antonyms.size(); i++) {
							aLoop += antonyms.get(i) + ", ";
						}
						
						if (sLoop.equals(""))
							sLoop = "Unfourtunately, this word doesn't have a synonym in the database.";
						
						if (aLoop.equals(""))
							aLoop = "Unfourtunately, this word doesn't have an antonym in the database.";
						
						mainWord.setText("\t" + "\t" + "        " + thisButton.getText() + "\n" + "\n" + partsOfSpeech.get(currentPartOfSpeech) + ": " + definition.get(currentDefinition) + "\n" + "Synonyms: " + sLoop + "\n" + "Antonyms:" + aLoop);
					}
        		});
				buttonWords.revalidate();
				buttonWords.repaint();
        	}
        }
        
        frame.setVisible(true);        				
	}
	
	public static void main(String[] args) {
		new DictionaryWindow();
		
	}
	
}

