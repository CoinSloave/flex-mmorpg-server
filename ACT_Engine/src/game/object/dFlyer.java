package game.object;

public interface dFlyer {
	/****************************************************
	 * ���ݿ�����
	 ***************************************************/
	// 1.�켣�Ļ�������
	// path_base_data[][]��
	// [�켣���]
	// [offset_x0,offet_y0,offset_x1,offet_y1...offset_xn,offet_yn]
	// public final static int OFFSET_X = 0; //X���ƫ����
	// public final static int OFFSET_Y = 1; //Y���ƫ����

	// 2.�켣������
	// path_group_data[][][]:
	// [������ID]
	// [����Ĺ켣���]
	// [�켣���ID,�켣ѭ������];
	public final static int DATA_PATH_ID = 0; // �켣���
	public final static int DATA_PATH_LOOP = 1; // ѭ������
	public final static int DATA_PATH_STATE = 2; // ѭ������

	// 3.�ӵ���������
	// bullet_base_data[][][]��
	// [�ӵ�����ID]
	// [ÿ���ӵ�]
	// [�ӵ����ͣ��̶������������١����桢S�ͣ�������ID, ����ID, ������, �ٶȽǶ�,
	// �ؽǶȷ�����ٶ�, ��ЧID, ���ٶ�, ���ٶȽǶ�, �ӵ���������ĵ�ƫ��X,
	// �ӵ���������ĵ�ƫ��Y, ѭ������, �ӵ��Ƿ��������(0:��; 1:��), ��ʱ��, �ӵ��Ƿ��ܵ���,
	// �Ƿ������������, �ӵ������ķ�������, ��Զ����]
	public final static byte DATA_INFO_BULLET_LENGTH = 14;// �ӵ����ݿ������Դ�С

	public final static int DATA_INFO_BULLET_TYPE = 0; // �ӵ����ͣ��̶������������١����桢S�ͣ�
	public final static int DATA_INFO_BULLET_ANIMATION_ID = 1; // ����ID
	public final static int DATA_INFO_BULLET_ACTION_ID = 2; // ��ǰ����ID
	public final static int DATA_INFO_BULLET_POWER = 3; // ������
	public final static int DATA_INFO_BULLET_ANGLE = 4; // �ٶȽǶ�
	public final static int DATA_INFO_BULLET_SPEED = 5; // �ؽǶȷ�����ٶ�
	public final static int DATA_INFO_BULLET_DIE_ID = 6; // �ӵ�������ЧID
	public final static int DATA_INFO_BULLET_AX = 7; // ���ٶ�
	public final static int DATA_INFO_BULLET_AY = 8; // ���ٶȽǶ�
	public final static int DATA_INFO_BULLET_OFFSET_X = 9; // �ӵ���������ĵ�ƫ��x
	public final static int DATA_INFO_BULLET_OFFSET_Y = 10; // �ӵ���������ĵ�ƫ��y
	public final static int DATA_INFO_BULLET_FLAG = 11; // �ӵ���������ĵ�ƫ��y
	public final static int DATA_INFO_BULLET_LIVETIME = 12; // ����ʱ�� ���>0
															// ��ʾ����ʧ��,Ϊ1��ʱ����ʧ.���==0
															// ��һֱ����ʧһֱ���ڣ����<-1
															// ����ʱ��ִ�������߼�һֱǰ����
	public final static int DATA_INFO_BULLET_NUM_DIR = 13; // �ӵ������ķ�������

	// �ӵ�flag
	public final static int DATA_INFO_BULLET_FLAG_ISCOLLIDE = 1 << 5; // �ӵ��Ƿ��������(0:��;
																		// 1:��)
																		// NO(�����ò���)
	public final static int DATA_INFO_BULLET_FLAG_ISCIRCLE = 1 << 4; // ѭ������
	public final static int DATA_INFO_BULLET_FLAG_ISKILL = 1 << 3; // �ӵ��Ƿ��ܵ���
	public final static int DATA_INFO_BULLET_FLAG_ISFOLLOW = 1 << 2; // �Ƿ������������
	public final static int DATA_INFO_BULLET_FLAG_NEVER_DIE = 1 << 1;// ��Զ��������������ӵ���������ʱ����
	public final static int DATA_INFO_BULLET_FLAG_ISBACKUP = 1 << 0;// isBackup

	// 4.�ӵ�����������
	// bullet_group_data[][][]:
	// [������ID]
	// [������ӵ����]
	// [�ӵ���ID, ������ӵ�Ƶ��, ʣ�൯ҩ����(-1����,�̶�����), ����ʱ��];
	public final static byte DATA_BULLET_LENGTH = 18;// �ӵ����ݿ������Դ�С

	public final static int DATA_BULLET_ID = 0; // �ӵ���Id
	public final static int DATA_BULLET_FRE = 1; // ������ӵ���Ƶ��
	public final static int DATA_BULLET_NUMBER = 2; // ��ҩʣ������(-1����,�̶�����);
	public final static int DATA_BULLET_SHOOT_TIME = 3; // ����ʱ��

	/****************************************************
	 * ʹ������
	 ***************************************************/
	// 1.����Ĺ켣ʹ��
	// ĳ������Ĺ켣 actor_path[][]:
	// [�켣���]
	// [�켣���ID, �켣ѭ������, �Ѿ�ִ�д���, �Ѿ�ִ�е���];
	public final static int PATH_ID = 0; // �켣���
	public final static int PATH_LOOP = 1; // ѭ������
	public final static int PATH_FLOOP = 2; // �Ѿ�ִ�д���
	public final static int PATH_INDEX = 3; // �Ѿ�ִ�е���
	public final static int PATH_STATE = 4; // ·��״̬

	// 2.������ӵ�ʹ��
	// ����ĵ�ҩ actor_bullet[][]:
	// [�ӵ����]
	// [�ӵ���ID, ������ӵ�Ƶ��, ��ҩ����(-1����,�̶�����), �ӵ��ķ���ʱ��];
	public final static int BULLET_ID = 0; // �ӵ���Id
	public final static int BULLET_FRE = 1; // ������ӵ���Ƶ��
	public final static int BULLET_NUMBER = 2; // ��ҩʣ������(-1����,�̶�����);
	public final static int BULLET_SHOOT_TIME = 3; // ���ӵ��ķ���ʱ��

	// 3.�ӵ���������
	public final static int INFO_BULLET_LENGHT = 30;

	public final static int INFO_BULLET_TYPE = 0; // �ӵ����ͣ��̶������������١����桢S�ͣ�
	public final static int INFO_BULLET_ANIMATION_ID = 1; // ����ID
	public final static int INFO_BULLET_ACTION_ID = 2;// �ӵ�actionID
	public final static int INFO_BULLET_POWER = 3; // ������
	public final static int INFO_BULLET_ANGLE = 4; // �ٶȽǶ�
	public final static int INFO_BULLET_SPEED = 5; // �ؽǶȷ�����ٶ�
	public final static int INFO_BULLET_DIE_ID = 6;// �ӵ�����actionID.
	public final static int INFO_BULLET_AX = 7; // ���ٶ�x
	public final static int INFO_BULLET_AY = 8; // ���ٶ�y
	public final static int INFO_BULLET_X = 9; // �ӵ�x
	public final static int INFO_BULLET_Y = 10; // �ӵ�y
	public final static int INFO_BULLET_FLAG = 11; // �ӵ�flag
	public final static int INFO_BULLET_LIVE_TIME = 12; // ����ʱ�� ���>0
														// ��ʾ����ʧ��,Ϊ1��ʱ����ʧ.���==0
														// ��һֱ����ʧһֱ���ڣ����<-1
														// ����ʱ��ִ�������߼�һֱǰ����
	public final static int INFO_BULLET_NUM_DIR = 13;// �ӵ��ķ�����
	public final static int INFO_BULLET_FOLLOW_HOSTID = 14; // �������˵�IDInObjList��-1��ʾû�и��棬100��ʾ�������ǣ�>0��ʾ����Ķ���
	public final static int INFO_BULLET_DEPEND = 15; // �ӵ�����(0����,1����)
	public final static int INFO_BULLET_LOCKED_OBJID = 16; // �����ӵ������ĵĶ�����objList�еı��
	public final static int INFO_BULLET_SPEED_X = 17; // �ӵ��ٶ�x
	public final static int INFO_BULLET_SPEED_Y = 18; // �ӵ��ٶ�y
	public final static int INFO_BULLET_SQUENCE_INDEX = 19; // �ӵ�����������
	public final static int INFO_BULLET_FRAME_DELAY = 20; // �ӵ�����������
	public final static int INFO_BULLET_STATE = 21; // �ӵ�״̬
	public final static int INFO_BULLET_DIR = 22; // �ӵ�����
	public final static int INFO_BULLET_STATE_TIME = 23; // �ӵ�״̬ʱ��

	public final static int INFO_BULLET_CX0 = 24; // �ӵ���״
	public final static int INFO_BULLET_CY0 = 25; //
	public final static int INFO_BULLET_CX1 = 26; //
	public final static int INFO_BULLET_CY1 = 27; //
	public final static int INFO_BULLET_OFFSET_X = 28; // �ӵ���������ĵ�ƫ��x
	public final static int INFO_BULLET_OFFSET_Y = 29; // �ӵ���������ĵ�ƫ��y
	// �ӵ�flag
	public final static short INFO_BULLET_FLAG_ISCOLLIDE = 1 << 5; // �ӵ��Ƿ��������(0:��;
																	// 1:��)
																	// NO(�����ò���)
	public final static short INFO_BULLET_FLAG_ISCIRCLE = 1 << 4; // ѭ������
	public final static short INFO_BULLET_FLAG_ISKILL = 1 << 3; // �ӵ��Ƿ��ܵ���
	public final static short INFO_BULLET_FLAG_ISFOLLOW = 1 << 2; // �Ƿ������������
	public final static short INFO_BULLET_FLAG_NEVER_DIE = 1 << 1;// ��Զ��������������ӵ���������ʱ����
	public final static short INFO_BULLET_FLAG_ISBACKUP = 1 << 0;// isBackup

	/***************************************
	 * �������ݶ���Ķ���
	 **************************************/
	// �ӵ����Ͷ���
	public final static byte BULLET_TYPE_FIXATION = 0;// �̶�
	public final static byte BULLET_TYPE_LOCKED = 1;// ����
	public final static byte BULLET_TYPE_FOLLOW = 2;// ����
	public final static byte BULLET_TYPE_MIRROR = 3;// ����
	public final static byte BULLET_TYPE_S = 4;// S��

	// �������˵�IDInObjList
	public final static byte BULLET_FOLOW_HOSTID_NO_FOLLOW = -1;// ��ʾû�и���
	public final static byte BULLET_FOLOW_HOSTID_FOLLOW_HERO = -2;// ��ʾ��������

	// �ӵ�������ϵ
	public final static byte BULLET_DEPEND_HERO = 0;
	public final static byte BULLET_DEPEND_ENEMY = 1;

	// �ӵ���ʼλ�ö���
	public static final byte BULLET_Y_FROM_SCREEN_BOTTOM = -1;// �ӵ�����Ļ�ױ߳�y
	public static final byte BULLET_Y_RANDOM_IN_SCREEN = -2;// �ӵ�����Ļ�漴��y
	public static final byte BULLET_X_FROM_SCREEN_RIGHT = -1;// �ӵ�����Ļ�ұ߳�x
	public static final byte BULLET_X_RANDOM_IN_SCREEN = -2;// �ӵ�����Ļ�ױ߳�x

}
