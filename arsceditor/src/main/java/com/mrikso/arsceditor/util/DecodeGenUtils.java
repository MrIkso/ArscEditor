package com.mrikso.arsceditor.util;

import com.google.devrel.gmscore.tools.apk.arsc.BinaryResourceValue;
import com.google.devrel.gmscore.tools.apk.arsc.StringPoolChunk;
import com.mrikso.arsceditor.valueeditor.FormatValue;
import com.mrikso.arsceditor.valueeditor.ValueType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.Locale;

public class DecodeGenUtils {

    public static String decodeComplex(int data, boolean isFraction) {
        double value = (data & TypedValue.COMPLEX_MANTISSA_MASK << TypedValue.COMPLEX_MANTISSA_SHIFT)
                * TypedValue.RADIX_MULTS[data >> TypedValue.COMPLEX_RADIX_SHIFT & TypedValue.COMPLEX_RADIX_MASK];
        int unitType = data & TypedValue.COMPLEX_UNIT_MASK;
        String unit;
        if (isFraction) {
            value *= 100;
            switch (unitType) {
                case TypedValue.COMPLEX_UNIT_FRACTION:
                    unit = "%";
                    break;
                case TypedValue.COMPLEX_UNIT_FRACTION_PARENT:
                    unit = "%p";
                    break;

                default:
                    unit = "?f" + Integer.toHexString(unitType);
            }
        } else {
            switch (unitType) {
                case TypedValue.COMPLEX_UNIT_PX:
                    unit = "px";
                    break;
                case TypedValue.COMPLEX_UNIT_DIP:
                    unit = "dp";
                    break;
                case TypedValue.COMPLEX_UNIT_SP:
                    unit = "sp";
                    break;
                case TypedValue.COMPLEX_UNIT_PT:
                    unit = "pt";
                    break;
                case TypedValue.COMPLEX_UNIT_IN:
                    unit = "in";
                    break;
                case TypedValue.COMPLEX_UNIT_MM:
                    unit = "mm";
                    break;

                default:
                    unit = "?d" + Integer.toHexString(unitType);
            }
        }
        return doubleToString(value) + unit;
    }

    public static String doubleToString(double value) {
        if (Double.compare(value, Math.floor(value)) == 0
                && !Double.isInfinite(value)) {
            return Integer.toString((int) value);
        }
        // remove trailing zeroes
        NumberFormat f = NumberFormat.getInstance(Locale.ROOT);
        f.setMaximumFractionDigits(4);
        f.setMinimumIntegerDigits(1);
        return f.format(value);
    }

    public static String floatToString(float value) {
        return doubleToString(value);
    }


    public static FormatValue formatValue(
            @NotNull BinaryResourceValue resValue, @Nullable StringPoolChunk stringPool, String packageName) {
        int data = resValue.data();
        String decoded;

        AttrNameHelper nameHelper = AttrNameHelper.getInstance();
        //System.out.println(String.format("data: %d, size: %d", data, resValue.size()));
        switch (resValue.type()) {
            case NULL:
                return new FormatValue(ValueType.TYPE_STRING, null);
            case DYNAMIC_ATTRIBUTE:
                break;
            case DYNAMIC_REFERENCE:
                decoded = nameHelper.getName(data, packageName);
                if(decoded != null){
                    return new FormatValue(ValueType.TYPE_DYNAMIC_REFERENCE, String.format(Locale.US, "@0x%1$08x", data),
                            String.format("@%s", decoded));
                }
                return new FormatValue(ValueType.TYPE_DYNAMIC_REFERENCE, String.format(Locale.US, "@0x%1$08x", data));
            case REFERENCE:
                decoded = nameHelper.getName(data, packageName);
                if(decoded != null){
                    return new FormatValue(ValueType.TYPE_REFERENCE, String.format(Locale.US, "@0x%1$08x", data),
                            String.format("@%s", decoded));
                }
                return new FormatValue(ValueType.TYPE_REFERENCE, String.format(Locale.US, "@0x%1$08x", data));
            case ATTRIBUTE:
                decoded = nameHelper.getName(data, packageName);
                if(decoded != null){
                    return new FormatValue(ValueType.TYPE_ATTRIBUTE, String.format(Locale.US, "?0x%1$x", data),
                                    String.format("?%s", decoded));
                }
                return new FormatValue(ValueType.TYPE_ATTRIBUTE, String.format(Locale.US, "?0x%1$x", data));
            case STRING:
                if(stringPool != null && stringPool.getStringCount() < data){
                    return new FormatValue(ValueType.TYPE_STRING, stringPool.getString(data), data);
                }else {
                    // reference string link
                    decoded = nameHelper.getName(data, packageName);
                    if(decoded !=null){
                        return new FormatValue(ValueType.TYPE_REFERENCE, String.format(Locale.US, "@0x%1$x", data), data,
                                String.format("@%s", decoded));
                    }
                    return new FormatValue(ValueType.TYPE_REFERENCE, String.format(Locale.US, "@0x%1$x", data), data);
                }
            case DIMENSION:
                return new FormatValue(ValueType.TYPE_DIMENSION, decodeComplex(data, false));
            case FRACTION:
                return new FormatValue(ValueType.TYPE_FRACTION, decodeComplex(data, true));
            case FLOAT:
                return new FormatValue(ValueType.TYPE_FLOAT, floatToString(Float.intBitsToFloat(data)));
            case INT_DEC:
                return new FormatValue(ValueType.TYPE_INT_DEC, Integer.toString(data));
            case INT_HEX:
                return new FormatValue(ValueType.TYPE_INT_HEX, "0x" + Integer.toHexString(data));
            case INT_BOOLEAN:
                return new FormatValue(ValueType.TYPE_BOOLEAN, data == 0 ? "false" : "true");
            case INT_COLOR_ARGB8:
                return new FormatValue(ValueType.TYPE_COLOR, String.format("#%08x", data));
            case INT_COLOR_RGB8:
                return new FormatValue(ValueType.TYPE_COLOR, String.format("#%06x", data & 0xFFFFFF));
            case INT_COLOR_ARGB4:
                return new FormatValue(ValueType.TYPE_COLOR, String.format("#%04x", data & 0xFFFF));
            case INT_COLOR_RGB4:
                return new FormatValue(ValueType.TYPE_COLOR, String.format("#%03x", data & 0xFFF));
        }

        return new FormatValue(ValueType.TYPE_STRING, String.format("@res/0x%x", data));
    }

}
