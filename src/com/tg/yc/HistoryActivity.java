package com.tg.yc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class HistoryActivity extends Activity {
	ImageView backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final View view = View.inflate(this, R.layout.history_activity, null);
		setContentView(view);
		backButton = (ImageView) findViewById(R.id.back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	

}
