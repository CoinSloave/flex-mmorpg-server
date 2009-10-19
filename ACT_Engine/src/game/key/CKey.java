package game.key;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
/**
 * <p>Title: </p>
 *
 * <p>Description: Action_GameEngine</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author zeng kai, lin shu bin
 * @version 1.0
 */

import game.CGame;
import game.config.dGame;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;

public class CKey {
	public CKey() {
	}

	public static final int GK_UP = 1 << 0;
	public static final int GK_DOWN = 1 << 1;
	public static final int GK_LEFT = 1 << 2;
	public static final int GK_RIGHT = 1 << 3;
	public static final int GK_NUM0 = 1 << 4;
	public static final int GK_NUM1 = 1 << 5;
	public static final int GK_NUM3 = 1 << 6;
	public static final int GK_NUM5 = 1 << 7;
	public static final int GK_NUM7 = 1 << 8;
	public static final int GK_NUM9 = 1 << 9;
	public static final int GK_STAR = 1 << 10;
	public static final int GK_POUND = 1 << 11;
	public static final int GK_SOFT_LEFT = 1 << 12;
	public static final int GK_SOFT_RIGHT = 1 << 13;
	public static final int GK_MIDDLE = 1 << 14;
	public static final int GK_RETURN = 1 << 15;

	public static final int GK_FORWARD = 1 << 16; // 前
	public static final int GK_BACKWARD = 1 << 17; // 后

	// #if keymode_moto1||keymode_moto2||keymode_e680
	// # public static final int GK_OK = GK_MIDDLE | GK_SOFT_RIGHT;
	// # public static final int GK_CANCEL = GK_SOFT_LEFT|GK_RETURN;
	// #elif keymode_se
	// # public static final int GK_OK = GK_MIDDLE | GK_SOFT_LEFT;
	// # public static final int GK_CANCEL = GK_SOFT_RIGHT|GK_RETURN;
	// #else
	public static final int GK_OK = GK_MIDDLE | GK_SOFT_LEFT;
	public static final int GK_CANCEL = GK_SOFT_RIGHT;
	// #endif

	public static final int GK_ALL_NUM_KEY = GK_MIDDLE | GK_UP | GK_DOWN
			| GK_LEFT | GK_RIGHT | GK_NUM0 | GK_NUM1 | GK_NUM3 | GK_NUM7
			| GK_NUM9;
	public static final int GK_LEFT_OR_RIGHT = GK_LEFT | GK_RIGHT;
	public static final int GK_NUM_13 = GK_NUM1 | GK_NUM3;
	public static final int GK_NUM_79 = GK_NUM7 | GK_NUM9;
	public static final int GK_FLIPKEY = GK_LEFT_OR_RIGHT | GK_NUM_13
			| GK_NUM_79;

	// public static final int GK_NUM2 = 1 << 19;
	// public static final int GK_NUM8 = 1 << 20;
	// public static final int GK_NUM4 = 1 << 21;
	// public static final int GK_NUM6 = 1 << 22;

	public static final int GK_NONE = 0;

	public static final int GK_LEFTRIGHT = GK_LEFT | GK_RIGHT;
	public static final int GK_NUM13 = GK_NUM1 | GK_NUM3;
	public static final int GK_NUM79 = GK_NUM7 | GK_NUM9;

	// #if keymode_nokia
	// nokia key
	public static final int KEY_UP = 1;
	public static final int KEY_DOWN = 2;
	public static final int KEY_LEFT = 3;
	public static final int KEY_RIGHT = 4;
	public static final int KEY_SOFT_LEFT = 6;
	public static final int KEY_SOFT_RIGHT = 7;
	public static final int KEY_MID = 5;
	public static final int KEY_RETURN = -1;
	// #elif keymode_moto1
	// # //moto
	// # public static final int KEY_UP = 1;
	// # public static final int KEY_DOWN = 6;
	// # public static final int KEY_LEFT = 2;
	// # public static final int KEY_RIGHT = 5;
	// # public static final int KEY_SOFT_LEFT = 21;
	// # public static final int KEY_SOFT_RIGHT = 22;
	// # public static final int KEY_MID = 20;
	// # public static final int KEY_RETURN = -1;
	// #elif keymode_moto2
	// # //moto
	// # public static final int KEY_UP = 1;
	// # public static final int KEY_DOWN = 2;
	// # public static final int KEY_LEFT = 3;
	// # public static final int KEY_RIGHT = 4;
	// # public static final int KEY_SOFT_LEFT = 21;
	// # public static final int KEY_SOFT_RIGHT = 22;
	// # public static final int KEY_MID = 5;
	// # public static final int KEY_RETURN = -1;
	// #elif keymode_e680
	// # //moto
	// # public static final int KEY_UP = 1;
	// # public static final int KEY_DOWN = 2;
	// # public static final int KEY_LEFT = 3;
	// # public static final int KEY_RIGHT = 4;
	// # public static final int KEY_SOFT_LEFT = 6;
	// # public static final int KEY_SOFT_RIGHT = 7;
	// # public static final int KEY_MID = 5;
	// # public static final int KEY_RETURN = -1;
	// #elif keymode_se
	// # //se
	// # public static final int KEY_UP = 1;
	// # public static final int KEY_DOWN = 2;
	// # public static final int KEY_LEFT = 3;
	// # public static final int KEY_RIGHT = 4;
	// # public static final int KEY_SOFT_LEFT = 6;
	// # public static final int KEY_SOFT_RIGHT = 7;
	// # public static final int KEY_MID = 5;
	// # public static final int KEY_RETURN = 11;
	// #elif keymode_W958C
	// # //w958没有方向键，这里定义只是为了触摸屏 模拟按键，具体数值没有意义
	// # public static final int KEY_UP = 1;
	// # public static final int KEY_DOWN = 2;
	// # public static final int KEY_LEFT = 3333;
	// # public static final int KEY_RIGHT = 4444;
	// # public static final int KEY_SOFT_LEFT = 6;
	// # public static final int KEY_SOFT_RIGHT = 7;
	// # public static final int KEY_MID = 5;
	// # public static final int KEY_RETURN = 11;
	// #endif

	public static final int STD_BACK = -11;

	public static final byte GAMEKEY_QUEUE_SIZE = 6;
	public static final int GAMEKEY_COMBO_INTERVAL = 3;
	//
	// public static final int DOUBLE_WEAPON_MASK = 0x0100;
	// public static final int PRINCE_CLASS_ID = 0;
	// public static final int DARKPRINCE_CLASS_ID = 5;
	//
	// public static final byte GAMEKEY_COMBO_NUMBER = 5;

	public static int m_keyCurrent;
	public static int m_keyPressed;
	public static int m_keyReleased;
	public static int m_keyDblPressed;
	public static int m_lastPressed;
	public static int m_keyTick;
	public static int m_fastCurrentKey;

	private static int[] m_gkQueue = new int[GAMEKEY_QUEUE_SIZE];
	private static int m_queueStart;
	private static int m_queueEnd;

	// 初始化按键
	public static void initKey() {
		m_keyCurrent = 0;
		m_keyPressed = 0;
		m_keyReleased = 0;
		m_keyDblPressed = 0;
		m_lastPressed = 0;
		m_keyTick = 0;
		m_fastCurrentKey = 0;
		m_keySequence = 0;

		for (int i = 0; i < GAMEKEY_QUEUE_SIZE; i++) {
			m_gkQueue[i] = 0;
		}

		m_queueStart = m_queueEnd = 0;
	}

	/**
	 * 环型队列先入先出的原则
	 * 
	 * @param key
	 *            int 把按下的键压入栈中
	 * @return boolean
	 */
	public static boolean pushQueue(int key) {
		if ((m_queueStart + 1) % GAMEKEY_QUEUE_SIZE == m_queueEnd) {
			return false;
		} else {
			m_gkQueue[m_queueStart] = key;
			m_queueStart = (m_queueStart + 1) % GAMEKEY_QUEUE_SIZE;
		}
		return true;
	}

	/**
	 * 
	 * @return int 返回
	 */
	public static int popQueue() {
		if (m_queueStart == m_queueEnd) {
			return m_fastCurrentKey;
		}
		int key = m_gkQueue[m_queueEnd];
		m_queueEnd = (m_queueEnd + 1) % GAMEKEY_QUEUE_SIZE;
		return key;
	}

	/**
	 * 在每个循环更新按键的情况
	 */
	public static final byte TYPE_RELEASE_KEY = 2;
	public static final byte TYPE_PRESS_KEY = 0;

	public static int getKeyInMap(int keyID) {
		switch (keyID) {
		case 0:
			return GK_NUM0;
		case 1:
			return GK_NUM1;
		case 2:
			return GK_UP;
		case 3:
			return GK_NUM3;
		case 4:
			return GK_LEFT;

		case 5:
			return GK_MIDDLE;
		case 6:
			return GK_RIGHT;

		case 7:
			return GK_NUM7;
		case 8:
			return GK_DOWN;
		case 9:
			return GK_NUM9;
		case 10:
			return GK_STAR;
		case 11:
			return GK_POUND;
		}
		return -1;
	}

	public static Vector m_keyVector = new Vector();
	public static boolean m_isKeyLocked;

	public static void initAutoKeyMap(int[] keyMap) {
		m_isKeyLocked = true;
		for (int i = 0; i < keyMap.length; i++) {
			m_keyVector.addElement(String.valueOf(keyMap[i]));
		}
	}

	public static void updateKey() {
		if (m_isKeyLocked) {
			if (m_keyVector.size() == 0) {
				m_isKeyLocked = false;
				if (CGame.m_preState == dGame.GST_SCRIPT_RUN) {
					CGame.setGameState(CGame.m_preState);
				}
				return;
			}

			int currentKey;

			int counter = Integer.parseInt((String) m_keyVector.elementAt(2));

			int type = Integer.parseInt((String) m_keyVector.elementAt(1));

			int dur = Integer.parseInt((String) m_keyVector.elementAt(3));

			int pressKey = getKeyInMap(Integer.parseInt((String) m_keyVector
					.elementAt(0)));

			currentKey = pressKey;

			if (type == TYPE_RELEASE_KEY) {
				m_keyCurrent &= ~pressKey;
			}

			// 当按键执行完毕
			if (counter == 0) {
				currentKey &= ~pressKey;
				if (dur > 0) {
					initKey();
					dur--;
					m_keyVector.removeElementAt(3);
					m_keyVector.insertElementAt(String.valueOf(dur), 3);
					return;
				}

				m_keyVector.removeElementAt(3);
				m_keyVector.removeElementAt(2);
				m_keyVector.removeElementAt(1);
				m_keyVector.removeElementAt(0);

			} else {
				if (counter > 0) {
					counter--;
					m_keyVector.removeElementAt(2);
					m_keyVector.insertElementAt(String.valueOf(counter), 2);
				}
			}

			m_keyPressed = ~m_keyCurrent & currentKey;
			m_keyReleased = m_keyCurrent & ~currentKey;
			m_keyCurrent = currentKey;

		} else {
			int currentKey;
			currentKey = popQueue();
			m_keyPressed = ~m_keyCurrent & currentKey; //
			m_keyReleased = m_keyCurrent & ~currentKey; //
			m_keyCurrent = currentKey;
		}
		m_keyTick++;

		if (m_keyPressed != 0) {
			// for double key only
			if ((m_lastPressed != 0) && (m_keyTick < 4)
					&& (m_lastPressed == m_keyPressed)) {
				m_keyDblPressed = m_keyPressed;
				m_lastPressed = 0;
			} else {
				m_keyTick = 0;
				m_keyDblPressed = 0;
				m_lastPressed = m_keyPressed;
			}
		} else if (m_keyDblPressed != 0) {
			m_keyDblPressed = 0;
		}

	}

	// 按键持续不放
	public static boolean isKeyHold(int gameKey) {
		return (m_keyCurrent & gameKey) != 0;
	}

	// 按下任意键
	public static boolean isAnyKeyPressed() {
		return m_keyPressed != 0;
	}

	// 双击按键
	public static boolean isKeyDblPressed(int gameKey) {
		return (m_keyDblPressed & gameKey) != 0;
	}

	// 单次按键
	public static boolean isKeyPressed(int gKey) {
		return (m_keyPressed & gKey) != 0;
	}

	// //组合键攻击的按键判断
	// public static boolean isComboKeyPressed(int gKey){
	// if (gKey==GK_FORWARD) {
	// return isKeyPressed(CGame.m_hero.getFaceDir()==-1?GK_LEFT:GK_RIGHT);
	// }
	// else if(gKey==GK_BACKWARD){
	// return isKeyPressed(CGame.m_hero.getFaceDir()==1?GK_LEFT:GK_RIGHT);
	// }
	// else {
	// return isKeyPressed(gKey);
	// }
	// }

	/**
	 * 转换系统按键为游戏定义按键
	 * 
	 * @param keyCode
	 *            int
	 * @return int
	 */
	public static int GetKey(int keyCode) {
		if (keyCode < 0) {
			keyCode = -keyCode;
		}
		// #if keymode_W958C
		// # //每个按键有2个键值
		// # switch (keyCode) {
		// # case KEY_UP:
		// # case 116:
		// # case 121:
		// # return GK_UP;
		// # case KEY_DOWN:
		// # case 118:
		// # case 98:
		// # return GK_DOWN;
		// # case KEY_LEFT:
		// # case 100:
		// # case 102:
		// # return GK_LEFT;
		// # case 106:
		// # case 107:
		// # case KEY_RIGHT:
		// # return GK_RIGHT;
		// # case 103:
		// # case 104:
		// # case KEY_MID:
		// # return GK_MIDDLE;
		// # case 32:
		// # return GK_NUM0;
		// # case 101:
		// # case 114:
		// # return GK_NUM1;
		// # case 117:
		// # case 105:
		// # return GK_NUM3;
		// # case 110:
		// # case 109:
		// # return GK_NUM9;
		// # case 120:
		// # case 99:
		// # return GK_NUM7;
		// # case KEY_SOFT_LEFT:
		// # return GK_SOFT_LEFT;
		// # case KEY_SOFT_RIGHT:
		// # return GK_SOFT_RIGHT;
		// # case KEY_RETURN:
		// # return GK_RETURN;
		// # case 4:
		// # return GK_POUND;
		// # case 3:
		// # return GK_STAR;
		// # default:
		// # return 0;
		// # }
		// #else
		switch (keyCode) {
		case KEY_UP:
		case Canvas.KEY_NUM2:
			return GK_UP;
		case KEY_DOWN:
		case Canvas.KEY_NUM8:
			return GK_DOWN;
		case KEY_LEFT:
		case Canvas.KEY_NUM4:
			return GK_LEFT;
		case KEY_RIGHT:
		case Canvas.KEY_NUM6:
			return GK_RIGHT;
		case KEY_MID:
		case Canvas.KEY_NUM5:
			return GK_MIDDLE;
		case KEY_SOFT_LEFT:
			return GK_SOFT_LEFT;
		case KEY_SOFT_RIGHT:
			return GK_SOFT_RIGHT;
		case Canvas.KEY_NUM0:
			return GK_NUM0;
		case Canvas.KEY_NUM1:
			return GK_NUM1;
		case Canvas.KEY_NUM3:
			return GK_NUM3;
		case Canvas.KEY_NUM9:
			return GK_NUM9;
		case Canvas.KEY_NUM7:
			return GK_NUM7;
		case Canvas.KEY_POUND:
			return GK_POUND;
		case Canvas.KEY_STAR:
			return GK_STAR;
		case KEY_RETURN:
			return GK_RETURN;
		default:
			return 0;
		}
		// #endif
	}

	public static int m_keySequence;

	public static int checkCombinedKeys() {
		// if(m_keySequence >= 99999){
		// m_keySequence =0;
		// return 0;
		// }
		int curkeybuffer = getNewPressed(GK_ALL_NUM_KEY); // 做掩码
		if (curkeybuffer != 0) {
			m_keySequence = m_keySequence * 10;
			if ((curkeybuffer & GK_NUM1) != 0) { // 1
				m_keySequence++;
			} else if ((curkeybuffer & GK_LEFT) != 0) { // 2
				m_keySequence += 2;
			} else if ((curkeybuffer & GK_NUM3) != 0) { // 3
				m_keySequence += 3;
			} else if ((curkeybuffer & GK_DOWN) != 0) { // 4
				m_keySequence += 4;
			} else if ((curkeybuffer & GK_MIDDLE) != 0) { // 5
				m_keySequence += 5;
			} else if ((curkeybuffer & GK_RIGHT) != 0) { // 6
				m_keySequence += 6;
			} else if ((curkeybuffer & GK_NUM7) != 0) { // 7
				m_keySequence += 7;
			} else if ((curkeybuffer & GK_DOWN) != 0) { // 8
				m_keySequence += 8;
			} else if ((curkeybuffer & GK_NUM9) != 0) { // 9
				m_keySequence += 9;
			}
			System.out.println(m_keySequence);
		}
		return m_keySequence;
	}

	public static final int getNewPressed(int _Mask) {
		return m_keyPressed & _Mask;
	}

}
