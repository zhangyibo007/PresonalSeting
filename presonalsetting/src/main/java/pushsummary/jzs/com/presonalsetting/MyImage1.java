package pushsummary.jzs.com.presonalsetting;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * Created by a on 2016/11/23.
 */
public class MyImage1 extends ImageView {

    private int type; //图片的类型
    private static final int TYPE_CIRCLE = 0; //圆形
    private static final int TYPE_ROUND = 1;//矩形
    private int mradius; //圆角弧度
    private Bitmap msrc; //原始图片
    private int mwidth; //图片的宽度
    private int mheight;//图片的高度

    public MyImage1(Context context) {
        this(context, null);
    }

    public MyImage1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImage1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取自定属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyImage1, defStyleAttr, 0);
        int n = a.getIndexCount();
        Log.e("TAG", "=======" + n);

        for (int i = 0; i < n; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.MyImage1_type:
                    type = a.getInt(index, TYPE_CIRCLE);
                    break;
                case R.styleable.MyImage1_radius:
                    mradius = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, getResources().getDisplayMetrics()));
                    break;

                case R.styleable.MyImage1_src:
                    msrc = BitmapFactory.decodeResource(getResources(), a.getResourceId(index, 0));
                    break;
            }

        }

        a.recycle();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //设置宽度
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        Log.e("TAG", "getmessage" + getMeasuredWidth());
        Log.e("TAG", "width======" + msrc.getWidth() + "===" + getPaddingLeft() + "r" + getPaddingRight());
        Log.e("TAG", "height====" + specSize);
        if (specMode == MeasureSpec.EXACTLY) {
            mwidth = specSize;
        } else {
            int imagewidth = getPaddingLeft() + getPaddingRight() + msrc.getWidth();

            if (specMode == MeasureSpec.AT_MOST) {
                mwidth = Math.min(imagewidth, specSize);
            } else {
                mwidth = imagewidth;
            }

        }

        //设置高度

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mheight = specSize;
        } else {
            int imageheght = getPaddingBottom() + getPaddingTop() + msrc.getHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                mheight = Math.min(imageheght, specSize);
            } else {
                mheight = specSize;
            }
        }
        setMeasuredDimension(mwidth, mheight);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        switch (type) {
            case TYPE_CIRCLE:
                int min = Math.min(mheight, mwidth);
                msrc = Bitmap.createScaledBitmap(msrc, min, min, false);
                canvas.drawBitmap(createCircleImage(msrc, min), 0, 0, null);

                break;
            case TYPE_ROUND:
                msrc = Bitmap.createScaledBitmap(msrc, mwidth, mheight, false);
                canvas.drawBitmap(createRoundConerImage(msrc), 0, 0, null);
                break;
        }


    }

    /**
     * 根据原图添加圆角
     *
     * @param source
     * @return
     */
    private Bitmap createRoundConerImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(mwidth, mheight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rect, mradius, mradius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN，参考上面的说明
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}
