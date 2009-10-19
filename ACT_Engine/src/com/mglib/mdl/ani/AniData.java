package com.mglib.mdl.ani;

import game.CDebug;
import game.CGame;
import game.res.ResLoader;

import java.io.DataInputStream;

import com.mglib.mdl.ContractionMLG;

/**
 * <p>
 * Title: engine of game
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
public class AniData {

	public short mlgCount;
	protected short[] mlgs;
	protected short[][] moduleIDs;
	protected short[][] mappingInfo;

	// module
	protected short moduleCount; // module����
	protected int[] modules; // ����module����(MLGID,offsetID)(short����bit��3��13)

	// frame
	protected short frameCount; // ����֡
	protected byte[] frameBoxIndexs; // ÿ֡��box����(unsignByte: colBoxIndex,
										// attBoxIndex): ��СΪframeCount * 2
	protected byte[] frameSpriteCounts; // ÿ��֡��sprite����: ��СΪframeCount
	protected short[] frameSpriteOffset; // ÿ��֡��Spriteƫ����

	// frame sprite
	protected short spritesCount; // ����֡�����е�sprite������
	protected int[] spritesDatas; // ���е�sprite����(flip,moduleID,dx,dy)(int����bit��3,10,9,9)

	// action
	protected short actionCount;
	protected byte[] actionMechModelIndexs; // ÿ��������mechModel���: ��СΪactionCount
	protected byte[] actionSequenceCounts; // ÿ��������Sequence����: ��СΪactionCount
	protected short[] actionSequenceOffset; // ÿ��������sequenceƫ����

	// action sequence
	protected short sequenceCount; // ���ж��������е�sequence������
	protected short[] sequenceDatas; // ���е�sequence����(duration,
										// frameID)(short����bit��5,10), attackFame

	// box
	protected short colBoxCount;
	protected short attBoxCount;
	protected short[] colBoxs;
	protected short[] attBoxs;

	// mech model
	protected short mechCount; // ���еĶ����Ĳ�ͬ���˶���Ϣ����
	protected byte[] mechModels; // ���еĶ������˶���Ϣ����(ax,ay,vx,vy,flag): ��СΪMechCount
									// * 4

	protected short[] curMlgs;

	protected short[] defaultMLGInfo;

	/****************************************************************************
	 * Animation������ģ��
	 ***************************************************************************/
	public static ContractionMLG[] aniMLGs;

	public static void setMlgs(ContractionMLG[] mlg) {
		aniMLGs = mlg;
	}

	public static ContractionMLG[] getMlgs() {
		return aniMLGs;
	}

	/**
	 * ����animation
	 */
	public void destroy() {
		modules = null;
		frameBoxIndexs = null;
		frameSpriteCounts = null;
		spritesDatas = null;
		actionMechModelIndexs = null;
		actionSequenceCounts = null;
		sequenceDatas = null;
		colBoxs = null;
		attBoxs = null;
		mechModels = null;
	}

	/**
	 * �ӵ�ǰ�ļ����ж�ȡһ��Animation
	 * 
	 * @param dis
	 *            DataInputStream
	 * @return CAnimation
	 */
	public void loadAni(DataInputStream dis) throws Exception {

		// ͼ
		mlgCount = dis.readShort();
		mlgs = new short[mlgCount];
		curMlgs = new short[mlgCount];
		moduleIDs = new short[mlgCount][];
		mappingInfo = new short[mlgCount][];
		for (int i = 0; i < mlgCount; i++) {
			mlgs[i] = dis.readShort();
			curMlgs[i] = mlgs[i];
		}
		for (int i = 0; i < mlgCount; i++) {
			int mc = dis.readShort();
			moduleIDs[i] = new short[mc];
			for (int j = 0; j < mc; j++) {
				moduleIDs[i][j] = dis.readShort();
			}
		}
		for (int i = 0; i < mlgCount; i++) {
			int mc = dis.readByte();
			mappingInfo[i] = new short[mc];
			for (int j = 0; j < mc; j++) {
				mappingInfo[i][j] = dis.readShort();
			}
		}
		// module
		moduleCount = dis.readShort();
		modules = new int[moduleCount];
		for (int i = 0; i < moduleCount; i++) {
			modules[i] = dis.readInt();
		}
		// frame
		frameCount = dis.readShort();
		frameBoxIndexs = new byte[frameCount << 1];
		dis.read(frameBoxIndexs);
		frameSpriteCounts = new byte[frameCount];
		dis.read(frameSpriteCounts);
		frameSpriteOffset = new short[frameCount + 1];
		for (int i = 0; i < frameSpriteOffset.length; i++) {
			frameSpriteOffset[i] = dis.readShort();
		}

		// frame sprite
		spritesCount = dis.readShort();
		spritesDatas = new int[spritesCount];
		for (int i = 0; i < spritesCount; i++) {
			spritesDatas[i] = dis.readInt();
		}
		// action
		actionCount = dis.readShort();
		actionMechModelIndexs = new byte[actionCount];
		dis.read(actionMechModelIndexs);
		actionSequenceCounts = new byte[actionCount];
		dis.read(actionSequenceCounts);
		actionSequenceOffset = new short[actionCount + 1];
		for (int i = 0; i < actionSequenceOffset.length; i++) {
			actionSequenceOffset[i] = dis.readShort();
		}

		// action sequence
		sequenceCount = dis.readShort();
		sequenceDatas = new short[sequenceCount << 1];
		for (int i = 0; i < sequenceDatas.length; i++) {
			sequenceDatas[i] = dis.readShort();
		}

		// box
		colBoxCount = dis.readShort();
		attBoxCount = dis.readShort();
		colBoxs = new short[colBoxCount << 2];
		attBoxs = new short[attBoxCount << 2];
		for (int i = 0; i < colBoxs.length; i++) {
			colBoxs[i] = dis.readShort();
		}
		for (int i = 0; i < attBoxs.length; i++) {
			attBoxs[i] = dis.readShort();
		}
		// mech model
		mechCount = dis.readShort();
		mechModels = new byte[mechCount * 5];
		dis.read(mechModels);

		// mlgInfo
		getDefaultMLGInfo();

	}

	/******************************************************
      *
      ***************************************************/

	/**
	 * ͨ��mlgID��û�װ��mlg
	 * 
	 * @param MLGID
	 *            int
	 * @return ContractionMLG
	 */
	protected ContractionMLG getMLG(int MLGID) {
		int iIdx = 0;
		for (; iIdx < this.mlgCount; iIdx++) {
			if (this.mlgs[iIdx] == MLGID) {
				break;
			}
		}
		return aniMLGs[this.curMlgs[iIdx]];
	}

	/**
	 * �������е�animation��ǰ��imageIndex��palIndex
	 * 
	 * @return short[]
	 */
	public short[] getDefaultMLGInfo() {
		if (defaultMLGInfo == null) {
			defaultMLGInfo = new short[mlgCount]; // Ĭ��Ϊ0
			// �Ժ����Ҫ��չΪ����������ЩΪĬ��ֵ������
		}
		short[] v = new short[mlgCount];
		System.arraycopy(defaultMLGInfo, 0, v, 0, mlgCount);
		return v;
	}

	/**
	 * ���mlg��Flag
	 * 
	 * @param flag
	 *            long[]
	 */
	public void getMLGFlag(long[] flag) {
		for (int i = 0; i < this.mlgs.length; i++) {
			flag[mlgs[i] / 64] |= 1L << (mlgs[i] % 64);
			for (int j = 0; j < this.mappingInfo[i].length; j++) {
				flag[mappingInfo[i][j] / 64] |= 1L << (mappingInfo[i][j] % 64);
			}
		}
	}

	// private static int[] releaseAniID;
	// private static int[] animationID;

	// ȡ�ü��ص�AniID
	public static int[] getLoadAniID(int newLevel) {
		long[] aniFlag = null;
		long[] aniMlgFlag = null;
		// animatin flag
		if (aniFlag == null) {
			aniFlag = new long[ResLoader.animationCount / 64 + 1];
		}

		for (int i = 0; i < aniFlag.length; i++) {
			aniFlag[i] = 0;
		}

		// animation mlg flag
		if (aniMlgFlag == null) {
			aniMlgFlag = new long[ResLoader.aniMLGCount / 64 + 1];
		}

		int id = newLevel;
		for (int j = ResLoader.animationMasks[id].length - 1; j >= 0; j--) {
			int aniID = ResLoader.animationMasks[id][j];
			aniFlag[aniID / 64] |= 1L << (aniID % 64);
		}

		// if(oldLevel != -1) {
		// for(int i = ResLoader.animationMasks[oldLevel].length - 1; i >= 0;
		// i--) {
		// int aniID = ResLoader.animationMasks[oldLevel][i];
		// if( (aniFlag[aniID / 64] & (1L << (aniID % 64))) != 0) {
		// aniFlag[aniID / 64] &= ~ (1L << (aniID % 64)); //����
		// }
		// }
		// }
		// ��Ҫ���ص�animationID����
		int counter = 0;

		for (int i = 0; i < ResLoader.animationCount; i++) {
			if ((aniFlag[i / 64] & (1L << (i % 64))) != 0) {
				counter++;
			}
		}

		int animationID[] = new int[counter];
		counter = 0;
		for (int i = 0; i < ResLoader.animationCount; i++) {
			if ((aniFlag[i / 64] & (1L << (i % 64))) != 0) {
				animationID[counter] = i;
				counter++;
			}
		}
		return animationID;

	}

	// ȡ���ͷŵ�����ID
	public static int[] getReleaseAniID(int oldLevel) {
		if (oldLevel == -1)
			return null;

		long[] aniReleaseFlag = new long[ResLoader.animationCount / 64 + 1];

		for (int i = ResLoader.animationMasks[oldLevel].length - 1; i >= 0; i--) {
			int aniID = ResLoader.animationMasks[oldLevel][i];
			aniReleaseFlag[aniID / 64] |= 1L << (aniID % 64);
		}

		// int id = newLevel;
		// for(int j = ResLoader.animationMasks[id].length - 1; j >= 0; j--) {
		// int aniID = ResLoader.animationMasks[id][j];
		// if( (aniReleaseFlag[aniID / 64] & (1L << (aniID % 64))) != 0) {
		// aniReleaseFlag[aniID / 64] &= ~ (1L << (aniID % 64)); //����
		// }
		// }

		int[] tempreleaseAni = new int[ResLoader.animationCount];
		int counter = 0;
		for (int i = 0; i < ResLoader.animationCount; i++) {
			int idx = i / 64;
			int bit = i % 64;
			if ((aniReleaseFlag[idx] & (1L << bit)) != 0) {
				tempreleaseAni[counter] = i;
				counter++;
			}
		}
		int[] releaseAniID = new int[counter];
		System.arraycopy(tempreleaseAni, 0, releaseAniID, 0, counter);
		return releaseAniID;
	}

	// �ͷŶ�����Դ
	public static void releaseAni(int animationID[], AniData[] anim,
			ContractionMLG[] aniMlg) {
		// �ͷ�Ani
		long[] flag = new long[aniMlg.length / 64 + 1];
		for (int i = 0; i < animationID.length; i++) {
			if (anim[animationID[i]] != null) {
				anim[animationID[i]].getMLGFlag(flag);
				anim[animationID[i]].destroy();
				if (CDebug.showDebugInfo) {
					System.out.println("DEBUG >>��\release m_animations[" + i
							+ "]");
				}
			}
		}
		// �ͷ�Mlg
		for (int i = 0; i < aniMlg.length; i++) {
			if ((flag[i / 64] & (1L << (i % 64))) != 0) {
				aniMlg[i].destroy();

			}
		}
	}

	public static AniData[] loadAnimation(String fileName, int animationID[],
			AniData[] anim, ContractionMLG[] aniMlg) {
		// �򿪶����ļ���
		DataInputStream dis = new DataInputStream("".getClass()
				.getResourceAsStream(fileName));
		try {

			long[] flag = new long[aniMlg.length / 64 + 1];
			short aniCount = dis.readShort();
			short mlgCount = dis.readShort();
			// load animation data
			int[] offset = new int[aniCount + 1];
			for (int j = 0; j < offset.length; j++) {
				offset[j] = dis.readInt();
			}

			// ...animationID��Ҫ��������
			sort(animationID);

			int animIndex = 0;
			for (int i = 0; i < anim.length; i++) {
				if (animationID.length > 0 && i == animationID[animIndex]) {
					anim[i] = new AniData();
					anim[i].loadAni(dis);
					// anim[i].getMLGFlag(flag);
					if (CDebug.showDebugInfo) {
						System.out.println("LoadAni >>>>>>>>>>>>>" + " anim["
								+ i + "]");
					}
					animIndex++;
					if (animIndex >= animationID.length - 1) {
						animIndex = animationID.length - 1;
					}
				} else if (i < aniCount) {
					dis.skip(offset[i + 1] - offset[i]);
				}
			}

			// ��ȡMlgƫ������
			offset = new int[mlgCount + 1];
			for (int i = 0; i < offset.length; i++) {
				offset[i] = dis.readInt();
			}

			// ����mlg�����־
			for (int i = 0; i < animationID.length; i++) {
				anim[animationID[i]].getMLGFlag(flag);
				if (CDebug.showDebugInfo) {
					for (int j = 0; j < anim[animationID[i]].mlgs.length; j++) {
						System.out.print("anim[" + animationID[i] + "]"
								+ anim[animationID[i]].mlgs[j] + "\n");
					}
				}

			}

			// ����Mlg����
			for (int i = 0; i < aniMlg.length; i++) {
				if (i >= offset.length - 1) {
					break;
				}
				if ((flag[i / 64] & (1L << (i % 64))) != 0 /*
															 * && aniMlg[i] ==
															 * null
															 */) {
					aniMlg[i] = ContractionMLG.read(dis);
					if (CGame.s_isCreatAllModuleImage) {
						aniMlg[i].getAllModuleImage(true);
					}

				} else {
					dis.skip(offset[i + 1] - offset[i]);
				}
			}

			dis.close();
			dis = null;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return anim;
	}

	/**
	 * ÿ�����������Ϊ2���ļ������ݺ�ͼƬ��
	 * 
	 * @param fileName
	 *            String
	 * @param aniLoadIDs
	 *            int[]
	 * @param anims
	 *            AniData[]
	 * @param aniMlgs
	 *            ContractionMLG[]
	 * @return AniData[]
	 */
	public static AniData[] loadAnimationK700(String fileName,
			int[] aniLoadIDs, AniData[] anims, ContractionMLG[] aniMlgs) {
		boolean isCG = fileName.equals("res.bin");
		// �򿪶����ļ���
		DataInputStream dis;
		try {
			long[] flag = new long[(aniMlgs.length >> 6) + 1];
			// ����������
			for (int i = 0; i < aniLoadIDs.length; i++) {
				int aid = aniLoadIDs[i];
				dis = new DataInputStream("".getClass().getResourceAsStream(
						"/bin/Ani/" + aid + (isCG ? ".r" : ".a")));

				if (CDebug.showDebugInfo) {
					if (anims[aid] != null) {
						System.out.println(">>DEBUG: loadAnimation() ��" + aid
								+ "�����Ѿ����ڣ�");
					}
				}
				anims[aid] = new AniData();
				anims[aid].loadAni(dis);
				anims[aid].getMLGFlag(flag);
				dis.close();
				dis = null;
			}
			// mlg������
			for (int i = 0; i < aniMlgs.length; i++) {
				if ((flag[i >> 6] & (1L << (i % 64))) != 0) {
					dis = new DataInputStream("".getClass()
							.getResourceAsStream(
									("/bin/Ani/" + i + (isCG ? ".rm" : ".am"))));
					aniMlgs[i] = ContractionMLG.read(dis);
					if (CDebug.showDebugInfo) {
						System.out.println(">>Debug: loadAnimation mlgID = "
								+ i);
					}

					dis.close();
					dis = null;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return anims;

	}

	// ����
	public static void sort(int data[]) {
		int temp;
		for (int i = 1; i < data.length; i++) {
			for (int j = data.length - 1; j >= i; j--) {
				if (data[j] < data[j - 1]) {
					temp = data[j - 1];
					data[j - 1] = data[j];
					data[j] = temp;
				}
			}
		}

	}

}
