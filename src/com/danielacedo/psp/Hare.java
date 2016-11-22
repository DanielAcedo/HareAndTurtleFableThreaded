package com.danielacedo.psp;

import java.util.Random;

public class Hare extends Thread{
	
	private GameBoard board;
	
	public Hare(GameBoard board){
		this.board = board;
	}
	
	@Override
	public void run (){
		Random rnd = new Random();
		
		while(!board.isFinished()){
			board.moveHare(1+rnd.nextInt(10-1));
		}
	}
}
