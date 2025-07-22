package de.keksuccino.biomesinjars.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public record BiomeData(ResourceKey<Biome> biome) {

    public static final Codec<BiomeData> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(ResourceKey.codec(Registries.BIOME).fieldOf("biome").forGetter(BiomeData::biome))
            .apply(instance, BiomeData::new));

    public static final StreamCodec<ByteBuf, BiomeData> STREAM_CODEC = StreamCodec.composite(
        ResourceKey.streamCodec(Registries.BIOME), BiomeData::biome, BiomeData::new);

}
