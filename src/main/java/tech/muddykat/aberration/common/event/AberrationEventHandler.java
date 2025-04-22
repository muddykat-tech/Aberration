package tech.muddykat.aberration.common.event;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import tech.muddykat.aberration.Aberration;
import tech.muddykat.aberration.registration.AberrationRegistry;

import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = Aberration.MODID)
public class AberrationEventHandler {

    private static WeakHashMap<LivingEntity, Integer> ABERRATION_EFFECT_MAP = new WeakHashMap<>();

    @SubscribeEvent
    public static void onEffectApplied(MobEffectEvent.Added event)
    {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();

        // Check if the expiring effect is our aberration effect
        MobEffect aberration_effect = AberrationRegistry.MobEffects.ABERRATION.get();
        if (instance != null && instance.getEffect() == aberration_effect) {
            //if(entity.hasEffect(aberration_effect)) entity.removeEffectNoUpdate(aberration_effect);
            int currentDuration = instance.getDuration();
            ABERRATION_EFFECT_MAP.put(entity, currentDuration);
        }
    }

    @SubscribeEvent
    public static void onEffectExpiring(MobEffectEvent.Expired event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        MobEffectInstance instance = event.getEffectInstance();

        // Check if the expiring effect is our aberration effect
        if (instance != null && instance.getEffect() == AberrationRegistry.MobEffects.ABERRATION.get()) {
            int currentAmplifier = instance.getAmplifier();

            // If the effect has an amplifier > 0, we want to degrade it
            if (currentAmplifier > 0) {
                // Calculate the new duration (150% of the original)
                int originalDuration = ABERRATION_EFFECT_MAP.getOrDefault(entity, 20);
                int newDuration = (int)(originalDuration * 2);

                // Add the effect at one level lower, with 150% of the original duration
                MobEffectInstance newInstance = new MobEffectInstance(
                        AberrationRegistry.MobEffects.ABERRATION.get(),
                        newDuration,
                        currentAmplifier - 1,
                        instance.isAmbient(),
                        instance.isVisible(),
                        instance.showIcon()
                );

                entity.removeEffectNoUpdate(AberrationRegistry.MobEffects.ABERRATION.get());
                entity.addEffect(newInstance);
                ABERRATION_EFFECT_MAP.remove(entity);
            }
        }
    }
}
