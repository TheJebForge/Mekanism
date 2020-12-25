package mekanism.client.gui.element.window;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.text.TextComponentUtil;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiRightArrow;
import mekanism.client.gui.element.slot.GuiVirtualSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.QIOItemViewerContainer;

public class GuiCraftingWindow extends GuiWindow {

    private final List<GuiVirtualSlot> slots;
    private final byte index;
    private QIOItemViewerContainer container;

    public GuiCraftingWindow(IGuiWrapper gui, int x, int y, QIOItemViewerContainer container, byte index) {
        super(gui, x, y, 118, 80);
        this.container = container;
        this.index = index;
        interactionStrategy = InteractionStrategy.ALL;
        slots = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                GuiVirtualSlot slot = new GuiVirtualSlot(SlotType.NORMAL, gui, relativeX + 8 + column * 18, relativeY + 18 + row * 18);
                slot.updateVirtualSlot(container.getCraftingWindowSlot(this.index, row * 3 + column));
                addChild(slot);
                slots.add(slot);
            }
        }
        addChild(new GuiRightArrow(gui, relativeX + 66, relativeY + 38).jeiCrafting());
        GuiVirtualSlot slot = new GuiVirtualSlot(SlotType.NORMAL, gui, relativeX + 92, relativeY + 36);
        slot.updateVirtualSlot(container.getCraftingWindowSlot(this.index, 9));
        addChild(slot);
        slots.add(slot);
    }

    public void updateContainer(QIOItemViewerContainer container) {
        //Update our container reference and lookup the slots again
        // and update the stored lookup method
        this.container = container;
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).updateVirtualSlot(container.getCraftingWindowSlot(index, i));
        }
    }

    @Override
    public void onFocused() {
        super.onFocused();
        container.setSelectedCraftingGrid(index);
    }

    public byte getIndex() {
        return index;
    }

    @Override
    public void renderForeground(MatrixStack matrix, int mouseX, int mouseY) {
        super.renderForeground(matrix, mouseX, mouseY);
        //TODO: Should this have its own translation key
        //drawTitleText(matrix, MekanismLang.CRAFTING.translate(), 5);
        //Display the index for debug purposes
        drawTitleText(matrix, TextComponentUtil.build(MekanismLang.CRAFTING, " ", index), 5);
    }
}