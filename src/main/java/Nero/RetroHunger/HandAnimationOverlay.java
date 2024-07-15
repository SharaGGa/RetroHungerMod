package Nero.RetroHunger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RetroHungerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HandAnimationOverlay {

    private static final long ANIMATION_DURATION = 200;

    private static long animationStart = 0;
    private static boolean isAnimating = false;
    private static int animationPlayerId = -1;

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (isAnimating && animationPlayerId == Minecraft.getInstance().player.getId()) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - animationStart;

            if (elapsedTime < ANIMATION_DURATION) {
                float progress = (float) elapsedTime / ANIMATION_DURATION;
                float offset;

                if (progress <= 0.5) {
                    offset = progress * 2 * -0.5f;
                } else {
                    offset = (1 - (progress - 0.5f) * 2) * -0.5f;
                }

                event.getPoseStack().translate(0, offset, 0);
            } else {
                isAnimating = false;
                animationPlayerId = -1;
            }
        }
    }

    public static void startAnimation(int playerId) {
        animationPlayerId = playerId;
        animationStart = System.currentTimeMillis();
        isAnimating = true;
    }

    public static boolean isAnimating() {
        return isAnimating;
    }
}
