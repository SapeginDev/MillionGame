package com.example.milliongamne;

import android.widget.Button;

import java.util.Random;

public class ButtonPosition {
    protected int screenWidth, screenHeight, buttonWidth, buttonHeight;

    private Random random = new Random();
    private Button button;

    private float x, y;

    public ButtonPosition(Button button, int screenWidth, int screenHeight) {
        this.button = button;
        this.buttonWidth = button.getWidth();
        this.buttonHeight = button.getHeight();
        this.x = button.getX();
        this.y = button.getY();

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public Button getButton(){
        return button;
    }

    private int getRandomInt(int max){
        return random.nextInt(max) + 1;
    }

    private boolean trySetNewPosition(float x, float y){
        if (x < 0 || (x + buttonWidth) > screenWidth || y < 0 || (y + buttonHeight) > screenHeight) {
            return false;
        }
        return true;
    }

    public void setNewPositionXY(float downX, float downY){
        float trialX = x;
        float trialY = y;

        //Вычисляем новый x
       int randomSign = getRandomInt(2);

        if (randomSign == 1){
            //плюс
            trialX = trialX + downX;
            if (!trySetNewPosition(trialX, trialY)){
                trialX = trialX - (buttonWidth - downX);
            }
        }

        if (randomSign == 2){
            //Минус
            trialX = trialX - (buttonWidth - downX);

            if (!trySetNewPosition(trialX, trialY)){
                trialX = trialX + downX;
            }
        }

        //Вычисляем новый y
        randomSign = getRandomInt(2);

        if (randomSign == 1){
            //плюс
            trialY = trialY + downY;
            if (!trySetNewPosition(trialX, trialY)){
                trialY = trialY - (buttonHeight - downY);
            }
        }

        if (randomSign == 2){
            //Минус
            trialY = trialY - (buttonHeight - downY);

            if (!trySetNewPosition(trialX, trialY)){
                trialY = trialY + downY;
            }
        }

        button.setX(trialX);
        button.setY(trialY);
        this.x = trialX;
        this.y = trialY;
    }
}
