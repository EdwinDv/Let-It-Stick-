package Let.It.Stick;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.util.ArrayList;

public class DictionaryWindow extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private JButton search;
	private ArrayList<JButton> listOfWords = new ArrayList<JButton>();
	//Dictionary dictionary;
	
	public DictionaryWindow() {
		
		search = new JButton("Search word");
		search.setBounds(1366 / 2, 768 / 2, 200, 100);
		search.addActionListener(new ActionListener() {
			public void  actionPerformed(ActionEvent e) {	
				String word = JOptionPane.showInputDialog(null , "Word searcher", "Enter a word.");
				JButton button = new JButton(word);
				button.setBounds(0, 50 + (listOfWords.size() * 100), 200, 100);
				listOfWords.add(button);
				
				
				//Dictionary dict = new Dictionary(word);

				//label.setText(dict.getDefinition(0, "adjective").get(0));
				DictionaryWindow.this.add(button);
			}
		});
		
		JLabel label = new JLabel("Words");
		label.setBounds(75, 10, 100, 50);
		
		JTextArea textArea = new JTextArea(5, 30);
		
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setPreferredSize(new Dimension(450, 110));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		this.add(search);
		this.add(label);
		this.add(scroll);
		this.setVisible(true);
		
		
	}
	
	public static void main(String[] args) {
		new DictionaryWindow();
		/*String word = "sad";
		Dictionary dict = new Dictionary(word);
			
		//System.out.println(dict.getPartsOfSpeech(0));
		System.out.println(dict.getDefinition(0,"adjective"));
		//System.out.println(dict.getSynonyms(0, 0, "adjective"));
		*/
	}
	
	
	
	
}
