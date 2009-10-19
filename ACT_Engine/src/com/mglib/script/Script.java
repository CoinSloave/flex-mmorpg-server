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
	 * 加载脚本
	 **********************/
	/************
	 * 关卡脚本关卡脚本
	 ************/
	// 关卡脚本的条数
	public static short scriptLevelCount;

	// 所有人物的脚本条件: [scriptID][子脚本的编号][前提条件，条件单元数量，(条件单元，(参数1，参数2，...))...]
	public static byte[][][] scriptLevelConditions;

	// 所有人物的脚本执行: [scriptID][子脚本的编号][执行数量，(执行单元，(参数1，参数2，...))...]
	public static byte[][][] scriptLevelConducts;

	// 关卡脚本对话的文本数目
	public static short dialogLeveTextlCount;

	// 所有人物的脚本对话文本
	public static String[] dailogLevelText;

	// 所有人物的脚本对话文本标志信息
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
	 * k700，每个场景2个文件（对话文本和脚本）
	 * 
	 * @param newLevel
	 *            int
	 */
	public static void loadScriptK700(int newLevel) {
		DataInputStream dis = null;
		try {
			// 读取脚本
			dis = new DataInputStream("".getClass().getResourceAsStream(
					"/bin/Script/" + newLevel + ".sd"));
			readLevel(dis, newLevel, false);
			// #if !W958C
			System.gc();
			// #endif

			// 读取对话文本
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
			System.out.println("DEBUG >>　\nread level:" + --newLevel
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
				System.out.println("DEBUG >>　read " + (newLevel - 1) + " text "
						+ i + ": " + str);
			}
		}
		dialogLeveTextlCount = (short) dtCount;

		// read end, skip for next
		dis.skip(offset[segmentCount] - offset[newLevel + 1]);

	}

	/**
	 * k700载入对话文本
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
				System.out.println("DEBUG >>　read " + (newLevel - 1) + " text "
						+ i + ": " + str);
			}
		}
	}

	/**
	 * 读取脚本文件时用到的临时量
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
			System.out.println("DEBUG >>　\ta script: sub script count is "
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
		int length = dis.readInt(); // 所有子条件的长度
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
		int length = dis.readInt(); // 所有子执行的长度
		byte[] conductUnits = new byte[length + 1];
		dis.read(conductUnits);

		if (CDebug.showScripLoadInfo) {
			int count = conductUnits[0];
			// output information
			System.out.println("DEBUG >>　\t\tConduct: " + " count: " + count);
			System.out.println("DEBUG >>　\t\t\tunits: ");
			String s = "";
			for (int i = 1; i < length + 1; i++) {
				s += Integer.toHexString(conductUnits[i]) + " ";
			}
			System.out.println("\t\t\t\t" + s);
		}

		return conductUnits;
	}

	/*******************************
	 * 脚本变量
	 *******************************/
	// 当前人物的脚本条件: [子脚本的编号][前提条件，条件单元数量，(条件单元，(参数1，参数2，...))...]
	public byte[][] currentConditions;

	// 当前人物的脚本执行: [子脚本的编号][执行数量，(执行单元，(参数1，参数2，...))...]
	public byte[][] currentConducts;

	// 脚本条件检测从此编号开始
	public short scanStartSubScriptID;

	// 指向curScriptCondition里的一个字节
	public short conditionIndex = 0;

	// 当前脚本条件
	public byte[] curScriptConditions;

	// 是否处于脚本运行中
	public boolean isInScriptRunning;

	// 人物对象的脚本中是否含有指定信息
	public boolean hasWord; // 是否有对话

	public Vector vTaskList = new Vector(); // 对象关联的任务编号链表

	/**********************************
	 * 脚本部分
	 ******************************/

	/*******************************
	 * 脚本数据段
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

	// 新增指令
	public static final byte CDN_Key = 5;
	public static final byte CDN_ConditionRoleProperty = 6;
	public static final byte CDN_ConditionRoleState = 7;
	public static final byte CDN_ConditionRoleCol = 8; // 对象碰撞
	public static final byte CDN_ConditionOptionDlg = 9; // 选择性对话

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

	// 新增指令
	public static final byte CDT_SetSign = 20;
	public static final byte CDT_SetReborn = 21;
	public static final byte CDT_SetCameraCtrl = 22;
	public static final byte CDT_SetMoveCameraToPt = 23;
	public static final byte CDT_SetCameraFollowObj = 24;
	public static final byte CDT_SetMoveCameraToObj = 25;
	public static final byte CDT_SetScriptRunPause = 26;
	// 新增指令 2008/9/12
	public static final byte CDT_SetScriptAutoKey = 27;
	public static final byte CDT_SetDelMutiRole = 28;
	public static final byte CDT_SetAddMutiRole = 29;
	public static final byte CDT_SetMoveMutiRole = 30;
	// add by zengkai 2008/9/23
	public static final byte CDT_SetShowCG = 31; // 显示CG
	public static final byte CDT_SetTeamPlayer = 32; // 队员加入或离开队伍
	//
	public static final byte CDT_SetOptionDlg = 33; // 设置选择性对话
	public static final byte CDT_SetShopItem = 34; // 商店

	/**
	 * first dimension : condition ID second dimesion : 参数1长度，参数2长度，。。。 Note:
	 * “长度”以 byte 为单位
	 */
	private final static byte[][] SCRIPT_CONDITION_TABLE = {
			{ 2, 1, 2 },
			{ 2, 1, 2 }, // 任务完成度, 对象基本属性
			{ 2, 1, 2 }, { 2 }, { 2, 1, 2 }, { 1, 1 }, { 2, 2, 1, 2 },
			{ 2, 2, 1 }, { 2, 1, 2 }, { 2, 2 }, // 系统变量, 对话中包含, 物品条件, 按键条件,
												// 对象属性,, 对象状态,, 对象接近对象, 选择性对话

	};

	/**
	 * first dimension : script ID second dimesion : 参数1长度，参数2长度，。。。 Note: “长度”以
	 * byte 为单位
	 */
	private final static byte[][] SCRIPT_CONDUCT_TABLE = { { 2, 2, 2, 1 },
			{ 2, 2, 2, 2, 2, 2, 2 }, { 1, 2, 2, 1 }, { 2, 2, 2, 1 }, { 2, },
			{ 2, 1, 1, 1, 1 }, { 2, 1, 2, 2 }, { 1, 2, 2 }, { 1, 2, 2 },
			{ 1, 2, 2 }, { 1, 2, 1, 2, 2 }, { 2, 2, 1, 2 }, { 2, 2, 1, },
			{ 2, 1, }, { 1, }, { 2, 1, 2, },
			{ 2, 1, 2, },
			{ 1, }, // roleId(0表示对主角，1表示对脚本对象本身) roleStateName roleStateValue
					// 修改角色状态, ActorItem(对象在地图中的编号) MapCoord (X,Y) roleInitState
					// 移动角色, MapIndex MapCoord (X,Y) roleInitState 传送指令, NpcItem
					// MapCoord (X,Y) roleInitState 增加角色, NpcItem 删除角色, NpcItem
					// roleActor(动作Id号) 修改角色动作, PropertyIndex
					// PropertyFinishValue roleId(0表示对自身，1表示对敌人) 角色属性,
					// roleId(0表示对主角，1表示对脚本对象本身) goodsId goodsValue(负数表示减) 增加物品,
					// roleId(0表示对主角，1表示对脚本对象本身) equipsId equipsValue(负数表示减)
					// 增加装备, roleId(0表示对主角，1表示对脚本对象本身) skillsId skillsValue
					// 学到技能, dialogType普通对话:0强制对话:1自定义菜单:2自定义:3
					// dialogText(String类型) 普通对话, MapCoord (X,Y) MapLayerId
					// MapObjectId 创建地物, MapCoord (X,Y) MapLayerId 删除地物, NpcItem
					// roleEnalbe 改变角色有效性, systemStateId 改变系统状态, QuestIndex
					// QuestFinishValue 任务状态值, PropertyIndex PropertyFinishValue
					// 系统变量
			{ 1, },
			{ 1, }, // musicId 播放音乐, aviId 播放特效, musicId 停止音乐
			{ 2, 2 }, { 2, 2, 2 }, { 1, 2 }, { 2, 2, 2 }, { 2 }, { 2, 2 },
			{ 2 }, { 1 }, { 2 }, { 2 }, { 2 }, { 2, 2, 1, 2, 1, 1 }, { 2, 1 },
			{ 2 }, { 2 }, { 2 },

	};

	// 与工具有关的数据
	private static final byte COUNT_SYSTEM_VARIATE = 100;
	private static final byte COUNT_SYSTEM_TASK = 50; // <=127

	// 任务完成度: RECEIVED,ACCOMPLISHED,SUBMITED//
	public static final byte TASK_VALUE_NOT_RECEIVED = 0;
	public static final byte TASK_VALUE_RECEIVED_NOT_ACCOMPLISHED = 1;
	public static final byte TASK_VALUE_ACCOMPLISHED_NOT_SUBMITED = 99;
	public static final byte TASK_VALUE_SUBMITED = 100;

	// 屏幕控制类型

	/**
	 * 分两级状态
	 */
	// private static final byte CCT_DELAY_TIME = 0; //执行原子单元时间延迟,逻辑次数
	private static final byte CCT_SHAKE_SCREEN_SWING = 0; // 振屏幕：振幅
	private static final byte CCT_SHAKE_SCREEN_TIME = 1; // 振屏幕：振时
	/*************
      *
      *************/
	private static final byte CCT_CONTROL_SCREEN = 2;
	private static final byte SUB_PULL_DOWN = 0; // 下拉平幕
	private static final byte SUB_PULL_UP = 1; // 上拉平幕
	private static final byte SUB_COVER = 2; // 遮挡屏幕
	private static final byte SUB_CLOSE_SCREEN = 3; // 关闭屏幕
	private static final byte SUB_OPEN_SCREEN = 4; // 打开屏幕
	/**
      *
      */

	private static final byte CCT_SHUTTER = 3;
	private static final byte SUB_SHUTTER_OPEN = 0; // 百叶窗开
	private static final byte SUB_SHUTTER_CLOSE = 1; // 百叶窗关

	private static final byte CCT_FADE_IN = 4; // 渐黑
	private static final byte CCT_FADE_OUT = 5; // 渐亮

	// 系统脚本'段'的定义
	private static final byte SV_INDEX_SEG_LEVEL = 0; // 关卡用到的量
	private static final byte SV_INDEX_SEG_GLOBAL = 29; // 整个游戏的量
	private static final byte SV_INDEX_SEG_OTHER = 60; // 其他的量

	// 系统变量索引定义
	private static final byte SV_INDEX_SCRIPT_HEROINE_FOLLOW = 0; // 女主角是否跟随
	private static final byte SV_INDEX_SCRIPT_SHOP = 8; // 商店
	private static final byte SV_INDEX_SCRIPT_FACE = 9; // faceID设置
	private static final byte SV_INDEX_SCRIPT_SHOW_BOSS_HP = 10; // 显示boss血槽
	private static final byte SV_INDEX_SCRIPT_KEY5_ATTACK = 11; // 主角5键是否可以攻击标志设置
	private static final byte SV_INDEX_SCRIPT_HEROINE_DIED = 12; // 判断女主角是否死亡
	private static final byte SV_INDEX_SCRIPT_CHANGE_HERO = 13; // 换主角
	public static final byte SV_INDEX_SCRIPT_CAN_CHANGE_HERO = 22; // 可以换主角

	private static final byte SV_INDEX_SCRIPT_SAVE = 15; // 游戏存档

	private static final byte SV_INDEX_SCRIPT_WEAPON = 25; // 武器索引
	private static final byte SV_INDEX_SCRIPT_GAME_END = 26; // 游戏通关

	private static final byte SV_INDEX_SCRIPT_UI_EQUIP = 33; // 装备界面
	private static final byte SV_INDEX_SCRIPT_UI_SKILL = 34; // 技能
	private static final byte SV_INDEX_SCRIPT_UI_MAKE = 35; // 打造

	private static final byte SV_INDEX_SCRIPT_BUY_LEVEL = 36; // 触发关卡计费
	public static final byte SV_INDEX_SCRIPT_HAVE_BUY_LEVEL = 37; // 关卡计费已经买过，一次性永久保存

	public static final byte SV_INDEX_SCRIPT_BOSS2_ATTACK = 39; // 教官是否可以攻击标志设置，0：不可，1：可以
	public static final byte SV_INDEX_SCRIPT_FOOTMAN_ATTACK = 40; // 人类士兵是否可以攻击标志设置，0：不可，1：可以

	public static short[] systemVariates = new short[COUNT_SYSTEM_VARIATE]; // 记录系统变量值
	public static short[] m_SystemVariates_Backup = new short[COUNT_SYSTEM_VARIATE]; // 记录系统变量值
	public static short[] systemTasks = new short[COUNT_SYSTEM_TASK]; // 记录系统任务完成度值
	public static short[] systemTasksActorIDs = new short[COUNT_SYSTEM_TASK]; // 记录系统任务所属levelID
																				// +
																				// actorID,(6bits
																				// +
																				// 10bits)

	//
	public final static byte SUPER_GLOBLE_VARIATE = 0; // 超全局
	public final static byte GLOBLE_VARIATE = 1; // 全局
	public final static byte LOCAL_VARIATE = 2; // 局部
	public final static byte OTHER_VARIATE = 3; // 局部

	// 数据
	public static byte[] svType = new byte[COUNT_SYSTEM_VARIATE]; // 系统变量类型
	public static byte[] smType = new byte[COUNT_SYSTEM_TASK]; // 任务的类型
	public static String[] strSM = new String[COUNT_SYSTEM_TASK << 1]; // 任务文本
																		// (名称,
																		// 描述)

	public static final int NONE_SCRIPT_ID = -1;

	/**
	 * 读取任务及系统变量数据
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
	 * 初始化对象的脚本信息
	 * 
	 * @param objScaner
	 *            XObject
	 */
	public static void initObjectScript(CObject obj) {
		// 更新当前对象的脚本
		short scriptID = (short) obj.m_scriptID;
		if (scriptID > NONE_SCRIPT_ID) {

			obj.currentConditions = scriptLevelConditions[scriptID];
			obj.currentConducts = scriptLevelConducts[scriptID];

		}
	}

	public static void scanScript(CObject objScaner) {
		for (int i = objScaner.scanStartSubScriptID; i < objScaner.currentConditions.length; i++) { // for已优化
			objScaner.curScriptConditions = objScaner.currentConditions[i];
			// 0.条件检测前提条件4个
			switch (objScaner.curScriptConditions[0]) {
			case CDN_PreCdn_NearMe: { // 靠近NPC: 无参数
				if (!heroIsNear(objScaner)) {
					continue;
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						// System.out.println("DEBUG >>　script precondition:主角靠近NPC");
					}
				}
				break;
			}
			case CDN_PreCdn_SayToMe: { // 玩家对此对象说话时
				if (!(CKey.isKeyPressed(CKey.GK_MIDDLE) && heroIsNear(objScaner))) {
					continue;
				} else {
					// CKey.initKey ();
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println("DEBUG >>　script precondition:玩家对此对象说话时");
					}
				}
				break;
			}
			case CDN_PreCdn_PlayerWin: { // 当玩家战斗获胜时
				// debug
				if (CDebug.showErrorInfo) {
					// if ( objScaner.property == null ||
					// objScaner.property.length < XObject.PRO_OBJ_HP )
					// {
					// System.out.println (
					// "WARNNING! scanScript() precondition: 当玩家战斗获胜时,actorID="
					// + objScaner.m_actorID + ", this actor no HP." );
					// }
				}
				if (!objScaner.testFlag(dActor.FLAG_DIE_TO_SCRIPT)) {
					continue;
				} else {
					objScaner.clearFlag(dActor.FLAG_DIE_TO_SCRIPT);
					// objScaner.die(false);
					if (CDebug.showDebugInfo) {
						System.out.println("DEBUG >>　script precondition:战斗胜利");
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
					// "DEBUG >>　script precondition:当玩家战斗获胜时, actorID=" +
					// objScaner.baseInfo[XObject.INFO_OBJ_ACTOR_ID] );
					// }
					// }
				}
				break;
			}
			case CDN_PreCdn_PlayerFail: { // 当玩家战斗失败时
				if (!CGame.m_hero.testFlag(dActor.FLAG_DIE_TO_SCRIPT)) {
					continue;
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println("script precondition:当玩家战斗失败时, actorID="
										+ objScaner.m_actorID);
					}
				}
				break;
			}
			case CDN_PreCdn_NoCdn: { // 无条件执行
				break;
			}

			case CDN_PreCdn_LeaveMe: // 离开对象
				if (heroIsNear(objScaner)) {
					continue;
				} else {
					// debug
					if (CDebug.showDebugInfo) {
						System.out
								.println("DEBUG >>　script precondition:主角离开NPC");
					}
				}

				break;

			}
			// 1.前提条件成立，进入后续条件检测
			objScaner.conditionIndex = 1;
			if (checkCondition(objScaner)) {
				// 条件成立
				objScaner.scanStartSubScriptID = (short) ((i + 1) % objScaner.currentConditions.length);
				objScaner.isInScriptRunning = true;
				// 通知系统进入脚本执行状态
				setScriptRun(objScaner, objScaner.currentConducts[i]);
				return; // 再次进入时从上次已经检测成功的位置（即scanStartScriptID）开始检测条件
			}
		}
		// 从头开始稍描
		objScaner.scanStartSubScriptID = 0;
	}

	/**
	 * 检测当前脚本条件 使用外部变量：curScriptCondition, conditionIndex
	 * 
	 * @return boolean
	 */
	private static boolean checkCondition(CObject objScaner) {
		boolean isOK = true;
		// 条件检测
		int cmd = 0; // 第几个条件
		int conditionID = 0; // 条件命令编号
		int conditionCount = objScaner.curScriptConditions[objScaner.conditionIndex++]; // 条件数量
		while (isOK && cmd < conditionCount) { // 如果还有子条件要检测并且前面所有的子条件&&结果是true,
												// 则继续
			cmd++;
			conditionID = objScaner.curScriptConditions[objScaner.conditionIndex++];
			// 找不符合条件的子条件，尽快地跳出while循环
			switch (conditionID) {
			case CDN_ConditionQuestItem: { // 任务完成度
				int sysTaskIndex = readConditionParam(objScaner, conditionID, 0);
				int operate = readConditionParam(objScaner, conditionID, 1);
				int value = readConditionParam(objScaner, conditionID, 2);

				isOK = compare(systemTasks[sysTaskIndex], operate, value);

				break;

			}

			case CDN_ConditionPropertyValueItem: { // 基本属性数据
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

			case CDN_ConditionGoodsValueItem: { // 物品条件
				int goodsIndex = readConditionParam(objScaner, conditionID, 0);
				int operate = readConditionParam(objScaner, conditionID, 1);
				int value = readConditionParam(objScaner, conditionID, 2);
				if (CDebug.showDebugInfo) {
					System.out.println("物品条件,还没有实现！");
				}

				// 获得物品数量
				int num = CGame.m_hero.getGoodsNum(goodsIndex);
				isOK = compare(num, operate, value); // 要根据物品的结构定义！！！
				break;
			}

			case CDN_ConditionSystemValueItem: { // 系统变量

				int sysVarIndex = readConditionParam(objScaner, conditionID, 0);
				int operate = readConditionParam(objScaner, conditionID, 1);
				int value = readConditionParam(objScaner, conditionID, 2);
				isOK = compare(systemVariates[sysVarIndex], operate, value);

				break;
			}

			case CDN_DialogItem: { // 对话中包含, 对策划来说：第一个选项为1，第二个选项为2

				int textIndex = readConditionParam(objScaner, conditionID, 0);
				textIndex = Integer.parseInt(dailogLevelText[textIndex].trim());

				// debug
				if (CDebug.showDebugInfo) {
					if (textIndex <= 0 || textIndex > 4) {
						System.out
								.println("WARNNING >> checkCondition(): 对话中包含 ,textIndex="
										+ textIndex + " maybe illegal!");
					}
				}
				isOK = (CGame.dlgCurLineIndex == textIndex);
				if (isOK) { // 有一个选项被选中就清空当前记录的选项编号
					CGame.dlgCurLineIndex = -1; // 这步操作可能还有问题！！！！
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
					if (cdt == 0) { // 切入
						isOK = (obj.m_actorProperty[IObject.PRO_INDEX_CUTINSTATE] == sIdx);
					} else { // 切出
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
								.println("DEBUG >>　script precondition 对象>>>"
										+ obj1.m_actorID + " 靠近 对象>>>"
										+ obj2.m_actorID);
					}
				}

				break;

			case CDN_ConditionOptionDlg: // 选择性对话
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
	 * 根据actorID获得对象
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

		// 如果该对象没有被激活则强制激活
		if (obj == null) {
			obj = CGame.tryActivateActor(actorID);
			System.out.println(">>对象不在激活区域需要强制激活：roleID=" + actorID);
		}
		if (CDebug.showDebugInfo) {
			if (obj == null) {
				System.out
						.println("runScript()->getObject(): 角色没有创建,请检测是否actived！actorID="
								+ actorID);
			}
		}
		return obj;
	}

	/**
	 * 判断两个人是否靠近
	 * 
	 * @param obj
	 *            XObject
	 * @param objOther
	 *            XObject
	 * @return boolean
	 */
	private static boolean heroIsNear(CObject obj) {
		CObject objOther = CGame.m_hero;
		// 对于系统脚本对象来说：使用其激活区域来判断靠近
		if (obj.testClasssFlag(dActor.CLASS_FLAG_SYSTEMOBJ)) {
			return obj.testActiveBoxCollideObject(objOther);
		} else {
			return Math.abs(obj.m_x - objOther.m_x)
					+ Math.abs(obj.m_y - objOther.m_y) < ((MapData.TILE_WIDTH * 3) << dConfig.FRACTION_BITS);
		}

	}

	/**
	 * 按键设置的
	 * 
	 * @param key
	 *            int
	 * @return boolean
	 */
	private static boolean getPressedKey(int key) {
		switch (key) {
		case 0: // 5键
			return CKey.isKeyPressed(CKey.GK_MIDDLE);
		case 1: // 向左按键
			return CKey.isKeyPressed(CKey.GK_LEFT);
		case 2: // 向右按键
			return CKey.isKeyPressed(CKey.GK_RIGHT);
		case 3: // 向上按键
			return CKey.isKeyPressed(CKey.GK_UP);
		case 4: // 向下按键
			return CKey.isKeyPressed(CKey.GK_DOWN);
		case 99:
			return true;
		}
		return false;
	}

	/**
	 * 从conditionIndex开始读取length个字节长度数据并返回
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
		objScaner.conditionIndex += length; // 越过已读取的字节数
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
	 * 比较2个数大小
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
	 * 执行代码段
	 *******************************/
	public static int stateBeforeScript; // 脚本前的游戏状态
	public static CObject objScriptRun; // 脚本的承载对象
	public static byte[] curScriptConduct; // 当前要执行的内容

	/**
	 * 脚本执行参数
	 */
	private static int conduct; // 当前的执行单元
	private static int conductIndex; // 指向curScriptConduct里的一个字节
	private static int conductCount; // 执行单元数量

	/**
	 * 对象obj脚本条件检测成功，告诉系统执行其脚本scriptConduct
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
		conduct = 0; // 当前的执行单元
		conductIndex = 0; // 指向curScriptConduct里的一个字节
		conductCount = curScriptConduct[conductIndex++]; // 执行单元数量
		Script.scriptLock = true;
		CGame.setGameState(dGame.GST_SCRIPT_RUN);
	}

	/**
	 * 从conductIndex开始读取length个字节长度数据并返回
	 * 
	 * @param length
	 *            int
	 * @return int
	 */
	private static int readConductParam(int conductID, int paramIndex) {
		int length = SCRIPT_CONDUCT_TABLE[conductID][paramIndex];
		int param = CTools.readFromByteArray(curScriptConduct, conductIndex,
				length);
		conductIndex += length; // 越过已读取的字节数
		return param;
	}

	private static Vector vObjShowAction = new Vector(); // 脚本指令：改变角色动作

	public static int autoMoveActorCounter; // 自动移动对象的数量

	public static int autoshowActionCouner; // 自动改变动作状态

	public static boolean isActorAutoMove() {
		return autoMoveActorCounter == 0 ? false : true;
	}

	public static boolean isActorAutoAction() {
		return autoshowActionCouner == 0 ? false : true;
	}

	// /**
	// * 脚本执行
	// * @return boolean
	// */
	private static boolean runScript() {
		int conductID = -1; // 执行命令编号
		while (conduct < conductCount) {
			conduct++;
			conductID = curScriptConduct[conductIndex++];
			switch (conductID) {
			case CDT_SetRoleStateValueItem: { // 修改角色状态:
												// roleId(0表示对主角，1表示对脚本对象本身)
												// roleStateName roleStateValue
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():修改角色状态 conductID="
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

			case CDT_SetMoveRoleItem: { // 移动角色: actorID mapCoord(X,Y) dir
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():移动角色 conductID="
							+ conductID);
				}

				int actorID = readConductParam(conductID, 0); //
				int type = readConductParam(conductID, 1);
				int mapCoordX = readConductParam(conductID, 2); // 地图格x坐标
				int mapCoordY = readConductParam(conductID, 3); // 地图格y坐标
				int dis = readConductParam(conductID, 4); // 移动距离
				int dir = readConductParam(conductID, 5); // 朝向
				int actionID = readConductParam(conductID, 6) - 1; // 动作ID
				// 自动移动对象的数量
				autoMoveActorCounter++;

				CObject obj = getObject(actorID);
				if (obj == null) {
					if (CDebug.showDebugInfo) {
						System.out
								.println("DEBUG >>　runScript():移动角色 obj= null");
					}
					return false;
				}
				// 如果没有初始化 进行一次初始化
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
				// 设置动作
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

				// 如果有连续的两条执行人物动作指令，那么就让他们一起执行
				if (conduct < conductCount
						&& curScriptConduct[conductIndex] == CDT_SetMoveRoleItem) {
					break;
				} else {
					return false; // 有状态切换的时候，就要return
									// false告诉调用者脚本转入下一个状态，还没有结束脚本的运行
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

			case CDT_SetMoveCommandItem: { // 传送指令: mapIndex mapCoord(X,Y)
											// roleInitState
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():传送指令 conductID="
							+ conductID);
				}

				int newLevel = readConductParam(conductID, 0);
				int x = readConductParam(conductID, 1);
				int y = readConductParam(conductID, 2);
				/**
				 * 、 dir == 0 不翻转 dir ==时翻转
				 */
				int dir = (short) readConductParam(conductID, 3);

				x = (short) (x * MapData.TILE_WIDTH + (MapData.TILE_WIDTH >> 1));
				y = (short) (y * MapData.TILE_HEIGHT + (MapData.TILE_HEIGHT >> 1));
				CHero hero = CGame.m_hero;
				CKey.initKey();
				if (newLevel == CGame.curLevelID) {
					// 设置出现的位置
					hero.setXY(x, y);
					// 设置出现的方向
					dir = hero.getSetDir(dir);
					hero.setFaceDir(dir);
					// 更新缓冲
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
					return false; // 有状态切换的时候，就要return
									// false告诉调用者脚本转入下一个状态，还没有结束脚本的运行
				}
			}

			case CDT_SetAddRoleItem: { // 增加角色: actorID mapCoord(X,Y)
										// roleInitState
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():增加角色 conductID="
							+ conductID);
				}
				int actorID = readConductParam(conductID, 0);
				int x = readConductParam(conductID, 1);
				int y = readConductParam(conductID, 2);
				int dir = readConductParam(conductID, 3);

				// trailer 要分为主角触发和直接触发
				CObject obj = CGame.tryActivateActor(actorID);
				if (ResLoader.classAIIDs[obj.m_classID] == dActorClass.CLASS_ID_TRAILERCAMERA) {
					obj.clearFlag(dActor.FLAG_BASLIC_NOT_LOADABLE);
					obj.do_trailer();
					CGame.setGameState(dGame.GST_TRAILER_RUN);
					return false;
				}

				// 增加角色
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
					// 增加角色
					obj.setFlag(dActor.FLAG_BASIC_VISIBLE);
					obj.clearFlag(dActor.FLAG_BASLIC_NOT_LOADABLE);
					obj.setActorInfo(dActor.INDEX_BASIC_FLAGS, obj.m_flags);

					obj.setXY((short) x, (short) y);
					dir = obj.getSetDir(dir);
					obj.setFaceDir(dir);
					// 设置出生点位置
					obj.setXY(x, y);

					if (obj.testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV)) {
						obj.droptoGround();
					}
				}

				break;
			}

			case CDT_SetDeleteRoleItem: { // 删除角色: actorID
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():删除角色 conductID="
							+ conductID);
				}
				int actorID = readConductParam(conductID, 0);
				CObject obj = getObject(actorID);
				if (obj != null) {
					obj.die(false);
				}
				break;
			}

			case CDT_SetRoleActorItem: { // 修改角色动作: actorID actionID
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():修改角色动作 conductID="
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
								.println("DEBUG >>　runScript():移动角色 obj= null");
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
				// 如果没有初始化 进行一次初始化
				// if(obj.testFlag (dActor.FLAG_NEED_INIT)) {
				// obj.clearFlag (dActor.FLAG_NEED_INIT);
				// obj.update ();
				// }

				obj.isAutoAction = true;

				// //设置动作
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
					return false; // 有状态切换的时候，就要return
									// false告诉调用者脚本转入下一个状态，还没有结束脚本的运行
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
				// //如果有连续的两条执行人物动作指令，那么就让他们一起执行
				// if(conduct < conductCount && curScriptConduct[conductIndex]
				// == CDT_SetRoleActorItem){
				// break;
				// } else{
				// return false; // 有状态切换的时候，就要return
				// false告诉调用者脚本转入下一个状态，还没有结束脚本的运行
				// }
				// }
				// break;
			}

			case CDT_SetRolePropertyValueItem: { // 角色属性:
													// roleId(0表示对主角，1表示对脚本对象本身)
													// propertyIndex
													// propertyValue
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():角色属性 conductID="
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
						// 增加经验
						if (pIdx == IObject.PRO_INDEX_EXP) {
							obj.addExp(value);
						} else {
							obj.m_actorProperty[pIdx] += value;
						}

						// obj.addBonusShow(0 , "属性" + pIdx + "增加：" + value ,
						// CObject.NORMAL_ATT);
						break;
					case 1:
						obj.m_actorProperty[pIdx] = (short) value;
						// obj.addBonusShow(0 , "属性" + pIdx + "被置为：" + value ,
						// CObject.NORMAL_ATT);
						break;
					}
					// 血量检测
					if (pIdx == IObject.PRO_INDEX_HP) {
						// if ( obj.m_actorProperty[CObject.PRO_INDEX_HP] <= 0 )
						// {
						// obj.setState ( CObject.ST_OBJ_DIE );
						// }
					}
					// 数据修正
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
				// 增加物品: roleId(0表示对主角，1表示对脚本对象本身) goodsId goodsValue(负数表示减)
			case CDT_SetAddGoodsItem: {
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():增加物品 conductID="
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

			case CDT_SetAddEquipsItem: { // 增加装备: roleId(0表示对主角，1表示对脚本对象本身)
											// equipsId equipsValue(负数表示减)
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():增加装备 conductID="
							+ conductID);
				}
				int actorID = readConductParam(conductID, 0);
				int goodsID = readConductParam(conductID, 1);
				int num = readConductParam(conductID, 2);

				if (num < 0) {
					// CGame.m_hero.dropAEquip((0<<12)|goodsID,-num);
					System.out.println("增加装备,暂时没做，equipID = " + goodsID);
				} else if (num > 0) {
					for (int i = 0; i < num; i++) {
						Goods equip = Goods.createGoods((short) 0,
								(short) goodsID);
						CGame.m_hero.addAEquip(equip);
					}
				}

				return false;
			}

			case CDT_SetAddSkillsItem: { // 学到技能: roleId(0表示对主角，1表示对脚本对象本身)
											// skillsId skillsValue
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():学到技能 conductID="
							+ conductID);
				}
				int actorID = readConductParam(conductID, 0);
				int skillID = readConductParam(conductID, 1);
				int skillID1 = readConductParam(conductID, 2);
				// ...
				return false;
			}

			case CDT_SetAddCommonTalkItem: { // 普通对话: dialogType(普通对话:0 强制对话:1
												// 自定义菜单:2 自定义:3) textID
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():对话 conductID="
							+ conductID);
				}

				CGame.dlgType = (short) readConductParam(conductID, 0);
				CGame.dlgActorID = (short) readConductParam(conductID, 1);
				CGame.dlgShowPos = (short) readConductParam(conductID, 2);
				CGame.dlgHeadActionID = (short) (readConductParam(conductID, 3) - 1);
				CGame.dlgTextID = (short) readConductParam(conductID, 4);
				CHero hero = CGame.m_hero;
				/**
				 * 转换到脚本对话运行状态
				 * 
				 * @param <any> GAME_STATE_SCRIPT_DIALOG
				 */
				hero.droptoGround();
				// 如果对话状态非自定义 则装换为站立
				if (CGame.dlgType != CGame.DLG_TYPE_DEFINE) {
					hero.setState(IObject.ST_ACTOR_WAIT, -1, true);
				}

				CKey.initKey();
				// hero.updateCamera();
				// hero.update();
				// Camera.updateCamera(false);
				// CGame.gMap.updateMap(Camera.cameraLeft,Camera.cameraTop,true);

				CGame.setGameState(dGame.GST_SCRIPT_DIALOG);

				return false; // 有脚本状态切换的时候，就要return
								// false告诉调用者脚本转入下一个状态，还没有结束脚本的运行
			}
			case CDT_SetAddMapObjectItem: { // 创建地物: mapCoord(X,Y) mapLayerId
											// mapObjectId
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():创建地物 conductID="
							+ conductID);
				}

				int gx = readConductParam(conductID, 0);
				int gy = readConductParam(conductID, 1);
				int mapLayerID = readConductParam(conductID, 2);
				// 工具数据层索引错误
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
			case CDT_SetDeleteMapObjectItem: { // 删除地物: mapCoord(X,Y) mapLayerId
			// //debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():删除地物 conductID="
							+ conductID);
				}

				int gx = readConductParam(conductID, 0);
				int gy = readConductParam(conductID, 1);
				int mapLayerID = readConductParam(conductID, 2);
				byte none = (byte) 0;
				// 工具数据层索引错误
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
			case CDT_SetObjectEnalbeItem: { // 改变角色有效性: npcItem roleEnalbe
				// debug
				if (CDebug.showDebugInfo) {
					System.out
							.println("DEBUG >>　runScript():改变角色有效性 conductID="
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
			case CDT_SetSystemStateItem: { // 改变系统状态: systemStateId
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():改变系统状态 conductID="
							+ conductID);
				}
				byte sysStateIndex = (byte) readConductParam(conductID, 0);
				stateBeforeScript = sysStateIndex; // 更改stateBeforeScript
				// 更改
				CGame.setGameState(sysStateIndex);
				if (sysStateIndex == dGame.GST_GAME_RUN) {
					return false;
				}
				return true; // 切换系统状态后,就直接结束脚本. 如果后面还有脚本,那么就忽略!
			}

			case CDT_SetQuestItem: { // 任务状态值: questIndex questFinishValue
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():任务状态值 conductID="
							+ conductID);
				}

				int sysTasksIndex = readConductParam(conductID, 0);
				int op = readConductParam(conductID, 1);
				int newValue = readConductParam(conductID, 2);
				setTaskInfo(sysTasksIndex, (byte) newValue, op == 0);
				break;
			}
			case CDT_SetConditionSystemValueItem: { // 系统变量: propertyIndex
													// propertyFinishValue
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():系统变量 conductID="
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
				// 换主角
				case SV_INDEX_SCRIPT_CHANGE_HERO:
					CGame.m_hero.switchHero(newValue, false);
					break;
				// 装备界面
				case SV_INDEX_SCRIPT_UI_EQUIP:

					CGame.setGameState(dGame.GST_GAME_UI);
					GameUI.setCurrent(GameUI.Form_equip);

					return false;
					// 打造界面
				case SV_INDEX_SCRIPT_UI_MAKE:

					CGame.setGameState(CGame.GST_GAME_UI);
					GameUI.setCurrent(GameUI.Form_make);

					return false;
					// 技能界面
				case SV_INDEX_SCRIPT_UI_SKILL:

					CGame.setGameState(CGame.GST_GAME_UI);
					GameUI.setCurrent(GameUI.Form_SKILL);

					return false;
					// 关卡计费
				case SV_INDEX_SCRIPT_BUY_LEVEL:

					return false;

					// 游戏存档
				case SV_INDEX_SCRIPT_SAVE:
					// 先保存全局对象 add by lin 09.4.29
					CGame.addActorInfo2hs(CGame.curLevelID);
					game.rms.Record.saveToRMS(game.rms.Record.DB_NAME_GAME, 1);
					if (CGame.m_hero != null) {
						// CGame.m_hero.addBonusShow(0,"游戏已经保存",(byte)1);
					}
					break;
				// 游戏通关
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
					// 累计
					if (op == 0) {
						systemVariates[sysVarIndex] += newValue;
					} else {
						systemVariates[sysVarIndex] = (short) newValue;
					}
					break;
				}
				break;
			}
			case CDT_SetPlayMusicItem: { // 播放音乐: musicId
				int musicID = readConductParam(conductID, 0);
				SoundPlayer.playSingleSound(musicID, -1);
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():播放音乐 conductID="
							+ conductID);
				}
				break;
			}
				// case CDT_SetPlayAviItem: //播放特效: aviId
				//
				// //debug
				// if ( CDebug.showDebugInfo )
				// {
				// System.out.println ( "DEBUG >>　runScript():播放特效 conductID=" +
				// conductID );
				// }
				// System.out.println ( "WARNING >> \tNo implement!" );
				// break;
			case CDT_SetStopMusicItem: // 停止音乐: musicId
				int musicID = readConductParam(conductID, 0);
				SoundPlayer.stopSingleSound();
				// debug
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>　runScript():停止音乐 conductID="
							+ conductID);
				}
				System.out.println("WARNING >> \tNo implement!");
				break;
			//
			// //新增指令
			case CDT_SetSign: { // 20
				int roleID = readConductParam(conductID, 0);
				// modify by lin 2009.4.27 表情id要减1
				int faceID = readConductParam(conductID, 1) - 1;
				System.out.println(">>设置标志：roleID=" + roleID + ", faceID="
						+ faceID);
				CObject aa = getObject(roleID);
				if (aa != null) {
					aa.setFaceInfo(faceID);
				}
				break;
			}
			case CDT_SetReborn: { // 21
				int roleID = readConductParam(conductID, 0);
				int count = readConductParam(conductID, 1); // 重生的次数
				int span = readConductParam(conductID, 2); // 重生的时间间隔
				System.out.println(">>角色重生：roleID=" + roleID + ", count="
						+ count + ", span=" + span);
				CObject actor = getObject(roleID);
				if (actor != null) {
					actor.initialize();
				}

				break;
			}
			case CDT_SetCameraCtrl: { // 22
				int type = readConductParam(conductID, 0); // 系统变量的类型
				int value = readConductParam(conductID, 1);
				System.out.println(">>屏幕控制：type=" + type + ", value=" + value);
				switch (type) {
				case CCT_SHAKE_SCREEN_SWING: // （像素）
					GameEffect.setSysShakeScreen(value, 0);
					break;
				case CCT_SHAKE_SCREEN_TIME: // （ms）
					GameEffect.setSysShakeScreen(0, value);
					break;

				case CCT_CONTROL_SCREEN: // 所有屏幕的操作
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

				case CCT_SHUTTER: // 百叶窗效果
					switch (value) {
					case SUB_SHUTTER_OPEN:
						GameEffect.setShutter(GameEffect.SHUTTER_OPEN);
						break;

					case SUB_SHUTTER_CLOSE:
						GameEffect.setShutter(GameEffect.SHUTTER_CLOSE);
						break;
					}
					break;

				case CCT_FADE_IN: // （ms）
					GameEffect.setScreenFade(GameEffect.TYPE_FADE_IN, 0, value);
					break;

				case CCT_FADE_OUT: // （ms）
					GameEffect
							.setScreenFade(GameEffect.TYPE_FADE_OUT, 0, value);
					break;
				}
				break;
			}
				//
			case CDT_SetMoveCameraToPt: { // 将camera移动指定地点, 23
				CHero hero = CGame.m_hero;
				int x = readConductParam(conductID, 0);
				int y = readConductParam(conductID, 1);
				int sp = readConductParam(conductID, 2);
				System.out.println(">>屏幕移至某点：x=" + x + ", y=" + y + ", sp="
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
				System.out.println(">>跟随对象：roleID=" + roleID);

				CObject obj = getObject(roleID);
				// 缺少设置对象相对屏幕位置的参数
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
				System.out.println(">>镜头移至角色：roleID=" + roleID + ", sp=" + sp);
				CObject obj = getObject(roleID);
				CGame.vsDstX = obj.m_x;
				CGame.vsDstY = obj.m_y;
				CGame.setGameState(CGame.GST_CAMARE_MOVE);
				return false;
			}
				//
			case CDT_SetScriptRunPause: { // 26

				int pauseTime = readConductParam(conductID, 0);
				System.out.println(">>执行延时：pauseTime=" + pauseTime);
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
			case CDT_SetDelMutiRole: // 删除多角色
				int delCount = readConductParam(conductID, 0);
				while (delCount > 0) {
					delCount--;
					int actorID = CTools.readFromByteArray(curScriptConduct,
							conductIndex, 2);
					if (CDebug.showDebugInfo) {
						System.out.println("DEBUG >>　DelRoleID" + actorID);
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
						System.out.println("DEBUG >>　addRoleID" + actorID);
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
						// 增加角色
						obj.setFlag(dActor.FLAG_BASIC_VISIBLE);
						obj.clearFlag(dActor.FLAG_BASLIC_NOT_LOADABLE);
						obj.setActorInfo(dActor.INDEX_BASIC_FLAGS, obj.m_flags);

						obj.setXY((short) x, (short) y);
						dir = obj.getSetDir(dir);
						obj.setFaceDir(dir);
						// 设置出生点位置
						obj.setXY(x, y);

						if (obj.testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV)) {
							obj.droptoGround();
						}
					}
				}
				break;
			//
			// case CDT_SetMoveMutiRole: //多角色移动
			// int moveCount = readConductParam(conductID , 0); //移动敌人数量
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
			// case CDT_SetShowCG: //游戏中展示
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
			// //........设置对象.........
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
				// 进入商店
			case CDT_SetShopItem:
				int shopId = (byte) readConductParam(conductID, 0);
				GameUI.setShopId((byte) shopId);
				CGame.setGameState(CGame.GST_GAME_UI);
				GameUI.setCurrent(GameUI.Form_shop);
				return false;
			default:
				if (CDebug.showDebugInfo) {
					System.out
							.println("DEBUG >>　runScript(): no match the conduct!");
				}
				break;
			}
		}
		return true;
	}

	/******************************
	 * 处理脚本logic
	 ******************************/
	// rumTask()的停止时间
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
	 * 任务
	 */

	/**
	 * 任务完成情况
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
	 * 设置任务的完成信息 并作为任务完成情况的监听器
	 * 
	 * @param sysTasksIndex
	 *            int
	 * @param newValue
	 *            byte
	 */
	public static void setTaskInfo(int sysTasksIndex, byte newValue,
			boolean accumulate) {
		// 指定任务第一次被接受还未完成时，就记录任务绑定的对象
		if (TASK_VALUE_RECEIVED_NOT_ACCOMPLISHED == newValue) {
			systemTasksActorIDs[sysTasksIndex] = (short) ((CGame.curLevelID << 10) | objScriptRun.m_actorID);
		}
		if (accumulate) {
			systemTasks[sysTasksIndex] += newValue;
		} else {
			systemTasks[sysTasksIndex] = newValue;
		}
		// 检测任务的状态变化，如果任务提交则绑定对象任务链表就移出次任务
		switch (newValue) {
		case TASK_VALUE_NOT_RECEIVED:
			break;
		case TASK_VALUE_RECEIVED_NOT_ACCOMPLISHED:
			// m_hero.addSubHPShow ( 0 , "接到:" );
			break;
		case TASK_VALUE_ACCOMPLISHED_NOT_SUBMITED:
			// m_hero.addSubHPShow ( 0 , "完成:" );
			break;
		case TASK_VALUE_SUBMITED:
			// m_hero.addSubHPShow ( 0 , "提交:" );
			break;
		}
	}

	/**
	 * 获取接过、未提交的任务
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
