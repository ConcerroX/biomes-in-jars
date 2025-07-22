package de.keksuccino.biomesinjars.item;

import de.keksuccino.biomesinjars.BiomesInJars;
import de.keksuccino.biomesinjars.datacomponent.BiomeData;
import de.keksuccino.biomesinjars.registry.ModBiomes;
import de.keksuccino.biomesinjars.registry.ModDataComponents;
import de.keksuccino.biomesinjars.registry.ModItems;
import de.keksuccino.biomesinjars.util.LevelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilledBiomeJarItem extends Item {

    public FilledBiomeJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockPos blockPos = useOnContext.getClickedPos();
        Direction direction = useOnContext.getClickedFace();
        BlockState blockState = level.getBlockState(blockPos);
        BlockPos blockPos2 =
            blockState.getCollisionShape(level, blockPos).isEmpty() ? blockPos : blockPos.relative(direction);
        if (onUse((ServerLevel) level, useOnContext.getPlayer(), itemStack, blockPos2)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
        @NotNull InteractionHand interactionHand
    ) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemStack);
        }
        if (onUse((ServerLevel) level, player, itemStack,
            BlockPos.containing(player.position().x, player.position().y, player.position().z))) {
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    private boolean onUse(ServerLevel level, Player player, ItemStack itemStack, BlockPos blockPos) {
        if (!LevelUtils.chunkContainsBiome(level.registryAccess(), level.getChunk(blockPos),
            ModBiomes.DEAD_LAND) || BiomesInJars.config.getOrDefault("allow_overriding_dead_land", false)) {
            ResourceKey<Biome> biome = getBiomeOfStack(itemStack);
            if (biome == null) {
                biome = ResourceKey.create(Registries.BIOME, net.minecraft.world.level.biome.Biomes.PLAINS.location());
            }
            if (setChunkBiome(level, blockPos, biome)) {
                player.onEquippedItemBroken(itemStack.getItem(), EquipmentSlot.MAINHAND);
                level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                    SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }

    private static boolean setChunkBiome(ServerLevel level, BlockPos pos, ResourceKey<Biome> biome) {
        try {
            if (LevelUtils.setChunkBiomeAtBlockPos(level, pos, biome)) {
                level.getServer().getPlayerList().getPlayers()
                    .forEach(player -> ChunkMap.markChunkPendingToSend(player, level.getChunkAt(pos)));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    private static ResourceKey<Biome> getBiomeOfStack(ItemStack stack) {
        try {
            if (stack.has(ModDataComponents.BIOME_DATA)) {
                var biomeData = stack.get(ModDataComponents.BIOME_DATA);
                return biomeData.biome();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        ResourceKey<Biome> biome = getBiomeOfStack(itemStack);
        if (biome != null) {
            String biomeNamespace = biome.location().getNamespace();
            String biomePath = biome.location().getPath();
            return Component.translatable("item.biomesinjars.filled_biome_jar",
                Component.translatable("biome." + biomeNamespace + "." + biomePath));
        }
        return Component.translatable("item.biomesinjars.filled_biome_jar.generic");
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
        @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag
    ) {
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.translatable("item.biomesinjars.filled_biome_jar.tooltip.line_1"));
        tooltipComponents.add(Component.translatable("item.biomesinjars.filled_biome_jar.tooltip.line_2"));
        tooltipComponents.add(Component.translatable("item.biomesinjars.filled_biome_jar.tooltip.line_3"));
        tooltipComponents.add(Component.translatable("item.biomesinjars.filled_biome_jar.tooltip.line_4"));
        tooltipComponents.add(Component.translatable("item.biomesinjars.filled_biome_jar.tooltip.line_5"));
    }

    public static ItemStack createStack(ResourceKey<Biome> biome) {
        ItemStack s = new ItemStack(ModItems.FILLED_BIOME_JAR_ITEM.get());
        s.set(ModDataComponents.BIOME_DATA, new BiomeData(biome));
        return s;
    }

}
