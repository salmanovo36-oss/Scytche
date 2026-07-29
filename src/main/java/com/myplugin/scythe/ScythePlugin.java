package com.myplugin.scythe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class ScythePlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        registerScytheRecipe();
    }

    private void registerScytheRecipe() {
        ItemStack scythe = new ItemStack(Material.NETHERITE_HOE);
        ItemMeta meta = scythe.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("\u00a76\u0411\u043e\u0435\u0432\u0430\u044f \u041a\u043e\u0441\u0430");
            meta.setLore(Collections.singletonList("\u00a77\u0423\u0440\u043e\u043d \u0443\u0432\u0435\u043b\u0438\u0447\u0438\u0432\u0430\u0435\u0442\u0441\u044f \u043f\u0440\u0438 \u043f\u0430\u0440\u043d\u043e\u043c \u043d\u043e\u0448\u0435\u043d\u0438\u0438"));
            meta.setCustomModelData(777);
            scythe.setItemMeta(meta);
        }

        NamespacedKey key = new NamespacedKey(this, "custom_scythe");
        ShapedRecipe recipe = new ShapedRecipe(key, scythe);

        recipe.shape(
                "BB ",
                " S ",
                " S "
            );

        recipe.setIngredient('B', Material.NETHERITE_BLOCK);
        recipe.setIngredient('S', Material.STICK);

        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        
        Player player = (Player) event.getDamager();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (mainHand.getType() == Material.NETHERITE_HOE && offHand.getType() == Material.NETHERITE_HOE) {
            if (mainHand.hasItemMeta() && offHand.hasItemMeta()) {
                ItemMeta mainMeta = mainHand.getItemMeta();
                ItemMeta offMeta = offHand.getItemMeta();
                
                if (mainMeta.hasCustomModelData() && mainMeta.getCustomModelData() == 777 &&
                    offMeta.hasCustomModelData() && offMeta.getCustomModelData() == 777) {
                    
                    if (mainMeta.hasDisplayName() && offMeta.hasDisplayName()) {
                        String expectedKeyword = "\u041a\u043e\u0441\u0430";
                        if (mainMeta.getDisplayName().contains(expectedKeyword) && offMeta.getDisplayName().contains(expectedKeyword)) {
                            event.setDamage(event.getDamage() + 15.0);
                        }
                    }
                }
            }
        }
    }
}
