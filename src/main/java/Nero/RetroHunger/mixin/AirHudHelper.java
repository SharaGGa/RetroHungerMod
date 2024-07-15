package Nero.RetroHunger.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public class AirHudHelper {

    @Unique
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("nohung","textures/gui/icons.png");

    private int rightHeight = 39;

    @Inject(method = "renderAir", at = @At("HEAD"), cancellable = true, remap = false)
    void onRenderAir(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = (Player) minecraft.getCameraEntity();
        if (player == null) return;


        int left = width / 2 - 91;
        int top = height - this.rightHeight;
        int air = player.getAirSupply();

        if (player.isEyeInFluidType((FluidType) ForgeMod.WATER_TYPE.get()) || air < 300) {
            int full = Mth.ceil((double)(air - 2) * 10.0 / 300.0);
            int partial = Mth.ceil((double)air * 10.0 / 300.0) - full;


            for(int i = 0; i < 10; ++i) {
                if (i < full) {
                    guiGraphics.blit(GUI_ICONS_LOCATION, left + i * 8, top, 16, 18, 9, 9);
                } else if (i < full + partial) {
                    guiGraphics.blit(GUI_ICONS_LOCATION, left + i * 8, top, 25, 18, 9, 9);
                }
            }

            this.rightHeight -= 10;
        }
        ci.cancel();
    }
}