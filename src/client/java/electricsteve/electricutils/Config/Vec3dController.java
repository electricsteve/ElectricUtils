package electricsteve.electricutils.Config;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ActionController;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import dev.isxander.yacl3.gui.utils.UndoRedoHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

public class Vec3dController implements Controller<Vec3d> {
    private final Option<Vec3d> option;
    private final Text text;

    public Vec3dController(Option<Vec3d> option) {
        this(option, ActionController.DEFAULT_TEXT);
    }

    public Vec3dController(Option<Vec3d> option, Text text) {
        this.option = option;
        this.text = text;
    }

    // Getters
    public double getX() {
        return option().pendingValue().x;
    }
    public double getY() {
        return option().pendingValue().y;
    }
    public double getZ() {
        return option().pendingValue().z;
    }

    @Override
    public Option<Vec3d> option() {
        return option;
    }

    @Override
    public Text formatValue() {
        return text;
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new Vec3dControllerWidget(this, screen, widgetDimension);
    }

    public static class Vec3dControllerWidget extends ControllerWidget<Vec3dController> {

        // Input fields
        protected Double inputFieldX;
        protected Dimension<Integer> inputFieldXBounds;
        protected boolean inputFieldXFocused;
        
        protected Double inputFieldY;
        protected Dimension<Integer> inputFieldYBounds;
        protected boolean inputFieldYFocused;
        
        protected Double inputFieldZ;
        protected Dimension<Integer> inputFieldZBounds;
        protected boolean inputFieldZFocused;
        
        
        protected int caretPos;
        protected int selectionLength;
        protected int renderOffset;

        protected float ticks;

        private final Text emptyText;

        protected boolean hovered1;
        protected boolean hovered2;
        protected boolean hovered3;

        public Vec3dControllerWidget(Vec3dController control, YACLScreen screen, Dimension<Integer> dim) {
            super(control, screen, dim);
            inputFieldX = control.getX();
            inputFieldY = control.getY();
            inputFieldZ = control.getZ();

            inputFieldXFocused = false;
            inputFieldYFocused = false;
            inputFieldZFocused = false;

            selectionLength = 0;
            emptyText = Text.literal("Click to type...").formatted(Formatting.GRAY);
            control.option().addListener((opt, val) -> {
                inputFieldX = control.getX();
            });
            setDimension(dim);
        }

        @Override
        public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
            Dimension<Integer> dim = getDimension();
            inputFieldXBounds = Dimension.ofInt(dim.x(), dim.y(), dim.width() / 3, dim.height());
            inputFieldYBounds = Dimension.ofInt(dim.x() + dim.width() / 3, dim.y(), dim.width() / 3, dim.height());
            inputFieldZBounds = Dimension.ofInt(dim.x() + dim.width() / 3 * 2, dim.y(), dim.width() / 3, dim.height());

            hovered = isMouseOver(mouseX, mouseY);
            hovered1 = isMouseOver(mouseX, mouseY, inputFieldXBounds);
            hovered2 = isMouseOver(mouseX, mouseY, inputFieldYBounds);
            hovered3 = isMouseOver(mouseX, mouseY, inputFieldZBounds);

            Text name = control.option().changed() ? modifiedOptionName : control.option().name();
            Text shortenedName = Text.literal(GuiUtils.shortenString(name.getString(), textRenderer, getDimension().width() - getControlWidth() - getXPadding() - 7, "...")).setStyle(name.getStyle());

            drawButtonRect(graphics, inputFieldXBounds.x(), inputFieldXBounds.y(), inputFieldXBounds.xLimit(), inputFieldXBounds.yLimit(), hovered1 || focused, isAvailable());
            drawButtonRect(graphics, inputFieldYBounds.x(), inputFieldYBounds.y(), inputFieldYBounds.xLimit(), inputFieldYBounds.yLimit(), hovered2 || focused, isAvailable());
            drawButtonRect(graphics, inputFieldZBounds.x(), inputFieldZBounds.y(), inputFieldZBounds.xLimit(), inputFieldZBounds.yLimit(), hovered3 || focused, isAvailable());

            graphics.drawText(textRenderer, shortenedName, getDimension().x() + getXPadding(), getTextY(), getValueColor(), true);


            drawValueText(graphics, mouseX, mouseY, delta);
            if (isHovered()) {
                drawHoveredControl(graphics, mouseX, mouseY, delta);
            }
        }

        public boolean isMouseOver(double mouseX, double mouseY, Dimension<Integer> dim) {
            if (dim == null) return false;
            return dim.isPointInside((int) mouseX, (int) mouseY);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!isMouseOver(mouseX, mouseY) || !isAvailable())
                return false;


            return true;
        }

        @Override
        protected int getHoveredControlWidth() {
            return getUnhoveredControlWidth();
        }

        @Override
        protected Text getValueText() {
            return super.getValueText();
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!isFocused()) {
                return false;
            }

            if (keyCode == InputUtil.GLFW_KEY_ENTER || keyCode == InputUtil.GLFW_KEY_SPACE || keyCode == InputUtil.GLFW_KEY_KP_ENTER) {

                return true;
            }

            return false;
        }
    }
}
