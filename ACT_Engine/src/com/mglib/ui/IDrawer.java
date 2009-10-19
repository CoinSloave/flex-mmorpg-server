package com.mglib.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public interface IDrawer {
	/*
	 * drawImage ��ָ��ͼƬ��ѡ�񲿷���ʾ����Ļָ��λ��
	 * 
	 * @Graphics g
	 * 
	 * @Image image
	 * 
	 * @int dx dy width height ָ��СͼƬ�ڴ�ͼƬ�е�λ������
	 * 
	 * @int showX showY СͼƬ����Ļ����ʾ��λ��
	 * 
	 * @int anchor
	 */
	void drawImage(Graphics g, Image image, int sX, int sY, int width,
			int height, int showX, int showY, int anchor, int type);

}
