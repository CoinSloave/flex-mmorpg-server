package com.mglib.script;

import game.CDebug;
import game.CGame;
import game.CTools;
import game.GameUI;
import game.Goods;
import game.config.dConfig;
import game.config.dGame;
import game.key.CKey;
import game.object.CHero;
import game.object.CObject;
import game.object.IObject;
import game.object.dActor;
import game.object.dActorClass;
import game.pak.Camera;
import game.pak.GameEffect;
import game.res.ResLoader;

import java.io.DataInputStream;
import java.util.Vector;

import com.mglib.mdl.ani.Player;
import com.mglib.mdl.map.MapData;
import com.mglib.sound.SoundPlayer;

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

public class Script {
	public Script() {
	}

	public static final String s_filenameScript = "/bin/script.bin";
	public static final String s_filenameQuest = "/bin/questvar.bin";

	public static boolean scriptLock = false;
	/************************
	 * ���ؽű�
	 **********************/
	/************
	 * �ؿ��ű��ؿ��ű�
	 ************/
	// �ؿ��ű�������
	public static short scriptLevelCount;

	// ��������Ľű�����: [scriptID][�ӽű��ı��][ǰ��������������Ԫ������(������Ԫ��(����1������2��...))...]
	public static byte[][][] scriptLevelConditions;

	// ��������Ľű�ִ��: [scriptID][�ӽű��ı��][ִ��������(ִ�е�Ԫ��(����1������2��...))...]
	public static byte[][][] scriptLevelConducts;

	// �ؿ��ű��Ի����ı���Ŀ
	public static short dialogLeveTextlCount;

	// ��������Ľű��Ի��ı�
	public static String[] dailogLevelText;

	// ��������Ľű��Ի��ı���־��Ϣ
	public static short[][] dailogLevelSignInfo;

	public static void loadScript(int newLevel) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream("".getClass().getResourceAsStream(
					s_filenameScript));
			readScript(dis, newLevel);
			readDialogText(dis, newLevel);
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dis = null;
		}
	}

	/**
	 * k700��ÿ������2���ļ����Ի��ı��ͽű���
	 * 
	 * @param newLevel
	 *            int
	 */
	public static void loadScriptK700(int newLevel) {
		DataInputStream dis = null;
		try {
			// ��ȡ�ű�
			dis = new DataInputStream("".getClass().getResourceAsStream(
					"/bin/Script/" + newLevel + ".sd"));
			readLevel(dis, newLevel, false);
			// #if !W958C
			System.gc();
			// #endif

			// ��ȡ�Ի��ı�
			dis = new DataInputStream("".getClass().getResourceAsStream(
					"/bin/Script/" + newLevel + ".st"));
			readDialogTextK700(dis, newLevel);
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dis = null;
		}
	}

	/**
	 * readScript
	 * 
	 * @param dis
	 *            DataInputStream
	 * @param levelID
	 *            int
	 */
	private static void readScript(DataInputStream dis, int newLevel)
			throws Exception {
		int segmentCount = dis.readShort();
		int[] offset = new int[segmentCount + 1];
		for (int i = 0; i < offset.length; i++) {
			offset[i] = dis.readInt();
		}

		dis.skip(offset[newLevel]);
		readLevel(dis, newLevel, false); // read newLevel script

		dis.skip(offset[segmentCount] - offset[newLevel + 1]);

	}

	/**
	 * readLevel
	 * 
	 * @param dis
	 *            DataInputStream
	 * @param levelID
	 *            int
	 */
	private static void readLevel(DataInputStream dis, int newLevel,
			boolean isCommon) throws Exception {
		byte[][][] conditions;
		byte[][][] conducts;

		int count = dis.readShort();
		conditions = new byte[count][][];
		conducts = new byte[count][][];

		if (CDebug.showScripLoadInfo) {
			System.out.println("DEBUG >>��\nread level:" + --newLevel
					+ ", AScript count is " + count);
		}

		for (int i = 0; i < count; i++) {
			readAScript(dis);
			conditions[i] = subConditions;
			conducts[i] = subConducts;
			subConditions = null;
			subConducts = null;
		}

		scriptLevelConditions = conditions;
		scriptLevelConducts = conducts;
		scriptLevelCount = (short) conditions.length;

	}

	/**
	 * readDialogText
	 * 
	 * @param dis
	 *            DataInputStream
	 * @param levelID
	 *            int
	 */
	private static void readDialogText(DataInputStream dis, int newLevel)
			throws Exception {
		// boolean isDialogCommonLoaded = ( dailogCommonText != null );
		int segmentCount = dis.readShort();
		int[] offset = new int[segmentCount + 1];
		for (int i = 0; i < offset.length; i++) {
			offset[i] = dis.readInt();
		}

		dis.skip(offset[newLevel]);
		// read newLevel dialog text
		int dtCount = dis.readShort();
		dailogLevelText = new String[dtCount];
		dailogLevelSignInfo = new short[dtCount][];
		for (int i = 0; i < dtCount; i++) {
			int sc = dis.readShort();
			dailogLevelSignInfo[i] = new short[sc];
			for (int j = 0; j < sc; j++) {
				dailogLevelSignInfo[i][j] = dis.readShort();
			}
			String str = dis.readUTF();
			dailogLevelText[i] = str;
			if (CDebug.showScripLoadInfo) {
				System.out.println("DEBUG >>��read " + (newLevel - 1) + " text "
						+ i + ": " + str);
			}
		}
		dialogLeveTextlCount = (short) dtCount;

		// read end, skip for next
		dis.skip(offset[segmentCount] - offset[newLevel + 1]);

	}

	/**
	 * k700����Ի��ı�
	 * 
	 * @param dis
	 *            DataInputStream
	 * @param newLevel
	 *            int
	 * @throws Exception
	 */
	private static void readDialogTextK700(DataInputStream dis, int newLevel)
			throws Exception {
		// int segmentCount = dis.readShort ();
		// int[] offset = new int[segmentCount + 1];
		// for(int i = 0; i < offset.length; i++) {
		// offset[i] = dis.readInt ();
		// }
		//
		// dis.skip (offset[newLevel]);
		// read newLevel dialog text
		int dtCount = dis.readShort();
		dailogLevelText = new String[dtCount];
		dailogLevelSignInfo = new short[dtCount][];
		for (int i = 0; i < dtCount; i++) {
			int sc = dis.readShort();
			dailogLevelSignInfo[i] = new short[sc];
			for (int j = 0; j < sc; j++) {
				dailogLevelSignInfo[i][j] = dis.readShort();
			}
			String str = dis.readUTF();
			dailogLevelText[i] = str;
			if (CDebug.showScripLoadInfo) {
				System.out.println("DEBUG >>��read " + (newLevel - 1) + " text "
						+ i + ": " + str);
			}
		}
	}

	/**
	 * ��ȡ�ű��ļ�ʱ�õ�����ʱ��
	 */
	static byte[][] subConditions;
	static byte[][] subConducts;

	/**
	 * readAScript
	 * 
	 * @param dis
	 *            DataInputStream
	 */
	private static void readAScript(DataInputStream dis) throws Exception {
		int count = dis.readShort();
		subConditions = new byte[count][];
		subConducts = new byte[count][];
		if (CDebug.showScripLoadInfo) {
			System.out.println("DEBUG >>��\ta script: sub script count is "
					+ count);
		}
		// read conditions
		for (int i = 0; i < count; i++) {
			subConditions[i] = readConditions(dis);
		}
		// read conducts
		for (int i = 0; i < count; i++) {
			subConducts[i] = readConducts(dis);
		}
	}

	/**
	 * readConditions
	 * 
	 * @param dis
	 *            DataInputStream
	 */
	private static byte[] readConditions(DataInputStream dis) throws Exception {
		int length = dis.readInt(); // �����������ĳ���
		byte[] conditionUnits = new byte[length + 2];
		dis.read(conditionUnits);

		if (CDebug.showScripLoadInfo) {
			int scriptType = conditionUnits[0];
			int count = conditionUnits[1];
			System.out.println("\t\tConditon: " + " scriptType: " + scriptType
					+ " count: " + count);
			System.out.println("\t\t\tunits: ");
			String s = "";
			for (int i = 2; i < length + 2; i++) {
				s += Integer.toHexString(conditionUnits[i]) + " ";
			}
			System.out.println("\t\t\t\t" + s);
		}
		return conditionUnits;
	}

	/**
	 * readConducts
	 * 
	 * @param dis
	 *            DataInputStream
	 */
	private static byte[] readConducts(DataInputStream dis) throws Exception {
		int length = dis.readInt(); // ������ִ�еĳ���
		byte[] conductUnits = new byte[length + 1];
		dis.read(conductUnits);

		if (CDebug.showScripLoadInfo) {
			int count = conductUnits[0];
			// output information
			System.out.println("DEBUG >>��\t\tConduct: " + " count: " + count);
			System.out.println("DEBUG >>��\t\t\tunits: ");
			String s = "";
			for (int i = 1; i < length + 1; i++) {
				s += Integer.toHexString(conductUnits[i]) + " ";
			}
			System.out.println("\t\t\t\t" + s);
		}

		return conductUnits;
	}

	/*******************************
	 * �ű�����
	 *******************************/
	// ��ǰ����Ľű�����: [�ӽű��ı��][ǰ��������������Ԫ������(������Ԫ��(����1������2��...))...]
	public byte[][] currentConditions;

	// ��ǰ����Ľű�ִ��: [�ӽű��ı��][ִ��������(ִ�е�Ԫ��(����1������2��...))...]
	public byte[][] currentConducts;

	// �ű��������Ӵ˱�ſ�ʼ
	public short scanStartSubScriptID;

	// ָ��curScriptCondition���һ���ֽ�
	public short conditionIndex = 0;

	// ��ǰ�ű�����
	public byte[] curScriptConditions;

	// �Ƿ��ڽű�������
	public boolean isInScriptRunning;

	// �������Ľű����Ƿ���ָ����Ϣ
	public boolean hasWord; // �Ƿ��жԻ�

	public Vector vTaskList = new Vector(); // �������������������

	/**********************************
	 * �ű�����
	 ******************************/

	/*******************************
	 * �ű����ݶ�
	 *******************************/
	/************
	 * script condition and conduct table
	 ************/
	// condition
	// condition
	public static final byte CDN_PreCdn_NearMe = 0;
	public static final byte CDN_PreCdn_SayToMe = 1;
	public static final byte CDN_PreCdn_PlayerWin = 2;
	public static final byte CDN_PreCdn_PlayerFail = 3;
	public static final byte CDN_PreCdn_NoCdn = 4;
	public static final byte CDN_PreCdn_LeaveMe = 5;

	public static final byte CDN_ConditionQuestItem = 0;
	public static final byte CDN_ConditionPropertyValueItem = 1;
	public static final byte CDN_ConditionSystemValueItem = 2;
	public static final byte CDN_DialogItem = 3;
	public static final byte CDN_ConditionGoodsValueItem = 4;

	// ����ָ��
	public static final byte CDN_Key = 5;
	public static final byte CDN_ConditionRoleProperty = 6;
	public static final byte CDN_ConditionRoleState = 7;
	public static final byte CDN_ConditionRoleCol = 8; // ������ײ
	public static final byte CDN_ConditionOptionDlg = 9; // ѡ���ԶԻ�

	// conduct
	public static final byte CDT_SetRoleStateValueItem = 0;
	public static final byte CDT_SetMoveRoleItem = 1;
	public static final byte CDT_SetMoveCommandItem = 2;
	public static final byte CDT_SetAddRoleItem = 3;
	public static final byte CDT_SetDeleteRoleItem = 4;
	public static final byte CDT_SetRoleActorItem = 5;
	public static final byte CDT_SetRolePropertyValueItem = 6;
	public static final byte CDT_SetAddGoodsItem = 7;
	public static final byte CDT_SetAddEquipsItem = 8;
	public static final byte CDT_SetAddSkillsItem = 9;
	public static final byte CDT_SetAddCommonTalkItem = 10;
	public static final byte CDT_SetAddMapObjectItem = 11;
	public static final byte CDT_SetDeleteMapObjectItem = 12;
	public static final byte CDT_SetObjectEnalbeItem = 13;
	public static final byte CDT_SetSystemStateItem = 14;
	public static final byte CDT_SetQuestItem = 15;
	public static final byte CDT_SetConditionSystemValueItem = 16;
	public static final byte CDT_SetPlayMusicItem = 17;
	public static final byte CDT_SetPlayAviItem = 18;
	public static final byte CDT_SetStopMusicItem = 19;

	// ����ָ��
	public static final byte CDT_SetSign = 20;
	public static final byte CDT_SetReborn = 21;
	public static final byte CDT_SetCameraCtrl = 22;
	public static final byte CDT_SetMoveCameraToPt = 23;
	public static final byte CDT_SetCameraFollowObj = 24;
	public static final byte CDT_SetMoveCameraToObj = 25;
	public static final byte CDT_SetScriptRunPause = 26;
	// ����ָ�� 2008/9/12
	public static final byte CDT_SetScriptAutoKey = 27;
	public static final byte CDT_SetDelMutiRole = 28;
	public static final byte CDT_SetAddMutiRole = 29;
	public static final byte CDT_SetMoveMutiRole = 30;
	// add by zengkai 2008/9/23
	public static final byte CDT_SetShowCG = 31; // ��ʾCG
	public static final byte CDT_SetTeamPlayer = 32; // ��Ա������뿪����
	//
	public static final byte CDT_SetOptionDlg = 33; // ����ѡ���ԶԻ�
	public static final byte CDT_SetShopItem = 34; // �̵�

	/**
	 * first dimension : condition ID second dimesion : ����1���ȣ�����2���ȣ������� Note:
	 * �����ȡ��� byte Ϊ��λ
	 */
	private final static byte[][] SCRIPT_CONDITION_TABLE = {
			{ 2, 1, 2 },
			{ 2, 1, 2 }, // ������ɶ�, �����������
			{ 2, 1, 2 }, { 2 }, { 2, 1, 2 }, { 1, 1 }, { 2, 2, 1, 2 },
			{ 2, 2, 1 }, { 2, 1, 2 }, { 2, 2 }, // ϵͳ����, �Ի��а���, ��Ʒ����, ��������,
												// ��������,, ����״̬,, ����ӽ�����, ѡ���ԶԻ�

	};

	/**
	 * first dimension : script ID second dimesion : ����1���ȣ�����2���ȣ������� Note: �����ȡ���
	 * byte Ϊ��λ
	 */
	private final static byte[][] SCRIPT_CONDUCT_TABLE = { { 2, 2, 2, 1 },
			{ 2, 2, 2, 2, 2, 2, 2 }, { 1, 2, 2, 1 }, { 2, 2, 2, 1 }, { 2, },
			{ 2, 1, 1, 1, 1 }, { 2, 1, 2, 2 }, { 1, 2, 2 }, { 1, 2, 2 },
			{ 1, 2, 2 }, { 1, 2, 1, 2, 2 }, { 2, 2, 1, 2 }, { 2, 2, 1, },
			{ 2, 1, }, { 1, }, { 2, 1, 2, },
			{ 2, 1, 2, },
			{ 1, }, // roleId(0��ʾ�����ǣ�1��ʾ�Խű�������) roleStateName roleStateValue
					// �޸Ľ�ɫ״̬, ActorItem(�����ڵ�ͼ�еı��) MapCoord (X,Y) roleInitState
					// �ƶ���ɫ, MapIndex MapCoord (X,Y) roleInitState ����ָ��, NpcItem
					// MapCoord (X,Y) roleInitState ���ӽ�ɫ, NpcItem ɾ����ɫ, NpcItem
					// roleActor(����Id��) �޸Ľ�ɫ����, PropertyIndex
					// PropertyFinishValue roleId(0��ʾ������1��ʾ�Ե���) ��ɫ����,
					// roleId(0��ʾ�����ǣ�1��ʾ�Խű�������) goodsId goodsValue(������ʾ��) ������Ʒ,
					// roleId(0��ʾ�����ǣ�1��ʾ�Խű�������) equipsId equipsValue(������ʾ��)
					// ����װ��, roleId(0��ʾ�����ǣ�1��ʾ�Խű�������) skillsId skillsValue
					// ѧ������, dialogType��ͨ�Ի�:0ǿ�ƶԻ�:1�Զ���˵�:2�Զ���:3
					// dialogText(String����) ��ͨ�Ի�, MapCoord (X,Y) MapLayerId
					// MapObjectId ��������, MapCoord (X,Y) MapLayerId ɾ������, NpcItem
					// roleEnalbe �ı��ɫ��Ч��, systemStateId �ı�ϵͳ״̬, QuestIndex
					// QuestFinishValue ����״ֵ̬, PropertyIndex PropertyFinishValue
					// ϵͳ����
			{ 1, },
			{ 1, }, // musicId ��������, aviId ������Ч, musicId ֹͣ����
			{ 2, 2 }, { 2, 2, 2 }, { 1, 2 }, { 2, 2, 2 }, { 2 }, { 2, 2 },
			{ 2 }, { 1 }, { 2 }, { 2 }, { 2 }, { 2, 2, 1, 2, 1, 1 }, { 2, 1 },
			{ 2 }, { 2 }, { 2 },

	};

	// �빤���йص�����
	private static final byte COUNT_SYSTEM_VARIATE = 100;
	private static final byte COUNT_SYSTEM_TASK = 50; // <=127

	// ������ɶ�: RECEIVED,ACCOMPLISHED,SUBMITED//
	public static final byte TASK_VALUE_NOT_RECEIVED = 0;
	public static final byte TASK_VALUE_RECEIVED_NOT_ACCOMPLISHED = 1;
	public static final byte TASK_VALUE_ACCOMPLISHED_NOT_SUBMITED = 99;
	public static final byte TASK_VALUE_SUBMITED = 100;

	// ��Ļ��������

	/**
	 * ������״̬
	 */
	// private static final byte CCT_DELAY_TIME = 0; //ִ��ԭ�ӵ�Ԫʱ���ӳ�,�߼�����
	private static final byte CCT_SHAKE_SCREEN_SWING = 0; // ����Ļ�����
	private static final byte CCT_SHAKE_SCREEN_TIME = 1; // ����Ļ����ʱ
	/*************
      *
      *************/
	private static final byte CCT_CONTROL_SCREEN = 2;
	private static final byte SUB_PULL_DOWN = 0; // ����ƽĻ
	private static final byte SUB_PULL_UP = 1; // ����ƽĻ
	private static final byte SUB_COVER = 2; // �ڵ���Ļ
	private static final byte SUB_CLOSE_SCREEN = 3; // �ر���Ļ
	private static final byte SUB_OPEN_SCREEN = 4; // ����Ļ
	/**
      *
      */

	private static final byte CCT_SHUTTER = 3;
	private static final byte SUB_SHUTTER_OPEN = 0; // ��Ҷ����
	private static final byte SUB_SHUTTER_CLOSE = 1; // ��Ҷ����

	private static final byte CCT_FADE_IN = 4; // ����
	private static final byte CCT_FADE_OUT = 5; // ����

	// ϵͳ�ű�'��'�Ķ���
	private static final byte SV_INDEX_SEG_LEVEL = 0; // �ؿ��õ�����
	private static final byte SV_INDEX_SEG_GLOBAL = 29; // ������Ϸ����
	private static final byte SV_INDEX_SEG_OTHER = 60; // ��������

	// ϵͳ������������
	private static final byte SV_INDEX_SCRIPT_HEROINE_FOLLOW = 0; // Ů�����Ƿ����
	private static final byte SV_INDEX_SCRIPT_SHOP = 8; // �̵�
	private static final byte SV_INDEX_SCRIPT_FACE = 9; // faceID����
	private static final byte SV_INDEX_SCRIPT_SHOW_BOSS_HP = 10; // ��ʾbossѪ��
	private static final byte SV_INDEX_SCRIPT_KEY5_ATTACK = 11; // ����5���Ƿ���Թ�����־����
	private static final byte SV_INDEX_SCRIPT_HEROINE_DIED = 12; // �ж�Ů�����Ƿ�����
	private static final byte SV_INDEX_SCRIPT_CHANGE_HERO = 13; // ������
	public static final byte SV_INDEX_SCRIPT_CAN_CHANGE_HERO = 22; // ���Ի�����

	private static final byte SV_INDEX_SCRIPT_SAVE = 15; // ��Ϸ�浵

	private static final byte SV_INDEX_SCRIPT_WEAPON = 25; // ��������
	private static final byte SV_INDEX_SCRIPT_GAME_END = 26; // ��Ϸͨ��

	private static final byte SV_INDEX_SCRIPT_UI_EQUIP = 33; // װ������
	private static final byte SV_INDEX_SCRIPT_UI_SKILL = 34; // ����
	private static final byte SV_INDEX_SCRIPT_UI_MAKE = 35; // ����

	private static final byte SV_INDEX_SCRIPT_BUY_LEVEL = 36; // �����ؿ��Ʒ�
	public static final byte SV_INDEX_SCRIPT_HAVE_BUY_LEVEL = 37; // �ؿ��Ʒ��Ѿ������һ�������ñ���

	public static final byte SV_INDEX_SCRIPT_BOSS2_ATTACK = 39; // �̹��Ƿ���Թ�����־���ã�0�����ɣ�1������
	public static final byte SV_INDEX_SCRIPT_FOOTMAN_ATTACK = 40; // ����ʿ���Ƿ���Թ�����־���ã�0�����ɣ�1������

	public static short[] systemVariates = new short[COUNT_SYSTEM_VARIATE]; // ��¼ϵͳ����ֵ
	public static short[] m_SystemVariates_Backup = new short[COUNT_SYSTEM_VARIATE]; // ��¼ϵͳ����ֵ
	public static short[] systemTasks = new short[COUNT_SYSTEM_TASK]; // ��¼ϵͳ������ɶ�ֵ
	public static short[] systemTasksActorIDs = new short[COUNT_SYSTEM_TASK]; // ��¼ϵͳ��������levelID
																				// +
																				// actorID,(6bits
																				// +
																				// 10bits)

	//
	public final static byte SUPER_GLOBLE_VARIATE = 0; // ��ȫ��
	public final static byte GLOBLE_VARIATE = 1; // ȫ��
	public final static byte LOCAL_VARIATE = 2; // �ֲ�
	public final static byte OTHER_VARIATE = 3; // �ֲ�

	// ����
	public static byte[] svType = new byte[COUNT_SYSTEM_VARIATE]; // ϵͳ��������
	public static byte[] smType = new byte[COUNT_SYSTEM_TASK]; // ���������
	public static String[] strSM = new String[COUNT_SYSTEM_TASK << 1]; // �����ı�
																		// (����,
																		// ����)

	public static final int NONE_SCRIPT_ID = -1;

	/**
	 * ��ȡ����ϵͳ��������
	 */
	public static void loadQuestVar() {
		DataInputStream dis = null;
		try {
			// clear
			for (int i = 0; i < COUNT_SYSTEM_VARIATE; i++) {
				svType[i] = -1;
			}
			for (int i = 0; i < COUNT_SYSTEM_TASK; i++) {
				smType[i] = -1;
			}

			dis = new DataInputStream("".getClass().getResourceAsStream(
					s_filenameQuest));
			int count = dis.readShort();
			for (int i = 0; i < count; i++) {
				byte ov = dis.readByte();
				byte t = dis.readByte();
				svType[ov] = t;
			}
			count = dis.readShort();
			for (int i = 0; i < count; i++) {
				byte ov = dis.readByte();
				byte t = dis.readByte();
				smType[ov] = t;
				strSM[ov << 1] = dis.readUTF();
				strSM[(ov << 1) + 1] = dis.readUTF();
			}

			dis.close();
		} catch (Exception ex) {
			if (CDebug.showDebugInfo) {
				System.out.println(">>ERROR: loadQuestVar()");
				ex.printStackTrace();
			}
		} finally {
			dis = null;
		}
	}

	/**
	 * ��ʼ������Ľű���Ϣ
	 * 
	 * @param objScaner
	 *            XObject
	 */
	public static void initObjectScript(CObject obj) {
		// ���µ�ǰ����Ľű�
		short scriptID = (short) obj.m_scriptID;
		if (scriptID > NONE_SCRIPT_ID) {

			obj.currentConditions = scriptLevelConditions[scriptID];
			obj.currentConducts = scriptLevelConducts[scriptID];

		}
	}

	public static void scanScript(CObject objScaner) {
		for (int i = objScaner.scanStartSubScriptID; i < objScaner.currentConditions.length; i++) { // for���Ż�
			objScaner.curScriptConditions = objScaner.currentConditions[i];
			// 0.�������ǰ������4��
			switch (objScaner.curScriptConditions[0]) {
			case CDN_PreCdn_NearMe: { // ����NPC: �޲���
				if (!heroIsNear(objScaner)) {
					continue;
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						// System.out.println("DEBUG >>��script precondition:���ǿ���NPC");
					}
				}
				break;
			}
			case CDN_PreCdn_SayToMe: { // ��ҶԴ˶���˵��ʱ
				if (!(CKey.isKeyPressed(CKey.GK_MIDDLE) && heroIsNear(objScaner))) {
					continue;
				} else {
					// CKey.initKey ();
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println("DEBUG >>��script precondition:��ҶԴ˶���˵��ʱ");
					}
				}
				break;
			}
			case CDN_PreCdn_PlayerWin: { // �����ս����ʤʱ
				// debug
				if (CDebug.showErrorInfo) {
					// if ( objScaner.property == null ||
					// objScaner.property.length < XObject.PRO_OBJ_HP )
					// {
					// System.out.println (
					// "WARNNING! scanScript() precondition: �����ս����ʤʱ,actorID="
					// + objScaner.m_actorID + ", this actor no HP." );
					// }
				}
				if (!objScaner.testFlag(dActor.FLAG_DIE_TO_SCRIPT)) {
					continue;
				} else {
					objScaner.clearFlag(dActor.FLAG_DIE_TO_SCRIPT);
					// objScaner.die(false);
					if (CDebug.showDebugInfo) {
						System.out.println("DEBUG >>��script precondition:ս��ʤ��");
					}
					// CAnimation ani =
					// XObject.animations[objScaner.baseInfo[XObject.INFO_OBJ_ANIMATION_ID]];
					// int actionID =
					// objScaner.baseInfo[XObject.INFO_OBJ_ACTION_ID];
					// int maxSCount = ani.actionSequenceCounts[actionID];
					// int lastMaxSDelay = ( (
					// ani.sequenceDatas[ani.actionSequenceOffset[actionID] + (
					// objScaner.asc[Animation.ASC_SQUENCE_INDEX] << 1 )] >>
					// Animation.MOVE_BIT_DURATION ) & Animation.MASK_DURATION
					// );
					// if ( ! ( objScaner.asc[Animation.ASC_SQUENCE_INDEX] + 1
					// >= maxSCount && objScaner.asc[Animation.ASC_FRAME_DELAY]
					// + 1 >= lastMaxSDelay ) )
					// {
					// continue;
					// }
					// else
					// {
					// //debug
					// if ( CDebug.showDebugInfo )
					// {
					// System.out.println (
					// "DEBUG >>��script precondition:�����ս����ʤʱ, actorID=" +
					// objScaner.baseInfo[XObject.INFO_OBJ_ACTOR_ID] );
					// }
					// }
				}
				break;
			}
			case CDN_PreCdn_PlayerFail: { // �����ս��ʧ��ʱ
				if (!CGame.m_hero.testFlag(dActor.FLAG_DIE_TO_SCRIPT)) {
					continue;
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println("script precondition:�����ս��ʧ��ʱ, actorID="
										+ objScaner.m_actorID);
					}
				}
				break;
			}
			case CDN_PreCdn_NoCdn: { // ������ִ��
				break;
			}

			case CDN_PreCdn_LeaveMe: // �뿪����
				if (heroIsNear(objScaner)) {
					continue;
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println("DEBUG >>��script precondition:�����뿪NPC");
					}
				}

				break;

			}
			// 1.ǰ��������������������������
			objScaner.conditionIndex = 1;
			if (checkCondition(objScaner)) {
				// ��������
				objScaner.scanStartSubScriptID = (short) ((i + 1) % objScaner.currentConditions.length);
				objScaner.isInScriptRunning = true;
				// ֪ͨϵͳ����ű�ִ��״̬
				setScriptRun(objScaner, objScaner.currentConducts[i]);
				return; // �ٴν���ʱ���ϴ��Ѿ����ɹ���λ�ã���scanStartScriptID����ʼ�������
			}
		}
		// ��ͷ��ʼ����
		objScaner.scanStartSubScriptID = 0;
	}

	/**
	 * ��⵱ǰ�ű����� ʹ���ⲿ������curScriptCondition, conditionIndex
	 * 
	 * @return boolean
	 */
	private static boolean checkCondition(CObject objScaner) {
		boolean isOK = true;
		// �������
		int cmd = 0; // �ڼ�������
		int conditionID = 0; // ����������
		int conditionCount = objScaner.curScriptConditions[objScaner.conditionIndex++]; // ��������
		while (isOK && cmd < conditionCount) { // �������������Ҫ��Ⲣ��ǰ�����е�������&&�����true,
												// �����
			cmd++;
			conditionID = objScaner.curScriptConditions[objScaner.conditionIndex++];
			// �Ҳ����������������������������whileѭ��
			switch (conditionID) {
			case CDN_ConditionQuestItem: { // ������ɶ�
				int sysTaskIndex = readConditionParam(objScaner, conditionID, 0);
				int operate = readConditionParam(objScaner, conditionID, 1);
				int value = readConditionParam(objScaner, conditionID, 2);

				isOK = compare(systemTasks[sysTaskIndex], operate, value);

				break;

			}

			case CDN_ConditionPropertyValueItem: { // ������������
				if (objScaner.m_actorProperty == null) {
					isOK = false;
					break;
				}

				int objProIndex = readConditionParam(objScaner, conditionID, 0);
				int operate = readConditionParam(objScaner, conditionID, 1);
				int value = readConditionParam(objScaner, conditionID, 2);

				if (objProIndex == IObject.PRO_INDEX_MONEY) {
					isOK = compare(
							(CGame.m_hero.m_actorProperty[objProIndex] & 0x0000ffff),
							operate, value);
				} else {
					isOK = compare(CGame.m_hero.m_actorProperty[objProIndex],
							operate, value);
				}
				break;
			}

			case CDN_ConditionGoodsValueItem: { // ��Ʒ����
				int goodsIndex = readConditionParam(objScaner, conditionID, 0);
				int operate = readConditionParam(objScaner, conditionID, 1);
				int value = readConditionParam(objScaner, conditionID, 2);
				if (CDebug.showDebugInfo) {
					System.out.println("��Ʒ����,��û��ʵ�֣�");
				}

				// �����Ʒ����
				int num = CGame.m_hero.getGoodsNum(goodsIndex);
				isOK = compare(num, operate, value); // Ҫ������Ʒ�Ľṹ���壡����
				break;
			}

			case CDN_ConditionSystemValueItem: { // ϵͳ����

				int sysVarIndex = readConditionParam(objScaner, conditionID, 0);
				int operate = readConditionParam(objScaner, conditionID, 1);
				int value = readConditionParam(objScaner, conditionID, 2);
				isOK = compare(systemVariates[sysVarIndex], operate, value);

				break;
			}

			case CDN_DialogItem: { // �Ի��а���, �Բ߻���˵����һ��ѡ��Ϊ1���ڶ���ѡ��Ϊ2

				int textIndex = readConditionParam(objScaner, conditionID, 0);
				textIndex = Integer.parseInt(dailogLevelText[textIndex].trim());

				// debug
				if (CDebug.showDebugInfo) {
					if (textIndex <= 0 || textIndex > 4) {
						System.out
								.println("WARNNING >> checkCondition(): �Ի��а��� ,textIndex="
										+ textIndex + " maybe illegal!");
					}
				}
				isOK = (CGame.dlgCurLineIndex == textIndex);
				if (isOK) { // ��һ��ѡ�ѡ�о���յ�ǰ��¼��ѡ����
					CGame.dlgCurLineIndex = -1; // �ⲽ�������ܻ������⣡������
				}

				break;
			}

			case CDN_Key: {
				int op = readConditionParam(objScaner, conditionID, 0);
				int key = readConditionParam(objScaner, conditionID, 1);
				isOK = getPressedKey(key);
				if (isOK) {
					CKey.initKey();
				}
				break;
			}

			case CDN_ConditionRoleProperty: {
				int rID = readConditionParam(objScaner, conditionID, 0);
				int pIdx = readConditionParam(objScaner, conditionID, 1);
				int cdt = readConditionParam(objScaner, conditionID, 2);
				int value = readConditionParam(objScaner, conditionID, 3);

				CObject obj = getObject(rID);
				if (obj != null && obj.m_actorProperty != null
						&& pIdx < obj.m_actorProperty.length) {
					isOK = compare(obj.m_actorProperty[pIdx], cdt, value);
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println(">>Error: CDN_ConditionRoleProperty invalid parameters!!");
					}
				}
				break;
			}

			case CDN_ConditionRoleState: {

				int rID = readConditionParam(objScaner, conditionID, 0);
				int sIdx = readConditionParam(objScaner, conditionID, 1);
				int cdt = readConditionParam(objScaner, conditionID, 2);

				CObject obj = getObject(rID);

				if (obj != null) {
					if (cdt == 0) { // ����
						isOK = (obj.m_actorProperty[IObject.PRO_INDEX_CUTINSTATE] == sIdx);
					} else { // �г�
						isOK = (obj.m_actorProperty[IObject.PRO_INDEX_CUTOUTSTATE] == sIdx);
					}
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println(">>Error: CDN_ConditionRoleState invalid parameters!!");
					}
				}

				break;
			}

			case CDN_ConditionRoleCol:

				int rID1 = readConditionParam(objScaner, conditionID, 0);
				int rID2 = readConditionParam(objScaner, conditionID, 1);
				CObject obj1 = getObject(rID1);
				CObject obj2 = getObject(rID2);

				if (!actorIsNear(obj1, obj2)) {
					continue;
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println("DEBUG >>��script precondition ����>>>"
										+ obj1.m_actorID + " ���� ����>>>"
										+ obj2.m_actorID);
					}
				}

				break;

			case CDN_ConditionOptionDlg: // ѡ���ԶԻ�
				int dlgId = readConditionParam(objScaner, conditionID, 0);
				int opiontId = readConditionParam(objScaner, conditionID, 0);
				isOK = (ResLoader.dlgOpValue[dlgId] == opiontId);
				break;

			default:
				if (CDebug.showErrorInfo) {
					System.out
							.println("ERRROR >> checkCondition(): invalid conditionID="
									+ conditionID
									+ "\n\tScript.bin export error!");
				}
				break;
			}
		}
		return isOK;
	}

	/**
	 * ����actorID��ö���
	 * 
	 * @param actorID
	 *            int
	 * @return XObject
	 */
	public static final CObject getObject(int actorID) {
		if (actorID < 0) {
			return null;
		}

		CObject obj = null;
		if (CGame.actorsShellID[actorID] != -1) {
			obj = CGame.m_actorShells[CGame.actorsShellID[actorID]];
		}

		// ����ö���û�б�������ǿ�Ƽ���
		if (obj == null) {
			obj = CGame.tryActivateActor(actorID);
			System.out.println(">>�����ڼ���������Ҫǿ�Ƽ��roleID=" + actorID);
		}
		if (CDebug.showDebugInfo) {
			if (obj == null) {
				System.out
						.println("runScript()->getObject(): ��ɫû�д���,�����Ƿ�actived��actorID="
								+ actorID);
			}
		}
		return obj;
	}

	/**
	 * �ж��������Ƿ񿿽�
	 * 
	 * @param obj
	 *            XObject
	 * @param objOther
	 *            XObject
	 * @return boolean
	 */
	private static boolean heroIsNear(CObject obj) {
		CObject objOther = CGame.m_hero;
		// ����ϵͳ�ű�������˵��ʹ���伤���������жϿ���
		if (obj.testClasssFlag(dActor.CLASS_FLAG_SYSTEMOBJ)) {
			return obj.testActiveBoxCollideObject(objOther);
		} else {
			return Math.abs(obj.m_x - objOther.m_x)
					+ Math.abs(obj.m_y - objOther.m_y) < ((MapData.TILE_WIDTH * 3) << dConfig.FRACTION_BITS);
		}

	}

	/**
	 * �������õ�
	 * 
	 * @param key
	 *            int
	 * @return boolean
	 */
	private static boolean getPressedKey(int key) {
		switch (key) {
		case 0: // 5��
			return CKey.isKeyPressed(CKey.GK_MIDDLE);
		case 1: // ���󰴼�
			return CKey.isKeyPressed(CKey.GK_LEFT);
		case 2: // ���Ұ���
			return CKey.isKeyPressed(CKey.GK_RIGHT);
		case 3: // ���ϰ���
			return CKey.isKeyPressed(CKey.GK_UP);
		case 4: // ���°���
			return CKey.isKeyPressed(CKey.GK_DOWN);
		case 99:
			return true;
		}
		return false;
	}

	/**
	 * ��conditionIndex��ʼ��ȡlength���ֽڳ������ݲ�����
	 * 
	 * @param length
	 *            int
	 * @return int
	 */
	private static int readConditionParam(CObject objScaner, int conditionID,
			int paramIndex) {
		int length = SCRIPT_CONDITION_TABLE[conditionID][paramIndex];
		int param = CTools.readFromByteArray(objScaner.curScriptConditions,
				objScaner.conditionIndex, length);
		objScaner.conditionIndex += length; // Խ���Ѷ�ȡ���ֽ���
		return param;
	}

	private static boolean actorIsNear(CObject obj1, CObject obj2) {
		if (obj1.testClasssFlag(dActor.CLASS_FLAG_SYSTEMOBJ)) {
			return obj1.testActiveBoxCollideObject(obj2);
		} else {
			return Math.abs(obj1.m_x - obj2.m_x)
					+ Math.abs(obj1.m_y - obj2.m_y) < ((MapData.TILE_WIDTH * 3) << dConfig.FRACTION_BITS);
		}

	}

	/**
	 * �Ƚ�2������С
	 * 
	 * @param value0
	 *            int
	 * @param operate
	 *            int
	 * @param value1
	 *            int
	 * @return boolean
	 */
	private static boolean compare(int value0, int operate, int value1) {
		switch (operate) {
		case 0: // >
			return value0 > value1;
		case 1: // >=
			return value0 >= value1;
		case 2: // =
			return value0 == value1;
		case 3: // <=
			return value0 <= value1;
		case 4: // <
			return value0 < value1;
		case 5: // !=
			return value0 != value1;
		default:
			return false;
		}
	}

	/*******************************
	 * ִ�д����
	 *******************************/
	public static int stateBeforeScript; // �ű�ǰ����Ϸ״̬
	public static CObject objScriptRun; // �ű��ĳ��ض���
	public static byte[] curScriptConduct; // ��ǰҪִ�е�����

	/**
	 * �ű�ִ�в���
	 */
	private static int conduct; // ��ǰ��ִ�е�Ԫ
	private static int conductIndex; // ָ��curScriptConduct���һ���ֽ�
	private static int conductCount; // ִ�е�Ԫ����

	/**
	 * ����obj�ű��������ɹ�������ϵͳִ����ű�scriptConduct
	 * 
	 * @param obj
	 *            XObject
	 * @param scriptConduct
	 *            byte[]
	 */

	public static void setScriptRun(CObject obj, byte[] scriptConduct) {
		stateBeforeScript = CGame.m_curState;
		objScriptRun = obj;
		curScriptConduct = scriptConduct;
		conduct = 0; // ��ǰ��ִ�е�Ԫ
		conductIndex = 0; // ָ��curScriptConduct���һ���ֽ�
		conductCount = curScriptConduct[conductIndex++]; // ִ�е�Ԫ����
		Script.scriptLock = true;
		CGame.setGameState(dGame.GST_SCRIPT_RUN);
	}

	/**
	 * ��conductIndex��ʼ��ȡlength���ֽڳ������ݲ�����
	 * 
	 * @param length
	 *            int
	 * @return int
	 */
	private static int readConductParam(int conductID, int paramIndex) {
		int length = SCRIPT_CONDUCT_TABLE[conductID][paramIndex];
		int param = CTools.readFromByteArray(curScriptConduct, conductIndex,
				length);
		conductIndex += length; // Խ���Ѷ�ȡ���ֽ���
		return param;
	}

	private static Vector vObjShowAction = new Vector(); // �ű�ָ��ı��ɫ����

	public static int autoMoveActorCounter; // �Զ��ƶ����������

	public static int autoshowActionCouner; // �Զ��ı䶯��״̬

	public static boolean isActorAutoMove() {
		return autoMoveActorCounter == 0 ? false : true;
	}

	public static boolean isActorAutoAction() {
		return autoshowActionCouner == 0 ? false : true;
	}

	// /**
	// * �ű�ִ��
	// * @return boolean
	// */
	private static boolean runScript() {
		int conductID = -1; // ִ��������
		while (conduct < conductCount) {
			conduct++;
			conductID = curScriptConduct[conductIndex++];
			switch (conductID) {
			case CDT_SetRoleStateValueItem: { // �޸Ľ�ɫ״̬:
												// roleId(0��ʾ�����ǣ�1��ʾ�Խű�������)
												// roleStateName roleStateValue
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():�޸Ľ�ɫ״̬ conductID="
							+ conductID);
				}
				int rID = readConductParam(conductID, 0);
				int value = readConductParam(conductID, 1);
				int actionID = readConductParam(conductID, 2);
				int param = readConductParam(conductID, 3);
				CObject obj = getObject(rID);
				if (obj != null) {
					obj.setState((short) value);
					obj.setAnimAction(actionID);
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println(">>Error: CDT_SetRoleStateValueItem invalid parameters!!");
					}
				}
				break;
			}

			case CDT_SetMoveRoleItem: { // �ƶ���ɫ: actorID mapCoord(X,Y) dir
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():�ƶ���ɫ conductID="
							+ conductID);
				}

				int actorID = readConductParam(conductID, 0); //
				int type = readConductParam(conductID, 1);
				int mapCoordX = readConductParam(conductID, 2); // ��ͼ��x����
				int mapCoordY = readConductParam(conductID, 3); // ��ͼ��y����
				int dis = readConductParam(conductID, 4); // �ƶ�����
				int dir = readConductParam(conductID, 5); // ����
				int actionID = readConductParam(conductID, 6) - 1; // ����ID
				// �Զ��ƶ����������
				autoMoveActorCounter++;

				CObject obj = getObject(actorID);
				if (obj == null) {
					if (CDebug.showDebugInfo) {
						System.out
								.println("DEBUG >>��runScript():�ƶ���ɫ obj= null");
					}
					return false;
				}
				// ���û�г�ʼ�� ����һ�γ�ʼ��
				if (obj.testFlag(dActor.FLAG_NEED_INIT)) {
					obj.clearFlag(dActor.FLAG_NEED_INIT);
					obj.update();
				}

				obj.isAutoMove = true;

				dir = obj.getSetDir(dir);
				obj.setFaceDir(dir);
				if (obj.testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV)) {
					obj.droptoGround();
				}
				// ���ö���
				if (actionID > 0) {
					obj.aniPlayer.clearAniPlayFlag(Player.FLAG_ACTION_ALL);
					obj.setAnimAction(actionID);
				}

				if (type == 1) {
					obj.m_destX = dis << dConfig.FRACTION_BITS;
					obj.m_destY = 0;
				} else {
					obj.m_destX = Math
							.abs(((mapCoordX * MapData.TILE_WIDTH + (MapData.TILE_WIDTH >> 1)) << dConfig.FRACTION_BITS)
									- obj.m_x);
					obj.m_destY = Math
							.abs(((mapCoordY * MapData.TILE_HEIGHT + (MapData.TILE_HEIGHT >> 1)) << dConfig.FRACTION_BITS)
									- obj.m_y);
				}

				// ���������������ִ�����ﶯ��ָ���ô��������һ��ִ��
				if (conduct < conductCount
						&& curScriptConduct[conductIndex] == CDT_SetMoveRoleItem) {
					break;
				} else {
					return false; // ��״̬�л���ʱ�򣬾�Ҫreturn
									// false���ߵ����߽ű�ת����һ��״̬����û�н����ű�������
				}

				// if(actionID > 0) {
				// obj.setAnimAction (actionID);
				// obj.updateAnimation ();
				// }
				// if(type == 1){
				// mapCoordX = dis << dConfig.FRACTION_BITS;
				// mapCoordY = 0;
				// } else{
				// mapCoordX = Math.abs( ( (mapCoordX * MapData.TILE_WIDTH +
				// (MapData.TILE_HEIGHT >> 1)) << dConfig.FRACTION_BITS) -
				// obj.m_x);
				// mapCoordY = 0 /*(mapCoordY * CMap.TILE_HEIGHT +
				// (CMap.TILE_HEIGHT >> 1) << Def.FRACTION_BITS)*/;
				// }

				// vObjMoveObj.addElement(obj);
				// vObjMoveObj.addElement(String.valueOf(mapCoordX));
				// vObjMoveObj.addElement(String.valueOf(mapCoordY));

				// return false;
			}

			case CDT_SetMoveCommandItem: { // ����ָ��: mapIndex mapCoord(X,Y)
											// roleInitState
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():����ָ�� conductID="
							+ conductID);
				}

				int newLevel = readConductParam(conductID, 0);
				int x = readConductParam(conductID, 1);
				int y = readConductParam(conductID, 2);
				/**
				 * �� dir == 0 ����ת dir ==ʱ��ת
				 */
				int dir = (short) readConductParam(conductID, 3);

				x = (short) (x * MapData.TILE_WIDTH + (MapData.TILE_WIDTH >> 1));
				y = (short) (y * MapData.TILE_HEIGHT + (MapData.TILE_HEIGHT >> 1));
				CHero hero = CGame.m_hero;
				CKey.initKey();
				if (newLevel == CGame.curLevelID) {
					// ���ó��ֵ�λ��
					hero.setXY(x, y);
					// ���ó��ֵķ���
					dir = hero.getSetDir(dir);
					hero.setFaceDir(dir);
					// ���»���
					// adjust camera
					CObject.m_otherCamera = 0;
					hero.updateCamera();
					Camera.updateCamera(false);
					hero.droptoGround();
					CGame.m_hero.setState(IObject.ST_ACTOR_WAIT, -1, true);
					// update map buffer
					CGame.gMap.updateMap(Camera.cameraLeft, Camera.cameraTop,
							true);
					break;
				} else {
					CGame.initLoad_Common(newLevel, x, y, dir, true);
					return false; // ��״̬�л���ʱ�򣬾�Ҫreturn
									// false���ߵ����߽ű�ת����һ��״̬����û�н����ű�������
				}
			}

			case CDT_SetAddRoleItem: { // ���ӽ�ɫ: actorID mapCoord(X,Y)
										// roleInitState
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():���ӽ�ɫ conductID="
							+ conductID);
				}
				int actorID = readConductParam(conductID, 0);
				int x = readConductParam(conductID, 1);
				int y = readConductParam(conductID, 2);
				int dir = readConductParam(conductID, 3);

				// trailer Ҫ��Ϊ���Ǵ�����ֱ�Ӵ���
				CObject obj = CGame.tryActivateActor(actorID);
				if (ResLoader.classAIIDs[obj.m_classID] == dActorClass.CLASS_ID_TRAILERCAMERA) {
					obj.clearFlag(dActor.FLAG_BASLIC_NOT_LOADABLE);
					obj.do_trailer();
					CGame.setGameState(dGame.GST_TRAILER_RUN);
					return false;
				}

				// ���ӽ�ɫ
				if (x > 0 && y > 0) {
					x = x * MapData.TILE_WIDTH + (MapData.TILE_WIDTH >> 1);
					y = y * MapData.TILE_HEIGHT + (MapData.TILE_HEIGHT >> 1);
				} else {
					x = obj.m_x >> dConfig.FRACTION_BITS;
					y = obj.m_y >> dConfig.FRACTION_BITS;
				}

				obj.setActorInfo(dActor.INDEX_POSITION_X, x);
				obj.setActorInfo(dActor.INDEX_POSITION_Y, y);

				if (obj != null) {
					// ���ӽ�ɫ
					obj.setFlag(dActor.FLAG_BASIC_VISIBLE);
					obj.clearFlag(dActor.FLAG_BASLIC_NOT_LOADABLE);
					obj.setActorInfo(dActor.INDEX_BASIC_FLAGS, obj.m_flags);

					obj.setXY((short) x, (short) y);
					dir = obj.getSetDir(dir);
					obj.setFaceDir(dir);
					// ���ó�����λ��
					obj.setXY(x, y);

					if (obj.testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV)) {
						obj.droptoGround();
					}
				}

				break;
			}

			case CDT_SetDeleteRoleItem: { // ɾ����ɫ: actorID
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():ɾ����ɫ conductID="
							+ conductID);
				}
				int actorID = readConductParam(conductID, 0);
				CObject obj = getObject(actorID);
				if (obj != null) {
					obj.die(false);
				}
				break;
			}

			case CDT_SetRoleActorItem: { // �޸Ľ�ɫ����: actorID actionID
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():�޸Ľ�ɫ���� conductID="
							+ conductID);
				}

				int actorID = readConductParam(conductID, 0);
				int actionID = readConductParam(conductID, 1);
				int recover = readConductParam(conductID, 2);
				int time = readConductParam(conductID, 3);
				int dur = readConductParam(conductID, 4);
				CObject obj = getObject(actorID);
				if (obj == null) {
					if (CDebug.showDebugInfo) {
						System.out
								.println("DEBUG >>��runScript():�ƶ���ɫ obj= null");
					}
					return false;
				}
				Script.autoshowActionCouner++;
				if (obj == CGame.m_hero) {
					CGame.m_hero.droptoGround();
				}
				// if(obj.testFlag(dActor.FLAG_BASIC_FLIP_X))
				// {
				// int j = 0;
				// }
				// ���û�г�ʼ�� ����һ�γ�ʼ��
				// if(obj.testFlag (dActor.FLAG_NEED_INIT)) {
				// obj.clearFlag (dActor.FLAG_NEED_INIT);
				// obj.update ();
				// }

				obj.isAutoAction = true;

				// //���ö���
				// if(actionID > 0 && dur == 0) {
				// obj.aniPlayer.clearAniPlayFlag(obj.aniPlayer.FLAG_ACTION_ALL);
				// obj.setAnimAction (actionID);
				// }
				obj.preAction = obj.m_actionID;
				obj.changeActionID = actionID;
				obj.actionDur = dur;
				obj.playTimes = time;
				obj.isRecover = recover == 0 ? true : false;

				if (conduct < conductCount
						&& curScriptConduct[conductIndex] == CDT_SetRoleActorItem) {
					break;
				} else {
					return false; // ��״̬�л���ʱ�򣬾�Ҫreturn
									// false���ߵ����߽ű�ת����һ��״̬����û�н����ű�������
				}

				// if(obj != null){
				// vObjShowAction.addElement(obj);
				// vObjShowAction.addElement(String.valueOf(obj.m_actionID));
				// vObjShowAction.addElement(String.valueOf(actionID));
				// vObjShowAction.addElement(String.valueOf(time));
				// vObjShowAction.addElement(String.valueOf(recover));
				// vObjShowAction.addElement(String.valueOf(dur));
				//
				// if(dur == 0){
				// obj.setAnimAction( (short)actionID);
				// }
				// //���������������ִ�����ﶯ��ָ���ô��������һ��ִ��
				// if(conduct < conductCount && curScriptConduct[conductIndex]
				// == CDT_SetRoleActorItem){
				// break;
				// } else{
				// return false; // ��״̬�л���ʱ�򣬾�Ҫreturn
				// false���ߵ����߽ű�ת����һ��״̬����û�н����ű�������
				// }
				// }
				// break;
			}

			case CDT_SetRolePropertyValueItem: { // ��ɫ����:
													// roleId(0��ʾ�����ǣ�1��ʾ�Խű�������)
													// propertyIndex
													// propertyValue
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():��ɫ���� conductID="
							+ conductID);
				}
				int pIdx = readConductParam(conductID, 0);
				int cdt = readConductParam(conductID, 1);
				int value = readConductParam(conductID, 2);
				int rID = readConductParam(conductID, 3);
				CObject obj = getObject(rID);

				if (obj != null && obj.m_actorProperty != null
						&& pIdx < obj.m_actorProperty.length) {
					switch (cdt) {
					case 0:
						// ���Ӿ���
						if (pIdx == IObject.PRO_INDEX_EXP) {
							obj.addExp(value);
						} else {
							obj.m_actorProperty[pIdx] += value;
						}

						// obj.addBonusShow(0 , "����" + pIdx + "���ӣ�" + value ,
						// CObject.NORMAL_ATT);
						break;
					case 1:
						obj.m_actorProperty[pIdx] = (short) value;
						// obj.addBonusShow(0 , "����" + pIdx + "����Ϊ��" + value ,
						// CObject.NORMAL_ATT);
						break;
					}
					// Ѫ�����
					if (pIdx == IObject.PRO_INDEX_HP) {
						// if ( obj.m_actorProperty[CObject.PRO_INDEX_HP] <= 0 )
						// {
						// obj.setState ( CObject.ST_OBJ_DIE );
						// }
					}
					// ��������
					if (pIdx == IObject.PRO_INDEX_HP
							|| pIdx == IObject.PRO_INDEX_MP) {
						obj.adjustHPMP();
					}
					// else if(pIdx == CObject.PRO_INDEX_EXP) {
					// if(obj == CGame.m_hero) {
					// obj.isLevelup ();
					// }
					// }
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println(">>Error: CDT_SetRolePropertyValueItem invalid parameters!!!");
					}
				}
				return false;
			}
				// ������Ʒ: roleId(0��ʾ�����ǣ�1��ʾ�Խű�������) goodsId goodsValue(������ʾ��)
			case CDT_SetAddGoodsItem: {
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():������Ʒ conductID="
							+ conductID);
				}
				int actorID = readConductParam(conductID, 0);
				int goodsID = readConductParam(conductID, 1);
				int num = readConductParam(conductID, 2);

				if (num < 0) {
					CGame.m_hero.dropItem((1 << 12) | goodsID, -num);
				} else if (num > 0) {
					for (int i = 0; i < num; i++) {
						Goods equip = Goods.createGoods((short) 1,
								(short) goodsID);
						CGame.m_hero.addAItem(equip);
					}
				}

				return false;
			}

			case CDT_SetAddEquipsItem: { // ����װ��: roleId(0��ʾ�����ǣ�1��ʾ�Խű�������)
											// equipsId equipsValue(������ʾ��)
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():����װ�� conductID="
							+ conductID);
				}
				int actorID = readConductParam(conductID, 0);
				int goodsID = readConductParam(conductID, 1);
				int num = readConductParam(conductID, 2);

				if (num < 0) {
					// CGame.m_hero.dropAEquip((0<<12)|goodsID,-num);
					System.out.println("����װ��,��ʱû����equipID = " + goodsID);
				} else if (num > 0) {
					for (int i = 0; i < num; i++) {
						Goods equip = Goods.createGoods((short) 0,
								(short) goodsID);
						CGame.m_hero.addAEquip(equip);
					}
				}

				return false;
			}

			case CDT_SetAddSkillsItem: { // ѧ������: roleId(0��ʾ�����ǣ�1��ʾ�Խű�������)
											// skillsId skillsValue
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():ѧ������ conductID="
							+ conductID);
				}
				int actorID = readConductParam(conductID, 0);
				int skillID = readConductParam(conductID, 1);
				int skillID1 = readConductParam(conductID, 2);
				// ...
				return false;
			}

			case CDT_SetAddCommonTalkItem: { // ��ͨ�Ի�: dialogType(��ͨ�Ի�:0 ǿ�ƶԻ�:1
												// �Զ���˵�:2 �Զ���:3) textID
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():�Ի� conductID="
							+ conductID);
				}

				CGame.dlgType = (short) readConductParam(conductID, 0);
				CGame.dlgActorID = (short) readConductParam(conductID, 1);
				CGame.dlgShowPos = (short) readConductParam(conductID, 2);
				CGame.dlgHeadActionID = (short) (readConductParam(conductID, 3) - 1);
				CGame.dlgTextID = (short) readConductParam(conductID, 4);
				CHero hero = CGame.m_hero;
				/**
				 * ת�����ű��Ի�����״̬
				 * 
				 * @param <any> GAME_STATE_SCRIPT_DIALOG
				 */
				hero.droptoGround();
				// ����Ի�״̬���Զ��� ��װ��Ϊվ��
				if (CGame.dlgType != CGame.DLG_TYPE_DEFINE) {
					hero.setState(IObject.ST_ACTOR_WAIT, -1, true);
				}

				CKey.initKey();
				// hero.updateCamera();
				// hero.update();
				// Camera.updateCamera(false);
				// CGame.gMap.updateMap(Camera.cameraLeft,Camera.cameraTop,true);

				CGame.setGameState(dGame.GST_SCRIPT_DIALOG);

				return false; // �нű�״̬�л���ʱ�򣬾�Ҫreturn
								// false���ߵ����߽ű�ת����һ��״̬����û�н����ű�������
			}
			case CDT_SetAddMapObjectItem: { // ��������: mapCoord(X,Y) mapLayerId
											// mapObjectId
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():�������� conductID="
							+ conductID);
				}

				int gx = readConductParam(conductID, 0);
				int gy = readConductParam(conductID, 1);
				int mapLayerID = readConductParam(conductID, 2);
				// �������ݲ���������
				if (mapLayerID == 3) {
					mapLayerID = MapData.MAP_LAYER_PHY;
				}
				int newLayerValue = readConductParam(conductID, 3);
				CGame.gMap.mapRes.modifyMapLayer(mapLayerID, gx, gy,
						newLayerValue);
				if (CGame.gMap.mapRes.modifyMapLayer(mapLayerID, gx, gy,
						newLayerValue)) {
					CGame.addSaveInfo2hs(CGame.TYPE_SAVE_INFO_MAP,
							CGame.curLevelID, ((gx & 0xfff) << 12)
									| (gy & 0xfff), new short[] {
									(byte) mapLayerID, (byte) newLayerValue });
				}
				break;
			}
			case CDT_SetDeleteMapObjectItem: { // ɾ������: mapCoord(X,Y) mapLayerId
			// //debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():ɾ������ conductID="
							+ conductID);
				}

				int gx = readConductParam(conductID, 0);
				int gy = readConductParam(conductID, 1);
				int mapLayerID = readConductParam(conductID, 2);
				byte none = (byte) 0;
				// �������ݲ���������
				if (mapLayerID == 3) {
					mapLayerID = MapData.MAP_LAYER_PHY;
				}
				if (mapLayerID == MapData.MAP_LAYER_BUILD) {
					none = MapData.MAP_LAYER_BUILD_NONE_DATA;
				} else if (mapLayerID == MapData.MAP_LAYER_GROUND) {
					none = MapData.MAP_LAYER_DATA_NONE_DATA;
				} else if (mapLayerID == MapData.MAP_LAYER_PHY) {
					none = MapData.PHY_AIR;
				}
				CGame.gMap.mapRes.modifyMapLayer(mapLayerID, gx, gy, none);
				break;
			}
			case CDT_SetObjectEnalbeItem: { // �ı��ɫ��Ч��: npcItem roleEnalbe
				// debug
				if (CDebug.showDebugInfo) {
					System.out
							.println("DEBUG >>��runScript():�ı��ɫ��Ч�� conductID="
									+ conductID);
				}
				int actorID = readConductParam(conductID, 0);
				int op = readConductParam(conductID, 1);

				CObject actor = getObject(actorID);
				if (actor != null) {
					if (op == 1) {
						actor.clearFlag(dActor.FLAG_NO_LOGIC);
					} else {
						actor.setFlag(dActor.FLAG_NO_LOGIC);
					}
				}
				break;
			}
			case CDT_SetSystemStateItem: { // �ı�ϵͳ״̬: systemStateId
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():�ı�ϵͳ״̬ conductID="
							+ conductID);
				}
				byte sysStateIndex = (byte) readConductParam(conductID, 0);
				stateBeforeScript = sysStateIndex; // ����stateBeforeScript
				// ����
				CGame.setGameState(sysStateIndex);
				if (sysStateIndex == dGame.GST_GAME_RUN) {
					return false;
				}
				return true; // �л�ϵͳ״̬��,��ֱ�ӽ����ű�. ������滹�нű�,��ô�ͺ���!
			}

			case CDT_SetQuestItem: { // ����״ֵ̬: questIndex questFinishValue
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():����״ֵ̬ conductID="
							+ conductID);
				}

				int sysTasksIndex = readConductParam(conductID, 0);
				int op = readConductParam(conductID, 1);
				int newValue = readConductParam(conductID, 2);
				setTaskInfo(sysTasksIndex, (byte) newValue, op == 0);
				break;
			}
			case CDT_SetConditionSystemValueItem: { // ϵͳ����: propertyIndex
													// propertyFinishValue
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():ϵͳ���� conductID="
							+ conductID);
				}

				int sysVarIndex = readConductParam(conductID, 0);
				int op = readConductParam(conductID, 1);
				int newValue = readConductParam(conductID, 2);
				switch (sysVarIndex) {
				case SV_INDEX_SCRIPT_SHOW_BOSS_HP:
					GameEffect.prepareToRender(GameEffect.YES_RENDER,
							GameEffect.EFF_BOSS_HP);
					break;
				// ������
				case SV_INDEX_SCRIPT_CHANGE_HERO:
					CGame.m_hero.switchHero(newValue, false);
					break;
				// װ������
				case SV_INDEX_SCRIPT_UI_EQUIP:

					CGame.setGameState(dGame.GST_GAME_UI);
					GameUI.setCurrent(GameUI.Form_equip);

					return false;
					// �������
				case SV_INDEX_SCRIPT_UI_MAKE:

					CGame.setGameState(CGame.GST_GAME_UI);
					GameUI.setCurrent(GameUI.Form_make);

					return false;
					// ���ܽ���
				case SV_INDEX_SCRIPT_UI_SKILL:

					CGame.setGameState(CGame.GST_GAME_UI);
					GameUI.setCurrent(GameUI.Form_SKILL);

					return false;
					// �ؿ��Ʒ�
				case SV_INDEX_SCRIPT_BUY_LEVEL:

					return false;

					// ��Ϸ�浵
				case SV_INDEX_SCRIPT_SAVE:
					// �ȱ���ȫ�ֶ��� add by lin 09.4.29
					CGame.addActorInfo2hs(CGame.curLevelID);
					game.rms.Record.saveToRMS(game.rms.Record.DB_NAME_GAME, 1);
					if (CGame.m_hero != null) {
						// CGame.m_hero.addBonusShow(0,"��Ϸ�Ѿ�����",(byte)1);
					}
					break;
				// ��Ϸͨ��
				case SV_INDEX_SCRIPT_GAME_END:

					CGame.destroyLevel(true);
					CGame.setGameState(CGame.GST_MAIN_MENU);
					return false;

				case SV_INDEX_SCRIPT_WEAPON:
					if (newValue == 0) {
						if (CGame.m_hero != null) {
							// systemVariates[sysVarIndex] = 1;
							CGame.m_hero.m_actorProperty[CObject.PRO_WEAPON_NUM] = 2;
						}
					} else if (newValue == 1) {
						if (CGame.m_hero != null) {
							// systemVariates[sysVarIndex] = 2;
							CGame.m_hero.m_actorProperty[CObject.PRO_WEAPON_NUM] = 3;
						}
					}
					break;
				default:
					// �ۼ�
					if (op == 0) {
						systemVariates[sysVarIndex] += newValue;
					} else {
						systemVariates[sysVarIndex] = (short) newValue;
					}
					break;
				}
				break;
			}
			case CDT_SetPlayMusicItem: { // ��������: musicId
				int musicID = readConductParam(conductID, 0);
				SoundPlayer.playSingleSound(musicID, -1);
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():�������� conductID="
							+ conductID);
				}
				break;
			}
				// case CDT_SetPlayAviItem: //������Ч: aviId
				//
				// //debug
				// if ( CDebug.showDebugInfo )
				// {
				// System.out.println ( "DEBUG >>��runScript():������Ч conductID=" +
				// conductID );
				// }
				// System.out.println ( "WARNING >> \tNo implement!" );
				// break;
			case CDT_SetStopMusicItem: // ֹͣ����: musicId
				int musicID = readConductParam(conductID, 0);
				SoundPlayer.stopSingleSound();
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��runScript():ֹͣ���� conductID="
							+ conductID);
				}
				System.out.println("WARNING >> \tNo implement!");
				break;
			//
			// //����ָ��
			case CDT_SetSign: { // 20
				int roleID = readConductParam(conductID, 0);
				// modify by lin 2009.4.27 ����idҪ��1
				int faceID = readConductParam(conductID, 1) - 1;
				System.out.println(">>���ñ�־��roleID=" + roleID + ", faceID="
						+ faceID);
				CObject aa = getObject(roleID);
				if (aa != null) {
					aa.setFaceInfo(faceID);
				}
				break;
			}
			case CDT_SetReborn: { // 21
				int roleID = readConductParam(conductID, 0);
				int count = readConductParam(conductID, 1); // �����Ĵ���
				int span = readConductParam(conductID, 2); // ������ʱ����
				System.out.println(">>��ɫ������roleID=" + roleID + ", count="
						+ count + ", span=" + span);
				CObject actor = getObject(roleID);
				if (actor != null) {
					actor.initialize();
				}

				break;
			}
			case CDT_SetCameraCtrl: { // 22
				int type = readConductParam(conductID, 0); // ϵͳ����������
				int value = readConductParam(conductID, 1);
				System.out.println(">>��Ļ���ƣ�type=" + type + ", value=" + value);
				switch (type) {
				case CCT_SHAKE_SCREEN_SWING: // �����أ�
					GameEffect.setSysShakeScreen(value, 0);
					break;
				case CCT_SHAKE_SCREEN_TIME: // ��ms��
					GameEffect.setSysShakeScreen(0, value);
					break;

				case CCT_CONTROL_SCREEN: // ������Ļ�Ĳ���
					switch (value) {
					case SUB_PULL_DOWN:
						GameEffect.setCover(GameEffect.COVER_PULL_DOWN);
						break;

					case SUB_PULL_UP:
						GameEffect.setCover(GameEffect.COVER_PULL_UP);
						break;

					case SUB_COVER:
						GameEffect.setCover(GameEffect.COVER_NORMAL);
						break;

					case SUB_CLOSE_SCREEN:
						GameEffect.setCover(GameEffect.COVER_CLOSE);
						break;

					case SUB_OPEN_SCREEN:
						GameEffect.setCover(GameEffect.COVER_OPEN);
						break;
					}
					break;

				case CCT_SHUTTER: // ��Ҷ��Ч��
					switch (value) {
					case SUB_SHUTTER_OPEN:
						GameEffect.setShutter(GameEffect.SHUTTER_OPEN);
						break;

					case SUB_SHUTTER_CLOSE:
						GameEffect.setShutter(GameEffect.SHUTTER_CLOSE);
						break;
					}
					break;

				case CCT_FADE_IN: // ��ms��
					GameEffect.setScreenFade(GameEffect.TYPE_FADE_IN, 0, value);
					break;

				case CCT_FADE_OUT: // ��ms��
					GameEffect
							.setScreenFade(GameEffect.TYPE_FADE_OUT, 0, value);
					break;
				}
				break;
			}
				//
			case CDT_SetMoveCameraToPt: { // ��camera�ƶ�ָ���ص�, 23
				CHero hero = CGame.m_hero;
				int x = readConductParam(conductID, 0);
				int y = readConductParam(conductID, 1);
				int sp = readConductParam(conductID, 2);
				System.out.println(">>��Ļ����ĳ�㣺x=" + x + ", y=" + y + ", sp="
						+ sp);
				CGame.VS_SP = sp << dConfig.FRACTION_BITS;
				CGame.vsDstX = (x * MapData.TILE_WIDTH + (MapData.TILE_WIDTH >> 1)) << dConfig.FRACTION_BITS;
				CGame.vsDstY = (y * MapData.TILE_HEIGHT + (MapData.TILE_HEIGHT >> 1)) << dConfig.FRACTION_BITS;
				CGame.setGameState(CGame.GST_CAMARE_MOVE);

				hero.droptoGround();
				hero.setState(hero.ST_ACTOR_WAIT, -1, true);
				CKey.initKey();
				hero.update();
				return false;

			}
				//
			case CDT_SetCameraFollowObj: { // 24

				int roleID = readConductParam(conductID, 0);
				System.out.println(">>�������roleID=" + roleID);

				CObject obj = getObject(roleID);
				// ȱ�����ö��������Ļλ�õĲ���
				obj.m_posInCamera = obj.HERO_IN_CAMERA_X_BATTLE
						| obj.HERO_IN_CAMERA_Y_BOTTOM;
				obj.updateCamera();
				if (obj == CGame.m_hero) {
					Camera.cammeraObj = null;
				} else {
					Camera.cammeraObj = obj;
				}
				Camera.updateCamera(false);
				CGame.gMap.updateMap(Camera.cameraLeft, Camera.cameraTop, true);
				break;

			}
				//
			case CDT_SetMoveCameraToObj: { // 25
				int roleID = readConductParam(conductID, 0);
				int sp = readConductParam(conductID, 1);
				System.out.println(">>��ͷ������ɫ��roleID=" + roleID + ", sp=" + sp);
				CObject obj = getObject(roleID);
				CGame.vsDstX = obj.m_x;
				CGame.vsDstY = obj.m_y;
				CGame.setGameState(CGame.GST_CAMARE_MOVE);
				return false;
			}
				//
			case CDT_SetScriptRunPause: { // 26

				int pauseTime = readConductParam(conductID, 0);
				System.out.println(">>ִ����ʱ��pauseTime=" + pauseTime);
				runScriptPauseTimer = pauseTime / dConfig.FPS_RATE;

				return false;
			}
				//
			case CDT_SetScriptAutoKey:

				int keyID = readConductParam(conductID, 0);
				if (keyID >= 0) {
					CKey.initAutoKeyMap(ResLoader.keyMapArray[keyID]);
					CGame.setGameState(CGame.GST_GAME_RUN);
				}

				return false;
				//
			case CDT_SetDelMutiRole: // ɾ�����ɫ
				int delCount = readConductParam(conductID, 0);
				while (delCount > 0) {
					delCount--;
					int actorID = CTools.readFromByteArray(curScriptConduct,
							conductIndex, 2);
					if (CDebug.showDebugInfo) {
						System.out.println("DEBUG >>��DelRoleID" + actorID);
					}

					CObject obj = getObject(actorID);
					if (obj != null) {
						obj.die(false);
					}
					conductIndex += 2;
				}
				break;
			//
			case CDT_SetAddMutiRole:
				int addCount = readConductParam(conductID, 0);
				while (addCount > 0) {
					addCount--;
					int actorID = readConductParam(CDT_SetAddRoleItem, 0);
					int x = readConductParam(CDT_SetAddRoleItem, 1);
					int y = readConductParam(CDT_SetAddRoleItem, 2);
					int dir = readConductParam(CDT_SetAddRoleItem, 3);
					CObject obj = null;
					obj = CGame.tryActivateActor(actorID);
					if (CDebug.showDebugInfo) {
						System.out.println("DEBUG >>��addRoleID" + actorID);
					}

					if (x > 0 && y > 0) {
						x = x * MapData.TILE_WIDTH + (MapData.TILE_WIDTH >> 1);
						y = y * MapData.TILE_HEIGHT
								+ (MapData.TILE_HEIGHT >> 1);
					} else {
						x = obj.m_x >> dConfig.FRACTION_BITS;
						y = obj.m_y >> dConfig.FRACTION_BITS;
					}
					obj.setActorInfo(dActor.INDEX_POSITION_X, x);
					obj.setActorInfo(dActor.INDEX_POSITION_Y, y);

					if (obj != null) {
						// ���ӽ�ɫ
						obj.setFlag(dActor.FLAG_BASIC_VISIBLE);
						obj.clearFlag(dActor.FLAG_BASLIC_NOT_LOADABLE);
						obj.setActorInfo(dActor.INDEX_BASIC_FLAGS, obj.m_flags);

						obj.setXY((short) x, (short) y);
						dir = obj.getSetDir(dir);
						obj.setFaceDir(dir);
						// ���ó�����λ��
						obj.setXY(x, y);

						if (obj.testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV)) {
							obj.droptoGround();
						}
					}
				}
				break;
			//
			// case CDT_SetMoveMutiRole: //���ɫ�ƶ�
			// int moveCount = readConductParam(conductID , 0); //�ƶ���������
			// while(moveCount > 0){
			// moveCount--;
			// int roleId = CTools.readFromByteArray(curScriptConduct ,
			// conductIndex , 2);
			// conductIndex += 2;
			//
			// int type = CTools.readFromByteArray(curScriptConduct ,
			// conductIndex , 2);
			// conductIndex += 2;
			//
			// int x = CTools.readFromByteArray(curScriptConduct , conductIndex
			// , 2);
			// conductIndex += 2;
			//
			// int y = CTools.readFromByteArray(curScriptConduct , conductIndex
			// , 2);
			// conductIndex += 2;
			//
			// int dis = CTools.readFromByteArray(curScriptConduct ,
			// conductIndex , 2);
			// conductIndex += 2;
			//
			// int dir = CTools.readFromByteArray(curScriptConduct ,
			// conductIndex , 2);
			// conductIndex += 2;
			//
			// int actionID = CTools.readFromByteArray(curScriptConduct ,
			// conductIndex , 2);
			//
			// // AbstractActor obj = getObject(actorID);
			// // if(obj != null){
			// // vObjShowAction.addElement(obj);
			// // vObjShowAction.addElement(String.valueOf(obj.m_actionID));
			// // vObjShowAction.addElement(String.valueOf(actionID));
			// // vObjShowAction.addElement(String.valueOf(time));
			// // vObjShowAction.addElement(String.valueOf(recover));
			// // vObjShowAction.addElement(String.valueOf(dur));
			// // }
			//
			// }
			//
			// break;
			//
			// case CDT_SetShowCG: //��Ϸ��չʾ
			// CGanmationID = (short)readConductParam(conductID , 0);
			// CGactionID = (short)readConductParam(conductID , 1);
			// dlgShowPos = (short)readConductParam(conductID , 2);
			// dlgTextID = (short)readConductParam(conductID , 3);
			// dlgTextPos = (short)readConductParam(conductID , 4);
			// dlgShowType = (short)readConductParam(conductID , 5);
			//
			// if(CGanmationID >= 0){
			// loadAnimation(Def.s_fileCGResName , new int[]{CGanmationID} ,
			// CGAnimations , CGaniMlgs);
			// }
			//
			// setGameState(GAME_STATE_CG);
			// return false;
			//
			// case CDT_SetTeamPlayer:
			// int roleID = (short)readConductParam(conductID , 0);
			// int queueType =(short)readConductParam(conductID , 1);
			// //........���ö���.........
			// break;
			//
			case CDT_SetOptionDlg:
				CHero hero = CGame.m_hero;
				hero.droptoGround();
				hero.setState(hero.ST_ACTOR_WAIT, -1, true);
				CGame.dlgIndex = (byte) readConductParam(conductID, 0);
				if (GameEffect.windowOpen) {
					GameEffect.clearAllWindow();
				}

				CGame.setGameState(CGame.GST_SCRIPT_OPDLG);
				return false;
				// �����̵�
			case CDT_SetShopItem:
				int shopId = (byte) readConductParam(conductID, 0);
				GameUI.setShopId((byte) shopId);
				CGame.setGameState(CGame.GST_GAME_UI);
				GameUI.setCurrent(GameUI.Form_shop);
				return false;
			default:
				if (CDebug.showDebugInfo) {
					System.out
							.println("DEBUG >>��runScript(): no match the conduct!");
				}
				break;
			}
		}
		return true;
	}

	/******************************
	 * ����ű�logic
	 ******************************/
	// rumTask()��ֹͣʱ��
	private static int runScriptPauseTimer;

	public static void doScriptLogic() {
		if (runScriptPauseTimer <= 0) {
			if (runScript()) {
				objScriptRun.isInScriptRunning = false;
				objScriptRun = null;
				curScriptConduct = null;
				if (stateBeforeScript == CGame.GST_GAME_LOAD) {
					stateBeforeScript = CGame.GST_GAME_RUN;
				}
				CGame.setGameState(stateBeforeScript);
			}
		} else if (runScriptPauseTimer > 0) {
			runScriptPauseTimer--;
			CGame.updateAnimLogic();
		}
	}

	/**
	 * ����
	 */

	/**
	 * ����������
	 * 
	 * @param taskID
	 *            int
	 * @param taskValue
	 *            byte
	 * @return boolean
	 */
	public static final boolean isTask(int taskID, byte taskValue) {
		return systemTasks[taskID] == taskValue;
	}

	/**
	 * ��������������Ϣ ����Ϊ�����������ļ�����
	 * 
	 * @param sysTasksIndex
	 *            int
	 * @param newValue
	 *            byte
	 */
	public static void setTaskInfo(int sysTasksIndex, byte newValue,
			boolean accumulate) {
		// ָ�������һ�α����ܻ�δ���ʱ���ͼ�¼����󶨵Ķ���
		if (TASK_VALUE_RECEIVED_NOT_ACCOMPLISHED == newValue) {
			systemTasksActorIDs[sysTasksIndex] = (short) ((CGame.curLevelID << 10) | objScriptRun.m_actorID);
		}
		if (accumulate) {
			systemTasks[sysTasksIndex] += newValue;
		} else {
			systemTasks[sysTasksIndex] = newValue;
		}
		// ��������״̬�仯����������ύ��󶨶�������������Ƴ�������
		switch (newValue) {
		case TASK_VALUE_NOT_RECEIVED:
			break;
		case TASK_VALUE_RECEIVED_NOT_ACCOMPLISHED:
			// m_hero.addSubHPShow ( 0 , "�ӵ�:" );
			break;
		case TASK_VALUE_ACCOMPLISHED_NOT_SUBMITED:
			// m_hero.addSubHPShow ( 0 , "���:" );
			break;
		case TASK_VALUE_SUBMITED:
			// m_hero.addSubHPShow ( 0 , "�ύ:" );
			break;
		}
	}

	/**
	 * ��ȡ�ӹ���δ�ύ������
	 * 
	 * @return int[]
	 */
	public static int[] getActiveTaskId() {
		Vector v = new Vector();
		for (int i = 0; i < systemTasks.length; i++) {
			if (systemTasks[i] >= 1 && systemTasks[i] <= 99) {
				v.addElement(new Integer(i));
			}
		}
		int id[] = new int[v.size()];
		for (int i = 0; i < v.size(); i++) {
			id[i] = ((Integer) v.elementAt(i)).intValue();
		}
		return id;
		// return new int[]{0,1,2,3};
	}

}
