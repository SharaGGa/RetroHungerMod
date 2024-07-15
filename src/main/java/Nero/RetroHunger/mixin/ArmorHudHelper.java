package Nero.RetroHunger.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public abstract class ArmorHudHelper {


    @Unique
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("minecraft","textures/gui/icons.png");


    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true, remap = false)
    private void injectNewArmorRendering(GuiGraphics guiGraphics, int width, int height, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        int level = player.getArmorValue();
        int left = width / 2 + 82;

        boolean isRiding = player.getVehicle() != null;
        int top = height - 49 + (isRiding ? 0 : 10);

        for (int i = 1; level > 0 && i < 20; i += 2) {
            if (i <= level) {
                guiGraphics.blit(GUI_ICONS_LOCATION, left, top, 34, 9, 9, 9);
            } else if (i == level + 1) {
                guiGraphics.blit(GUI_ICONS_LOCATION, left, top, 25, 9, 9, 9);
            } else {
                guiGraphics.blit(GUI_ICONS_LOCATION, left, top, 16, 9, 9, 9);
            }
            left -= 8;
        }
        ci.cancel();
    }

}