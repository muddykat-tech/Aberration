package tech.muddykat.aberration.common.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.function.Consumer;

public class AberrationEffect extends MobEffect {

    public AberrationEffect(MobEffectCategory category, int color) {
        super(category, color);
        // Apply some base attributes that this effect will modify
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "7107DE5E-7CE8-4030-940E-514C1F160890",
                -0.15F, // 15% movement speed reduction
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // This is where the actual effect happens when it ticks

        // Create damage based on the amplifier level
        float damage = 1.0F + (0.5F * amplifier); // Scales with level

        // Apply damage to the entity
        entity.hurt(entity.damageSources().magic(), damage);

        // Only run visual effects on the server to be sent to clients
        if (entity.level() instanceof ServerLevel serverLevel) {
            // Spawn particles based on the amplifier
            int particleCount = 3 + (2 * amplifier); // More particles at higher levels

            // Create particle effects
            for (int i = 0; i < particleCount; i++) {
                double offsetX = entity.getRandom().nextGaussian() * 0.2;
                double offsetY = entity.getRandom().nextGaussian() * 0.2;
                double offsetZ = entity.getRandom().nextGaussian() * 0.2;

                Vec3 position = entity.position().add(offsetX, 1.0 + offsetY, offsetZ);

                // Purple particles swirling around the entity
                serverLevel.sendParticles(
                        ParticleTypes.PORTAL, // You could replace with custom particles later
                        position.x,
                        position.y,
                        position.z,
                        1, // particle count per packet
                        0, 0, 0, // no additional motion
                        0.1 // speed
                );
            }
        }

        // Additional effects that increase with amplifier levels
        if (amplifier >= 1) {
            // Level 2+: Reduce attack speed
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, amplifier - 1));
        }

        if (amplifier >= 2) {
            // Level 3+: Make vision darker
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
        }

        if (amplifier >= 3) {
            // Level 4+: Add nausea
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0));
        }

        if (amplifier >= 4) {
            // Level 5: Add withering effect
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 40, 0));
        }

        super.applyEffectTick(entity, amplifier);
    }

    @Override
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        // Register client-side extensions for rendering
        consumer.accept(new IClientMobEffectExtensions() {
            @Override
            public boolean isVisibleInInventory(MobEffectInstance instance) {
                return true;
            }

            @Override
            public boolean isVisibleInGui(MobEffectInstance instance) {
                return true;
            }

            @Override
            public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha) {
                return false;
            }

            @Override
            public boolean renderInventoryText(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, GuiGraphics guiGraphics, int x, int y, int blitOffset) {
                return false;
            }
        });
    }
}
