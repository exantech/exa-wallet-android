package com.exawallet.widget;

import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.exawallet.monerowallet.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class Chart extends View {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
    private final int mGradientFromColor;
    private final int mGradientToColor;

    private Path mStrokePath = new Path();
    private Paint mStrokePaint = new Paint();

    private Path mGradientPath = new Path();
    private Paint mGradientPaint = new Paint();

    private Path mGridPath = new Path();
    private Paint mGridPaint = new Paint();

    private Paint mTextPaint = new Paint();

    private ChartDataProvider mDataProvider;

    private long mOffsetX;
    private float mScaleX;

    private float mOffsetY;
    private float mScaleY;
    private int mMaxY;
    private int mMaxX;

    public Chart(Context context) {
        this(context, null);
    }

    public Chart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Chart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initPaint(mStrokePaint, ContextCompat.getColor(context, R.color.blue), getResources().getDimension(R.dimen.size4), Paint.Style.STROKE);
        initPaintDash(mGridPaint, ContextCompat.getColor(context, R.color.green), getResources().getDimension(R.dimen.size4) / 2, getResources().getDimension(R.dimen.size20), Paint.Style.STROKE);
        initPaintText(mTextPaint, ContextCompat.getColor(context, R.color.black), 0, getResources().getDimension(R.dimen.text22), Paint.Style.STROKE);

        mGradientFromColor = ContextCompat.getColor(context, R.color.gradient_from_color);
        mGradientToColor = ContextCompat.getColor(context, R.color.gradient_to_color);

        mMaxX = 3;
        mMaxY = 8;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mGradientPaint.setShader(new LinearGradient(0.0F, 0.0F, 0.0F, getHeight() + 0.0F, mGradientFromColor, mGradientToColor, Shader.TileMode.MIRROR));
    }

    private void initPaint(Paint paint, int color, float width, Paint.Style style) {
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(width);
        paint.setStyle(style);
    }

    private void initPaintDash(Paint paint, int color, float dimension, float dimension1, Paint.Style stroke) {
        initPaint(paint, color, dimension, stroke);
        paint.setPathEffect(new DashPathEffect(new float[]{dimension1, dimension1}, 0));
    }

    private void initPaintText(Paint paint, int color, float dimension, float dimension1, Paint.Style stroke) {
        initPaint(paint, color, dimension, stroke);
        paint.setTextSize(dimension1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (null != mDataProvider && mDataProvider.isInited()) {
            processScaleX(mDataProvider.getMinTime(), mDataProvider.getMaxTime());
            processScaleY(mDataProvider.getMinValue(), mDataProvider.getMaxValue());

            drawChart(canvas);
            drawGrid(canvas);
        }
    }

    private void drawGrid(Canvas canvas) {
        for (int index = 1; index < mMaxY; index++) {
            mGridPath.reset();
            int y = getHeight() / mMaxY * index;

            mGridPath.moveTo(0, y);
            mGridPath.lineTo(getWidth(), y);

            canvas.drawPath(mGridPath, mGridPaint);
            canvas.drawText(String.valueOf(scale1Y(y)), 0, y, mTextPaint);
        }

        for (int index = 1; index < mMaxX; index++) {
            mGridPath.reset();
            int x = getWidth() / mMaxX * index;

            mGridPath.moveTo(x, 0);
            mGridPath.lineTo(x, getHeight());

            canvas.drawPath(mGridPath, mGridPaint);
            canvas.drawText(DATE_FORMAT.format(new Date(scale1X(x))), x, getHeight(), mTextPaint);
        }

        canvas.drawText(DATE_FORMAT.format(new Date(scale1X(0))), 0, getHeight(), mTextPaint);
    }

    private void drawChart(Canvas canvas) {
        Iterator<ChartData> iterator = mDataProvider.getIterator();

        mStrokePath.reset();
        mStrokePath.moveTo(0, getHeight());

        mGradientPath.reset();
        mGradientPath.moveTo(0, getHeight());

        while (iterator.hasNext()) {
            ChartData chartData = iterator.next();

            float x = scaleX(chartData.getTime());
            float y = scaleY(chartData.getValue());

            mStrokePath.lineTo(x, y);
            mGradientPath.lineTo(x, y);
        }

        mGradientPath.lineTo(getWidth(), getHeight());
        mGradientPath.lineTo(0, getHeight());

        canvas.drawPath(mStrokePath, mStrokePaint);
        canvas.drawPath(mGradientPath, mGradientPaint);
    }

    private float scaleX(long time) {
        return (time - mOffsetX) * mScaleX;
    }

    private long scale1X(float value) {
        return (long) (value / mScaleX + mOffsetX);
    }

    private float scaleY(double value) {
        return getHeight() - (float) ((value - mOffsetY) * mScaleY);
    }

    private float scale1Y(double value) {
        return (float) ((getHeight() - value) / mScaleY + mOffsetY);
    }

    private void processScaleX(long minTime, long maxTime) {
        mOffsetX = minTime;
        mScaleX = getWidth() / (float) (maxTime - minTime);
    }

    private void processScaleY(float minValue, float maxValue) {
        mOffsetY = minValue;
        mScaleY = getHeight() / (maxValue - minValue);
    }

    public void setDataProvider(ChartDataProvider dataProvider) {
        mDataProvider = dataProvider;
    }
}