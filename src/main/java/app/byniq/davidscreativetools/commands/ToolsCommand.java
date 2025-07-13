package app.byniq.davidscreativetools.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ToolsCommand {

    // Keep track of spawn rules in memory
    private static final Map<EntityType<?>, Boolean> entitySpawnToggles = new HashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("tools")
                        .then(Commands.literal("entities")
                                .then(Commands.literal("spawning")
                                        .then(Commands.argument("entity", ResourceLocationArgument.id())
                                                .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                        .executes(context -> {
                                                            ResourceLocation entityId = ResourceLocationArgument.getId(context, "entity");
                                                            Optional<? extends EntityType<?>> optional = EntityType.byString(entityId.toString());

                                                            if (optional.isEmpty()) {
                                                                context.getSource().sendFailure(Component.literal("Invalid entity ID: " + entityId));
                                                                return 0;
                                                            }

                                                            EntityType<?> entityType = optional.get();
                                                            boolean enable = BoolArgumentType.getBool(context, "enabled");

                                                            ToolsCommand.setSpawningEnabled(entityType, enable);
                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal("Spawning for " + entityId + " set to " + enable),
                                                                    true
                                                            );
                                                            return 1;
                                                        })))))

                        .then(Commands.literal("rename")
                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            var source = context.getSource();
                                            var player = source.getPlayerOrException();
                                            ItemStack item = player.getMainHandItem();

                                            if (item.isEmpty() || item.getItem() == Items.AIR) {
                                                source.sendFailure(Component.literal("You must hold an item to rename it."));
                                                return 0;
                                            }

                                            String name = StringArgumentType.getString(context, "name");
                                            Component displayName = Component.literal(name);

                                            item.setHoverName(displayName);

                                            source.sendSuccess(() ->
                                                    Component.literal("Renamed item to: ").append(displayName), true
                                            );

                                            return 1;
                                        })
                                ))

                        );
    }

    public static void setSpawningEnabled(EntityType<?> entityType, boolean enabled) {
        entitySpawnToggles.put(entityType, enabled);
    }

    public static boolean isSpawningEnabled(EntityType<?> entityType) {
        return entitySpawnToggles.getOrDefault(entityType, true);
    }
}
