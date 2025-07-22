package de.keksuccino.biomesinjars.item;

import de.keksuccino.biomesinjars.entity.entities.biomejar.empty.EmptyBiomeJarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmptyBiomeJarItem extends Item {

    public EmptyBiomeJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
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
        EmptyBiomeJarEntity.spawnAt((ServerLevel) level, blockPos2, 0.0F, true);
        itemStack.shrink(1);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
        @NotNull InteractionHand interactionHand
    ) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
        @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag
    ) {
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.translatable("item.biomesinjars.empty_biome_jar.tooltip.line_1"));
        tooltipComponents.add(Component.translatable("item.biomesinjars.empty_biome_jar.tooltip.line_2"));
        tooltipComponents.add(Component.translatable("item.biomesinjars.empty_biome_jar.tooltip.line_3"));
        tooltipComponents.add(Component.translatable("item.biomesinjars.empty_biome_jar.tooltip.line_4"));
        tooltipComponents.add(Component.translatable("item.biomesinjars.empty_biome_jar.tooltip.line_5"));
    }

}
