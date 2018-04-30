package clear.itsen.com.clearviewdemo;

import android.graphics.Canvas;

/**
 * Created by zhoud on 2018/4/30.
 */

public abstract class ClearViewAnim {
    /**
     * 执行绘画
     *
     * @param canvas 画布
     */
    public abstract void drawState(Canvas canvas);

    /**
     * 执行动画
     */
    public abstract void startAnim();

    /**
     * 停止动画
     */
    public abstract void stopAnim();
}
