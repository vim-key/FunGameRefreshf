package com.zuck.swipe.hitblockrefresh.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Hitomis on 2016/3/09.
 * email:196425254@qq.com
 */
public class BattleCityView extends FunGameView {

    private static final String tag = "BattleCityView";

    private static int TANK_ROW_NUM = 3;

    static final float TANK_BARREL_RATIO = 1/3.f;

    static final float BULLET_NUM_RATIO = .4f;

    static final int TANK_EXTRA_SPACING = 10;

    private SparseArray<Queue<RectF>> eTankSparseArray;

    private Queue<Point> mBulletList;

    private Point usedBullet;

    private Random random;

    private float heightSize;

    private float bulletRadius;

    private int barrelSize, tankSpaceSize, bulletSpaceSize;

    private int enemySpeed = 2, bulletSpeed = 7;

    private int offsetETankX, offsetMBulletX;

    private boolean isAdditional, isLaunch;

    public BattleCityView(Context context) {
        this(context, null);
    }

    public BattleCityView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BattleCityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initConcreteView() {
        random = new Random();

        heightSize = screenHeight * VIEW_HEIGHT_RATIO;
        controllerSize = (int) (Math.floor((heightSize - (TANK_ROW_NUM + 1) * DIVIDING_LINE_SIZE) / TANK_ROW_NUM + .5f));
        barrelSize = (int) Math.floor(controllerSize * TANK_BARREL_RATIO + .5f);
        tankSpaceSize = controllerSize + barrelSize + TANK_EXTRA_SPACING;
        bulletSpaceSize = (int) (screenWidth * BULLET_NUM_RATIO);

        bulletRadius = (barrelSize - 2 * DIVIDING_LINE_SIZE) * .5f;

        resetConfigParams();
    }

    @Override
    protected void drawGame(Canvas canvas) {
        drawEnemyTank(canvas);
        drawSelfTank(canvas);
        if (status >=STATUS_GAME_PLAY && status<= STATUS_GAME_FINISHED)
            makeBulletPath(canvas);
    }

    @Override
    protected void resetConfigParams() {
        controllerPosition = DIVIDING_LINE_SIZE;

        eTankSparseArray = new SparseArray<>();
        int option = apperanceOption();
        for (int i = 0; i < TANK_ROW_NUM; i++) {
            Queue<RectF> rectFQueue = new LinkedList<>();
            if (i == option) {
                rectFQueue.offer(generateEnemyTank(i));
            }
            eTankSparseArray.put(i, rectFQueue);
        }

        mBulletList = new LinkedList<>();
    }

    private RectF generateEnemyTank(int index) {
        float left = - (controllerSize + barrelSize);
        float top = index * (controllerSize + DIVIDING_LINE_SIZE) + DIVIDING_LINE_SIZE ;
        return new RectF(left, top, left + controllerSize, top + controllerSize);
    }

    private void makeBulletPath(Canvas canvas) {
        offsetMBulletX += bulletSpeed;
        if (offsetMBulletX / bulletSpaceSize == 1) {
            isLaunch = true;
            offsetMBulletX = 0;
        } else {
            isLaunch = false;
        }

        if (isLaunch) {
            Point bulletPoint = new Point();
            bulletPoint.x = screenWidth - controllerSize - barrelSize;
            bulletPoint.y = (int) (controllerPosition + controllerSize * .5f);
            mBulletList.offer(bulletPoint);
        }

        boolean isOversetp = false;
        for (Point point : mBulletList) {
            if (checkWipeOutETank(point)) {
                usedBullet = point;
                continue;
            }
            if (point.x + bulletRadius <= 0) {
                isOversetp = true;
            }
            drawBullet(canvas, point);
        }

        if (isOversetp) {
            mBulletList.poll();
        }

        mBulletList.remove(usedBullet);
        usedBullet = null;
    }

    private boolean checkWipeOutETank(Point point) {
        boolean beHit = false;
        int index = point.y / (getMeasuredHeight() / TANK_ROW_NUM);
        index = index == TANK_ROW_NUM ? TANK_ROW_NUM - 1 : index;
        index = index == -1 ? 0 : index;
        Queue<RectF> rectFQueue = eTankSparseArray.get(index);
        for (RectF rectF : rectFQueue) {
            if (rectF.contains(point.x, point.y)) { // 击中
                beHit = true;
                break;
            }
        }
        if (beHit) {
            rectFQueue.poll();
        }
        return beHit;
    }

    private void drawBullet(Canvas canvas, Point point) {
        point.x -= bulletSpeed;
        canvas.drawCircle(point.x, point.y, bulletRadius, mPaint);
    }

    private void drawSelfTank(Canvas canvas) {
        canvas.drawRect(screenWidth - controllerSize, controllerPosition, screenWidth, controllerPosition + controllerSize, mPaint);
        canvas.drawRect(screenWidth - controllerSize - barrelSize,
                controllerPosition + (controllerSize - barrelSize) * .5f,
                screenWidth - controllerSize,
                controllerPosition + (controllerSize - barrelSize) * .5f + barrelSize,
                mPaint);
    }


    private void drawEnemyTank(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#909090"));
        offsetETankX += enemySpeed;
        if (offsetETankX / tankSpaceSize == 1) {
            isAdditional = true;
            offsetETankX = 0;
        } else {
            isAdditional = false;
        }

        boolean isOversetp = false;
        int option = apperanceOption();

        for (int i = 0; i < TANK_ROW_NUM; i++) {
            Queue<RectF> rectFQueue = eTankSparseArray.get(i);

            if (isAdditional && i == option) {
                rectFQueue.offer(generateEnemyTank(i));
            }

            for (RectF rectF : rectFQueue) {
                if (rectF.left >= screenWidth) {
                    isOversetp = true;
                    continue;
                }
                drawTank(canvas, rectF);
            }
            if (isOversetp) {
                rectFQueue.poll();
                isOversetp = false;
            }
        }
        invalidate();
    }

    private void drawTank(Canvas canvas, RectF rectF) {
        rectF.set(rectF.left + enemySpeed, rectF.top, rectF.right + enemySpeed, rectF.bottom);
        canvas.drawRect(rectF, mPaint);
        float barrelTop = rectF.top + (controllerSize - barrelSize) * .5f;
        canvas.drawRect(rectF.right, barrelTop, rectF.right + barrelSize, barrelTop + barrelSize, mPaint);

    }

    private int apperanceOption() {
        return random.nextInt(TANK_ROW_NUM);
    }

}
