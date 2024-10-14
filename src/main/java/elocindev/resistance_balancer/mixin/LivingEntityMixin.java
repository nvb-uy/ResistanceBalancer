package elocindev.resistance_balancer.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elocindev.resistance_balancer.config.ResistanceBalancerConfig;
import elocindev.resistance_balancer.math.FormulaParser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "modifyAppliedDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;getAmplifier()I", ordinal = 0), cancellable = true)
    private void injectResistanceCalculation(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.hasStatusEffect(StatusEffects.RESISTANCE) && !source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)) {
            int resistanceLevel = entity.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1;

            float reducedAmount;
            if (ResistanceBalancerConfig.INSTANCE.enable_custom_formula) {
                String formula = ResistanceBalancerConfig.INSTANCE.custom_formula;
                String parsedFormula = formula.replace("RESISTANCE", String.valueOf(resistanceLevel));
                
                float reductionFactor = (float) FormulaParser.evaluateFormula(parsedFormula);
                reducedAmount = amount * (1.0F - reductionFactor);
            } else {
                float fallbackReduction = resistanceLevel * ResistanceBalancerConfig.INSTANCE.percentile_reduction_fallback;
                reducedAmount = amount * (1.0F - fallbackReduction);
            }

            float resistedDamage = amount - reducedAmount;
            if (resistedDamage > 0.0F && resistedDamage < Float.MAX_VALUE) {
                if (entity instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) entity).increaseStat(Stats.DAMAGE_RESISTED, Math.round(resistedDamage * 10.0F));
                } else if (source.getAttacker() instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) source.getAttacker()).increaseStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(resistedDamage * 10.0F));
                }
            }

            cir.setReturnValue(Math.max(reducedAmount, 0.0F));
        }
    }
}
