package de.keksuccino.biomesinjars.event;

import de.keksuccino.biomesinjars.BiomesInJars;
import de.keksuccino.biomesinjars.command.ServerBiomeJarCommand;
import de.keksuccino.biomesinjars.command.ServerSetChunkBiomeCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;

import static de.keksuccino.biomesinjars.BiomesInJars.res;

@EventBusSubscriber(modid = BiomesInJars.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEventHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity() instanceof ServerPlayer) {
            ResourceLocation recipe = res("empty_biome_jar");
            if (!((ServerPlayer) e.getEntity()).getRecipeBook().contains(recipe)) {
                e.getEntity().awardRecipesByKey(List.of(recipe));
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterServerCommands(RegisterCommandsEvent event) {
        ServerSetChunkBiomeCommand.register(event.getDispatcher());
        ServerBiomeJarCommand.register(event.getDispatcher());
    }

}
