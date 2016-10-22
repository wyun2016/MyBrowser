package com.androidex.mybrowser;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by ASUS on 2016/10/22.
 */

public class MyEditText extends EditText {

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    //	文本变化的时候 ，会触发这个方法
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        // TODO Auto-generated method stub
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        Log.e("EditTextWithClearButton", text + "");

        Drawable[] drawable = getCompoundDrawables();

        if (!getText().toString().equals("") && hasFocus()) {

            setCompoundDrawablesWithIntrinsicBounds(drawable[0], drawable[1],
                    getContext().getResources().getDrawable(R.mipmap.ic_clear), drawable[3]);

        } else {
            setCompoundDrawablesWithIntrinsicBounds(drawable[0], drawable[1],
                    null, drawable[3]);
        }

//		最后 调用方法invalidate(); 来刷新  自定义组件里面经常使用
        invalidate();

    }


    //	再来监听焦点变化
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        Drawable[] drawable = getCompoundDrawables();
        if (!getText().toString().equals("") && focused) {

            setCompoundDrawablesWithIntrinsicBounds(drawable[0], drawable[1],
                    getContext().getResources().getDrawable(R.mipmap.ic_clear), drawable[3]);

        } else {
            setCompoundDrawablesWithIntrinsicBounds(drawable[0], drawable[1],
                    null, drawable[3]);
        }

        invalidate();
    }


    //	重写父类中的方法onTouchEvent(Mon....),当用户手指触摸这个 组件的时候  会触发这个方法，
//	并且咱们在在这里 定义  根据业务需求来实现特定的功能----当用手指 触摸的这个 输入框 的位置，正好
//	是 这个删除图片的 位置，那么我们就将输入框中的 内容清空
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean re = super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_UP://当触摸松开的时候
                Drawable[] drawable = getCompoundDrawables();

                if (drawable[2] != null) {

//				删除图片 左边 的边界值
                    int left = getWidth() - getPaddingRight() - drawable[2].getIntrinsicWidth();

//				删除图片 右边 的边界值
                    int right = getWidth() - getPaddingRight();

//				删除图片 上边 的边界值
                    int top = getPaddingTop();

//				删除图片 下边 的边界值
                    int bottom = getHeight() - getPaddingBottom();

//				event.getX() 和 event.getY()  ---(x,y)  若在删除图片的 上下左右的范围内，则说明 你 触摸了
//				这个删除图片,这时 你就可以将文本字符串 设为 “”
                    if (event.getX() < right && event.getX() > left &&
                            event.getY() > top && event.getY() < bottom) {
                        setText("");
                        invalidate();
//					自定义组件的时候，经常使用invalidate(); 来刷新视图
//					在UI线程中， 刷新界面 用invalidate();
//					在非UI线程中， 刷新界面 用postInvalidate();
                    }

                }

                break;

            default:
                break;
        }

        return re;
    }
}
