package io.github.theminiluca.virtual.item;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
public class RegistryItem {
    private final @NotNull String uniqueId;
    private final @NotNull Material type;
    private final @Nullable Component displayname;
    private final @Nullable List<Component> lore;
    private final @Nullable Map<Enchantment, Integer> enchantments;
    private final @Nullable Set<ItemFlag> itemFlags;
    private final boolean unbreakable;

    private final @NotNull Plugin plugin;

    public RegistryItem(@NotNull String uniqueId, @NotNull Plugin plugin, ConfigurationSection section) {
        this.plugin = plugin;

        this.uniqueId = uniqueId;
        // type
        String typeStr = section.getString("type");
        Preconditions.checkNotNull(typeStr, "아이템 '%s'의 type 값이 없습니다!", uniqueId);

        Material material = Material.getMaterial(typeStr.toUpperCase());
        Preconditions.checkNotNull(material, "아이템 '%s'의 type '%s'이(가) 유효하지 않습니다!", uniqueId, typeStr);
        this.type = material;

        // displayname
        if (section.isString("displayname")) {
            this.displayname = MiniMessage.miniMessage().deserialize(Objects.requireNonNull(section.getString("displayname")));
        } else {
            this.displayname = null;
        }

        // lore
        if (section.isList("lore")) {
            this.lore = new ArrayList<>();
            List<String> loreList = section.getStringList("lore");
            for (String line : loreList) {
                this.lore.add(MiniMessage.miniMessage().deserialize(line));
            }
        } else {
            this.lore = null;
        }

        // enchantments
        ConfigurationSection enchSection = section.getConfigurationSection("enchantments");
        if (enchSection != null) {
            this.enchantments = new HashMap<>();
            for (String key : enchSection.getKeys(false)) {
                Enchantment ench = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(key));
                if (ench != null) {
                    int level = enchSection.getInt(key, 1);
                    enchantments.put(ench, level);
                }
            }
        } else {
            this.enchantments = null;
        }

        // item_flags
        List<String> flagsList = section.getStringList("item_flags");
        if (section.isList("lore")) {
            this.itemFlags = new HashSet<>();
            for (String flag : flagsList) {
                try {
                    itemFlags.add(ItemFlag.valueOf(flag.toUpperCase()));
                } catch (IllegalArgumentException ignored) {
                }
            }
        } else {
            this.itemFlags = null;
        }

        // unbreakable
        this.unbreakable = section.getBoolean("unbreakable", false);
    }

    public @NotNull ItemStack toBukkitItem() {
        return toBukkitItem(1);
    }

    public @NotNull ItemStack toBukkitItem(int amount) {
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        ItemStack itemStack = new ItemStack(type, amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.lore(lore);
        meta.displayName(displayname);
        NamespacedKey key = new NamespacedKey(plugin, "unique_id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, uniqueId);

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
