package net.refy.mc.mods.twitter;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by yutaka on 9/11/2016.
 */
public class CommandAuthTwitter extends CommandBase {
    private RequestToken mRequestToken;

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
        return "auth-twitter";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "start authorization process";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Twitter twitter = TwitterFactory.getSingleton();
        if(args.length == 0){
            try {
                mRequestToken = twitter.getOAuthRequestToken();
                // build chat message
                ChatUtil.sendUrl(mRequestToken.getAuthorizationURL());
            } catch (TwitterException e) {
                ChatUtil.send(e);
            }
        }else if(args.length==1){
            try {
                AccessToken token = twitter.getOAuthAccessToken(mRequestToken, args[0]);
                ModMCTwitter.instance.accessToken = token.getToken();
                ModMCTwitter.instance.accessTokenSecret = token.getTokenSecret();
                ModMCTwitter.instance.saveConfig();
                ChatUtil.send("authentication succeeded");
            } catch (TwitterException e) {
                ChatUtil.send(e);
            }
        }else{
            ChatUtil.send("usage: /auth-twitter [pin]", TextFormatting.YELLOW);
        }
    }
}
