package de.keksuccino.biomesinjars.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.keksuccino.biomesinjars.util.LevelUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.biome.Biome;

public class ServerSetChunkBiomeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setchunkbiome").requires(stack -> stack.hasPermission(2)).then(
            Commands.argument("biome", ResourceLocationArgument.id()).suggests(CommandHelper::buildBiomeSuggestions)
                .executes(context -> setChunkBiome(context.getSource(), CommandHelper.getBiomeArgument(context)))));
    }

    private static int setChunkBiome(CommandSourceStack stack, Biome biome) {
        stack.getServer().execute(() -> {
            try {
                //Set all sections in chunk to new biome
                var level = stack.getLevel();
                var biomeKey = LevelUtils.getBiomeRegistry(level).getResourceKey(biome)
                    .orElseThrow(IllegalArgumentException::new);
                if (LevelUtils.setChunkBiomeAtBlockPos(level, stack.getPlayerOrException().blockPosition(), biomeKey)) {
                    stack.sendSuccess(() -> Component.literal("§aChunk biome changed to: " + biomeKey.location()),
                        false);
                } else {
                    stack.sendFailure(Component.literal("§cUnable to change biome!"));
                }

                //Update client chunk renderer for all players
                stack.getServer().getPlayerList().getPlayers().forEach((player) -> {
                    try {
                        ChunkMap.markChunkPendingToSend(player,
                            level.getChunkAt(stack.getPlayerOrException().blockPosition()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                stack.sendFailure(Component.literal("§cError while executing command!"));
                e.printStackTrace();
            }
        });
        return 1;
    }

}