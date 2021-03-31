package com.mrikso.arsceditor.model;

import com.google.devrel.gmscore.tools.apk.arsc.*;
import com.mrikso.arsceditor.gui.tree.ResourceDirectory;
import com.mrikso.arsceditor.gui.tree.ResourceEntry;
import com.mrikso.arsceditor.util.DecodeGenUtils;
import com.mrikso.arsceditor.valueeditor.FormatValue;
import com.mrikso.arsceditor.valueeditor.ValueHelper;
import com.mrikso.arsceditor.valueeditor.ValueType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ResourceModel {

    private final StringPoolChunk stringPool;
    private final PackageChunk packageChunk;
    private final TypeSpecChunk typeSpec;
    private final TypeChunk type;
    private final String tableName;
    private ResourceDirectory root;

    public ResourceModel(@NotNull StringPoolChunk stringPool, @NotNull PackageChunk packageChunk, @NotNull TypeSpecChunk typeSpec,
                         @NotNull TypeChunk typeChunk, String tableName) {
        this.stringPool = stringPool;
        this.packageChunk = packageChunk;
        this.typeSpec = typeSpec;
        this.type = typeChunk;
        this.tableName = tableName;
    }

    public void readTable() {
        root = new ResourceDirectory(String.format("Resource Table (%s)", tableName));
        if(type!=null) {
            for (Map.Entry<Integer, TypeChunk.Entry> entry : type.getEntries().entrySet()) {
                int entryIndex = entry.getKey();
                BinaryResourceIdentifier id = BinaryResourceIdentifier.create(packageChunk.getId(), typeSpec.getId(), entry.getKey());
                String idN = id.toString();
                FormatValue valueNormal = null;
                BinaryResourceValue value = entry.getValue().value();

                ResourceDirectory resourceDirectory = new ResourceDirectory(idN);
                resourceDirectory.setName(entry.getValue().key());
                // set index in stringpool
                resourceDirectory.setNameIndex(entry.getValue().keyIndex());

                // set index in typemap
                resourceDirectory.setEntryIndex(entryIndex);

                ValueHelper.setTypeChunk(type);

                if (value != null) {
                    valueNormal = formatValue(value);
                }

                // add complex values (style, attr, plurals, array)
                if (entry.getValue().isComplex()) {
                    resourceDirectory.setIsComplex(true);
                    // get map sub values
                    Map<Integer, BinaryResourceValue> values = entry.getValue().values();
                    if (values != null) {
                        for (Map.Entry<Integer, BinaryResourceValue> valueEntry : values.entrySet()) {
                            valueNormal = formatValue(valueEntry.getValue());
                            int key = valueEntry.getKey();
                            String idParent = String.format("0x%1$08x", key);

                            ResourceEntry resourceEntry = new ResourceEntry(idParent);
                            resourceEntry.setName(idParent);
                            resourceEntry.setValue(valueNormal.getValue());
                            resourceEntry.setValueType(valueNormal.getValueType());
                            // value index if value type is string
                            resourceEntry.setValueIndex(valueNormal.getIndex());

                            // set parent index
                            resourceEntry.setEntryIndex(entryIndex);
                            // set children index
                            resourceEntry.setEntryChildrenIndex(key);

                            resourceDirectory.add(resourceEntry);
                        }
                    }
                }
                // set simple values (string, colors, xml, etc.)
                else {
                    if (valueNormal != null) {
                        // set value
                        resourceDirectory.setValue(valueNormal.getValue());
                        // set value type(string, attr, color etc.)
                        resourceDirectory.setValueType(valueNormal.getValueType());
                        // set value index
                        resourceDirectory.setValueIndex(valueNormal.getIndex());
                    }
                }

                // add parent references if type is style
                if (type.getTypeName().equals("style")) {
                    int parentId = entry.getValue().parentEntry();
                    if (parentId != 0) {
                        resourceDirectory.setValueType(ValueType.TYPE_REFERENCE);
                        resourceDirectory.setValue(String.format("@0x%1$08x", parentId));
                        resourceDirectory.setValueIndex(-1);
                    }
                }

                root.add(resourceDirectory);
            }
        }
    }

    @NotNull
    private FormatValue formatValue(@NotNull BinaryResourceValue value) {
        if (value.type() == BinaryResourceValue.Type.STRING) {
            return new FormatValue(ValueType.TYPE_STRING, stringPool.getString(value.data()), value.data());
        }

        return DecodeGenUtils.formatValue(value, stringPool);
    }

    public ResourceDirectory getRoot() {
        return root;
    }
}
