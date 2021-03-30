package com.mrikso.arsceditor.valueeditor;

import com.google.common.io.Files;
import com.google.devrel.gmscore.tools.apk.arsc.ResourceTableChunk;

import java.io.File;
import java.io.IOException;

public class ArscWriter {

    private final ResourceTableChunk resourceTableChunk;
    private final File out;

    public ArscWriter(ResourceTableChunk chunk, File out){
        this.resourceTableChunk = chunk;
        this.out = out;
    }

    public void write() throws IOException {
        byte[] bytes = resourceTableChunk.toByteArray();
        Files.write(bytes, out);
    }
}
