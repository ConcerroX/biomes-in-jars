package de.keksuccino.biomesinjars.event;

import de.keksuccino.biomesinjars.BiomesInJars;
import de.keksuccino.biomesinjars.entity.entities.biomejar.empty.EmptyBiomeJarEntity;
import de.keksuccino.biomesinjars.entity.entities.biomejar.empty.EmptyBiomeJarEntityModel;
import de.keksuccino.biomesinjars.entity.entities.biomejar.empty.EmptyBiomeJarEntityRenderer;
import de.keksuccino.biomesinjars.entity.entities.biomejar.filled.FilledBiomeJarEntity;
import de.keksuccino.biomesinjars.entity.entities.biomejar.filled.FilledBiomeJarEntityModel;
import de.keksuccino.biomesinjars.entity.entities.biomejar.filled.FilledBiomeJarEntityRenderer;
import de.keksuccino.biomesinjars.registry.ModBiomes;
import de.keksuccino.biomesinjars.registry.ModEntityTypes;
import de.keksuccino.biomesinjars.registry.ModItems;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.util.Set;

@EventBusSubscriber(modid = BiomesInJars.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventHandler {

    @SubscribeEvent
    public static void onCreateEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.EMPTY_BIOME_JAR_ENTITY.get(),
            EmptyBiomeJarEntity.createEmptyBiomeJarEntityAttributes().build());
        event.put(ModEntityTypes.FILLED_BIOME_JAR_ENTITY.get(),
            FilledBiomeJarEntity.createFilledBiomeJarEntityAttributes().build());
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.EMPTY_BIOME_JAR_ITEM.get());
            event.accept(ModItems.FILLED_BIOME_JAR_ITEM.get());
        }
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.EMPTY_BIOME_JAR_ENTITY.get(), EmptyBiomeJarEntityRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.FILLED_BIOME_JAR_ENTITY.get(), FilledBiomeJarEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeServer(),
            new DatapackBuiltinEntriesProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(),
                new RegistrySetBuilder().add(Registries.BIOME, ModBiomes::register), Set.of(BiomesInJars.MOD_ID)));
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EmptyBiomeJarEntityRenderer.LAYER_LOCATION,
            EmptyBiomeJarEntityModel::createBodyLayer);
        event.registerLayerDefinition(FilledBiomeJarEntityRenderer.LAYER_LOCATION,
            FilledBiomeJarEntityModel::createBodyLayer);
    }

}
