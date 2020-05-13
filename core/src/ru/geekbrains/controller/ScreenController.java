package ru.geekbrains.controller;

import com.badlogic.gdx.Screen;

import ru.geekbrains.SpaceShooter;
import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.screen.MenuScreen;

public class ScreenController {

    private final SpaceShooter spaceShooter;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    public ScreenController(SpaceShooter spaceShooter) {
        this.spaceShooter = spaceShooter;
        this.menuScreen = new MenuScreen();
        this.menuScreen.setScreenController(this);
        setMenuScreen();
    }

    public void setMenuScreen(){
        spaceShooter.setScreen(menuScreen);
    }

    public void setGameScreen(){
        if(this.gameScreen == null){
            this.gameScreen = new GameScreen();
            this.gameScreen.setScreenController(this);
        }
         spaceShooter.setScreen(gameScreen);
        }
}



