package mekanism.client.gui.filter;

import mekanism.api.EnumColor;
import mekanism.common.content.filter.IModIDFilter;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.LangUtils;
import mekanism.common.util.text.TextComponentUtil;
import mekanism.common.util.text.Translation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class GuiModIDFilter<FILTER extends IModIDFilter, TILE extends TileEntityMekanism> extends GuiTextFilter<FILTER, TILE> {

    protected GuiModIDFilter(PlayerEntity player, TILE tile) {
        super(player, tile);
    }

    protected abstract void updateStackList(String modName);

    @Override
    protected void setText() {
        String name = text.getText();
        if (name.isEmpty()) {
            status = TextComponentUtil.build(EnumColor.DARK_RED, Translation.of("gui.modIDFilter.noID"));
            return;
        } else if (name.equals(filter.getModID())) {
            status = TextComponentUtil.build(EnumColor.DARK_RED, Translation.of("gui.modIDFilter.sameID"));
            return;
        }
        updateStackList(name);
        filter.setModID(name);
        text.setText("");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawString((isNew ? LangUtils.localize("gui.new") : LangUtils.localize("gui.edit")) + " " +
                                LangUtils.localize("gui.modIDFilter"), 43, 6, 0x404040);
        drawString(TextComponentUtil.build(Translation.of("mekanism.gui.status"), ": ", status), 35, 20, 0x00CD00);
        renderScaledText(TextComponentUtil.build(Translation.of("mekanism.gui.id"), ": " + filter.getModID()), 35, 32, 0x00CD00, 107);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}