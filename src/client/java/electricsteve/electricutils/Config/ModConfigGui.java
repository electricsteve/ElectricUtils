package electricsteve.electricutils.Config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ModConfigGui {
    public static ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> YetAnotherConfigLib.create(ModConfig.HANDLER, (defaults, config, builder) -> builder
                        .title(Text.literal("HEHEHEHA"))
                        .category(ConfigCategory.createBuilder()
                                .name(Text.literal("Name of the category"))
                                .tooltip(Text.literal("This is the TOOLTIP!"))
                                .group(ListOption.<String>createBuilder()
                                        .name(Text.literal("Name of the group"))
                                        .description(OptionDescription.of(Text.literal("A description.")))
                                        .binding(
                                                defaults.stringList,
                                                () -> config.stringList,
                                                val -> config.stringList = val
                                        )
                                        .controller(StringControllerBuilder::create)
                                        .initial("")
                                        .minimumNumberOfEntries(0)
                                        .maximumNumberOfEntries(5)
                                        .insertEntriesAtEnd(true)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.literal("Test Group of Options"))
                                        .option(Option.<Vec3d>createBuilder()
                                                .name(Text.literal("Test Option"))
                                                .binding(
                                                        defaults.vec3d,
                                                        () -> config.vec3d,
                                                        val -> config.vec3d = val
                                                )
                                                .controller(Vec3dControllerBuilder::create)
                                                .build())
                                        .build())
                                .build())
                )
                .generateScreen(parent);
    }
}
