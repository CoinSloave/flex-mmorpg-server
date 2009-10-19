package game.object;

import game.CGame;
import game.CTools;
import game.Goods;
import game.config.dConfig;
import game.pak.Camera;
import game.res.ResLoader;

import javax.microedition.lcdui.Graphics;

import com.mglib.mdl.ani.Player;

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

public class CElementor extends CObject {
	public CElementor() {
	}

	/*****************
	 * 初始化
	 ****************/
	public void initialize() {
		if (testClasssFlag(dActor.CLASS_FLAG_CHECK_ENV)) {
			initPhy();
			m_phyAttrib |= PHYATTRIB_COLENGINE | PHYATTRIB_GRAVITY;
		}
		super.initialize();
		// //表示这个对象需要初开始化
		// setFlag (dActor.FLAG_NEED_INIT);
	}

	public boolean update() {
		if (!super.update()) {
			return false;
		}
		int aiID = ResLoader.classAIIDs[m_classID];
		switch (aiID) {
		case dActorClass.CLASS_ID_OBJ_CAR:
			ai_car();
			break;
		case dActorClass.CLASS_ID_OBJ_CANATTACKBOX:
			ai_box();
			break;
		case dActorClass.CLASS_ID_ATTACK_EFFECT:
			ai_effect();
			break;
		case dActorClass.CLASS_ID_CAMERA_HINT:
			ai_CameraHint();
			break;
		case dActorClass.CLASS_ID_OBJ_DOOR:
			ai_door();
			break;
		case dActorClass.CLASS_ID_OBJ_KAIGUAN:
			ai_switch();
			break;
		case dActorClass.CLASS_ID_OBJ_ITEM:
			ai_Item();
			break;
		case dActorClass.CLASS_ID_OBJ_MONEY:
			ai_Money();
			break;
		default:
			break;
		}
		return true;
	}

	final static byte MOVEPLAT_IN_TYPE = 0;
	final static byte MOVEPLAT_IN_SPEED = 1;
	final static byte MOVEPLAT_IN_DIS = 2;
	final static byte MOVEPLAT_IN_DIR = 3;
	final static byte MOVEPLAT_IN_WAITTIME = 4;
	final static byte MOVEPLAT_IN_START = 5;
	final static byte MOVEPLAT_IN_END = 6;
	final static byte MOVEPLAT_IN_FACEDIR = 7;
	final static byte MOVEPLAT_IN_INIT = 8;

	final static byte MOVEPLAT_ST_WAIT = 0;
	final static byte MOVEPLAT_ST_MOVE = 1;

	final static byte TYPE_AUTO_LIFT = 0;
	final static byte TYPE_CONTROL_LIFT = 1;

	// private void ai_MovePlat () {
	// if(testFlag (dActor.FLAG_NEED_INIT)) {
	// clearFlag (dActor.FLAG_NEED_INIT);
	// m_parameters[MOVEPLAT_IN_TYPE] = getActorInfo
	// (dActorClass.Index_Param_OBJ_MOVEPLAT_TYPE);
	// m_parameters[MOVEPLAT_IN_SPEED] = getActorInfo
	// (dActorClass.Index_Param_OBJ_MOVEPLAT_SPEED);
	// m_parameters[MOVEPLAT_IN_DIS] = getActorInfo
	// (dActorClass.Index_Param_OBJ_MOVEPLAT_DISTANCE);
	// m_parameters[MOVEPLAT_IN_DIR] = getActorInfo
	// (dActorClass.Index_Param_OBJ_MOVEPLAT_DIR);
	// m_parameters[MOVEPLAT_IN_WAITTIME] = getActorInfo
	// (dActorClass.Index_Param_OBJ_MOVEPLAT_WAITTIME);
	//
	// // 0 : 上 1：下 2：左 3：右
	// if(m_parameters[MOVEPLAT_IN_DIR] == 0) {
	// m_parameters[MOVEPLAT_IN_FACEDIR] = -1; //
	// m_parameters[MOVEPLAT_IN_START] = m_initY >> dConfig.FRACTION_BITS;
	// m_parameters[MOVEPLAT_IN_END] = (m_initY >> dConfig.FRACTION_BITS) +
	// m_parameters[MOVEPLAT_IN_DIS] * m_parameters[MOVEPLAT_IN_FACEDIR];
	// }
	//
	// else if(m_parameters[MOVEPLAT_IN_DIR] == 1) {
	// m_parameters[MOVEPLAT_IN_FACEDIR] = 1;
	// m_parameters[MOVEPLAT_IN_END] = m_initY >> dConfig.FRACTION_BITS;
	// m_parameters[MOVEPLAT_IN_START] = (m_initY >> dConfig.FRACTION_BITS) +
	// m_parameters[MOVEPLAT_IN_DIS] * m_parameters[MOVEPLAT_IN_FACEDIR];
	// }
	//
	// else if(m_parameters[MOVEPLAT_IN_DIR] == 2) {
	// m_parameters[MOVEPLAT_IN_FACEDIR] = -1;
	// m_parameters[MOVEPLAT_IN_START] = m_initX >> dConfig.FRACTION_BITS;
	// m_parameters[MOVEPLAT_IN_END] = (m_initX >> dConfig.FRACTION_BITS) +
	// m_parameters[MOVEPLAT_IN_DIS] * m_parameters[MOVEPLAT_IN_FACEDIR];
	// }
	//
	// else if(m_parameters[MOVEPLAT_IN_DIR] == 3) {
	// m_parameters[MOVEPLAT_IN_FACEDIR] = 1;
	// m_parameters[MOVEPLAT_IN_END] = m_initX >> dConfig.FRACTION_BITS;
	// m_parameters[MOVEPLAT_IN_START] = (m_initX >> dConfig.FRACTION_BITS) +
	// m_parameters[MOVEPLAT_IN_DIS] * m_parameters[MOVEPLAT_IN_FACEDIR];
	//
	// }
	// setState (MOVEPLAT_ST_WAIT);
	//
	// }
	// else {
	// CGame.m_hero.checkCollionWithActor (this);
	// m_timer += getTimerStep ();
	// switch(m_currentState) {
	// case MOVEPLAT_ST_WAIT:
	// if(m_parameters[MOVEPLAT_IN_TYPE] == TYPE_CONTROL_LIFT) {
	// if(m_timer > m_parameters[MOVEPLAT_IN_WAITTIME] * getTimerStep ()) {
	// setState (MOVEPLAT_ST_MOVE);
	// if(m_parameters[MOVEPLAT_IN_DIR] == 1 || m_parameters[MOVEPLAT_IN_DIR] ==
	// 0) {
	// m_vY = (Math.abs (m_parameters[MOVEPLAT_IN_SPEED]) *
	// m_parameters[MOVEPLAT_IN_FACEDIR])
	// << dConfig.FRACTION_BITS;
	// }
	// else {
	// m_vX = (Math.abs (m_parameters[MOVEPLAT_IN_SPEED]) *
	// m_parameters[MOVEPLAT_IN_FACEDIR])
	// << dConfig.FRACTION_BITS;
	// }
	// }
	// }
	// else if(m_parameters[MOVEPLAT_IN_TYPE] == TYPE_AUTO_LIFT) {
	// if(CGame.m_hero.m_relativeMisc == this
	// || m_initY != m_y || m_initX != m_x) {
	// setState (MOVEPLAT_ST_MOVE);
	// if(m_parameters[MOVEPLAT_IN_DIR] == 1 || m_parameters[MOVEPLAT_IN_DIR] ==
	// 0) {
	// m_vY = (Math.abs (m_parameters[MOVEPLAT_IN_SPEED]) *
	// m_parameters[MOVEPLAT_IN_FACEDIR])
	// << dConfig.FRACTION_BITS;
	// }
	// else {
	// m_vX = (Math.abs (m_parameters[MOVEPLAT_IN_SPEED]) *
	// m_parameters[MOVEPLAT_IN_FACEDIR])
	// << dConfig.FRACTION_BITS;
	// }
	//
	// }
	// }
	// break;
	//
	// case MOVEPLAT_ST_MOVE:
	// updatePostionNophy ();
	// if(m_parameters[MOVEPLAT_IN_FACEDIR] == 1) { //向下移动
	// if(m_parameters[MOVEPLAT_IN_DIR] == 1 || m_parameters[MOVEPLAT_IN_DIR] ==
	// 0) {
	// if(m_y >= (m_parameters[MOVEPLAT_IN_START] << dConfig.FRACTION_BITS)) {
	// m_y = m_parameters[MOVEPLAT_IN_START] << dConfig.FRACTION_BITS;
	// m_parameters[MOVEPLAT_IN_FACEDIR] *= -1;
	// setState (MOVEPLAT_ST_WAIT);
	// }
	// }
	// else {
	// if(m_x >= (m_parameters[MOVEPLAT_IN_START] << dConfig.FRACTION_BITS)) {
	// m_x = m_parameters[MOVEPLAT_IN_START] << dConfig.FRACTION_BITS;
	// m_parameters[MOVEPLAT_IN_FACEDIR] *= -1;
	// setState (MOVEPLAT_ST_WAIT);
	// }
	//
	// }
	// }
	// else {
	// if(m_parameters[MOVEPLAT_IN_DIR] == 1 || m_parameters[MOVEPLAT_IN_DIR] ==
	// 0) {
	// if(m_y <= (m_parameters[MOVEPLAT_IN_END] << dConfig.FRACTION_BITS)) {
	// m_y = m_parameters[MOVEPLAT_IN_END] << dConfig.FRACTION_BITS;
	// m_parameters[MOVEPLAT_IN_FACEDIR] *= -1;
	// setState (MOVEPLAT_ST_WAIT);
	// }
	// }
	// else {
	// if(m_x <= (m_parameters[MOVEPLAT_IN_END] << dConfig.FRACTION_BITS)) {
	// m_x = m_parameters[MOVEPLAT_IN_END] << dConfig.FRACTION_BITS;
	// m_parameters[MOVEPLAT_IN_FACEDIR] *= -1;
	// setState (MOVEPLAT_ST_WAIT);
	// }
	// }
	// }
	//
	// break;
	// }
	//
	// if(CGame.m_hero.m_relativeMisc != null && CGame.m_hero.m_relativeMisc ==
	// this) {
	// CGame.m_hero.m_y = m_y + (aniPlayer.boxInf[TOP] <<
	// dConfig.FRACTION_BITS);
	// if(m_parameters[MOVEPLAT_IN_DIR] == 2 || m_parameters[MOVEPLAT_IN_DIR] ==
	// 3) {
	// CGame.m_hero.m_phyEvx = m_vX;
	// }
	// if(m_currentState == MOVEPLAT_ST_WAIT) {
	// CGame.m_hero.m_phyEvx = 0;
	// }
	// }
	//
	// }
	// }

	/**
	 * 镜头锁定
	 */

	public static final int INDEX_CAMERA_HINT_LOCK_X = 0;
	public static final int INDEX_CAMERA_HINT_LOCK_Y = 1;
	public static final int INDEX_CAMERA_HINT_HERO_X = 2;
	public static final int INDEX_CAMERA_HINT_HERO_Y = 3;
	public static final int INDEX_CAMERA_HINT_INAREA = 4;
	public static final int INDEX_CAMERA_HINT_ENEMY_UNLOCK = 5;
	public static final int INDEX_CAMERA_HINT_ENEMY1 = 6;
	public static final int INDEX_CAMERA_HINT_ENEMY2 = 7;
	public static final int INDEX_CAMERA_HINT_ENEMY3 = 8;
	public static final int INDEX_CAMERA_HINT_ENEMY4 = 9;

	void ai_CameraHint() {
		if (testFlag(dActor.FLAG_NEED_INIT)) {
			m_parameters[INDEX_CAMERA_HINT_LOCK_X] = getActorInfo(dActorClass.Index_Param_CAMERA_HINT_LOCK_X);
			m_parameters[INDEX_CAMERA_HINT_LOCK_Y] = getActorInfo(dActorClass.Index_Param_CAMERA_HINT_LOCK_Y);
			m_parameters[INDEX_CAMERA_HINT_HERO_X] = getActorInfo(dActorClass.Index_Param_CAMERA_HINT_HERO_X);
			m_parameters[INDEX_CAMERA_HINT_HERO_Y] = getActorInfo(dActorClass.Index_Param_CAMERA_HINT_HERO_Y);
			m_parameters[INDEX_CAMERA_HINT_INAREA] = getActorInfo(dActorClass.Index_Param_CAMERA_HINT_LOCKINAREA);
			m_parameters[INDEX_CAMERA_HINT_ENEMY_UNLOCK] = getActorInfo(dActorClass.Index_Param_CAMERA_HINT_ENEMY_UNLOCK);
			m_parameters[INDEX_CAMERA_HINT_ENEMY1] = getActorInfo(dActorClass.Index_Param_CAMERA_HINT_ENEMY1);
			m_parameters[INDEX_CAMERA_HINT_ENEMY2] = getActorInfo(dActorClass.Index_Param_CAMERA_HINT_ENEMY2);
			m_parameters[INDEX_CAMERA_HINT_ENEMY3] = getActorInfo(dActorClass.Index_Param_CAMERA_HINT_ENEMY3);
			m_parameters[INDEX_CAMERA_HINT_ENEMY4] = getActorInfo(dActorClass.Index_Param_CAMERA_HINT_ENEMY4);
			clearFlag(dActor.FLAG_NEED_INIT);
		}

		// 如果是敌人死完就解锁
		if (m_parameters[INDEX_CAMERA_HINT_ENEMY_UNLOCK] != 0) {
			boolean allEnemyDead = true;
			for (int i = 0; i < 4; i++) {
				if (m_parameters[INDEX_CAMERA_HINT_ENEMY1 + i] >= 0) {
					CObject enemy = tryActivateLinkedActor(
							m_parameters[INDEX_CAMERA_HINT_ENEMY1 + i], false);
					if (enemy != null
							&& (enemy.m_actorProperty[PRO_INDEX_HP] > 0 || // add
																			// by
																			// lin
																			// 09.04.27
																			// 死亡动画播完
							enemy.testFlag(dActor.FLAG_DIE_TO_SCRIPT))) {
						allEnemyDead = false;
						break;
					}
				}
			}
			// 解锁
			if (allEnemyDead) {
				Camera.isLockInArea = false;
				die(false);
				CObject.m_otherCamera = 0;
				m_parameters[INDEX_CAMERA_HINT_LOCK_X] = 0;
				m_parameters[INDEX_CAMERA_HINT_LOCK_Y] = 0;
				return;
			}
		}

		if (m_parameters[INDEX_CAMERA_HINT_INAREA] != 0) {
			// 镜头是否和激活区域碰撞
			boolean isInActiveBox = CTools.isIntersecting(Camera.cameraBox,
					getActorActivateBoxInfo(m_actorID, CObject.s_colBox1));
			if (isInActiveBox) {
				if (!Camera.isLockInArea) {
					System.arraycopy(CObject.s_colBox1, 0,
							Camera.lockCameraBox, 0, 4);
					Camera.lockCameraBox[Camera.LEFT] -= dConfig.CAMERA_WIDTH;
					Camera.lockCameraBox[Camera.RIGHT] += dConfig.CAMERA_WIDTH;
				}
				Camera.isLockInArea = true;
				return;
			}
		} else {
			getActorActivateBoxInfo(m_actorID, s_colBox1);
			CGame.m_hero.getActorBoxInfo(dActor.BOX_COLLIDE, s_colBox2);

			// 用激活区域激活这个对象的AI
			if (!CTools.isIntersecting(s_colBox1, s_colBox2)) {
				if (m_parameters[INDEX_CAMERA_HINT_ENEMY_UNLOCK] == 0) {
					if (m_parameters[INDEX_CAMERA_HINT_LOCK_X] != 0
							|| m_parameters[INDEX_CAMERA_HINT_LOCK_Y] != 0) {
						CObject.m_otherCamera = 0;
					}
				}
				return;
			}

			// 屏幕等分的区域来控制屏幕为位置
			if (m_parameters[INDEX_CAMERA_HINT_LOCK_X] == 0
					&& m_parameters[INDEX_CAMERA_HINT_LOCK_Y] == 0) {
				// 根据填入的参数锁定镜头x坐标值
				CObject.m_otherCamera |= ((m_parameters[INDEX_CAMERA_HINT_HERO_X] & 0xf) << dConfig.FRACTION_BITS);
				// 根据填入的参数锁定镜头y坐标值
				CObject.m_otherCamera |= m_parameters[INDEX_CAMERA_HINT_HERO_Y] & 0xf;
			}

			// 根据当前对象的实际坐标来控制屏幕位置
			else if (m_parameters[INDEX_CAMERA_HINT_LOCK_X] != 0
					&& m_parameters[INDEX_CAMERA_HINT_LOCK_Y] != 0) {
				CObject.m_lockCameraX = m_x >> dConfig.FRACTION_BITS;
				CObject.m_lockCameraY = m_y >> dConfig.FRACTION_BITS;
				CObject.m_otherCamera |= PARA_LOCKCAMERA_XY;
			} else if (m_parameters[INDEX_CAMERA_HINT_LOCK_X] != 0) {
				CObject.m_otherCamera |= PARA_LOCKCAMERA_X;
				CObject.m_lockCameraX = m_x >> dConfig.FRACTION_BITS;
			} else if (m_parameters[INDEX_CAMERA_HINT_LOCK_Y] != 0) {
				CObject.m_otherCamera |= PARA_LOCKCAMERA_Y;
				CObject.m_lockCameraY = m_y >> dConfig.FRACTION_BITS;
			}
		}
		CObject.m_otherCamera |= m_actionID << 24;
	}

	public final static byte INDEX_MONEY_HOWMUCH = 0;

	/**
	 * 金钱
	 */
	void ai_Money() {
		if (testFlag(dActor.FLAG_NEED_INIT)) {
			clearFlag(dActor.FLAG_NEED_INIT);
			setState(0);

			m_parameters[INDEX_MONEY_HOWMUCH] = getActorInfo(dActorClass.Index_Param_OBJ_MONEY_TYPE);
			return;
		}
		CHero hero = CGame.m_hero;
		int temp_vY = m_vY;
		doPhysics();
		if (m_bBlockedBottom) {
			m_vY = m_aY = 0;
			m_vX = m_aX = 0;
		}
		if (testColliding(hero)) {
			CGame.m_hero.addBonusShow(m_parameters[INDEX_MONEY_HOWMUCH], "金钱+"
					+ m_parameters[INDEX_MONEY_HOWMUCH], DOUBLE_ATT);
			CGame.m_hero.addMoney(m_parameters[INDEX_MONEY_HOWMUCH]);
			die(false);
			setFlag(dActor.FLAG_BASIC_DIE);
			return;
		}

		// 时间长消失
		if (m_actorID < 0 && m_timer++ > 130
				&& !testFlag(dActor.FLAG_BASIC_DIE)) {
			die(false);
		}

	}

	/**
	 * 火堆小机关,可以关联boss_fire，也可以单独使用 shubinl
	 */
	/*
	 * void ai_huodui () { if(testFlag (dActor.FLAG_NEED_INIT)) {
	 * 
	 * clearFlag (dActor.FLAG_NEED_INIT); return; } //被打灭 if(testFlag
	 * (dActor.FLAG_BEATTACKED)) { if(m_actionID ==
	 * dActionID.Action_ID_huodui_fire || m_actionID ==
	 * dActionID.Action_ID_huodui_appear) { setAnimAction
	 * (dActionID.Action_ID_huodui_disappear); } clearFlag
	 * (dActor.FLAG_BEATTACKED); } CHero hero = CGame.m_hero; switch(m_actionID)
	 * { case dActionID.Action_ID_huodui_putout:
	 * 
	 * 
	 * // int
	 * linkFireBossId=getActorInfo(dActorClass.Index_Param_OBJ_HUODUI_LINK); //
	 * if (linkFireBossId>=0) { // AbstractActor
	 * fireBoss=tryActivateLinkedActor(linkFireBossId,true); // if
	 * ((fireBoss!=null)&&fireBoss.m_parameters[AI_BOSS_Water.INDEX_FIRE]!=0) {
	 * // setAnimAction(dActionID.Action_ID_huodui_appear); // } // } break;
	 * case dActionID.Action_ID_huodui_appear: if(aniPlayer.testAniPlayFlag
	 * (aniPlayer.FLAG_ACTION_OVER)) { setAnimAction
	 * (dActionID.Action_ID_huodui_fire); // setState(ST_NORMAL); } break; case
	 * dActionID.Action_ID_huodui_fire: boolean collideHero = testColliding
	 * (hero); if(collideHero) { if(hero.beAttack (getActorInfo
	 * (dActorClass.Index_Param_OBJ_HUODUI_DAMAGE))) { hero.m_invincible = 12; }
	 * } break; case dActionID.Action_ID_huodui_disappear:
	 * if(aniPlayer.testAniPlayFlag (aniPlayer.FLAG_ACTION_OVER)) {
	 * setAnimAction (dActionID.Action_ID_huodui_putout); } break; } }
	 */
	/**
	 * 放电的机关、电球，主角碰到攻击框受伤
	 * 
	 * @author shubinl
	 */
	/*
	 * void ai_fangDian(){ if (testFlag(dActor.FLAG_NEED_INIT)) {
	 * m_actorProperty[PRO_INDEX_ATT]=
	 * getActorInfo(dActorClass.Index_Param_OBJ_FANGDIAN_DAMAGE);
	 * clearFlag(dActor.FLAG_NEED_INIT); } attHero(); //
	 * getActorBoxInfo(dActor.BOX_ATTACK,s_colBox1); //
	 * CGame.m_hero.getActorBoxInfo(dActor.BOX_COLLIDE,s_colBox2); // if
	 * (CTools.isIntersecting(s_colBox2,s_colBox1)) { //
	 * CGame.m_hero.beAttack(getActorInfo
	 * (dActorClass.Index_Param_OBJ_FANGDIAN_DAMAGE)); // } }
	 */
	/**
	 * 电极，和ai_leiDian雷电关联，2极相同则雷电关闭，被攻击电极转换
	 * 
	 * @author shubinl
	 */
	// void ai_dianJi(){
	// if (testFlag(dActor.FLAG_BEATTACKED)) {
	// setAnimAction((++m_actionID)%2);
	// clearFlag(dActor.FLAG_BEATTACKED);
	// }
	// }

	// 关联2个电极
	public static final int INDEX_leiDian_link1 = 0;
	public static final int INDEX_leiDian_link2 = 1;
	/**
	 * 雷电，ai_dianJi电极关联，2极相同则雷电关闭
	 * 
	 * @author shubinl
	 */
	// void ai_leiDian() {
	// if (testFlag(dActor.FLAG_NEED_INIT)) {
	// m_actorProperty[PRO_INDEX_ATT] =
	// getActorInfo(dActorClass.Index_Param_OBJ_LEIDIAN_DAMAGE);
	// m_parameters[INDEX_leiDian_link1]=
	// getActorInfo(dActorClass.Index_Param_OBJ_LEIDIAN_LINK1);
	// m_parameters[INDEX_leiDian_link2]=
	// getActorInfo(dActorClass.Index_Param_OBJ_LEIDIAN_LINK2);
	// clearFlag(dActor.FLAG_NEED_INIT);
	// }
	//
	// if
	// (m_parameters[INDEX_leiDian_link1]<0||m_parameters[INDEX_leiDian_link2]<0)
	// {
	// return;
	// }
	//
	// //关联的电极
	// CObject dianJi1 =
	// tryActivateLinkedActor(m_parameters[INDEX_leiDian_link1],true);
	// CObject dianJi2 =
	// tryActivateLinkedActor(m_parameters[INDEX_leiDian_link2],true);
	// if (dianJi1==null||dianJi2==null) {
	// return;
	// }
	//
	// boolean on=dianJi1.m_actionID!=dianJi2.m_actionID;
	// if (m_actionID==dActionID.Action_ID_leidian_01on||
	// m_actionID==dActionID.Action_ID_leidian_02on) {
	// if (!on) {
	// setAnimAction(m_actionID+1);
	// }
	// }
	// else if (m_actionID==dActionID.Action_ID_leidian_01off||
	// m_actionID==dActionID.Action_ID_leidian_02off) {
	// if (on) {
	// setAnimAction(m_actionID-1);
	// }
	// }
	//
	// attHero();
	// }

	final static byte INDEX_TRACK_SPEED = 0;

	/**
	 * 水流，改变主角X方向速度
	 * 
	 * @author shubinl
	 */
	// private void ai_track() {
	// if (testFlag(dActor.FLAG_NEED_INIT)) {
	// clearFlag(dActor.FLAG_NEED_INIT);
	// m_parameters[INDEX_TRACK_SPEED] =
	// getActorInfo(dActorClass.Index_Param_OBJ_TRACK_SPEED);
	// return;
	// }
	//
	// CHero hero = CGame.m_hero;
	// if (testActiveBoxCollideObject(hero)) {
	// hero.m_phyEvx = m_parameters[INDEX_TRACK_SPEED] << dConfig.FRACTION_BITS;
	// }
	// }

	/**
	 * 石钟乳，主角碰到激活框掉落
	 * 
	 * @author shubinl
	 */
	/*
	 * void ai_fallIce(){ if (testFlag(dActor.FLAG_NEED_INIT)) {
	 * m_actorProperty[
	 * PRO_INDEX_ATT]=getActorInfo(dActorClass.Index_Param_OBJ_FALLICE_DAMAGE);
	 * clearFlag(dActor.FLAG_NEED_INIT); return; }
	 * 
	 * if (testFlag(dActor.FLAG_BEATTACKED)) { if
	 * (m_actionID==dActionID.Action_ID_ice_normal||
	 * m_actionID==dActionID.Action_ID_ice_prepare) {
	 * setAnimAction(dActionID.Action_ID_ice_die); //背向主角 if
	 * (isFaceTo(CGame.m_hero)) { turnAround(); } }
	 * clearFlag(dActor.FLAG_BEATTACKED); } CHero hero=CGame.m_hero; // boolean
	 * collideHero=testColliding(hero); // if (collideHero){ // int damage =
	 * Math.max(1,
	 * m_actorProperty[PRO_INDEX_ATT]-hero.m_actorProperty[PRO_INDEX_DEF]); //
	 * if (hero.beAttack(damage)) { // hero.m_invincible = 12; //
	 * setAnimAction(dActionID.Action_ID_ice_disappear); // } // }
	 * 
	 * attHero(); switch (m_actionID) { case dActionID.Action_ID_ice_normal: if
	 * (testActiveBoxCollideObject(hero)) {
	 * setAnimAction(dActionID.Action_ID_ice_prepare); } break; case
	 * dActionID.Action_ID_ice_prepare: if
	 * (aniPlayer.testAniPlayFlag(aniPlayer.FLAG_ACTION_OVER)) {
	 * setAnimAction(dActionID.Action_ID_ice_fall); }
	 * 
	 * break; case dActionID.Action_ID_ice_fall: doPhysics(); if
	 * (m_bBlockedBottom) { setAnimAction(dActionID.Action_ID_ice_disappear); }
	 * break; case dActionID.Action_ID_ice_disappear: case
	 * dActionID.Action_ID_ice_die: if
	 * (aniPlayer.testAniPlayFlag(aniPlayer.FLAG_ACTION_OVER)) { die(false); }
	 * break; }
	 * 
	 * }
	 */

	public final static byte JUMPPLAT_ST_WAIT = 0;
	public final static byte JUMPPLAT_ST_JUMP = 1;
	/**
	 * 跳板
	 */
	/*
	 * private void ai_JumpPlat() { if (testFlag(dActor.FLAG_NEED_INIT)) {
	 * clearFlag(dActor.FLAG_NEED_INIT); return; } CHero hero = CGame.m_hero;
	 * CGame.m_hero.checkCollionWithActor(this); switch (m_currentState) { case
	 * JUMPPLAT_ST_WAIT: if (testActiveBoxCollideObject(hero) &&
	 * (hero.m_relativeMisc == this)) { // m_timer+=getTimerStep(); //
	 * if(m_timer > 5 * getTimerStep()){ setState(JUMPPLAT_ST_JUMP);
	 * setAnimAction(dActionID.Action_ID_wind_rebound);
	 * 
	 * 
	 * hero.setState (ST_ACTOR_JUMP, -1, false); hero.setAnimAction (
	 * hero.action_map[ST_ACTOR_JUMP][hero.INCLINED_UP_INDEX]);
	 * hero.aniPlayer.setAniPlayFlag(hero.aniPlayer.FLAG_ACTION_NOT_CYCLE);
	 * hero.m_vY = -31 << dConfig.FRACTION_BITS; // } } break; case
	 * JUMPPLAT_ST_JUMP: if
	 * (aniPlayer.testAniPlayFlag(aniPlayer.FLAG_ACTION_OVER)) {
	 * setState(JUMPPLAT_ST_WAIT);
	 * setAnimAction(dActionID.Action_ID_wind_normal); } break; } }
	 */

	private static final byte STICK_ST_WAIT = ST_COUNT + 0;
	private static final byte STICK_ST_ALARM = ST_COUNT + 1;
	private static final byte STICK_ST_ATTACK = ST_COUNT + 2;

	private static final byte INDEX_compoundFire_WAITTIME = 0;
	private static final byte INDEX_compoundFire_DELAY = 2;

	/**
	 * 喷火机关
	 * 
	 * @author fanxiaoqing
	 */
	/*
	 * private void ai_compoundFire() { if (testFlag(dActor.FLAG_NEED_INIT)) {
	 * clearFlag(dActor.FLAG_NEED_INIT); setState(STICK_ST_WAIT);
	 * m_parameters[INDEX_compoundFire_WAITTIME] =
	 * getActorInfo(dActorClass.Index_Param_OBJ_COMPOUNDFIRE_WAITTIME);
	 * m_actorProperty[PRO_INDEX_ATT] =
	 * getActorInfo(dActorClass.Index_Param_OBJ_COMPOUNDFIRE_DAMAGE);
	 * m_parameters[INDEX_compoundFire_DELAY] =
	 * getActorInfo(dActorClass.Index_Param_OBJ_COMPOUNDFIRE_DELAY); return; }
	 * 
	 * switch (m_currentState) { case STICK_ST_WAIT:
	 * m_parameters[INDEX_compoundFire_WAITTIME]--; if
	 * (m_parameters[INDEX_compoundFire_WAITTIME] <= 0) {
	 * m_parameters[INDEX_compoundFire_WAITTIME] =
	 * getActorInfo(dActorClass.Index_Param_OBJ_COMPOUNDFIRE_WAITTIME);
	 * setState(STICK_ST_ALARM); this.setAnimAction(++m_actionID); } break; case
	 * STICK_ST_ALARM: if
	 * (aniPlayer.testAniPlayFlag(aniPlayer.FLAG_ACTION_OVER)) {
	 * aniPlayer.setAniPlayFlag(aniPlayer.FLAG_ACTION_NOT_CYCLE);
	 * setState(STICK_ST_ATTACK); this.setAnimAction(++m_actionID); m_y =
	 * m_initY; } break; case STICK_ST_ATTACK: attHero();
	 * m_parameters[INDEX_compoundFire_DELAY]--; if
	 * (aniPlayer.testAniPlayFlag(aniPlayer.FLAG_ACTION_OVER)) { if
	 * (m_parameters[INDEX_compoundFire_DELAY] <= 0) {
	 * m_parameters[INDEX_compoundFire_DELAY] =
	 * getActorInfo(dActorClass.Index_Param_OBJ_COMPOUNDFIRE_DELAY);
	 * setState(STICK_ST_WAIT); this.setAnimAction(m_actionID -= 2);
	 * aniPlayer.clearAniPlayFlag(aniPlayer.FLAG_ACTION_NOT_CYCLE); } }
	 * 
	 * break; } }
	 */

	static final byte TYPEID = 0;
	static final byte GOODID = 1;

	/**
	 * 物 品
	 */
	private void ai_Item() {
		if (testFlag(dActor.FLAG_NEED_INIT)) {
			clearFlag(dActor.FLAG_NEED_INIT);
			m_parameters[TYPEID] = getActorInfo(dActorClass.Index_Param_OBJ_ITEM_TYPE);
			m_parameters[GOODID] = getActorInfo(dActorClass.Index_Param_OBJ_ITEM_ID);
			return;
		}

		doPhysics();
		if (m_bBlockedBottom) {
			m_vX = 0;
		}

		if (testColliding(CGame.m_hero)) {
			Goods theGoods = null;
			if (m_parameters[TYPEID] == 0) {
				theGoods = Goods.createGoods((short) m_parameters[TYPEID],
						(short) m_parameters[GOODID]);
				if (CGame.m_hero.addAEquip(theGoods)) {
					CGame.m_hero.addBonusShow(0, theGoods.getName(),
							CObject.NORMAL_ATT);
				}
			} else if (m_parameters[TYPEID] == 1) {
				theGoods = Goods.createGoods((short) m_parameters[TYPEID],
						(short) m_parameters[GOODID]);
				if (CGame.m_hero.addAItem(theGoods)) {
					CGame.m_hero.addBonusShow(0, theGoods.getName(),
							CObject.NORMAL_ATT);
				}
			}
			die(false);
		}

		// 时间长消失
		if (m_actorID < 0 && m_timer++ > 230
				&& !testFlag(dActor.FLAG_BASIC_DIE)) {
			die(false);
		}
	}

	/**
	 * 宝箱，被攻击掉物品
	 */
	/*
	 * private void ai_itemBox () { if(m_actionID ==
	 * dActionID.Action_ID_box_box1 || m_actionID ==
	 * dActionID.Action_ID_box_box2 || m_actionID ==
	 * dActionID.Action_ID_box_box3 || m_actionID ==
	 * dActionID.Action_ID_box_box4 || m_actionID ==
	 * dActionID.Action_ID_box_box5) { if(testFlag (dActor.FLAG_BEATTACKED)) {
	 * setState (ST_ACTOR_DIE); setAnimAction (m_actionID + 1); } } else
	 * if(aniPlayer.testAniPlayFlag (aniPlayer.FLAG_ACTION_OVER)) { die (false);
	 * } }
	 */

	/**
	 * 阻挡，李逵可撞开 shubinl
	 */
	private void ai_door() {
		CGame.m_hero.checkCollionWithActor(this);
		switch (m_actionID) {
		// 泥墙
		case dActionID.Action_ID_door_common:
			if (testFlag(dActor.FLAG_BEATTACKED)) {
				setAnimAction(dActionID.Action_ID_door_pohuai);
			}
			break;
		case dActionID.Action_ID_door_pohuai:
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				die(false);
			}
			break;
		// 铁门
		case dActionID.Action_ID_door_door2opening:
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				setAnimAction(dActionID.Action_ID_door_door2open);
			}
			break;
		case dActionID.Action_ID_door_door2open:
			if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
				die(false);
			}
			break;
		}
	}

	private void ai_car() {
		CGame.m_hero.checkCollionWithActor(this);
	}

	/**
	 * 铁门的开关
	 */
	void ai_switch() {
		if (testColliding(CGame.m_hero)) {
			CObject door = tryActivateLinkedActor(
					getActorInfo(dActorClass.Index_Param_OBJ_KAIGUAN_DOOR),
					false);
			if (door != null
					&& door.m_actionID == dActionID.Action_ID_door_door2) {
				door.setAnimAction(dActionID.Action_ID_door_door2opening);
				setAnimAction(dActionID.Action_ID_kaiguan_on);
			}
		}
	}

	/**
	 * 宝箱
	 */
	void ai_box() {
		if (testFlag(dActor.FLAG_BEATTACKED)) {
			setState(ST_ACTOR_DIE);
			setAnimAction(dActionID.Action_ID_box_3);
			setFlag(dActor.FLAG_BASIC_NOT_PACKABLE);
			clearFlag(dActor.FLAG_BEATTACKED);
		} else if (m_actionID == dActionID.Action_ID_box_3
				&& aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			die(false);
		}
	}

	/**
	 * 提示箭头
	 */
	/*
	 * private void ai_Direction () { if(testActiveBoxCollideObject
	 * (CGame.m_hero)) { setFlag (dActor.FLAG_BASIC_VISIBLE); } else { clearFlag
	 * (dActor.FLAG_BASIC_VISIBLE); } }
	 */

	/**
	 * 绘制
	 * 
	 * @param g
	 *            Graphics
	 */
	public void paint(Graphics g) {
		super.paint(g);
	}

	/**
	 * 攻击特效
	 */
	void ai_effect() {
		if (aniPlayer.testAniPlayFlag(Player.FLAG_ACTION_OVER)) {
			die(false);
		}
	}

}
