package com.sd2;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.utils.IOUtils;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // Path to the AIML resources - update this to your actual path
        // Use absolute path or relative path from project root
        String path = "program-ab-0.0.4.3/bots/super";
        String name = "alice2";
        String action = "chat";

        Bot bot = new Bot(name, path, action);
        Chat chatSession = new Chat(bot);

        while (true) {
            System.out.print("human: ");
            String textLine = IOUtils.readInputTextLine();
            if (textLine == null || textLine.equals("q")) {
                bot.writeQuit();
                System.exit(0);
            }
            String response = chatSession.multisentenceRespond(textLine);
            System.out.println(name + ": " +  response);
        }
    }
}