package Nero.RetroHunger;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

import com.mojang.datafixers.util.Pair;

@Mod.EventBusSubscriber(modid = RetroHungerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FoodHandler {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack mainHandItem = player.getMainHandItem();

        if (event.getHand() == InteractionHand.OFF_HAND) {
            if (isEdibleFood(mainHandItem)) {
                event.setCanceled(true);
                return;
            }

        }

        if (isEdibleFood(mainHandItem) || canAlwaysEat(mainHandItem)) {
            if(!player.isCreative()) {
                if (mainHandItem.getItem() instanceof ChorusFruitItem) {
                    event.setCanceled(true);
                    chorusTeleport(player);
                } else if (canAlwaysEat(mainHandItem) || !player.getFoodData().needsFood()) {
                    consumeFood(player, mainHandItem);
                    event.setCanceled(true);
                }
            } else
                event.setCanceled(true);
        }


    }

    private static boolean isEdibleFood(ItemStack itemStack) {
        return itemStack.isEdible() && itemStack.getItem().getFoodProperties() != null;
    }
    private static boolean canAlwaysEat(ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.getItem().getFoodProperties()).canAlwaysEat();
    }

    private static void consumeFood(Player player, ItemStack foodItem) {
        if (!player.level().isClientSide) {
            FoodProperties foodProperties = foodItem.getItem().getFoodProperties();
            if (foodProperties != null) {
                int nutrition = foodProperties.getNutrition();

                player.heal(nutrition);
                applyFoodEffects(player, foodProperties);


                if (!player.getAbilities().instabuild) {
                    foodItem.shrink(1);

                }

            }
        }
    }

    private static void applyFoodEffects(Player player, FoodProperties food) {
        List<Pair<MobEffectInstance, Float>> effects = food.getEffects();
        for (Pair<MobEffectInstance, Float> pair : effects) {
            if (player.level().random.nextFloat() < pair.getSecond()) {
                player.addEffect(new MobEffectInstance(pair.getFirst()));
            }
        }
    }
    private static void chorusTeleport(Player player) {
        if (!player.level().isClientSide) {
            ItemStack itemStack = player.getMainHandItem();
            if (!(itemStack.getItem() instanceof ChorusFruitItem chorusFruitItem)) {
                return;
            }

            double d0 = player.getX();
            double d1 = player.getY();
            double d2 = player.getZ();

            for(int i = 0; i < 16; ++i) {
                double d3 = player.getX() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                double d4 = Math.min(player.level().getMaxBuildHeight(), Math.max(player.level().getMinBuildHeight(), player.getY() + (double)(player.getRandom().nextInt(16) - 8)));
                double d5 = player.getZ() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;

                if (player.isPassenger()) {
                    player.stopRiding();
                }

                EntityTeleportEvent.ChorusFruit event = new EntityTeleportEvent.ChorusFruit(player, d3, d4, d5);
                if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
                    if (player.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
                        player.level().playSound(null, d0, d1, d2, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

                        player.getCooldowns().addCooldown(chorusFruitItem, 20);

                        if (!player.getAbilities().instabuild) {
                            itemStack.shrink(1);
                        }
                        break;
                    }
                }
            }
        }
    }
}