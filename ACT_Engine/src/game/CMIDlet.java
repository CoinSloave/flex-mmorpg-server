package game;

/**
 * <p>Title: engine of RPG game</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: glu</p>
 * @author kai zeng
 * @version 1.0
 */
import game.config.dGame;

import com.mglib.sound.SoundPlayer;

public class CMIDlet extends javax.microedition.midlet.MIDlet {

	// microedition.platform ƽ̨���ƣ���j2me
	// microedition.configuration CLDC��CDC�汾����CLDC-1.0
	// microedition.profiles MIDP�汾����MIDP-1.0
	// microedition.encoding Ĭ�ϵ�ϵͳ���룬��GBK
	// microedition.locale Ĭ�ϵ��������ã���zh-CN
	//
	// MMAPI���
	// microedition.media.version MMAPI�İ汾����1.1
	// supports.mixing �Ƿ�֧�ֻ�������true
	// supports.audio.capture �Ƿ�֧����Ƶ������true
	// supports.video.capture �Ƿ�֧����Ƶ������true
	// supports.recording �Ƿ�֧��¼������true
	// audio.encodings ��Ƶ�����ʽ����encoding=pcm
	// encoding=pcm&rate=8000&bits=8&channels=1
	// video.snapshot.encodings ����ͼƬ�ı����ʽ����encoding=jpeg encoding=png
	// streamable.contents ֧�ֵ���ý���ʽ����audio/x-wav
	//
	// WMA���
	// wireless.messaging.sms.smsc ����SMS�ķ������ģ���+8613800010000
	// wireless.messaging.mms.mmsc ����MMS�ķ������ģ���http://mmsc.monternet.com
	//
	// ����
	// microedition.m3g.version ����Mobile 3D�İ汾����1.0
	// bluetooth.api.version ��������API�İ汾����1.0
	// microedition.io.file.FileConnection.version ����FileConnection�İ汾����1.0
	// microedition.pim.version ����PIM�İ汾����1.0

	// J2ME MIDP 1.0��2.0Ӧ�ó��򶼿���ͨ��System.getProperty(String
	// key)���ĳһ�����Ե���Ϣ�������������Ч��
	// �����ض�Ӧ���ַ��������򣬷���null����ʾϵͳ��֧�ִ˹��ܡ�

	// for test
	// String version = System.getProperty("microedition.media.version");

	public static CGame display;
	public static CMIDlet midlet;

	public CMIDlet() {
		midlet = this;
	}

	/**
	 * Ӧ�ó���һ��ʼ�ͻ���õ�startApp()
	 */
	public void startApp() {
		if (display == null) {
			display = new CGame();
		}
		javax.microedition.lcdui.Display.getDisplay(midlet).setCurrent(display);

		if (CGame.m_curState != dGame.GST_GAME_MENU) {
			SoundPlayer.playSingleSound(SoundPlayer.getCurMidiID(), -1);
		}
	}

	/**
     *
     */
	public void pauseApp() {
		if (SoundPlayer.m_isMusicOn) {
			// if (CGame.m_curState!=CGame.GST_MAIN_MENU
			// &&CGame.m_curState!=CGame.GST_GAME_LOAD
			// &&CGame.m_curState!=CGame.GST_GAME_EXIT
			// &&CGame.m_curState!=CGame.GST_GAME_HELP
			// &&CGame.m_curState!=CGame.GST_GAME_ABOUT) {
			SoundPlayer.stopSingleSound();
			// }

		}

		if (CGame.m_curState == dGame.GST_GAME_RUN) {
			CGame.setGameState(dGame.GST_GAME_MENU);
		}
	}

	/**
	 * 
	 * @param b
	 *            boolean
	 */
	public void destroyApp(boolean b) {
		// //�˳���Ϸ,��������
		// display.saveToRMS ( CGame.DB_NAME_CONFIG , 1 );

		midlet.notifyDestroyed();
		midlet = null;
		display = null;
	}
}
