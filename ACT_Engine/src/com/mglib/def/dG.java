package com.mglib.def;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public interface dG {
	Font F_SMALL_DEFAULT = Font.getFont(0, 0, 8);
	Font F_MIDDLE = Font.getFont(0, 1, 0);
	Font F_LARGE = Font.getFont(64, 1, 16);

	/**
	 * 小字体SF尺寸信息
	 */
	int SF_HEIGHT = F_SMALL_DEFAULT.getHeight() - 1;
	int SF_WIDTH = F_SMALL_DEFAULT.stringWidth("宽");
	int SF_BASELINE_POS = F_SMALL_DEFAULT.getBaselinePosition();
	int SF_DELTA_BASELINE = SF_BASELINE_POS - (SF_HEIGHT >> 1); // =
																// (S_FONT_HEIGHT/2)
																// 距离 baseline
																// 的像素距离

	/**
	 * Anchor绘制锚点
	 */
	int ANCHOR_LT = Graphics.LEFT | Graphics.TOP;
	int ANCHOR_LV = Graphics.LEFT | Graphics.VCENTER;
	int ANCHOR_LB = Graphics.LEFT | Graphics.BOTTOM;
	int ANCHOR_HT = Graphics.HCENTER | Graphics.TOP;
	int ANCHOR_HV = Graphics.HCENTER | Graphics.VCENTER;
	int ANCHOR_HB_LINE = Graphics.HCENTER | Graphics.BASELINE;
	int ANCHOR_HB = Graphics.HCENTER | Graphics.BOTTOM;
	int ANCHOR_RT = Graphics.RIGHT | Graphics.TOP;
	int ANCHOR_RV = Graphics.RIGHT | Graphics.VCENTER;
	int ANCHOR_RB = Graphics.RIGHT | Graphics.BOTTOM;

	int COLOR_WHITE = 0x00FFFFFF;
	int COLOR_BLACK = 0x00000000;
	int COLOR_GRAY = 0x00808080;
	int COLOR_RED = 0x00FF0000;
	int COLOR_DARKRED = 0x00a80000;
	int COLOR_ORANGE = 0x00FF658D;
	int COLOR_YELLOW = 0x00FFFF00;
	int COLOR_GREEN = 0x0000FF00;
	int COLOR_CYAN = 0x0000FFFF;
	int COLOR_BLUE = 0x000000FF;
	int COLOR_PURPLE = 0x00FF00FF;
	int DEFAULT_WATER_COLOR = 0x7E;
	int CUSTOM_FONT_COLOR = 0x84F68C;

}
