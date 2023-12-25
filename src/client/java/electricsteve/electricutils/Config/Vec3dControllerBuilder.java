package electricsteve.electricutils.Config;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import net.minecraft.util.math.Vec3d;

public interface Vec3dControllerBuilder extends ControllerBuilder<Vec3d> {
    static Vec3dControllerBuilder create(Option<Vec3d> option) {
        return new Vec3dControllerBuilderImpl(option);
    }
}
