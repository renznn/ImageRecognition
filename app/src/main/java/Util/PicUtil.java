package Util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/3.
 */

public class PicUtil {
    public static String saveBitmapToSDCard(String picName, Bitmap bitmap) {
        try {
            File saveFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + picName
                    + ".png");
            if (!saveFile.exists()) {
                File dir = new File(saveFile.getParent());
                dir.mkdirs();
                saveFile.createNewFile();
            }
            FileOutputStream saveFileOutputStream;
            saveFileOutputStream = new FileOutputStream(saveFile);
            boolean nowbol = bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    saveFileOutputStream);
            saveFileOutputStream.close();
            if(nowbol) {
                return saveFile.getPath();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
