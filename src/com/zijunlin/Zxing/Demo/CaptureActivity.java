package com.zijunlin.Zxing.Demo;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mc.model.ItemDao;
import com.mc.util.HttpUtil;
import com.tg.yc.R;
import com.zijunlin.Zxing.Demo.camera.CameraManager;
import com.zijunlin.Zxing.Demo.decoding.CaptureActivityHandler;
import com.zijunlin.Zxing.Demo.decoding.InactivityTimer;
import com.zijunlin.Zxing.Demo.view.ViewfinderView;

public class CaptureActivity extends Activity implements Callback {

	private static String returnString = null;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	// private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	Intent intent;
	String userName = "18291994584";// 账户

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);
		// 初始化 CameraManager
		CameraManager.init(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		// txtResult = (TextView) findViewById(R.id.txtResult);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();
		/*
		 * txtResult.setText(obj.getBarcodeFormat().toString() + ":" +
		 * obj.getText());
		 */
		returnString = obj.getText().toString();

		QingQiuAsyntask qingQiuAsyntask = new QingQiuAsyntask();
		qingQiuAsyntask.execute();

	}

	/**
	 * 扫描条形码 请求网络，将物品加入购物车
	 * 
	 * @author mc 2013-10-27
	 */
	class QingQiuAsyntask extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub
			String url;
			try {
				url = HttpUtil.BASE_URL
						+ "AddGwcListServlet?userName="
						+ URLEncoder.encode(
								URLEncoder.encode(userName, "UTF-8"),
								"UTF-8")
							+"&item_no="+URLEncoder.encode(
									URLEncoder.encode(returnString, "UTF-8"),"UTF-8")
									+"&count=1";
				String result = HttpUtil.queryStringForPost(url);
				System.out.println("result:" + result);
				return result;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("result:" + result);
			intent = new Intent();
			if(result.equals("ok")){
				intent.putExtra("result", "ok");//添加成功
			}else {
				intent.putExtra("result", "error");//添加失败
			}
			
			setResult(RESULT_OK, intent);
			finish();
		}

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}