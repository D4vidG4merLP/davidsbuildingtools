package app.byniq.davidscreativetools;

import app.byniq.davidscreativetools.commands.ToolsCommand;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod("davidscreativetools")
public class DavidsCreativeTools {

    public DavidsCreativeTools() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(SpawnControlHandler.class);
    }

    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ToolsCommand.register(event.getDispatcher());
    }
}
