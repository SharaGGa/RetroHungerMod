package Nero.RetroHunger;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;


@Mod("nohung")
public class RetroHungerMod {
    public static final String MOD_ID = "nohung";

    public RetroHungerMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Регистрация обработчиков событий
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Код инициализации
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Межмодовая коммуникация
    }

    private void processIMC(final InterModProcessEvent event) {
        // Обработка межмодовой коммуникации
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventHandler {
        private static void applyFoodEffects(Player player, FoodProperties food) {
            List<Pair<MobEffectInstance, Float>> effects = food.getEffects();
            for (Pair<MobEffectInstance, Float> pair : effects) {
                if (player.level().random.nextFloat() < pair.getSecond()) {
                    player.addEffect(new MobEffectInstance(pair.getFirst()));
                }
            }
        }


        @SubscribeEvent
        public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
            Player player = event.getEntity();
            ItemStack heldItem = player.getMainHandItem();

            if (heldItem.isEdible()) {
                Item item = heldItem.getItem();
                FoodProperties food = item.getFoodProperties();

                if (food != null) {
                    int nutrition = food.getNutrition();

                    event.setCanceled(true);

                    if (!player.isCreative()) {
                        player.startUsingItem(event.getHand());
                        ItemStack foodStack = heldItem.copy();
                        foodStack.setCount(1);
                        LivingEntityUseItemEvent.Finish foodEvent = new LivingEntityUseItemEvent.Finish(player, foodStack, food.getNutrition(), foodStack);
                        MinecraftForge.EVENT_BUS.post(foodEvent);
                        HandAnimationOverlay.startAnimation(player.getId());
                        player.heal(nutrition);
                        applyFoodEffects(player, food);
                        player.stopUsingItem();
                    }
                    if (!player.getAbilities().instabuild) {
                        heldItem.shrink(1);
                    }
                }
            }
        }
    }

}
