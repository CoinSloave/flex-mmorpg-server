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

	int si; // 屏幕索引
	int curi; // 当前项索引
	int dsi; // 绘制项的开始
	int dei; // 绘制项的截止

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
	 * 减少一项时就更新一下
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
			} else if (dsi != 0) // 不在顶端
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
			} else if (needUpdateDrawPos()) // 绘制范围改变
			{
				dsi++;
				dei++;
			}
		} else // 如果到了尾巴则返回到顶端
		{
			curi = 0;
			si = 0;
			// 绘制范围改变
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
			} else if (needUpdateDrawPos()) // 绘制范围改变
			{
				dsi--;
				dei--;
			}
		} else // 如果到了顶端则返回到尾巴
		{
			curi = size - 1;
			si = (size > capcity) ? capcity - 1 : size - 1;
			// 绘制范围改变
			if (needUpdateDrawPos()) {
				dsi = size - capcity;
				dei = size - 1;
			}
		}
		return true;
	}

	public void drawList(Graphics g, String[] strLst, int[] b) {
		if (isEmpty()) {
			// 如果为则显示什么？
			// to do
			g.setColor(0xff0000);
			g.drawString("空", b[1] + (b[3] >> 1), b[2] + (b[4] >> 1), 0);
			return;
		} else {
			int dsi = getDrawStartIndex();
			int dei = getDrawEndIndex();
			int si = getScreenIndex();
			int curi = getSelectedIndex();
			for (int c = 0, i = dsi; i <= dei; i++, c++) {
				if (c == si) {
					// 屏幕光标？
					// to do
					g.setColor(0);
					g.fillRect(b[1], b[2] + g.getFont().getHeight() * c, b[3],
							g.getFont().getHeight());
				}
				if (i == curi) {
					// 选中项光标？
					// to do
					g.setColor(0xff0000);
				} else {
					// 项的颜色？
					// to do
					g.setColor(0xffffff);
				}
				g.drawString(strLst[i], b[1], b[2] + g.getFont().getHeight()
						* c, 0);
			}
		}
	}

}
