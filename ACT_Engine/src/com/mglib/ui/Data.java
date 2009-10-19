package com.mglib.ui;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * <p>
 * Title: engine of RPG game ��Ϸ���ݱ༭��
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: mig
 * </p>
 * 
 * @author chen lianghao
 * @version 1.0.0
 * @version 1.0.1 1. 2008.09.02 �غ���ģ�����ӵĵ��˼�����Ϣ 2. 2008.09.18
 *          �غ���ģ���������Ǽ�����Ϣ�����ܺ���Ʒ�ļ�Ҫ������Ϣ 3. 2008.12.10
 *          �̵������ۿ��ʺͱ����ֶ��Լ���Ʒ����ʾ������Ʒװ������ʾ������Ʒ�ģ�ʹ�ã�����
 */
public final class Data {
	/***************************************************************************
	 * ���ݱ༭���еĳ������ݶ���
	 ***************************************************************************/
	// ����/�з�/��/Ⱥ: �����ݱ༭����Ķ�Ӧ
	public static final int FS_NONE = -1; // ��
	public static final int FS_TO_self_1 = 0; // ��1��
	public static final int FS_TO_self_n = 1; // ��n��
	public static final int FS_TO_1 = 2; // ��1��
	public static final int FS_TO_n = 3; // ��n��
	public static final int FS_TO_self = 4; // �Լ�
	public static final int FS_TO_self_1_hp0 = 5; // ��hp=0 1��
	public static final int FS_TO_self_n_hp0 = 6; // ��hp=0 n��

	// 1.dataParam
	// a. equip part indices
	public static final byte IDX_EP_weapon = 0;
	public static final byte IDX_EP_loricae = 1;
	public static final byte IDX_EP_shoes = 2;
	public static final byte IDX_EP_ring = 3;
	public static final byte IDX_EP_jade = 4;

	// b. role/affected property indices
	public static final byte IDX_PRO_hp = 0;
	public static final byte IDX_PRO_maxHp = 1;
	public static final byte IDX_PRO_mp = 2;
	public static final byte IDX_PRO_maxMp = 3;
	public static final byte IDX_PRO_atk = 4;
	public static final byte IDX_PRO_def = 5;

	public static final byte IDX_PRO_cri_rate = 6; // ������
	public static final byte IDX_PRO_zhuiHun_rate = 7; // ׷����
	public static final byte IDX_PRO_hunMi_rate = 8; // ������
	public static final byte IDX_PRO_mage_def = 9;
	public static final byte IDX_PRO_hit_rate = 10; // ����
	public static final byte IDX_PRO_next_exp = 11; // ��������

	// ����
	public static int COUNT_FIVE_PROPERTY;
	public static int COUNT_ROLE_PROPERTY;
	public static int COUNT_STATE;

	// base data
	public static int ANIMATION_ID; // ����ID
	public static String[] STR_ROLE_PROPERTY_NAMES; // ����������
	public static String[] STR_EQUIP_PART_NAMES; // װ����������
	public static String[] STR_FIVE_PROPERTY_NAMES; // ����������

	// 2.animation

	// 3.states
	// state info indices
	public static final byte IDX_STI_actionID = 0;
	public static final byte IDX_STI_value0 = 1;
	public static final byte IDX_STI_value1 = 2;
	public static final byte IDX_STI_time = 3;

	// state data
	public static String[] STR_STATE_NAMES;
	public static short[][] STATE_INFO; // [״̬][����0,...]

	// 4.skills
	public static final byte SKILL_TYPE_INITIATIVE = 0;
	public static final byte SKILL_TYPE_INITIATIVE_ASSISTANT = 1;
	public static final byte SKILL_TYPE_PASSIVE = 2;

	// skill info indices
	public static final byte IDX_SKI_icon = 0;
	public static final byte IDX_SKI_type = 1;
	public static final byte IDX_SKI_upLvDest = 2;
	public static final byte IDX_SKI_target = 3;

	// public static final byte IDX_SKI_condition = 4;
	public static final byte IDX_SKI_cg = 4;
	public static final byte IDX_SKI_cgDest = 5;

	// public static final byte IDX_SKI_music = 7;
	public static final byte IDX_SKI_price = 6; // ˮ��У���Ӧ������������ļ��ܵ���

	// public static final byte IDX_SKI_isNearShow = 9;
	public static final byte IDX_SKI_keepTime = 7;
	public static final byte IDX_SKI_coolTime = 8;
	public static final byte IDX_SKI_consumeMP = 9;
	public static final byte IDX_SKI_stateRate = 10;
	public static final byte IDX_SKI_upExp = 11;

	// skill data
	public static String[][] STR_SKILL_NAMES; // [skillID][name0��desc0,
												// desc_short0]
	public static short[][] SKILL_INFO; // [��������ID][����0,...]
	public static short[][] SKILL_FIVE_PROPERTY; // [��������ID][��������0, ...]
	public static short[][] SKILL_AFFECTED_STATE; // [��������ID][Ӱ��״̬DID0,value0,...]
	public static short[][] SKILL_AFFECTED_PROPERTY; // [��������ID][Ӱ������0,...]

	// skill group
	public static short[][] SKILL_GROUP; // [����ID][��������ID]

	// 5.equips
	// equip info indices
	public static final byte IDX_EI_icon = 0;
	public static final byte IDX_EI_price = 1;
	public static final byte IDX_EI_level = 2;
	public static final byte IDX_EI_part = 3;
	public static final byte IDX_EI_newEquipID = 4; // 999 ��ʾû����װ��Ϣ
	public static final byte IDX_EI_adtRate = 5;
	public static final byte IDX_EI_adtID = 6;
	public static final byte IDX_EI_antiID = 7;
	public static final byte IDX_EI_showlevel = 8; // 2008.12.10

	// equip data
	public static String[][] STR_EQUIP_NAMES;
	public static short[][] EQUIP_INFO; // [װ��][����0,...]
	public static short[][] EQUIP_FIVE_PROPERTY; // [װ��][��������0, ...]
	public static short[][] EQUIP_AFFECTED_PROPERTY; // [װ��][Ӱ������0,...]

	// 6.goods
	// goods info indices
	public static final byte TYPE_GI_COMMON_ITEM = 0; // ͨ����Ʒ��ƽʱս���Կ��ã�
														// //2008.12.10
	public static final byte TYPE_GI_QUEST_ITEM = 1; // ������Ʒ
	public static final byte TYPE_GI_FIGHT_ITEM = 2; // ս����Ʒ��ս���ã�
	public static final byte TYPE_GI_NORMAL_ITEM = 3; // ��ͨ��Ʒ��ƽʱ�ã�
	public static final byte TYPE_GI_OTHER_ITEM = 4; // ����

	// goods info indices
	// goods info indices
	public static final byte IDX_GI_icon = 0;
	public static final byte IDX_GI_price = 1;
	public static final byte IDX_GI_target = 2;
	public static final byte IDX_GI_type = 3;
	public static final byte IDX_GI_cg = 4;
	public static final byte IDX_GI_cgDest = 5;
	public static final byte IDX_GI_time = 6;
	public static final byte IDX_GI_showlevel = 7; // 2008.12.10

	// goods data
	public static String[][] STR_GOODS_NAMES; // [��Ʒ][name0, desc0, desc_short0]
	public static short[][] GOODS_INFO; // [��Ʒ][����0,...]
	public static short[][] GOODS_AFFECTED_PROPERTY; // [��Ʒ][Ӱ������0,...]
	public static short[][] GOODS_AFFECTED_STATE; // [��Ʒ][Ӱ��״̬DID0,value0,...]

	// 7.class
	// class data
	public static String[][] STR_CLASS_NAMES; // [ְҵ][name, desc]
	public static short[][] CLASS_EQUIP_USED; // [ְҵ][װ��0,....]
	public static short[][] CLASS_SKILL; // [ְҵ][����ID,����level]

	// 8.roles
	// roles info indices
	public static final byte IDX_RI_classID = 0; // ְҵ
	public static final byte IDX_RI_level = 1;
	public static final byte IDX_RI_maxLevel = 2;
	// 2008.09.02 �غ���ģ�� ���������6������
	public static final byte IDX_RI_SKILL0 = 3;
	public static final byte IDX_RI_SKILL0_INIT_LEVEL = 4;
	public static final byte IDX_RI_SKILL1 = 5;
	public static final byte IDX_RI_SKILL1_INIT_LEVEL = 6;
	public static final byte IDX_RI_SKILL2 = 7;
	public static final byte IDX_RI_SKILL2_INIT_LEVEL = 8;
	public static final byte IDX_RI_SKILL3 = 9;
	public static final byte IDX_RI_SKILL3_INIT_LEVEL = 10;
	public static final byte IDX_RI_SKILL4 = 11;
	public static final byte IDX_RI_SKILL4_INIT_LEVEL = 12;
	public static final byte IDX_RI_SKILL5 = 13;
	public static final byte IDX_RI_SKILL5_INIT_LEVEL = 14;

	// roles data
	public static String[][] STR_ROLE_NAMES; // [��ɫ][name, desc]
	public static short[][] ROLE_INFO; // [��ɫ][����0,����1,]
	public static short[][][] ROLE_FORMULA_PARAM; // [��ɫ][������������][a,b,c,d]
	public static short[][] ROLE_INIT_EQUIP; // [��ɫ][��ʼװ��0,...]
	public static short[][] ROLE_INIT_GOODS; // [��ɫ][��ʼ������][��ƷID0,����,...]
	public static short[][] ROLE_FIVE_PROPERTY; // [��ɫ][��������0, ...]

	// 9.dealers
	public static final byte IDX_DI_discount = 0;
	public static final byte IDX_DI_reserved = 1;
	// dealer data
	public static String[][] STR_DEALER_NAMES; // [����][name, desc]
	public static short[][] DEALER_INFO;
	public static short[][] DEALER_SKILL; // [����][����ID0, ...]
	public static short[][] DEALER_EQUIP; // [����][װ��ID0, ...]
	public static short[][] DEALER_GOODS; // [����][��ƷID0, ...]

	// 10.mobs
	// mob info indices
	public static final byte IDX_MI_level = 0;
	public static final byte IDX_MI_type = 1;
	public static final byte IDX_MI_money = 2;
	public static final byte IDX_MI_adtRate = 3;
	public static final byte IDX_MI_adtID = 4;
	public static final byte IDX_MI_antiRate = 5;
	public static final byte IDX_MI_antiID = 6;
	public static final byte IDX_MI_dropRate = 7;
	public static final byte IDX_MI_dropEquipID = 8;
	public static final byte IDX_MI_dropItemID = 9;
	// 2008.09.02 �غ���ģ��
	public static final byte IDX_MI_SKILL0 = 10;
	public static final byte IDX_MI_SKILL0_RATE = 11;
	public static final byte IDX_MI_SKILL1 = 12;
	public static final byte IDX_MI_SKILL1_RATE = 13;
	public static final byte IDX_MI_SKILL2 = 14;
	public static final byte IDX_MI_SKILL2_RATE = 15;

	// mob data
	public static String[][] STR_MOB_NAMES; // [����][name, desc]
	public static short[][] MOB_INFO; // [����][level, exp, ...]
	public static short[][] MOB_FIVE_PROPERTY; // [����][��������ID0, ...]
	public static short[][] MOB_AFFECTED_PROPERTY; // [����][Ӱ������0, ...]

	// //11.troops
	// //troop data
	// public static String[][] STR_TROOP_NAMES; //[����][name, desc]
	// public static short[][][] TROOP_FIGHT; //[����][ս����][x,y],
	// (x,y)Ϊ176*208��Ļ�е�λ��

	/***************************************
	 * ��װ�Ĳ��ַ���
	 **************************************/
	/**
	 * ��װ��Ϣ��ת�� 3��ʮ����λ��ʾ3����Ϣ[mlgID, iIndex, pIndex]
	 * 
	 * @param newEquipID
	 *            int�� =999��ʾû����Ч��Ϣ
	 * @return short[]
	 */
	public static short[] getSuit(int newEquipID) {
		short[] suit = new short[3];
		suit[0] = (short) (newEquipID / 100);
		suit[1] = (short) ((newEquipID % 100) / 10);
		suit[2] = (short) (newEquipID % 10);
		// ����ǲ������
		if (suit[0] == 9 && suit[1] == 9 && suit[2] == 9) {
			suit[0] = -1;
		}
		return suit;
	}

	/***************************************
	 * ������Դ�Ķ�ȡ
	 **************************************/
	private static final String STR_FN_GAME_DATA = "/bin/gameData.bin";

	/**
	 * ������Ϸ������Դ
	 */
	public static void loadGameData() {
		long t = Runtime.getRuntime().freeMemory();
		DataInputStream dis = null;
		try {
			dis = new DataInputStream("".getClass().getResourceAsStream(
					STR_FN_GAME_DATA));
			// dis = CGame.getFileStream ( Data.STR_FN_GAME_DATA );
			int fileCount = dis.readByte();
			int[] offset = new int[fileCount + 1];
			for (int i = 0; i < offset.length; i++) {
				offset[i] = dis.readInt();
			}
			loadDataParam(dis);
			loadSkills(dis);
			loadEquips(dis);
			loadGoods(dis);
			loadClasses(dis);
			loadRoles(dis);
			loadDealers(dis);
			loadMobs(dis);

			dis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			dis = null;
		}
		System.out.println("GameData Mem :"
				+ (t - Runtime.getRuntime().freeMemory()) / 1024 + "k");
	}

	/**
	 * ��ȡӰ��������
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 * @return short[]
	 */
	private static short[] readAffectedProperty(DataInputStream dis)
			throws IOException {
		int cnt = dis.readByte();
		short[] affectedProperty = new short[cnt];
		for (int i = 0; i < cnt; i++) {
			affectedProperty[i] = dis.readShort();
		}
		return affectedProperty;
	}

	/**
	 * ��ȡӰ��״̬��
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 * @return short[]
	 */
	private static short[] readAffectedState(DataInputStream dis)
			throws IOException {
		int cnt = dis.readByte();
		cnt = cnt * 2;
		short[] affectedState = new short[cnt];
		for (int i = 0; i < cnt; i++) {
			affectedState[i] = dis.readShort();
		}
		return affectedState;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 * @return short[]
	 */
	private static short[] readFiveProperty(DataInputStream dis)
			throws IOException {
		int cnt = dis.readByte();
		short[] fiveProperty = new short[cnt];
		for (int j = 0; j < cnt; j++) {
			fiveProperty[j] = dis.readShort();
		}
		return fiveProperty;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 */
	private static void loadDataParam(DataInputStream dis) throws IOException {
		Data.ANIMATION_ID = dis.readByte();
		// role property
		Data.COUNT_ROLE_PROPERTY = dis.readByte();
		Data.STR_ROLE_PROPERTY_NAMES = new String[Data.COUNT_ROLE_PROPERTY];
		for (int i = 0; i < Data.COUNT_ROLE_PROPERTY; i++) {
			Data.STR_ROLE_PROPERTY_NAMES[i] = dis.readUTF();
		}
		// equip part name
		Data.STR_EQUIP_PART_NAMES = new String[dis.readByte()];
		for (int i = 0; i < Data.STR_EQUIP_PART_NAMES.length; i++) {
			Data.STR_EQUIP_PART_NAMES[i] = dis.readUTF();
		}
		// five property
		Data.COUNT_FIVE_PROPERTY = dis.readByte();
		Data.STR_FIVE_PROPERTY_NAMES = new String[Data.COUNT_FIVE_PROPERTY];
		for (int i = 0; i < Data.COUNT_FIVE_PROPERTY; i++) {
			Data.STR_FIVE_PROPERTY_NAMES[i] = dis.readUTF();
		}
		// state
		Data.COUNT_STATE = dis.readByte();
		Data.STR_STATE_NAMES = new String[Data.COUNT_STATE];
		Data.STATE_INFO = new short[Data.COUNT_STATE][4];
		for (int i = 0; i < Data.COUNT_STATE; i++) {
			Data.STR_STATE_NAMES[i] = dis.readUTF();
			Data.STATE_INFO[i][IDX_STI_actionID] = dis.readShort();
			Data.STATE_INFO[i][IDX_STI_value0] = dis.readShort();
			Data.STATE_INFO[i][IDX_STI_value1] = dis.readShort();
			Data.STATE_INFO[i][IDX_STI_time] = dis.readShort();
		}
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 */
	private static void loadSkills(DataInputStream dis) throws IOException {
		int count = dis.readByte();
		Data.STR_SKILL_NAMES = new String[count][3];
		Data.SKILL_INFO = new short[count][12];

		Data.SKILL_FIVE_PROPERTY = new short[count][];
		Data.SKILL_AFFECTED_STATE = new short[count][];
		Data.SKILL_AFFECTED_PROPERTY = new short[count][];

		Vector vSkillDataID = new Vector();
		for (int i = 0; i < count; i++) {
			vSkillDataID.addElement(String.valueOf(i));
			Data.STR_SKILL_NAMES[i][0] = dis.readUTF();
			Data.STR_SKILL_NAMES[i][1] = dis.readUTF();
			Data.STR_SKILL_NAMES[i][2] = dis.readUTF();
			Data.SKILL_INFO[i][IDX_SKI_icon] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_type] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_upLvDest] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_target] = dis.readShort();
			// Data.SKILL_INFO[i][IDX_SKI_condition] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_cg] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_cgDest] = dis.readShort();
			// Data.SKILL_INFO[i][IDX_SKI_music] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_price] = dis.readShort();
			// Data.SKILL_INFO[i][IDX_SKI_isNearShow] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_keepTime] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_coolTime] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_consumeMP] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_stateRate] = dis.readShort();
			Data.SKILL_INFO[i][IDX_SKI_upExp] = dis.readShort();
			// five property
			Data.SKILL_FIVE_PROPERTY[i] = readFiveProperty(dis);
			// affected property
			Data.SKILL_AFFECTED_PROPERTY[i] = readAffectedProperty(dis);
			// affected state
			Data.SKILL_AFFECTED_STATE[i] = readAffectedState(dis);
		}
		// ����������
		int skillCount = count;
		boolean[] isLinkHead = new boolean[count];
		for (int i = 0; i < count; i++) {
			isLinkHead[i] = true;
		}
		for (int i = 0; i < count; i++) {
			short dest = Data.SKILL_INFO[i][IDX_SKI_upLvDest];
			if (dest != -1) {
				skillCount--;
				isLinkHead[dest] = false;
			}
		}
		SKILL_GROUP = new short[skillCount][];
		int skillGroupIndex = 0;
		for (int i = 0; i < count; i++) {
			if (isLinkHead[i]) {
				Vector v = new Vector();
				v.addElement(String.valueOf(i));
				int dest = Data.SKILL_INFO[i][IDX_SKI_upLvDest];
				while (dest != -1) {
					if (v.contains(String.valueOf(dest))) {
						System.out.println(">>Error! �������ݱ༭�����������ݼ�ǵ�һ����!������");
						break;
					}
					v.addElement(String.valueOf(dest));
					dest = Data.SKILL_INFO[dest][IDX_SKI_upLvDest];
				}
				SKILL_GROUP[skillGroupIndex] = new short[v.size()];
				for (int j = 0; j < v.size(); j++) {
					SKILL_GROUP[skillGroupIndex][j] = Short
							.parseShort((String) v.elementAt(j));
				}
				skillGroupIndex++;
			}
		}
		// ͳһһ������Ϊ�׸�DataID��Ӧ������
		// ???????????
		// for ( int i = 0; i < SKILL_GROUP.length; i++ )
		// {
		// for ( int j = 1; j < SKILL_GROUP[i].length; j++ )
		// {
		// Data.STR_SKILL_NAMES[SKILL_GROUP[i][j]][0] =
		// Data.STR_SKILL_NAMES[SKILL_GROUP[i][0]][0];
		// Data.STR_SKILL_NAMES[SKILL_GROUP[i][j]][1] =
		// Data.STR_SKILL_NAMES[SKILL_GROUP[i][0]][1];
		// }
		// }
		int ii = 0;
	}

	/**
	 * ������ݶ�Ӧ�ļ����������
	 * 
	 * @param did
	 *            int
	 * @return int
	 */
	public static int getSkillGroupIdx(int did) {
		for (int i = 0; i < SKILL_GROUP.length; i++) {
			for (int j = 0; j < SKILL_GROUP[i].length; j++) {
				if (SKILL_GROUP[i][j] == did) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * �ж������Ƿ��Ǽ�����ĵ�һ������
	 * 
	 * @param did
	 *            int
	 * @return boolean
	 */
	public static boolean isSkillGroupFirst(int did) {
		for (int i = 0; i < SKILL_GROUP.length; i++) {
			if (SKILL_GROUP[i][0] == did) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ��ü������������󳤶�
	 * 
	 * @param sgi
	 *            int
	 * @return int
	 */
	public static int getSkillML(int sgi) {
		return Data.SKILL_GROUP[sgi].length;
	}

	/**
	 * ��ȡװ������
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 */
	private static void loadEquips(DataInputStream dis) throws IOException {
		int count = dis.readByte();
		Data.STR_EQUIP_NAMES = new String[count][3];
		Data.EQUIP_INFO = new short[count][9];
		Data.EQUIP_FIVE_PROPERTY = new short[count][];
		Data.EQUIP_AFFECTED_PROPERTY = new short[count][];
		for (int i = 0; i < count; i++) {
			Data.STR_EQUIP_NAMES[i][0] = dis.readUTF();
			Data.STR_EQUIP_NAMES[i][1] = dis.readUTF();
			Data.STR_EQUIP_NAMES[i][2] = dis.readUTF(); // 2008.12.10
			Data.EQUIP_INFO[i][IDX_EI_icon] = dis.readShort();
			Data.EQUIP_INFO[i][IDX_EI_price] = dis.readShort();
			Data.EQUIP_INFO[i][IDX_EI_level] = dis.readShort();
			Data.EQUIP_INFO[i][IDX_EI_part] = dis.readShort();
			Data.EQUIP_INFO[i][IDX_EI_newEquipID] = dis.readShort();
			Data.EQUIP_INFO[i][IDX_EI_adtRate] = dis.readShort();
			Data.EQUIP_INFO[i][IDX_EI_adtID] = dis.readShort();
			Data.EQUIP_INFO[i][IDX_EI_antiID] = dis.readShort();
			Data.EQUIP_INFO[i][IDX_EI_showlevel] = dis.readShort(); // 2008.12.10

			Data.EQUIP_FIVE_PROPERTY[i] = Data.readFiveProperty(dis);
			Data.EQUIP_AFFECTED_PROPERTY[i] = Data.readAffectedProperty(dis);
		}
	}

	/**
	 * ��ȡ��Ʒ����
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 */
	private static void loadGoods(DataInputStream dis) throws IOException {
		int count = dis.readByte();
		Data.STR_GOODS_NAMES = new String[count][3];
		Data.GOODS_INFO = new short[count][8];
		Data.GOODS_AFFECTED_PROPERTY = new short[count][];
		Data.GOODS_AFFECTED_STATE = new short[count][];
		for (int i = 0; i < count; i++) {
			Data.STR_GOODS_NAMES[i][0] = dis.readUTF();
			Data.STR_GOODS_NAMES[i][1] = dis.readUTF();
			Data.STR_GOODS_NAMES[i][2] = dis.readUTF();
			Data.GOODS_INFO[i][IDX_GI_icon] = dis.readShort();
			Data.GOODS_INFO[i][IDX_GI_price] = dis.readShort();
			Data.GOODS_INFO[i][IDX_GI_target] = dis.readShort();
			Data.GOODS_INFO[i][IDX_GI_type] = dis.readShort();
			Data.GOODS_INFO[i][IDX_GI_cg] = dis.readShort();
			Data.GOODS_INFO[i][IDX_GI_cgDest] = dis.readShort();
			Data.GOODS_INFO[i][IDX_GI_time] = dis.readShort();
			Data.GOODS_INFO[i][IDX_GI_showlevel] = dis.readShort(); // 2008.12.10
																	// //2009.01.13
																	// bug

			Data.GOODS_AFFECTED_PROPERTY[i] = Data.readAffectedProperty(dis);
			Data.GOODS_AFFECTED_STATE[i] = Data.readAffectedState(dis);
		}
	}

	/**
	 * ��ȡְҵ����
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 */
	private static void loadClasses(DataInputStream dis) throws IOException {
		int count = dis.readByte();
		Data.STR_CLASS_NAMES = new String[count][2];
		Data.CLASS_EQUIP_USED = new short[count][8];
		Data.CLASS_SKILL = new short[count][];
		for (int i = 0; i < count; i++) {
			Data.STR_CLASS_NAMES[i][0] = dis.readUTF();
			int cnt;
			// equip used
			cnt = dis.readShort();
			Data.CLASS_EQUIP_USED[i] = new short[cnt];
			for (int j = 0; j < cnt; j++) {
				Data.CLASS_EQUIP_USED[i][j] = dis.readShort();
			}
			// my skills
			cnt = dis.readShort();
			cnt <<= 1;
			Data.CLASS_SKILL[i] = new short[cnt];
			for (int j = 0; j < cnt; j++) {
				Data.CLASS_SKILL[i][j] = dis.readShort();
			}
		}
	}

	/**
	 * ��ȡ��ɫ����
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 */
	private static void loadRoles(DataInputStream dis) throws IOException {
		int count = dis.readByte();
		Data.STR_ROLE_NAMES = new String[count][2];
		Data.ROLE_INFO = new short[count][15];
		Data.ROLE_FORMULA_PARAM = new short[count][Data.COUNT_ROLE_PROPERTY][4];
		Data.ROLE_INIT_EQUIP = new short[count][5];
		Data.ROLE_INIT_GOODS = new short[count][];
		Data.ROLE_FIVE_PROPERTY = new short[count][];
		for (int i = 0; i < count; i++) {
			Data.STR_ROLE_NAMES[i][0] = dis.readUTF();
			Data.STR_ROLE_NAMES[i][1] = dis.readUTF();
			Data.ROLE_INFO[i][IDX_RI_classID] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_level] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_maxLevel] = dis.readShort();
			// //2008.09.02 �غ���ģ�� ���������6������
			Data.ROLE_INFO[i][IDX_RI_SKILL0] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL0_INIT_LEVEL] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL1] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL1_INIT_LEVEL] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL2] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL2_INIT_LEVEL] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL3] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL3_INIT_LEVEL] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL4] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL4_INIT_LEVEL] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL5] = dis.readShort();
			Data.ROLE_INFO[i][IDX_RI_SKILL5_INIT_LEVEL] = dis.readShort();

			int cnt;
			// formula parameters
			cnt = dis.readShort();
			for (int j = 0; j < cnt; j++) {
				Data.ROLE_FORMULA_PARAM[i][j][0] = dis.readShort();
				Data.ROLE_FORMULA_PARAM[i][j][1] = dis.readShort();
				Data.ROLE_FORMULA_PARAM[i][j][2] = dis.readShort();
				Data.ROLE_FORMULA_PARAM[i][j][3] = dis.readShort();
			}
			// init equipment
			Data.ROLE_INIT_EQUIP[i][0] = dis.readShort();
			Data.ROLE_INIT_EQUIP[i][1] = dis.readShort();
			Data.ROLE_INIT_EQUIP[i][2] = dis.readShort();
			Data.ROLE_INIT_EQUIP[i][3] = dis.readShort();
			Data.ROLE_INIT_EQUIP[i][4] = dis.readShort();
			// init goods
			cnt = dis.readShort();
			cnt <<= 1;
			Data.ROLE_INIT_GOODS[i] = new short[cnt];
			for (int j = 0; j < cnt; j++) {
				Data.ROLE_INIT_GOODS[i][j] = dis.readShort();
			}
			Data.ROLE_FIVE_PROPERTY[i] = Data.readFiveProperty(dis);
		}
	}

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 */
	private static void loadDealers(DataInputStream dis) throws IOException {
		int count = dis.readByte();
		Data.STR_DEALER_NAMES = new String[count][2];
		Data.DEALER_INFO = new short[count][2];
		Data.DEALER_SKILL = new short[count][];
		Data.DEALER_EQUIP = new short[count][];
		Data.DEALER_GOODS = new short[count][];

		for (int i = 0; i < count; i++) {
			Data.STR_DEALER_NAMES[i][0] = dis.readUTF();
			Data.STR_DEALER_NAMES[i][1] = dis.readUTF();
			Data.DEALER_INFO[i][0] = dis.readShort(); // 2008.12.10
			Data.DEALER_INFO[i][1] = dis.readShort(); // 2008.12.10
			int cnt;
			// skill
			cnt = dis.readShort();
			Data.DEALER_SKILL[i] = new short[cnt];
			for (int j = 0; j < cnt; j++) {
				Data.DEALER_SKILL[i][j] = dis.readShort();
			}
			// equip
			cnt = dis.readShort();
			Data.DEALER_EQUIP[i] = new short[cnt];
			for (int j = 0; j < cnt; j++) {
				Data.DEALER_EQUIP[i][j] = dis.readShort();
			}
			// goods
			cnt = dis.readShort();
			Data.DEALER_GOODS[i] = new short[cnt];
			for (int j = 0; j < cnt; j++) {
				Data.DEALER_GOODS[i][j] = dis.readShort();
			}
		}
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 */
	private static void loadMobs(DataInputStream dis) throws IOException {
		int count = dis.readByte();
		Data.STR_MOB_NAMES = new String[count][2];
		Data.MOB_INFO = new short[count][16];
		Data.MOB_FIVE_PROPERTY = new short[count][];
		Data.MOB_AFFECTED_PROPERTY = new short[count][];
		for (int i = 0; i < count; i++) {
			Data.STR_MOB_NAMES[i][0] = dis.readUTF();
			Data.STR_MOB_NAMES[i][1] = dis.readUTF();
			Data.MOB_INFO[i][IDX_MI_level] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_type] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_money] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_adtRate] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_adtID] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_antiRate] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_antiID] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_dropRate] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_dropEquipID] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_dropItemID] = dis.readShort();
			// 2008.09.02 �غ���ģ��
			Data.MOB_INFO[i][IDX_MI_SKILL0] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_SKILL0_RATE] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_SKILL1] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_SKILL1_RATE] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_SKILL2] = dis.readShort();
			Data.MOB_INFO[i][IDX_MI_SKILL2_RATE] = dis.readShort();

			Data.MOB_FIVE_PROPERTY[i] = Data.readFiveProperty(dis);
			Data.MOB_AFFECTED_PROPERTY[i] = Data.readAffectedProperty(dis);
		}
	}

}
