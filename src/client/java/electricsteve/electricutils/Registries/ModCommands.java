package electricsteve.electricutils.Registries;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.xpple.clientarguments.arguments.CEntityArgumentType;
import electricsteve.electricutils.AdminUtils.PunishmentReason;
import electricsteve.electricutils.Commands.arguments.PunishmentArgumentType;
import electricsteve.electricutils.ElectricUtilsClient;
import electricsteve.electricutils.data.PunishmentReasonsSystem;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.io.IOException;

public class ModCommands {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("punish_test")
                        .then(ClientCommandManager.argument("player", CEntityArgumentType.player())
                                .then(ClientCommandManager.argument("reason", PunishmentArgumentType.punishmentReason()).executes(context -> {
                                    final PunishmentReason punishmentReason = PunishmentArgumentType.getPunishmentReason(context, "reason");
                                    final PlayerEntity player = CEntityArgumentType.getCPlayer(context, "player");
                                    MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("kick" + player.getName());
                                    return 1;
                                }
                )))));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("electricutils")
                        .then(ClientCommandManager.argument("arg1", StringArgumentType.string())
                                .executes(context -> {
                                    final String string = StringArgumentType.getString(context, "arg1");
                                    if (string.equals("reset_punishment_reasons_file")) {
                                        try {
                                            PunishmentReasonsSystem.getInstance().replaceFileWithDefault();
                                        } catch (IOException e) {
                                            ElectricUtilsClient.LOGGER.error("Failed to replace punishment reasons file with default. Error: " + e);
                                        }
                                    }
                                    return 1;
                                }))));

    }
}
