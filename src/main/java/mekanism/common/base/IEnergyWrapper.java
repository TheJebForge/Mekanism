package mekanism.common.base;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IEnergyStorage;
import mekanism.api.energy.IStrictEnergyAcceptor;
import mekanism.api.energy.IStrictEnergyOutputter;
import mekanism.api.energy.IStrictEnergyStorage;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.integration.ic2.IC2Integration;
import mekanism.common.integration.redstoneflux.RFIntegration;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import net.minecraftforge.fml.common.Optional.Method;

@InterfaceList({
      @Interface(iface = "cofh.redstoneflux.api.IEnergyProvider", modid = MekanismHooks.REDSTONEFLUX_MOD_ID),
      @Interface(iface = "cofh.redstoneflux.api.IEnergyReceiver", modid = MekanismHooks.REDSTONEFLUX_MOD_ID),
      @Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = MekanismHooks.IC2_MOD_ID),
      @Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = MekanismHooks.IC2_MOD_ID),
      @Interface(iface = "ic2.api.energy.tile.IEnergyEmitter", modid = MekanismHooks.IC2_MOD_ID),
      @Interface(iface = "ic2.api.tile.IEnergyStorage", modid = MekanismHooks.IC2_MOD_ID)
})
public interface IEnergyWrapper extends IStrictEnergyStorage, IEnergyReceiver, IEnergyProvider, IEnergySink, IEnergySource, IEnergyStorage, IStrictEnergyAcceptor,
      IStrictEnergyOutputter, IInventory {

    double getMaxOutput();

    //Redstone Flux
    @Override
    @Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    default int getEnergyStored(EnumFacing from) {
        return RFIntegration.toRF(getEnergy());
    }

    @Override
    @Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    default int getMaxEnergyStored(EnumFacing from) {
        return RFIntegration.toRF(getMaxEnergy());
    }

    //IC2
    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default boolean isTeleporterCompatible(EnumFacing side) {
        return !MekanismConfig.current().general.blacklistIC2.val() && canOutputEnergy(side);
    }

    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing direction) {
        return !MekanismConfig.current().general.blacklistIC2.val() && canReceiveEnergy(direction);
    }

    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default int getSinkTier() {
        return !MekanismConfig.current().general.blacklistIC2.val() ? 4 : 0;
    }

    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default int getSourceTier() {
        return !MekanismConfig.current().general.blacklistIC2.val() ? 4 : 0;
    }

    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default int getStored() {
        return IC2Integration.toEUAsInt(getEnergy());
    }

    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default int getCapacity() {
        return IC2Integration.toEUAsInt(getMaxEnergy());
    }

    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default int getOutput() {
        return IC2Integration.toEUAsInt(getMaxOutput());
    }

    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default double getOfferedEnergy() {
        return !MekanismConfig.current().general.blacklistIC2.val() ? IC2Integration.toEU(Math.min(getEnergy(), getMaxOutput())) : 0;
    }

    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default double getOutputEnergyUnitsPerTick() {
        return !MekanismConfig.current().general.blacklistIC2.val() ? IC2Integration.toEU(getMaxOutput()) : 0;
    }

    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        return !MekanismConfig.current().general.blacklistIC2.val() && canOutputEnergy(direction) && receiver instanceof IEnergyConductor;
    }

    @Override
    @Method(modid = MekanismHooks.IC2_MOD_ID)
    default void setStored(int energy) {
        if (!MekanismConfig.current().general.blacklistIC2.val()) {
            setEnergy(IC2Integration.fromEU(energy));
        }
    }
}