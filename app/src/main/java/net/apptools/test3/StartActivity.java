package net.apptools.test3;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class StartActivity extends Activity {

	private File mDir;
	private File mFile;

	private TextView mMsg;
	private final String TAG = "StartActivity";


	public Handler startHandler = new Handler() {
		public void handleMessage(Message msg) {
			Intent mainview = new Intent(getBaseContext(), MainActivity.class);
			mainview.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainview);

			finish();
//			Toast.makeText(getApplicationContext(), "핸들러 메시지.", Toast.LENGTH_LONG).show();

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		mDir = Utils.getDbDirectory(getApplicationContext());
		mFile = Utils.getDbFile(getApplicationContext());

		mMsg = (TextView) findViewById(R.id.tvstart);


		if(getIntent()!=null){
			Bundle bundle = getIntent().getExtras();
			if(bundle!=null){
				if(bundle.getBoolean("reload", false)){
					mMsg.setText("로딩 중입니다.");
				}
			}
		}

		new DataTask().execute();
	}


	 class DataTask extends AsyncTask<Boolean, Boolean, Boolean> {


		@Override
		protected Boolean doInBackground(Boolean... params) {


			if (!mFile.exists()) {
				mDir.mkdirs();

				AssetManager am = null;
				InputStream[] arrIs = new InputStream[4];
				BufferedInputStream[] arrBis = new BufferedInputStream[4];
				FileOutputStream fos = null;
				BufferedOutputStream bos = null;

				try {
					am = getResources().getAssets();

					for (int i = 0; i < arrIs.length; i++) {
						arrIs[i] = am.open("BusDataCut" + (i + 1) + ".kms");
						arrBis[i] = new BufferedInputStream(arrIs[i]);
						Log.d("BusDataCut open?", i +"");
					}

					fos = new FileOutputStream(mFile);
					bos = new BufferedOutputStream(fos);
					int read = -1;
					byte[] buffer = new byte[1024];

					for (int i = 0; i < arrIs.length; i++) {
						while ((read = arrBis[i].read(buffer, 0, 1024)) != -1) {
							bos.write(buffer, 0, read);
						}
						bos.flush();
					}

				} catch (Exception e) {
				} finally {
					for (int i = 0; i < arrIs.length; i++) {
						try {
							if (arrIs[i] != null)
								arrIs[i].close();
						} catch (Exception e) {
						}
						try {
							if (arrBis[i] != null)
								arrBis[i].close();
						} catch (Exception e) {
						}
					}
					try {
						if (fos != null)
							fos.close();
					} catch (Exception e) {
					}
					try {
						if (bos != null)
							bos.close();
					} catch (Exception e) {
					}
					arrIs = null;
					arrBis = null;
				}
			}
			Log.d("StartActivity", "doinbackground 실행완료");
			return mFile.exists();
		}

		@Override
		protected void onPostExecute(Boolean aBoolean) {
			if (aBoolean) {
				startHandler.sendEmptyMessageDelayed(0, 800);
			} else {
				Toast.makeText(getApplicationContext(), "데이터에 문제가 있어 실행 할 수 없습니다.", Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}

}
