package de.keksuccino.biomesinjars.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class CommandHelper {

    public static CompletableFuture<Suggestions> buildBiomeSuggestions(CommandContext<CommandSourceStack> context,
        SuggestionsBuilder builder
    ) {
        var result = new ArrayList<String>();
        for (ResourceLocation res : context.getSource().getServer().registryAccess().registryOrThrow(Registries.BIOME)
            .keySet()) {
            result.add(res.toString());
        }
        if (result.isEmpty()) {
            result.add("");
        }
        return buildStringSuggestions(builder, result.toArray(String[]::new));
    }

    private static CompletableFuture<Suggestions> buildStringSuggestions(SuggestionsBuilder suggestionsBuilder,
        String... suggestions
    ) {
        return SharedSuggestionProvider.suggest(suggestions, suggestionsBuilder);
    }

    public static Biome getBiomeArgument(CommandContext<CommandSourceStack> context) {
        ResourceLocation res = ResourceLocationArgument.getId(context, "biome");
        return context.getSource().getServer().registryAccess().registryOrThrow(Registries.BIOME).get(res);
    }

}
