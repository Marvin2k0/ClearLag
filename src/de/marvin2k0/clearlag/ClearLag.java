package de.marvin2k0.clearlag;

import de.marvin2k0.clearlag.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ClearLag extends JavaPlugin implements Listener
{
    public static final String AUTHOR = "Marvin2k0";

    private ArrayList<ItemStack> items = new ArrayList<>();
    private ArrayList<Player> cooldown = new ArrayList<>();
    private Inventory inventory;
    private long nextClear;
    private boolean abyss;
    private int taskId;
    private int delay;

    @Override
    public void onEnable()
    {
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        Text.setUp(this);

        inventory = Bukkit.createInventory(null, 54, Text.get("prefix"));
        delay = 1000 * 60 * 10;

        console.sendMessage(" ");
        console.sendMessage(" ");
        console.sendMessage("§7[§bAbyss§7] Plugin by §9" + AUTHOR + " §7enabled!");
        console.sendMessage(" ");
        console.sendMessage(" ");

        getCommand("abyss").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);

        start();
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Text.get("noplayer"));
            return true;
        }

        Player player = (Player) sender;

        if (!abyss)
        {
            player.sendMessage(Text.get("notintime"));
            return true;
        }

        player.openInventory(inventory);

        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        if (!abyss)
            return;

        if (event.getView().getTitle().equals(Text.get("prefix")))
        {
            Player player = (Player) event.getWhoClicked();

            if (!cooldown.contains(player))
            {
                cooldown.add(player);

                new BukkitRunnable()
                {
                    public void run()
                    {
                        cooldown.remove(player);
                    }
                }.runTaskLater(this, 20 * Long.valueOf(Text.get("cooldown", false)) / 1000);
            }
            else
            {
                event.setCancelled(true);
            }
        }
    }

    private void start()
    {
        abyss = false;
        nextClear = System.currentTimeMillis() + delay;

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            @Override
            public void run()
            {
                if (abyss)
                    return;

                long timeLeft = nextClear - System.currentTimeMillis();

                if (timeLeft <= 0)
                {
                    items.clear();

                    for (World world : Bukkit.getWorlds())
                    {
                        for (Entity e: world.getEntities())
                        {
                            if (!(e instanceof Item))
                                continue;

                            Item item = (Item) e;

                            addItem(item);
                        }

                        world.getEntities().clear();
                    }

                    startRaffle();
                }
                else
                {
                    long minutes = timeLeft / 1000 / 60;
                    long seconds = (timeLeft / 1000) % 60;

                    if (minutes == 1 && seconds == 0)
                    {
                        for (Player p : Bukkit.getOnlinePlayers())
                            p.sendMessage(Text.get("1min"));
                    }
                    else if (minutes == 0 && seconds == 10)
                    {
                        for (Player p : Bukkit.getOnlinePlayers())
                            p.sendMessage(Text.get("10sec"));
                    }
                    else if (minutes == 0 && seconds <=3 && seconds > 0)
                    {
                        for (Player p : Bukkit.getOnlinePlayers())
                            p.sendMessage(Text.get("countdown").replace("%seconds%", seconds + ""));
                    }
                }
            }
        }, 0, 20);
    }

    private void addItem(Item i)
    {
        ItemStack item = i.getItemStack();
        items.add(item);

        i.remove();
    }

    private void startRaffle()
    {
        abyss = true;

        ItemStack[] content = new ItemStack[inventory.getSize()];

        int i = 0;

        for (ItemStack item : items)
        {
            content[i] = item;
            i++;
        }

        inventory.setContents(content);

        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage(Text.get("started"));

        new BukkitRunnable() {

            @Override
            public void run()
            {
                abyss = false;

                for (Player player : Bukkit.getOnlinePlayers())
                {
                    if (player.getOpenInventory() != null && player.getOpenInventory().getTitle().equals(Text.get("prefix")))
                        player.closeInventory();

                    player.sendMessage(Text.get("prefix") + " §7The abyss is closed!");
                }

                nextClear = System.currentTimeMillis() + delay;
            }
        }.runTaskLater(this, 20 * 30);
    }
}
