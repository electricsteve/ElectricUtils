package electricsteve.electricutils;

import electricsteve.electricutils.Config.ModConfig;
import net.fabricmc.api.ClientModInitializer;

public class ElectricUtilsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModConfig.HANDLER.load();
	}
}