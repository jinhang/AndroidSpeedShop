package com.tg.yc;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	
		final View view = View.inflate(this, R.layout.activity_main, null);
		setContentView(view);
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(3000);
		view.startAnimation(aa);

		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
				  
			}

		
		});
    }
    
    private void redirectTo() {
    	
		Intent intent = new Intent(this,MapActivity.class);
		startActivity(intent);
		finish();
	}



    
}
