package com.is_great.pro.pets;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

public class PetProfileActivity extends AppCompatActivity {

    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);
        mScrollView = (ScrollView) findViewById(R.id.ScrollView);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        final int startScrollPos =
                getResources().getDimensionPixelSize(R.dimen.init_scroll_up_distance);
        Animator animator = ObjectAnimator.ofInt(
                mScrollView,
                "scrollY",
                startScrollPos
        ).setDuration(300);
        animator.start();
    }

}
