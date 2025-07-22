package de.keksuccino.biomesinjars.entity.entities.biomejar.empty;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static de.keksuccino.biomesinjars.BiomesInJars.res;

public class EmptyBiomeJarEntityRenderer extends MobRenderer<EmptyBiomeJarEntity, EmptyBiomeJarEntityModel> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(res("empty_biome_jar"), "main");

    public EmptyBiomeJarEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new EmptyBiomeJarEntityModel(context.bakeLayer(LAYER_LOCATION)), 0.3f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EmptyBiomeJarEntity entity) {
        return res("textures/entity/biome_jar/empty_biome_jar.png");
    }

}
