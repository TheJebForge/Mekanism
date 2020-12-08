package mekanism.common.integration.crafttweaker.content.builder;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.impl.util.MCResourceLocation;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentBuilder;
import mekanism.common.integration.crafttweaker.CrTConstants;
import mekanism.common.integration.crafttweaker.content.CrTContentUtils;
import mekanism.common.integration.crafttweaker.content.attribute.ICrTChemicalAttribute.ICrTPigmentAttribute;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name(CrTConstants.CLASS_BUILDER_PIGMENT)
public class CrTPigmentBuilder extends CrTChemicalBuilder<Pigment, PigmentBuilder, ICrTPigmentAttribute, CrTPigmentBuilder> {

    @ZenCodeType.Method
    public static CrTPigmentBuilder builder(@ZenCodeType.Optional MCResourceLocation textureLocation) {
        return new CrTPigmentBuilder(textureLocation == null ? PigmentBuilder.builder() : PigmentBuilder.builder(textureLocation.getInternal()));
    }

    protected CrTPigmentBuilder(PigmentBuilder builder) {
        super(builder);
    }

    @Override
    protected void build(ResourceLocation registryName) {
        CrTContentUtils.queuePigmentForRegistration(registryName, getInternal());
    }
}