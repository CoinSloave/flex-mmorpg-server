package com.mglib.mdl.ani;

import game.pak.Camera;

import javax.microedition.lcdui.Graphics;

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
public class AniPlayer extends Player {

	private int spriteX;
	private int spriteY;

	/**
	 * 
	 * @param spriteData
	 *            spriteData to be played
	 * @param spriteX
	 *            x position at which animation is to be played
	 * @param spriteY
	 *            y position at which animation is to be played
	 */
	public AniPlayer(AniData aData) {
		super(aData);
	}

	public AniPlayer(AniData aData, int spriteX, int spriteY) {
		super(aData);
		this.spriteX = spriteX;
		this.spriteY = spriteY;

	}

	public AniPlayer(AniData aData, int spriteX, int spriteY, int actionID) {
		super(aData);
		this.spriteX = spriteX;
		this.spriteY = spriteY;
		this.setAnimAction(actionID);

	}

	public void setSpritePos(int valx, int valy) {
		this.spriteX = valx;
		this.spriteY = valy;
	}

	public void setSpriteX(int val) {
		this.spriteX = val;
	}

	public void setSpriteY(int val) {
		this.spriteY = val;
	}

	// public void notifyStartOfAnimation ()
	// {
	// isPlaying = true;
	// }
	//
	// public void notifyEndOfAnimation ()
	// {
	// isPlaying = false;
	// }
	//
	// public boolean isPlaying ()
	// {
	// return isPlaying;
	// }

	public int getSpriteDrawX() {
		return spriteX;
	}

	public int getSpriteDrawY() {
		return spriteY;
	}

	public void updateSpritePositionBy(int xinc, int yinc) {
		spriteX += xinc;
		spriteY += yinc;
	}

	/**
	 * 绘制一帧，带场景镜头左上点偏移
	 * 
	 * @param g
	 *            Graphics
	 * @param mlgInfo
	 *            short[] ：换装信息
	 */
	public void drawFrame(Graphics g, short[] mlgInfo) {
		boolean flipX = getSpriteFlipX() == -1 ? true : false;
		drawFrame(g, actionID, actionSequenceID, spriteX - Camera.cameraLeft,
				spriteY - Camera.cameraTop, flipX, mlgInfo);
	}

	/**
	 * 绘制一帧，不带场景镜头左上点偏移
	 * 
	 * @param g
	 *            Graphics
	 */
	public void drawFrame(Graphics g) {
		boolean flipX = getSpriteFlipX() == -1 ? true : false;
		drawFrame(g, actionID, actionSequenceID, spriteX, spriteY, flipX, null);
	}

	// 残影计时
	public byte shadowTimer;
}
