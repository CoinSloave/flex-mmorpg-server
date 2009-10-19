package game.object;

import game.CGame;
import game.CTools;
import game.config.dConfig;
import game.pak.Camera;
import game.pak.GameEffect;
import game.res.ResLoader;

import com.mglib.mdl.ani.Player;
import com.mglib.mdl.map.MapData;

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
public class CEnemy extends CObject {

	public CEnemy() {
	}

	// public byte[][] getSaveInfo () {
	// return new byte[][] {SAVED_INFO_DEFAULT, {}};
	// }

	public void initialize() {
		if (testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV)) {
			initPhy();
			initSurroundingPro();
			m_phyAttrib |= PHYATTRIB_COLENGINE | PHYATTRIB_GRAVITY;
		}
		for (int i = 0; i < m_actorProperty.length; i++) {
			m_actorProperty[i] = 0;
		}
		super.initialize();
	}

	public boolean setState(int newState) {
		super.setState(newState);
		switch (m_currentState) {
		case ST_ACTOR_WAIT:
			break;
		case ST_ACTOR_WALK:
			break;
		case ST_ACTOR_RUN:
			break;
		case ST_ACTOR_ATTACK:
			break;
		case ST_ACTOR_JUMP:
			break;
		default:
			break;
		}
		return false;
	}

	public static boolean bAllEnemyStop; // ���е��˾�ֹ

	public boolean update() {
		if (!super.update()) {
			return false;
		}

		if (bAllEnemyStop) {
			return false;
		}

		int aiID = ResLoader.classAIIDs[m_classID];
		switch (aiID) {
		case dActorClass.CLASS_ID_OBJ_BULLET:
			ai_bullet();
			break;
		// #if size_500k
		case dActorClass.CLASS_ID_BOSS_GAO:
			ai_gaoQiu();
			break;
		// #endif
		case dActorClass.CLASS_ID_ENEMY_COMMON:
			ai_enemy();
			break;
		case dActorClass.CLASS_ID_OBJ_DICI:
			ai_dici();
			break;
		case dActorClass.CLASS_ID_ENEMY_MAFENG:
			ai_wasp();
			break;
		}
		initForcely = false;
		return true;
	}

	public void ai_normalenemy() {
		switch (m_currentState) {
		case ST_ACTOR_WAIT:
			do_Wait();
			break;
		case ST_ACTOR_WALK:
			do_Walk();
			break;
		case ST_ACTOR_RUN:
			do_Run();
			break;
		case ST_ACTOR_ATTACK:
			do_Attack();
			break;
		case ST_ACTOR_JUMP:
			do_Jump();
			break;
		default:
			break;
		}
	}

	private void do_Wait() {
	}

	private void do_Walk() {
	}

	private void do_Run() {
	}

	private void do_Attack() {
	}

	private void do_Jump() {
	}

	public boolean canSeeHero(int faceDir, int dx, int dy) {
		int x = m_x >> dConfig.FRACTION_BITS;
		int y = m_y >> dConfig.FRACTION_BITS;
		getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);

		CHero hero = CGame.m_hero;
		int heroX = hero.m_x >> dConfig.FRACTION_BITS;
		int heroY = (hero.m_y >> dConfig.FRACTION_BITS);
		hero.getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox2);

		// ���򲻶� �� dy����Χ
		if ((heroX - x) * faceDir < 0
				|| Math.abs((s_colBox1[3] + s_colBox1[1]) / 2
						- (s_colBox2[3] + s_colBox2[1]) / 2) >= dy) {
			return false;
		}

		// dx������Ұ,����˲���Ļ��
		if (Math.abs(heroX - x) >= dx) {
			return false;
		}

		// �����赲
		getActorOppBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
		hero.getActorOppBoxInfo(dActor.BOX_COLLIDE, s_colBox2);
		if (!CTools.testConnectivity(x, y + s_colBox1[1], heroX, heroY
				+ s_colBox2[3])
				&& !CTools.testConnectivity(x, y + s_colBox1[1], heroX, heroY
						+ s_colBox2[1])) {
			return false;
		}
		return true;
	}

	/**
	 * ��������Ѳ��
	 * 
	 * @param patrolArea
	 *            int,ˮƽ�����Ѳ�߷�Χ
	 */
	public boolean patrol(int patrolArea) {
		if (patrolArea <= 0) {
			return false;
		}

		int x = m_x >> dConfig.FRACTION_BITS;
		int initX = m_initX >> dConfig.FRACTION_BITS;

		getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
		int row_bottom = s_colBox1[3] / MapData.TILE_WIDTH + 1;
		int column_front = (getFaceDir() == -1 ? s_colBox1[0] : s_colBox1[2])
				/ MapData.TILE_WIDTH;

		int tileVlaueFrontBottom = CGame.gMap.mapRes.getTilePhyEnv(
				column_front, row_bottom);
		boolean blockBottomFront = tileVlaueFrontBottom >= MapData.PHY_SOLID
				|| tileVlaueFrontBottom == MapData.PHY_LADDER;

		// ����Ѳ�����򣬻�������ǰ�赲�����£�ת��
		if ((Math.abs(x - initX) > patrolArea && (initX - x) * getFaceDir() < 0)
				|| (testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV) && (m_bBlockedFrontSolid || !blockBottomFront))) {
			return true;
		}
		return false;
	}

	/***
	 * ͨ��AI����2
	 */

	private short att1_pro; // ��һ�ֹ�����ʽ�ĸ���
	private short att2_pro; // �ڶ��ֹ�����ʽ�ĸ���
	private short att3_pro; // �����ֹ�����ʽ�ĸ���
	private short walk_pro; // ���ߵĸ���
	private short run_pro; // �ܶ��ĸ���
	private short walkBack_pro; // ���˵ĸ���
	private short stop_pro; // ֹͣ�ĸ���
	private short jump_pro; // ��Ծ�ĸ���

	private short defend_pro; // �����ĸ���
	private short faint_pro; // ѣ�εĸ���
	private short turnAround_pro = 40; // ת��ĸ���
	private short escape_pro;// ���ܵĸ���
	private short escape_hp;// ���ܵ�ʱ���HP

	private short counterAtt_pro; // �����ĸ���
	private short walk_frame; // ���ߵ�֡��
	private short walkBack_frame; // ���˵�֡��
	private short run_frame; // �ܶ���֡��
	private short stop_frame; // ֹͣ��֡��
	private short faint_frame; // ѣ�ε�֡��

	private short att_Type;
	private short att_dis;
	private short att1_dis; // ��һ������ʽ�Ĺ����ľ���
	private short att2_dis; // �ڶ�������ʽ�Ĺ����ľ���
	private short att3_dis; // ����������ʽ�Ĺ����ľ���
	private short sight_dis; // ��Ұ
	private short back_dis; // ���˵ľ���
	private static final byte ATT_TYPE_A = 0;
	private static final byte ATT_TYPE_B = 1;
	private static final byte ATT_TYPE_C = 2;

	public final static int enemyActionMap[][] = {
	// ST_ACTOR_WAIT
			{ dActionID.Action_ID_E_1_stand, dActionID.Action_ID_E_1_stand_wait },
			// ST_ACTOR_WALK
			{ dActionID.Action_ID_E_1_walk },
			// ST_ACTOR_RUN
			{ dActionID.Action_ID_E_1_run },
			// ST_ACTOR_ATTACK
			{ dActionID.Action_ID_E_1_attack_1,
					dActionID.Action_ID_E_1_attack_2,
					dActionID.Action_ID_E_1_attack_3 },
			// ST_ACTOR_DIE
			{ dActionID.Action_ID_E_1_die, dActionID.Action_ID_E_1_lie2, },
			// ST_ACTOR_HURT
			{ dActionID.Action_ID_E_1_hurt_1, dActionID.Action_ID_E_1_hurt_2,
					dActionID.Action_ID_E_1_hurt_3,
					dActionID.Action_ID_E_1_stand_up,
					dActionID.Action_ID_E_1_lie, },
			// ST_ACTOR_JUMP
			{},
			// ST_ACTOR_DEFEND
			{ dActionID.Action_ID_E_1_defend },
			// ST_ACTOR_FAINT
			{ dActionID.Action_ID_E_1_faint },
			// ST_ACTOR_WALK_BACK
			{ dActionID.Action_ID_E_1_walk },
			// ST_ACTOR_TURN
			{ -1 /* dActionID.Action_ID_Diannan_stand_turn */},

	};

	public final static byte ST_ACTOR_WALK_BACK = ST_COUNT + 0;
	public final static byte ST_ACTOR_TURN = ST_COUNT + 1;
	public final static byte ST_ACTOR_ESCAPE = ST_COUNT + 2;

	private int checkBehaviorPro() {
		int random = CTools.random(0, 100);
		if (isAchieveRandom(random, att1_pro)) {
			att_Type = ATT_TYPE_A;
			setAttDis(att_Type);
			return ST_ACTOR_ATTACK;
		} else if (isAchieveRandom(random, att2_pro)) {
			att_Type = ATT_TYPE_B;
			setAttDis(att_Type);
			return ST_ACTOR_ATTACK;
		} else if (isAchieveRandom(random, att3_pro)) {
			att_Type = ATT_TYPE_C;
			setAttDis(att_Type);
			return ST_ACTOR_ATTACK;
		} else if (isAchieveRandom(random, walk_pro)) {
			return ST_ACTOR_WALK;
		} else if (isAchieveRandom(random, run_pro)) {
			return ST_ACTOR_RUN;
		} else if (isAchieveRandom(random, walkBack_pro)) {
			return ST_ACTOR_WALK_BACK;
		} else if (isAchieveRandom(random, stop_pro)) {
			return ST_ACTOR_WAIT;
		} else if (isAchieveRandom(random, jump_pro)) {
			return ST_ACTOR_JUMP;
		}

		return -1;
	}

	public boolean setState(int newState, boolean changeAction) {
		super.setState(newState);
		if (changeAction) {
			switch (m_currentState) {
			case ST_ACTOR_WAIT:

				// int random = CTools.random(0,100);
				// if(random<50)
				setAnimAction(enemyActionMap[ST_ACTOR_WAIT][0]);

				// else
				// setAnimAction(enemyActionMap[ST_ACTOR_WAIT][1]);
				break;

			case ST_ACTOR_WALK:
				setAnimAction(enemyActionMap[ST_ACTOR_WALK][0]);
				break;

			case ST_ACTOR_RUN:
				setAnimAction(enemyActionMap[ST_ACTOR_RUN][0]);
				break;

			case ST_ACTOR_ATTACK:
				setAttAction(att_Type);
				break;

			case ST_ACTOR_JUMP:
				break;

			case ST_ACTOR_WALK_BACK:
				setAnimAction(enemyActionMap[ST_ACTOR_WALK_BACK][0]);
				m_vX = -m_vX;
				break;

			case ST_ACTOR_TURN:
				setAnimAction(enemyActionMap[ST_ACTOR_TURN][0]);
				break;

			case ST_ACTOR_DEFEND:
				setAnimAction(enemyActionMap[ST_ACTOR_DEFEND][0]);
				break;

			case ST_ACTOR_FAINT:
				setAnimAction(enemyActionMap[ST_ACTOR_FAINT][0]);
				break;

			case ST_ACTOR_DIE:
				setAnimAction(enemyActionMap[ST_ACTOR_DIE][0]);
				break;

			default:
				break;
			}
		}
		return true;
	}

	private void updateBehavior() {
		int state = checkBehaviorPro();
		switch (state) {
		case ST_ACTOR_ATTACK:
			if (isInArea(att_dis, true)) {
				setState(ST_ACTOR_ATTACK, true);
			} else {
				updateBehavior();
			}
			break;

		case ST_ACTOR_WAIT:
			if (!isFaceTo(CGame.m_hero) && isInArea(sight_dis, false)) {
				turnAround();
			}
			setState(ST_ACTOR_WAIT, true);
			break;

		case ST_ACTOR_WALK:
			if (!isFaceTo(CGame.m_hero) && isInArea(sight_dis, false)) {
				turnAround();
			}
			if (isInArea(back_dis, false)) {
				setState(ST_ACTOR_WALK_BACK, true);
			} else {
				setState(ST_ACTOR_WALK, true);
			}
			break;

		case ST_ACTOR_RUN:
			if (!isFaceTo(CGame.m_hero) && isInArea(sight_dis, false)) {
				turnAround();
			}

			if (isInArea(back_dis, false)) {
				setState(ST_ACTOR_WALK_BACK, true);
			} else if (isInArea(sight_dis, false)) {
				setState(ST_ACTOR_RUN, true);
			} else if (isRandom(turnAround_pro)) {
				turnAround();
				setState(ST_ACTOR_WALK, true);
			} else {
				updateBehavior();
			}

			break;

		case ST_ACTOR_WALK_BACK:
			if (isInArea(sight_dis, false)) {
				setState(ST_ACTOR_WALK_BACK, true);
			} else if (isRandom(turnAround_pro)) {
				turnAround();
				setState(ST_ACTOR_WALK, true);
			} else {
				updateBehavior();
			}
			break;

		case ST_ACTOR_JUMP:
			setState(ST_ACTOR_JUMP, true);
			break;

		default:
			break;
		}
	}

	public void ai_enemy() {
		if (testFlag(dActor.FLAG_NEED_INIT)) {
			clearFlag(dActor.FLAG_NEED_INIT);
			droptoGround();
			// m_actorProperty[PRO_INDEX_HP] = m_actorProperty[PRO_INDEX_MAX_HP]
			// = getActorInfo (dActorClass.Index_Param_ENEMY_COMMON_HP);
			// m_actorProperty[PRO_INDEX_ATT] = getActorInfo
			// (dActorClass.Index_Param_ENEMY_COMMON_DAMAGE);
			int level = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_LEVEL);
			m_actorProperty[PRO_INDEX_ROLE_ID] = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_LIST);
			// ͨ��������ʽ�������
			levelUp(level);
			att1_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_ATT1_PRO); // ��һ�ֹ�����ʽ�ĸ���
			att2_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_ATT2_PRO); // �ڶ��ֹ�����ʽ�ĸ���
			att3_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_ATT3_PRO); // �����ֹ�����ʽ�ĸ���
			walk_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_WALK_PRO); // ���ߵĸ���
			run_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_RUN_PRO); // �ܶ��ĸ���
			walkBack_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_WALKBACK_PRO); // ���˵ĸ���
			stop_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_STOP_PRO); // ֹͣ�ĸ���
			jump_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_JUMP_PRO); // ��Ծ�ĸ���

			defend_odds = defend_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_DEFEND_PRO); // �����ĸ���
			counterAtt_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_COUNTERATT_PRO); // �����ĸ���
			faint_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_FAINT_PRO); // ѣ�εĸ���
			escape_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_ESCAPE_PRO); // ���ܵĸ���
			turnAround_pro = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_TURN_PRO); // ת��ĸ���
			escape_hp = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_ESCAPE_HP); // ����ʱ��hp

			walk_frame = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_WALK_FRAME); // ���ߵ�֡��
			walkBack_frame = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_WALKBACK_FRAME); // ���˵�֡��
			run_frame = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_RUN_FRAME); // �ܶ���֡��
			stop_frame = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_STOP_FRAME); // ֹͣ��֡��

			sight_dis = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_SIGHT_DIS); // ��Ұ
			att1_dis = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_ATT1_DIS); // ��һ������ʽ�Ĺ����ľ���
			att2_dis = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_ATT2_DIS); // �ڶ�������ʽ�Ĺ����ľ���
			att3_dis = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_ATT3_DIS); // ����������ʽ�Ĺ����ľ���

			back_dis = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_BACK_DIS); // ���˵ľ���

			linkActorID[0] = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_LINKENEMYID);
			condition_hp = getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_CON_EXIT);
			updateBehavior();
			// ��ʼ��bossѪ��
			if (m_animationID == dActorClass.Animation_ID_BOSS_LIKUI
					|| m_animationID == dActorClass.Animation_ID_BOSS_TOUMU) {
				GameEffect.init_BossHp(m_actorProperty[PRO_INDEX_MAX_HP]);
			}
		} else {

			if (isRemote(150, 100)) {
				return;
			}

			// ����������
			updateBeAttacked();

			// ����Ѫ��
			if (m_animationID == dActorClass.Animation_ID_BOSS_LIKUI
					|| m_animationID == dActorClass.Animation_ID_BOSS_TOUMU) {
				GameEffect.set_BossHp(m_actorProperty[PRO_INDEX_HP]);
			}

			// ����link����
			createLinkedEnemy(0);
			// ������Ϊ
			do_enemyBehavior();

			// ����߼�����
			if (!testFlag(dActor.FLAG_BASIC_DIE)) {
				doPhysics();
			}
			// ���Ƶ����ھ�ͷ��
			if (Camera.isLockInArea && !testFlag(dActor.FLAG_BASIC_DIE)) {
				lockActorInArea(Camera.lockCameraBox);
			}
		}
	}

	private void do_enemyBehavior() {
		switch (m_currentState) {
		case ST_ACTOR_WAIT:
			do_behaviorWait();
			break;

		case ST_ACTOR_WALK:
			do_behaviorWalk();
			break;

		case ST_ACTOR_WALK_BACK:
			do_behaviorWalkBack();
			break;

		case ST_ACTOR_RUN:
			do_behaviorRun();
			break;

		case ST_ACTOR_ATTACK:
			do_behaviorAttack();
			break;

		case ST_ACTOR_HURT:
			do_behaviorHurt();
			break;

		case ST_ACTOR_FAINT:
			do_behaviorFaint();
			break;

		case ST_ACTOR_DEFEND:
			do_behaviorDefend();
			break;

		case ST_ACTOR_ESCAPE:
			do_behaviorEscape();
			break;
		case ST_ACTOR_DIE:
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				if (m_actionID == enemyActionMap[ST_ACTOR_DIE][0]) {
					setAnimAction(enemyActionMap[ST_ACTOR_DIE][1]);
					setFlag(dActor.FLAG_DIE_TO_SCRIPT);
					if (m_animationID == dActorClass.Animation_ID_BOSS_LIKUI
							|| m_animationID == dActorClass.Animation_ID_BOSS_TOUMU) {
						// ���Ѫ��
						GameEffect.prepareToRender(GameEffect.NO_RENDER,
								GameEffect.EFF_BOSS_HP);
					}
					// ����������
					if (getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_DIEEFFECT) > 0) {
						Player.useSlowMotion = false;
					}
				} else {

					// if (testFlag(dActor.FLAG_DIE_TO_SCRIPT)) {
					die(false);
					// }
					// else{
					// //�ű����������ս����ʤ
					// setFlag(dActor.FLAG_DIE_TO_SCRIPT);
					// setAnimAction(m_actionID);
					// }
				}
			}
			break;
		default:
			break;
		}

	}

	// �ȴ���Ϊ
	private void do_behaviorWait() {
		m_timer += getTimerStep();
		// �����������ǣ���������Ұ��Χ ��ת��
		if (!isFaceTo(CGame.m_hero) && isInArea(sight_dis, false)) {
			turnAround();
		}

		if (m_timer >= stop_frame * getTimerStep()) {
			updateBehavior();
		}
	}

	// ������Ϊ
	private void do_behaviorWalk() {
		m_timer += getTimerStep();
		// ���ǰ���赲����߽�С��ͼ�����Է��赲��ת��8
		if (isFrontBlock()) {
			turnAround();
		}
		if (m_timer >= walk_frame * getTimerStep()) {
			updateBehavior();
		}
	}

	// �ܶ���Ϊ
	private void do_behaviorRun() {
		m_timer += getTimerStep();
		// ���ǰ���赲����߽�С��ͼ�����Է��赲��ת��8
		if (isFrontBlock()) {
			turnAround();
		}
		if (m_timer >= run_frame * getTimerStep()) {
			updateBehavior();
		}
	}

	// ������Ϊ
	private void do_behaviorWalkBack() {
		m_timer += getTimerStep();
		// ���ǰ���赲����߽�С��ͼ�����Է��赲��ת��8
		if (m_timer >= walkBack_frame * getTimerStep() || isBackBlock()) {
			updateBehavior();
		}
	}

	// ������Ϊ
	private void do_behaviorAttack() {
		if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			updateBehavior();
		}

		// ���
		if (getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_BULLET) >= 0) {
			if (isAttackKeyFrame()) {
				getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
				CObject bullet = allocActor(
						getActorInfo(dActorClass.Index_Param_ENEMY_COMMON_BULLET),
						s_colBox1[0] << dConfig.FRACTION_BITS,
						s_colBox1[1] << dConfig.FRACTION_BITS,
						getFaceDir() == -1);
				if (bullet != null) {
					System.arraycopy(m_actorProperty, 0,
							bullet.m_actorProperty, 0, m_actorProperty.length);
					bullet.m_actorProperty[PRO_INDEX_HP] = bullet.m_actorProperty[PRO_INDEX_CRI_RATE] = 0;
				}
			}
		} else {
			attHero();
		}
	}

	// ������Ϊ
	private void do_behaviorHurt() {
		if (m_actionID == enemyActionMap[ST_ACTOR_HURT][0]
				|| m_actionID == enemyActionMap[ST_ACTOR_HURT][1]) {
			if (isCanCounterAtt()) {
				setState(ST_ACTOR_ATTACK, true);
				return;
			}
		}

		if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			isCounterAtt = true;
			if (m_actionID == enemyActionMap[ST_ACTOR_HURT][0]
					|| m_actionID == enemyActionMap[ST_ACTOR_HURT][1]) {

				setState(ST_ACTOR_WAIT, true);
				aniPlayer.clearAniPlayFlag(Player.FLAG_ACTION_NOT_CYCLE);

			} else if (m_actionID == enemyActionMap[ST_ACTOR_HURT][2]) {
				setAnimAction(enemyActionMap[ST_ACTOR_HURT][4]);
			} else if (m_actionID == enemyActionMap[ST_ACTOR_HURT][4]) {
				// aniPlayer.clearAniPlayFlag (Player.FLAG_ACTION_NOT_CYCLE);
				setAnimAction(enemyActionMap[ST_ACTOR_HURT][3]);
			} else if (m_actionID == enemyActionMap[ST_ACTOR_HURT][3]) {
				// ��һ����������
				if (isRandom(escape_pro)
						&& m_actorProperty[PRO_INDEX_HP] <= escape_hp) {
					setState(ST_ACTOR_ESCAPE, true);
					return;
				}

				if (isRandom(faint_pro)) {
					setState(ST_ACTOR_FAINT, true);
				} else {
					setState(ST_ACTOR_WAIT, true);
				}
			}
		}
	}

	// ������Ϊ
	private void do_behaviorDefend() {
		if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			updateBehavior();
		}
	}

	// ѣ����Ϊ
	private void do_behaviorFaint() {
		m_timer++;
		if (m_timer >= faint_frame) {
			updateBehavior();
		}
	}

	// ������Ϊ
	private void do_behaviorEscape() {
		boolean isInActiveBox = CTools.isIntersecting(Camera.cameraBox,
				getActorActivateBoxInfo(m_actorID, CObject.s_colBox1));
		if (!isInActiveBox) {
			die(false);
		}
	}

	// �ж�������Ƿ����
	private boolean isRandom(int pro) {
		int random = CTools.random(0, 100);
		if (random <= pro) {
			return true;
		}

		return false;
	}

	private void enemyAppearOn(int type) {

	}

	private void enemyAppearCondition(int type) {

	}

	// �ж��Ƿ�ﵽ�����ֵ
	private boolean isAchieveRandom(int random, int pro) {

		if (random <= pro) {
			return true;
		}
		return false;

	}

	private boolean isFrontBlock() {
		// modify by lin 09.4.30 б����
		int tileValue = CGame.gMap.mapRes.getTilePhyEnv(
				(m_x >> dConfig.FRACTION_BITS) / MapData.TILE_WIDTH,
				(m_y >> dConfig.FRACTION_BITS) / MapData.TILE_WIDTH);
		if (m_bBlockedFrontSolid
				|| (!m_bBlockedBottomFront && !isOnSlope(tileValue))) {
			return true;
		}
		return false;

	}

	private boolean isBackBlock() {
		if (m_bBlockedBack || !m_bBlockedBottomBack) {
			return true;
		}
		return false;
	}

	private static final byte ATTTYPE_A = 0;
	private static final byte ATTTYPE_B = 1;
	private static final byte ATTTYPE_C = 2;

	private void setAttDis(int type) {
		if (type == ATTTYPE_A) {
			att_dis = att1_dis;
		} else if (type == ATTTYPE_B) {
			att_dis = att2_dis;
			;
		} else if (type == ATTTYPE_C) {
			att_dis = att3_dis;
			;
		}

	}

	private boolean isCounterAtt = true;

	// �Ƿ���Է���
	private boolean isCanCounterAtt() {
		if (isCounterAtt) {
			isCounterAtt = false;
			int random = CTools.random(0, 100);
			// System.out.println (random);
			if (random <= counterAtt_pro) {
				m_invincible = 10;
				return true;
			}
		}
		return false;
	}

	private void setAttAction(int type) {
		if (type == ATTTYPE_A) {
			setAnimAction(enemyActionMap[ST_ACTOR_ATTACK][0]);
		} else if (type == ATTTYPE_B) {
			setAnimAction(enemyActionMap[ST_ACTOR_ATTACK][1]);
		} else if (type == ATTTYPE_C) {
			setAnimAction(enemyActionMap[ST_ACTOR_ATTACK][2]);
		}
	}

	// �Ƿ����

	private void updateBeAttacked() {
		// ����
		if (testFlag(dActor.FLAG_BEATTACKED)) {
			if (m_actorProperty[PRO_INDEX_HP] <= 0) {
				setState(ST_ACTOR_DIE, true);
				// ����������
				if (getActorInfo(dActorClass.Index_Param_BOSS_TOUMU_DIEEFFECT) > 0) {
					CGame.s_screenFlashTimer = 16;
					CGame.s_screenFlashInterval = 3;
					CGame.s_screenFlashColor = dConfig.COLOR_RED;
					Player.useSlowMotion = true;
				}
			} else {
				if (isDefend) {
					isDefend = false;
					setState(ST_ACTOR_DEFEND, true);
				} else {
					setState(ST_ACTOR_HURT);
				}
			}
			clearFlag(dActor.FLAG_BEATTACKED);
			aniPlayer.clearAniPlayFlag(Player.FLAG_ACTION_OVER);
			aniPlayer.setAniPlayFlag(Player.FLAG_ACTION_NOT_CYCLE);
			if (!isFaceTo(CGame.m_hero)) {
				turnAround();
			}
		}
	}

	public static short INDEX_BULLET_FROM_HERO = 0;

	/**
	 * �ӵ�
	 */
	void ai_bullet() {
		if (testFlag(dActor.FLAG_NEED_INIT)) {
			clearFlag(dActor.FLAG_NEED_INIT);
			return;
		}

		// ��ʧ����Ч
		if (m_actionID == dActionID.Action_ID_Bullet_boom) {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				die(false);
			}
			return;
		}
		// ���˵��ӵ�
		if (m_parameters[INDEX_BULLET_FROM_HERO] == 0) {
			doPhysics();
			if (testColliding(CGame.m_hero)) {
				if (CGame.m_hero.beAttack(m_actorProperty[PRO_INDEX_ATT])) {
					CGame.m_hero.setAnimAction(0);
				}
				die(false);
			}
			if (m_bBlockedFrontSolid) {
				die(false);
			}
		}
		// ���ǵ��ӵ�
		else {
			CObject enemy = null;
			int shellId = CGame.activeActorsListHead;
			while (shellId != -1) {
				enemy = CGame.m_actorShells[shellId];
				// ����
				if (ResLoader.classAIIDs[enemy.m_classID] == dActorClass.CLASS_ID_OBJ_CANATTACKBOX) {
					if ((enemy.m_actionID == dActionID.Action_ID_box_1)
							&& testColliding(enemy)) {
						enemy.setFlag(dActor.FLAG_BEATTACKED);
					}
				}

				if ((ResLoader.classesFlags[enemy.m_classID] & dActor.CLASS_FLAG_IS_ENEMY) != 0
						&& enemy.m_actorProperty[PRO_INDEX_HP] > 0
						&& enemy.m_invincible <= 0
						&& enemy.testFlag(dActor.FLAG_BASIC_VISIBLE)) {
					if (testColliding(enemy)) {
						if (enemy.beAttack(m_actorProperty[PRO_INDEX_ATT])) {
							// �������Թ���
							boolean aaAttSuccess = false;
							if (m_actionID < dActionID.Action_ID_Bullet_Fire_phoenix1) {
								aaAttSuccess = aaAttack(enemy, AA_YUN);
								if (!aaAttSuccess && !enemy.aaOpen[AA_YUN]) {
									enemy.setAnimAction(0);
								}
							}
							// ���������������Ч
							if (m_actionID == dActionID.Action_ID_Bullet_bulejian
									|| m_actionID == dActionID.Action_ID_Bullet_redjian
									|| (m_actionID >= dActionID.Action_ID_Bullet_Fire_phoenix1 && m_actionID <= dActionID.Action_ID_Bullet_Fire_phoenix4)) {
								enemy.clearAdditionAttack(AA_YUN);
								enemy.setAnimAction(0);
							}
						}

						// ����,��������ʧ��Ч
						if (m_actionID < dActionID.Action_ID_Bullet_Fire_phoenix1
								|| m_actionID == dActionID.Action_ID_Bullet_bulejian
								|| m_actionID == dActionID.Action_ID_Bullet_redjian) {
							setAnimAction(dActionID.Action_ID_Bullet_boom);
							break;
						}
					}
				}
				shellId = CGame.nextActorShellID[shellId];
			}
		}

		// ����
		if (m_actionID == dActionID.Action_ID_Bullet_Fire_phoenix4) {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				die(false);
			}
		} else if (m_actionID == dActionID.Action_ID_Bullet_Fire_phoenix1
				|| m_actionID == dActionID.Action_ID_Bullet_Fire_phoenix2
				|| m_actionID == dActionID.Action_ID_Bullet_Fire_phoenix3) {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setAnimAction(m_actionID + 1);
			}
		} else {
			// ����Ŷ���
			// ///
			// ��
			if (m_actionID == dActionID.Action_ID_Bullet_bulejian
					|| m_actionID == dActionID.Action_ID_Bullet_redjian) {
				doPhysics();
				if (m_bBlockedFrontSolid || m_bBlackedActorFront) {
					setAnimAction(dActionID.Action_ID_Bullet_boom);
					return;
				}
			}
			// ����
			else {
				m_vX += m_aX;
				m_vY += m_aY;
				m_x += m_vX;
				m_y += m_vY;
			}
		}
		if (isRemote(10, 20)) {
			die(false);
		}
	}

	/**
	 * �ش̻���
	 */
	void ai_dici() {
		if (m_actionID == dActionID.Action_ID_dici_normal) {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setAnimAction(dActionID.Action_ID_dici_attack);
			}
		} else if (m_actionID == dActionID.Action_ID_dici_attack) {
			if (testColliding(CGame.m_hero)) {
				if (CGame.m_hero
						.beAttack(getActorInfo(dActorClass.Index_Param_OBJ_DICI_DAMAGE))) {
					CGame.m_hero.setAnimAction(0);
				}

			}
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setAnimAction(dActionID.Action_ID_dici_normal);
			}
		}
	}

	static final int ST_GQ_SLOW = ST_COUNT + 0;
	static final int ST_GQ_FLY_UP = ST_COUNT + 1;
	static final int ST_GQ_FLY = ST_COUNT + 2;
	static final int ST_GQ_FLY_DOWN = ST_COUNT + 3;

	public static int INDEX_GQ_ATT_MODE = 0; // ������ʽ��2��
	public static int INDEX_GQ_FIRE_COUNTER = 1; // ���ڼ���
	public static int INDEX_GQ_HURT_COUNTER = 2; // ���˼���
	public static int INDEX_GQ_REFL_COUNTER = 3; // ���������
	static final int GQ_FLY_V = 7;

	// #if size_500k
	/**
	 * ��ٴAI
	 * 
	 * @return vodi
	 */
	void ai_gaoQiu() {
		CHero hero = CGame.m_hero;

		// �ڵ�
		if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_fire) {
			if (testFlag(dActor.FLAG_NEED_INIT)) {
				clearFlag(dActor.FLAG_NEED_INIT);
				return;
			}

			if (testColliding(hero)) {
				if (hero.beAttack(m_actorProperty[PRO_INDEX_ATT])) {
					hero.setAnimAction(0);
				}
			}
			doPhysics();
			if (m_bBlockedBottom) {
				die(false);
			}
			return;
		}
		// ����
		else if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_ball) {
			if (testFlag(dActor.FLAG_NEED_INIT)) {
				clearFlag(dActor.FLAG_NEED_INIT);
				return;
			}

			if (testColliding(hero)) {
				if (hero.beAttack(m_actorProperty[PRO_INDEX_ATT])) {
					hero.setAnimAction(0);
				}
			}
			doPhysics();
			getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
			if (m_bBlockedFront) {
				m_vX = -m_vX;
				m_parameters[INDEX_GQ_REFL_COUNTER]++;
			} else if (m_bBlockedBottom) {
				m_vY = -m_vY;
				m_parameters[INDEX_GQ_REFL_COUNTER]++;
			} else if (m_bBlockedTop || s_colBox1[1] <= 1) {
				m_vY = -m_vY;
				m_parameters[INDEX_GQ_REFL_COUNTER]++;
			}
			if (m_parameters[INDEX_GQ_REFL_COUNTER] > 4) {
				die(false);
			}
			return;
		}

		if (testFlag(dActor.FLAG_NEED_INIT)) {
			int level = getActorInfo(dActorClass.Index_Param_BOSS_GAO_LEVEL);
			m_actorProperty[PRO_INDEX_ROLE_ID] = getActorInfo(dActorClass.Index_Param_BOSS_GAO_LIST);
			// ͨ��������ʽ�������
			levelUp(level);
			clearFlag(dActor.FLAG_NEED_INIT);
			setState(ST_ACTOR_WAIT);
			m_phyAttrib &= ~PHYATTRIB_GRAVITY;
			droptoGround();
			GameEffect.init_BossHp(m_actorProperty[PRO_INDEX_MAX_HP]);
			return;
		}

		// ����
		if (testFlag(dActor.FLAG_BEATTACKED)) {
			if (m_actorProperty[PRO_INDEX_HP] <= 0) {
				setState(ST_ACTOR_DIE);
				setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_die);
				// ����������������
				if (getActorInfo(dActorClass.Index_Param_BOSS_TOUMU_DIEEFFECT) > 0) {
					CGame.s_screenFlashTimer = 16;
					CGame.s_screenFlashInterval = 3;
					CGame.s_screenFlashColor = dConfig.COLOR_RED;
					Player.useSlowMotion = true;
				}
			} else {
				setState(ST_ACTOR_HURT);
			}
			clearFlag(dActor.FLAG_BEATTACKED);
		}

		boolean actionOver = aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER);
		m_timer++;
		// ����Ѫ��
		GameEffect.set_BossHp(m_actorProperty[PRO_INDEX_HP]);
		switch (m_currentState) {
		case ST_ACTOR_WAIT:
			if (actionOver) {
				if (!isFaceTo(hero)) {
					turnAround();
				}
				int random = CTools.random(0, 100);
				// ��
				if (random < 35) {
					setState(ST_ACTOR_WALK);
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_walk);
				}
				// ����
				else if (random < 80) {
					setState(ST_GQ_FLY_UP);
					clearAllAddition();
					setFaceTo(hero);
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_fly_read);
					m_parameters[INDEX_GQ_HURT_COUNTER] = 0;
					random = CTools.random(0, 100);
					if (random > 50) {
						// ����
						m_parameters[INDEX_GQ_ATT_MODE] = 0;
						m_parameters[INDEX_GQ_FIRE_COUNTER] = 0;
					} else {
						// ����
						m_parameters[INDEX_GQ_ATT_MODE] = 1;
					}
				}
				// ����wait
				else {
					aniPlayer.clearAniPlayFlag(Player.FLAG_ACTION_OVER);
				}
			} else {

			}
			break;
		case ST_ACTOR_WALK:
			m_invincible = 1;
			if (testColliding(hero)) {
				if (hero.beAttack(m_actorProperty[PRO_INDEX_ATT])) {
					hero.setAnimAction(0);
				}
			}
			if (!isFaceTo(hero)) {
				setState(ST_GQ_SLOW);
				m_aX = -m_vX / 15;
			}
			break;
		case ST_GQ_SLOW:
			if (Math.abs(m_vX) <= 300 || m_bBlockedFrontSolid) {
				setState(ST_ACTOR_WAIT);
				setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_stand);
			}
			break;
		case ST_GQ_FLY_UP:
			m_invincible = 1;
			// ����׼��
			if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_fly_read) {
				if (actionOver) {
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_fly_up);
					m_timer = 0;
				}
			}
			// ������
			else if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_fly_up) {
				if (m_timer >= 23) {
					setState(ST_GQ_FLY);
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_fly);
					// ����
					if (m_parameters[INDEX_GQ_ATT_MODE] == 0) {
						int dis = hero.m_x - m_x;
						if (Math.abs(dis) <= m_vX) {
							setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_fly_attack);
						} else {
							m_vX = (dis > 0 ? GQ_FLY_V : -GQ_FLY_V) << dConfig.FRACTION_BITS;
						}
					}
					// ����
					else {
						int dis = Camera.cameraCenterX
								- (m_x >> dConfig.FRACTION_BITS);
						if (Math.abs(dis) < 15) {
							setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_attack1);
						} else {
							m_vX = (dis > 0 ? GQ_FLY_V : -GQ_FLY_V) << dConfig.FRACTION_BITS;
						}
					}
				}
			}
			break;
		case ST_GQ_FLY:
			m_invincible = 1;
			// ����״̬
			if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_fly) {
				// ����
				if (m_parameters[INDEX_GQ_ATT_MODE] == 0) {
					int dis = hero.m_x - m_x;
					if (Math.abs(dis) <= m_vX) {
						setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_fly_attack);
					} else {
						m_vX = (dis > 0 ? GQ_FLY_V : -GQ_FLY_V) << dConfig.FRACTION_BITS;
					}
				}
				// ����
				else {
					int dis = Camera.cameraCenterX
							- (m_x >> dConfig.FRACTION_BITS);
					if (Math.abs(dis) < 20 || m_timer > 60) {
						setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_attack1);
					}
				}
			}
			// ����
			else if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_fly_attack) {
				if (isAttackKeyFrame()) {
					getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
					CObject bullet = allocActor(
							getActorInfo(dActorClass.Index_Param_BOSS_GAO_BULLET),
							s_colBox1[0] << dConfig.FRACTION_BITS,
							s_colBox1[1] << dConfig.FRACTION_BITS, false);
					if (bullet != null) {
						bullet
								.setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_fire);
						bullet.m_vY = 17 << dConfig.FRACTION_BITS;
						System.arraycopy(m_actorProperty, 0,
								bullet.m_actorProperty, 0,
								m_actorProperty.length);
						bullet.m_actorProperty[PRO_INDEX_HP] = bullet.m_actorProperty[PRO_INDEX_CRI_RATE] = 0;
					}
					getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
					bullet = allocActor(
							getActorInfo(dActorClass.Index_Param_BOSS_GAO_BULLET),
							s_colBox1[0] << dConfig.FRACTION_BITS,
							s_colBox1[1] << dConfig.FRACTION_BITS, false);

					if (bullet != null) {
						bullet
								.setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_fire);
						bullet.m_vY = 12 << dConfig.FRACTION_BITS;
						bullet.m_vX = -12 << dConfig.FRACTION_BITS;
						System.arraycopy(m_actorProperty, 0,
								bullet.m_actorProperty, 0,
								m_actorProperty.length);
						bullet.m_actorProperty[PRO_INDEX_HP] = bullet.m_actorProperty[PRO_INDEX_CRI_RATE] = 0;
					}
					getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
					bullet = allocActor(
							getActorInfo(dActorClass.Index_Param_BOSS_GAO_BULLET),
							s_colBox1[0] << dConfig.FRACTION_BITS,
							s_colBox1[1] << dConfig.FRACTION_BITS, false);
					if (bullet != null) {
						bullet
								.setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_fire);
						bullet.m_vY = 12 << dConfig.FRACTION_BITS;
						bullet.m_vX = 12 << dConfig.FRACTION_BITS;
						System.arraycopy(m_actorProperty, 0,
								bullet.m_actorProperty, 0,
								m_actorProperty.length);
						bullet.m_actorProperty[PRO_INDEX_HP] = bullet.m_actorProperty[PRO_INDEX_CRI_RATE] = 0;
					}
				}
				if (actionOver) {
					m_parameters[INDEX_GQ_FIRE_COUNTER]++;
					// ����
					if (m_parameters[INDEX_GQ_FIRE_COUNTER] >= 4) {
						setState(ST_GQ_FLY_DOWN);
						setFaceTo(hero);
						setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_down_read);
					} else {
						setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_fly);
					}
				}
			}
			// ��������
			else if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_attack1) {
				if (isAttackKeyFrame()) {
					getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
					CObject ball = allocActor(
							getActorInfo(dActorClass.Index_Param_BOSS_GAO_BULLET),
							s_colBox1[0] << dConfig.FRACTION_BITS,
							s_colBox1[1] << dConfig.FRACTION_BITS, false);
					if (ball != null) {
						ball
								.setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_ball);
						int angle = CTools.arcTan(hero.m_x - m_x,
								(hero.m_y - 30 * 256) - m_y);
						ball.m_vX = CTools.lenCos(18, angle) << dConfig.FRACTION_BITS;
						ball.m_vY = CTools.lenSin(18, angle) << dConfig.FRACTION_BITS;
						System
								.arraycopy(m_actorProperty, 0,
										ball.m_actorProperty, 0,
										m_actorProperty.length);
						ball.m_actorProperty[PRO_INDEX_HP] = ball.m_actorProperty[PRO_INDEX_CRI_RATE] = 0;
					}
				}

				if (actionOver) {
					setState(ST_GQ_FLY_DOWN);
					setFaceTo(hero);
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_down);
				}
			}
			break;
		case ST_GQ_FLY_DOWN:
			m_invincible = 1;
			if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_down_read) {
				if (actionOver) {
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_down);
				}
			} else if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_down) {
				if (m_bBlockedBottom) {
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_down_land);
				}
			} else if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_down_land) {
				if (actionOver) {
					setState(ST_ACTOR_WAIT);
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_stand);
				}
			}
			break;
		case ST_ACTOR_HURT:
			if (actionOver) {
				if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_hurt3) {
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_lie);
				} else if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_lie
						|| m_parameters[INDEX_GQ_HURT_COUNTER] >= 3) {
					setState(ST_GQ_FLY_UP);
					clearAllAddition();
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_fly_read);
					m_parameters[INDEX_GQ_HURT_COUNTER] = 0;
					if (CTools.random(0, 100) > 50) {
						// ����
						m_parameters[INDEX_GQ_ATT_MODE] = 0;
						m_parameters[INDEX_GQ_FIRE_COUNTER] = 0;
					} else {
						// ����
						m_parameters[INDEX_GQ_ATT_MODE] = 1;
					}

				} else {
					setState(ST_ACTOR_WAIT);
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_stand);
					m_parameters[INDEX_GQ_HURT_COUNTER]++;
				}
			}
			break;
		case ST_ACTOR_DIE:
			if (actionOver) {
				if (m_actionID == dActionID.Action_ID_Boss_Gaoqiu_die) {
					setAnimAction(dActionID.Action_ID_Boss_Gaoqiu_lie2);
					setFlag(dActor.FLAG_DIE_TO_SCRIPT);
					GameEffect.prepareToRender(GameEffect.NO_RENDER,
							GameEffect.EFF_BOSS_HP);
					// ����������
					if (getActorInfo(dActorClass.Index_Param_BOSS_GAO_DIEEFFECT) > 0) {
						Player.useSlowMotion = false;
					}
				} else {
					// if (testFlag(dActor.FLAG_DIE_TO_SCRIPT)) {

					die(false);
					// }
					// else {
					// //�ű����������ս����ʤ
					// setFlag(dActor.FLAG_DIE_TO_SCRIPT);
					// setAnimAction(m_actionID);
					//
					// }
				}
			}
			break;
		}

		// ����߼�����
		if (!testFlag(dActor.FLAG_BASIC_DIE)) {
			doPhysics();
		}

	}

	// #endif

	// ------------״̬��
	static final int ST_WASP_PREPARE = ST_COUNT + 0;

	// ------------����
	// �������Ϸ��ȴ���λ��
	static final int WASP_ABOVE_DIS = 110;
	static final int WASP_HORIZONTAL_DIS = 70;
	// �������Ϸ��ȴ���ʱ��
	static final int WASP_PREPARE_TIME = 15;
	// ��ͨ״̬���ƶ��ٶ�
	static final int WASP_NORMAL_V = 4;

	// ------------����������
	static final int INDEX_WASP_HORIZONTAL_DIS = 2;
	static final int INDEX_WASP_ABOVE_DIS = 3;

	/**
	 * �Ʒ�
	 */
	void ai_wasp() {
		// ����
		if (m_actionID == dActionID.Action_ID_mifeng_ci) {
			if (testFlag(dActor.FLAG_NEED_INIT)) {
				clearFlag(dActor.FLAG_NEED_INIT);
				return;
			}

			m_vX += m_aX;
			m_vY += m_aY;
			m_x += m_vX;
			m_y += m_vY;
			if (testColliding(CGame.m_hero)) {
				if (CGame.m_hero.beAttack(m_actorProperty[PRO_INDEX_ATT])) {
					CGame.m_hero.setAnimAction(0);
				}
				die(false);
			}
			return;
		}

		if (testFlag(dActor.FLAG_NEED_INIT)) {
			int level = getActorInfo(dActorClass.Index_Param_ENEMY_MAFENG_LEVEL);
			m_actorProperty[PRO_INDEX_ROLE_ID] = getActorInfo(dActorClass.Index_Param_ENEMY_MAFENG_LIST);
			// ͨ��������ʽ�������
			levelUp(level);
			clearFlag(dActor.FLAG_NEED_INIT);
			setState(ST_ACTOR_WAIT);
			waspResetTargetPosition();
			// m_actorProperty[PRO_INDEX_HP]=m_actorProperty[PRO_INDEX_MAX_HP]=500;
			return;
		}

		if (isRemote(150, 100)) {
			return;
		}

		if (testFlag(dActor.FLAG_BEATTACKED)) {
			if (m_actorProperty[PRO_INDEX_HP] <= 0) {
				setState(ST_ACTOR_DIE);
				setAnimAction(dActionID.Action_ID_mifeng_die);
			} else {
				// setState(ST_ACTOR_HURT);
			}
			clearFlag(dActor.FLAG_BEATTACKED);
		}

		CHero hero = CGame.m_hero;
		hero.getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
		int heroCenterX = (s_colBox1[0] + s_colBox1[2]) / 2 << dConfig.FRACTION_BITS;
		int heroCenterY = (s_colBox1[1] + s_colBox1[3]) / 2 << dConfig.FRACTION_BITS;
		int heroTopY = (s_colBox1[1] + 3) << dConfig.FRACTION_BITS;
		int heroBottomY = s_colBox1[3] << dConfig.FRACTION_BITS;

		// Ŀ��㣬�����Ϸ�
		int heroAboveX = heroCenterX
				+ ((m_x <= heroCenterX ? -m_parameters[INDEX_WASP_HORIZONTAL_DIS]
						: m_parameters[INDEX_WASP_HORIZONTAL_DIS]) << dConfig.FRACTION_BITS);
		int heroAboveY = heroBottomY
				- (m_parameters[INDEX_WASP_ABOVE_DIS] << dConfig.FRACTION_BITS);
		// �����������򲻻ᱻ����
		setFlag(dActor.FLAG_BASIC_NOT_PACKABLE);

		// ��������
		if (m_actorProperty[PRO_INDEX_HP] > 0) {
			setFaceTo(hero);
		}
		m_timer++;

		// ������Ļ��Զ
		if (isRemote(80, 80)) {
			return;
		}
		switch (m_currentState) {
		// ------------��ͨ״̬
		case ST_ACTOR_WAIT:
			int dis = CTools.getDistance(heroAboveX - m_x, heroAboveY - m_y);
			// δ����Ŀ���
			if (dis > CTools.getDistance(m_vX, m_vY)
					+ (1 << dConfig.FRACTION_BITS)) {
				if (hero.m_y > m_y && hero.m_vY < 0) {
					m_vY = m_aY = m_vX = m_aX = 0;
				} else {
					int angle = CTools.arcTan(heroAboveX - m_x, heroAboveY
							- m_y);
					m_vX = CTools.lenCos(WASP_NORMAL_V, angle) << dConfig.FRACTION_BITS;
					m_vY = CTools.lenSin(WASP_NORMAL_V, angle) << dConfig.FRACTION_BITS;
				}
			} else if (hero.m_actorProperty[PRO_INDEX_HP] > 0 && m_timer >= 20) {
				// ׼������
				setState(ST_WASP_PREPARE);
				m_vX = m_vY = 0;
				m_aX = m_aY = 0;
			}
			break;
		// ------------׼������
		case ST_WASP_PREPARE:
			if (m_timer >= WASP_PREPARE_TIME) {
				setState(ST_ACTOR_ATTACK);
				setAnimAction(dActionID.Action_ID_mifeng_attack);
			}
			break;
		// ------------����
		case ST_ACTOR_HURT:

			break;
		case ST_ACTOR_DIE:
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				die(false);
			}
			// ������Ļ����ʧ
			if (isRemote(10, 10)) {
				die(false);
				return;
			}
			break;
		case ST_ACTOR_ATTACK:
			getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
			if (s_colBox1[0] != s_colBox1[2] && s_colBox1[1] != s_colBox1[3]) {
				int angle = CTools.arcTan(heroCenterX
						- (s_colBox1[0] << dConfig.FRACTION_BITS), heroCenterY
						- (s_colBox1[1] << dConfig.FRACTION_BITS));

				// �ӵ�
				CObject bullet = allocActor(
						getActorInfo(dActorClass.Index_Param_ENEMY_MAFENG_BULLET),
						s_colBox1[0] << dConfig.FRACTION_BITS,
						s_colBox1[1] << dConfig.FRACTION_BITS,
						getFaceDir() == -1);
				if (bullet != null) {
					bullet.m_vX = CTools.lenCos(17, angle) << dConfig.FRACTION_BITS;
					bullet.m_vY = CTools.lenSin(27, angle) << dConfig.FRACTION_BITS;
					bullet.m_z = 100;
					System.arraycopy(m_actorProperty, 0,
							bullet.m_actorProperty, 0, m_actorProperty.length);
					bullet.m_actorProperty[PRO_INDEX_HP] = bullet.m_actorProperty[PRO_INDEX_CRI_RATE] = 0;
				}
			}

			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setState(ST_ACTOR_WAIT);
				setAnimAction(dActionID.Action_ID_mifeng_wait);
				waspResetTargetPosition();
			}
			break;
		}

		m_vX += m_aX;
		m_vY += m_aY;
		m_x += m_vX;
		m_y += m_vY;
	}

	/**
	 * Ŀ�꣬�����Ϸ�
	 */
	void waspResetTargetPosition() {
		m_parameters[INDEX_WASP_HORIZONTAL_DIS] = WASP_HORIZONTAL_DIS
				+ CTools.random(-20, 20);
		m_parameters[INDEX_WASP_ABOVE_DIS] = WASP_ABOVE_DIS
				+ CTools.random(-20, 20);
	}

	public void doCollionWithActor() {
		int shellId = CGame.activeActorsListHead;
		while (shellId != -1) {
			CObject enemy = CGame.m_actorShells[shellId];

			// ������赲����
			if (enemy != this
					&& enemy.testClasssFlag(dActor.CLASS_FLAG_BLOCKABLE)) {
				if (m_phyEvx != 0) {
					m_vX += m_phyEvx;
				}
				if (enemy != this && checkCollionWithActor(enemy)) {
					checkRelativeMisc();
				} else {
					if (m_phyEvx != 0) {
						m_vX -= m_phyEvx;
					}
				}

			}
			// ����ǿ����ƶ��Ķ���
			// else if(enemy != this && enemy.testClasssFlag
			// (dActor.CLASS_FLAG_PUSHABLE)) {
			// enemy.getActorOppBoxInfo (dActor.BOX_COLLIDE, s_colBox2);
			// if(!enemy.m_bBlockedBottom) {
			// CGame.m_hero.postMessage (MSG_BOX_AWAY, this);
			// }
			// else if(intersectBox (enemy.m_x, enemy.m_y, s_colBox2, 8)) {
			// postMessage (MSG_BOX_NEAR, enemy);
			//
			// int col = collide_With_Actor (enemy, CLASS_CAN_COLLIDE_UNDERSIDE
			// | CLASS_CAN_COLLIDE_FRONT);
			// m_colWithActorFlag = col;
			// checkRelativeMisc ();
			// }
			// else {
			// postMessage (MSG_BOX_AWAY, enemy);
			// }
			// }
			shellId = CGame.nextActorShellID[shellId];
			//
		}

	}

	public boolean checkCollionWithActor(CObject obj) {

		int aiId = ResLoader.classAIIDs[obj.m_classID];
		switch (aiId) {
		// case dActorClass.CLASS_ID_OBJ_MOVEPLAT:
		// case dActorClass.CLASS_ID_OBJ_JUMPPLAT:
		// if(m_relativeMisc != obj || m_relativeMisc == null) {
		// int col = collide_With_Actor (obj, CLASS_CAN_COLLIDE_UNDERSIDE);
		// if( (col & (CLASS_CAN_COLLIDE_UNDERSIDE)) != 0) {
		// m_colWithActorFlag = col;
		// m_relativeMisc = obj;
		// return true;
		// }
		// }
		// else {
		// m_relativeMisc.getActorBoxInfo (dActor.BOX_COLLIDE, s_colBox1);
		// getActorBoxInfo (dActor.BOX_COLLIDE, s_colBox2);
		// if(m_relativeMisc == obj && !CTools.isIntersecting (s_colBox1,
		// s_colBox2)) {
		// m_colWithActorFlag = 0;
		// m_relativeMisc = null;
		// return false;
		// }
		// }
		// break;
		case dActorClass.CLASS_ID_OBJ_DOOR:
		case dActorClass.CLASS_ID_OBJ_CAR:
			if (m_relativeMisc != obj || m_relativeMisc == null) {
				int col = collide_With_Actor(obj, CLASS_CAN_COLLIDE_FRONTBACK
						| CLASS_CAN_COLLIDE_TOPUNDER);
				if ((col & (CLASS_CAN_COLLIDE_FRONTBACK)) != 0) {
					m_colWithActorFlag = col;
					m_relativeMisc = obj;
					return true;
				}
			} else {
				m_relativeMisc.getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
				getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox2);
				if (m_relativeMisc == obj
						&& !CTools.isIntersecting(s_colBox1, s_colBox2)) {
					m_colWithActorFlag = 0;
					m_relativeMisc = null;
					return false;
				}
			}
			break;

		//
		// case dActorClass.CLASS_ID_LEVEL3ACTOR:
		// if(m_relativeMisc != obj || m_relativeMisc == null) {
		// int col = collide_With_Actor (obj, CLASS_CAN_COLLIDE_TOPUNDER |
		// CLASS_CAN_COLLIDE_FRONT);
		// if( (col & (CLASS_CAN_COLLIDE_TOPUNDER | CLASS_CAN_COLLIDE_FRONT)) !=
		// 0) {
		// m_colWithActorFlag = col;
		// m_relativeMisc = obj;
		// return true;
		// }
		// }
		// else {
		// m_relativeMisc.getActorBoxInfo (dActor.BOX_COLLIDE, s_colBox1);
		// getActorBoxInfo (dActor.BOX_COLLIDE, s_colBox2);
		// if(m_relativeMisc == obj && !CTools.isIntersecting (s_colBox1,
		// s_colBox2)) {
		// m_colWithActorFlag = 0;
		// m_relativeMisc = null;
		// return false;
		// }
		// }
		// break;
		}
		return true;
	}

}
