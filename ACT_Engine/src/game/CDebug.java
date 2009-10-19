package game;

import game.config.dConfig;
import game.pak.Camera;

import javax.microedition.lcdui.Graphics;

/**
 * <p>
 * Title: engine of RPG game
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
 * Company: mig
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class CDebug {
	public CDebug() {
	}

	public static long _time = 0; // for debug

	public static long m_activeActorTime;
	public static long m_updateActorTime;
	public static long m_updateMapTime;
	public static long m_paintMapTime;
	public static long m_sortActorTime;
	public static long m_paintActorTime;
	public static long m_updateInterfaceTime;
	public static long m_updateMessageTime;

	public static boolean bEnableShowMap = true;
	public final static boolean bEnableAssert = true;
	public static boolean bEnableMemTrace = false; // �Ƿ���Կ��ڴ�����
	public static boolean bEnableTrace = true;
	public static boolean bEnablePrint = true;
	public static boolean bShowKeyInfo = false; // �Ƿ���ʾ������Ϣ
	public static boolean bShowPhysicalLayer = false; // �Ƿ���ʾ�����
	public static boolean bShowPhysicalLayerOnly = false; // �Ƿ�ֻ��ʾ�����

	public final static boolean bShowActorCollideBox = false; // �Ƿ���ʾ��ײ����
	public final static boolean bShowActorAttackBox = false; // �Ƿ���ʾ��������
	public final static boolean bShowActorHP = true; // �Ƿ���ʾ����hp
	public final static boolean bShowActorActivateBox = false;
	public final static boolean bShowActorDectiveTile = false;
	public final static boolean showDebugInfo = true;
	public final static boolean bEnableSpeedTraceInCGame = false;
	public final static boolean bEnableTraceBasicInfo = false;
	public final static boolean bshowCG = true;

	public final static boolean showErrorInfo = true;
	// ��ʾ�ű�������Ϣ
	public final static boolean showScripLoadInfo = true;

	public final static boolean bForTest = false;

	public final static boolean bShowKeyCode = false;
	public final static boolean bShowFreeMem = false;

	public final static boolean bEnableSpeedTraceInCLevel = false;
	public final static boolean showAnimInfo = true; // ��ʾ�������غ��ͷ�

	public static long beforeMemory;
	public static long usedMemory;

	public final static boolean bEnablePlaneMode = false;

	public static int __active_actors_count; // for debug
	static int s_maxActorCount; // ����������ֵ
	public static long m_debugTimer;

	public static void _drawDebugBox(Graphics g, short[] box,
			boolean bScreenCoords, int boxColor, boolean bFilled) {
		int x, y, w, h;
		w = box[2] - box[0];
		h = box[3] - box[1];
		if (bScreenCoords) {
			x = box[0];
			y = box[1];
		} else {
			x = box[0] - Camera.cameraLeft;
			y = box[1] - Camera.cameraTop;
		}

		g.setClip(x, y, w + 1, h + 1);

		g.setColor(boxColor);
		if (bFilled) {
			g.fillRect(x, y, w, h);
			g.setColor(0);
		}
		g.drawRect(x, y, w, h);
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
	}

	public static void _debugInfo(String msg) {
		if (CDebug.showDebugInfo) {
			System.out.print(" !!! WARNING: ");
			System.out.println(msg);
		}
	}

	public static void _assert(boolean value, String msg) {
		if (CDebug.bEnableAssert) {
			try {
				if (!value) {
					if (msg != null) {
						System.out.print(" !!! ASSERT: ");
						System.out.println(msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void _trace(String msg, long num) {
		if (CDebug.bEnableTrace) {
			System.out.print("Trace > ");
			System.out.print(msg);
			System.out.print("   ");
			System.out.println(num);
		}
	}

	public static void _trace(String msg) {
		if (CDebug.bEnableTrace) {
			System.out.print("Trace > ");
			System.out.println(msg);
		}
	}

	public static void _drawDebugLine(Graphics g, int boxColor, int x0, int y0,
			int x1, int y1) {
		g.setColor(boxColor);
		g.drawLine(x0, y0, x1, y1);

	}

	/**
	 * debug ��ʾ������������
	 */
	public static void _showActiveActorCounter() {
		++CDebug.__active_actors_count;
		if (CDebug.bEnableTrace) {
			if (s_maxActorCount < __active_actors_count) {
				s_maxActorCount = __active_actors_count;
			}
			CDebug._trace(" ++ _active_actors_count "
					+ CDebug.__active_actors_count + " , max = "
					+ s_maxActorCount);
		}

	}

	// /**
	// * debug ��ʾ����һ��AI��logic��Ҫ��ʱ��
	// */
	// public static void _showUpdateActorAIWasteTimer ( AbstractActor actor )
	// {
	// if ( CDebug.bEnableSpeedTraceInCLevel )
	// {
	// _time = System.currentTimeMillis () - _time;
	// System.out.println ( " -----  update ai actor " + actor.m_actorID +
	// " --- " + _time + " ms." );
	// _time = System.currentTimeMillis ();
	// }
	//
	// }

}
