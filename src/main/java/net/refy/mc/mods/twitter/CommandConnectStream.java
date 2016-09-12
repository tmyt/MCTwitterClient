package net.refy.mc.mods.twitter;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import twitter4j.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by yutaka on 9/11/2016.
 */
public class CommandConnectStream extends CommandBase implements StatusListener{
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
        return "connect-stream";
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
        ArrayList<URLEntity> entities = new ArrayList<URLEntity>();
        entities.addAll(Arrays.asList(status.getMediaEntities()));
        entities.addAll(Arrays.asList(status.getURLEntities()));
        entities.sort(SortUrlEntities);
        // format
        int p = 0;
        TextComponentString components = new TextComponentString("");
        TextComponentString user = new TextComponentString("@" + name);
        user.getStyle().setBold(true);
        components.appendSibling(user);
        components.appendText(": ");
        for(URLEntity entity : entities){
            if(p < entity.getStart()){
                components.appendSibling(new TextComponentString(text.substring(p, entity.getStart())));
            }
            String url = entity.getDisplayURL();
            TextComponentString urlComponent = new TextComponentString(url);
            urlComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, entity.getURL()));
            urlComponent.getStyle().setUnderlined(true);
            components.appendSibling(urlComponent);
            p = entity.getEnd();
        }
        if(p < text.length()){
            components.appendSibling(new TextComponentString(text.substring(p)));
        }
        components.getStyle().setColor(TextFormatting.AQUA);
        ChatUtil.send(components);
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

    private static Comparator<URLEntity> SortUrlEntities = new Comparator<URLEntity>() {
        @Override
        public int compare(URLEntity o1, URLEntity o2) {
            return o1.getStart() - o2.getStart();
        }
    };
}
