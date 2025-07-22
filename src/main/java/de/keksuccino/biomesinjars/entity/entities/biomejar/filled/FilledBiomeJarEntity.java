package de.keksuccino.biomesinjars.entity.entities.biomejar.filled;

import de.keksuccino.biomesinjars.registry.ModEntityTypes;
import de.keksuccino.biomesinjars.registry.ModItems;
import de.keksuccino.biomesinjars.item.FilledBiomeJarItem;
import de.keksuccino.biomesinjars.util.ItemUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FilledBiomeJarEntity extends Mob {

    private static final EntityDataAccessor<Boolean> DATA_READY_TO_PICKUP = SynchedEntityData.defineId(
        FilledBiomeJarEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SPAWNED_BY_EMPTY_JAR = SynchedEntityData.defineId(
        FilledBiomeJarEntity.class, EntityDataSerializers.BOOLEAN);

    public AnimationState rotationAnimationState = new AnimationState();
    public AnimationState hoverAnimationState = new AnimationState();
    public AnimationState vibrateAnimationState = new AnimationState();
    public AnimationState flyDownAnimationState = new AnimationState();

    public ResourceKey<Biome> biome;

    public FilledBiomeJarEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_READY_TO_PICKUP, false);
        builder.define(DATA_SPAWNED_BY_EMPTY_JAR, false);
    }

    public void setReadyToPickup(boolean b) {
        this.entityData.set(DATA_READY_TO_PICKUP, b);
    }

    public boolean isReadyToPickup() {
        return this.entityData.get(DATA_READY_TO_PICKUP);
    }

    public void setSpawnedByEmptyJar(boolean b) {
        this.entityData.set(DATA_SPAWNED_BY_EMPTY_JAR, b);
    }

    public boolean isSpawnedByEmptyJar() {
        return this.entityData.get(DATA_SPAWNED_BY_EMPTY_JAR);
    }

    public static AttributeSupplier.Builder createFilledBiomeJarEntityAttributes() {
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
    public boolean hurt(DamageSource damageSource, float f) {
        // no hurting! (._.)
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public void tick() {
        if (level().isClientSide) {
            if (!this.isReadyToPickup() && this.isSpawnedByEmptyJar()) {
                this.rotationAnimationState.startIfStopped(this.tickCount);
                if (this.tickCount < 30) {
                    this.hoverAnimationState.startIfStopped(this.tickCount);
                    this.vibrateAnimationState.startIfStopped(this.tickCount);
                } else {
                    this.hoverAnimationState.stop();
                    this.vibrateAnimationState.stop();
                    this.flyDownAnimationState.startIfStopped(this.tickCount);
                }
            } else {
                this.hoverAnimationState.stop();
                this.vibrateAnimationState.stop();
                this.flyDownAnimationState.stop();
                this.rotationAnimationState.stop();
            }
        }
        if (this.tickCount >= 130) {
            if (!this.isReadyToPickup()) {
                this.setReadyToPickup(true);
                if (!level().isClientSide) {
                    level().playSound(null, this.position().x, this.position().y, this.position().z,
                        SoundEvents.ILLUSIONER_PREPARE_MIRROR, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
        super.tick();
    }

    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putString("biomesinjars_biome", this.biome.location().toString());
        compoundTag.putBoolean("biomesinjars_ready_to_pickup", this.isReadyToPickup());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("biomesinjars_biome")) {
            ResourceKey<Biome> biomeResourceKey = null;
            try {
                biomeResourceKey = ResourceKey.create(Registries.BIOME,
                    ResourceLocation.parse(compoundTag.getString("biomesinjars_biome")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (biomeResourceKey != null) {
                this.biome = biomeResourceKey;
            } else {
                this.biome = Biomes.PLAINS;
            }
        }
        if (compoundTag.contains("biomesinjars_ready_to_pickup")) {
            this.setReadyToPickup(compoundTag.getBoolean("biomesinjars_ready_to_pickup"));
        }
    }

    //TODO übernehmen
    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        if (!level().isClientSide) {
            if (this.isReadyToPickup()) {
                this.kill();
                this.remove(RemovalReason.KILLED);
                level().playSound(null, this.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                int slot = player.getInventory().getFreeSlot();
                ItemStack filledJarStack;
                if (this.biome != null) {
                    filledJarStack = FilledBiomeJarItem.createStack(this.biome);
                } else {
                    filledJarStack = new ItemStack(ModItems.FILLED_BIOME_JAR_ITEM.get());
                }
                if (slot != -1) {
                    player.getInventory().setItem(slot, filledJarStack);
                } else {
                    ItemUtils.dropItemStack(level(), filledJarStack, this.blockPosition());
                }
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    public static FilledBiomeJarEntity spawnAt(ServerLevel serverLevel, Vec3 position, float xRot,
        ResourceKey<Biome> biome, boolean spawnedByEmptyJarEntity
    ) {
        FilledBiomeJarEntity entity = ModEntityTypes.FILLED_BIOME_JAR_ENTITY.get().create(serverLevel);
        if (entity == null) {
            return null;
        }
        entity.setSpawnedByEmptyJar(spawnedByEmptyJarEntity);
        entity.biome = biome;
        entity.setPos(position.x, position.y, position.z);
        entity.moveTo(position.x, position.y, position.z, xRot, 0.0f);
        entity.yHeadRot = entity.getYRot();
        entity.yBodyRot = entity.getYRot();
        entity.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(entity.blockPosition()),
            MobSpawnType.SPAWN_EGG, null);
        entity.playAmbientSound();
        EntityType.updateCustomEntityTag(serverLevel, null, entity, CustomData.EMPTY);
        serverLevel.addFreshEntityWithPassengers(entity);
        return entity;
    }

}
