package io.github.theminiluca.virtual.item.entry;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public abstract class ItemEntry {
    protected final int amount;

    protected ItemEntry(int amount) {
        this.amount = amount;
    }

    public abstract ItemStack toBukkitItem();

    public static ItemEntry fromItemEntry(ConfigurationSection section) {
        int amount = section.getInt("amount", 1);
        Preconditions.checkArgument(amount > 0, "ItemEntry amount must be positive");
        if (section.contains("material")) {
            String materialName = Preconditions.checkNotNull(section.getString("material"), "material must not be null");
            return new VanillaItemEntry(amount, materialName);
        }
        else if (section.contains("registry")) {
            String registry = Preconditions.checkNotNull(section.getString("registry"), "registry must not be null");
            return new RegistryItemEntry(amount, registry);
        }
        else {
            throw new IllegalArgumentException("ItemEntry must have either 'material' or 'registry'");
        }
    }
}
