package Nero.RetroHunger.EventClasses;

import Nero.RetroHunger.RetroHungerMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RetroHungerMod.MOD_ID)
public class PeacefulRegen {

    private static final int REGEN_INTERVAL = 20;
    private static final float REGEN_AMOUNT = 1.0F;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level world = player.level();

        if (event.phase == TickEvent.Phase.END && !world.isClientSide()) {
            if (world.getDifficulty().getId() == 0 && player.tickCount % REGEN_INTERVAL == 0) {
                if (player.getHealth() < player.getMaxHealth()) {
                    player.heal(REGEN_AMOUNT);
                }
            }
        }
    }
}