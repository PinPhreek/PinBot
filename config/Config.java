package config;

import java.io.*;
import java.util.*;

import main.Bot;


public class Config {

	public static String os;
	
	/*TODO Für Windows anpassen*/
	private static File f; //= new File("config/config.txt");
	private static FileWriter w;
	private static Scanner sc;
	public  static String msg = null;
	private static List<String> buf = new ArrayList<String>();
	
	public static boolean enableUwu      = true;
	public static boolean enableMute     = true;
	public static boolean enableMotd     = true;
	public static boolean enableSecret   = true;
	public static boolean enableVersion  = true;
	public static boolean enableCommands = true;
	public static boolean enableQuote    = true;
	public static boolean enableTest     = true;
	public static boolean enableLore     = true;
	public static boolean enableStory    = true;
	public static boolean enableBadass   = true;
	
	public static boolean enableBedResponse = true;
	
	public static String accName = null;
	public static String oauth = null;
	public static String channel = null;
	
	@SuppressWarnings("unlikely-arg-type")
	public Config() throws IOException {
		
		os = System.getProperties().getProperty("os.name");
		System.out.println(os);
		if(os.contains("Windows")) {//we don't support mac here
			f = new File("config\\config.txt");
		}
		else if(os.equalsIgnoreCase("Linux")) {//we also don't support mac here
			f = new File("config/config.txt");
		}
		else {
			f = new File("config.txt");
		}
		
		if(!f.exists()) {
			try {
				f.createNewFile();
				msg = "Created your config-file $c.";
				w = new FileWriter(f);
				w.write("MOTD=Welcome to $c's Rift! Hope you enjoy the stream $s.\r\n");
				w.write("ALLOW_MOTD=yes\r\n");
				w.write("ALLOW_SECRET=yes\r\n");
				w.write("ALLOW_VERSION=yes\r\n");
				w.write("ALLOW_COMMANDS=yes\r\n");
				w.write("ALLOW_QUOTE=yes\r\n");
				w.write("ALLOW_TEST=yes\r\n");
				w.write("ALLOW_LORE=yes\r\n");
				w.write("ALLOW_STORY=yes\r\n");
				w.write("ALLOW_BADASS=yes\r\n");
				w.write("ALLOW_BED_RESPONSE=yes\r\n");
				w.write("ACCOUNT=\r\n");
				w.write("OAUTH=\r\n");
				w.write("CHANNEL=\r\n");
				w.flush();
				w.close();
				System.out.println("PLEASE COMPLETE YOUR CONFIG-FILE!");
				System.exit(0);
			} catch (IOException e) {e.printStackTrace();}
		}
		else {
			sc = new Scanner(f);
			while(sc.hasNextLine()) {
				buf.add(noDouplicat(sc.nextLine()));
			}
			sc.close();
			String[] splitt;
			List<Integer> badLines = new ArrayList<Integer>();
			for(int i = 0; i < buf.size(); i++) {
				if(buf.get(i).isEmpty() || buf.get(i) == null) {
					badLines.add(i);
					continue;
				}	
				else if(buf.get(i).startsWith("MOTD")) {
					
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						Bot.motd = splitt[1]; 
					else {
						System.out.println("REALLY? YOU KNOW HOW THIS WORKS, RIGHT?");
					}
				}
				else if(buf.get(i).startsWith("ALLOW_MOTD")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						enableMotd = splitt[1].equalsIgnoreCase("0") || splitt[1].equalsIgnoreCase("no") ? false : true;
				}
				else if(buf.get(i).startsWith("ALLOW_SECRET")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						enableSecret = splitt[1].equalsIgnoreCase("0") || splitt[1].equalsIgnoreCase("no") ? false : true;
				}
				else if(buf.get(i).startsWith("ALLOW_VERSION")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						enableVersion = splitt[1].equalsIgnoreCase("0") || splitt[1].equalsIgnoreCase("no") ? false : true;
				}
				else if(buf.get(i).startsWith("ALLOW_COMMANDS")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						enableCommands = splitt[1].equalsIgnoreCase("0") || splitt[1].equalsIgnoreCase("no") ? false : true;
				}
				else if(buf.get(i).startsWith("ALLOW_QUOTE")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						enableQuote = splitt[1].equalsIgnoreCase("0") || splitt[1].equalsIgnoreCase("no") ? false : true;
				}
				else if(buf.get(i).startsWith("ALLOW_TEST")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						enableTest = splitt[1].equalsIgnoreCase("0") || splitt[1].equalsIgnoreCase("no") ? false : true;
				}
				else if(buf.get(i).startsWith("ALLOW_LORE")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						enableLore = splitt[1].equalsIgnoreCase("0") || splitt[1].equalsIgnoreCase("no") ? false : true;
				}
				else if(buf.get(i).startsWith("ALLOW_STORY")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						enableStory = splitt[1].equalsIgnoreCase("0") || splitt[1].equalsIgnoreCase("no") ? false : true;
				}
				else if(buf.get(i).startsWith("ALLOW_BADASS")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						enableBadass = splitt[1].equalsIgnoreCase("0") || splitt[1].equalsIgnoreCase("no") ? false : true;
				}
				else if(buf.get(i).startsWith("ALLOW_BED_RESPONSE")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						enableBedResponse = splitt[1].equalsIgnoreCase("0") || splitt[1].equalsIgnoreCase("no") ? false : true;
				}
				
				else if(buf.get(i).startsWith("ACCOUNT")) {//buggy
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						accName = splitt[1];
					else {
						System.err.println("Malformated config-file!");
						System.exit(-1);
					}
				}
				else if(buf.get(i).startsWith("OAUTH")) {
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						oauth = splitt[1];
					else {
						System.err.println("Malformated config-file!");
						System.exit(-1);
					}
				}
				else if(buf.get(i).startsWith("CHANNEL")) {
					splitt = buf.get(i).split("=");
					if(splitt.length == 2)
						channel = splitt[1];
					else {
						System.err.println("Malformated config-file!");
						System.exit(-1);
					}
				}
			}
			if(channel == null || accName == null || oauth == null) {
				
				System.err.println("Mal formated config-file!");
				System.exit(-2);
				
			}
			
			//remove unnecessary lines
			for(int i = badLines.size(); i > -1;i--) {
				buf.remove(badLines.get(i)); //I think I removed a bug (F to my config-files)
			}
		}
	}
	
	public static void writeArgument(String name, String value) {
		boolean found = false;
		for(int i = 0; i < buf.size(); i++) {
			if(buf.get(i).startsWith(name)) {
				buf.set(i, name + "=" + value);
			}
		}
		if(!found) {
			buf.add(name + "=" + value);
		}
	}
	
	public static void writeConfig() throws IOException{
		
		if(os.contains("Windows")) {//we don't support mac here
			f = new File("config\\config.txt");
		}
		else if(os.equalsIgnoreCase("Linux")) {//we also don't support mac here
			f = new File("config/config.txt");
		}
		else {//have fun using this mac users
			f = new File("config.txt");
		}
		w = new FileWriter(f);
		for(int i = 0; i < buf.size(); i++) {
			w.write(buf.get(i) + "\r\n");
			System.out.println(buf.get(i));
		}
		w.flush();
		w.close();
		
		System.out.println("Config saved!");
	
	}
	public static String noDouplicat(String text) {
		
		for(String s : buf) {
			if(s.equals(text))
				return null;
		}
		return text;
	}
	
	//KEYWORDS IN CONFIG-FILE
	public static final String MOTD = "MOTD";
	public static final String UWU = "ALLOW_UWU";
	public static final String MUTE = "ALLOW_MUTE";
	public static final String A_MOTD = "ALLOW_MOTD";
	public static final String SECRET = "ALLOW_SECRET";
	public static final String VERSION = "ALLOW_VERSION";
	public static final String COMMANDS = "ALLOW_COMMANDS";
	public static final String QUOTE = "ALLOW_QUOTE";
	public static final String TEST = "ALLOW_TEST";
	public static final String LORE = "ALLOW_LORE";
	public static final String STORY = "ALLOW_STORY";
	public static final String BADASS = "ALLOW_BADASS";
	
}
