package org.secretlife.recipeplugin.events;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.SpawnEgg;


import java.util.Random;

public class MobEvents implements Listener {

    @EventHandler
    public void onMobDeath(EntityDeathEvent event){
        LivingEntity entity = event.getEntity();
        if(entity.getKiller() != null){
            if (entity instanceof Monster) {
                Random random = new Random();
                int value = random.nextInt(101);
                if (value == 100) {
                    Material material = Material.getMaterial(entity.getType() + "_SPAWN_EGG");
                    if (material != null) {
                        ItemStack egg = new ItemStack(material);
                        egg.setType(material);
                        if (egg.getItemMeta() instanceof SpawnEggMeta meta) {
                            egg.setItemMeta(meta);
                            event.getDrops().add(egg);
                        }
                    }

                }
            }

        }
    }
}
