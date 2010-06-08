package com.g414.sort.impl;

import java.io.File;
import java.io.IOException;

import com.g414.sort.api.*;

/**
 * Default {@link TempFileProvider} implementation which uses JDK default
 * temporary file generation mechanism.
 * 
 * @author tatu
 */
public class StandardTempFileProvider
    implements TempFileProvider
{
    @Override
    public File provide(String prefix, String suffix) throws IOException
    {
        File f = File.createTempFile(prefix, suffix);
        f.deleteOnExit();
        return f;
    }
}
