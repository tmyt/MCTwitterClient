package net.refy.mc.mods.twitter;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import twitter4j.*;

/**
 * Created by yutaka on 9/11/2016.
 */
public class CommandRunStream extends CommandBase implements StatusListener{
    private TwitterStream mStream;

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
        return "run-stream";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Connect chat window to userstream";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0){
            if(mStream != null){
                mStream.shutdown();
                mStream=null;
            }
            TwitterStreamFactory factory = new TwitterStreamFactory();
            mStream = factory.getInstance(ModMCTwitter.instance.getAccessToken());
            mStream.addListener(this);
            mStream.user();
        }else if(args[0] == "kill"){
            if(mStream != null){
                mStream.shutdown();
                mStream=null;
            }
        }else{
            ChatUtil.send("usage: /run-stream [kill]", TextFormatting.YELLOW);
        }
    }

    @Override
    public void onStatus(Status status) {
        String name = status.getUser().getScreenName();
        String text = status.getText();
        ChatUtil.send("@" + name + ": " + text, TextFormatting.AQUA);
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {

    }

    @Override
    public void onStallWarning(StallWarning warning) {

    }

    @Override
    public void onException(Exception ex) {

    }
}
