package org.secretlife.recipeplugin.events;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class EnchantEvents implements Listener {

    @EventHandler
    public void removeHighLevelEnchants(PrepareItemEnchantEvent event) {
        for (EnchantmentOffer offer: event.getOffers()) {
            if (offer.getEnchantment().getKey().equals(NamespacedKey.minecraft("protection"))) {
                offer.setEnchantmentLevel(Math.min(offer.getEnchantmentLevel(), 2));
            }
        }
        //TODO - Set more nerfed enchantments
    }


    @EventHandler
    public void itemEnchant(EnchantItemEvent event) {
        for (Map.Entry<Enchantment, Integer> enchantment: event.getEnchantsToAdd().entrySet()) {
            System.out.println(enchantment.getKey().getKey());
            if(enchantment.getKey().getKey().equals(NamespacedKey.minecraft("protection"))) {
                if (enchantment.getValue() > 2) {
                    enchantment.setValue(2);
                    Bukkit.broadcastMessage(event.getEnchanter().getDisplayName() + " has been nerfed lol!");
                }
            }
        }
    }

    @EventHandler
    public void trade(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager villager)) return;

        List<MerchantRecipe> recipes = new ArrayList<>();
        Iterator<MerchantRecipe> recipeIterator;
        for (recipeIterator = villager.getRecipes().iterator(); recipeIterator.hasNext(); ) {
            MerchantRecipe recipe = recipeIterator.next();
            ItemStack result = recipe.getResult();
            ItemMeta meta = result.getItemMeta();
            if (meta != null) {
                meta = checkEnchantment(meta, Enchantment.PROTECTION_ENVIRONMENTAL);
            }
            result.setItemMeta(meta);
            MerchantRecipe replacement = new MerchantRecipe(result, recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier());
            replacement.setIngredients(recipe.getIngredients());
            recipes.add(replacement);
        }
        villager.setRecipes(recipes);

        if (villager.getRecipes().isEmpty()) {
            event.setCancelled(true);
        }
    }

    public ItemMeta checkEnchantment(ItemMeta meta, Enchantment enchantment) {
        if (meta.getEnchants().containsKey(enchantment) && meta.getEnchants().get(enchantment) > 2) {
            meta.removeEnchant(enchantment);
            meta.addEnchant(enchantment, 2, true);
        }
        return meta;
    }
}
