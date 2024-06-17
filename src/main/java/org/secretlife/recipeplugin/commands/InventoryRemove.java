package org.secretlife.recipeplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;

import static org.secretlife.recipeplugin.Utils.checkEnchantment;

public class InventoryRemove implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You must be an operator to run this command.");
            return true;
        }

        for (Player player: Bukkit.getOnlinePlayers()) {
            for (ItemStack armour: player.getInventory().getArmorContents()) {
                if (armour != null) {
                    ItemMeta meta = checkEnchantment(armour.getItemMeta(), Enchantment.PROTECTION_ENVIRONMENTAL);
                    armour.setItemMeta(meta);
                }
            }

            for (ItemStack item: player.getInventory().getContents()) {
                if (item != null && item.getItemMeta() instanceof ArmorMeta) {
                    ItemMeta meta = checkEnchantment(item.getItemMeta(), Enchantment.PROTECTION_ENVIRONMENTAL);
                    item.setItemMeta(meta);
                }
            }
        }

        sender.sendMessage("Armour has been nerfed to protection 2");
        return true;
    }
}
