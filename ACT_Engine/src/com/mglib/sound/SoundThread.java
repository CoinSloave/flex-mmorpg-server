package com.mglib.sound;

import game.CMIDlet;

import javax.microedition.lcdui.Display;
import javax.microedition.media.Player;

/**
 * <p>
 * Title: engine of RPG game
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: mig
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public final class SoundThread implements Runnable {
	private Thread soundThread;
	private boolean Enable = false;

	public Player Players[];
	private Player LastSound, SoundMelody;
	private int loopCount = 1;
	private boolean isRunning;

	public static boolean m_soundIsOn = false;

	public static boolean m_vibraIsOn = false;

	public SoundThread(String name, byte midiCount) {
		Players = SoundLoader.loadSound(name, midiCount);
		isRunning = true;
		soundThread = new Thread(this);
		soundThread.setPriority(Thread.MIN_PRIORITY);
		soundThread.start();
	}

	public void run() {
		while (isRunning) {
			if (Enable) {
				if (play(SoundMelody)) {
					Enable = false;
					SoundMelody = null;
				}
			}

			Thread.yield();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isPlayed() {
		if (LastSound != null) {
			if (loopCount == -1) {
				return true;
			}

			if (LastSound.getState() == Player.STARTED) {
				return true;
			}
		}
		return false;
	}

	public void play(int index, int new_loopCount) {
		if ((!m_soundIsOn) || (Players[index] == null)) {
			return;
		}

		loopCount = new_loopCount;
		SoundMelody = Players[index];
		Enable = true;
	}

	private boolean play(Player NewSound) {
		if ((LastSound != null) && (LastSound.getState() == Player.STARTED)) {
			return false;
		}

		try {
			NewSound.setLoopCount(loopCount);
			NewSound.start();
			LastSound = NewSound;
		} catch (Exception e) {
			LastSound = NewSound;
			stop();
			return false;
		}
		return true;
	}

	public void stop() {
		if (LastSound != null) {
			if (LastSound.getState() == Player.STARTED) {
				try {
					LastSound.stop();
				} catch (Exception e) {
				}
			}
			LastSound = null;
		}
	}

	public void destroy() {
		stop();
		isRunning = false;
		Players = null;
		try {
			soundThread.join();
		} catch (Exception ex) {
		}
		soundThread = null;
	}

	public void setPriority(String priority) {
		int p;
		if (priority.equals("NORM_PRIORITY")) {
			p = Thread.NORM_PRIORITY;
		} else if (priority.equals("MIN_PRIORITY")) {
			p = Thread.MIN_PRIORITY;
		} else if (priority.equals("MAX_PRIORITY")) {
			p = Thread.MAX_PRIORITY;
		} else {
			return;
		}
		soundThread.setPriority(p);
	}

	/************************
	 * …Ë÷√’∂Ø
	 *************************/

	public static void vibrate(CMIDlet midlet, int duration_in_ms) {
		if (m_vibraIsOn) {
			try {
				Display.getDisplay(midlet).vibrate(duration_in_ms);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
