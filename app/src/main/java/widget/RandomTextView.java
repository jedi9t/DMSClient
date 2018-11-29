package widget;

import android.os.Handler;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RandomTextView {
	private static final int timerAnimation = 1;
	List<String> txtlist;
	int index;
	TextView view;		
	

	public RandomTextView(TextView view,List<String> txtlist,int duration){
		this.txtlist=txtlist;
		if(txtlist==null||txtlist.size()<=0)
			return;		
		index=0;
		this.view=view;
		timer.schedule(task, 0, duration*1000);//�ڶ�������"0"����˼��:(0�ͱ�ʾ���ӳ�)������ø÷����󣬸÷�����Ȼ����� TimerTask �� TimerTask �� �е� run() �����������������������֮��Ĳ�ֵ��ת���ɺ������˼����˵���û����� schedule() ������Ҫ�ȴ���ô����ʱ��ſ��Ե�һ��ִ�� run() ������
	}
	private Timer timer = new Timer();
	private final TimerTask task = new TimerTask()
	{
		public void run()
		{
			mHandler.sendEmptyMessage(timerAnimation);
		}
	};
	
	
	private final Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
			case timerAnimation:
				index++;
				if(index>=txtlist.size())
					index=0;	
				view.setText(txtlist.get(index));				
				break;
			default:
				break;
			}
		};
	};
	
	public void release()
	{
		if(timer!=null)
		{
		  timer.cancel();
		  timer=null;
		}
	}
}
