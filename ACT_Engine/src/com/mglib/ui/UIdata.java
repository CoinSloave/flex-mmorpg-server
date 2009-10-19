package com.mglib.ui;

import game.CGame;
import game.config.dConfig;

import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.lcdui.Graphics;

import com.mglib.mdl.ContractionMLG;
import com.mglib.mdl.ani.AniData;
import com.mglib.mdl.ani.AniPlayer;

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
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class UIdata {
	public UIdata() {
		loadUIData(FILE_UIDATA);
		loadUIAni();
		UI_offset_X = (UI_WIDTH - dConfig.S_WIDTH) / 2;
		UI_offset_Y = (UI_HEIGHT - dConfig.S_HEIGHT) / 2;
	}

	public final static String FILE_UIDATA = "/bin/uiform.bin";
	public final static String FILE_UIANI = "/bin/uires.bin";

	public final static int UI_WIDTH = 176;
	public final static int UI_HEIGHT = 208;
	public static int UI_offset_X;
	public static int UI_offset_Y;
	private final static short B_TYPE = 0; // ����
	private final static short B_LAYER = 1; // ���
	private final static short B_ANIID = 2; // ����ID
	private final static short B_ACTID = 3; // ���� ID
	private final static short B_FRAMEID = 4; // frameID
	private final static short B_BGCOLOR = 5; // ������ɫ
	private final static short B_FBC = 6; // �߿���ɫ
	private final static short B_BS = 7; // bordersty0le
	private final static short B_TEXY_ID = 8; // �ı�ID
	private final static short B_X = 9; // x ����
	private final static short B_Y = 10; // y ����
	private final static short B_WIDTH = 11; // ���
	private final static short B_HEIGHT = 12; // �߶�
	private final static short B_ANCHOR = 13; // ê��
	private final static short B_SCROOLTYPE = 14; // �Ƿ��й���par
	private final static short B_SL_ANI = 15; // ѡ�еĶ���ID
	private final static short B_SL_ACITION = 16; // ѡ�еĶ���ID
	private final static short B_SL_FRAMEID = 17; // ѡ�е�frameID

	private final static short B_LENGTH = 18;

	// m_formData[form������][block������][block�ľ�������]
	public static short[][][] m_formData;
	public static String m_UIString[];
	public static short m_UIStringSign[][];

	public final static byte ANI_COUNTER = 20;

	public static AniData[] UIanimations;
	public static ContractionMLG[] UIaniMlgs;

	static {
		UIanimations = new AniData[ANI_COUNTER];
		UIaniMlgs = new ContractionMLG[ANI_COUNTER];
	}

	// public static int[] m_formID;
	// static {
	// m_formID = new int[3];
	// for (int i = 0; i < m_formID.length; i++) {
	// m_formID[i] = i;
	// }
	//
	//
	// // public static int m_curBlockID;
	//
	// }

	public final static void loadUIData(String fileName) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream("".getClass().getResourceAsStream(
					fileName));
			loadUIStringData(dis);
			loadFormData(dis);
			dis.close();
			dis = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void loadUIStringData(DataInputStream dis)
			throws IOException {
		int count = dis.readShort();
		m_UIString = new String[count];
		m_UIStringSign = new short[count][];
		for (int i = 0; i < count; i++) {
			int sc = dis.readShort();
			m_UIStringSign[i] = new short[sc];
			for (int j = 0; j < sc; j++) {
				m_UIStringSign[i][j] = dis.readShort();
			}
			String str = dis.readUTF();
			m_UIString[i] = str;
		}
	}

	private static void loadFormData(DataInputStream dis) throws IOException {
		short formCount = dis.readShort(); // form������
		m_formData = new short[formCount][][];
		for (int i = 0; i < formCount; i++) {
			short Blockcount = dis.readShort(); // block������
			m_formData[i] = new short[Blockcount][];

			for (int j = 0; j < Blockcount; j++) {
				m_formData[i][j] = new short[B_LENGTH];
				for (int k = 0; k < B_LENGTH; k++) {
					m_formData[i][j][k] = dis.readShort();
				}
				// System.out.println("---------------------" + j);
			}
		}
		int i = 0;
	}

	//
	// public static void setForm(int formID)
	// {
	// m_formID[formID] = formID;
	// }
	// UI ������������
	private static AniPlayer uianiPlayer;
	private static AniData UIaniData;
	public final static byte UI_ANIID_INGAME = 0;
	public static short UIanimationID;
	public static short UIactionID;

	// ----�µĲ�����������UI�еĶ���Ч�� add by lin 09.5.8
	private static AniPlayer uianiPlayer2;

	public static void loadUIAni() {
		if (UIanimations[UI_ANIID_INGAME] == null) {
			UIaniData = new AniData();
			AniData.loadAnimation(FILE_UIANI, new int[] { UI_ANIID_INGAME },
					UIanimations, UIaniMlgs);
			uianiPlayer = new AniPlayer(UIanimations[UI_ANIID_INGAME], 0, 0, 0);
			uianiPlayer2 = new AniPlayer(UIanimations[UI_ANIID_INGAME], 0, 0, 0);
		}
	}

	public static void destroyUIAni(int aniID) {
		// CGame.releaseAni(new int[]{aniID} , CGame.CGAnimations ,
		// CGame.CGaniMlgs);
	}

	// ----------------------------------------------------------------

	// ȡ������
	public static short getType(int formId, int blockIndex) {
		return m_formData[formId][blockIndex][B_TYPE];
	}

	// ȡ�û��Ʋ��
	public static short getLayerID(int formId, int blockIndex) {
		return m_formData[formId][blockIndex][B_LAYER];
	}

	// ȡ�ö���ID

	public static short getAniID(int formId, int blockIndex) {
		return m_formData[formId][blockIndex][B_ANIID];
	}

	public static short getActionID(int formId, int blockIndex) {
		return m_formData[formId][blockIndex][B_ACTID];
	}

	// ���ò��Ŷ���
	public static void setAniID(int formId, int blockIndex, short ANIID) {
		m_formData[formId][blockIndex][B_ANIID] = ANIID;
	}

	// ���ò��Ŷ���
	public static void setActionID(int formId, int blockIndex, short ActionID) {
		m_formData[formId][blockIndex][B_ACTID] = ActionID;
	}

	// ȡ�ñ�����ɫ
	public static int getBGcolor(int formId, int blockIndex) {
		if (m_formData[formId][blockIndex][B_BGCOLOR] <= 0) {
			return -1;
		}
		return dConfig.SD_COLOR_TABLE[m_formData[formId][blockIndex][B_BGCOLOR]];
	}

	// ȡ��
	public static short getBS(int formId, int blockIndex) {
		return m_formData[formId][blockIndex][B_BS];
	}

	// ȡ�ñ߿���ɫ
	public static int getFBC(int formId, int blockIndex) {
		if (m_formData[formId][blockIndex][B_BGCOLOR] <= 0) {
			return -1;
		}
		return dConfig.SD_COLOR_TABLE[m_formData[formId][blockIndex][B_FBC]];
	}

	// ȡ��textID
	public static short getStringID(int formId, int blockIndex) {
		return m_formData[formId][blockIndex][B_TEXY_ID];
	}

	// ȡ��block������
	public static short[] getBlock(int formId, int blockIndex) {
		short[] block = new short[4];
		System.arraycopy(m_formData[formId][blockIndex], B_X, block, 0, 4);
		return block;
	}

	public static short getAnchor(int formId, int blockIndex) {
		return (short) CGame
				.getAnchor(m_formData[formId][blockIndex][B_ANCHOR]);
	}

	public static int getIsscroll(int formId, int blockIndex) {
		return m_formData[formId][blockIndex][B_SCROOLTYPE];
	}

	// public static boolean getIsSelectedBG(int formId, int blockIndex) {
	// return m_formData[formId][blockIndex][B_SELECTIEDBG] == 0 ? true : false;
	// }

	public static short getSLAni(int formId, int blockIndex) {
		return m_formData[formId][blockIndex][B_SL_ANI];
	}

	public static short getSLAction(int formId, int blockIndex) {
		return m_formData[formId][blockIndex][B_SL_ACITION];
	}

	// -----------------------���ƽӿ�--------------------------
	private static final int DLG_TEXT_DEFAULT_COLOR = 0xffffff;
	private static final int DLG_TEXT_BG_COLOR = -1;
	private static int startTextColor = DLG_TEXT_DEFAULT_COLOR;
	public static int bgTextColor;
	private static int endTextColor;

	public static void setBgTextColor(int color) {
		bgTextColor = color;
	}

	// �����ַ���
	/*
	 * public static void drawUIText(Graphics g , int formID,int blockID ) {
	 * short block[] = getBlock(formID , blockID); int textID =
	 * getStringID(formID , blockID); int anchor = getAnchor(formID , blockID);
	 * if(textID < 0){ System.out.println("�ı�ID������"); return; } drawText(g ,
	 * m_UIString[textID] , m_UIStringSign[textID] , block ,
	 * bgTextColor,anchor); }
	 * 
	 * private static void drawText(Graphics g , String showString,short[]
	 * dlgSign , short[] block,int clrBg,int anchor){ char ch; boolean bNewLine;
	 * 
	 * // int x = block[0] , y = block[1] ; int x = 0; int y = 0; int wid =
	 * g.getFont().stringWidth(showString); if(anchor == dConfig.ANCHOR_LT){ x =
	 * block[0]- UI_offset_X; y = block[1]- UI_offset_Y; } else if(anchor ==
	 * dConfig.ANCHOR_HB){
	 * 
	 * x = block[0] + wid / 2- UI_offset_X; y = block[1] + block[3]-
	 * UI_offset_Y; } else if(anchor == dConfig.ANCHOR_HT){ x = block[0] + wid /
	 * 2- UI_offset_X; y = block[1] - UI_offset_Y; }
	 * 
	 * 
	 * int curLine = 0; //2.draw dialog text g.setColor(startTextColor);
	 * 
	 * for(int i = 0; i < showString.length(); i++){ bNewLine = false;
	 * //2.1.��ǰ�����Ƿ��б�־ for(int id = 0 , j = 0; j <
	 * dlgSign.length/CGame.DLG_SIGN_INFO_LENGTH; j++){ if(i == dlgSign[id +
	 * CGame.DLG_SIGN_INFO_INDEX_POS]){ switch(dlgSign[id +
	 * CGame.DLG_SIGN_INFO_INDEX_TYPE]){ case CGame.SIGN_NEW_LINE: // /n����
	 * bNewLine = true; break;
	 * 
	 * case CGame.SIGN_SMILE: // smile ������� //............................
	 * //������Ҫ������� break;
	 * 
	 * case CGame.SIGN_CSS: //������ʽ int cssID = dlgSign[id +
	 * CGame.DLG_SIGN_INFO_INDEX_VALUE]; //ȡ����ɫ
	 * g.setColor(CGame.getColor(cssID));
	 * 
	 * //ȡ������ g.setFont(CGame.getFont(cssID)); break;
	 * 
	 * case CGame.SIGN_CSS_DEFAULT: //ȡ����ɫ g.setColor(CGame.getColor(0));
	 * 
	 * //ȡ������ g.setFont(CGame.getFont(0));
	 * 
	 * break;
	 * 
	 * default:
	 * System.out.println("matchDialogSign():no match the dialog text sign = " +
	 * dlgSign[j + CGame.DLG_SIGN_INFO_INDEX_TYPE]); break; } break; } id +=
	 * CGame.DLG_SIGN_INFO_LENGTH; } //---------�Ի����Ʒ�ʽ----------------
	 * //2.2.��ʾ���� ch = showString.charAt(i); //һ�л��ƽ��� if(bNewLine || x +
	 * g.getFont().charWidth(ch) > block[0] + block[2]){ curLine++; x =
	 * block[0]; y += dConfig.SF_HEIGHT*2; // //һҳ���ƽ��� // if(curLine >= numRow){
	 * // curEndIndex = i; // continue; //һҳ������Ͳ�Ҫ���ƺ������ַ��� // } } //2.3.����
	 * 
	 * endTextColor = g.getColor(); // if(clrBg >= 0){ g.setColor(clrBg);
	 * g.drawChar(ch , x + 1 , y , anchor); g.drawChar(ch , x - 1 , y , anchor);
	 * g.drawChar(ch , x , y + 1 , anchor); g.drawChar(ch , x , y - 1 , anchor);
	 * }
	 * 
	 * g.setColor(endTextColor); g.drawChar(ch , x , y , anchor); x +=
	 * g.getFont().charWidth(ch); //---------------------------- // //2.4.�Ի����ƽ���
	 * // if(i == showString.length() - 1){ // CGame.curEndIndex =
	 * showString.length(); // } }
	 * 
	 * }
	 */
	// -----------------����block-----------------
	// public final static byte IM
	public final static byte B_TYPE_RECT = 1;
	public final static byte B_TYPE_B = 2;

	public static void drawBlock(Graphics g, int formID, int blockID) {

		if (blockID == -1) {
			System.out.println("ui�����Ѿ����߼�ɾ��");
			return;
		}

		/**
		 * ���Ʊ�����
		 */
		// if(getBGcolor(formID , blockID) != -1){
		// fillRectangleClip(g ,formID, blockID);
		// }

		/***
		 * ���Ʊ߿�
		 */

		// if(getFBC(formID , blockID) != -1){
		// drawRectangleClip(g ,formID, blockID);
		// }

		/***
		 * ���ƶ���
		 */

		if (getAniID(formID, blockID) >= 0) {
			setUIAni(formID, blockID);
			drawUIAni(g);
		}

		/**********
		 * �����ַ���
		 ***********/

		// if (getStringID(formID, blockID) != -1) {
		// drawUIText(g, formID,blockID);
		// }

	}

	private static void drawRectangleClip(Graphics g, int formID, int blockID) {
		short[] block = getBlock(formID, blockID);
		int color = getFBC(formID, blockID);
		g.setColor(color);

		int xPos = block[0] - UI_offset_X;
		int yPos = block[1] - UI_offset_Y;
		int width = block[2];
		int height = block[3];

		g.drawRect(xPos, yPos, width, height);
	}

	private static void fillRectangleClip(Graphics g, int formID, int blockID) {

		short[] block = getBlock(formID, blockID);
		int color = getBGcolor(formID, blockID);
		g.setColor(color);

		int xPos = block[0] - UI_offset_X;
		int yPos = block[1] - UI_offset_Y;
		int width = block[2];
		int height = block[3];

		g.fillRect(xPos, yPos, width, height);

	}

	// private static short sa[] = new short[]{0,0};
	public static void drawSLAni(Graphics g, int formID, int blockID, short[] sc) {

		// CGame.CGanmationID = getSLAni(formID , blockID);
		UIactionID = getSLAction(formID, blockID);
		if (UIactionID < 0) {
			// System.out.println("��ťѡ�е���ʾЧ��Ϊ�գ�����������");
			return;
		}
		short[] block = getBlock(formID, blockID);
		int x = block[0] + block[2] / 2 - UI_offset_X;
		int y = block[1] + block[3] / 2 - UI_offset_Y;
		uianiPlayer2.setSpriteX(x);
		uianiPlayer2.setSpriteY(y);
		if (uianiPlayer2.actionID != UIactionID) {
			uianiPlayer2.setAnimAction(UIactionID);
		}
		AniData.setMlgs(UIaniMlgs);
		uianiPlayer2.updateAnimation();
		uianiPlayer2.drawFrame(g);
	}

	private static short sc[] = new short[] { 0, 0 };

	public static void setUIAni(int formID, int blockID) {
		// UIanimationID = getAniID(formID, blockID);
		UIactionID = getActionID(formID, blockID);
		// if(UIanimationID >= 0 && UIanimations[UIanimationID] == null){
		// UIaniData.loadAnimation(FILE_UIANI , new int[]{UIanimationID}
		// ,UIanimations ,UIaniMlgs);
		// }

		if (UIanimationID < 0) {
			System.out.println("����ID������");
			return;
		}
		short[] block = getBlock(formID, blockID);
		int x = block[0] + block[2] / 2 - UI_offset_X;
		int y = block[1] + block[3] / 2 - UI_offset_Y;
		uianiPlayer.setSpriteX(x);
		uianiPlayer.setSpriteY(y);

		uianiPlayer.setAnimAction(UIactionID);
		AniData.setMlgs(UIaniMlgs);
	}

	public static void drawUIAni(Graphics g) {
		uianiPlayer.updateAnimation();
		uianiPlayer.drawFrame(g);
	}

	/***
	 * ������ϷһЩicon����Ϣ
	 */
	// public static void drawIconAni(Graphics g, AniData ani[],int actionID,int
	// formID, int blockID,
	// short[] sc) {
	//
	// short[] block = getBlock(formID, blockID);
	// int x = block[0] + block[2] / 2;
	// int y = block[1] + block[3] / 2;
	// ContractionMLG[] tempMlg = AniData.getMlgs();
	// //��ʾͼƬ��������
	// AniData.setMlgs(CGame.CGaniMlgs);
	// ani[actionID].drawAction(g, CGame.CGactionID, sc, x,
	// y, false);
	// AniData.setMlgs(tempMlg);
	//
	// }

	/***********************
	 * ������Ϸ�е�һЩ�ַ�����Ϣ
	 ************************/

	public static void drawProString(Graphics g, String proStr, int formID,
			int blockID, int anchor, int color, int bgColor) {
		short[] block = getBlock(formID, blockID);
		int x = 0, y = 0;

		// if (anchor == dConfig.ANCHOR_LT)

		if (anchor == dConfig.ANCHOR_HT) {
			x = block[0] + block[2] / 2 - UI_offset_X;
			y = block[1] - UI_offset_Y;
		} else {
			x = block[0] + 2 - UI_offset_X;
			y = block[1] + 2 - UI_offset_Y;
		}

		if (bgColor >= 0) {
			g.setColor(bgColor);
			g.drawString(proStr, x + 1, y, anchor);
			g.drawString(proStr, x - 1, y, anchor);
			g.drawString(proStr, x, y + 1, anchor);
			g.drawString(proStr, x, y - 1, anchor);
		}

		g.setColor(color);

		g.drawString(proStr, x, y, anchor);

	}

	public static int Vcounter = 0;
	public static int listID = 0;
	public static int V_SCROLL_WAIT_TIME = 1500; // ���¹������ȴ�2��
	public static long vScrollTimer;

	public static final void drawTxt(Graphics g, String proStr, int framId,
			int blockID, int anchor, int color, int bgColor, int ListID) {
		short[] block = UIdata.getBlock(framId, blockID);
		switch (getIsscroll(framId, blockID)) {
		// ���ҹ���
		case 0:
			Rect r = new Rect(block[0] - UI_offset_X, block[1] - UI_offset_Y,
					block[2], block[3]);
			UITools.drawHScrollString(g, proStr, r, color, bgColor);
			break;
		// ���Ϲ���
		case 1:
			if (UIdata.listID != ListID) {
				Vcounter = 0;
				UIdata.listID = ListID;
				vScrollTimer = System.currentTimeMillis();
			}
			int scrollPos = UITools.drawVScrollString(g, proStr, Vcounter,
					block[0] - UI_offset_X, block[1] - UI_offset_Y, block[2],
					block[3], anchor, color, bgColor);

			// �ȴ�2�����,�����ı��߶ȴ��ڿ�߶�
			if (System.currentTimeMillis() - vScrollTimer >= V_SCROLL_WAIT_TIME
					&& scrollPos > block[3]) {
				Vcounter -= 1;
			}
			if (-Vcounter > scrollPos) {
				Vcounter = block[3];
			}
			break;
		default:
			drawProString(g, proStr, framId, blockID, anchor, color, bgColor);
			break;
		}
	}

	static int HScrollingIndex; // ����ˮƽ�������ֵĿ�����
	static int HScrollOffsetX;
	static long HScrollTimer;
	static long H_SCROLL_WAIT_TIME = 1500; // �ȴ�1.5�����

	/**
	 * ˮƽ��������,
	 * 
	 * @param g
	 *            Graphics
	 * @param str
	 *            String
	 * @param r
	 *            Rect
	 * @param color
	 *            int
	 * @param BGcolor
	 *            int
	 * @param cursorIndex
	 *            int ,��ǰ���ֵ�����
	 */
	public static void drawHScrollStr(Graphics g, String str, Rect r,
			int color, int BGcolor, int textIndex) {
		if (str == null || str == "") {
			return;
		}

		// �Ƿ�Ҫ����
		boolean scrolling = (textIndex == HScrollingIndex)
				&& (System.currentTimeMillis() - HScrollTimer >= H_SCROLL_WAIT_TIME)
				&& (str.length() * g.getFont().stringWidth("��") > r.w);

		int offsetX = scrolling ? HScrollOffsetX : 0;
		// pushClip ( g );
		g.setClip(r.x0, r.y0, r.w - 3, r.h);

		// g.setColor(0xff0000);
		// g.fillRect( r.x0 , r.y0 , r.w - 3 , r.h );
		if (BGcolor >= 0) {
			g.setColor(BGcolor);
			g.drawString(str, r.x0 - offsetX + 1, r.y0 + (r.h >> 1),
					dConfig.ANCHOR_LT);
			g.drawString(str, r.x0 - offsetX - 1, r.y0 + (r.h >> 1),
					dConfig.ANCHOR_LT);
			g.drawString(str, r.x0 - offsetX, r.y0 + (r.h >> 1) + 1,
					dConfig.ANCHOR_LT);
			g.drawString(str, r.x0 - offsetX, r.y0 + (r.h >> 1) - 1,
					dConfig.ANCHOR_LT);
		}
		g.setColor(color);
		g.drawString(str, r.x0 - offsetX, r.y0, dConfig.ANCHOR_LT);

		if (scrolling) {
			HScrollOffsetX += 3;
			if (HScrollOffsetX > g.getFont().stringWidth(str)) {
				HScrollOffsetX = (short) -r.w;
			}
		}

		// popClip ( g );
	}

	/**
	 *�����ĸ���ˮƽ����
	 * 
	 * @param index
	 *            int
	 */
	public static void setHScrollIndex(int index) {
		if (HScrollingIndex != index) {
			HScrollingIndex = index;
			HScrollOffsetX = 0;
			HScrollTimer = System.currentTimeMillis();
		}
	}

}
