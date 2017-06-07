/**
 * Project: BroodMother
 * Package: io.github.sidmishraw.broodmother.core
 * File: BroodMother.java
 * 
 * @author sidmishraw
 *         Last modified: Jun 6, 2017 9:04:06 PM
 */
package io.github.sidmishraw.broodmother.core;

/**
 * @author sidmishraw
 *
 *         Qualified Name: io.github.sidmishraw.broodmother.core.BroodMother
 *
 */
public class BroodMother {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Spiderling sp1 = new Spiderling("https://gist.github.com/sidmishraw",
				"https://gist.github.com/sidmishraw");
		Thread t1 = new Thread(sp1);
		t1.start();
	}
}
