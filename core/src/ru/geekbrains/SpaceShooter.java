package ru.geekbrains;


import com.badlogic.gdx.Game;

import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.screen.MenuScreen;


public class SpaceShooter extends Game {
	@Override
	public void create() {
		new ScreenController(this);
	}
}
