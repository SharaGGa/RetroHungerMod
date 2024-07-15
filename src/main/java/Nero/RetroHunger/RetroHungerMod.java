package Nero.RetroHunger;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


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

                    if (!player.isCreative())
                        HandAnimationOverlay.startAnimation();

                    player.heal(nutrition);

                    if (!player.getAbilities().instabuild) {
                        heldItem.shrink(1);
                    }
                }
            }
        }
    }
}