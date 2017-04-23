package com.example.wolf.testseries.AudioCache;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.wolf.testseries.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by WOLF on 26-04-2015.
 */
public class AudioCache
{
    private File cacheDir;

    public AudioCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), context.getResources().getString(R.string.app_name) + "_audio");
        }
        else
        {
            cacheDir=context.getCacheDir();
        }
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String filename){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
//        String filename=String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;
    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

    public void saveDataToSDCard(String fileName, byte[] data)
    {
        File file = getFile(fileName);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e)
        {
            Log.d("error", "exception --> " + e.getMessage());     // handle exception
        } catch (IOException e) {
            // handle exception
            Log.d("error", "exception --> "+e.getMessage());
        }
    }

    public String getDirectoryAbsolutePath()
    {
       String absolutePath=cacheDir.getAbsolutePath();
       Log.d("path", "absolute Path --> "+absolutePath);
        return absolutePath;
    }

}
