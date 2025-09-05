package snake;

import java.io.IOException;

public class OVSnake {
	
	/*
	 
		 +--------------------+
		 | One Variable Snake |
		 +--------------------+
	
	 */
	
	
	// the only declared variable :)
	private static boolean[] a = new boolean[2096];
	
	
	
	// 8-bit-esque system
	
	// 0..2047 is game field ( #0..255 )
	// 2048..2055 is iterator ( #256 )
	// 2056..2063 is iterator 2 ( #257 )
	// 2064..2071 is snake length ( #258 )
	// 2072..2079 is snake direction ( #259 ) -> up down left right
	// 2080..2087 is snake head position ( #260 ) -> x + 16 * y
	// 2088..2095 is temporary value ( #261 )

	public static void main(String[] args) throws IOException {
		// empty -> 0
		// head -> 1
		// body -> 2..254
		// apple -> 255
		
		// init snake
		wP(0, 0, 3);
		wP(1, 0, 2);
		wP(2, 0, 1);
		
		wB(258, 3);// length
		wB(259, 3);// direction
		
		makeApple();
		
		printGame();
		
		
		
		while(iterateGame() == 0) {
			System.out.println("================================");
			printGame();
			System.out.print("next: 0-up 1-down 2-left 3-right\n>");
			wB(259, System.in.read()-48);
			System.in.read();// read the newline
		}
		
	}
	
	private static int rB(int pos) {// read byte
		pos*=8;
		return 
			128 * (a[pos  ] ? 1 : 0) + 
			64  * (a[pos+1] ? 1 : 0) + 
			32  * (a[pos+2] ? 1 : 0) + 
			16  * (a[pos+3] ? 1 : 0) + 
			8   * (a[pos+4] ? 1 : 0) + 
			4   * (a[pos+5] ? 1 : 0) + 
			2   * (a[pos+6] ? 1 : 0) + 
			1   * (a[pos+7] ? 1 : 0)
		;
	}
	
	private static void wB(int pos, int value) {// write byte
		pos*=8;
		if(value >= 128) {a[pos  ]=true;value-=128;} else {a[pos  ]=false;}
		if(value >= 64 ) {a[pos+1]=true;value-=64 ;} else {a[pos+1]=false;}
		if(value >= 32 ) {a[pos+2]=true;value-=32 ;} else {a[pos+2]=false;}
		if(value >= 16 ) {a[pos+3]=true;value-=16 ;} else {a[pos+3]=false;}
		if(value >= 8  ) {a[pos+4]=true;value-=8  ;} else {a[pos+4]=false;}
		if(value >= 4  ) {a[pos+5]=true;value-=4  ;} else {a[pos+5]=false;}
		if(value >= 2  ) {a[pos+6]=true;value-=2  ;} else {a[pos+6]=false;}
		if(value >= 1  ) {a[pos+7]=true;value-=1  ;} else {a[pos+7]=false;}
	}
	
	private static void wP(int x, int y, int value) {// read position
		wB(x + 16 * y, value);
	}
	private static int rP(int x, int y) { // write position
		return rB(x + 16 * y);
	}
	
	private static void printGame() {
		for(wB(256, 0); rB(256) < 16; wB(256, rB(256) + 1)) {
			for(wB(257, 0); rB(257) < 16; wB(257, rB(257) + 1)) {
				if(rP(rB(257), rB(256)) > 0) {
					if(rP(rB(257), rB(256)) == 1) {
						System.out.print("{}");
					}else if(rP(rB(257), rB(256)) == 255) {
						System.out.print("<>");
					}else {
						System.out.print("[]");
					}
				}else {
					System.out.print(".'");
				}
			}
			System.out.print('\n');
		}
	}
	
	
	private static int iterateGame() {
		for(wB(256, 0); rB(256) < 16; wB(256, rB(256) + 1)) {
			for(wB(257, 0); rB(257) < 16; wB(257, rB(257) + 1)) {
				
				if(rP(rB(256), rB(257)) > 0) {// if not empty
					
					if(rP(rB(256), rB(257)) == 1){// if head
						
						switch(rB(259)) {// switch direction
						
						case 0: // up
							if(rB(257) == 0) {endGame();return 1;}
							if(rP(rB(256), rB(257)-1) > 0) {
								if(rP(rB(256), rB(257)-1) == 255) {
									// apple
									wB(258, rB(258) + 1);makeApple();// increment length
								}else {
									endGame();return 1;// self :(
								}
							}
							wB(260, rB(256) + 16 * (rB(257)-1));
							break;
						case 1: // down
							if(rB(257) == 15) {endGame();return 1;}
							if(rP(rB(256), rB(257)+1) > 0) {
								if(rP(rB(256), rB(257)+1) == 255) {
									wB(258, rB(258) + 1);makeApple();
								}else {
									endGame();return 1;
								}
							}
							wB(260, rB(256) + 16 * (rB(257)+1));
							break;
						case 2: // left
							if(rB(256) == 0) {endGame();return 1;}
							if(rP(rB(256)-1, rB(257)) > 0) {
								if(rP(rB(256)-1, rB(257)) == 255) {
									wB(258, rB(258) + 1);makeApple();
								}else {
									endGame();return 1;
								}
							}
							wB(260, rB(256) - 1 + 16 * rB(257));
							break;
						case 3: // right
							if(rB(256) == 15) {endGame();return 1;}
							if(rP(rB(256)+1, rB(257)) > 0) {
								if(rP(rB(256)+1, rB(257)) == 255) {
									wB(258, rB(258) + 1);makeApple();
								}else {
									endGame();return 1;
								}
							}
							wB(260, rB(256) + 1 + 16 * rB(257));
							break;
						
						}
						
					}
					
					// increment counter
					wP(rB(256), rB(257), rP(rB(256), rB(257)) + 1);
					
					if(rP(rB(256), rB(257)) == 255) {continue;}// if this apple
					
					if(rP(rB(256), rB(257)) > rB(258)) {// if counter > length
						wP(rB(256), rB(257), 0);// clear
					}
					
				}
				
			}
		}
		
		wB(rB(260), 1);// write new head
		
		return 0; // no death (1 is death)
		
	}
	
	private static void endGame() {
		System.out.println("game over");
	}
	
	private static void makeApple() {
		
		if(rB(258) >= 256) {endGame();return;}// if screen full (max length)
		
		while(rB(rB(261)) != 0) {
			wB(261, (int)(Math.random()*16) + 16*(int)(Math.random()*16)); // new random position
		}// this would only inf loop if entire screen full, which wont happen because thats already checked
		
		wB(rB(261), 255); // place apple
		
	}
	

}