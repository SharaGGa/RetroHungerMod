package Nero.RetroHunger.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class HungerSystemHelper {

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;tick(Lnet/minecraft/world/entity/player/Player;)V", shift = At.Shift.AFTER))
    private void onFoodDataTick(CallbackInfo ci) {

        Player player = (Player) (Object) this;

        player.getFoodData().setFoodLevel(20);

        player.getFoodData().setSaturation(20.0f);
    }
}