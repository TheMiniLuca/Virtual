package io.github.theminiluca.virtual.recipe.registry;

import io.github.theminiluca.virtual.Virtual;
import io.github.theminiluca.virtual.item.entry.ItemEntry;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

@Getter
public abstract class RegistryRecipe implements Recipe{

    private final NamespacedKey key;
    private final ConfigurationSection section;
    private final ItemEntry result;


    public RegistryRecipe(@NotNull String key, final ConfigurationSection section) {
        this.section = section;
        this.key = Virtual.getInstance().virtual(key);
        this.result = ItemEntry.fromItemEntry(Objects.requireNonNull(section.getConfigurationSection("result")));
    }

    public abstract Recipe toBukkitRecipe();

    @Override
    public @NotNull ItemStack getResult() {
        return result.toBukkitItem();
    };
}
