package Nero.RetroHunger;

import Nero.RetroHunger.EventClasses.FoodHandler;
import Nero.RetroHunger.EventClasses.PeacefulRegen;
import net.minecraftforge.common.MinecraftForge;
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
        MinecraftForge.EVENT_BUS.register(FoodHandler.class);
        MinecraftForge.EVENT_BUS.register(PeacefulRegen.class);
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



}
