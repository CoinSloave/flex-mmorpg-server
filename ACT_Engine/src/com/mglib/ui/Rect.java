package com.mglib.ui;

import com.mglib.def.dG;

public class Rect {
	short x0;
	short y0;
	short x1;
	short y1;
	short w;
	short h;

	public Rect(int x0, int y0, int w, int h) {
		this.x0 = (short) x0;
		this.y0 = (short) y0;
		this.x1 = (short) (x0 + w);
		this.y1 = (short) (y0 + h);

		this.w = (short) w;
		this.h = (short) h;
	}

	/**
	 * 根据指定的anchor在Rect中找到坐标点
	 * 
	 * @param anchor
	 *            int
	 * @return int[]
	 */
	public short[] getXY(int anchor) {
		short[] p = new short[2];
		p[0] = x0;
		p[1] = y0;
		switch (anchor) {
		case dG.ANCHOR_LT:
			break;
		case dG.ANCHOR_LV:
			p[1] += (h >> 1);
			break;
		case dG.ANCHOR_LB:
			p[1] += h;
			break;
		case dG.ANCHOR_HT:
			p[0] += (w >> 1);
			break;
		case dG.ANCHOR_HV:
			p[0] += (w >> 1);
			p[1] += (h >> 1);
			break;
		case dG.ANCHOR_HB_LINE:
			p[0] += (w >> 1);
			p[1] += (h >> 1) + dG.SF_DELTA_BASELINE;
			break;
		case dG.ANCHOR_HB:
			p[0] += (w >> 1);
			p[1] += h;
			break;
		case dG.ANCHOR_RT:
			p[0] += w;
			break;
		case dG.ANCHOR_RV:
			p[0] += w;
			p[1] += (h >> 1);
			break;
		case dG.ANCHOR_RB:
			p[0] += w;
			p[1] += h;
			break;
		}
		return p;
	}

}
