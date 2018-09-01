package com.example.arvind.toizone;

import android.app.ActionBar;
import android.content.Intent;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class Splash extends AwesomeSplash {

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }*/

    @Override
    public void initSplash(ConfigSplash configSplash) {

        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        configSplash.setBackgroundColor(R.color.logo);
        configSplash.setAnimCircularRevealDuration(2000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);


        configSplash.setLogoSplash(R.drawable.logo6);

        configSplash.setAnimLogoSplashDuration(2000);
        //configSplash.setAnimLogoSplashTechnique(Techniques.BounceInDown);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeInRight);
        configSplash.setOriginalHeight(10);
        configSplash.setOriginalWidth(10);

        configSplash.setTitleSplash(getString(R.string.splashtitle));
        configSplash.setAnimTitleDuration(2000);
        // configSplash.setTitleFont(getString(R.string.Font));
        configSplash.setTitleTextColor(R.color.text);
        configSplash.setTitleTextSize(30f);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);


    }

    @Override
    public void animationsFinished() {
        startActivity(new Intent(Splash.this,MapsActivity.class));
        finish();
    }
}
