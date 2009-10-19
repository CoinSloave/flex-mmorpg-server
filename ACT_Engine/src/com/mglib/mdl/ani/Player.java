package com.mglib.mdl.ani;

import game.CDebug;

import javax.microedition.lcdui.Graphics;

import com.mglib.mdl.ContractionMLG;

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
public abstract class Player {

	public AniData aniData;

	// protected int animationID;

	public int actionID;

	public int actionSequenceDuration;

	public int actionSequenceID;

	public int extDuration;

	public int flipX;

	public int m_tickAction;

	// 对象碰撞框的信息
	public short[] boxInf = new short[12];

	public int aniPlayFlag;

	public static final int FLAG_ACTION_HOLD = 1 << 0; //
	public static final int FLAG_ACTION_REVERSE = (1 << 1); // 动画倒播
	public static final int FLAG_ACTION_OVER = (1 << 2); // 动画结束
	public static final int FLAG_ACTION_NOT_CYCLE = (1 << 3); // 动画不循环
	public static final int FLAG_ACTION_ALL = FLAG_ACTION_HOLD
			| FLAG_ACTION_REVERSE | FLAG_ACTION_OVER | FLAG_ACTION_NOT_CYCLE;

	// public static final int FLAG_ACTION_GRAB_MECH_INFO = ( 1<<4 );

	public static final short INDEX_COLLISION_BOX_LEFT = 0;
	public static final short INDEX_COLLISION_BOX_TOP = 1;
	public static final short INDEX_COLLISION_BOX_RIGHT = 2;
	public static final short INDEX_COLLISION_BOX_BOTTOM = 3;
	public static final short INDEX_ATTACK_BOX_LEFT = 4;
	public static final short INDEX_ATTACK_BOX_TOP = 5;
	public static final short INDEX_ATTACK_BOX_RIGHT = 6;
	public static final short INDEX_ATTACK_BOX_BOTTOM = 7;
	public static final short INDEX_COLLISION_BOX_FRONT = 8;
	public static final short INDEX_COLLISION_BOX_BACK = 9;
	public static final short INDEX_COLLISION_BOX_X_CENTER = 10;
	public static final short INDEX_COLLISION_BOX_Y_CENTER = 11;

	public Player(AniData data) {
		this.aniData = data;
	}

	/****************************************************************************
	 * mask for data using
	 ***************************************************************************/
	// sprite data
	public static final int MASK_FLIP = 0x00000007; // 3
	public static final int MASK_MODULEID = 0x000003FF; // 10
	public static final int MASK_DX = 0x000001FF; // 9
	public static final int MASK_DY = 0x000001FF; // 9
	public static final byte MOVE_BIT_FLIP = 28;
	public static final byte MOVE_BIT_MODULEID = 18;
	public static final byte MOVE_BIT_DX = 9;
	public static final byte MOVE_BIT_DY = 0;
	public static final byte MINUS_TEST_DX = 8;
	public static final byte MINUS_TEST_DY = 8;

	// sequence data
	public static final int MASK_FLAG = 0x00000001; // 1
	public static final int MASK_DURATION = 0x0000001F; // 5
	public static final int MASK_FRAMEID = 0x000003FF; // 10
	public static final byte MOVE_BIT_FLAG = 15;
	public static final byte MOVE_BIT_DURATION = 10;
	public static final byte MOVE_BIT_FRAMEID = 0;

	// newModule package mask
	public static final int MASK_MLGID = 0x0000FFFF; // 16
	public static final int MASK_OFFSETID = 0x0000FFFF; // 16
	public static final byte MOVE_BIT_MLGID = 16;
	public static final byte MOVE_BIT_OFFSETID = 0;

	// Action Squence Controller: 人物action的帧索引, 帧延迟的临时计数
	public static final short ASC_SQUENCE_INDEX = 0;
	public static final short ASC_FRAME_DELAY = 1;

	/****************************************************************************
	 * about box
	 ***************************************************************************/
	public static final byte BOX_COLLIDE = 1;
	public static final byte BOX_ATTACK = 2;

	private static short[] attBox = new short[4];
	private static short[] colBox = new short[4];

	public void setAniPlayFlag(int flag) {
		aniPlayFlag |= flag;
	}

	public boolean testAniPlayFlag(int flag) {
		return (aniPlayFlag & flag) == flag;
	}

	public void clearAniPlayFlag(int flag) {
		aniPlayFlag &= ~flag;
	}

	public void clear() {
		clearAniPlayFlag(FLAG_ACTION_ALL);
		actionSequenceDuration = 0;
		actionSequenceID = 0;
		extDuration = 0;
		flipX = 0;
	}

	/*************
	 * 设置动画
	 *************/
	public void setAnimAction(int actID, int sqID, int flag) {
		setAniPlayFlag(flag);
		setAnimAction(actID);
		actionSequenceID = getSquenceCount(actID) - 1;
	}

	/**
	 * 设置动画的动作状态
	 * 
	 * @param actionID
	 *            int the new anim action id
	 */
	public boolean setAnimAction(int actID) {
		if (actID < 0) {
			return false;
		}

		m_tickAction = 0;

		actionID = actID;
		/**
		 * 检测是否倒着播放
		 */
		if (testAniPlayFlag(FLAG_ACTION_REVERSE)) {
			actionSequenceID = aniData.actionSequenceCounts[actionID] - 1;
		} else {
			actionSequenceID = 0;
		}
		/**
		 * 检测是否停
		 */
		if (testAniPlayFlag(FLAG_ACTION_HOLD)) {
			actionSequenceDuration = -1;
		} else {
			actionSequenceDuration = 0;
		}

		clearAniPlayFlag(FLAG_ACTION_OVER);

		grabBoxesInfo();

		// if ( testAniPlayFlag ( FLAG_ACTION_GRAB_MECH_INFO ) )
		// {
		// m_modvX = m_modvY = 0;
		// grabMechInfo ();
		// }

		// setAniPlayFlag ( FLAG_ACTION_GRAB_MECH_INFO );
		return true;

	}

	public void grabBoxesInfo() {

		short[] colBox = getBoxesInfo(BOX_COLLIDE, actionID, actionSequenceID);
		short[] attBox = getBoxesInfo(BOX_ATTACK, actionID, actionSequenceID);
		// ----------------------------------------------------------------
		setActorBoxInfo(INDEX_COLLISION_BOX_TOP, colBox[1]);
		setActorBoxInfo(INDEX_COLLISION_BOX_BOTTOM, colBox[3]);

		setActorBoxInfo(INDEX_ATTACK_BOX_TOP, attBox[1]);
		setActorBoxInfo(INDEX_ATTACK_BOX_BOTTOM, attBox[3]);

		setActorBoxInfo(INDEX_COLLISION_BOX_Y_CENTER,
				(colBox[1] + colBox[3]) >> 1);

		if (getSpriteFlipX() == -1) {
			setActorBoxInfo(INDEX_COLLISION_BOX_LEFT, -colBox[2]);
			setActorBoxInfo(INDEX_COLLISION_BOX_RIGHT, -colBox[0]);

			setActorBoxInfo(INDEX_ATTACK_BOX_LEFT, -attBox[2]);
			setActorBoxInfo(INDEX_ATTACK_BOX_RIGHT, -attBox[0]);

			setActorBoxInfo(INDEX_COLLISION_BOX_FRONT, -colBox[2]);
			setActorBoxInfo(INDEX_COLLISION_BOX_BACK, -colBox[0]);

			setActorBoxInfo(INDEX_COLLISION_BOX_X_CENTER,
					-((colBox[2] + colBox[0]) >> 1));

		} else {
			setActorBoxInfo(INDEX_COLLISION_BOX_LEFT, colBox[0]);
			setActorBoxInfo(INDEX_COLLISION_BOX_RIGHT, colBox[2]);

			setActorBoxInfo(INDEX_ATTACK_BOX_LEFT, attBox[0]);
			setActorBoxInfo(INDEX_ATTACK_BOX_RIGHT, attBox[2]);

			setActorBoxInfo(INDEX_COLLISION_BOX_FRONT, colBox[2]);
			setActorBoxInfo(INDEX_COLLISION_BOX_BACK, colBox[0]);

			setActorBoxInfo(INDEX_COLLISION_BOX_X_CENTER,
					(colBox[2] + colBox[0]) >> 1);

		}

	}

	public void setActorBoxInfo(int index, int boxData) {
		boxInf[index] = (short) boxData;
	}

	/**
	 * 
	 * @param orientation
	 *            int 1:不翻转；-1翻转
	 */
	public void setSpriteFlipX(int orientation) {
		this.flipX = orientation;
	}

	public int getSpriteFlipX() {
		return flipX;
	}

	/**
	 * 获得指定动作序列帧(Squenceframe)数量
	 * 
	 * @param actionID
	 *            int
	 * @return int
	 */
	public int getSquenceframeCount(int actionID) {
		int count = 0;
		for (int i = 0; i < aniData.actionSequenceCounts[actionID]; i++) {
			count += ((aniData.sequenceDatas[aniData.actionSequenceOffset[actionID]
					+ (i << 1)] >> MOVE_BIT_DURATION) & MASK_DURATION);
		}
		return count;
	}

	/**
	 * 获得指定动作控制器的序列帧(Squenceframe)索引
	 * 
	 * @param actionID
	 *            int
	 * @param asc
	 *            short[]
	 * @return int
	 */
	public int getSquenceframeIndex(int actionID, short[] asc) {
		int count = 0;
		for (int i = 0; i < asc[ASC_SQUENCE_INDEX]; i++) {
			count += ((aniData.sequenceDatas[aniData.actionSequenceOffset[actionID]
					+ (i << 1)] >> MOVE_BIT_DURATION) & MASK_DURATION);
		}
		count += asc[ASC_FRAME_DELAY];
		return count;
	}

	/**
	 * 动作的执行到最后一个 序列帧(Squenceframe)
	 * 
	 * @param actionID
	 *            int
	 * @param asc
	 *            short[]
	 * @return boolean
	 */
	public boolean isOnLastSquenceframe(int actionID, short[] asc) {
		int count = getSquenceframeCount(actionID);
		int idx = getSquenceframeIndex(actionID, asc);
		return idx == count - 1;
	}

	/**
	 * 获得指定动作的squence count
	 * 
	 * @param actionID
	 *            int
	 * @return int
	 */
	public int getSquenceCount(int actionID) {
		return aniData.actionSequenceCounts[actionID];
	}

	/**
	 * 获得指定动作和指定序列的delay count
	 * 
	 * @param actionID
	 *            int
	 * @param squIdx
	 *            int
	 * @return int
	 */
	public int getDelayCount(int actionID, int squIdx) {
		return ((aniData.sequenceDatas[aniData.actionSequenceOffset[actionID]
				+ (squIdx << 1)] >> MOVE_BIT_DURATION) & MASK_DURATION);
	}

	/**
	 * 注意：获得 box 是一个静态缓冲, 调用者要谨慎使用!!!!
	 * 
	 * @param type
	 *            int
	 * @param actionID
	 *            int
	 * @param actionSquenceIndex
	 *            int
	 * @return byte[]
	 */
	public short[] getBoxesInfo(byte type, int actionID, int asc) {
		int frame = aniData.sequenceDatas[aniData.actionSequenceOffset[actionID]
				+ (asc << 1)] >> MOVE_BIT_FRAMEID
				& MASK_FRAMEID;
		int offset = 0;
		switch (type) {
		case BOX_COLLIDE:
			offset = (aniData.frameBoxIndexs[frame << 1] & 0xff) << 2;
			System.arraycopy(aniData.colBoxs, offset, colBox, 0, 4);
			return colBox;
		case BOX_ATTACK:
			offset = (aniData.frameBoxIndexs[(frame << 1) + 1] & 0xff) << 2;
			System.arraycopy(aniData.attBoxs, offset, attBox, 0, 4);
			return attBox;
		}
		return null;
	}

	public byte[] grabMechInfo() {
		byte[] mechModel = aniData.mechModels;
		int offset = aniData.actionMechModelIndexs[actionID] * 5;
		byte[] objectV = new byte[4];
		System.arraycopy(mechModel, offset, objectV, 0, objectV.length);
		return objectV;
	}

	/**
	 * 绘制带有套信息
	 * 
	 * @param g
	 *            Graphics
	 * @param actionID
	 *            int
	 * @param actionSquenceIndex
	 *            int
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param flipX
	 *            boolean
	 * @param mlgInfo
	 *            short[]
	 */
	public void drawFrame(Graphics g, int actionID, int actionSquenceIndex,
			int x, int y, boolean flipX, short[] mlgInfo) {
		// 设置当前的mlg
		short[] info = (mlgInfo == null) ? aniData.defaultMLGInfo : mlgInfo;
		for (int iIdx, pIdx, i = 0; i < aniData.mlgCount; i++) {
			iIdx = (info[i] >> 8) & 0xff;
			pIdx = info[i] & 0xff;
			this.aniData.curMlgs[i] = (iIdx == 0) ? this.aniData.mlgs[i]
					: this.aniData.mappingInfo[i][iIdx - 1];
			AniData.aniMLGs[this.aniData.curMlgs[i]].setMapping(
					AniData.aniMLGs[this.aniData.mlgs[i]], pIdx);
		}
		drawFrameWithNoSuit(g, actionID, actionSquenceIndex, x, y, flipX);
	}

	/**
	 * 绘制动画的一个祯
	 * 
	 * @param frameIndex
	 *            int
	 * @param screenX
	 *            int
	 * @param screenY
	 *            int
	 * @param flipX
	 *            boolean
	 */
	public void drawFrameWithNoSuit(Graphics g, int actionID,
			int actionSquenceIndex, int x, int y, boolean flipX) {
		ContractionMLG mlg;
		int spriteIndex;
		int moduleID, deltaX, deltaY, flip;
		int MLGID, offsetID;

		int frameID = ((aniData.sequenceDatas[aniData.actionSequenceOffset[actionID]
				+ (actionSquenceIndex << 1)] >> MOVE_BIT_FRAMEID) & MASK_FRAMEID);
		for (int i = 0; i < aniData.frameSpriteCounts[frameID]; ++i) {
			spriteIndex = aniData.frameSpriteOffset[frameID] + i;
			// module info
			moduleID = ((aniData.spritesDatas[spriteIndex] >> MOVE_BIT_MODULEID) & MASK_MODULEID);
			deltaX = ((aniData.spritesDatas[spriteIndex] >> MOVE_BIT_DX) & MASK_DX);
			deltaY = (aniData.spritesDatas[spriteIndex] & MASK_DY);
			flip = ((aniData.spritesDatas[spriteIndex] >> MOVE_BIT_FLIP) & MASK_FLIP);
			if ((deltaX & (1 << MINUS_TEST_DX)) != 0) {
				deltaX |= ~MASK_DX;
			}
			if ((deltaY & (1 << MINUS_TEST_DY)) != 0) {
				deltaY |= ~MASK_DY;
			}
			// mlg info
			MLGID = (this.aniData.modules[moduleID] >> MOVE_BIT_MLGID)
					& MASK_MLGID;
			offsetID = this.aniData.modules[moduleID] & MASK_OFFSETID;

			// mlg
			mlg = aniData.getMLG(MLGID);
			if (mlg == null) {
				// debug
				if (CDebug.showDebugInfo) {
					System.out
							.println(">>Error: drawFrameWithNoSuit(), mlg = null, MLGID = "
									+ MLGID + ", moudleID = " + moduleID);
				}
				continue;
			}
			// frame flipX
			if (flipX) {
				flip = (flip == 0) ? (4) : ((flip == 4) ? (0)
						: ((flip == 2) ? (5) : ((flip == 5) ? (2)
								: ((flip == 1) ? (6) : ((flip == 3) ? (7)
										: (flip))))));
				// module的偏移量是左上点的偏移量,所以必须这样麻烦的操作
				if (flip == 6 || flip == 7) {
					deltaX = -deltaX - mlg.getModuleHeight(offsetID);
				} else {
					deltaX = -deltaX - mlg.getModuleWidth(offsetID);
				}
			}
			// draw a module
			mlg.drawModule(g, offsetID, x + deltaX, y + deltaY,
					com.mglib.def.dG.ANCHOR_LT, flip);
		}
	}

	/**
	 * 当前动作当前帧是否为攻击帧
	 * 
	 * @param actionID
	 *            int
	 * @param actionSquenceIndex
	 *            int
	 * @return boolean
	 */
	public boolean isAttackFrame(int actionID, int actionSquenceIndex) {
		int keyFrameInfo = aniData.sequenceDatas[aniData.actionSequenceOffset[actionID]
				+ ((actionSquenceIndex << 1) + 1)];
		return keyFrameInfo != 0;
	}

	/**
	 * 获得当前动作当前帧的伤害编号
	 * 
	 * @param actionID
	 *            int
	 * @param actionSquenceIndex
	 *            int
	 * @return int
	 */
	public int getAttackFrameHurtID(int actionID, int actionSquenceIndex) {
		int keyFrameInfo = aniData.sequenceDatas[aniData.actionSequenceOffset[actionID]
				+ ((actionSquenceIndex << 1) + 1)];
		return keyFrameInfo & 0x000f;
	}

	/**
	 * 获得当前动作当前帧攻击到跳帧数量
	 * 
	 * @param actionID
	 *            int
	 * @param actionSquenceIndex
	 *            int
	 * @return int
	 */
	public int getAttackFrameSkipNum(int actionID, int actionSquenceIndex) {
		int keyFrameInfo = aniData.sequenceDatas[aniData.actionSequenceOffset[actionID]
				+ ((actionSquenceIndex << 1) + 1)];
		return (keyFrameInfo & 0x00f0) >> 4;
	}

	/**
	 * 获得当前动作当前帧攻击位移
	 * 
	 * @param actionID
	 *            int
	 * @param actionSquenceIndex
	 *            int
	 * @return int
	 */
	public int getAttackFrameMoveDistance(int actionID, int actionSquenceIndex) {
		int keyFrameInfo = aniData.sequenceDatas[aniData.actionSequenceOffset[actionID]
				+ ((actionSquenceIndex << 1) + 1)];
		return (keyFrameInfo & 0x0f00) >> 8;
	}

	/**
	 * 获得当前动作当前帧 保留信息
	 * 
	 * @param actionID
	 *            int
	 * @param actionSquenceIndex
	 *            int
	 * @return int
	 */
	public int getAttackFrameReserve(int actionID, int actionSquenceIndex) {
		int keyFrameInfo = aniData.sequenceDatas[aniData.actionSequenceOffset[actionID]
				+ ((actionSquenceIndex << 1) + 1)];
		return (keyFrameInfo & 0xf000) >> 12;
	}

	public static boolean useSlowMotion;
	public static final byte Slow_Motion_Ratio = 3;

	public static int getTimerStep() {
		if (useSlowMotion == true) {
			return 1;
		}
		return Slow_Motion_Ratio;
	}

	/**
	 * This method will update sprite to next frame
	 */
	public void updateAnimation() {
		if (/* animationID < 0 || */actionID < 0) {
			return;
		}
		// AniPlayer anim = ResLoader.animations[animationID];
		// 如果动画非倒着播放
		if (!testAniPlayFlag(FLAG_ACTION_REVERSE)) {
			if (!testAniPlayFlag(FLAG_ACTION_HOLD)) {
				actionSequenceDuration += getTimerStep();
			} else {
				setAniPlayFlag(FLAG_ACTION_OVER);
			}
			// 取得当前帧持续的时间　
			int offset = aniData.actionSequenceOffset[actionID];
			try {
				if (actionSequenceDuration < ((aniData.sequenceDatas[offset
						+ (actionSequenceID << 1)] >> MOVE_BIT_DURATION & MASK_DURATION) + extDuration)
						* Slow_Motion_Ratio) {
					return;
				}
				extDuration = 0;
			} catch (Exception e) {
				e.printStackTrace();
				// #if mode_debug
				System.out.println("***m_actionID=" + actionID);
				System.out.println("m_actionSequenceID =" + actionSequenceID);
				System.out.println("offset =" + offset);
				System.out.println("count="
						+ aniData.actionSequenceCounts[actionID]);
				// #endif
			}
			actionSequenceDuration = 0;
			//
			++actionSequenceID;

			if (actionSequenceID < aniData.actionSequenceCounts[actionID]) {
				grabBoxesInfo();
				return;
			}

			setAniPlayFlag(FLAG_ACTION_OVER);

			if (testAniPlayFlag(FLAG_ACTION_NOT_CYCLE)) {
				--actionSequenceID;
				return;
			}
			actionSequenceID = 0;
			grabBoxesInfo();
		} else {

			// 如果不是保持某一帧
			if (!testAniPlayFlag(FLAG_ACTION_HOLD)) {
				actionSequenceDuration += getTimerStep();
			} else {
				setAniPlayFlag(FLAG_ACTION_OVER);
			}

			// 取得当前帧持续的时间　
			int offset = aniData.actionSequenceOffset[actionID];
			try {
				if (actionSequenceDuration < ((aniData.sequenceDatas[offset
						+ (actionSequenceID << 1)] >> MOVE_BIT_DURATION & MASK_DURATION) + extDuration)
						* Slow_Motion_Ratio) {
					return;
				}
				extDuration = 0;
			} catch (Exception e) {
				e.printStackTrace();
				// #if mode_debug
				System.out.println("***m_actionID=" + actionID
						+ "$$$m_actorID==");
				System.out.println("m_actionSequenceID =" + actionSequenceID);
				System.out.println("offset =" + offset);
				System.out.println("count="
						+ aniData.actionSequenceCounts[actionID]);
				// #endif
			}

			actionSequenceDuration = 0;
			//
			--actionSequenceID;

			if (actionSequenceID >= 0) {
				grabBoxesInfo();
				return;
			}

			setAniPlayFlag(FLAG_ACTION_OVER);

			if (testAniPlayFlag(FLAG_ACTION_NOT_CYCLE)) {
				++actionSequenceID;
				return;
			}
			actionSequenceID = aniData.actionSequenceCounts[actionID];
			grabBoxesInfo();

		}

	}

}
