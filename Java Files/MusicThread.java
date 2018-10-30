package hmPKG;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**
 * MusicThread manages music playing during the game and main menu screen. The song to be played is given to MusicThread as a String for the file name.
 */
public class MusicThread implements Runnable{
	private Thread t;
	private Clip c;
	private String name;
	/**
	 * Constructs a MusicThread given a file name
	 * @param n the file name
	 */
	public MusicThread(String n) {
		name = n;
	}
	/**
	 * Starts the thread
	 */
	public void start (){
		t = new Thread(this,"Music Thread");
		t.start();
	}
	/**
	 * Stops the thread by stopping the clip.
	 */
	@SuppressWarnings("deprecation")
	public void stop() {
		c.stop();
		t.stop();
	}
	/**
	 * Inherited method run, runs the thread.
	 */
	public void run() {
		for(int count = 1; count <=10; count++) {
			try {
				File f = new File(name);
				c = AudioSystem.getClip();
				c.open(AudioSystem.getAudioInputStream(f));	
				c.start();
				Thread.sleep(c.getMicrosecondLength()/1000);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
