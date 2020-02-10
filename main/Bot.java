package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jibble.pircbot.PircBot;

import config.Config;

public class Bot extends PircBot {

	String chName = null;
	Reader r = new Reader();
	int i = 0;
	public static final String VERSION = "1.0B";
	public static String motd = "Welcome to $c's Rift! Hope you enjoy the stream $s.";
	Random rand = new Random();
	public static String[] splitt = null;
	public static List<String> names = new ArrayList<String>();
	private long starttime = 0, startmutetime = 0;
	private boolean mute = false;
	private int mutetime = 0;

	public Bot() {
		super.setName(Config.accName);
		System.out.printf("Account Name: %s\n", Config.accName);
		starttime = System.currentTimeMillis();
	}

	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {

		if(message.contains("!mute") && !this.mute && Config.enableMute) {
			if(sender.equalsIgnoreCase(chName)) {
				this.mute = true;
				splitt = message.split(" ");
				if(splitt.length > 1) {
					mutetime = Integer.valueOf(splitt[1])*1000;
					this.startmutetime = System.currentTimeMillis();
				}
			}
			else {
				this.sendMsg(sender, channel, "You are not $c $s!");
			}
			return;
		}
		if(message.equalsIgnoreCase("!unmute") && this.mute && Config.enableMute) {
			if(sender.equalsIgnoreCase(chName)) {
				this.mute = false;
			}
			return;
		}
		
		if(this.mute) {
			if(this.startmutetime != 0 && System.currentTimeMillis() >= this.startmutetime + mutetime) {
				this.mute = false;
			}
			else
				return;
		}
		
		if(!isInList(sender) && !this.mute && Config.enableMotd) {
			sendGreeting(sender,channel);
			names.add(sender);
			try {
				Thread.sleep(1000);
				this.onMessage(channel, sender, login, hostname, message);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(message.contains("!motd") || message.contains("!greeting") && Config.enableMotd) {//TODO motd speichern
			System.out.println("MOTD");
			if(sender.equalsIgnoreCase(chName)) {
				splitt = null;
				splitt = message.split(" ");
				for (int i = 1; i < splitt.length; i++) {
					if(i == 1) {
						motd = splitt[i];
					}
					else {
						motd = motd + " " + splitt[i];
					}
				}
				System.out.println(motd);
				this.sendMsg(sender, channel, "MOTD set!");
				Config.writeArgument(Config.MOTD, motd);
			}
			else {
				this.sendMsg(sender, channel, "You are not $c $s!");
			}
			return;
		}
		
		message = message.toLowerCase();
		if (message.equalsIgnoreCase("!test") && Config.enableTest) {
			this.sendMsg(sender, channel, "Yup. Works fine so far!");
		}
		else if (message.contains("!quote") && Config.enableQuote) {
			splitt = message.split(" ");
			if(splitt[1].equalsIgnoreCase("bard") || splitt[1].equalsIgnoreCase("rek'sai") || splitt[1].equalsIgnoreCase("reksai")) {
			
				this.sendMsg(sender, channel, (splitt[1].substring(0, 1).toUpperCase() + splitt[1].substring(1)) + " has no quotes.");
				return;
				
			}
			
			if (splitt[0].equals("!quote") && splitt.length == 2) {
				r.failure = false;
				message = r.getRandomLine(splitt[1]);
				splitt[1] = splitt[1].substring(0, 1).toUpperCase() + splitt[1].substring(1);
				if (!r.failure)
					this.sendMsg(sender, channel, message + " - " + splitt[1]);
				else
					this.sendMsg(sender, channel, "I'm sorry $s. It seems the champion " + splitt[1] +" doesn't exist or isn't in the database now");
			} 
			else if (splitt.length == 3 && (splitt[2].equalsIgnoreCase("number") || splitt[2].equalsIgnoreCase("count"))) {
				r.failure = false;
				i = r.getNumberOfQuotes(splitt[1]);
				splitt[1] = splitt[1].substring(0, 1).toUpperCase() + splitt[1].substring(1);
				if (!r.failure)
					super.sendMessage(channel, "The champion " + splitt[1] + " has " + i + " quotes"); //Leave it as it is. A relic from great times.
				else
					this.sendMsg(sender, channel, "I'm sorry $s. It seems the champion " + splitt[1] + " doesn't exist or isn't in the database now");
			} 
			else if(splitt.length == 3 && (splitt[2].equalsIgnoreCase("stats") || splitt[2].equalsIgnoreCase("statistics"))) {
				r.failure = false;
				message = r.getStats(splitt[1]);
				splitt[1] = splitt[1].substring(0, 1).toUpperCase() + splitt[1].substring(1);
				if(!r.failure)
					this.sendMsg(sender, channel, message);
				else
					this.sendMsg(sender, channel, "I'm sorry $s. It seems the champion " + splitt[1] + " doesn't exist or isn't in the database now");
			}
			else
				this.sendMsg(sender, channel, "You forgot to name me a champion $s.");
		} 
		else if (message.equalsIgnoreCase("!commands") && Config.enableCommands)
			this.sendMsg(sender, channel, "I respond to the commands: !test, !quote <champion> (number|count|stats|statistics), !version, !bug and !lore <champion> $s");
		else if (message.contains("!lore") && message.charAt(0) == '!' && Config.enableLore) {
			splitt = message.split(" ");
			if (splitt.length > 1) {
				
				splitt[1] = splitt[1].toLowerCase();
				splitt[1] = splitt[1].substring(0, 1).toUpperCase() + splitt[1].substring(1);
				this.sendMsg(sender, channel, "https://universe.leagueoflegends.com/en_US/story/champion/" + splitt[1]);
				
				//splitt[1] = splitt[1].toLowerCase();
				//super.sendMessage(channel, riot.getChampionInformation(splitt[1]));
			} 
			else {
				this.sendMsg(sender, channel, "You forgot to say me the name of the champion $s.");
			}
			splitt = null;
		} 
		else if (message.equalsIgnoreCase("!version") && Config.enableVersion) {
			this.sendMsg(sender, channel, "Quotes-Twitchbot by Pin Phreek v." + VERSION);
		}
		else if(message.equalsIgnoreCase("!secret") && Config.enableSecret) {
			switch(rand.nextInt(10)) {
			case 0:
				this.sendMsg(sender, channel, "That tasted too purple... (error message) - Pin Phreek");
				break;
			case 1:
				this.sendMsg(sender, channel, "You know that this is a secret $s. Right?");
				break;
			case 2:
				this.sendMsg(sender, channel, "Even the darkest dark has something bright. So I thought before I turned into the shadow monster man.");
				break;
			case 3:
				this.sendMsg(sender, channel, "!discord");
				break;
			case 4:
				this.sendMsg(sender, channel, "01000100 01101111 01101110 00100111 01110100 00100000 01100110 01100101 01100001 01110010 00100000 01110100 01101000 01100101 00100000 01100100 01100001 01110010 01101011 00101110 00100000 01001001 01110100 00100000 01100011 01100001 01101110 00100000 01100010 01100101 00100000 01111001 01101111 01110101 01110010 00100000 01100010 01100101 01110011 01110100 00100000 01100110 01110010 01101001 01100101 01101110 01100100 00101110");
				break;
			case 5:
				this.sendMsg(sender, channel, "Pssst. $s");
				break;
			case 6:
				this.sendMsg(sender, channel, "DARKNESS!");
				break;
			case 7:
				this.sendMsg(sender, channel, "It's now " + System.currentTimeMillis() + " ms from the 1.1.1970");
				break;
			case 8:
				this.sendMsg(sender, channel, "No this command doesn't exits. $s");
				break;
			case 9:
				this.sendMsg(sender, channel, "Ok google! *Google-Dot-Sounds* Clear history.");
				break;
			default:
				this.sendMsg(sender, channel, "Something went terribly, terribly wrong! Please write a message to @Pin_Phreek_yt that you used the secret command and got this message. For more information uns !bug $s.");
			}
		}
		else if(message.equalsIgnoreCase("!bug")) {
			this.sendMsg(sender, channel, "If you found a bug write @Pin_Phreek_yt a message or write an email to derneue49@gmail.com.");
		}
		else if(message.equalsIgnoreCase("!uwu") && Config.enableUwu) {
			this.sendMsg(sender, channel, "doskiiUwus doskiiUwus doskiiUwus doskiiUwus doskiiUwus doskiiUwus doskiiUwus");
		}
		else if(message.equalsIgnoreCase("!story") && Config.enableStory) {
			this.sendMsg(sender, channel, "Hey $c, tell $s a story.");
		}
		else if(message.equalsIgnoreCase("!uptime")) {
			long delta = System.currentTimeMillis() - starttime;
			delta /= 1000;
			this.sendMsg(sender, channel, "This bot is online since " + delta + " seconds");
		}
		else if(message.equalsIgnoreCase("!badass") && Config.enableBadass) {
			this.sendMsg(sender, channel, "Watch out everyone $c is in BAD-ASS-MODE!");
		}
		else if(message.contains("bed") && Config.enableBedResponse) {
			this.sendMsg(sender, channel, "Good night $s! Have some nice dreams.");
		}
		else if(message.equalsIgnoreCase("$s") || message.equalsIgnoreCase("$c")) {
			this.sendMsg(sender, channel, message);
		}
		else if(message.equalsIgnoreCase("!stop")) {
			if(this.chName.equalsIgnoreCase(sender)) {
				sendMsg(sender, channel, "Bot is going down. Thank you for using this bot.");
				try {
					Config.writeConfig();
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("CAN'T WRITE CONFIG-FILE!");
				}
				System.exit(0);
			}
			else
				sendMsg(sender, channel, "You are not $c $s");
		}
	}
	private void sendGreeting(String sender, String channel) {
		String send = motd.replace("$c", chName + "");
		System.out.println(send);
		String r_send = send.replace("$s", "@" + sender);
		super.sendMessage(channel, r_send);
	}
	
	private void sendMsg(String sender, String channel, String message) {
		String s1 = message.replace("$c", chName);
		String s2 = s1.replace("$s", "@" + sender);
		super.sendMessage(channel, s2);
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private void sendMsg(String sender, String channel, String message, Object extra) {
		String s1 = message.replace("$c", chName);
		String s2 = s1.replace("$s", "@" + sender);
		s1 = null;
		s1 = s2.replace("$i", String.valueOf(extra));
		super.sendMessage(channel, s2);
	}

	private boolean isInList(String name) {
		
		boolean found = false;
		
		for(String s : names) {
			if(s.equals(name)) {
				found = true;
			}
		}
		
		return found;
	}

}
