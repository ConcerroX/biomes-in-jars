package de.keksuccino.biomesinjars.registry;

import de.keksuccino.biomesinjars.BiomesInJars;
import de.keksuccino.biomesinjars.item.EmptyBiomeJarItem;
import de.keksuccino.biomesinjars.item.FilledBiomeJarItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BiomesInJars.MOD_ID);

    public static final DeferredHolder<Item, Item> EMPTY_BIOME_JAR_ITEM = ITEMS.register("empty_biome_jar",
        () -> new EmptyBiomeJarItem(
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant()));

    public static final DeferredHolder<Item, Item> FILLED_BIOME_JAR_ITEM = ITEMS.register("filled_biome_jar",
        () -> new FilledBiomeJarItem(
            new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant()));

}
