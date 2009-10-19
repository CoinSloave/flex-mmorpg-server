package com.mglib.sound;

import game.CMIDlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
public class SoundPlayer {
	// public SoundPlayer ( String name, byte midiCount )
	// {
	// initSound ( name, midiCount );
	// }

	public static final String s_filenameSound = "/bin/s.bin";

	public static boolean m_isMusicOn = false; // midi����
	public static boolean m_isSoundOn = false; // wav����
	public static boolean m_vibraIsOn = false; // ������

	private static int m_curWaveID;
	private static int m_curMidiID;

	public static byte m_MIDICount;

	private static Player[] m_sound;

	// private static Player sound

	private static final byte MIDICOUNT = 4;

	public static void initSound(String name, byte midiCount) {
		// #if mode_noSound
		// # if (true) {
		// # return ;
		// # }
		// #endif

		setmidiCount(midiCount);
		// m_sound = SoundLoader.loadSound (name, m_MIDICount);
		SoundLoader.loadSingleSound(name);
	}

	public static void setmidiCount(byte count) {
		m_MIDICount = count;
	}

	/**
	 * ��������
	 * 
	 * @param id
	 *            byte
	 */
	public static void playSound(int id, int loopCount) {
		// #if mode_noSound
		// # if (true) {
		// # return ;
		// # }
		// #endif

		if (m_sound == null) {
			System.out.println(">>>>>>> " + " m_sound" + "������ ");
			return;
		}
		if (id <= m_MIDICount) { // midi
			if (!m_isMusicOn) {
				m_curMidiID = id;
				return;
			}
		} else // wav
		{
			if (!m_isSoundOn) {
				m_curWaveID = id;
				return;
			}
		}

		if (m_sound[id] == null
				|| (m_sound[id].getState() == javax.microedition.media.Player.STARTED)) {
			if (m_sound[id] == null) {
				System.out
						.println(">>>>>>> " + " m_sound[" + id + "]" + "������ ");
			}
			return;
		}

		if (id <= m_MIDICount) { // midi
			if (m_curMidiID >= 0)
				stopSound(m_curMidiID); // stop last midi
			m_curMidiID = id;
		} else {
			if (m_curWaveID >= 0) {
				stopSound(m_curWaveID); // stop last wav
			}
			m_curWaveID = id;
		}
		try {
			// m_sound[id].realize ();
			// m_sound[id].prefetch ();

			if (m_sound[id].getState() != Player.PREFETCHED) {
				// m_sound[id].realize ();
				m_sound[id].prefetch();
			}
			m_sound[id].setLoopCount(loopCount);
			m_sound[id].start(); // ��������
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getCurMidiID() {
		return m_curMidiID;
	}

	public static int getCurWavID() {
		return m_curWaveID;
	}

	// /**
	// * ��������ѭ��
	// * @param timer int
	// */
	// private static void setLoop ( int loopCount )
	// {
	// if ( m_sound != null )
	// {
	// setLoopCount ( loopCount );
	// }
	// }
	/**
	 * ����������С
	 */
	public static void setVolume() {

	}

	public static void stopSound(int id) {
		// #if mode_noSound
		// # if (true) {
		// # return ;
		// # }
		// #endif

		try {
			if (m_sound[id] == null) {
				System.out
						.println(">>>>>>> " + " m_sound[" + id + "]" + "������ ");
				return;
			}
			if (m_sound[id].getState() == javax.microedition.media.Player.STARTED)
				m_sound[id].stop();
			// if ( m_sound[id].getState () ==
			// javax.microedition.media.Player.PREFETCHED )
			// m_sound[id].deallocate ();
		} catch (Exception e) {
		}
	}

	public static void resumeSound() {
		if (m_sound == null) {
			return;
		}

		try {
			if (m_isSoundOn && m_curWaveID >= 0 && m_sound[m_curWaveID] != null) {
				m_sound[m_curWaveID].start();
			}
			if (m_isMusicOn && m_curMidiID >= 0 && m_sound[m_curMidiID] != null) {
				m_sound[m_curMidiID].start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/************************
	 * ������
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

	// ���������������Ĳ���

	private static Player m_player;

	public static void playSingleSound(int id, int loopCount) {

		// #if mode_noSound
		// # if (true) {
		// # return ;
		// # }
		// #endif

		if (!m_isMusicOn) {
			m_curMidiID = id;
			return;
		}

		// playing
		if (id < 0) {
			return;
		}

		// moto
		if (m_player == null || m_curMidiID != id) {
			stopSingleSound();
			m_curMidiID = id;

			InputStream is = new ByteArrayInputStream(
					SoundLoader.m_soundBuffer[id], 0,
					SoundLoader.m_soundBuffer[id].length);
			try {
				m_player = javax.microedition.media.Manager.createPlayer(is,
						"audio/midi");
				m_player.realize();
				m_player.setLoopCount(loopCount);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (javax.microedition.media.MediaException e) {
				e.printStackTrace();
			}
		} else if (m_player.getState() == javax.microedition.media.Player.STARTED) {
			return;
		}

		try {
			m_player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stopSingleSound() {
		// #if mode_noSound
		// # if (true) {
		// # return ;
		// # }
		// #endif

		// stop last wav or midi
		try {
			if (m_player == null)
				return;
			if (m_player.getState() == javax.microedition.media.Player.STARTED)
				m_player.stop();
			// if (m_player.getState() ==
			// javax.microedition.media.Player.PREFETCHED)
			// m_player.deallocate();
			m_player.close();
			// m_curMidiID = -1;
			m_player = null;
			// #if !W958C
			System.gc();
			// #endif

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
