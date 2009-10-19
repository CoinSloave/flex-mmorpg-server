package game.object;

import game.CDebug;
import game.CGame;
import game.CMIDlet;
import game.CTools;
import game.Goods;
import game.config.dConfig;
import game.config.dGame;
import game.key.CKey;
import game.pak.Camera;
import game.res.ResLoader;
import game.rms.Record;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import com.mglib.mdl.ani.AniPlayer;
import com.mglib.mdl.ani.Player;
import com.mglib.mdl.map.MapData;
import com.mglib.mdl.map.MapDraw;
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
public class CHero extends CObject implements dHero {
	CGame cGame;

	public CHero() {
		cGame = CMIDlet.display;
	}

	public void initialize() {
		super.initialize();
		m_phyAttrib |= PHYATTRIB_COLENGINE | PHYATTRIB_GRAVITY;
		droptoGround();
		setState(ST_ACTOR_WAIT, -1, true);
		initShadow();
		upLevelAni = null;
		skillFlashAni = null;
	}

	/**
	 * ���Գ�ʼ��
	 */
	public void initActorProprety() {
		hsEquipList.clear();
		hsItemList.clear();
		skills = new Goods[2];

		for (int i = 0; i < m_actorProperty.length; i++) {
			m_actorProperty[i] = 0;
		}
		m_actorProperty[IObject.PRO_INDEX_LORICAE] = -1;
		m_actorProperty[IObject.PRO_INDEX_SHOES] = -1;
		m_actorProperty[IObject.PRO_INDEX_WEAPON] = -1;
		m_actorProperty[IObject.PRO_INDEX_JADE] = -1;
		m_actorProperty[IObject.PRO_PATH_ID] = -1;
		m_actorProperty[IObject.PRO_PATH_END] = 1;
		m_actorProperty[PRO_WEAPON_NUM] = 1;
		m_actorProperty[PRO_CUR_WP] = 0;

		levelUp(1);
		// ����Ϸ������ʾ��������
		bshowUpLevelAni = false;
		m_bonus.removeAllElements();

		m_actorProperty[PRO_INDEX_ROLE_ID] = 0;
		m_animationID = dActorClass.Animation_ID_HERO;

		// 2�����ǵĳ�ʼ������װ��
		Goods basicWeapon = null;
		if (Data.ROLE_INIT_EQUIP[0][0] >= 0) {
			basicWeapon = Goods.createGoods((short) 0,
					Data.ROLE_INIT_EQUIP[0][0]);
			addAEquip(basicWeapon);
			putOnEquip(basicWeapon);
		}
		if (Data.ROLE_INIT_EQUIP[1][0] >= 0) {
			basicWeapon = Goods.createGoods((short) 0,
					Data.ROLE_INIT_EQUIP[1][0]);
			addAEquip(basicWeapon);
			// ��һ������
			if (basicWeapon != null) {
				Record.savedWeaponId = basicWeapon.getKey();
			} else {
				Record.savedWeaponId = -1;
			}
		}

		// ��ʼ����
		Goods skill = Goods.createGoods((short) 2,
				Data.ROLE_INFO[0][Data.IDX_RI_SKILL0]);
		skills[0] = skill;
		skill = Goods.createGoods((short) 2,
				Data.ROLE_INFO[0][Data.IDX_RI_SKILL1]);
		skills[1] = skill;

	}

	/**
	 * �л����� ����<-->����
	 * 
	 * @param roleId
	 *            int
	 * @param showChangePose
	 *            boolean ��ʾ�л���pose
	 */
	public void switchHero(int roleId, boolean showChangePose) {
		// ������
		if (roleId == 0) {
			m_animationID = dActorClass.Animation_ID_HERO;// ����
		} else {
			m_animationID = dActorClass.Animation_ID_HERO_LIKUI;// ����
		}
		aniPlayer = new AniPlayer(ResLoader.animations[m_animationID],
				m_x >> dConfig.FRACTION_BITS, m_y >> dConfig.FRACTION_BITS);
		suitInfo = aniPlayer.aniData.getDefaultMLGInfo();
		if (showChangePose) {
			// ������pose
			setState(ST_ACTOR_CHANGE, -1, false);
			if (roleId == 0) {
				setAnimAction(action_map[ST_ACTOR_SQUAT][SQUAT_INDEX]);
			} else {
				setAnimAction(action_map[ST_ACTOR_WIN][WIN_INDEX]);
			}
		} else {
			setState(ST_ACTOR_WAIT, -1, true);
		}

		Goods curWeapon = null;
		if (m_actorProperty[PRO_INDEX_ROLE_ID] != roleId) {
			m_actorProperty[PRO_INDEX_ROLE_ID] = (short) roleId;
			// ������
			short curWeaponId = m_actorProperty[PRO_INDEX_WEAPON];
			putOffEquip((Goods) hsEquipList.get(curWeaponId + ""));
			curWeapon = (Goods) hsEquipList.get(Record.savedWeaponId + "");
			putOnEquip(curWeapon);
			m_actorProperty[PRO_INDEX_WEAPON] = Record.savedWeaponId;
			Record.savedWeaponId = curWeaponId;
			// ������
			Goods[] temp = skills;
			skills = Record.savedSkills;
			Record.savedSkills = temp;
		}
		// ��һ��װ����ˢ�»�װ��Ϣ
		curWeapon = (Goods) hsEquipList.get(m_actorProperty[PRO_INDEX_WEAPON]
				+ "");
		putOnEquip(curWeapon);
	}

	/**
	 * ��ȡ�浵����Ϣ
	 */
	public void getSavedInf(CHero savedHero) {
		// װ��
		this.hsEquipList = savedHero.hsEquipList;
		// ��Ʒ
		this.hsItemList = savedHero.hsItemList;

		// ����
		this.skills = savedHero.skills;
		// ����
		this.m_actorProperty = savedHero.m_actorProperty;
		strLevel = m_actorProperty[PRO_INDEX_LEVEL] + "";
	}

	/**
	 * ֻ�л�״̬
	 * 
	 * @param newState
	 *            int
	 * @return boolean
	 */
	public boolean setState(int newState) {
		super.setState(newState);
		if ((newState == ST_ACTOR_WALK || newState == ST_ACTOR_WAIT || newState == ST_ACTOR_RUN)
				&& m_relativeMisc == null) {
			m_phyAttrib |= PHYATTRIB_GRAVITY;
		} else {
			m_phyAttrib &= ~PHYATTRIB_GRAVITY;
		}

		m_posInCamera = (HERO_IN_CAMERA_X_BATTLE) | HERO_IN_CAMERA_Y_BOTTOM;

		switch (newState) {

		}

		return true;
	}

	/**
	 * ���ö���״̬ �� ���� ����״̬
	 * 
	 * @param newState
	 *            int
	 * @param flag
	 *            int
	 * @param isSetAni
	 *            boolean
	 * @return boolean
	 */
	public boolean setState(int newState, int flag, boolean isSetAni) {
		setState(newState);
		isInAir = false;
		if (isSetAni) {
			// ���ö����Ĳ���״̬
			if (flag != -1) {
				this.aniPlayer.setAniPlayFlag(flag);
			}
			switch (newState) {
			// ����
			case ST_ACTOR_CHANGE:
				setAnimAction(action_map[ST_ACTOR_CHANGE][CHANGE_INDEX]);
				break;
			case ST_ACTOR_WAIT:
				setAnimAction(action_map[ST_ACTOR_WAIT][WAIT_ACT_INDEX]);
				break;

			case ST_ACTOR_WALK:
				break;

			case ST_ACTOR_RUN:
				setAnimAction(action_map[ST_ACTOR_RUN][RUN_ACT_INDEX]);
				break;

			case ST_ACTOR_HURT:
				setAnimAction(action_map[ST_ACTOR_HURT][HURT_ACT_INDEX]);
				break;

			case ST_ACTOR_JUMP:
				setAnimAction(action_map[ST_ACTOR_JUMP][JUMP_UP_INDEX]);
				break;

			case ST_ACTOR_SQUAT:
				setAnimAction(action_map[ST_ACTOR_SQUAT][STAND_TO_SQUAT_INDEX]);
				break;

			case ST_ACTOR_LAND:
				// �ߴ����£�����land2
				if (m_y - jumpTopY > 16 * JUMP_H_LIMITE << dConfig.FRACTION_BITS) {
					setAnimAction(action_map[ST_ACTOR_LAND][LAND2_INDEX]);
				} else {
					setAnimAction(action_map[ST_ACTOR_LAND][LAND_INDEX]);
				}
				break;

			case ST_ACTOR_FLY:
				setAnimAction(action_map[ST_ACTOR_FLY][FLY_INDEX]);
				break;

			case ST_ACTOR_JINK:
				setAnimAction(action_map[ST_ACTOR_JINK][JINK_INDEX]);
				break;

			case ST_ACTOR_ATTACK_SP:
				setAnimAction(action_map[ST_ACTOR_ATTACK_SP][ATTACK_SP_INDEX]);
				break;

			default:
				break;
			}
		}
		return true;
	}

	/*************
	 * ���ǵ�AI����
	 *************/
	// �Ƿ������ҷ����
	public int getDirectionKey(boolean isFront) {
		return (isFront ^ testFlag(dActor.FLAG_BASIC_FLIP_X)) ? CKey.GK_RIGHT
				: CKey.GK_LEFT;
	}

	// ��Ծ�İ���
	public int getJumpKey(boolean isFront) {
		return (isFront != testFlag(dActor.FLAG_BASIC_FLIP_X)) ? CKey.GK_NUM3
				: CKey.GK_NUM1;
	}

	public boolean beAttack(int damage) {
		if (super.beAttack(damage)) {
			m_invincible = 12;
			return true;
		}
		return false;
	}

	private void ai_Hero() {
		// ������л����� �����߼�
		// if(checkWeaponPlayer())
		// return;
		// �����������
		checkComboKey();
		switch (m_currentState) {
		case ST_ACTOR_CHANGE:
			do_Change();
			break;
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

		case ST_ACTOR_HURT:
			do_Hurt();
			break;

		case ST_ACTOR_JUMP:
			do_Jump();
			break;

		case ST_ACTOR_SQUAT:
			do_Squat();
			break;

		case ST_ACTOR_LAND:
			do_Land();
			break;

		case ST_ACTOR_FLY:
			do_Fly();
			break;

		case ST_ACTOR_JINK:
			// �޵�
			m_invincible = 2;
			do_Jink();
			break;

		case ST_ACTOR_ATTACK_SP:
			// �޵�
			m_invincible = 2;
			do_AttackSp();
			break;

		case ST_ACTOR_DIE:
			do_Die();
			break;

		default:
			break;
		}
	}

	private void do_Die() {
		if (m_actionID == action_map[ST_ACTOR_DIE][DIE_INDEX]) {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				s_heroDie = true;
			}
		}
	}

	private boolean orderToRun() {
		// ��
		if (CKey.isKeyHold(getDirectionKey(true))
				|| CKey.isKeyPressed(getDirectionKey(true))) {
			setState(ST_ACTOR_RUN, -1, true);
			return true;
		}
		return false;

	}

	private boolean orderToTurn() {
		// ��
		if (CKey.isKeyHold(getDirectionKey(false))
				|| CKey.isKeyPressed(getDirectionKey(false))) {
			// setAnimAction (action_map[ST_ACTOR_WAIT][TURN_ACT_INDEX]);
			turnAround();
			return true;
		}
		return false;

	}

	private void orderToJump() {
		if (CKey.isKeyPressed(CKey.GK_UP) || CKey.isKeyHold(CKey.GK_UP)) {
			setState(ST_ACTOR_JUMP, -1, true);
			return;
		} else if (CKey.isKeyPressed(getJumpKey(true))
				|| CKey.isKeyHold(getJumpKey(true))) {
			setState(ST_ACTOR_JUMP, -1, false);
			setAnimAction(action_map[ST_ACTOR_JUMP][INCLINED_UP_INDEX]);
		} else if (CKey.isKeyPressed(getJumpKey(false))
				|| CKey.isKeyHold(getJumpKey(false))) {
			turnAround();
			// setState (ST_ACTOR_WAIT, -1, false);
			// setAnimAction (action_map[ST_ACTOR_WAIT][TURN_ACT_INDEX]);
			setState(ST_ACTOR_JUMP, -1, false);
			setAnimAction(action_map[ST_ACTOR_JUMP][INCLINED_UP_INDEX]);
		}
	}

	private boolean orderToSquat() {
		if (CKey.isKeyPressed(CKey.GK_DOWN) || CKey.isKeyHold(CKey.GK_DOWN)) {
			setState(ST_ACTOR_SQUAT, -1, true);
			return true;
		}
		return false;

	}

	private boolean orderToFall() {
		if (!m_bBlockedBottom) {
			isInAir = true;
			setState(ST_ACTOR_JUMP);
			setAnimAction(action_map[ST_ACTOR_JUMP][JUMP_DOWN_INDEX]);
			return true;
		}
		int tileX = (m_x >> dConfig.FRACTION_BITS) / MapData.TILE_WIDTH;
		int tileY = ((m_y >> dConfig.FRACTION_BITS) + 1) / MapData.TILE_HEIGHT;
		if (m_currentState == ST_ACTOR_SQUAT) {
			if (CGame.gMap.mapRes.getTilePhyEnv(tileX, tileY) == MapData.PHY_HALF_BLOCK) {
				if (CKey.isKeyPressed(CKey.GK_DOWN)) {
					isPhyDown = false;
					m_y += MapData.TILE_HEIGHT << dConfig.FRACTION_BITS;
					isInAir = true;
					setState(ST_ACTOR_JUMP);
					setAnimAction(action_map[ST_ACTOR_JUMP][JUMP_DOWN_INDEX]);
					return true;
				}
			}
		}
		return false;

	}

	private boolean checkWeaponPlayer() {
		if (weaponPlayer != null
				&& !weaponPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			return true;
		}
		return false;
	}

	private void setWeaponPlayer(int iconId, int animationID) {
		if (iconId == -1) {
			return;
		}

		int posx = m_x >> dConfig.FRACTION_BITS;
		int posy = (m_y >> dConfig.FRACTION_BITS);

		// int animationID = dActorClass.Animation_ID_EFFECT_SWICH;

		if (weaponPlayer == null) {
			weaponPlayer = new AniPlayer(ResLoader.animations[animationID],
					posx, posy, iconId);
		}
	}

	// �л�����װ��
	private static AniPlayer weaponPlayer;

	private void drawChangeWeapon(Graphics g) {
		if (weaponPlayer != null) {
			weaponPlayer.setSpritePos(m_x >> dConfig.FRACTION_BITS,
					m_y >> dConfig.FRACTION_BITS);
			// ʼ�ղ���ת
			weaponPlayer.setSpriteFlipX(1);
			weaponPlayer.drawFrame(g, null);
			weaponPlayer.updateAnimation();
			if (weaponPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				weaponPlayer.clear();
				weaponPlayer = null;
			}
		}
	}

	/**
	 * �л�����
	 * 
	 * @return boolean
	 */
	private boolean orderToChangeWeapon() {
		// if(m_actorProperty[PRO_WEAPON_NUM] <= 1) {
		//
		// }
		// if(m_actorProperty[PRO_WEAPON_NUM] == 2) {
		// if(CKey.isKeyPressed (CKey.GK_NUM9)) {
		// if(m_actorProperty[PRO_CUR_WP] == 0) {
		// m_actorProperty[PRO_CUR_WP] = 1;
		// setWeaponPlayer (dActionID.Action_ID_swich_swich4,
		// dActorClass.Animation_ID_EFFECT_SWICH);

		// putOnEquip((short)((0<<12)|2));

		//
		// }
		// else {
		// m_actorProperty[PRO_CUR_WP] = 0;
		// setWeaponPlayer (dActionID.Action_ID_swich_swich5,
		// dActorClass.Animation_ID_EFFECT_SWICH);

		// putOnEquip((short)((0<<12)|0));

		// }
		// return true;
		// }
		// }
		// else if(m_actorProperty[PRO_WEAPON_NUM] == 3) {
		// if(CKey.isKeyPressed (CKey.GK_NUM9)) {
		// if(m_actorProperty[PRO_CUR_WP] == 0) {
		// m_actorProperty[PRO_CUR_WP] = 1;
		// setWeaponPlayer (dActionID.Action_ID_swich_swich1,
		// dActorClass.Animation_ID_EFFECT_SWICH);

		// putOnEquip((short)((0<<12)|2));

		//
		// }
		// else if(m_actorProperty[PRO_CUR_WP] == 1) {
		// m_actorProperty[PRO_CUR_WP] = 2;
		// setWeaponPlayer (dActionID.Action_ID_swich_swich2,
		// dActorClass.Animation_ID_EFFECT_SWICH);

		// putOnEquip((short)((0<<12)|1));

		//
		// }
		// else {
		// m_actorProperty[PRO_CUR_WP] = 0;
		// setWeaponPlayer (dActionID.Action_ID_swich_swich3,
		// dActorClass.Animation_ID_EFFECT_SWICH);

		// putOnEquip((short)((0<<12)|0));

		//
		// }
		// return true;
		// }
		// }
		return false;
	}

	// private boolean checkPressFall () {
	// if(CMap.getPhyByPos (m_x, m_y + (1 << Def.FRACTION_BITS)) ==
	// CMap.PHY_HALF_BLOCK) {
	// if(CKey.isKeyPressed (CKey.GK_DOWN)) {
	// m_y += 1 << Def.FRACTION_BITS;
	// m_noCheckBlockY = true;
	// setState (dHero.STATE_FALL);
	// setFlag (dActor.FLAG_ACTION_NOT_CYCLE);
	// setAnimAction (dHero.m_stateMap[dHero.STATE_FALL][dHero.ID_FALL_FALL]);
	// return true;
	// }
	// }
	// return false;
	// }

	/*****************
	 * ����
	 * 
	 * @return boolean
	 ******************/
	// private boolean orderToChangeFly (boolean isCanChange) {
	// if(isCanChange) {
	// if(CKey.isKeyPressed (CKey.GK_NUM0)) {
	// setState (ST_ACTOR_FLY, -1, true);
	// return true;
	// }
	//
	// }
	// return false;
	// }

	/**************
	 * ����
	 *************/
	private boolean orderTojink() {
		// if(CKey.isKeyPressed (CKey.GK_NUM7)) {
		// setState (ST_ACTOR_JINK, -1, true);
		// return true;
		// }
		return false;
	}

	private final static int UES_MP = 50;

	/**
	 * 7��9���ż���
	 * 
	 * @return boolean
	 */
	private boolean orderToSpAttack() {
		if (CKey.isKeyPressed(CKey.GK_NUM7) || CKey.isKeyPressed(CKey.GK_NUM9)) {
			// ���ﲻ��ʹ�ü���
			if (!CGame.testSceneFlag(CGame.SCENE_FLAG_SWITCH_HERO)) {
				addBonusShow(0, "�����ڴ�ʹ�ü���", (byte) 1);
				return false;
			}

			if (m_actorProperty[PRO_INDEX_MP] >= UES_MP) {
				m_actorProperty[PRO_INDEX_MP] -= UES_MP;
				setState(ST_ACTOR_ATTACK_SP, -1, false);
				bShowSkillFlash = true;
				if (CKey.isKeyPressed(CKey.GK_NUM7)) {
					setAnimAction(action_map[ST_ACTOR_JINK][JINK_INDEX]);
				} else {
					setAnimAction(action_map[ST_ACTOR_ATTACK_SP][ATTACK_SP_INDEX]);
				}
				return true;
			} else {
				addBonusShow(0, "ħ������", (byte) 1);
			}
		}
		return false;
	}

	private void do_Wait() {
		int actionWait = action_map[ST_ACTOR_WAIT][WAIT_ACT_INDEX];
		if (orderToFall()) {
			return;
		}
		if (orderToSpAttack()) {
			return;
		}
		// �л�����
		// if(orderToChangeWeapon ()) {
		// return;
		// }
		// ����
		if (orderToAttack()) {
			return;
		}

		// if(orderToChangeFly (true)) {
		// return;
		// }
		// ����
		if (orderTojink()) {
			return;
		}

		if (m_actionID == actionWait) {
			if (!orderToTurn()) {
				if (!orderToSquat()) {
					orderToJump();
					orderToRun();

				}
			}
		}
		// else if(m_actionID == action_map[ST_ACTOR_WAIT][TURN_ACT_INDEX]) {
		// if(aniPlayer.testAniPlayFlag (aniPlayer.FLAG_ACTION_OVER)) {
		// if(!orderToRun ()) {
		// //ת��
		// turnAround();
		// setAnimAction (action_map[ST_ACTOR_WAIT][WAIT_ACT_INDEX]);
		// }
		// }
		// }

		// 0��������
		if (Script.systemVariates[Script.SV_INDEX_SCRIPT_CAN_CHANGE_HERO] == 1
				&& CKey.isKeyPressed(CKey.GK_NUM0)) {
			if (CGame.testSceneFlag(CGame.SCENE_FLAG_SWITCH_HERO)) {
				setState(ST_ACTOR_CHANGE, -1, true);
			} else {
				addBonusShow(0, "�����ڴ��л�����", (byte) 1);
			}
		}

	}

	/******************
	 * ����
	 ******************/
	private void do_Change() {
		MapDraw.bShowMap = false;
		CEnemy.bAllEnemyStop = true;
		boolean actionOver = aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER);
		if (m_actionID == action_map[ST_ACTOR_CHANGE][CHANGE_INDEX]) {
			if (actionOver) {
				switchHero((m_actorProperty[PRO_INDEX_ROLE_ID] + 1) % 2, true);
			}
		} else if (m_actionID == action_map[ST_ACTOR_WIN][WIN_INDEX]) {
			if (actionOver) {
				setState(ST_ACTOR_WAIT, -1, true);
			}
		} else if (m_actionID == action_map[ST_ACTOR_SQUAT][SQUAT_INDEX]) {
			if (actionOver) {
				setAnimAction(action_map[ST_ACTOR_SQUAT][SQUAT_TO_STAND_INDEX]);
			}
		} else if (m_actionID == action_map[ST_ACTOR_SQUAT][SQUAT_TO_STAND_INDEX]) {
			if (actionOver) {
				setState(ST_ACTOR_WAIT, -1, true);
			}
		}
	}

	/******************
	 * walk״̬���߼�
	 ******************/

	private void do_Walk() {

	}

	/******************
	 *run״̬���߼�
	 *********************/
	private void do_Run() {
		m_timer++;
		int run = action_map[ST_ACTOR_RUN][RUN_ACT_INDEX];
		int runStop = action_map[ST_ACTOR_RUN][RUN_STOP_INDEX];
		if (orderToFall()) {
			return;
		}

		if (orderToSpAttack()) {
			return;
		}

		// ����
		if (orderToAttack()) {
			return;
		}

		// �¶�
		if (orderToSquat()) {
			return;
		}

		if (orderTojink()) {
			return;
		}

		if (m_actionID == run) {
			if (!CKey.isKeyHold(getDirectionKey(true))) {
				if (m_timer > 8) {
					m_timer = 0;
					setAnimAction(runStop);
				} else {
					setState(ST_ACTOR_WAIT, -1, true);
				}
				return;
			} else {
				orderToJump();
			}
		} else if (m_actionID == runStop) {
			if (!orderToTurn()) {
				if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
					setState(ST_ACTOR_WAIT, -1, true);
				}
			} else {
				setState(ST_ACTOR_WAIT, -1, true);
			}
		}
	}

	/****************
	 * squat״̬
	 *****************/
	private boolean orderToStand() {
		if (CKey.isKeyPressed(CKey.GK_UP)) {
			// tryStand ();
			setAnimAction(action_map[ST_ACTOR_SQUAT][SQUAT_TO_STAND_INDEX]);
			// setState (ST_ACTOR_WAIT, -1, true);
			CKey.initKey();
			return true;
		}
		return false;
	}

	private void do_Squat() {
		int actionSquat = action_map[ST_ACTOR_SQUAT][SQUAT_INDEX];
		int actionSqToSt = action_map[ST_ACTOR_SQUAT][SQUAT_TO_STAND_INDEX];
		int actionStToSq = action_map[ST_ACTOR_SQUAT][STAND_TO_SQUAT_INDEX];
		int actionSqAct = action_map[ST_ACTOR_SQUAT][SQ_ACT_INDEX];
		int actionSqHurt = action_map[ST_ACTOR_SQUAT][SQ_HURT_INDEX];
		int actionSqAtt = action_map[ST_ACTOR_SQUAT][SQ_ATT_INDEX];
		if (orderToFall()) {
			return;
		}

		if (m_actionID == actionStToSq) {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setAnimAction(actionSquat);
				return;
			}
		} else if (m_actionID == actionSqToSt) {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setState(ST_ACTOR_WAIT, -1, true);
				return;
			}
		} else if (m_actionID == actionSquat) {
			if (!orderToStand()) {
				if (CKey.isKeyPressed(CKey.GK_MIDDLE)) {
					Goods heroWeapon = (Goods) hsEquipList
							.get(m_actorProperty[PRO_INDEX_WEAPON] + "");
					if (heroWeapon != null
							&& (heroWeapon.getName().equals("������")
									|| heroWeapon.getName().equals("���๭") || heroWeapon
									.getName().equals("˫�ع�"))) {
						setAnimAction(actionSqAtt + 1);
					} else {
						setAnimAction(actionSqAtt);
					}
				} else if (CKey.isKeyPressed(getDirectionKey(false))) {
					turnAround();
				}
				// �¶׻���
				else if (canSlide()
						&& (CKey.isKeyPressed(getDirectionKey(true)) || CKey
								.isKeyHold(getDirectionKey(true)))) {
					setAnimAction(actionSqAct);
				}
			}

		} else if (m_actionID == actionSqAct) {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setAnimAction(actionSquat);
			}
			attackEnemy();
		} else if (m_actionID == actionSqHurt) {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setAnimAction(actionSquat);
			}
		} else if (m_actionID == actionSqAtt || m_actionID == actionSqAtt + 1
				|| m_actionID == actionSqAtt + 2) {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setAnimAction(actionSquat);
			}
			// �������
			if (m_actorProperty[PRO_INDEX_ROLE_ID] == 0
					&& m_actionID == actionSqAtt + 1) {

				shot();
			} else {
				attackEnemy();
			}
		}

	}

	/**
	 * �ܷ񻬲�
	 * 
	 * @return boolean
	 */
	boolean canSlide() {
		// �������
		return m_actorProperty[PRO_INDEX_ROLE_ID] == 0;
	}

	/*********************
	 * ����״̬
	 *********************/
	private boolean orderToAttack() {
		if (CKey.isKeyPressed(CKey.GK_MIDDLE) || CKey.isKeyHold(CKey.GK_MIDDLE)) {
			setState(IObject.ST_ACTOR_ATTACK);
			Goods heroWeapon = (Goods) hsEquipList
					.get(m_actorProperty[PRO_INDEX_WEAPON] + "");
			// ���࣬���
			if (heroWeapon != null
					&& (heroWeapon.getName().equals("������") || heroWeapon
							.getName().equals("���๭"))) {
				setAnimAction(action_map[ST_ACTOR_ATTACK][ATT_ACT_INDEX_4]);
			} else if (heroWeapon != null
					&& (heroWeapon.getName().equals("˫�ع�"))) {
				startRecordCombo(ACTION_INDEX_1);
				checkComboKey();
				setComboAction();
			} else {
				startRecordCombo(ACTION_INDEX_0);
				checkComboKey();
				setComboAction();
			}
			return true;
		}
		// ���ӣ�ײ
		else if (m_actorProperty[PRO_INDEX_ROLE_ID] == 1
				&& CKey.isKeyDblPressed(getDirectionKey(true))) {
			setState(IObject.ST_ACTOR_ATTACK);
			setAnimAction(action_map[ST_ACTOR_SPRINT][SPRINT_INDEX]);
			return true;
		}
		return false;
	}

	private void do_Attack() {
		if (orderToFall()) {
			return;
		}

		Goods heroWeapon = (Goods) hsEquipList
				.get(m_actorProperty[PRO_INDEX_WEAPON] + "");
		// ���࣬���
		if (m_actorProperty[PRO_INDEX_ROLE_ID] == 0
				&& m_actionID == action_map[ST_ACTOR_ATTACK][ATT_ACT_INDEX_4]) {
			shot();
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setState(ST_ACTOR_WAIT, -1, true);
			}
		} else {
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				if (!setComboAction()) {
					setState(ST_ACTOR_WAIT, -1, true);
				}
				clearFlag(dHero.FLAG_ATTACK_SUCCESS);
			}
			attackEnemy();
		}
	}

	/**
	 * ���
	 */
	public void shot() {
		if (isAttackKeyFrame()) {
			getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
			CObject arrow = allocActor(
					getActorInfo(dActorClass.Index_Param_HERO_BULLET),
					s_colBox1[0] << dConfig.FRACTION_BITS,
					s_colBox1[1] << dConfig.FRACTION_BITS, getFaceDir() == -1);
			if (arrow != null) {
				System.arraycopy(m_actorProperty, 0, arrow.m_actorProperty, 0,
						m_actorProperty.length);
				Goods curbow = (Goods) hsEquipList
						.get(m_actorProperty[PRO_INDEX_WEAPON] + "");
				int arrowActionId = dActionID.Action_ID_Bullet_bulejian;
				if (curbow != null && curbow.getName().equals("������")) {
					arrowActionId = dActionID.Action_ID_Bullet_redjian;
				}
				arrow.setAnimAction(arrowActionId);
				arrow.m_parameters[CEnemy.INDEX_BULLET_FROM_HERO] = 1;
			}
		}
	}

	/**
	 * ���ܹ���
	 */
	private void do_AttackSp() {
		MapDraw.bShowMap = false;
		if (orderToFall()) {
			return;
		}

		// ���༼�ܣ������е���
		if (m_actorProperty[PRO_INDEX_ROLE_ID] == 0) {
			if (isAttackKeyFrame()) {
				getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
				// ����
				if (m_actionID == action_map[ST_ACTOR_JINK][JINK_INDEX]) {
					// �������
					int num = getKFDataReserve();
					int offsetY = num > 1 ? (s_colBox1[3] - s_colBox1[1])
							/ (num - 1) : 0;
					int offsetAngle = num > 1 ? (60 / (num - 1)) : 0;
					CObject[] bullets = new CObject[num];
					for (int i = 0; i < bullets.length; i++) {
						bullets[i] = allocActor(
								getActorInfo(dActorClass.Index_Param_HERO_BULLET),
								s_colBox1[0] << dConfig.FRACTION_BITS,
								(s_colBox1[1] + offsetY * i) << dConfig.FRACTION_BITS,
								getFaceDir() == -1);
						if (bullets[i] != null) {
							bullets[i].setAnimAction(CTools.random(
									dActionID.Action_ID_Bullet_note_01,
									dActionID.Action_ID_Bullet_note_03));
							// ���ܴ���������
							System.arraycopy(m_actorProperty, 0,
									bullets[i].m_actorProperty, 0,
									m_actorProperty.length);
							bullets[i].m_actorProperty[PRO_INDEX_ATT] = skills[0].affectProperty[PRO_INDEX_ATT];
							bullets[i].m_parameters[CEnemy.INDEX_BULLET_FROM_HERO] = 1;
							bullets[i].m_z = 100;
							// ����ɢ��
							if (bullets.length > 1) {
								int angle = (226 + offsetAngle * i) % 256;
								bullets[i].m_vX = (CTools.lenCos(17, angle) << dConfig.FRACTION_BITS)
										* getFaceDir();
								bullets[i].m_vY = CTools.lenSin(17, angle) << dConfig.FRACTION_BITS;
								// ����
								CGame.s_screenFlashTimer = 1;
								CGame.s_screenFlashInterval = 2;
								CGame.s_screenFlashColor = dConfig.COLOR_WHITE;
							}
						}
					}
				}
				// ���
				else if (m_actionID == action_map[ST_ACTOR_ATTACK_SP][ATTACK_SP_INDEX]) {
					CObject bullet = allocActor(
							getActorInfo(dActorClass.Index_Param_HERO_BULLET),
							Camera.cameraCenterX << dConfig.FRACTION_BITS,
							Camera.cameraCenterY << dConfig.FRACTION_BITS,
							false);
					if (bullet != null) {
						bullet
								.setAnimAction(dActionID.Action_ID_Bullet_Fire_phoenix1);
						// ����
						System.arraycopy(m_actorProperty, 0,
								bullet.m_actorProperty, 0,
								m_actorProperty.length);
						bullet.m_actorProperty[PRO_INDEX_ATT] = skills[1].affectProperty[PRO_INDEX_ATT];
						bullet.m_parameters[CEnemy.INDEX_BULLET_FROM_HERO] = 1;
						bullet.m_z = 100;
					}
				}
			}
		}
		// ����
		else {
			// ����ն
			if (m_actionID == action_map[ST_ACTOR_ATTACK_SP][ATTACK_SP_INDEX]) {
				if (CKey.isKeyPressed(CKey.GK_LEFT)
						|| CKey.isKeyHold(CKey.GK_LEFT)) {
					m_vX = -4 << dConfig.FRACTION_BITS;
				} else if (CKey.isKeyPressed(CKey.GK_RIGHT)
						|| CKey.isKeyHold(CKey.GK_RIGHT)) {
					m_vX = 4 << dConfig.FRACTION_BITS;
				}
			}
			attackEnemy();
		}

		if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			setState(ST_ACTOR_WAIT, -1, true);
			m_invincible = 0;
		}
	}

	private void do_Hurt() {
		if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			setState(ST_ACTOR_WAIT, -1, true);

		}
	}

	/**
	 * ���˼��
	 */
	void checkHurt() {
		if (!testFlag(dActor.FLAG_BEATTACKED)) {
			return;
		}
		// --------------����
		if (m_actorProperty[PRO_INDEX_HP] <= 0) {
			setState(ST_ACTOR_DIE);
			setAnimAction(action_map[ST_ACTOR_DIE][DIE_INDEX]);
		}
		// -------------------����
		else {
			// ����
			if (m_currentState == ST_ACTOR_JUMP) {
				setAnimAction(action_map[ST_ACTOR_JUMP][JUMP_HURT_INDEX]);
			}
			// ��
			else if (m_currentState == ST_ACTOR_SQUAT) {
				setAnimAction(action_map[ST_ACTOR_SQUAT][SQ_HURT_INDEX]);
			}
			// ����
			else {
				setState(ST_ACTOR_HURT);
				// �ǹؼ�֡����������
				if (!keyFrameHurt) {
					setAnimAction(action_map[ST_ACTOR_HURT][HURT_ACT_INDEX]);
				}
			}
		}
		clearFlag(dActor.FLAG_BEATTACKED);
		keyFrameHurt = false;
	}

	/************************
	 *��Ծ״̬���߼�
	 ************************/
	// ���ǿ���X�����ٶ�
	public static final int JUMP_V_X = 8 << dConfig.FRACTION_BITS;
	public static final int JUMP_V_S_X = 5;
	public static final int JUMP_V_F_X = 8;
	public static final int JUMP_DUR = 4;
	public static final int JUMP_AY = 3;

	private void changeJumpHByKey() {
		if (m_vY > 0) {
			m_aY = m_aY == (JUMP_AY << dConfig.FRACTION_BITS) ? (JUMP_AY << dConfig.FRACTION_BITS)
					: m_aY + (1 << dConfig.FRACTION_BITS);
		}
		if (m_vY <= 0 && m_timer < JUMP_DUR * Player.getTimerStep()) {
			if ((m_actionID == action_map[ST_ACTOR_JUMP][JUMP_UP_INDEX])) {
				if (CKey.isKeyHold(CKey.GK_UP)) {
					m_vY -= (1 << dConfig.FRACTION_BITS);
				} else {
					m_vY += m_aY >> 1;
				}
			} else if ((m_actionID == action_map[ST_ACTOR_JUMP][INCLINED_UP_INDEX])) {
				if (CKey.isKeyHold(getJumpKey(true))
						|| (CKey.isKeyHold(getDirectionKey(true)) && CKey
								.isKeyHold(CKey.GK_UP))) {
					m_vY -= (1 << dConfig.FRACTION_BITS);
				} else {
					m_vY += m_aY >> 1;
				}
			}
		}

	}

	private static boolean isPressTwoJump;

	private void orderToTwoJump() {
		if (CKey.isKeyPressed(CKey.GK_UP) && canJumpTwice() && !isPressTwoJump) {
			isPressTwoJump = true;
			setState(ST_ACTOR_JUMP, -1, false);
			setAnimAction(action_map[ST_ACTOR_JUMP][TWO_UP_INDEX]);
			// m_vY = m_vY*2/3;
			return;
		} else if (!isPressTwoJump) {
			if (isInAir) {
				if (CKey.isKeyPressed(getJumpKey(true)) && canJumpTwice()) {
					isPressTwoJump = true;
					setState(ST_ACTOR_JUMP, -1, false);
					setAnimAction(action_map[ST_ACTOR_JUMP][TWO_INCLINED_INDEX]);
				} else if (CKey.isKeyPressed(getJumpKey(false))
						&& canJumpTwice()) {
					isPressTwoJump = true;
					setState(ST_ACTOR_JUMP, -1, false);
					setAnimAction(action_map[ST_ACTOR_JUMP][TWO_INCLINED_INDEX]);
					turnAround();
				}
			}
		}
	}

	/**
	 * �ܷ������
	 * 
	 * @return boolean
	 */
	boolean canJumpTwice() {
		// �������
		return m_actorProperty[PRO_INDEX_ROLE_ID] == 0;
	}

	/**
	 * ײ
	 * 
	 * @return boolean
	 */
	boolean canBump() {
		// ���ӿ���
		return m_actorProperty[PRO_INDEX_ROLE_ID] == 1;
	}

	private boolean orderToAirAttack() {
		int actionAirAttack = action_map[ST_ACTOR_ATTACK][ATT_ACT_INDEX_AIR];
		int actionAirAttack1 = action_map[ST_ACTOR_ATTACK][ATT_ACT_INDEX_AIR_1];
		int actionAirAttack2 = action_map[ST_ACTOR_ATTACK][ATT_ACT_INDEX_AIR_2];
		if (m_actionID != actionAirAttack && m_actionID != actionAirAttack1
				&& m_actionID != actionAirAttack2
				&& CKey.isKeyPressed(CKey.GK_MIDDLE)) {
			clearFlag(dActor.FLAG_ACTION_GRAB_MECH_INFO);
			Goods heroWeapon = (Goods) hsEquipList
					.get(m_actorProperty[PRO_INDEX_WEAPON] + "");
			if (heroWeapon != null
					&& (heroWeapon.getName().equals("������")
							|| heroWeapon.getName().equals("���๭") || heroWeapon
							.getName().equals("˫�ع�"))) {
				setAnimAction(actionAirAttack1);
			} else {
				setAnimAction(actionAirAttack);
			}

			// if(m_actorProperty[PRO_CUR_WP] == 0)
			// setAnimAction (actionAirAttack);
			// else if(m_actorProperty[PRO_CUR_WP] == 1)
			// setAnimAction (actionAirAttack1);
			// else if(m_actorProperty[PRO_CUR_WP] == 2)
			// setAnimAction (actionAirAttack2);
			return true;
		}
		return false;
	}

	private void changeVxByKey() {
		int airAttack = action_map[ST_ACTOR_ATTACK][ATT_ACT_INDEX_AIR];
		// ����ת��
		if (CKey.isKeyPressed(getDirectionKey(false))) {
			if (m_actionID != airAttack) {
				turnAround();
			}
		}

		// ���������ƶ�
		if (CKey.isKeyPressed(getDirectionKey(true))
				|| CKey.isKeyHold(getDirectionKey(true))) {
			m_vX = getFaceDir() * JUMP_V_X;
		}
	}

	public int jumpTopY; // �������ߵ�
	public static byte JUMP_H_LIMITE = 7;// ����7����������land2

	private void do_Jump() {
		int actionJumpUp = action_map[ST_ACTOR_JUMP][JUMP_UP_INDEX];
		int actionJumpDown = action_map[ST_ACTOR_JUMP][JUMP_DOWN_INDEX];
		int actionAirAttack = action_map[ST_ACTOR_ATTACK][ATT_ACT_INDEX_AIR];

		int inclinedJump = action_map[ST_ACTOR_JUMP][INCLINED_UP_INDEX];
		int twoJump = action_map[ST_ACTOR_JUMP][TWO_UP_INDEX];
		int inclinedDown = action_map[ST_ACTOR_JUMP][INCLINED_DOWN_INDEX];
		int twoinClineUp = action_map[ST_ACTOR_JUMP][TWO_INCLINED_INDEX];
		int jumpHurt = action_map[ST_ACTOR_JUMP][JUMP_HURT_INDEX];

		int topUp = action_map[ST_ACTOR_JUMP][TOP_UP_INDEX];
		int topInclined = action_map[ST_ACTOR_JUMP][TOP_INCLINE_INDEX];

		// һЩ�ڿ��еĶ�������
		if (handleInAir()) {
			return;
		}

		if (m_actionID == actionJumpUp) {
			if (m_actionID != action_map[ST_ACTOR_JUMP][JUMP_PRE_INDEX]) {
				isInAir = true;
			}
			if (m_vY >= 0) {
				clearFlag(dActor.FLAG_ACTION_GRAB_MECH_INFO);
				setAnimAction(actionJumpDown);

			}
		} else if (m_actionID == inclinedJump || m_actionID == twoJump
				|| m_actionID == twoinClineUp) {
			if (m_actionID != action_map[ST_ACTOR_JUMP][JUMP_PRE_INDEX]) {
				isInAir = true;
			}
			if (m_vY >= 0) {
				// if(m_actionID == twoJump) {
				// setAnimAction (topUp);
				// }
				// else if(m_actionID == twoinClineUp) {
				// setAnimAction (topInclined);
				// }
				// else if(m_actionID == inclinedJump) {
				clearFlag(dActor.FLAG_ACTION_GRAB_MECH_INFO);
				aniPlayer.clearAniPlayFlag(Player.FLAG_ACTION_NOT_CYCLE);
				setAnimAction(inclinedDown);
				// }
			}
		}
		// else if(m_actionID == topUp || m_actionID == topInclined) {
		// aniPlayer.clearAniPlayFlag (aniPlayer.FLAG_ACTION_NOT_CYCLE);
		// if(m_actionID == topUp) {
		// setAnimAction (actionJumpDown);
		// }
		// else if(m_actionID == topInclined) {
		// setAnimAction (inclinedDown);
		// }
		// }
		else if (m_actionID == actionAirAttack
				|| m_actionID == actionAirAttack + 1
				|| m_actionID == actionAirAttack + 2) {
			if (m_actorProperty[PRO_INDEX_ROLE_ID] == 0
					&& m_actionID == actionAirAttack + 1) {
				shot();
			} else {
				attackEnemy();
			}
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				clearFlag(dActor.FLAG_ACTION_GRAB_MECH_INFO);
				setAnimAction(actionJumpDown);
			}
		} else if (m_actionID == jumpHurt) {
			// modify by lin 09.4.28
			// if(aniPlayer.testAniPlayFlag (aniPlayer.FLAG_ACTION_OVER)) {
			// orderToFall();
			// }
			checkBottom();
		}

	}

	private boolean checkBottom() {
		if (m_bBlockedBottom) {
			isPressTwoJump = false;
			isInAir = false;
			setState(ST_ACTOR_LAND, -1, true);
			return true;

		}
		return false;
	}

	// �ڿ��еĴ���
	private boolean handleInAir() {
		if (isInAir) {
			if (checkBottom()) {
				return true;
			}

			if (!orderToAirAttack()) {
				orderToTwoJump();
				changeVxByKey();
				changeJumpHByKey();
			}
			// ���ͷ�������赲�� y�ٶ���0
			if (m_vY <= 0 && m_bBlockedTop) {
				m_vY = 0;
			}
		}
		return false;
	}

	/*************
	 * LAND ״̬
	 ************/

	private void do_Land() {
		if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			orderToTurn();
			if (!orderToRun()) {
				setState(ST_ACTOR_WAIT, -1, true);
			}
		}
	}

	/****************
	 * FLY״̬
	 ****************/
	private static int ax;
	static int ay;

	public static final int CURSOR_SPEED = 2 << dConfig.FRACTION_BITS; // base
																		// speed
	public static final int CURSOR_ACCELERATION = 1 << dConfig.FRACTION_BITS; // acceleration
	public static final int CURSOR_ACCELERATION_MAX = 9 << dConfig.FRACTION_BITS; // acceleration

	private static boolean isFlyAttack;

	private void do_Fly() {
		// int fly = action_map[ST_ACTOR_FLY][FLY_INDEX];
		// int flying = action_map[ST_ACTOR_FLY][FLYING_INDEX];
		//
		// if(m_actionID == fly) {
		// if(aniPlayer.testAniPlayFlag (aniPlayer.FLAG_ACTION_OVER)) {
		// setAnimAction (flying);
		// }
		//
		// }
		// else if(m_actionID == flying) {
		//
		// if(CKey.isKeyPressed (getDirectionKey (false))) {
		// turnAround ();
		// }
		// boolean isLeft = CKey.isKeyHold (CKey.GK_LEFT);
		// boolean isRight = CKey.isKeyHold (CKey.GK_RIGHT);
		// boolean isUp = CKey.isKeyHold (CKey.GK_UP);
		// boolean isDown = CKey.isKeyHold (CKey.GK_DOWN);
		//
		// int vx = 0, vy = 0;
		// if(isLeft) {
		// vx = -CURSOR_SPEED;
		// if(ax > -CURSOR_ACCELERATION_MAX) {
		// ax -= CURSOR_ACCELERATION;
		// }
		// }
		// if(isRight) {
		// vx = CURSOR_SPEED;
		// if(ax < CURSOR_ACCELERATION_MAX) {
		// ax += CURSOR_ACCELERATION;
		// }
		// }
		// if(isUp) {
		// vy = -CURSOR_SPEED;
		// if(ay > -CURSOR_ACCELERATION_MAX) {
		// ay -= CURSOR_ACCELERATION;
		// }
		// }
		// if(isDown) {
		// vy = CURSOR_SPEED;
		// if(ay < CURSOR_ACCELERATION_MAX) {
		// ay += CURSOR_ACCELERATION;
		// }
		// }
		// m_vX = vx + ax;
		// m_vY = vy + ay;
		//
		// if(m_vY >= 8 << dConfig.FRACTION_BITS) {
		// m_vY = 8 << dConfig.FRACTION_BITS;
		//
		// }
		// else if(m_vY <= -8 << dConfig.FRACTION_BITS) {
		// m_vY = -8 << dConfig.FRACTION_BITS;
		// }
		//
		// if(m_vX == (8 << dConfig.FRACTION_BITS) * getFaceDir ()) {
		// m_vX = (8 << dConfig.FRACTION_BITS) * getFaceDir ();
		// }
		//
		// if(!CKey.isKeyHold (getDirectionKey (true)) && !CKey.isKeyPressed
		// (getDirectionKey (true))) {
		// m_vX = 0;
		// }
		//
		// if( (!CKey.isKeyHold (CKey.GK_UP) && !CKey.isKeyPressed (CKey.GK_UP))
		// && (!CKey.isKeyHold (CKey.GK_DOWN) && !CKey.isKeyPressed
		// (CKey.GK_DOWN))) {
		// m_vY = 0;
		// }
		//
		// if(CKey.isKeyPressed (CKey.GK_NUM0)) {
		// if(!orderToFall ()) {
		// setState (ST_ACTOR_WAIT, -1, true);
		// }
		// }
		// }
	}

	/********************
	 *Jink����״̬
	 ********************/

	private void do_Jink() {
		if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			setState(ST_ACTOR_WAIT, -1, true);
		}
	}

	/*************
	 * ����Ʒ
	 *************/

	// private void do_PushPull () {
	// switch(m_actionID) {
	// case dActionID.Action_ID_spy_pushReady:
	// if(CKey.isKeyHold (CKey.GK_LEFT) || CKey.isKeyHold (CKey.GK_RIGHT)) {
	// boolean left = CKey.isKeyHold (CKey.GK_LEFT);
	// if(left && m_x > m_relativeMisc.m_x || !left && m_x < m_relativeMisc.m_x)
	// { //�ж����������ӵ���һ��
	// setAnimAction (dActionID.Action_ID_spy_push);
	// }
	// else {
	// if(!m_bBlockedBack) {
	// setAnimAction (dActionID.Action_ID_spy_pull);
	// }
	// else {
	// return;
	// }
	// }
	// followBox ();
	// }
	//
	// break;
	//
	// case dActionID.Action_ID_spy_pullReady:
	// if(CKey.isKeyHold (CKey.GK_LEFT) || CKey.isKeyHold (CKey.GK_RIGHT)) {
	// boolean left = CKey.isKeyHold (CKey.GK_LEFT);
	// if(left && m_x > m_relativeMisc.m_x || !left && m_x < m_relativeMisc.m_x)
	// { //�ж����������ӵ���һ��
	// // �ƵĶ���״̬
	// setAnimAction (dActionID.Action_ID_spy_push);
	// }
	// else {
	// if(!m_bBlockedBack) {
	// setAnimAction (dActionID.Action_ID_spy_pull);
	// }
	// else {
	// return;
	// }
	// }
	// followBox ();
	// }
	// else if(CKey.isKeyPressed (CKey.GK_MIDDLE) || CKey.isKeyPressed
	// (CKey.GK_DOWN)) {
	// m_relativeMisc.m_x -= m_relativeMisc.m_vX;
	// m_relativeMisc.m_vX = 0;
	// setState (ST_ACTOR_WAIT, -1, true);
	// getActorBoxInfo (dActor.BOX_COLLIDE, s_colBox1);
	// phyStaticDetect (null, s_colBox1);
	// }
	// break;
	//
	// case dActionID.Action_ID_spy_push:
	// followBox ();
	// if(m_relativeMisc != null) {
	// if(m_vX == 0
	// || (m_actionID == dActionID.Action_ID_spy_push
	// && (getFaceDir () == -1 && !CKey.isKeyHold (CKey.GK_LEFT)
	// || getFaceDir () == 1 && !CKey.isKeyHold (CKey.GK_RIGHT)))) {
	// setState (this.ST_ACTOR_WAIT, -1, true);
	// getActorBoxInfo (dActor.BOX_COLLIDE, s_colBox1);
	// phyStaticDetect (null, s_colBox1);
	// m_vX = 0;
	// }
	// }
	// break;
	//
	// case dActionID.Action_ID_spy_pull:
	// if( (m_actionID == dActionID.Action_ID_spy_pull
	// && (getFaceDir () == 1 && !CKey.isKeyHold (CKey.GK_LEFT) || getFaceDir ()
	// == -1 && !CKey.isKeyHold (CKey.GK_RIGHT)
	// || m_bBlockedBack))) {
	// setAnimAction (dActionID.Action_ID_spy_pullReady);
	// getActorBoxInfo (dActor.BOX_COLLIDE, s_colBox1);
	// m_relativeMisc.m_x -= m_relativeMisc.m_vX;
	// m_relativeMisc.m_vX = 0;
	// if(phyStaticDetect (null, s_colBox1)) {
	// // m_relativeMisc.m_x =
	// }
	//
	// }
	//
	// break;
	// }
	// }

	// *******************************************************************************************
	// �������
	// ******************************************************************************************
	int m_tickCombo; // ���� ��ϰ���
	// ����������
	public static final int[][] COMBO_KEY_MAP = {
			{ CKey.GK_MIDDLE, CKey.GK_MIDDLE, CKey.GK_MIDDLE },
			{ CKey.GK_MIDDLE, CKey.GK_MIDDLE, CKey.GK_MIDDLE },
			{ CKey.GK_MIDDLE, CKey.GK_MIDDLE, CKey.GK_MIDDLE } };
	// ����������
	public static final int[][] COMBO_ACTION_MAP = {
			{ ATT_ACTION_1, ATT_ACTION_2, ATT_ACTION_3 },
			{ ATT_ACTION_4, ATT_ACTION_5, ATT_ACTION_6 },
			{ ATT_ACTION_7, ATT_ACTION_8, ATT_ACTION_9 }, };

	// ��������
	public static final int COMBO_ACTION_NUM = COMBO_KEY_MAP.length;
	// ��������
	public static final int ACTION_INDEX_0 = 1 << 0;
	public static final int ACTION_INDEX_1 = 1 << 1;
	public static final int ACTION_INDEX_2 = 1 << 2;

	// �����������flags
	static int checkActionFlags;
	// ��������
	static byte checkKeyIndex;
	// �����ɹ�������
	static int comboSuccessAction;

	// ��Ч���������֡���
	public static final int MAX_COMBO_INTERVAL = 12;

	/**
	 * ��ʼ��¼��������
	 * 
	 * @param flags
	 *            int ������flag
	 */
	public void startRecordCombo(int actionflags) {
		m_tickCombo = MAX_COMBO_INTERVAL;
		comboSuccessAction = -1;
		checkActionFlags = 0;
		// ȷ����Ҫ���Ķ���
		if (actionflags == -1) {
			checkActionFlags = 0xffffffff;
		} else {
			checkActionFlags |= actionflags;
		}
		// �ӱ��еĵ�0����ʼ���
		checkKeyIndex = 0;
	}

	/**
	 * �����������
	 */
	void checkComboKey() {
		// ����ʱЧ����
		if (m_tickCombo <= 0) {
			return;
		}
		m_tickCombo--;

		// ɨ�谴���������������
		for (int i = 0; i < COMBO_ACTION_NUM; i++) {
			if ((checkActionFlags & (1 << i)) != 0) {
				if (CKey.isKeyPressed(COMBO_KEY_MAP[i][checkKeyIndex])) {
					comboSuccessAction = i;
					// System.out.println ("index = " + i);
					for (int j = 0; j < COMBO_ACTION_NUM; j++) {
						if (i == j) {
							continue;
						}
						// �������ʧЧ������
						if ((checkActionFlags & (1 << j)) != 0) {
							if (!CKey
									.isKeyPressed(COMBO_KEY_MAP[j][checkKeyIndex])
									|| checkKeyIndex >= COMBO_KEY_MAP[j].length - 1) {
								checkActionFlags &= ~(1 << j);
							}
						}
					}

					// ���ټ���������
					m_tickCombo = -1;
					return;
				}
			}
		}

	}

	/**
	 * �л���������
	 * 
	 * @return boolean
	 */
	boolean setComboAction() {
		if (comboSuccessAction < 0) {
			return false;
		}

		// �Ƿ���е���
		if (testFlag(dHero.FLAG_ATTACK_SUCCESS) || checkKeyIndex == 0) { // ��һ������,����Ҫ���е���Ҳ�ܷ���

			// �л�����
			setAnimAction(COMBO_ACTION_MAP[comboSuccessAction][checkKeyIndex]);
			// ���¿�ʼ��ⰴ��
			m_tickCombo = MAX_COMBO_INTERVAL;
			// �����ɹ�����������++
			checkKeyIndex++;

			// һ����������
			if (checkKeyIndex >= COMBO_ACTION_MAP[comboSuccessAction].length) {
				checkActionFlags &= ~(1 << comboSuccessAction);
			}

			comboSuccessAction = -1;
			return true;
		}
		return false;
	}

	private void attackEnemy() {
		getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
		// û�й�������
		if (s_colBox1[0] == s_colBox1[2] || s_colBox1[1] == s_colBox1[3]) {
			return;
		}

		// �ж��ǲ��ǹؼ�֡
		if (!isAttackKeyFrame()) {
			return;
		}

		/**
		 * ȡ���˺�ֵ
		 */
		CObject enemy = null;
		int shellId = CGame.activeActorsListHead;
		while (shellId != -1) {
			enemy = CGame.m_actorShells[shellId];
			// ��ǽ������ײ��
			if (ResLoader.classAIIDs[enemy.m_classID] == dActorClass.CLASS_ID_OBJ_DOOR) {
				if ((enemy.m_actionID == dActionID.Action_ID_door_common)
						&& isAttackedObj(enemy)
						&& m_actorProperty[PRO_INDEX_ROLE_ID] == 1
						&& m_actionID == action_map[ST_ACTOR_SPRINT][SPRINT_INDEX]) {
					enemy.setFlag(dActor.FLAG_BEATTACKED);
				}
			}
			// ����
			else if (ResLoader.classAIIDs[enemy.m_classID] == dActorClass.CLASS_ID_OBJ_CANATTACKBOX) {
				if ((enemy.m_actionID == dActionID.Action_ID_box_1)
						&& isAttackedObj(enemy)) {
					enemy.setFlag(dActor.FLAG_BEATTACKED);
				}
			}

			if ((ResLoader.classesFlags[enemy.m_classID] & dActor.CLASS_FLAG_IS_ENEMY) != 0
					&& enemy.m_actorProperty[PRO_INDEX_HP] > 0
					&& enemy.m_invincible <= 0
					&& enemy.testFlag(dActor.FLAG_BASIC_VISIBLE)) {
				if (isAttackedObj(enemy)) {
					/**
					 * ����ؼ�֡�򵽵��� ���� flag FLAG_DO_KEYFRAME_LOGIC
					 * 
					 * @param <any> FLAG_DO_KEYFRAME_LOGIC
					 */
					setFlag(dHero.FLAG_ATTACK_SUCCESS);
					// ��������ɹ������Լ��ؼ�֡�߼�
					this.setKFDur();

					// ���ù���֡�Ա������ߵ�Ӱ��
					enemy.setFlag(dActor.FLAG_DO_KEYFRAME_LOGIC);
					// ���ӿڳ�����
					if (m_actorProperty[PRO_INDEX_ROLE_ID] == 1
							&& m_actionID == action_map[ST_ACTOR_JINK][JINK_INDEX]) {
						aaAttack(enemy, AA_DU);
					}
					boolean isCri = enemy.setKeyFrame(this);
					// ������Ч
					enemy.getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
					CObject effect = null;
					if (getActorInfo(dActorClass.Index_Param_HERO_EFFECT) >= 0) {
						effect = allocActor(
								getActorInfo(dActorClass.Index_Param_HERO_EFFECT),
								(s_colBox1[0] + s_colBox1[2]) << 7,
								(s_colBox1[3] + s_colBox1[1]) << 7, this
										.getFaceDir() == -1);
					}

					if (effect != null) {
						effect.m_z = 100;
						if (!isCri)
							effect
									.setAnimAction(CTools
											.random(
													dActionID.Action_ID_attack_Effect_attack_Effect1,
													dActionID.Action_ID_attack_Effect_attack_Effect7));
						else
							effect.setAnimAction(CTools.random(
									dActionID.Action_ID_attack_Effect_baoji2,
									dActionID.Action_ID_attack_Effect_baoji3));
					}
					// aaAttack(enemy);
				}
			}
			shellId = CGame.nextActorShellID[shellId];
		}
	}

	public void doCollionWithActor() {
		int shellId = CGame.activeActorsListHead;
		while (shellId != -1) {
			CObject enemy = CGame.m_actorShells[shellId];
			// ������赲����
			if (enemy.testClasssFlag(dActor.CLASS_FLAG_BLOCKABLE)) {
				if (m_phyEvx != 0) {
					m_vX += m_phyEvx;
				}
				if (enemy != this && checkCollionWithActor(enemy)) {
					checkRelativeMisc();
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

	public static final int MSG_BOX_NEAR = 1; // �Ƿ񿿽�����
	public static final int MSG_BOX_AWAY = 2; // �Ƿ��뿪����

	// public void postMessage (int msg, CObject sender) {
	// switch(msg) {
	//
	// case MSG_BOX_NEAR:
	// if( (m_relativeMisc == null || !m_relativeMisc.equals (sender))
	// && m_actionID != dActionID.Action_ID_spy_pushReady) {
	// m_relativeMisc = sender;
	// }
	//
	//
	// /**
	// * ��������Զ���
	// */
	// if(sender != null && isFaceTo (sender)) { //�Ƿ�����ö���
	// if( (m_currentState == ST_ACTOR_WAIT || m_currentState == ST_ACTOR_RUN)
	// && sender.testClasssFlag (dActor.CLASS_FLAG_PUSHABLE)) {
	// if(m_bBlockedBottom && !m_bBlockedFront && !m_bBlockedBack) {
	// //y��������һ������, ǰ�����赲, �󷽷��赲
	// //ת���������״̬
	// setState (ST_ACTOR_PUSH);
	// setAnimAction (dActionID.Action_ID_spy_pushReady);
	// // setAnimAction
	// (dHero.m_stateMap[dHero.STATE_PULL_PUSH][dHero.ID_READY_PUSH]);
	//
	// followBox ();
	// }
	// }
	// else if(m_currentState == ST_ACTOR_WAIT && sender.testClasssFlag
	// (dActor.CLASS_FLAG_PUSHABLE)) {
	// if(CKey.isKeyPressed (CKey.GK_MIDDLE) && m_bBlockedBottom) {
	// setState (ST_ACTOR_PUSH);
	// setAnimAction (dActionID.Action_ID_spy_pullReady);
	// // setAnimAction
	// (dHero.m_stateMap[dHero.STATE_PULL_PUSH][dHero.ID_READY_PULL]);
	// followBox ();
	// CKey.initKey ();
	// }
	//
	// }
	// }
	// break;
	//
	// case MSG_BOX_AWAY:
	//
	// //�й������� ���� ��������
	// if(m_relativeMisc != null && m_relativeMisc.equals (sender)
	// /*&& CKey.isKeyPressed(CKey.GK_MIDDLE)*/) {
	// m_relativeMisc.m_vX = 0;
	// m_relativeMisc = null;
	// if(m_currentState == ST_ACTOR_PUSH) {
	// //ת���������״̬
	// setState (ST_ACTOR_WAIT, -1, true);
	// m_colWithActorFlag = 0;
	// }
	// }
	// break;
	// }
	// }

	// public void followBox () {
	// if(m_relativeMisc == null) {
	// CDebug._assert (false, "m_relativeMisc is null(in followBox())");
	// return;
	// }
	// //���������帳���ٶ�
	// m_relativeMisc.m_vX = m_vX;
	// //�������ǵ�����
	// m_relativeMisc.getActorOppBoxInfo (dActor.BOX_COLLIDE, s_colBox1);
	// getActorOppBoxInfo (dActor.BOX_COLLIDE, s_colBox2);
	// if(testFlag (dActor.FLAG_BASIC_FLIP_X)) {
	// if(!m_bBlockedFront || m_actionID == dActionID.Action_ID_spy_pushReady
	// || m_actionID == dActionID.Action_ID_spy_pullReady) {
	// m_x = m_relativeMisc.m_x + ( (m_relativeMisc.s_colBox1[RIGHT] -
	// s_colBox2[LEFT]) << dConfig.FRACTION_BITS);
	// if(m_vX != 0) {
	// m_x -= m_vX;
	// }
	// return;
	// }
	// }
	// else {
	// if(!m_bBlockedFront || m_actionID == dActionID.Action_ID_spy_pushReady
	// || m_actionID == dActionID.Action_ID_spy_pullReady) {
	// m_x = m_relativeMisc.m_x + ( (m_relativeMisc.s_colBox1[LEFT] -
	// s_colBox2[RIGHT]) << dConfig.FRACTION_BITS);
	// if(m_vX != 0) {
	// m_x -= m_vX;
	// }
	// return;
	// }
	// }
	// postMessage (MSG_BOX_AWAY, m_relativeMisc);
	// }

	private boolean debugMode() {
		if (!CDebug.bForTest) {
			return false;
		}

		if (true) {
			return false;
		}

		// ----------------------------------------------------------- ����
		if (CKey.isKeyPressed(CKey.GK_POUND) && CDebug.bForTest) {

			// CGame.loadType = CGame.LOAD_TYPE_COMMON_LOAD2;
			// CGame.setLoadInfo ((CGame.curLevelID+1)%16);
			// levelUp(m_actorProperty[PRO_INDEX_LEVEL]+5);
			// addAEquip(Goods.createGoods((short)0,(short)6));
			// m_actorProperty[PRO_INDEX_HP]+=1000;
			// m_actorProperty[PRO_INDEX_ATT]+=300;
			// m_actorProperty[PRO_INDEX_SP]+=5000;
			// addAItem(Goods.createGoods( (short) 1, (short) 0));
			// addAItem(Goods.createGoods( (short) 1, (short) 12));
			// m_actorProperty[PRO_INDEX_SP] = 10000;
			// for (int i = 0; i < 30; i++) {
			// addAEquip(Goods.createGoods( (short) 0, (short) i));
			// }
			// for (int i = 0; i < 18; i++) {
			// addAItem(Goods.createGoods( (short) 1, (short) i));
			// }
			// for (int i = 0; i < 100; i++) {
			// addAItem(Goods.createGoods( (short) 1, (short) 12));
			// }

		}

		// -----------------------------------------------------------

		if (CKey.isKeyPressed(CKey.GK_STAR)) {
			// if(m_currentState == ST_ACTOR_CHEAT) {
			// getActorBoxInfo (dActor.BOX_COLLIDE, s_colBox1);
			// getSurroundingEnvInfo (s_colBox1, getFaceDir (), 0);
			// aniPlayer.clearAniPlayFlag (aniPlayer.FLAG_ACTION_HOLD |
			// aniPlayer.FLAG_ACTION_REVERSE);
			// setState (this.ST_ACTOR_WAIT);
			// }
			// else {
			// m_currentState = (ST_ACTOR_CHEAT);
			// }
		}

		if (m_currentState == ST_ACTOR_CHEAT) {
			if (CKey.isKeyHold(CKey.GK_RIGHT)) {
				m_x += 16 << 8;
			} else if (CKey.isKeyHold(CKey.GK_LEFT)) {
				m_x -= 16 << 8;
			} else if (CKey.isKeyHold(CKey.GK_UP)) {
				m_y -= 16 << 8;
			} else if (CKey.isKeyHold(CKey.GK_DOWN)) {
				m_y += 16 << 8;
			}
			updateCamera();
			return true;
		}
		return false;

	}

	public static boolean s_heroDie; // add by lin 09.04.24

	/************
	 * �߼�����
	 **************/
	public boolean update() {

		s_heroDie = false;
		MapDraw.bShowMap = true;
		CEnemy.bAllEnemyStop = false;
		if (debugMode()) {
			return true;
		}
		if (super.update()) {
			// ������ͼ�� add by lin 09.04.24
			if (m_bInDeadZone) {
				s_heroDie = true;
				return false;
			}

			// ��������
			if (checkLevelup()) {
				// setWeaponPlayer(dActionID.Action_ID_levelup_levelup,
				// dActorClass.Animation_ID_EFFECT_LEVELUP);
				levelUp(m_actorProperty[PRO_INDEX_LEVEL] + 1);
			}
			checkHurt();
			ai_Hero();
			if (CGame.m_curState == dGame.GST_MAIN_MENU
					|| CGame.m_curState == dGame.GST_GAME_HERO_DIE) {
				return false;
			}

			doPhysics();
			if (Camera.isLockInArea) {
				lockActorInArea(Camera.cameraBox);
			}

		}
		checkTask();

		return true;
	}

	/**
	 * ����
	 * 
	 * @param g
	 *            Graphics
	 */
	public void paint(Graphics g) {
		if (aniPlayer == null) {
			// CDebug._debugInfo ("aniPlaye Ϊnull");
			return;
		}

		showBonusInfo(g, (m_x >> dConfig.FRACTION_BITS) - Camera.cameraLeft,
				(m_y >> dConfig.FRACTION_BITS) - Camera.cameraTop);
		showFaceInfo(g);

		if (m_invincible <= 0 || m_invincible % 2 == 0) {
			// if (cGame.m_curState==CGame.GST_GAME_RUN) {
			// drawShadow(g);
			// }
			// else{
			// clearShadow();
			// }
			showUpLevelAni(g);
			// ���»���λ��
			aniPlayer.setSpritePos(m_x >> dConfig.FRACTION_BITS,
					m_y >> dConfig.FRACTION_BITS);
			// #if size_500k
			aniPlayer.drawFrame(g, suitInfo);
			// #else
			// # aniPlayer.drawFrame (g, null);
			// #endif
			drawChangeWeapon(g);
			showSkillFlash(g);
		}
		paintDebugBox(g);
	}

	void showUpLevelAni(Graphics g) {
		if (bshowUpLevelAni) {
			if (upLevelAni == null || upLevelAni.aniData == null) {
				upLevelAni = new AniPlayer(
						ResLoader.animations[dActorClass.Animation_ID_ATTACK_EFFECT],
						0, 0, dActionID.Action_ID_attack_Effect_shenji);
			}
			getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
			upLevelAni.setSpritePos(m_x >> dConfig.FRACTION_BITS, s_colBox1[1]);
			// ʼ�ղ���ת
			upLevelAni.setSpriteFlipX(1);
			upLevelAni.drawFrame(g, null);
			upLevelAni.updateAnimation();
			if (upLevelAni.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				upLevelAni.clearAniPlayFlag(Player.FLAG_ACTION_OVER);
				bshowUpLevelAni = false;
			}
		}

	}

	AniPlayer skillFlashAni;
	boolean bShowSkillFlash;

	/**
	 * �ż����ǵľ۹���Ч
	 */
	void showSkillFlash(Graphics g) {
		if (bShowSkillFlash) {
			CEnemy.bAllEnemyStop = true;
			if (skillFlashAni == null || skillFlashAni.aniData == null) {
				skillFlashAni = new AniPlayer(
						ResLoader.animations[dActorClass.Animation_ID_ATTACK_EFFECT],
						0, 0, dActionID.Action_ID_attack_Effect_flash1);
			}
			// ���λ��
			getActorBoxInfo(dActor.BOX_ATTACK, s_colBox1);
			skillFlashAni.setSpritePos(s_colBox1[0], s_colBox1[1]);
			// ʼ�ղ���ת
			skillFlashAni.setSpriteFlipX(1);
			skillFlashAni.drawFrame(g, null);
			skillFlashAni.updateAnimation();
			if (skillFlashAni.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				skillFlashAni.clearAniPlayFlag(Player.FLAG_ACTION_OVER);
				bShowSkillFlash = false;
				// CEnemy.bAllEnemyStop=false;
			}
		}
	}

	// public byte[][] getSaveInfo () {
	// return new byte[][] {SAVED_INFO_DEFAULT, {}
	// };
	// }
	//

	/**
	 * ����վ���ķ���
	 */
	private void tryStand() {
		/**
		 * for bug ����λ�����겻��ȷ zeng kai
		 */
		// if(m_currentState == dHero.STATE_CLIMB) {
		// setActionWithCollision
		// (dHero.m_stateMap[dHero.STATE_WAIT][dHero.ID_WAIT_STAND],
		// dActor.INDEX_COLLISION_BOX_FRONT, -1);
		// }
		setAnimAction(action_map[ST_ACTOR_WAIT][WAIT_ACT_INDEX]);
		getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);
		getSurroundingEnvInfo(s_colBox1, getFaceDir(), 0);
		/**
		 * ����Ҫ�޸� �����г�ǹ�����
		 */
		if (m_bBlockedTop) {
			setState(ST_ACTOR_SQUAT, -1, true);
		}

		else {
			setState(ST_ACTOR_WAIT, -1, true);
		}
	}

	/**
	 * ��Ǯ
	 * 
	 * @param num
	 *            int.
	 * @param showInf
	 *            boolean.�Ƿ���ʾ��Ϣ
	 * @return boolean
	 */
	public boolean addMoney(int num) {
		int buf = m_actorProperty[PRO_INDEX_MONEY];
		if (num + buf > 30000) {
			m_actorProperty[PRO_INDEX_MONEY] = 30000;
		} else if (num + buf < 0) {
			m_actorProperty[PRO_INDEX_MONEY] = 0;
		} else {
			m_actorProperty[PRO_INDEX_MONEY] += num;
		}
		return true;
	}

	/***************************************
	 * ��Ʒ������
	 **************************************/
	public static final int GOODS_NUM = 100;

	public Hashtable hsEquipList = new Hashtable(); // װ�� List
	public Hashtable hsItemList = new Hashtable(); // ��ƷList
	// public Hashtable hsSkillList = new Hashtable (); //����list

	/**
	 * �����Ʒ����С
	 * 
	 * @return int
	 */
	// public int getBucketSize ()
	// {
	// int eqNum = getEquipCount (); //װ������
	// int itNum = getItemCount (); //��������
	//
	// return eqNum + itNum;
	// }

	/***************************************
	 * װ������
	 **************************************/

	public static int equipAddIndex; // װ�����뱳����˳����Ϊkey

	/**
	 * ��һ��װ������Ʒ����
	 * 
	 * @param equip
	 *            Goods
	 * @return boolean
	 */
	public boolean addAEquip(Goods goods) {
		// if ( getBucketSize () > GOODS_NUM )
		// {
		// if ( CDebug.showDebugInfo )
		// {
		// System.out.println ( "error: ��Ʒ�����������������Ʒ" );
		// }
		// return false;
		// }

		// �Ƚ����װ���Ƿ��Ѿ����ˣ����˾ͼ�����
		// for ( Enumeration e = hsEquipList.keys (); e.hasMoreElements (); )
		// {
		// String key = ( String ) e.nextElement ();
		// Goods g = ( Goods ) ( hsEquipList.get ( key ) );
		// if ( Goods.isTheSame ( g , goods ) )
		// {
		// g.property[Goods.PRO_COUNT]++;
		// return true;
		// }
		// }

		// �������ͬ ��װ����ӵ���Ʒ������
		goods.property[Goods.PRO_COUNT]++;
		goods.setKey((equipAddIndex++) % Short.MAX_VALUE);
		hsEquipList.put(String.valueOf(goods.getKey()), goods);
		System.out.println("����װ���� " + goods.getName());
		return true;
	}

	public Goods getOneEquip(int key) {
		for (Enumeration e = hsEquipList.keys(); e.hasMoreElements();) {
			String keyStr = (String) e.nextElement();
			if (Integer.parseInt(keyStr) == key) {
				return (Goods) (hsEquipList.get(keyStr));
			}
			// Goods g = ( Goods ) ( hsEquipList.get ( key ) );
		}
		return null;
	}

	/**
	 * ����keyID���� ��Ʒ
	 * 
	 * @param equipID
	 *            int
	 * @return boolean
	 */
	public boolean dropAEquip(int key, int count) {
		if (key < 0) {
			if (CDebug.showDebugInfo) {
				System.out.println("error: ������Ʒ������");
			}
			return false;
		}

		Goods equip = (Goods) (hsEquipList.get(String.valueOf(key)));

		if (equip != null) {
			int num = equip.property[Goods.PRO_COUNT] - count;
			if (num > 0) {
				equip.property[Goods.PRO_COUNT] = (short) num;
			} else// ��list�����޳�
			{
				hsEquipList.remove(String.valueOf(key));
			}
			return true;
		}
		return false;
	}

	/**
	 * ��װ����Ʒ���ж���һ����Ʒ
	 * 
	 * @param goods
	 *            Goods
	 * @return boolean
	 */
	public boolean dropAEquip(Goods equip, int count) {
		if (equip == null) {
			if (CDebug.showDebugInfo) {
				System.out.println("error: ������Ʒ������,id =  ");
			}
			return false;
		}
		return dropAEquip(equip.getKey(), count);
	}

	public short[] getChangeInfo(Goods item) {
		int proIndex = -1;
		switch (item.getDetailType()) {
		case Data.IDX_EP_weapon:
			proIndex = PRO_INDEX_WEAPON;
			break;
		case Data.IDX_EP_loricae:
			proIndex = PRO_INDEX_LORICAE;
			break;
		case Data.IDX_EP_shoes:
			proIndex = PRO_INDEX_SHOES;
			break;
		case Data.IDX_EP_ring:
			proIndex = PRO_INDEX_RING;
			break;
		case Data.IDX_EP_jade:
			proIndex = PRO_INDEX_JADE;
			break;

		}
		short[] data1 = item.getAffectedPro();
		if (proIndex != -1) {
			Goods equip = (Goods) (hsEquipList.get(String
					.valueOf(m_actorProperty[proIndex])));
			if (equip == null) {
				return data1;
			}
			short[] data2 = equip.getAffectedPro();
			short[] changePro = new short[data1.length];
			for (int i = 0; i < data2.length; i++) {
				changePro[i] = (short) (data1[i] - data2[i]);
			}
			return changePro;
		}
		return data1;
	}

	/**
	 * ��һ��װ��
	 * 
	 * @param key
	 *            int
	 * @return boolean ��false��ʾ�ȼ�����
	 */
	public boolean putOnEquip(int key) {
		Goods equip = (Goods) (hsEquipList.get(String.valueOf(key)));

		// �ȼ��������
		if (m_actorProperty[PRO_INDEX_LEVEL] < equip.getInfo(Data.IDX_EI_level)) {
			if (CDebug.showDebugInfo) {
				System.out.println("putOnEquip()->equipID=" + key
						+ ", heroLevel=" + m_actorProperty[PRO_INDEX_LEVEL]
						+ ", equipLevel=" + equip.getInfo(Data.IDX_EI_level)
						+ ",�ȼ��������޷���");
			}
			return false;
		}

		int proIndex = -1;
		switch (equip.getDetailType()) {
		case Data.IDX_EP_weapon:
			proIndex = PRO_INDEX_WEAPON;
			int id = equip.getDataID();
			if (id == 0) {
				m_actorProperty[PRO_CUR_WP] = 0;
			} else if (id == 1) {
				m_actorProperty[PRO_CUR_WP] = 2;
			} else if (id == 2) {
				m_actorProperty[PRO_CUR_WP] = 1;
			}
			break;
		case Data.IDX_EP_loricae:
			proIndex = PRO_INDEX_LORICAE;
			break;
		case Data.IDX_EP_shoes:
			proIndex = PRO_INDEX_SHOES;
			break;
		case Data.IDX_EP_ring:
			proIndex = PRO_INDEX_RING;
			break;
		case Data.IDX_EP_jade:
			proIndex = PRO_INDEX_JADE;
			break;

		}

		if (m_actorProperty[proIndex] != -1) {
			putOffEquip(m_actorProperty[proIndex]);
		}

		m_actorProperty[proIndex] = (short) key;
		short[] data = equip.getAffectedPro();
		for (int i = 0; i < data.length; i++) {
			if (data[i] != 0) {
				m_actorProperty[i] += data[i];
			}
		}
		adjustHPMP();

		// ----------------------------������װ
		if (proIndex == PRO_INDEX_WEAPON) {
			String s = equip.getDescPredigest();
			if (s != null && s.length() > 0) {
				int i = Integer.parseInt(s);
				// ǧλ����ʾ������ͼ
				int weaponPicId = i / 1000;
				// ��λ����ʾ��Ч��ͼ
				int effectPicId = i - weaponPicId * 1000;
				// ����
				if (m_actorProperty[PRO_INDEX_ROLE_ID] == 0) {
					// ����װ1��ͼ
					if (equip.getName().equals("������")
							|| equip.getName().equals("���๭")) {
						setSuit(4, weaponPicId, 0);
					}// ����װ2��ͼ
					else {
						setSuit(2, weaponPicId, 0);
						setSuit(3, effectPicId, 0);
					}
				}
				// ����
				else {
					setSuit(3, weaponPicId, 0);
					setSuit(4, effectPicId, 0);
				}
			}
		}
		return true;
	}

	/**
	 * װ��һ����Ʒ
	 * 
	 * @param equip
	 *            Goods
	 * @return boolean
	 */
	public boolean putOnEquip(Goods equip) {
		if (equip == null) {
			return false;
		}
		return putOnEquip(equip.getKey());
	}

	/**
	 * �ܷ�ĳ��װ��
	 * 
	 * @return boolean
	 */
	public boolean canEquip(Goods equip) {
		int equipId = equip.property[Goods.PRO_ID];
		// ���ǵ�ְҵ
		int heroClassId = Data.ROLE_INFO[m_actorProperty[PRO_INDEX_ROLE_ID]][Data.IDX_RI_classID];
		for (int i = 0; i < Data.CLASS_EQUIP_USED[heroClassId].length; i++) {
			if (equipId == Data.CLASS_EQUIP_USED[heroClassId][i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ĳ��װ���Ƿ����Ǵ���
	 * 
	 * @return boolean
	 */
	public boolean beenEquiped(Goods g) {
		if (g.getKey() != m_actorProperty[IObject.PRO_INDEX_WEAPON]
				&& g.getKey() != m_actorProperty[IObject.PRO_INDEX_LORICAE]
				&& g.getKey() != m_actorProperty[IObject.PRO_INDEX_JADE]
				// ��һ�����ǵ�����
				&& g.getKey() != Record.savedWeaponId) {
			return false;
		}
		return true;
	}

	/**
	 * ��һ��װ��
	 * 
	 * @param equipID
	 *            int
	 */
	public boolean putOffEquip(int key) {
		Goods equip = (Goods) (hsEquipList.get(String.valueOf(key)));
		if (equip == null) {
			return false;
		}
		int proIndex = -1;
		switch (equip.getDetailType()) {
		case Data.IDX_EP_weapon:
			proIndex = PRO_INDEX_WEAPON;
			break;
		case Data.IDX_EP_loricae:
			proIndex = PRO_INDEX_LORICAE;
			break;
		case Data.IDX_EP_shoes:
			proIndex = PRO_INDEX_SHOES;
			break;
		case Data.IDX_EP_ring:
			proIndex = PRO_INDEX_RING;
			break;
		case Data.IDX_EP_jade:
			proIndex = PRO_INDEX_JADE;
			break;
		}
		m_actorProperty[proIndex] = -1;
		short[] data = equip.getAffectedPro();
		for (int i = 0; i < data.length; i++) {
			if (data[i] != 0) {
				m_actorProperty[i] -= data[i];
			}
		}

		adjustHPMP();

		// �����װ
		if (proIndex == PRO_INDEX_WEAPON) {
			if (m_actorProperty[PRO_INDEX_ROLE_ID] == 0) {
				setSuit(4, 0, 0);
				setSuit(2, 0, 0);
				setSuit(3, 0, 0);
			}
			// ����
			else {
				setSuit(3, 0, 0);
				setSuit(4, 0, 0);
			}
		}
		return true;
	}

	/**
	 * �ѵ�һ����Ʒ
	 * 
	 * @param equip
	 *            Goods
	 */
	public boolean putOffEquip(Goods equip) {
		if (equip == null) {
			return false;
		}
		return putOffEquip(equip.getKey());
	}

	public short getEquipCount() {
		short num = 0;
		for (Enumeration e = hsEquipList.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			Goods g = (Goods) (hsEquipList.get(key));
			int EqCount = g.property[Goods.PRO_COUNT];
			if (EqCount >= 0) {
				num += EqCount;
			}
		}
		return num;
	}

	/**
	 * ���ݱ�ţ�ȡ��ĳ����Ʒ�����������ڽű��е� ��Ʒ���� ���ж�
	 * 
	 * @param goodsId
	 *            int ��id ����ԭ��û������goods������
	 * @return short
	 */
	public int getGoodsNum(int goodsId) {
		int num = 0;
		int type = 0;
		int key = 0;
		Goods g = null;
		// ����Ʒ��
		if (goodsId >= Data.STR_EQUIP_NAMES.length) {
			type = 1;
			goodsId -= Data.STR_EQUIP_NAMES.length;
		}

		key = (short) ((type << 12) | goodsId);
		// װ��,������ͨ�����ƱȽ�
		if (type == 0) {
			Goods equip = Goods.createGoods((short) 0, (short) goodsId);
			if (equip == null) {
				num = 0;
			} else {
				String name = equip.getName();
				Enumeration e = hsEquipList.keys();
				while (e.hasMoreElements()) {
					Goods temp = (Goods) hsEquipList.get(e.nextElement());
					if (temp.getName().equals(name)) {
						num++;
					}
				}
			}
			return num;
		}// ��Ʒ
		else {
			g = (Goods) hsItemList.get(key + "");
		}
		// �����Ʒ����
		if (g == null) {
			num = 0;
		} else {
			num = g.property[Goods.PRO_COUNT];
		}
		return num;
	}

	/**
	 * ȡ�����е����������ڴ������
	 * 
	 * @return Goods ��������������
	 */
	public Goods[] getAllWeapons() {
		Vector temp = new Vector();
		Goods[] allWeapons = null;
		Enumeration e = hsEquipList.keys();
		while (e.hasMoreElements()) {
			Object key = e.nextElement();
			Goods g = (Goods) hsEquipList.get(key);
			if (g.getDetailType() == Data.IDX_EP_weapon) {
				temp.addElement(g);
			}
		}
		if (temp.size() > 0) {
			allWeapons = new Goods[temp.size()];
			for (int i = 0; i < allWeapons.length; i++) {
				allWeapons[i] = (Goods) temp.elementAt(i);
			}
		}
		return allWeapons;
	}

	/**
	 * �������ϴ���װ�����ڴ������
	 * 
	 * @return Goods[]
	 */
	public Goods[] getCurEquips() {
		Vector temp = new Vector();
		Goods[] allWeapons = null;
		Goods equip = (Goods) hsEquipList.get(m_actorProperty[PRO_INDEX_WEAPON]
				+ "");
		if (equip != null) {
			temp.addElement(equip);
		}
		equip = (Goods) hsEquipList
				.get(m_actorProperty[PRO_INDEX_LORICAE] + "");
		if (equip != null) {
			temp.addElement(equip);
		}
		equip = (Goods) hsEquipList.get(m_actorProperty[PRO_INDEX_JADE] + "");
		if (equip != null) {
			temp.addElement(equip);
		}

		if (temp.size() > 0) {
			allWeapons = new Goods[temp.size()];
			for (int i = 0; i < allWeapons.length; i++) {
				allWeapons[i] = (Goods) temp.elementAt(i);
			}
		}
		return allWeapons;

	}

	/**
	 * �������װ���ı�ʯ����
	 * 
	 * @param id
	 *            weaponId
	 * @return int
	 */
	public int getEquipStoneNum(Goods equip) {
		int num = 0;
		int key = 0;
		key = (1 << 12) | equip.getRequiredStoneId();
		Goods stones = (Goods) hsItemList.get(key + "");
		if (stones == null) {
			num = 0;
		} else {
			num = stones.property[Goods.PRO_COUNT];
		}
		return num;
	}

	/***************************************
	 * ��Ʒ����
	 **************************************/

	/**
	 * ��һ����Ʒ����Ʒ����
	 * 
	 * @param goods
	 *            Goods
	 * @return boolean
	 */
	public boolean addAItem(Goods goods) {

		// if ( getBucketSize () > GOODS_NUM )
		// {
		// if ( CDebug.showDebugInfo )
		// {
		// System.out.println ( "error: ��Ʒ�����������������Ʒ" );
		// }
		// return false;
		// }

		// �Ƚ����װ���Ƿ��Ѿ����ˣ����˾ͼ�����
		for (Enumeration e = hsItemList.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			Goods g = (Goods) (hsItemList.get(key));
			if (Goods.isTheSame(g, goods)) {
				g.property[Goods.PRO_COUNT]++;
				// ���999��
				if (g.property[Goods.PRO_COUNT] > 999) {
					g.property[Goods.PRO_COUNT] = 999;
				}
				return true;
			}
		}
		// �������ͬ ��װ����ӵ���Ʒ������
		goods.property[Goods.PRO_COUNT]++;
		hsItemList.put(String.valueOf(goods.getKey()), goods);
		System.out.println("������Ʒ�� " + goods.getName());
		return true;
	}

	/**
	 * ʹ�õ�����Ʒ
	 * 
	 * @param goods
	 *            Goods
	 * @return boolean
	 */
	public boolean useItem(int key) {
		Goods item = (Goods) (hsItemList.get(String.valueOf(key)));

		if (item.property[Goods.PRO_COUNT] > 0) {
			item.property[Goods.PRO_COUNT]--;
			// ȡ��Ӱ������Ե���ֵ
			short[] data = item.getAffectedPro();
			for (int i = 0; i < data.length; i++) {
				if (data[i] != 0) {
					m_actorProperty[i] += data[i];
				}
			}
			adjustHPMP();
			if (item.property[Goods.PRO_COUNT] <= 0) {
				hsItemList.remove(String.valueOf(key));
			}
			return true;
		}
		return false;
	}

	/**
	 * ʹ����Ʒ����
	 * 
	 * @param item
	 *            Goods
	 */
	public boolean useItem(Goods item) {
		return useItem(item.getKey());
	}

	/**
	 * ������Ʒ
	 * 
	 * @param itemID
	 *            int
	 * @param count
	 *            int
	 */
	public boolean dropItem(int key, int count) {
		Goods item = (Goods) (hsItemList.get(String.valueOf(key)));
		if (item == null) {
			System.out.println("��������Ʒ�����ڣ�id =  " + (key & 0xfff));
			return false;
		}
		int num = item.property[Goods.PRO_COUNT] - count;
		if (num > 0) {
			item.property[Goods.PRO_COUNT] = (short) num;
		} else// ��list�����޳�
		{
			item.property[Goods.PRO_COUNT] = (short) num;
			hsItemList.remove(String.valueOf(key));
		}
		return true;
	}

	/**
	 * ��������
	 * 
	 * @param item
	 *            Goods
	 * @param count
	 *            int
	 */
	public boolean dropItem(Goods item, int count) {
		return dropItem(item.getKey(), count);
	}

	/******************************************************************************
	 * ���ܲ���
	 *****************************************************************************/
	public Goods[] skills = new Goods[2]; // ����list

	/**
	 * �������߼�
	 * 
	 * @param x
	 *            int
	 * @param y
	 *            int
	 */
	public void doPointer(int x, int y) {
		// �˵�����ͼ
		if (CGame.m_curState == dGame.GST_GAME_RUN) {
			if (CTools.isPointerInBox(x, y, new short[] { 0, 300, 25, 320 })) {
				cGame.keyPressed(CKey.KEY_SOFT_LEFT);
				return;
			} else if (CTools.isPointerInBox(x, y, new short[] { 210, 300, 240,
					320 })) {
				cGame.keyPressed(CKey.KEY_SOFT_RIGHT);
				return;
			}
			// 7
			if (CTools.isPointerInBox(x, y,
					new short[] { 160, 0, 160 + 24, 24 })) {
				// #if W958C
				// # cGame.keyPressed(99);
				// #else
				cGame.keyPressed(Canvas.KEY_NUM7);
				// #endif
				return;
			}
			// 9
			else if (CTools.isPointerInBox(x, y, new short[] { 160 + 24, 0,
					160 + 24 + 24, 24 })) {
				// #if W958C
				// # cGame.keyPressed(109);
				// #else
				cGame.keyPressed(Canvas.KEY_NUM9);
				// #endif

				return;
			}// 0
			else if (CTools.isPointerInBox(x, y, new short[] { 160 + 24 + 24,
					0, 160 + 24 * 3, 24 })) {
				// #if W958C
				// # cGame.keyPressed(32);
				// #else
				cGame.keyPressed(Canvas.KEY_NUM0);
				// #endif
				return;
			}

			// ���Ƕ���
			int centerX;
			int centerY;
			x += Camera.cameraLeft;
			y += Camera.cameraTop;
			getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox1);

			centerX = (s_colBox1[0] + s_colBox1[2]) / 2;
			centerY = (s_colBox1[1] + s_colBox1[3]) / 2;
			// ����

			// ����
			if (CTools.isContaining(s_colBox1, x, y)) {
				cGame.keyPressed(CKey.KEY_MID);
			}
			// ��
			else if (y > s_colBox1[1] && y < s_colBox1[3] && x > 20) {
				cGame.keyPressed(x > centerX ? CKey.KEY_RIGHT : CKey.KEY_LEFT);
			}// ������
			else if (x > (s_colBox1[0] - 10) && x < (s_colBox1[2] + 10)
					&& x > 20) {
				cGame.keyPressed(y < centerY ? CKey.KEY_UP : CKey.KEY_DOWN);
			}// б
			else if (x > centerX && x > 20 && y < centerY) {
				cGame.keyPressed(Canvas.KEY_NUM3);
			}// б
			else if (x < centerX && x > 20 && y < centerY) {
				cGame.keyPressed(Canvas.KEY_NUM1);
			}
		} else if (CGame.m_curState == dGame.GST_SCRIPT_OPDLG) {

		} else {
			cGame.keyPressed(CKey.KEY_MID);
		}

	}

	/**
	 * ��ͼƬ������ɫ��
	 * 
	 * @param mlgIndex
	 *            int ��������ͼƬ���
	 * @param iIndex
	 *            int ��Ҫ������ͼƬ�ı��
	 * @param pIndex
	 *            int ��ɫ���� Ĭ�������Ϊ0 �����л�ɫ���
	 */
	public short[] suitInfo;

	void setSuit(int mlgIndex, int iIndex, int pIndex) {
		// #if !size_500K
		if (true) {
			return;
		}
		// #endif
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

	// ------------��Ӱ
	static AniPlayer[] shadow = new AniPlayer[2];

	void initShadow() {
		// shadow [0]= new AniPlayer (ResLoader.animations[m_animationID],
		// m_x >> dConfig.FRACTION_BITS, m_y >> dConfig.FRACTION_BITS);
		// shadow [1]= new AniPlayer (ResLoader.animations[m_animationID],
		// m_x >> dConfig.FRACTION_BITS, m_y >> dConfig.FRACTION_BITS);
	}

	AniPlayer shadowTemp;
	public int shadowTick;

	public void pushShadow() {
		// shadowTemp=shadow[0];
		// shadow[0]=shadow[1];
		// shadowTemp.setAnimAction(m_actionID);
		// shadowTemp.actionSequenceID=aniPlayer.actionSequenceID;
		// shadowTemp.setSpritePos (m_x >> dConfig.FRACTION_BITS, m_y >>
		// dConfig.FRACTION_BITS);
		// shadowTemp.setSpriteFlipX(aniPlayer.getSpriteFlipX());
		// shadowTemp.shadowTimer=2;
		// shadow[1]=shadowTemp;
	}

	void drawShadow(Graphics g) {
		// for (int i = 0; i < shadow.length; i++) {
		// if (shadow[i].shadowTimer-->0) {
		// shadow[i].drawFrame (g, null);
		// }
		// }
	}

	void clearShadow() {
		// for (int i = 0; i < shadow.length; i++) {
		// shadow[i].shadowTimer=0;
		// }
	}

	// ������ɶ�
	void checkTask() {
		// �ռ�ţ������
		if (Script.systemTasks[15] <= 0 || Script.systemTasks[15] >= 100) {
			return;
		}

		int beafNum = 0;
		Goods beaf = (Goods) hsItemList.get(((1 << 12) | 1) + "");
		if (beaf != null) {
			beafNum = beaf.getCount();
		}
		if (beafNum >= 10 && Script.systemTasks[15] < 99) {
			Script.systemTasks[15] = 99;
		} else if (beafNum < 10 && Script.systemTasks[15] == 99) {
			Script.systemTasks[15] = 98;
		}
	}

}
