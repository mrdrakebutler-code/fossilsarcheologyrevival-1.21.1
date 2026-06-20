package com.github.teamfossilsarcheology.fossil.client.gui.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DebugSelectionList<E extends ContainerObjectSelectionList.Entry<E>> extends ContainerObjectSelectionList<E> {
    protected final List<Renderable> renderables = new ArrayList<>();
    protected final List<GuiEventListener> listeners = new ArrayList<>();
    protected final int rowWidth;
    // In 1.21 AbstractSelectionList no longer exposes x0/x1/y0 fields; we keep our own
    // geometry source-of-truth and wire it into the widget position below.
    protected int x0;
    protected int x1;
    protected int y0;
    private GuiEventListener focused;

    public DebugSelectionList(Minecraft minecraft, int rowWidth, int width, int height, int y0, int y1, int itemHeight) {
        // 1.21 ctor is (Minecraft, width, height, y, itemHeight)
        super(minecraft, width, height, y0, itemHeight);
        this.rowWidth = rowWidth;
        this.y0 = y0;
        setY(y0);
    }

    @Override
    public int getRowWidth() {
        return rowWidth;
    }

    @Override
    public int getRowLeft() {
        return getEntryLeftPos();
    }

    /**
     * Left position used for entry rendering and mouse picking. Subclasses override to offset.
     */
    protected int getEntryLeftPos() {
        return x0;
    }

    @Override
    protected int getScrollbarPosition() {
        return x0 + rowWidth + 6;
    }

    @Override
    public @NotNull Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
        for (GuiEventListener guiEventListener : listeners) {
            if (!guiEventListener.isMouseOver(mouseX, mouseY)) continue;
            return Optional.of(guiEventListener);
        }
        return super.getChildAt(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GuiEventListener guiEventListener : listeners) {
            if (!guiEventListener.mouseClicked(mouseX, mouseY, button)) continue;
            focused = guiEventListener;
            if (button == 0) {
                setDragging(true);
            }
            return true;
        }
        focused = null;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (focused != null && isDragging() && button == 0) {
            return focused.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        renderables.forEach(renderable -> renderable.render(guiGraphics, mouseX, mouseY, partialTick));
    }

    protected <T extends Renderable & GuiEventListener> void removeWidget(T widget) {
        renderables.remove(widget);
        listeners.remove(widget);
    }

    protected <T extends Renderable & GuiEventListener> T addWidget(T widget) {
        renderables.add(widget);
        listeners.add(widget);
        return widget;
    }
}
