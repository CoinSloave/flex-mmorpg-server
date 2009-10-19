package game;

import game.config.dConfig;
import game.config.dGame;
import game.key.CKey;
import game.object.CHero;
import game.object.IObject;
import game.res.ResLoader;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import com.mglib.mdl.ani.AniData;
import com.mglib.mdl.ani.AniPlayer;
import com.mglib.script.Script;
import com.mglib.sound.SoundPlayer;
import com.mglib.ui.Data;
import com.mglib.ui.Rect;
import com.mglib.ui.UITools;
import com.mglib.ui.UIdata;

public class GameUI {
	public static List curList = List.listInstance;
	// ���������
	private static int preFrame, curFrame, nextFrame;
	private static int[] frameManager = new int[15];
	private static int fmPointer = -1; // ָ��frameManager
	// ����
	public static CHero curHero;

	// /////////////////////////////////////////////////////////
	public static void drawScrollBar(Graphics g, int formID, int blockIndex,
			int length, int showLength, int curIndex, int rgb) {
		if (length <= showLength) {
			return;
		}
		short[] block = UIdata.getBlock(formID, blockIndex);
		int startX = block[0] - UIdata.UI_offset_X;
		int startY = block[1] - UIdata.UI_offset_Y;
		int w = block[2];
		int h = block[3];
		int drawh;
		int drawY;
		if (length <= showLength) {
			drawh = h;
			drawY = startY;
		} else {
			drawh = showLength * h / length;
			drawY = startY + (curIndex) * (h - drawh) / (length - 1);
		}
		g.setClip(startX, drawY, w, drawh);
		g.setColor(rgb);
		g.fillRect(startX, drawY, w, drawh);
	};

	/**
	 * װ�����ԶԱ�
	 * 
	 * @param selectEquip
	 *            Goods
	 */
	public static void updateDifference(Goods selectEquip) {
		if (selectEquip == null) {
			for (int i = 0; i < weaponDifference.length; i++) {
				weaponDifference[i] = 0;
			}
			return;
		}
		// ѡ����װ��
		short[] dataSelect = selectEquip.getAffectedPro();
		// ���ǵ�װ��
		short[] dataHero = null;
		int part = -1;
		switch (selectEquip.getDetailType()) {
		case Data.IDX_EP_weapon:
			part = IObject.PRO_INDEX_WEAPON;
			break;
		case Data.IDX_EP_loricae:
			part = IObject.PRO_INDEX_LORICAE;
			break;
		case Data.IDX_EP_jade:
			part = IObject.PRO_INDEX_JADE;
			break;
		}
		if (curHero.m_actorProperty[part] >= 0) {
			Goods Curequip = (Goods) (curHero.hsEquipList.get(String
					.valueOf(curHero.m_actorProperty[part])));
			if (Curequip != null) {
				dataHero = Curequip.getAffectedPro();
			} else {
				System.out.println("------------------��ǰ����Ϊ��");
			}
		}
		// ��ֵ
		weaponDifference[0] = (short) (dataSelect[Data.IDX_PRO_maxHp] - (dataHero != null ? dataHero[Data.IDX_PRO_maxHp]
				: 0));
		weaponDifference[1] = (short) (dataSelect[Data.IDX_PRO_maxMp] - (dataHero != null ? dataHero[Data.IDX_PRO_maxMp]
				: 0));
		weaponDifference[2] = (short) (dataSelect[Data.IDX_PRO_atk] - (dataHero != null ? dataHero[Data.IDX_PRO_atk]
				: 0));
		weaponDifference[3] = (short) (dataSelect[Data.IDX_PRO_def] - (dataHero != null ? dataHero[Data.IDX_PRO_def]
				: 0));
		weaponDifference[4] = (short) (dataSelect[Data.IDX_PRO_cri_rate] - (dataHero != null ? dataHero[Data.IDX_PRO_cri_rate]
				: 0));
	}

	// ////////////////////////////////////////////////////////
	// ���������Ĵ���
	public static AniPlayer iconPlayer;

	public static void setIconPlayer(int formID, int blockId, int id) {
		int actionID = id;
		if (actionID == -1)
			return;
		short[] block = UIdata.getBlock(formID, blockId);
		int x = block[0] + block[2] / 2 - UIdata.UI_offset_X;
		int y = block[1] + block[3] / 2 - UIdata.UI_offset_Y;
		int animationID = ResLoader.equipIconAniID;
		if (iconPlayer == null) {
			iconPlayer = new AniPlayer(ResLoader.animations[animationID], x, y,
					actionID);
			AniData.setMlgs(ResLoader.aniMlgs);
		}
		iconPlayer.setSpriteX(x);
		iconPlayer.setSpriteY(y);
		iconPlayer.setAnimAction(id);
	}

	/**
	 * ͸�� Vector ����ַ�������[]
	 * 
	 * @param v
	 *            Vector
	 * @return String[]
	 */
	protected static final String[] getStringAry(Vector v) {
		String[] sTemp = new String[v.size()];
		for (int i = 0; i < sTemp.length; i++) {
			sTemp[i] = (String) v.elementAt(i);
		}
		return sTemp;
	}

	/**
	 * ͨ�� Vector��Vector�� ����ַ�������[][]
	 * 
	 * @param v
	 *            Vector
	 * @return String[][]
	 */
	protected static final String[][] getStringAryTwo(Vector v) {
		String[][] sTemp = new String[v.size()][];
		for (int i = 0; i < sTemp.length; i++) {
			sTemp[i] = getStringAry((Vector) v.elementAt(i));
		}
		v.removeAllElements();
		return sTemp;
	}

	/****************************************************************************
	 * UI �Ķ�����Ҫ�ӿ�
	 ***************************************************************************/
	public static void setCurrentHero(CHero h) {
		curHero = h;
	}

	/**
	 * ���⹫����,UI�߼�
	 */
	public static void doLogic() {
		doFrame(curFrame);
	}

	/**
	 * ���⹫����UI����
	 * 
	 * @param g
	 *            Graphics
	 */
	public static void paint(Graphics g) {
		CGame.cls(g, 0);
		// #if size_500k
		AniData.setMlgs(ResLoader.aniMlgs);
		CGame.paintLevel(g);
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		CTools.fillPolygon(g, 0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT,
				0x60000000);
		// #endif
		g.setFont(dConfig.F_SMALL_DEFAULT);
		// for(int i = 0; i <= fmPointer; i++){
		show(g, frameManager[fmPointer]);
		// }
	}

	static CGame cGame;

	/**
	 * ���⹫����,���õ�ǰframe
	 * 
	 * @param frameName
	 *            int
	 */
	public static void setCurrent(int frameName) {
		cGame = CMIDlet.display;
		curPointForm = frameName;
		boolean exist = false; // ���ڹ�������Ƿ��Ѵ���
		for (int i = 0; i <= fmPointer; i++) {
			if (frameManager[i] == frameName) {
				exist = true;
				fmPointer = i;
				curFrame = frameManager[fmPointer];
				if (fmPointer > 0) {
					preFrame = frameManager[fmPointer - 1];
				} else {
					preFrame = -1;
				}
				break;
			}
		}
		// ������
		if (!exist) {
			preFrame = curFrame;
			curFrame = frameName;
			fmPointer++;
			frameManager[fmPointer] = curFrame;
		}
		// ��ʼ����ǰ����
		initFrame(curFrame);
		// curIndex=0;
	}

	/****************************************************************************
	 * ���壺 ��ʼ�� �����߼� ����
	 ***************************************************************************/
	private static void initFrame(int frameName) {
		switch (frameName) {
		case Form_mainMenu:
			initForm_mainMenu();
			break;
		case Form_property:
			initForm_property();
			break;
		case Form_task:
			initForm_task();
			break;
		case Form_equip:
			initForm_equip();
			break;
		case Form_make:
			initForm_make();
			break;
		case Form_GOODS:
			initForm_GOODS();
			break;
		case Form_SKILL:
			initForm_SKILL();
			break;
		// case Form_systemMenu:
		// initForm_systemMenu();
		// break;
		case Form_shop:
			initForm_shop();
			break;

		}
	}

	private static void doFrame(int frameName) {
		switch (frameName) {
		case Form_mainMenu:
			doForm_mainMenu();
			break;
		case Form_property:
			doForm_property();
			break;
		case Form_task:
			doForm_task();
			break;
		case Form_equip:
			doForm_equip();
			break;
		case Form_make:
			doForm_make();
			break;
		case Form_GOODS:
			doForm_GOODS();
			break;
		case Form_SKILL:
			doForm_SKILL();
			break;
		// case Form_systemMenu:
		// doForm_systemMenu();
		// break;
		case Form_shop:
			doForm_shop();
			break;
		}
	}

	private static void show(Graphics g, int frameName) {
		// MessageCharge.drawBack(g, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		switch (frameName) {
		case Form_mainMenu:
			drawForm_mainMenu(g);
			break;
		case Form_property:
			drawForm_property(g);
			break;
		case Form_task:
			drawForm_task(g);
			break;
		case Form_equip:
			drawForm_equip(g);
			break;
		case Form_make:
			drawForm_make(g);
			break;
		case Form_GOODS:
			drawForm_GOODS(g);
			break;
		case Form_SKILL:
			drawForm_SKILL(g);
			break;
		// case Form_systemMenu:
		// drawForm_systemMenu(g);
		// break;
		case Form_shop:
			drawForm_shop(g);
			break;
		}

		// #if !N73
		// #
		// # //����ȷ�������ذ�ť
		// #if keymode_moto1||keymode_moto2||keymode_e680
		// # if (curPointForm!=Form_mainMenu) {
		// # drawFrame(g, Form_Moto1);
		// # }
		// #
		// # if (curPointForm==Form_buyMenu) {
		// # drawFrame(g, Form_Moto2);
		// # }
		// #else
		// # if (curPointForm!=Form_mainMenu) {
		// # drawFrame(g, Form_Nokia1);
		// # }
		// # if (curPointForm==Form_buyMenu) {
		// # drawFrame(g, Form_Nokia2);
		// # }
		// #endif
		// #
		// #endif

	}

	public static void drawFrame(Graphics g, int frameID) {
		for (int i = 0; i < UIdata.m_formData[frameID].length; i++) {
			UIdata.drawBlock(g, frameID, i);
		}
	}

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
	// ���峣����
	static final int BGCOLOR = -1;
	// static final int TXTCOLOR = 0Xffcc77;
	static final int TXTCOLOR = 0x03ACFF;
	// form 0
	// form 0����
	static final int BUTTON_NUM = 6;
	// form 0������
	public static int form0_index = 0;

	// .....................................
	private static void initForm_mainMenu() {
		// if (CGame.m_preState==CGame.GST_GAME_RUN) {
		// form0_index = 0;
		// }
		curHero = CGame.m_hero;
		// ....
	}

	private static void doForm_mainMenu() {
		if (CKey.isKeyPressed(CKey.GK_LEFT)) {
			if (form0_index > 0) {
				form0_index--;
			} else {
				form0_index = BUTTON_NUM - 1;
			}
		}
		if (CKey.isKeyPressed(CKey.GK_RIGHT)) {
			if (form0_index < BUTTON_NUM - 1) {
				form0_index++;
			} else {
				form0_index = 0;
			}
		}

		if (CKey.isKeyPressed(CKey.GK_OK)) {
			setCurrent(form0_index + 1);
			// #if mode_noSound
			// # // if (curFrame==Form_systemMenu) {
			// # // curPointForm=Form_noMusic;
			// # // }
			// #endif
		}

		if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			CGame.setGameState(dGame.GST_GAME_RUN);
			SoundPlayer.playSingleSound(SoundPlayer.getCurMidiID(), -1);
		}

		// ....
	}

	private static void drawForm_mainMenu(Graphics g) {
		UIdata.drawBlock(g, Form_mainMenu, B_mainMenu_or1);
		UIdata.drawSLAni(g, Form_mainMenu, B_mainMenu_bt1 + form0_index,
				new short[] { 0, 0 });
		UIdata.drawBlock(g, Form_mainMenu, B_mainMenu_or3);
	}

	private static void initForm_property() {
		// ....
	}

	private static void doForm_property() {
		if (CKey.isKeyPressed(CKey.GK_CANCEL) || CKey.isKeyPressed(CKey.GK_OK)) {
			setCurrent(Form_mainMenu);
		}

		// ....
	}

	private static void drawForm_property(Graphics g) {
		drawFrame(g, Form_property);

		UIdata.drawTxt(g, CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_DEF]
				+ "", Form_property, B_property_tb7, 0, TXTCOLOR, -1,
				B_property_tb7);
		UIdata.drawTxt(g, CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_ATT]
				+ "", Form_property, B_property_tb6, 0, TXTCOLOR, -1,
				B_property_tb6);
		UIdata.drawTxt(g,
				CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_CRI_RATE] + "%",
				Form_property, B_property_tb8, 0, TXTCOLOR, -1, B_property_tb8);

		UIdata.drawTxt(g, CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_MP]
				+ "/" + CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_MAX_MP],
				Form_property, B_property_tb5, 0, TXTCOLOR, -1, B_property_tb5);

		UIdata.drawTxt(g, CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_HP]
				+ "/" + CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_MAX_HP],
				Form_property, B_property_tb4, 0, TXTCOLOR, -1, B_property_tb4);

		UIdata.drawTxt(g, CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_EXP]
				+ "/"
				+ CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_NEXT_EXP],
				Form_property, B_property_tb3, 0, TXTCOLOR, -1, B_property_tb3);
		UIdata.drawTxt(g, CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_LEVEL]
				+ "", Form_property, B_property_tb2, 0, TXTCOLOR, -1, -1);
		UIdata.drawTxt(g, CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_MONEY]
				+ "", Form_property, B_property_tb1, 0, TXTCOLOR, -1, -1);

		// ͷ�񣬵���һ��form
		if (CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_ROLE_ID] == 0)
			drawFrame(g, Form_yanqing);
		else
			drawFrame(g, Form_likui);
	}

	/******************************************************************
	 * �������
	 ******************************************************************/
	static String[][] strTaskInf; // ��������ƺ�����
	static int taskStartIndex; // [0,strTaskInf.length-TASK_ROW]
	static int taskCursor; // ���
	static final int TASK_MAX_ROW = 4; // ���ͬʱ��ʾ4������

	private static void initForm_task() {
		// ��ȡ��Ҫ��ʾ������id
		int[] taskIds = Script.getActiveTaskId();
		strTaskInf = new String[taskIds.length][2];
		for (int i = 0; i < taskIds.length; i++) {
			strTaskInf[i][0] = Script.strSM[taskIds[i] * 2];
			if (Script.systemTasks[taskIds[i]] == 99) {
				strTaskInf[i][0] += "(δ�ύ)";
			} else {
				strTaskInf[i][0] += "(δ���)";
			}
			strTaskInf[i][1] = Script.strSM[taskIds[i] * 2 + 1];
		}
		taskStartIndex = 0;
		taskCursor = 0;
		UIdata.listID = -1;
		UIdata.setHScrollIndex(0);
	}

	private static void doForm_task() {
		if (CKey.isKeyPressed(CKey.GK_CANCEL) || CKey.isKeyPressed(CKey.GK_OK)) {
			setCurrent(Form_mainMenu);
		}
		// ����ƶ�
		else if (CKey.isKeyPressed(CKey.GK_UP)) {
			if (taskCursor > 0) {
				taskCursor--;
			} else {
				if (taskStartIndex > 0) {
					taskStartIndex--;
				}
			}
			UIdata.setHScrollIndex(taskCursor + taskStartIndex);
		} else if (CKey.isKeyPressed(CKey.GK_DOWN)) {
			int len = Math.min(TASK_MAX_ROW, strTaskInf.length);
			if (taskCursor < len - 1) {
				taskCursor++;
			} else {
				if (taskStartIndex < strTaskInf.length - TASK_MAX_ROW) {
					taskStartIndex++;
				}
			}
			UIdata.setHScrollIndex(taskCursor + taskStartIndex);
		}
	}

	private static void drawForm_task(Graphics g) {
		drawFrame(g, Form_task);
		if (strTaskInf.length > 0) {
			UIdata.drawSLAni(g, Form_task, B_task_bt4 + taskCursor, null);
		}

		// ��������
		int len = Math.min(TASK_MAX_ROW, strTaskInf.length);
		for (int i = 0; i < len; i++) {
			short[] block = UIdata.getBlock(Form_task, B_task_tb6 + i);
			Rect r = new Rect(block[0] - UIdata.UI_offset_X, block[1]
					- UIdata.UI_offset_Y, block[2], block[3]);
			UIdata.drawHScrollStr(g, strTaskInf[taskStartIndex + i][0], r,
					TXTCOLOR, BGCOLOR, i + taskStartIndex);
		}
		// ��������
		if (taskCursor + taskStartIndex < strTaskInf.length) {
			UIdata.drawTxt(g, strTaskInf[taskCursor + taskStartIndex][1],
					Form_task, B_task_tb5, UIdata.getAnchor(Form_task,
							B_task_tb5), TXTCOLOR, BGCOLOR, taskCursor
							+ taskStartIndex);
		}
	}

	private static final String[] strWeaponTYPE = { "����", "����", "��Ʒ" };
	private static final String strDrop = "����";
	private static final String strReturn = "����";
	private static final String strNoEquip = "δװ��";
	static short weaponDifference[] = new short[5]; // װ�����ԱȽ�
	private static boolean weaponIsSelect = false; // ��������˵�
	private static int heroCurKey; // ��������װ��

	static boolean bLevelLow; // �ȼ�����
	static String strLevelLow = "�ȼ��������޷�װ��";

	private static void initForm_equip() {
		weaponIsSelect = false;
		updateDifference(null);
		curList.setCurrent(List.LIST_HERO_EQUIP, true);
	}

	private static void doForm_equip() {
		// �ȼ��������޷�װ��
		if (bLevelLow) {
			if (CKey.isAnyKeyPressed()) {
				bLevelLow = false;
			}
			return;
		}
		Goods selectEquip = null;
		boolean move = curList.navigate();
		selectEquip = curList.curItem();
		// װ���Ա�
		if (move && weaponIsSelect) {
			updateDifference(selectEquip);
		}

		// �����˵��Ĳ���
		if (weaponIsSelect) {
			if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
				weaponIsSelect = false;
				updateDifference(null);
				curList.setCurrent(List.LIST_HERO_EQUIP, false);
			}
			if (CKey.isKeyPressed(CKey.GK_OK)) {
				if (curList.index() == 0) {
					// ж�¡�����
					// ˮ��У�����ж��װ���Ĺ���
					// curHero.putOffEquip(heroCurKey);
				} else {
					if (!curHero.putOnEquip(selectEquip)) {
						bLevelLow = true;
						return;
					}
				}
				weaponIsSelect = false;
				updateDifference(null);
				curList.setCurrent(List.LIST_HERO_EQUIP, false);
			}
			return;
		}
		// ����
		if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			// ����Ǵӽű��������ؽű�״̬
			if (CGame.m_preState == dGame.GST_SCRIPT_DIALOG
					|| CGame.m_preState == dGame.GST_SCRIPT_RUN
					|| CGame.m_preState == dGame.GST_SCRIPT_OPDLG) {
				CGame.setGameState(dGame.GST_SCRIPT_RUN);
			} else {
				setCurrent(Form_mainMenu);
			}
		}
		// ��������˵�
		else if (CKey.isKeyPressed(CKey.GK_OK)) {
			weaponIsSelect = true;
			switch (curList.index()) {
			case 0:
				curList.setCurrent(List.LIST_WEAPON, true);
				heroCurKey = curHero.m_actorProperty[IObject.PRO_INDEX_WEAPON];
				break;
			case 1:
				curList.setCurrent(List.LIST_ARMOR, true);
				heroCurKey = curHero.m_actorProperty[IObject.PRO_INDEX_LORICAE];
				break;
			case 2:
				curList.setCurrent(List.LIST_JADE, true);
				heroCurKey = curHero.m_actorProperty[IObject.PRO_INDEX_JADE];
				break;
			}
		}
	}

	private static void drawForm_equip(Graphics g) {
		drawFrame(g, Form_equip);
		// if (true) {
		// return;
		// }
		// װ���Ա�
		for (int i = 0; i < weaponDifference.length; i++) {
			int property = 0;
			switch (i) {
			case 4:
				property = IObject.PRO_INDEX_CRI_RATE;
				break;
			case 3:
				property = IObject.PRO_INDEX_DEF;
				break;
			case 2:
				property = IObject.PRO_INDEX_ATT;
				break;
			case 1:
				property = IObject.PRO_INDEX_MAX_MP;
				break;
			case 0:
				property = IObject.PRO_INDEX_MAX_HP;
				break;
			}
			// ������ɫ����-�̣���-��
			int color = 0;
			String change = " ";
			if (weaponDifference[i] > 0) {
				color = dConfig.COLOR_GREEN;
				change = "��";
			} else if (weaponDifference[i] < 0) {
				color = dConfig.COLOR_RED;
				change = "��";
			} else
				color = TXTCOLOR;
			// ����,ˮ������
			if (i == 4) {
				UIdata.drawTxt(g, CGame.m_hero.m_actorProperty[property]
						+ weaponDifference[i] + "%", Form_equip, B_equip_tb26,
						0, color, BGCOLOR, B_equip_tb4 - i);
				UIdata.drawTxt(g, change, Form_equip, B_equip_tb27, 0, color,
						BGCOLOR, B_equip_tb23 - i);
			} else {
				UIdata.drawTxt(g, CGame.m_hero.m_actorProperty[property]
						+ weaponDifference[i] + "", Form_equip,
						B_equip_tb4 - i, 0, color, BGCOLOR, B_equip_tb4 - i);
				UIdata.drawTxt(g, change, Form_equip, B_equip_tb23 - i, 0,
						color, BGCOLOR, B_equip_tb23 - i);
			}
		}
		// �����˵�
		if (weaponIsSelect) {
			int num = Math.min(curList.showRow(), curList.size());
			// ѡ�еĹ��
			UIdata.drawSLAni(g, Form_equip,
					B_equip_bt4 + curList.cursorIndex(), null);
			for (int i = 0; i < num; i++) {
				// ��һ�У���ʾж�¡�����
				if (i + curList.startIndex() == 0) {
					String s = heroCurKey < 0 ? strReturn : strDrop;
					UIdata.drawTxt(g, s, Form_equip, B_equip_bt4, 0, TXTCOLOR,
							BGCOLOR, B_equip_bt4);
				} else {
					Goods equip = curList.elementAt(i
							+ curList.startIndex());
					UIdata.drawTxt(g, "װ��" + equip.getName(), Form_equip,
							B_equip_bt4 + i, 0, TXTCOLOR, BGCOLOR, B_equip_bt4);
				}
			}
		} else {

			// ѡ�еĹ��
			UIdata.drawSLAni(g, Form_equip,
					B_equip_bt4 + curList.cursorIndex(), null);
			Goods temp = null;
			for (int i = 0; i < curList.showRow(); i++) {
				UIdata.drawTxt(g, strWeaponTYPE[i + curList.startIndex()],
						Form_equip, B_equip_tb15 + i * 3, 0, TXTCOLOR, BGCOLOR,
						B_equip_tb15);
				temp = curList.elementAt(i + curList.startIndex());
				if (temp != null) {
					// ������
					UIdata.drawTxt(g, temp.getName(), Form_equip, B_equip_tb17
							+ i * 3, 0, TXTCOLOR, BGCOLOR, B_equip_tb17);
					// �м����ͼ��
					setIconPlayer(Form_equip, B_equip_tb16 + i * 3, temp
							.getIconID());
					CGame.drawIconFrame(g, iconPlayer);
				} else {
					UIdata.drawTxt(g, strNoEquip, Form_equip, B_equip_tb17 + i
							* 3, 0, TXTCOLOR, BGCOLOR, B_equip_tb17);
				}
			}
		}
		// ������
		if (curList.size() > 0) {
			drawScrollBar(g, Form_equip, B_equip_sb1, curList.size(), curList
					.showRow(), curList.index(), dConfig.COLOR_RED);
		}

		if (curList.curItem() != null) {
			// װ����ϸ����
			UIdata.drawTxt(g, curList.curItem().getDescParticular(),
					Form_equip, B_equip_tb14, dConfig.ANCHOR_HT, TXTCOLOR,
					BGCOLOR, B_equip_bt4 + curList.index());
		}
		if (bLevelLow) {
			CTools.promptString(g, strLevelLow, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		}
	}

	/******************************************************************
	 * �������
	 ******************************************************************/
	static Goods[] heroWeapons; // ������������
	static int makeWeaponIndex;
	static String strWeaponName, strNeedStoneInf, strUpLevelResult;// ��Ҫ��ʾ���ı�
	static boolean bMakeSuccess;
	static boolean bIsMaxLevel;
	static boolean bNotEnoughStone;
	// ���־�̬�Ի���
	static boolean bDlgMakeSuccess;
	static String strMakeSuccess = "��ϲ����Ƕ�ɹ���";
	static boolean bDlgIsMaxLevel;
	static String strIsMaxLevel = "װ���������";
	static boolean bDlgNotEnoughStone;
	static String strNotEnoughStone = "��ʯ����";
	static boolean bDlgMakeFailed;
	static String strMakeFailed = "���ź�������ʧ��";

	private static void initForm_make() {
		heroWeapons = CGame.m_hero.getCurEquips();
		if (heroWeapons == null) {
			return;
		}
		makeWeaponIndex = 0;
		bMakeSuccess = bIsMaxLevel = bNotEnoughStone = false;
		updateForm_make();
	}

	private static void exitForm_make() {
	}

	/**
	 * ˮ���Ŀ�У�ֻ�ܴ����������ϵ�����װ��
	 */
	private static void doForm_make() {
		// �ɹ�������
		if (bDlgMakeSuccess) {
			if (CKey.isAnyKeyPressed()) {
				updateForm_make();
				bDlgMakeSuccess = false;
			}
			return;
		}
		// ��ʯ����������
		if (bDlgNotEnoughStone) {
			if (CKey.isAnyKeyPressed()) {
				bDlgNotEnoughStone = false;
			}
			return;
		}
		// ����������
		if (bDlgIsMaxLevel) {
			if (CKey.isAnyKeyPressed()) {
				bDlgIsMaxLevel = false;
			}
			return;
		}
		// ʧ�ܵ�����
		if (bDlgMakeFailed) {
			if (CKey.isAnyKeyPressed()) {
				updateForm_make();
				bDlgMakeFailed = false;
			}
			return;
		}

		if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			// ����Ǵӽű��������ؽű�״̬
			if (CGame.m_preState == dGame.GST_SCRIPT_DIALOG
					|| CGame.m_preState == dGame.GST_SCRIPT_RUN
					|| CGame.m_preState == dGame.GST_SCRIPT_OPDLG) {
				CGame.setGameState(dGame.GST_SCRIPT_RUN);
			} else {
				setCurrent(Form_mainMenu);
			}
		}
		if (heroWeapons == null) {
			return;
		}

		// ��������
		if (CKey.isKeyPressed(CKey.GK_OK)) {
			// �Ѿ�����
			if (bIsMaxLevel) {
				bDlgIsMaxLevel = true;
				return;
			}// ��ʯ����
			else if (bNotEnoughStone) {
				bDlgNotEnoughStone = true;
				return;
			} else {
				int probability = CTools.random(1, 100);
				int newLevel = heroWeapons[makeWeaponIndex].property[Goods.PRO_LEVEL] + 1;
				// -----------------�ɹ�
				if (probability <= Goods
						.getSuccessProbability(newLevel)) {
					short[] proChange = heroWeapons[makeWeaponIndex].levelUp();
					// ���������װ�����������Ķ���������
					int key = heroWeapons[makeWeaponIndex].getKey();
					int heroWeaponKey = CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_WEAPON];
					if (key == heroWeaponKey) {
						for (int i = 0; i < proChange.length; i++) {
							CGame.m_hero.m_actorProperty[i] += proChange[i];
						}
					}
					// ���ı�ʯ
					CGame.m_hero.dropItem(
							(1 << 12)
									| heroWeapons[makeWeaponIndex]
											.getRequiredStoneId(),
							heroWeapons[makeWeaponIndex]
									.getRequiredStoneNum(newLevel));
					bDlgMakeSuccess = true;
					return;
				}
				// -----------------ʧ��
				else {
					// ���ı�ʯ
					CGame.m_hero.dropItem(
							(1 << 12)
									| heroWeapons[makeWeaponIndex]
											.getRequiredStoneId(),
							heroWeapons[makeWeaponIndex]
									.getRequiredStoneNum(newLevel));
					bDlgMakeFailed = true;
					return;
				}

			}
		}
		if (heroWeapons.length < 2) {
			return;
		}
		if (CKey.isKeyPressed(CKey.GK_LEFT)) {
			makeWeaponIndex--;
			if (makeWeaponIndex < 0) {
				makeWeaponIndex = heroWeapons.length - 1;
			}
			updateForm_make();
		} else if (CKey.isKeyPressed(CKey.GK_RIGHT)) {
			makeWeaponIndex++;
			if (makeWeaponIndex >= heroWeapons.length) {
				makeWeaponIndex = 0;
			}
			updateForm_make();
		}
	}

	private static void drawForm_make(Graphics g) {
		drawFrame(g, Form_make);
		if (heroWeapons == null) {
			return;
		}

		// ͼ��
		AniData.setMlgs(ResLoader.aniMlgs);
		setIconPlayer(Form_make, B_make_tb8, heroWeapons[makeWeaponIndex]
				.getIconID());
		CGame.drawIconFrame(g, iconPlayer);

		// ����64466
		UIdata.drawTxt(g, strWeaponName, Form_make, B_make_tb10,
				dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR, B_make_tb10);

		short[] block = UIdata.getBlock(Form_make, B_make_tb1);
		UITools.drawVScrollString(g, strNeedStoneInf, 0, block[0]
				- UIdata.UI_offset_X, block[1] - UIdata.UI_offset_Y, block[2],
				block[3], dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR);

		UIdata.drawTxt(g, strUpLevelResult, Form_make, B_make_tb9,
				dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR, B_make_tb9);

		if (bDlgMakeSuccess) {
			CTools.promptString(g, strMakeSuccess, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		}
		if (bDlgNotEnoughStone) {
			CTools.promptString(g, strNotEnoughStone, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		}
		if (bDlgIsMaxLevel) {
			CTools.promptString(g, strIsMaxLevel, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		}
		if (bDlgMakeFailed) {
			CTools.promptString(g, strMakeFailed, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		}

	}

	public static void updateForm_make() {
		if (heroWeapons == null) {
			return;
		}
		UIdata.listID = -1;
		Goods curWeapon = heroWeapons[makeWeaponIndex];
		strWeaponName = curWeapon.getName()/*
											 * +" "+curWeapon.property[Goods.PRO_LEVEL
											 * ]+"��"
											 */;
		// ��������
		if (curWeapon.property[Goods.PRO_LEVEL] == curWeapon.property[Goods.PRO_MAX_LEVEL]) {
			strNeedStoneInf = "װ�����������";
			strUpLevelResult = " ";
			bIsMaxLevel = true;
		} else {
			int newLevel = curWeapon.property[Goods.PRO_LEVEL] + 1;
			int weaponType = curWeapon.getDetailType();
			String stoneName = weaponType == Data.IDX_EP_weapon ? "����֮ʯ"
					: (weaponType == Data.IDX_EP_loricae ? "����֮ʯ" : "��ɱ֮ʯ");
			int requiedStoneNum = curWeapon.getRequiredStoneNum(newLevel);
			int heroHasStoneNum = CGame.m_hero.getEquipStoneNum(curWeapon);
			// ��ʯ����
			if (requiedStoneNum > heroHasStoneNum) {
				bNotEnoughStone = true;
			} else {
				bNotEnoughStone = false;
			}
			// ������Ҫ�ı�ʯ��Ϣ
			strNeedStoneInf = "��" + stoneName + heroHasStoneNum + "&" + "����Ƕ"
					+ (curWeapon.property[Goods.PRO_LEVEL] - 1) + "/"
					+ (curWeapon.property[Goods.PRO_MAX_LEVEL] - 1) + "&�ɹ��ʣ�"
					+ (Goods.getSuccessProbability(newLevel)) + "%";
			// ��ʱ�������󣬴�����������������
			Goods newWeaponTemp = new Goods();
			System.arraycopy(curWeapon.affectProperty, 0,
					newWeaponTemp.affectProperty, 0,
					curWeapon.affectProperty.length);

			System.arraycopy(curWeapon.property, 0, newWeaponTemp.property, 0,
					curWeapon.property.length);

			newWeaponTemp.levelUp();
			int attDifference = newWeaponTemp.affectProperty[Data.IDX_PRO_atk]
					- curWeapon.affectProperty[Data.IDX_PRO_atk];
			int defDifference = newWeaponTemp.affectProperty[Data.IDX_PRO_def]
					- curWeapon.affectProperty[Data.IDX_PRO_def];
			int criDifference = newWeaponTemp.affectProperty[Data.IDX_PRO_cri_rate]
					- curWeapon.affectProperty[Data.IDX_PRO_cri_rate];

			// ��������ֵ�Ա�
			if (weaponType == Data.IDX_EP_weapon) {
				strUpLevelResult = "���� + " + attDifference;
			} else if (weaponType == Data.IDX_EP_loricae) {
				strUpLevelResult = "���� + " + defDifference;
			} else {
				strUpLevelResult = "���� + " + (criDifference) + "%";
			}
			bIsMaxLevel = false;
		}
	}

	private static final int GOOD_TYPE_POTION = 0;
	private static final int GOOD_TYPE_EQUIP = 1;
	private static final int GOOD_TYPE_STONE = 2;
	private static final int GOOD_TYPE_SPECIAL = 3;
	private static final int GOOD_TYPE_LENGTH = 4;
	private static int good_typeIndex = 0;// ����
	// ��������˵�
	private static boolean good_isSelect = false;
	// ѡ����ۻ�ʹ��
	private static int good_selectIndex;
	// ������۽��棬�����˵�
	private static boolean good_isSell = false;
	private static int good_SellCount = 1;
	private static String strSell = "����";
	// ���ܳ���
	private static boolean bCanNotSell;
	private static String strCanNotSell = "�޷����ۣ�";

	private static void initForm_GOODS() {
		good_isSell = false;
		good_isSelect = false;
		good_typeIndex = 0;
		curList.setCurrent(List.LIST_POTION, true);
	}

	private static void doForm_GOODS() {
		// ���ܳ��۵�����
		if (bCanNotSell) {
			if (CKey.isAnyKeyPressed()) {
				bCanNotSell = false;
			}
			return;
		}

		// �����������
		if (good_isSell) {
			if (CKey.isKeyPressed(CKey.GK_LEFT)) {
				if (good_SellCount > 1) {
					good_SellCount--;
				} else {
					good_SellCount = curList.curItem().getCount();
				}
			} else if (CKey.isKeyPressed(CKey.GK_RIGHT)) {
				if (good_SellCount < curList.curItem().getCount()) {
					good_SellCount++;
				} else {
					good_SellCount = 1;
				}
			} else if (CKey.isKeyPressed(CKey.GK_OK)) {
				switch (good_typeIndex) {
				case GOOD_TYPE_EQUIP:
					curHero.dropAEquip(curList.curItem(), good_SellCount);
					break;
				default:
					curHero.dropItem(curList.curItem(), good_SellCount);
					break;
				}
				// ���
				curHero.m_actorProperty[IObject.PRO_INDEX_MONEY] += good_SellCount
						* curList.curItem().getPrice() / 2;
				good_isSell = false;
				curPointForm = Form_GOODS;
				curList.update();
			} else if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
				good_isSell = false;
				curPointForm = Form_GOODS;
			}
			return;
		}

		// ��������ѡ����ۻ�ʹ��
		if (good_isSelect) {
			if (CKey.isKeyPressed(CKey.GK_UP)
					|| CKey.isKeyPressed(CKey.GK_DOWN)) {
				good_selectIndex = good_selectIndex == 0 ? 1 : 0;
			}
			if (CKey.isKeyPressed(CKey.GK_OK)) {
				switch (good_selectIndex) {
				case 0:// ʹ��
					good_isSelect = false;
					CGame.m_hero.useItem(curList.curItem());
					curList.update();
					curPointForm = Form_GOODS;
					break;
				case 1:// ����
					good_isSelect = false;
					good_isSell = true;
					curPointForm = Form_buyMenu;
					good_SellCount = 1;
					break;
				}
			}
			if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
				good_isSelect = false;
				curPointForm = Form_GOODS;
			}
			return;
		}

		// һ������
		curList.navigate();
		int tempType = good_typeIndex;
		// ����ѡ����Ʒ����
		if (CKey.isKeyPressed(CKey.GK_LEFT)) {
			if (good_typeIndex > 0) {
				good_typeIndex--;
			} else {
				good_typeIndex = GOOD_TYPE_LENGTH - 1;
			}
		}
		if (CKey.isKeyPressed(CKey.GK_RIGHT)) {
			if (good_typeIndex < GOOD_TYPE_LENGTH - 1) {
				good_typeIndex++;
			} else {
				good_typeIndex = 0;
			}
		}
		// �л�list
		if (tempType != good_typeIndex) {
			UIdata.listID = -1;
			switch (good_typeIndex) {
			case GOOD_TYPE_POTION:
				curList.setCurrent(List.LIST_POTION, true);
				break;
			case GOOD_TYPE_EQUIP:
				curList.setCurrent(List.LIST_EQUIP, true);
				break;
			case GOOD_TYPE_STONE:
				curList.setCurrent(List.LIST_STONE, true);
				break;
			case GOOD_TYPE_SPECIAL:
				curList.setCurrent(List.LIST_SPECIAL, true);
				break;
			}
		}

		if (CKey.isKeyPressed(CKey.GK_OK)) {
			if (curList.size() <= 0) {
				return;
			}
			Goods g = curList.curItem();
			switch (good_typeIndex) {
			case GOOD_TYPE_POTION: // ҩƷ.
				good_isSelect = true;
				good_selectIndex = 0;
				curPointForm = Form_USE;
				break;
			case GOOD_TYPE_EQUIP: // װ��
			case GOOD_TYPE_STONE: // ��ʯ.
			case GOOD_TYPE_SPECIAL: // ����.
				// Լ�����۸�0����Ʒ���ܳ���
				if (g.getPrice() > 0) {
					good_isSell = true;
					curPointForm = Form_buyMenu;
					good_SellCount = 1;
				} else {
					bCanNotSell = true;
					return;
				}
				break;
			}
		}
		if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			setCurrent(Form_mainMenu);
		}
	}

	private static void drawForm_GOODS(Graphics g) {
		int cruRect = B_GOODS_bt2 + good_typeIndex;
		UIdata.setAniID(Form_GOODS, cruRect, UIdata.getSLAni(Form_GOODS,
				cruRect));
		UIdata.setActionID(Form_GOODS, cruRect, UIdata.getSLAction(Form_GOODS,
				cruRect));

		drawFrame(g, Form_GOODS);
		UIdata.setAniID(Form_GOODS, cruRect, (short) -1);
		UIdata.setActionID(Form_GOODS, cruRect, (short) -1);

		int len = Math.min(curList.size(), curList.showRow());
		Goods gg = null;
		// ѡ�еĹ��
		UIdata.drawSLAni(g, Form_GOODS, B_GOODS_bt6 + curList.cursorIndex(),
				null);
		for (int i = 0; i < len; i++) {
			gg = curList.elementAt(i + curList.startIndex());
			UIdata.drawTxt(g, gg.getName(), Form_GOODS, B_GOODS_tb8 + i * 3, 0,
					TXTCOLOR, BGCOLOR, B_GOODS_tb8);
			UIdata.drawTxt(g, gg.getCount() + "", Form_GOODS, B_GOODS_tb14 + i
					* 3, 0, TXTCOLOR, BGCOLOR, B_GOODS_tb14);
			// �м����ͼ��
			setIconPlayer(Form_GOODS, B_GOODS_tb9 + i * 3, gg.getIconID());
			CGame.drawIconFrame(g, iconPlayer);
		}
		// ��ϸ����
		if (curList.curItem() != null) {
			UIdata.drawTxt(g, curList.curItem().getDescParticular(),
					Form_GOODS, B_GOODS_tb5, dConfig.ANCHOR_HT, TXTCOLOR,
					BGCOLOR, curList.index());
		}
		// ������
		if (curList.size() > 0) {
			drawScrollBar(g, Form_GOODS, B_GOODS_sb1, curList.size(), curList
					.showRow(), curList.index(), dConfig.COLOR_RED);
		}

		if (good_isSelect) {
			UIdata.setAniID(Form_USE, B_USE_bt3 + good_selectIndex, UIdata
					.getSLAni(Form_USE, B_USE_bt3 + good_selectIndex));
			UIdata.setActionID(Form_USE, B_USE_bt3 + good_selectIndex, UIdata
					.getSLAction(Form_USE, B_USE_bt3 + good_selectIndex));
			drawFrame(g, Form_USE);
			UIdata.setAniID(Form_USE, B_USE_bt3 + good_selectIndex, (short) -1);
			UIdata.setActionID(Form_USE, B_USE_bt3 + good_selectIndex,
					(short) -1);
			// �������������
			// UIdata.drawTxt(g,curHero.m_actorProperty[curHero.PRO_INDEX_HP]+"/"+curHero.m_actorProperty[curHero.PRO_INDEX_MAX_HP],Form_USE,B_USE_tb1,dConfig.ANCHOR_HT,TXTCOLOR,BGCOLOR,B_USE_tb1);
			// UIdata.drawTxt(g,curHero.m_actorProperty[curHero.PRO_INDEX_MP]+"/"+curHero.m_actorProperty[curHero.PRO_INDEX_MAX_MP],Form_USE,B_USE_tb2,dConfig.ANCHOR_HT,TXTCOLOR,BGCOLOR,B_USE_tb2);
			// UIdata.drawTxt(g,curHero.m_actorProperty[curHero.PRO_INDEX_EXP]+"/"+curHero.m_actorProperty[curHero.PRO_INDEX_NEXT_EXP],Form_USE,B_USE_tb3,dConfig.ANCHOR_HT,TXTCOLOR,BGCOLOR,B_USE_tb3);
		} else if (good_isSell) {
			drawFrame(g, Form_buyMenu);
			UIdata.drawTxt(g, strSell, Form_buyMenu, B_buyMenu_tb4,
					dConfig.ANCHOR_HT, dConfig.COLOR_BLUE2, BGCOLOR,
					B_buyMenu_tb4);
			// ����
			UIdata.drawTxt(g, curList.curItem().getName(), Form_buyMenu,
					B_buyMenu_tb2, dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR,
					B_buyMenu_tb2);
			// ����
			UIdata.drawTxt(g, good_SellCount + "", Form_buyMenu, B_buyMenu_tb1,
					dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR, B_buyMenu_tb1);
			// ���ۼ�
			UIdata.drawTxt(g,
					(good_SellCount * curList.curItem().getPrice() / 2) + "",
					Form_buyMenu, B_buyMenu_tb3, dConfig.ANCHOR_HT, TXTCOLOR,
					BGCOLOR, B_buyMenu_tb3);
		}
		if (bCanNotSell) {
			CTools.promptString(g, strCanNotSell, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		}
	}

	// form 6 ���ܽ���
	private static boolean bSkillUp;
	private static String strSkillUp = "��ϲ��������������";
	private static boolean bNoSp;
	private static String strNoSp = "û�м��ܵ���";
	private static boolean bSkillIsMax;
	private static String strSkillIsMax = "�����Ѵ���ߵȼ�";

	private static void initForm_SKILL() {
		curList.setCurrent(List.LIST_SKILL, true);
		CKey.initKey();
	}

	private static void doForm_SKILL() {
		CHero hero = CGame.m_hero;
		// û���ܵ� �����Ѵ���ߵȼ�
		if (bNoSp || bSkillIsMax) {
			if (CKey.isAnyKeyPressed()) {
				bSkillUp = bNoSp = bSkillIsMax = false;
				CKey.initKey();
			}
			return;
		}
		// ----------------�����ɹ�
		if (bSkillUp) {
			if (CKey.isAnyKeyPressed()) {
				bSkillUp = bNoSp = bSkillIsMax = false;
				CKey.initKey();
				curList.update();
			}
		}

		if (curList.navigate()) {
			UIdata.setHScrollIndex(curList.index());
		}
		if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			// ����Ǵӽű��������ؽű�״̬
			if (CGame.m_preState == dGame.GST_SCRIPT_DIALOG
					|| CGame.m_preState == dGame.GST_SCRIPT_RUN
					|| CGame.m_preState == dGame.GST_SCRIPT_OPDLG) {
				CGame.setGameState(dGame.GST_SCRIPT_RUN);
			} else {
				setCurrent(Form_mainMenu);
			}
		} else if (CKey.isKeyPressed(CKey.GK_OK) || CKey.isKeyHold(CKey.GK_OK)) {
			if (hero.m_actorProperty[IObject.PRO_INDEX_SP] <= 0) {
				bNoSp = true;
				return;
			} else if (curList.curItem().getNextSkill() < 0) {
				bSkillIsMax = true;
				return;
			} else {
				int needSP = Data.SKILL_INFO[curList.curItem().getDataID()][Data.IDX_SKI_price];
				// ���ܵ�����
				hero.m_actorProperty[IObject.PRO_INDEX_SP]--;
				curList.curItem().property[Goods.PRO_LEVEL]++;
				// �Ƿ��Ѽ���
				if (curList.curItem().property[Goods.PRO_LEVEL] >= needSP) {
					Goods nextLevelSkill = Goods
							.createGoods(
									(short) 2,
									Data.SKILL_INFO[curList.curItem()
											.getDataID()][Data.IDX_SKI_upLvDest]);
					for (int i = 0; i < hero.skills.length; i++) {
						if (hero.skills[i] == curList.curItem()) {
							hero.skills[i] = nextLevelSkill;
						}
					}
					bSkillUp = true;
					CKey.initKey();
				}
			}
		}
	}

	private static void drawForm_SKILL(Graphics g) {
		drawFrame(g, Form_SKILL);
		// ʣ�༼�ܵ�
		UIdata.drawTxt(g, curHero.m_actorProperty[IObject.PRO_INDEX_SP] + "",
				Form_SKILL, B_SKILL_tb10, dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR,
				B_SKILL_tb10);

		// ��������
		UIdata.drawSLAni(g, Form_SKILL, B_SKILL_bt3 + curList.cursorIndex(),
				new short[] { 0, 0 });
		for (int i = 0; i < curList.size(); i++) {
			short[] block = UIdata.getBlock(Form_SKILL, B_SKILL_tb9 + i);
			Rect r = new Rect(block[0] - UIdata.UI_offset_X, block[1]
					- UIdata.UI_offset_Y, block[2], block[3]);
			// ���˶��ټ��ܵ�
			int needSP = Data.SKILL_INFO[curList.elementAt(
					i + curList.startIndex()).getDataID()][Data.IDX_SKI_price];
			int curSP = curList.elementAt(i + curList.startIndex()).property[Goods.PRO_LEVEL];
			if (needSP > 0) {
				int len = block[2] * curSP / needSP;
				g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
				g.setColor(dConfig.COLOR_SKILL_BAR);
				g.fillRoundRect(block[0] - UIdata.UI_offset_X, block[1]
						- UIdata.UI_offset_Y + 1, len, block[3] - 2, 3, 3);
			}
			UIdata.drawHScrollStr(g, curList
					.elementAt(i + curList.startIndex()).getName()
					+ " " + curSP + "/" + needSP, r, TXTCOLOR, BGCOLOR, i);

		}

		// ������ϸ����
		UIdata.drawTxt(g, curList.curItem().getDescParticular(), Form_SKILL,
				B_SKILL_tb6, dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR, curList
						.index());

		// ͷ�񣬵���һ��form
		if (CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_ROLE_ID] == 0)
			drawFrame(g, Form_yanqing);
		else
			drawFrame(g, Form_likui);

		if (bSkillUp) {
			CTools.promptString(g, strSkillUp, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		} else if (bNoSp) {
			CTools.promptString(g, strNoSp, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		} else if (bSkillIsMax) {
			CTools.promptString(g, strSkillIsMax, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		}

	}

	private static void update_skill() {

	}

	private static void initForm_systemMenu() {
		// ....
	}

	static int systemIndex;

	private static void doForm_systemMenu() {
		if (CKey.isKeyPressed(CKey.GK_UP)) {
			systemIndex--;
			if (systemIndex < 0) {
				systemIndex = 1;
			}
		} else if (CKey.isKeyPressed(CKey.GK_DOWN)) {
			systemIndex++;
			if (systemIndex > 1) {
				systemIndex = 0;
			}
		} else if (CKey.isKeyPressed(CKey.GK_OK)) {
			// #if mode_noSound
			// # CGame.destroyLevel(true);
			// # CGame.setGameState(CGame.GST_MAIN_MENU);
			// #else
			// ��������
			if (systemIndex == 0) {
				SoundPlayer.m_isMusicOn = !SoundPlayer.m_isMusicOn;
				if (!SoundPlayer.m_isMusicOn) {
					SoundPlayer.stopSingleSound();
				} else {
					SoundPlayer.playSingleSound(SoundPlayer.getCurMidiID(), -1);
				}
			}
			// �������˵�
			else if (systemIndex == 1) {
				CGame.destroyLevel(true);
				CGame.setGameState(dGame.GST_MAIN_MENU);
			}
			// #endif
		} else if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			setCurrent(Form_mainMenu);
		}
	}

	private static void drawForm_systemMenu(Graphics g) {
		// #if mode_noSound
		// # // drawFrame(g,Form_noMusic);
		// # // UIdata.drawSLAni(g,Form_noMusic,B_noMusic_bt2,null);
		// # //
		// UIdata.drawTxt(g,"�����˵�",Form_noMusic,B_noMusic_tb3,dConfig.ANCHOR_HT,TXTCOLOR,BGCOLOR,0);
		// #else
		// drawFrame(g,Form_systemMenu);
		// UIdata.drawTxt(g,"����",Form_systemMenu,B_systemMenu_tb1,dConfig.ANCHOR_HT,TXTCOLOR,BGCOLOR,
		// 0);
		// UIdata.drawSLAni(g,Form_systemMenu,B_systemMenu_bt1+systemIndex,null);
		// UIdata.drawTxt(g,SoundPlayer.m_isMusicOn?"���ֿ�":"���ֹ�",
		// Form_systemMenu,B_systemMenu_tb2,dConfig.ANCHOR_HT,TXTCOLOR,BGCOLOR,
		// 0);
		// UIdata.drawTxt(g,"�����˵�",Form_systemMenu,B_systemMenu_tb3,dConfig.ANCHOR_HT,TXTCOLOR,BGCOLOR,
		// 0);
		// #endif
	}

	// �̵���Ʊ���
	private static byte shopID = 0; // �̵���
	private static int shopTypeID = 0; // ��Ʒ����
	// ѡ�еĿ��Ʊ���
	private static boolean shopIsSelect = false; // ѡ�У����������
	private static int buyCount = 0;
	// ��ʾ��Ϣ
	private static boolean buySuc = false;
	private static boolean notEnoughMoney = false;
	private static String strBuySuc = "����ɹ�";
	private static String strNotEnoughMoneyShop = "��Ǯ����";

	public static void setShopId(byte id) {
		shopID = id;
	}

	public static byte curShopId() {
		return shopID;
	}

	public static void initForm_shop() {
		notEnoughMoney = false;
		buySuc = false;
		buyCount = 0;
		shopIsSelect = false;
		shopTypeID = 0;
		curList.setCurrent(List.LIST_EQUIP_SHOP, true);
	}

	public static void exitForm_shop() {
		curList.removeAllElements();
	}

	public static boolean fromUI; // ��UI����̴��̵�
	public static int fromUIID;
	public static boolean fromScript; // �ӽű�����̴��̵�

	public static void doForm_shop() {
		// ����ɹ�
		if (buySuc) {
			if (CKey.isAnyKeyPressed()) {
				buySuc = false;
			}
			return;
		}
		// ��Ǯ����
		if (notEnoughMoney) {
			if (CKey.isAnyKeyPressed()) {
				notEnoughMoney = false;
			}
			return;
		}
		// �����˵�
		if (shopIsSelect) {
			if (CKey.isKeyPressed(CKey.GK_LEFT)) {
				if (buyCount > 1)
					buyCount--;
			}
			if (CKey.isKeyPressed(CKey.GK_RIGHT)) {
				buyCount++;
			}
			int payMoney = curList.curItem().getPrice() * buyCount;
			if (CKey.isKeyPressed(CKey.GK_OK)) {
				// ��Ǯ����
				if (payMoney > curHero.m_actorProperty[IObject.PRO_INDEX_MONEY]) {
					notEnoughMoney = true;
					return;
				} else {
					shopIsSelect = false;
					buySuc = true;
					curHero.m_actorProperty[IObject.PRO_INDEX_MONEY] -= payMoney;
					switch (shopTypeID) {
					case Goods.TYPE_EQUIP:
						for (int i = 0; i < buyCount; i++) {
							curHero.addAEquip(curList.curItem());
						}
						break;
					case Goods.TYPE_ITEM:
						for (int i = 0; i < buyCount; i++) {
							curHero.addAItem(curList.curItem());
						}
						break;
					}
				}
				curPointForm = Form_shop;
			}
			if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
				shopIsSelect = false;
				curPointForm = Form_shop;
				buyCount = 0;
				payMoney = 0;
			}
			return;
		}

		if (CKey.isKeyPressed(CKey.GK_LEFT) || CKey.isKeyPressed(CKey.GK_RIGHT)) {
			shopTypeID = shopTypeID == 0 ? (byte) 1 : (byte) 0;
			curList.setCurrent(shopTypeID == 0 ? List.LIST_EQUIP_SHOP
					: List.LIST_ITEM_SHOP, true);
		} else {
			curList.navigate();
		}
		// ���ص��ű�״̬
		if (CKey.isKeyPressed(CKey.GK_CANCEL)) {
			exitForm_shop();
			setCurrent(Form_mainMenu);
			CGame.setGameState(dGame.GST_SCRIPT_RUN);
		} else if (CKey.isKeyPressed(CKey.GK_OK)) {
			buyCount = 1;
			shopIsSelect = true;
			curPointForm = Form_buyMenu;
		}
	}

	public static void drawForm_shop(Graphics g) {
		// UIdata.setAniID(Form_shop, B_shop_bt2 + shopTypeID,
		// UIdata.getSLAni(Form_shop, B_shop_bt2 + shopTypeID));
		// UIdata.setActionID(Form_shop, B_shop_bt2 + shopTypeID,
		// UIdata.getSLAction(Form_shop,B_shop_bt2 + shopTypeID));
		drawFrame(g, Form_shop);
		UIdata.drawSLAni(g, Form_shop, B_shop_bt2 + shopTypeID, new short[] {
				0, 0 });
		UIdata.drawBlock(g, Form_shop, B_shop_or15);
		UIdata.drawBlock(g, Form_shop, B_shop_or16);
		// UIdata.setAniID(Form_shop, B_shop_bt1 + shopTypeID, (short) - 1);
		// UIdata.setActionID(Form_shop, B_shop_bt1 + shopTypeID, (short) - 1);

		UIdata.drawTxt(g, curHero.m_actorProperty[IObject.PRO_INDEX_MONEY] + "",
				Form_shop, B_shop_tb7, dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR,
				B_shop_tb7);
		int num = Math.min(curList.showRow(), curList.size());
		UIdata.drawSLAni(g, Form_shop, B_shop_bt8 + curList.cursorIndex(),
				new short[] { 0, 0 });
		Goods temp = null;
		for (int i = 0; i < num; i++) {
			temp = curList.elementAt(i + curList.startIndex());
			UIdata.drawTxt(g, temp.getName(), Form_shop, B_shop_tb13 + i * 2,
					0, TXTCOLOR, BGCOLOR, B_shop_tb16);
			// icon
			setIconPlayer(Form_shop, B_shop_tb15 + i * 2, temp.getIconID());
			CGame.drawIconFrame(g, iconPlayer);
			// ����
			UIdata.drawTxt(g, temp.getPrice() + "", Form_shop, B_shop_tb18 + i,
					0, TXTCOLOR, BGCOLOR, B_shop_tb18);
		}
		if (curList.size() > 0) {
			drawScrollBar(g, Form_shop, B_shop_sb1, curList.size(), curList
					.showRow(), curList.index(), 0xff0000);
		}
		if (curList.curItem() != null) {
			UIdata.drawTxt(g, curList.curItem().getDescParticular(), Form_shop,
					B_shop_tb6, dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR, curList
							.index());
		}

		if (shopIsSelect) {
			drawFrame(g, Form_buyMenu);
			UIdata.drawTxt(g, "����", Form_buyMenu, B_buyMenu_tb4,
					dConfig.ANCHOR_HT, dConfig.COLOR_BLUE2, -1, B_buyMenu_tb4);
			// ����
			UIdata.drawTxt(g, curList.curItem().getName(), Form_buyMenu,
					B_buyMenu_tb2, dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR,
					B_buyMenu_tb2);
			// ����
			UIdata.drawTxt(g, buyCount + "", Form_buyMenu, B_buyMenu_tb1,
					dConfig.ANCHOR_HT, TXTCOLOR, BGCOLOR, B_buyMenu_tb1);
			// �ܼ�
			UIdata.drawTxt(g, curList.curItem().getPrice() * buyCount + "",
					Form_buyMenu, B_buyMenu_tb3, dConfig.ANCHOR_HT, TXTCOLOR,
					BGCOLOR, B_buyMenu_tb3);
		}

		if (buySuc) {
			CTools.promptString(g, strBuySuc, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		} else if (notEnoughMoney) {
			CTools.promptString(g, strNotEnoughMoneyShop, null, null, TXTCOLOR,
					dConfig.COLOR_BLACK);
		}
	}

	/*****************************************************************************
	 * ����������
	 *****************************************************************************/
	/**
	 * ���������� {pointForm,x,y,w,h,index(����100������������С��100����������)}
	 */
	private static final short[][][] POINT_RECT = {
	// mainMenu 0
			{ { 28, 240, 28 + 30, 270, 100 },
					{ 28 + 30, 240, 28 + 30 * 2, 270, 101 },
					{ 28 + 30 * 2, 240, 28 + 30 * 3, 270, 102 },
					{ 28 + 30 * 3, 240, 28 + 30 * 4, 270, 103 },
					{ 28 + 30 * 4, 240, 28 + 30 * 5, 270, 104 },
					{ 28 + 30 * 5, 240, 28 + 30 * 6, 270, 105 },
			// {28+30*6,240,28+30*7,270, 106},
			},
			// ���Խ��� 1
			{},
			// װ������ 2
			{ { 50, 140, 182, 160, 0 }, { 50, 160, 182, 180, 1 },
					{ 50, 180, 182, 200, 2 }, },
			// ��Ʒ���� 3
			{ { 54, 72, 79, 96, 100 }, { 88, 72, 114, 96, 101 },
					{ 124, 72, 150, 96, 102 }, { 160, 72, 185, 96, 103 },

					{ 49, 116, 180, 135, 0 }, { 49, 134, 180, 154, 1 },
					{ 49, 154, 180, 173, 2 }, { 49, 173, 180, 190, 3 },

			},
			// ������� 4
			{ { 82, 70, 107, 91, 0 }, { 133, 70, 153, 91, 0 }, },
			// ���ܽ��� 5
			{ { 48, 70, 179, 153, 0 }, { 48, 153, 179, 173, 1 },
					{ 48, 173, 179, 193, 2 }, },
			// ������� 6
			{ { 48, 82, 179, 82 + 20, 0 },
					{ 48, 82 + 20, 179, 82 + 20 * 2, 1 },
					{ 48, 82 + 20 * 2, 179, 82 + 20 * 3, 2 },
					{ 48, 82 + 20 * 3, 179, 82 + 20 * 4, 3 }, },
			// ϵͳ���� 7
			{ { 72, 138, 168, 158, 0 }, { 72, 178, 168, 198, 1 }, },
			// OKCANCEL 8
			{},
			// ʹ�û���� 9
			{ { 102, 122, 140, 141, 0 }, { 102, 140, 140, 158, 1 }, },
			// �̴� 10
			{},
			// �̵� 11
			{ { 45, 73, 83, 90, 100 }, { 83, 73, 122, 90, 101 },

			{ 50, 96, 180, 96 + 20, 0 }, { 50, 96 + 20, 180, 96 + 20 * 2, 1 },
					{ 50, 96 + 20 * 2, 180, 96 + 20 * 3, 2 },
					{ 50, 96 + 20 * 3, 180, 96 + 20 * 4, 3 },
					{ 50, 96 + 20 * 4, 180, 96 + 20 * 5, 4 }, },
			// ���� 12
			{ { 87, 156, 106, 170, 0 }, { 133, 156, 152, 170, 0 },

			{ 67, 191, 94, 204, 0 }, { 148, 191, 175, 204, 0 }, },
			// moto��ť 13
			{},
			// nokia��ť 14
			{},
			// moto��ť2 15
			{},
			// nokia��ť2 16
			{},
			// ϵͳ ������ 17
			{ { 73, 150, 168, 171, 0 }, },

			// ��̬������ 18
			{ { 11, 94, 42, 210, 0 }, { 196, 94, 230, 210, 0 }, },
			// ȫ��ȷ�Ϸ��� 19
			{ { 34, 245, 65, 261, 0 }, { 174, 245, 208, 261, 0 }, } };

	public static int curPointForm; // ��ǰ���д���������form

	/**
	 * �������߼�
	 * 
	 * @param x
	 *            int
	 * @param y
	 *            int
	 */
	public static void doPointer(int x, int y) {
		// ������
		if (bCanNotSell || bDlgIsMaxLevel || bDlgMakeSuccess || buySuc || bNoSp
				|| bSkillUp || bLevelLow || bDlgMakeFailed
				|| bDlgNotEnoughStone || notEnoughMoney) {
			cGame.keyPressed(CKey.KEY_MID);
			return;
		}
		// else if(bDlgMakeFailed||bDlgNotEnoughStone
		// ||notEnoughMoney){
		// if (cGame.isPointInRect(x,y,POINT_RECT[18][0])) {
		// cGame.keyPressed(CKey.KEY_SOFT_LEFT);
		// }
		// else if (cGame.isPointInRect(x,y,POINT_RECT[18][1])) {
		// cGame.keyPressed(CKey.KEY_SOFT_RIGHT);
		// }
		// return;
		// }

		// ȷ�Ϸ���
		if (curPointForm != Form_buyMenu && curPointForm != Form_mainMenu) {
			if (CGame.isPointInRect(x, y, POINT_RECT[19][0])) {
				cGame.keyPressed(CKey.KEY_SOFT_LEFT);
			} else if (CGame.isPointInRect(x, y, POINT_RECT[19][1])) {
				cGame.keyPressed(CKey.KEY_SOFT_RIGHT);
			}
		}

		int index = pointerInRectId(x, y, POINT_RECT[curPointForm]);
		if (index < 0) {
			if (curPointForm == Form_mainMenu) {
				// #if keymode_nokia||keymode_se||keymode_W958C
				cGame.keyPressed(CKey.KEY_SOFT_RIGHT);
				// #else
				// # cGame.keyPressed(CKey.KEY_SOFT_LEFT);
				// #endif
			}
			return;
		}
		switch (curPointForm) {
		case Form_mainMenu:
			if (index - 100 == form0_index) {
				cGame.keyPressed(CKey.KEY_MID);
			} else {
				form0_index = index - 100;
			}
			break;
		case Form_property:
			break;
		case Form_equip:
			if (index == curList.cursorIndex()) {
				cGame.keyPressed(CKey.KEY_MID);
			} else {
				curList.setCursorIndex(index);
			}
			updateDifference(curList.curItem());
			break;
		case Form_GOODS:
			// ����ѡ����
			if (index >= 100) {
				good_typeIndex = index - 100;
				switch (good_typeIndex) {
				case GOOD_TYPE_POTION:
					curList.setCurrent(List.LIST_POTION, true);
					break;
				case GOOD_TYPE_EQUIP:
					curList.setCurrent(List.LIST_EQUIP, true);
					break;
				case GOOD_TYPE_STONE:
					curList.setCurrent(List.LIST_STONE, true);
					break;
				case GOOD_TYPE_SPECIAL:
					curList.setCurrent(List.LIST_SPECIAL, true);
					break;
				}
			}
			// ����ѡ��Ʒ
			else {
				if (index == curList.cursorIndex()) {
					cGame.keyPressed(CKey.KEY_MID);
				} else {
					curList.setCursorIndex(index);
				}
			}
			break;
		case Form_make:
			if (CGame.isPointInRect(x, y, POINT_RECT[4][0])) {
				cGame.keyPressed(CKey.KEY_LEFT);
			} else if (CGame.isPointInRect(x, y, POINT_RECT[4][1])) {
				cGame.keyPressed(CKey.KEY_RIGHT);
			}
			break;
		case Form_SKILL:
			if (curList.index() == index) {
				cGame.keyPressed(CKey.KEY_MID);
			} else {
				curList.setCursorIndex(index);
			}
			UIdata.setHScrollIndex(curList.index());
			break;
		case Form_task:
			if (taskCursor != index) {
				taskCursor = index;
				UIdata.setHScrollIndex(taskCursor + taskStartIndex);
			}
			break;
		// case Form_systemMenu:
		// if (systemIndex==index) {
		// cGame.keyPressed(CKey.KEY_MID);
		// }
		// else{
		// systemIndex=index;
		// }
		// break;
		// case Form_noMusic:
		// cGame.keyPressed(CKey.KEY_MID);
		// break;
		case Form_USE:
			if (good_selectIndex == index) {
				cGame.keyPressed(CKey.KEY_MID);
			} else {
				good_selectIndex = index;
			}
			break;
		case Form_shop:
			// ����ѡ����
			if (index >= 100) {
				shopTypeID = index - 100;
				curList.setCurrent(shopTypeID == 0 ? List.LIST_EQUIP_SHOP
						: List.LIST_ITEM_SHOP, true);
			}
			// ����ѡ��Ʒ
			else {
				if (index == curList.cursorIndex()) {
					cGame.keyPressed(CKey.KEY_MID);
				} else {
					curList.setCursorIndex(index);
				}
			}
			break;
		case Form_buyMenu:
			if (CGame.isPointInRect(x, y, POINT_RECT[12][2])) {
				cGame.keyPressed(CKey.KEY_SOFT_LEFT);
			} else if (CGame.isPointInRect(x, y, POINT_RECT[12][3])) {
				cGame.keyPressed(CKey.KEY_SOFT_RIGHT);
			}
			break;
		}
	}

	/**
	 * �����ĸ�������
	 * 
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param Rects
	 *            short[][]
	 * @return int
	 */
	public static int pointerInRectId(int x, int y, short[][] Rects) {
		int index = -1;
		if (Rects == null || Rects.length <= 0) {
			return index;
		}
		for (int i = 0; i < Rects.length; i++) {
			if (CGame.isPointInRect(x, y, Rects[i])) {
				index = Rects[i][CGame.PT_INDEX];
				break;
			}
		}
		return index;
	}

	// #if N73
	// form 0
	public final static short Form_mainMenu = 0;
	public final static short B_mainMenu_or1 = 1;
	public final static short B_mainMenu_bt1 = 2;
	public final static short B_mainMenu_bt3 = 3;
	public final static short B_mainMenu_bt4 = 4;
	public final static short B_mainMenu_bt5 = 5;
	public final static short B_mainMenu_bt6 = 6;
	public final static short B_mainMenu_bt7 = 7;
	public final static short B_mainMenu_or3 = 8;
	// form 1
	public final static short Form_property = 1;
	public final static short B_property_or2 = 1;
	public final static short B_property_or12 = 2;
	public final static short B_property_or10 = 3;
	public final static short B_property_or9 = 4;
	public final static short B_property_or11 = 5;
	public final static short B_property_or8 = 6;
	public final static short B_property_or3 = 7;
	public final static short B_property_or4 = 8;
	public final static short B_property_or19 = 9;
	public final static short B_property_or7 = 10;
	public final static short B_property_or6 = 11;
	public final static short B_property_or5 = 12;
	public final static short B_property_or14 = 13;
	public final static short B_property_or15 = 14;
	public final static short B_property_or20 = 15;
	public final static short B_property_or17 = 16;
	public final static short B_property_or18 = 17;
	public final static short B_property_tb1 = 18;
	public final static short B_property_tb7 = 19;
	public final static short B_property_tb6 = 20;
	public final static short B_property_tb5 = 21;
	public final static short B_property_tb4 = 22;
	public final static short B_property_tb3 = 23;
	public final static short B_property_tb2 = 24;
	public final static short B_property_tb8 = 25;
	// form 2
	public final static short Form_equip = 2;
	public final static short B_equip_or2 = 1;
	public final static short B_equip_or3 = 2;
	public final static short B_equip_or4 = 3;
	public final static short B_equip_or5 = 4;
	public final static short B_equip_or10 = 5;
	public final static short B_equip_or15 = 6;
	public final static short B_equip_or16 = 7;
	public final static short B_equip_or11 = 8;
	public final static short B_equip_or14 = 9;
	public final static short B_equip_or20 = 10;
	public final static short B_equip_or17 = 11;
	public final static short B_equip_or18 = 12;
	public final static short B_equip_tb1 = 13;
	public final static short B_equip_tb2 = 14;
	public final static short B_equip_tb3 = 15;
	public final static short B_equip_tb4 = 16;
	public final static short B_equip_or19 = 17;
	public final static short B_equip_tb14 = 18;
	public final static short B_equip_bt4 = 19;
	public final static short B_equip_bt5 = 20;
	public final static short B_equip_bt3 = 21;
	public final static short B_equip_tb15 = 22;
	public final static short B_equip_tb16 = 23;
	public final static short B_equip_tb17 = 24;
	public final static short B_equip_tb18 = 25;
	public final static short B_equip_tb19 = 26;
	public final static short B_equip_tb20 = 27;
	public final static short B_equip_tb10 = 28;
	public final static short B_equip_tb8 = 29;
	public final static short B_equip_tb9 = 30;
	public final static short B_equip_sb1 = 31;
	public final static short B_equip_or21 = 32;
	public final static short B_equip_tb22 = 33;
	public final static short B_equip_tb25 = 34;
	public final static short B_equip_tb24 = 35;
	public final static short B_equip_tb23 = 36;
	public final static short B_equip_or22 = 37;
	public final static short B_equip_tb26 = 38;
	public final static short B_equip_tb27 = 39;
	// form 3
	public final static short Form_GOODS = 3;
	public final static short B_GOODS_or1 = 1;
	public final static short B_GOODS_or2 = 2;
	public final static short B_GOODS_or3 = 3;
	public final static short B_GOODS_or4 = 4;
	public final static short B_GOODS_bt2 = 5;
	public final static short B_GOODS_bt3 = 6;
	public final static short B_GOODS_bt4 = 7;
	public final static short B_GOODS_bt1 = 8;
	public final static short B_GOODS_or5 = 9;
	public final static short B_GOODS_or22 = 10;
	public final static short B_GOODS_or7 = 11;
	public final static short B_GOODS_or8 = 12;
	public final static short B_GOODS_or9 = 13;
	public final static short B_GOODS_or15 = 14;
	public final static short B_GOODS_or10 = 15;
	public final static short B_GOODS_or11 = 16;
	public final static short B_GOODS_or13 = 17;
	public final static short B_GOODS_or16 = 18;
	public final static short B_GOODS_or17 = 19;
	public final static short B_GOODS_or18 = 20;
	public final static short B_GOODS_tb5 = 21;
	public final static short B_GOODS_or19 = 22;
	public final static short B_GOODS_or21 = 23;
	public final static short B_GOODS_or20 = 24;
	public final static short B_GOODS_bt6 = 25;
	public final static short B_GOODS_bt7 = 26;
	public final static short B_GOODS_bt8 = 27;
	public final static short B_GOODS_bt5 = 28;
	public final static short B_GOODS_tb9 = 29;
	public final static short B_GOODS_tb8 = 30;
	public final static short B_GOODS_tb14 = 31;
	public final static short B_GOODS_tb11 = 32;
	public final static short B_GOODS_tb10 = 33;
	public final static short B_GOODS_tb15 = 34;
	public final static short B_GOODS_tb13 = 35;
	public final static short B_GOODS_tb12 = 36;
	public final static short B_GOODS_tb16 = 37;
	public final static short B_GOODS_tb7 = 38;
	public final static short B_GOODS_tb6 = 39;
	public final static short B_GOODS_tb17 = 40;
	public final static short B_GOODS_or23 = 41;
	public final static short B_GOODS_sb1 = 42;
	// form 4
	public final static short Form_make = 4;
	public final static short B_make_or1 = 1;
	public final static short B_make_or2 = 2;
	public final static short B_make_or3 = 3;
	public final static short B_make_or5 = 4;
	public final static short B_make_or16 = 5;
	public final static short B_make_or6 = 6;
	public final static short B_make_or7 = 7;
	public final static short B_make_or8 = 8;
	public final static short B_make_or9 = 9;
	public final static short B_make_tb3 = 10;
	public final static short B_make_or10 = 11;
	public final static short B_make_or11 = 12;
	public final static short B_make_tb10 = 13;
	public final static short B_make_tb8 = 14;
	public final static short B_make_tb9 = 15;
	public final static short B_make_or13 = 16;
	public final static short B_make_or14 = 17;
	public final static short B_make_or17 = 18;
	public final static short B_make_tb1 = 19;
	public final static short B_make_or15 = 20;
	// form 5
	public final static short Form_SKILL = 5;
	public final static short B_SKILL_or3 = 1;
	public final static short B_SKILL_or4 = 2;
	public final static short B_SKILL_or6 = 3;
	public final static short B_SKILL_or12 = 4;
	public final static short B_SKILL_or13 = 5;
	public final static short B_SKILL_tb6 = 6;
	public final static short B_SKILL_or16 = 7;
	public final static short B_SKILL_bt3 = 8;
	public final static short B_SKILL_bt2 = 9;
	public final static short B_SKILL_bt1 = 10;
	public final static short B_SKILL_tb9 = 11;
	public final static short B_SKILL_tb8 = 12;
	public final static short B_SKILL_tb7 = 13;
	public final static short B_SKILL_or17 = 14;
	public final static short B_SKILL_tb10 = 15;
	// form 6
	public final static short Form_task = 6;
	public final static short B_task_or1 = 1;
	public final static short B_task_or4 = 2;
	public final static short B_task_or3 = 3;
	public final static short B_task_or2 = 4;
	public final static short B_task_or5 = 5;
	public final static short B_task_or6 = 6;
	public final static short B_task_or9 = 7;
	public final static short B_task_or11 = 8;
	public final static short B_task_or10 = 9;
	public final static short B_task_or7 = 10;
	public final static short B_task_or8 = 11;
	public final static short B_task_tb5 = 12;
	public final static short B_task_sb1 = 13;
	public final static short B_task_bt4 = 14;
	public final static short B_task_bt2 = 15;
	public final static short B_task_bt6 = 16;
	public final static short B_task_bt5 = 17;
	public final static short B_task_tb6 = 18;
	public final static short B_task_tb7 = 19;
	public final static short B_task_tb8 = 20;
	public final static short B_task_tb4 = 21;
	// form 7
	public final static short Form_systemMenu = 7;
	public final static short B_systemMenu_or1 = 1;
	public final static short B_systemMenu_or2 = 2;
	public final static short B_systemMenu_tb1 = 3;
	public final static short B_systemMenu_or3 = 4;
	public final static short B_systemMenu_or4 = 5;
	public final static short B_systemMenu_bt1 = 6;
	public final static short B_systemMenu_bt2 = 7;
	public final static short B_systemMenu_tb2 = 8;
	public final static short B_systemMenu_tb3 = 9;
	// form 8
	public final static short Form_OKCANCEL = 8;
	public final static short B_OKCANCEL_or1 = 1;
	public final static short B_OKCANCEL_tb1 = 2;
	// form 9
	public final static short Form_USE = 9;
	public final static short B_USE_or1 = 1;
	public final static short B_USE_bt3 = 2;
	public final static short B_USE_bt4 = 3;
	public final static short B_USE_or2 = 4;
	public final static short B_USE_tb1 = 5;
	public final static short B_USE_tb2 = 6;
	public final static short B_USE_tb3 = 7;
	// form 10
	public final static short Form_chargeMenu = 10;
	public final static short B_chargeMenu_or1 = 1;
	public final static short B_chargeMenu_or2 = 2;
	public final static short B_chargeMenu_or3 = 3;
	public final static short B_chargeMenu_or9 = 4;
	public final static short B_chargeMenu_or8 = 5;
	public final static short B_chargeMenu_or7 = 6;
	public final static short B_chargeMenu_or6 = 7;
	public final static short B_chargeMenu_or5 = 8;
	public final static short B_chargeMenu_or4 = 9;
	public final static short B_chargeMenu_bt1 = 10;
	public final static short B_chargeMenu_bt2 = 11;
	public final static short B_chargeMenu_bt3 = 12;
	public final static short B_chargeMenu_bt4 = 13;
	public final static short B_chargeMenu_bt5 = 14;
	public final static short B_chargeMenu_bt6 = 15;
	public final static short B_chargeMenu_bt7 = 16;
	public final static short B_chargeMenu_tb1 = 17;
	public final static short B_chargeMenu_tb2 = 18;
	public final static short B_chargeMenu_tb3 = 19;
	public final static short B_chargeMenu_tb4 = 20;
	public final static short B_chargeMenu_tb5 = 21;
	public final static short B_chargeMenu_tb6 = 22;
	public final static short B_chargeMenu_tb7 = 23;
	// form 11
	public final static short Form_shop = 11;
	public final static short B_shop_or1 = 1;
	public final static short B_shop_or11 = 2;
	public final static short B_shop_or3 = 3;
	public final static short B_shop_or4 = 4;
	public final static short B_shop_or5 = 5;
	public final static short B_shop_or9 = 6;
	public final static short B_shop_or6 = 7;
	public final static short B_shop_or10 = 8;
	public final static short B_shop_or7 = 9;
	public final static short B_shop_bt2 = 10;
	public final static short B_shop_bt1 = 11;
	public final static short B_shop_or12 = 12;
	public final static short B_shop_or13 = 13;
	public final static short B_shop_or15 = 14;
	public final static short B_shop_or21 = 15;
	public final static short B_shop_or16 = 16;
	public final static short B_shop_or14 = 17;
	public final static short B_shop_or18 = 18;
	public final static short B_shop_tb6 = 19;
	public final static short B_shop_or17 = 20;
	public final static short B_shop_or20 = 21;
	public final static short B_shop_or19 = 22;
	public final static short B_shop_bt8 = 23;
	public final static short B_shop_bt7 = 24;
	public final static short B_shop_bt6 = 25;
	public final static short B_shop_bt5 = 26;
	public final static short B_shop_bt4 = 27;
	public final static short B_shop_tb7 = 28;
	public final static short B_shop_sb1 = 29;
	public final static short B_shop_tb15 = 30;
	public final static short B_shop_tb13 = 31;
	public final static short B_shop_tb14 = 32;
	public final static short B_shop_tb12 = 33;
	public final static short B_shop_tb11 = 34;
	public final static short B_shop_tb9 = 35;
	public final static short B_shop_tb17 = 36;
	public final static short B_shop_tb16 = 37;
	public final static short B_shop_tb10 = 38;
	public final static short B_shop_tb8 = 39;
	public final static short B_shop_tb18 = 40;
	public final static short B_shop_tb19 = 41;
	public final static short B_shop_tb20 = 42;
	public final static short B_shop_tb21 = 43;
	public final static short B_shop_tb22 = 44;
	// form 12
	public final static short Form_buyMenu = 12;
	public final static short B_buyMenu_or1 = 1;
	public final static short B_buyMenu_or2 = 2;
	public final static short B_buyMenu_or3 = 3;
	public final static short B_buyMenu_tb1 = 4;
	public final static short B_buyMenu_tb4 = 5;
	public final static short B_buyMenu_tb3 = 6;
	public final static short B_buyMenu_tb2 = 7;
	// form 13
	public final static short Form_buyMoney = 13;
	public final static short B_buyMoney_or1 = 1;
	public final static short B_buyMoney_or2 = 2;
	public final static short B_buyMoney_or3 = 3;
	public final static short B_buyMoney_or4 = 4;
	public final static short B_buyMoney_bt1 = 5;
	public final static short B_buyMoney_bt2 = 6;
	public final static short B_buyMoney_bt3 = 7;
	public final static short B_buyMoney_bt4 = 8;
	public final static short B_buyMoney_tb1 = 9;
	public final static short B_buyMoney_or5 = 10;
	public final static short B_buyMoney_or6 = 11;
	public final static short B_buyMoney_tb2 = 12;
	public final static short B_buyMoney_tb3 = 13;
	public final static short B_buyMoney_tb4 = 14;
	public final static short B_buyMoney_tb5 = 15;
	public final static short B_buyMoney_tb6 = 16;
	// form 14
	public final static short Form_yanqing = 14;
	public final static short B_yanqing_or1 = 1;
	// form 15
	public final static short Form_likui = 15;
	public final static short B_likui_or1 = 1;
	// #else
	// # //form 0
	// # public final static short Form_mainMenu = 0;
	// # public final static short B_mainMenu_or1 = 1;
	// # public final static short B_mainMenu_bt1 = 2;
	// # public final static short B_mainMenu_bt3 = 3;
	// # public final static short B_mainMenu_bt4 = 4;
	// # public final static short B_mainMenu_bt5 = 5;
	// # public final static short B_mainMenu_bt6 = 6;
	// # public final static short B_mainMenu_bt7 = 7;
	// # public final static short B_mainMenu_or3 = 8;
	// # //form 1
	// # public final static short Form_property = 1;
	// # public final static short B_property_or2 = 1;
	// # public final static short B_property_or12 = 2;
	// # public final static short B_property_or10 = 3;
	// # public final static short B_property_or9 = 4;
	// # public final static short B_property_or11 = 5;
	// # public final static short B_property_or8 = 6;
	// # public final static short B_property_or3 = 7;
	// # public final static short B_property_or4 = 8;
	// # public final static short B_property_or19 = 9;
	// # public final static short B_property_or7 = 10;
	// # public final static short B_property_or6 = 11;
	// # public final static short B_property_or5 = 12;
	// # public final static short B_property_or14 = 13;
	// # public final static short B_property_or15 = 14;
	// # public final static short B_property_or20 = 15;
	// # public final static short B_property_tb1 = 16;
	// # public final static short B_property_tb7 = 17;
	// # public final static short B_property_tb6 = 18;
	// # public final static short B_property_tb5 = 19;
	// # public final static short B_property_tb4 = 20;
	// # public final static short B_property_tb3 = 21;
	// # public final static short B_property_tb2 = 22;
	// # public final static short B_property_tb8 = 23;
	// # //form 2
	// # public final static short Form_equip = 2;
	// # public final static short B_equip_or2 = 1;
	// # public final static short B_equip_or3 = 2;
	// # public final static short B_equip_or4 = 3;
	// # public final static short B_equip_or5 = 4;
	// # public final static short B_equip_or10 = 5;
	// # public final static short B_equip_or15 = 6;
	// # public final static short B_equip_or16 = 7;
	// # public final static short B_equip_or11 = 8;
	// # public final static short B_equip_or14 = 9;
	// # public final static short B_equip_or20 = 10;
	// # public final static short B_equip_tb1 = 11;
	// # public final static short B_equip_tb2 = 12;
	// # public final static short B_equip_tb3 = 13;
	// # public final static short B_equip_tb4 = 14;
	// # public final static short B_equip_or19 = 15;
	// # public final static short B_equip_tb14 = 16;
	// # public final static short B_equip_bt4 = 17;
	// # public final static short B_equip_bt5 = 18;
	// # public final static short B_equip_bt3 = 19;
	// # public final static short B_equip_tb15 = 20;
	// # public final static short B_equip_tb16 = 21;
	// # public final static short B_equip_tb17 = 22;
	// # public final static short B_equip_tb18 = 23;
	// # public final static short B_equip_tb19 = 24;
	// # public final static short B_equip_tb20 = 25;
	// # public final static short B_equip_tb10 = 26;
	// # public final static short B_equip_tb8 = 27;
	// # public final static short B_equip_tb9 = 28;
	// # public final static short B_equip_sb1 = 29;
	// # public final static short B_equip_or21 = 30;
	// # public final static short B_equip_tb22 = 31;
	// # public final static short B_equip_tb25 = 32;
	// # public final static short B_equip_tb24 = 33;
	// # public final static short B_equip_tb23 = 34;
	// # public final static short B_equip_or22 = 35;
	// # public final static short B_equip_tb26 = 36;
	// # public final static short B_equip_tb27 = 37;
	// # //form 3
	// # public final static short Form_GOODS = 3;
	// # public final static short B_GOODS_or1 = 1;
	// # public final static short B_GOODS_or2 = 2;
	// # public final static short B_GOODS_or3 = 3;
	// # public final static short B_GOODS_or4 = 4;
	// # public final static short B_GOODS_bt2 = 5;
	// # public final static short B_GOODS_bt3 = 6;
	// # public final static short B_GOODS_bt4 = 7;
	// # public final static short B_GOODS_bt1 = 8;
	// # public final static short B_GOODS_or5 = 9;
	// # public final static short B_GOODS_or22 = 10;
	// # public final static short B_GOODS_or7 = 11;
	// # public final static short B_GOODS_or8 = 12;
	// # public final static short B_GOODS_or9 = 13;
	// # public final static short B_GOODS_or15 = 14;
	// # public final static short B_GOODS_or10 = 15;
	// # public final static short B_GOODS_or11 = 16;
	// # public final static short B_GOODS_or13 = 17;
	// # public final static short B_GOODS_or16 = 18;
	// # public final static short B_GOODS_tb5 = 19;
	// # public final static short B_GOODS_or19 = 20;
	// # public final static short B_GOODS_or21 = 21;
	// # public final static short B_GOODS_or20 = 22;
	// # public final static short B_GOODS_bt6 = 23;
	// # public final static short B_GOODS_bt7 = 24;
	// # public final static short B_GOODS_bt8 = 25;
	// # public final static short B_GOODS_bt5 = 26;
	// # public final static short B_GOODS_tb9 = 27;
	// # public final static short B_GOODS_tb8 = 28;
	// # public final static short B_GOODS_tb14 = 29;
	// # public final static short B_GOODS_tb11 = 30;
	// # public final static short B_GOODS_tb10 = 31;
	// # public final static short B_GOODS_tb15 = 32;
	// # public final static short B_GOODS_tb13 = 33;
	// # public final static short B_GOODS_tb12 = 34;
	// # public final static short B_GOODS_tb16 = 35;
	// # public final static short B_GOODS_tb7 = 36;
	// # public final static short B_GOODS_tb6 = 37;
	// # public final static short B_GOODS_tb17 = 38;
	// # public final static short B_GOODS_or23 = 39;
	// # public final static short B_GOODS_sb1 = 40;
	// # //form 4
	// # public final static short Form_make = 4;
	// # public final static short B_make_or1 = 1;
	// # public final static short B_make_or2 = 2;
	// # public final static short B_make_or3 = 3;
	// # public final static short B_make_or5 = 4;
	// # public final static short B_make_or16 = 5;
	// # public final static short B_make_or6 = 6;
	// # public final static short B_make_or7 = 7;
	// # public final static short B_make_or8 = 8;
	// # public final static short B_make_tb3 = 9;
	// # public final static short B_make_or11 = 10;
	// # public final static short B_make_tb10 = 11;
	// # public final static short B_make_tb8 = 12;
	// # public final static short B_make_tb9 = 13;
	// # public final static short B_make_or13 = 14;
	// # public final static short B_make_or14 = 15;
	// # public final static short B_make_or17 = 16;
	// # public final static short B_make_tb1 = 17;
	// # public final static short B_make_or15 = 18;
	// # //form 5
	// # public final static short Form_SKILL = 5;
	// # public final static short B_SKILL_or3 = 1;
	// # public final static short B_SKILL_or4 = 2;
	// # public final static short B_SKILL_or6 = 3;
	// # public final static short B_SKILL_tb6 = 4;
	// # public final static short B_SKILL_or16 = 5;
	// # public final static short B_SKILL_bt3 = 6;
	// # public final static short B_SKILL_bt2 = 7;
	// # public final static short B_SKILL_bt1 = 8;
	// # public final static short B_SKILL_tb9 = 9;
	// # public final static short B_SKILL_tb8 = 10;
	// # public final static short B_SKILL_tb7 = 11;
	// # public final static short B_SKILL_or17 = 12;
	// # public final static short B_SKILL_tb10 = 13;
	// # //form 6
	// # public final static short Form_task = 6;
	// # public final static short B_task_or1 = 1;
	// # public final static short B_task_or4 = 2;
	// # public final static short B_task_or3 = 3;
	// # public final static short B_task_or2 = 4;
	// # public final static short B_task_or5 = 5;
	// # public final static short B_task_or6 = 6;
	// # public final static short B_task_or9 = 7;
	// # public final static short B_task_or11 = 8;
	// # public final static short B_task_or10 = 9;
	// # public final static short B_task_tb5 = 10;
	// # public final static short B_task_sb1 = 11;
	// # public final static short B_task_bt4 = 12;
	// # public final static short B_task_bt2 = 13;
	// # public final static short B_task_bt6 = 14;
	// # public final static short B_task_bt5 = 15;
	// # public final static short B_task_tb6 = 16;
	// # public final static short B_task_tb7 = 17;
	// # public final static short B_task_tb8 = 18;
	// # public final static short B_task_tb4 = 19;
	// # //form 7
	// # public final static short Form_systemMenu = 7;
	// # public final static short B_systemMenu_tb1 = 1;
	// # public final static short B_systemMenu_or3 = 2;
	// # public final static short B_systemMenu_or4 = 3;
	// # public final static short B_systemMenu_bt1 = 4;
	// # public final static short B_systemMenu_bt2 = 5;
	// # public final static short B_systemMenu_tb2 = 6;
	// # public final static short B_systemMenu_tb3 = 7;
	// # //form 8
	// # public final static short Form_OKCANCEL = 8;
	// # public final static short B_OKCANCEL_or1 = 1;
	// # public final static short B_OKCANCEL_tb1 = 2;
	// # //form 9
	// # public final static short Form_USE = 9;
	// # public final static short B_USE_or1 = 1;
	// # public final static short B_USE_bt3 = 2;
	// # public final static short B_USE_bt4 = 3;
	// # public final static short B_USE_or2 = 4;
	// # public final static short B_USE_tb1 = 5;
	// # public final static short B_USE_tb2 = 6;
	// # public final static short B_USE_tb3 = 7;
	// # //form 10
	// # public final static short Form_chargeMenu = 10;
	// # public final static short B_chargeMenu_or3 = 1;
	// # public final static short B_chargeMenu_or9 = 2;
	// # public final static short B_chargeMenu_or8 = 3;
	// # public final static short B_chargeMenu_or7 = 4;
	// # public final static short B_chargeMenu_or6 = 5;
	// # public final static short B_chargeMenu_or5 = 6;
	// # public final static short B_chargeMenu_or4 = 7;
	// # public final static short B_chargeMenu_bt1 = 8;
	// # public final static short B_chargeMenu_bt2 = 9;
	// # public final static short B_chargeMenu_bt3 = 10;
	// # public final static short B_chargeMenu_bt4 = 11;
	// # public final static short B_chargeMenu_bt5 = 12;
	// # public final static short B_chargeMenu_bt6 = 13;
	// # public final static short B_chargeMenu_bt7 = 14;
	// # public final static short B_chargeMenu_tb1 = 15;
	// # public final static short B_chargeMenu_tb2 = 16;
	// # public final static short B_chargeMenu_tb3 = 17;
	// # public final static short B_chargeMenu_tb4 = 18;
	// # public final static short B_chargeMenu_tb5 = 19;
	// # public final static short B_chargeMenu_tb6 = 20;
	// # public final static short B_chargeMenu_tb7 = 21;
	// # //form 11
	// # public final static short Form_shop = 11;
	// # public final static short B_shop_or1 = 1;
	// # public final static short B_shop_or11 = 2;
	// # public final static short B_shop_or3 = 3;
	// # public final static short B_shop_or4 = 4;
	// # public final static short B_shop_or5 = 5;
	// # public final static short B_shop_or9 = 6;
	// # public final static short B_shop_or6 = 7;
	// # public final static short B_shop_or10 = 8;
	// # public final static short B_shop_or7 = 9;
	// # public final static short B_shop_bt2 = 10;
	// # public final static short B_shop_bt1 = 11;
	// # public final static short B_shop_or12 = 12;
	// # public final static short B_shop_or15 = 13;
	// # public final static short B_shop_or21 = 14;
	// # public final static short B_shop_or16 = 15;
	// # public final static short B_shop_or18 = 16;
	// # public final static short B_shop_tb6 = 17;
	// # public final static short B_shop_or17 = 18;
	// # public final static short B_shop_or20 = 19;
	// # public final static short B_shop_or19 = 20;
	// # public final static short B_shop_bt8 = 21;
	// # public final static short B_shop_bt7 = 22;
	// # public final static short B_shop_bt6 = 23;
	// # public final static short B_shop_bt5 = 24;
	// # public final static short B_shop_bt4 = 25;
	// # public final static short B_shop_tb7 = 26;
	// # public final static short B_shop_sb1 = 27;
	// # public final static short B_shop_tb15 = 28;
	// # public final static short B_shop_tb13 = 29;
	// # public final static short B_shop_tb14 = 30;
	// # public final static short B_shop_tb12 = 31;
	// # public final static short B_shop_tb11 = 32;
	// # public final static short B_shop_tb9 = 33;
	// # public final static short B_shop_tb17 = 34;
	// # public final static short B_shop_tb16 = 35;
	// # public final static short B_shop_tb10 = 36;
	// # public final static short B_shop_tb8 = 37;
	// # public final static short B_shop_tb18 = 38;
	// # public final static short B_shop_tb19 = 39;
	// # public final static short B_shop_tb20 = 40;
	// # public final static short B_shop_tb21 = 41;
	// # public final static short B_shop_tb22 = 42;
	// # //form 12
	// # public final static short Form_buyMenu = 12;
	// # public final static short B_buyMenu_or1 = 1;
	// # public final static short B_buyMenu_or2 = 2;
	// # public final static short B_buyMenu_or3 = 3;
	// # public final static short B_buyMenu_tb1 = 4;
	// # public final static short B_buyMenu_tb4 = 5;
	// # public final static short B_buyMenu_tb3 = 6;
	// # public final static short B_buyMenu_tb2 = 7;
	// # public final static short B_buyMenu_or4 = 8;
	// # public final static short B_buyMenu_or5 = 9;
	// # //form 13
	// # public final static short Form_buyMoney = 13;
	// # public final static short B_buyMoney_or1 = 1;
	// # public final static short B_buyMoney_or2 = 2;
	// # public final static short B_buyMoney_or3 = 3;
	// # public final static short B_buyMoney_or4 = 4;
	// # public final static short B_buyMoney_bt1 = 5;
	// # public final static short B_buyMoney_bt2 = 6;
	// # public final static short B_buyMoney_bt3 = 7;
	// # public final static short B_buyMoney_bt4 = 8;
	// # public final static short B_buyMoney_tb1 = 9;
	// # public final static short B_buyMoney_tb2 = 10;
	// # public final static short B_buyMoney_tb3 = 11;
	// # public final static short B_buyMoney_tb4 = 12;
	// # public final static short B_buyMoney_tb5 = 13;
	// # public final static short B_buyMoney_tb6 = 14;
	// # //form 14
	// # public final static short Form_yanqing = 14;
	// # public final static short B_yanqing_or1 = 1;
	// # //form 15
	// # public final static short Form_likui = 15;
	// # public final static short B_likui_or1 = 1;
	// # //form 16
	// # public final static short Form_Moto1 = 16;
	// # public final static short B_Moto1_or1 = 1;
	// # public final static short B_Moto1_or2 = 2;
	// # //form 17
	// # public final static short Form_Nokia1 = 17;
	// # public final static short B_Nokia1_or1 = 1;
	// # public final static short B_Nokia1_or2 = 2;
	// # //form 18
	// # public final static short Form_Moto2 = 18;
	// # public final static short B_Moto2_or1 = 1;
	// # public final static short B_Moto2_or2 = 2;
	// # //form 19
	// # public final static short Form_Nokia2 = 19;
	// # public final static short B_Nokia2_or1 = 1;
	// # public final static short B_Nokia2_or2 = 2;
	// #
	// #
	// #
	// #
	// #
	// #
	// #
	// #endif

}
