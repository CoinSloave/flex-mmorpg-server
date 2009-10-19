package game.object;

import game.config.dConfig;

import com.mglib.ui.Data;

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
public interface IObject {

	/***
	 * ���õ��˶�״̬
	 */
	public final static byte ST_ACTOR_WAIT = 0;
	public final static byte ST_ACTOR_WALK = 1;
	public final static byte ST_ACTOR_RUN = 2;
	public final static byte ST_ACTOR_ATTACK = 3;
	public final static byte ST_ACTOR_DIE = 4;
	public final static byte ST_ACTOR_HURT = 5;
	public final static byte ST_ACTOR_JUMP = 6;
	public final static byte ST_ACTOR_DEFEND = 7;
	public final static byte ST_ACTOR_FAINT = 8;
	/**
	 *����״̬������
	 */
	public final static byte ST_COUNT = 9;

	public final static byte ST_ACTOR_CHEAT = 99;

	static int REBOUND_VY = -46;

	public static final int HERO_IN_CAMERA_X_LEFT = (6) << dConfig.FRACTION_BITS;
	public static final int HERO_IN_CAMERA_X_LEFT_LITTLE = (5) << dConfig.FRACTION_BITS;
	// #if screen_240_320
	public static final int HERO_IN_CAMERA_X_BATTLE = (6) << dConfig.FRACTION_BITS;
	// #else
	// # public static final int HERO_IN_CAMERA_X_BATTLE =
	// (5)<<dConfig.FRACTION_BITS;
	// #endif

	public static final int HERO_IN_CAMERA_X_RIGHT = 11 << dConfig.FRACTION_BITS;
	public static final int HERO_IN_CAMERA_X_CENTER = 8 << dConfig.FRACTION_BITS;
	public static final int HERO_IN_CAMERA_X_ROPE = 7 << dConfig.FRACTION_BITS;
	public static final int HERO_IN_CAMERA_Y_TOP = 2;
	public static final int HERO_IN_CAMERA_Y_TOP_LITTLE = 6;
	public static final int HERO_IN_CAMERA_Y_ROPE = 7;
	public static final int HERO_IN_CAMERA_Y_CENTER = 8;
	public static final int HERO_IN_CAMERA_Y_BELOW_LITTLE = 9;
	// #if screen_240_320
	public static final int HERO_IN_CAMERA_Y_BOTTOM = 12;
	// #else
	// # public static final int HERO_IN_CAMERA_Y_BOTTOM = 13;
	// #endif
	public static final int HERO_IN_CAMERA_Y_BOTTOM2 = 11;
	public static final int HERO_IN_CAMERA_Y_MORE_BELOW = 14;
	public static final int HERO_IN_CAMERA_SHIFT = 4;
	public static final int CAMERA_WIDTH_UNIT = dConfig.CAMERA_WIDTH >> HERO_IN_CAMERA_SHIFT;
	public static final int CAMERA_HEIGHT_UNIT = dConfig.CAMERA_HEIGHT >> HERO_IN_CAMERA_SHIFT;

	/**
	 * ��������
	 */
	public final static byte PRO_LENGTH = 38;

	public final static byte PRO_BASIC_LEN = 12;
	public final static byte PRO_INDEX_HP = Data.IDX_PRO_hp; // ������
	public final static byte PRO_INDEX_MAX_HP = Data.IDX_PRO_maxHp; // ���������
	public final static byte PRO_INDEX_MP = Data.IDX_PRO_mp; // ħ��ֵ
	public final static byte PRO_INDEX_MAX_MP = Data.IDX_PRO_maxMp; // ���ħ��ֵ
	public final static byte PRO_INDEX_ATT = Data.IDX_PRO_atk; // ������
	public final static byte PRO_INDEX_DEF = Data.IDX_PRO_def; // ������

	// �������Եĸ��ʺ͹�����
	public final static byte PRO_INDEX_CRI_RATE = Data.IDX_PRO_cri_rate;
	public final static byte PRO_INDEX_YUN_RATE = Data.IDX_PRO_zhuiHun_rate; // ����A
	public final static byte PRO_INDEX_DU_RATE = Data.IDX_PRO_hunMi_rate; // ����B
	public final static byte PRO_INDEX_MAGE_DEF = Data.IDX_PRO_mage_def; // ����C
	public final static byte PRO_INDEX_HIT_RATE = Data.IDX_PRO_hit_rate; // ����D
	public final static byte PRO_INDEX_NEXT_EXP = Data.IDX_PRO_next_exp; // ����C

	public final static byte PRO_INDEX_EFFECT_ID = PRO_BASIC_LEN + 0; // ��ЧID
	public final static byte PRO_INDEX_ATTID = PRO_BASIC_LEN + 1; //
	public final static byte PRO_INDEX_MONEY = PRO_BASIC_LEN + 2; // ��Ǯ
	public final static byte PRO_INDEX_OOXX = PRO_BASIC_LEN + 3; // ������һ����Ҫ�ľ���
	public final static byte PRO_INDEX_MAPRATE = PRO_BASIC_LEN + 4; // ��ͼ�����
	public final static byte PRO_INDEX_STEP_DG = PRO_BASIC_LEN + 5;//
	public final static byte PRO_INDEX_NORMAL_DG = PRO_BASIC_LEN + 6;//
	public final static byte PRO_INDEX_LEVEL = PRO_BASIC_LEN + 7; //

	// װ��
	public final static byte PRO_INDEX_WEAPON = PRO_BASIC_LEN + 8; // װ��ID
	public final static byte PRO_INDEX_LORICAE = PRO_BASIC_LEN + 9; // װ��ID
	public final static byte PRO_INDEX_SHOES = PRO_BASIC_LEN + 10; // װ��ID
	public final static byte PRO_INDEX_RING = PRO_BASIC_LEN + 11; // װ��ID
	public final static byte PRO_INDEX_JADE = PRO_BASIC_LEN + 12; // װ��ID

	// ------------------------------�ű��̶�ʹ��
	public final static byte PRO_INDEX_CUTINSTATE = PRO_BASIC_LEN + 13;
	public final static byte PRO_INDEX_CUTOUTSTATE = PRO_BASIC_LEN + 14;

	// ·�����
	public final static int PRO_PATH_ID = PRO_BASIC_LEN + 15; // ·��ID
	public final static int PRO_AFTER_PATH_STATE = PRO_BASIC_LEN + 16; // ·��ִ�к���߼�״̬
	public final static int PRO_NOW_PATH = PRO_BASIC_LEN + 17; // -1��ʾ�޹켣,��ǰִ�е�PATH
	public final static int PRO_PATH_AGAIN = PRO_BASIC_LEN + 18;
	public final static int PRO_PATH_END = PRO_BASIC_LEN + 19; // ·���Ƿ�ִ����?
	public final static int PRO_PATH_INIT_STATE = PRO_BASIC_LEN + 20; // ����pathǰ��actionID
	// �����л�
	public final static int PRO_WEAPON_NUM = PRO_BASIC_LEN + 21; // ��ǰ���е���������
	public final static int PRO_CUR_WP = PRO_BASIC_LEN + 22;

	public final static int PRO_INDEX_EXP = PRO_BASIC_LEN + 23; // ����ֵ
	public final static int PRO_INDEX_SP = PRO_BASIC_LEN + 24; // ���ܵ�
	public final static int PRO_INDEX_ROLE_ID = PRO_BASIC_LEN + 25; // ��ɫID
	// �������Թ���
	// public final static int PRO_IMMU_FIRE=PRO_BASIC_LEN + 24; //��������
	// public final static int PRO_RESIST_FIRE=PRO_BASIC_LEN + 25; //�������ˣ�ǧ�ֱ�
	// public final static int PRO_IMMU_THUNDER=PRO_BASIC_LEN + 26;
	// public final static int PRO_IMMU_ICE=PRO_BASIC_LEN + 27;
	// public final static int PRO_RESIST_ICE=PRO_BASIC_LEN + 28;

}
