package io.github.theminiluca.virtual;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.theminiluca.virtual.item.ItemConfig;
import io.github.theminiluca.virtual.item.RegistryItem;
import io.github.theminiluca.virtual.recipe.RecipeConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

@Slf4j
@Getter
public final class Virtual extends JavaPlugin {

    @Getter
    private static Virtual instance;


    public @NotNull NamespacedKey virtual(String key) {
        return new NamespacedKey("virtual", key);
    }

    private RecipeConfig recipeConfig;
    private ItemConfig itemConfig;

    @Override
    public void onEnable() {
        instance = this;
        itemConfig = new ItemConfig(this);
        recipeConfig = new RecipeConfig(this);
        reload();
        CommandAPICommand virtualCommand = new CommandAPICommand("virtual");
        virtualCommand.setPermission(CommandPermission.OP);
        virtualCommand.withSubcommand(new CommandAPICommand("get").withArguments(
                    new StringArgument("item")
                        .replaceSuggestions(ArgumentSuggestions.strings(info -> new ArrayList<>(itemConfig.getItems().keySet()).toArray(new String[]{})
                        ))
                )
                .executesPlayer((player, args) -> {
                    String itemKey = (String) args.get("item");
                    RegistryItem registryItem = itemConfig.getItems().get(itemKey);

                    if (registryItem == null) {
                        player.sendMessage("존재하지 않는 아이템입니다!");
                        return;
                    }

                    player.getInventory().addItem(registryItem.toBukkitItem());
                    player.sendMessage("아이템을 받았습니다: " + itemKey);
                })
        );
        virtualCommand.withSubcommand(new CommandAPICommand("reload").executes((sender, args) -> {
            reload();
        })).register(this);
    }

    public void reload() {
        itemConfig.load();
        recipeConfig.load();
        log.info("아이템, 레시피 모듈을 리로드 했습니다.");

    }

}
