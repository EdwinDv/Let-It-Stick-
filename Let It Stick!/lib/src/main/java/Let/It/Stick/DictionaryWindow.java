package Let.It.Stick;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader; 
import java.io.BufferedWriter; 

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DictionaryWindow{
	
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPane;
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
				JButton button = new JButton(word.toString());
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
				scrollPane.revalidate();
				scrollPane.repaint();
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
						
						try {
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
						catch (NullPointerException e1) {
							mainWord.setText("Please check your spelling!");
						}
					}
				});
			}
        });
        search.setBounds(930, 0, 200, 100);
        
        JButton removeWord = new JButton("Remove");
        removeWord.setBounds(930, 100, 200, 100);
        removeWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = JOptionPane.showInputDialog(null , "Word remover", "Enter a word.");
				for (int i = 0; i < listOfWords.size(); i++) {
					if (listOfWords.get(i).getText().equals(word)) {
						try {
							String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
							String removal = fileContent.replaceAll("\\b" + word + "\\b", "");
				            Files.write(Paths.get(filePath), removal.getBytes());

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						words.remove(listOfWords.get(i));
						listOfWords.remove(i);
						
					}
					
				}
				scrollPane.revalidate();
				scrollPane.repaint();

			}
        });
        
        JButton help = new JButton("Help");
        help.setBounds(930, 200, 200, 100);
        help.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JOptionPane.showConfirmDialog(null, "If you click on the left and right buttons, and nothing happens, either an error has occured or there aren't any more definitions.\nSwitch to another word and back for everything to work properly.\nOccassionally, a small blank box may appear between words.\nTo fix this, go into your home folder and delete any spaces between words (or go to remove and remove a blank).", "Common Errors", JOptionPane.PLAIN_MESSAGE);
        	}
        });
        
        JButton recommendedWord = new JButton("Recommended Word");
        recommendedWord.setBounds(940, 300, 200, 300);
        recommendedWord.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JButton firstWord = listOfWords.get(0);
        		JButton secondWord = listOfWords.get(1);
        		
        		//secondWord.doClick();
        		
        		listOfWords.remove(0);
        		listOfWords.add(firstWord);
        		
				words.remove(firstWord);
				words.add(firstWord);
				
				try {
					List<String> allLines =  Files.readAllLines(Paths.get(filePath));
					List<String> separateLines = allLines.stream().filter(line -> !line.contains(firstWord.getText())).collect(Collectors.toList());
		            Files.write(Paths.get(filePath), separateLines);

		            writer.write(firstWord.getText() + "\n");
		            writer.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				scrollPane.revalidate();
				scrollPane.repaint();
        		
        		
        	}
        });
               
        scrollPane = new JScrollPane(words);
        
        JButton leftButton = new JButton("<");
        
        leftButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (currentDefinition == 0) {
        			if (currentPartOfSpeech > 0) {
        				currentPartOfSpeech--;
        				listOfWords.get(currentWord).doClick();
        	        	mainWord.revalidate();
        				scrollPane.repaint();
        			}
        		}
        else {
        	currentDefinition--;
        	listOfWords.get(currentWord).doClick();
        	mainWord.revalidate();
			scrollPane.repaint();
        }
        	}
        });
    
        leftButton.setBounds(topButton.WIDTH, 384, 50, 100);
        
        JButton rightButton = new JButton(">");
        rightButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		currentDefinition++;
        		listOfWords.get(currentWord).doClick();
        		mainWord.revalidate();
				scrollPane.repaint();
        	}
        });
        rightButton.setBounds(topButton.WIDTH + 920, 384, 50, 100);
        
        mainWord = new JTextArea("Search for a word and click on it to get started...\n (The recommended word is the first word)");
        mainWord.setLineWrap(true);
        mainWord.setWrapStyleWord(true);
        mainWord.setEditable(false);
        mainWord.setFont(mainWord.getFont().deriveFont(25f));
        mainWord.setBounds(topButton.WIDTH, 0, topButton.WIDTH + 970, 768);
        
        wordWindow.add(mainWord, Integer.valueOf(1));
        wordWindow.add(leftButton, Integer.valueOf(2));
        wordWindow.add(rightButton, Integer.valueOf(2));
        wordWindow.add(search);
        wordWindow.add(help);
        wordWindow.add(removeWord);
        wordWindow.add(recommendedWord);

        content.add(scrollPane, BorderLayout.WEST);
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
						
						try{
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
						catch (NullPointerException u) {
							mainWord.setText("Please check for a spelling mistake!");
						}
					}
        		});
				scrollPane.revalidate();
				scrollPane.repaint();
        	}
        }
        
        frame.setVisible(true);        				
	}
	
	public static void main(String[] args) {
		new DictionaryWindow();
		
	}
	
}

