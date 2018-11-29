package widget;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3dAnimation extends Animation {
	    //��ʼ�Ƕ�
	  private final float mFromDegrees;  
	    // �����Ƕ�  
	    private final float mToDegrees;  
	    // ���ĵ�  
	    private final float mCenterX;  
	    private final float mCenterY;  
	    private final float mDepthZ;  
	    // �Ƿ���ҪŤ��  
	    private final boolean mReverse;  
	    // ����ͷ  
	    private Camera mCamera;  
	  
	    public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX,  
	            float centerY, float depthZ, boolean reverse) {  
	        mFromDegrees = fromDegrees;  
	        mToDegrees = toDegrees;  
	        mCenterX = centerX;  
	        mCenterY = centerY;  
	        mDepthZ = depthZ;  
	        mReverse = reverse;  
	    }  
	  
	    @Override  
	    public void initialize(int width, int height, int parentWidth,  
	            int parentHeight) {  
	        super.initialize(width, height, parentWidth, parentHeight);  
	        mCamera = new Camera();  
	    }  
	  
	    // ����Transformation  
	    @Override  
	    protected void applyTransformation(float interpolatedTime, Transformation t) {  
	        final float fromDegrees = mFromDegrees;  
	        // �����м�Ƕ�  
	        float degrees = fromDegrees  
	                + ((mToDegrees - fromDegrees) * interpolatedTime);  
	  
	        final float centerX = mCenterX;  
	        final float centerY = mCenterY;  
	        final Camera camera = mCamera;  
	  
	        final Matrix matrix = t.getMatrix();  
	        // ����ǰ������ͷλ�ñ����������Ա�任������ɺ�ָ���ԭλ��  
	        camera.save(); 
	        // camera.translate�������������3���������ֱ���x,y,z�������ƫ��������������ֻ��z�������ƫ�ƣ�  
	        if (mReverse) {  
	        	// z��ƫ�ƻ�Խ��Խ����ͻ��γ�����һ��Ч����view�ӽ���Զ  
	            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);  
	        } else {  
	        	 // z��ƫ�ƻ�Խ��ԽС����ͻ��γ�����һ��Ч�������ǵ�View��һ����Զ�ĵط��������ƹ�����Խ��Խ���������Ƶ������ǵĴ������桫 
	            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));  
	        }  
	        // �Ǹ����ǵ�View������תЧ�������ƶ��Ĺ����У���ͼ������Y��Ϊ���Ľ�����ת��  
	        camera.rotateY(degrees);  
	        
	       // �Ǹ����ǵ�View������תЧ�������ƶ��Ĺ����У���ͼ������X��Ϊ���Ľ�����ת��  
	       //  camera.rotateX(degrees);  
	        
	        // ȡ�ñ任��ľ���  ,����ǽ����ǸղŶ����һϵ�б任Ӧ�õ��任��������
	        camera.getMatrix(matrix);  
	        //�ǾͿ��Խ�camera��λ�ûָ��ˣ��Ա���һ����ʹ�á�  
	        camera.restore();  
	       // ��View�����ĵ�Ϊ��ת����,������������䣬�����ԣ�0,0����Ϊ��ת����  
	        matrix.preTranslate(-centerX, -centerY);  
	        matrix.postTranslate(centerX, centerY);  
	    }  
	}  