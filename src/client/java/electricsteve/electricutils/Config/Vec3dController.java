package electricsteve.electricutils.Config;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ActionController;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

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

    boolean isInputValid(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
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

        protected boolean hoveredX;
        protected boolean hoveredY;
        protected boolean hoveredZ;

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
                inputFieldY = control.getY();
                inputFieldZ = control.getZ();
            });
            setDimension(dim);
        }

        public Double getSelectedField() {
            if (inputFieldXFocused) return inputFieldX;
            if (inputFieldYFocused) return inputFieldY;
            if (inputFieldZFocused) return inputFieldZ;
            return null;
        }

        public void setSelectedField(Double value) {
            if (inputFieldXFocused) inputFieldX = value;
            if (inputFieldYFocused) inputFieldY = value;
            if (inputFieldZFocused) inputFieldZ = value;
        }

        public Dimension<Integer> getSelectedFieldBounds() {
            if (inputFieldXFocused) return inputFieldXBounds;
            if (inputFieldYFocused) return inputFieldYBounds;
            if (inputFieldZFocused) return inputFieldZBounds;
            return null;
        }

        public boolean isInputFieldFocused() {
            if (inputFieldXFocused) return true;
            if (inputFieldYFocused) return true;
            if (inputFieldZFocused) return true;
            return false;
        }

        @Override
        public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
            Dimension<Integer> dim = getDimension();
            inputFieldXBounds = Dimension.ofInt(dim.x(), dim.y(), dim.width() / 3, dim.height());
            inputFieldYBounds = Dimension.ofInt(dim.x() + dim.width() / 3, dim.y(), dim.width() / 3, dim.height());
            inputFieldZBounds = Dimension.ofInt(dim.x() + dim.width() / 3 * 2, dim.y(), dim.width() / 3, dim.height());

            hovered = isMouseOver(mouseX, mouseY);
            hoveredX = isMouseOver(mouseX, mouseY, inputFieldXBounds);
            hoveredY = isMouseOver(mouseX, mouseY, inputFieldYBounds);
            hoveredZ = isMouseOver(mouseX, mouseY, inputFieldZBounds);

            Text name = control.option().changed() ? modifiedOptionName : control.option().name();
            Text shortenedName = Text.literal(GuiUtils.shortenString(name.getString(), textRenderer, getDimension().width() - getControlWidth() - getXPadding() - 7, "...")).setStyle(name.getStyle());

            drawButtonRect(graphics, inputFieldXBounds.x(), inputFieldXBounds.y(), inputFieldXBounds.xLimit(), inputFieldXBounds.yLimit(), hoveredX || focused, isAvailable());
            drawButtonRect(graphics, inputFieldYBounds.x(), inputFieldYBounds.y(), inputFieldYBounds.xLimit(), inputFieldYBounds.yLimit(), hoveredY || focused, isAvailable());
            drawButtonRect(graphics, inputFieldZBounds.x(), inputFieldZBounds.y(), inputFieldZBounds.xLimit(), inputFieldZBounds.yLimit(), hoveredZ || focused, isAvailable());

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
            if (isAvailable() && getDimension().isPointInside((int) mouseX, (int) mouseY)) {
                if (hoveredX) {
                    inputFieldXFocused = true;
                    inputFieldYFocused = false;
                    inputFieldZFocused = false;
                } else if (hoveredY) {
                    inputFieldXFocused = false;
                    inputFieldYFocused = true;
                    inputFieldZFocused = false;
                } else if (hoveredZ) {
                    inputFieldXFocused = false;
                    inputFieldYFocused = false;
                    inputFieldZFocused = true;
                }

                if (!inputFieldXBounds.isPointInside((int) mouseX, (int) mouseY) && !inputFieldYBounds.isPointInside((int) mouseX, (int) mouseY) && !inputFieldZBounds.isPointInside((int) mouseX, (int) mouseY)) {
                    caretPos = getDefaultCaretPos();
                } else {
                    // gets the appropriate caret position for where you click
                    int textX = (int) mouseX - (getSelectedFieldBounds().xLimit() - textRenderer.getWidth(getValueText()));
                    int pos = -1;
                    int currentWidth = 0;
                    for (char ch : getSelectedField().toString().toCharArray()) {
                        pos++;
                        int charLength = textRenderer.getWidth(String.valueOf(ch));
                        if (currentWidth + charLength / 2 > textX) { // if more than halfway past the characters select in front of that char
                            caretPos = pos;
                            break;
                        } else if (pos == getSelectedField().toString().length() - 1) {
                            // if we have reached the end and no matches, it must be the second half of the char so the last position
                            caretPos = pos + 1;
                        }
                        currentWidth += charLength;
                    }

                    selectionLength = 0;
                }

                return true;
            } else {
                inputFieldXFocused = false;
                inputFieldYFocused = false;
                inputFieldZFocused = false;
            }

            return false;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!inputFieldXFocused && !inputFieldYFocused && !inputFieldZFocused)
                return false;

            switch (keyCode) {
                case InputUtil.GLFW_KEY_ESCAPE, InputUtil.GLFW_KEY_ENTER -> {
                    unfocus();
                    return true;
                }
                case InputUtil.GLFW_KEY_LEFT -> {
                    if (Screen.hasShiftDown()) {
                        if (Screen.hasControlDown()) {
                            int spaceChar = findSpaceIndex(true);
                            selectionLength += caretPos - spaceChar;
                            caretPos = spaceChar;
                        } else if (caretPos > 0) {
                            caretPos--;
                            selectionLength += 1;
                        }
                        checkRenderOffset();
                    } else {
                        if (caretPos > 0) {
                            if (selectionLength != 0)
                                caretPos += Math.min(selectionLength, 0);
                            else
                                caretPos--;
                        }
                        checkRenderOffset();
                        selectionLength = 0;
                    }

                    return true;
                }
                case InputUtil.GLFW_KEY_RIGHT -> {
                    if (Screen.hasShiftDown()) {
                        if (Screen.hasControlDown()) {
                            int spaceChar = findSpaceIndex(false);
                            selectionLength -= spaceChar - caretPos;
                            caretPos = spaceChar;
                        } else if (caretPos < getSelectedField().toString().length()) {
                            caretPos++;
                            selectionLength -= 1;
                        }
                        checkRenderOffset();
                    } else {
                        if (caretPos < getSelectedField().toString().length()) {
                            if (selectionLength != 0)
                                caretPos += Math.max(selectionLength, 0);
                            else
                                caretPos++;
                            checkRenderOffset();
                        }
                        selectionLength = 0;
                    }

                    return true;
                }
                case InputUtil.GLFW_KEY_BACKSPACE -> {
                    doBackspace();
                    return true;
                }
                case InputUtil.GLFW_KEY_DELETE -> {
                    doDelete();
                    return true;
                }
            }

            if (Screen.isPaste(keyCode)) {
                return doPaste();
            } else if (Screen.isCopy(keyCode)) {
                return doCopy();
            } else if (Screen.isCut(keyCode)) {
                return doCut();
            } else if (Screen.isSelectAll(keyCode)) {
                return doSelectAll();
            }

            return false;
        }

        protected boolean doPaste() {
            this.write(client.keyboard.getClipboard());
            return true;
        }

        protected boolean doCopy() {
            if (selectionLength != 0) {
                client.keyboard.setClipboard(getSelection());
                return true;
            }
            return false;
        }

        protected boolean doCut() {
            if (selectionLength != 0) {
                client.keyboard.setClipboard(getSelection());
                this.write("");
                return true;
            }
            return false;
        }

        protected boolean doSelectAll() {
            caretPos = getSelectedField().toString().length();
            checkRenderOffset();
            selectionLength = -caretPos;
            return true;
        }

        protected void doBackspace() {
            if (selectionLength != 0) {
                write("");
            } else if (caretPos > 0) {
                if (modifyInput(builder -> builder.deleteCharAt(caretPos - 1))) {
                    caretPos--;
                    checkRenderOffset();
                }
            }
        }

        protected void doDelete() {
            if (selectionLength != 0) {
                write("");
            } else if (caretPos < getSelectedField().toString().length()) {
                modifyInput(builder -> builder.deleteCharAt(caretPos));
            }
        }

        public void write(String string) {
            if (selectionLength == 0) {
                if (modifyInput(builder -> builder.insert(caretPos, string))) {
                    caretPos += string.length();
                    checkRenderOffset();
                }
            } else {
                int start = getSelectionStart();
                int end = getSelectionEnd();

                if (modifyInput(builder -> builder.replace(start, end, string))) {
                    caretPos = start + string.length();
                    selectionLength = 0;
                    checkRenderOffset();
                }
            }
        }

        public boolean modifyInput(Consumer<StringBuilder> consumer) {
            StringBuilder temp = new StringBuilder(getSelectedField().toString());
            consumer.accept(temp);
            if (!control.isInputValid(temp.toString()))
                return false;
            setSelectedField(Double.valueOf(temp.toString()));
            return true;
        }

        public int getUnshiftedLength() {
            if (optionNameString.isEmpty())
                return getDimension().width() - getXPadding() * 2;
            return getDimension().width() / 8 * 5;
        }

        public int getMaxUnwrapLength() {
            if (optionNameString.isEmpty())
                return getDimension().width() - getXPadding() * 2;
            return getDimension().width() / 2;
        }

        protected int getDefaultCaretPos() {
            return getSelectedField().toString().length();
        }

        public int getSelectionStart() {
            return Math.min(caretPos, caretPos + selectionLength);
        }

        public int getSelectionEnd() {
            return Math.max(caretPos, caretPos + selectionLength);
        }

        protected String getSelection() {
            return getSelectedField().toString().substring(getSelectionStart(), getSelectionEnd());
        }

        protected int findSpaceIndex(boolean reverse) {
            int i;
            int fromIndex = caretPos;
            if (reverse) {
                if (caretPos > 0)
                    fromIndex -= 1;
                i = this.getSelectedField().toString().lastIndexOf(" ", fromIndex);

                if (i == -1) i = 0;
            } else {
                if (caretPos < getSelectedField().toString().length())
                    fromIndex += 1;
                i = this.getSelectedField().toString().indexOf(" ", fromIndex);

                if (i == -1) i = getSelectedField().toString().length();
            }

            return i;
        }

        @Override
        public void setFocused(boolean focused) {
            super.setFocused(focused);
            inputFieldXFocused = focused;
        }

        @Override
        public void unfocus() {
            super.unfocus();
            inputFieldXFocused = false;
            inputFieldYFocused = false;
            inputFieldZFocused = false;
            renderOffset = 0;
        }

        @Override
        public void setDimension(Dimension<Integer> dim) {
            super.setDimension(dim);

//            int width = Math.max(6, Math.min(textRenderer.getWidth(getValueText()), getUnshiftedLength()));
//            inputFieldBounds = Dimension.ofInt(dim.xLimit() - getXPadding() - width, dim.centerY() - textRenderer.fontHeight / 2, width, textRenderer.fontHeight);
            inputFieldXBounds = Dimension.ofInt(dim.x(), dim.y(), dim.width() / 3, dim.height());
            inputFieldYBounds = Dimension.ofInt(dim.x() + dim.width() / 3, dim.y(), dim.width() / 3, dim.height());
            inputFieldZBounds = Dimension.ofInt(dim.x() + dim.width() / 3 * 2, dim.y(), dim.width() / 3, dim.height());
        }

        @Override
        public boolean isHovered() {
            return super.isHovered() || inputFieldXFocused || inputFieldYFocused || inputFieldZFocused;
        }

        @Override
        protected int getUnhoveredControlWidth() {
            return !isHovered() ? Math.min(getHoveredControlWidth(), getMaxUnwrapLength()) : getHoveredControlWidth();
        }

        @Override
        protected int getHoveredControlWidth() {
            return Math.min(textRenderer.getWidth(getValueText()), getUnshiftedLength());
        }

        @Override
        protected Text getValueText() {
            return !isInputFieldFocused() ? control.formatValue() : Text.literal(getSelectedField().toString());
        }

        protected void checkRenderOffset() {
            if (textRenderer.getWidth(getSelectedField().toString()) < getUnshiftedLength()) {
                renderOffset = 0;
                return;
            }

            int textX = getDimension().xLimit() - textRenderer.getWidth(getSelectedField().toString()) - getXPadding();
            int caretX = textX + textRenderer.getWidth(getSelectedField().toString().substring(0, caretPos)) - 1;

            int minX = getDimension().xLimit() - getXPadding() - getUnshiftedLength();
            int maxX = minX + getUnshiftedLength();

            if (caretX + renderOffset < minX) {
                renderOffset = minX - caretX;
            } else if (caretX + renderOffset > maxX) {
                renderOffset = maxX - caretX;
            }
        }
    }
}
