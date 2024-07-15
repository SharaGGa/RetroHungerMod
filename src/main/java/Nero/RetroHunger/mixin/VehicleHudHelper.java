package Nero.RetroHunger.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ForgeGui.class)
public class VehicleHudHelper {

    @Unique
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("nohung","textures/gui/icons.png");

    private int rightHeight = 39;

    @Inject(method = "renderHealthMount", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderVehicle(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = (Player) minecraft.getCameraEntity();
        Entity tmp = player.getVehicle();

        if (tmp instanceof LivingEntity) {
            boolean unused = false;
            int left_align = width / 2 + 91;
            RenderSystem.enableBlend();
            LivingEntity mount = (LivingEntity)tmp;
            int health = (int)Math.ceil((double)mount.getHealth());
            float healthMax = mount.getMaxHealth();
            int hearts = (int)(healthMax + 0.5F) / 2;
            if (hearts > 30) {
                hearts = 30;
            }

            boolean MARGIN = true;
            int BACKGROUND = 52 + (unused ? 1 : 0);
            boolean HALF = true;
            boolean FULL = true;

            for(int heart = 0; hearts > 0; heart += 20) {
                int top = height - this.rightHeight;
                int rowCount = Math.min(hearts, 10);
                hearts -= rowCount;

                for(int i = 0; i < rowCount; ++i) {
                    int x = left_align - i * 8 - 9;
                    guiGraphics.blit(GUI_ICONS_LOCATION, x, top - 10, BACKGROUND, 9, 9, 9);
                    if (i * 2 + 1 + heart < health) {
                        guiGraphics.blit(GUI_ICONS_LOCATION, x, top - 10, 88, 9, 9, 9);
                    } else if (i * 2 + 1 + heart == health) {
                        guiGraphics.blit(GUI_ICONS_LOCATION, x, top - 10, 97, 9, 9, 9);
                    }
                }

                this.rightHeight += 10;
            }


        }

        ci.cancel();
    }
}
