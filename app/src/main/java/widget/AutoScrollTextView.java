package widget;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * 
 * TODO �����ı�����ƿؼ�
 *
 * @author tianlu
 * @version 1.0
 * Create At : 2010-2-16 ����09:35:03
 */
public class AutoScrollTextView extends TextView {
    public final static String TAG = AutoScrollTextView.class.getSimpleName();
    
    private float textLength = 0f;//�ı�����
    private float viewWidth = 0f;
    private float step = 0f;//���ֵĺ�����
    private float y = 0f;//���ֵ�������
    private float x = 0f;//���ֵ�������
    private float temp_view_plus_text_length = 0.0f;//���ڼ������ʱ����
    private float temp_view_plus_two_text_length = 0.0f;//���ڼ������ʱ����
    public boolean isStarting = false;//�Ƿ�ʼ����
    private Paint paint = null;//��ͼ��ʽ
    private String text = "";//�ı�����

    
    public AutoScrollTextView(Context context) {
        super(context);
        
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    
    
    
    public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

    
    /**
     * �ı���ʼ����ÿ�θ����ı����ݻ����ı�Ч����֮����Ҫ���³�ʼ��һ��
     */
    public void init(WindowManager windowManager,int color,int size,String content,int gravity,int width,int height)
    {
        paint = getPaint();
        paint.setColor(color);
        paint.setTextSize(size);
        text = content;
        textLength = paint.measureText(text);
        viewWidth = width;
        Log.i("mytest", "viewWidth="+viewWidth+",textLength="+textLength);
        if(viewWidth <= 0)
        {
            if(windowManager != null)
            {
                Display display = windowManager.getDefaultDisplay();
                viewWidth = display.getWidth();
            }
        }
        step = textLength;
        temp_view_plus_text_length = viewWidth + textLength;
        temp_view_plus_two_text_length = viewWidth + textLength * 2;
        
        float textSize=getTextSize();
     //   int height=getHeight();
      //  int paddingTop=getPaddingTop();
        Log.i("mytest", "textSize="+textSize+",height="+height);
       switch(gravity) {
       case Gravity.LEFT | Gravity.TOP:
    	   x=0;
    	   y=textSize;
    	   break;
       case Gravity.LEFT | Gravity.CENTER:
    	   x=0;
    	   y=(textSize+height)/2;
    	   break;
       case Gravity.LEFT | Gravity.BOTTOM:
    	   x=0;
    	   y=height;
    	   break;
       case Gravity.CENTER | Gravity.TOP:
    	   x=(viewWidth-textLength)/2;
    	   y=textSize;
    	   break;
       case Gravity.CENTER:
    	   x=(viewWidth-textLength)/2;
    	   y=(textSize+height)/2;
    	   break;
       case Gravity.CENTER | Gravity.BOTTOM:
    	   x=(viewWidth-textLength)/2;
    	   y=height;
    	   break;
       case Gravity.RIGHT | Gravity.TOP:
    	   x=viewWidth-textLength;
    	   y=textSize;
    	   break;
       case Gravity.RIGHT | Gravity.CENTER:
    	   x=viewWidth-textLength;
    	   y=(textSize+height)/2;
    	   break;
       case Gravity.RIGHT | Gravity.BOTTOM:
    	   x=viewWidth-textLength;
    	   y=height;
    	   break;
    	   
    	   
       }
       
      //  y = textSize +paddingTop;
    }
    
    @Override
    public Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        
        ss.step = step;
        ss.isStarting = isStarting;
        
        return ss;
        
    }
    
    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        
        step = ss.step;
        isStarting = ss.isStarting;

    }
    
    public static class SavedState extends BaseSavedState {
        public boolean isStarting = false;
        public float step = 0.0f;
        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[]{isStarting});
            out.writeFloat(step);
        }


        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

        private SavedState(Parcel in) {
            super(in);
            boolean[] b = null;
            in.readBooleanArray(b);
            if(b != null && b.length > 0)
                isStarting = b[0];
            step = in.readFloat();
        }
    }

    /**
     * ��ʼ����
     */
    public void startScroll()
    {
        isStarting = true;
        invalidate();
    }
    
    /**
     * ֹͣ����
     */
    public void stopScroll()
    {
        isStarting = false;
        invalidate();
    }
    

    @Override
    public void onDraw(Canvas canvas) {
        if(isStarting) {
        	canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
        	
        }else {
        	canvas.drawText(text, x, y, paint);
        	return;
        }
        step += 0.5;
        if(step > temp_view_plus_two_text_length)
            step = textLength;
        invalidate();

    }


}