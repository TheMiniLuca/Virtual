package io.github.theminiluca.virtual.item.entry;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

class VanillaItemEntry extends ItemEntry {
    private final Material material;

    public VanillaItemEntry(int amount, String material) {
        super(amount);
        this.material = Preconditions.checkNotNull(Material.matchMaterial(material), "Invalid VANILLA material: %s", material);
    }

    @Contract("->new")
    @Override
    public ItemStack toBukkitItem() {
        return new ItemStack(material, amount);
    }
}
