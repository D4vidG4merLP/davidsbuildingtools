package app.byniq.davidscreativetools;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import app.byniq.davidscreativetools.commands.ToolsCommand;

@Mod.EventBusSubscriber
public class SpawnControlHandler {

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;

        if (event.getLevel().isClientSide()) return;
        EntityType<?> type = mob.getType();
        if (!ToolsCommand.isSpawningEnabled(type)) {
            mob.discard();
        }
    }
}
