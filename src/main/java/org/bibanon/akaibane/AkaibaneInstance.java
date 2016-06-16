package org.bibanon.akaibane;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Random;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class AkaibaneInstance extends ListenerAdapter {

    static PircBotX bot;
    static ArchiveIsHtmlParser archiveis = new ArchiveIsHtmlParser();
    static IAGrabSiteProcessManager iagrabsite = new IAGrabSiteProcessManager();

    private static Random rand = new Random();
    private static Users users = new Users();
    //    static URLValidator validator = new URLValidator();
    //util
    private static File cwd = new File(System.getProperty("user.dir", "./"));
    private static String url;
    private static String[] cmdutil;
    private static String igsets, meta = "";
    private static int i;

    @Override
    public void onMessage(MessageEvent event) {
        String[] message = event.getMessage().split(" ");

        if (!users.hasPermission(event.getUser().getNick(), message[0])) {
            return;
        }
        switch (message[0]) {
            case ".grab": {
                cmdutil = new String[message.length - 1];
                for (i = 1; i < message.length; i++) {
                    cmdutil[i - 1] = message[i];
                }
                grab(cmdutil, event);
                cmdutil = null;
                return;
            }
            case ".is": {
                String response = "No dice.";
                if (message.length > 1) {
                    archiveis.init();
                    response = archiveis.submitURL(message[1]);

                    event.respond(response);
                    url = null;
                    return;
                }
                event.respond("Usage: \".is <url to archive>\"");
                url = null;
                return;
            }
            case ".rr": {
                int r = rand.nextInt() % 6;
                if (r == 0) {
                    event.respond("*bang*");
                } else {
                    event.respond("*click*");
                }
                break;
            }
            case ".time": {
                event.respond("The current time is: " + new Date());
                break;
            }
            default: {
                return;
            }
        }
    }

    public void grab(String[] cmd, MessageEvent event) {
        System.out.println("Grab...");
        igsets = "";
        meta = "";
        if (cmd.length > 1) {
            if (cmd.length % 3 == 0 || cmd.length % 5 == 0) {
                for (i = 0; i < cmd.length; i++) {
                    switch (cmd[i]) {
                        case "set": {
                            i++;
                            igsets = cmd[i];
                            break;
                        }
                        case "meta": {
                            i++;
                            meta = cmd[i];
                            break;
                        }
                        case "help": {
                            event.respond("Usage: \".grab [<set> <igsetsoptions>] [<meta> <metadata[;tags[;separated[;...]]]>] <url>\"");
                            return;
                        }
                        default: {
                            break;
                        }
                    }
                }

            } else {
                event.respond("Usage: \".grab [<set> <igsetsoptions>] [<meta> <metadata[;tags[;separated[;...]]]>] <url>\"");
                return;
            }
        } else if (cmd[0] != null) {
            url = cmd[0];
        } else {
            event.respond("Usage: \".grab [<set> <igsetsoptions>] [<meta> <metadata[;tags[;separated[;...]]]>] <url>\"");
            url = "";
            return;
        }
        event.respond("Grab-Site started: PID: " + iagrabsite.addGrab(url, igsets, meta));
        igsets = "";
        meta = "";
        url = "";
    }

    public void init(String[] args) throws Exception {
        System.out.println("Starting...");
        users.tmpImit();
        iagrabsite = new IAGrabSiteProcessManager();
        iagrabsite.start();
        System.out.println("Loading Configuration...");
        //Configure what we want our bot to do
        Configuration configuration = new Configuration.Builder()
                .setName("Akabane_")
                .addServer("irc.rizon.net")
                .addAutoJoinChannel("#bibanon-ab")
                .addListener(new AkaibaneInstance())
                .buildConfiguration();

        //Create our bot with the configuration
        bot = new PircBotX(configuration);
        //Connect to the server
        bot.startBot();
    }
}
