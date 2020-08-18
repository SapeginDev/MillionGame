package com.example.milliongamne;

import androidx.appcompat.app.AppCompatActivity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, SoundPool.OnLoadCompleteListener {
    final int MAX_STREAMS = 3;
    private LinearLayout layout;

    private SoundPool sp;
    private int soundIdKeyPress, soundIdActionFailed, soundIdCheerUp;
    private int countDown, countCheerUp, countMessage, countDownMessage;
    private Random random = new Random();
    private String[] messagesCheerUp;

    //Анимация текста
    TextView messageView;
    private Animation mFadeInAnimation, mFadeOutAnimation;
    private boolean animationComplete = true;
    private int width;
    private int height;
    private ButtonPosition buttonPosition;

    private int getRandomInt(int max){
        return random.nextInt(max) + 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.leanearLayout);
        layout.setOnTouchListener(this);

        Button button = (Button) findViewById(R.id.button);
        button.setOnTouchListener(this);

        //Инициализация звуков
        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);

        soundIdKeyPress = sp.load(this, R.raw.keypress, 1);
        soundIdActionFailed = sp.load(this, R.raw.actionfailed, 1);
        soundIdCheerUp = sp.load(this, R.raw.cheerup, 1);

        countDown = 0;
        countCheerUp = getRandomInt(20);

        messagesCheerUp = getResources().getStringArray(R.array.cheerUpStrings);
        countMessage = getRandomInt(20);
        countDownMessage = 0;

        //Анимация
        messageView = (TextView) findViewById(R.id.messageCheerUp);
        // подключаем файл анимации
        mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        mFadeInAnimation.setAnimationListener(animationFadeInListener);

        ViewTreeObserver viewTreeObserver = layout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    width = layout.getWidth();
                    height = layout.getHeight();
                }
            });
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {

    }

    public void onClickButton(View v){
        TextView timerView = (TextView) findViewById(R.id.timer);
        timerView.setText("Ура!!! Ты выиграл. За Миллионом можешь обраиться к своей МАМЕ!");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = 0f;
        float y = 0f;

        int playSound = 0;
        if (view.getId() == R.id.button){
            playSound = soundIdActionFailed;
        } else if (view.getId() == R.id.leanearLayout) {
            playSound = soundIdKeyPress;
        }

        x = motionEvent.getX();
        y = motionEvent.getY();

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && playSound != 0) {
            countDown++;
            if (animationComplete) {
                countDownMessage++;
            }
            if (countCheerUp == countDown){
                sp.play(soundIdCheerUp, 1, 1, 0, 0, 1);
                countDown = 0;
                countCheerUp = getRandomInt(20);
            } else {
                sp.play(playSound, 1, 1, 0, 0, 1);
            }

            //Сообщение
            if (countDownMessage == countMessage && animationComplete){
                messageView.setText(messagesCheerUp[getRandomInt(messagesCheerUp.length - 1)]);
                messageView.startAnimation(mFadeInAnimation);
                countDownMessage = 0;
            }
        }

        if (view.getId() != R.id.button){
            return true;
        }

        if (motionEvent.getAction() != MotionEvent.ACTION_DOWN){
            return true;
        }

        if (buttonPosition == null){
            buttonPosition = new ButtonPosition((Button) view, width, height);
        }
        buttonPosition.setNewPositionXY(x, y);

//        TextView positionX = (TextView) findViewById(R.id.positionX);
//        TextView positionY = (TextView) findViewById(R.id.positionY);
//        positionX.setText("X: " + String.valueOf(x));
//        positionY.setText("Y: " + String.valueOf(y));

        Button button = buttonPosition.getButton();

        if (x > button.getX() && x < button.getX() + button.getWidth() && y > button.getY() && y < button.getY() + button.getHeight()){
            onClickButton(button);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        messageView.clearAnimation();
    }

    Animation.AnimationListener animationFadeInListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            messageView.clearAnimation();
            messageView.setText("");
            animationComplete = true;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
            animationComplete = false;
        }
    };
}