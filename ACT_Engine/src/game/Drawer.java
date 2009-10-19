package game;

import game.config.dConfig;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.mglib.ui.IDrawer;
import com.nokia.mid.ui.DirectGraphics;

/**
 * 
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
 * Company: mig
 * </p>
 * 
 * @author lianghao chen
 * @version 1.0
 */
public class Drawer implements IDrawer {
	public static IDrawer drawer;

	public static IDrawer getInstance() {
		if (drawer == null) {
			drawer = new Drawer();
		}
		return drawer;
	}

	private Drawer() {
	}

	// cooledit translate and flip paraments
	public static final int Cooledit_NONE = 0;
	public static final int Cooledit_CCW_ROTATE90 = 1;
	public static final int Cooledit_CCW_ROTATE180 = 2;
	public static final int Cooledit_CCW_ROTATE270 = 3;
	public static final int Cooledit_FLIP_HORIZONTAL = 4;
	public static final int Cooledit_FLIP_VERTICAL = 5;
	public static final int Cooledit_FLIP_HORIZONTAL__CCW_ROTATE270 = 6;
	public static final int Cooledit_FLIP_HORIZONTAL__CCW_ROTATE90 = 7;

	public static final int[] COOLEDIT_2_MIDP2 = { 0, 6, 3, 5, 2, 1, 7, 4, };
	public static final int[] COOLEDIT_2_NOKIA = { 0, 90, 180, 270, 8192,
			16384, 90 | 8192, 270 | 8192, };

	/**
	 * 绘制器
	 * 
	 * @param g
	 *            Graphics
	 * @param image
	 *            Image
	 * @param sX
	 *            int
	 * @param sY
	 *            int
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param showX
	 *            int
	 * @param showY
	 *            int
	 * @param anchor
	 *            int
	 * @param type
	 *            int
	 */
	public void drawImage(Graphics g, Image image, int sX, int sY, int width,
			int height, int showX, int showY, int anchor, int type) {
		// #if nokiaAPI
		com.nokia.mid.ui.DirectGraphics dg = com.nokia.mid.ui.DirectUtils
				.getDirectGraphics(g);
		switch (type) {
		case Cooledit_NONE:
			g.setClip(showX, showY, width, height);
			g.drawImage(image, showX - sX, showY - sY, anchor);
			break;
		case Cooledit_CCW_ROTATE90:
			g.setClip(showX, showY, height, width);
			dg.drawImage(image, showX - sY, showY
					- (image.getWidth() - sX - width), anchor,
					DirectGraphics.ROTATE_90);
			break;
		case Cooledit_CCW_ROTATE180:
			g.setClip(showX, showY, width, height);
			dg.drawImage(image, showX - (image.getWidth() - sX - width), showY
					- (image.getHeight() - sY - height), anchor,
					DirectGraphics.ROTATE_180);
			break;
		case Cooledit_CCW_ROTATE270:
			g.setClip(showX, showY, height, width);
			dg.drawImage(image, showX - (image.getHeight() - sY - height),
					showY - sX, anchor, DirectGraphics.ROTATE_270);
			break;
		case Cooledit_FLIP_HORIZONTAL:
			g.setClip(showX, showY, width, height);
			dg.drawImage(image, showX - (image.getWidth() - sX - width), showY
					- sY, anchor, DirectGraphics.FLIP_HORIZONTAL); // - -
			break;
		case Cooledit_FLIP_VERTICAL:
			g.setClip(showX, showY, width, height);
			dg.drawImage(image, showX - sX, showY
					- (image.getHeight() - sY - height), anchor,
					DirectGraphics.FLIP_VERTICAL);
			break;
		case Cooledit_FLIP_HORIZONTAL__CCW_ROTATE270: // 先水平镜像后逆旋转270
			g.setClip(showX, showY, height, width);
			dg.drawImage(image, showX - (image.getHeight() - sY - height),
					showY - (image.getWidth() - sX - width), anchor,
					0x5A | 0x2000);
			break;
		case Cooledit_FLIP_HORIZONTAL__CCW_ROTATE90: // 先水平镜像逆后旋转90
			g.setClip(showX, showY, height, width);
			dg.drawImage(image, showX - sY, showY - sX, anchor, 0x10E | 0x2000);
			break;
		}
		dg = null;
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		// #else
		// # type = COOLEDIT_2_MIDP2[type];
		// # if ( sX < 0 )
		// # {
		// # sX = 0;
		// # }
		// # if ( sY < 0 )
		// # {
		// # sY = 0;
		// # }
		// # if ( sX + width > image.getWidth () )
		// # {
		// # width -= sX + width - image.getWidth ();
		// # }
		// # if ( sY + height > image.getHeight () )
		// # {
		// # height -= sY + height - image.getHeight ();
		// # }
		// #
		// # g.setClip ( 0 , 0 , MapDraw.m_bufWidth , MapDraw.m_bufHeight );
		// # g.drawRegion ( image, sX, sY, width, height, type, showX, showY,
		// anchor );
		// #
		// #endif
	}

}
