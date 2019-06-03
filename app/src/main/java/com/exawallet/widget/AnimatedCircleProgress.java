package com.exawallet.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.exawallet.monerowallet.R;

import static android.graphics.Paint.Style.STROKE;

public class AnimatedCircleProgress extends View {
    private Paint mPaint;
    private float mStrokeWidth;
    private float mIntVelocity;
    private float mExtVelocity;
    private float mStartAngle;
    private float mSweepAngle;
    private boolean mPhase = true;

    public AnimatedCircleProgress(Context context) {
        super(context);
        init(context, null);
    }

    public AnimatedCircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnimatedCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public AnimatedCircleProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mStrokeWidth = context.getResources().getDimension(R.dimen.size10);

        mPaint = new Paint();
        mPaint.setStyle(STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(context.getResources().getColor(R.color.white));

        mIntVelocity = 2.0f;
        mExtVelocity = 6.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPhase) {
            mStartAngle = extractAngle(mStartAngle + mIntVelocity);
            mSweepAngle = extractAngle(mSweepAngle + (mExtVelocity - mIntVelocity));

            mPhase = 360 > mSweepAngle + (mExtVelocity - mIntVelocity);
        } else {
            mStartAngle = extractAngle(mStartAngle + mExtVelocity);
            mSweepAngle = extractAngle(mSweepAngle - (mExtVelocity - mIntVelocity));

            mPhase = 0 > mSweepAngle - (mExtVelocity - mIntVelocity);
        }

        canvas.drawArc(mStrokeWidth, mStrokeWidth, getWidth() - mStrokeWidth, getHeight() - mStrokeWidth, mStartAngle, mSweepAngle, false, mPaint);

        postDelayed(this::postInvalidate, 10);
    }

    private float extractAngle(float angle) {
        float result = angle;

        while (360 < result) {
            result -= 360;
        }

        return result;
    }
}