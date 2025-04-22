package tech.muddykat.aberration.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import tech.muddykat.aberration.Aberration;
import tech.muddykat.aberration.common.effect.AberrationEffect;

import java.util.function.Supplier;

public class AberrationRegistry {

    /**
     * Initialize all registries
     */
    public static void init(IEventBus modEventBus) {
        Items.ITEMS.register(modEventBus);
        Blocks.BLOCKS.register(modEventBus);
        BlockEntities.BLOCK_ENTITIES.register(modEventBus);
        Entities.ENTITIES.register(modEventBus);
        MobEffects.MOB_EFFECTS.register(modEventBus);
    }

    public static class Items {
        // Item Registry
        public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Aberration.MODID);

        // Default item properties
        private static final Item.Properties DEFAULT_PROPERTIES = new Item.Properties();

        public static final Supplier<Item> ABERRANT_CRYSTAL = ITEMS.registerSimpleItem(
                "aberrant_crystal",
                new Item.Properties() // The properties to use.
        );

        public static final Supplier<Item> BASIC_POPPET = ITEMS.registerSimpleItem(
                "basic_poppet",
                new Item.Properties() // The properties to use.
        );
    }

    /**
     * Block Registry Handler
     */
    public static class Blocks {
        // Block Registry
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Aberration.MODID);

        // Example block registration
        // public static final DeferredRegister.RegisterEvent<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block",
        //     () -> new Block(Block.Properties.of().mapColor(MapColor.STONE)));
    }

    /**
     * Block Entity Registry Handler
     */
    public static class BlockEntities {
        // Block Entity Registry
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
                DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Aberration.MODID);

        // Example block entity registration
        // public static final DeferredRegister.RegisterEvent<BlockEntityType<?>> EXAMPLE_BLOCK_ENTITY =
        //     BLOCK_ENTITIES.register("example_block_entity",
        //         () -> BlockEntityType.Builder.of(ExampleBlockEntity::new,
        //             Blocks.EXAMPLE_BLOCK.get()).build(null));
    }

    /**
     * Entity Registry Handler (includes Mobs)
     */
    public static class Entities {
        // Entity Registry
        public static final DeferredRegister<EntityType<?>> ENTITIES =
                DeferredRegister.create(Registries.ENTITY_TYPE, Aberration.MODID);

        // Example mob entity registration
        // public static final DeferredRegister.RegisterEvent<EntityType<?>> EXAMPLE_MOB =
        //     ENTITIES.register("example_mob",
        //         () -> EntityType.Builder.<ExampleMob>of(ExampleMob::new, MobCategory.MONSTER)
        //             .sized(0.6F, 1.8F)
        //             .clientTrackingRange(8)
        //             .build("example_mob"));
    }

    /**
     * Mob Effects Registry Handler
     */
    public static class MobEffects {
        // Mob Effect Registry
        public static final DeferredRegister<MobEffect> MOB_EFFECTS =
                DeferredRegister.create(Registries.MOB_EFFECT, Aberration.MODID);

        // Our custom aberration effect
        public static final Supplier<MobEffect> ABERRATION = MOB_EFFECTS.register("aberration",
                () -> new AberrationEffect(MobEffectCategory.HARMFUL, 0x800080) // Purple color
        );
    }
}
