package game.object;

import game.CDebug;
import game.CGame;
import game.CTools;
import game.Goods;
import game.config.dConfig;
import game.config.dGame;
import game.pak.Camera;
import game.pak.GameEffect;
import game.res.ResLoader;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import com.mglib.mdl.ani.AniPlayer;
import com.mglib.mdl.ani.Player;
import com.mglib.mdl.map.MapData;
import com.mglib.script.Script;
import com.mglib.ui.Data;

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
public class CObject implements IObject {

	public CObject() {

	}

	public AniPlayer aniPlayer;

	public int m_shellID; // ��Ӧ����list���������
	public int m_actorID; // ��Ӧ���������ж���ı��v
	public int m_classID;
	public int m_scriptID;
	public int m_linkID = -1;
	public int m_dataID;

	public int m_x; // Actor �ڳ����еľ���x����
	public int m_y; // Actor �ڳ����еľ���y����

	public int m_initX; // ��ʼλ��
	public int m_initY;

	public int m_vX; // Actor x������ٶ�
	public int m_vY; // Actor y������ٶ�
	public int m_aX; // Actor x����ļ��ٶ�
	public int m_aY; // Acror y����ļ��ٶ�
	public int m_z; // ���������

	public int m_currentState; // ��ǰ״̬

	public int m_animationID; // ��Ӧ������id
	public int m_actionID; // ��ǰ����״̬��id

	public int m_invincible;

	public boolean isInAir;

	public static short[] s_colBox1 = new short[4];
	public static short[] s_colBox2 = new short[4];

	public int m_flags;

	public int m_timer; // ����ͨ�ü�����

	// �����Զ��ƶ��Ĵ���
	// �Ƿ��Զ��ƶ�
	public boolean isAutoMove;

	public int m_destX; // �ƶ�Ŀ��XĿ��
	public int m_destY; // �ƶ�Ŀ��YĿ��

	// ����ı䶯���Ĵ���
	public boolean isAutoAction;

	public int preAction;
	public int changeActionID;
	public int actionDur;
	public int playTimes;
	public boolean isRecover;

	public boolean isPhyDown;

	public short m_actorProperty[] = new short[PRO_LENGTH];

	public final static int PARA_LENGTH = 23;
	public int[] m_parameters = new int[PARA_LENGTH];
	boolean initForcely;

	public void initialize() {

		m_vX = 0;
		m_vY = 0;
		m_aX = 0;
		m_aY = 0;
		m_invincible = 0;
		m_z = ResLoader.classesDefaultZ[m_classID];
		m_timer = 0;
		setFlag(dActor.FLAG_ACTION_GRAB_MECH_INFO);
		m_initX = m_x;
		m_initY = m_y;
		isInAir = false;
		m_linkID = -1;
		isAutoMove = false;
		isAutoAction = false;
		isPhyDown = false;
		m_beLooted = false;
		keyFrameHurt = false;
		// ����������ƶ���Ϊnull �����
		if (aniPlayer == null && m_animationID != -1) {
			aniPlayer = new AniPlayer(ResLoader.animations[m_animationID],
					m_x >> dConfig.FRACTION_BITS, m_y >> dConfig.FRACTION_BITS);
		}

		// m_Lift_Vx = 0;
		for (int i = 0; i < m_parameters.length; i++) {
			m_parameters[i] = 0;
		}

		// CGame.m_hero.m_actorProperty[PRO_INDEX_HP] = 100;
		timelineIndex = -1;
		m_relativeMisc = null;
		m_bonus.removeAllElements();

		setAnimAction(m_actionID);
		/**
		 * ����ű���ʼ��
		 */
		Script.initObjectScript(this);
		/**
		 * ·����ʼ��
		 */
		initPath();

		// ��ʾ���������Ҫ����ʼ��
		setFlag(dActor.FLAG_NEED_INIT);
		initForcely = true;
		clearAllAddition();
		update();
	}

	/**
	 * ���ձ������������Ϣ
	 * 
	 * @return boolean �Ƿ�ɹ��ı����˻�����Ϣ
	 */
	public boolean pack() {
		if (m_shellID < 0) {
			return false;
		}
		// �����ǰ��trailer״̬
		// ע��
		if (CGame.currentTrailerIndex >= 0 && timelineIndex >= 0) {
			return false;
		}
		// ע�ͽ���
		// for some special reason, can't be packed
		if (testFlag(dActor.FLAG_BASIC_NOT_PACKABLE)) {
			return false;
		}
		m_shellID = -1;
		return true;
	}

	// ���ض�������
	/**
	 * 
	 * @param shellID
	 *            int
	 * @param actorID
	 *            int
	 * @param bForcely
	 *            boolean
	 * @return boolean
	 */
	public boolean load(int shellID, int actorID, boolean bForcely) {
		if (m_shellID >= 0) {
			return false;
		}

		m_actorID = actorID;

		if (m_actorID >= 0) {
			m_flags = getActorInfo(dActor.INDEX_BASIC_FLAGS); // ���ػ�����flag��Ϣ

			if (!bForcely) {
				if (testFlag(dActor.FLAG_BASLIC_NOT_LOADABLE)) {
					return false;
				}
			}

			m_classID = getActorInfo(dActor.INDEX_CLASS_ID); // ��Ӧ
			// m_flags = GetActorInfo( dActor.Index_Basic_Flags );
			m_x = getActorInfo(dActor.INDEX_POSITION_X) << game.config.dConfig.FRACTION_BITS; // ��Ӧ������
																								// ����x����
			m_y = getActorInfo(dActor.INDEX_POSITION_Y) << game.config.dConfig.FRACTION_BITS; // ��Ӧ������
																								// ����y����
			m_scriptID = getActorInfo(dActor.INDEX_SCRIPT_ID);
			m_dataID = getActorInfo(dActor.INDEX_DATA_ID);
			// m_animationID = GetActorInfo( dActor.INDEX_ANIMATION_ID );
			m_animationID = ResLoader.classesAnimID[m_classID]; // ������ID

			if (CDebug.bEnablePrint) {
				if (testFlag(dActor.FLAG_BASIC_VISIBLE) && m_animationID > 0
						&& ResLoader.animations[m_animationID] == null) {
					System.out.println("you are a foolish pig");
					System.out.println("m_classID>>=" + m_classID
							+ "   Animationsi's flag is not seleceted");
				}
			}

			m_actionID = getActorInfo(dActor.INDEX_ACTION_ID); // ��ǰ�����Ķ���ID
			m_currentState = getActorInfo(dActor.INDEX_STATE); // ȡ�õ�ǰ��״̬ID

		} else {
			m_flags = dActor.FLAG_BASIC_VISIBLE
					| dActor.FLAG_BASIC_ALWAYS_ACTIVE;
		}

		m_shellID = shellID;
		return true;
	}

	/**
	 * �������ķ�������ص�����
	 */
	public void destroy() {
		// ע��
		// if(CGame.m_hero.m_leftObjective == this) {
		// CGame.m_hero.m_leftObjective = null;
		// }
		// if(CGame.m_hero.m_rightObjective == this) {
		// CGame.m_hero.m_rightObjective = null;
		// }
		// initialize ();
		if (m_relativeMisc != null) {
			m_relativeMisc = null;
		}

		if (CGame.m_hero.m_relativeMisc == this) {
			CGame.m_hero.m_relativeMisc = null;
		}
		// add by lin 09.5.4 �������ָ�򱾶��������
		CObject actorTemp = null;
		int shellId = CGame.activeActorsListHead;
		while (shellId != -1) {
			actorTemp = CGame.m_actorShells[shellId];
			if (actorTemp.m_relativeMisc == this) {
				actorTemp.m_relativeMisc = null;
			}
			shellId = CGame.nextActorShellID[shellId];
		}

		if (aniPlayer != null) {
			aniPlayer.clear();
			aniPlayer = null;
		}

	}

	/**
	 * �������ActorShell�����޳�
	 * 
	 * @param reloadable
	 *            boolean
	 */
	public void die(boolean reloadable) {

		if (m_shellID < 0) {
			return; // can't die twice
		}
		setFlag(dActor.FLAG_BASIC_DIE);
		clearFlag(dActor.FLAG_BASIC_NOT_PACKABLE);
		setFlag(dActor.FLAG_BASLIC_NOT_LOADABLE);
		if (reloadable) {
			setFlag(dActor.FLAG_BASIC_REBORNABLE);
		}
		m_vX = 0;
		if (m_actorID >= 0) {
			setActorInfo(dActor.INDEX_BASIC_FLAGS, m_flags
					& dActor.MASK_BASIC_FLAGS);
		}
		int shell_id = m_shellID; //
		int nextupdateid = CGame.nextActorShellID[shell_id];
		if (CGame.deactivateShell(m_shellID) != null) {
			destroy();
			if (CGame.nextUpdateShellID == shell_id) {
				CGame.nextUpdateShellID = nextupdateid;
			}
		}
		setActorInfo(dActor.INDEX_BASIC_FLAGS, m_flags
				& dActor.MASK_BASIC_FLAGS);
	}

	/**
	 * ���ö����ĳ������
	 * 
	 * @param infoIndex
	 *            short info index
	 * @param infoValue
	 *            int info value
	 * 
	 */
	public void setActorInfo(short infoIndex, int infoValue) {
		if (m_actorID < 0) {
			return;
		}
		if (m_actorID >= 0) {
			ResLoader.actorsBasicInfo[(infoIndex & dActor.MASK_ACTOR_INFO_INDEX)
					+ ResLoader.actorsBasicInfoOffset[m_actorID]] = (short) infoValue;
		}
	}

	/**
	 * ����ָ��Actor ��Ϣ
	 * 
	 * @param actorID
	 *            int
	 * @param infoIndex
	 *            short info index
	 * @param infoValue
	 *            int info value
	 */
	public static void setActorInfo(int actorID, short infoIndex, int infoValue) {
		if (actorID < 0) {
			return;
		}
		ResLoader.actorsBasicInfo[(infoIndex & dActor.MASK_ACTOR_INFO_INDEX)
				+ ResLoader.actorsBasicInfoOffset[actorID]] = (short) infoValue;
	}

	public boolean testFlag(int flag) {
		flag &= dActor.MASK_ALL_FLAGS;
		return (m_flags & flag) == flag;
	}

	// ����ӦactorID��basicFlag
	public boolean testBasicFlag(int actorID, int flag) {
		return ((ResLoader.actorsBasicInfo[(dActor.INDEX_BASIC_FLAGS & dActor.MASK_ACTOR_INFO_INDEX)
				+ ResLoader.actorsBasicInfoOffset[actorID]] & (flag & dActor.MASK_BASIC_FLAGS)) == (flag & dActor.MASK_BASIC_FLAGS));
	}

	/**
	 * set face dir
	 * 
	 * @param dx
	 *            int negative for left, positive for right, 0 for unchanged
	 */
	public void setFaceDir(int dx) {
		if (dx < 0) {
			setFlag(dActor.FLAG_BASIC_FLIP_X);
			if (aniPlayer != null) {
				aniPlayer.setSpriteFlipX(-1);
			}
		} else if (dx > 0) {
			clearFlag(dActor.FLAG_BASIC_FLIP_X);
			if (aniPlayer != null) {
				aniPlayer.setSpriteFlipX(1);
			}

		}
	}

	/**
	 * face to a specified actor
	 * 
	 * @param the
	 *            specified actor
	 */

	public void setFaceTo(CObject a) {
		setFaceDir(a.m_x - m_x);
	}

	/**
	 * get current face dir
	 * 
	 * @return int
	 */
	public int getFaceDir() {
		return testFlag(dActor.FLAG_BASIC_FLIP_X) ? -1 : 1;
	}

	/**
	 * �õ����õķ���
	 * 
	 * @param dir
	 *            int
	 * @return int
	 */
	public int getSetDir(int dir) {
		if (dir == 1) {
			return 1;
		} else {
			return -1;
		}
	}

	public void setXY(int x, int y) {
		m_x = x << dConfig.FRACTION_BITS;
		m_y = y << dConfig.FRACTION_BITS;
	}

	/**
	 * check the current face dir
	 * 
	 * @param a
	 *            CActorShell
	 * @return boolean
	 */
	public boolean isFaceTo(CObject a) {
		return ((m_x - a.m_x) ^ getFaceDir()) < 0;
	}

	/**
	 * set a flag
	 * 
	 * @param flag
	 *            int the flag will be set.
	 */
	public void setFlag(int flag) {
		m_flags |= (flag & dActor.MASK_ALL_FLAGS);
	}

	/**
	 * clear a specified flag
	 * 
	 * @param flag
	 *            int the flag will be cleared
	 */
	public void clearFlag(int flag) {
		m_flags &= ~(flag & dActor.MASK_ALL_FLAGS);
	}

	/**
	 * �����flag�ķ���
	 * 
	 * @param classFlag
	 *            int
	 * @return boolean
	 */
	public boolean testClasssFlag(int classFlag) {
		if ((ResLoader.classesFlags[m_classID] & classFlag) != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �������Ƿ�����ײ ���μ��
	 * 
	 * @param actor
	 *            CActor
	 * @return boolean
	 */
	public boolean testColliding(CObject actor) {
		getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
		actor.getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox2);
		return CTools.isIntersecting(s_colBox1, s_colBox2);
	}

	/**
	 * ȡ��һ��ָ��Actor����ײ����
	 * 
	 * @param actorID
	 *            �����е��� һ��actor
	 * @param boxData
	 *            short[]
	 */
	public static short[] getActorActivateBoxInfo(int actorID, short[] boxData) {
		System.arraycopy(ResLoader.actorsBasicInfo,
				ResLoader.actorsBasicInfoOffset[actorID]
						+ (dActor.INDEX_ACTIIVE_BOX_LEFT), boxData, 0, 4);
		return boxData;
	}

	/**
	 * ��ȡ��ɫ��Ϣ
	 * 
	 * @param infoIndex
	 *            short info index
	 * @return short info value
	 */
	public short getActorInfo(int infoIndex) {
		if (m_actorID < 0) {
			return getActorInfoInlink(infoIndex, m_linkID);
		}
		return ResLoader.actorsBasicInfo[(infoIndex & dActor.MASK_ACTOR_INFO_INDEX)
				+ ResLoader.actorsBasicInfoOffset[m_actorID]];
	}

	/**
	 * ��ȡ��ɫ��Ϣ
	 * 
	 * @param infoIndex
	 *            short info index
	 * @return short info value
	 */
	public short getActorInfoInlink(int infoIndex, int linkID) {
		if (linkID < 0) {
			return 0;
		}
		return ResLoader.actorsBasicInfo[(infoIndex & dActor.MASK_ACTOR_INFO_INDEX)
				+ ResLoader.actorsBasicInfoOffset[linkID]];
	}

	/**
	 * ����ײ����͹�������ŵ�ָ����������
	 * 
	 * @param boxType
	 *            int Box_Collide or Attack_Box
	 * @param boxData
	 *            short[] the result array
	 */
	public void getActorBoxInfo(int boxType, short[] boxData) {
		int param_index = ((boxType == dActor.BOX_COLLIDE) ? Player.INDEX_COLLISION_BOX_LEFT
				: Player.INDEX_ATTACK_BOX_LEFT);
		System.arraycopy(aniPlayer.boxInf, param_index, boxData, 0, 4);
		CTools.shiftBox(boxData, m_x >> dConfig.FRACTION_BITS,
				m_y >> dConfig.FRACTION_BITS);
	}

	public void setPosX(int x) {
		m_x = x;
	}

	public void setPosY(int y) {
		m_y = y;
	}

	public int getActorTopPos() {
		int top = aniPlayer.boxInf[Player.INDEX_ATTACK_BOX_TOP]
				+ (m_y >> dConfig.FRACTION_BITS);
		return top;
	}

	public int getActorBottomPos() {
		int bottom = aniPlayer.boxInf[Player.INDEX_ATTACK_BOX_BOTTOM]
				+ (m_y >> dConfig.FRACTION_BITS);
		return bottom;
	}

	public void getActorOppBoxInfo(int boxType, short[] boxData) {
		int param_index = ((boxType == dActor.BOX_COLLIDE) ? Player.INDEX_COLLISION_BOX_LEFT
				: Player.INDEX_ATTACK_BOX_LEFT);
		System.arraycopy(aniPlayer.boxInf, param_index, boxData, 0, 4);
	}

	/**
	 * ����һ����λ����Ϣ
	 * 
	 * @param mlgIndex
	 *            int ��������ͼƬ���
	 * @param iIndex
	 *            int ��Ҫ������ͼƬ�ı��
	 * @param pIndex
	 *            int ��ɫ���� Ĭ�������Ϊ0 �����л�ɫ���
	 */
	public short[] suitInfo;

	private void setSuit(int mlgIndex, int iIndex, int pIndex) {
		// ��ʼ������Ϣ
		if (suitInfo == null && m_animationID != -1) {
			suitInfo = aniPlayer.aniData.getDefaultMLGInfo();
		}
		if (suitInfo == null) {
			// debug
			if (CDebug.bEnableTrace) {
				System.out.println("setSuit() -> suitInfo = null!!");
			}
			return;
		}
		suitInfo[mlgIndex] = (short) ((iIndex << 8) | (pIndex & 0xff));
	}

	public void grabMechInfo() {
		byte objectV[] = aniPlayer.grabMechInfo();
		m_vX = objectV[0] << dConfig.FRACTION_BITS;
		m_vY = objectV[1] << dConfig.FRACTION_BITS;
		m_aX = objectV[2] << dConfig.FRACTION_BITS;
		m_aY = objectV[3] << dConfig.FRACTION_BITS;
		if (testFlag(dActor.FLAG_BASIC_FLIP_X)) {
			m_vX = -m_vX;
			m_aX = -m_aX;
		}
	}

	/***********************
	 * �������� �� ״̬����
	 *********************************/
	public int m_preState;

	public boolean setState(int newState) {
		if (newState == -1) {
			return false;
		}
		// m_standElapse = -1;
		setActorInfo(dActor.INDEX_STATE, newState); // ���浱ǰ��ɫ��״̬��basicInfo��
		m_preState = m_currentState;
		m_currentState = newState;
		aniPlayer.clearAniPlayFlag(Player.FLAG_ACTION_ALL);
		setCutInOutSTInScript((short) m_preState, (short) m_currentState);
		m_timer = 0;
		// ������������Ʒ
		if (newState == ST_ACTOR_DIE) {
			loot();

		}
		return true;
	}

	public boolean setState(int newState, boolean changeAction) {
		setState(newState);
		return true;
	}

	/**
	 * ���ö���״̬
	 * 
	 * @param actionID
	 *            int
	 */

	public void setAnimAction(int actionID) {
		m_actionID = actionID;
		if ((aniPlayer == null || aniPlayer.aniData == null)) {
			System.out.println("actorID>>>" + m_actorID + " , aiID>>>"
					+ ResLoader.classAIIDs[m_classID] + ">>>>�Ķ�������Ϊnull");
			return;
		}

		// ������
		if (testFlag(dActor.FLAG_FROZEN)) {
			return;
		}

		aniPlayer.setSpriteFlipX(getFaceDir());
		aniPlayer.setSpritePos(m_x >> dConfig.FRACTION_BITS,
				m_y >> dConfig.FRACTION_BITS);
		try {
			aniPlayer.setAnimAction(actionID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// �������л���ٶȼ��ٶ�
		if (testFlag(dActor.FLAG_ACTION_GRAB_MECH_INFO)) {
			grabMechInfo();
		}

		setFlag(dActor.FLAG_ACTION_GRAB_MECH_INFO);
		KFMoveDir = 0;
		if (this == CGame.m_hero) {
			CGame.m_hero.shadowTick = 5;
		}
	}

	/**
	 * ��������Ϣ��
	 * 
	 * @param actionID
	 *            int
	 * @param flag
	 *            int
	 */
	public void setAnimAction(int actionID, int flag) {
		if (aniPlayer == null) {
			// CDebug._debugInfo ("aniPlaye Ϊnull");
			return;
		}
		aniPlayer.setAniPlayFlag(flag);
		setAnimAction(actionID);

	}

	public void updateAnimation() {
		if (aniPlayer == null) {
			// CDebug._debugInfo ("aniPlaye Ϊnull");
			return;
		}
		if (aaEffectPlayer != null) {
			aaEffectPlayer.updateAnimation();
		}

		// ������
		if (testFlag(dActor.FLAG_FROZEN)) {
			return;
		}
		if (this == CGame.m_hero) {
			int i = 0;
		}
		aniPlayer.updateAnimation();

	}

	/*********************
	 * trailer
	 **********************/
	public int timelineIndex; // ÿ���������ʱ���ߵ�����
	public int currentKeyFrameIndex = 0; // ÿ��������е�keyFrame������
	public static final int FRAME_LENGTH = 9;
	static final int FRAME_INDEX_POSITION = 0; // ��ǰ�ؼ�֡�Ŀ̶�
	static final int ACTION_ID_POSITION = 2; // ��ǰActor��ActionID
	static final int TEXT_ID_POSITION = 3; // �ַ���������

	static final int POS_X_POSTIION = 4; // ��ǰActor��x����
	static final int POS_Y_POSITION = 6; // ��ǰActor��y����
	static final int FRAME_FLAG_POSITION = 8; // ��ǰActor��flag

	static final int FLAG_FLIP_X = 0x01;
	static final int FLAG_HIDE = 0x02;
	static final int FLAG_DEACTIVATE = 0x04;
	static final int FLAG_KICKOFF_TRAILER = 0x08;
	static long FPS_RATE_TRAILER;

	/***
	 * Trailer ���ֵ�AI
	 */

	public void ai_TrailerCamera() {
		if (CGame.m_curState == dGame.GST_GAME_RUN
				&& Script.autoMoveActorCounter == 0
				&& Script.autoshowActionCouner == 0) {
			if ((testActiveBoxCollideObject(CGame.m_hero))) {
				do_trailer();
			}
		}
	}

	public void do_trailer() {
		if (CGame.m_hero.m_actorProperty[PRO_INDEX_HP] > 0) {
			// ���ԭ�ȵ� trailer ��Ϊ null ֹͣԭ�ȵ�trailer
			if (CGame.currentTrailerCamera != null
					&& CGame.currentTrailerCamera != this) {
				CGame.currentTrailerCamera.timelineIndex = -1;
				CGame.currentTrailerCamera.die(false);
			}
			// �������trailerCarmera���ܱ�����
			setFlag(dActor.FLAG_BASLIC_NOT_LOADABLE);
			if (m_actorID >= 0) {
				setActorInfo(dActor.INDEX_BASIC_FLAGS, m_flags
						& dActor.MASK_BASIC_FLAGS);
			}

			// ȡ�õ�ǰ������trailer�� trailerID
			CGame.currentTrailerIndex = CGame.getTrailerIDByCameraID(m_actorID);
			// ȡ�õ�ǰtrailer��ID
			CGame.currentTrailerCameraActorID = m_actorID;
			CGame.currentTrailerCamera = this;
			CGame.currentTrailerFrame = -1;
		}
	}

	/**
	 * trailer����flag ������˵�� FlipX �����Ƿ���x����ķ�ת Hide ���õ�ǰActor�Ƿ�ɼ� Deactived
	 * ���ö����Ƿ��ActorList���ͷ� kick off trailer �����Ƿ���ֹ��ǰActor��trailer��ʱ����
	 * 
	 * ����trailer
	 */

	public void updateTrailer() {
		int offset;
		int flags;
		// ȡ�õ�ǰActor���йؼ�֡������
		byte[] timeline = ResLoader.trailers[CGame.currentTrailerIndex][timelineIndex];
		int keyFrameCount = timeline.length / FRAME_LENGTH; // �ܵĹؼ�֡������

		while (true) {
			offset = currentKeyFrameIndex * FRAME_LENGTH;
			flags = timeline[offset + FRAME_FLAG_POSITION];
			// �����ؼ�֡ʱ�����logic
			if (CGame.currentTrailerFrame == (CTools.combineShort(
					timeline[offset + FRAME_INDEX_POSITION], timeline[offset
							+ FRAME_INDEX_POSITION + 1]))) {
				if (CGame.currentTrailerFrameX3 % 3 != 0) {
					break;
				}
				// ��������� ���ò�����
				if ((flags & FLAG_HIDE) == FLAG_HIDE) {
					clearFlag(dActor.FLAG_BASIC_VISIBLE);
				} else {
					setFlag(dActor.FLAG_BASIC_VISIBLE);
				}

				// �����ת���÷�ת
				if ((flags & FLAG_FLIP_X) == FLAG_FLIP_X) {
					setFlag(dActor.FLAG_BASIC_FLIP_X);
				} else {
					clearFlag(dActor.FLAG_BASIC_FLIP_X);
				}
				// ����trailer��ʱ����
				if ((flags & FLAG_KICKOFF_TRAILER) == FLAG_KICKOFF_TRAILER) {
					timelineIndex = -1;
					return;
				}

				// ����ʱ���� ���������ؼ�������б��ȥ
				if ((flags & FLAG_DEACTIVATE) == FLAG_DEACTIVATE
						&& m_actorID != CGame.currentTrailerCameraActorID) { //
					timelineIndex = -1;
					die(false);
					return;
				}

				int new_action = timeline[offset + ACTION_ID_POSITION] & 0xff;

				m_x = CTools.combineShort(timeline[offset + POS_X_POSTIION],
						timeline[offset + POS_X_POSTIION + 1]) << dConfig.FRACTION_BITS;
				m_y = CTools.combineShort(timeline[offset + POS_Y_POSITION],
						timeline[offset + POS_Y_POSITION + 1]) << dConfig.FRACTION_BITS;

				// ȡ��stringID
				int string_id = timeline[offset + TEXT_ID_POSITION];
				boolean hasString = string_id >= 0;
				// Class ����������actionId ��һЩ���⴦��
				if (ResLoader.classAIIDs[m_classID] == dActorClass.CLASS_ID_TRAILERCAMERA) {
					// ���ݲ�ͬ��ActionID ����Ӧ�Ĳ���
					// ����ͷ��ʼ�ı�־
					if (new_action == dActionID.Action_ID_TrailerCamera_start_slow_motion) {
						GameEffect.setSlowStatus(true);
					}
					// ����ͷ�����ı�־
					else if (new_action == dActionID.Action_ID_TrailerCamera_stop_slow_motion) {
						GameEffect.setSlowStatus(false);
					}
					//
					else if (new_action == dActionID.Action_ID_TrailerCamera_press_8) {
						// CGame.m_counter = 0;
					}

					else if (new_action == dActionID.Action_ID_TrailerCamera_text_start) {
						// do nothing
					} else if (new_action == dActionID.Action_ID_TrailerCamera_text_end) {
						// ����ʾ���ַ�������Ϊnull
						// CGame.m_currentStringDate = null;
					}
					// ����Ч��
					if (new_action == dActionID.Action_ID_TrailerCamera_open_screen) {
						GameEffect.setCover(GameEffect.COVER_OPEN);
					}
					// ����Ч��
					else if (new_action == dActionID.Action_ID_TrailerCamera_close_screen) {
						GameEffect.setCover(GameEffect.COVER_CLOSE);
					}
					// �ڵ���Ļ
					else if (new_action == dActionID.Action_ID_TrailerCamera_close_cover_screen) {
						GameEffect.setCover(GameEffect.COVER_NORMAL);
					} else if (new_action == dActionID.Action_ID_TrailerCamera_light_off) {
						CGame.bShowMap = false;
					} else if (new_action == dActionID.Action_ID_TrailerCamera_light_on) {
						CGame.bShowMap = true;
					} else {
						if (m_animationID == dActionID.Action_ID_TrailerCamera_press_8) {
							CGame.stopCurrentTrailer();
						}
					}
				}

				// ������ַ�����ʾ�� ȡ���ַ�����Ϣ
				if (hasString) {
					// if(dConfig.m_gameLevelString[string_id] != null){
					// CGame.m_currentStringDate =
					// CGame.breakLongMsg(dConfig.m_gameLevelString[string_id] ,
					// dConfig.m_smallFont , 60);
					// }
				}
				setAnimAction(new_action);
				break;
			}
			// ʱ���߽��� ���m_currentTrailerFrame���� ��ǰActor��ʱ���ߵ����һ���ؼ�֡ʱ����ʱ��ֹͣ break

			else if (CGame.currentTrailerFrame > ((CTools.combineShort(
					timeline[timeline.length - FRAME_LENGTH
							+ FRAME_INDEX_POSITION], timeline[timeline.length
							- FRAME_LENGTH + FRAME_INDEX_POSITION + 1])) & 0xFFFF)) {
				break;
			}
			// ��ǰ֡���ǹؼ�֡ break
			else if (CGame.currentTrailerFrame > ((CTools.combineShort(
					timeline[offset + FRAME_INDEX_POSITION], timeline[offset
							+ FRAME_INDEX_POSITION + 1])))
					&& CGame.currentTrailerFrame < (CTools.combineShort(
							timeline[offset + FRAME_INDEX_POSITION
									+ FRAME_LENGTH], timeline[offset
									+ FRAME_INDEX_POSITION + 1 + FRAME_LENGTH]))) {
				break;
			}
			// ���ָ֡��С�ڵ�һ�ؼ�֡�ؼ�֡֡�̶� �����õ�ǰ��Actor������
			else if (CGame.currentTrailerFrame < (CTools.combineShort(
					timeline[offset + FRAME_INDEX_POSITION], timeline[offset
							+ FRAME_INDEX_POSITION + 1]))) {
				--currentKeyFrameIndex;
				if (currentKeyFrameIndex < 0) {
					clearFlag(dActor.FLAG_BASIC_VISIBLE);
					currentKeyFrameIndex = 0;
					return;
				}
			}
			// ���ָ֡�������һ���ؼ�֡֡�̶� ��Actor ����ؼ�֡����++
			else if (CGame.currentTrailerFrame >= ((CTools.combineShort(
					timeline[offset + FRAME_INDEX_POSITION + FRAME_LENGTH],
					timeline[offset + FRAME_INDEX_POSITION + 1 + FRAME_LENGTH])))) {
				++currentKeyFrameIndex;
			}

		}

		// */

		// ���õ�ǰ������λ�øı�
		// ͨ��actor������ٶȸı�
		if (m_vX != 0 || m_vY != 0 || m_aX != 0 || m_aY != 0) {
			m_vX += mechStep(m_aX);
			m_vY += mechStep(m_aY);
			// m_x += (flipX) ? -m_vX : m_vX;
			m_x += mechStep(m_vX);
			m_y += mechStep(m_vY);

		} else { // �����ǰ����ؼ�֡û�н��������������ؼ�֮֡��������ϵȷ���ƶ�
			if (currentKeyFrameIndex < keyFrameCount - 1) {
				int x = CTools.combineShort(timeline[offset + POS_X_POSTIION],
						timeline[offset + POS_X_POSTIION + 1]); // ��ǰ�ؼ�֡��x����
				int y = CTools.combineShort(timeline[offset + POS_Y_POSITION],
						timeline[offset + POS_Y_POSITION + 1]); // ��ǰ�ؼ�֡��y����
				int nextX = CTools.combineShort(timeline[offset
						+ POS_X_POSTIION + FRAME_LENGTH], timeline[offset
						+ POS_X_POSTIION + FRAME_LENGTH + 1]); // �õ���һ���ؼ�֡��x����
				int nextY = CTools.combineShort(timeline[offset
						+ POS_Y_POSITION + FRAME_LENGTH], timeline[offset
						+ POS_Y_POSITION + FRAME_LENGTH + 1]); // �õ��¸��ؼ�֡��y����
				int totalCount = ((CTools.combineShort(timeline[offset
						+ FRAME_INDEX_POSITION + FRAME_LENGTH], timeline[offset
						+ FRAME_INDEX_POSITION + 1 + FRAME_LENGTH])) - (CTools
						.combineShort(timeline[offset + FRAME_INDEX_POSITION],
								timeline[offset + FRAME_INDEX_POSITION + 1]))) * 3; // �õ������ؼ�֡�Ŀ̶�
				int escape_frames = CGame.currentTrailerFrameX3
						- (CTools.combineShort(timeline[offset
								+ FRAME_INDEX_POSITION], timeline[offset
								+ FRAME_INDEX_POSITION + 1])) * 3; // �õ�ʣ�ڵ�֡��
				m_x = (x + (nextX - x) * escape_frames / totalCount) << dConfig.FRACTION_BITS;
				m_y = (y + (nextY - y) * escape_frames / totalCount) << dConfig.FRACTION_BITS;
			}
		}
	}

	public int mechStep(int mechValue) {
		// if(CGame.gData.useSlowMotion) {
		if (Player.useSlowMotion) {
			return mechValue / dConfig.Slow_Motion_Ratio;
		} else {
			return mechValue;
		}
	}

	/*******************************
	 * �ű�����
	 *******************************/
	// ��ǰ����Ľű�����: [�ӽű��ı��][ǰ��������������Ԫ������(������Ԫ��(����1������2��...))...]
	public byte[][] currentConditions;

	// ��ǰ����Ľű�ִ��: [�ӽű��ı��][ִ��������(ִ�е�Ԫ��(����1������2��...))...]
	public byte[][] currentConducts;

	// �ű��������Ӵ˱�ſ�ʼ
	public short scanStartSubScriptID;

	// ָ��curScriptCondition���һ���ֽ�
	public short conditionIndex = 0;

	// ��ǰ�ű�����
	public byte[] curScriptConditions;

	// �Ƿ��ڽű�������
	public boolean isInScriptRunning;

	// �������Ľű����Ƿ���ָ����Ϣ
	public boolean hasWord; // �Ƿ��жԻ�

	public Vector vTaskList = new Vector(); // �������������������

	/**
	 * ɨ��ű�
	 */
	public final boolean scanScript() {
		if (m_scriptID > Script.NONE_SCRIPT_ID && !isInScriptRunning
				&& !Script.scriptLock) {
			// try{
			Script.scanScript(this);
			// }
			// catch(Exception e){
			// e.printStackTrace();
			// }
			// //����ű������г�״̬
			clearCutInOutSTInScript();

			if (isInScriptRunning) {
				return true;
			}
		}
		return false;
	}

	/***************************
       *
       ***********************/
	void setCutInOutSTInScript(short prestate, short curstate) {
		m_actorProperty[PRO_INDEX_CUTINSTATE] = curstate;
		m_actorProperty[PRO_INDEX_CUTOUTSTATE] = prestate;
	}

	void clearCutInOutSTInScript() {
		m_actorProperty[PRO_INDEX_CUTINSTATE] = -1;
		m_actorProperty[PRO_INDEX_CUTOUTSTATE] = -1;
	}

	/**************************
	 * Camera
	 **************************/
	public static int m_otherCamera;
	public int m_posInCamera;
	static boolean s_isLockCamera;
	public static int m_lockCameraX;
	public static int m_lockCameraY;
	int center_y;

	public static final int PARA_LOCKCAMERA_X = 1 << (24 + 1);
	public static final int PARA_LOCKCAMERA_Y = 1 << (24 + 2);
	public static final int PARA_LOCKCAMERA_XY = 1 << (24 + 3);
	public static final int PARA_HERO_LOCKCAM_Y = 1 << (24 + 4);
	public static final int PARA_AUTOMOVE_CAM_Y = 1 << (24 + 5); // ��ͷY�����Զ��ƶ�
	public static final int PARA_AUTOMOVE_CAM_X = 1 << (24 + 6); // ��ͷX�����Զ��ƶ�

	/**
	 * ��������Ļ�е�λ�ã�0-7bit��ʾy���꣬8-15bit��ʾx���꣬24-31bit��ʾ��ͷ���������͡�
	 * ��Ļ�ĳ��Ϳ�ƽ�ֳ�16����Ԫ���������ֵ��ʾ�������ĸ���Ԫ�
	 */
	public void updateCamera() {
		// �õ�Camera����������
		int posInCamera = m_otherCamera & 0xff000000;

		// �õ�������y����Camera��y��
		posInCamera |= ((m_otherCamera & 0xff) != 0 ? m_otherCamera
				: m_posInCamera) & 0xff;
		// �õ�������x����Camera��y��
		posInCamera |= ((m_otherCamera & 0xff00) != 0 ? m_otherCamera
				: m_posInCamera) & 0xff00;

		if ((posInCamera & PARA_HERO_LOCKCAM_Y) == 0
				&& (posInCamera & PARA_AUTOMOVE_CAM_X) == 0) {
			center_y = (m_y >> dConfig.FRACTION_BITS)
					- ((posInCamera & 0xFF) - HERO_IN_CAMERA_Y_CENTER)
					* CAMERA_HEIGHT_UNIT;
		}

		/***************************************
		 * �����ͷ�Զ������ƶ�
		 ********************************************/

		else if ((posInCamera & PARA_AUTOMOVE_CAM_Y) != 0 /*
														 * &&
														 * CGame.m_cameraCenterY
														 * < center_y
														 */) {
			center_y = Camera.cameraCenterY;
		}

		// 0x80 00 00 00 ��ʾ��ͷ�����ƶ����������ƶ�
		// if( (posInCamera & 0x80000000) != 0 && CGame.m_cameraCenterY <
		// center_y){
		// center_y = CGame.m_cameraCenterY;
		// }

		int relativex = (((posInCamera & 0xff00) - HERO_IN_CAMERA_X_CENTER) >> dConfig.FRACTION_BITS)
				* CAMERA_WIDTH_UNIT;

		if (getFaceDir() == -1 && (m_otherCamera & 0xff00) == 0) {
			relativex = -relativex;
		}

		relativex = (m_x >> dConfig.FRACTION_BITS) - relativex;

		// ��Ļ��x������ס
		if ((posInCamera & PARA_LOCKCAMERA_X) != 0) {
			relativex = m_lockCameraX;
			lockActorInCamera();
		}

		// ��Ļ��y������ס
		else if ((posInCamera & PARA_LOCKCAMERA_Y) != 0) {
			center_y = m_lockCameraY;
		}

		// ��Ļ��x,y���򶼱���ס
		else if ((posInCamera & PARA_LOCKCAMERA_XY) != 0) {
			relativex = m_lockCameraX;
			center_y = m_lockCameraY;
			lockActorInCamera();
		}

		if ((posInCamera & PARA_AUTOMOVE_CAM_X) != 0) {
			relativex = Camera.cameraCenterX;
			center_y = Camera.cameraCenterY;
			// lockHeroInCamera ();
		}

		Camera.setCameraCenter(relativex, center_y);

		if ((posInCamera & PARA_LOCKCAMERA_X) == 0
				&& (posInCamera & PARA_LOCKCAMERA_Y) == 0
				&& (posInCamera & PARA_LOCKCAMERA_XY) == 0
				&& (posInCamera & PARA_HERO_LOCKCAM_Y) == 0
				&& (posInCamera & PARA_AUTOMOVE_CAM_X) == 0
				&& (posInCamera & PARA_AUTOMOVE_CAM_Y) == 0) {
			m_otherCamera = 0;
		}
	}

	public void lockActorInCamera() {
		int width = getFaceDir() == 1 ? aniPlayer.boxInf[LEFT]
				: aniPlayer.boxInf[RIGHT] /*
										 * Math.abs (aniPlayer.boxInf[LEFT] +
										 * aniPlayer.boxInf[RIGHT]) / 2
										 */;
		if (m_x <= (Camera.cameraCenterX - dConfig.S_WIDTH / 2 + Math
				.abs(width)) << dConfig.FRACTION_BITS
				&& m_vX <= 0) {
			m_x = (Camera.cameraCenterX - dConfig.S_WIDTH / 2 + Math.abs(width)) << dConfig.FRACTION_BITS;
			m_vX = m_aX = 0;
		} else if (m_x >= (Camera.cameraCenterX + dConfig.S_WIDTH / 2 - Math
				.abs(width)) << dConfig.FRACTION_BITS
				&& m_vX >= 0) {
			m_x = (Camera.cameraCenterX + dConfig.S_WIDTH / 2 - Math.abs(width)) << dConfig.FRACTION_BITS;
			m_vX = m_aX = 0;
		}

	}

	/**
	 * ����������һ����������
	 * 
	 * @param camBox
	 *            short
	 */
	public void lockActorInArea(short[] camBox) {

		int width = getFaceDir() == 1 ? aniPlayer.boxInf[LEFT]
				: aniPlayer.boxInf[RIGHT] /*
										 * Math.abs (aniPlayer.boxInf[LEFT] +
										 * aniPlayer.boxInf[RIGHT]) / 2
										 */;
		if (m_x <= (camBox[LEFT] + Math.abs(width)) << dConfig.FRACTION_BITS
				&& m_vX <= 0) {
			m_x = (camBox[LEFT] + Math.abs(width)) << dConfig.FRACTION_BITS;
			m_vX = m_aX = 0;
		} else if (m_x >= (camBox[RIGHT] - Math.abs(width)) << dConfig.FRACTION_BITS
				&& m_vX >= 0) {
			m_x = (camBox[RIGHT] - Math.abs(width)) << dConfig.FRACTION_BITS;
			m_vX = m_aX = 0;
		}
	}

	/*******************
	 * ��������
	 ************/

	public void checkInvincible() {
		m_invincible--;
		if (m_invincible <= 0) {
			m_invincible = 0;
		}

	}

	/**************************
       *
       *************************/
	/**
	 * ���ö���,��������ײ���λ�ò���
	 * 
	 * @param newaction
	 * @param direction
	 */
	public void setActionWithCollision(int newaction, int xAlign, int yAlign) {
		if (xAlign != -1) {
			m_x += aniPlayer.boxInf[xAlign] << dConfig.FRACTION_BITS;
		}

		if (yAlign != -1) {
			m_y += aniPlayer.boxInf[yAlign] << dConfig.FRACTION_BITS;
		}

		// m_nextAction = newaction;
		setAnimAction(newaction);

		if (xAlign != -1) {
			m_x -= aniPlayer.boxInf[xAlign] << dConfig.FRACTION_BITS;
		}

		if (yAlign != -1) {
			m_y -= aniPlayer.boxInf[yAlign] << dConfig.FRACTION_BITS;
		}
	}

	/****************************
	 * ������⴦��
	 *****************************/

	int m_modvX;
	int m_modvY;
	short[] m_colBox = new short[4];
	short[] m_moveArea = new short[4];
	boolean m_useMoveArea;

	// Input
	public int m_phyAttrib; // ��������Ե����Բ���
	public int m_phyEvx; // carrier modified vx Я�����޸�v�ٶ�
	public int m_phyEvy; // carrier modified vy Я�����޸�y�ٶ�
	boolean m_flipGravity; // ����Ч��

	// Output
	int m_phyResult; // engine result
	int m_phyColObjectIdX; // colliding actor id
	int m_phyColObjectIdY; // colliding actor id

	private int[] s_phyMoveBox = new int[4];
	private int[] s_phyRealBox = new int[4];

	private short[] _ld_nextbox = new short[4];

	// 24-31,
	public final static int PHYATTRIB_COLENGINE = 1 << 24; // �Ƿ������ײ���ı�־λ
	final static int PHYATTRIB_KEEPRESULT = 1 << 26;

	// 0-7, basic �������Ļ�������
	public final static int PHYATTRIB_NONE = 0;
	public final static int PHYATTRIB_GRAVITY = 1;
	public final static int PHYATTRIB_LANDFORM = 1 << 1;
	public final static int PHYATTRIB_OBJECT = 1 << 2;
	public final static int PHYATTRIB_AVOIDFALLING = 1 << 3;
	public final static int PHYATTRIB_SYNSPEED = 1 << 4;

	// 8-23, �����������õ�һ����
	public final static int PHYATTRIB_NOTUPDATEPOS = 1 << 5;
	public final static int PHYATTRIB_NOTUPDATE_VY = 1 << 6;
	public final static int PHYRESULT_NOCHECK_BLOCKY = 1 << 7;

	// RESULT 0-31, �������������з��ص����н��
	public final static int PHYRESULT_NONE = 0;
	public final static int PHYRESULT_COL_FALLING = 1;
	public final static int PHYRESULT_COL_AVOIDFALLING = 1 << 1;
	public final static int PHYRESULT_COL_LANDFORMX = 1 << 2;
	public final static int PHYRESULT_COL_LANDFORMY = 1 << 3;
	public final static int PHYRESULT_LANDFORM_SLOPEDOWN = 1 << 6;
	public final static int PHYRESULT_LANDFORM_SLOPEUP = 1 << 7;
	public final static int PHYRESULT_LANDFORM_STEEPSLOPE = 1 << 8;
	public final static int PHYRESULT_COL_LANDFORMY_DOWN = 1 << 9;

	// RESULT, combinations
	private final static int PHYRESULT_COL_LANDFORM = PHYRESULT_COL_LANDFORMX
			| PHYRESULT_COL_LANDFORMY;

	// ��ͼ��Ĵ�С
	private final static int PHYPARAM_TILESIZE = MapData.TILE_WIDTH;
	private final static int PHYPARAM_TILESHIFT = PHYPARAM_TILESIZE == 16 ? 4
			: 3;

	private final static int PHYPARAM_FALLING_DEPTH = (PHYPARAM_TILESIZE + PHYPARAM_TILESIZE / 4) << dConfig.FRACTION_BITS;

	public void initPhy() {
		m_phyAttrib = 0;
		m_phyEvx = 0;
		m_phyEvy = 0;
		// result
		m_phyResult = PHYRESULT_NONE;
		pcbSetMoveArea(null);
	}

	/**
	 * �����������flag
	 */
	public void clearPhyFlag() {
		if ((m_phyAttrib & PHYATTRIB_COLENGINE) != 0) {
			m_phyAttrib &= ~PHYATTRIB_COLENGINE;
		}
	}

	/**
	 * ��������ײ��ı䣬�����ٶ�û�иı�����
	 * 
	 * @param box
	 *            short[]
	 */
	protected void pcbSetMoveArea(short[] box) {
		// default move area
		if (box == null) {
			m_moveArea[0] = -0x7fff;
			m_moveArea[1] = -0x7fff;
			m_moveArea[2] = 0x7fff;
			m_moveArea[3] = 0x7fff;
			m_useMoveArea = false;
		} else {
			System.arraycopy(box, 0, m_moveArea, 0, 4);
			m_useMoveArea = true;
		}
	}

	/***********************************
	 * �������logic
	 ************************************/

	public void doPhysics() {
		/**
		 * �����Ҫ������������������߼�
		 */
		if ((m_phyAttrib & PHYATTRIB_COLENGINE) != 0) {
			// getActorBoxforPhy ();
			/***************************
			 * ����������߼�
			 ***************************/
			phyEngine();
			/**
			 * @param <any> BOX_COLLIDE
			 */
			getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);

			if (m_modvX != 0) {

				s_colBox1[0] += mechStep(m_modvX) >> dConfig.FRACTION_BITS;
				s_colBox1[2] += mechStep(m_modvX) >> dConfig.FRACTION_BITS;
			}

			if (m_modvY != 0) {

				s_colBox1[1] += mechStep(m_modvY) >> dConfig.FRACTION_BITS;
				s_colBox1[3] += mechStep(m_modvY) >> dConfig.FRACTION_BITS;
			}
			getSurroundingEnvInfo(s_colBox1, getFaceDir(), 0);

		} else {
			m_phyResult = PHYRESULT_NONE;
			m_modvX = mechStep(m_vX);
			m_modvY = mechStep(m_vY);
		}
		updatePostion();
		doCollionWithActor();
		m_phyEvx = 0;
		m_phyEvy = 0;
	}

	public static final int MASK_INTEGER = 0xffffff00;

	public void updatePostion() {
		if (this == CGame.m_hero) {
			if (m_vY <= 0 && (m_vY + mechStep(m_aY)) > 0) {
				CGame.m_hero.jumpTopY = m_y;
			}
		}
		int m_yTemp = m_y;
		/**
		 * �����Ҫ����λ������и��¾���������
		 */
		if ((m_phyAttrib & PHYATTRIB_NOTUPDATEPOS) == 0) {
			m_x += mechStep(m_modvX);
			m_y += mechStep(m_modvY);
			m_vX += mechStep(m_aX);
			m_vY += mechStep(m_aY);

		}
		m_modvX = m_modvY = 0;
		if (this == CGame.m_hero && m_y != m_yTemp) {
			CGame.m_hero.shadowTick = 5;
		}
	}

	public void updatePostionNophy() {
		/**
		 * �����Ҫ����λ������и��¾���������
		 */

		m_x += mechStep(m_vX);
		m_y += mechStep(m_vY);
		m_vX += mechStep(m_aX);
		m_vY += mechStep(m_aY);

	}

	public void droptoGround() {
		// if(!testClasssFlag (dActor.CLASS_FLAG_CHECK_ENV) ||
		// m_actorProperty[PRO_PATH_END] == 0) {
		// return;
		// }
		m_vY = 100 << dConfig.FRACTION_BITS;
		doPhysics();
		m_vY = 0;
	}

	public void turnAround() {
		// must keep the box unchanged
		int left = aniPlayer.boxInf[Player.INDEX_COLLISION_BOX_LEFT];
		int right = aniPlayer.boxInf[Player.INDEX_COLLISION_BOX_RIGHT];

		m_x += ((left + right) << dConfig.FRACTION_BITS);
		aniPlayer.setActorBoxInfo(Player.INDEX_COLLISION_BOX_LEFT, -right);
		aniPlayer.setActorBoxInfo(Player.INDEX_COLLISION_BOX_RIGHT, -left);

		left = aniPlayer.boxInf[Player.INDEX_COLLISION_BOX_FRONT];
		right = aniPlayer.boxInf[Player.INDEX_COLLISION_BOX_BACK];
		aniPlayer.setActorBoxInfo(Player.INDEX_COLLISION_BOX_FRONT, -left);
		aniPlayer.setActorBoxInfo(Player.INDEX_COLLISION_BOX_BACK, -right);

		left = aniPlayer.boxInf[Player.INDEX_ATTACK_BOX_LEFT];
		right = aniPlayer.boxInf[Player.INDEX_ATTACK_BOX_RIGHT];
		aniPlayer.setActorBoxInfo(Player.INDEX_ATTACK_BOX_LEFT, -left);
		aniPlayer.setActorBoxInfo(Player.INDEX_ATTACK_BOX_RIGHT, -right);

		setFaceDir(-getFaceDir());
		aniPlayer.setSpriteFlipX(getFaceDir());

		m_vX = -m_vX;
		m_aX = -m_aX;

	}

	/**
	 * ����
	 */
	// ֻ������ʹ��
	// public boolean m_checkBlockY = false;

	public boolean m_bInDeadZone; // ������ͼ��
	public boolean m_bBlockedFront; // ǰ�赲
	public boolean m_bBlockedFrontSolid; // ǰ�赲,�赲����solid
	public boolean m_bBlockedFrontTop; // ǰ���赲
	public boolean m_bBlockedBack; // ���赲
	public boolean m_bBlockedBackSolid; // ���赲,�赲����solid
	public boolean m_bBlockedTop; // ͷ���ϲ��赲
	public boolean m_bBlockedBottom; // �����赲
	public boolean m_bBlockedBottomSolid; // �����赲,�赲����solid

	public boolean m_bBlockedBottomCenter; // ���������赲
	public boolean m_bBlockedBottomFront; // ����ǰ�赲
	public boolean m_bBlockedBottomBack; // ���º��赲
	public boolean m_bBlackedActorFront;
	public boolean m_bBlackedActorBack;

	public boolean m_bBlockedActorBottom;
	public boolean m_bBlockedActorTop;

	boolean m_bReboundable; // ����

	boolean m_bCanClimbUp; // ſס
	boolean m_bCanClimbLowUp; // �Ϸ�
	int m_climbupPointX;
	int m_climbupPointY;

	int m_climbupActorPointX;
	int m_climbupActorPointY;

	boolean m_bCanClimbDown;
	boolean m_bCanGoStraight;

	public boolean m_bCanClimbUpLadder; // �Ƿ����������������
	public boolean m_bCanClimbDownLadder; // �Ƿ��������������

	int m_climbdownPointX;
	int m_climbdownPointY;
	int m_climbdownFaceDir;

	boolean m_bVWallingable; // ������ˮƽǽ��������
	int m_vWallingLineX; // �����ڴ�ֱǽ��������

	// int m_frayFaceDir;
	// int m_frayPointX;
	// int m_frayPointY;

	boolean m_bHWallingable;
	int m_hideX; // ����X����

	public final static int LEFT = 0;
	public final static int TOP = 1;
	public final static int RIGHT = 2;
	public final static int BOTTOM = 3;

	// temp here
	public static final byte PHY_FLAG_TILE_BOX = 0x40;
	public static final byte PHY_FLAG_WALL_EDGE = 0x20;

	public void initSurroundingPro() {
		// reset info variables
		m_bInDeadZone = false;
		m_bBlockedFront = false;
		m_bBlockedBack = false;
		m_bBlockedTop = false;
		m_bBlockedBottom = false;
		m_bBlockedBottomCenter = false;
		m_bBlockedBottomFront = false;
		m_bBlockedBottomBack = false;
		m_bReboundable = false;
		m_bCanClimbDown = false;
		m_bCanClimbUp = false;
		m_bCanClimbLowUp = false;
		m_bVWallingable = false;
		m_bHWallingable = false;
		m_bCanGoStraight = true;
		m_bCanClimbUpLadder = false;
		m_bCanClimbDownLadder = false;
		m_bBlockedFrontTop = false;
		m_bBlackedActorFront = false;
		m_bBlackedActorBack = false;
		m_bBlockedActorBottom = false;
		m_bBlockedActorTop = false;
		m_bBlockedBackSolid = false;
		m_bBlockedFrontSolid = false;
	}

	/**
	 *��ײ������Χ����������.
	 * 
	 * @param box
	 *            short[]
	 * @param faceDir
	 *            int generally, box is the collision of some a actor, the value
	 *            of faceDir represents the face dir of that actor.
	 * @param checkFlags
	 *            int reserved
	 */
	public void getSurroundingEnvInfo(short[] box, int faceDir, int checkFlags) {
		/**
		 * ��ʼ�����Բ���
		 */
		initSurroundingPro();
		// ******************************************************************************************
		/**
		 * ���������ͼ�߽�������Ϊ������ͼ��
		 */
		if (box[BOTTOM] >= CGame.gMap.mapRes.m_mapTotalHeightByPixel - 1
				|| box[BOTTOM] < 1) {
			m_bInDeadZone = true;
			return;
		}

		if (box[TOP] < 1) {
			box[TOP] = 1;
		}

		/*******************************
		 * ���������ͼ��߽� ���ݳ����ж�ǰ���赲
		 ************************************/
		if (box[LEFT] <= 1) {
			if (faceDir > 0) {
				m_bBlockedBack = true;
				m_bBlockedBackSolid = true;
			} else {
				m_bBlockedFront = true;
				m_bBlockedFrontSolid = true;
				m_bBlockedFrontTop = true;
			}
		}

		if (box[RIGHT] >= CGame.gMap.mapRes.m_mapTotalWidthByPixel - 1) {
			if (faceDir < 0) {
				m_bBlockedBack = true;
				m_bBlockedBackSolid = true;
			} else {
				m_bBlockedFront = true;
				m_bBlockedFrontSolid = true;
				m_bBlockedFrontTop = true;
			}
		}
		// *************************************************************************//
		// ********************ȡ�ø����ߵ�tile��ֵ************************************************
		int col_left = (box[LEFT]) / MapData.TILE_WIDTH;
		int col_right = (box[RIGHT]) / MapData.TILE_WIDTH;
		int row_top = (box[TOP]) / MapData.TILE_WIDTH;
		int row_bottom = (box[BOTTOM]) / MapData.TILE_WIDTH;
		int col_center = (box[LEFT] + box[RIGHT]) / (MapData.TILE_WIDTH << 1);
		int row_center = (box[TOP] + box[BOTTOM]) / (MapData.TILE_WIDTH << 1);
		// *******************����һЩ�ٽ����************************
		int outer_col_left = (box[LEFT] - 1) / MapData.TILE_WIDTH;
		int outer_col_right = (box[RIGHT] + 1) / MapData.TILE_WIDTH;
		int outer_row_top = (box[TOP] - 1) / MapData.TILE_WIDTH;
		int outer_row_bottom = (box[BOTTOM] + 1) / MapData.TILE_WIDTH;

		int col_front, col_back;
		int outer_col_front, outer_col_back;
		int patch_x;
		/**
		 * ���ݳ��� �жϼ���ǰ���ͺ�
		 */
		if (faceDir > 0) {
			col_front = col_right;
			col_back = col_left;
			outer_col_front = outer_col_right;
			outer_col_back = outer_col_left;
			patch_x = MapData.TILE_WIDTH - 1;
		} else {
			col_front = col_left;
			col_back = col_right;
			outer_col_front = outer_col_left;
			outer_col_back = outer_col_right;
			patch_x = 0;
		}

		// checking ...
		int tile_index_top = row_top * CGame.gMap.mapRes.m_mapWidthByTile;
		int tile_index_bottom = row_bottom * CGame.gMap.mapRes.m_mapWidthByTile;
		int tile_index;
		int tile_value;
		int tiel_value1;
		int tile_index1;
		int row, col, turn;

		/***************************
		 * ���ˮƽ����������ߵ������
		 ******************************/
		// h-walling / dead zone
		tile_index = tile_index_bottom + col_center;
		tile_value = 0;
		try {
			tile_value = CGame.gMap.mapRes.m_phyData[tile_index];
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE) == MapData.PHY_DEAD) {
			m_bInDeadZone = true;
			return;
		}

		tile_index = tile_index_bottom + col_front;
		tile_value = CGame.gMap.mapRes.m_phyData[tile_index];
		/**********************************
		 * ��ⴹֱ������Կ������ߵ������
		 **********************************/
		if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE) == MapData.PHY_V_WALLING) {
			tile_index += faceDir;
			tile_value = CGame.gMap.mapRes.m_phyData[tile_index];
			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) >= MapData.PHY_SOLID) {
				m_bVWallingable = true;
				m_vWallingLineX = (col_front * MapData.TILE_WIDTH + patch_x) << dConfig.FRACTION_BITS;
			}
		}

		/**
		 * �������� ���ݳ����ǰ�����
		 */
		// check climb up \ block \ rebound in front
		tile_index = tile_index_top + outer_col_front;
		row = row_top - 1;
		while (row != row_bottom) {
			++row;
			tile_value = CGame.gMap.mapRes.m_phyData[tile_index];
			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) >= MapData.PHY_SOLID) {
				m_bBlockedFront = true;

				if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) == MapData.PHY_SOLID) {
					m_bBlockedFrontSolid = true;
				}

				if (row == row_top) {
					m_bBlockedFrontTop = true;
				}

				if (row == row_bottom) {
					m_bReboundable = true;
				}
			}
			tile_index += CGame.gMap.mapRes.m_mapWidthByTile;
		}

		tile_index = tile_index_top + outer_col_front;
		row = row_top - 1;

		/********************************************
		 * ����Ƿ��������
		 **************************************/

		while (row != row_center) {
			++row;
			tile_value = CGame.gMap.mapRes.m_phyData[tile_index];
			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE) == MapData.PHY_WALL_EDGE) {
				if ((CGame.gMap.mapRes.m_phyData[tile_index
						- CGame.gMap.mapRes.m_mapWidthByTile] & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) < MapData.PHY_SOLID) {
					m_bCanClimbUp = true;
					m_climbupPointX = (col_front * MapData.TILE_WIDTH + patch_x) << dConfig.FRACTION_BITS;
					m_climbupPointY = (row * MapData.TILE_HEIGHT - 1) << dConfig.FRACTION_BITS;
				}
			} else if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE) == MapData.PHY_LAW_WALL) {
				if ((CGame.gMap.mapRes.m_phyData[tile_index
						- CGame.gMap.mapRes.m_mapWidthByTile] & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) < MapData.PHY_SOLID) {
					m_bCanClimbLowUp = true;
					m_climbupPointX = (col_front * MapData.TILE_WIDTH + patch_x) << dConfig.FRACTION_BITS;
					m_climbupPointY = (row * MapData.TILE_HEIGHT - 1) << dConfig.FRACTION_BITS;
				}
			}

			tile_index += CGame.gMap.mapRes.m_mapWidthByTile;
		}

		// check climb down \ block below
		// ��ǰ���󣬼��ŵ�һ��
		tile_index = tile_index_bottom + col_front;

		if (outer_row_bottom > row_bottom) {
			tile_index += CGame.gMap.mapRes.m_mapWidthByTile;
		}

		col = col_front + faceDir;

		if ((CGame.gMap.mapRes.m_phyData[tile_index] & MapData.MASK___TILE_PHY_ENV_VALUE) == MapData.PHY_WALL_EDGE) {
			m_bCanGoStraight = false;
		}
		while (col != col_back) {
			col -= faceDir;
			// -----------------------------------------------------------------------------------------------------------------
			tile_value = CGame.gMap.mapRes.m_phyData[tile_index];
			/*************************************
			 * ���ǽ��Ե�Ƿ��������
			 ************************************/
			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE) == MapData.PHY_WALL_EDGE) {
				m_bCanClimbDown = true;

				m_climbdownFaceDir = (CGame.gMap.mapRes.m_phyData[tile_index
						+ faceDir] & MapData.MASK___TILE_PHY_ENV_VALUE) < MapData.PHY_SOLID ? -faceDir
						: faceDir;

				m_climbdownPointY = (outer_row_bottom
						* MapData.TILE_HEIGHT - 1) << dConfig.FRACTION_BITS;
				m_climbdownPointX = (col * MapData.TILE_WIDTH + (m_climbdownFaceDir > 0 ? -1
						: MapData.TILE_WIDTH)) << dConfig.FRACTION_BITS;
			}

			/******************************
			 * ���������� �������Ƿ��ǿ����������ӵĵ�ͼ��
			 ********************************/

			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE) == MapData.PHY_LADDER) {
				m_bCanClimbDownLadder = true; // ������������
				// ����λ������ ��ͼ������ĵ���
				m_climbupPointX = (col * MapData.TILE_WIDTH + (MapData.TILE_WIDTH >> 1)) << dConfig.FRACTION_BITS;
				m_climbupPointY = (row_bottom * MapData.TILE_HEIGHT
						+ MapData.TILE_HEIGHT - 1) << dConfig.FRACTION_BITS;
			}

			if (((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) >= MapData.PHY_SOLID && (tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) != MapData.PHY_HALF_BLOCK)
					|| isOnSlope(tile_value)
					|| (isPhyDown && (tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) == MapData.PHY_HALF_BLOCK)
			/*
			 * || (tile_value &
			 * CGame.gMap.mapRes.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) ==
			 * CGame.gMap.mapRes.PHY_LADDER
			 */
			) {
				m_bBlockedBottom = true;

				if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) == MapData.PHY_SOLID) {
					m_bBlockedBottomSolid = true;
				}

				if (col == col_front) {
					m_bBlockedBottomFront = true;
				}

				if (col == col_back) {
					m_bBlockedBottomBack = true;
				}
				if (col == col_center) {
					m_bBlockedBottomCenter = true;
				}

				if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE) == MapData.PHY_DEAD) {
					m_bInDeadZone = true;
				}

				// if(!m_bBlockedBottomFront) {
				// m_frayPointY = (outer_row_bottom *
				// CGame.gMap.mapRes.TILE_HEIGHT - 1) << dConfig.FRACTION_BITS;
				// m_frayPointX = (col * CGame.gMap.mapRes.TILE_WIDTH + (faceDir
				// == 1 ? CGame.gMap.mapRes.TILE_WIDTH : 0)) <<
				// dConfig.FRACTION_BITS;
				//
				// }
			}

			tile_index -= faceDir;
		}

		// check block on back
		tile_index = tile_index_top + outer_col_back;
		row = row_top - 1;
		while (row != row_bottom) {
			++row;
			tile_value = CGame.gMap.mapRes.m_phyData[tile_index];

			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) >= MapData.PHY_SOLID) {
				m_bBlockedBack = true;
			}

			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) == MapData.PHY_SOLID) {
				m_bBlockedBackSolid = true;
			}

			tile_index += CGame.gMap.mapRes.m_mapWidthByTile;
		}

		// check block on front
		tile_index = tile_index_top + col_front + faceDir;
		row = row_top - 1;
		while (row != row_bottom) {
			++row;
			tile_value = CGame.gMap.mapRes.m_phyData[tile_index];

			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) >= MapData.PHY_SOLID) {
				m_bCanGoStraight = false;
			}

			tile_index += CGame.gMap.mapRes.m_mapWidthByTile;
		}

		// check climb up \ block on top
		tile_index = tile_index_top + col_front;
		tile_index1 = tile_index_top + col_front;

		if (outer_row_top < row_top) {
			tile_index -= CGame.gMap.mapRes.m_mapWidthByTile;
		}

		col = col_front + faceDir;

		while (col != col_back) {
			col -= faceDir;
			tile_value = CGame.gMap.mapRes.m_phyData[tile_index];
			tiel_value1 = CGame.gMap.mapRes.m_phyData[tile_index1];

			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE) == MapData.PHY_WALL_EDGE) {
				if ((CGame.gMap.mapRes.m_phyData[tile_index + faceDir] & MapData.MASK___TILE_PHY_ENV_VALUE) >= MapData.PHY_SOLID
						&& col != col_back) {
					m_bCanClimbUp = true;

					m_climbupPointY = (outer_row_top
							* MapData.TILE_HEIGHT - 1) << dConfig.FRACTION_BITS;
					m_climbupPointX = (col * MapData.TILE_WIDTH + (faceDir > 0 ? -1
							: MapData.TILE_WIDTH)) << dConfig.FRACTION_BITS;
				}
			}
			/**
			 * ���������� ���ͷ���ϲ��Ƿ���������������Եĵ�ͼ��
			 */
			if ((tiel_value1 & MapData.MASK___TILE_PHY_ENV_VALUE) == MapData.PHY_LADDER) {
				m_bCanClimbUpLadder = true; // ������������
				// ����λ������ ��ͼ������ĵ���
				m_climbupPointX = (col * MapData.TILE_WIDTH + (MapData.TILE_WIDTH >> 1)) << dConfig.FRACTION_BITS;
				m_climbupPointY = ((row + 1) * MapData.TILE_HEIGHT - 1) << dConfig.FRACTION_BITS;
			}

			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) >= MapData.PHY_SOLID
					&& (tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) != MapData.PHY_HALF_BLOCK) {
				m_bBlockedTop = true;
			}

			tile_index -= faceDir;
			tile_index1 -= faceDir;
		}

		// for bug
		if (m_bBlockedTop && m_bCanClimbUp) {
			tile_index += faceDir; // col_back
			tile_value = CGame.gMap.mapRes.m_phyData[tile_index];
			if ((tile_value & MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG) >= MapData.PHY_SOLID) {
				m_bCanClimbUp = false;
			}
		}

	}

	/**
	 * ����Ƿ���б����
	 * 
	 * @param value
	 *            int
	 * @return boolean
	 */
	static boolean isOnSlope(int value) {
		if ((value & MapData.MASK___TILE_PHY_ENV_VALUE) >= MapData.PHY_SLOP3_1
				&& (value & MapData.MASK___TILE_PHY_ENV_VALUE) <= MapData.PHY_SLOP2_2) {
			return true;
		}
		return false;
	}

	protected void phyEngine() {
		/********************
		 * ������������Ϣ ��0
		 ***********************/
		m_phyResult = PHYRESULT_NONE;
		getActorBoxInfo(dActor.BOX_COLLIDE, m_colBox);
		// ���û����ײ�����򲻽����������
		if (m_colBox[0] == m_colBox[2] || m_colBox[1] == m_colBox[3]) {
			return;
		}
		/******************************
		 * �õ���ʵ���ٶ�ƫ��
		 ******************************/
		int realVX, realVY;
		realVX = m_vX + m_phyEvx /* + m_Lift_Vx */;
		realVY = m_vY + m_phyEvy;

		/****************************
		 *����ٶȵ���0
		 ****************************/
		if (m_vY == 0 && (m_phyAttrib & PHYATTRIB_GRAVITY) != 0) {
			m_phyResult |= PHYRESULT_COL_FALLING;
			realVY = m_flipGravity ? -PHYPARAM_FALLING_DEPTH
					: PHYPARAM_FALLING_DEPTH;
		}

		/**************************
		 * �����ٶ����ж϶���� ���� ���׻���������
		 **************************/
		int dirX, dirY;
		dirX = (realVX >= 0) ? 2 : 0;
		dirY = (realVY >= 0) ? 3 : 1;

		// �õ��ƶ���box�Ĵ�С
		System.arraycopy(m_colBox, 0, _ld_nextbox, 0, 4);
		_ld_nextbox[dirX] += (realVX >> dConfig.FRACTION_BITS);
		_ld_nextbox[dirY] += (realVY >> dConfig.FRACTION_BITS);

		int ret;
		/****************************
		 * ������Ҫ���Ľ��
		 ****************************/
		ret = phyDetect(m_colBox, _ld_nextbox, dirX, dirY);

		m_phyResult |= ret;

		s_phyMoveBox[0] = _ld_nextbox[0];
		s_phyMoveBox[1] = _ld_nextbox[1];
		s_phyMoveBox[2] = _ld_nextbox[2];
		s_phyMoveBox[3] = _ld_nextbox[3];

		if ((m_phyAttrib & PHYATTRIB_OBJECT) != 0) {
			// ret = phyBlockObjectDetect(m_colBox , _ld_nextbox , dirX , dirY);
			m_phyResult |= ret;
		}
		// ------------------------------------------------------
		s_phyRealBox[0] = m_colBox[0];
		s_phyRealBox[1] = m_colBox[1];
		s_phyRealBox[2] = m_colBox[2];
		s_phyRealBox[3] = m_colBox[3];
		// -------------------------------------------------------
		s_phyMoveBox[0] = _ld_nextbox[0];
		s_phyMoveBox[1] = _ld_nextbox[1];
		s_phyMoveBox[2] = _ld_nextbox[2];
		s_phyMoveBox[3] = _ld_nextbox[3];
		// ----------------------------------------------------------
		int tx0, ty0, tx1, ty1, tx2, ty2;
		int xp, yp;
		//
		/****************************
		 * �õ����ص���ʱ���
		 ***************************/
		int px, py;
		//
		px = s_phyMoveBox[dirX] - s_phyRealBox[dirX];
		py = s_phyMoveBox[dirY] - s_phyRealBox[dirY];
		//
		/**************************
		 * ���ݷ�������ƫ�� ʹ�ٶ�ͬ��
		 **************************/
		if ((m_phyAttrib & PHYATTRIB_SYNSPEED) != 0) {
			if ((m_phyResult & (PHYRESULT_COL_LANDFORM /*
														 * |
														 * PHYRESULT_COL_OBJECT
														 */)) != 0) {
				if (px == 0) {
					py = 0;
				} else if (py == 0) {
					px = 0;
				} else {
					xp = realVX / px;
					yp = realVY / py;
					// all regular
					if (xp > (1 << dConfig.FRACTION_BITS)
							&& yp > (1 << dConfig.FRACTION_BITS)) {
						if (xp < yp) {
							px = realVX / yp;
						} else if (xp > yp) {
							py = realVY / xp;
						}
					} else {
						px = 0;
						py = 0;
					}
				}
			}
		}
		//
		/**
		 * ���䷵�صĽ��
		 */
		boolean fallingSpeed = (m_phyResult & PHYRESULT_COL_FALLING) != 0;
		boolean fullfalling = false;
		/**
		 * ����һЩ��ֹ��������
		 */
		if (fallingSpeed) {
			if (py == (realVY >> dConfig.FRACTION_BITS)) {
				fullfalling = true;
				// forbid falling
				if ((m_phyAttrib & PHYATTRIB_AVOIDFALLING) != 0) {
					px = 0;
					py = 0;
					m_phyResult &= ~PHYRESULT_COL_FALLING;
					m_phyResult |= PHYRESULT_COL_AVOIDFALLING;
				}
			} else {
				m_phyResult &= ~PHYRESULT_COL_FALLING;
			}
		}
		//
		// ////////////////////////////////////////////////////////////
		/************************************************
		 * б������� б���λ�õ���ֻ��Ҫ���ƶ�������״̬
		 ***********************************************/
		if (m_vY >= 0) {
			// reuse xp,yp,ty1
			xp = (m_x >> dConfig.FRACTION_BITS) + px;
			yp = (m_y >> dConfig.FRACTION_BITS) + py;
			/*************************
			 * �õ�б���ϵĶ����λ��
			 **************************/
			ty1 = pcbGetSlopePos(xp, yp);
			if (ty1 >= 0) {
				py += ty1 - yp - 1;
				m_phyResult |= PHYRESULT_COL_LANDFORMY;
			}
		}
		// ////////////////////////////////////////////////////////////
		// Get result
		if (realVX >> dConfig.FRACTION_BITS == px) {
			m_modvX = realVX;
		} else {
			m_modvX = px << dConfig.FRACTION_BITS;
			// m_modvY = py << dConfig.FRACTION_BITS;
		}

		if (realVY >> dConfig.FRACTION_BITS == py) {
			m_modvY = realVY;
		} else {
			m_modvY = py << dConfig.FRACTION_BITS;
		}
	}

	private final static int PHYPARAM_TILEMASK = 0xf;

	public static final byte PHYSHIFT_SLOPE3_START = 6;
	public static final byte PHYSHIFT_SLOPE2_START = 9;
	public static final int PHYSHIFT_SLOPE3 = /* 0x70 */1 << MapData.PHY_SLOP3_1
			| 1 << MapData.PHY_SLOP3_2 | 1 << MapData.PHY_SLOP3_3; // 1:3 б��
	public static final int PHYSHIFT_SLOPE2 = /* 0x300 */1 << MapData.PHY_SLOP2_1
			| 1 << MapData.PHY_SLOP2_2; // 1:2 б��
	public static final int PHYSHIFT_SLOPE = /* 0x300 */1 << MapData.PHY_SLOP; // 1:1
																				// б��
	public static final int PHYSHIFT_SLOPE_PURE = PHYSHIFT_SLOPE2
			| PHYSHIFT_SLOPE3 | PHYSHIFT_SLOPE; // ����б��
	public static final int PHYSHIFT_SLOPE_SQUARE = 0x80; // square slope
	public static int PHYSHIFT_BLOCKY_DOWN = 1 << MapData.PHY_HALF_BLOCK;

	public static final int PHYSHIFT_SLOPE_ALL = PHYSHIFT_SLOPE_PURE
			| PHYSHIFT_SLOPE_SQUARE; // all type slope
	public static int PHYSHIFT_BLOCKY = (1 << MapData.PHY_SOLID)
			| (1 << MapData.PHY_OUT) | (1 << MapData.PHY_WALL_EDGE)
			| (1 << MapData.PHY_SLOPE_BASE) | (1 << MapData.PHY_LAW_WALL) /*
																		 * |
																		 * (1<<
																		 * MapData
																		 * .
																		 * PHY_HALF_BLOCK
																		 * )
																		 */; // slope,
																				// solid,
																				// out,
																				// thorn,
																				// lake

	public static int PHYSHIFT_BLOCKX = (1 << MapData.PHY_SOLID)
			| (1 << MapData.PHY_OUT) | (1 << MapData.PHY_WALL_EDGE)
			| (1 << MapData.PHY_LAW_WALL); // solid, out , climb

	private final static short[] s_tileoffset = { 1, 1, 0, 0 };
	private final static short[] s_pixeloffset = { 0, 0, -1, -1 };

	protected boolean phyStaticDetect(short[] oldColBox, short[] newColBox) {
		boolean top_left_in_wall = false;
		boolean top_right_in_wall = false;
		short[] noModColBox = new short[4];
		System.arraycopy(newColBox, 0, noModColBox, 0, 4);
		if (oldColBox == null) {
			oldColBox = new short[4];

			top_left_in_wall = ((1 << CGame.gMap.mapRes.getTilePhyEnv(
					newColBox[LEFT] / MapData.TILE_WIDTH, newColBox[TOP]
							/ MapData.TILE_HEIGHT)) & PHYSHIFT_BLOCKX) != 0;
			top_right_in_wall = (1 << CGame.gMap.mapRes.getTilePhyEnv(
					newColBox[RIGHT] / MapData.TILE_WIDTH, newColBox[TOP]
							/ MapData.TILE_HEIGHT) & PHYSHIFT_BLOCKX) != 0;

			if (top_left_in_wall == top_right_in_wall) {
				oldColBox[LEFT] = oldColBox[RIGHT] = (short) ((newColBox[LEFT] + newColBox[RIGHT]) / 2);
				oldColBox[TOP] = oldColBox[BOTTOM] = (short) ((newColBox[TOP] + newColBox[BOTTOM]) / 2);
			} else if (top_left_in_wall) {
				oldColBox[LEFT] = oldColBox[RIGHT] = newColBox[RIGHT];
				oldColBox[TOP] = oldColBox[BOTTOM] = newColBox[TOP];
			} else {
				oldColBox[LEFT] = oldColBox[RIGHT] = newColBox[LEFT];
				oldColBox[TOP] = oldColBox[BOTTOM] = newColBox[TOP];
			}
		}

		int tx0, ty0, tx1, ty1;
		int modix = LEFT;
		int ix, iy;
		tx0 = oldColBox[LEFT] / MapData.TILE_WIDTH;
		tx1 = newColBox[RIGHT] / MapData.TILE_WIDTH;

		ty0 = oldColBox[TOP] / MapData.TILE_WIDTH;
		ty1 = newColBox[BOTTOM] / MapData.TILE_WIDTH;
		ix = 1;
		iy = 1;
		if (top_left_in_wall) {
			ix = -1;
			modix = LEFT;
		}

		if (top_right_in_wall) {
			ix = 1;
			modix = RIGHT;
		}
		int xp = tx0;
		int yp = ty0;
		int tilepipe;
		while (xp != tx1 + 1) {
			yp = ty0;
			while (yp != ty1 + 1) {
				tilepipe = pcbGetTileAttrib(xp, yp, m_phyAttrib);
				if ((tilepipe & PHYRESULT_COL_LANDFORMX) != 0) {
					newColBox[modix] = (short) ((xp + s_tileoffset[modix] << PHYPARAM_TILESHIFT) + s_pixeloffset[modix]);
					break;
				}
				yp += iy;
			}

			xp += ix;
		}

		int deltaX = newColBox[modix] - noModColBox[modix];
		if (deltaX != 0) {
			m_x += deltaX << dConfig.FRACTION_BITS;
			return true;
		}

		return false;

	}

	protected int phyDetect(short[] bodybox, short[] nextbox, int modix,
			int modiy) {
		// clear result
		int ret = 0;

		/**
		 * �������boxû���κ��������κδ���
		 */

		if (nextbox[modix] == bodybox[modix]
				&& nextbox[modiy] == bodybox[modiy]) {
			return ret;
		}

		// ////////////////////////////////////////////////////////////
		// Moving area

		if (m_useMoveArea) {

			if (nextbox[modix] < m_moveArea[0]
					|| nextbox[modix] > m_moveArea[2]) {
				nextbox[modix] = m_moveArea[modix];
				ret |= PHYRESULT_COL_LANDFORMX;
			}

			if (nextbox[modiy] < m_moveArea[1]
					|| nextbox[modiy] > m_moveArea[3]) {
				nextbox[modiy] = m_moveArea[modiy];
				ret |= PHYRESULT_COL_LANDFORMY;
			}
			pcbSetMoveArea(null);
		}

		// ////////////////////////////////////////////////////////////
		// �Ƚ�����
		//
		// p1_________
		// | | | -> dir
		// |____p0 |
		// |_________p2
		//
		// ����p1��p0֮�������

		//

		int ix, iy;

		ix = modix >> 1; // 1 or 0
		iy = modiy >> 1; // 1 or 0

		// ��ͼ��ÿ�����index
		int tx0, ty0, tx1, ty1, tx2, ty2;

		// p0, p1, p2

		tx0 = bodybox[modix] /*- ix*/;
		ty0 = bodybox[modiy] + iy;
		tx1 = bodybox[2 - modix] /*- 1+ ix*/;
		ty1 = bodybox[4 - modiy] /*- 1+ iy*/;

		tx2 = nextbox[modix] /*- ix*/;
		ty2 = nextbox[modiy] /*- iy*/;

		tx0 >>= PHYPARAM_TILESHIFT;
		tx1 >>= PHYPARAM_TILESHIFT;
		tx2 >>= PHYPARAM_TILESHIFT;
		ty0 >>= PHYPARAM_TILESHIFT;
		ty1 >>= PHYPARAM_TILESHIFT;
		ty2 >>= PHYPARAM_TILESHIFT;

		// reuse ix, iy as tile step
		ix = modix - 1; // 1 or -1
		iy = modiy - 2; // 1 or -1

		//
		// (xp,yp)��Ϊѭ��������ֵ
		int xp, yp;
		int tilepipe;

		int tilepipe_ext = 0;
		// int lastYP = -1;
		// whether no more tile on y-dir
		boolean checkX, checkY;
		/**
		 * ��startY1 == true ��ʾy����û��ƫ��
		 */
		checkX = (ty0 == ty2);
		/**
		 * ���y����û��ƫ�Ƽ�����ʼ��� tx0��ʼ
		 * 
		 * tx1___tx0___tx2 | | | ---->dir | | | |______|______|
		 * 
		 * 
		 * 
		 */
		xp = checkX ? (tx0 /* + ix */) : tx1;
		// (tx2, ty2) ��Ϊѭ���ݵķֽ�
		tx2 += ix;
		ty2 += iy;
		isPhyDown = iy == 1 ? true : false;
		// scanline testing
		while (xp != tx2) {
			// ����ͨ��starY1������p1��p0�ļ��
			// System.out.println ("xp >>>>>>" + xp);
			if (!checkX) {
				checkX = (xp == tx0 /* + ix */);
			}

			checkY = !checkX;

			yp = checkX ? ty1 : (ty0 /* + iy */);

			// y loop
			while (yp != ty2) {
				// test flag
				if (!checkY) {
					checkY = (yp == ty0 /* + iy */);
				}

				// ȡ�õ�ͼ������
				tilepipe = pcbGetTileAttrib(xp, yp, m_phyAttrib);
				// block yv
				if (checkY
						&& (((tilepipe & PHYRESULT_COL_LANDFORMY) != 0) || ((tilepipe & PHYRESULT_COL_LANDFORMY_DOWN) != 0 && iy == 1))) {
					/* m_checkBlockY = true; */
					ret |= PHYRESULT_COL_LANDFORMY;
					/**************************************
					 * �õ�Y�����赲�ĵ�ͼ�飬��nextbox��������
					 **************************************/
					nextbox[modiy] = (short) ((yp + s_tileoffset[modiy] << PHYPARAM_TILESHIFT) + s_pixeloffset[modiy]);
					ty2 = yp + iy;

					break;
				}
				// block x
				else if (checkX && (tilepipe & PHYRESULT_COL_LANDFORMX) != 0) {
					ret |= PHYRESULT_COL_LANDFORMX;
					/*************************************************
					 * �õ�X�����赲�ĵ�ͼ�飬��nextbox��������
					 ***************************************************/
					nextbox[modix] = (short) ((xp + s_tileoffset[modix] << PHYPARAM_TILESHIFT) + s_pixeloffset[modix]);
					return ret;
				}
				// step
				yp += iy;
			}

			// step
			xp += ix;
		}

		return ret;

	}

	protected int pcbGetTileAttrib(int tx, int ty, int attr) {
		int phy, ret;

		phy = 1 << CGame.gMap.mapRes.getTilePhyEnv(tx, ty);
		ret = PHYRESULT_NONE;

		if ((phy & PHYSHIFT_BLOCKX) != 0) {
			ret |= PHYRESULT_COL_LANDFORMX;
		}
		if ((phy & PHYSHIFT_BLOCKY) != 0) {
			ret |= PHYRESULT_COL_LANDFORMY;
		}
		// //�봩Խ��ͼ��
		if ((phy & PHYSHIFT_BLOCKY_DOWN) != 0) {
			ret |= PHYRESULT_COL_LANDFORMY_DOWN;
		}

		return ret;
	}

	protected int pcbGetSlopePos(int px, int py) {
		int tx, ty;

		tx = px >> PHYPARAM_TILESHIFT;
		ty = py >> PHYPARAM_TILESHIFT;

		int phy, phy_shift;
		phy = CGame.gMap.mapRes.getTilePhyEnv(tx, ty);
		phy_shift = 1 << phy;

		// not in a slope tile
		if ((phy_shift & PHYSHIFT_SLOPE_ALL) == 0) {
			// if( (py & PHYPARAM_TILEMASK) != 0){
			// return -1;
			// }

			ty = (py + 1) >> PHYPARAM_TILESHIFT;

			phy = CGame.gMap.mapRes.getTilePhyEnv(tx, ty);
			phy_shift = 1 << phy;

			if ((phy_shift & PHYSHIFT_SLOPE_ALL) == 0) {
				return -1;
			}
		}

		else if ((phy_shift & PHYSHIFT_SLOPE_SQUARE) != 0) {
			phy = CGame.gMap.mapRes.getTilePhyEnv(tx, --ty);
			phy_shift = 1 << phy;

			if ((phy_shift & PHYSHIFT_SLOPE_ALL) == 0) {
				// roll back
				++ty;
				phy = CGame.gMap.mapRes.getTilePhyEnv(tx, ty);
				phy_shift = 1 << phy;
			}
		}
		/****************************************
		 * �����б����
		 *********************************************/
		if ((phy_shift & PHYSHIFT_SLOPE_PURE) != 0) {
			px = px & PHYPARAM_TILEMASK;
			// ����Ϊԭʼ�� ����Ϊ��ת��
			// tile flip x, down as origin, up as flip
			if ((MapData.s_lastTileFlag & MapData.TILE_FLIP_MASK) == 0) {
				px = PHYPARAM_TILESIZE - px;
				m_phyResult |= PHYRESULT_LANDFORM_SLOPEDOWN;
			} else {
				m_phyResult |= PHYRESULT_LANDFORM_SLOPEUP;
			}
			// 1:3 slope
			//
			// 6 |\ -PHYSHIFT_SLOPE3_START
			// 5 | \
			// 4 |__\

			if ((phy_shift & PHYSHIFT_SLOPE3) != 0) {
				px = ((PHYSHIFT_SLOPE3_START - phy) << PHYPARAM_TILESHIFT) + px;
				py = ((ty + 1) << PHYPARAM_TILESHIFT) - (px / 3);
			}
			// 1:2 slope
			//
			// 9 |\ -PHYSHIFT_SLOPE2_START
			// 8 |_\
			else if ((phy_shift & PHYSHIFT_SLOPE2) != 0) {
				px = ((PHYSHIFT_SLOPE2_START - phy) << PHYPARAM_TILESHIFT) + px;
				py = ((ty + 1) << PHYPARAM_TILESHIFT) - (px / 2);
				// result
				m_phyResult |= PHYRESULT_LANDFORM_STEEPSLOPE;
			}
			// 1:1 slope
			// 10 |\ -PHYSHIFT_SLOPE2_START
			else {
				px = px;
				py = ((ty + 1) << PHYPARAM_TILESHIFT) - px;
			}

		} else { // if( (phy_shift & MapData.PHYSHIFT_SLOPE_SQUARE) != 0 )
			py = ty << PHYPARAM_TILESHIFT;
		}

		return py;

	}

	/***********************
	 * ��������
	 **************************/
	/**
	 * �ж��Ƿ����ж���
	 * 
	 * @param object
	 *            AbstractActor
	 * @return boolean
	 */
	public boolean isAttackedObj(CObject object) {
		if (object != null) {
			this.getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
			object.getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox2);
			return CTools.isIntersecting(s_colBox1, s_colBox2);
		}
		return false;
	}

	/**************************
	 * �ؼ�֡�Ĵ���
	 *********************/

	public final static byte KF_ACTION_INDEX = 0;
	public final static byte KF_DUR_INDEX = 1;
	public final static byte KF_DISX_INDEX = 2;
	public final static byte KF_DAMAGE_INDEX = 3;
	public final static byte KF_SELF_DIR_INDEX = 4; // lin ����ؼ�֡λ�Ƶķ����ɹ����߾���

	public final static int KEY_FRAME_LENGTH = 5;
	public int kFDataFromAttackor[] = new int[KEY_FRAME_LENGTH];

	/**
	 * 
	 * @param attActor
	 *            CObject
	 * @return boolean �Ƿ񱩻�
	 */
	public boolean setKeyFrame(CObject attActor) {
		boolean isCri = false;
		// ��չؼ�֡��Ϣ
		for (int i = 0; i < kFDataFromAttackor.length; i++) {
			kFDataFromAttackor[i] = 0xff;
		}
		// ȡ������ID
		kFDataFromAttackor[KF_ACTION_INDEX] = attActor.getKFHurtID();
		kFDataFromAttackor[KF_DUR_INDEX] = attActor.getKFDur();

		kFDataFromAttackor[KF_DISX_INDEX] = attActor.getKFMoveDis()
				* attActor.getFaceDir();

		int damage = 0;

		if (attActor == CGame.m_hero) {
			// ���ǵļ����˺�
			if (attActor.m_currentState == dHero.ST_ACTOR_ATTACK_SP) {
				short[] skillAffect;
				if (m_actionID == dHero.action_map[dHero.ST_ACTOR_JINK][dHero.JINK_INDEX]) {
					skillAffect = CGame.m_hero.skills[0].getAffectedPro();
				} else {
					skillAffect = CGame.m_hero.skills[1].getAffectedPro();
				}
				damage = skillAffect[Data.IDX_PRO_atk];
			} else {
				// ���ܱ���
				isCri = attActor.isCriAtt();
				damage = attActor.getKFDataReserve()
						+ attActor.m_actorProperty[PRO_INDEX_ATT]
						* (isCri ? 2 : 1);
			}

			if (CDebug.bForTest) {
				damage += 5000;
			}

		} else {
			damage = attActor.getKFDataReserve()
					+ attActor.m_actorProperty[PRO_INDEX_ATT];
		}
		kFDataFromAttackor[KF_DAMAGE_INDEX] = damage;
		kFDataFromAttackor[KF_SELF_DIR_INDEX] = attActor.getFaceDir();
		return isCri;
	}

	public int[] getKeyFrameData() {
		return kFDataFromAttackor;
	}

	// ȡ�����˶�������
	int getKFHurtID() {
		return aniPlayer.getAttackFrameHurtID(m_actionID,
				aniPlayer.actionSequenceID);
	}

	// ȡ���ӳ�ʱ��
	int getKFDur() {
		return aniPlayer.getAttackFrameSkipNum(m_actionID,
				aniPlayer.actionSequenceID);
	}

	// ȡ�ù���λ��
	int getKFMoveDis() {
		return aniPlayer.getAttackFrameMoveDistance(m_actionID,
				aniPlayer.actionSequenceID);
	}

	// ȡ����չ����
	int getKFDataReserve() {
		return aniPlayer.getAttackFrameReserve(m_actionID,
				aniPlayer.actionSequenceID);
	}

	public boolean isHaveKFDis() {
		return (getKFMoveDis() != 0);
	}

	// ���ù���֡��ʱ
	public void setKFDur() {
		aniPlayer.extDuration = getKFDur();
	}

	// lin �ؼ�֡ƫ�Ƶķ���
	int KFMoveDir;

	/**
	 * ����Ĺؼ�֡ƫ��
	 */
	public void setKFMoveDis() {
		if (getKFDataReserve() == 11) {
			if (testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV)) {
				this.m_phyEvx = (getKFMoveDis() << dConfig.FRACTION_BITS)
						* (-getFaceDir());
			} else {
				this.m_x += (getKFMoveDis() << dConfig.FRACTION_BITS)
						* (-getFaceDir());
			}
		} else {
			// ������ʱ�������й����߾����������ɳ������
			int dir = KFMoveDir == 0 ? getFaceDir() : KFMoveDir;
			if (testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV)) {
				this.m_phyEvx = (getKFMoveDis() << dConfig.FRACTION_BITS) * dir;
			} else {
				this.m_x += (getKFMoveDis() << dConfig.FRACTION_BITS) * dir;
			}
		}
	}

	public void setKFDurFromAttackor() {
		aniPlayer.extDuration = kFDataFromAttackor[KF_DUR_INDEX];
	}

	public void setKFMoveDisFromAttackor() {
		if (testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV)) {
			this.m_phyEvx = kFDataFromAttackor[KF_DISX_INDEX] << dConfig.FRACTION_BITS;
		} else {
			this.m_x += kFDataFromAttackor[KF_DISX_INDEX] << dConfig.FRACTION_BITS;
		}
	}

	boolean keyFrameHurt; // lin �ؼ�֡����������,�������������߼�

	public void doKeyFrame() {
		// ����ؼ�֡�߼�ִ��
		if (testFlag(dActor.FLAG_DO_KEYFRAME_LOGIC)) {
			clearFlag(dActor.FLAG_DO_KEYFRAME_LOGIC);
			// ���ѣ��
			clearAdditionAttack(AA_YUN);
			if (beAttack(kFDataFromAttackor[KF_DAMAGE_INDEX])
					&& !testFlag(dActor.FLAG_FROZEN)) {
				// �������˶���
				setAnimAction(kFDataFromAttackor[KF_ACTION_INDEX]);
				// �ı�����ؼ�֡ƫ��
				KFMoveDir = kFDataFromAttackor[KF_SELF_DIR_INDEX];
				// ���ñ�������ʱ
				setKFDurFromAttackor();
				// ���ñ�����ƫ�ƹ���ƫ��
				setKFMoveDisFromAttackor();
				// ������
				keyFrameHurt = true;
			}
		}
	}

	/**
	 * �жϵ�ǰ����Ĺؼ�֡
	 * 
	 * @return boolean
	 */
	public boolean isAttackKeyFrame() {
		if (aniPlayer == null) {
			// CDebug._debugInfo ("aniPlaye Ϊnull");
			return false;
		}
		/**
		 * �ǹؼ�֡ ���� ��ǰ�ؼ�֡
		 */
		if (aniPlayer.isAttackFrame(m_actionID, aniPlayer.actionSequenceID)
				&& aniPlayer.actionSequenceDuration == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**********************
	 * ����֮�����ײ����
	 **********************/
	public boolean checkCollionWithActor(CObject obj) {
		return false;
	}

	public void doCollionWithActor() {

	}

	public boolean testCollionActor(CObject obj) {
		if (m_relativeMisc == null) {
			return false;
		}
		if (m_relativeMisc.equals(obj)) {
			return true;
		}
		return false;
	}

	/**
	 * �������Ƿ�ͼ��������ཻ
	 * 
	 * @param actor
	 *            CActor
	 * @return boolean
	 */
	public boolean testActiveBoxCollideObject(CObject actor) {

		actor.getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
		getActorActivateBoxInfo(m_actorID, s_colBox2);
		return CTools.isIntersecting(s_colBox1, s_colBox2);
	}

	/**************************************
	 * ������֮�����ײ
	 *****************************************/

	byte m_phbLeft; // left phb of player
	byte m_phbTop; // top phb of player
	byte m_phbRight; // right phb of player
	byte m_phbBottom; // bottom phb of player

	public int m_colWithActorFlag;
	public CObject m_relativeMisc;

	public final static int CLASS_CAN_COLLIDE_TOPSIDE = 1 << 0;
	public final static int CLASS_CAN_COLLIDE_UNDERSIDE = 1 << 1;
	public final static int CLASS_CAN_COLLIDE_TOPUNDER = CLASS_CAN_COLLIDE_TOPSIDE
			| CLASS_CAN_COLLIDE_UNDERSIDE;
	public final static int CLASS_CAN_COLLIDE_FRONT = 1 << 2;
	public final static int CLASS_CAN_COLLIDE_BACK = 1 << 3;
	public final static int CLASS_CAN_COLLIDE_FRONTBACK = CLASS_CAN_COLLIDE_FRONT
			| CLASS_CAN_COLLIDE_BACK;
	public final static int CLASS_CAN_COLLIDE_STATIC = 1 << 4;

	public int collide_With_Actor(CObject obj, int collision) {
		boolean check;
		int side = 0, anotherside = 0;
		int col = 0;
		/******************************
		 * collision �ж������Ƿ��м���flag
		 ******************************/
		if ((collision & (CLASS_CAN_COLLIDE_TOPUNDER | CLASS_CAN_COLLIDE_FRONT)) == 0) {
			return 0;
		}
		obj.getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
		// ȡ�õ����ߵ���ײ����
		getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox2);
		boolean alwayscheck = (collision & CLASS_CAN_COLLIDE_STATIC) != 0;

		/***************************************
		 * ������������Ƿ���ײ Ĭ�ϳ����� �� �����sender û�з�ת m_vX > 0 �������obj m_vX<0 ���������
		 * ����ߵ��ҷ� ˵������ߵ�ǰ�����ڼ��״̬
		 * 
		 ***************************************/
		if ((collision & CLASS_CAN_COLLIDE_FRONT) == CLASS_CAN_COLLIDE_FRONT) {
			if (!testFlag(dActor.FLAG_BASIC_FLIP_X)) {
				check = (obj.m_vX < 0 || m_vX > 0) && obj.m_x >= m_x;
				anotherside = LEFT;
				side = RIGHT;
			} else {
				check = (obj.m_vX > 0 || m_vX < 0) && obj.m_x <= m_x;
				anotherside = RIGHT;
				side = LEFT;
			}
			if (alwayscheck || check) {
				if (check) {
					s_colBox1[anotherside] += obj.m_vX >> dConfig.FRACTION_BITS;
					s_colBox2[side] += m_vX >> dConfig.FRACTION_BITS;
				}
				if (CTools.isIntersecting(s_colBox1, s_colBox2)) {
					col |= CLASS_CAN_COLLIDE_FRONT;
				}
				if (check) {
					s_colBox1[anotherside] -= obj.m_vX >> dConfig.FRACTION_BITS;
					s_colBox2[side] -= m_vX >> dConfig.FRACTION_BITS;
				}
			}
		}
		/***************************************
		 * ����ߺ��Ƿ���ײ
		 * 
		 * **************************************/
		if ((collision & CLASS_CAN_COLLIDE_BACK) == CLASS_CAN_COLLIDE_BACK) {
			if (!testFlag(dActor.FLAG_BASIC_FLIP_X)) {
				check = (obj.m_vX > 0 || m_vX < 0) && obj.m_x <= m_x;
				anotherside = RIGHT;
				side = LEFT;
			} else {
				check = (obj.m_vX < 0 || m_vX > 0) && obj.m_x >= m_x;
				anotherside = LEFT;
				side = RIGHT;
			}
			if (alwayscheck || check) {
				if (check) {
					s_colBox1[anotherside] += obj.m_vX >> dConfig.FRACTION_BITS;
					s_colBox2[side] += m_vX >> dConfig.FRACTION_BITS;
				}
				if (CTools.isIntersecting(s_colBox1, s_colBox2)) {
					col |= CLASS_CAN_COLLIDE_BACK;
				}
				if (check) {
					s_colBox1[anotherside] -= obj.m_vX >> dConfig.FRACTION_BITS;
					s_colBox2[side] -= m_vX >> dConfig.FRACTION_BITS;
				}
			}
		}
		/************************************
		 * ���ͷ��
		 ***************************************/
		if ((collision & CLASS_CAN_COLLIDE_TOPSIDE) == CLASS_CAN_COLLIDE_TOPSIDE) {
			check = (obj.m_vY > 0 || m_vY < 0)
					&& (m_y >> dConfig.FRACTION_BITS)
							+ (aniPlayer.boxInf[TOP] + aniPlayer.boxInf[BOTTOM])
							/ 2 >= (obj.m_y >> dConfig.FRACTION_BITS)
							+ obj.aniPlayer.boxInf[BOTTOM];
			anotherside = BOTTOM;
			side = TOP;
			if (alwayscheck || check) {
				if (check) {
					s_colBox1[anotherside] += obj.m_vY >> dConfig.FRACTION_BITS;
					s_colBox2[side] += m_vY >> dConfig.FRACTION_BITS;
				}
				if (CTools.isIntersecting(s_colBox1, s_colBox2)) {
					col |= CLASS_CAN_COLLIDE_TOPSIDE;
				}
				if (check) {
					s_colBox1[anotherside] -= obj.m_vY >> dConfig.FRACTION_BITS;
					s_colBox2[side] -= m_vY >> dConfig.FRACTION_BITS;
				}
			}
		}
		/*********************************
		 * ���ŵ�
		 ******************************/

		if ((collision & CLASS_CAN_COLLIDE_UNDERSIDE) == CLASS_CAN_COLLIDE_UNDERSIDE) {
			check = (obj.m_vY < 0 || m_vY > 0)
					&& (m_y >> dConfig.FRACTION_BITS)
							+ (aniPlayer.boxInf[BOTTOM] + aniPlayer.boxInf[TOP])
							/ 2 <= (obj.m_y >> dConfig.FRACTION_BITS)
							+ obj.aniPlayer.boxInf[TOP];
			anotherside = TOP;
			side = BOTTOM;
			if (alwayscheck || check) {
				if (check) {
					s_colBox1[anotherside] += obj.m_vY >> dConfig.FRACTION_BITS;
					s_colBox2[side] += m_vY >> dConfig.FRACTION_BITS;
				}
				if (CTools.isIntersecting(s_colBox1, s_colBox2)) {
					col |= CLASS_CAN_COLLIDE_UNDERSIDE;
				}
				if (check) {
					s_colBox1[anotherside] -= obj.m_vY >> dConfig.FRACTION_BITS;
					s_colBox2[side] -= m_vY >> dConfig.FRACTION_BITS;
				}
			}
		}
		return col;
	}

	public boolean checkActorBox(CObject actor) {

		if (actor == null) {
			return false;
		}

		m_phbLeft = MapData.PHY_NONE;
		m_phbRight = MapData.PHY_NONE;
		m_phbTop = MapData.PHY_NONE;
		m_phbBottom = MapData.PHY_NONE;

		int posX = (m_x - actor.m_x) >> dConfig.FRACTION_BITS;
		int posY = (m_y - actor.m_y) >> dConfig.FRACTION_BITS;

		actor.getActorOppBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
		short[] miscBox = s_colBox1;

		// top & bottom first
		if (m_vY > 0) {
			if ((m_colWithActorFlag & CLASS_CAN_COLLIDE_UNDERSIDE) != 0) {
				// m_colWithActorFlag &= ~CLASS_CAN_COLLIDE_UNDERSIDE;
				m_y = actor.m_y + (miscBox[1] << dConfig.FRACTION_BITS);
				m_phbBottom = MapData.PHY_SOLID;
				m_vX = m_modvX = 0;
				m_vY = m_modvY = 0;
				m_aY = 0;
				return true;

			}
		} else if (m_vY < 0) {
			if ((m_colWithActorFlag & CLASS_CAN_COLLIDE_TOPSIDE) != 0) {
				m_y = actor.m_y + (miscBox[3] << dConfig.FRACTION_BITS);
				m_vY = m_modvY = -m_vY;
				m_phbTop = MapData.PHY_SOLID;
				return true;

			}
		}
		// m_vY == 0
		else if (posY + aniPlayer.boxInf[3] == miscBox[1]) {
			m_phbBottom = MapData.PHY_SOLID;
			return true;
		} else if (posY + aniPlayer.boxInf[1] == miscBox[3]) {
			m_phbTop = MapData.PHY_SOLID;
			return true;
		}

		// // left & right last
		if ((m_colWithActorFlag & CLASS_CAN_COLLIDE_FRONT) != 0) {
			// m_colWithActorFlag &= ~CLASS_CAN_COLLIDE_FRONT;
			if (testFlag(dActor.FLAG_BASIC_FLIP_X)) {
				m_phbLeft = MapData.PHY_SOLID;
			} else {
				m_phbRight = MapData.PHY_SOLID;
			}

			if (m_phbLeft == MapData.PHY_SOLID) {
				m_x = actor.m_x
						+ ((actor.aniPlayer.boxInf[RIGHT] - aniPlayer.boxInf[LEFT]) << dConfig.FRACTION_BITS);
				m_x += 1 << dConfig.FRACTION_BITS;
				// m_vX = m_modvX = 0;
			} else if (m_phbRight == MapData.PHY_SOLID) {
				m_x = actor.m_x
						+ ((actor.aniPlayer.boxInf[LEFT] - aniPlayer.boxInf[RIGHT]) << dConfig.FRACTION_BITS);
				m_x -= 1 << dConfig.FRACTION_BITS;
				// m_vX = m_modvX = 0;
			}

		}

		else if ((m_colWithActorFlag & CLASS_CAN_COLLIDE_BACK) != 0) {
			// m_colWithActorFlag &= ~CLASS_CAN_COLLIDE_BACK;
			if (!testFlag(dActor.FLAG_BASIC_FLIP_X)) {
				m_phbLeft = MapData.PHY_SOLID;
			} else {
				m_phbRight = MapData.PHY_SOLID;
			}

			if (m_phbLeft == MapData.PHY_SOLID) {
				m_x = actor.m_x
						+ ((actor.aniPlayer.boxInf[RIGHT] - aniPlayer.boxInf[LEFT]) << dConfig.FRACTION_BITS);
				m_x += 1 << dConfig.FRACTION_BITS;
				// m_vX = m_modvX = 0;

			} else if (m_phbRight == MapData.PHY_SOLID) {
				m_x = actor.m_x
						+ ((actor.aniPlayer.boxInf[LEFT] - aniPlayer.boxInf[RIGHT]) << dConfig.FRACTION_BITS);
				m_x -= 1 << dConfig.FRACTION_BITS;
				// m_vX = m_modvX = 0;
			}

		}

		return true;
	}

	public void checkRelativeMisc() {

		if (m_relativeMisc != null) {
			if (checkActorBox(m_relativeMisc)) {
				if (m_phbTop == MapData.PHY_SOLID) {
					m_bBlockedTop = true;
					m_bBlockedActorTop = true;
				}
				if (m_phbBottom == MapData.PHY_SOLID) {
					m_bBlockedBottom = true;
					m_bBlockedActorBottom = true;
				}

				if ((m_phbLeft == MapData.PHY_SOLID && getFaceDir() == -1)
						|| (m_phbRight == MapData.PHY_SOLID && getFaceDir() == 1)) {
					m_bBlackedActorFront = true;
				}

				if ((m_phbLeft == MapData.PHY_SOLID && getFaceDir() == 1)
						|| (m_phbRight == MapData.PHY_SOLID && getFaceDir() == -1)) {
					m_bBlackedActorBack = true;
				}
			}
		}

	}

	/**
	 * try to activate a linked actor
	 * 
	 * @param actorID
	 *            int actor id of the linked actor
	 * @return CActorShell the shell of the linked actor
	 */
	public CObject tryActivateLinkedActor(int actorID) {
		if (CGame.actorsShellID[actorID] < 0) {
			CObject actor = CGame.tryActivateActor(actorID);
			getActorActivateBoxInfo(actorID, s_colBox1);
			getActorActivateBoxInfo(m_actorID, s_colBox2);
			CTools.unionBox(s_colBox1, s_colBox2);
			actor.modifyActivatedBox(s_colBox1);
			CGame.actorsRegionFlags[actor.m_actorID] = CGame
					.getRegionFlags(s_colBox1);
		}
		return CGame.m_actorShells[CGame.actorsShellID[actorID]];
	}

	public CObject tryActivateLinkedActor(int actorID, boolean bForcely) {
		if (CGame.actorsShellID[actorID] < 0) {
			CObject actor = CGame.activateActor(actorID, bForcely);
			if (actor == null) {
				return null;
			}
			// enlarge active box of actor
			actor.initialize();
			// getActorActivateBoxInfo(actorID , s_colBox1);
			// getActorActivateBoxInfo(m_actorID , s_colBox2);
			// CTools.unionBox(s_colBox1 , s_colBox2);
			// actor.modifyActivatedBox(s_colBox1);
			// CGame.m_actorsRegionFlags[actor.m_actorID] =
			// CGame.getRegionFlags(s_colBox1);
		}
		return CGame.m_actorShells[CGame.actorsShellID[actorID]];
	}

	/**
	 * ���ļ������� modify the activated box
	 * 
	 * @param box
	 *            short[]
	 */
	void modifyActivatedBox(short[] box) {

		setActorInfo(dActor.INDEX_ACTIIVE_BOX_LEFT, box[0]);
		setActorInfo(dActor.INDEX_ACTIVE_BOX_TOP, box[1]);
		setActorInfo(dActor.INDEX_ACTIVE_BOX_RIGHT, box[2]);
		setActorInfo(dActor.INDEX_ACTIVE_BOX_BOTTOM, box[3]);
	}

	/*********************
	 * ai�߼�����
	 *****************/

	public boolean update() {
		if (CGame.m_curState == dGame.GST_MAIN_MENU) {
			return false;
		}

		if (CGame.m_curState != dGame.GST_SCRIPT_RUN
				&& CGame.m_curState != dGame.GST_TRAILER_RUN
				&& CGame.m_curState != CGame.GST_SCRIPT_DIALOG
				&& CGame.m_curState != CGame.GST_CAMARE_MOVE
				&& CGame.m_curState != CGame.GST_SCRIPT_OPDLG
				&& Script.autoshowActionCouner == 0
				&& Script.autoMoveActorCounter == 0) {
			if (scanScript()) {
				return false;
			}
		}

		if (testFlag(dActor.FLAG_NO_LOGIC)) {
			return false;
		}

		doAdditionAttack();

		// ������ʱ�Ĺؼ�֡�߼�
		doKeyFrame();

		// ·���߼�
		if (doPathMove() && !initForcely) {
			return false;
		}

		// ������������
		if (testFlag(dActor.FLAG_FROZEN) || testFlag(dActor.FLAG_FAINT)) {
			return false;
		}

		// ����ؼ�֡ƫ��
		if (isAttackKeyFrame() && isHaveKFDis()) {
			this.setKFMoveDis();
		}
		return true;
	}

	private short[][] objPath; // �ɻ���·��
	int PathAgainNum = 0;

	/**
	 * ·����ʼ��
	 */
	public void initPath() {

		if (true) {
			return;
		}

		getPathBasicInf();
		// �����ɻ��Ĺ켣��Ϣ��
		if (m_actorProperty[PRO_PATH_ID] != -1
				&& CFlyerData.pathGroupData != null
				&& CFlyerData.pathGroupData.length > 0) {
			final short[][][] obj_path_data = CFlyerData.pathGroupData;
			objPath = new short[obj_path_data[m_actorProperty[PRO_PATH_ID]].length][5];
			for (int i = 0; i < objPath.length; i++) {
				for (int j = 0; j < objPath[0].length; j++) {
					objPath[i][dFlyer.PATH_ID] = obj_path_data[m_actorProperty[PRO_PATH_ID]][i][dFlyer.DATA_PATH_ID];
					objPath[i][dFlyer.PATH_LOOP] = obj_path_data[m_actorProperty[PRO_PATH_ID]][i][dFlyer.DATA_PATH_LOOP];
					objPath[i][dFlyer.PATH_FLOOP] = 0;
					objPath[i][dFlyer.PATH_INDEX] = 1;
					objPath[i][dFlyer.PATH_STATE] = obj_path_data[m_actorProperty[PRO_PATH_ID]][i][dFlyer.DATA_PATH_STATE];
					;
				}
			}
			// ����·���ƶ�����
			// setAnimAction (objPath[0][dFlyer.PATH_STATE]);
			setState(objPath[0][dFlyer.PATH_STATE], true);
			m_actorProperty[PRO_PATH_END] = 0;
		} else { // û�г���·����ֱ�ӽ���
			m_actorProperty[PRO_PATH_END] = 1;
			// setState (m_actorProperty[PRO_PATH_INIT_STATE]);

		}
	}

	// ·�����Ե�����
	public static int PATH_ID = (0 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;
	public static int PATH_CONTINUE_STATE = (1 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;
	public static int PATH_INF_COUNT = 2;

	/**
	 * ��ȡ·����ص�����
	 */
	void getPathBasicInf() {

		if (true) {
			return;
		}

		// û��·��
		if (!testClasssFlag(dActor.CLASS_FLAG_HAVE_PATH)) {
			m_actorProperty[PRO_PATH_ID] = -1;
			return;
		}
		// ����е�����Ϣ��ƫ��
		if (testClasssFlag(dActor.CLASS_FLAG_LOOT)) {
			PATH_ID += LOOT_INF_COUNT;
		}
		m_actorProperty[PRO_PATH_ID] = getActorInfo(PATH_ID);
		m_actorProperty[PRO_AFTER_PATH_STATE] = getActorInfo(PATH_CONTINUE_STATE);
	}

	/**
	 *·���߼�
	 * 
	 */
	public boolean doPathMove() {
		if (true) {
			return false;
		}
		// ���·������
		if (m_actorProperty[PRO_PATH_END] == 1) {
			return false;
		}

		// ��������·���߼����
		if (testFlag(dActor.FLAG_BEATTACKED)) {
			m_actorProperty[PRO_PATH_END] = 1;
			droptoGround();
			return false;
		}

		final short[][] path_base_data = CFlyerData.pathAtom;
		// ȷ���ɻ��ĳ���
		// int oldX = baseInfo[INFO_OBJ_X];
		// int oldY = baseInfo[INFO_OBJ_Y];
		int pathID = 0;
		// try{
		pathID = objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_ID];
		// }
		// catch(Exception e){
		// e.printStackTrace();
		// }
		int pathIndex = objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_INDEX];
		int newX = (m_x >> 8) + path_base_data[pathID][pathIndex << 1];
		int newY = (m_y >> 8) + path_base_data[pathID][(pathIndex << 1) + 1];
		// У��newXY
		if (newX <= 0) {
			newX = MapData.TILE_WIDTH >> 1;
		} else if (newX >= CGame.gMap.mapRes.m_mapTotalWidthByPixel) {
			newX = CGame.gMap.mapRes.m_mapTotalWidthByPixel
					- (MapData.TILE_WIDTH >> 1);
		}
		if (newY <= 0) {
			newY = MapData.TILE_WIDTH >> 1;
		} else if (newY >= CGame.gMap.mapRes.m_mapTotalHeightByPixel) {
			newY = CGame.gMap.mapRes.m_mapTotalHeightByPixel
					- (MapData.TILE_WIDTH >> 1);
		}
		setXY((short) newX, (short) newY);

		// �ɻ��켣���ƶ�
		objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_INDEX]++;
		if (objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_INDEX] >= path_base_data[objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_ID]].length / 2) {
			objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_FLOOP]++;
			if (objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_FLOOP] < objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_LOOP]) {
				objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_INDEX] = 1;
			} else {
				m_actorProperty[PRO_NOW_PATH]++;
				if (m_actorProperty[PRO_NOW_PATH] < objPath.length) {
					objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_FLOOP] = 0;
					// ���ö���ID
					// setAnimAction
					// (objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_STATE]);
					setState(
							objPath[m_actorProperty[PRO_NOW_PATH]][dFlyer.PATH_STATE],
							true);
				} else {
					if (m_actorProperty[PRO_PATH_AGAIN] == -1) {
						m_actorProperty[PRO_NOW_PATH] = 0;
						for (int i = 0; i < objPath.length; i++) {
							for (int j = 0; j < objPath[0].length; j++) {
								objPath[i][dFlyer.PATH_FLOOP] = 0;
								objPath[i][dFlyer.PATH_INDEX] = 1;
							}
						}
					} else if (m_actorProperty[PRO_PATH_AGAIN] > PathAgainNum + 1) {
						m_actorProperty[PRO_NOW_PATH] = 0;
						for (int i = 0; i < objPath.length; i++) {
							for (int j = 0; j < objPath[0].length; j++) {
								objPath[i][dFlyer.PATH_FLOOP] = 0;
								objPath[i][dFlyer.PATH_INDEX] = 1;
							}
						}
						PathAgainNum++;
					} // ·��ִ�н���
					else {
						m_actorProperty[PRO_PATH_END] = 1;
						// �µ�״̬�����һ��pathʱ�л��ڴ�
						// setState ( objPath[objPath.length - 1
						// /*���һ��path*/][dFlyer.PATH_STATE] );
						setState(m_actorProperty[PRO_AFTER_PATH_STATE], true);
						return false;
					}
				}
			}
		}
		return true;
		// }
	}

	/**
	 * ���˹ؼ�֡��������
	 */
	public void attHero() {
		getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
		// û�й�������
		if (s_colBox1[0] == s_colBox1[2] || s_colBox1[1] == s_colBox1[3]) {
			return;
		}

		// �ж��ǲ��ǹؼ�֡
		if (!isAttackKeyFrame()) {
			return;
		}

		if (isAttackedObj(CGame.m_hero)) {
			/**
			 * ����ؼ�֡������ ���� flag FLAG_DO_KEYFRAME_LOGIC
			 * 
			 * @param <any> FLAG_DO_KEYFRAME_LOGIC
			 */
			CHero hero = CGame.m_hero;
			// ��������ɹ������Լ��ؼ�֡�߼�
			this.setKFDur();

			// ���ù���֡�Ա������ߵ�Ӱ��
			hero.setFlag(dActor.FLAG_DO_KEYFRAME_LOGIC);
			hero.setKeyFrame(this);
			if (!hero.isFaceTo(this)) {
				hero.turnAround();
			}
			// ������Ч
			hero.getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
			CObject effect = null;
			if (getActorInfo(dActorClass.Index_Param_HERO_EFFECT) >= 0) {
				effect = allocActor(hero
						.getActorInfo(dActorClass.Index_Param_HERO_EFFECT),
						(s_colBox1[0] + s_colBox1[2]) << 7,
						(s_colBox1[3] + s_colBox1[1]) << 7,
						this.getFaceDir() == -1);
			}

			if (effect != null) {
				effect.m_z = 100;
				effect.setAnimAction(CTools.random(
						dActionID.Action_ID_attack_Effect_attack_Effect1,
						dActionID.Action_ID_attack_Effect_attack_Effect7));
			}

		}
	}

	/**
	 * ����Ļ��Ƶķ���
	 * 
	 * @param g
	 *            Graphics
	 */
	public void paint(Graphics g) {
		if (aniPlayer == null) {
			// CDebug._debugInfo ("aniPlaye Ϊnull");
			return;
		}

		if (isRemote(40, 40)) {
			return;
		}

		// ��run״̬�������ӵ��࣬�������ű�ʱ����ķ�˻��ڲ���
		if (m_animationID == dActorClass.Animation_ID_OBJ_BULLET
				&& CGame.m_curState != CGame.GST_GAME_RUN) {
			return;
		}

		// ���»���λ��
		aniPlayer.setSpritePos(m_x >> dConfig.FRACTION_BITS,
				m_y >> dConfig.FRACTION_BITS);
		aniPlayer.drawFrame(g, null);
		showBonusInfo(g, (m_x >> dConfig.FRACTION_BITS) - Camera.cameraLeft,
				(m_y >> dConfig.FRACTION_BITS) - Camera.cameraTop);
		showFaceInfo(g);
		paintDebugBox(g);
		showAAEffect(g);
		showDesName(g);
	}

	AniPlayer upLevelAni; // ��������
	boolean bshowUpLevelAni;

	/**
	 * ������ײ���򣬹������򣬼�������
	 */
	public void paintDebugBox(Graphics g) {
		if (!CDebug.bForTest) {
			return;
		}
		// ��ʾ��ײ����
		if (CDebug.bShowActorCollideBox) {
			getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
			CDebug._drawDebugBox(g, s_colBox1, false, 0xcc00ff, false);
			g.drawLine((m_x >> dConfig.FRACTION_BITS) - Camera.cameraLeft,
					(m_y >> dConfig.FRACTION_BITS) - Camera.cameraTop - 100,
					(m_x >> dConfig.FRACTION_BITS) - Camera.cameraLeft,
					(m_y >> dConfig.FRACTION_BITS) - Camera.cameraTop);
			g.drawString("" + m_shellID, (m_x >> dConfig.FRACTION_BITS)
					- Camera.cameraLeft, (m_y >> dConfig.FRACTION_BITS)
					- Camera.cameraTop, 0);
		}

		// ��ʾ��������
		if (CDebug.bShowActorAttackBox) {
			getActorBoxInfo(dActor.BOX_ATTACK, s_colBox2);
			CDebug._drawDebugBox(g, s_colBox2, false, 0xdd0000, false);
		}

		// ��ʾ��������
		if (CDebug.bShowActorActivateBox && m_actorID >= 0) {
			CGame.getActorActivateBoxInfo(m_actorID, s_colBox1);
			CDebug._drawDebugBox(g, s_colBox1, false, 0x0000ff, false);
		}

		int drawX = (m_x >> dConfig.FRACTION_BITS) - Camera.cameraLeft;
		int drawY = (m_y >> dConfig.FRACTION_BITS) - Camera.cameraTop;
		if (CDebug.bShowActorHP && testClasssFlag(dActor.CLASS_FLAG_IS_ENEMY)) {
			getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
			CTools.afficheSmall(g, "hp=" + m_actorProperty[PRO_INDEX_HP],
					drawX, drawY, dConfig.ANCHOR_HT, dConfig.COLOR_GREEN,
					dConfig.COLOR_BLACK);
		}
	}

	/**
	 * 
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param box
	 *            byte[]
	 * @param extra
	 *            int
	 * @return boolean
	 */
	public boolean intersectBox(int x, int y, short[] box, int extra) {
		if (box[0] == box[2]) {
			return false;
		}

		getActorOppBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
		short[] colBox = s_colBox1;

		x = (x - m_x) >> dConfig.FRACTION_BITS;
		y = (y - m_y) >> dConfig.FRACTION_BITS;
		if (x + box[0] > colBox[2] + extra || y + box[1] > colBox[3] + extra) {
			return false;
		}
		if (x + box[2] < colBox[0] - extra || y + box[3] < colBox[1] - extra) {
			return false;
		}
		return true;
	}

	// �������Flag��Ϣ ״̬��Ϣ ������Ϣ �����Ķ���ID
	public static final byte[] SAVED_INFO_DEFAULT = { dActor.INDEX_BASIC_FLAGS, /*
																				 * dActor.
																				 * INDEX_STATE
																				 * ,
																				 * dActor
																				 * .
																				 * INDEX_POSITION_X
																				 * ,
																				 * dActor
																				 * .
																				 * INDEX_POSITION_Y
																				 * ,
																				 * dActor
																				 * .
																				 * INDEX_ACTION_ID
																				 */};

	/**
	 * ���Ҫ������Ϣ������
	 * 
	 * @return byte[][]
	 */
	public static byte[][] getSaveInfo() {
		return new byte[][] { SAVED_INFO_DEFAULT, {} };
	};

	/**
	 * 
	 * @param deltaHP
	 *            int
	 * @param strShow
	 *            String
	 */
	Vector m_bonus = new Vector();
	final static byte DISPLAYTIME_BONUS = 20;

	public final static byte DOUBLE_ATT = 1;
	public final static byte NORMAL_ATT = 0;

	public final void addBonusShow(int deltaHP, String strShow, byte isBiff) {
		if (strShow == null) {
			strShow = String.valueOf(deltaHP);
		}
		int pos = 0;
		// if(m_bonus.size() > 0){
		// Integer.parseInt( ( (String[]) (m_bonus.elementAt(m_bonus.size() -
		// 1)))[1]);
		// pos -= Def.SF_HEIGHT;
		// }
		m_bonus.addElement(new String[] { strShow, String.valueOf(pos),
				String.valueOf(isBiff) });
	}

	/**
	 * ��ʾ������ʱ����Ѫ
	 * 
	 * @param g
	 *            Graphics
	 * @param x
	 *            int
	 * @param y
	 *            int
	 */
	public final static int TEXTCOLOR_2 = 0X000000;
	public final static int TEXTCOLOR_1 = 0XF3D55E;

	public final void showBonusInfo(Graphics g, int x, int y) {
		if (m_bonus.size() == 0) {
			return;
		}
		int exitPosy = 55;
		String[] e;
		int counter = 0;
		int type = 0;

		int w = 0;
		int h = 0;
		int h2 = 0;
		int w2 = 0;
		if (CGame.number_Image[0] != null) {
			w = CGame.number_Image[0].getWidth() / 10;
			h = CGame.number_Image[0].getHeight() / 10;
		}

		if (CGame.number_Image[1] != null) {
			w2 = CGame.number_Image[1].getWidth() / 10;
			h2 = CGame.number_Image[1].getHeight() / 10;
		}

		for (int i = 0; i < m_bonus.size(); i++) {
			e = (String[]) m_bonus.elementAt(i);
			counter = Integer.parseInt(e[1]);
			type = Integer.parseInt(e[2]);
			if (e[0].startsWith("-")) { // �ǵ�Ѫ
				counter++;
				int halfW = 0;
				if (type == NORMAL_ATT) {
					halfW = (String.valueOf(e[0]).length() * w) >> 1; // ���ֿ����8
					CTools.drawImageNumber(g, CGame.number_Image[0], String
							.valueOf(e[0]), new int[] {
							0,
							x - halfW,
							(y - exitPosy /* aniPlayer.boxInf[1] */- 2 * counter)
									- h, halfW << 1, h });
				} else {
					halfW = (String.valueOf(e[0]).length() * w2) >> 1; // ���ֿ����8
					CTools.drawImageNumber(g, CGame.number_Image[1], String
							.valueOf(e[0]), new int[] {
							0,
							x - halfW,
							(y - exitPosy /* aniPlayer.boxInf[1] */- 2 * counter)
									- h2 /* ���ָ߶���7 */, halfW << 1, 10 });
				}
			} else { // ���ı�
				counter++;
				if (counter > 0) {
					CTools.afficheSmall(g, e[0], x, y - exitPosy - counter * 3,
							dConfig.ANCHOR_HB, dConfig.COLOR_SCENE_NAME,
							dConfig.COLOR_SCENE_NAME_OUT);
				}
			}
			m_bonus.removeElementAt(i);
			if (counter < DISPLAYTIME_BONUS) {
				e[1] = String.valueOf(counter);
				e[2] = String.valueOf(type);
				m_bonus.insertElementAt(e, i);
			}
		}
	}

	/**
	 * ����HP,MP��Խ��
	 */
	public final void adjustHPMP() {
		if (m_actorProperty[PRO_INDEX_MP] < 0) {
			m_actorProperty[PRO_INDEX_MP] = 0;
		} else if (m_actorProperty[PRO_INDEX_MP] > m_actorProperty[PRO_INDEX_MAX_MP]) {
			m_actorProperty[PRO_INDEX_MP] = m_actorProperty[PRO_INDEX_MAX_MP];
		}

		if (m_actorProperty[PRO_INDEX_HP] < 0) {
			m_actorProperty[PRO_INDEX_HP] = 0;
		} else if (m_actorProperty[PRO_INDEX_HP] > m_actorProperty[PRO_INDEX_MAX_HP]) {
			m_actorProperty[PRO_INDEX_HP] = m_actorProperty[PRO_INDEX_MAX_HP];
		}
	}

	// �������ActionID
	public int faceIndex = -1;

	/**
	 * ���ñ���
	 * 
	 * @param faceIndex
	 *            int
	 */
	public final void setFaceInfo(int faceIndex) {
		if (this.faceIndex != faceIndex) {
			this.faceIndex = faceIndex;
			setFacePayer(CGame.PUB_ANI_SYS);
		}
	}

	/**
	 * ��ʾ����
	 * 
	 * @param g
	 *            Graphics
	 * @param x
	 *            int
	 * @param y
	 *            int
	 */
	AniPlayer systemIconPlayer;

	public final void setFacePayer(int type) {
		if (faceIndex != -1) {
			int animationID = -1; // CGame.globalAnimationID;
			switch (type) {
			case CGame.PUB_ANI_SYS:
				animationID = CGame.gData.systemFaceAniID;
				break;
			case CGame.PUB_ANI_DLG_HEAD:
				animationID = CGame.gData.dialogHeadAniID;
				break;
			case CGame.PUB_ANI_GOODS_ICON:
				animationID = CGame.gData.equipIconAniID;
				break;
			case CGame.PUB_ANI_EFFECT:
				animationID = CGame.gData.effectAniID;
				break;
			case CGame.PUB_ANI_UI:
				animationID = -1;
				break;
			case CGame.PUB_ANI_CG:
				animationID = -1;
				break;
			}
			if (animationID < 0 || animationID > CGame.gData.animations.length) {
				// debug
				if (CDebug.showErrorInfo) {
					System.out.println(">>������������(): Public AnimationID="
							+ animationID + ", ���⹫�������������Ƿ���ȷ����������");
				}
			}
			if (systemIconPlayer == null) {
				int posx = (m_x >> dConfig.FRACTION_BITS)
						+ (aniPlayer.boxInf[aniPlayer.INDEX_COLLISION_BOX_LEFT] + aniPlayer.boxInf[aniPlayer.INDEX_COLLISION_BOX_RIGHT])
						/ 2;
				int posy = (m_y >> dConfig.FRACTION_BITS)
						+ aniPlayer.boxInf[aniPlayer.INDEX_COLLISION_BOX_TOP]
						- 20;
				systemIconPlayer = new AniPlayer(
						CGame.gData.animations[animationID], posx, posy,
						faceIndex); // ID��1��ʼ��ţ�0ΪcommonAnimation
			}
		}
	}

	public final void showFaceInfo(Graphics g) {
		if (systemIconPlayer == null) {
			return;
		}

		if (CGame.drawPublicFrame(g, systemIconPlayer)) {
			systemIconPlayer = null;
			faceIndex = -1;
		}
	}

	/**************************
	 * �����Զ��ƶ�
	 *************************/
	// �Զ��ƶ��ĸ���
	public void updateAutoMove() {
		if (m_vX == 0) {
			m_destX = 0;
		} else {
			m_destX -= Math.abs(m_vX);
		}

		if (m_destX <= 0) {
			m_destX = 0;
		}

		if (m_vY == 0) {
			m_destY = 0;
		} else {
			m_destY -= Math.abs(m_vY);
		}

		if (m_destY <= 0) {
			m_destY = 0;
		}

		if (m_destX <= 0 && m_destY <= 0) {
			isAutoMove = false;
			Script.autoMoveActorCounter--;
		}

		if (!isAutoMove) {
			return;
		}
		/*******************
		 * ��ͷ����
		 *******************/

		if (Camera.cammeraObj != null) {
			Camera.cammeraObj.updateCamera();
		} else {
			CGame.m_hero.updateCamera();
		}

		doPhysics();
		// // ͨ��actor������ٶȸı�
		// if(m_vX != 0 || m_vY != 0 || m_aX != 0 || m_aY != 0) {
		// m_vX += mechStep (m_aX);
		// m_vY += mechStep (m_aY);
		// m_x += mechStep (m_vX);
		// m_y += mechStep (m_vY);
		//
		// }
	}

	public void updateAutoAction() {
		if (!isAutoAction) {
			return;
		}

		if (actionDur > 0) {
			actionDur--;
			return;
		}
		if (changeActionID > 0 && actionDur == 0) {
			actionDur = -1;
			aniPlayer.clearAniPlayFlag(aniPlayer.FLAG_ACTION_ALL);
			setAnimAction(changeActionID);
		}
		updateAnimation();

		if (playTimes > 0) {
			if (aniPlayer.testAniPlayFlag(aniPlayer.FLAG_ACTION_OVER)) {
				playTimes--;
				if (playTimes > 0) {
					aniPlayer.clearAniPlayFlag(aniPlayer.FLAG_ACTION_OVER);
				}
			}
		}
		if (playTimes == 0) {
			isAutoAction = false;
			Script.autoshowActionCouner--;
			if (isRecover) {
				setAnimAction(preAction);
			} else {
				// setAnimAction (m_actionID);
				int sqID = aniPlayer.getSquenceCount(m_actionID) - 1;
				aniPlayer.setAnimAction(m_actionID, sqID,
						aniPlayer.FLAG_ACTION_HOLD);
			}
		}

		if (Camera.cammeraObj != null) {
			Camera.cammeraObj.updateCamera();
		} else {
			CGame.m_hero.updateCamera();
		}
	}

	/**
	 * �������Ĵ�����
	 * 
	 * @return int
	 */
	final static byte HERO_SLOW_MOTION = 0;
	final static byte ALL_SLOW_MOTION = 1;
	final static byte ENEMY_SLOW_MOTION = 2;

	public static void setSlowMotion(byte type) {
		GameEffect.slowMotionType = type;
	}

	public int getTimerStep() {
		if (GameEffect.slowMotionType == ALL_SLOW_MOTION) {
			return 1;
		} else if (GameEffect.slowMotionType == ENEMY_SLOW_MOTION) {
			if (m_classID != dActorClass.CLASS_ID_HERO) {
				return 1;
			}
		} else if (GameEffect.slowMotionType == HERO_SLOW_MOTION) {
			if (m_classID == dActorClass.CLASS_ID_HERO) {
				return 1;
			}

		}
		if (GameEffect.useSlowMotion == true) {
			return 1;
		}

		return dConfig.Slow_Motion_Ratio;
	}

	/**
	 * ��ʱ����һ���¶���ķ���
	 * 
	 * @param classID
	 *            int
	 * @param actionID
	 *            int
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param faceLeft
	 *            boolean
	 * @return AbstractActor
	 */

	public static CObject allocActor(int linkID, int x, int y, boolean faceLeft) {
		// ����m_classID������ϵ��
		CGame.setTempClassID(linkID);

		CObject actor = CGame.activateActor(-1, true);
		if (actor == null) {
			if (CDebug.showDebugInfo) {
				System.out.println("****��������Ϊnull******");
			}
			return null;
		}
		CObject beclonedActor = null;
		if (CGame.actorsShellID[linkID] != -1) {
			beclonedActor = CGame.m_actorShells[CGame.actorsShellID[linkID]];
		} else {
			if (CDebug.showDebugInfo) {
				System.out.println("******���������LinkIDΪnull ���� δ������*******");
			}
			// add by lin 09.5.5 �쳣��������
			actor.die(false);
			return null;
		}

		actor.m_x = x;
		actor.m_y = y;
		actor.m_classID = beclonedActor.m_classID;
		actor.m_actionID = beclonedActor.m_actionID;
		actor.m_scriptID = -1;
		actor.m_animationID = actor.m_classID < 0 ? -1
				: ResLoader.classesAnimID[actor.m_classID];

		actor.setFaceDir(faceLeft ? -1 : 1);
		actor.initialize();
		// linkID
		actor.m_linkID = linkID;

		if (actor.m_actionID != -1) {
			actor.setAnimAction(actor.m_actionID);
		}
		actor.setFlag(dActor.FLAG_BASIC_VISIBLE);
		actor.setFlag(dActor.FLAG_BASIC_ALWAYS_ACTIVE);
		return actor;
	}

	/**
	 * ��ʱ����һ���¶���ķ���
	 * 
	 * @param classID
	 *            int
	 * @param actionID
	 *            int
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param faceLeft
	 *            boolean
	 * @return AbstractActor �������Ҫ����ʹ�� ����classID �п��ܲ��������ģ�������������������
	 */

	// public static CObject allocActor(int classID , int actionID , int x , int
	// y , boolean faceLeft)
	// {
	// CGame.m_tempClassID = classID;
	// CObject actor = CGame.activateActor( -1 , true);
	// if(actor == null) {
	// return null;
	// }
	//
	// actor.m_x = x;
	// actor.m_y = y;
	//
	// actor.m_classID = classID;
	// actor.m_actionID = actionID;
	// actor.m_scriptID = -1;
	//
	// actor.m_animationID = classID < 0 ? -1 :
	// CGame.gData.classesAnimID[actor.m_classID];
	// actor.setFaceDir(faceLeft ? -1 : 1);
	// actor.initialize();
	// if(actionID != -1){
	// actor.setAnimAction(actionID);
	// }
	// actor.setFlag(dActor.FLAG_BASIC_VISIBLE);
	// return actor;
	// }

	public boolean beAttack(int damage) {
		if (m_invincible > 0 || m_actorProperty[PRO_INDEX_HP] <= 0) {
			return false;
		}
		changeHp(-damage);
		// if(CTools.random (0, 100) < 50) {
		// addBonusShow (damage, null, NORMAL_ATT);
		// m_actorProperty[PRO_INDEX_HP] -= damage;
		// }
		// else {
		// addBonusShow (2 * damage, null, DOUBLE_ATT);
		// m_actorProperty[PRO_INDEX_HP] -= 2 * damage;
		// }

		// ��������flag
		setFlag(dActor.FLAG_BEATTACKED);
		return true;
	}

	static final int MAX_DEF_RATE = 90; // �������˵����ޣ��ٷֱ�
	public boolean isDefend;
	public short defend_odds;

	/**
	 * �ı�hp
	 * 
	 * @param damage
	 *            int
	 * @return byte
	 */
	public boolean changeHp(int damage) {
		// -----------------��Ѫ
		if (damage < 0) {
			if (m_actorProperty[PRO_INDEX_HP] <= 0 || m_invincible > 0) {
				return false;
			}
			// ������
			if (CTools.random(0, 100) <= defend_odds) {
				isDefend = true;
				return true;
			}

			// ��������
			if (m_actorProperty[PRO_INDEX_DEF] > 0) {
				int temp = (Math.abs(damage) * MAX_DEF_RATE / 100)
						- m_actorProperty[PRO_INDEX_DEF];
				if (temp < 0) {
					temp = 0;
				}
				damage = (damage * (100 - MAX_DEF_RATE)) / 100 - temp;
				if (damage == 0) {
					damage = -1;
				}
			}
			// ��ʾ��Ϣ
			if (Math.abs(damage) >= 50) {
				addBonusShow(damage, null, DOUBLE_ATT);
			} else if (damage != 0) {
				addBonusShow(damage, null, NORMAL_ATT);
			}

			m_actorProperty[PRO_INDEX_HP] += damage;

			// �����������
			if (m_actorProperty[PRO_INDEX_HP] <= 0) {
				// �������
				// clearFlag (dActor.FLAG_FROZEN);
				clearAllAddition();
				setFlag(dActor.FLAG_BEATTACKED);
			}
			adjustHP();
			return true;
		}
		// -----------------��Ѫ
		else if (damage > 0) {
			addBonusShow(damage, "����+" + damage, DOUBLE_ATT);
			m_actorProperty[PRO_INDEX_HP] += damage;

			adjustHP();
			return true;
		}

		return true;
	}

	public void changeMp(int mp) {
		m_actorProperty[PRO_INDEX_MP] += mp;
		adjustHPMP();
		if (mp > 0) {
			addBonusShow(mp, "ħ��+" + mp, DOUBLE_ATT);
		}
	}

	/**
	 * ����HP��Խ��
	 */
	public final void adjustHP() {
		if (m_actorProperty[PRO_INDEX_HP] < 0) {
			m_actorProperty[PRO_INDEX_HP] = 0;
		} else if (m_actorProperty[PRO_INDEX_HP] > m_actorProperty[PRO_INDEX_MAX_HP]) {
			m_actorProperty[PRO_INDEX_HP] = m_actorProperty[PRO_INDEX_MAX_HP];
		}
	}

	/**
	 * ���Ӿ���
	 */
	public void addExp(int exp) {
		if (m_actorProperty[PRO_INDEX_LEVEL] >= 99) {
			return;
		}
		int rate = 1;

		m_actorProperty[PRO_INDEX_EXP] += exp * (rate > 1 ? rate : 1);
	}

	boolean m_beLooted; // loot�����ù�

	// ��Ǯ
	static final int LOOT_MONEY_RATE = (0 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;
	static final int LOOT_MONEY_NUM = (1 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;

	// ����Ʒװ��
	static final int LOOT_ITEM1_RATE = (2 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;
	static final int LOOT_ITEM1_TYPE = (3 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;
	static final int LOOT_ITEM1_ID = (4 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;

	// ������
	static final int LOOT_EXP = (5 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;

	// �����ܵ���
	static final int LOOT_SP = (6 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;
	static final int LOOT_LINK_MONEY = (7 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;
	static final int LOOT_LINK_ITEM = (8 + dActor.INDEX_PARAMS_BASE)
			| dActor.SIGN_BASE_INFO;
	static final int LOOT_INF_COUNT = 9; // ���������������

	/**
	 * ���������Ʒ�Ĵ��� ���ж���ת��ST_ACTOR_DIE״̬������������
	 * ������Ʒ�Ķ���classFlagҪ����CLASS_FLAG_LOOT��ǰ8������ͳһ����
	 * 
	 * @author shubinl
	 */
	void loot() {
		if (!testClasssFlag(dActor.CLASS_FLAG_LOOT) || m_beLooted) {
			return;
		}
		m_beLooted = true;
		// ------------�����ܵ�
		int sp = getActorInfo(LOOT_SP);
		if (sp > 0) {
			CGame.m_hero.m_actorProperty[PRO_INDEX_SP] += sp;
		}

		// ------------������
		int exp = getActorInfo(LOOT_EXP);
		if (exp > 0) {
			CGame.m_hero.addExp(exp);
		}

		int random = CTools.random(0, 100);
		int itemActorId = 0;
		int itemValue = 0;
		int itemType = 0;
		// ------------��Ǯ
		if (random < getActorInfo(LOOT_MONEY_RATE)) {
			itemActorId = getActorInfo(LOOT_LINK_MONEY);
			itemValue = getActorInfo(LOOT_MONEY_NUM);
			CObject money = null;
			CObject moneyInScenes = null;
			if (itemActorId >= 0 && itemValue > 0) {
				// ������link��Ǯ����
				moneyInScenes = tryActivateLinkedActor(itemActorId, true);
				if (moneyInScenes == null) {
					return;
				} else {
					// ��ȡǮ�����������ֵ
					moneyInScenes.m_parameters[CElementor.INDEX_MONEY_HOWMUCH] = moneyInScenes
							.getActorInfo(dActorClass.Index_Param_OBJ_MONEY_TYPE);
				}
				for (int i = 0; i < itemValue; i++) {
					money = allocActor(itemActorId, m_x, m_y,
							getFaceDir() == -1);
					if (money != null) {
						money.m_parameters[CElementor.INDEX_MONEY_HOWMUCH] = moneyInScenes.m_parameters[CElementor.INDEX_MONEY_HOWMUCH];
						money.m_vX = (CTools.random(-5, 5)) << 8;
						money.m_vY = (CTools.random(-16, -9)) << 8;
						money.m_aY = 2 << 8;
					}
				}
			}
		}
		// ------------����Ʒװ��
		if (random < getActorInfo(LOOT_ITEM1_RATE)) {
			itemActorId = getActorInfo(LOOT_LINK_ITEM);
			itemValue = getActorInfo(LOOT_ITEM1_ID);
			itemType = getActorInfo(LOOT_ITEM1_TYPE);
			CObject item = null;
			if (itemActorId >= 0) {
				item = allocActor(itemActorId, m_x, m_y, getFaceDir() == -1);
			}

			if (item != null) {
				item.m_parameters[CElementor.TYPEID] = itemType;
				item.m_parameters[CElementor.GOODID] = itemValue;
				// ���ݼ�Ҫ�������ı���Ʒ��actionID
				Goods temp = Goods.createGoods((short) itemType,
						(short) itemValue);
				if (temp != null) {
					int actionid = Integer.parseInt(temp.getDescPredigest());
					item.setAnimAction(actionid);
				}

			}
		}

	}

	/**
	 * �Ƿ�����
	 * 
	 * @return boolean
	 */
	public boolean checkLevelup() {
		if (m_actorProperty[PRO_INDEX_LEVEL] >= 99) {
			return false;
		}
		if (m_actorProperty[PRO_INDEX_EXP] >= m_actorProperty[PRO_INDEX_NEXT_EXP]
				&& m_actorProperty[PRO_INDEX_EXP] > 0) {
			return true;
		}
		return false;
	}

	public String strLevel = "";

	/**
	 * ����ĳһ��
	 * 
	 * @param lv
	 *            int
	 */
	public void levelUp(int lv) {
		short[] newPro = getProbyLevel(lv);
		short[] oldPro = getProbyLevel(m_actorProperty[PRO_INDEX_LEVEL]);

		m_actorProperty[PRO_INDEX_HP] = m_actorProperty[PRO_INDEX_MAX_HP] += newPro[PRO_INDEX_HP]
				- oldPro[PRO_INDEX_HP];
		m_actorProperty[PRO_INDEX_MP] = m_actorProperty[PRO_INDEX_MAX_MP] += newPro[PRO_INDEX_MP]
				- oldPro[PRO_INDEX_MP];
		m_actorProperty[PRO_INDEX_ATT] += newPro[PRO_INDEX_ATT]
				- oldPro[PRO_INDEX_ATT];
		m_actorProperty[PRO_INDEX_DEF] += newPro[PRO_INDEX_DEF]
				- oldPro[PRO_INDEX_DEF];
		m_actorProperty[PRO_INDEX_NEXT_EXP] = newPro[PRO_INDEX_NEXT_EXP];
		m_actorProperty[PRO_INDEX_CRI_RATE] += newPro[PRO_INDEX_CRI_RATE]
				- oldPro[PRO_INDEX_CRI_RATE];

		m_actorProperty[PRO_INDEX_EXP] = 0;
		m_actorProperty[PRO_INDEX_LEVEL] = (short) lv;
		if (this == CGame.m_hero) {
			if (m_actorProperty[PRO_INDEX_LEVEL] >= 99) {
				m_actorProperty[PRO_INDEX_LEVEL] = 99;
				addBonusShow(0, "��������ߵȼ�", (byte) 0);
			} else {
				addBonusShow(0, "����", (byte) 0);
			}
		}

		bshowUpLevelAni = true;
		strLevel = m_actorProperty[PRO_INDEX_LEVEL] + "";
	}

	/**
	 * ȡ��ĳ���ȼ�������
	 * 
	 * @param level
	 *            int
	 * @return short[]
	 */
	public short[] getProbyLevel(int level) {
		short[] pro = new short[IObject.PRO_BASIC_LEN];
		level -= 1;
		if (level < 0 || m_actorProperty[PRO_INDEX_ROLE_ID] < 0) {
			return pro;
		}
		int a, b, c, d, temp;
		for (int i = 0; i < pro.length; i++) {
			a = Data.ROLE_FORMULA_PARAM[m_actorProperty[PRO_INDEX_ROLE_ID]][i][0];
			b = Data.ROLE_FORMULA_PARAM[m_actorProperty[PRO_INDEX_ROLE_ID]][i][1];
			c = Data.ROLE_FORMULA_PARAM[m_actorProperty[PRO_INDEX_ROLE_ID]][i][2];
			d = Data.ROLE_FORMULA_PARAM[m_actorProperty[PRO_INDEX_ROLE_ID]][i][3];
			temp = a * a * a * level / 100 + b * b * level / 100 + c * level
					/ 100 + d / 100;
			if (temp > Short.MAX_VALUE) {
				System.out.println("����ֵ����short�ͷ�Χ��roleId = "
						+ m_actorProperty[PRO_INDEX_ROLE_ID]
						+ " ,propertyId = " + i + " , level = " + level);
			}
			pro[i] = (short) (temp);
		}
		return pro;
	}

	/**
	 * �Ƿ������Ļ��Զ
	 * 
	 * @param dx
	 *            int
	 * @param dy
	 *            int
	 */
	public boolean isRemote(int dx, int dy) {
		int x = m_x >> dConfig.FRACTION_BITS;
		int y = m_y >> dConfig.FRACTION_BITS;
		return (x < Camera.cameraBox[0] - dx || x > Camera.cameraBox[2] + dx
				|| y < Camera.cameraBox[1] - dy || y > Camera.cameraBox[3] + dy);
	}

	/**************************************************************************
	 * ���ӹ�������
	 ***************************************************************************/
	public static final byte AA_YUN = 0; // ��
	public static final byte AA_DU = 1; // ��
	public static final byte AA_NUM = 2; // ����Ч��������
	public boolean[] aaOpen = new boolean[AA_NUM];
	public int[] aaTimer = new int[AA_NUM];
	public int[] aaEffectValue = new int[AA_NUM];
	public short[][] ASCAACtrl = new short[AA_NUM][2];

	/**
	 * û���κθ�����Ч������
	 * 
	 * @return boolean
	 */
	boolean allAaEffectClose() {
		for (int i = aaOpen.length - 1; i >= 0; i--) {
			if (aaOpen[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ��ø��ӹ����ĳ���ʱ��
	 * 
	 * @param aaID
	 *            int
	 * @return int
	 */
	int getAATime(int aaID) {

		return 0;
	}

	/**
	 * ��ø��ӹ������˺�ֵ
	 * 
	 * @param aaID
	 *            int
	 * @return int
	 */
	int getAADamage(int aaID) {

		return 0;
	}

	/**
	 * �����Լ���ָ�����ӹ���
	 * 
	 * @param aaID
	 *            int
	 * @param cdTime
	 *            int
	 * @param effectData
	 *            int
	 */
	boolean setAdditionAttack(int aaID, int cdTime, int effectData) {
		if (m_actorProperty[PRO_INDEX_HP] <= 0 || m_invincible > 0) {
			return false;
		}
		if (!allAaEffectClose()) {
			return false;
		}
		// ----����
		// if ((aaID==AA_FIRE&&m_actorProperty[PRO_IMMU_FIRE]>0)
		// ||(aaID==AA_THUNDER&&m_actorProperty[PRO_IMMU_THUNDER]>0)
		// ||(aaID==AA_ICE&&m_actorProperty[PRO_IMMU_ICE]>0)) {
		// return false;
		// }

		aaOpen[aaID] = true;
		aaTimer[aaID] = (cdTime) / dConfig.FPS_RATE;
		aaEffectValue[aaID] = effectData;
		int effectActionId = 0;
		if (aaID == AA_YUN) {
			effectActionId = dActionID.Action_ID_attack_Effect_xuanyun;
			setAnimAction(dActionID.Action_ID_E_1_faint);
			setFlag(dActor.FLAG_FAINT);
		} else if (aaID == AA_DU) {
			effectActionId = dActionID.Action_ID_attack_Effect_zhongdu1;
		}

		// if (effectAniId>=0) {
		// if (ResLoader.animations[effectAniId]==null) {
		// aaEffectPlayer=null;
		// }
		// else{
		if (aaEffectPlayer == null || aaEffectPlayer.aniData == null) {
			aaEffectPlayer = new AniPlayer(
					ResLoader.animations[dActorClass.Animation_ID_ATTACK_EFFECT],
					m_x >> dConfig.FRACTION_BITS, m_y >> dConfig.FRACTION_BITS);
		}
		aaEffectPlayer.setAnimAction(effectActionId);
		// }
		// }
		return true;
	}

	/**
	 * ����Ч�����߼�
	 */
	final void doAdditionAttack() {
		if (m_actorProperty[PRO_INDEX_HP] <= 0) {
			clearAllAddition();
			return;
		}

		for (byte aaID = 0; aaID < aaOpen.length; aaID++) {
			if (aaOpen[aaID]) {
				// ����Ч��ʱ�䵽
				if (--aaTimer[aaID] <= 0) {
					if (aaID == AA_DU) {
						if (aaEffectPlayer.actionID == dActionID.Action_ID_attack_Effect_zhongdu1) {
							aaEffectPlayer
									.setAnimAction(dActionID.Action_ID_attack_Effect_zhongdu2);
						} else {
							if (aaEffectPlayer
									.testAniPlayFlag(aaEffectPlayer.FLAG_ACTION_OVER)) {
								clearAdditionAttack(aaID);
							}
						}
					} else {
						clearAdditionAttack(aaID);
					}
				}

				if (!aaOpen[aaID]) {
					return;
				}

				switch (aaID) {
				case AA_DU:

					// ÿ3��ִ��һ��
					if (aaTimer[aaID] % (3000 / dConfig.FPS_RATE) != 0)
						continue;
					changeHp(-aaEffectValue[aaID]);
					break;
				case AA_YUN:

					break;
				}

			}
		}
	}

	// �������Թ����Ķ���
	AniPlayer aaEffectPlayer;

	/**
	 * ���ӹ�����Ч����ʾ
	 * 
	 * @param g
	 *            Grahpics
	 * @param x
	 *            int
	 * @param y
	 *            int
	 */
	public void showAAEffect(Graphics g) {
		if (aaEffectPlayer == null) {
			return;
		}
		// ���ӹ���Ч������
		for (byte aaID = AA_NUM - 1; aaID >= 0; aaID--) {
			if (this.aaOpen[aaID]) {
				if (aaEffectPlayer != null) {
					aaEffectPlayer.setSpriteX(m_x >> 8);
					getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
					if (aaID == AA_DU) {
						aaEffectPlayer
								.setSpriteY(((s_colBox1[1] + s_colBox1[3]) >> 1) - 10);
					} else {
						aaEffectPlayer.setSpriteY(s_colBox1[1] - 10);
					}
					aaEffectPlayer.drawFrame(g, null);
				}
				return;
			}
		}
	}

	/**
	 * ���ָ���ĸ���Ч��
	 * 
	 * @param aaID
	 *            byte
	 */
	public final void clearAdditionAttack(int aaID) {
		if (!aaOpen[aaID]) {
			return;
		}
		switch (aaID) {
		case AA_YUN:
			clearFlag(dActor.FLAG_FAINT);
			m_phyEvx = m_phyEvy = 0;
			setState(ST_ACTOR_WAIT);
			setAnimAction(dActionID.Action_ID_E_1_stand);
		case AA_DU:
			break;
		}
		aaOpen[aaID] = false;
		aaTimer[aaID] = 0;
		aaEffectValue[aaID] = 0;
	}

	public final void clearAllAddition() {
		for (int i = 0; i < AA_NUM; i++) {
			if (aaOpen[i]) {
				clearAdditionAttack(i);
			}

		}
	}

	public static byte LINKID_COUNTER = 5;
	public int[] linkActorID = new int[LINKID_COUNTER];
	public static short condition_hp;

	public final static byte CON_HP = 0;
	public final static byte CON_COUNTER = 1;

	public boolean isReachCondition(int type) {
		switch (type) {
		case CON_HP:
			if (m_actorProperty[PRO_INDEX_HP] <= condition_hp)
				return true;
			break;

		case CON_COUNTER:
			break;
		}
		return false;
	}

	public void createLinkedEnemy(int linkID) {
		// //���û��link����
		if (linkActorID[linkID] < 0) {
			return;
		}
		if (isReachCondition(CON_HP)) {
			CObject enemy = allocActor(linkActorID[linkID], -1, -1, false);
			linkActorID[linkID] = -1;
		}

	}

	/*******************************
	 * �ж��Ƿ�����Ұ��Χ�ڵķ���
	 *******************************/
	public boolean isInArea(int distance, boolean mustFace) {
		CHero hero = CGame.m_hero;
		int x1 = m_x >> dConfig.FRACTION_BITS;
		int y1 = m_y >> dConfig.FRACTION_BITS;

		int x2 = hero.m_x >> dConfig.FRACTION_BITS;
		int y2 = (hero.m_y >> dConfig.FRACTION_BITS);

		y1 += aniPlayer.boxInf[TOP];

		// ��������ǲ�����
		if (mustFace) {
			if ((x1 > x2) != testFlag(dActor.FLAG_BASIC_FLIP_X)) {
				return false;
			}
		}
		if ((Math.abs(y2 - y1) >> dConfig.FRACTION_BITS) >= 4 * MapData.TILE_HEIGHT - 1) {
			return false;
		}

		if (!CTools.testConnectivity(x1, y1, x2, y2
				+ hero.getActorInfo(aniPlayer.INDEX_COLLISION_BOX_BOTTOM))
				&& !CTools.testConnectivity(x1, y1, x2, y2
						+ hero.getActorInfo(aniPlayer.INDEX_COLLISION_BOX_TOP))) {
			return false;
		}

		if (Math.abs(x1 - x2) <= distance) {
			return true;
		}

		// if(CEngine.GetDistance(x1 - x2, y1 - y2) <= distance){
		// return true;
		// }

		return false;
	}

	/**
	 * �������Թ���
	 */
	boolean aaAttack(CObject enemy, int id) {
		// �۷�
		if (enemy.m_animationID == dActorClass.Animation_ID_ENEMY_MAFENG) {
			return false;
		}

		int rate = 0, aaId = 0, time = 0, damage = 0;
		CHero hero = CGame.m_hero;
		if (id == AA_YUN) {
			rate = hero.skills[0].affectProperty[PRO_INDEX_YUN_RATE];
			damage = 0;
			aaId = AA_YUN;
			time = 10000;
		} else if (id == AA_DU) {
			rate = hero.skills[0].affectProperty[PRO_INDEX_DU_RATE];
			damage = hero.skills[0].affectProperty[PRO_INDEX_ATT];
			aaId = AA_DU;
			time = 15000;
		} else
			return false;

		int random = CTools.random(1, 100);
		if (random > rate) {
			return false;
		}

		if (!enemy.setAdditionAttack(aaId, time, damage)) {
			return false;
		}
		return true;
	}

	/**
	 * �Ƿ񱩻�
	 * 
	 * @return boolean
	 */
	public boolean isCriAtt() {
		return CTools.random(0, 100) <= m_actorProperty[PRO_INDEX_CRI_RATE];
	}

	static final String[] s = { "�߸�", "������", "¬��", "����", "�ϱ�ɽ", "����", "��ɽ�Ƶ�",
			"������", "��ɽ",

			"�����ϳ�", "Ұ����", "����¥", "���ⶫ��", "���⸮", "����", "�������ص�", };

	void showDesName(Graphics g) {
		// #if size_300k
		// # //300k����ʾĿ�ĵ�
		// # if (m_animationID==dActorClass.Animation_ID_OBJ_CHUKOU
		// # &&m_actionID>=0
		// # &&m_actionID<dActionID.Action_ID_zi_shou) {
		// # CTools.afficheSmall(g,s[m_actionID],(m_x>>8)-Camera.cameraLeft,
		// # (m_y-5>>8)-Camera.cameraTop,dConfig.ANCHOR_HT,
		// # dConfig.COLOR_SCENE_NAME,
		// # dConfig.COLOR_SCENE_NAME_OUT);
		// # }
		// #endif
	}

}
