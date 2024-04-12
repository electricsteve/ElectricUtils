package electricsteve.electricutils;

import electricsteve.electricutils.Commands.arguments.PunishmentArgumentType;
import electricsteve.electricutils.Config.ModConfig;
import electricsteve.electricutils.Registries.ModCommands;
import electricsteve.electricutils.data.PunishmentReasonsSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class ElectricUtilsClient implements ClientModInitializer {
	public static final String MOD_ID = "ElectricUtils";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);
	public static PunishmentReasonsSystem PUNISHMENT_REASONS_SYSTEM;

	@Override
	public void onInitializeClient() {
		ModConfig.HANDLER.load();
		ModCommands.registerCommands();
		PUNISHMENT_REASONS_SYSTEM = new PunishmentReasonsSystem();
		ArgumentTypeRegistry.registerArgumentType(new Identifier("electricutils", "punishmentreason"), PunishmentArgumentType.class, ConstantArgumentSerializer.of(PunishmentArgumentType::punishmentReason));
	}
}