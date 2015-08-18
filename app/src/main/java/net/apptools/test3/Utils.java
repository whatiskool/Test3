package net.apptools.test3;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by degackt on 15. 8. 12..
 */
public class Utils {
	public static File getDbDirectory(Context context) {

		File cacheDir;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			File file2 = context.getApplicationContext().getExternalCacheDir();
//			Log.d("Utils_ErrorTest","실행 되었습니다");
//			cacheDir = new File(file2.getAbsolutePath() + "/databases");
			cacheDir = new File(context.getApplicationContext().getExternalCacheDir(), "/databases");
		} else {
			cacheDir = new File(context.getCacheDir(), "/databases");
		}

		return cacheDir;
	}

	public static File getDbFile(Context context) {

		File cacheDir;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

//			File file2 = context.getApplicationContext().getExternalCacheDir();
//			cacheDir = new File(file2.getAbsolutePath() + "/databases" + "/" + "BusData.kms");
//			try {
//				cacheDir.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			if(file2.exists()) {
//				Log.d("에러테스트","파일생성됨");
//			}

			cacheDir = new File(context.getApplicationContext().getExternalCacheDir(), "/databases/BusData.kms");

		} else {
//
//			File file2 = context.getCacheDir();
//			cacheDir = new File(file2.getAbsolutePath() + "/databases" + "/" + "BusData.kms");
//			try {
//				cacheDir.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			if(file2.exists()) {
//				Log.d("에러테스트","파일생성됨");
//			}

			cacheDir = new File(context.getCacheDir(), "/databases/BusData.kms");
		}
		if (cacheDir.exists()){
			Log.d("에러 테스트","생성됨");
		}


		return cacheDir;
	}
}
