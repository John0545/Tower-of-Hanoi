package com.example.ravi.tweenanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity {

    private static int[] towers = {R.id.tower_1, R.id.tower_2, R.id.tower_3};

    private static final int UNDECIDED = -1;
    private int fromTower = UNDECIDED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < towers.length; ++i) {
            ViewGroup tower = findViewById(towers[i]);
            tower.setOnClickListener(new TowerPicker(i));
        }

    }

    private class BlockMover implements Animation.AnimationListener {
        private View block;
        private int from;
        private int to;

        protected BlockMover(View block, int from, int to) {
            this.to = to;
            this.from = from;
            this.block = block;
        }

        public void move() {
            int block_anim_id;
            if (to < from)
                block_anim_id = R.anim.move_block_left;
            else block_anim_id = R.anim.move_block_right;

            Animation removeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), block_anim_id);
            block.startAnimation(removeAnimation);
            Log.e("TAG", "Animation started");

            removeAnimation.setAnimationListener(this);
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            Log.e("TAG","onAnimaionEnd called");
            block.post(new Runnable() {
                @Override
                public void run() {

                    ViewGroup toTower = findViewById(towers[to]);
                    ViewGroup fromTower = (ViewGroup) block.getParent();
                    fromTower.removeView(block);
                    Log.e("from Count",""+fromTower.getChildCount());
                    fromTower.clearDisappearingChildren();

                    toTower.addView(block);
                    Log.e("to Count",""+toTower.getChildCount());

                    Animation addAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.block_drop);
                    block.startAnimation(addAnimation);

                    ;
                }
            });
        }

        @Override
        public void onAnimationRepeat(Animation animation) {


        }


    }

    private class TowerPicker implements View.OnClickListener {

        private int towerIndex;

        public TowerPicker(int towerIndex) {
            this.towerIndex = towerIndex;
        }

//        @Override
//        public void onClick(View v) {
//            StringBuilder message = new StringBuilder();
//            ViewGroup from = (ViewGroup) v;
//            TextView child = findViewById(R.id.block_1);
//            from.removeView(child);
////            from.removeView(findViewById(R.id.block_1));
//            Log.e("Onclick","From child+"+from.getChildCount()+"");
//            ViewGroup to = findViewById(towers[1]);
//
//            if (child==null)
//                Log.e("Child","null");
//            else to.addView(child);
//           Log.e("Onclick","To child"+to.getChildCount()+"");
//            Log.e("Onclick",""+v.getId());
//        }

        @Override
        public void onClick(View view) {

            if (fromTower == UNDECIDED) {
                ViewGroup tower = findViewById(towers[towerIndex]);
                if (tower.getChildCount() > 0) {
                    fromTower = towerIndex;
                    Animation glow_animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tower_glow);
                    tower.startAnimation(glow_animation);

                }
            } else {
                ViewGroup fromTowerView = findViewById(towers[fromTower]);
                if (fromTower != towerIndex) {
                    ViewGroup toTowerView = findViewById(towers[towerIndex]);
                    View block = fromTowerView.getChildAt(0);
                    Log.e("TAG",""+block.toString());
                    View supportingBlock = toTowerView.getChildAt(0);
                    if (supportingBlock == null || supportingBlock.getWidth() > block.getWidth()) {
                        new BlockMover(block, fromTower, towerIndex).move();
                    }
                }

                fromTowerView.clearAnimation();
                fromTower = UNDECIDED;
            }


        }
    }
}