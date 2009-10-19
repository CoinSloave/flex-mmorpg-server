package game;

import game.config.dConfig;
import game.config.dGame;
import game.config.dText;
import game.key.CKey;
import game.object.CElementor;
import game.object.CEnemy;
import game.object.CHero;
import game.object.CObject;
import game.object.IObject;
import game.object.dActor;
import game.object.dActorClass;
import game.pak.Camera;
import game.pak.GameEffect;
import game.res.ResLoader;
import game.rms.Record;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import com.mglib.mdl.ani.AniData;
import com.mglib.mdl.ani.AniPlayer;
import com.mglib.mdl.ani.Player;
import com.mglib.mdl.map.MapData;
import com.mglib.mdl.map.MapDraw;
import com.mglib.script.Script;
import com.mglib.sound.SoundLoader;
import com.mglib.sound.SoundPlayer;
import com.mglib.ui.Data;
import com.mglib.ui.IDrawer;
import com.mglib.ui.UIdata;
import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import com.nokia.mid.ui.FullCanvas;

/**
 * Engine类
 * <p>
 * Title: engine of Act game
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: glu
 * </p>
 * 
 * @author kai zeng
 * @version 1.0
 */
public class CGame extends
// #if N73 || N5500 || N7370 || N6230i || N7610|| N7260|| N7210|| N6101
		FullCanvas
// #else
		// # Canvas
		// #endif
		implements Runnable, dGame {
	/***************************************************************************
      *
      ***************************************************************************/
	public static CHero m_hero; // 主角类

	public static int m_curState;
	public static int m_preState;

	public static Graphics m_g;
	// #if nokiaAPI
	public static DirectGraphics m_dg;
	// #endif
	public static boolean bShowMap;

	public static boolean s_isCreatAllModuleImage = false;

	public static int m_gameCounter; // 游戏logic状态下的通用计数器
	/*******
	 * 绘制器
	 ******/
	public static IDrawer gdraw;
	/*********
	 * 资源加载器
	 *******/
	public static ResLoader gData;
	/*******
	 * 地图绘制
	 *******/
	public static MapDraw gMap;
	/**
      *
      */
	static {
		gdraw = Drawer.getInstance();
		gData = ResLoader.getInstance();
	}

	//
	// public static Image imgNumber;
	// public static Image imgNumber2;
	// //加载数字图片
	// static {
	// imgNumber = CTools.loadImage ("num", false);
	// imgNumber2 = CTools.loadImage ("num1", false);
	// }

	public static CGame cGame;

	public CGame() {
		// #if !(N73 || N5500 || N7370 || N6230i || N7610|| N7260|| N7210||
		// N6101)
		// # setFullScreenMode (true);
		// #endif
		gData.loadGlobalData();
		setGameState(GST_TEAM_LOGO);
		Script.loadQuestVar();
		gData.loadOptionDlgData();
		gData.loadCssData();

		UIdata d = new UIdata();
		Data.loadGameData();

		// #if touch_screen
		// # touchImg1=CTools.loadImage("touch1");
		// # touchImg2=CTools.loadImage("touch2");
		// #endif
		SoundPlayer.m_isMusicOn = false;
		SoundPlayer.initSound(SoundPlayer.s_filenameSound, (byte) 4);
		cGame = this;
		new Thread(this).start();
	}

	public static void setGameState(int newState) {
		m_preState = m_curState;
		m_gameCounter = 0;
		// 离开当前状态的处理
		switch (m_preState) {
		case GST_MINI_GAME:
			exit_MINI_GAME();
			break;
		// case GST_GAME_MORE_GAME://--freeket---推荐游戏
		// MoreGame.exitMoreGame();
		// break;
		// FXQ
		case GST_GAME_UI:
			exit_GAME_UI();
			break;

		case GST_GAME_MENU:
			exit_GAME_MENU();
			break;
		case GST_GAME_ABOUT:
			exit_Game_About();
			break;
		case GST_TEAM_LOGO: // logo
			exit_TEAM_LOGO();
			break;

		case GST_MAIN_MENU:
			exit_MainMenu();
			break;

		case GST_GAME_LOAD: // 游戏加载
			exit_Game_Load();
			break;

		case GST_GAME_RUN: // 游戏运行
			exit_Game_Run();
			break;

		case GST_GAME_EXIT:
			exit_Game_Exit();
			break;

		case GST_SCRIPT_OPDLG: // 选择性对话
			exit_OpDlg();
			break;

		case GST_CAMARE_MOVE: // 镜头移动
			exit_Camera_Move();
			break;

		case GST_SCRIPT_DIALOG: // 脚本对话状态
			exit_ScriptDlg();
			break;

		case GST_SCRIPT_RUN: // 脚本状态
			exit_Script();
			break;

		}

		m_curState = newState;
		// 进入新状态的处理
		switch (newState) {
		case GST_MINI_GAME:
			init_MINI_GAME();
			break;
		// case GST_GAME_MORE_GAME:
		// MoreGame.initMoreGame();
		// break;
		case GST_GAME_OPTION:
			init_Game_Option();
			break;
		// FXQ
		case GST_GAME_UI:
			init_GAME_UI();
			break;
		case GST_GAME_MENU:
			init_GAME_MENU();
			break;

		case GST_GAME_ABOUT:
			init_Game_About();
			break;
		case GST_GAME_HELP:
			init_Game_Help();
			break;

		case GST_TEAM_LOGO: // logo
			init_TEAM_LOGO();
			break;

		case GST_MAIN_MENU:
			init_MainMenu();
			break;

		case GST_GAME_LOAD: // 游戏加载
			init_Game_Load();
			break;

		case GST_GAME_RUN: // 游戏运行
			init_Game_Run();
			break;

		case GST_GAME_EXIT: // 游戏退出状态
			init_Game_Exit();
			break;

		case GST_SCRIPT_OPDLG: // 选择性对话

			init_OpDlg();
			break;

		case GST_CAMARE_MOVE: // 镜头移动
			init_Camera_Move();
			break;

		case GST_SCRIPT_DIALOG: // 脚本对话状态
			init_ScriptDlg();
			break;

		case GST_SCRIPT_RUN: // 脚本状态
			init_Script();
			break;

		}
		MapDraw.bShowMap = true;
	}

	private static void init_MINI_GAME() {

	}

	private static void do_MINI_GAME() {

	}

	private static void draw_MINI_GAME(Graphics g) {

	}

	private static void exit_MINI_GAME() {

	}

	/***************************************************************************
	 *主逻辑状态的处理
	 ***************************************************************************/
	// private static MSimpleAnimationPlayer animationPlayer;
	// private static MSpriteData animationData;
	private static AniPlayer aniPlayer;
	private static AniData aniData;
	private static Image imageLogo;
	private static final byte LOGO_TIMER = 20;

	// logo状态
	private static void init_TEAM_LOGO() {
		imageLogo = CTools.loadImage("logo");
	}

	private static void exit_TEAM_LOGO() {
		imageLogo = null;
	}

	private static void do_TEAM_LOGO() {
		m_gameCounter++;
		if (m_gameCounter >= LOGO_TIMER) {
			// #if mode_noSound
			// # SoundPlayer.m_isMusicOn=false;
			// # setGameState(GST_MAIN_MENU);
			// # if (!Record.haveRecord()) {
			// # m_curIndex = 0;
			// # }
			// #else
			setGameState(GST_GAME_IF_MUSIC);
			// #endif
		}
	}

	private static void draw_TEAM_LOGO(Graphics g) {
		cls(g, dConfig.COLOR_WHITE);
		if (imageLogo != null) {
			g.drawImage(imageLogo, dConfig.S_WIDTH / 2, dConfig.S_HEIGHT / 2,
					dConfig.ANCHOR_HV);
		}
	}

	// 封面CG
	public static AniPlayer CGPlayer;

	/**
	 * 游戏主菜单状态
	 */
	private static void init_MainMenu() {
		imageLogo = CTools.loadImage("mainMenu");
		m_curIndex = 1;
		SoundPlayer.playSingleSound(SoundLoader.getLevelBGM(-1), -1);
	}

	private static void exit_MainMenu() {
		imageLogo = null;
		// 封面CG没删除
	}

	private static void do_MainMenu() {
		navigateMenu(m_stringMainMenu.length);
		confirmMenu(m_curIndex);
	}

	// 主菜单文字的Y坐标
	// #if screen_240_320
	static int mainMenuStringY = 284;

	// #elif screen_176_208||screen_208_208||screen_176_204
	// # static int mainMenuStringY=163;
	// #elif screen_176_220
	// # static int mainMenuStringY=175;
	// #else
	// # static int mainMenuStringY=153;
	// #endif
	private static void draw_MainMenu(Graphics g) {
		cls(g, dConfig.COLOR_BLACK);
		if (imageLogo != null) {
			// #if N6230i
			// # g.setColor(0x8fa17b);
			// # g.fillRect(0,0,dConfig.S_WIDTH,dConfig.S_HEIGHT);
			// # g.drawImage (imageLogo, dConfig.S_WIDTH_HALF, 2,
			// dConfig.ANCHOR_HT);
			// #else
			g.drawImage(imageLogo, 0, 0, dConfig.ANCHOR_LT);
			// #endif

		}

		if (CGPlayer != null && CGPlayer.actionID == 0) {
			return;
		}
		Font f = g.getFont();
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
				Font.SIZE_MEDIUM));
		// #if touch_screen
		// # CTools.afficheSmall (g, m_stringMainMenu[m_curIndex],
		// # dConfig.S_WIDTH / 2,
		// # mainMenuStringY, dConfig.ANCHOR_HT,
		// # dConfig.COLOR_SCENE_NAME,dConfig.COLOR_SCENE_NAME_OUT);
		// # CTools.afficheSmall (g, " <                         > ",
		// # dConfig.S_WIDTH / 2,
		// # mainMenuStringY, dConfig.ANCHOR_HT,
		// # dConfig.COLOR_SCENE_NAME,dConfig.COLOR_SCENE_NAME_OUT);
		// #
		// #else
		CTools.afficheSmall(g, m_stringMainMenu[m_curIndex],
				dConfig.S_WIDTH / 2, mainMenuStringY, dConfig.ANCHOR_HT,
				dConfig.COLOR_SCENE_NAME, dConfig.COLOR_SCENE_NAME_OUT);
		// #endif
		g.setFont(f);
		if (noRecord) {
			CTools.promptString(g, STR_NO_RECORD, null, null, GameUI.TXTCOLOR,
					dConfig.COLOR_BLACK);
		}
		if (sureNewGame) {
			CTools.promptString(g, STR_SURE_NEW_GAME, dText.STR_OK,
					dText.STR_CANCEL, GameUI.TXTCOLOR, dConfig.COLOR_BLACK);
		}

	}

	// 无声音，无设置的菜单
	// #if mode_noSound
	// # static String[] m_stringMainMenu = {"新游戏", "继续游戏", "帮助", "关于", "退出"};
	// # final static byte MainMenu_NEWGAME = 0;
	// # final static byte MainMenu_CONTIUNE = 1;
	// # final static byte MainMenu_HELP = 2;
	// # final static byte MainMenu_ABOUT = 3;
	// # final static byte MainMenu_OPITION = -1;
	// # final static byte MainMenu_EXIT = 4;
	// # final static byte MainMenu_MORE_GAME = 5; //qq游戏中心、更多精彩。。。。
	// # final static String[] m_stingGameMenu = {"继续游戏", "帮助", "返回主菜单"};
	// # final static byte GameMenu_CONTIUNE = 0;
	// # final static byte GameMenu_HELP = 1;
	// # final static byte GameMenu_OPTION = -1;
	// # final static byte GameMenu_TO_MAIN_MENU = 2;
	// #else
	static String[] m_stringMainMenu = { "新游戏", "继续游戏", "帮助", "关于", "设置", "退出" };
	final static byte MainMenu_NEWGAME = 0;
	final static byte MainMenu_CONTIUNE = 1;
	final static byte MainMenu_HELP = 2;
	final static byte MainMenu_ABOUT = 3;
	final static byte MainMenu_OPITION = 4;
	final static byte MainMenu_EXIT = 5;
	final static byte MainMenu_MORE_GAME = 6; // qq游戏中心、更多精彩。。。。
	final static String[] m_stingGameMenu = { "继续游戏", "帮助", "设置", "返回主菜单" };
	final static byte GameMenu_CONTIUNE = 0;
	final static byte GameMenu_HELP = 1;
	final static byte GameMenu_OPTION = 2;
	final static byte GameMenu_TO_MAIN_MENU = 3;
	// #endif

	public static int m_curIndex; // 当前索引

	public static boolean navigateMenu(int length) {
		if (CKey.isKeyPressed(CKey.GK_LEFT | CKey.GK_UP)) {
			// if(m_curState == GST_GAME_MENU) {
			// if( /*m_nOpenedLevelsCount <= 1 &&*/m_curIndex == 2) {
			// m_curIndex = m_curIndex - 1;
			// }
			// }
			m_curIndex = m_curIndex == 0 ? length - 1 : --m_curIndex;
			return true;
		} else if (CKey.isKeyPressed(CKey.GK_RIGHT | CKey.GK_DOWN)) {
			// if(m_curState == GST_GAME_MENU) {
			// if( /*m_nOpenedLevelsCount <= 1 &&*/m_curIndex == 0) {
			// m_curIndex = m_curIndex + 1;
			// }
			// }
			m_curIndex = m_curIndex == length - 1 ? 0 : ++m_curIndex;
		}
		return false;
	}

	public static boolean noRecord;
	public static String STR_NO_RECORD = "没 有 存 档。";
	public static boolean sureNewGame;
	public static String STR_SURE_NEW_GAME = "新游戏将删除存档!";

	public static void confirmMenu(int menuItem) {
		// 继续游戏时，如果没有存档，给出提示
		if (noRecord) {
			if (CKey.isAnyKeyPressed()) {
				noRecord = false;
			}
			return;
		}

		// 是否覆盖存档
		if (sureNewGame) {
			if (CKey.isKeyPressed(CKey.GK_OK)) {
				initGameSystemInf();
				loadType = LOAD_TYPE_NEW_GAME;
				setLoadInfo(0);
				sureNewGame = false;
			} else if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
				sureNewGame = false;
			}
			return;
		}

		if (CKey.isKeyPressed(CKey.GK_OK)) {
			switch (menuItem) {
			case MainMenu_NEWGAME:
				if (Record.haveRecord()) {
					sureNewGame = true;
				} else {
					initGameSystemInf();
					loadType = LOAD_TYPE_NEW_GAME;
					setLoadInfo(0);
				}
				return;
			case MainMenu_CONTIUNE:
				if (Record.readFromRMS(Record.DB_NAME_GAME, 1)) {
					loadType = LOAD_TYPE_CONTINUE_GAME;
					setLoadInfo(curLevelID);
				} else {
					noRecord = true;
					return;
				}
				break;
			case MainMenu_HELP:
				setGameState(GST_GAME_HELP);
				break;
			case MainMenu_ABOUT:
				setGameState(GST_GAME_ABOUT);
				break;
			case MainMenu_OPITION:
				setGameState(GST_GAME_OPTION);
				break;
			case MainMenu_EXIT:
				// #if QQ || others
				// # if ( MoreGame.isHaveGamelist ){
				// # MoreGame.m_ctrlIntoMoreGame = false;
				// # CGame.setGameState (CGame.GST_GAME_MORE_GAME );
				// # }
				// # else{
				// # setGameState(GST_GAME_EXIT);
				// # }
				// #else
				setGameState(GST_GAME_EXIT);
				// #endif
				break;
			// qq游戏中心、更多精彩。。。。
			case MainMenu_MORE_GAME:
				visitMigCom();
				break;
			}
		}
	}

	private static void visitMigCom() {
		// new Thread(){
		// public void run(){
		// try {
		// if (CMIDlet.midlet.platformRequest(MessageCharge.MIG_URL)){
		// CMIDlet.midlet.notifyDestroyed();
		// }
		// } catch (Exception ex) {
		// }
		// }
		// }.start();
	}

	// 游戏退出状态
	private static void init_Game_Exit() {

	}

	private static void exit_Game_Exit() {

	}

	private static void do_Game_Exit() {
		if (CKey.isKeyPressed(CKey.GK_OK)) {
			// 从暂停菜单退出游戏，先释放场景资源
			if (m_preState == GST_GAME_MENU) {
				destroyLevel(true);
			}
			// destroyGame ();
			isRunning = false;
		} else if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			setGameState(m_preState);
		}
	}

	public final static int LINE_H = 20;

	private static void draw_Game_Exit(Graphics g) {
		cls(g, 0);
		CTools.afficheSmall(g, dConfig.STR_EXIT, dConfig.S_WIDTH / 2,
				dConfig.S_HEIGHT / 2, dConfig.ANCHOR_HB, GameUI.TXTCOLOR,
				GameUI.BGCOLOR);
		drawLineBox(g, LINE_H);
		drawOption(g, dConfig.STR_OK, dConfig.STR_BACK, GameUI.TXTCOLOR,
				dConfig.COLOR_BLACK);
	}

	/**
	 * 屏幕底部的 确认、取消
	 * 
	 * @param g
	 *            Graphics
	 * @param okButtton
	 *            String
	 * @param noButton
	 *            String
	 */
	public static void drawOption(Graphics g, String okButtton,
			String noButton, int color, int outColor) {
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		if (okButtton != null) {
			// #if keymode_moto1||keymode_moto2||keymode_e680
			// # CTools.afficheSmall (g, okButtton,dConfig.S_WIDTH,
			// dConfig.S_HEIGHT - dConfig.SF_HEIGHT,
			// # dConfig.ANCHOR_RT, color,outColor);
			// #else
			CTools.afficheSmall(g, okButtton, 0, dConfig.S_HEIGHT
					- dConfig.SF_HEIGHT, dConfig.ANCHOR_LT, color, outColor);
			// #endif
		}

		if (noButton != null) {
			// #if keymode_moto1||keymode_moto2||keymode_e680
			// # CTools.afficheSmall (g, noButton, 0, dConfig.S_HEIGHT -
			// dConfig.SF_HEIGHT,
			// # dConfig.ANCHOR_LT, color,outColor);
			// #else
			CTools.afficheSmall(g, noButton, dConfig.S_WIDTH, dConfig.S_HEIGHT
					- dConfig.SF_HEIGHT, dConfig.ANCHOR_RT, color, outColor);
			// #endif
		}
	}

	public static void fillBackGround(Graphics g, int color, int startY,
			int height) {
		g.setClip(0, startY, dConfig.S_WIDTH, height);
		g.setColor(color);
		g.fillRect(0, startY, dConfig.S_WIDTH, height);
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
	}

	public static void drawLineBox(Graphics g, int height) {
		fillBackGround(g, 0, 0, height);

		g.setColor(0xADC1C7);
		g.drawLine(0, height + 2, dConfig.S_WIDTH, height + 2);
		g.setColor(0xC4CCCE);
		g.drawLine(0, height + 1, dConfig.S_WIDTH, height + 1);
		g.setColor(0xD9E2E5);
		g.drawLine(0, height, dConfig.S_WIDTH, height);

		fillBackGround(g, 0, dConfig.S_HEIGHT - height, height);

		g.setColor(0xADC1C7);
		g.drawLine(0, dConfig.S_HEIGHT - height, dConfig.S_WIDTH,
				dConfig.S_HEIGHT - height);
		g.setColor(0xC4CCCE);
		g.drawLine(0, dConfig.S_HEIGHT - height - 1, dConfig.S_WIDTH,
				dConfig.S_HEIGHT - height - 1);
		g.setColor(0xD9E2E5);
		g.drawLine(0, dConfig.S_HEIGHT - height - 2, dConfig.S_WIDTH,
				dConfig.S_HEIGHT - height - 2);
	}

	// 游戏加载状态

	private static final int LOAD_OK = 100; // 载入资源结束
	private static int loadingProgress; // 载入进度
	private static int loadingPaintTimer;

	private static void createLoadThread() {
		// 加载线程启动 多线程载入
		new Thread() {
			public void run() {
				try {
					while (loadingProgress < LOAD_OK) {
						loadLevel();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.start();
	}

	/****************************************************
	 * 镜头移动处理
	 ***************************************************/

	// 镜头移动速度
	public static int VS_SP = 5 << dConfig.FRACTION_BITS;

	public static int vsVX, vsVY; // 移动速度
	public static int vsDstX, vsDstY; // 视点转换的　最终坐标值
	public static int vsCurVX, vsCurVY; // 当前视点的作标
	public static int vsMoveTime;

	private static void init_Camera_Move() {
		vsCurVX = (Camera.cameraLeft + dConfig.CAMERA_WIDTH / 2) << dConfig.FRACTION_BITS; // 视窗移动的起始坐标
		vsCurVY = (Camera.cameraTop + dConfig.CAMERA_HEIGHT / 2) << dConfig.FRACTION_BITS;

		int a = CTools.arcTan(vsDstX - vsCurVX, vsDstY - vsCurVY);
		vsVX = CTools.lenCos(VS_SP, a);
		vsVY = CTools.lenSin(VS_SP, a);

		if (VS_SP <= 0) {
			VS_SP = 5 << dConfig.FRACTION_BITS;
		}
		vsMoveTime = CTools.getDistance(Math.abs(vsCurVX - vsDstX) >> 8, Math
				.abs(vsCurVY - vsDstY) >> 8)
				/ (VS_SP >> 8);
	}

	private static void exit_Camera_Move() {

	}

	private void do_CameraMove() {
		/*********************************************************************************/
		if (vsMoveTime == 0) {
			setGameState(GST_SCRIPT_RUN);
			return;
		}
		vsMoveTime--;

		vsCurVX += vsVX;
		vsCurVY += vsVY;

		Camera.setCameraCenter(vsCurVX >> dConfig.FRACTION_BITS,
				vsCurVY >> dConfig.FRACTION_BITS);
		updateEngineLogic();
	}

	private void draw_CameraMove(Graphics g) {
		drawLevel(g);
	}

	// 切换场景的类型
	public static byte loadType;
	// 切换场景的类型定义
	public static final byte LOAD_TYPE_NEW_GAME = 0; // 新游戏
	public static final byte LOAD_TYPE_CONTINUE_GAME = 1; // 继续游戏
	public static final byte LOAD_TYPE_COMMON_LOAD = 2;
	public static final byte LOAD_TYPE_COMMON_LOAD2 = 3;

	private static int heroActorID = 0;

	public static void initLevel() {
		initActorShellList();
		// 初始化激活list
		resetShellList();

		for (int actor_id = 0; actor_id < ResLoader.nActorsCount; ++actor_id) {
			actorsShellID[actor_id] = -1;
			getActorActivateBoxInfo(actor_id, CObject.s_colBox1);
			actorsRegionFlags[actor_id] = getRegionFlags(CObject.s_colBox1);
		}
		CObject.m_otherCamera = 0;
		// add by lin 09.04.30
		Camera.isLockInArea = false;
		GameEffect.resetRenderInfo();
		activateActor(heroActorID, true);
		m_hero.initialize();

		Script.scriptLock = false;
		// setShutter (SHUTTER_OPEN);
		m_hero.updateCamera();
		Camera.updateCamera(false);
		Script.autoshowActionCouner = 0;
		Script.autoMoveActorCounter = 0;
		bRedrawmap = true;

		GameEffect.m_coverState = 0;
		GameEffect.m_currentCoverTick = 0;

		CKey.initKey();
		gMap.updateMap(Camera.cameraLeft, Camera.cameraTop, bRedrawmap);
		// #if !W958C
		System.gc();
		// #endif

		GameUI.curHero = m_hero;

		// 关卡计费
		// Script.systemVariates[Script.SV_INDEX_SCRIPT_HAVE_BUY_LEVEL]
		// =(short)(MessageCharge.isHavaBuy[MessageCharge.TYPE_GAME]?1:0);
		// add by lin 09.4.29 激活全局对象
		updateActorLogic();
	}

	public static void doAfterLoadLevel() {
		initShowSceneName();
		switch (loadType) {
		case LOAD_TYPE_NEW_GAME:
			initLevel();
			m_hero.initActorProprety();
			// modify by lin 可能已触发脚本
			if (m_curState != GST_SCRIPT_DIALOG && m_curState != GST_SCRIPT_RUN
					&& m_curState != GST_SCRIPT_OPDLG) {
				setGameState(GST_GAME_RUN);
			}
			Record.saveToRMS(Record.DB_NAME_GAME, 1);
			break;
		case LOAD_TYPE_CONTINUE_GAME:
			initLevel();
			m_hero.getSavedInf(Record.savedHeroTemp);
			m_hero.switchHero(Record.savedHeroId, false);
			// 位置
			m_hero.m_x = Record.savedHeroTemp.m_x;
			m_hero.m_y = Record.savedHeroTemp.m_y;
			m_hero.updateCamera();
			Camera.updateCamera(false);
			modifySceneBySaveInfo(curLevelID);
			// modify by lin 可能已触发脚本
			if (m_curState != GST_SCRIPT_DIALOG && m_curState != GST_SCRIPT_RUN
					&& m_curState != GST_SCRIPT_OPDLG) {
				setGameState(GST_GAME_RUN);
			}
			break;
		case LOAD_TYPE_COMMON_LOAD:
			initLevel();
			m_hero.switchHero(
					m_hero.m_actorProperty[IObject.PRO_INDEX_ROLE_ID], false);
			m_hero.setXY(nextHeroX, nextHeroY);
			m_hero.setFaceDir(m_hero.getSetDir(nextHeroDir));
			m_hero.setState(IObject.ST_ACTOR_WAIT, -1, true);
			updateActorLogic();
			modifySceneBySaveInfo(curLevelID);
			m_hero.updateCamera();
			Camera.updateCamera(false);
			if (isScript) {
				setGameState(GST_SCRIPT_RUN);
			} else {
				setGameState(GST_GAME_RUN);
			}
			break;
		// case LOAD_TYPE_COMMON_LOAD2:
		// initLevel ();
		// m_hero.switchHero(m_hero.m_actorProperty[CHero.PRO_INDEX_ROLE_ID],false);
		// m_hero.updateCamera ();
		// Camera.updateCamera (false);
		// setGameState (GST_GAME_RUN);
		// break;
		}
		// 0.局部关卡信息初始化
		// initPoint ();

		// 清除局部变量
		for (int i = 0; i < Script.systemVariates.length; i++) {
			if (Script.svType[i] == Script.LOCAL_VARIATE) {
				Script.systemVariates[i] = 0;
			}
		}
		// 2.系统数据初始化
		// GameEffect.systemFlag = 0;
		// GameEffect.shakeSwing = 0; //振动幅度
		// shakeTime = 0;
		// //初始化备份数组
		// backupLevel ();
		// 按键初始化
		CKey.initKey();
		Camera.updateCamera(false);
		SoundPlayer.playSingleSound(SoundLoader.getLevelBGM(curLevelID), -1);
	}

	public static int px, py, pw, ph;

	private static void init_Game_Load() {
		SoundPlayer.stopSingleSound();
		loadInterfaceImage();
		loadingProgress = 0;
		loadingPaintTimer = 0;
		// #if size_500k&&!N7370&&!E6&&!W958C
		imageLoading = CTools.loadImage("loading");
		pw = imageLoading.getWidth();
		ph = imageLoading.getHeight();
		px = (dConfig.S_WIDTH - pw) / 2;
		py = (dConfig.S_HEIGHT - ph) / 2;
		// #endif
		// 启动加载线程
		createLoadThread();
	}

	private static void exit_Game_Load() {
		GameEffect.clearAllWindow();
		imageLoading = null;
	}

	private static void do_Game_Load() {
		loadingPaintTimer += dConfig.LOAD_FRAME;
		loadingPaintTimer = Math.min(100, loadingPaintTimer);
		if (loadingPaintTimer >= LOAD_OK && loadingProgress >= LOAD_OK) {
			loadingPaintTimer = 0;
			doAfterLoadLevel();
		}
	}

	static Image imageLoading;

	/**
	 * 进度条的统一绘制
	 */
	private static void drawProcessBar(Graphics g) {
		cls(g, dConfig.COLOR_BLACK);
		// 进度条
		int curph = loadingPaintTimer * ph / 100;
		// #if size_500k&&!N7370&&!E6&&!W958C
		if (imageLoading == null) {
			imageLoading = CTools.loadImage("loading");
		} else {
			g.drawImage(imageLoading, px, py, 0);
		}
		CTools.afficheSmall(g, "载入中 " + loadingPaintTimer + "%",
				dConfig.S_WIDTH / 2, dConfig.S_HEIGHT - 30, dConfig.ANCHOR_HB,
				dConfig.COLOR_SCENE_NAME, dConfig.COLOR_SCENE_NAME_OUT);
		// #else
		// #
		// CTools.afficheSmall(g,"载入中 "+loadingPaintTimer+"%",dConfig.S_WIDTH/2,
		// # dConfig.S_HEIGHT-30,
		// #
		// dConfig.ANCHOR_HB,dConfig.COLOR_SCENE_NAME,dConfig.COLOR_SCENE_NAME_OUT);
		// #endif
	}

	private static void draw_Game_Load(Graphics g) {
		drawProcessBar(g);
	}

	/*****************************************************
	 * 帮助
	 ******************************************************/
	static String[] m_showString;

	static void init_Game_Help() {
		if (m_showString == null) {
			m_showString = CTools.breakLongMsg(dConfig.m_helpString,
					dConfig.F_SMALL_DEFAULT, dConfig.HELP_T_LENGTH);
		}
		index = 0;
	}

	static void exit_Game_Help() {
		m_showString = null;
		index = 0;
	}

	static void do_Game_Help() {
		if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			setGameState(m_preState);
		}

		if (CKey.isKeyPressed(CKey.GK_RIGHT)) {
			if (index < pages - 1) {
				index++;
			}
		} else if (CKey.isKeyPressed(CKey.GK_LEFT)) {
			if (index > 0) {
				index--;
			}
		}

	}

	private static int pages; // 页数
	// #if screen_240_320
	final static int ROW_NUMBER = 10; // 每页显示的行数
	// #else
	// # final static int ROW_NUMBER = 7; //每页显示的行数
	// #endif
	static int index = 0;

	static void draw_Game_Help(Graphics g) {
		cls(g, 0);
		// drawLineBox(g , LINE_H);
		drawHelp(g);
		drawOption(g, "", dConfig.m_confirmString[dConfig.INDeX_BACK],
				dConfig.COLOR_SCENE_NAME, dConfig.COLOR_BLACK);
	}

	// #if screen_240_320
	public static final int HELP_TOP_Y = 60;

	// #else
	// # public static final int HELP_TOP_Y=30;
	// #endif

	private static void drawHelp(Graphics g) {
		pages = (m_showString.length - 1) / ROW_NUMBER + 1; // 总页数 0~pages-1
		if (index >= pages || index < 0) {
			index = (index + pages) % pages; // 调整menuCursor到合理范围
		}
		int start = index * ROW_NUMBER; // 本页开始指针
		for (int i = 0; i < ROW_NUMBER && start + i < m_showString.length; i++) {
			CTools.afficheSmall(g, m_showString[start + i],
					dConfig.S_WIDTH / 2, HELP_TOP_Y + (dConfig.SF_HEIGHT + 3)
							* i, dConfig.ANCHOR_HB, dConfig.COLOR_SCENE_NAME,
					dConfig.COLOR_TEXT_OUTER);
		}

		if (index != 0) {
			CTools.afficheSmall(g, "<<", (dConfig.S_WIDTH >>> 1) - 15,
					dConfig.S_HEIGHT - 25, Graphics.HCENTER | Graphics.TOP,
					dConfig.COLOR_SCENE_NAME, -1);
		}
		if (index != pages - 1) {
			CTools.afficheSmall(g, ">>", (dConfig.S_WIDTH >>> 1) + 15,
					dConfig.S_HEIGHT - 25, Graphics.HCENTER | Graphics.TOP,
					dConfig.COLOR_SCENE_NAME, -1);
		}
	}

	/*****************************************************
	 * 关于
	 ******************************************************/
	static void init_Game_About() {
		// #if box
		m_showString = CTools.breakLongMsg(dConfig.m_aboutString,
				dConfig.F_SMALL_DEFAULT, dConfig.ABOUT_LENGTH);
		// #else
		// # if
		// (MessageCharge.str_About!=null&&MessageCharge.str_About.length>0) {
		// # m_showString = CTools.breakLongMsg(MessageCharge.str_About[0] ,
		// dConfig.F_SMALL_DEFAULT,
		// # dConfig.ABOUT_LENGTH);
		// # }
		// # else{
		// # m_showString= CTools.breakLongMsg(dConfig.m_aboutString ,
		// dConfig.F_SMALL_DEFAULT,
		// # dConfig.ABOUT_LENGTH);
		// # }
		// #endif
	}

	static void exit_Game_About() {
		m_showString = null;
	}

	static void do_Game_About() {
		m_gameCounter += 2;
		if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			setGameState(m_preState);
		}
	}

	static void draw_Game_About(Graphics g) {
		cls(g, 0);
		if (m_showString != null) {
			CTools.drawStringArray(m_showString, dConfig.S_WIDTH / 2,
					dConfig.S_HEIGHT - m_gameCounter * 2,
					dConfig.COLOR_SCENE_NAME, -1, dConfig.ANCHOR_HB);
		}

		if (m_gameCounter * 2 - dConfig.S_HEIGHT >= m_showString.length
				* LINE_H) {
			m_gameCounter = 0;
		}
		drawOption(g, "", dConfig.m_confirmString[dConfig.INDeX_BACK],
				dConfig.COLOR_SCENE_NAME, dConfig.COLOR_BLACK);

	}

	static void init_GAME_MENU() {
		SoundPlayer.stopSingleSound();
	}

	static void exit_GAME_MENU() {

	}

	static void do_GAME_MENU() {
		navigateMenu(m_stingGameMenu.length);
		if (CKey.isKeyPressed(CKey.GK_OK)) {
			switch (m_curIndex) {
			// 返回
			case GameMenu_CONTIUNE:
				setGameState(GST_GAME_RUN);
				SoundPlayer.playSingleSound(SoundPlayer.getCurMidiID(), -1);
				break;
			// 帮助
			case GameMenu_HELP:
				setGameState(GST_GAME_HELP);
				break;
			// 设置
			case GameMenu_OPTION:
				setGameState(GST_GAME_OPTION);
				break;
			// 返回主菜单
			case GameMenu_TO_MAIN_MENU:
				destroyLevel(true);
				setGameState(GST_MAIN_MENU);
				break;
			}
		} else if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			setGameState(GST_GAME_RUN);
			SoundPlayer.playSingleSound(SoundPlayer.getCurMidiID(), -1);
		}

	}

	static void draw_GAME_MENU(Graphics g) {
		paintLevel(g);
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		// #if nokiaAPI
		CTools.fillPolygon(g, 0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT,
				0x60404040);
		// #else
		// # cls(g,0);
		// #endif
		for (int i = 0; i < m_stingGameMenu.length; i++) {
			CTools.afficheSmall(g, m_stingGameMenu[i], dConfig.S_WIDTH_HALF,
					dConfig.S_HEIGHT / 3 + (dConfig.SF_HEIGHT + 5) * i,
					dConfig.ANCHOR_HT, dConfig.COLOR_SCENE_NAME,
					dConfig.COLOR_SCENE_NAME_OUT);
		}

		CTools.afficheSmall(g, m_stingGameMenu[m_curIndex],
				dConfig.S_WIDTH_HALF, dConfig.S_HEIGHT / 3
						+ (dConfig.SF_HEIGHT + 5) * m_curIndex,
				dConfig.ANCHOR_HT, dConfig.COLOR_WHITE,
				dConfig.COLOR_DIALOG_OUT);

	}

	/**
	 * 主角死亡
	 */
	private static void do_Game_Hero_Die() {
		if (CKey.isAnyKeyPressed()) {
			setGameState(GST_MAIN_MENU);
		}
	}

	private static void draw_Game_Hero_Die(Graphics g) {
		cls(g, 0);
		CTools.afficheSmall(g, dText.STR_HERO_DIE, dConfig.S_WIDTH_HALF,
				dConfig.S_HEIGHT_HALF, dConfig.ANCHOR_HB,
				dConfig.COLOR_SCENE_NAME, dConfig.COLOR_SCENE_NAME_OUT);
		CTools.afficheSmall(g, "按任意键返回菜单", dConfig.S_WIDTH_HALF,
				dConfig.S_HEIGHT_HALF + 30, dConfig.ANCHOR_HB,
				dConfig.COLOR_SCENE_NAME, dConfig.COLOR_SCENE_NAME_OUT);
	}

	private static void init_Game_Option() {
		if (SoundPlayer.m_isMusicOn) {
			m_curIndex = 0;
		} else {
			m_curIndex = 1;
		}
	}

	private static void exit_Game_Option() {
	}

	private static void do_Game_Option() {
		navigateMenu(dText.STR_OPTION.length);
		if (CKey.isKeyPressed(CKey.GK_OK)) {
			if (m_curIndex == 0) {
				SoundPlayer.m_isMusicOn = true;
			} else {
				SoundPlayer.m_isMusicOn = false;
				SoundPlayer.stopSingleSound();
			}

			setGameState(m_preState);
		} else if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			setGameState(m_preState);
		}
	}

	private static void draw_Game_Option(Graphics g) {
		cls(g, dConfig.COLOR_BLACK);
		for (int i = 0; i < dText.STR_OPTION.length; i++) {
			CTools.afficheSmall(g, dText.STR_OPTION[i], dConfig.S_WIDTH_HALF,
					dConfig.S_HEIGHT / 3 + (dConfig.SF_HEIGHT + 5) * i,
					dConfig.ANCHOR_HT, dConfig.COLOR_SCENE_NAME, -1);
		}

		CTools.afficheSmall(g, dText.STR_OPTION[m_curIndex],
				dConfig.S_WIDTH_HALF, dConfig.S_HEIGHT / 3
						+ (dConfig.SF_HEIGHT + 5) * m_curIndex,
				dConfig.ANCHOR_HT, dConfig.COLOR_WHITE,
				dConfig.COLOR_DIALOG_OUT);
		drawOption(g, dText.STR_OK, dText.STR_CANCEL, dConfig.COLOR_SCENE_NAME,
				GameUI.BGCOLOR);
	}

	/**
	 * 是否需要音乐
	 */
	private static void do_Game_IfMusic() {
		if (CKey.isKeyPressed(CKey.GK_OK)) {
			needSound = null;
			SoundPlayer.m_isMusicOn = true;
			setGameState(GST_MAIN_MENU);
			if (!Record.haveRecord()) {
				m_curIndex = 0;
			}
		} else if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			needSound = null;
			SoundPlayer.m_isMusicOn = false;
			setGameState(GST_MAIN_MENU);
			if (!Record.haveRecord()) {
				m_curIndex = 0;
			}
		}

	}

	static Image needSound;

	private static void draw_Game_IfMusic(Graphics g) {
		cls(g, dConfig.COLOR_BLACK);
		if (needSound == null) {
			needSound = CTools.loadImage("needSound");
		}
		g.drawImage(needSound, 0, 0, 0);
		// CTools.afficheSmall(g,dText.STR_NEED_MUSIC,dConfig.S_WIDTH_HALF,dConfig.S_HEIGHT_HALF,
		// dConfig.ANCHOR_HB,GameUI.TXTCOLOR,dConfig.COLOR_BLACK);
		// drawOption(g,dText.STR_YES_NO[dText.INDEX_YES],dText.STR_YES_NO[dText.INDEX_NO]
		// ,GameUI.TXTCOLOR,dConfig.COLOR_BLACK);
	}

	// 游戏运行状态
	private static void init_Game_Run() {
		AniData.setMlgs(ResLoader.aniMlgs);
		// if(m_preState == GST_SCRIPT_RUN) {
		// Script.scriptLock = true;
		// }

	}

	private static void exit_Game_Run() {

	}

	private static void updatelevel() {
		boolean isTrailer = currentTrailerIndex >= 0;
		updateEngineLogic();

		// 主角死亡 add by lin 09.04.24
		if (CHero.s_heroDie) {
			CGame.destroyLevel(true);
			CGame.setGameState(dGame.GST_GAME_HERO_DIE);
			CKey.initKey();
		}

		// -------------在trailer执行的过程是否要跳过trailer的处理----------------------
		pressSkipCurrentTrailer(isTrailer);

		// if(currentTrailerIndex == -1) {
		// pressInGameRun ();
		// }
		/**
		 * 过关处理
		 */
		// gotoEndLevel ();

	}

	private static void do_Game_Run() {
		// #if keymode_nokia&&!D608
		int pauseKey = CKey.GK_SOFT_LEFT;
		int sysKey = CKey.GK_SOFT_RIGHT;
		// #else
		// # int pauseKey=CKey.GK_SOFT_RIGHT;
		// # int sysKey=CKey.GK_SOFT_LEFT;
		// #endif

		// 暂停菜单
		if (CKey.isKeyPressed(pauseKey)
				&& m_hero.m_actorProperty[IObject.PRO_INDEX_HP] > 0) {
			setGameState(GST_GAME_MENU);
		}
		// 属性界面
		else if ((CKey.isKeyPressed(sysKey)/*
											 * ||CKey.isKeyPressed(CKey.GK_RETURN
											 * )
											 */)
				&& m_hero.m_actorProperty[IObject.PRO_INDEX_HP] > 0) {
			setGameState(GST_GAME_UI);
			GameUI.setCurrent(GameUI.Form_mainMenu);
			GameUI.form0_index = 0;
			CKey.initKey();
		}
		updatelevel();
	}

	private static void draw_Game_Run(Graphics g) {
		drawLevel(g);
		// #if keymode_nokia
		// #if D608
		// # CTools.afficheSmall(g,"属性",0, dConfig.S_HEIGHT - dConfig.SF_HEIGHT,
		// #
		// dConfig.ANCHOR_LT,dConfig.COLOR_SCENE_NAME,dConfig.COLOR_SCENE_NAME_OUT);
		// # CTools.afficheSmall(g,"系统",dConfig.S_WIDTH, dConfig.S_HEIGHT -
		// dConfig.SF_HEIGHT,
		// #
		// dConfig.ANCHOR_RT,dConfig.COLOR_SCENE_NAME,dConfig.COLOR_SCENE_NAME_OUT);
		// #
		// #else
		CTools.afficheSmall(g, "系统", 0, dConfig.S_HEIGHT - dConfig.SF_HEIGHT,
				dConfig.ANCHOR_LT, dConfig.COLOR_SCENE_NAME,
				dConfig.COLOR_SCENE_NAME_OUT);
		CTools.afficheSmall(g, "属性", dConfig.S_WIDTH, dConfig.S_HEIGHT
				- dConfig.SF_HEIGHT, dConfig.ANCHOR_RT,
				dConfig.COLOR_SCENE_NAME, dConfig.COLOR_SCENE_NAME_OUT);

		// #endif

		// #else
		// # CTools.afficheSmall(g,"属性",0, dConfig.S_HEIGHT - dConfig.SF_HEIGHT,
		// #
		// dConfig.ANCHOR_LT,dConfig.COLOR_SCENE_NAME,dConfig.COLOR_SCENE_NAME_OUT);
		// # CTools.afficheSmall(g,"系统",dConfig.S_WIDTH, dConfig.S_HEIGHT -
		// dConfig.SF_HEIGHT,
		// #
		// dConfig.ANCHOR_RT,dConfig.COLOR_SCENE_NAME,dConfig.COLOR_SCENE_NAME_OUT);
		// #endif
	}

	public static final void drawLevel(Graphics g) {
		cls(g, dConfig.COLOR_BLACK);
		// -------------------------------
		GameEffect.shakeCamera();
		paintLevel(g);
		// drawScanLine ();
		GameEffect.drawEffect(g);
		GameEffect.drawWindow(g);
		// GameEffect.drawAWindow(g,GameEffect.openedWindow);
	}

	/******
	 * 脚本trailer 运行
	 */
	private void init_TrailerRun() {

	}

	private void exit_TrailerRun() {

	}

	private void do_TrailerRun() {
		updateActorLogic();
		runTrailerLogic();
		gMap.updateMap(Camera.cameraLeft, Camera.cameraTop, bRedrawmap);
		bRedrawmap = false;
	}

	private void draw_TrailerRun(Graphics g) {
		drawLevel(g);
	}

	/************************
      *
      */

	/*******************************
	 * script dialog 普通对话/强制对话样本："[left=aID=headID]主角：我说的话很普通！" 提示对话样本:
	 * "[prompt]这是提示信息！" 菜单对话样本: "[left=aID=headID]选项头[r1]1.选项一[/n]2.选项二",
	 * 选项要用[/n]隔开,[r1]前的部分为菜单选项头 说明：[left=aID=headID]说明显示的位置在主角这边，相反为[right],
	 * aID:actorID, headID:headImageID
	 *******************************/
	private static final int[] SD_COLOR_TABLE = { 0x000000ff, 0x000000ff,
			0x0000ff00, 0x00ff0000, 0x00ffffff, };
	private static final Font[] SD_FONT_TABLE = { dConfig.F_SMALL_DEFAULT,
			dConfig.F_MIDDLE, dConfig.F_LARGE, };
	private static final int[] SD_ARRANGE_TABLE = { 0, 1, 2, };
	private static final int[] SD_FACE_TABLE = { 0, 1, 2, };
	private static final int[] SD_BACKGROUND_TABLE = { 0, 1, 2, };

	// //dialog text sign information
	// private static final String[] SIGN_KEY = {"/N", "/C", "/F", "/A", "/S",
	// "/B", "/R", //换行, 颜色, 字体, 对齐, 表情, 背景, 保留
	// };
	// 未修改前的标志
	// sign key id
	private static final int SIGN_KEY_ID_NEW_LINE = 0;
	private static final int SIGN_KEY_ID_COLOR = 1;
	private static final int SIGN_KEY_ID_FONT = 2;
	private static final int SIGN_KEY_ID_ARRANGE = 3;
	private static final int SIGN_KEY_ID_SMILE = 4;
	private static final int SIGN_KEY_ID_BACKGROUND = 5;
	private static final int SIGN_KEY_ID_RESERVED = 6;

	// 修改后的标志
	public static final int SIGN_NEW_LINE = 0;
	public static final int SIGN_SMILE = 1;
	public static final int SIGN_CSS = 2;
	public static final int SIGN_CSS_DEFAULT = 3;

	// dialog type
	private static final int DLG_TYPE_COMMON = 0; // 普通对话
	private static final int DLG_TYPE_FORCE = 2; // 强制对话
	// private static final int DLG_TYPE_OPTION_MENU = 2; //菜单对话
	private static final int DLG_TYPE_PROMPT = 1; // 自定义,提示对话
	public static final int DLG_TYPE_DEFINE = 3;
	// add by zengkai 08/9/23
	private static final int DLG_TYPE_CGDLG = 4; // 动画字符

	// dialog show position definition
	private static final int DLG_SP_LEFT = 0; // 左边
	private static final int DLG_SP_RIGHT = 1; // 右边
	private static final int DLG_SP_MIDDLE = 2; // 中间提示的

	// (startPos, type, value)说明：startPos为去掉标志的可显示串中的pos
	public static final int DLG_SIGN_INFO_LENGTH = 3;
	public static final int DLG_SIGN_INFO_INDEX_POS = 0; // (startPos, type,
															// value)
	public static final int DLG_SIGN_INFO_INDEX_TYPE = 1;
	public static final int DLG_SIGN_INFO_INDEX_VALUE = 2;

	// dialog text information about sign etc.
	public static short dlgType; // 对话类型
	public static short dlgActorID; // 对话对象编号
	public static short dlgShowPos; // 对话显示位置
	public static short dlgHeadActionID; // 当前对话头像的actionID
	public static short dlgTextID; // 对话文本编号
	// add by zengkai 08/9/23
	private static short dlgTextPos; // 对话字符显示的位置
	private static short dlgShowType; // 对话显示的方式

	public final static byte SHOW_TYPE_A = 0;
	public final static byte SHOW_TYPE_B = 1;
	public final static byte SHOW_TYPE_C = 2;

	public static short CGanmationID;
	public static short CGactionID;

	public static CObject objCurrentSaying; // 当前说话的人物

	private static String dlgShowText; // 要显示的串
	private static int signCount; // sign数量
	private static short[] dlgSignInfo; // (startPos, type, value)

	// dialog draw information
	private static final int DLG_TEXT_DEFAULT_COLOR = 0xffffff /*
																 * GameUI.FONT_COLOR
																 */; // 文本显示默认的颜色
	private static final int DLG_SELECTED_COLOR = 0xff0000 /*
															 * GameUI.COLOR_LIST_SELECTED
															 */; // 文本选中的颜色
	private static final int DLG_TEXT_DEFAULT_COLOR_PMT = 0x75F2DC; // 文本显示默认的颜色
	private static int startTextColor; // 开始绘制对话结束时的颜色
	private static int endTextColor; // 页结束时的绘制颜色
	private static int curStartIndex; // 显示串中要显示的开始位置
	private static int curEndIndex; // 显示串中要显示的结束位置
	private static int curTempIndex;
	private static int[][] dlgRect; // 绘制对话内容的矩形框type, x,y,w,h
	private static int[] tempRect;
	private static int[] tempRect2;

	private static final int DLG_ROWS = 2; // 一页显示多少行
	private static final int DLG_OP_ROWS = 3; // 选项的行数

	// 1.普通对话

	// 2.提示对话/强制对话
	private static final int DLG_FORCE_COUNT = 18; // 强制对话一页的显示时间
	private static int dlgForceCount; // 强制对话的计数器

	// 3.菜单选择对话
	private static int optionNumOfRow; // 当前的选项页的行数量(包含选项头和选项本身)
	public static int dlgCurLineIndex; // 当前选择项索引, 从1开始, 第0项是选项头标题
	private static int[] optionPosOfRows; // 各个选项页中行的位置
	private static boolean haveOptionHead; // 是否有选项头

	//
	// //dialog type
	// private static final int DLG_TYPE_COMMON = 0; //普通对话
	// private static final int DLG_TYPE_FORCE = 1; //强制对话
	// // private static final int DLG_TYPE_OPTION_MENU = 2; //菜单对话
	// private static final int DLG_TYPE_PROMPT = 2; //自定义,提示对话
	//
	// public static CObject objCurrentSaying; //当前说话的人物
	// private static String dlgShowText; //要显示的串
	// private static short dlgTextID; //对话文本编号
	//
	// private static String dlgContent; //选择性对话的题目内容
	// private static short[] dlgSign; //
	//
	// private static String[] dlgOpContent; //选择性对话的选项内容
	// private static short[][] dlgOpSign;
	// private static byte dlgIndex;
	//
	// private static int opIndex;
	//
	// private static int dlgOpValue[];
	//
	// private static int startTextColor; //开始绘制对话结束时的颜色
	// private static int endTextColor; //页结束时的绘制颜色
	// private static int curStartIndex; //显示串中要显示的开始位置
	// private static int curEndIndex; //显示串中要显示的结束位置
	// private static int curTempIndex;
	//
	// public static final int DLG_SIGN_INFO_LENGTH = 3;
	// public static final int DLG_SIGN_INFO_INDEX_POS = 0; //(startPos, type,
	// value)
	// public static final int DLG_SIGN_INFO_INDEX_TYPE = 1;
	// public static final int DLG_SIGN_INFO_INDEX_VALUE = 2;

	private static int opIndex;
	private static int opdrawIndex;
	private static String[] dlgOpContent; // 选择性对话的选项内容
	private static short[][] dlgOpSign;
	private static String dlgContent; // 选择性对话的题目内容
	public static byte dlgIndex;
	private static short[] dlgSign;

	// 初始化选择性对话的信息
	private static void init_OpDlg() {
		dlgContent = ResLoader.questionContent[dlgIndex];
		dlgSign = ResLoader.questionSign[dlgIndex];
		dlgOpContent = ResLoader.optionContent[dlgIndex];
		dlgOpSign = ResLoader.optionSign[dlgIndex];

		curStartIndex = 0;
		curEndIndex = dlgContent.length();
		if (dlgOpContent.length >= 3) {
			initList((byte) 3, (byte) dlgOpContent.length);
		} else {
			initList((byte) 2, (byte) dlgOpContent.length);
		}
		// opIndex = 0;

		int width = dConfig.S_WIDTH - 2 * LINE_W;
		int height = dConfig.S_HEIGHT / 4;
		int x = LINE_W;
		int y = dConfig.S_HEIGHT - height - LINE_W;

		GameEffect.openedWindow = GameEffect.initWindow(x, y, width, height,
				GameEffect.WINDOWS_STATE_OPENING_V,
				GameEffect.windowColorShuiHu, 3);
		int[] block = new int[] { 0, x, y, width, height };
		tempRect = block;

		int width_op = dConfig.S_WIDTH - 10 * LINE_W;
		// 窗口高度，3行字以上
		int height_op = Math.max(m_g.getFont().getHeight() * 3 + 8,
				dConfig.S_HEIGHT / 5);
		int x_op = 5 * LINE_W;
		int y_op = dConfig.S_HEIGHT / 2 - height_op / 2;

		GameEffect.openedWindow1 = GameEffect.initWindow(x_op, y_op, width_op,
				height_op, GameEffect.WINDOWS_STATE_OPENING_V,
				GameEffect.windowColorShuiHu, 3);
		int[] block_op = new int[] { 0, x_op, y_op, width_op, height_op };
		tempRect2 = block_op;
		CKey.initKey();
	}

	private static void exit_OpDlg() {
		CKey.initKey();
		GameEffect.closeWindow(GameEffect.openedWindow);
		GameEffect.closeWindow(GameEffect.openedWindow1);
	}

	private static byte list_Size;
	private static byte list_Capcity;
	private static byte opDrawIndex;

	private static void initList(byte size, byte capcity) {
		list_Size = size;
		list_Capcity = capcity;
		opIndex = 0;
		opDrawIndex = 0;
	}

	// 选择性对话的逻辑处理
	private static void do_OpDlg() {
		updateAnimLogic();

		if (!GameEffect.windowOpen) {
			return;
		}

		if (CKey.isKeyPressed(CKey.GK_UP)) {
			opIndex--;

			if (opIndex < list_Capcity - list_Size
					&& opDrawIndex >= list_Capcity - list_Size) {
				opDrawIndex--;
			}

			if (opIndex == -1) {
				opIndex = dlgOpContent.length - 1;
				opDrawIndex = (byte) (list_Capcity - list_Size);
			}
			CKey.initKey();
		} else if (CKey.isKeyPressed(CKey.GK_DOWN)) {
			opIndex++;

			if (opIndex >= list_Size && opIndex <= list_Capcity
					&& (opDrawIndex + list_Size) < list_Capcity) {
				opDrawIndex++;
			}

			if (opIndex == dlgOpContent.length) {
				opIndex = 0;
				opDrawIndex = 0;
			}
			CKey.initKey();
		}

		if (CKey.isKeyPressed(CKey.GK_RIGHT)) {
			if (curEndIndex == dlgContent.length()) {
				curStartIndex = 0;
				curEndIndex = dlgContent.length();
			} else {
				curTempIndex = curStartIndex;
				curStartIndex = curEndIndex;
				curEndIndex = dlgContent.length();
			}
			CKey.initKey();
		}

		if (CKey.isKeyPressed(CKey.GK_MIDDLE)) {
			ResLoader.dlgOpValue[dlgIndex] = opIndex;
			setGameState(GST_SCRIPT_RUN);
			CKey.initKey();
		}

	}

	// 绘制选择性对话
	private static void draw_OpDlg(Graphics g) {
		drawLevel(g);
		if (GameEffect.windowOpen) {
			drawOpDlgText(g, dlgContent, dlgSign, 4, tempRect,
					dConfig.COLOR_CYAN);
			drawOptionDlg(g, tempRect2, dConfig.COLOR_WHITE,
					dConfig.COLOR_BLACK);
		}
	}

	/**
	 * 选择性对话
	 * 
	 * @param g
	 *            Graphics
	 * @param block
	 *            int[]
	 * @param color
	 *            int
	 * @param clrBg
	 *            int
	 */
	private static void drawOptionDlg(Graphics g, int[] block, int color,
			int clrBg) {
		int x, y, w, h;
		x = block[1];
		y = block[2] + 3;
		w = block[3];
		h = block[4];

		int fontH = dConfig.SF_HEIGHT;
		for (int i = opDrawIndex; i < list_Size + opDrawIndex; i++) {
			if (opIndex == i) {
				CTools.scrollStringInRect(g, dlgOpContent[i], x, y + fontH
						* (i - opDrawIndex), w, dConfig.SF_HEIGHT,
						dConfig.COLOR_CYAN, clrBg);
			} else {
				CTools
						.scrollStringInRect(g, dlgOpContent[i], x, y + fontH
								* (i - opDrawIndex), w, dConfig.SF_HEIGHT,
								color, clrBg);
			}
		}

		// for(int i = 0; i < dlgOpContent.length; i++) {
		// if(opIndex == i) {
		// CTools.scrollStringInRect (g, dlgOpContent[i], x, y + fontH * i, w,
		// dConfig.SF_HEIGHT, dConfig.DEFAULT_WATER_COLOR
		// , clrBg);
		// }
		// else {
		// CTools.scrollStringInRect (g, dlgOpContent[i], x, y + fontH * i, w,
		// dConfig.SF_HEIGHT, color, clrBg);
		// }
		// }

	}

	private static void drawOpDlgText(Graphics g, String dlgContent,
			short[] dlgSign, int numRow, int[] block, int clrBg) {
		char ch;
		boolean bNewLine;
		int x = block[1], y = block[2] + 3, curLine = 0;
		// 2.draw dialog text
		g.setColor(startTextColor);
		g.setFont(dConfig.F_SMALL_DEFAULT);
		for (int i = curStartIndex; i < curEndIndex; i++) {
			bNewLine = false;
			// 2.1.当前索引是否有标志
			for (int id = 0, j = 0; j < dlgSign.length / DLG_SIGN_INFO_LENGTH; j++) {
				if (i == dlgSign[id + DLG_SIGN_INFO_INDEX_POS]) {
					switch (dlgSign[id + DLG_SIGN_INFO_INDEX_TYPE]) {
					case SIGN_NEW_LINE: // /n换行
						bNewLine = true;
						break;

					case SIGN_SMILE: // smile 表情符号

						// ............................
						// 根据需要自行添加
						break;

					case SIGN_CSS: // 字体样式
						int cssID = dlgSign[id + DLG_SIGN_INFO_INDEX_VALUE];
						// 取得颜色
						g.setColor(getColor(cssID));
						// 取得字体
						g.setFont(getFont(cssID));
						break;

					case SIGN_CSS_DEFAULT:

						// 取得颜色
						g.setColor(getColor(0));

						// 取得字体
						g.setFont(getFont(0));
						break;

					default:
						System.out
								.println("matchDialogSign():no match the dialog text sign = "
										+ dlgSign[j + DLG_SIGN_INFO_INDEX_TYPE]);
						break;
					}
					break;
				}
				id += DLG_SIGN_INFO_LENGTH;
			}

			// ---------对话绘制方式----------------
			// 2.2.显示控制
			ch = dlgContent.charAt(i);
			// 一行绘制结束
			if (bNewLine || x + g.getFont().charWidth(ch) > block[1] + block[3]) {
				curLine++;
				x = block[1];
				y += dConfig.SF_HEIGHT;
				// 一页绘制结束
				if (curLine >= numRow) {
					curEndIndex = i;
					continue; // 一页结束后就不要绘制后续的字符了
				}
			}
			// 2.3.绘制
			endTextColor = g.getColor();

			if (clrBg >= 0) {
				g.setColor(clrBg);
				g.drawChar(ch, x + 1, y, dConfig.ANCHOR_LT);
				g.drawChar(ch, x - 1, y, dConfig.ANCHOR_LT);
				g.drawChar(ch, x, y + 1, dConfig.ANCHOR_LT);
				g.drawChar(ch, x, y - 1, dConfig.ANCHOR_LT);
			}

			g.setColor(endTextColor);
			g.drawChar(ch, x, y, dConfig.ANCHOR_LT);
			x += g.getFont().charWidth(ch);
			// ----------------------------------------------------------------------------------
			// 2.4.对话绘制结束
			if (i == dlgContent.length() - 1) {
				curEndIndex = dlgContent.length();
			}
		}

	}

	/**
	 * 初始化对话的,编译对话文本
	 */
	private final static int LINE_W = 10;
	public static AniPlayer iconPlayer;

	public static void setIconPlayer(int iconId) {
		if (iconId == -1 || ResLoader.dialogHeadAniID < 0) {
			return;
		}

		int animationID = ResLoader.dialogHeadAniID;

		int posx = 3 * LINE_W;
		int posy = dConfig.S_HEIGHT * 4 / 5 - LINE_W - 4;

		if (dlgShowPos == DLG_SP_RIGHT) {
			posx = dConfig.S_WIDTH - 2 * LINE_W - 20;
		}

		if (iconPlayer == null) {
			iconPlayer = new AniPlayer(ResLoader.animations[animationID], posx,
					posy, iconId);
		}
	}

	private static void init_ScriptDlg() {

		objCurrentSaying = Script.getObject(dlgActorID);
		dlgShowText = Script.dailogLevelText[dlgTextID];
		dlgSignInfo = Script.dailogLevelSignInfo[dlgTextID];

		signCount = dlgSignInfo.length / DLG_SIGN_INFO_LENGTH;

		setIconPlayer(dlgHeadActionID);
		/**
		 * 和rpg有区别 需要修改
		 */
		// 设置对话人的状态
		// if(Script.objScriptRun.m_currentState != 100) {
		// Script.objScriptRun.setState (Script.objScriptRun.ST_ACTOR_WAIT);
		// }

		// m_hero.setState (CHero.ST_ACTOR_WAIT, -1, true);

		// init draw dialog informations
		curStartIndex = 0;
		curEndIndex = dlgShowText.length();
		startTextColor = DLG_TEXT_DEFAULT_COLOR;
		int width;
		int height;
		int x;
		int y;
		int[] block;
		switch (dlgType) {
		case DLG_TYPE_COMMON: // 普通对话　
		case DLG_TYPE_FORCE: // 提示对话
		case DLG_TYPE_DEFINE:

			// 需要预处理
			// dlgRect = (dlgShowPos == DLG_SP_LEFT) ? GameUI.getDialogBlock
			// (GameUI.DLG_TYPE_LEFT)
			// : GameUI.getDialogBlock (GameUI.DLG_TYPE_RIGHT);
			width = dConfig.S_WIDTH - 2 * LINE_W;
			height = dConfig.S_HEIGHT / 5;
			x = LINE_W;
			y = dConfig.S_HEIGHT - height - LINE_W - 4;

			GameEffect.openedWindow = GameEffect.initWindow(x, y, width,
					height, GameEffect.WINDOWS_STATE_OPENING_V,
					GameEffect.windowColorShuiHu, 3);
			block = new int[] { 0, x, y, width, height };
			tempRect = block;
			break;
		//
		case DLG_TYPE_PROMPT: // 提示对话
			width = dConfig.S_WIDTH - 4 * LINE_W;
			height = dConfig.S_HEIGHT / 4;
			x = 2 * LINE_W;
			y = dConfig.S_HEIGHT / 2 - height / 2;

			GameEffect.openedWindow = GameEffect.initWindow(x, y, width,
					height, GameEffect.WINDOWS_STATE_OPENING_V,
					GameEffect.windowColorShuiHu, 3);
			block = new int[] { 0, x, y, width, height };
			tempRect = block;

			// dlgRect = GameUI.getDialogBlock (GameUI.DLG_TYPE_PROMPT);
			// startTextColor = DLG_TEXT_DEFAULT_COLOR_PMT;
			break;
		}
	}

	/**
	 * 退出对话状态
	 */
	private static void exit_ScriptDlg() {
		if (objCurrentSaying != null) {
			objCurrentSaying = null;
		}
		dlgShowText = null;
		signCount = 0;
		dlgSignInfo = null;
		// 需要预处理
		dlgRect = null;
		tempRect = null;
		optionPosOfRows = null;

		if (iconPlayer != null) {
			iconPlayer.clear();
			iconPlayer = null;
		}
	}

	public static void updateAnimLogic() {
		CObject actor_shell;
		int shell_id = activeActorsListHead;
		while (shell_id >= 0) {
			nextUpdateShellID = nextActorShellID[shell_id];
			actor_shell = m_actorShells[shell_id];
			actor_shell.updateAnimation();
			shell_id = nextUpdateShellID;
		}
	}

	/**
	 * ScriptDialog状态逻辑
	 */
	private static void do_ScriptDlg() {
		updateAnimLogic();
		if (!GameEffect.windowOpen) {
			return;
		}
		switch (dlgType) {
		case DLG_TYPE_COMMON:
		case DLG_TYPE_DEFINE:
			break;

		case DLG_TYPE_FORCE:

			// 如果等不及按了攻击键
			if (CKey.isKeyPressed(CKey.GK_MIDDLE)) {
				dlgForceCount = 0;
				break;
			}

			// 自动的翻页
			dlgForceCount++;
			if (dlgForceCount > DLG_FORCE_COUNT) {
				dlgForceCount = 0;
				CKey.m_fastCurrentKey |= CKey.GK_MIDDLE;
			}
			break;

		}
		// 翻页
		if (CKey.isKeyPressed(CKey.GK_MIDDLE)) {
			if (curEndIndex == dlgShowText.length()) {
				GameEffect.clearWindow(GameEffect.openedWindow);
				setGameState(GST_SCRIPT_RUN);
			} else {
				curStartIndex = curEndIndex;
				curEndIndex = dlgShowText.length();
				startTextColor = endTextColor;
			}
			CKey.initKey();
		}
	}

	/**
	 * ScriptDialog 状态绘制
	 * 
	 * @param g
	 *            Graphics
	 */

	private static void draw_ScriptDlg(Graphics g) {

		drawLevel(g);
		switch (dlgType) {
		case DLG_TYPE_COMMON: // 普通对话
		case DLG_TYPE_DEFINE:

			// 普通对话/强制对话 需要预处理
			if (dlgShowPos == DLG_SP_LEFT) {
				// GameUI.showDialog (g, GameUI.DLG_TYPE_LEFT);
				// drawPublicFrame (g, CGame.PUB_ANI_DLG_HEAD, dlgHeadActionID,
				// dlgRect[1][1] + (dlgRect[1][3] >> 1)
				// , dlgRect[1][2] + (dlgRect[1][4] >> 1));
				// draw text
				// drawDlgText (g, DLG_ROWS, -1, dlgRect[0]);
				if (GameEffect.windowOpen) {
					if (iconPlayer != null) {
						CGame.drawIconFrame(g, iconPlayer);
					}
					drawDlgText(g, DLG_ROWS, -1, tempRect,
							dConfig.COLOR_DIALOG_OUT);
				}
			} else {
				// GameUI.showDialog (g, GameUI.DLG_TYPE_RIGHT);
				// drawPublicFrame (g, CGame.PUB_ANI_DLG_HEAD, dlgHeadActionID,
				// dlgRect[1][1] + (dlgRect[1][3] >> 1)
				// , dlgRect[1][2] + (dlgRect[1][4] >> 1));
				// drawDlgText (g, DLG_ROWS, -1, dlgRect[0]);
				if (GameEffect.windowOpen) {
					if (iconPlayer != null) {
						iconPlayer.setSpriteFlipX(-1);
						CGame.drawIconFrame(g, iconPlayer);
					}
					drawDlgText(g, DLG_ROWS, -1, tempRect,
							dConfig.COLOR_DIALOG_OUT);
				}
			}
			break;
		//
		case DLG_TYPE_FORCE: // 提示对话/强制对话

			// if(dlgShowPos == DLG_SP_LEFT) {
			// GameUI.showDialog (g, GameUI.DLG_TYPE_LEFT);
			// drawPublicFrame (g, CGame.PUB_ANI_DLG_HEAD, dlgHeadActionID,
			// dlgRect[0][1] + (dlgRect[0][3] >> 1)
			// , dlgRect[0][2] + (dlgRect[0][4] >> 1));
			// drawDlgText (g, DLG_ROWS, -1, dlgRect[0]);
			// }
			// else {
			// GameUI.showDialog (g, GameUI.DLG_TYPE_LEFT);
			// drawPublicFrame (g, CGame.PUB_ANI_DLG_HEAD, dlgHeadActionID,
			// dlgRect[0][1] + (dlgRect[0][3] >> 1)
			// , dlgRect[0][2] + (dlgRect[0][4] >> 1));
			// drawDlgText (g, DLG_ROWS, -1, dlgRect[0]);
			// }
			break;
		//
		//
		case DLG_TYPE_PROMPT:
			if (GameEffect.windowOpen) {
				drawDlgText(g, DLG_ROWS, -1, tempRect, dConfig.COLOR_WHITE);
			}

			// drawDlgText (g, DLG_ROWS, -1, dlgRect[1]);
			break;
		}
	}

	/**
	 * 绘制对话的文本 调用前要设置： dlgRectStartX, dlgRectStartY, startTextColor,
	 * curStartIndex, curEndIndex signCount, dlgSignInformation
	 * 
	 * @param g
	 *            Graphics
	 * @param numRow
	 *            int
	 * @param curLineIndex
	 *            int
	 * @param block
	 *            int[]
	 */
	public static void drawDlgText(Graphics g, int numRow, int curLineIndex,
			int[] block, int clrBg) {
		char ch;
		boolean bNewLine;
		int x = block[1] + 4, y = block[2] + 4, curLine = 0;
		// 1.菜单选项采用到！绘制选中行的背景
		if (curLineIndex > 0) {
			g.setColor(DLG_SELECTED_COLOR);
			g.fillRect(x, y + curLineIndex * dConfig.SF_HEIGHT, block[3],
					dConfig.SF_HEIGHT);
		}
		// 2.draw dialog text
		g.setColor(startTextColor);
		g.setFont(dConfig.F_SMALL_DEFAULT);
		for (int i = curStartIndex; i < curEndIndex; i++) {
			bNewLine = false;
			// 2.1.当前索引是否有标志
			for (int id = 0, j = 0; j < dlgSignInfo.length
					/ DLG_SIGN_INFO_LENGTH; j++) {
				if (i == dlgSignInfo[id + DLG_SIGN_INFO_INDEX_POS]) {
					switch (dlgSignInfo[id + DLG_SIGN_INFO_INDEX_TYPE]) {
					case SIGN_NEW_LINE: // /n换行
						bNewLine = true;
						break;

					case SIGN_SMILE: // smile 表情符号

						// ............................
						if (objCurrentSaying != null) {
							objCurrentSaying.setFaceInfo(dlgSignInfo[id
									+ DLG_SIGN_INFO_INDEX_VALUE] - 1);
						}

						// 根据需要自行添加
						break;

					case SIGN_CSS: // 字体样式
						int cssID = dlgSignInfo[id + DLG_SIGN_INFO_INDEX_VALUE];

						// 取得颜色
						g.setColor(getColor(cssID));

						// 取得字体
						g.setFont(getFont(cssID));
						break;

					case SIGN_CSS_DEFAULT:

						// 取得颜色
						g.setColor(getColor(0));

						// 取得字体
						g.setFont(getFont(0));
						break;

					default:
						System.out
								.println("matchDialogSign():no match the dialog text sign = "
										+ dlgSignInfo[j
												+ DLG_SIGN_INFO_INDEX_TYPE]);
						break;
					}
					break;
				}
				id += DLG_SIGN_INFO_LENGTH;
			}

			// 2.2.显示控制
			ch = dlgShowText.charAt(i);
			// 一行绘制结束
			if (bNewLine || x + g.getFont().charWidth(ch) > block[1] + block[3]) {
				curLine++;
				x = block[1] + 4;
				y += dConfig.SF_HEIGHT;
				// 一页绘制结束
				if (curLine >= numRow) {
					curEndIndex = i;
					continue; // 一页结束后就不要绘制后续的字符了
				}
			}
			endTextColor = g.getColor();
			if (clrBg >= 0) {
				g.setColor(clrBg);
				g.drawChar(ch, x + 1, y, dConfig.ANCHOR_LT);
				g.drawChar(ch, x - 1, y, dConfig.ANCHOR_LT);
				g.drawChar(ch, x, y + 1, dConfig.ANCHOR_LT);
				g.drawChar(ch, x, y - 1, dConfig.ANCHOR_LT);
			}
			g.setColor(endTextColor);
			// 2.3.绘制
			g.drawChar(ch, x, y, dConfig.ANCHOR_LT);
			x += g.getFont().charWidth(ch);
			// 2.4.对话绘制结束
			if (i == dlgShowText.length() - 1) {
				curEndIndex = dlgShowText.length();
			}
		}
		endTextColor = g.getColor();
		// 3.draw dialog end
	}

	// 根据css的数据得到得到颜色 字体 和样式
	// 取得字体
	public static Font getFont(int cssID) {
		int value = ResLoader.CSS[cssID][1]; // 取得字体样式
		switch (value) {
		case 0:
			return dConfig.F_SMALL_DEFAULT;
		case 1:
			return dConfig.F_MIDDLE;

		case 2:
			return dConfig.F_LARGE;

		default:
			return dConfig.F_SMALL_DEFAULT;
		}

	}

	public static int getColor(int cssID) {
		int value = ResLoader.CSS[cssID][0]; // 取得颜色
		switch (value) {
		case 0:
			return dConfig.DLG_TEXT_DEFAULT_COLOR;

		default:
			return dConfig.SD_COLOR_TABLE[value];

		}

	}

	public static int getAnchor(int anchorID) {
		switch (anchorID) {
		case 0:
			return dConfig.ANCHOR_LT;

		default:
			return dConfig.ANCHOR_TABLE[anchorID];

		}

	}

	// 公共动画控制器
	public static final byte PUB_ANI_SYS = 0; // 系统/表情/任务标示，"!" "?" etc. //全局
	public static final byte PUB_ANI_DLG_HEAD = 1; // 对话头像 //全局
	public static final byte PUB_ANI_GOODS_ICON = 2; // 物品Icon，数据编辑器关联的 //UI中使用
	public static final byte PUB_ANI_EFFECT = 3; // 特效贴图，数据编辑器关联的 //一般是战斗中使用
	public static final byte PUB_ANI_UI = 4;
	public static final byte PUB_ANI_CG = 5;

	// 公共动画控制器
	private static short[][][] publicASC;

	/**
	 * 绘制公共动画，如头像、显示在地上的物品等
	 * 
	 * @param g
	 *            Graphics
	 * @param x
	 *            int
	 * @param y
	 *            int
	 */

	public static boolean drawPublicFrame(Graphics g, AniPlayer aniPlayer) {

		if (aniPlayer == null) {
			// System.out.println (
			// ">>drawPublicFrame(): Cant paint! maybe you don't set \"Pack Anim?\" to \"No\"!! Or you dont select this animation into this scene! Or has error in load animation"
			// );
			return false;
		}

		aniPlayer.updateAnimation();
		aniPlayer.drawFrame(g, null);
		if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			return true;
		}

		return false;
	}

	public static void drawIconFrame(Graphics g, AniPlayer aniPlayer) {
		AniData.setMlgs(ResLoader.aniMlgs);
		if (aniPlayer == null) {
			// System.out.println (
			// ">>drawPublicFrame(): Cant paint! maybe you don't set \"Pack Anim?\" to \"No\"!! Or you dont select this animation into this scene! Or has error in load animation"
			// );
			return;
		}
		aniPlayer.drawFrame(g);
		aniPlayer.updateAnimation();
	}

	/***************
      *
      ******************/
	private static void init_Script() {
		// Script.scriptLock = true;
	}

	private static void exit_Script() {
		Script.scriptLock = false;
		CKey.initKey();
	}

	private static void do_Script() {
		Script.doScriptLogic();
	}

	private static void draw_Script(Graphics g) {
		drawLevel(g);
	}

	/**
	 * paint
	 * 
	 * @param graphics
	 *            Graphics
	 */
	protected void paint(Graphics g) {
		if (!isRunning) {
			return;
		}

		// #if K700C
		// # if (m_curState!=GST_GAME_RUN) {
		// # System.gc();
		// # }
		// #endif

		this.m_g = g;
		// #if nokiaAPI
		this.m_dg = DirectUtils.getDirectGraphics(g);
		// #endif
		GameEffect.renderInfo = GameEffect.getRenderInfo();

		// --------------短代界面
		// if (MessageCharge.isMessageState) {
		// MessageCharge.draw_Message(g,dConfig.S_WIDTH,dConfig.S_HEIGHT);
		// return;
		// }
		g.setFont(dConfig.F_SMALL_DEFAULT);
		switch (m_curState) {
		case GST_MINI_GAME:
			do_MINI_GAME();
			if (m_curState == GST_MINI_GAME) {
				draw_MINI_GAME(g);
			}
			break;
		// 更多精彩
		// case GST_GAME_MORE_GAME:
		// MoreGame.doMoreGmae();
		// if (GST_GAME_MORE_GAME == m_curState) {
		// MoreGame.drawMoreGame(g);
		// }
		// break;
		case GST_GAME_HERO_DIE:
			do_Game_Hero_Die();
			if (m_curState == GST_GAME_HERO_DIE) {
				draw_Game_Hero_Die(g);
			}
			break;
		case GST_GAME_OPTION:
			do_Game_Option();
			if (m_curState == GST_GAME_OPTION) {
				draw_Game_Option(g);
			}
			break;
		case GST_GAME_IF_MUSIC:
			do_Game_IfMusic();
			if (m_curState == GST_GAME_IF_MUSIC) {
				draw_Game_IfMusic(g);
			}
			break;
		// FXQ
		case GST_GAME_UI:
			do_GAME_UI();
			if (m_curState == GST_GAME_UI) {
				draw_GAME_UI(g);
			}
			break;
		case GST_GAME_MENU:
			do_GAME_MENU();
			if (m_curState == GST_GAME_MENU) {
				draw_GAME_MENU(g);
			}
			break;
		case GST_GAME_HELP:
			do_Game_Help();
			if (m_curState == GST_GAME_HELP) {
				draw_Game_Help(g);
			}
			break;
		case GST_GAME_ABOUT:
			do_Game_About();
			if (m_curState == GST_GAME_ABOUT) {
				draw_Game_About(g);
			}

			break;
		// 游戏logo 状态
		case GST_TEAM_LOGO:
			do_TEAM_LOGO();
			if (m_curState == GST_TEAM_LOGO) {
				draw_TEAM_LOGO(g);
			}
			break;

		case GST_MAIN_MENU:
			do_MainMenu();
			if (m_curState == GST_MAIN_MENU) {
				draw_MainMenu(g);
			}
			break;

		// 游戏加载状态
		case GST_GAME_LOAD:
			do_Game_Load();
			if (m_curState == GST_GAME_LOAD) {
				draw_Game_Load(g);
			}
			break;

		// 游戏运行状态
		case GST_GAME_RUN:
			do_Game_Run();
			if (m_curState == GST_GAME_RUN) {
				draw_Game_Run(g);
			}

			break;

		case GST_GAME_EXIT: // 退出状态
			do_Game_Exit();
			if (m_curState == GST_GAME_EXIT) {
				draw_Game_Exit(g);
			}
			break;
		// 游戏选择状态
		case GST_SCRIPT_OPDLG:
			do_OpDlg();
			if (m_curState == GST_SCRIPT_OPDLG) {
				draw_OpDlg(g);
			}
			break;

		// 镜头移动状态
		case GST_CAMARE_MOVE:
			do_CameraMove();
			if (m_curState == GST_CAMARE_MOVE) {
				draw_CameraMove(g);
			}
			break;

		// 脚本对话状态
		case GST_SCRIPT_DIALOG:
			do_ScriptDlg();
			if (m_curState == GST_SCRIPT_DIALOG) {
				draw_ScriptDlg(g);
			}
			break;

		case GST_SCRIPT_RUN:
			do_Script();
			if (m_curState == GST_SCRIPT_RUN) {
				draw_Script(g);
			}
			break;

		case GST_TRAILER_RUN:
			do_TrailerRun();
			if (m_curState == GST_TRAILER_RUN) {
				draw_TrailerRun(g);
			}
			break;

		}

		if (CDebug.bForTest && CDebug.bShowKeyCode) {
			g.setClip(0, 0, 240, 320);
			g.setColor(0xaffe);
			g.drawString(testkeycode + "", 100, 100, 0);
		}
		if (CDebug.bForTest && CDebug.bShowFreeMem) {
			g.setClip(0, 0, 240, 320);
			g.setColor(0xff);
			g.drawString("free = " + Runtime.getRuntime().freeMemory() / 1024
					+ "k", 100, 100, 0);
		}

		if (CDebug.bForTest) {
			// g.setClip(0, 0, 240, 320);
			// g.setColor(0xff0000);
			// g.drawString("" + showKey, 100, 100, 0);
		}

		// g.setClip(0,0,240,320);
		// g.setColor(0xff0000);
		// g.drawString("hero_VY = "+heroVY,100,100,0);
		// g.drawString("hero_VY2 = "+heroVY2,100,120,0);
		// g.drawString("error = "+error,100,140,0);
	}

	public static int heroVY, heroVY2, error;
	/**************************
	 * 按键处理
	 **************************/
	// ------------------按键处理 -----------------------------
	public static boolean m_isKeyLocked;
	static int m_curRealKeyCode;
	static int showKey;

	public void keyPressed(int keyCode) {
		if (m_isKeyLocked) {
			return;
		}
		showKey = keyCode;
		int key = CKey.GetKey(keyCode);
		testkeycode = m_curRealKeyCode = keyCode;
		getKeyPressed(key);

		// 短代按键处理
		// if (MessageCharge.isMessageState) {
		// CKey.updateKey();
		// MessageCharge.key_Message(key);
		// }
		// #if N5500
		// # if (key==CKey.GK_SOFT_LEFT) {
		// # keyReleased(keyCode);
		// # }
		// #endif

		// #if D508||D608||N5500
		// # if (key==CKey.GK_SOFT_LEFT||key==CKey.GK_SOFT_RIGHT) {
		// # keyReleased(keyCode);
		// # }
		// #endif

	}

	public static void getKeyPressed(int key) {
		CKey.m_fastCurrentKey |= key;
		if (CDebug.bShowKeyInfo) {
			System.out.print("press -----> ");
		}
		// 压栈
		CKey.pushQueue(CKey.m_fastCurrentKey);
	}

	public void keyReleased(int keyCode) {
		if (m_isKeyLocked) {
			return;
		}
		int key = CKey.GetKey(keyCode);
		m_curRealKeyCode = 0;
		getKeyReleased(key);
		// #if S700
		// # if (key==CKey.GK_LEFT||key==CKey.GK_RIGHT) {
		// # if (m_hero.m_actionID==
		// m_hero.action_map[m_hero.ST_ACTOR_RUN][m_hero.RUN_ACT_INDEX]) {
		// # m_hero.setState (m_hero.ST_ACTOR_WAIT, -1, true);
		// # }
		// # }
		// #endif

	}

	public static void getKeyReleased(int key) {
		CKey.m_fastCurrentKey &= ~key;
		if (CDebug.bShowKeyInfo) {
			System.out.print("release <----- ");
		}

		// 压栈
		CKey.pushQueue(CKey.m_fastCurrentKey);

	}

	/**
	 * 线程控制
	 */
	long m_timeCur, m_tick_start;
	private static long curFrameTime;
	public static boolean isRunning = true; // control game run
	// #if S700
	// # public final static int FPS_RATE = 110;
	// #else
	public final static int FPS_RATE = 80;

	// #endif
	public void run() {
		long limit = 0;
		while (isRunning) {
			limit = System.currentTimeMillis() + FPS_RATE;
			curFrameTime = System.currentTimeMillis();

			CKey.updateKey();

			repaint();
			serviceRepaints();
			while (limit > System.currentTimeMillis()) {
				Thread.yield();
			}
		}
		if (!isRunning) {
			destroyGame();
		}
	}

	/**
	 * destroyGame 退出游戏的处理
	 */
	public static void destroyGame() {
		CMIDlet.midlet.destroyApp(true);
		// CMIDlet.midlet.notifyDestroyed ();
	}

	/**
	 * 用黑色清屏
	 * 
	 * @param g
	 *            Graphics
	 */
	public static void cls(Graphics g, int color) {
		g.setColor(color);
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		g.fillRect(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
	}

	private static int aniLoadIndex;
	private static int[] animationID;
	private static int[] releaseAniID;
	public static int oldLevelID = -1; // previous level id
	public static int curLevelID = 0; // current level id

	private static int loadLevel() {
		long freeMem = 0;
		if (loadingProgress == 1) {
			if (loadingProgress == 1) {
				destroyLevel(false);
				// 取得释放的AnimationID信息
				if (oldLevelID != -1) {
					releaseAniID = AniData.getReleaseAniID(oldLevelID);
					AniData.releaseAni(releaseAniID, ResLoader.animations,
							ResLoader.aniMlgs);
				}
				// #if !W958C
				System.gc();
				// #endif

				// try {
				// Thread.sleep(3000);
				// }
				// catch (Exception e) {
				// e.printStackTrace();
				// }
			}
		} else if (loadingProgress == 15) { // 1.map数据
			freeMem = Runtime.getRuntime().freeMemory();
			MapData mapData = MapData.getInstance();
			mapData.loadLevelMap(curLevelID, ResLoader.s_fileMapResName);
			gMap = new MapDraw(mapData);
			System.out.println("Map Mem  :"
					+ (freeMem - Runtime.getRuntime().freeMemory()) / 1023
					+ "k");
			// #if K700C||S700||D608
			// # Script.loadScriptK700(curLevelID);
			// #else
			Script.loadScript(curLevelID); // 加载脚本信息
			// #endif
		} else if (loadingProgress == 25) { // 2.初始动画载入
			// 取得加载的AnimationID信息
			animationID = AniData.getLoadAniID(curLevelID);
		} else if (loadingProgress == 59) { // 动画载入
			freeMem = Runtime.getRuntime().freeMemory();
			// 加载需要加载的资源
			// #if K700C||S700||D608
			// # AniData.loadAnimationK700(gData.s_fileAniResName, animationID,
			// # gData.animations, gData.aniMlgs);
			// #else
			AniData.loadAnimation(ResLoader.s_fileAniResName, animationID,
					ResLoader.animations, ResLoader.aniMlgs);
			// #endif
			AniData.setMlgs(ResLoader.aniMlgs);
			loadingProgress = 59;
			System.out.println("Animation Mem  :"
					+ (freeMem - Runtime.getRuntime().freeMemory()) / 1023
					+ "k");
			// System.gc();
			// try {
			// Thread.sleep(3000);
			// }
			// catch (Exception e) {
			// e.printStackTrace();
			// }
		} else if (loadingProgress == 80) { // 3.脚本和脚本对话数据scriptsDialog.bin,
											// 场景actors数据
			freeMem = Runtime.getRuntime().freeMemory();
			gData.loadScene(curLevelID); // 加载场景信息
			MapDraw.setMapDrawMode(curLevelID);
			System.out.println("Scenes+Script Mem  :"
					+ (freeMem - Runtime.getRuntime().freeMemory()) / 1023
					+ "k");
		} else if (loadingProgress == LOAD_OK) {

		}
		loadingProgress++;
		return loadingProgress; // 载入进度
	}

	public final static byte SCENE_FLAG_SWITCH_HERO = 1 << 0; // 切换主角
	public final static byte SCENE_FLAG_SCROLL_AT_SCENE = 1 << 1; // 地图多层卷轴,中间层固定在屏幕
	public final static byte SCENE_FLAG_SCROLL_AT_SCREEN = 1 << 2; // 地图多层卷轴,中间层固定在场景

	public static boolean testSceneFlag(byte Mode) {
		return ((ResLoader.sceneFlag & Mode) != 0) ? true : false;
	}

	public static void destroyLevel(boolean destroyAllAnimation) {
		ResLoader.actorsBasicInfo = null;
		ResLoader.actorsBasicInfoOffset = null;
		actorsShellID = null;
		actorsRegionFlags = null;
		if (gMap != null) {
			gMap.mapRes.destroyLeveLMap(destroyAllAnimation);
		}
		// 释放动画资源
		destroyAni(destroyAllAnimation);

		// 释放trailer数据和信息
		ResLoader.nTrailersDuration = null;
		ResLoader.nTrailersTimeLinesCount = null;
		ResLoader.nTrailersTimeLinesActorID = null;
		ResLoader.trailers = null;

		currentTrailerIndex = -1;
		currentTrailerFrame = -1;
		currentTrailerCameraActorID = -1;

		// 3.release actor data and script data 释放脚本信息和数据
		// scriptCommonConditions = null;
		// scriptCommonConducts = null;

		Script.scriptLevelConditions = null;
		Script.scriptLevelConducts = null;

		// dailogCommonText = null;
		Script.dailogLevelText = null;
		// //4.脚本执行对象的shellID, curScriptObjectShellID
		// curScriptObjectShellID = -1;
		if (GameUI.iconPlayer != null) {
			GameUI.iconPlayer.clear();
			GameUI.iconPlayer = null;
		}
		// #if !W958C
		System.gc();
		// #endif

		// -------------------------------------------------------------------
	}

	public static void destroyAni(boolean destroyAllAnimation) {
		if (destroyAllAnimation) {
			// 释放动画数据
			for (int i = 0; i < ResLoader.animationCount; i++) {
				if (ResLoader.animations[i] != null) {
					ResLoader.animations[i].destroy();
					ResLoader.animations[i] = null;
					if (CDebug.showAnimInfo) {
						System.out.println("DEBUG >>　realse animation[" + i
								+ "].");
					}
				}
			}
			// 释放图片资源
			for (int i = 0; i < ResLoader.aniMlgs.length; i++) {
				if (ResLoader.aniMlgs[i] != null) {
					ResLoader.aniMlgs[i].destroy();
					ResLoader.aniMlgs[i] = null;
				}

			}
			MapData.mlgFlag = null;
		}
	}

	/********************************************
	 * 对象管理
	 *************************************/
	// #if size_500k
	public final static int MAX_ACTIVE_ACTORS_COUNT = 80; // 一关场景中最多可以激活的对象数量
	// #else
	// # public final static int MAX_ACTIVE_ACTORS_COUNT = 40;
	// //一关场景中最多可以激活的对象数量
	// #endif
	public static int[] nextActorShellID; // = new short [
											// dLevel.nMaxActivatedActorsCount
											// ];
	public static int[] prevActorShellID; // = new short [
											// dLevel.nMaxActivatedActorsCount
											// ];
	public static int activeActorsListHead; // for the BT bug on s60, we change
											// the type form short to int
	public static int inactiveActorsListHead; // 未激活的头
	public static int[] nextDisplayedShellID; //
	public static int nextUpdateShellID;
	public static short[] actorsShellID;
	public static long[] actorsRegionFlags;

	public static CObject[] m_actorShells = new CObject[MAX_ACTIVE_ACTORS_COUNT];
	// 初始化游戏中的一些静态参数
	static {
		nextActorShellID = new int[MAX_ACTIVE_ACTORS_COUNT];
		prevActorShellID = new int[MAX_ACTIVE_ACTORS_COUNT];
		nextDisplayedShellID = new int[MAX_ACTIVE_ACTORS_COUNT];
		// 创建缓冲区
		MapDraw.createBufferMap();
	}

	/**
	 * 激活一个Actor 并把它放放到激活列表中
	 * 
	 * @param actorID
	 *            指场景中的哪一个对象被激活
	 * @return CActorShell 返回指定的激活的那个Actor, null 表示 激活失败.
	 */
	public static int m_tempClassID = -1;

	public static short getBasicClassID(int linkID) {
		if (linkID < 0) {
			return -1;
		}
		return ResLoader.actorsBasicInfo[(dActor.INDEX_CLASS_ID & dActor.MASK_ACTOR_INFO_INDEX)
				+ ResLoader.actorsBasicInfoOffset[linkID]];

	}

	public static void setTempClassID(int linkID) {
		CGame.m_tempClassID = ResLoader.classAIIDs[getBasicClassID(linkID)];
	}

	public static CObject activateActor(int actorID, boolean bForcely) {
		if (CDebug.bEnableAssert) {
			CDebug._assert(inactiveActorsListHead >= 0,
					"No available actor shells!");
			CDebug._assert(actorID < 0 || actorsShellID[actorID] < 0, "actor #"
					+ actorID + " has been already activated.");
		}

		short shell_id = (short) inactiveActorsListHead;
		/**
		 * 创建子类对象的实例
		 * 
		 * @param <any> actorID
		 */

		createSubObject(actorID, shell_id, bForcely);

		if (shell_id < 0) {
			return null;
		}
		if (actorID == 20) {
			int j = 0;
		}

		if (m_actorShells[shell_id].load(shell_id, actorID, bForcely)) {
			if (actorID >= 0) {
				actorsShellID[actorID] = shell_id;
			}

			// update inactive list
			inactiveActorsListHead = nextActorShellID[shell_id];
			if (inactiveActorsListHead >= 0) {
				prevActorShellID[inactiveActorsListHead] = -1;
			}

			// update active list
			nextActorShellID[shell_id] = activeActorsListHead;
			if (activeActorsListHead >= 0) {
				prevActorShellID[activeActorsListHead] = shell_id;
			}

			activeActorsListHead = shell_id;

			CDebug._showActiveActorCounter();

			return m_actorShells[shell_id];
		}

		return null;
	}

	public final static void createSubObject(int actorID, int shellid,
			boolean bForcely) {
		int aiID = 0;
		if (actorID < 0 && !bForcely) {
			return;
		}

		if (actorID >= 0) {
			int basic_flag = ResLoader.actorsBasicInfo[(dActor.INDEX_BASIC_FLAGS & dActor.MASK_ACTOR_INFO_INDEX)
					+ ResLoader.actorsBasicInfoOffset[actorID]];
			if ((basic_flag & (dActor.FLAG_BASLIC_NOT_LOADABLE & dActor.MASK_BASIC_FLAGS)) != 0
					&& !bForcely) {
				return;
			}

			int id = ResLoader.actorsBasicInfo[(dActor.INDEX_CLASS_ID & dActor.MASK_ACTOR_INFO_INDEX)
					+ ResLoader.actorsBasicInfoOffset[actorID]];
			aiID = ResLoader.classAIIDs[id];
		} else {
			aiID = m_tempClassID;
		}

		if (aiID < 0) {
			if (CDebug.showDebugInfo) {
				CDebug._trace("ClassID 不存在 ");
			}
			return;
		}

		switch (aiID) {
		case dActorClass.CLASS_ID_CAMERA_HINT:
		case dActorClass.CLASS_ID_OBJ_DOOR:
		case dActorClass.CLASS_ID_OBJ_CAR:
		case dActorClass.CLASS_ID_OBJ_KAIGUAN:
		case dActorClass.CLASS_ID_OBJ_MONEY:
		case dActorClass.CLASS_ID_OBJ_ITEM:
		case dActorClass.CLASS_ID_ATTACK_EFFECT:
		case dActorClass.CLASS_ID_OBJ_CANATTACKBOX:
			m_actorShells[shellid] = new CElementor();
			break;
		case dActorClass.CLASS_ID_OBJ_BULLET:
		case dActorClass.CLASS_ID_ENEMY_COMMON:
		case dActorClass.CLASS_ID_OBJ_DICI:
		case dActorClass.CLASS_ID_BOSS_GAO:
		case dActorClass.CLASS_ID_ENEMY_MAFENG:
			m_actorShells[shellid] = new CEnemy();
			break;
		// case dActorClass.CLASS_ID_TRAILERCAMERA:
		// m_actorShells[shellid] = new CNpc ();
		// break;

		default:
			if (m_actorShells[shellid] != CGame.m_hero) {
				m_actorShells[shellid] = new CObject();
			}
			break;

		}

		m_actorShells[shellid].m_shellID = -1;
		if (m_tempClassID >= 0) {
			m_tempClassID = -1;
		}

	}

	/**
	 * 释放一个激活Actor 并更新激活列表
	 * 
	 * @param shellID
	 *            哪一个actor将被释放
	 * @return CActorShell 返回释放的对象 null表示释放失败
	 */
	public static CObject deactivateShell(int shellID)
	// throws Exception
	{
		int actor_id = m_actorShells[shellID].m_actorID;

		if (CDebug.bEnableAssert) {
			CDebug._assert(actor_id < 0 || actorsShellID[actor_id] >= 0,
					"actor " + actor_id + " has not been activated yet.");
		}

		// pack actor
		if (m_actorShells[shellID].pack()) {
			if (actor_id >= 0) {
				actorsShellID[actor_id] = -1;
			}

			// update active list
			if (prevActorShellID[shellID] >= 0) {
				nextActorShellID[prevActorShellID[shellID]] = nextActorShellID[shellID];
			} else {
				activeActorsListHead = nextActorShellID[shellID];
			}

			if (nextActorShellID[shellID] >= 0) {
				prevActorShellID[nextActorShellID[shellID]] = prevActorShellID[shellID];
			}

			// update inactive list
			prevActorShellID[shellID] = -1;
			nextActorShellID[shellID] = inactiveActorsListHead;
			if (inactiveActorsListHead >= 0) {
				prevActorShellID[inactiveActorsListHead] = (short) shellID;
			}
			inactiveActorsListHead = (short) shellID;

			--CDebug.__active_actors_count;
			if (CDebug.bEnableTrace) {
				CDebug._trace(" -- _active_actors_count "
						+ CDebug.__active_actors_count);
			}

			return m_actorShells[shellID];
		}

		return null;
	}

	public static CObject tryActivateActor(int actorID) {
		if (actorsShellID[actorID] < 0) {
			CObject actor = activateActor(actorID, true);
			actor.initialize();
		}
		return m_actorShells[actorsShellID[actorID]];
	}

	/************************************************
	 * 游戏对象的逻辑管理
	 ********************************************/
	public static Image touchImg1, touchImg2; // 触摸屏,操作主角放大招的按钮
	static int testkeycode;
	public static int s_screenFlashTimer;// 闪屏
	public static int s_screenFlashInterval = 2;// 闪屏间隔
	public static int s_screenFlashColor = dConfig.COLOR_WHITE;

	/**
	 * 绘制关卡背景数据，并对人物进行排序绘制
	 * 
	 * @param g
	 *            Graphics
	 */
	public static void paintLevel(Graphics g) { // 绘制关卡
		gMap.paintMap(g, Camera.cameraLeft, Camera.cameraTop); // 绘制背景地图
		// 闪屏
		if (s_screenFlashTimer-- > 0
				&& s_screenFlashTimer % s_screenFlashInterval == 0) {
			cls(g, s_screenFlashColor);
		}
		sortAndPaintActors(g); // 对m_actorShells［］中激活的对象进行排序 并绘制
		updateInterface(g);
		showSceneName(g, curLevelID);
	}

	static boolean ShowSceneName;
	static int sceneNameClip;
	static int sceneNameState;
	static int sceneNameTimer;

	static void initShowSceneName() {
		ShowSceneName = true;
		sceneNameClip = 0;
		sceneNameState = 0;
		sceneNameTimer = 0;
	}

	/**
	 * 显示场景名称
	 */
	static void showSceneName(Graphics g, int levelId) {
		if (ShowSceneName) {
			g.setColor(dConfig.COLOR_WHITE);
			Font f = g.getFont();
			g.setFont(dConfig.F_LARGE_BOLD);
			g.setClip(0, dConfig.S_HEIGHT / 3, dConfig.S_WIDTH,
					f.getHeight() + 3);
			if (sceneNameState == 0) {
				sceneNameClip += 1;
				if (sceneNameClip >= f.getHeight() + 3) {
					sceneNameState = 1;
				}
			} else if (sceneNameState == 1) {
				if (sceneNameTimer++ > 15) {
					sceneNameState = 2;
					sceneNameTimer = 0;
				}
			} else if (sceneNameState == 2) {
				sceneNameClip += 3;
				if (sceneNameClip > 2 * (f.getHeight() + 3)) {
					ShowSceneName = false;
				}
			}

			CTools.afficheSmall(g, SCENE_NAME[levelId], dConfig.S_WIDTH / 2,
					dConfig.S_HEIGHT / 3 + f.getHeight() + 3 - sceneNameClip,
					dConfig.ANCHOR_HT, dConfig.COLOR_SCENE_NAME,
					dConfig.COLOR_SCENE_NAME_OUT);
			g.setFont(f);
		}
	}

	static String[] SCENE_NAME = { "高     府", "大  名  府", "卢     府", "西     郊",
			"断  贝  山", "东     郊", "西 山 酒 店", "聚  义  厅", "梁     山", "大 名 府 地 道",
			"大 名 南 城", "野  猪  林", "醉  杏  楼", "开 封 东 城", "开  封  府", "地     牢", };

	/**
	 * 　对m_actorShells［］中激活的对象进行排序
	 */
	public static void sortAndPaintActors(Graphics g) {
		int display_list_head = -1;
		int insert_shell = activeActorsListHead;
		while (insert_shell >= 0) {
			int z_depth = m_actorShells[insert_shell].m_z;
			if (z_depth >= 0
					&& m_actorShells[insert_shell]
							.testFlag(dActor.FLAG_BASIC_VISIBLE)) {
				int before_shell = display_list_head;
				int after_shell = -1;
				while (before_shell >= 0
						&& m_actorShells[before_shell].m_z < z_depth) {
					after_shell = before_shell;
					before_shell = nextDisplayedShellID[before_shell];

				}
				if (after_shell < 0) {
					display_list_head = insert_shell;
				} else {
					nextDisplayedShellID[after_shell] = insert_shell;
				}

				nextDisplayedShellID[insert_shell] = before_shell;
			}
			insert_shell = nextActorShellID[insert_shell];
		}
		// paint actors　绘制m_actorShells［］中的所有对象
		nextUpdateShellID = display_list_head;
		while (nextUpdateShellID >= 0) {
			m_actorShells[nextUpdateShellID].paint(g);
			nextUpdateShellID = nextDisplayedShellID[nextUpdateShellID];
		}
	}

	/**
	 * 采用激活区域的产生释放机制
	 */
	public static boolean bRedrawmap = true; // 是否更新整个地图背景缓冲区

	public static void updateEngineLogic() { // 更新逻辑 处理逻辑的部分
		updateActorLogic();
		if (currentTrailerIndex >= 0)
		// in trailer 执行trailer部分的logic
		{
			runTrailerLogic();
		} else {
			if (m_curState == GST_CAMARE_MOVE) {
				Camera.updateCamera(false); // 更新镜头
			} else {
				Camera.updateCamera(Script.autoMoveActorCounter <= 0); // 更新镜头
			}
		}
		gMap.updateMap(Camera.cameraLeft, Camera.cameraTop, bRedrawmap);
		bRedrawmap = false;
	}

	// 加标签
	private static void initActorShellList() {
		for (int i = 0; i < m_actorShells.length; i++) {
			m_actorShells[i] = new CObject();
			if (m_actorShells[i].aniPlayer != null) {
				m_actorShells[i].aniPlayer.clear();
				m_actorShells[i].aniPlayer = null;
			}
		}

		if (m_hero == null) {
			m_hero = new CHero();
		}

		m_hero.aniPlayer = null;

		m_actorShells[0] = m_hero;
	}

	/**
	 * 重新设置激活列表和未激活列表
	 */
	private static void resetShellList() {
		if (CDebug.bEnableTrace) {
			CDebug.__active_actors_count = 0;
		} // 重置激活列表
		for (int shell_id = 0; shell_id < MAX_ACTIVE_ACTORS_COUNT; ++shell_id) {
			nextActorShellID[shell_id] = (short) (shell_id + 1);
			prevActorShellID[shell_id] = (short) (shell_id - 1);
			m_actorShells[shell_id].m_shellID = -1;
		}
		prevActorShellID[0] = -1;
		nextActorShellID[MAX_ACTIVE_ACTORS_COUNT - 1] = -1;
		activeActorsListHead = -1;
		inactiveActorsListHead = 0;

	}

	private static void updateActorLogic() {
		CObject actor_shell;
		long camera_region_flags = getRegionFlags(Camera.cameraBox);
		for (int actor_id = ResLoader.nActorsCount - 1; actor_id >= 0; --actor_id) {

			// 取得基本flag信息 包括是否可见 是否翻转flipx 是否总是处于被激活状态
			int basic_flag = ResLoader.actorsBasicInfo[(dActor.INDEX_BASIC_FLAGS)
					+ ResLoader.actorsBasicInfoOffset[actor_id]];
			boolean b_activated; // 可以被激活
			boolean b_deactivated; // 不可以被激活

			// 是否全局激活
			boolean isAlwaysActive = (basic_flag & (dActor.FLAG_BASIC_ALWAYS_ACTIVE & dActor.MASK_BASIC_FLAGS)) == (dActor.FLAG_BASIC_ALWAYS_ACTIVE & dActor.MASK_BASIC_FLAGS);

			boolean isInRegion = (actorsRegionFlags[actor_id] & camera_region_flags) != 0;
			// 是否在激活区域
			boolean isInActiveBox = CTools.isIntersecting(Camera.cameraBox,
					getActorActivateBoxInfo(actor_id, CObject.s_colBox1));

			// 处于激活的两种情况 1.全局激活 2. 处于激活区域
			b_activated = isAlwaysActive || (isInRegion && isInActiveBox);
			b_deactivated = !b_activated;
			int actor_shell_id = actorsShellID[actor_id];
			if (b_activated) {
				// 如果能被激活 则初始化对象信息
				if (actor_shell_id < 0
						&& (actor_shell = activateActor(actor_id, false)) != null) {
					// try {
					actor_shell.initialize();
					// }
					// catch(Exception ex) {
					// ex.printStackTrace ();
					// }
				}
			} else if (b_deactivated) {
				// 不非激活 则释放整个对象信息
				if (actor_shell_id >= 0
						&& (actor_shell = deactivateShell(actor_shell_id)) != null) {
					actor_shell.destroy();
				}
				// 所有trailer的使用的Actor 不能在外部激活,设置flag :FLAG_BASLIC_NOT_LOADABLE
				// 控制
				if ((basic_flag & ((dActor.FLAG_BASIC_REBORNABLE | dActor.FLAG_BASLIC_NOT_LOADABLE) & dActor.MASK_BASIC_FLAGS)) == ((dActor.FLAG_BASIC_REBORNABLE | dActor.FLAG_BASLIC_NOT_LOADABLE) & dActor.MASK_BASIC_FLAGS)) {
					basic_flag &= ~(dActor.FLAG_BASLIC_NOT_LOADABLE & dActor.MASK_BASIC_FLAGS);
					CObject.setActorInfo(actor_id, dActor.INDEX_BASIC_FLAGS,
							basic_flag);
				}
			}
		}

		if (CDebug.bEnableTraceBasicInfo) {
			CDebug.m_updateActorTime = System.currentTimeMillis();
		}

		// 判断是否执行trailer 动画指针大于0 并且trailer的索引大于0的时候
		boolean trailer_running = currentTrailerFrame >= 0
				&& currentTrailerIndex >= 0;
		int shell_id = activeActorsListHead;
		while (shell_id >= 0) {
			nextUpdateShellID = nextActorShellID[shell_id];
			actor_shell = m_actorShells[shell_id];

			// trailer 执行
			if (trailer_running && actor_shell.timelineIndex >= 0) {
				actor_shell.updateAnimation();
				actor_shell.updateTrailer();
			}
			// 自动移动
			else if (Script.autoMoveActorCounter > 0 && actor_shell.isAutoMove
					&& m_curState != GST_TRAILER_RUN) {
				actor_shell.updateAnimation();
				actor_shell.updateAutoMove();
				if (!Script.isActorAutoMove()) {
					if (Script.objScriptRun != null
							&& Script.objScriptRun.isInScriptRunning) {
						setGameState(GST_SCRIPT_RUN);
						return;
					}
				}
			}
			// 改变角色动作
			else if (Script.autoshowActionCouner > 0
					&& actor_shell.isAutoAction
					&& m_curState != GST_TRAILER_RUN) {
				actor_shell.updateAutoAction();
				if (!Script.isActorAutoAction()) {
					if (Script.objScriptRun != null
							&& Script.objScriptRun.isInScriptRunning) {
						setGameState(GST_SCRIPT_RUN);
						return;
					}
				}
			}
			// 游戏正常逻辑
			else {
				if (!CEnemy.bAllEnemyStop || !(actor_shell instanceof CEnemy)) {
					// 残影
					if (actor_shell == m_hero && m_hero.shadowTick-- > 0) {
						m_hero.pushShadow();
					}
					actor_shell.updateAnimation();
				}

				// 如果有脚本执行 则后面逻辑都不操作
				/*************
				 * 检测无敌时间
				 ***********/
				if (m_curState == GST_CAMARE_MOVE) {
					if (actor_shell != CGame.m_hero) {
						actor_shell.checkInvincible();
						actor_shell.update();
					}
				} else {
					// 如果其他对象处于自动移动 或者 自动播放状态 主角不执行逻辑
					if (Script.autoshowActionCouner > 0
							|| Script.autoMoveActorCounter > 0) {
						if (actor_shell == CGame.m_hero) {
							return;
						}
					}

					actor_shell.checkInvincible();
					actor_shell.update();

					if (m_curState == dGame.GST_SCRIPT_RUN)
						return;

					if (Camera.cammeraObj != null) {
						Camera.cammeraObj.updateCamera();
					} else {
						if (CGame.m_hero == actor_shell) {
							CGame.m_hero.updateCamera();
						}
					}

				}

			}
			/**
			 * debug 显示更新一个AI逻辑所需要的时间
			 */
			// CDebug._showUpdateActorAIWasteTimer (actor_shell);
			shell_id = nextUpdateShellID;
		}

		// if(CDebug.bEnableTraceBasicInfo) {
		// CDebug.m_updateActorTime = System.currentTimeMillis () -
		// CDebug.m_updateActorTime;
		// System.out.println (m_levelTicker + " : update actors " +
		// CDebug.m_updateActorTime + " ms. ");
		// }

	}

	public static final int REGION_SIZE_BITS = 8;

	public static long getRegionFlags(short[] box) {

		long flag = 0;

		int max_col = (gMap.mapRes.m_mapTotalWidthByPixel - 1) >> REGION_SIZE_BITS;
		int max_row = (gMap.mapRes.m_mapTotalHeightByPixel - 1) >> REGION_SIZE_BITS;

		int end_col = box[2] >> REGION_SIZE_BITS;
		if (end_col > max_col) {
			end_col = max_col;
		}

		int end_row = box[3] >> REGION_SIZE_BITS;
		if (end_row > max_row) {
			end_row = max_row;
		}

		int row = box[1] >> REGION_SIZE_BITS;
		if (row < 0) {
			row = 0;
		}

		int start_col = box[0] >> REGION_SIZE_BITS;
		if (start_col < 0) {
			start_col = 0;
		}

		int num_cols = max_col + 1;
		long region_flag = 1L << (row * num_cols);

		for (; row <= end_row; ++row) {
			for (int col = start_col; col <= end_col; ++col) {
				flag |= (region_flag << col);
			}

			region_flag <<= num_cols;
		}

		return flag;
	}

	/**
	 * 取得一个指定Actor的碰撞区域
	 * 
	 * @param actorID
	 *            场景中的哪 一个actor
	 * @param boxData
	 *            short[]
	 */
	public static short[] getActorActivateBoxInfo(int actorID, short[] boxData) {
		System.arraycopy(gData.actorsBasicInfo,
				gData.actorsBasicInfoOffset[actorID]
						+ (dActor.INDEX_ACTIIVE_BOX_LEFT), boxData, 0, 4);
		return boxData;
	}

	/*************************************
	 * trailer 代码
	 ************************************/
	public static int currentTrailerIndex; // 当前trailer的索引
	public static int currentTrailerFrame; // 关键帧的索引
	public static int currentTrailerFrameX3;
	public static int currentTrailerCameraActorID;
	public static CObject currentTrailerCamera;

	final static byte TYPE_CAMARE_AUTO = 0;
	final static byte TYPE_CAMARE_FALLOWHERO = 1;
	final static byte TYPE_CAMARE_NOLOCK = 2;

	// will be removed, maybe
	/**
	 * 从指定的trailerCamera 中取得　cameraID
	 * 
	 * @param cameraID
	 *            int
	 * @return int
	 */
	public static int getTrailerIDByCameraID(int cameraID) {
		for (int trailer_id = 0; trailer_id < gData.nTrailersCount; ++trailer_id) {
			for (int timeline_id = 0; timeline_id < gData.nTrailersTimeLinesCount[trailer_id]; ++timeline_id) {
				if (gData.nTrailersTimeLinesActorID[trailer_id][timeline_id] == cameraID) {
					return trailer_id;
				}
			}
		}

		return -1;
	}

	/**
	 * 初始化trailer
	 */
	public static void initCurrentTrailer() {
		int shell_id = activeActorsListHead;
		while (shell_id >= 0) {
			m_actorShells[shell_id].timelineIndex = -1;
			shell_id = nextActorShellID[shell_id];
		}

		currentTrailerFrame = 0;
		currentTrailerFrameX3 = 0;

		for (int timeline_id = 0; timeline_id < gData.nTrailersTimeLinesCount[currentTrailerIndex]; ++timeline_id) {
			// 激活当前时间线上的Actor
			int actor_id = gData.nTrailersTimeLinesActorID[currentTrailerIndex][timeline_id];
			CObject actor = tryActivateActor(actor_id);
			actor.timelineIndex = timeline_id; // 取得这个Actor是时间线的索引
			actor.currentKeyFrameIndex = 0;
			actor.updateTrailer();
		}
		// 注释
		// CGame.currentStringDate = null;
		if (currentTrailerCamera
				.getActorInfo(dActorClass.Index_Param_TRAILERCAMERA_TYPE) == TYPE_CAMARE_AUTO
				|| currentTrailerCamera
						.getActorInfo(dActorClass.Index_Param_TRAILERCAMERA_TYPE) == TYPE_CAMARE_FALLOWHERO) {
			GameEffect.setCover(GameEffect.COVER_NORMAL);
		}
		// 结束注释
	}

	/**
	 * 停止trailer
	 */

	public static void stopCurrentTrailer() {
		currentTrailerIndex = -1;
		currentTrailerFrame = -1;
		currentTrailerFrameX3 = -1;
		// 防止再一次触发trailer
		int id = actorsShellID[currentTrailerCameraActorID];
		if (id >= 0) {
			m_actorShells[id].die(false);
		}
		currentTrailerCameraActorID = -1;
		currentTrailerCamera = null;
		GameEffect.setCover(GameEffect.COVER_NON);

		// CGame.m_currentStringDate = null;

		// 主角的一些特殊处理
		if (m_hero.timelineIndex >= 0) {
			m_hero.droptoGround();
			m_hero.setState(m_hero.ST_ACTOR_WAIT, -1, true);
			m_hero.m_flags &= dActor.MASK_BASIC_FLAGS;
			m_hero.setFlag(dActor.FLAG_ACTION_GRAB_MECH_INFO);
		}
	}

	/**
	 * 跳过 trailer
	 */
	public static void skipCurrentTrailer() {
		/** 跳过 trailer */
		while (currentTrailerIndex >= 0 && currentTrailerFrame >= 0) {
			updateEngineLogic();
		}

	}

	/**
	 * 按键跳过trailer
	 * 
	 * @param isTrailer
	 *            boolean
	 */
	public static void pressSkipCurrentTrailer(boolean isTrailer) {
		if (currentTrailerCamera != null
				&& currentTrailerCamera
						.getActorInfo(dActorClass.Index_Param_TRAILERCAMERA_TYPE) == 0
				&& isTrailer) {
			// 绘制trailer
			if (CKey.isKeyPressed(CKey.GK_SOFT_LEFT
			/* CKey.GK_RIGHT_SOFT | CKey.GK_M */)) {
				// skip trailer
				skipCurrentTrailer();
			}
		}

	}

	public static void runTrailerLogic() {
		if (currentTrailerFrame < 0) {
			initCurrentTrailer();
		} else {

			if (GameEffect.useSlowMotion) {
				++currentTrailerFrameX3;
				if ((currentTrailerFrameX3 % 3) == 0) {
					++currentTrailerFrame;
				}
			} else {
				currentTrailerFrameX3 += 3;
				++currentTrailerFrame;
			}

			// 如果当前帧指针大于整个trailer最长的帧长度的时候停止trailer
			if (currentTrailerFrame >= gData.nTrailersDuration[currentTrailerIndex]) {
				stopCurrentTrailer();
				if (m_curState == GST_TRAILER_RUN) {
					setGameState(m_preState);
				}
			} else {
				// 如果镜头类型为2的时候镜头跟随主角
				if (currentTrailerCamera
						.getActorInfo(dActorClass.Index_Param_TRAILERCAMERA_TYPE) == TYPE_CAMARE_NOLOCK
						|| currentTrailerCamera
								.getActorInfo(dActorClass.Index_Param_TRAILERCAMERA_TYPE) == TYPE_CAMARE_FALLOWHERO) {
					Camera.updateCamera(true);
					if (currentTrailerCamera
							.getActorInfo(dActorClass.Index_Param_TRAILERCAMERA_TYPE) == CGame.TYPE_CAMARE_FALLOWHERO) {
						m_hero.updateCamera();
					}
				} else { // 否则把镜头中心点设置在TrailerCamera的位置
					Camera.cameraCenterX = currentTrailerCamera.m_x >> dConfig.FRACTION_BITS;
					Camera.cameraCenterY = currentTrailerCamera.m_y >> dConfig.FRACTION_BITS;
					if (currentTrailerCamera.currentKeyFrameIndex == 0) {
						Camera.updateCamera(true); // false);
					} else {
						Camera.updateCamera(false);
					}
				}
			}
		}
	}

	/**
	 * 设置脚本载入地图的前期初始化
	 */
	private static int nextHeroX; // 切换场景后主角的位置坐标
	private static int nextHeroY; // 切换场景后主角的位置坐标
	private static int nextHeroDir; // 切换场景后主角的方向
	private static boolean isScript;

	public static void initLoad_Common(int newLevel, int x, int y, int dir,
			boolean script) {
		nextHeroX = x;
		nextHeroY = y;
		nextHeroDir = dir;
		isScript = script;
		backupActorInfo();
		// 固定不变
		addActorInfo2hs(curLevelID); // 保存此场景中的对象

		loadType = LOAD_TYPE_COMMON_LOAD;
		setLoadInfo(newLevel);
	}

	/**
	 * 设置切换场景的信息
	 * 
	 * @param newLevel
	 *            int
	 */
	public static void setLoadInfo(int newLevel) {
		oldLevelID = curLevelID;
		curLevelID = newLevel;
		setGameState(GST_GAME_LOAD);
	}

	private static final void backupActorInfo() {
		CObject obj;
		int shell_id = activeActorsListHead;
		while (shell_id >= 0) {
			obj = m_actorShells[shell_id];
			if (obj != null
					&& obj.testFlag(dActor.FLAG_BASIC_IS_SAVE
							| dActor.FLAG_BASIC_ALWAYS_ACTIVE)) {
				obj.setActorInfo(obj.m_actorID, dActor.INDEX_BASIC_FLAGS,
						obj.m_flags);
				obj.setActorInfo(obj.m_actorID, dActor.INDEX_POSITION_X,
						obj.m_x >> dConfig.FRACTION_BITS);
				obj.setActorInfo(obj.m_actorID, dActor.INDEX_POSITION_Y,
						obj.m_y >> dConfig.FRACTION_BITS);
				obj.setActorInfo(obj.m_actorID, dActor.INDEX_ACTION_ID,
						obj.m_actionID);
				obj.setActorInfo(obj.m_actorID, dActor.INDEX_STATE,
						obj.m_currentState);
			}
			shell_id = nextActorShellID[shell_id];
		}

	}

	public static final int TYPE_SAVE_INFO_ACTOR = 0;
	public static final int TYPE_SAVE_INFO_MAP = 1;
	public static final int TYPE_SAVE_INFO_other = 2;

	public static Hashtable hsSaveInfo = new Hashtable();

	/**
	 * 将指定场景的所有对象需要保存的对象的特定信息存入到hashtable中
	 * 
	 * @param mapID
	 *            int 需要保存角色的信息的对象 必须被定义未全局激活对象
	 */
	public static final void addActorInfo2hs(int mapID) {
		CObject obj = null;
		short[] pValue;
		byte[][] pIndex;
		// 遍历场景中的对象,不管是否激活
		// int shell_id = CGame.m_activeActorsListHead;
		// while(shell_id >= 0){
		// obj = CGame.m_actorShells[shell_id];
		for (int actor_id = gData.nActorsCount - 1; actor_id >= 0; --actor_id) {
			int basic_flag = gData.actorsBasicInfo[(dActor.INDEX_BASIC_FLAGS)
					+ gData.actorsBasicInfoOffset[actor_id]];
			boolean needSave = ((basic_flag & (dActor.FLAG_BASIC_ALWAYS_ACTIVE & dActor.MASK_BASIC_FLAGS)) == (dActor.FLAG_BASIC_ALWAYS_ACTIVE & dActor.MASK_BASIC_FLAGS))
					&& ((basic_flag & (dActor.FLAG_BASIC_IS_SAVE & dActor.MASK_BASIC_FLAGS)) == (dActor.FLAG_BASIC_IS_SAVE & dActor.MASK_BASIC_FLAGS));

			// 需要保存信息的对象必须是全局激活
			if (needSave) {
				pIndex = CObject.getSaveInfo();
				if (actorsShellID[actor_id] >= 0) {
					obj = CGame.m_actorShells[actorsShellID[actor_id]];
				}
				// if (obj==null) {
				// continue;
				// }
				if (pIndex != null) {
					if (CDebug.showDebugInfo) {
						if (pIndex.length != 2) {
							System.out.println("ERROR: "
									+ ", 获得保存信息出错！约定返回为2维数组");
							continue;
						}
					}
					int bLen = pIndex[0] == null ? 0 : pIndex[0].length;
					int pLen = pIndex[1] == null ? 0 : pIndex[1].length;
					pValue = new short[bLen + pLen];
					int vi = 0;
					if (pIndex[0] != null) {
						for (int j = 0; j < bLen; j++) {
							pValue[vi++] // = obj.getActorInfo(pIndex[0][j])
							= gData.actorsBasicInfo[(pIndex[0][j] & dActor.MASK_ACTOR_INFO_INDEX)
									+ gData.actorsBasicInfoOffset[actor_id]];
						}
					}
					// if(pIndex[1] != null&&obj!=null){
					// for(int j = 0; j < pLen; j++){
					// pValue[vi++] = obj.m_actorProperty[pIndex[1][j]];
					// }
					// }
					addSaveInfo2hs(TYPE_SAVE_INFO_ACTOR, mapID, actor_id,
							pValue);
				}
			}
			// shell_id = CGame.m_nextActorShellID[shell_id];
			// }
		}

	}

	/**
	 * 获得存贮的信息的key
	 * 
	 * @param type
	 *            int
	 * @param k0
	 *            int
	 * @param k1
	 *            int
	 * @return int
	 */
	public static final int getHsSaveInfoKey(int type, int k0, int k1) {
		return ((type & 0x03) << 30) | ((k0 & 0x3f) << 24) | (k1 & 0xffffff);
	}

	/**
	 * 把指定信息存入hashtable中
	 * 
	 * @param type
	 *            int
	 * @param k0
	 *            int
	 * @param k1
	 *            int
	 * @param value
	 *            short[]
	 */
	public static final void addSaveInfo2hs(int type, int k0, int k1,
			short[] value) {
		hsSaveInfo.put(String.valueOf(getHsSaveInfoKey(type, k0, k1)), value);
	}

	/**
	 * 用保存到hashtable的信息恢复指定场景内容
	 * 
	 * @param mapID
	 *            int
	 */
	private static final void modifySceneBySaveInfo(int mapID) {
		CObject obj;
		String str;
		int type, mID, k1;
		short[] value;
		int t;
		for (Enumeration e = hsSaveInfo.keys(); e.hasMoreElements();) {
			str = (String) e.nextElement();
			t = Integer.parseInt((String) str);
			mID = (t >> 24) & 0x3f;
			if (mapID == mID) {
				type = (t >> 30) & 0x03;
				k1 = t & 0xffffff;
				value = (short[]) hsSaveInfo.get(str);
				// 人物
				switch (type) {
				case TYPE_SAVE_INFO_ACTOR:
					if (actorsShellID[k1] < 0) {
						System.out.println("永久性对象没有全局激活，actorId = " + k1);
						return;
					}

					obj = CGame.m_actorShells[actorsShellID[k1]];
					if (obj != null
							&& obj.testFlag(dActor.FLAG_BASIC_IS_SAVE
									| dActor.FLAG_BASIC_ALWAYS_ACTIVE)) {
						byte[][] pIndex = obj.getSaveInfo();
						if (pIndex != null) {
							int vi = 0;
							int bLen;
							if (pIndex[0] != null) {
								bLen = pIndex[0].length;
								for (int j = 0; j < bLen; j++) {
									// 将保存的数据写回到basicinfo中
									int val = value[vi++];
									CObject.setActorInfo(obj.m_actorID,
											pIndex[0][j], val);
									if (pIndex[0][j] == dActor.INDEX_BASIC_FLAGS) {
										obj.m_flags = val;
									} else if (pIndex[0][j] == dActor.INDEX_POSITION_X) {
										obj.m_x = val << dConfig.FRACTION_BITS;
									} else if (pIndex[0][j] == dActor.INDEX_POSITION_Y) {
										obj.m_y = val << dConfig.FRACTION_BITS;
									} else if (pIndex[0][j] == dActor.INDEX_ACTION_ID) {
										obj.m_actionID = val;
										obj.setAnimAction(obj.m_actionID);
									} else if (pIndex[0][j] == dActor.INDEX_STATE) {
										obj.m_currentState = val;
										obj.setState(obj.m_currentState);
									}

								}
							}

							// 对已经死亡的人清除
							if (obj.testFlag(dActor.FLAG_BASIC_DIE)) {
								obj.die(false);
								// objList[k1] = null;
								continue;
							}
							if (pIndex[1] != null) {
								bLen = pIndex[1].length;
								for (int j = 0; j < bLen; j++) {
									obj.m_actorProperty[pIndex[1][j]] = value[vi++];
								}
							}
							System.out.println("更改对象，actorID = "
									+ obj.m_actorID);
						}
					}

					break;
				// case TYPE_SAVE_INFO_MAP:
				//
				// int gx = (k1 >> 12) & 0xfff;
				// int gy = (k1) & 0xfff;
				// gMap.mapRes.modifyMapLayer (value[0], gx, gy, value[1]);
				// break;
				}
			}
		}
	}

	// ---------------------------------------------------
	// public final static byte IMAGE_FACE = 0;
	// public final static byte IMAGE_MONEY = 1;
	// public final static byte IMAGE_HP = 2;
	// public final static byte IMAGE_LEVEL = 3;
	// public final static byte IMAGE_SHOP = 4;
	// public final static byte IMAGE_MENU = 5;
	public final static byte IMAGE_HP = 0;
	public final static byte IMAGE_MP = 1;
	public final static byte IMAGE_XP = 2;
	public final static byte IMAGE_UI = 3;

	public final static byte IMG_LENGTH = 4;

	public static Image[] UI_Image;

	public static Image[] border_Image;

	public static Image[] number_Image;

	// 加载游戏中UI图片
	public static void loadInterfaceImage() {
		if (UI_Image == null) {
			UI_Image = new Image[IMG_LENGTH];
		}
		// if(UI_Image[IMAGE_FACE] == null) {
		// UI_Image[IMAGE_FACE] = CTools.loadImage ("face");
		// }
		//
		// if(UI_Image[IMAGE_MONEY] == null) {
		// UI_Image[IMAGE_MONEY] = CTools.loadImage ("money");
		// }
		//
		// if(UI_Image[IMAGE_HP] == null) {
		// UI_Image[IMAGE_HP] = CTools.loadImage ("hp");
		// }
		//
		// if(UI_Image[IMAGE_LEVEL] == null) {
		// UI_Image[IMAGE_LEVEL] = CTools.loadImage ("level");
		// }
		//
		// if(UI_Image[IMAGE_SHOP] == null) {
		// UI_Image[IMAGE_SHOP] = CTools.loadImage ("shop");
		// }
		//
		// if(UI_Image[IMAGE_MENU] == null) {
		// UI_Image[IMAGE_MENU] = CTools.loadImage ("menu");
		// }
		if (UI_Image[IMAGE_HP] == null) {
			UI_Image[IMAGE_HP] = CTools.loadImage("001");
		}
		if (UI_Image[IMAGE_MP] == null) {
			UI_Image[IMAGE_MP] = CTools.loadImage("002");
		}
		if (UI_Image[IMAGE_XP] == null) {
			UI_Image[IMAGE_XP] = CTools.loadImage("003");
		}
		if (UI_Image[IMAGE_UI] == null) {
			UI_Image[IMAGE_UI] = CTools.loadImage("ui");
		}

		if (border_Image == null) {
			border_Image = new Image[4];
		}

		if (border_Image[0] == null) {
			border_Image[0] = CTools.loadImage("border");
			int w = border_Image[0].getWidth();
			int h = border_Image[0].getHeight();
			border_Image[1] = border_Image[0].createImage(border_Image[0], 0,
					0, w, h, Sprite.TRANS_ROT270);
			border_Image[2] = border_Image[0].createImage(border_Image[0], 0,
					0, w, h, Sprite.TRANS_ROT90);
			border_Image[3] = border_Image[0].createImage(border_Image[0], 0,
					0, w, h, Sprite.TRANS_MIRROR_ROT90);
		}

		if (number_Image == null) {
			number_Image = new Image[2];
		}

		if (number_Image[0] == null) {
			number_Image[0] = CTools.loadImage("num1");
		}
		if (number_Image[1] == null) {
			number_Image[1] = CTools.loadImage("num2");
		}
	}

	// 释放游戏中UI图片
	public static void releaseInterfaceImage() {
		if (UI_Image == null) {
			return;
		}

		for (int i = 0; i < UI_Image.length; i++) {
			if (UI_Image[i] != null) {
				UI_Image[i] = null;
			}
		}
	}

	public static void updateInterface(Graphics g) {
		CHero hero = CGame.m_hero;
		int drawPosX = 29;
		int height = 3;
		if (UI_Image[IMAGE_UI] != null) {
			int h = UI_Image[IMAGE_UI].getHeight();
			int drawPosY = 7;
			int drawPosY1 = 12;
			int drawPosY2 = 17;
			int length_hp = 54;
			int length_mp = 54;
			int length_exp = 54;

			g.drawImage(UI_Image[IMAGE_UI], 0, 0, dConfig.ANCHOR_LT);
			// g.drawImage (UI_Image[IMAGE_LEVEL], 0, h, dConfig.ANCHOR_LT);
			// g.setColor(dConfig.COLOR_HP);
			// g.fillRect(drawPosX,drawPosY,length_hp*hero.m_actorProperty[CObject.PRO_INDEX_HP]/hero.m_actorProperty[CObject.PRO_INDEX_MAX_HP],height);
			g.setClip(drawPosX, drawPosY, length_hp
					* hero.m_actorProperty[CObject.PRO_INDEX_HP]
					/ hero.m_actorProperty[CObject.PRO_INDEX_MAX_HP], height);
			g.drawImage(UI_Image[IMAGE_HP], drawPosX, drawPosY, 0);
			// g.setColor(dConfig.COLOR_MP);
			g.setClip(drawPosX, drawPosY1, length_mp
					* hero.m_actorProperty[CObject.PRO_INDEX_MP]
					/ hero.m_actorProperty[CObject.PRO_INDEX_MAX_MP], height);
			g.drawImage(UI_Image[IMAGE_MP], drawPosX, drawPosY1, 0);
			// g.setColor(dConfig.COLOR_EXP);
			int explen = length_exp
					* hero.m_actorProperty[CObject.PRO_INDEX_EXP]
					/ hero.m_actorProperty[CObject.PRO_INDEX_NEXT_EXP];
			explen = Math.min(length_exp, explen);
			g.setClip(drawPosX, drawPosY2, explen, height);
			g.drawImage(UI_Image[IMAGE_XP], drawPosX, drawPosY2, 0);
			// int w = CGame.number_Image[0].getWidth () / 10;
			// int imgh = CGame.number_Image[0].getHeight();
			// int halfW = (String.valueOf
			// (hero.m_actorProperty[CObject.PRO_INDEX_LEVEL]).length () * w) >>
			// 1; //数字宽度是8
			// CTools.drawImageNumber (g, CGame.number_Image[0],
			// String.valueOf (hero.m_actorProperty[CObject.PRO_INDEX_LEVEL])
			// , new int[] {0, 26
			// , 20, halfW << 1, imgh});
			g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
			CTools.afficheSmall(g, cGame.m_hero.strLevel, 115, 4, 0,
					dConfig.COLOR_HERO_LEVEL, dConfig.COLOR_HERO_LEVEL_OUT);
		}

		// if(UI_Image[IMAGE_HP] != null) {
		//
		// int length = 60;
		// int w = UI_Image[IMAGE_HP].getWidth ();
		// int h = UI_Image[IMAGE_HP].getHeight ();
		// g.drawImage (UI_Image[IMAGE_HP], dConfig.S_WIDTH - w, 0,
		// dConfig.ANCHOR_LT);
		// g.drawImage (UI_Image[IMAGE_MONEY], dConfig.S_WIDTH - w, h,
		// dConfig.ANCHOR_LT);
		//
		// }

		// if(UI_Image[IMAGE_SHOP] != null) {
		// int shopw = UI_Image[IMAGE_SHOP].getWidth ();
		// int shoph = UI_Image[IMAGE_SHOP].getHeight ();
		// g.drawImage (UI_Image[IMAGE_SHOP], dConfig.S_WIDTH - shopw,
		// dConfig.S_HEIGHT - shoph, dConfig.ANCHOR_LT);
		// }
		//
		// if(UI_Image[IMAGE_MENU] != null) {
		// int menuh = UI_Image[IMAGE_MENU].getHeight ();
		// g.drawImage (UI_Image[IMAGE_MENU], 0, dConfig.S_HEIGHT - menuh,
		// dConfig.ANCHOR_LT);
		// }

		// 触摸屏,操作主角放大招的按钮
		// #if touch_screen
		// #
		// # if
		// (Script.systemVariates[Script.SV_INDEX_SCRIPT_CAN_CHANGE_HERO]==1) {
		// # g.setClip(160,1,72,25);
		// # }
		// # else{
		// # g.setClip(160,1,49,25);
		// # }
		// #
		// # if (m_hero.m_actorProperty[CHero.PRO_INDEX_ROLE_ID]==0) {
		// # if(touchImg1!=null){
		// # g.drawImage(touchImg1,160,2,dConfig.ANCHOR_LT);
		// # }
		// # }
		// # else{
		// # if(touchImg2!=null){
		// # g.drawImage(touchImg2,160,2,dConfig.ANCHOR_LT);
		// # }
		// # }
		// #
		// # // g.setColor(dConfig.COLOR_SCENE_NAME);
		// # // g.drawRect(160,1,24,24);
		// # //
		// CTools.afficheSmall(g,"7",160,1,0,dConfig.COLOR_SCENE_NAME,dConfig.COLOR_SCENE_NAME_OUT);
		// # //
		// # // g.drawRect(160+24,1,24,24);
		// # //
		// CTools.afficheSmall(g,"9",160+24,1,0,dConfig.COLOR_SCENE_NAME,dConfig.COLOR_SCENE_NAME_OUT);
		// # //
		// # // g.drawRect(160+24+24,1,24,24);
		// # //
		// CTools.afficheSmall(g,"0",160+48,1,0,dConfig.COLOR_SCENE_NAME,dConfig.COLOR_SCENE_NAME_OUT);
		// #endif
	}

	// ---------------------------------------------------
	/**
	 * 新游戏，初始化一些全局信息 shubinl
	 */
	public static void initGameSystemInf() {
		// 脚本的初始化:系统变量,任务计数器等
		for (int i = 0; i < Script.systemVariates.length; i++) {
			Script.systemVariates[i] = 0;
		}
		// 任务
		for (int i = 0; i < Script.systemTasks.length; i++) {
			Script.systemTasks[i] = 0;
		}
		// 全局对象
		hsSaveInfo.clear();
		// 主角的备份
		Record.savedSkills[0] = Goods.createGoods((short) 2,
				(short) Data.ROLE_INFO[1][Data.IDX_RI_SKILL0]);
		Record.savedSkills[1] = Goods.createGoods((short) 2,
				(short) Data.ROLE_INFO[1][Data.IDX_RI_SKILL1]);

	}

	// FXQ
	static Image uiImage;
	static int counter = 0;
	static final int[] block = { 29, 213, 176, 76 };

	private static void exit_GAME_UI() {
		uiImage = null;
		// #if !W958C
		System.gc();
		// #endif

	}

	private static void do_GAME_UI() {
		GameUI.doLogic();
	}

	private static void draw_GAME_UI(Graphics g) {

		GameUI.paint(g);

	}

	private static int curTaskId;

	private static void init_GAME_UI() {

	}

	protected void showNotify() {
		if (m_curState != GST_GAME_MENU) {
			SoundPlayer.playSingleSound(SoundPlayer.getCurMidiID(), -1);
		}
	}

	public void hideNotify() {
		if (SoundPlayer.m_isMusicOn) {
			// if (m_curState!=GST_MAIN_MENU
			// &&m_curState!=GST_GAME_LOAD
			// &&m_curState!=GST_GAME_EXIT
			// &&m_curState!=GST_GAME_HELP
			// &&m_curState!=GST_GAME_ABOUT) {
			SoundPlayer.stopSingleSound();
			// }
		}

		if (CGame.m_curState == CGame.GST_GAME_RUN) {
			CGame.setGameState(CGame.GST_GAME_MENU);
		}
	}

	/***********************************************************************
	 * 触摸屏
	 ***********************************************************************/
	public static int pointerX, pointerY; // pointer 按下或松开的坐标

	public void pointerPressed(int x, int y) {
		pointerX = x;
		pointerY = y;
		// if(MessageCharge.isMessageState){
		// MessageCharge.doPointer(x,y);
		// }
		// else
		if (m_curState == GST_GAME_UI) {
			GameUI.doPointer(x, y);
		} else if (m_curState == GST_GAME_RUN || m_curState == GST_SCRIPT_RUN
				|| m_curState == GST_SCRIPT_DIALOG
				|| m_curState == GST_SCRIPT_OPDLG) {
			m_hero.doPointer(x, y);
		} else {
			this.doPointer(x, y);
		}
	}

	/**
	 * pointer released event handler
	 * 
	 * @param x
	 *            int
	 * @param y
	 *            int
	 */
	public void pointerReleased(int x, int y) {
		pointerX = x;
		pointerY = y;
		CKey.initKey();
	}

	public static final int PT_X = 0;
	public static final int PT_Y = 1;
	public static final int PT_W = 2;
	public static final int PT_H = 3;
	public static final int PT_INDEX = 4;
	/**
	 * 触摸屏区域 {pointForm,x,y,w,h,index}
	 */
	private static final short[][] POINT_RECT = {
			// 左下角，右下角
			{ 0, 295, 40, 320, 0 }, { 200, 295, 240, 320, 0 },
			// 主菜单[2,3]
			{ 35, 280, 85, 310, 0 }, { 80, 280, 158, 310, 0 },
			{ 156, 280, 197, 310, 0 },
			// 音乐设置[5,6]
			{ 100, 106, 139, 123, 0 }, { 98, 130, 142, 143, 1 },
			// 帮助[7,8]
			{ 85, 296, 120, 311, 0 }, { 123, 297, 150, 314, 1 },
			// 弹出框【9，10】
			{ 11, 94, 42, 210, 0 }, { 196, 94, 230, 210, 0 },
	// 暂停菜单【11，14】
	// {80,105,150,125, 0},
	// {80,125,150,145, 1},
	// {80,145,150,165, 2},
	// {80,165,150,185, 3},
	};

	// 暂停菜单【11，14】
	private static final short[][] POINT_RECT_GAME_MENU = {
			{ 80, 105, 150, 125, 0 }, { 80, 125, 150, 145, 1 },
			{ 80, 145, 150, 165, 2 }, { 80, 165, 150, 185, 3 }, };

	/**
	 * 点是否在触摸区域
	 * 
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param pointRect
	 *            short[]
	 * @return boolean
	 */
	public static boolean isPointInRect(int x, int y, short[] pointRect) {
		short[] box = new short[] { pointRect[PT_X], pointRect[PT_Y],
				pointRect[PT_W], pointRect[PT_H] };
		return CTools.isPointerInBox(x, y, box);
	}

	/**
	 * 触摸屏逻辑
	 * 
	 * @param x
	 *            int
	 * @param y
	 *            int
	 */
	static void doPointer(int x, int y) {
		if (noRecord) {
			cGame.keyPressed(CKey.KEY_MID);
			return;
		}

		if (sureNewGame) {
			if (isPointInRect(x, y, POINT_RECT[9])) {
				cGame.keyPressed(CKey.KEY_SOFT_LEFT);
			} else if (isPointInRect(x, y, POINT_RECT[10])) {
				cGame.keyPressed(CKey.KEY_SOFT_RIGHT);
			}
			return;
		}

		// 左右软键
		if (m_curState == GST_GAME_IF_MUSIC || m_curState == GST_GAME_ABOUT
				|| m_curState == GST_GAME_HELP || m_curState == GST_GAME_OPTION
				|| m_curState == GST_GAME_EXIT) {
			if (isPointInRect(x, y, POINT_RECT[0])) {
				cGame.keyPressed(CKey.KEY_SOFT_LEFT);
			} else if (isPointInRect(x, y, POINT_RECT[1])) {
				cGame.keyPressed(CKey.KEY_SOFT_RIGHT);
			}
		}

		// 主菜单
		if (m_curState == GST_MAIN_MENU) {
			if (isPointInRect(x, y, POINT_RECT[2])) {
				cGame.keyPressed(CKey.KEY_UP);
			} else if (isPointInRect(x, y, POINT_RECT[3])) {
				cGame.keyPressed(CKey.KEY_MID);
			} else if (isPointInRect(x, y, POINT_RECT[4])) {
				cGame.keyPressed(CKey.KEY_DOWN);
			}
		}
		// 音乐设置
		if (m_curState == GST_GAME_OPTION) {
			if (isPointInRect(x, y, POINT_RECT[5])) {
				if (m_curIndex == POINT_RECT[5][PT_INDEX]) {
					cGame.keyPressed(CKey.KEY_MID);
				} else {
					m_curIndex = POINT_RECT[5][PT_INDEX];
				}
			} else if (isPointInRect(x, y, POINT_RECT[6])) {
				if (m_curIndex == POINT_RECT[6][PT_INDEX]) {
					cGame.keyPressed(CKey.KEY_MID);
				} else {
					m_curIndex = POINT_RECT[6][PT_INDEX];
				}
			}
		}
		// 帮助
		if (m_curState == GST_GAME_HELP) {
			if (isPointInRect(x, y, POINT_RECT[7])) {
				cGame.keyPressed(CKey.KEY_LEFT);
			} else if (isPointInRect(x, y, POINT_RECT[8])) {
				cGame.keyPressed(CKey.KEY_RIGHT);
			}
		}
		// 暂停菜单
		if (m_curState == GST_GAME_MENU) {
			int pointIndex = GameUI.pointerInRectId(x, y, POINT_RECT_GAME_MENU);
			if (pointIndex >= 0) {
				if (m_curIndex == pointIndex) {
					cGame.keyPressed(CKey.KEY_MID);
				} else {
					m_curIndex = pointIndex;
				}
			}
		}

	}

}
