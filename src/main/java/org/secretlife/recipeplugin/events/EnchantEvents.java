package org.secretlife.recipeplugin.events;

import com.google.common.collect.Lists;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;

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
                }
            }
        }
    }

    @EventHandler
    public void anvilCombine(PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result == null) return;
        if (result.getItemMeta() instanceof EnchantmentStorageMeta meta) {
            if (meta.getStoredEnchants().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL) && meta.getStoredEnchants().get(Enchantment.PROTECTION_ENVIRONMENTAL) > 2) {
                event.setResult(null);
                System.out.println("book result removed");
            }
        }
        else {
            ItemMeta meta = result.getItemMeta();
            if (meta == null) return;
            meta = checkEnchantment(meta, Enchantment.PROTECTION_ENVIRONMENTAL);
            result.setItemMeta(meta);
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

    @EventHandler
    public void brewing(BrewEvent event) {
        List<ItemStack> results = event.getResults();
        for (ItemStack item: results) {
            if (item.getItemMeta() instanceof PotionMeta meta) {
                PotionData data = meta.getBasePotionData();
                PotionType type = meta.getBasePotionData().getType();
                for (int i = 0; i < type.getPotionEffects().size(); i++) {
                    PotionEffect potionEffect = type.getPotionEffects().get(i);
                    if (potionEffect.getType() == PotionEffectType.INCREASE_DAMAGE && data.isUpgraded()) {
                        PotionType type2 = PotionType.POISON;
                        PotionData data2 = new PotionData(type2, false, false);
                        meta.setBasePotionData(data2);
                        meta.setDisplayName("Trouble");
                        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                        item.setItemMeta(meta);
                        break;
                    }
                }




            }
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
