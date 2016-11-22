package com.danielacedo.psp;

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
	
	public static final int MAX_SQUARE = 70; //Last square in the board
	public static final int MIN_SQUARE = 1;  //First square in the board
	
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
	}
	
	public void checkWinCondition(){
		
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
		

	
	public synchronized void moveTurtle(int movement){
		System.out.println("The turtle is about to move");
		
		try {
			
			while(getCurrentTurn() != Players.TURTLE.getNumVal()){
				System.out.println("The turtle is waiting to move");
				this.wait();
			}
			
			if(!isFinished()){
				turtle_position = clampMovement(turtle_position + movement);
				System.out.println("\nThe turtle moved "+movement+" positions to square "+turtle_position);
				
				this.currentTurn= (this.currentTurn + 1) % Players.values().length;
				
				this.notify();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public synchronized void moveHare(int movement){
		
		System.out.println("The hare is about to move");
		
		try {
			while(getCurrentTurn() != Players.HARE.getNumVal()){
				System.out.println("The hare is waiting to move");
				this.wait();
			}
			
			if(!isFinished()){
				hare_position = clampMovement(hare_position + movement);
				System.out.println("\nThe hare moved "+movement+" positions to square "+hare_position);
				
				this.currentTurn= (this.currentTurn + 1) % Players.values().length;
				
				checkWinCondition();
				
				this.notify();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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

