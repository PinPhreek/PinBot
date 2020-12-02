package main;

import config.Config;

public class Main {

	public static void main(String[] args) throws Exception{
		new Config();
		Bot bot = new Bot();
		bot.setVerbose(true);
		bot.connect("irc.twitch.tv", 6667, Config.oauth);
		bot.chName = Config.channel;
		bot.joinChannel("#" + Config.channel);
		bot.sendMessage("#" + bot.chName, "Hello Chat!");
		System.out.println("################################################################");
	}

}
