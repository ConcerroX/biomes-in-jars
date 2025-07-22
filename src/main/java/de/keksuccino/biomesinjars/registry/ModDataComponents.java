package de.keksuccino.biomesinjars.registry;

import de.keksuccino.biomesinjars.BiomesInJars;
import de.keksuccino.biomesinjars.datacomponent.BiomeData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(
        Registries.DATA_COMPONENT_TYPE, BiomesInJars.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BiomeData>> BIOME_DATA = DATA_COMPONENTS.registerComponentType(
        "biome_data", builder -> builder.persistent(BiomeData.CODEC).networkSynchronized(BiomeData.STREAM_CODEC));

}
