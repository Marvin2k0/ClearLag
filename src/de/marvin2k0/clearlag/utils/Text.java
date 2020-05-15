package de.marvin2k0.clearlag.utils;

import de.marvin2k0.clearlag.ClearLag;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Text
{
    static FileConfiguration config;
    static Plugin plugin;

    public static String get(String path)
    {
        return path.equalsIgnoreCase("prefix") ? get(path, false) : get(path, true);
    }

    public static String get(String path, boolean prefix)
    {
        return ChatColor.translateAlternateColorCodes('&', prefix ? config.getString("prefix") + " " + config.getString(path) : config.getString(path));
    }

    public static void setUp(Plugin plugin)
    {
        Text.plugin = plugin;
        Text.config = plugin.getConfig();

        config.options().copyDefaults(true);
        config.options().header("Plugin by " + ClearLag.AUTHOR + ", made for 'wiktorlew'");
        config.addDefault("prefix", "&8[&bAbyss&8]");
        config.addDefault("1min", "&7The abyss will open in &b1 minute&7!");
        config.addDefault("10sec", "&7The abyss will open in &b10 seconds&7!");
        config.addDefault("countdown", "&7The abyss will open in &b%seconds% seconds&7!");
        config.addDefault("started", "&7The abyss has opened for &b30 seconds&7! Command: &b/abyss");
        config.addDefault("noplayer", "&cThis command is only for players!");
        config.addDefault("notintime", "&cYou can't do that now!");
        config.addDefault("delayinseconds", 60 * 10);
        config.addDefault("nextpage", "&aNext page");
        config.addDefault("previouspage", "&cPrevious page");
        config.addDefault("cooldown", 500);
        config.addDefault("closed", "&7The abyss is closed");

        saveConfig();
    }

    private static void saveConfig()
    {
        plugin.saveConfig();
    }
}
