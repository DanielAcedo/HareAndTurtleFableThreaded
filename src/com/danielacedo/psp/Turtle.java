package com.danielacedo.psp;

import java.util.Random;

public class Turtle extends Thread{
	
private GameBoard board;
	
	public Turtle(GameBoard board){
		this.board = board;
	}
	
	@Override
	public void run(){
		Random rnd = new Random();
		
		while(!board.isFinished()){
			board.moveTurtle(1+rnd.nextInt(10-1));
		}
	}

}
