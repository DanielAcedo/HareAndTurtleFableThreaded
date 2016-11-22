package com.danielacedo.psp;


public class Turtle extends Thread{
	
private GameBoard board;
	
	public Turtle(GameBoard board){
		this.board = board;
	}
	
	@Override
	public void run(){
	
		while(!board.isFinished()){
			board.moveTurtle();
		}
	}

}
