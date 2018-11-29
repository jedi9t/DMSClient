package util;


import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import widget.Rotate3dAnimation;

public class AnimationUtil {
	  
	  public class DecelerateAccelerateInterpolator implements Interpolator {
		  
		    //input��0��1������ֵҲ��0��1.����ֵ�����߱����ٶȼӼ�����
		    @Override
		    public float getInterpolation(float input) {
		        return (float) (Math.tan((input * 2 - 1) / 4 * Math.PI)) / 2.0f + 0.5f;
		    }
		}
	public static void scaleBig(View v) {// �Ŵ�
		AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(1f, 1.1f, 1f,1.1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);		
		 
		scale.setDuration(500);
		animSet.addAnimation(scale);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);
		v.clearAnimation();
		v.startAnimation(animSet);
	}

	public static void scaleSmall(View v) {// ��С
		AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(300);
		animSet.addAnimation(scale);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);
		v.clearAnimation();
		v.startAnimation(animSet);

	}
	public static void scaleBig0(View v) {// ��һ��ͼÿһ�ηŴ�
		AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(0.0f, 1.05f, 0.0f, 1.05f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		scale.setDuration(200);
		animSet.addAnimation(scale);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);
		v.clearAnimation();
		v.startAnimation(animSet);
	}
	
	
	public static void alpha(View v) {// ���룬����
		// ����һ��AnimationSet���󣬲���ΪBoolean�ͣ�
		// true��ʾʹ��Animation��interpolator��false����ʹ���Լ���
		AnimationSet animationSet = new AnimationSet(true);
		// ����һ��AlphaAnimation���󣬲�������ȫ��͸���ȣ�����ȫ�Ĳ�͸��
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1);// 1��0
		// ���ö���ִ�е�ʱ��
		alphaAnimation.setDuration(1000);
		// ��alphaAnimation������ӵ�AnimationSet����
		animationSet.addAnimation(alphaAnimation);
		// ʹ��ImageView��startAnimation����ִ�ж���
		v.startAnimation(animationSet);
	}
	public static void alpha_disappear(View v) {// ���룬����
		// ����һ��AnimationSet���󣬲���ΪBoolean�ͣ�
		// true��ʾʹ��Animation��interpolator��false����ʹ���Լ���
		AnimationSet animationSet = new AnimationSet(true);
		// ����һ��AlphaAnimation���󣬲�������ȫ��͸���ȣ�����ȫ�Ĳ�͸��
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.5f);// 1��0
		// ���ö���ִ�е�ʱ��
		alphaAnimation.setDuration(1000);
		// ��alphaAnimation������ӵ�AnimationSet����
		animationSet.addAnimation(alphaAnimation);
		// ʹ��ImageView��startAnimation����ִ�ж���
		v.startAnimation(animationSet);
	}
	

	public static void translate(View v) {// �ƶ�
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		translateAnimation.setDuration(1000);
		animationSet.addAnimation(translateAnimation);
		v.startAnimation(animationSet);
	}
	public static void translate(View v,int duration) {// ƽ��
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation= new TranslateAnimation(0, 0,500, 0); 
		translateAnimation.setDuration(duration);
		animationSet.addAnimation(translateAnimation);
		v.startAnimation(animationSet);
	}
	
	public static void translate(View v,int duration,float fromx,float tox,float fromy,float toy) {// ƽ��
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation= new TranslateAnimation(fromx, tox,fromy, toy); 
		translateAnimation.setDuration(duration);
		animationSet.addAnimation(translateAnimation);
		v.startAnimation(animationSet);
		
	}
	public static void translate(View v,int duration,float fromx,float tox,float fromy,float toy,int repeatCount) {// ƽ��
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation= new TranslateAnimation(fromx, tox,fromy, toy); 
		translateAnimation.setDuration(duration);
		animationSet.addAnimation(translateAnimation);
		translateAnimation.setRepeatCount(repeatCount);				
		v.startAnimation(animationSet);
		
	}
	public static void translate_X(View v,boolean fromLeftToRight) {// ƽ��
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation;
		if(fromLeftToRight){
		    translateAnimation= new TranslateAnimation(-100, 0,0, 0); 
		}else{
			translateAnimation= new TranslateAnimation(100, 0,0, 0); 
		}
		translateAnimation.setDuration(100);
		animationSet.addAnimation(translateAnimation);
		v.startAnimation(animationSet);
	}
	
	public static void translate_XY(final View v,final TextView v2,final String text) {// ƽ��
		 AnimationSet set = new AnimationSet(true);
		 TranslateAnimation translateAnimationX=new TranslateAnimation(0, 20.0f, 0, 0);
	     translateAnimationX.setInterpolator(new LinearInterpolator());	       
	     TranslateAnimation translateAnimationY=new TranslateAnimation(0, 0, 0, -30.0f);
	     translateAnimationY.setInterpolator(new DecelerateInterpolator());      
	     set.addAnimation(translateAnimationY);
	     set.addAnimation(translateAnimationX);
	     set.setDuration(500);
	     set.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				v.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				v.setVisibility(View.VISIBLE);
				v2.setText(text);
			}});
		v.startAnimation(set);
	}
	
	final static int count = 300;  
	 
	public static void trans_parabolic(View v,float[][] points){
		Keyframe[] keyframes = new Keyframe[count];  
        final float keyStep = 1f / (float) count;  
        float key = keyStep;  
        for (int i = 0; i < count; ++i) {  
            keyframes[i] = Keyframe.ofFloat(key, i + 1);  
            key += keyStep;  
        }    
        calculate(points);        
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofKeyframe("translationX", keyframes);  
        key = keyStep;  
        for (int i = 0; i < count; ++i) {  
            keyframes[i] = Keyframe.ofFloat(key, -getY(i + 1));  
            key += keyStep;  
        }   
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofKeyframe("translationY", keyframes);  
        ObjectAnimator yxBouncer = ObjectAnimator.ofPropertyValuesHolder(v, pvhY, pvhX).setDuration(5000);  
        yxBouncer.setInterpolator(new BounceInterpolator());  
        yxBouncer.start();  
	}

	 private static float getY(float x) {  
	        return a * x * x + b * x+c;  
	  }  
	 
	 static float a;
	 static float b;
	 static float c;
	 
	 private static void calculate(float[][] points) {  
	        float x1 = points[0][0];  
	        float y1 = points[0][1];  
	        float x2 = points[1][0];  
	        float y2 = points[1][1];  
	        float x3 = points[2][0];  
	        float y3 = points[2][1];  	  
	        a = (y1 * (x2 - x3) + y2 * (x3 - x1) + y3 * (x1 - x2))  
	                / (x1 * x1 * (x2 - x3) + x2 * x2 * (x3 - x1) + x3 * x3 * (x1 - x2));  
	        b = (y1 - y2) / (x1 - x2) - a * (x1 + x2);  
	        c = y1 - (x1 * x1) * a - x1 * b;  	          
	        System.out.println("-a->" + a + " b->" +b + " c->" +c);  
	    }  
	 
	public static void rotate(View v) {// ��ת
		AnimationSet animationSet = new AnimationSet(true);
		// ����1�����ĸ���ת�Ƕȿ�ʼ
		// ����2��ת��ʲô�Ƕ�
		// ��4��������������Χ������ת��Բ��Բ��������
		// ����3��ȷ��x����������ͣ���ABSOLUT�������ꡢRELATIVE_TO_SELF������������ꡢRELATIVE_TO_PARENT����ڸ��ؼ�������
		// ����4��x���ֵ��0.5f����������������ؼ���һ�볤��Ϊx��
		// ����5��ȷ��y�����������
		// ����6��y���ֵ��0.5f����������������ؼ���һ�볤��Ϊx��
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
		Animation.RELATIVE_TO_SELF, 0.5f,
		Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(1000);		
		animationSet.addAnimation(rotateAnimation);
		v.startAnimation(animationSet);
	}
			
	public void showScrollAnim(View view,int duration) {	  
	    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
	    mShowAction.setDuration(duration);
	    view.startAnimation(mShowAction);
	}
	
	public void randomAnimation(View v,int index){
		switch(index){
		//Y�᷽��
		  case 0: ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	         scaleAnimation.setDuration(1000);
	         v.startAnimation(scaleAnimation);break;
	         //X�᷽������
		  case 1:ScaleAnimation scaleAnimation1 = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		         scaleAnimation1.setDuration(1000);
		         v.startAnimation(scaleAnimation1);break;
		         //��������
		  case 2:ScaleAnimation scaleAnimation2 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	            scaleAnimation2.setDuration(1000);
	            v.startAnimation(scaleAnimation2);break;
	            //�������󻬶�
		  case 3:showScrollAnim(v,1000);break;
		  //����
		  case 4:AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                 alphaAnimation.setDuration(1000);
                 v.startAnimation(alphaAnimation);break;
		  case 5:
			   final float centerX = v.getWidth() / 2.0f;
	           final float centerY = v.getHeight() / 2.0f;
			   Rotate3dAnimation rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, true);
	           rotation.setDuration(1000);  
	          // rotation.setFillAfter(true);  
	          // rotation.setInterpolator(new DecelerateInterpolator());
               v.startAnimation(rotation);;break;//��ת		
		  default:rotate(v);break;
		  
		}		
	}
	public void changeImageAnimation(final ImageView v1,final ImageView v2,int duration){
		  AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
          alphaAnimation.setDuration(duration*1000);
          alphaAnimation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				v1.setImageDrawable(v2.getDrawable());
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
        	  
          });
          v2.startAnimation(alphaAnimation);
	}
	public void rotate3dAnimation(final int position,float start, float end,final View container,final View layout_one,final View layout_two){
		final float centerX = container.getWidth() / 2.0f;  
        final float centerY = container.getHeight() / 2.0f;  
  
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);  
        rotation.setDuration(500);  
        rotation.setFillAfter(true);  
       // rotation.setRepeatCount(-1);
        rotation.setInterpolator(new AccelerateInterpolator());  
        // ���ü���  
        rotation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				container.post(new SwapViews(position,container,layout_one,layout_two));								
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });  
  
        container.startAnimation(rotation);  
	}
	
	
	 private final class SwapViews implements Runnable {
	        private final int mPosition;
	        View container;
	        View layout_one;
	        View layout_two;

	        public SwapViews(int position,View container,View layout_one,View layout_two) {
	            mPosition = position;
	            this.container=container;
	            this.layout_one=layout_one;
	            this.layout_two=layout_two;
	        }

	        public void run() {
	            final float centerX = container.getWidth() / 2.0f;
	            final float centerY = container.getHeight() / 2.0f;
	            Rotate3dAnimation rotation;
	            
	            if (mPosition > -1) {
	            	layout_one.setVisibility(View.GONE);
	            	layout_two.setVisibility(View.VISIBLE);

	                rotation = new Rotate3dAnimation(90,180, centerX, centerY, 310.0f, false);
	            } else {
	            	layout_two.setVisibility(View.GONE);
	            	layout_one.setVisibility(View.VISIBLE);
	                rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
	            }

	            rotation.setDuration(500);
	            rotation.setFillAfter(true);
	            rotation.setInterpolator(new DecelerateInterpolator());
	            container.startAnimation(rotation);
	        }
	    }
	
}
