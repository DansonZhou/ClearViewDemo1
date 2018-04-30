package clear.itsen.com.clearviewdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoud on 2018/4/28.
 */

public class ClearView extends View {

    /**
     * 大圆背半径
     */
    private int mRadius;
    /**
     * 小圆的半径
     */
    private int mRadiusBall;
    /**
     * View的中心坐标X
     */
    private float mCenterX;
    /**
     * View的中心坐标Y
     */
    private float mCenterY;
    /**
     * 颜色数组
     */
    private int[] mCircleColors;
    /**
     * 最大角度差
     */
    private float mRotationAngle;
    /**
     * 旋转角度
     */
    private float mCurrentRotationAngle = 0;
    /**
     * 当前角度差
     */
    private float mCurrentDAngle = 0;
    /**
     * 最小宽度/高度
     */
    private int mMinHeightAndWidth = 80;
    /**
     * 动画时间
     */
    private final static int ANIM_TIME = 1000;
    /**
     * 初始状态
     */
    private final static int STATE_INIT = 1;
    /**
     * 清理运行状态
     */
    private final static int STATE_RUNNING = 2;
    /**
     * 清理完成状态
     */
    private final static int STATE_FINISH = 3;
    /**
     * 当前状态
     */
    private int currentState;
    private int mColorRed;
    private int mColorGreen;
    private float mDycenter = 1;
    /**
     * 开关 1 - 0
     */
    private int onOff;
    private final static int ON = 1;
    private final static int OFF = 0;
    private Paint mPaint;
    private ValueAnimator valueAnimator;
    private ValueAnimator valueAnimDangle;
    private List<Boll> list = new ArrayList();
    private int mRadiusListBall;
    private ClearViewAnim mViewAnim;

    public ClearView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mCircleColors = context.getResources().getIntArray(R.array.clear_circle_colors);
        mColorRed = context.getResources().getColor(R.color.orangered);
        mColorGreen = context.getResources().getColor(R.color.limegreen);
        mRadiusBall = DensityUtils.dp2px(8);
        mRadiusListBall = DensityUtils.dp2px(15);
        currentState = STATE_INIT;
        onOff = OFF;
        // 角度差 = 180度/小圆个数
        mRotationAngle = (float) (Math.PI / mCircleColors.length);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minHW = DensityUtils.dp2px(mMinHeightAndWidth);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        height = Math.min(width, height);
        height = height > minHW ? height : minHW;
        mRadius = height / 4;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mViewAnim == null) {
            mViewAnim = new InitAnim(STATE_INIT);
        }
        mViewAnim.drawState(canvas);
    }

    class InitAnim extends ClearViewAnim {
        public InitAnim(int status) {
            currentState = status;
            onOff = ON;
            valueAnimator = ValueAnimator.ofFloat(0, -mRadius);
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mDycenter = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mDycenter = 1;
                    mViewAnim = new RunningAinm();
                }
            });
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackground(canvas);
        }

        @Override
        public void startAnim() {
            valueAnimator.start();
        }

        @Override
        public void stopAnim() {
            valueAnimator.cancel();
        }
    }

    class RunningAinm extends ClearViewAnim {
        public RunningAinm() {
            currentState = STATE_RUNNING;
            //控制整体旋转
            float angle = (float) (2 * Math.PI);
            valueAnimator = ValueAnimator.ofFloat(0, angle);
            valueAnimator.setDuration(ANIM_TIME);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationAngle = (float) animation.getAnimatedValue();
                }
            });
            //控制小球间距
            valueAnimDangle = ValueAnimator.ofFloat(0, 1, 0);
            valueAnimDangle.setDuration(ANIM_TIME);
            valueAnimDangle.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimDangle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentDAngle = (float) animation.getAnimatedValue() * mRotationAngle;
                    invalidate();
                }
            });
            valueAnimator.setStartDelay(200);
            valueAnimDangle.setStartDelay(200);
            startAnim();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBoll(canvas);
        }

        @Override
        public void startAnim() {
            valueAnimator.start();
            valueAnimDangle.start();
        }

        @Override
        public void stopAnim() {
            valueAnimator.cancel();
            valueAnimDangle.cancel();
            mCurrentDAngle = (float) (2 * Math.PI / mCircleColors.length);
            invalidate();
            mViewAnim = new FinishAinm();

        }
    }

    class FinishAinm extends ClearViewAnim {
        public FinishAinm() {
            valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(200);
            valueAnimator.setStartDelay(200);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mDycenter = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mCurrentRotationAngle = 0;
                    mCurrentDAngle = 0;
                    mViewAnim = new ExpendAmin();
                }
            });
            startAnim();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBoll(canvas);
        }

        @Override
        public void startAnim() {
            valueAnimator.start();
        }

        @Override
        public void stopAnim() {

        }
    }

    class ExpendAmin extends ClearViewAnim {
        public ExpendAmin() {
            creatBoll();
            currentState = STATE_FINISH;
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new FastOutLinearInInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mDycenter = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            //扩散小球动画
            valueAnimDangle = ValueAnimator.ofFloat(0, 1);
            valueAnimDangle.setDuration(500);
            valueAnimDangle.setInterpolator(new FastOutSlowInInterpolator());
            valueAnimDangle.setStartDelay(300);
            valueAnimDangle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percnet = (float) animation.getAnimatedValue();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setXY(percnet, mRadius, mCenterX, mCenterY);
                    }
                    invalidate();
                }
            });
            startAnim();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawListBoll(canvas);
        }

        @Override
        public void startAnim() {
            valueAnimDangle.start();
            valueAnimator.start();
        }

        @Override
        public void stopAnim() {
            valueAnimator.start();
            valueAnimDangle.start();
        }
    }

    /**
     * 画运动的圆
     *
     * @param canvas
     */
    private void drawBoll(Canvas canvas) {
        for (int i = 0; i < mCircleColors.length; i++) {
            mPaint.setColor(mCircleColors[i]);
            //每个小圆的角度 间距角度+当前旋转角度 - 90度偏移到顶部
            double angle = i * mCurrentDAngle + mCurrentRotationAngle - Math.PI / 2;
            //计算每个圆的坐标
            int cx = (int) (mDycenter * (mRadius + mRadiusBall) * Math.cos(angle) + mCenterX);
            int cy = (int) (mDycenter * (mRadius + mRadiusBall) * Math.sin(angle) + mCenterY);
            canvas.drawCircle(cx, cy, mRadiusBall, mPaint);
        }
    }

    /**
     * 画背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        if (currentState == STATE_INIT) {
            mPaint.setColor(mColorRed);
        }
        if (currentState == STATE_FINISH) {
            mPaint.setColor(mColorGreen);
        }
        canvas.drawCircle(mCenterX, mCenterY + mDycenter * onOff, mRadius + (mDycenter + mRadiusBall) * onOff, mPaint);
    }

    /**
     * 画相关扩散的球
     *
     * @param canvas
     */
    private void drawListBoll(Canvas canvas) {
        if (currentState == STATE_FINISH) {
            for (int i = 0; i < list.size(); i++) {
                Boll boll = list.get(i);
                mPaint.setColor(boll.color);
                mPaint.setAlpha(boll.alpha);
                canvas.drawCircle(boll.x, boll.y, boll.radius, mPaint);
            }
        }
    }

    private void creatBoll() {
        //清空上一次的小球
        list.clear();
        int num = Utils.getNum();
        for (int i = 0; i < num; i++) {
            Boll boll = new Boll();
            boll.radius = Utils.getIntPer(mRadiusListBall);
            boll.alpha = Utils.getIntPer(255);
            boll.distance = (int) (Utils.getPercnet() * 4 * mRadius);
            boll.percent = Utils.getStartPercent();
            boll.stopPercent = Utils.getStopPercent();
            boll.color = mColorGreen;
            boll.angle = (float) (Utils.getPercnet() * 2 * Math.PI);
            boll.setXY(0, mRadius, mCenterX, mCenterY);
            list.add(boll);
        }
    }

    public void runningAinm() {
        if (mViewAnim instanceof InitAnim) {
            mViewAnim.startAnim();
        }
    }

    public void finishAinm() {
        if (mViewAnim instanceof RunningAinm) {
            mViewAnim.stopAnim();
        }
    }

    public void initAnim() {
        if (mViewAnim instanceof ExpendAmin) {
            mViewAnim = new InitAnim(STATE_INIT);
            invalidate();
        }
    }

}
