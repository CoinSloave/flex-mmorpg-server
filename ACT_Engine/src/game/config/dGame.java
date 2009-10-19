package game.config;

public interface dGame {

	/*********************************************************
	 * 游戏主逻辑状态的定义
	 *******************************************************/
	// add by zk 08 11 13
	public static final byte GST_NONE = -1; // 空状态
	public static final byte GST_TEAM_LOGO = 0; // mig logo状态
	public static final byte GST_CG = 1; // 开篇动画状态
	public static final byte GST_MAIN_MENU = 2; // 主菜单状态
	public static final byte GST_GAME_LOAD = 3; // 资源载入状态
	public static final byte GST_GAME_RUN = 4; // 游戏运新状态
	public static final byte GST_GAME_OVER = 5; // 游戏结束状态
	public static final byte GST_GAME_PASS = 6; // 游戏通关状态
	public static final byte GST_GAME_MENU = 7; // 游戏菜单状态
	public static final byte GST_GAME_EXIT = 8; // 游戏退出
	public static final byte GST_GAME_HELP = 9; // 游戏帮助
	public static final byte GST_GAME_ABOUT = 10; // 游戏关于

	public static final byte GST_SCRIPT_DIALOG = 15;// 对话状态
	public static final byte GST_SCRIPT_RUN = 16;// 脚本运行状态
	public static final byte GST_CAMARE_MOVE = 17;// 镜头移动
	public static final byte GST_SCRIPT_OPDLG = 18;// 选择对话
	public static final byte GST_TRAILER_RUN = 19; // 游戏trailer 运行状态

	public static final byte GST_GAME_UI = 20;// UI
	public static final byte GST_GAME_OPTION = 21;// 选项设置
	public static final byte GST_GAME_IF_MUSIC = 22;// 是否需要音乐
	public static final byte GST_GAME_HERO_DIE = 23;// 主角死亡状态

	public static final byte GST_GAME_SMS = 24; // 短信计费
	public static final byte GST_GAME_MORE_GAME = 25; // 更多精彩

	public static final byte GST_MINI_GAME = 26; // 迷你游戏状态

	// //修改后的标志
	// public static final int SIGN_NEW_LINE = 0;
	// public static final int SIGN_SMILE = 1;
	// public static final int SIGN_CSS = 2;
	// public static final int SIGN_CSS_DEFAULT = 3;
}
