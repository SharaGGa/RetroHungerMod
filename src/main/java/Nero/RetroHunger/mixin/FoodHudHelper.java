package Nero.RetroHunger.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public class FoodHudHelper {

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderFood(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {

        ci.cancel();

    }

}
