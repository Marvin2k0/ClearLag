package de.marvin2k0.clearlag.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ScrollerInventory
{
    public static HashMap<UUID, ScrollerInventory> users = new HashMap<UUID, ScrollerInventory>();

    public ArrayList<Inventory> pages = new ArrayList<Inventory>();
    public UUID id;
    public int currpage = 0;

    public ScrollerInventory(ArrayList<ItemStack> items, String name, Player p)
    {
        this.id = UUID.randomUUID();

        Inventory page = getBlankPage(name);

        for (int i = 0; i < items.size(); i++)
        {
            if (page.firstEmpty() == 46)
            {
                pages.add(page);
                page = getBlankPage(name);
                page.addItem(items.get(i));
            }
            else
            {
                page.addItem(items.get(i));
            }
        }
        pages.add(page);

        p.openInventory(pages.get(currpage));
        users.put(p.getUniqueId(), this);
    }


    public static final String nextPageName = "§aNext Page";
    public static final String previousPageName = "§cPrevious Page";

    private Inventory getBlankPage(String name)
    {
        Inventory page = Bukkit.createInventory(null, 54, name);

        ItemStack nextpage = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta meta = nextpage.getItemMeta();
        meta.setDisplayName(nextPageName);
        nextpage.setItemMeta(meta);

        ItemStack prevpage = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        meta = prevpage.getItemMeta();
        meta.setDisplayName(previousPageName);
        prevpage.setItemMeta(meta);


        page.setItem(53, nextpage);
        page.setItem(45, prevpage);

        return page;
    }
}
