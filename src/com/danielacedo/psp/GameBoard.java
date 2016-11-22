package com.danielacedo.psp;

import java.util.Random;

public class GameBoard {
	
	public enum Players{
		HARE(0), TURTLE(1);
		
		private int numVal;
		
		private Players(int numVal){
			this.numVal = numVal;
		}

		public int getNumVal() {
			return numVal;
		}
		
	};
	
	private Random rnd;
	
	public static final int MAX_SQUARE = 70; //Last square in the board
	public static final int MIN_SQUARE = 1;  //First square in the board
	
	private static final int TURTLE_FAST_ADVANCE = 3;
	private static final int TURTLE_SLIP = -6;
	private static final int TURTLE_SLOW_ADVANCE = 1;
	
	private static final int HARE_SLEEP = 0;
	private static final int HARE_BIG_JUMP = 9;
	private static final int HARE_BIG_SLIP = -12;
	private static final int HARE_SMALL_JUMP = 1;
	private static final int HARE_SMALL_SLIP = -2; 
	
	public static final Object mutexTurn = new Object();
	public static final Object mutexFinished = new Object();
	
	private int hare_position;
	private int turtle_position;
	
	private boolean finished;
	
	private int currentTurn;
	
	public GameBoard(){
		this.finished = false;
		this.currentTurn = Players.TURTLE.getNumVal();
		hare_position = MIN_SQUARE;
		turtle_position = MIN_SQUARE;
		
		rnd = new Random();
	}
	
	public void checkWinCondition(){
		
		if(hare_position == MAX_SQUARE && turtle_position == MAX_SQUARE){
			System.out.println("It's a draw!");
			finished = true;
		}else if(hare_position == 70){
			System.out.println("The hare won!");
			finished = true;
			
		}else if (turtle_position == MAX_SQUARE){
			System.out.println("The turtle won!");
			finished = true;
		}
	}
	
	private void drawBoard(){
		System.out.println("Positions!");
		System.out.println("------------\n");
		
		for(int i = 1; i<=70; i++){
			if(i == hare_position && i == turtle_position){
				System.out.print("B");
			}else if(i == hare_position){
				System.out.print("H");
			}else if (i == turtle_position){
				System.out.print("T");
			}else{
				System.out.print("-");
			}
		}
		
		System.out.println("\n");
	}
		

	
	public synchronized void moveTurtle(){
		
		try {
			
			while(getCurrentTurn() != Players.TURTLE.getNumVal()){
				this.wait();
			}
			
			if(!isFinished()){
				int movement = turtleActions();
				turtle_position = clampMovement(turtle_position + movement);
				System.out.println("\nThe turtle moved "+movement+" positions to square "+turtle_position);
				
				this.currentTurn= (this.currentTurn + 1) % Players.values().length;
				
				drawBoard();
				
				this.notify();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public synchronized void moveHare(){
		
		
		try {
			while(getCurrentTurn() != Players.HARE.getNumVal()){
				this.wait();
			}
			
			if(!isFinished()){
				int movement = hareActions();
				hare_position = clampMovement(hare_position + movement);
				System.out.println("\nThe hare moved "+movement+" positions to square "+hare_position);
				
				this.currentTurn= (this.currentTurn + 1) % Players.values().length;
				
				drawBoard();
				
				checkWinCondition();
				
				this.notify();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	private int turtleActions(){
		int movement = 0;
		int roll = rnd.nextInt(101);
		
		if(roll < 20){
			movement = TURTLE_SLIP;
		}else if (roll >= 20 && roll < 50){
			movement = TURTLE_SLOW_ADVANCE;
		}else{
			movement = TURTLE_FAST_ADVANCE;
		}
		
		return movement;
	}
	
	private int hareActions(){
		int movement = 0;
		int roll = rnd.nextInt(101);
		
		if(roll < 20){
			movement = HARE_SLEEP;
		}else if (roll >= 20 && roll < 50){
			movement = HARE_SMALL_JUMP;
		}else if(roll >= 50 && roll < 60){
			movement = HARE_BIG_SLIP;
		}else if (roll >= 60 && roll < 80){
			movement = HARE_SMALL_SLIP;
		}else{
			movement = HARE_BIG_JUMP;
		}
		
		return movement;
	}
	
	
	public boolean isFinished() {
		synchronized (mutexFinished){
			return finished;
		}
	}
	

	public void setFinished(boolean finished) {
		synchronized (mutexFinished){
			this.finished = finished;
		}
	}
	

	public int getCurrentTurn() {
		synchronized (mutexTurn){
			return currentTurn;
		}
	}
	

	public void setCurrentTurn(int currentTurn) {
		synchronized (mutexTurn){
			this.currentTurn = currentTurn;
		}
	}

	private int clampMovement(int position){
		int finalPosition;
		
		if(position > MAX_SQUARE)
			finalPosition = MAX_SQUARE;
		else if (position < MIN_SQUARE)
			finalPosition = MIN_SQUARE;
		else
			finalPosition = position;
		
		return finalPosition;
	}
}

