package Nero.RetroHunger;

import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RetroHungerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HandAnimationOverlay {

    private static final long ANIMATION_DURATION = 200;


    private static long animationStart = 0;
    private static boolean isAnimating = false;

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (isAnimating) {
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
            }
        }
    }


    public static void startAnimation() {
        animationStart = System.currentTimeMillis();
        isAnimating = true;
    }

    public static boolean isAnimating() {
        return isAnimating;
    }
}
