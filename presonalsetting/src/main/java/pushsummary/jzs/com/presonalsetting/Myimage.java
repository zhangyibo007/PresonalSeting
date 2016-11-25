package pushsummary.jzs.com.presonalsetting;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * Created by a on 2016/11/22.
 */
public class Myimage extends ImageView {


    private static final int BODER_RADIUS_DEFAULT = 10;
    private static final int TYPE_CIRCLE = 0;
    private static int type = 0;
    private Matrix matrix;
    private Paint paint;
    private int BorderRadius;
    private int width;
    private int radius;
    private BitmapShader mBitmapShader;
    private int TYPE_ROUND=1;

    public Myimage(Context context) {
        this(context, null);
    }

    public Myimage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Myimage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inital(context, attrs);
    }

    private void inital(Context context, AttributeSet attrs) {
        matrix = new Matrix();
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //无锯齿
        paint.setAntiAlias(true);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.RoundAngelImageView);

        //如果没设置圆角的默认值，在这设置默认值为10dp
        BorderRadius = dp2px(BODER_RADIUS_DEFAULT);
        // 默认为Circle
        type = array.getInt(R.styleable.RoundAngelImageView_imagetype, TYPE_CIRCLE);

        array.recycle();

    }

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, getResources().getDisplayMetrics());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(type==TYPE_CIRCLE){
            width=Math.min(getMeasuredHeight(),getMeasuredWidth());
            radius=width/2;
            setMeasuredDimension(width,width);

        }

    }

//    @Override
//    protected void onDraw(Canvas canvas)
//    {
//
//
//        if (getDrawable() == null)
//        {
//            return;
//        }
//        setUpShader();
//
//        if (type == TYPE_ROUND)
//        {
//            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
//                    mBitmapPaint);
//        } else
//        {
//            canvas.drawCircle(radius, radius, radius, paint);
//            // drawSomeThing(canvas);
//        }
//    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh)
//    {
//        super.onSizeChanged(w, h, oldw, oldh);
//        // 圆角图片的范围
//        if (type == TYPE_ROUND)
//            mRoundRect = new RectF(0, 0, getWidth(), getHeight());
//    }

    /**
     * 初始化BitmapShader
     */
    private void setUpShader()
    {
        Drawable drawable = getDrawable();
        if (drawable == null)
        {
            return;
        }

        Bitmap bmp = drawableToBitamp(drawable);
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE)
        {
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = width * 1.0f / bSize;

        } else if (type == TYPE_ROUND)
        {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight()
                    * 1.0f / bmp.getHeight());
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        matrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(matrix);
        // 设置shader
        paint.setShader(mBitmapShader);
    }


    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
        {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

}
