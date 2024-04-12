package electricsteve.electricutils.Commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import electricsteve.electricutils.AdminUtils.PunishmentReason;
import electricsteve.electricutils.data.PunishmentReasonsSystem;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.UUID;

public class PunishmentArgumentType implements ArgumentType<PunishmentReason> {
    public static final SimpleCommandExceptionType INCOMPLETE_PUNISHMENT_REASON_EXCEPTION = new SimpleCommandExceptionType(Text.literal("Incomplete"));
    public static final SimpleCommandExceptionType INVALID_PUNISHMENT_REASON_EXCEPTION = new SimpleCommandExceptionType(Text.literal("Invalid Punishment Reason: "));

    public static PunishmentArgumentType punishmentReason() {
        return new PunishmentArgumentType();
    }

    public static PunishmentReason getPunishmentReason(final CommandContext<FabricClientCommandSource> context, final String name) {
        return context.getArgument(name, PunishmentReason.class);
    }

    @Override
    public PunishmentReason parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }
        while (reader.canRead() && Character.isLetter(reader.peek())) {
            reader.skip();
        }
        String punishmentReasonString = reader.getString().substring(argBeginning, reader.getCursor());
        PunishmentReason pr = PunishmentReasonsSystem.getInstance().getPunishmentReason(punishmentReasonString);
        if (pr == null) {
            throw INVALID_PUNISHMENT_REASON_EXCEPTION.createWithContext(reader);
        }
        return pr;
    }

    @Override
    public Collection<String> getExamples() {
        return PunishmentReasonsSystem.getInstance().getPunishmentReasonNames();
    }

    public static final class PunishmentReasonArgument {

    }
}
