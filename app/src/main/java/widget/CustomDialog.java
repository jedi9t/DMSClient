package widget;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.victgroup.signup.dmsclient.R;

public class CustomDialog  extends Dialog {
	//ʵ��Ĭ�Ϲ��캯��  
	public CustomDialog(Context context, int theme) {  
	  
	  
	    super(context, theme);  
	    // TODO Auto-generated constructor stub  
	}  
	  
	protected CustomDialog(Context context, boolean cancelable,  
	        OnCancelListener cancelListener) {  
	    super(context, cancelable, cancelListener);  
	    // TODO Auto-generated constructor stub  
	}  
	  
	public CustomDialog(Context context) {  
	    super(context);  
	  
	    // TODO Auto-generated constructor stub  
	}  
	//���еķ���ִ���궼�᷵��һ��Builderʹ�ú������ֱ��create��show  
	 public static class Builder {  
	        private Context context;  
	        private String title;  
	        private String message;  
	        private String positiveButtonText;//ȷ����ť  
	        private String negativeButtonText;//ȡ����ť  
	        private View contentView;  
	        private BaseAdapter adapter;//listview��adapter  
	        //ȷ����ť�¼�  
	        private OnClickListener positiveButtonClickListener;
	        //ȡ����ť�¼�  
	        private OnClickListener negativeButtonClickListener;
	        //listview��item����¼�  
	        private AdapterView.OnItemClickListener listViewOnclickListener;  
	  
	       public Builder(Context context) {  
	            this.context = context;  
	        }  
	        //������Ϣ  
	        public Builder setMessage(String message) {  
	            this.message = message;  
	            return this;  
	        }  
	  
	        /** 
	         *�������� 
	         *  

	         * @return 
	         */  
	        public Builder setMessage(int message) {  
	            this.message = (String) context.getText(message);  
	            return this;  
	        }  
	  
	        /** 
	         * ���ñ��� 
	         *  
	         * @param title 
	         * @return 
	         */  
	        public Builder setTitle(int title) {  
	            this.title = (String) context.getText(title);  
	            return this;  
	        }  
	  
	        /** 
	         *���ñ��� 
	         *  
	         * @param title 
	         * @return 
	         */  
	  
	        public Builder setTitle(String title) {  
	            this.title = title;  
	            return this;  
	        }  
	        //����������  
	        public Builder setAdapter(BaseAdapter adapter) {  
	            this.adapter = adapter;  
	            return this;  
	        }  
	        //���õ���¼�  
	        public Builder setOnClickListener(AdapterView.OnItemClickListener listViewOnclickListener) {  
	            this.listViewOnclickListener = listViewOnclickListener;  
	            return this;  
	        }  
	        //������������  
	        public Builder setContentView(View v) {  
	            this.contentView = v;  
	            return this;  
	        }  
	        /** 
	         * ����ȷ����ť�������¼� 
	         *  
	         * @param positiveButtonText 
	         * @return 
	         */  
	        public Builder setPositiveButton(int positiveButtonText,  
	                OnClickListener listener) {
	            this.positiveButtonText = (String) context  
	                    .getText(positiveButtonText);  
	            this.positiveButtonClickListener = listener;  
	            return this;  
	        }  
	  
	        public Builder setPositiveButton(String positiveButtonText,  
	                OnClickListener listener) {
	            this.positiveButtonText = positiveButtonText;  
	            this.positiveButtonClickListener = listener;  
	            return this;  
	        }  
	        //����ȡ����ť�����¼�  
	        public Builder setNegativeButton(int negativeButtonText,  
	                OnClickListener listener) {
	            this.negativeButtonText = (String) context  
	                    .getText(negativeButtonText);  
	            this.negativeButtonClickListener = listener;  
	            return this;  
	        }  
	  
	        public Builder setNegativeButton(String negativeButtonText,  
	                OnClickListener listener) {
	            this.negativeButtonText = negativeButtonText;  
	            this.negativeButtonClickListener = listener;  
	            return this;  
	        }  
	    //createview����  
	        public CustomDialog create() {  
	            LayoutInflater inflater = (LayoutInflater) context  
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	            // ��������  
	            final CustomDialog dialog = new CustomDialog(context,R.style.CustomDialog);
	            View layout = inflater.inflate(R.layout.qr_activity, null);  
	            dialog.addContentView(layout, new LayoutParams(  
	                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));  

	            return dialog;  
	        }  
	  
	    }  
}
