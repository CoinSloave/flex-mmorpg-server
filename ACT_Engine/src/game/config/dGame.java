package game.config;

public interface dGame {

	/*********************************************************
	 * ��Ϸ���߼�״̬�Ķ���
	 *******************************************************/
	// add by zk 08 11 13
	public static final byte GST_NONE = -1; // ��״̬
	public static final byte GST_TEAM_LOGO = 0; // mig logo״̬
	public static final byte GST_CG = 1; // ��ƪ����״̬
	public static final byte GST_MAIN_MENU = 2; // ���˵�״̬
	public static final byte GST_GAME_LOAD = 3; // ��Դ����״̬
	public static final byte GST_GAME_RUN = 4; // ��Ϸ����״̬
	public static final byte GST_GAME_OVER = 5; // ��Ϸ����״̬
	public static final byte GST_GAME_PASS = 6; // ��Ϸͨ��״̬
	public static final byte GST_GAME_MENU = 7; // ��Ϸ�˵�״̬
	public static final byte GST_GAME_EXIT = 8; // ��Ϸ�˳�
	public static final byte GST_GAME_HELP = 9; // ��Ϸ����
	public static final byte GST_GAME_ABOUT = 10; // ��Ϸ����

	public static final byte GST_SCRIPT_DIALOG = 15;// �Ի�״̬
	public static final byte GST_SCRIPT_RUN = 16;// �ű�����״̬
	public static final byte GST_CAMARE_MOVE = 17;// ��ͷ�ƶ�
	public static final byte GST_SCRIPT_OPDLG = 18;// ѡ��Ի�
	public static final byte GST_TRAILER_RUN = 19; // ��Ϸtrailer ����״̬

	public static final byte GST_GAME_UI = 20;// UI
	public static final byte GST_GAME_OPTION = 21;// ѡ������
	public static final byte GST_GAME_IF_MUSIC = 22;// �Ƿ���Ҫ����
	public static final byte GST_GAME_HERO_DIE = 23;// ��������״̬

	public static final byte GST_GAME_SMS = 24; // ���żƷ�
	public static final byte GST_GAME_MORE_GAME = 25; // ���ྫ��

	public static final byte GST_MINI_GAME = 26; // ������Ϸ״̬

	// //�޸ĺ�ı�־
	// public static final int SIGN_NEW_LINE = 0;
	// public static final int SIGN_SMILE = 1;
	// public static final int SIGN_CSS = 2;
	// public static final int SIGN_CSS_DEFAULT = 3;
}
