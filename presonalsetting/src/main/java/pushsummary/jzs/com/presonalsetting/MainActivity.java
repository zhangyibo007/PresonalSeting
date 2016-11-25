package pushsummary.jzs.com.presonalsetting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {
    private ImageView icon;

    private PopupWindow popupWindow;
    private Button cancelBtn;
    private Button photoBtn;
    private Button pictureBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopuWindow();

            }
        });
    }

    private void ShowPopuWindow() {
        if (popupWindow == null) {
            View view = View.inflate(MainActivity.this, R.layout.popwindow_get_img, null);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //设置属性
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            //设置点击事件
            cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
            cancelBtn.setOnClickListener(this);
            photoBtn = (Button) view.findViewById(R.id.btn_photo);
            photoBtn.setOnClickListener(this);
            pictureBtn = (Button) view.findViewById(R.id.btn_picture);
            pictureBtn.setOnClickListener(this);
        }

        //在固定位置显示窗口
        View root = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        if (root != null) {
            popupWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
        }

        setActivityTransparent(0.5f);


        //监听popuwinds消失
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setActivityTransparent(1f);
            }
        });

    }


    private void setActivityTransparent(float transparent) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = transparent;
        layoutParams.dimAmount = 1 - transparent;
        getWindow().setAttributes(layoutParams);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    private void initView() {
        icon = (ImageView) findViewById(R.id.icon);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:  //取消
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                break;

            case R.id.btn_photo:  // 拍照
                doTakePhoto();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                break;

            case R.id.btn_picture: //相册
                doPickPhotoFromGallery();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }

                break;
        }

    }

    public void showToast(String text, int duration) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(this, text, duration).show();
        }
    }

    private static final String photoCacheFile = "temp.jpg";
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";

    /**
     * 从相册取图片
     */
    public void doPickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, PHOTOZOOM);
    }


    protected void doTakePhoto() {
        if (!DeviceTool.hasSDCard()) {
            showToast("sd卡不存在，暂时无法拍照", Toast.LENGTH_SHORT);
            return;
        }
        File imgPath = MainActivity.this.getExternalFilesDir(null);

        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(imgPath.toString() + "/" + photoCacheFile);
            Uri uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, PHOTOHRAPH);
        } catch (Exception e) {

        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PHOTOHRAPH:
                if (resultCode != Activity.RESULT_OK) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                // 设置文件保存路径这里放在跟目录下
                try {
                    File imgPath = MainActivity.this.getExternalFilesDir(null);

                    if (TextUtils.isEmpty(imgPath.toString())) {
                        showToast("抱歉,照片存储失败", 0);
                        return;
                    }
                    File picture = new File(imgPath.toString() + "/" + photoCacheFile);
                    Uri uri = Uri.fromFile(picture);
                    startPhotoZoom(uri);
                } catch (Exception e) {

                }
                break;

            case PHOTOZOOM:
                if (data == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case PHOTORESOULT:
                if (data == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    if (photo == null) {
                        super.onActivityResult(requestCode, resultCode, data);
                        return;
                    } else {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                    }
                    icon.setImageBitmap(photo);
                    String imgFile = null;// 上传图片
                    try {
                        imgFile = ImageTools.saveFile(photo, "g" + System.currentTimeMillis(), MainActivity.this.getExternalFilesDir(null).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (imgFile != null) {
                        //上传到服务器
                    }
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
