package game.object;

import game.res.ResLoader;

import java.io.DataInputStream;
import java.io.InputStream;

/**
 * ����������ӵ�����ը��·���Ĵ洢����ȡ
 * <p>
 * Title: MIG ACT ENGINE
 * </p>
 * <p>
 * Description: The engine for mig action game
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007 1.5.0
 * </p>
 * <p>
 * Company: MIG
 * </p>
 * 
 * @author MIG ACT STUDIO
 * @version 1.5.0
 */
public class CFlyerData {
	// [/*��һά���ɻ��ӵ���ID*/][/*�ڶ�ά���ɻ��ӵ�����ID*/]
	// [/*����ά����һ���Ӧ�ӵ���ID���ڶ������Ƶ�ʣ�������ӵ�����*/]
	static short[][][] flyBulletGroup; // �ɻ��ӵ���

	// [/*��һά���ӵ���ID*/][/*�ڶ�ά���ӵ�����ID*/]
	// [/*����ά��Type/ObjectId/ActionId/Attack/Angle/Speed/EffectID/Ax
	// Ay/offsetX/offSetY/IsCircle*/]
	static short[][][] bulletGroupData; // �ӵ���

	// [/*��һά����ը��ID*/][/*�ڶ�ά����ը����ID*/]
	// [/*����ά����һ�objectID���ڶ��actionID�������offsetX�������offSetY��
	// �����Time*/
	static short[][][] bombGroupData; // ��ը��

	// [/*��һά����ը��ID*/]
	// [/*�ڶ�ά����һ�offsetX���ڶ��offsetY;�����AX�������AY*/]
	static short[][] pathAtom; // ·��ԭ��

	// [/*��һά��·����ID*/][/*�ڶ�ά��·������ID*/]
	// [/*����ά����һ�ID-��Ӧ��·��ԭ�ӵ����ID���ڶ��steps-����*/
	static short[][][] pathGroupData; // ·����

	public CFlyerData() {
	}

	/**
	 * load flyer data
	 */
	public static void loadFlyerData() {
		DataInputStream dis = null;
		try {
			InputStream is = "".getClass().getResourceAsStream(
					ResLoader.s_filenameFlyData);
			dis = new DataInputStream(is);
			flyBulletGroup = readFlyerBullet(dis);
			bulletGroupData = readBullet(dis);
			bombGroupData = readBomb(dis);
			pathAtom = readPathAtom(dis);
			pathGroupData = readPath(dis);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * read flyer bullet data
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return short[][][]
	 */
	private static short[][][] readFlyerBullet(DataInputStream dis)
			throws Exception {
		int length = dis.readShort();
		short[][][] temp = new short[length][][];
		for (int i = 0; i < length; i++) {
			int length1 = dis.readShort();
			short[][] temp1 = new short[length1][];
			for (int j = 0; j < length1; j++) {
				short[] temp2 = new short[3];
				temp2[0] = dis.readShort(); // ID
				temp2[1] = dis.readShort(); // BulletFrequency
				temp2[2] = dis.readShort(); // BulletCount
				// temp2[3] = dis.readShort();//�ӵ��ķ���ʱ��
				temp1[j] = temp2;
			}
			temp[i] = temp1;
		}
		return temp;
	}

	/**
	 * Read bullet data
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return short[][][] bullet data
	 */
	private static short[][][] readBullet(DataInputStream dis) throws Exception {
		int length = dis.readShort();
		short[][][] temp = new short[length][][];
		for (int i = 0; i < length; i++) {
			int length1 = dis.readShort();
			short[][] temp1 = new short[length1][];
			for (int j = 0; j < length1; j++) {
				short[] temp2 = new short[dFlyer.DATA_INFO_BULLET_LENGTH];

				temp2[0] = dis.readShort(); // type
				temp2[1] = dis.readShort(); // ObjectID
				temp2[2] = dis.readShort(); // ActionID
				temp2[3] = dis.readShort(); // Attack
				temp2[4] = dis.readShort(); // S_Angle
				// �Ƕȵ�ת��[0, 360) -> [0, 256)
				if (temp2[4] < 0) {
					temp2[4] += 360;
				}
				temp2[4] = (short) (256 - (temp2[4] * 256) / 360);
				temp2[5] = dis.readShort(); // Speed
				temp2[6] = dis.readShort(); // effectID
				temp2[7] = dis.readShort(); // AX
				temp2[8] = dis.readShort(); // AY
				temp2[9] = dis.readShort(); // OffsetX
				temp2[10] = dis.readShort(); // offsetY
				temp2[11] = dis.readShort(); // flag
				temp2[12] = dis.readShort(); // delayTime
				temp2[13] = dis.readShort(); // numDirection

				temp1[j] = temp2;
			}
			temp[i] = temp1;
		}
		return temp;
	}

	/**
	 * read bomb data
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return short[][][]
	 */
	private static short[][][] readBomb(DataInputStream dis) throws Exception {
		int length = dis.readShort();
		short[][][] temp = new short[length][][];
		for (int i = 0; i < length; i++) {
			int length1 = dis.readShort();
			short[][] temp1 = new short[length1][];
			for (int j = 0; j < length1; j++) {
				short[] temp2 = new short[5];
				temp2[0] = dis.readShort(); // ObjectId
				temp2[1] = dis.readShort(); // ActionId
				temp2[2] = dis.readShort(); // Time
				temp2[3] = dis.readShort(); // OffsetX
				temp2[4] = dis.readShort(); // OffsetY
				temp1[j] = temp2;
			}
			temp[i] = temp1;
		}
		return temp;
	}

	/**
	 * read path atom data
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return short[][]
	 */
	private static short[][] readPathAtom(DataInputStream dis) throws Exception {
		int typeLength = dis.readShort();
		short[][] temp = new short[typeLength][];
		for (int i = 0; i < typeLength; i++) {
			int itemLength = (dis.readShort() << 1);
			short[] tempItemData = new short[itemLength];
			for (int j = 0; j < itemLength; j++) {
				tempItemData[j] = dis.readByte(); // x, y, ... x, y
				temp[i] = tempItemData;
			}
		}
		return temp;
	}

	/**
	 * read path data
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return short[][][]
	 */
	private static short[][][] readPath(DataInputStream dis) throws Exception {
		int length = dis.readShort();
		short[][][] temp = new short[length][][];
		for (int i = 0; i < length; i++) {
			int length1 = dis.readShort();
			short[][] temp1 = new short[length1][];
			for (int j = 0; j < length1; j++) {
				System.out.println("");
				short[] temp2 = new short[3];
				temp2[0] = dis.readShort(); // ID
				temp2[1] = dis.readShort(); // Times
				temp2[2] = dis.readShort();
				temp1[j] = temp2;
			}
			temp[i] = temp1;
		}
		return temp;
	}
}
