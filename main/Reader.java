package main;

import java.io.*;
import java.util.*;

import config.Config;

public class Reader {

	Scanner sc;
	private static ArrayList<String> quotes = new ArrayList<String>();
	public boolean failure = false;

	public Reader() {}

	public String getRandomLine(String champion) {

		Random r = new Random();
		this.readContents(champion);

		return quotes.get(r.nextInt(quotes.size()));

	}

	public int getNumberOfQuotes(String champion) {
		this.readContents(champion);
		return quotes.size();
	}

	private void readContents(String champion) {
		File f;
		String prefix = null;
		if(Config.os.equalsIgnoreCase("Linux")) {
			prefix = "quotes/";
		}
		else if(Config.os.contains("Windows")) {
			prefix = "quotes\\";
		}
		if (quotes.size() > 0)
			quotes.clear();
		f = new File(prefix + champion + ".txt");
		String s = null;
		try {
			sc = new Scanner(f);
			while (sc.hasNextLine()) {
				s = sc.nextLine();
				if (s.length() > 0) {
					if (s.charAt(0) == '"')
						quotes.add(s);
				}
				s = null;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			quotes.add("This tasted too purple... sry");
			failure = true;
		}

	}
	public String getStats(String champion) {
		
		quotes.clear();
		
		File f; 
		if(Config.os.contains("Windows")){
			f = new File("quotes\\" + champion + ".txt");
		}
		else if(Config.os.equalsIgnoreCase("Linux")) {
			f = new File("quotes/" + champion + ".txt");
		}
		else {
			f = new File(champion + ".txt");
		}
		try {
			sc = new Scanner(f);
			String longest = null, shortest = null,s = null;
			double length = 0;
			
			while (sc.hasNextLine()) {
				s = sc.nextLine();
				if (s.length() > 0) {
					if (s.charAt(0) == '"')
						quotes.add(s);
				}
				s = null;
			}
			sc.close();
			if(quotes.isEmpty()) {
				return "That tasted too purple... ";
			}
			shortest = quotes.get(0);
			longest = quotes.get(0);
			int i = 1;
			while(i < quotes.size()) {
				if(quotes.get(i).length() < shortest.length()) {
					shortest = quotes.get(i);
				}
				else if(quotes.get(i).length() > longest.length()) {
					longest = quotes.get(i);
				}
				length += quotes.get(i).length();
				i++;
			}
			champion = champion.substring(0, 1).toUpperCase() + champion.substring(1);
			
			return "The longest quote from the champion " + champion + " is: " + longest + " - " + champion 
					+ ". The shortest is: " + shortest + " - " + champion + 
					". The champion " + champion + " has " + quotes.size() + " quotes, which are normally " + (int)(length/quotes.size()) + " characters long";
		} catch (FileNotFoundException e) {
			System.out.println("That tasted too purple... GET_STATS");
			e.printStackTrace();
		}
		return null;
	}
}
