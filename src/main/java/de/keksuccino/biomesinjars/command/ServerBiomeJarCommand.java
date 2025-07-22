package de.keksuccino.biomesinjars.command;

import com.mojang.brigadier.CommandDispatcher;
import de.keksuccino.biomesinjars.item.FilledBiomeJarItem;
import de.keksuccino.biomesinjars.util.LevelUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;

public class ServerBiomeJarCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("biomejar").requires(stack -> stack.hasPermission(2)).then(
            Commands.argument("biome", ResourceLocationArgument.id()).suggests(CommandHelper::buildBiomeSuggestions)
                .executes(context -> giveBiomeJar(context.getSource(), CommandHelper.getBiomeArgument(context)))));
    }

    private static int giveBiomeJar(CommandSourceStack stack, Biome biome) {
        stack.getServer().execute(() -> {
            try {
                var biomeKey = LevelUtils.getBiomeRegistry(stack.getLevel()).getResourceKey(biome)
                    .orElseThrow(IllegalArgumentException::new);
                int slot = stack.getPlayerOrException().getInventory().getFreeSlot();
                ItemStack filledJarStack = FilledBiomeJarItem.createStack(biomeKey);
                if (slot != -1) {
                    stack.getPlayerOrException().getInventory().setItem(slot, filledJarStack);
                } else {
                    stack.sendFailure(Component.translatable("biomesinjars.command.biomejar.unable_to_give"));
                }
            } catch (Exception e) {
                stack.sendFailure(Component.literal("§cError while executing command!"));
                e.printStackTrace();
            }
        });
        return 1;
    }

}