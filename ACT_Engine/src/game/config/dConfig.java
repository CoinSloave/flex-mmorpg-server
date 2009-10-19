package game.config;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * <p>
 * Title: engine of RPG game
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: glu
 * </p>
 * 
 * @author lianghao chen
 * @version 1.0
 */
public final class dConfig {
	/**
	 * 机型列表
	 */
	public static final byte MODE_NOKIA = 0;
	public static final byte MODE_MOTO = 1;

	/**
	 * 公司所属
	 */
	public static final byte COMPANY_MIG = 0;
	public static final byte COMPANY_MLF = 1;

	public final static int FRACTION_BITS = 8; // 坐标移位

	public static final byte belongTo = COMPANY_MIG;

	/**
	 * PFS时间控制
	 */
	public static final byte FPS_RATE = 70;

	/**
	 * moto是否FullScreen
	 */
	public static final boolean FULL_SCREEN_MODE = true;

	/**
	 * 字体常量
	 */
	public static final Font F_SMALL_DEFAULT = Font.getFont(Font.FACE_SYSTEM,
			Font.STYLE_PLAIN, Font.SIZE_SMALL);
	public static final Font F_MIDDLE = Font.getFont(Font.FACE_SYSTEM,
			Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	public static final Font F_LARGE = Font.getFont(Font.FACE_SYSTEM,
			Font.STYLE_PLAIN, Font.SIZE_LARGE);

	public static final Font F_LARGE_BOLD = Font.getFont(Font.FACE_SYSTEM,
			Font.STYLE_BOLD, Font.SIZE_LARGE);
	// ------------ 屏幕尺寸
	// #if screen_240_320
	public final static int S_WIDTH = 240;
	public final static int S_HEIGHT = 320;
	// #elif screen_176_208
	// # public final static int S_WIDTH = 176;
	// # public final static int S_HEIGHT = 208;
	// #elif screen_176_220
	// # public final static int S_WIDTH = 176;
	// # public final static int S_HEIGHT = 220;
	// #elif screen_176_204
	// # public final static int S_WIDTH = 176;
	// # public final static int S_HEIGHT = 204;
	// #elif screen_208_208
	// # public final static int S_WIDTH = 208;
	// # public final static int S_HEIGHT = 208;
	// #elif screen_128_128
	// # public final static int S_WIDTH = 128;
	// # public final static int S_HEIGHT = 128;
	// #elif screen_128_160
	// # public final static int S_WIDTH = 128;
	// # public final static int S_HEIGHT = 160;
	// #elif screen_128_116
	// # public final static int S_WIDTH = 128;
	// # public final static int S_HEIGHT = 116;
	// #endif

	public static final int S_WIDTH_HALF = S_WIDTH >> 1;
	public static final int S_HEIGHT_HALF = S_HEIGHT >> 1;

	public final static int INTERFACE_TOP_HEIGHT = 0;

	public final static int CAMERA_WIDTH = S_WIDTH;
	public final static int CAMERA_HEIGHT = S_HEIGHT - INTERFACE_TOP_HEIGHT;

	public final static int CAMERA_W_HALF = CAMERA_WIDTH >> 1;
	public final static int CAMERA_H_HALF = CAMERA_HEIGHT >> 1;

	public final static int SCREEN_W_HALF = S_WIDTH >> 1;
	public final static int SCREEN_H_HALF = S_HEIGHT >> 1;

	/**
	 * 小字体SF尺寸信息
	 */
	public static final int SF_HEIGHT = F_SMALL_DEFAULT.getHeight() - 1;
	public static final int SF_WIDTH = F_SMALL_DEFAULT.stringWidth("宽");
	private static final int SF_BASELINE_POS = F_SMALL_DEFAULT
			.getBaselinePosition();
	public static final int SF_DELTA_BASELINE = SF_BASELINE_POS
			- (SF_HEIGHT >> 1); // = (S_FONT_HEIGHT/2) 距离 baseline 的像素距离

	/**
	 * Anchor绘制锚点
	 */
	public static final int ANCHOR_LT = Graphics.LEFT | Graphics.TOP;
	public static final int ANCHOR_LV = Graphics.LEFT | Graphics.VCENTER;
	public static final int ANCHOR_LB = Graphics.LEFT | Graphics.BOTTOM;
	public static final int ANCHOR_HT = Graphics.HCENTER | Graphics.TOP;
	public static final int ANCHOR_HV = Graphics.HCENTER | Graphics.VCENTER;
	public static final int ANCHOR_HB_LINE = Graphics.HCENTER
			| Graphics.BASELINE;
	public static final int ANCHOR_HB = Graphics.HCENTER | Graphics.BOTTOM;
	public static final int ANCHOR_RT = Graphics.RIGHT | Graphics.TOP;
	public static final int ANCHOR_RV = Graphics.RIGHT | Graphics.VCENTER;
	public static final int ANCHOR_RB = Graphics.RIGHT | Graphics.BOTTOM;

	public static final int[] ANCHOR_TABLE = { ANCHOR_LT, ANCHOR_HB, ANCHOR_HT };

	/****************************************************************************
	 * 游戏关于帮助
	 * 
	 * 
	 * // 米格-中国地区发行商|010-65101811-263|www.mig.com.cn|supportcn@mig.com.cn" //
	 * 麦立方通信有限公司|www.mthree.com.cn|010-58690495|help@mthree.com.cn //
	 * 湖南智源|010-65676166|service@fumobile.com //
	 * 北京当途摩宝科技发展有限公司发行|www.down2mobile
	 * .com|010-84832907|Email：khfw@down2mobile.com // TOM.COM 互联网在线集团|明日在线
	 * 今日再现|www.tom.com|010-67868800|javaservice@tomonline-inc.com
	 * 
	 * 
	 * 
	 * /************************************************************************
	 * **** 按键定义
	 ***************************************************************************/

	public static final int COLOR_WHITE = 0x00FFFFFF;
	public static final int COLOR_BLACK = 0x00000000;
	public static final int COLOR_GRAY = 0x00808080;
	public static final int COLOR_RED = 0x00FF0000;
	public static final int COLOR_DARKRED = 0x00a80000;
	public static final int COLOR_ORANGE = 0x00FF658D;
	public static final int COLOR_YELLOW = 0x00FFFF00;
	public static final int COLOR_GREEN = 0x0000FF00;
	public static final int COLOR_CYAN = 0x0000FFFF;
	public static final int COLOR_BLUE = 0x000000FF;
	public static final int COLOR_PURPLE = 0x00FF00FF;

	public static final int DEFAULT_WATER_COLOR = 0x7E;
	public static final int CUSTOM_FONT_COLOR = 0x84F68C;

	public static final int COLOR_BORDER = 0x419cb4;
	public static final int COLOR_HP = 0x00ff0c;
	public static final int COLOR_MP = 0xff81e6;
	public static final int COLOR_EXP = 0xfff600;

	public static final int COLOR_TEXT = 0Xffcc77; // 文本颜色
	public static final int COLOR_TEXT_OUTER = -1;

	public static final int COLOR_SCENE_NAME = 0xFFCF00; // 显示场景名称
	public static final int COLOR_SCENE_NAME_OUT = 0x8D560F;

	public static final int COLOR_HERO_LEVEL = 0xF2F185; // 游戏中UI，主角等级
	public static final int COLOR_HERO_LEVEL_OUT = 0xAA571A;

	public static final int COLOR_SKILL_BAR = 0xFFCB00;

	public static final int COLOR_DIALOG_OUT = 0x9F5400; // 普通脚本对话

	public static final int COLOR_BLUE2 = 0x03ACFF;

	public final static String STR_EXIT = "是否退出游戏?";
	public final static String STR_OK = "确定";
	public final static String STR_BACK = "返回";

	public static final int DLG_TEXT_DEFAULT_COLOR = 0xffffff;

	public static final int[] SD_COLOR_TABLE = { 0x00ffff, 0x000000, 0x0000ff,
			0xff00ff, 0x808080, 0x008000, 0x00ff00, 0x800000, 0x000080,
			0x808000, 0x800080, 0xff0000, 0xc0c0c0, 0x008080, 0xffffff,
			0xffff00 };

	public final static int MATH_DATA_FRACTION_BITS = 12; // 数学常量移位

	public static short[] m_tanTable = { 0, 100, 201, 302, 403, 505, 607, 710,
			814, 919, 1025, 1133, 1242, 1353, 1465, 1580, 1696, 1815, 1937,
			2061, 2189, 2320, 2455, 2593, 2736, 2884, 3037, 3196, 3361, 3533,
			3712, 3899, 4095, };

	public static short[] m_sinTable = { 0, 100, 200, 301, 401, 501, 601, 700,
			799, 897, 995, 1092, 1189, 1284, 1379, 1474, 1567, 1659, 1751,
			1841, 1930, 2018, 2105, 2191, 2275, 2358, 2439, 2519, 2598, 2675,
			2750, 2824, 2896, 2966, 3034, 3101, 3166, 3229, 3289, 3348, 3405,
			3460, 3513, 3563, 3612, 3658, 3702, 3744, 3784, 3821, 3856, 3889,
			3919, 3947, 3973, 3996, 4017, 4035, 4051, 4065, 4076, 4084, 4091,
			4094, 4096, };

	public static short[] m_cosTable = { 4096, 4094, 4091, 4084, 4076, 4065,
			4051, 4035, 4017, 3996, 3973, 3947, 3919, 3889, 3856, 3821, 3784,
			3744, 3702, 3658, 3612, 3563, 3513, 3460, 3405, 3348, 3289, 3229,
			3166, 3101, 3034, 2966, 2896, 2824, 2750, 2675, 2598, 2519, 2439,
			2358, 2275, 2191, 2105, 2018, 1930, 1841, 1751, 1659, 1567, 1474,
			1379, 1284, 1189, 1092, 995, 897, 799, 700, 601, 501, 401, 301,
			200, 100, 0, };

	// 慢动作
	public static final byte Slow_Motion_Ratio = 3;

	public static final byte LOAD_FRAME = 1;

	// #if checkVersion
	// # public final static String m_aboutString
	// # = " ";
	// #elif mig
	public final static String m_aboutString = "关于||米格-中国地区发行商|010-59632878|www.mig.com.cn|supp"
			+ "ortcn@mig.com.cn||"
			+ "程  序|林 树 彬||策  划|方  堃||美  工|余  亮|郭 俊 瑜|徐  磊|张  辉|杨 子 庆||测试|朱凌|吴成其|殷宗庆";
	// #else
	// # public final static String m_aboutString
	// # = "关于|";
	// #endif

	public final static int ABOUT_LENGTH = S_WIDTH - 10;

	// #if keymode_nokia
	// #if D608
	// # public final static String m_helpString =
	// # "按键操作|方向键上(或2键):跳跃 空中时再次按上键：二段跳 方向键下(或8键)：蹲下 蹲下时按左右键：滑铲 方向"+
	// # "键左或右(或 4,6键)：向左或向右移动  方向键中(或5键):攻击 特殊键1或3：斜方向跳跃 特殊键7：技能1 特殊"+
	// # "键9：技能2  0键：换人 菜单中左软键确定，右软键返回，游戏中右软键系统菜单。";
	// #
	// #else
	public final static String m_helpString = "按键操作|方向键上(或2键):跳跃 空中时再次按上键：二段跳 方向键下(或8键)：蹲下 蹲下时按左右键：滑铲 方向"
			+ "键左或右(或 4,6键)：向左或向右移动  方向键中(或5键):攻击 特殊键1或3：斜方向跳跃 特殊键7：技能1 特殊"
			+ "键9：技能2  0键：换人 菜单中左软键确定，右软键返回，游戏中左软键系统菜单。";
	// #endif

	// #elif keymode_se||keymode_W958C
	// #if touch_screen
	// # public final static String m_helpString =
	// # "按键操作|2键:跳跃 空中时再次按2键：二段跳 8键：蹲下  蹲下时按4,6键：滑铲  4或6键：向左或向右移动   5键:攻"+
	// # "击  ||触摸屏操作|菜单中各选项均可实现触摸操作，点击主角：攻击 点击主角两边：左右移动 点击主角上方：跳跃 点击主角"+
	// # "下方：蹲下 点击屏幕右上方图标：施放技能和切换主角  点击屏幕左下角可以调出属性菜单，点击屏幕右下角可以"+
	// # "调出系统菜单。";
	// #else
	// # public final static String m_helpString =
	// # "按键操作|方向键上(或2键):跳跃 空中时再次按上键：二段跳 方向键下(或8键)：蹲下 蹲下时按左右键：滑铲 方向"+
	// # "键左或右(或 4,6键)：向左或向右移动  方向键中(或5键):攻击 特殊键1或3：斜方向跳跃 特殊键7：技能1 特殊"+
	// # "键9：技能2  0键：换人 菜单中左软键确定，右软键返回，游戏中右软键系统菜单。";
	// #
	// #endif
	// #else
	// #if touch_screen
	// # public final static String m_helpString =
	// # "按键操作|方向键上:跳跃 空中时再次按上键：二段跳 方向键下：蹲下  蹲下时按左右键：滑铲  方向键左或右：向左或向"+
	// # "右移动   方向键中:攻击  ||触摸屏操作|菜单中各选项均可实现触摸操作，点击主角：攻击 点击主角两边：左右移动 "+
	// # "点击主角上方：跳跃 点击主角下方：蹲下 点击屏幕右上方图标：施放技能和切换主角  点击屏幕左下角可以"+
	// # "调出属性菜单，点击屏幕右下角可以调出系统菜单。";
	// #else
	// # public final static String m_helpString =
	// # "按键操作|方向键上(或2键):跳跃 空中时再次按上键：二段跳 方向键下(或8键)：蹲下 蹲下时按左右键：滑铲 方向"+
	// # "键左或右(或 4,6键)：向左或向右移动  方向键中(或5键):攻击 特殊键1或3：斜方向跳跃 特殊键7：技能1 特殊"+
	// # "键9：技能2  0键：换人 菜单中右软键确定，左软键返回，游戏中右软键系统菜单。";
	// #
	// #endif
	// #
	// #endif

	public final static int HELP_T_LENGTH = S_WIDTH - 30;

	// "游戏按键:|方向键左和数字键4:|往左行走.|方向键右"
	// +"和数字键6:|往右行走|方向键上和数字键2:|跳跃|数字键1:|左跨越跳|数字键3:|右跨越跳|8键：下蹲|蹲下后按下方向键:下跳|方向键中和数字键5:|攻击|数字键0:使用技能";

	public final static String[] m_confirmString = { "确定", "返回" };
	public final static byte INDEX_OK = 0;
	public final static byte INDeX_BACK = 1;

}
