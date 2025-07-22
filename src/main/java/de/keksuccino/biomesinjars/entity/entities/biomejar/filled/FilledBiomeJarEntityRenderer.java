package de.keksuccino.biomesinjars.entity.entities.biomejar.filled;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static de.keksuccino.biomesinjars.BiomesInJars.res;

public class FilledBiomeJarEntityRenderer extends MobRenderer<FilledBiomeJarEntity, FilledBiomeJarEntityModel> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(res("filled_biome_jar"), "main");

    public FilledBiomeJarEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new FilledBiomeJarEntityModel(context.bakeLayer(LAYER_LOCATION)), 0.3f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FilledBiomeJarEntity entity) {
        return res("textures/entity/biome_jar/filled_biome_jar.png");
    }

}
