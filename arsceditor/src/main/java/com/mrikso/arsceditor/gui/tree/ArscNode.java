package com.mrikso.arsceditor.gui.tree;

import org.jetbrains.annotations.NotNull;
import com.google.devrel.gmscore.tools.apk.arsc.PackageChunk;
import com.google.devrel.gmscore.tools.apk.arsc.StringPoolChunk;
import com.google.devrel.gmscore.tools.apk.arsc.TypeChunk;
import com.google.devrel.gmscore.tools.apk.arsc.TypeSpecChunk;

import javax.swing.*;

public class ArscNode extends JNode {
    private String name;
    private String packageName;
    private int id;
    private boolean isRootPackage;
    private StringPoolChunk stringPool;
    private PackageChunk packageChunk;
    private TypeSpecChunk typeSpec;
    private TypeChunk typeChunk;
    private String type;

    public ArscNode() {
        super();
        this.name = null;
        this.packageName = null;
    }

    public ArscNode(String name) {
        super(name);
        this.name = name;
        this.packageName = null;
    }

    public ArscNode(String name, String packageName) {
        super(name);
        this.name = name;
        this.packageName = packageName;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @NotNull
    public String getPackageName() {
        return packageName;
    }

    public PackageChunk getPackageChunk() {
        return packageChunk;
    }

    public void setPackageChunk(PackageChunk packageChunk) {
        this.packageChunk = packageChunk;
    }

    public StringPoolChunk getStringPool() {
        return stringPool;
    }

    public void setStringPool(StringPoolChunk stringPool) {
        this.stringPool = stringPool;
    }

    public TypeSpecChunk getTypeSpec() {
        return typeSpec;
    }

    public void setTypeSpec(TypeSpecChunk typeSpec) {
        this.typeSpec = typeSpec;
    }

    public TypeChunk getTypeChunk() {
        return typeChunk;
    }

    public void setType(TypeChunk type) {
        this.typeChunk = type;
    }

    public boolean isRootPackage() {
        return isRootPackage;
    }

    public void setRootPackage(boolean rootPackage) {
        isRootPackage = rootPackage;
    }
}
