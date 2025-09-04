package io.github.theminiluca.virtual.recipe.registry;

import com.google.common.base.Preconditions;
import io.github.theminiluca.virtual.item.entry.ItemEntry;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
class RegistryShapedRecipe extends RegistryRecipe {
    private final String[] shape;
    private final Map<Character, ItemEntry> ingredients;

    public RegistryShapedRecipe(@NotNull final String key, final ConfigurationSection section) {
        super(key, section);
        this.shape = extractShape(section, "shape");
        @Nullable ConfigurationSection ingredients = section.getConfigurationSection("ingredients");
        Preconditions.checkNotNull(ingredients, "ingredients cannot be null");

        Map<Character, ItemEntry> ingredientMap = new HashMap<>();
        for (String ingKey : ingredients.getKeys(false)) {
            Preconditions.checkArgument(ingKey != null && ingKey.length() == 1,
                "Ingredient key must be exactly one character: %s", ingKey);
            char keyChar = ingKey.charAt(0);
            ingredientMap.put(keyChar, ItemEntry.fromItemEntry(Objects.requireNonNull(ingredients.getConfigurationSection(ingKey))));
        }
        this.ingredients = ingredientMap;
    }

    public static String[] extractShape(ConfigurationSection section, String path) {
        List<String> list = section.getStringList(path);

        Preconditions.checkArgument(list.size() <= 3, "Shape must have at most 3 rows");

        for (String row : list) {
            Preconditions.checkArgument(row.length() <= 3, "Each row must have at most 3 characters");
        }

        return list.toArray(new String[0]);
    }

    @Override
    public Recipe toBukkitRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getKey(), getResult());
        shapedRecipe.shape(shape);
        for (Map.Entry<Character, ItemEntry> entry : ingredients.entrySet()) {
            shapedRecipe.setIngredient(entry.getKey(), entry.getValue().toBukkitItem());
        }
        return shapedRecipe;
    }
}
