package io.github.theminiluca.virtual.item;

import io.github.theminiluca.virtual.Virtual;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
public class ItemConfig extends YamlConfiguration {

    private final Virtual plugin;

    private final Map<String, RegistryItem> items = new HashMap<>();
    public ItemConfig(final Virtual plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveResource("items.yml", false);
        try {
            load(new File(plugin.getDataFolder(), "items.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        items.clear();
        for (String key : getKeys(false)) {
            try {
                items.put(key, new RegistryItem(key, plugin, Objects.requireNonNull(getConfigurationSection(key))));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public @Nullable RegistryItem getItem(String key) {
        return items.get(key);
    }


}
