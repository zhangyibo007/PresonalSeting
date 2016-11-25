package pushsummary.jzs.com.presonalsetting;

import android.os.Environment;

/**
 * Created by a on 2016/11/21.
 */
public class DeviceTool {

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }
}
