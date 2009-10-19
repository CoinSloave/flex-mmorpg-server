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

	// microedition.platform 平台名称，如j2me
	// microedition.configuration CLDC或CDC版本，如CLDC-1.0
	// microedition.profiles MIDP版本，如MIDP-1.0
	// microedition.encoding 默认的系统编码，如GBK
	// microedition.locale 默认的区域设置，如zh-CN
	//
	// MMAPI相关
	// microedition.media.version MMAPI的版本，如1.1
	// supports.mixing 是否支持混音，如true
	// supports.audio.capture 是否支持音频捕获，如true
	// supports.video.capture 是否支持视频捕获，如true
	// supports.recording 是否支持录音，如true
	// audio.encodings 音频编码格式，如encoding=pcm
	// encoding=pcm&rate=8000&bits=8&channels=1
	// video.snapshot.encodings 拍摄图片的编码格式，如encoding=jpeg encoding=png
	// streamable.contents 支持的流媒体格式，如audio/x-wav
	//
	// WMA相关
	// wireless.messaging.sms.smsc 返回SMS的服务中心，如+8613800010000
	// wireless.messaging.mms.mmsc 返回MMS的服务中心，如http://mmsc.monternet.com
	//
	// 其他
	// microedition.m3g.version 返回Mobile 3D的版本，如1.0
	// bluetooth.api.version 返回蓝牙API的版本，如1.0
	// microedition.io.file.FileConnection.version 返回FileConnection的版本，如1.0
	// microedition.pim.version 返回PIM的版本，如1.0

	// J2ME MIDP 1.0和2.0应用程序都可以通过System.getProperty(String
	// key)检测某一个属性的信息。如果该属性有效，
	// 将返回对应的字符串，否则，返回null，表示系统不支持此功能。

	// for test
	// String version = System.getProperty("microedition.media.version");

	public static CGame display;
	public static CMIDlet midlet;

	public CMIDlet() {
		midlet = this;
	}

	/**
	 * 应用程序一开始就会调用的startApp()
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
		// //退出游戏,保存配置
		// display.saveToRMS ( CGame.DB_NAME_CONFIG , 1 );

		midlet.notifyDestroyed();
		midlet = null;
		display = null;
	}
}
