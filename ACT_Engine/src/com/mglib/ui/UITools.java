package com.mglib.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.mglib.def.dG;

public final class UITools {
	// 栈的变量定义
	private static int preColor, preX, preY, preCW, preCH;

	public static final void pushCC(Graphics g) {
		pushColor(g);
		pushClip(g);
	}

	public static final void popCC(Graphics g) {
		popColor(g);
		popClip(g);
	}

	/**
	 * 颜色进栈
	 * 
	 * @param g
	 *            Graphics
	 */
	public static final void pushColor(Graphics g) {
		preColor = g.getColor();
	}

	/**
	 * 颜色出栈
	 * 
	 * @param g
	 *            Graphics
	 */
	public static final void popColor(Graphics g) {
		g.setColor(preColor);
	}

	/**
	 * 裁剪进栈
	 * 
	 * @param g
	 *            Graphics
	 */
	public static final void pushClip(Graphics g) {
		preX = g.getClipX();
		preY = g.getClipY();
		preCW = g.getClipWidth();
		preCH = g.getClipHeight();
	}

	/**
	 * 裁剪出栈
	 * 
	 * @param g
	 *            Graphics
	 */
	public static final void popClip(Graphics g) {
		g.setClip(preX, preY, preCW, preCH);
	}

	/*
	 * public static final void decorateRect ( Graphics g , Rect r , int bgc ,
	 * int[] olcs, int type ) { pushCC(g); //背景填充 g.setColor ( bgc ); g.fillRect
	 * ( r.x0 , r.y0 , r.w , r.h ); //边框颜色 if ( olcs != null ) { int n =
	 * olcs.length; for ( int i = 0; i < n; i++ ) { g.setColor ( olcs[n - i - 1]
	 * ); g.drawRect ( r.x0 - i , r.y0 - i , r.w + i * 2 , r.h + i * 2 ); } }
	 * popCC(g); }
	 */
	/**
	 * 在指定的block中以指定anchor绘制图片
	 * 
	 * @param g
	 *            Graphics
	 * @param img
	 *            Image
	 * @param block
	 *            int[]
	 * @param anchor
	 *            int
	 */
	public static void drawImageIn(Graphics g, Image img, Rect r, int anchor) {
		short[] p = r.getXY(anchor);
		g.drawImage(img, p[0], p[1], anchor);
	}

	/**
	 * 相对低级的绘制滚屏文本
	 * 
	 * @param g
	 *            Graphics
	 * @param str
	 *            String
	 * @param counter
	 *            int
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param w
	 *            int
	 * @param h
	 *            int
	 * @param anchor
	 *            int
	 * @return int 返回整个文本的高度
	 */
	public static int drawVScrollString(Graphics g, String str, int counter,
			int x, int y, int w, int h, int anchor, int color, int bgColor) {
		char cf = '&';
		String line_buf = null; // 行缓冲
		int cur_width = 0;
		int sx = x;
		int sy = y;
		int max_width = w;
		int cur_height = 0;
		int total_height = 0;
		int max_height = h;
		int space = 1;
		int ps = 0; // 起始指针
		int pe = 0; // 终点指针
		for (int i = 0; i < str.length(); i++) {
			cur_width += g.getFont().charWidth(str.charAt(i));
			if (str.charAt(i) == cf) {
				pe = i - 1;
				line_buf = str.substring(ps, pe + 1);
				ps = pe = i + 1;
				cur_width = 0;
			} else if (cur_width > max_width) {
				i--;
				pe = i;
				line_buf = str.substring(ps, pe + 1);
				cur_width = 0;
				pe = ps = i + 1;
			} else if (i == str.length() - 1) {
				pe = i;
				line_buf = str.substring(ps, pe + 1);
				cur_width = 0;
			}
			if (line_buf != null) {
				pushClip(g);
				g.setClip(x, y, w, h);
				switch (anchor) {
				case dG.ANCHOR_HT:
					sx = x + (w >> 1);
					if (bgColor >= 0) {
						g.setColor(bgColor);
						g.drawString(line_buf, sx + 1, sy + counter, anchor);
						g.drawString(line_buf, sx - 1, sy + counter, anchor);
						g.drawString(line_buf, sx, sy + 1 + counter, anchor);
						g.drawString(line_buf, sx, sy - 1 + counter, anchor);
					}
					g.setColor(color);
					g.drawString(line_buf, sx, sy + counter, anchor);
					break;
				case dG.ANCHOR_LT:
					if (bgColor >= 0) {
						g.setColor(bgColor);
						g.drawString(line_buf, sx + 1, sy + counter, anchor);
						g.drawString(line_buf, sx - 1, sy + counter, anchor);
						g.drawString(line_buf, sx, sy + 1 + counter, anchor);
						g.drawString(line_buf, sx, sy - 1 + counter, anchor);
					}
					g.setColor(color);
					g.drawString(line_buf, sx, sy + counter, anchor);
					break;
				}
				sy += g.getFont().getHeight() + space;
				total_height = sy;
				line_buf = null;
				popClip(g);
			}
		}

		return total_height - y;
	}

	/**
	 * 用block给定的区域绘制滚屏文本
	 * 
	 * @param g
	 *            Graphics
	 * @param str
	 *            String
	 * @param counter
	 *            int
	 * @param block
	 *            int[]
	 * @param anchor
	 *            int
	 * @return int
	 */
	public static int drawVScrollString(Graphics g, String str, int counter,
			Rect r, int anchor, int color, int bgColor) {
		return drawVScrollString(g, str, counter, r.x0, r.y0, r.w, r.h, anchor,
				color, bgColor);
	}

	/**
	 * 在指定的block中以指定anchor绘制字符串
	 * 
	 * @param g
	 *            Graphics
	 * @param s
	 *            String
	 * @param block
	 *            int[]
	 * @param anchor
	 *            int
	 */
	// public static void drawStringIn (Graphics g, String s, Rect r, int
	// anchor) {
	// if(s != null) {
	// short[] p = r.getXY (anchor);
	// g.drawString (s, p[0], p[1], anchor);
	// }
	// }

	/**
	 * 重字体
	 * 
	 * @param g
	 *            Graphics
	 * @param str
	 *            String 内容
	 * @param x
	 *            int 绘制位置X
	 * @param y
	 *            int 绘制位置Y
	 * @param color1
	 *            int 主颜色
	 * @param color2
	 *            int 边颜色
	 */
	public static final void drawArtFont(Graphics g, String str, int x, int y,
			int anchor, int mainColor, int edgeColor) {
		pushCC(g);
		if (anchor == (Graphics.HCENTER | Graphics.BASELINE)) {
			y += dG.SF_DELTA_BASELINE;
		}
		g.setColor(edgeColor);
		g.drawString(str, x - 1, y, anchor);
		g.drawString(str, x + 1, y, anchor);
		g.drawString(str, x, y - 1, anchor);
		g.drawString(str, x, y + 1, anchor);
		g.setColor(mainColor);
		g.drawString(str, x, y, anchor);
		popCC(g);
	}

	/**
	 * 画透明矩形
	 * 
	 * @param beginX
	 *            int 开始点X
	 * @param beginY
	 *            int 开始点Y
	 * @param width
	 *            int 宽
	 * @param height
	 *            int 高
	 * @param g
	 *            Graphics
	 * @param argp
	 *            int ARGB值
	 */
	public static final void fillPolygon(Graphics g, int x, int y, int w,
			int h, int alphaColor) {
		// #if motoAPI
		// # int preColor = g.getColor ();
		// # g.setColor ( alphaColor );
		// # g.fillRect ( x , y , w , h );
		// # g.setColor ( preColor );
		// #else
		// nokia

		int a[] = { x, x + w, x + w, x };
		int b[] = { y, y, y + h, y + h };
		com.nokia.mid.ui.DirectUtils.getDirectGraphics(g).fillPolygon(a, 0, b,
				0, 4, alphaColor);
		// #endif
	}

	/**
	 * 
	 * @param g
	 *            Graphics
	 * @param b
	 *            int[]
	 * @param cs
	 *            int
	 * @param ce
	 *            int
	 * @param bH
	 *            boolean
	 */
	/*
	 * public static final void drawShadowRect ( Graphics gr , int[] bl , int cs
	 * , int ce , boolean bVertical ) { final int GAPE = 1; final int RATE = 10;
	 * //颜色分量 int a , r , g , b; int a0 , r0 , g0 , b0; a = ( cs >> 24 ) & 0xff;
	 * r = ( cs >> 16 ) & 0xff; g = ( cs >> 8 ) & 0xff; b = ( cs >> 0 ) & 0xff;
	 * a0 = ( ce >> 24 ) & 0xff; r0 = ( ce >> 16 ) & 0xff; g0 = ( ce >> 8 ) &
	 * 0xff; b0 = ( ce >> 0 ) & 0xff;
	 * 
	 * int x , y , w , h; x = bl[1]; y = bl[2]; w = bl[3]; h = bl[4];
	 * 
	 * int c; int rate = ( 1 << RATE ); int num = ( bVertical ? h : w ) / GAPE;
	 * // int dr = rate / num; dr /= 6; //颜色补充 if ( dr <= 0 ) { dr = 1; } //绘制
	 * for ( int i = 0; i < num; i++ ) { a = ( ( a - a0 ) * rate + ( a0 << RATE
	 * ) ) >> RATE; r = ( ( r - r0 ) * rate + ( r0 << RATE ) ) >> RATE; g = ( (
	 * g - g0 ) * rate + ( g0 << RATE ) ) >> RATE; b = ( ( b - b0 ) * rate + (
	 * b0 << RATE ) ) >> RATE; c = ( ( a << 24 ) & 0xff000000 ) | ( ( r << 16 )
	 * & 0x00ff0000 ) | ( ( g << 8 ) & 0x0000ff00 ) | ( ( b << 0 ) & 0x000000ff
	 * ); gr.setColor ( c ); gr.fillRect ( x , y , bVertical ? w : GAPE ,
	 * bVertical ? GAPE : h ); if ( rate > 0 ) { rate -= dr; } if ( bVertical )
	 * { y += GAPE; } else { x += GAPE; } } }
	 */
	/**
	 * 画图片数字 规格：0123456789:-/+*#
	 * 
	 * @param g
	 *            Graphics
	 * @param str
	 *            String
	 * @param x
	 *            int
	 * @param y
	 *            int
	 */
	public static final void drawImageNumber(Graphics g, Image img, String str,
			Rect r, int anchor) {
		pushClip(g);
		int cw = img.getWidth() / 16; // 每个数字图片的尺寸
		int ch = img.getHeight();
		int x, y; /* 是右上点坐标 */
		int xx; // 变动的 是每个数字图的左上点位置

		x = r.x0 + r.w - 1;
		y = r.y0;
		y += r.h - ch;
		for (int j = str.length() - 1; j >= 0; j--) {
			int pos = 0;
			switch (str.charAt(j)) {
			case ':':
			case '：':
				pos = 10;
				break;
			case '-':
				pos = 11;
				break;
			case '/':
				pos = 12;
				break;
			case '+':
				pos = 13;
				break;
			case '*':
				pos = 14;
				break;
			case '#':
				pos = 15;
				break;
			case ' ':
				continue;
			default:
				pos = str.charAt(j) - 0x30;
				break;
			}
			xx = x - (str.length() - j) * cw + 1
			/* 特殊情况: 美工给的图是5*8, 没有间隔, +1 是间隔 */;
			g.setClip(xx, y, cw, ch);
			g.drawImage(img, xx - pos * cw, y, Graphics.LEFT | Graphics.TOP);
		}
		popClip(g);
	}

	private static short iDHS = 0; // 用于scrollStringInRect 方法中 文字滚动效果
	private static String sDHS = ""; // 标示先前显示的字符串

	/**
	 * 在指定的区域中以滚动的方式绘制字符串
	 * 
	 * @param g
	 *            Graphics
	 * @param str
	 *            String
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param w
	 *            int
	 * @param h
	 *            int
	 */
	public static final void drawHScrollString(Graphics g, String str, Rect r,
			int color, int BGcolor) {
		if (str == null || str == "") {
			return;
		}
		if (!sDHS.equals(str)) {
			iDHS = (short) -r.w;
			sDHS = str;
		}
		pushClip(g);
		g.setClip(r.x0, r.y0, r.w - 3, r.h);
		if (BGcolor >= 0) {
			g.setColor(BGcolor);
			g.drawString(str, r.x0 - iDHS + 1, r.y0 + (r.h >> 1), dG.ANCHOR_LT);
			g.drawString(str, r.x0 - iDHS - 1, r.y0 + (r.h >> 1), dG.ANCHOR_LT);
			g.drawString(str, r.x0 - iDHS, r.y0 + (r.h >> 1) + 1, dG.ANCHOR_LT);
			g.drawString(str, r.x0 - iDHS, r.y0 + (r.h >> 1) - 1, dG.ANCHOR_LT);
		}
		g.setColor(color);
		g.drawString(str, r.x0 - iDHS, r.y0 + (r.h >> 1), dG.ANCHOR_LT);
		iDHS += 5;
		if (iDHS > g.getFont().stringWidth(str)) {
			iDHS = (short) -r.w;
		}
		popClip(g);
	}

}
