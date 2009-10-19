package game.object;

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
public interface dHero {
	public final static byte ST_ACTOR_SQUAT = 9;
	public final static byte ST_ACTOR_LAND = 10;
	public final static byte ST_ACTOR_FLY = 11;
	public final static byte ST_ACTOR_JINK = 12;
	public final static byte ST_ACTOR_ATTACK_SP = 13;
	public final static byte ST_ACTOR_CHANGE = 14;// 变身中
	public final static byte ST_ACTOR_WIN = 15;//
	public final static byte ST_ACTOR_SPRINT = 16;// 装
	// public final static byte ST_ACTOR_ONTEETER = 10;
	// public final static byte ST_ACTOR_PUSH = 11;

	// 跳跃的类型
	public final static byte ST_ACTOR_TWO_JUMP = 12;
	public final static byte ST_ACTOR_NOR_JUMP = 13;

	// public final static byte ST_ACTOR_JUMP = 24;
	public final static int FLAG_ATTACK_SUCCESS = (1 << 16)
			| dActor.SIGN_EXTENDED_FLAGS;
	/***
      *
      */
	// public final static byte ST_ACTOR_WAIT = 0;
	// public final static byte ST_ACTOR_WALK = 1;
	// public final static byte ST_ACTOR_RUN = 2;
	// public final static byte ST_ACTOR_ATTACK = 3;
	// public final static byte ST_ACTOR_DIE = 4;
	// public final static byte ST_ACTOR_HURT = 5;
	// public final static byte ST_ACTOR_JUMP = 6;
	// public final static byte ST_ACTOR_AUTO_RUN = 7;

	// ST_ACTOR_WAIT
	public final static byte WAIT_ACT_INDEX = 0;
	public final static byte TURN_ACT_INDEX = 1;

	public final static byte WAIT_ACTION = dActionID.Action_ID_hero_stand;
	public final static byte WAIT_TURN = -1/*
											 * dActionID.Action_ID_hero_stand_turn
											 */;

	// ST_ACTOR_WALK
	public final static byte WALK_ACT_INDEX = 0;
	public final static byte WALK_ACTION = -1;

	// ST_ACTOR_RUN
	public final static byte RUN_ACT_INDEX = 0;
	public final static byte RUN_ACTION = dActionID.Action_ID_hero_walk;

	public final static byte RUN_STOP_INDEX = 1;
	public final static byte RUN_STOP_ACTION = dActionID.Action_ID_hero_walk_stop;

	// ST_ACTOR_HURT
	public final static byte HURT_ACT_INDEX = 0;
	public final static byte HURT_ACTION = dActionID.Action_ID_hero_hurt1;

	// public final static byte RUN_STOP_INDEX = 1;
	// public final static byte RUN_STOP_ACTION =
	// dActionID.Action_ID_hero_walk_stop;

	// ST_ACTOR_ATTACK
	public final static byte ATT_ACT_INDEX_1 = 0;
	public final static byte ATT_ACTION_1 = dActionID.Action_ID_hero_attack1_1;

	public final static byte ATT_ACT_INDEX_2 = 1;
	public final static byte ATT_ACTION_2 = dActionID.Action_ID_hero_attack1_2;

	public final static byte ATT_ACT_INDEX_3 = 2;
	public final static byte ATT_ACTION_3 = dActionID.Action_ID_hero_attack1_3;

	public final static byte ATT_ACT_INDEX_AIR = 3;
	public final static byte ATT_ACTION_AIR = dActionID.Action_ID_hero_jump_attack1;

	public final static byte ATT_ACT_INDEX_4 = 4;
	public final static byte ATT_ACTION_4 = dActionID.Action_ID_hero_attack2_1;

	public final static byte ATT_ACT_INDEX_5 = 5;
	public final static byte ATT_ACTION_5 = dActionID.Action_ID_hero_attack2_2;

	public final static byte ATT_ACT_INDEX_6 = 6;
	public final static byte ATT_ACTION_6 = dActionID.Action_ID_hero_attack2_3;

	public final static byte ATT_ACT_INDEX_AIR_1 = 7;
	public final static byte ATT_ACTION_AIR_1 = dActionID.Action_ID_hero_jump_attack2;

	public final static byte ATT_ACT_INDEX_7 = 8;
	public final static byte ATT_ACTION_7 = dActionID.Action_ID_hero_attack3_1;

	public final static byte ATT_ACT_INDEX_8 = 9;
	public final static byte ATT_ACTION_8 = dActionID.Action_ID_hero_attack3_2;

	public final static byte ATT_ACT_INDEX_9 = 10;
	public final static byte ATT_ACTION_9 = dActionID.Action_ID_hero_attack3_3;

	public final static byte ATT_ACT_INDEX_AIR_2 = 11;
	public final static byte ATT_ACTION_AIR_2 = dActionID.Action_ID_hero_jump_attack3;

	// ST_ACTOR_JUMP
	public final static byte JUMP_PRE_INDEX = 0;
	public final static byte JUMP_PRE_ACTION = dActionID.Action_ID_hero_jump_prepare;

	public final static byte JUMP_UP_INDEX = 1;
	public final static byte JUMP_UP_ACTION = dActionID.Action_ID_hero_jump_up;

	public final static byte JUMP_DOWN_INDEX = 2;
	public final static byte JUMP_DOWN_ACTION = dActionID.Action_ID_hero_jump_down;

	public final static byte INCLINED_UP_INDEX = 3;
	public final static byte INCLINED_UP_ACTION = dActionID.Action_ID_hero_jump_up_forward;

	public final static byte INCLINED_DOWN_INDEX = 4;
	public final static byte INCLINED_DOWN_ACTION = dActionID.Action_ID_hero_jump_down_forward;

	public final static byte TWO_INCLINED_INDEX = 5;
	public final static byte TWO_INCLINED_ACTION = dActionID.Action_ID_hero_jump_jump_forward;

	public final static byte TWO_UP_INDEX = 6;
	public final static byte TWO_UP_ACTION = dActionID.Action_ID_hero_jump_jump;

	public final static byte TOP_UP_INDEX = 7;
	public final static byte TOP_UP_ACTION = dActionID.Action_ID_hero_jump_jump_top;

	public final static byte TOP_INCLINE_INDEX = 8;
	public final static byte TOP_INCLINE_ACTION = dActionID.Action_ID_hero_jump_jump_forward_top;
	// 跳跃受伤
	public final static byte JUMP_HURT_INDEX = 9;
	public final static byte JUMP_HURT_ACTION = dActionID.Action_ID_hero_hurt2;

	// public final static byte TWO_DOWN_INDEX = 6;
	// public final static byte TWO_DOWN_ACTION =
	// dActionID.Action_ID_hero_jump_down_forward;

	// public final static byte JUMP_UP_INDEX = 1;
	// public final static byte JUMP_UP_ACTION =
	// dActionID.Action_ID_hero_jump_up;
	//
	// public final static byte JUMP_UP_INDEX = 1;
	// public final static byte JUMP_UP_ACTION =
	// dActionID.Action_ID_hero_jump_up;

	// ST_ACTOR_SQUAT
	public final static byte SQUAT_INDEX = 0;
	public final static byte SQUAT_ACTION = dActionID.Action_ID_hero_squat;

	public final static byte SQUAT_TO_STAND_INDEX = 1;
	public final static byte SQUAT_TO_STAND_ACTION = dActionID.Action_ID_hero_squat_stand;

	public final static byte STAND_TO_SQUAT_INDEX = 2;
	public final static byte STAND_TO_SQUAT_ACTION = dActionID.Action_ID_hero_stand_squat;

	public final static byte SQ_ACT_INDEX = 3;
	public final static byte SQ_ACT_ACTION = dActionID.Action_ID_hero_squat_move;

	public final static byte SQ_HURT_INDEX = 4;
	public final static byte SQ_HURT_ACTION = dActionID.Action_ID_hero_squat_hurt;

	public final static byte SQ_ATT_INDEX = 5;
	public final static byte SQ_ATT_ACTION = dActionID.Action_ID_hero_squat_attack1;

	// ST_ACTOR_LAND
	public final static byte LAND_INDEX = 0;
	public final static byte LAND_ACTION = dActionID.Action_ID_hero_land1;
	public final static byte LAND2_INDEX = 1;
	public final static byte LAND2_ACTION = dActionID.Action_ID_hero_land2;
	// ST_ACTOR_FLY

	public final static byte FLY_INDEX = 0;
	public final static byte FLY_ACTION = dActionID.Action_ID_hero_change;

	public final static byte FLYING_INDEX = 1;
	public final static byte FLYING_ACTION = dActionID.Action_ID_hero_fly;

	public final static byte JINK_INDEX = 0;
	public final static byte JINK_ACTION = dActionID.Action_ID_hero_jink;

	// ST_ATTACK_SP
	public final static byte ATTACK_SP_INDEX = 0;
	public final static byte ATTACK_SP_ACTION = dActionID.Action_ID_hero_attack_sp;

	// ST_CHANGE变身
	public final static byte CHANGE_INDEX = 0;
	public final static byte CHANGE_ACTION = dActionID.Action_ID_hero_change;

	// ST_ACTOR_DIE
	public final static byte DIE_INDEX = 0;
	public final static byte DIE_ACTION = dActionID.Action_ID_hero_die;

	// ST_ACTOR_WIN
	public final static byte WIN_INDEX = 0;
	public final static byte WIN_ACTION = dActionID.Action_ID_hero_win;

	// ST_ACTOR_DIE
	public final static byte SPRINT_INDEX = 0;
	public final static byte SPRINT_ACTION = dActionID.Action_ID_hero_sprint;

	public static int action_map[][] = {
			// ST_ACTOR_WAIT
			{ WAIT_ACTION, WAIT_TURN },
			// ST_ACTOR_WALK
			{ WALK_ACTION },
			// ST_ACTOR_RUN
			{ RUN_ACTION, RUN_STOP_ACTION },
			// ST_ACTOR_ATTACK
			{ ATT_ACTION_1, ATT_ACTION_2, ATT_ACTION_3, ATT_ACTION_AIR,
					ATT_ACTION_4, ATT_ACTION_5, ATT_ACTION_6, ATT_ACTION_AIR_1,
					ATT_ACTION_7, ATT_ACTION_8, ATT_ACTION_9, ATT_ACTION_AIR_2 },
			// ST_ACTOR_DIE
			{ DIE_ACTION },
			// ST_ACTOR_HURT
			{ HURT_ACTION },
			// ST_ACTOR_JUMP
			{ JUMP_PRE_ACTION, JUMP_UP_ACTION, JUMP_DOWN_ACTION,
					INCLINED_UP_ACTION, INCLINED_DOWN_ACTION,
					TWO_INCLINED_ACTION, TWO_UP_ACTION, TOP_UP_ACTION,
					TOP_INCLINE_ACTION, JUMP_HURT_ACTION, },
			//
			{},
			// ST_ACTOR_AUTO_ACTION
			{},
			// ST_SQUAT
			{ SQUAT_ACTION, SQUAT_TO_STAND_ACTION, STAND_TO_SQUAT_ACTION,
					SQ_ACT_ACTION, SQ_HURT_ACTION, SQ_ATT_ACTION },
			// ST_LAND
			{ LAND_ACTION, LAND2_ACTION },
			// ST_FLY
			{ FLY_ACTION, FLYING_ACTION },
			// ST_JINK
			{ JINK_ACTION },
			// ST_ATTACK_SP
			{ ATTACK_SP_ACTION }, { CHANGE_ACTION }, { WIN_ACTION },
			{ SPRINT_ACTION }, };
}
