package com.mglib.sound;

import game.dSoundID;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

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
public class SoundLoader {
	public SoundLoader() {
	}

	private final static byte AUDIO_MIDI = 0;
	private final static byte AUDIO_AMR = 1;
	private final static byte AUDIO_X_WAV = 2;
	private final static byte AUDIO_MMF = 3;

	private static String soundTypes[] = { "audio/midi", "audio/amr",
			"audio/x-wav", "audio/mmf" };
	private static String fileExtensions[] = { ".mid", ".amr", ".wav", ".mmf" };

	// 是否显示加载声音所消耗的内存
	static boolean isShowMemInSound = false;

	public static Player[] loadSound(String soundName, byte MidiCount) {
		// #if mode_noSound
		// # if (true) {
		// # return null;
		// # }
		// #endif
		try {
			DataInputStream dis = new DataInputStream("".getClass()
					.getResourceAsStream( /* "/bin/" + */soundName));
			int m_nDataBlocksCount = dis.readShort();
			int[] m_dataBlocksOffset = new int[m_nDataBlocksCount + 1];
			m_dataBlocksOffset[0] = 0;
			for (int block_id = 1; block_id < m_nDataBlocksCount + 1; ++block_id) {
				m_dataBlocksOffset[block_id] = dis.readInt(); // - table_size;
			}

			javax.microedition.media.Player m_sound[] = new javax.microedition.media.Player[m_nDataBlocksCount];
			byte[] buffer = new byte[m_dataBlocksOffset[m_nDataBlocksCount]];
			int[] header = m_dataBlocksOffset;
			dis.read(buffer);
			dis.close();

			// #if !W958C
			System.gc();
			// #endif

			for (int i = 0; i < m_nDataBlocksCount; i++) {
				if (isShowMemInSound) {
					// #if !W958C
					System.gc();
					// #endif

					System.out.println("++++ sound " + i + " : "
							+ Runtime.getRuntime().freeMemory());
				}

				InputStream is;
				int length = header[i + 1] - header[i];
				if (length > 0) {
					if (i <= MidiCount) {
						is = new ByteArrayInputStream(buffer, header[i], length);
						m_sound[i] = javax.microedition.media.Manager
								.createPlayer(is, soundTypes[AUDIO_MIDI]);
					} else {
						byte[] wavBff = new byte[length];
						System.arraycopy(buffer, header[i], wavBff, 0, length);
						is = new ByteArrayInputStream(wavBff);
						m_sound[i] = javax.microedition.media.Manager
								.createPlayer(is, soundTypes[AUDIO_X_WAV]);
					}
					m_sound[i].realize();
					// m_sound[i].prefetch();
					is.close();
				}
				is = null;
				if (isShowMemInSound) {
					Thread.sleep(100);
					// #if !W958C
					System.gc();
					// #endif

					System.out.println("---- sound " + i + " : "
							+ Runtime.getRuntime().freeMemory());
				}
			}
			header = null;
			buffer = null;
			// #if !W958C
			System.gc();
			// #endif

			return m_sound;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 加载所有声音数据，保存到字节数组m_soundBuffer中
	public static byte[][] m_soundBuffer = null;

	public static void loadSingleSound(String soundName) {
		try {
			DataInputStream dis = new DataInputStream("".getClass()
					.getResourceAsStream( /* "/bin/" + */soundName));

			int m_nDataBlocksCount = dis.readShort();
			int[] m_dataBlocksOffset = new int[m_nDataBlocksCount + 1];
			m_dataBlocksOffset[0] = 0;
			for (int block_id = 1; block_id < m_nDataBlocksCount + 1; ++block_id) {
				m_dataBlocksOffset[block_id] = dis.readInt(); // - table_size;
			}

			// m_sound = new
			// javax.microedition.media.Player[m_nDataBlocksCount];
			byte[] buffer = new byte[m_dataBlocksOffset[m_nDataBlocksCount]];
			int[] header = m_dataBlocksOffset;
			m_soundBuffer = new byte[m_nDataBlocksCount][];
			dis.read(buffer);
			dis.close();
			// #if !W958C
			System.gc();
			// #endif
			for (int i = 0; i < m_nDataBlocksCount; i++) {
				int length = header[i + 1] - header[i];
				m_soundBuffer[i] = new byte[length];
				System
						.arraycopy(buffer, header[i], m_soundBuffer[i], 0,
								length);

			}
			header = null;
			buffer = null;
			// #if !W958C
			System.gc();
			// #endif

		} catch (Exception e) {
		}
	}

	/**
	 * 返回某一关的背景音
	 * 
	 * @param level
	 *            int
	 * @return String
	 */
	public static byte getLevelBGM(int level) {
		if (level < 0) {
			return dSoundID.Sound_ID_KAICHANG;
		} else {
			return LEVEL_BGM[level];
		}
	}

	// #if size_500k
	public static final byte[] LEVEL_BGM = { dSoundID.Sound_ID_GAOFU,
			dSoundID.Sound_ID_DAMINGFU, dSoundID.Sound_ID_DAMINGFU,
			dSoundID.Sound_ID_BATTLE, dSoundID.Sound_ID_BATTLE,
			dSoundID.Sound_ID_BATTLE, dSoundID.Sound_ID_CHENGZHEN,
			dSoundID.Sound_ID_JUYITING, dSoundID.Sound_ID_BATTLE,
			dSoundID.Sound_ID_DIDAO, dSoundID.Sound_ID_BATTLE,
			dSoundID.Sound_ID_BATTLE, dSoundID.Sound_ID_HONGLOU,
			dSoundID.Sound_ID_BATTLE, dSoundID.Sound_ID_CHENGZHEN,
			dSoundID.Sound_ID_DILAO, };
	// #else
	// # public static final byte[] LEVEL_BGM={
	// # dSoundID.Sound_ID_GAOFU,
	// # dSoundID.Sound_ID_DAMINGFU,
	// # dSoundID.Sound_ID_DAMINGFU,
	// # dSoundID.Sound_ID_BATTLE,
	// # dSoundID.Sound_ID_BATTLE,
	// # dSoundID.Sound_ID_BATTLE,
	// # dSoundID.Sound_ID_DAMINGFU,
	// # dSoundID.Sound_ID_JUYITING,
	// # dSoundID.Sound_ID_BATTLE,
	// # dSoundID.Sound_ID_DIDAO,
	// # dSoundID.Sound_ID_BATTLE,
	// # dSoundID.Sound_ID_BATTLE,
	// # dSoundID.Sound_ID_DAMINGFU,
	// # dSoundID.Sound_ID_BATTLE,
	// # dSoundID.Sound_ID_DAMINGFU,
	// # dSoundID.Sound_ID_DIDAO,
	// # };
	// #
	// #
	// #
	// #endif

}
