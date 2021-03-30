package com.mrikso.arsceditor.valueeditor;

import com.google.devrel.gmscore.tools.apk.arsc.ResourceTableChunk;
import com.google.devrel.gmscore.tools.apk.arsc.StringPoolChunk;
import com.google.devrel.gmscore.tools.apk.arsc.TypeChunk;

public class ValueHelper {
    private static StringPoolChunk stringPoolChunk;

    private static ResourceTableChunk resourceTableChunk;
    private static TypeChunk typeChunk;

    public static StringPoolChunk getStringPoolChunk() {
        return stringPoolChunk;
    }

    public static void setStringPoolChunk(StringPoolChunk stringPoolChunk) {
        ValueHelper.stringPoolChunk = stringPoolChunk;
    }

    public static ResourceTableChunk getResourceTableChunk() {
        return resourceTableChunk;
    }

    public static void setResourceTableChunk(ResourceTableChunk resourceTableChunk) {
        ValueHelper.resourceTableChunk = resourceTableChunk;
    }

    public static TypeChunk getTypeChunk() {
        return typeChunk;
    }

    public static void setTypeChunk(TypeChunk typeChunk) {
        ValueHelper.typeChunk = typeChunk;
    }
}
