package pushsummary.jzs.com.presonalsetting;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by a on 2016/11/21.
 */
public class ImageTools {

    /**
     * 将下载的图片保存到SD卡上
     *
     * @param bm
     * @param name
     *@param fileName  @throws IOException
     */
    public static String saveFile(Bitmap bm,  String fileName,String path) throws IOException {
        if (bm == null || fileName == null)
            return null;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            if(!TextUtils.isEmpty(path)){
                String imgCachePath = path+ "/" + fileName + ".jpg";
                File cacheRoot = new File(imgCachePath);
                cacheRoot.createNewFile();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cacheRoot));
                bm.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                bos.flush();
                bos.close();
                return imgCachePath;
            }
        }
        return null;
    }


}
