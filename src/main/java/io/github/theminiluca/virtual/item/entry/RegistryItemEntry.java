package io.github.theminiluca.virtual.item.entry;

import com.google.common.base.Preconditions;
import io.github.theminiluca.virtual.Virtual;
import io.github.theminiluca.virtual.item.RegistryItem;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
class RegistryItemEntry extends ItemEntry {
    private final @NotNull RegistryItem registryItem;

    public RegistryItemEntry(int amount, String itemKey) {
        super(amount);
        @Nullable RegistryItem item = Virtual.getInstance().getItemConfig().getItem(itemKey);
        Preconditions.checkNotNull(item, "registryItem with key '%s' does not exist", itemKey);
        this.registryItem = item;
    }

    @Override
    public ItemStack toBukkitItem() {
        return registryItem.toBukkitItem(amount);
    }
}
