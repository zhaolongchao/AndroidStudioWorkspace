package com.zhuoxin.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.adapter.ChannelUserAdapter;
import com.zhuoxin.newsday01.R;
import com.zhuoxin.utils.BaseDataUtil;

/**
 * Created by l on 2016/11/17.
 */

/**
 * 用户频道的网格视图中，处理拖拽逻辑
 * 触摸事件
 * onInterceptTouchEvent
 * onTouchEvent
 * 屏幕上的触摸位置
 * 手指触摸在控件上的位置
 * 拖拽动画的动画层--不定量的动画
 * 拖拽控件与交换的偏差值
 * 注册单项长按事件--处理成功之后返回true，震动
 * 操作适配器控件内容交换顺序（不是替换，是插队）
 *最后一次拖拽的位置
 */
public class MyGridView extends GridView {
    private int downX;//按下时的X的位置
    private int downY;//按下时的Y的位置
    private int startPosition;//第一次按钮是子视图的位置
    private int dragPosition;//拖拽的位置
    private int dropPosition;//放下的位置
    private ImageView dragImageView;//拖拽层的图片控件
    private ViewGroup dragViewGroup;//拖拽层的视图容器
    private int dragItemWidth;//点击的item的宽度
    private int  dragItemHeight;//点击的item的高度
    private int colNums=4;//网格视图的列数
    private int rowNums;//网格视图的行数
    private double dragScale=1.2;//拖动层视图的伸缩率
    private int dragOffsetX;//拖动开始的x的坐标
    private int dragOffsetY;//拖动开始的y的坐标
    private WindowManager windowManager;//窗口管理器对象
    private Vibrator vibrator;//振动器对象
    private WindowManager.LayoutParams windowParams;//窗口管理器布局参数设置对象
    private int holdPosition;//滑动过程中停留的位置
    private int horizontalSpacing=15;//每一个item水平间距
    private int verticalSpacing=15;//每一个item垂直间距
    private String lastAnimationStr;
    protected boolean isMoving=true;
    public MyGridView(Context context) {
        super(context);
        init(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    //初始化
    private void init(Context context) {
        //通过系统服务获取窗口管理对象
        windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //通过系统服务获取振动器对象
        vibrator= (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        //窗口管理布局参数
        windowParams=new WindowManager.LayoutParams();
        horizontalSpacing= (int) BaseDataUtil.dip2px(getContext(),horizontalSpacing);
        verticalSpacing= (int) BaseDataUtil.dip2px(getContext(),verticalSpacing);
    }

    /**
     * 重写测量方法，设计高度的计算方法
     * 确定组件的高度1.组件大小，2.组件大小尺寸模式
     * AT_MOST取决于父组件能够给的最大尺寸
     * EXACTLY精确模式，那么这个组件的长或宽就是多少
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 触摸事件的拦截处理
     * 针对按下的事件，注册一个单项长点击的监听
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            downX= (int) ev.getX();
            downY= (int) ev.getY();
            //注册触摸的长点击事件
            setItemTouchListener(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }
    //长按事件监听
    private void setItemTouchListener(final MotionEvent ev) {
        setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,
                                           int i, long l) {
                //手指在控件中的位置
                int x= (int) ev.getX();
                int y= (int) ev.getY();
                //第一次点击的位置
                startPosition=i;
                //开始拖拽的位置
                dragPosition=i;
                //直接屏蔽0,1
                if(startPosition<=1){
                    return false;
                }
                //获取当前点击的子视图   第二个参数view
                dragViewGroup= (ViewGroup) getChildAt(dragPosition);
                //获取子视图的文本控件
                TextView tv= (TextView) dragViewGroup.findViewById(R.id.tv_channel_item);
                //设置控件的状态
                tv.setSelected(true);
                tv.setEnabled(false);
                //获取宽高
                dragItemHeight=tv.getHeight();
                dragItemWidth=tv.getWidth();
                //根据当前子项的数量设置行数
                int totalNum=MyGridView.this.getCount();
                rowNums=totalNum/colNums;
                if(totalNum%colNums!=0){
                    rowNums=rowNums+1;
                }
                /**
                 * 判断拖拽的位置是否不合法的位置
                 * INVALID_POSITION不合法位置的常量
                 */
                if(dragPosition!=AdapterView.INVALID_POSITION){
                    //清除缓存
                    dragViewGroup.destroyDrawingCache();
                    //开启缓存
                    dragViewGroup.setDrawingCacheEnabled(true);
                    //将v转换成bitmap对象
                    Bitmap bitmap=Bitmap.createBitmap(dragViewGroup.getDrawingCache());
                    //关闭缓存
                    dragViewGroup.setDrawingCacheEnabled(false);
                    //设置震动
                    vibrator.vibrate(50);
                    //拖拽开始的xy的位置
                    dragOffsetX= (int) (ev.getRawX()-x);
                    dragOffsetY= (int) (ev.getRawY()-y);
                    /**
                     * 开始拖拽设置拖拽层
                     * 1.拖动的bitmap找到视图层--一定是在窗口中
                     * 2.放大
                     */
                    startDrag(bitmap,(int)ev.getRawX(),(int)ev.getRawY());
                    //隐藏放下的位置的textview的内容
                    ((ChannelUserAdapter)getAdapter()).saveClickPosition(i);
                    //设置当前长按的视图控件不可见
                    dragViewGroup.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }

    /**
     * 拖拽视图层可以在窗口的任意位置出现，通过窗口管理器获取视图容器
     * 将bitmap设置到ImageView中，然后将ImageView放入窗口管理器，
     * 设置窗口管理器的相关配置参数
     * @param bitmap
     * @param rawX
     * @param rawY
     */
    protected void startDrag(Bitmap bitmap, int rawX, int rawY) {
        //清除原有控件
        if(dragImageView!=null){
            windowManager.removeView(dragImageView);
            dragImageView=null;
        }
        dragImageView=new ImageView(getContext());
        dragImageView.setImageBitmap(bitmap);
        //设置当前项的距窗口的距离
        windowParams.x=rawX-(int)((dragItemWidth/2)*dragScale);
        windowParams.y=rawY-(int)((dragItemHeight/2)*dragScale);
        //对齐方式
        windowParams.gravity= Gravity.TOP|Gravity.LEFT;
        //设置附加参数，窗口管理视图不获取焦点，设置视图在屏幕上
        windowParams.flags=
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                |WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        windowParams.width=(int)(dragItemWidth*dragScale);
        windowParams.height=(int)(dragItemHeight*dragScale);
        //在窗口管理对象中添加一个视图控件
        windowManager.addView(dragImageView,windowParams);
    }
    //触摸的事件处理回调
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x= (int) ev.getX();
        int y= (int) ev.getY();
        //动画层的控件不为空时再进行动画
        if(dragImageView!=null){
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN://按下
                    downX=(int)ev.getX();
                    downY= (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE://移动
                    //拖拽层移动
                    onDragMove((int)ev.getRawX(),(int)ev.getRawY());
                    //判断当前位置是否在合法位置内
                    if(pointToPosition(x,y)==-1){
                        break;
                    }
                  //  if(isMoving){
                        //拖拽层移动到合法位置时，GridView子项的联动
                        onMove(x,y);
                   // }
                    break;
                case MotionEvent.ACTION_UP://抬起
                    //清除原有控件
                    if(dragImageView!=null){
                        windowManager.removeView(dragImageView);
                        dragImageView=null;
                    }
                    //设置目标位置抬起时内容可见
                //    ((ChannelUserAdapter)getAdapter()).setShowItem(true);
                    //重新适配
                    ((ChannelUserAdapter)getAdapter()).notifyDataSetChanged();
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * GridView在拖拽层划过的合法位置的移动
     * @param x
     * @param y
     */
    private void onMove(int x, int y) {
        //拖动的时候GridView对应的位置
        int holdPosition=pointToPosition(x,y);
        //只有在合法并且是第二个位置以后才执行移动
        if(holdPosition>1){
            if(holdPosition==dragPosition){
                return;
            }
            //本次可能抬起的位置，放下当前拖拽的item
            dropPosition=holdPosition;
            int moveCount=dropPosition-dragPosition;
            //循环移动和执行动画的数量
            int moveCountAbs=Math.abs(moveCount);
            for (int i = 0; i < moveCountAbs; i++) {
                //移动到x，y的位置
                float toX=0,toY=0;
                float xUnitValue=1.0f+(float)horizontalSpacing/dragItemWidth;
                float yUnitValue=1.0f+(float)verticalSpacing/dragItemHeight;
                //向左向上拖动动画向右向下移
                if(moveCount<0){
                    this.holdPosition=dragPosition-i-1;
                    //判断是否在一行
                    if((this.holdPosition+1)%4==0){
                        toX=-3*xUnitValue;
                        toY=yUnitValue;
                    }else{
                        toX=xUnitValue;
                        toY=0;
                    }
                }else{//向右向下拖拽动画向左向上
                    this.holdPosition=dragPosition+i+1;
                    //判断是否在同一行
                    if(this.holdPosition%4==0){
                        toX=3*xUnitValue;
                        toY=-yUnitValue;
                    }else{
                        toX=-xUnitValue;
                        toY=0;
                    }
                }
                //获取要移动的位置的控件
                ViewGroup holdViewGroup= (ViewGroup) getChildAt(this.holdPosition);
                //启动平移动画
                TranslateAnimation tran=moveAnimation(toX,toY);
                TextView tv = (TextView)holdViewGroup.findViewById(R.id.tv_channel_item);
                holdViewGroup.startAnimation(tran);
                if(this.holdPosition==dropPosition){
                    //动画标志缓存  最后一个动画
                    lastAnimationStr=tran.toString();
                }
                //注册动画监听
                tran.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //正在移动中
                        isMoving=true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //如果是最后一个动画结束了，进行适配
                        if(animation.toString().equals(lastAnimationStr)){
                            //在适配器其中封装交换item的位置
                            ((ChannelUserAdapter)getAdapter()).exchange(dragPosition,dropPosition);
                            ((ChannelUserAdapter) getAdapter()).saveClickPosition(dropPosition);
                            startPosition=dropPosition;
                            dragPosition=dropPosition;
                            //移动状态
                            isMoving=false;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
    }
    //平移动画
    private TranslateAnimation moveAnimation(float toX, float toY) {
        TranslateAnimation tran=new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,
                0f,
                TranslateAnimation.RELATIVE_TO_SELF,
                toX,
                TranslateAnimation.RELATIVE_TO_SELF,
                0f,
                TranslateAnimation.RELATIVE_TO_SELF,
                toY);
        tran.setFillAfter(true);
        tran.setDuration(300);
        return tran;
    }

    private void onDragMove(int rawX, int rawY) {
        //设置透明度
        windowParams.alpha=0.7f;
        windowParams.x=(int)(rawX-(dragItemWidth/2)*dragScale);
        windowParams.y=(int)(rawY-(dragItemHeight/2)*dragScale);
        windowManager.updateViewLayout(dragImageView,windowParams);
    }
}
