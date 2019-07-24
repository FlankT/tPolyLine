package com.cd.t.polyline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by TU on 2018/11/30.
 * Custom double fold line
 */
public class PolyLine extends View {
    private List<Float> lineDatas;//第一条折线数据
    private List<Float> secondLineDatas;//第二条折线数据

    //获取view宽高
    private int measuredHeight;
    private int measuredWidth;

    private int widthText;//右侧Y轴文字宽度
    private int heightText;//右侧Y轴文字高度
    private float yMax;//最大值 (基准值)

    private float yMin;//最小值 (基准值)

    private int maxDataSize;

    private float[] y_TextArray;//刻度分割后的数据组,六条虚线分6个
    private float split_difference; // 刻度分割差值

    private Paint mTxtPaintY;//Y轴刻度

    private Paint mDashLinePaint;//背景虚线

    private Paint mTimingLinePaint;//折线一

    private Paint mTimingLinePaintSecond;//折线二

    //画笔:实时横线右侧的红色的框和实时数据
    private Paint mTimingTxtBgPaint;//实时数据的背景
    private Paint mTimingTxtPaint;//实时数据文本

    private Paint mTimingTxtPaintSecond;
    private Paint mTimingTxtBgPaintSecond;

    //图形距离顶部距离(防止第一条线被视图覆盖,大概一个状态栏高度)
    private int linePaddingTop;

    //文本框矩形
    private int textRectRight;
    private int textRectBottom;
    private int textRectTop;
    private int textRectLeft;

    public PolyLine(Context context) {
        this(context, null);
    }

    public PolyLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolyLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //关闭硬件加速 显示虚线
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        lineDatas = new ArrayList<>();
        secondLineDatas = new ArrayList<>();
        y_TextArray = new float[6];

        //测试基准值
        yMax = 26.225f;
        yMin = 26.225f;
        split_difference = 0.005f;

        initPaint();
        initDefAttrs();//初始化间距
        getTextBoundsY(); //获取y轴文字宽高
        getOffsetY();   //模拟y轴坐标刻度

    }

    private void initPaint() {
        initYTxPaint();//Y轴刻度
        initDashLinePaint();//背景虚线
        initTimingTxtPaint();//实时文本 和文本 背景
        initTimingLinePaint();//实时折线(包含实时横线)

        //第二条折线
        initTimingTxtPaint_Second();
        initTimingLinePaint_Second();
    }

    /**
     * 初始化相关
     */
    private void initDefAttrs() {
        measuredHeight = 400;
        measuredWidth = 1080;

        maxDataSize = 50;

        linePaddingTop = 45;

        textRectRight = 28;
        textRectBottom = 70;
        textRectTop = 20;
        textRectLeft = 100;
    }

    private void getTextBoundsY() {
        Paint paint = new Paint();
        Rect rect = new Rect();
        String yMaxstr = String.valueOf(yMax);
        paint.getTextBounds(yMaxstr, 0, yMaxstr.length(), rect);
        widthText = rect.width();
        heightText = rect.height();

        //widthText =  paint.measureText(yMaxstr);
    }

    private void getOffsetY() {
        for (int i = 0; i < 6; i++) {
            y_TextArray[i] = yMin;
            yMin = yMin - split_difference;
        }
    }

    private void initTimingLinePaint_Second() {
        mTimingLinePaintSecond = new Paint();
        mTimingLinePaintSecond.setAntiAlias(true);
        mTimingLinePaintSecond.setPathEffect(new CornerPathEffect(10));
        // mTimingLinePaintSecond.setStrokeJoin(Paint.Join.ROUND);
        mTimingLinePaintSecond.setColor(getResources().getColor(R.color.color3));
        mTimingLinePaintSecond.setStyle(Paint.Style.STROKE);
        mTimingLinePaintSecond.setStrokeWidth(3);
    }

    private void initTimingTxtPaint_Second() {
        mTimingTxtPaintSecond = new Paint();
        mTimingTxtPaintSecond.setAntiAlias(true);
        mTimingTxtPaintSecond.setColor(Color.WHITE);
        mTimingTxtPaintSecond.setTextSize(34);
        mTimingTxtPaintSecond.setStrokeWidth(1);
        mTimingTxtPaintSecond.setTextAlign(Paint.Align.CENTER);
        mTimingTxtPaintSecond.setTypeface(Typeface.DEFAULT_BOLD);
        //文本框背景
        mTimingTxtBgPaintSecond = new Paint();
        mTimingTxtBgPaintSecond.setAntiAlias(true);
        mTimingTxtBgPaintSecond.setStrokeWidth(5);
        mTimingTxtBgPaintSecond.setStrokeCap(Paint.Cap.ROUND);
        mTimingTxtBgPaintSecond.setColor(getResources().getColor(R.color.color3));
        mTimingTxtBgPaintSecond.setStyle(Paint.Style.FILL);
    }

    private void initTimingLinePaint() {
        mTimingLinePaint = new Paint();
        mTimingLinePaint.setAntiAlias(true);
        mTimingLinePaint.setPathEffect(new CornerPathEffect(10));
        //mTimingLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mTimingLinePaint.setColor(getResources().getColor(R.color.color4));
        mTimingLinePaint.setStyle(Paint.Style.STROKE);
        mTimingLinePaint.setStrokeWidth(3);
    }

    private void initTimingTxtPaint() {
        mTimingTxtPaint = new Paint();
        mTimingTxtPaint.setAntiAlias(true);
        mTimingTxtPaint.setColor(Color.WHITE);
        mTimingTxtPaint.setTextSize(34);
        mTimingTxtPaint.setStrokeWidth(1);
        mTimingTxtPaint.setTextAlign(Paint.Align.CENTER);
        mTimingTxtPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //文本框背景
        mTimingTxtBgPaint = new Paint();
        mTimingTxtBgPaint.setAntiAlias(true);
        mTimingTxtBgPaint.setStrokeWidth(10);
        mTimingTxtBgPaint.setStrokeCap(Paint.Cap.ROUND);
        mTimingTxtBgPaint.setColor(getResources().getColor(R.color.color4));
        mTimingTxtBgPaint.setStyle(Paint.Style.FILL);
    }

    private void initDashLinePaint() {
        //虚线初始化
        mDashLinePaint = new Paint();
        mDashLinePaint.setAntiAlias(true);
        mDashLinePaint.setStyle(Paint.Style.STROKE);
        mDashLinePaint.setColor(getResources().getColor(R.color.color2));//设置画笔颜色
        mDashLinePaint.setStrokeWidth(2);
        mDashLinePaint.setPathEffect(new DashPathEffect(new float[]{15, 30, 15, 30}, 0));
    }

    private void initYTxPaint() {
        mTxtPaintY = new Paint();
        mTxtPaintY.setAntiAlias(true);
        mTxtPaintY.setTextAlign(Paint.Align.LEFT);
        mTxtPaintY.setTypeface(Typeface.DEFAULT_BOLD);
        mTxtPaintY.setStyle(Paint.Style.FILL);
        mTxtPaintY.setColor(getResources().getColor(R.color.color2));
        mTxtPaintY.setStrokeWidth(1);
        mTxtPaintY.setTextSize(34);
    }

    /**
     * 根据需求 修改
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
//        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
//
//        if (modeWidth == MeasureSpec.EXACTLY) {
//            measuredWidth = sizeWidth;
//        } else {//默认宽度1080dp
//            measuredWidth = 1080;
//        }
//        Log.e("------------->", "width:" + measuredWidth);
//        setMeasuredDimension(measuredWidth, measuredWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("TAG", "onDraw: " + measuredWidth + "," + measuredHeight);
        //todo 还可优化计算方式
        //整个view画布背景
        //canvas.drawColor(getResources().getColor(R.color.color1)); //背景
        //画Y文字
        drawTextY(canvas);
        //循环画5条背景虚线 两点一线
        drawDashLine(canvas);
        //点连线(折线) 画点数据源 第一条折线
        drawLine(canvas);
        //第二条折线
        drawSecondLine(canvas);
    }

    @SuppressLint("DefaultLocale")
    private void drawTextY(Canvas canvas) {
        for (int i = 0; i < 6; i++) {
            canvas.drawText(String.format("%.3f", y_TextArray[i]), measuredWidth - widthText - textRectLeft,
                    (measuredHeight + linePaddingTop - (measuredHeight / (y_TextArray[0] - y_TextArray[5])) *
                            (y_TextArray[i] - y_TextArray[5])), mTxtPaintY);
        }
    }

    private void drawDashLine(Canvas canvas) {
        for (int i = 0; i < 6; i++) {
            float[] pts = {0, (measuredHeight + linePaddingTop - (measuredHeight / (y_TextArray[0] - y_TextArray[5])) *
                    (y_TextArray[i] - y_TextArray[5])), measuredWidth - widthText - textRectLeft,
                    (measuredHeight + linePaddingTop - (measuredHeight / (y_TextArray[0] - y_TextArray[5])) *
                            (y_TextArray[i] - y_TextArray[5]))};//数据
            canvas.drawLines(pts, mDashLinePaint); //绘制多条虚线
        }
    }

    @SuppressLint("DefaultLocale")
    private void drawSecondLine(Canvas canvas) {
        if (secondLineDatas.size() != 0) {
            Path pathLine = new Path();
            pathLine.moveTo(0,
                    (measuredHeight + linePaddingTop - (measuredHeight / (yMax - y_TextArray[5])) * (secondLineDatas.get(0) - y_TextArray[5])));
            for (int i = 1; i <= secondLineDatas.size() - 1; i++) {
                pathLine.lineTo(((measuredWidth - widthText - textRectLeft) / maxDataSize) * i,
                        (measuredHeight + linePaddingTop - (measuredHeight / (yMax - y_TextArray[5])) * (secondLineDatas.get(i) - y_TextArray[5])));
            }
            canvas.drawPath(pathLine, mTimingLinePaintSecond);

            //实时横线二
            canvas.drawLine(((measuredWidth - widthText - textRectLeft) / maxDataSize) * (secondLineDatas.size() - 1),
                    (measuredHeight + linePaddingTop - (measuredHeight / (yMax - y_TextArray[5])) * (secondLineDatas.get(secondLineDatas.size() - 1) - y_TextArray[5])),
                    measuredWidth - widthText - textRectLeft,
                    (measuredHeight + linePaddingTop - (measuredHeight / (yMax - y_TextArray[5])) * (secondLineDatas.get(secondLineDatas.size() - 1) - y_TextArray[5])),
                    mTimingLinePaintSecond);

            //绘制文本-矩形框
            canvas.drawRoundRect(measuredWidth - widthText - textRectLeft, (measuredHeight + textRectTop - (measuredHeight / (yMax - y_TextArray[5]))
                            * (secondLineDatas.get(secondLineDatas.size() - 1) - y_TextArray[5]) - heightText + 5),
                    measuredWidth - textRectRight, (measuredHeight + textRectBottom - (measuredHeight / (yMax - y_TextArray[5]))
                            * (secondLineDatas.get(secondLineDatas.size() - 1) - y_TextArray[5])) + heightText - 5,
                    3, 3, mTimingTxtBgPaintSecond);

            //绘制文本-文字
            canvas.drawText(String.format("%.3f", secondLineDatas.get(secondLineDatas.size() - 1)), measuredWidth - widthText - 45,
                    (measuredHeight + linePaddingTop - (measuredHeight / (yMax - y_TextArray[5]))
                            * (secondLineDatas.get(secondLineDatas.size() - 1) - y_TextArray[5])) + 12, mTimingTxtPaintSecond);

        }
//        else{
//            throw(new NullPointerException("数据为空"));
//        }
    }

    @SuppressLint("DefaultLocale")
    private void drawLine(Canvas canvas) {
        if (lineDatas.size() != 0) {
            Path pathLine = new Path();
            pathLine.moveTo(0,
                    (measuredHeight + linePaddingTop - (measuredHeight / (yMax - y_TextArray[5])) * (lineDatas.get(0) - y_TextArray[5])));
            for (int i = 1; i <= lineDatas.size() - 1; i++) {
                pathLine.lineTo(((measuredWidth - widthText - textRectLeft) / maxDataSize) * i,
                        (measuredHeight + linePaddingTop - (measuredHeight / (yMax - y_TextArray[5])) * (lineDatas.get(i) - y_TextArray[5])));
            }
            canvas.drawPath(pathLine, mTimingLinePaint);

            //实时横线一
            canvas.drawLine(((measuredWidth - widthText - textRectLeft) / maxDataSize) * (lineDatas.size() - 1),
                    (measuredHeight + linePaddingTop - (measuredHeight / (yMax - y_TextArray[5])) * (lineDatas.get(lineDatas.size() - 1) - y_TextArray[5])),
                    measuredWidth - widthText - textRectLeft,
                    (measuredHeight + linePaddingTop - (measuredHeight / (yMax - y_TextArray[5])) * (lineDatas.get(lineDatas.size() - 1) - y_TextArray[5])),
                    mTimingLinePaint);
            //绘制文本-矩形框

            canvas.drawRoundRect(measuredWidth - widthText - textRectLeft, (measuredHeight + textRectTop - (measuredHeight / (yMax - y_TextArray[5]))
                            * (lineDatas.get(lineDatas.size() - 1) - y_TextArray[5]) - heightText + 5),
                    measuredWidth - textRectRight, (measuredHeight + textRectBottom - (measuredHeight / (yMax - y_TextArray[5]))
                            * (lineDatas.get(lineDatas.size() - 1) - y_TextArray[5])) + heightText - 5,
                    3, 3, mTimingTxtBgPaint);

            //绘制文本-文字
            canvas.drawText(String.format("%.3f", lineDatas.get(lineDatas.size() - 1)), measuredWidth - widthText - 45,
                    (measuredHeight + linePaddingTop - (measuredHeight / (yMax - y_TextArray[5]))
                            * (lineDatas.get(lineDatas.size() - 1) - y_TextArray[5])) + 12, mTimingTxtPaint);
        }
    }

    public float getyMax() {
        return yMax;
    }

    public void setyMax(float yMax) {
        this.yMax = yMax;
    }

    public float getyMin() {
        return yMin;
    }

    public void setyMin(float yMin) {
        this.yMin = yMin;
    }

    public void setLineDatas(List<Float> lineDatas) {
        this.lineDatas = lineDatas;
    }

    public void addData(float f) {
        if (lineDatas.size() >= maxDataSize) lineDatas.remove(0);
        lineDatas.add(f);
    }

    public void setSecondLineDatas(List<Float> secondLineDatas) {
        this.secondLineDatas = secondLineDatas;
    }

    public void addSecondData(float f) {
        if (secondLineDatas.size() >= maxDataSize) secondLineDatas.remove(0);
        secondLineDatas.add(f);
    }
//    public void invalidateLine() {
//        invalidate();
//    }
}
