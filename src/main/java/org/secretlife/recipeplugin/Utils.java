package org.secretlife.recipeplugin;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

    public static ItemMeta checkEnchantment(ItemMeta meta, Enchantment enchantment) {
        if (meta.getEnchants().containsKey(enchantment) && meta.getEnchants().get(enchantment) > 2) {
            meta.removeEnchant(enchantment);
            meta.addEnchant(enchantment, 2, true);
        }
        return meta;
    }
}
