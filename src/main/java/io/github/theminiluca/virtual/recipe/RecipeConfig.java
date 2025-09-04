package io.github.theminiluca.virtual.recipe;

import io.github.theminiluca.virtual.Virtual;
import io.github.theminiluca.virtual.recipe.registry.RegistryRecipe;
import io.github.theminiluca.virtual.recipe.registry.RecipeType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Slf4j
public class RecipeConfig extends YamlConfiguration {

    private final Virtual plugin;

    private final Map<String, RegistryRecipe> recipes = new HashMap<>();

    public RecipeConfig(final Virtual plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveResource("recipes.yml", false);
        try {
            load(new File(plugin.getDataFolder(), "recipes.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        recipes.forEach((key, recipe) -> {
            Bukkit.removeRecipe(recipe.getKey());
        });

        recipes.clear();
        for (String key : getKeys(false)) {
            try {
                recipes.put(key, RecipeType.create(key, Objects.requireNonNull(getConfigurationSection(key))));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        recipes.forEach((key, recipe) -> {
            Bukkit.addRecipe(recipe.toBukkitRecipe());
        });
    }


}
