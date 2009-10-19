package com.mglib.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public interface IDrawer {
	/*
	 * drawImage 从指定图片中选择部分显示到屏幕指定位置
	 * 
	 * @Graphics g
	 * 
	 * @Image image
	 * 
	 * @int dx dy width height 指定小图片在大图片中的位置区域
	 * 
	 * @int showX showY 小图片在屏幕中显示的位置
	 * 
	 * @int anchor
	 */
	void drawImage(Graphics g, Image image, int sX, int sY, int width,
			int height, int showX, int showY, int anchor, int type);

}
