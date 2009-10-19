package game.object;

import game.res.ResLoader;

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
public class CNpc extends CObject {
	public CNpc() {
	}

	public boolean update() {
		if (super.update()) {
			int aiID = ResLoader.classAIIDs[m_classID];
			switch (aiID) {
			case dActorClass.CLASS_ID_TRAILERCAMERA:
				ai_TrailerCamera();
				break;
			}
		}
		return true;
	}

	//
	// public byte[][] getSaveInfo () {
	// return new byte[][] {SAVED_INFO_DEFAULT, {}
	// };
	// }

	public void paint(Graphics g) {
		super.paint(g);
	}

}
