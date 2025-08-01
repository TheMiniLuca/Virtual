package io.github.theminiluca.virtual.hooks.paper_1_21_4;

import io.github.theminiluca.virtual.hooks.PaperHooks;
import net.minecraft.SharedConstants;
import org.slf4j.Logger;

public class PaperHooks1_21_4 implements PaperHooks {
    @Override
    public void doSomething(final Logger logger) {
        logger.info(SharedConstants.getCurrentVersion().getName());
    }
}
