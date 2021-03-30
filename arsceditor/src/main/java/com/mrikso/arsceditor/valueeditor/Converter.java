package com.mrikso.arsceditor.valueeditor;

import com.mrikso.arsceditor.util.HexUtil;
import com.mrikso.arsceditor.util.ResourceHelper;
import com.mrikso.arsceditor.util.ResourceHelper2;
import com.mrikso.arsceditor.util.TypedValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.google.devrel.gmscore.tools.apk.arsc.BinaryResourceValue;

public class Converter {

    public static BinaryResourceValue convertValue(@Nullable String value, @NotNull ValueType valueType){
        TypedValue typedValue = new TypedValue();
        int size = 8;
        switch (valueType){
            case TYPE_COLOR:
                BinaryResourceValue.Type type = ResourceHelper.getColorType(value);
                int data = ResourceHelper.getColor(value);
                return new BinaryResourceValue(size,type, data);
            case TYPE_FLOAT:
                ResourceHelper2.parseFloatAttribute(null, value, typedValue, false);
                return new BinaryResourceValue(size,BinaryResourceValue.Type.FLOAT, typedValue.data);
            case TYPE_BOOLEAN:
                int dataDec = -1;
                if ("true".equalsIgnoreCase(value)) {
                    dataDec = 1;
                } else if ("false".equalsIgnoreCase(value)) {
                    dataDec = 0;
                }
                return new BinaryResourceValue(size,BinaryResourceValue.Type.INT_BOOLEAN, dataDec);
            case TYPE_INT_DEC:
                return new BinaryResourceValue(size,BinaryResourceValue.Type.INT_DEC, convertInt(value));
            case TYPE_INT_HEX:
                return new BinaryResourceValue(size,BinaryResourceValue.Type.INT_HEX, convertInt(value));
            case TYPE_FRACTION:
                ResourceHelper2.parseFloatAttribute(null, value, typedValue, false);
                return new BinaryResourceValue(size,BinaryResourceValue.Type.FRACTION, typedValue.data);
            case TYPE_ATTRIBUTE:
                return new BinaryResourceValue(size,BinaryResourceValue.Type.ATTRIBUTE,
                        HexUtil.parseInt(value.substring(1)));
            case TYPE_DIMENSION:
                ResourceHelper2.parseFloatAttribute(null, value, typedValue, true);
                return new BinaryResourceValue(size,BinaryResourceValue.Type.DIMENSION, typedValue.data);
            case TYPE_REFERENCE:
                return new BinaryResourceValue(size,BinaryResourceValue.Type.REFERENCE,
                        HexUtil.parseInt(value.substring(1)));
            case TYPE_DYNAMIC_REFERENCE:
                return new BinaryResourceValue(size,BinaryResourceValue.Type.DYNAMIC_REFERENCE,
                        HexUtil.parseInt(value.substring(1)));
        }
        return null;
    }


    private static int convertInt(String rawValue) {
        try {
            // Decode into long, because there are some large hex values in the android resource files
            // (e.g. config_notificationsBatteryLowARGB = 0xFFFF0000 in sdk 14).
            // Integer.decode() does not support large, i.e. negative values in hex numbers.
            // try parsing decimal number
            return (int) Long.parseLong(rawValue);
        } catch (NumberFormatException nfe) {
            // try parsing hex number
            return Long.decode(rawValue).intValue();
        }
    }
}
