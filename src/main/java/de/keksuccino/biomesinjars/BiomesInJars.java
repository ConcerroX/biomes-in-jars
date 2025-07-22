package de.keksuccino.biomesinjars;

import com.mojang.logging.LogUtils;
import de.keksuccino.biomesinjars.registry.ModBiomes;
import de.keksuccino.biomesinjars.registry.ModEntityTypes;
import de.keksuccino.biomesinjars.registry.ModItems;
import de.keksuccino.biomesinjars.registry.ModDataComponents;
import de.keksuccino.konkrete.config.Config;
import de.keksuccino.konkrete.config.exceptions.InvalidValueException;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

@Mod(BiomesInJars.MOD_ID)
public class BiomesInJars {

    public static final String MOD_ID = "biomesinjars";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Config config;

    public BiomesInJars(IEventBus modEventBus) {
        setupModConfig();

        ModItems.ITEMS.register(modEventBus);
        ModBiomes.BIOMES.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
    }

    public static void setupModConfig() {
        try {
            config = new Config(FMLPaths.CONFIGDIR.get().toString() + "/biomesinjars.kfg");
            config.registerValue("convert_to_dead_land", true, "general",
                "If chunks around the empty jar should get converted to dead land when extracting a biome.");
            config.registerValue("filled_jar_uses", 250, "general");
            config.registerValue("allow_overriding_dead_land", false, "general",
                "If it should be possible to override dead land biomes with filled jars.");
            config.syncConfig();
            config.clearUnusedValues();
        } catch (InvalidValueException e) {
            LOGGER.error("Failed to load config: {}", e.toString());
        }
    }

    public static ResourceLocation res(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
