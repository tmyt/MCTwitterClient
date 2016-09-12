package net.refy.mc.mods.twitter;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import twitter4j.TwitterException;

/**
 * Created by yutaka on 9/11/2016.
 */
public class ChatUtil {
    public static void send(String string){
        send(string, TextFormatting.WHITE);
    }

    public static void send(String string, TextFormatting color){
        ITextComponent message = new TextComponentString(string);
        message.getStyle().setColor(color);
        send(message);
    }

    public static void send(ITextComponent components){
        FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(components);
    }

    public static void send(TwitterException e){
        send("error: " + e.getErrorMessage(), TextFormatting.RED);
    }

    public static void sendUrl(String url){
        ITextComponent message = new TextComponentString(url);
        message.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        message.getStyle().setUnderlined(true);
        FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(message);
    }
}
