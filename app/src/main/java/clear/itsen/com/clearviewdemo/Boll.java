package clear.itsen.com.clearviewdemo;

/**
 * Created by zhouds on 2018/4/30.
 */

public class Boll {
    /**
     * 坐标x
     */
    float x;
    /**
     * 坐标y
     */
    float y;
    /**
     * 半径
     */
    int radius;
    /**
     * 透明度 0-255
     */
    int alpha;
    /**
     * 颜色
     */
    int color;
    /**
     * 到此程度执行移动动画
     */
    float percent;
    /**
     * 到此程度停止
     */
    float stopPercent;
    /**
     * 移动的距离
     */
    int distance;

    /**
     * 角度
     */
    float angle;

    /**
     *  计算坐标
     * @param percent 0-1
     * @param bgRadius
     * @param cx
     * @param cy
     */
    public void setXY(float percent,int bgRadius,float cx,float cy){
        if (this.percent<=percent&&percent<=stopPercent){
            this.x = (float) ((distance*(percent-this.percent)+bgRadius)* Math.cos(angle)+cx);
            this.y = (float) ((distance*(percent-this.percent)+bgRadius)* Math.sin(angle)+cy);
        }else {
            this.x = cx;
            this.y = cy;
        }
    }
}
