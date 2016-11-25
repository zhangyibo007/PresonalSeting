package pushsummary.jzs.com.presonalsetting;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by a on 2016/11/24.
 */
public class MyTextView extends TextView {

    private int textsize;
    private int textcolor;
    private String text;

    private Paint mpaint;
    private Rect rect;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTextViewStyle, defStyleAttr, 0);
        //获取文本
        text = a.getString(R.styleable.MyTextViewStyle_text);
        if (TextUtils.isEmpty(text)) {
            text = "你好我是文本";
        }
        //获取文字颜色
        textcolor = a.getInt(R.styleable.MyTextViewStyle_textcolor, Color.BLUE);
        //获取文字大小
        textsize = a.getDimensionPixelSize(R.styleable.MyTextViewStyle_textsize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        a.recycle();

        Log.e("TAG", ""+text+"----"+textsize+"======"+textcolor);
        //初始化画笔
        mpaint = new Paint();
        mpaint.setAntiAlias(true);

        mpaint.setTextSize(textsize);
        rect = new Rect();
        mpaint.getTextBounds(text, 0, text.length(), rect);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode=MeasureSpec.getMode(widthMeasureSpec);
        int size=MeasureSpec.getSize(widthMeasureSpec);
        if(mode==MeasureSpec.AT_MOST){

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
          mpaint.setColor(Color.RED);
         canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mpaint);

          mpaint.setColor(Color.YELLOW);
          canvas.drawText(text,getWidth()/2-rect.width()/2,getHeight()/2+rect.height()/2,mpaint);


    }
}
