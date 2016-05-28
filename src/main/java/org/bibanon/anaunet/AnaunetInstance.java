package org.bibanon.anaunet;

import java.io.File;
import java.util.Date;
import java.util.Random;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class AnaunetInstance extends ListenerAdapter {

    static PircBotX bot;
    static ArchiveIsHtmlParser archiveis = new ArchiveIsHtmlParser();
    static GrabSiteInitializer grabSiteInit = new GrabSiteInitializer();
    static Random rand = new Random();
    static File cwd = new File(System.getProperty("user.dir", "./"));
    static String url;
    static URLValidator validator = new URLValidator();
    static Users users = new Users();

    @Override
    public void onMessage(MessageEvent event) {
        String[] message = event.getMessage().split(" ");
        //System.out.println(event.getUser().getNick() + "\n\n" + event.getMessage());
        if (!users.hasPermission(event.getUser().getNick(), message[0])) {
            return;
        }
        switch (message[0]) {
            case ".grab": {
                String igsets = "";
                if (message.length > 1) {
                    if (message.length > 2) {
                        igsets = message[1];
                        url = message[2];

                    } else {

                        url = message[1];
                    }

                    Process process = this.grabSiteInit.grabSite(url, igsets);
                    event.respond("Grab-Site started.");
                    url = null;
                    return;
                }
                event.respond("Usage: \".grab <igsets options> <url>\"");
                url = null;
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

    public void init(String[] args) throws Exception {
        users.tmpImit();
        //Configure what we want our bot to do
        Configuration configuration = new Configuration.Builder()
                .setName("Akabane")
                .addServer("irc.rizon.net")
                .addAutoJoinChannel("#bibanon-ab")
                .addListener(new AnaunetInstance())
                .buildConfiguration();

        //Create our bot with the configuration
        bot = new PircBotX(configuration);
        //Connect to the server
        bot.startBot();
    }
}
