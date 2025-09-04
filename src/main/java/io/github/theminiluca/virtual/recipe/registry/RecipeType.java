package io.github.theminiluca.virtual.recipe.registry;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public enum RecipeType {
    SHAPED,
    SHAPELESS;
    public static @NotNull RegistryRecipe create(String key, final ConfigurationSection section) {
        RecipeType recipeType = RecipeType.valueOf(section.getString("recipe_type"));
        return switch (recipeType) {
            case SHAPED -> new RegistryShapedRecipe(key, section);
            case SHAPELESS -> new RegistryShapelessRecipe(key, section);
        };
    }
}
