package clear.itsen.com.clearviewdemo;

import java.util.Random;

/**
 * Created by zhoud on 2018/4/30.
 */

public class Utils {
    static Random random = new Random();
    private final static int NUM = 40;
    private final static int MIN_NUM = 15;

    /**
     * 执行程度
     *
     * @return 0-1
     */
    public static float getPercnet() {
        return random.nextInt(100) / 100.f;
    }


    /**
     * @param value 0-value
     * @return
     */
    public static int getIntPer(int value) {
        return random.nextInt(value);
    }

    /**
     * @return
     */
    public static int getNum() {
        return Math.max(random.nextInt(NUM), MIN_NUM);
    }

    public static float getStopPercent() {
        return (random.nextInt(50) + 50) / 100.f;
    }

    public static float getStartPercent() {
        return random.nextInt(40) / 100.f;
    }
}
