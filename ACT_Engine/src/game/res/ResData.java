package game.res;

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
public interface ResData {

	/********************
	 * ����ȫ�ֵ�������Ϣ
	 *******************/
	public void loadGlobalData();

	/*******************
	 * ���عؿ���������Ϣ
	 * 
	 * @param newLevel
	 *            int
	 */
	public void loadScene(int newLevel);

	public void loadOptionDlgData();

}
