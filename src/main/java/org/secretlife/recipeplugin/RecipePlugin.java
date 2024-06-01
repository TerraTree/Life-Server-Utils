package org.secretlife.recipeplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.secretlife.recipeplugin.events.EnchantEvents;
import org.secretlife.recipeplugin.events.MobEvents;

public final class RecipePlugin extends JavaPlugin {

    EnchantEvents enchantEvents = new EnchantEvents();
    MobEvents mobEvents = new MobEvents();

    @Override
    public void onEnable() {
        System.out.println("Enabled Recipe Plugin");
        getServer().getPluginManager().registerEvents(enchantEvents, this);
        getServer().getPluginManager().registerEvents(mobEvents, this);
        elytraRecipe();
        saddleRecipe();
        tntRecipe();
        nametagRecipe();
        spawnerRecipe();


        //ShapedRecipe recipe2 = createRecipe(Material.CACTUS, "cactus");
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        System.out.println("Disabled Recipe Plugin");
        // Plugin shutdown logic
    }

    private ShapedRecipe createShapedRecipe(Material material, String namespacedKey) {
        ItemStack item = new ItemStack(material);
        NamespacedKey key = new NamespacedKey(this, namespacedKey);
        return new ShapedRecipe(key, item);
    }

    private ShapelessRecipe createShapelessRecipe(Material material, String namespacedKey) {
        ItemStack item = new ItemStack(material);
        NamespacedKey key = new NamespacedKey(this, namespacedKey);
        return new ShapelessRecipe(key, item);
    }

    private void elytraRecipe() {
        ShapedRecipe recipe = createShapedRecipe(Material.ELYTRA, "elytra");
        recipe.shape(
                "PEP",
                "ENE",
                "F F");
        recipe.setIngredient('P', Material.PHANTOM_MEMBRANE);
        recipe.setIngredient('E', Material.END_STONE);
        recipe.setIngredient('N', Material.NETHERITE_CHESTPLATE);
        recipe.setIngredient('F', Material.FIREWORK_ROCKET);
        Bukkit.addRecipe(recipe);
    }

    private void saddleRecipe() {
        ShapedRecipe recipe = createShapedRecipe(Material.SADDLE, "saddle");
        recipe.shape(
                "LLL",
                "LTL",
                "   ");
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('T', Material.TRIPWIRE_HOOK);
        Bukkit.addRecipe(recipe);
    }

    private void nametagRecipe() {
        ShapelessRecipe recipe = createShapelessRecipe(Material.NAME_TAG, "nametag");
        recipe.addIngredient(1, Material.IRON_BARS);
        recipe.addIngredient(1, Material.PAPER);
        recipe.addIngredient(1, Material.STRING);
        Bukkit.addRecipe(recipe);
    }
    private void tntRecipe() {
        ShapelessRecipe recipe = createShapelessRecipe(Material.TNT, "tnt");
        recipe.addIngredient(1, Material.GUNPOWDER);
        recipe.addIngredient(1, Material.PAPER);
        recipe.addIngredient(1, Material.STRING);
        Bukkit.addRecipe(recipe);
    }

    private void spawnerRecipe() {
        ShapedRecipe recipe = createShapedRecipe(Material.SPAWNER, "Spawner");

        recipe.shape(
                "III",
                "ISI",
                "III");
        recipe.setIngredient('I', Material.IRON_BARS);
        recipe.setIngredient('S', Material.DIAMOND);
        Bukkit.addRecipe(recipe);
    }
}
