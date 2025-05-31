package fuffles.ichthyology.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLLoader;

public class IchthyologyClient
{
    private static final IchthyologyClient INSTANCE = new IchthyologyClient();

    private IchthyologyClient() { }

    public static IchthyologyClient getInstance()
    {
        return FMLLoader.getDist() == Dist.CLIENT ? IchthyologyClient.INSTANCE : null;
    }

    public void registerEvents(IEventBus modBus, IEventBus forgeBus)
    {
        modBus.addListener(this::onRegisterShaders);
    }

    private void onRegisterShaders(RegisterShadersEvent event)
    {
        //event.registerShader();
    }
}