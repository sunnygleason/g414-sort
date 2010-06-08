package com.g414.sort;

import java.io.IOException;
import java.io.InputStream;


/**
 * And provider for {@link FileLinesDataSource}
 */
public class LineBasedDataSourceProvider
    implements SortableDataSourceProvider<String>
{
    final String encoding;
    
    public LineBasedDataSourceProvider() { this("UTF-8"); }
    public LineBasedDataSourceProvider(String e) { encoding = e; }
    
    public SortableDataSource<String> createDataSource(InputStream in) throws IOException
    {
        if (in == null) return null;
        return new LineBasedDataSource(in ,encoding);
    }
}