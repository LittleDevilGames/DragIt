package com.dragit.slickstars.game.android;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dragit.slickstars.game.MainGame;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication {
	
	protected AdView adView;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;
		config.useCompass = false;
		config.useAccelerometer = false;
		initialize(new MainGame(), config);
		
		
        
      //создём главный слой	
        RelativeLayout layout = new RelativeLayout(this);
        //устанавливаем флаги, которые устанавливались в методе initialize() вместо нас
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                       WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags( WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        //представление для LibGDX
        View gameView = initializeForView(new MainGame(), config);
        
        //представление и настройка AdMob
        
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

         //добавление представление игрык слою
        layout.addView(gameView);

        RelativeLayout.LayoutParams adParams = 
                      new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, 
                              RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); 
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); 
        //добавление представление рекламы к слою
        layout.addView(adView, adParams);  
            
        //всё соединяем в одной слое
        setContentView(layout); 
	}
}
