package tvdms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.victgroup.signup.dmsclient.R;

public class StartUpActivity extends Activity {
	
	Button button_ok;
	Button button_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_up);
		ExitApplication.getInstance().addActivity(this);
		button_ok = (Button) findViewById(R.id.btn_ok);
		button_cancel = (Button) findViewById(R.id.btn_cancel);
		button_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(StartUpActivity.this,
						MainActivity.class));
				finish();
			}
		});

		button_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}


}
