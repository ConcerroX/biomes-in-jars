package de.keksuccino.biomesinjars.registry;

import de.keksuccino.biomesinjars.BiomesInJars;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static de.keksuccino.biomesinjars.BiomesInJars.res;

public class ModBiomes {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registries.BIOME, BiomesInJars.MOD_ID);

    public static final ResourceKey<Biome> DEAD_LAND = ResourceKey.create(Registries.BIOME, res("dead_land"));

    public static void register(BootstrapContext<Biome> bootstrap) {
        bootstrap.register(DEAD_LAND, new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .foliageColorOverride(0xC2C9BF)
                .grassColorOverride(0xC2C9BF)
                .waterColor(10391163)
                .fogColor(10391163)
                .waterFogColor(10391163)
                .skyColor(6853283)
                .build()
            ).temperature(0.8F)
            .downfall(0.4F)
            .mobSpawnSettings(MobSpawnSettings.EMPTY)
            .generationSettings(BiomeGenerationSettings.EMPTY)
            .build()
        );
    }

}
