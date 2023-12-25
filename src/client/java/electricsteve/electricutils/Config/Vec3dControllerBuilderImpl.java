package electricsteve.electricutils.Config;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.impl.controller.AbstractControllerBuilderImpl;
import net.minecraft.util.math.Vec3d;

public class Vec3dControllerBuilderImpl extends AbstractControllerBuilderImpl<Vec3d> implements Vec3dControllerBuilder{
    public Vec3dControllerBuilderImpl(Option<Vec3d> option) {
        super(option);
    }

    @Override
    public Controller<Vec3d> build() {
        return new Vec3dController(option);
    }
}
