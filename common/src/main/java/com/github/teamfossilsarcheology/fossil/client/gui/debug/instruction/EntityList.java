package com.github.teamfossilsarcheology.fossil.client.gui.debug.instruction;

import com.github.teamfossilsarcheology.fossil.client.gui.debug.InstructionTab;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class EntityList extends ContainerObjectSelectionList<EntityList.EntityEntry> {
    private final Consumer<Entity> consumer;
    // 1.21 AbstractSelectionList no longer exposes x0/x1 fields; track our own left edge.
    private final int x0;

    public EntityList(int x0, int width, int height, List<LivingEntity> entities, Minecraft minecraft, Consumer<Entity> function) {
        // 1.21 ctor is (Minecraft, width, height, y, itemHeight)
        super(minecraft, width, height, 60, 25);
        this.x0 = x0;
        setX(x0);
        entities.forEach(entity -> addEntry(new EntityEntry(entity)));
        this.consumer = function;
    }

    @Override
    public int getRowLeft() {
        return x0;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        if (!isMouseOver(mouseX, mouseY)) {
            InstructionTab.entityListHighlight = null;
        }
    }

    @Override
    protected int getScrollbarPosition() {
        return x0 + getWidth() - 6;
    }

    protected class EntityEntry extends ContainerObjectSelectionList.Entry<EntityEntry> {
        private final Button changeButton;
        private final Entity entity;

        EntityEntry(Entity entity) {
            this.entity = entity;
            String display = String.format(Locale.ROOT, "%s[%d]", entity.getClass().getSimpleName(), entity.getId());
            changeButton = Button.builder(Component.literal(display), button -> consumer.accept(entity)).bounds(0, 0, 200, 20).build();
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(changeButton);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver,
                           float partialTick) {
            changeButton.setX(left);
            changeButton.setY(top);
            changeButton.render(guiGraphics, mouseX, mouseY, partialTick);
            if (isMouseOver) {
                InstructionTab.entityListHighlight = entity;
            }
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return ImmutableList.of(changeButton);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return changeButton.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return changeButton.mouseReleased(mouseX, mouseY, button);
        }
    }
}
