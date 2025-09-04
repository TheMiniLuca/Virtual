package io.github.theminiluca.virtual.hooks.paper_1_21_8;

import io.github.theminiluca.virtual.hooks.PaperHooks;
import net.minecraft.SharedConstants;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;

public class PaperHooks1_21_8 implements PaperHooks {
    @Override
    public void doSomething(final Logger logger) {
        logger.info(SharedConstants.getCurrentVersion().getName());

    }
}
