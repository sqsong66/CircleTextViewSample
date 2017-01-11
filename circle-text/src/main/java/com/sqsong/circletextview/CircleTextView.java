package com.sqsong.circletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by 青松 on 2016/10/27.
 */

public class CircleTextView extends TextView {

    private static final int[] colors = {
            0xff1abc9c, 0xff16a085, 0xfff1c40f, 0xfff39c12, 0xff2ecc71,
            0xff27ae60, 0xffe67e22, 0xffd35400, 0xff3498db, 0xff2980b9,
            0xffe74c3c, 0xffc0392b, 0xff9b59b6, 0xff8e44ad, 0xffbdc3c7,
            0xff34495e, 0xff2c3e50, 0xff95a5a6, 0xff7f8c8d, 0xffec87bf,
            0xffd870ad, 0xfff69785, 0xff9ba37e, 0xffb49255, 0xffb49255, 0xffa94136
    };

    private String text;
    private Paint mPaint;
    private float textSize;
    private int textColor = -1;
    private int defaultColor;

    public CircleTextView(Context context) {
        this(context, null);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTextView);
        text = typedArray.getString(R.styleable.CircleTextView_text);
        textSize = typedArray.getDimension(R.styleable.CircleTextView_textSize, 18);
        textColor = typedArray.getColor(R.styleable.CircleTextView_textColor, Color.WHITE);
        defaultColor = typedArray.getInt(R.styleable.CircleTextView_backgroundColor, -1);
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mPaint.setTextSize(textSize);
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int height = rect.height();
        int width = rect.width();
        int defaultValue = (Math.max(width, height) * 2);
        int measureWidth = measureDimension(defaultValue, widthMeasureSpec);
        int measureHeight = measureDimension(defaultValue, heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(defaultSize, size);
                break;
            default:
                result = defaultSize;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (defaultColor == -1) {
            defaultColor = colors[new Random().nextInt(colors.length)];
        }
        mPaint.setColor(defaultColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2, mPaint);

        if (textColor == -1) {
            textColor = Color.WHITE;
        }
        mPaint.setColor(textColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(textSize);
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int width = rect.width();
        float textWidth = width;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float baseLine = getMeasuredHeight() / 2 + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
        canvas.drawText(text, (getMeasuredWidth() - textWidth) / 2, baseLine, mPaint);
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            this.text = "A";
        }
        this.text = text.toUpperCase();
        postInvalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = sp2px(getContext(), textSize);
        postInvalidate();
    }

    public void setTextColor(int color) {
        this.textColor = color;
        postInvalidate();
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
