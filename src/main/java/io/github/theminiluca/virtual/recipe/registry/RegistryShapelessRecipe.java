package io.github.theminiluca.virtual.recipe.registry;

import com.google.common.base.Preconditions;
import io.github.theminiluca.virtual.item.entry.ItemEntry;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
class RegistryShapelessRecipe extends RegistryRecipe {


    private final @NotNull List<ItemEntry> ingredients;
    public RegistryShapelessRecipe(@NotNull final String key, final ConfigurationSection section) {
        super(key, section);
        List<ItemEntry> ingredients = new ArrayList<>();
        List<?> rawList = section.getList("ingredients");

        Preconditions.checkNotNull(rawList, "ingredients list must not be null");

        for (int i = 0; i < rawList.size(); i++) {
            Object obj = rawList.get(i);

            Preconditions.checkArgument(obj instanceof ConfigurationSection,
                "Ingredient at index %s is invalid: expected ConfigurationSection but got %s",
                i, obj == null ? "null" : obj.getClass().getSimpleName());

            ConfigurationSection ingSection = (ConfigurationSection) obj;
            ingredients.add(ItemEntry.fromItemEntry(ingSection));
        }

        this.ingredients = ingredients;
    }

    @Override
    public Recipe toBukkitRecipe() {
        ShapelessRecipe recipe = new ShapelessRecipe(getKey(), getResult());
        for (ItemEntry entry : ingredients) {
            recipe.addIngredient(entry.toBukkitItem());
        }
        return recipe;
    }
}
