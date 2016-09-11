package net.refy.mc.mods.twitter;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yutaka on 9/11/2016.
 */
public class CommandTwitter extends CommandBase {
    TwitterAdapter adapter = new TwitterAdapter(){
        @Override
        public void onException(TwitterException te, TwitterMethod method) {
            ChatUtil.send(te);
        }
    };

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "twitter";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "tweet a text to twitter";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("t");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        AccessToken token = ModMCTwitter.instance.getAccessToken();
        if(token == null){
            ChatUtil.send("error: did not authenticated yet", TextFormatting.RED);
            return;
        }
        AsyncTwitterFactory factory = new AsyncTwitterFactory();
        AsyncTwitter twitter = factory.getInstance(token);
        twitter.addListener(adapter);
        twitter.updateStatus(String.join(" ", args));
    }
}
