package de.keksuccino.biomesinjars.registry;

import de.keksuccino.biomesinjars.BiomesInJars;
import de.keksuccino.biomesinjars.entity.entities.biomejar.empty.EmptyBiomeJarEntity;
import de.keksuccino.biomesinjars.entity.entities.biomejar.filled.FilledBiomeJarEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static de.keksuccino.biomesinjars.BiomesInJars.res;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE,
        BiomesInJars.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<EmptyBiomeJarEntity>> EMPTY_BIOME_JAR_ENTITY = ENTITY_TYPES.register(
        "empty_biome_jar", () -> EntityType.Builder.of(EmptyBiomeJarEntity::new, MobCategory.MISC).sized(0.4f, 0.4f)
            .build(res("empty_biome_jar").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<FilledBiomeJarEntity>> FILLED_BIOME_JAR_ENTITY = ENTITY_TYPES.register(
        "filled_biome_jar", () -> EntityType.Builder.of(FilledBiomeJarEntity::new, MobCategory.MISC).sized(0.4f, 0.4f)
            .build(res("filled_biome_jar").toString()));

}
