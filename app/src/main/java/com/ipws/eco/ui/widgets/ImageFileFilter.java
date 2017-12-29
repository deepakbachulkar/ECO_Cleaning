package com.ipws.eco.ui.widgets;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by ziffi on 9/6/17.
 */

public class ImageFileFilter implements FileFilter
{

    private final String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};

    /**
     *
     */
    public ImageFileFilter()
    {}

    public boolean accept(File file)
    {
//        Log.d("Ziffi", "Path:"+file.getName()+ " | "+file.getAbsolutePath());
        for (String extension : okFileExtensions)
        {
            if (file.getPath().toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }

}