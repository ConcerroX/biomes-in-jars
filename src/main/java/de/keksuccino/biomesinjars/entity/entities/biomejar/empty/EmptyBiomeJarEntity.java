package de.keksuccino.biomesinjars.entity.entities.biomejar.empty;

import de.keksuccino.biomesinjars.BiomesInJars;
import de.keksuccino.biomesinjars.registry.ModBiomes;
import de.keksuccino.biomesinjars.registry.ModEntityTypes;
import de.keksuccino.biomesinjars.entity.entities.biomejar.filled.FilledBiomeJarEntity;
import de.keksuccino.biomesinjars.registry.ModItems;
import de.keksuccino.biomesinjars.util.ItemUtils;
import de.keksuccino.biomesinjars.util.LevelUtils;
import de.keksuccino.konkrete.math.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class EmptyBiomeJarEntity extends Mob {

    public AnimationState rotationAnimationState = new AnimationState();
    public AnimationState hoverAnimationState = new AnimationState();
    public AnimationState vibrateAnimationState = new AnimationState();

    protected boolean isTicking = true;
    protected boolean vibratingSoundPlayed = false;
    protected int vibratingParticleLastTick = -1000;

    public EmptyBiomeJarEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createEmptyBiomeJarEntityAttributes() {
        return createMobAttributes();
    }

    @Override
    public void push(double d, double e, double f) {
        // no pushing! (> - <)
    }

    @Override
    public void knockback(double d, double e, double f) {
        // no knocking!!!!
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float f) {
        // no hurting! (._.)
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public void tick() {
        Holder<Biome> biomeHolder = level().getBiome(this.blockPosition());
        if (!biomeHolder.is(ModBiomes.DEAD_LAND)) {
            if (level().isClientSide) {
                this.hoverAnimationState.startIfStopped(this.tickCount);
                this.rotationAnimationState.startIfStopped(this.tickCount);
                if (this.tickCount >= 200) {
                    this.vibrateAnimationState.startIfStopped(this.tickCount);
                    if (this.vibratingParticleLastTick <= (this.tickCount + 10)) {
                        this.vibratingParticleLastTick = this.tickCount;
                        level().addParticle(ParticleTypes.DRAGON_BREATH, this.getX() + MathUtils.getRandomNumberInRange(-2, 2), this.getY() + MathUtils.getRandomNumberInRange(-2, 2), this.getZ() + MathUtils.getRandomNumberInRange(-2, 2), 0.0, 0.0, 0.0);
                    }
                }
            } else {
                if (isTicking) {
                    if (this.tickCount >= 200) {
                        if (!this.vibratingSoundPlayed) {
                            level().playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.ENDER_DRAGON_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
                            this.vibratingSoundPlayed = true;
                        }
                    }
                    if (this.tickCount >= 350) {
                        FilledBiomeJarEntity.spawnAt((ServerLevel) level(), this.position(), this.getXRot(), biomeHolder.unwrapKey().get(), true);
                        if (BiomesInJars.config.getOrDefault("convert_to_dead_land", true)) {
                            setDeadLandBiomeAt(this.blockPosition());
                            setDeadLandBiomeAt(this.blockPosition().east(16));
                            setDeadLandBiomeAt(this.blockPosition().west(16));
                            setDeadLandBiomeAt(this.blockPosition().north(16));
                            setDeadLandBiomeAt(this.blockPosition().south(16));
                            setDeadLandBiomeAt(this.blockPosition().north(16).west(16));
                            setDeadLandBiomeAt(this.blockPosition().north(16).east(16));
                            setDeadLandBiomeAt(this.blockPosition().south(16).west(16));
                            setDeadLandBiomeAt(this.blockPosition().south(16).east(16));
                        }
                        level().playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.0F);
                        this.kill();
                        this.remove(RemovalReason.KILLED);
                        this.isTicking = false;
                    }
                }
            }
        }
        super.tick();
    }

    protected void setDeadLandBiomeAt(BlockPos blockPos) {
        LevelUtils.setChunkBiomeAtBlockPos(level(), blockPos, ModBiomes.DEAD_LAND);
        level().getServer().getPlayerList().getPlayers().forEach((player) -> {
            try {
                ((ChunkMap)((ServerLevel)level()).getChunkSource().chunkMap).markChunkPendingToSend(player, level().getChunkAt(blockPos));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {
        return false;
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!level().isClientSide) {
            this.kill();
            this.remove(RemovalReason.KILLED);
            level().playSound(null, this.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
            int slot = player.getInventory().getFreeSlot();
            if (slot != -1) {
                player.getInventory().setItem(slot, new ItemStack(ModItems.EMPTY_BIOME_JAR_ITEM.get()));
            } else {
                ItemUtils.dropItemStack(level(), new ItemStack(ModItems.EMPTY_BIOME_JAR_ITEM.get()), this.blockPosition());
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    public static void spawnAt(ServerLevel serverLevel, BlockPos blockPos, float xRot, boolean b) {
        EmptyBiomeJarEntity entity = ModEntityTypes.EMPTY_BIOME_JAR_ENTITY.get().create(serverLevel);
        if (entity == null) {
            return;
        }
        entity.setPos((double)blockPos.getX() + 0.5, blockPos.getY() + 1, (double)blockPos.getZ() + 0.5);
        double d = getYOffset(serverLevel, blockPos, b, entity.getBoundingBox());
        entity.moveTo((double)blockPos.getX() + 0.5, (double)blockPos.getY() + d, (double)blockPos.getZ() + 0.5, xRot, 0.0f);
        entity.yHeadRot = entity.getYRot();
        entity.yBodyRot = entity.getYRot();
        entity.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWN_EGG, null);
        entity.playAmbientSound();
        EntityType.updateCustomEntityTag(serverLevel, null, entity, CustomData.EMPTY);
        serverLevel.addFreshEntityWithPassengers(entity);
    }

    protected static double getYOffset(LevelReader levelReader, BlockPos blockPos, boolean bl, AABB aABB) {
        AABB aABB2 = new AABB(blockPos);
        if (bl) {
            aABB2 = aABB2.expandTowards(0.0, -1.0, 0.0);
        }
        Iterable<VoxelShape> iterable = levelReader.getCollisions(null, aABB2);
        return 1.0 + Shapes.collide(Direction.Axis.Y, aABB, iterable, bl ? -2.0 : -1.0);
    }

}
