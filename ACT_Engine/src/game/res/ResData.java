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
	 * 加载全局的数据信息
	 *******************/
	public void loadGlobalData();

	/*******************
	 * 加载关卡的数据信息
	 * 
	 * @param newLevel
	 *            int
	 */
	public void loadScene(int newLevel);

	public void loadOptionDlgData();

}
