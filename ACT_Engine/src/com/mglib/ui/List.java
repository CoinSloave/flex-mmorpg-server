package com.mglib.ui;

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
 * @author liangho chen
 * @version 1.0
 */
public class List {
	String name;
	String head;
	int capcity;
	int size;
	int logCol;
	int paintCol;

	int si; // ��Ļ����
	int curi; // ��ǰ������
	int dsi; // ������Ŀ�ʼ
	int dei; // ������Ľ�ֹ

	public List(String name, String head, int capcity, int size, int logCol,
			int paintCol) {
		this.name = name;
		this.head = head;
		this.capcity = capcity;
		this.size = size;
		this.logCol = logCol;
		this.paintCol = paintCol;
		//
		if (this.name == null || this.name == "") {
			this.name = "ListDefaultName";
		}
		if (this.head == null) {
			this.head = "";
		}
		init();
	}

	private void init() {
		if (isEmpty()) {
			si = -1;
			curi = -1;
			dsi = -1;
			dei = -1;
			return;
		}
		si = 0;
		curi = 0;
		dsi = 0;
		dei = (size > capcity) ? capcity - 1 : size - 1;
	}

	/**
	 * ����һ��ʱ�͸���һ��
	 * 
	 * @param s
	 *            int
	 */
	public void update(int size) {
		if (this.size == size) {
			return;
		}

		boolean isLess = this.size <= this.capcity;
		boolean isDec = this.size > size;
		this.size = size;
		if (size <= 0) {
			init();
			return;
		}

		if (isDec) {
			if (isLess) {
				if (curi > 0) {
					curi--;
				}
				if (si > 0) {
					si--;
				}
				dei = size - 1;
			} else if (dsi != 0) // ���ڶ���
			{
				curi--;
				dsi--;
				dei--;
			}
		}
	}

	private boolean needUpdateDrawPos() {
		return size > capcity;
	}

	public boolean isEmpty() {
		if (size <= 0) {
			return true;
		}
		return false;
	}

	public int getScreenIndex() {
		return si;
	}

	public int getSelectedIndex() {
		return curi;
	}

	public int getDrawStartIndex() {
		return dsi;
	}

	public int getDrawEndIndex() {
		return dei;
	}

	public int getSize() {
		return size;
	}

	public boolean down() {
		if (isEmpty()) {
			return false;
		}
		if (curi < size - 1) {
			curi++;
			if (si < capcity - 1) {
				si++;
			} else if (needUpdateDrawPos()) // ���Ʒ�Χ�ı�
			{
				dsi++;
				dei++;
			}
		} else // �������β���򷵻ص�����
		{
			curi = 0;
			si = 0;
			// ���Ʒ�Χ�ı�
			if (needUpdateDrawPos()) {
				dsi = 0;
				dei = (size > capcity) ? capcity - 1 : size - 1;
			}
		}
		return true;
	}

	public boolean up() {
		if (isEmpty()) {
			return false;
		}
		if (curi > 0) {
			curi--;
			if (si > 0) {
				si--;
			} else if (needUpdateDrawPos()) // ���Ʒ�Χ�ı�
			{
				dsi--;
				dei--;
			}
		} else // ������˶����򷵻ص�β��
		{
			curi = size - 1;
			si = (size > capcity) ? capcity - 1 : size - 1;
			// ���Ʒ�Χ�ı�
			if (needUpdateDrawPos()) {
				dsi = size - capcity;
				dei = size - 1;
			}
		}
		return true;
	}

	public void drawList(Graphics g, String[] strLst, int[] b) {
		if (isEmpty()) {
			// ���Ϊ����ʾʲô��
			// to do
			g.setColor(0xff0000);
			g.drawString("��", b[1] + (b[3] >> 1), b[2] + (b[4] >> 1), 0);
			return;
		} else {
			int dsi = getDrawStartIndex();
			int dei = getDrawEndIndex();
			int si = getScreenIndex();
			int curi = getSelectedIndex();
			for (int c = 0, i = dsi; i <= dei; i++, c++) {
				if (c == si) {
					// ��Ļ��ꣿ
					// to do
					g.setColor(0);
					g.fillRect(b[1], b[2] + g.getFont().getHeight() * c, b[3],
							g.getFont().getHeight());
				}
				if (i == curi) {
					// ѡ�����ꣿ
					// to do
					g.setColor(0xff0000);
				} else {
					// �����ɫ��
					// to do
					g.setColor(0xffffff);
				}
				g.drawString(strLst[i], b[1], b[2] + g.getFont().getHeight()
						* c, 0);
			}
		}
	}

}
