package game;

import game.config.dConfig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.mglib.mdl.map.MapData;
import com.mglib.ui.UITools;
import com.nokia.mid.ui.DirectUtils;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class CTools {
	static Random m_random = new Random();

	public CTools() {
	}

	/**
	 * 载图片资源
	 * 
	 * @param pictureName
	 *            String 资源名字
	 * @return Image
	 */
	public static final Image loadImage(String pictureName) {
		Image img = null;
		try {
			img = Image.createImage("/bin/" + pictureName + ".png");
		} catch (Exception e) {
		}
		return img;
	}

	/**
	 * 获得某个范围内的随机数
	 * 
	 * @param lower
	 *            int
	 * @param upper
	 *            int
	 * @return int 范围：[lower,upper]
	 */
	public static int random(int lower, int upper) {
		return (Math.abs(m_random.nextInt()) % (upper - lower + 1)) + lower;
	}

	// ---------------------一些公用的数学函数-------------------------------------

	// 三角函数运算
	/**
	 * arctan ( y / x )
	 * 
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @return int an int angle value in range [0, 256), which corresponds the
	 *         real angle range of [0, 360)
	 */
	public static int arcTan(int x, int y) {
		if (x == 0 && y == 0) {
			return 0;
		}

		int abs_x = x >= 0 ? x : -x;
		int abs_y = y >= 0 ? y : -y;

		int angle;
		if (abs_x >= abs_y) {
			int tan = (abs_y << dConfig.MATH_DATA_FRACTION_BITS) / abs_x;
			angle = findAngle(tan);
		} else {
			int ctan = (abs_x << dConfig.MATH_DATA_FRACTION_BITS) / abs_y;
			angle = findAngle(ctan);
			angle = 64 - angle;
		}

		return x >= 0 ? (y >= 0 ? angle : 256 - angle) : (y >= 0 ? 128 - angle
				: 128 + angle);
	}

	/**
	 * arctan ( tanValue )
	 * 
	 * @param tanValue
	 *            int a tan value, must be a non-nagetive
	 * @return int an int angle value in range [0, 64], which corresponds the
	 *         real angle range of [0, 90]
	 */
	public static int findAngle(int tanValue) {
		int front = 0;
		int end = 32;
		int half = 16;
		int half_len = 16;

		while (half_len > 0) {
			half_len >>= 1;
			if (dConfig.m_tanTable[half] > tanValue) {
				end = half;
				half -= half_len;
			} else {
				front = half;
				half += half_len;
			}
		}
		return dConfig.m_tanTable[end] - tanValue < tanValue
				- dConfig.m_tanTable[front] ? end : front;
	}

	/**
	 * len * cos ( angle )
	 * 
	 * @param len
	 *            int
	 * @param angle
	 *            int an int angle value in range [0, 256), which corresponds
	 *            the real angle range of [0, 360)
	 * @return int
	 */
	public static int lenCos(int len, int angle) {
		// if ( CDebug.bEnableAssert )
		// {
		// CDebug._assert( angle >= 0 && angle < 256, "Invalid angle in lenCos"
		// );
		// }

		int a = angle;
		if (a >= 128) {
			a = 256 - a;
		}
		if (a > 64) {
			a = 128 - a;
		}
		int len_cos = (len * dConfig.m_cosTable[a]) >> dConfig.MATH_DATA_FRACTION_BITS;
		return angle <= 64 || angle >= 192 ? len_cos : -len_cos;

	}

	/**
	 * len * shin ( angle )
	 * 
	 * @param len
	 *            int
	 * @param angle
	 *            int an int angle value in range [0, 256), which corresponds
	 *            the real angle range of [0, 360)
	 * @return int
	 */
	public static int lenSin(int len, int angle) {
		// if ( CDebug.bEnableAssert )
		// {
		// CDebug._assert( angle >= 0 && angle < 256, "Invalid angle in lenCos"
		// );
		// }

		int a = angle;
		if (a >= 128) {
			a = 256 - a;
		}
		if (a > 64) {
			a = 128 - a;
		}
		int len_sin = (len * dConfig.m_sinTable[a]) >> dConfig.MATH_DATA_FRACTION_BITS;
		return angle <= 128 ? len_sin : -len_sin;

	}

	/**
     *
     */
	public final static int SIN = 0;
	public final static int COS = 1;

	public static int sc(int op, int dir) {
		int sign = 1;
		while (dir > 360) {
			dir -= 360;
		}
		if (dir >= 180) {
			dir -= 180;
			sign = -1;
		}
		if (dir > 90) {
			dir = 180 - dir;
			if (op == COS) {
				sign *= -1;
			}
		}
		if (op == SIN) {
			dir = 90 - dir;
		}
		return sign * (8100 - dir * dir); // use parabola to simulate sine curve
	}

	/**
	 * 取得两点间距离的方法
	 * 
	 * @param dx
	 *            int
	 * @param dy
	 *            int
	 * @return int
	 */
	public static int getDistance(int dx, int dy) {
		int abs_dx = dx >= 0 ? dx : -dx;
		int abs_dy = dy >= 0 ? dy : -dy;
		int distance;
		distance = abs_dx > abs_dy ? abs_dy : abs_dx;
		distance = abs_dx + abs_dy - (distance >> 1) - (distance >> 2)
				+ (distance >> 3); // approximate
		return distance;
	}

	/**
	 * 检查两个box是否相交 发生碰撞
	 * 
	 * @param box1
	 * @param box2
	 * @return if the 2 boxes are intersected with each other
	 */
	public static boolean isIntersecting(short[] box1, short[] box2) {
		return box1[0] != box1[2] && box2[0] != box2[2] && box1[0] <= box2[2]
				&& box1[2] >= box2[0] && box1[1] <= box2[3]
				&& box1[3] >= box2[1];
	}

	/**
	 * 把box中的相对坐标转变成绝对坐标
	 * 
	 * @param box
	 *            short[] 将要坐标转化的数组
	 * @param dx
	 *            int
	 * @param dy
	 *            int
	 */
	public final static void shiftBox(short[] box, int dx, int dy) {
		box[0] += dx;
		box[2] += dx;
		box[1] += dy;
		box[3] += dy;
	}

	/**
	 * 
	 * 
	 * @param g
	 *            Graphics
	 * @param color
	 *            int
	 * @param startY
	 *            int
	 * @param height
	 *            int
	 */
	public static void fillBackGround(Graphics g, int color, int startY,
			int height) {
		g.setClip(0, startY, dConfig.S_WIDTH, height);
		g.setColor(color);
		g.fillRect(0, startY, dConfig.S_WIDTH, height);
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
	}

	public static boolean isContaining(short[] box, int pointX, int pointY) {
		return box[0] <= pointX && box[2] >= pointX && box[1] <= pointY
				&& box[3] >= pointY;
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
		// #if nokiaAPI
		int a[] = { x, x + w, x + w, x };
		int b[] = { y, y, y + h, y + h };
		// DirectGraphics gg=DirectUtils.getDirectGraphics(g);
		DirectUtils.getDirectGraphics(g).fillPolygon(a, 0, b, 0, 4, alphaColor);
		// #else
		// # int preColor = g.getColor ();
		// # g.setColor ( alphaColor );
		// # g.fillRect ( x , y , w , h );
		// # g.setColor ( preColor );
		// #
		// #endif
	}

	/**
	 * 从字节数组的offset处开始读取len个字节形成一个int 返回
	 * 
	 * @param data
	 *            byte[]
	 * @param offset
	 *            int
	 * @param length
	 *            int
	 * @return int
	 */
	public static final int readFromByteArray(byte[] data, int offset,
			int length) {
		int param = 0;
		int mbit = 0;
		for (int i = length - 1; i >= 0; i--) {
			mbit = (length - i - 1) << 3;
			param |= ((data[offset + i] << mbit) & (0xff << mbit));
		}
		switch (length) {
		case 1: //
			param = (byte) param;
			break;
		case 2: //
			param = (short) param;
			break;
		case 4: //
			param = param;
			break;
		}
		return param;
	}

	private static int scrollPos = 0; // 用于scrollStringInRect 方法中 文字滚动效果
	private static String strPrevious = ""; // 标示先前显示的字符串

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
	public static final void scrollStringInRect(Graphics g, String str, int x,
			int y, int w, int h, int color, int clrBg) {
		if (str == null || str == "") {
			return;
		}
		if (!strPrevious.equals(str)) {
			scrollPos = 0;
			strPrevious = str;
		}
		g.setClip(x, y, w - 3, h);
		CTools.afficheSmall(g, str, x - scrollPos, y, dConfig.ANCHOR_LT, color,
				clrBg);
		// g.drawString(str , x - scrollPos , y , dConfig.ANCHOR_LT);
		scrollPos += 6;
		if (scrollPos > dConfig.F_SMALL_DEFAULT.stringWidth(str)) {
			scrollPos = -w;
		}
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
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
	public static final void drawShadowRect(Graphics gr, int[] bl, int cs,
			int ce, boolean bVertical) {
		final int GAPE = 1;
		final int RATE = 10;
		// 颜色分量
		int a, r, g, b;
		int a0, r0, g0, b0;
		a = (cs >> 24) & 0xff;
		r = (cs >> 16) & 0xff;
		g = (cs >> 8) & 0xff;
		b = (cs >> 0) & 0xff;
		a0 = (ce >> 24) & 0xff;
		r0 = (ce >> 16) & 0xff;
		g0 = (ce >> 8) & 0xff;
		b0 = (ce >> 0) & 0xff;

		int x, y, w, h;
		x = bl[1];
		y = bl[2];
		w = bl[3];
		h = bl[4];

		int c;
		int rate = (1 << RATE);
		int num = (bVertical ? h : w) / GAPE;
		//
		int dr = rate / num;
		dr /= 6; // 颜色补充
		if (dr <= 0) {
			dr = 1;
		}
		// 绘制
		for (int i = 0; i < num; i++) {
			a = ((a - a0) * rate + (a0 << RATE)) >> RATE;
			r = ((r - r0) * rate + (r0 << RATE)) >> RATE;
			g = ((g - g0) * rate + (g0 << RATE)) >> RATE;
			b = ((b - b0) * rate + (b0 << RATE)) >> RATE;
			c = ((a << 24) & 0xff000000) | ((r << 16) & 0x00ff0000)
					| ((g << 8) & 0x0000ff00) | ((b << 0) & 0x000000ff);
			gr.setColor(c);
			gr.fillRect(x, y, bVertical ? w : GAPE, bVertical ? GAPE : h);
			if (rate > 0) {
				rate -= dr;
			}
			if (bVertical) {
				y += GAPE;
			} else {
				x += GAPE;
			}
		}
	}

	/**
	 * union 2 boxes. the result is stored in the first box
	 * 
	 * @param box1
	 *            short[]
	 * @param box2
	 *            short[]
	 */
	public static void unionBox(short[] box1, short[] box2) {
		box1[0] = (box1[0] < box2[0] ? box1[0] : box2[0]);
		box1[1] = (box1[1] < box2[1] ? box1[1] : box2[1]);
		box1[2] = (box1[2] > box2[2] ? box1[2] : box2[2]);
		box1[3] = (box1[3] > box2[3] ? box1[3] : box2[3]);
	}

	/************************************
	 * 中心点画线法
	 * 
	 * @param g
	 *            Graphics
	 * @param r
	 *            int
	 * @param color
	 *            int
	 **************************************/
	public static void midpointLine(Graphics g, int x0, int y0, int x1, int y1,
			int color) {
		int a, b, delta1, delta2, d, x, y;
		a = y0 - y1;
		b = x1 - x0;
		d = 2 * a + b;
		delta1 = 2 * a;
		delta2 = 2 * (a + b);
		x = x0;
		y = y0;
		g.setColor(color);
		g.drawLine(x, y, x, y);
		while (x < x1) {
			if (d < 0) {
				x++;
				y++;
				d += delta2;

			} else {
				x++;
				d += delta1;
			}
			g.setColor(color);
			g.drawLine(x, y, x, y);
		}
	}

	/**
	 * 中心点画圆
	 * 
	 * @param g
	 *            Graphics
	 * @param r
	 *            int
	 * @param color
	 *            int
	 */
	public static void midpointCircle(Graphics g, int r, int color) {
		int x, y, d;
		x = 0;
		y = r;
		d = 1 - r;
		g.setColor(color);
		g.drawLine(x, y, x, y);
		g.drawLine(y, x, y, x);
		while (x < y) {
			if (d < 0) {
				d += 2 * x + 3;
				x++;
			} else {
				d += 2 * (x - y) + 5;
				x++;
				y--;
			}
			g.setColor(color);
			g.drawLine(x, y, x, y);
			g.drawLine(y, x, y, x);
			g.drawLine(x, x, y, y);
			g.drawLine(y, y, x, x);

		}

	}

	static public final int CS_TOP = 0x01;
	static public final int CS_BOTTOM = 0x02;
	static public final int CS_RIGHT = 0x04;
	static public final int CS_LEFT = 0x08;

	static public int compOutcode(int x, int y, int xmin, int ymin, int xmax,
			int ymax) {
		int code = 0;

		if (y > ymax) {
			code |= CS_TOP;
		}
		if (y < ymin) {
			code |= CS_BOTTOM;
		}
		if (x > xmax) {
			code |= CS_RIGHT;
		}
		if (x < xmin) {
			code |= CS_LEFT;
		}

		return code;
	}

	/**
	 * 取得点到线段的距离
	 * 
	 * @param x0
	 *            int
	 * @param y0
	 *            int
	 * @param x1
	 *            int
	 * @param y1
	 *            int
	 * @param x2
	 *            int
	 * @param y2
	 *            int
	 * @return int
	 */
	static public int distPointToLine(int x0, int y0, int x1, int y1, int x2,
			int y2) {
		// get the line formula: Ax + By + C = 0
		// this line is linked by (x1, y1) and (x2, y2)
		int A = y2 - y1;
		int B = x1 - x2;
		int C = x2 * y1 - x1 * y2;
		// the distance: abs(A*x0 + B*y0 + C) / sqrt(A*A + B*B)
		if (A == 0 && B == 0) {
			return fastDistance(x1 - x0, y1 - y0);
		} else {
			return (int) (Math.abs((long) (A * x0 + B * y0 + C)) / fastDistance(
					A, B));
		}
	}

	static public int fastDistance2(int x, int y) {
		// this function computes the distance from 0,0 to x,y with 3.5% error
		// first compute the absolute value of x,y
		if (x < 0) {
			x = -x;
		}
		if (y < 0) {
			y = -y;
		}

		// compute the minimum of x,y
		int mn = x;
		if (y < x) {
			mn = y;
		}

		// return the distance
		return ((x + y) - (mn >> 1) - (mn >> 2) + (mn >> 3));
	}

	static public int fastDistance(int dx, int dy) {
		// int shift = 1;
		// if(Math.abs(dx) > 10000 || Math.abs(dy) > 10000)
		// shift = 8;
		// if(true) return (int)(Math.sqrt((dx >> shift) * (dx >> shift) + (dy
		// >> shift) * (dy >> shift))) << shift;

		int min, max;

		if (dx < 0) {
			dx = -dx;
		}
		if (dy < 0) {
			dy = -dy;
		}

		if (dx < dy) {
			min = dx;
			max = dy;
		} else {
			min = dy;
			max = dx;
		}

		// coefficients equivalent to ( 123/128 * max ) and ( 51/128 * min )
		return (((max << 8) + (max << 3) - (max << 4) - (max << 1) + (min << 7)
				- (min << 5) + (min << 3) - (min << 1)) >> 8);
	}

	/**
	 * Cohen Sutherland line clipping algorithm
	 * 
	 * @param x0
	 *            , y0, x1, y1: two point of the segment
	 * @param xmin
	 *            : Rectangle left
	 * @param ymin
	 *            : Rectangle top
	 * @param xmax
	 *            : Rectangle right
	 * @param ymax
	 *            : Rectangle bottom
	 * @return
	 */
	public static boolean cohen_LineClipping(int x0, int y0, int x1, int y1,
			int xmin, int ymin, int xmax, int ymax) {
		int outcode0, outcode1, outcode;
		int x = 0, y = 0;

		outcode0 = compOutcode(x0, y0, xmin, ymin, xmax, ymax);

		outcode1 = compOutcode(x1, y1, xmin, ymin, xmax, ymax);

		while (true) {

			if ((outcode0 | outcode1) == 0) {
				return true;
			}

			if ((outcode0 & outcode1) != 0) {
				return false;
			}

			outcode = (outcode0 != 0) ? outcode0 : outcode1;

			if ((outcode & CS_TOP) != 0) {
				x = x0 + (x1 - x0) * (ymax - y0) / (y1 - y0);
				y = ymax;
			} else {
				if ((outcode & CS_BOTTOM) != 0) {
					x = x0 + (x1 - x0) * (ymin - y0) / (y1 - y0);
					y = ymin;
				} else {
					if ((outcode & CS_RIGHT) != 0) {
						y = y0 + (y1 - y0) * (xmax - x0) / (x1 - x0);
						x = xmax;
					} else {
						if ((outcode & CS_LEFT) != 0) {
							y = y0 + (y1 - y0) * (xmin - x0) / (x1 - x0);
							x = xmin;
						}
					}
				}
			}

			if (outcode == outcode0) {
				x0 = x;
				y0 = y;
				outcode0 = compOutcode(x0, y0, xmin, ymin, xmax, ymax);
			} else {
				x1 = x;
				y1 = y;
				outcode1 = compOutcode(x1, y1, xmin, ymin, xmax, ymax);
			}
		}
	}

	public int getRand(int min, int max) {
		if (min == max) {
			return min;
		}

		return Math.abs(m_random.nextInt()) % (max - min) + min;
	}

	public int getRandNeg(int min, int max) {
		if (getRand(0, 2) == 0) {
			return getRand(min, max);
		} else {
			return -getRand(min, max);
		}
	}

	/**
	 * 画图片数字
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
			int[] block) {
		int widthANumber = img.getWidth() / 10; // 每个数字图片的尺寸
		int heightANumber = img.getHeight();
		int x, y; /* 是右上点坐标 */
		int xx; // 变动的 是每个数字图的左上点位置
		x = block[1];
		y = block[2];
		for (int j = 0; j < str.length(); j++) {
			int pos = 0;
			switch (str.charAt(j)) {
			// case '+':
			// pos = 0;
			// break;
			// case '-':
			// pos = 13;
			// break;
			// case '/':
			// pos = 2;
			// break;
			// case ' ':
			// continue;
			default:
				pos = str.charAt(j) - 0x30;
				break;
			}
			g.setClip(x + j * widthANumber, y, widthANumber, heightANumber);
			g.drawImage(img, x + j * widthANumber - pos * widthANumber, y,
					Graphics.LEFT | Graphics.TOP);
		}
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
	}

	static final String TEXT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	static final int TEXT_0 = TEXT.indexOf('0');
	static final int TEXT_A = TEXT.indexOf('A');
	static final int TEXT_a = TEXT.indexOf('a');
	static final int TEXT_LEN = TEXT.length();

	static void drawChar(Graphics g, String str, int start, int end, int x,
			int y) {

		int index = 0;

		char ch;
		for (int i = start; i < end; i++) {
			ch = str.charAt(i);
			if (ch >= 'a' && ch <= 'z') {
				index = ch - 'a' + TEXT_A;
			} else if (ch >= 'A' && ch <= 'Z') {
				index = ch - 'A' + TEXT_A;
			}
			if (ch >= '0' && ch <= '9') {
				index = ch - '0' + TEXT_0;
			} else {
				index = TEXT.indexOf(ch);
			}
			//
			if (index < 0) {
				index = 0;
			}

			// g.drawImage(s_fontImage[index] , x + i * fontWidth , y ,
			// dConfig.TOPLEFT);

		}

	}

	/**
	 * 保存short[]到指定的数据流
	 * 
	 * @param a
	 *            short[]
	 * @param dos
	 *            DataOutputStream
	 * @throws Exception
	 */
	public static final void saveArrayShort1(short[] a, DataOutputStream dos)
			throws Exception {
		int len = a.length;
		dos.writeShort(len);
		for (int i = 0; i < len; i++) {
			dos.writeShort(a[i]);
		}
	}

	/**
	 * 保存int[]到指定的数据流
	 * 
	 * @param a
	 *            int[]
	 * @param dos
	 *            DataOutputStream
	 * @throws Exception
	 */
	public static final void saveArrayInt1(int[] a, DataOutputStream dos)
			throws Exception {
		int len = a.length;
		dos.writeShort(len);
		for (int i = 0; i < len; i++) {
			dos.writeInt(a[i]);
		}
	}

	/**
	 * 保存short[][]到指定的数据流
	 * 
	 * @param a
	 *            short[][]
	 * @param dos
	 *            DataOutputStream
	 * @throws Exception
	 */
	public static final void saveArrayShort2(short[][] a, DataOutputStream dos)
			throws Exception {
		int len = a.length;
		dos.writeShort(len);
		for (int i = 0; i < len; i++) {
			dos.writeShort(a[i].length);
			for (int j = 0; j < a[i].length; j++) {
				dos.writeShort(a[i][j]);
			}
		}
	}

	/**
	 * 保存int[][]到指定的数据流
	 * 
	 * @param a
	 *            int[][]
	 * @param dos
	 *            DataOutputStream
	 * @throws Exception
	 */
	public static final void saveArrayInt2(int[][] a, DataOutputStream dos)
			throws Exception {
		int len = a.length;
		dos.writeShort(len);
		for (int i = 0; i < len; i++) {
			dos.writeShort(a[i].length);
			for (int j = 0; j < a[i].length; j++) {
				dos.writeInt(a[i][j]);
			}
		}
	}

	/**
	 * 从指定数据流读取short[]返回
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return short[]
	 */
	public static final short[] readArrayShort1(DataInputStream dis)
			throws Exception {
		int len = dis.readShort();
		short[] a = new short[len];
		for (int i = 0; i < len; i++) {
			a[i] = dis.readShort();
		}
		return a;
	}

	/**
	 * 从指定数据流读取int[]返回
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return int[]
	 */
	public static final int[] readArrayInt1(DataInputStream dis)
			throws Exception {
		int len = dis.readShort();
		int[] a = new int[len];
		for (int i = 0; i < len; i++) {
			a[i] = dis.readInt();
		}
		return a;
	}

	/**
	 * 从指定数据流读取short[][]返回
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return short[][]
	 */
	public static final short[][] saveArrayShort2(DataInputStream dis)
			throws Exception {
		int len = dis.readShort();
		short[][] a = new short[len][];
		for (int i = 0; i < len; i++) {
			a[i] = new short[dis.readShort()];
			for (int j = 0; j < a[i].length; j++) {
				a[i][j] = dis.readShort();
			}
		}
		return a;
	}

	/**
	 * 从指定数据流读取int[][]返回
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return int[][]
	 */
	public static final int[][] saveArrayInt2(DataInputStream dis)
			throws Exception {
		int len = dis.readShort();
		int[][] a = new int[len][];
		for (int i = 0; i < len; i++) {
			a[i] = new int[dis.readShort()];
			for (int j = 0; j < a[i].length; j++) {
				a[i][j] = dis.readInt();
			}
		}
		return a;
	}

	// -----------------------------------------------------------------------------------------------
	/**
	 * byge 转 short
	 * 
	 * @param a
	 *            byte
	 * @param b
	 *            byte
	 * @return short
	 */
	public static short combineShort(byte a, byte b) {
		return (short) ((a << 8) | (b & 0xff));
	}

	/**
	 * byte 转 int
	 * 
	 * @param a
	 *            byte
	 * @param b
	 *            byte
	 * @param c
	 *            byte
	 * @param d
	 *            byte
	 * @return int
	 */
	public static int combineInt(byte a, byte b, byte c, byte d) {
		return (((a & 0xff) << 24) | ((b & 0xff) << 16) | ((c & 0xff) << 8) | (d & 0xff));
	}

	/*
	 * 绘制描边字的方法
	 * 
	 * @param str String
	 * 
	 * @param x int
	 * 
	 * @param y int
	 * 
	 * @param clr int
	 * 
	 * @param clrBg int
	 * 
	 * @param anchor int
	 */
	public static void afficheSmall(Graphics g, String str, int x, int y,
			int anchor, int clr, int clrBg) {
		if (x == -1) {
			x = (dConfig.S_WIDTH - dConfig.F_SMALL_DEFAULT.stringWidth(str)) / 2;
		}
		// g.setFont (dConfig.F_SMALL_DEFAULT);
		if (clrBg >= 0) {
			g.setColor(clrBg);
			g.drawString(str, x + 1, y, anchor);
			g.drawString(str, x - 1, y, anchor);
			g.drawString(str, x, y + 1, anchor);
			g.drawString(str, x, y - 1, anchor);
		}
		g.setColor(clr);
		g.drawString(str, x, y, anchor);
	}

	/**
	 * 检测2点之间有没有阻挡
	 * 
	 * @param srcX
	 *            int
	 * @param srcY
	 *            int
	 * @param destX
	 *            int
	 * @param destY
	 *            int
	 * @return boolean
	 */
	public static boolean testConnectivity(int srcX, int srcY, int destX,
			int destY) {
		int dx = (destX - srcX);
		int dy = (destY - srcY);

		int steps = Math.max(Math.abs(dx / MapData.TILE_WIDTH), Math.abs(dy
				/ MapData.TILE_WIDTH)) << 1;

		steps = steps < 1 ? 1 : steps;

		int step_x = (dx << dConfig.FRACTION_BITS) / steps;
		int step_y = (dy << dConfig.FRACTION_BITS) / steps;

		srcX <<= dConfig.FRACTION_BITS;
		srcY <<= dConfig.FRACTION_BITS;

		for (int step = 0; step < steps; ++step) {
			srcX += step_x;
			srcY += step_y;
			int tileValue = CGame.gMap.mapRes.getTilePhyEnv(
					(srcX >> dConfig.FRACTION_BITS) / MapData.TILE_WIDTH,
					(srcY >> dConfig.FRACTION_BITS) / MapData.TILE_HEIGHT);
			// 这3种地图块阻挡视线
			if (tileValue == MapData.PHY_SOLID) {
				return false;
			}

		}

		return true;
	}

	/**
	 * 弹出框，带提示信息
	 * 
	 * @param g
	 *            Graphics
	 * @param str
	 *            String
	 * @param yes
	 *            String
	 * @param no
	 *            String
	 * @param color
	 *            int
	 * @param colorOuntline
	 *            int
	 */
	public static final void promptString(Graphics g, String message,
			String OkButton, String cancelButton, int color, int colorOuntline) {
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		CTools.fillPolygon(g, 10, dConfig.S_HEIGHT / 3, dConfig.S_WIDTH - 20,
				dConfig.S_HEIGHT / 3, 0x90FFFFFF);
		UITools.drawVScrollString(g, message, 0, 10, dConfig.S_HEIGHT / 3 + 10,
				dConfig.S_WIDTH - 20, dConfig.S_HEIGHT / 3 + 10,
				dConfig.ANCHOR_HT, color, colorOuntline);
		// CTools.afficheSmall(g,message, dConfig.SCREEN_W_HALF,
		// dConfig.SCREEN_H_HALF,dConfig.ANCHOR_HB,color,colorOuntline);

		if (OkButton != null) {
			// #if keymode_moto1||keymode_moto2||keymode_e680
			// # CTools.afficheSmall(g,OkButton, dConfig.S_WIDTH - 15,
			// dConfig.S_HEIGHT / 3 * 2,
			// # dConfig.ANCHOR_RB,color,colorOuntline);
			// #else
			CTools.afficheSmall(g, OkButton, 15, dConfig.S_HEIGHT / 3 * 2,
					dConfig.ANCHOR_LB, color, colorOuntline);
			// #endif
		}

		if (cancelButton != null) {
			// #if keymode_moto1||keymode_moto2||keymode_e680
			// # CTools.afficheSmall(g,cancelButton, 15, dConfig.S_HEIGHT / 3 *
			// 2, dConfig.ANCHOR_LB,
			// # color,colorOuntline);
			// #else
			CTools.afficheSmall(g, cancelButton, dConfig.S_WIDTH - 15,
					dConfig.S_HEIGHT / 3 * 2, dConfig.ANCHOR_RB, color,
					colorOuntline);
			// #endif
		}
	}

	/**
	 * 分割字符的方法
	 */
	public static Vector stringTokenVector = new Vector(1);

	static String[] breakLongMsg(String msg, Font font, int maxLineLen) {
		Vector lines = new Vector(10, 5);
		char c = '|';
		StringBuffer sbuf = new StringBuffer();
		int msgLen = msg.length();
		int i = 0;

		for (i = 0; i < msgLen; i++) {
			c = msg.charAt(i);
			if (c == '|' || c == '&') {
				// a new line
				lines.addElement(sbuf.toString());
				sbuf.delete(0, sbuf.length());
			} else {
				int len = font.stringWidth(sbuf.toString());
				if ((len + font.charWidth(c)) > maxLineLen) {
					// a new line
					lines.addElement(sbuf.toString());
					sbuf.delete(0, sbuf.length());
				}
				sbuf.append(c);
			}
		}
		if (i == msgLen && sbuf.length() > 0) {
			// the last line
			lines.addElement(sbuf.toString());
			sbuf = null;
		}
		// get the string array
		int size = lines.size();
		if (size > 0) {
			String[] result = new String[size];
			for (i = 0; i < size; i++) {
				result[i] = (String) lines.elementAt(i);
			}
			return result;
		} else {
			return null;
		}
	}

	public static void drawStringArray(String[] string, int x, int y, int col,
			int colbg, int anchor) {
		for (int i = 0; i < string.length; i++) {
			CTools.afficheSmall(CGame.m_g, string[i], x, y + dConfig.SF_HEIGHT
					* i, anchor, col, colbg);
		}
	}

	/**
	 * 一个点是否在矩形中
	 * 
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param rect
	 *            short[]
	 * @return boolean
	 */
	public static boolean isPointerInBox(int x, int y, short[] box) {
		return x >= box[0] && x <= box[2] && y >= box[1] && y <= box[3];
	}

}
