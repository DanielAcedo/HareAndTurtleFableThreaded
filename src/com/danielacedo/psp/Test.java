package com.danielacedo.psp;

public class Test {

	public static void main(String[] args) {
		GameBoard board = new GameBoard();
		
		Hare hare = new Hare(board);
		Turtle turtle = new Turtle(board);
		
		hare.start();
		turtle.start();

	}

}
