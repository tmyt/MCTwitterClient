package net.refy.mc.mods.twitter;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import twitter4j.auth.AccessToken;

import java.io.File;

/**
 * Created by yutaka on 9/11/2016.
 */
@Mod(modid = "mctwitter", version = "1.1.0")
public class ModMCTwitter {
    static final String CATEGORY = "twitter";
    static final String KEY_ACCESS_TOKEN = "access_token";
    static final String KEY_ACCESS_TOKEN_SECRET = "access_token_secret";

    public String accessToken;
    public String accessTokenSecret;

    private File mConfigFile;

    @Mod.Instance("mctwitter")
    public static ModMCTwitter instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        mConfigFile = e.getSuggestedConfigurationFile();
        loadConfig();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CommandTwitter());
        ClientCommandHandler.instance.registerCommand(new CommandAuthTwitter());
        ClientCommandHandler.instance.registerCommand(new CommandRunStream());
    }

    @SubscribeEvent
    public void load(WorldEvent.Load e) {
        System.out.println("World loaded.");
    }

    @SubscribeEvent
    public void unload(WorldEvent.Unload e) {
        System.out.println("World unloaded");
    }

    public void loadConfig() {
        Configuration cfg = new Configuration(mConfigFile);
        cfg.load();
        ConfigCategory category = cfg.getCategory(CATEGORY);
        Property prop = category.get(KEY_ACCESS_TOKEN);
        if (prop != null) accessToken = prop.getString();
        prop = category.get(KEY_ACCESS_TOKEN_SECRET);
        if (prop != null) accessTokenSecret = prop.getString();
    }

    public void saveConfig() {
        Configuration cfg = new Configuration(mConfigFile);
        ConfigCategory category = cfg.getCategory(CATEGORY);
        category.put(KEY_ACCESS_TOKEN, new Property(KEY_ACCESS_TOKEN, accessToken, Property.Type.STRING));
        category.put(KEY_ACCESS_TOKEN_SECRET, new Property(KEY_ACCESS_TOKEN_SECRET, accessTokenSecret, Property.Type.STRING));
        cfg.save();
    }

    public AccessToken getAccessToken() {
        if (accessToken == null || accessTokenSecret == null || accessToken.equals("") || accessTokenSecret.equals(""))
            return null;
        return new AccessToken(accessToken, accessTokenSecret);
    }
}