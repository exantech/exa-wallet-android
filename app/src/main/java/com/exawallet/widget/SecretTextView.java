package com.exawallet.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import com.devspark.robototextview.widget.RobotoTextView;

import java.util.List;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.createBitmap;
import static android.graphics.Paint.Style.FILL;
import static java.lang.Math.random;

public class SecretTextView extends RobotoTextView {
    private static int sCount;
    private int count = sCount++;
    private static boolean useSpyProtect;
    private boolean useSpyView;
    private static List<String> sWordList;
    private static Bitmap[] sShadow;

    private Bitmap mBackground;
    private Paint mPaint;

    public SecretTextView(Context context) {
        super(context);

        init(null);
    }

    public SecretTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public SecretTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == mBackground) {
            if (null == sShadow) {
                sShadow = new Bitmap[sWordList.size()];
                synchronized (sShadow) {
                    for (int index = 0; index < sWordList.size(); index++) {
                        drawText(sShadow[index] = createBitmap(getWidth(), getHeight(), ARGB_8888), sWordList.get(index));
                    }
                }
            }

            for (int index = 0; index < sWordList.size(); index++) {
                if (sWordList.get(index).equals(getText())) {
                    mBackground = sShadow[index];
                    useSpyView = useSpyProtect;
                }
            }

            if (null == mBackground) {
                useSpyView = false;
                drawText(mBackground = createBitmap(getWidth(), getHeight(), ARGB_8888), getText().toString());
            }
        }

        canvas.drawBitmap(!useSpyView || 0 != count++ % 10 ? mBackground : sShadow[(int) (sWordList.size() * random())], 0, 0, mPaint);

        invalidate();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);

        mBackground = null;
    }

    private void drawText(Bitmap bitmap, String text) {
        Rect bounds = new Rect();
        Paint paint = new Paint();

        paint.setStyle(FILL);
        paint.setColor(getCurrentTextColor());
        paint.setTypeface(getTypeface());
        paint.setTextSize(getTextSize());

        paint.getTextBounds(text, 0, text.length(), bounds);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, (getWidth() - bounds.width()) / 2f, (getHeight() + bounds.height()) / 2f, paint);
        canvas.save();
    }


    public static void initShadowCollector(List<String> words, boolean useSpyProtect) {
        sShadow = null;
        sWordList = words;
        SecretTextView.useSpyProtect = useSpyProtect;
    }
}