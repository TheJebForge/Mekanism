package mekanism.common.util;

import mekanism.api.text.IHasTextComponent;
import mekanism.common.util.text.TextComponentUtil;
import net.minecraft.util.text.ITextComponent;

/**
 * Code taken from UE and modified to fit Mekanism.
 */
public class UnitDisplayUtils {

    /**
     * Displays the unit as text. Does handle negative numbers, and will place a negative sign in front of the output string showing this. Use string.replace to remove
     * the negative sign if unwanted
     */
    public static String getDisplay(double value, ElectricUnit unit, int decimalPlaces, boolean isShort) {
        String unitName = unit.name;
        String prefix = "";
        if (value < 0) {
            value = Math.abs(value);
            prefix = "-";
        }
        if (isShort) {
            unitName = unit.symbol;
        } else if (value > 1) {
            unitName = unit.getPlural();
        }
        if (value == 0) {
            return value + " " + unitName;
        }
        for (int i = 0; i < MeasurementUnit.values().length; i++) {
            MeasurementUnit lowerMeasure = MeasurementUnit.values()[i];
            if (lowerMeasure.below(value) && lowerMeasure.ordinal() == 0) {
                return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
            }
            if (lowerMeasure.ordinal() + 1 >= MeasurementUnit.values().length) {
                return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
            }
            MeasurementUnit upperMeasure = MeasurementUnit.values()[i + 1];
            if ((lowerMeasure.above(value) && upperMeasure.below(value)) || lowerMeasure.value == value) {
                return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
            }
        }
        return prefix + roundDecimals(value, decimalPlaces) + " " + unitName;
    }

    public static ITextComponent getDisplayShort(double value, ElectricUnit unit) {
        return TextComponentUtil.build(getDisplay(value, unit, 2, true));
    }

    public static String getDisplayShort(double value, ElectricUnit unit, int decimalPlaces) {
        return getDisplay(value, unit, decimalPlaces, true);
    }

    public static String getDisplaySimple(double value, ElectricUnit unit, int decimalPlaces) {
        if (value > 1) {
            if (decimalPlaces < 1) {
                return (int) value + " " + unit.getPlural();
            }
            return roundDecimals(value, decimalPlaces) + " " + unit.getPlural();
        }
        if (decimalPlaces < 1) {
            return (int) value + " " + unit.name;
        }
        return roundDecimals(value, decimalPlaces) + " " + unit.name;
    }

    public static String getDisplay(double T, TemperatureUnit unit, int decimalPlaces, boolean shift, boolean isShort) {
        String unitName = unit.name;
        String prefix = "";
        double value = unit.convertFromK(T, shift);
        if (value < 0) {
            value = Math.abs(value);
            prefix = "-";
        }
        if (isShort) {
            unitName = unit.symbol;
        }
        if (value == 0) {
            return value + (isShort ? "" : " ") + unitName;
        }
        for (int i = 0; i < MeasurementUnit.values().length; i++) {
            MeasurementUnit lowerMeasure = MeasurementUnit.values()[i];
            if (lowerMeasure.below(value) && lowerMeasure.ordinal() == 0) {
                return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + (isShort ? "" : " ") + lowerMeasure.getName(isShort) + unitName;
            }

            if (lowerMeasure.ordinal() + 1 >= MeasurementUnit.values().length) {
                return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + (isShort ? "" : " ") + lowerMeasure.getName(isShort) + unitName;
            }

            MeasurementUnit upperMeasure = MeasurementUnit.values()[i + 1];
            if ((lowerMeasure.above(value) && upperMeasure.below(value)) || lowerMeasure.value == value) {
                return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + (isShort ? "" : " ") + lowerMeasure.getName(isShort) + unitName;
            }
        }
        return prefix + roundDecimals(value, decimalPlaces) + (isShort ? "" : " ") + unitName;
    }

    public static ITextComponent getDisplayShort(double value, TemperatureUnit unit) {
        return TextComponentUtil.build(getDisplayShort(value, true, unit));
    }

    public static String getDisplayShort(double value, boolean shift, TemperatureUnit unit) {
        return getDisplayShort(value, unit, shift, 2);
    }

    public static String getDisplayShort(double value, TemperatureUnit unit, boolean shift, int decimalPlaces) {
        return getDisplay(value, unit, decimalPlaces, shift, true);
    }

    public static double roundDecimals(double d, int decimalPlaces) {
        int j = (int) (d * Math.pow(10, decimalPlaces));
        return j / Math.pow(10, decimalPlaces);
    }

    public static double roundDecimals(double d) {
        return roundDecimals(d, 2);
    }

    public enum ElectricUnit {
        JOULES("Joule", "J"),
        FORGE_ENERGY("Forge Energy", "FE"),
        ELECTRICAL_UNITS("Electrical Unit", "EU");

        public String name;
        public String symbol;

        ElectricUnit(String s, String s1) {
            name = s;
            symbol = s1;
        }

        public String getPlural() {
            return this == FORGE_ENERGY ? name : name + "s";
        }
    }

    public enum TemperatureUnit {
        KELVIN("Kelvin", "K", 0, 1),
        CELSIUS("Celsius", "°C", 273.15, 1),
        RANKINE("Rankine", "R", 0, 9D / 5D),
        FAHRENHEIT("Fahrenheit", "°F", 459.67, 9D / 5D),
        AMBIENT("Ambient", "+STP", 300, 1);

        public String name;
        public String symbol;
        public double zeroOffset;
        public double intervalSize;

        TemperatureUnit(String s, String s1, double offset, double size) {
            name = s;
            symbol = s1;
            zeroOffset = offset;
            intervalSize = size;
        }

        public double convertFromK(double T, boolean shift) {
            return (T * intervalSize) - (shift ? zeroOffset : 0);
        }

        public double convertToK(double T, boolean shift) {
            return (T + (shift ? zeroOffset : 0)) / intervalSize;
        }
    }

    /**
     * Metric system of measurement.
     */
    public enum MeasurementUnit {
        FEMTO("Femto", "f", 0.000000000000001D),
        PICO("Pico", "p", 0.000000000001D),
        NANO("Nano", "n", 0.000000001D),
        MICRO("Micro", "u", 0.000001D),
        MILLI("Milli", "m", 0.001D),
        BASE("", "", 1),
        KILO("Kilo", "k", 1_000D),
        MEGA("Mega", "M", 1_000_000D),
        GIGA("Giga", "G", 1_000_000_000D),
        TERA("Tera", "T", 1_000_000_000_000D),
        PETA("Peta", "P", 1_000_000_000_000_000D),
        EXA("Exa", "E", 1_000_000_000_000_000_000D),
        ZETTA("Zetta", "Z", 1_000_000_000_000_000_000_000D),
        YOTTA("Yotta", "Y", 1_000_000_000_000_000_000_000_000D);

        /**
         * long name for the unit
         */
        public String name;

        /**
         * short unit version of the unit
         */
        public String symbol;

        /**
         * Point by which a number is consider to be of this unit
         */
        public double value;

        MeasurementUnit(String s, String s1, double v) {
            name = s;
            symbol = s1;
            value = v;
        }

        public String getName(boolean getShort) {
            if (getShort) {
                return symbol;
            } else {
                return name;
            }
        }

        public double process(double d) {
            return d / value;
        }

        public boolean above(double d) {
            return d > value;
        }

        public boolean below(double d) {
            return d < value;
        }
    }

    public enum EnergyType implements IHasTextComponent {
        J,
        FE,
        EU;

        @Override
        public ITextComponent getTextComponent() {
            return TextComponentUtil.build(name());
        }
    }

    public enum TempType implements IHasTextComponent {
        K,
        C,
        R,
        F,
        STP;

        @Override
        public ITextComponent getTextComponent() {
            return TextComponentUtil.build(name());
        }
    }
}