package game.object;

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
public class dActor {
	public dActor() {
	}

	// -------------------------------------------------------------
	// ���ж����еĻ�����Ϣ
	public static final short INDEX_CLASS_ID = 0;
	public static final short INDEX_ACTOR_ID = 1;
	public static final short INDEX_BASIC_FLAGS = 2;
	public static final short INDEX_STATE = 3;
	public static final short INDEX_LAYER_ID = 4;
	public static final short INDEX_SCRIPT_ID = 5;
	public static final short INDEX_ANIMATION_ID = 6;
	public static final short INDEX_ACTION_ID = 7;
	public static final short INDEX_POSITION_X = 8;
	public static final short INDEX_POSITION_Y = 9;
	public static final short INDEX_ACTIIVE_BOX_LEFT = 10;
	public static final short INDEX_ACTIVE_BOX_TOP = 11;
	public static final short INDEX_ACTIVE_BOX_RIGHT = 12;
	public static final short INDEX_ACTIVE_BOX_BOTTOM = 13;
	public static final short INDEX_DATA_ID = 14;

	public static final short INDEX_PARAMS_BASE = 15;

	public static final short MAX_EXTENDED_PARAMS_INFO = 32;

	// public static final short INDEX_COLLISION_BOX_LEFT = 0;
	// public static final short INDEX_COLLISION_BOX_TOP = 1;
	// public static final short INDEX_COLLISION_BOX_RIGHT = 2;
	// public static final short INDEX_COLLISION_BOX_BOTTOM = 3;
	// public static final short INDEX_ATTACK_BOX_LEFT = 4;
	// public static final short INDEX_ATTACK_BOX_TOP = 5;
	// public static final short INDEX_ATTACK_BOX_RIGHT = 6;
	// public static final short INDEX_ATTACK_BOX_BOTTOM = 7;
	// public static final short INDEX_COLLISION_BOX_FRONT = 8;
	// public static final short INDEX_COLLISION_BOX_BACK = 9;
	// public static final short INDEX_COLLISION_BOX_X_CENTER = 10;
	// public static final short INDEX_COLLISION_BOX_Y_CENTER = 11;

	// ----------------------------------------
	// һЩflag
	public static final int SIGN_BISIC_FLAGS = 1 << 31;
	public static final int SIGN_EXTENDED_FLAGS = 1 << 30;
	public static final int Sign_Flags_Reversed1 = 1 << 29;
	public static final int Sign_Flags_Reversed2 = 1 << 28;
	public static final int MASK_BASIC_FLAGS = 0x300000ff;
	public static final int MASK_EXTENDED_FLAGS = 0x3fffff00;
	public static final int MASK_ALL_FLAGS = 0x3fffffff;

	// basic info ���������ܳ���8 ȫ��flag
	public static final int FLAG_BASIC_FLIP_X = (1 << 0) | SIGN_BISIC_FLAGS; // �Ƿ�ת
	public static final int FLAG_BASLIC_NOT_LOADABLE = (1 << 1)
			| SIGN_BISIC_FLAGS; // �Ƿ��ܱ�����
	public static final int FLAG_BASIC_ALWAYS_ACTIVE = (1 << 2)
			| SIGN_BISIC_FLAGS; // �Ƿ���Լ�������
	public static final int FLAG_BASIC_VISIBLE = (1 << 3) | SIGN_BISIC_FLAGS; // �Ƿ����

	// basic flags, set in game, bits [4, 8]
	public static final int FLAG_BASIC_IS_SAVE = (1 << 4) | SIGN_BISIC_FLAGS; // �Ƿ���Ҫ�����ɫ��Ϣ
	public static final int FLAG_NO_LOGIC = (1 << 5) | SIGN_BISIC_FLAGS;
	public static final int FLAG_BASIC_REBORNABLE = (1 << 6) | SIGN_BISIC_FLAGS;
	public static final int FLAG_BASIC_DIE = (1 << 7) | SIGN_BISIC_FLAGS;
	public static final int FLAG_BASIC_NOT_PACKABLE = (1 << 8)
			| SIGN_BISIC_FLAGS;

	// 9-18 ÿ�������Զ����flag
	// -------------һЩ����flag [19,30]
	// ����flag �Ƿ񱻲�
	public static final int FLAG_ENEMY_NO_BE_ATT = (1 << 18)
			| SIGN_EXTENDED_FLAGS;
	public static final int FLAG_NEED_INIT = (1 << 19) | SIGN_EXTENDED_FLAGS; // ��Ҫ��ʼ��
	public static final int FLAG_BEATTACKED = (1 << 20) | SIGN_EXTENDED_FLAGS; // �Ƿ񱻹���
	public static final int FLAG_DIE_TO_SCRIPT = (1 << 21)
			| SIGN_EXTENDED_FLAGS; // �Ƿ�����ת�����ű�
	public static final int FLAG_ACTION_GRAB_MECH_INFO = (1 << 22)
			| SIGN_EXTENDED_FLAGS;
	// ִ�йؼ�֡�߼�
	public static final int FLAG_DO_KEYFRAME_LOGIC = (1 << 23)
			| SIGN_EXTENDED_FLAGS;
	// �������������ƶ��������¶���
	public static final int FLAG_FROZEN = (1 << 24) | SIGN_EXTENDED_FLAGS;
	// ѣ��
	public static final int FLAG_FAINT = (1 << 25) | SIGN_EXTENDED_FLAGS;

	// -----------------------------------------------------
	public static final byte BOX_ACTIVATE = 0;
	public static final byte BOX_COLLIDE = 1;
	public static final byte BOX_ATTACK = 2;

	public static final short MASK_ACTOR_INFO_INDEX = 0xff;
	public static final short SIGN_BASE_INFO = 1 << 14; // basic info

	public static final byte CLASS_FLAG_IS_ENEMY = 1 << 0; // �Ƿ��ǵ���
	public static final byte CLASS_FLAG_HAVE_PATH = 1 << 1; // �Ƿ���·��
	public static final byte CLASS_FLAG_IS_NPC = 1 << 2; // �Ƿ���NPC
	public static final byte CLASS_FLAG_CHECK_ENV = 1 << 3; // �Ƿ��������
	public static final byte CLASS_FLAG_PUSHABLE = 1 << 4;// �Ƿ�����ƶ�
	public static final byte CLASS_FLAG_BLOCKABLE = 1 << 5; // �Ƿ�����赲
	public static final byte CLASS_FLAG_SYSTEMOBJ = 1 << 6; // �Ƿ�ϵͳ����
	public static final short CLASS_FLAG_LOOT = 1 << 7; // �Ƿ������Ʒ

}
