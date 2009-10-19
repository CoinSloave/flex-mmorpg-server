package game.res;

import game.CDebug;
import game.CGame;
import game.object.CFlyerData;
import game.object.CObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mglib.mdl.ContractionMLG;
import com.mglib.mdl.ani.AniData;
import com.mglib.mdl.map.MapData;

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
 * @author kai zengk ��Դ������
 * @version 1.0
 */
public class ResLoader implements ResData {
	private ResLoader() {
	}

	public static final String s_fileMapResName = "/bin/map.bin";
	public static final String s_fileAniResName = "/bin/animation.bin";
	public static final String s_fileConfigResName = "/bin/config.bin";
	public static final String s_fileSceneResName = "/bin/scenes.bin";
	// public static final String s_filenameTexts = "/bin/string.bin";
	public static final String s_filenameFont = "/bin/font.bin";
	public static final String s_filenameFlyData = "/bin/flyer.bin";
	// public static final String s_filenameQuest = "/bin/questvar.bin";

	public static final String s_filenameUIdata = "/bin/uidata_n73.bin";
	// public static final String s_filenameUIres = "/bin/uires.bin";

	public static final String s_fileCGResName = "/bin/res.bin";
	public static final String s_fileCssData = "/bin/CSS.bin";
	public static final String s_fileOptionDlg = "/bin/choice.bin";

	private static ResLoader resLoader;

	public static ResLoader getInstance() {
		if (resLoader == null) {
			resLoader = new ResLoader();
		}
		return resLoader;
	}

	// �������޸�
	/**
	 * װ��ȫ�ֵ�������Ϣ
	 * 
	 * @return int
	 */
	public static int sceneCount; // ��������
	public static int[] sceneOffset; // ÿһ��������ƫ��

	public static int animationCount; // ��������������.

	public static short mapMLGCount;
	public static short aniMLGCount;

	public static int actorClassCount; // ����������������
	public static short[] classesAnimID; // ��Ӧ�������ݵ�id
	public static short[] classAIIDs;
	public static byte[] classesDefaultZ; // ��Ӧ���ƹ�ϵ��id
	public static byte[] classesDataType; // class data type

	// ��Ϸ�з���Ķ���animationID
	public static short systemFaceAniID;
	public static short dialogHeadAniID;
	public static short equipIconAniID;
	public static short effectAniID;

	public static AniData[] animations;
	public static ContractionMLG[] aniMlgs;

	public static AniData[] CGanimations;
	public static ContractionMLG[] CGaniMlgs;

	public final static byte ANI_COUNTER = 100;
	public final static byte MLG_COUNTER = 100;
	// ��Ϸ����ʱ�õĶ�������
	static {
		CGanimations = new AniData[ANI_COUNTER];
		CGaniMlgs = new ContractionMLG[MLG_COUNTER];
	}

	public static short[][] animationMasks; // �������ݵĴ���
	public static byte[] classesFlags;

	/********************
	 * ����ȫ�ֵ�������Ϣ
	 *******************/
	public void loadGlobalData() {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(this.getClass().getResourceAsStream(
					s_fileConfigResName));
			sceneCount = dis.readByte();
			sceneOffset = new int[sceneCount + 1];
			for (int i = 0; i < sceneOffset.length; i++) {
				sceneOffset[i] = dis.readInt();
			}

			// ��ȡ�����animationID
			systemFaceAniID = dis.readShort();
			dialogHeadAniID = dis.readShort();
			equipIconAniID = dis.readShort();
			effectAniID = dis.readShort();

			// globalAnimationID = dis.readShort();
			animationCount = dis.readShort(); // ����Ҫ�ĳ�short

			mapMLGCount = dis.readShort();
			aniMLGCount = dis.readShort();
			MapData.mapMLGs = new ContractionMLG[mapMLGCount];
			// ���峡����m_aniMlg����
			if (aniMlgs == null) {
				aniMlgs = new ContractionMLG[aniMLGCount];
			}
			// ���峡�����õ��� animations ����
			if (animations == null) {
				animations = new AniData[animationCount];
			}

			animationMasks = new short[sceneCount][];
			for (int i = 0; i < sceneCount; i++) {
				animationMasks[i] = new short[dis.readShort()];
				for (int j = 0; j < animationMasks[i].length; j++) {
					animationMasks[i][j] = dis.readShort();
				}
			}
			actorClassCount = dis.readByte();
			classesAnimID = new short[actorClassCount];
			classAIIDs = new short[actorClassCount];
			classesDefaultZ = new byte[actorClassCount];
			classesFlags = new byte[actorClassCount];
			classesDataType = new byte[actorClassCount];
			for (int i = 0; i < actorClassCount; i++) {
				classesAnimID[i] = dis.readShort();
				classAIIDs[i] = dis.readShort();
				classesDefaultZ[i] = dis.readByte();
				classesFlags[i] = dis.readByte();
				classesDataType[i] = dis.readByte();
			}
			dis.close();
			dis = null;
		} catch (Exception ex) {
			if (CDebug.bEnablePrint) {
				System.out.println(s_fileConfigResName + "is  failing Load");
				ex.printStackTrace();
			}
		}

		/**
		 * ����·�����ӵ�������
		 */
		CFlyerData.loadFlyerData();
	}

	/*******************
	 * ���عؿ���������Ϣ
	 * 
	 * @param newLevel
	 *            int
	 */
	public static int textIndex;
	public static int keyMapLevelID;
	public static int sceneFlag;
	public static short FPS_RATE_TRAILER;
	// actors data Actor��һЩ��̬���� ��Ҫģ�������
	public static int nActorsCount; // ������ Actor������
	// ������Actor�Ļ������� ����m_classID , ������flag,animationId,��ǰ��actionID, m_x ,m_y,
	// ������������� activeBox[4] �Լ������һЩ����
	public static short[] actorsBasicInfo;
	public static short[] actorsBasicInfoOffset; // ��Ϊ��һά���鱣��
													// ����Ҫ��¼ÿһ��actorid�Ķ���������ݵ�ƫ��

	public void loadScene(int newLevel) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream("".getClass().getResourceAsStream(
					s_fileSceneResName));
			dis.skip(sceneOffset[newLevel]);
			// ��ȡtextBlock������
			textIndex = dis.readShort();
			// �Զ�������ID
			keyMapLevelID = dis.readShort();

			sceneFlag = dis.readInt();

			FPS_RATE_TRAILER = dis.readShort();

			nActorsCount = dis.readShort();
			CGame.actorsShellID = new short[nActorsCount];
			int[] offset = new int[nActorsCount + 1];
			actorsBasicInfoOffset = new short[nActorsCount + 1];
			CGame.actorsRegionFlags = new long[nActorsCount];
			for (int i = 0; i < offset.length; i++) {
				offset[i] = dis.readInt();
				actorsBasicInfoOffset[i] = (short) (offset[i] >> 1);
			}

			actorsBasicInfo = new short[offset[nActorsCount] >> 1];
			for (int i = 0; i < actorsBasicInfo.length; i++) {
				actorsBasicInfo[i] = dis.readShort();
			}
			// -------------------------����trailer����--------------------------------
			loadTrailerData(dis);
			// -----------------------------------------------------------------------
			dis.close();
			dis = null;
			if (keyMapLevelID >= 0) {
				loadKeyMapArray(keyMapLevelID);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * k700����scene��ʽ��ÿ������һ���ļ�
	 * 
	 * @param newLevel
	 *            int
	 */
	public void loadSceneK700(int newLevel) {
		DataInputStream dis = null;
		try {
			InputStream s = "".getClass().getResourceAsStream(
					"/bin/" + newLevel + ".s");
			dis = new DataInputStream(s);
			// dis.skip (sceneOffset[newLevel]);
			// ��ȡtextBlock������
			textIndex = dis.readShort();
			// �Զ�������ID
			keyMapLevelID = dis.readShort();

			sceneFlag = dis.readInt();

			FPS_RATE_TRAILER = dis.readShort();

			nActorsCount = dis.readShort();
			CGame.actorsShellID = new short[nActorsCount];
			int[] offset = new int[nActorsCount + 1];
			actorsBasicInfoOffset = new short[nActorsCount + 1];
			CGame.actorsRegionFlags = new long[nActorsCount];
			for (int i = 0; i < offset.length; i++) {
				offset[i] = dis.readInt();
				actorsBasicInfoOffset[i] = (short) (offset[i] >> 1);
			}

			actorsBasicInfo = new short[offset[nActorsCount] >> 1];
			for (int i = 0; i < actorsBasicInfo.length; i++) {
				actorsBasicInfo[i] = dis.readShort();
			}
			// -------------------------����trailer����--------------------------------
			loadTrailerData(dis);
			// -----------------------------------------------------------------------
			dis.close();
			dis = null;
			if (keyMapLevelID >= 0) {
				loadKeyMapArray(keyMapLevelID);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	// /**
	// * ����trailer ����
	// */

	public static int nTrailersCount;
	public static short[] nTrailersDuration;
	public static byte[] nTrailersTimeLinesCount;
	public static short[][] nTrailersTimeLinesActorID;
	public static byte[][][] trailers;

	public static String m_currentStringDate[];

	public final static byte TYPE_CAMARE_AUTO = 0;
	public final static byte TYPE_CAMARE_FALLOWHERO = 1;
	public final static byte TYPE_CAMARE_NOLOCK = 2;

	public static boolean useSlowMotion;

	public void loadTrailerData(DataInputStream dis) throws IOException {
		// -----------------------------------------------
		// ---------------------------------------------------
		nTrailersCount = dis.readByte(); // ��ȡ��ǰ����trailer������
		nTrailersDuration = new short[nTrailersCount]; // ÿ��trailer���ִ��ʱ��
		nTrailersTimeLinesCount = new byte[nTrailersCount]; // ����ʱ���ߵ�����
		nTrailersTimeLinesActorID = new short[nTrailersCount][]; // ����ÿ��ʱ�������õ������ActorID
		trailers = new byte[nTrailersCount][][]; // ����ÿ���ؼ�֡������

		for (int trailer_id = 0; trailer_id < nTrailersCount; ++trailer_id) {
			nTrailersDuration[trailer_id] = dis.readShort(); // ��ȡÿ��trailer���ִ��ʱ��
			int timelines_count = dis.readByte(); // ��ȡʱ���ߵ�����
			nTrailersTimeLinesCount[trailer_id] = (byte) (timelines_count); //
			nTrailersTimeLinesActorID[trailer_id] = new short[timelines_count];
			trailers[trailer_id] = new byte[timelines_count][];
			for (int timeline_id = 0; timeline_id < timelines_count; ++timeline_id) {
				nTrailersTimeLinesActorID[trailer_id][timeline_id] = dis
						.readShort(); // ��ȡÿ��ʱ�������õ���ActorId
				int keyframes_count = dis.readByte(); // ��ȡ�ؼ�֡������
				trailers[trailer_id][timeline_id] = new byte[keyframes_count
						* CObject.FRAME_LENGTH];
				dis.read(trailers[trailer_id][timeline_id]); // ��ȡÿ���ؼ�֡������
			}
		}
	}

	public static int[][] keyMapArray;

	/**
	 * ��ȡ�Զ�������������Ϣ
	 * 
	 * @param KeyArrayID
	 *            int
	 */
	public static void loadKeyMapArray(int KeyArrayID) {
		try {
			DataInputStream dis = null;
			dis = new DataInputStream("".getClass().getResourceAsStream(
					"/bin/keyArray.bin"));
			short count = dis.readShort();
			int[] offset = new int[count + 1];
			for (int i = 0; i < offset.length; i++) {
				offset[i] = dis.readInt();
			}
			// ����offset
			dis.skip(offset[KeyArrayID]);

			short keyArrayCount = dis.readShort();
			keyMapArray = new int[keyArrayCount][];

			for (int i = 0; i < keyArrayCount; i++) {
				short itemCount = dis.readShort();
				itemCount *= 4;
				keyMapArray[i] = new int[itemCount];
				for (int j = 0; j < itemCount; j++) {
					keyMapArray[i][j] = dis.readInt();
				}

			}
			dis.close();
			dis = null;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static String[][] optionContent; // ѡ������
	public static short[][][] optionSign; // ���Ƶı�־
	public static String[] questionContent; // ��������
	public static short[][] questionSign; //

	public static int dlgOpValue[];

	public static void initDlgOpValue() {
		for (int i = 0; i < dlgOpValue.length; i++) {
			dlgOpValue[i] = -1;
		}
	}

	public void loadOptionDlgData() {
		try {
			DataInputStream dis = null;
			dis = new DataInputStream("".getClass().getResourceAsStream(
					s_fileOptionDlg));
			// ��Ŀ������
			int count = dis.readShort();
			dlgOpValue = new int[count];
			initDlgOpValue();

			int[] offset = new int[count + 1];
			questionContent = new String[count];
			questionSign = new short[count][];

			optionContent = new String[count][];
			optionSign = new short[count][][];

			for (int i = 0; i < count + 1; i++) {
				offset[i] = dis.readInt();
			}

			for (int i = 0; i < count; i++) {

				int sc = dis.readShort();
				questionSign[i] = new short[sc];
				for (int j = 0; j < sc; j++) {
					questionSign[i][j] = dis.readShort();
				}
				String str = dis.readUTF();
				questionContent[i] = str;

				int opCount = dis.readShort();
				optionContent[i] = new String[opCount];
				optionSign[i] = new short[opCount][];

				for (int j = 0; j < opCount; j++) {
					int oc = dis.readShort();
					optionSign[i][j] = new short[oc];
					for (int k = 0; k < oc; k++) {
						optionSign[i][j][k] = dis.readShort();
					}

					String opStr = dis.readUTF();
					optionContent[i][j] = opStr;
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	/************************
      *
      *****************************/

	public static short[][] CSS;

	public void loadCssData() {

		try {
			DataInputStream dis = null;
			dis = new DataInputStream("".getClass().getResourceAsStream(
					s_fileCssData));
			int count = dis.readShort();
			CSS = new short[count][3];
			for (int i = 0; i < CSS.length; i++) {
				for (int j = 0; j < 3; j++) {
					CSS[i][j] = dis.readShort();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
