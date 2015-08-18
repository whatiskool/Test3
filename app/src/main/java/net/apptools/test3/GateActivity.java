package net.apptools.test3;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;


/**
 * Created by degackt on 15. 8. 14..
 */
public class GateActivity extends Activity {
	String mStrageDir = Environment.getExternalStorageDirectory().getPath();
	File mBusDataFile = new File(mStrageDir + "/Android/data/net.apptools.test3/databases/BusData.kms");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!mBusDataFile.exists()){
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		}

		Intent intent = getIntent();

		if(Intent.ACTION_VIEW.equals(intent.getAction())) {
			Uri uri = intent.getData();
			String scheme = uri.getScheme();
			String host = uri.getHost();
			String path = uri.getPath();
			if (scheme.equals("busanbus")) {
				if (host.equals("home")) {
					Intent i = new Intent(this, MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);

				}

			}
		}


		finish();
	}
}