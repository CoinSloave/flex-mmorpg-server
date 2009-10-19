package game.pak;

import game.CDebug;
import game.CGame;
import game.CTools;
import game.config.dConfig;

import java.util.Random;

import javax.microedition.lcdui.Graphics;

/**
 * <p>
 * Title: engine of game
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
public class GameEffect {
	/**
	 * 绘制
	 */
	static int SNOWPIXELNUMBER = 1;
	static private int snowX[] = new int[SNOWPIXELNUMBER];
	static private int snowY[] = new int[SNOWPIXELNUMBER];
	static private int snowSpeedY[] = new int[SNOWPIXELNUMBER];
	static private int snowSpeedX[] = new int[SNOWPIXELNUMBER];
	static private byte snowBulk[] = new byte[SNOWPIXELNUMBER];
	static private int snow_wind; // wind direction
	static private int snow_windCount; // wind direction duration

	static private boolean isInitSnow = false; // whether snow pixel has been
												// inited
	static private boolean isInitRain = false; // whether rain pixel has been
												// inited
	static boolean isChangeSnow = true; // whether change the coordinate of snow
										// pixel
	static int frameCountOfChangeSnow = 1; // every "frame count" to change the
											// coordinate of snow pixel
	static int frameCircle = 0; // change snow tick

	static int baseNumberSpeedY = 10;
	static int baseNumberSpeedX = 3;
	static int baseNumberSnowWindDuration = 20;

	/**
	 * the method draw the snow effect
	 * 
	 * @param g
	 */
	/**
	 * the method draw the snow effect
	 * 
	 * @param g
	 */
	private static void drawSnow(Graphics g, int cameraLeft, int cameraTop,
			int cameraW, int cameraH) {
		Random r = new Random();
		int i;
		int rnd;
		if (!isInitSnow) {
			for (i = 0; i < SNOWPIXELNUMBER; i++) {
				snowX[i] = (r.nextInt() % 100) * cameraW / 100 + cameraLeft;
				snowY[i] = (r.nextInt() % 100) * cameraH / 100 + cameraTop;
				snowSpeedX[i] = (r.nextInt() % 2);
				snowBulk[i] = (byte) (r.nextInt() % 2);
			}
			isInitSnow = true;

			return;
		}
		g.setColor(255, 255, 255);

		if (isChangeSnow) {
			if (snow_windCount > 1) {
				snow_windCount -= 1;
			} else {
				snow_windCount = baseNumberSnowWindDuration + (r.nextInt() % 8);
				for (snow_wind = r.nextInt() % 2; snow_wind == 0; snow_wind = r
						.nextInt() % 2) {
					;
				}
			}
		}

		for (i = 0; i < SNOWPIXELNUMBER; i++) {
			if (isChangeSnow) {
				snowX[i] += snowSpeedX[i];
				snowY[i]++;
				snowX[i] = ((snowX[i] - cameraLeft) + cameraW) % cameraW
						+ cameraLeft;
				snowY[i] = ((snowY[i] - cameraTop) + cameraH) % cameraH
						+ cameraTop;

				// *
				if (snow_windCount <= 1) {
					rnd = r.nextInt() % 10;
					if (rnd < -6) {
						for (snowSpeedX[i] = r.nextInt() % 2; snowSpeedX[i] == 0; snowSpeedX[i] = r
								.nextInt() % 2) {
							;
						}
					} else {
						snowSpeedX[i] = snow_wind;
					}
				}
			}
			if (snowBulk[i] != 0) {
				// g.drawRect(snowX[i]-CGame.m_cameraLeft,snowY[i]-CGame.m_cameraTop,0,0);
				g.drawLine(snowX[i] - cameraLeft, snowY[i] - cameraTop,
						snowX[i] - cameraLeft, snowY[i] - cameraTop);
			} else {
				g.drawRect(snowX[i] - cameraLeft, snowY[i] - cameraTop, 1, 1);
			}

		}
	}

	static int RAINPIXELNUMBER = 30;

	static private void drawRain(Graphics g, int cameraLeft, int cameraTop,
			int cameraW, int cameraH) {
		Random r = new Random();
		int i;
		int rnd;
		if (!isInitRain) {
			for (i = 0; i < RAINPIXELNUMBER; i++) {
				// snowX[i]=(r.nextInt()%100)*Def.CAMERA_WIDTH/100+CGame.m_cameraLeft;
				snowX[i] = i * 8;
				snowY[i] = (r.nextInt() % 100) * cameraH / 100 + cameraTop;
				snowSpeedY[i] = baseNumberSpeedY + (r.nextInt() % 6);
				snowSpeedX[i] = Math.abs(baseNumberSpeedX + (r.nextInt() % 5));
				snowBulk[i] = (byte) (r.nextInt() % 6);
			}
			isInitRain = true;

			return;
		}
		// g.setColor(255,255,255);

		g.setColor(167, 167, 167);

		if (isChangeSnow) {
			if (snow_windCount > 1) {
				// snow_windCount-=1
				;
			} else {
				snow_windCount = baseNumberSnowWindDuration
						+ ((r.nextInt() % 80));
				snow_wind = Math.abs(((r.nextInt() % 5))); // wind direction
															// from -1 to 1,
			}
		}

		for (i = 0; i < RAINPIXELNUMBER; i++) {
			if (isChangeSnow) {
				// snowY[i]+=snowSpeedY[i];
				// *//
				if (snowBulk[i] == 0) {
					snowY[i] += 5;
					snowX[i] -= 3;
				} else { // */
					snowY[i] += 7;
					snowX[i] -= 5;
				}
				// if (snowY[i]>Def.CAMERA_HEIGHT) snowY[i]=0;
				snowY[i] = ((snowY[i] - cameraTop) + cameraH) % cameraH
						+ cameraTop;

				// snowX[i]-=(snowSpeedX[i]+snow_wind);

				// snowX[i]+=2;
				// if (snowX[i]>Def.CAMERA_WIDTH) snowX[i]=0;
				// if(snowX[i]<0)snowX[i]+=Def.CAMERA_WIDTH;
				snowX[i] = ((snowX[i] - cameraLeft) + cameraW) % cameraW
						+ cameraLeft;

				rnd = (r.nextInt() % 10); // base the random number to adjust
											// the snow x speed
				if (rnd > 8) {
					snowSpeedX[i] += 1;
				}
				if (rnd < 2) {
					snowSpeedX[i] -= 1;
				}
				if (snowSpeedX[i] < 0) {
					snowSpeedX[i] = 0;
				}
				if (snowSpeedX[i] > 5) {
					snowSpeedX[i] = 4;
				}
			}
			if (snowBulk[i] == 0) {
				// g.drawRect(snowX[i]-CGame.m_cameraLeft,snowY[i]-CGame.m_cameraTop,0,0);
				g.drawLine(snowX[i] - cameraLeft, snowY[i] - cameraTop,
						snowX[i] - cameraLeft - 1, snowY[i] - cameraTop + 1);
			} else {
				g.drawLine(snowX[i] - cameraLeft, snowY[i] - cameraTop,
						snowX[i] - cameraLeft - 7, snowY[i] - cameraTop + 13);
			}

		}
	}

	/****************************
	 * 关屏幕开屏幕的方法
	 ****************************/
	final static int COVER_TOP_HEIGHT = dConfig.S_HEIGHT / 5;
	final static int COVER_BOTTON_HEIGHT = dConfig.S_HEIGHT / 10;

	public static int m_currentCoverTick;
	public static int m_coverState;

	static int m_coverUpHeight; // 遮住屏幕上方的宽度
	static int m_coverDownHeight; // 遮住屏幕下方的宽度

	public static final int COVER_OPEN = 1;
	public static final int COVER_CLOSE = 2;
	public static final int COVER_NORMAL = 0;
	public static final int COVER_NON = 3;

	//
	public static final int COVER_PULL_DOWN = 4; // 下拉屏幕
	public static final int COVER_PULL_UP = 5; // 上拉屏幕
	public static final int COVER_ALL_SCREEN = 6;
	public static final int COVER_CHANGE_TICK = 15;

	/**
	 * 设置开关屏幕的装提
	 * 
	 * @param type
	 *            int
	 */
	public static void setCover(int type) {
		/**
		 * 开屏幕
		 */
		if (type == COVER_OPEN) {
			m_coverState = COVER_OPEN;
			m_currentCoverTick = COVER_CHANGE_TICK;
			prepareToRender(YES_RENDER, EFF_COVERSCREEN);
		}
		/**
		 * 关屏幕
		 */
		else if (type == COVER_CLOSE) {
			m_coverState = COVER_CLOSE;
			m_currentCoverTick = 0;
			prepareToRender(YES_RENDER, EFF_COVERSCREEN);
		}
		/**
		 * 下拉屏幕
		 */
		else if (type == COVER_PULL_DOWN) {
			m_coverUpHeight = 0;
			m_coverDownHeight = 0;
			m_coverState = COVER_PULL_DOWN;
			m_currentCoverTick = 0;
			prepareToRender(YES_RENDER, EFF_COVERSCREEN);
		}
		/**
		 * 上拉屏幕
		 */
		else if (type == COVER_PULL_UP) {
			m_coverUpHeight = COVER_TOP_HEIGHT;
			m_coverDownHeight = COVER_BOTTON_HEIGHT;
			m_coverState = COVER_PULL_UP;
			m_currentCoverTick = COVER_CHANGE_TICK;
			prepareToRender(YES_RENDER, EFF_COVERSCREEN);
		}

		/**
		 * 遮挡屏幕
		 */
		else if (type == COVER_NORMAL) {
			m_currentCoverTick = 0;
			m_coverUpHeight = COVER_TOP_HEIGHT;
			m_coverDownHeight = COVER_BOTTON_HEIGHT;
			m_coverState = COVER_NORMAL;
			prepareToRender(YES_RENDER, EFF_COVERSCREEN);
		}
		/**
		 * 取消遮挡屏幕效果
		 */
		else if (type == COVER_NON) {
			m_coverState = COVER_NON;
			m_coverUpHeight = 0;
			m_coverDownHeight = 0;
			m_currentCoverTick = 0;
			prepareToRender(NO_RENDER, EFF_COVERSCREEN);
			return;
		}
		/**
		 * 遮挡全屏幕
		 */
		else if (type == COVER_ALL_SCREEN) {
			CTools.fillBackGround(CGame.m_g, 0, 0, dConfig.S_HEIGHT);
			m_coverState = COVER_ALL_SCREEN;
			// m_coverUpHeight = m_coverDownHeight = dConfig.S_HEIGHT / 2;

		}
	}

	public static void drawcover(Graphics g) {
		int posTop, posBottom;
		if (m_coverState == COVER_OPEN) {
			--m_currentCoverTick;
			if (m_currentCoverTick < 0) {
				m_currentCoverTick = 0;
				setCover(COVER_NON);
			}
		} else if (m_coverState == COVER_CLOSE) {
			m_currentCoverTick++;
			if (m_currentCoverTick > COVER_CHANGE_TICK) {
				m_currentCoverTick = COVER_CHANGE_TICK;
				setCover(COVER_ALL_SCREEN);
			}

		} else if (m_coverState == COVER_PULL_DOWN) {
			m_currentCoverTick++;
			if (m_currentCoverTick > COVER_CHANGE_TICK) {
				m_currentCoverTick = COVER_CHANGE_TICK;
				setCover(COVER_NORMAL);
			}
		} else if (m_coverState == COVER_PULL_UP) {
			--m_currentCoverTick;
			if (m_currentCoverTick < 0) {
				m_currentCoverTick = 0;
				setCover(COVER_NON);
			}
		} else if (m_coverState == COVER_NORMAL) {
			m_currentCoverTick = 0;
		}

		if (m_coverState == COVER_ALL_SCREEN) {
			CTools.fillBackGround(CGame.m_g, 0, 0, dConfig.S_HEIGHT);

		}

		int tick = 0;
		if (m_coverState == COVER_CLOSE || m_coverState == COVER_OPEN
				|| m_coverState == COVER_NORMAL) {
			tick = (m_currentCoverTick > COVER_CHANGE_TICK ? COVER_CHANGE_TICK
					: m_currentCoverTick);
			posTop = m_coverUpHeight + tick
					* (dConfig.S_HEIGHT / 2 - m_coverUpHeight + 1)
					/ COVER_CHANGE_TICK;
			posBottom = m_coverDownHeight + tick
					* (dConfig.S_HEIGHT / 2 - m_coverDownHeight + 1)
					/ COVER_CHANGE_TICK;
		} else if (m_coverState == COVER_PULL_DOWN
				|| m_coverState == COVER_PULL_UP) {
			tick = m_currentCoverTick;
			posTop = tick * COVER_TOP_HEIGHT / COVER_CHANGE_TICK;
			posBottom = tick * COVER_BOTTON_HEIGHT / COVER_CHANGE_TICK;
		} else {
			posTop = posBottom = 0;
		}

		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		g.setColor(0);
		g.fillRect(0, 0, dConfig.S_WIDTH, posTop);
		g.fillRect(0, dConfig.S_HEIGHT - posBottom, dConfig.S_WIDTH, posBottom);

		// draw lines
		if (tick != COVER_CHANGE_TICK) {
			g.setColor(0x414141);
			g.drawLine(0, posTop, dConfig.S_WIDTH, posTop);
			g.drawLine(0, dConfig.S_HEIGHT - posBottom - 1, dConfig.S_WIDTH,
					dConfig.S_HEIGHT - posBottom - 1);

			g.setColor(0x6A6A6A);
			g.drawLine(0, posTop + 1, dConfig.S_WIDTH, posTop + 1);
			g.drawLine(0, dConfig.S_HEIGHT - posBottom - 2, dConfig.S_WIDTH,
					dConfig.S_HEIGHT - posBottom - 2);
		}
	}

	/*******************************
	 * 震动屏幕
	 ************************/
	private static int sysSSSwing;
	private static int sysSSTime; // 逻辑次数

	/**
	 * 设置振屏信息
	 * 
	 * @param ss
	 *            int
	 * @param st
	 *            int:是ms级
	 */
	public static void setSysShakeScreen(int ss, int st) {
		if (ss != 0) {
			sysSSSwing = ss;
		}
		if (st != 0) {
			sysSSTime = st / dConfig.FPS_RATE;
		}
	}

	/**
	 * 判断屏幕是否还在振动
	 * 
	 * @return boolean
	 */
	public static boolean isSysShaking() {
		return sysSSTime > 0;
	}

	public static void shakeCamera() {
		// 2.震屏
		if (sysSSTime > 0) {
			if ((sysSSTime & 1) == 0) {
				Camera.cameraLeft += sysSSSwing;
				Camera.cameraTop += sysSSSwing;
			} else {
				Camera.cameraLeft -= sysSSSwing;
				Camera.cameraTop -= sysSSSwing;
			}
			sysSSTime--;
		}

	}

	/****************************
	 * 绘制百叶窗效果
	 ***************************************/
	private static int m_shutterCounter = -1;
	private static byte m_shutterState;
	public final static byte SHUTTER_OPEN = 0;
	public final static byte SHUTTER_CLOSE = 1;

	public static void setShutter(byte type) {
		m_shutterState = type;
		prepareToRender(YES_RENDER, EFF_SHUTTER);
	}

	public final static int SHUTTER_W = 20;
	public final static int SHUTTER_X = (dConfig.S_WIDTH % SHUTTER_W) == 0 ? dConfig.S_WIDTH
			/ SHUTTER_W
			: (dConfig.S_WIDTH / SHUTTER_W) + 1;
	public final static int SHUTTER_T = 11;

	private static boolean drawShutter(Graphics g) {
		m_shutterCounter++;
		if (m_shutterCounter < 0 || m_shutterCounter >= SHUTTER_T) {
			prepareToRender(NO_RENDER, EFF_SHUTTER);
			m_shutterState = -1;
			return false;
		}
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		g.setColor(dConfig.COLOR_BLACK); // black
		int w = m_shutterState == SHUTTER_OPEN ? SHUTTER_T - m_shutterCounter
				: m_shutterCounter;
		for (int x = 0; x < SHUTTER_X; x++) {
			g.fillRect(x * SHUTTER_W, 0, w * 2, dConfig.S_HEIGHT);
		}
		return true;
	}

	/****************************
	 * 屏幕渐隐渐显效果
	 ************************************/

	public final static byte TYPE_FADE_OUT = 0;
	public final static byte TYPE_FADE_IN = 1;

	public static byte m_sysFadeType;
	public static int m_sysFadeColor;
	public static int m_sysWaitTime;
	public static int m_sysFadeSpeed;

	private static int m_tickTrans;
	private static int m_fadeColor;

	private static final int[] fx_1 = { 0, dConfig.CAMERA_WIDTH,
			dConfig.CAMERA_WIDTH, 0 };
	private static final int[] fy_1 = { 0, 0, dConfig.CAMERA_HEIGHT,
			dConfig.CAMERA_HEIGHT };

	public static void setScreenFade(byte type, int color, int tick) {
		if (type == TYPE_FADE_IN) {
			m_tickTrans = 0;
		} else {
			m_tickTrans = 0xFF;
		}
		m_sysFadeType = type;
		m_sysFadeColor = color;
		m_sysFadeSpeed = 0xff * dConfig.FPS_RATE / tick;
		prepareToRender(YES_RENDER, EFF_FADE_INOUT);
	}

	public static void drawScreenFade(Graphics g) {
		// 屏幕逐渐变暗
		if (m_sysFadeType == TYPE_FADE_IN) {
			m_tickTrans += m_sysFadeSpeed;
			if (m_tickTrans >= 0xFF) {
				m_tickTrans = 0xFF;
			}
		}
		// 屏幕逐渐变亮
		else {
			m_tickTrans -= m_sysFadeSpeed;
			if (m_tickTrans <= 0) {
				m_tickTrans = 0;
				prepareToRender(NO_RENDER, EFF_FADE_INOUT);
			}
		}

		int ARGB = m_tickTrans << 24 | m_fadeColor;

		g.setClip(0, 0, dConfig.CAMERA_WIDTH, dConfig.CAMERA_HEIGHT);

		// m_g.clipRect(0 , 0 , dConfig.CAMERA_WIDTH , dConfig.CAMERA_HEIGHT);
		// #if nokiaAPI
		CGame.m_dg.fillPolygon(fx_1, 0, fy_1, 0, 4, ARGB);
		// #else
		// # g.setColor(ARGB);
		// # g.fillRect(0,0,dConfig.CAMERA_WIDTH+2, dConfig.CAMERA_HEIGHT);
		// #endif
	}

	private final static int MAX_ELEMENT = 19;

	public final static byte EFF_ALARM = 0; // 警报
	public final static byte EFF_SNIPER = 1; // 狙击
	public final static byte EFF_SHUTTER = 2; // 百叶窗
	public final static byte EFF_CURVE = 4; // 曲线
	public final static byte EFF_SNOW = 5; // 雪效果
	public final static byte EFF_RAIN = 6; // 雨效果
	public final static byte EFF_STUN = 7; // 闪光弹特效
	public final static byte EFF_COVERSCREEN = 8; // 半屏幕
	public final static byte EFF_WINDOWS = 9; //
	public final static byte EFF_FADE_INOUT = 10;

	public final static byte EFF_UNBEAUTABLE = 12;
	public final static byte EFF_U = 13;
	public final static byte EFF_BOSS_HP = 14; // boss的血槽

	public static int[] renderInfo;
	private static int[] renderPipeLine = new int[MAX_ELEMENT];

	public final static byte NO_RENDER = 0;
	public final static byte YES_RENDER = 1;

	/**
	 * param >0 表示需要绘制 param ==0 表示不需要绘制
	 * 
	 * @param param
	 *            int
	 * @param plStyle
	 *            int
	 */
	public static void prepareToRender(int param, int plStyle) {
		if (plStyle >= 0 && plStyle < MAX_ELEMENT) {
			renderPipeLine[plStyle] = param;
			if (param <= 0) {
				return;
			}
		}
	}

	public static void clearRender(int plStyle) {
		if (plStyle >= 0 && plStyle < MAX_ELEMENT) {
			renderPipeLine[plStyle] = 0;
		}

	}

	public static final int[] getRenderInfo() {
		return renderPipeLine;
	}

	public static void resetRenderInfo() {
		for (int i = 0; i < renderPipeLine.length; i++) {
			renderPipeLine[i] = 0;
		}
	}

	// static Image m_imgInfo;
	public static void drawEffect(Graphics g) {
		/**
		 * 绘制狙击瞄准镜
		 */
		// if(renderInfo[EFF_SNIPER] > 0){
		// drawSniper(g);
		// }
		/***
		 * 绘制警报效果
		 */
		// if(renderInfo[EFF_ALARM] > 0){
		// if(--m_alarm == 0){
		// prepareToRender(0 , EFF_ALARM);
		// return;
		// }
		// drawAlarmLine();
		// }
		/**
		 * 开屏幕效果
		 */
		if (renderInfo[EFF_SHUTTER] > 0) {
			drawShutter(g);
		}
		/**
		 * 绘制雪的效果
		 */
		if (renderInfo[EFF_SNOW] > 0) {
			drawSnow(g, Camera.cameraLeft, Camera.cameraTop,
					dConfig.CAMERA_WIDTH, dConfig.CAMERA_HEIGHT);
		}
		/**
		 * 绘制雨效果
		 */
		if (renderInfo[EFF_RAIN] > 0) {
			drawRain(g, Camera.cameraLeft, Camera.cameraTop,
					dConfig.CAMERA_WIDTH, dConfig.CAMERA_HEIGHT);
		}
		// /**
		// * 绘制闪光弹效果
		// */
		// if(renderInfo[EFF_STUN] > 0){
		// drawWhiteSctreen();
		// }

		// if(renderInfo[EFF_UNBEAUTABLE] > 0){
		// drawUnbeautableSctreen();
		// }
		/**
		 * 绘制关屏幕效果
		 */
		if (renderInfo[EFF_COVERSCREEN] > 0) {
			drawcover(g);
		}
		/**
		 * 绘制windows窗口
		 */
		// if(renderInfo[EFF_WINDOWS] > 0){
		// drawHintString(g);
		// }
		/**
		 * 绘制渐隐渐显效果
		 */
		if (renderInfo[EFF_FADE_INOUT] > 0) {
			drawScreenFade(g);
		}

		// if(renderInfo[EFF_DISPLAY_NUM] > 0){
		// drawBonus();
		// }
		/**
		 * 绘制boss血槽
		 */
		if (renderInfo[EFF_BOSS_HP] > 0) {
			draw_BossHp(g);
		}

	}

	/**
	 * 设置 trailer慢镜头
	 * 
	 * @param isSlow
	 *            boolean
	 */
	public static boolean useSlowMotion;
	public static int slowMotionType;

	public static void setSlowStatus(boolean isSlow) {
		if (useSlowMotion == isSlow) {
			return;
		}
		useSlowMotion = isSlow;
	}

	/**********************
	 * 窗口绘制
	 ***********************/

	final static byte WINDOWS_STATUS = 0;
	final static byte WINDOWS_XPOS = 1;
	final static byte WINDOWS_YPOS = 2;
	final static byte WINDOWS_WIDTH = 3;
	final static byte WINDOWS_HEIGHT = 4;
	final static byte WINDOWS_COLOR = 5;
	final static byte WINDOWS_XTIMES = 6;
	final static byte WINDOWS_YTIMES = 7;
	final static byte WINDOWS_FLAG = 8;
	final static byte WINDOWS_SAVE = 9;

	public static int windowColor1 = 0x3f77AAFF;
	public static int windowColor2 = 0x3f7700FF;
	public static int windowColor3 = 0x3f77AAFF;
	public static int windowColor4 = 0xffffffff;
	public static int windowColorShuiHu = 0x309F5400;
	public static int windowspeed = 1;
	public static int windowbackoffset = 2;
	public static int wincornerimg = 2;
	public final static int numberOfWindows = 6;
	public static int openedWindow = -1;
	public static int openedWindow1 = -1;
	public static int openedWindow2 = -1;
	public static int openedWindow3 = -1;
	public static int openedWindow4 = -1;
	public static boolean windowOpen = false;

	static int[][] windowList = new int[numberOfWindows][10];
	// windows [窗口的数量][窗口的状态，x坐标，y坐标，宽度，高度,颜色，类型]//,z

	public static final byte WINDOWS_STATE_NULL = 0;
	public static final byte WINDOWS_STATE_OPENED = 1;
	public static final byte WINDOWS_STATE_OPENING = 2;
	public static final byte WINDOWS_STATE_CLOSING = 3;
	public static final byte WINDOWS_STATE_MOVING = 4;

	public static final byte WINDOWS_STATE_OPENING_H = 5;
	public static final byte WINDOWS_STATE_OPENING_V = 6;

	public static final byte WINDOWS_STATE_CLOSING_H = 7;
	public static final byte WINDOWS_STATE_CLOSING_V = 8;

	public static final byte WINDOWS_TIMES = 6;
	// 每帧拉开的像素
	public static final byte WINDOWS_TIMES_HV = 6;

	// 初始化一个窗体
	public static int initWindow(int xpos, int ypos, int width, int height,
			int status, int color, int windowCtrl) {
		int window = 0;
		int windowID = -1;

		for (window = 0; window < numberOfWindows; window++) {
			// 如果当前窗口状态为null,则录入数据
			if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_NULL) {
				windowList[window][WINDOWS_STATUS] = status;
				windowList[window][WINDOWS_XPOS] = xpos;
				windowList[window][WINDOWS_YPOS] = ypos;
				windowList[window][WINDOWS_WIDTH] = width;
				windowList[window][WINDOWS_HEIGHT] = height;
				windowList[window][WINDOWS_COLOR] = color;

				if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_MOVING) {
					windowList[window][WINDOWS_XTIMES] = ((width / 4)) - 1;
					windowList[window][WINDOWS_YTIMES] = ((height / 4)) - 1;
				} else if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_OPENING) {
					windowList[window][WINDOWS_XTIMES] = ((width / 2) / WINDOWS_TIMES) - 1;
					windowList[window][WINDOWS_YTIMES] = ((height / 2) / WINDOWS_TIMES) - 1;
				} else if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_OPENING_V) {
					windowList[window][WINDOWS_YTIMES] = ((height / 2) / WINDOWS_TIMES_HV) - 1;
				} else if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_OPENING_H) {
					windowList[window][WINDOWS_XTIMES] = ((width / 2) / WINDOWS_TIMES_HV) - 1;
				}

				windowList[window][WINDOWS_FLAG] = windowCtrl;
				windowID = window;
				window = numberOfWindows;
			}
		}

		return windowID;
	}

	public static void clearAllWindow() {
		if (windowList == null) {
			return;
		}

		for (int window = 0; window < numberOfWindows; window++) {
			windowList[window][WINDOWS_STATUS] = WINDOWS_STATE_NULL;
		}
		windowOpen = false;
	}

	public static void closeAllWindow() {
		if (windowList == null) {
			return;
		}

		for (int window = 0; window < numberOfWindows; window++) {
			if (windowList[window][WINDOWS_SAVE] == WINDOWS_STATE_OPENED) {
				windowList[window][WINDOWS_STATUS] = WINDOWS_STATE_CLOSING;
			} else if (windowList[window][WINDOWS_SAVE] == WINDOWS_STATE_OPENING_H) {
				windowList[window][WINDOWS_STATUS] = WINDOWS_STATE_CLOSING_H;
			} else if (windowList[window][WINDOWS_SAVE] == WINDOWS_STATE_OPENING_V) {
				windowList[window][WINDOWS_STATUS] = WINDOWS_STATE_CLOSING_V;
			}

		}

	}

	public static void clearWindow(int windowID) {
		if (windowList == null || windowID < 0) {
			return;
		}
		windowList[windowID][WINDOWS_STATUS] = WINDOWS_STATE_NULL;
	}

	public static void closeWindow(int windowID) {
		if (windowList == null) {
			return;
		}
		if (windowList[windowID][WINDOWS_SAVE] == WINDOWS_STATE_OPENED) {
			windowList[windowID][WINDOWS_STATUS] = WINDOWS_STATE_CLOSING;
		} else if (windowList[windowID][WINDOWS_SAVE] == WINDOWS_STATE_OPENING_H) {
			windowList[windowID][WINDOWS_STATUS] = WINDOWS_STATE_CLOSING_H;
		} else if (windowList[windowID][WINDOWS_SAVE] == WINDOWS_STATE_OPENING_V) {
			windowList[windowID][WINDOWS_STATUS] = WINDOWS_STATE_CLOSING_V;
		}

	}

	/**
	*
	*/
	private static int winShowIndex;

	public static void drawWindow(Graphics g) {
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		if (windowList == null) {
			return;
		}

		for (int window = 0; window < numberOfWindows; window++) {
			drawAWindow(g, window);
		}

	}

	public static void drawAWindow(Graphics g, int window) {
		if (window < 0) {
			CDebug._assert(false, "windowID 不正确");
			return;
		}
		// 根据游戏的状态
		switch (windowList[window][WINDOWS_STATUS]) {
		case WINDOWS_STATE_OPENED:
			winDraw(g, windowList[window][WINDOWS_XPOS],
					windowList[window][WINDOWS_YPOS],
					windowList[window][WINDOWS_WIDTH],
					windowList[window][WINDOWS_HEIGHT],
					windowList[window][WINDOWS_COLOR], 0);
			winShowIndex++;
			if (winShowIndex > 3) {
				windowOpen = true;
			}
			break;

		case WINDOWS_STATE_OPENING:
		case WINDOWS_STATE_OPENING_V:
		case WINDOWS_STATE_OPENING_H:
			winShowIndex = 0;
			windowOpen = false;
			for (int speed = 0; speed < windowspeed; speed++) {
				int xStep;
				int yStep;
				if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_OPENING) {
					xStep = windowList[window][WINDOWS_XTIMES] * WINDOWS_TIMES;
					yStep = windowList[window][WINDOWS_YTIMES] * WINDOWS_TIMES;
				} else {
					xStep = windowList[window][WINDOWS_XTIMES]
							* WINDOWS_TIMES_HV;
					yStep = windowList[window][WINDOWS_YTIMES]
							* WINDOWS_TIMES_HV;
				}
				if (speed == windowspeed - 1) {
					winDraw(g, windowList[window][WINDOWS_XPOS] + xStep,
							windowList[window][WINDOWS_YPOS] + yStep,
							windowList[window][WINDOWS_WIDTH] - (2 * xStep),
							windowList[window][WINDOWS_HEIGHT] - (2 * yStep),
							windowList[window][WINDOWS_COLOR], 0);
				}
				if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_OPENING) {
					if (windowList[window][WINDOWS_XTIMES] > 0) {
						windowList[window][WINDOWS_XTIMES]--;
					}

					if (windowList[window][WINDOWS_YTIMES] > 0) {
						windowList[window][WINDOWS_YTIMES]--;
					}
				} else if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_OPENING_H) {
					if (windowList[window][WINDOWS_XTIMES] > 0) {
						windowList[window][WINDOWS_XTIMES]--;
					}
				} else if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_OPENING_V) {
					if (windowList[window][WINDOWS_YTIMES] > 0) {
						windowList[window][WINDOWS_YTIMES]--;
					}
				}

				if (windowList[window][WINDOWS_XTIMES] == 0
						&& windowList[window][WINDOWS_YTIMES] == 0) {
					windowList[window][WINDOWS_SAVE] = windowList[window][WINDOWS_STATUS];
					windowList[window][WINDOWS_STATUS] = WINDOWS_STATE_OPENED;
				}
			}
			break;

		case WINDOWS_STATE_CLOSING:
		case WINDOWS_STATE_CLOSING_H:
		case WINDOWS_STATE_CLOSING_V:
			winShowIndex = 0;
			windowOpen = false;
			for (int speed = 0; speed < windowspeed; speed++) {
				int xStep;
				int yStep;
				int timesx;
				int timesy;
				if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_OPENING) {
					xStep = windowList[window][WINDOWS_XTIMES] * WINDOWS_TIMES;
					yStep = windowList[window][WINDOWS_YTIMES] * WINDOWS_TIMES;
					timesx = ((windowList[window][3] / 2) / WINDOWS_TIMES) - 1;
					timesy = ((windowList[window][4] / 2) / WINDOWS_TIMES) - 1;
				} else {
					xStep = windowList[window][WINDOWS_XTIMES]
							* WINDOWS_TIMES_HV;
					yStep = windowList[window][WINDOWS_YTIMES]
							* WINDOWS_TIMES_HV;
					timesx = ((windowList[window][3] / 2) / WINDOWS_TIMES_HV) - 1;
					timesy = ((windowList[window][4] / 2) / WINDOWS_TIMES_HV) - 1;
				}

				if (speed == windowspeed - 1) {
					winDraw(g, windowList[window][WINDOWS_XPOS] + xStep,
							windowList[window][WINDOWS_YPOS] + yStep,
							windowList[window][WINDOWS_WIDTH] - (2 * xStep),
							windowList[window][WINDOWS_HEIGHT] - (2 * yStep),
							windowList[window][WINDOWS_COLOR], 0);
				}

				if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_CLOSING) {
					if (windowList[window][WINDOWS_XTIMES] < timesx) {
						windowList[window][WINDOWS_XTIMES]++;
					}
					if (windowList[window][WINDOWS_YTIMES] < timesy) {
						windowList[window][WINDOWS_YTIMES]++;
					}
				} else if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_CLOSING_H) {
					if (windowList[window][WINDOWS_XTIMES] < timesx) {
						windowList[window][WINDOWS_XTIMES]++;
					}

				} else if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_CLOSING_V) {
					if (windowList[window][WINDOWS_YTIMES] < timesy) {
						windowList[window][WINDOWS_YTIMES]++;
					}
				}
				if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_CLOSING) {
					if (windowList[window][WINDOWS_XTIMES] == timesx
							&& windowList[window][WINDOWS_YTIMES] == timesy) {
						windowList[window][WINDOWS_STATUS] = WINDOWS_STATE_NULL;
					}
				} else if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_CLOSING_H) {
					if (windowList[window][WINDOWS_XTIMES] == timesx) {
						windowList[window][WINDOWS_STATUS] = WINDOWS_STATE_NULL;
					}
				} else if (windowList[window][WINDOWS_STATUS] == WINDOWS_STATE_CLOSING_V) {
					if (windowList[window][WINDOWS_YTIMES] == timesy) {
						windowList[window][WINDOWS_STATUS] = WINDOWS_STATE_NULL;
					}
				}
			}
			break;

		case WINDOWS_STATE_MOVING:
			winShowIndex = 0;
			windowspeed = 3;

			// closing
			if (windowList[window][8] == 0) {
				for (int speed = 0; speed < windowspeed; speed++) {
					int xStep = windowList[window][WINDOWS_XTIMES];
					int yStep = windowList[window][WINDOWS_YTIMES];
					if (speed == windowspeed - 1) {
						winDraw(g, windowList[window][1] + xStep,
								windowList[window][2] + yStep,
								windowList[window][3] - (2 * xStep),
								windowList[window][4] - (2 * yStep),
								windowList[window][5], windowList[window][0]);
					}

					if (windowList[window][6] < ((windowList[window][3] / 4)) - 1) {
						windowList[window][6]++;
					}
					if (windowList[window][7] < ((windowList[window][4] / 4)) - 1) {
						windowList[window][7]++;
					}

					if (windowList[window][6] == ((windowList[window][3] / 4)) - 1
							&& windowList[window][7] == ((windowList[window][4] / 4)) - 1) {
						windowList[window][WINDOWS_FLAG] = 1;
					}

				}
			}
			// open
			else {
				for (int speed = 0; speed < windowspeed; speed++) {
					int xStep = windowList[window][6];
					int yStep = windowList[window][7];
					if (speed == windowspeed - 1) {
						winDraw(g, windowList[window][1] + xStep,
								windowList[window][2] + yStep,
								windowList[window][3] - (2 * xStep),
								windowList[window][4] - (2 * yStep),
								windowList[window][5], windowList[window][0]);
					}

					if (windowList[window][6] > 0) {
						windowList[window][6]--;
					}
					if (windowList[window][7] > 0) {
						windowList[window][7]--;
					}

					if (windowList[window][6] == 0
							&& windowList[window][7] == 0) {
						windowList[window][8] = 0;
					}
				}
			}

			break;
		}
	}

	public static void winDraw(Graphics g, int x, int y, int width, int height,
			int backcol, int state) {

		// for(int i = x; i < x + width; i += imME_tile.getWidth ()) {
		// for(int j = y; j < y + height; j += imME_tile.getHeight ()) {
		// g.drawImage (imME_tile, i, j, Graphics.LEFT | Graphics.TOP);
		// }
		// }
		if (state != WINDOWS_STATE_MOVING) {
			// #if nokiaAPI
			CTools.fillPolygon(g, x, y, width, height, backcol);
			// #else
			// # int preColor = g.getColor ();
			// # g.setColor ( backcol );
			// # g.drawRect ( x , y , width , height );
			// # g.setColor ( preColor );
			// #endif

			// CTools.drawShadowRect(g , new int[]{0,x,y,width,height} ,
			// 0x000922 , 0x184262 , true);
			drawWinLine(g, x, y, width, height, backcol);
		}
		// 绘制windows的边框
		if (CGame.border_Image[0] != null) {
			int dx = CGame.border_Image[0].getWidth() >> 1;
			int dy = CGame.border_Image[0].getHeight() >> 1;
			g.drawImage(CGame.border_Image[0], x - dx + 2, y - dy + 2,
					Graphics.LEFT | Graphics.TOP);
			g.drawImage(CGame.border_Image[1], x - dx + 2, y + height - dy - 2,
					Graphics.LEFT | Graphics.TOP);

			g.drawImage(CGame.border_Image[2], x + width - dx - 2, y - dy + 2,
					Graphics.LEFT | Graphics.TOP);
			g.drawImage(CGame.border_Image[3], x + width - dx - 2, y + height
					- dy - 2, Graphics.LEFT | Graphics.TOP);
		}
	}

	/**
	 * 09.5.4 增加color参数 by lin
	 * 
	 * @param g
	 *            Graphics
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param color
	 *            int
	 */
	private static void drawWinLine(Graphics g, int x, int y, int width,
			int height, int color) {
		g.setColor(dConfig.COLOR_BLACK);
		g.drawLine(x, y - 4, x + width, y - 4);
		g.drawLine(x, y + height + 3, x + width, y + height + 3);
		g.drawLine(x - 4, y, x - 4, y + height);
		g.drawLine(x + width + 3, y, x + width + 3, y + height);

		g.setColor(color);
		g.drawLine(x, y - 3, x + width, y - 3);
		g.drawLine(x, y + height + 2, x + width, y + height + 2);
		g.drawLine(x - 3, y, x - 3, y + height);
		g.drawLine(x + width + 2, y, x + width + 2, y + height);
	}

	/****************************************
	 * boss的血槽 shubinl
	 *****************************************/
	// public static boolean m_bShowBossHp;
	public static int m_BossHpH;
	public static int m_BossHpW;
	public static int m_BossHpWMax;

	public static int m_bossHpShowRate; // boss血槽显示的缩减倍数
	static final int BOSS_HP_SHOW_DEFAULT_RATE = 6; // 默认缩减倍数
	static final int BOSS_HP_X_OFFSET = 30; // 血槽在屏幕中X偏移
	static final int BOSS_HP_DEFAULT_H = 10; // 血槽默认高度

	public static void init_BossHp(int maxHp) {
		if (maxHp <= 0) {
			return;
		}
		if (maxHp / BOSS_HP_SHOW_DEFAULT_RATE <= dConfig.S_WIDTH
				- BOSS_HP_X_OFFSET * 2) {
			m_bossHpShowRate = BOSS_HP_SHOW_DEFAULT_RATE;
		} else {
			m_bossHpShowRate = maxHp / (dConfig.S_WIDTH - BOSS_HP_X_OFFSET * 2)
					+ 1;
		}

		m_BossHpW = m_BossHpWMax = maxHp / m_bossHpShowRate;
		m_BossHpH = BOSS_HP_DEFAULT_H;
		// m_bShowBossHp=true;
	}

	public static void set_BossHp(int hp) {
		if (m_bossHpShowRate <= 0) {
			return;
		}
		if (hp < 0) {
			hp = 0;
		}
		m_BossHpW = hp / m_bossHpShowRate;
	}

	public static void draw_BossHp(Graphics g) {
		// if (m_bShowBossHp) {
		g.setColor(dConfig.COLOR_WHITE);
		g.drawRoundRect(BOSS_HP_X_OFFSET, dConfig.S_HEIGHT - BOSS_HP_DEFAULT_H
				- 15 - 1, m_BossHpWMax + 1, m_BossHpH, 2, 2);
		g.setColor(dConfig.COLOR_RED);
		g.fillRect(BOSS_HP_X_OFFSET + 1, dConfig.S_HEIGHT - BOSS_HP_DEFAULT_H
				- 15, m_BossHpW, m_BossHpH - 1);
		// }
	}

}
