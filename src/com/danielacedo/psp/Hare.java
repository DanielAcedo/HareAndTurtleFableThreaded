package com.danielacedo.psp;

public class Hare extends Thread{
	
	private GameBoard board;
	
	public Hare(GameBoard board){
		this.board = board;
	}
	
	@Override
	public void run (){
		while(!board.isFinished()){
			board.moveHare();
		}
	}
}
