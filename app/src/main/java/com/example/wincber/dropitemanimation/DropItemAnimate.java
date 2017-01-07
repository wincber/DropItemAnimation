package com.example.wincber.dropitemanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by wincber on 1/5/2017.
 */

public class DropItemAnimate {
    private Context mContext;
    private ViewGroup mViewGroup;
    private ViewGroup.LayoutParams mLayoutParams;
    private Random random;
    private Drawable[]dropItems;

    private long duration = 1500;
    private int []defaultItemsId = {R.drawable.leaf_1,R.drawable.leaf_2,R.drawable.leaf_3};

    private Interpolator []mInterpolator;
    public DropItemAnimate(Context mContext, ViewGroup mViewGroup){
        this.mContext = mContext;
        this.mViewGroup = mViewGroup;
        initView();
    }
    public DropItemAnimate(Context mContext,ViewGroup mViewGroup,Drawable []dropItems){
        this.mContext = mContext;
        this.mViewGroup = mViewGroup;
        this.dropItems = dropItems;
        initView();
    }
    void initView(){
        if(dropItems == null){
            dropItems = new Drawable[defaultItemsId.length];
            for(int i = 0;i < defaultItemsId.length;i++){
                dropItems[i] = mContext.getResources().getDrawable(defaultItemsId[i]);
            }
        }
        mInterpolator = new Interpolator[4];
        mInterpolator[0] = new AccelerateInterpolator();
        mInterpolator[1] = new OvershootInterpolator();
        mInterpolator[2] = new DecelerateInterpolator();
        mInterpolator[3] = new AccelerateDecelerateInterpolator();
        random = new Random();
        mLayoutParams = new ViewGroup.LayoutParams(DesityUtil.dip2px(mContext,30),DesityUtil.dip2px(mContext,30));
    }

    /**
     * 向上掉落（准确的说应该是上漂），x值随机
     */
    public void startDropUp(){
        float sx = random.nextInt(mViewGroup.getWidth());
        float sy = mViewGroup.getHeight();
        PointF start = new PointF(sx,sy);
        PointF end = new PointF(sx,0);
        startDropP2P(start,end);
    }

    /**
     * 向下掉落，x值随机
     */
    public void startDropDown(){
        float sx = random.nextInt(mViewGroup.getWidth());
        float ey = mViewGroup.getHeight();
        PointF start = new PointF(sx,0);
        PointF end = new PointF(sx,ey);
        startDropP2P(start,end);
    }

    /**
     * 随机位置到自定义点的掉落
     * @param end
     */
    public void startDropRandom2P(PointF end){
        PointF start = new PointF(random.nextInt(mViewGroup.getWidth()),random.nextInt(mViewGroup.getHeight()));
        startDropP2P(start,end);
    }

    /**
     * 点到点的掉落
     * @param start
     * @param end
     */
    public void startDropP2P(PointF start,PointF end){
        ImageView item = new ImageView(mContext);
        item.setX(start.x);
        item.setY(start.y);
        Drawable nextItem = dropItems[random.nextInt(dropItems.length) ];
        item.setImageDrawable(nextItem);
        mViewGroup.addView(item,mLayoutParams);
        startAnimate(item,start,end);
    }

    /**
     * View动画开始
     * @param view
     * @param start
     * @param end
     */
    public void startAnimate(final View view, PointF start, PointF end){
        if(start == null){
            start = new PointF(view.getX(),view.getY());
        }
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view,"alpha",0,1);
        alpha.setDuration(duration);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(view,"translationX",start.x,end.x);
        translateX.setDuration(duration);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(view,"translationY",start.y,end.y);
        translateY.setDuration(duration);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(alpha).with(translateX).with(translateY);
        mAnimatorSet.setInterpolator(mInterpolator[random.nextInt((mInterpolator.length-1)) ]);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mViewGroup.removeView(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet.start();
        }

    /**
     * 设置动画时长
     * @param time
     */
    public void setDuration(long time){
        duration = time;
    }
}
