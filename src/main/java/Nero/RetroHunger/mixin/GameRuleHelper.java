package Nero.RetroHunger.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Map;


@Mixin(GameRules.class)
public abstract class GameRuleHelper {

    @Final
    @Shadow private static Map<GameRules.Key<?>, GameRules.Type<?>> GAME_RULE_TYPES;

    @Unique
    private static Map<GameRules.Key<?>, GameRules.Type<?>> shadowGAME_RULE_TYPES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onGameRulesInit(CallbackInfo ci) {
        shadowGAME_RULE_TYPES = GAME_RULE_TYPES;
        shadowGAME_RULE_TYPES.remove(GameRules.RULE_NATURAL_REGENERATION);
    }

    // Теневой метод для перехвата getRule
    @SuppressWarnings("unchecked")
    @Inject(method = "getRule", at = @At("HEAD"), cancellable = true)
    private <T extends GameRules.Value<T>> void onGetRule(GameRules.Key<T> pKey, CallbackInfoReturnable<GameRules.Value<T>> cir) {
        if (pKey == GameRules.RULE_NATURAL_REGENERATION) {
            GameRules.BooleanValue booleanValue = new GameRules.BooleanValue(null, false) {
                @Override
                public boolean get() {
                    return false;
                }

                @Override
                public void set(boolean pValue, @Nullable MinecraftServer pServer) {

                }

                @Override
                public String serialize() {
                    return "false";
                }

                @Override
                public int getCommandResult() {
                    return 0;
                }

                @Override
                protected GameRules.BooleanValue getSelf() {
                    return this;
                }

                @Override
                protected GameRules.BooleanValue copy() {
                    return this;
                }

                @Override
                public void setFrom(GameRules.BooleanValue pValue, @Nullable MinecraftServer pServer) {

                }
            };


            cir.setReturnValue((GameRules.Value<T>) booleanValue);
            cir.cancel();
        }
        cir.getReturnValue();
    }

}