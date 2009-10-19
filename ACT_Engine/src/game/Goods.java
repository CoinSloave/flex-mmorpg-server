/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

package game;

import com.mglib.ui.Data;

public class Goods {

	public Goods() {
	}

	public static final byte TYPE_EQUIP = 0;
	public static final byte TYPE_ITEM = 1;
	public static final byte TYPE_SKILL = 2;
	public static final byte TYPE_MONEY = 3;

	public static final byte PRO_LENGTH = 6;
	// 武器本身的素质决定的部分
	public static final byte PRO_TYPE = 0; // 物品的种类：0：装备 1：物品 2：技能
	public static final byte PRO_ID = 1; // 数据的ID索引
	public static final byte PRO_COUNT = 2; // 物品的数量
	public static final byte PRO_KEY_ID = 3; // 物品List的Key

	public static final byte PRO_LEVEL = 4; // 对应装备，表示等级；对应技能，表示主角已投入几个技能点
	public static final byte PRO_MAX_LEVEL = 5; // 装备的等级上限

	public short[] property = new short[Goods.PRO_LENGTH]; // 属性

	// 对主角属性的影响
	public short[] affectProperty = new short[12];

	public String description;

	/***************************************
	 * 基本公共定义
	 **************************************/
	/**
	 * 取得物品的大类型
	 * 
	 * @return short
	 */
	public short getType() {
		return property[PRO_TYPE];
	}

	/**
	 * 获取数据ID
	 * 
	 * @return short
	 */
	public short getDataID() {
		return property[PRO_ID];
	}

	/**
	 * 获取物品数量
	 * 
	 * @return short
	 */
	public short getCount() {
		return property[PRO_COUNT];
	}

	/**
	 * 取得物品的名称
	 * 
	 * @return String
	 */
	public String getName() {
		switch (property[PRO_TYPE]) {
		case TYPE_EQUIP:
			return Data.STR_EQUIP_NAMES[property[PRO_ID]][0];
		case TYPE_ITEM:
			return Data.STR_GOODS_NAMES[property[PRO_ID]][0];
		case TYPE_SKILL:
			return Data.STR_SKILL_NAMES[property[PRO_ID]][0];
		default:
			return "未命名";
		}
	}

	/**
	 * 取得物品的详细描述
	 * 
	 * @return String
	 */
	public String getDescParticular() {
		switch (property[PRO_TYPE]) {
		case TYPE_EQUIP:
			// if (getDetailType()==Data.IDX_EP_weapon) {
			return getEquipDecription();
			// }
			// else{
			// return Data.STR_EQUIP_NAMES[property[PRO_ID]][1];
			// }
		case TYPE_ITEM:
			return Data.STR_GOODS_NAMES[property[PRO_ID]][1];
		case TYPE_SKILL:
			return Data.STR_SKILL_NAMES[property[PRO_ID]][1];
		default:
			return "无描述";
		}
	}

	/**
	 * 取得物品的简单描述
	 * 
	 * @return String
	 */
	public String getDescPredigest() {
		switch (property[PRO_TYPE]) {
		case TYPE_EQUIP:
			return Data.STR_EQUIP_NAMES[property[PRO_ID]][2];

		case TYPE_ITEM:
			return Data.STR_GOODS_NAMES[property[PRO_ID]][2];
		case TYPE_SKILL:
			return Data.STR_SKILL_NAMES[property[PRO_ID]][2];
		default:
			return "无描述";
		}
	}

	/**
	 * 取得物品的价格
	 * 
	 * @return short
	 */
	public short getPrice() {
		switch (property[PRO_TYPE]) {
		case TYPE_EQUIP:
			return Data.EQUIP_INFO[property[PRO_ID]][Data.IDX_EI_price];
		case TYPE_ITEM:
			return Data.GOODS_INFO[property[PRO_ID]][Data.IDX_GI_price];
		case TYPE_SKILL:
			return Data.SKILL_INFO[property[PRO_ID]][Data.IDX_SKI_price];
		default:
			return -1;
		}
	}

	/**
	 * 取得物品的详细类型
	 * 
	 * @return short
	 */
	public short getDetailType() {
		switch (property[PRO_TYPE]) {
		case TYPE_EQUIP:
			return Data.EQUIP_INFO[property[PRO_ID]][Data.IDX_EI_part];
		case TYPE_ITEM:
			return Data.GOODS_INFO[property[PRO_ID]][Data.IDX_GI_type];
		case TYPE_SKILL:
			return Data.SKILL_INFO[property[PRO_ID]][Data.IDX_SKI_type];
		default:
			return -1;
		}
	}

	/**
	 * 取得UI呈现ID
	 * 
	 * @return short
	 */
	public short getIconID() {
		switch (property[PRO_TYPE]) {
		case TYPE_EQUIP:
			return Data.EQUIP_INFO[property[PRO_ID]][Data.IDX_EI_icon];
		case TYPE_ITEM:
			return Data.GOODS_INFO[property[PRO_ID]][Data.IDX_GI_icon];
		case TYPE_SKILL:
			return Data.SKILL_INFO[property[PRO_ID]][Data.IDX_SKI_icon];
		case TYPE_MONEY:
			return 0;
		default:
			return -1;
		}
	}

	/***************************************
	 * 重写父类方法
	 **************************************/
	public boolean equals(Object o) {
		if (!(o.getClass() != this.getClass())) {
			return false;
		}
		return this == o || this.getName().equals(((Goods) o).getName());
	}

	// public String toString()
	// {
	// String strType;
	// switch ( this.getType () )
	// {
	// case Goods.TYPE_EQUIP:
	// strType = "装备";
	// break;
	// case Goods.TYPE_ITEM:
	// strType = "物品";
	// break;
	// case Goods.TYPE_SKILL:
	// strType = "技能";
	// break;
	// default:
	// strType = "未知大类型";
	// break;
	// }
	// return "Type=" + strType + ", dataID=" + this.getDataID() + ", name=" +
	// this.getName() + ", desc=" + this.getDescParticular();
	// }

	/***************************************
	 * 基础信息定义
	 **************************************/
	/**
	 * 取得每个类型物品具体info信息
	 * 
	 * @param index
	 *            int ：是和Data关联的数据索引
	 * @return short
	 */
	public short getInfo(int index) {
		switch (property[PRO_TYPE]) {
		case TYPE_EQUIP:
			return Data.EQUIP_INFO[property[PRO_ID]][index];
		case TYPE_ITEM:
			return Data.GOODS_INFO[property[PRO_ID]][index];
		case TYPE_SKILL:
			return Data.SKILL_INFO[property[PRO_ID]][index];
		default:
			return -1;
		}
	}

	/**
	 * rpg使用 取得五行属性
	 * 
	 * @return short[]
	 */
	public short[] getFivePro() {
		switch (property[PRO_TYPE]) {
		case TYPE_EQUIP:
			return Data.EQUIP_FIVE_PROPERTY[property[PRO_ID]];
		case TYPE_ITEM:
			return null;
		case TYPE_SKILL:
			return Data.SKILL_FIVE_PROPERTY[property[PRO_ID]];
		default:
			return null;
		}
	}

	/**
	 * 取得物品的影响属性数组
	 * 
	 * @return short[]
	 */
	public short[] getAffectedPro() {
		switch (property[PRO_TYPE]) {
		case TYPE_EQUIP:
			// return Data.EQUIP_AFFECTED_PROPERTY[property[PRO_ID]];
			return affectProperty;
		case TYPE_ITEM:
			return Data.GOODS_AFFECTED_PROPERTY[property[PRO_ID]];
		case TYPE_SKILL:
			// return Data.SKILL_AFFECTED_PROPERTY[property[PRO_ID]];
			return affectProperty;
		default:
			return null;
		}
	}

	/**
	 * 取得物品的影响状态数组
	 * 
	 * @return short[]
	 */
	public short[] getAffectedState() {
		switch (property[PRO_TYPE]) {
		case TYPE_EQUIP:
			return null;
		case TYPE_ITEM:
			return Data.GOODS_AFFECTED_STATE[property[PRO_ID]];
		case TYPE_SKILL:
			return Data.SKILL_AFFECTED_PROPERTY[property[PRO_ID]];
		default:
			return null;
		}
	}

	/***************************************
	 * 静态接口定义
	 **************************************/
	/**
	 * 创建一个Goods对象，并初始化
	 * 
	 * @param type
	 *            int 物品类型
	 * @param id
	 *            int 物品ID
	 * @return Goods
	 */
	public static Goods createGoods(final short type, short id) {
		if (id < 0 || type < 0) {
			return null;
		}
		Goods goods = new Goods();
		goods.property[PRO_TYPE] = type;
		goods.property[PRO_ID] = id;
		//
		goods.property[PRO_KEY_ID] = (short) ((type << 12) | (id));

		// 武器、装备，初始化affectProperty
		if (goods.property[PRO_TYPE] == TYPE_EQUIP) {
			System.arraycopy(
					Data.EQUIP_AFFECTED_PROPERTY[goods.property[PRO_ID]], 0,
					goods.affectProperty, 0, goods.affectProperty.length);
			goods.property[PRO_LEVEL] = 1;
			goods.property[PRO_MAX_LEVEL] = 3; // 水浒中，装备只能升级2次
		} else if (goods.property[PRO_TYPE] == TYPE_SKILL) {
			System.arraycopy(
					Data.SKILL_AFFECTED_PROPERTY[goods.property[PRO_ID]], 0,
					goods.affectProperty, 0, goods.affectProperty.length);
		}
		return goods;
	}

	public short getKey() {
		return property[PRO_KEY_ID];
	}

	// public static short getKey(int id)
	// {
	//
	// }

	// public static short getKey(Goods g)
	// {
	//
	// }

	/**
	 * 判断2个物品是不是一样的
	 * 
	 * @param g0
	 *            Goods
	 * @param g1
	 *            Goods
	 * @return boolean
	 */
	public static final boolean isTheSame(Goods g0, Goods g1) {
		return g0.getName().equals(g1.getName());
	}

	/***************************************
	 * 商店数据
	 **************************************/
	/**
	 * 过的指定武器店的装备
	 * 
	 * @param shopID
	 *            byte
	 * @return Goods[]:装备店里的装备
	 */
	public static final Goods[] getShopGoods(byte shopID) {
		int ec = Data.DEALER_EQUIP[shopID].length;
		int gc = Data.DEALER_GOODS[shopID].length;
		int sc = Data.DEALER_SKILL[shopID].length;
		int index = 0;
		Goods[] goods = new Goods[ec + gc + sc];
		for (int i = 0; i < ec; i++) {
			goods[index++] = Goods.createGoods(Goods.TYPE_EQUIP,
					Data.DEALER_EQUIP[shopID][i]);
		}
		for (int i = 0; i < gc; i++) {
			goods[index++] = Goods.createGoods(Goods.TYPE_ITEM,
					Data.DEALER_GOODS[shopID][i]);
		}
		for (int i = 0; i < sc; i++) {
			goods[index++] = Goods.createGoods(Goods.TYPE_SKILL,
					Data.DEALER_SKILL[shopID][i]);
		}
		return goods;
	}

	public static final Goods[] getShopGoodsType(byte shopID, byte Type) {
		int ec = Data.DEALER_EQUIP[shopID].length;
		int gc = Data.DEALER_GOODS[shopID].length;
		int sc = Data.DEALER_SKILL[shopID].length;
		int index = 0;
		Goods[] goods;
		switch (Type) {
		case TYPE_EQUIP:
			goods = new Goods[ec];
			for (int i = 0; i < ec; i++) {
				goods[index++] = Goods.createGoods(Goods.TYPE_EQUIP,
						Data.DEALER_EQUIP[shopID][i]);
			}
			return goods;
		case TYPE_ITEM:
			goods = new Goods[gc];
			for (int i = 0; i < gc; i++) {
				goods[index++] = Goods.createGoods(Goods.TYPE_ITEM,
						Data.DEALER_GOODS[shopID][i]);
			}
			return goods;
		case TYPE_SKILL:
			goods = new Goods[sc];
			for (int i = 0; i < sc; i++) {
				goods[index++] = Goods.createGoods(Goods.TYPE_SKILL,
						Data.DEALER_SKILL[shopID][i]);
			}
			return goods;
		}
		return null;
	}

	/**
	 * 返回折扣率
	 * 
	 * @param shopID
	 *            byte
	 * @return int
	 */
	public static final int getShopAgio(byte shopID) {
		return Data.DEALER_INFO[shopID][0];
	}

	/**
	 * 装备升级
	 * 
	 * @return int[] 属性的变化
	 */
	public short[] levelUp() {
		if (property[PRO_LEVEL] >= property[PRO_MAX_LEVEL]) {
			return null;
		}
		property[PRO_LEVEL] += 1;
		// 升级前属性
		short[] propertyChange = new short[12];
		System.arraycopy(affectProperty, 0, propertyChange, 0,
				affectProperty.length);
		switch (getDetailType()) {
		// 武器只加攻击
		case Data.IDX_EP_weapon:
			affectProperty[Data.IDX_PRO_atk] += 6;
			break;
		// 护甲只加防御
		case Data.IDX_EP_loricae:
			affectProperty[Data.IDX_PRO_def] += 4;
			break;
		// 饰品只加暴击
		case Data.IDX_EP_jade:
			affectProperty[Data.IDX_PRO_cri_rate] += 5;
			break;
		}
		// 返回变化值
		for (int i = 0; i < propertyChange.length; i++) {
			propertyChange[i] = (short) (affectProperty[i] - propertyChange[i]);
		}
		return propertyChange;
	}

	/**
	 * 升级失败，降回1级！
	 */
	short[] levelDown() {
		property[PRO_LEVEL] = 1;
		short[] proChange = new short[12];
		System.arraycopy(affectProperty, 0, proChange, 0, proChange.length);
		System.arraycopy(Data.EQUIP_AFFECTED_PROPERTY[property[PRO_ID]], 0,
				affectProperty, 0, affectProperty.length);
		for (int i = 0; i < proChange.length; i++) {
			proChange[i] = (short) (affectProperty[i] - proChange[i]);
		}
		return proChange;
	}

	/**
	 * 升到某级需要的宝石数量
	 * 
	 * @return int
	 */
	int getRequiredStoneNum(int newLevel) {
		return 1;
	}

	/**
	 * 装备升级需要的宝石id
	 * 
	 * @return int
	 */
	public int getRequiredStoneId() {
		int id = 12;
		switch (getDetailType()) {
		// 武器只加攻击
		case Data.IDX_EP_weapon:
			id = 12;
			break;
		// 护甲只加防御
		case Data.IDX_EP_loricae:
			id = 13;
			break;
		// 饰品只加暴击
		case Data.IDX_EP_jade:
			id = 14;
			break;
		}
		return id;
	}

	/**
	 * 升到某级的成功率
	 * 
	 * @param newLevel
	 *            int
	 * @return int
	 */
	static int getSuccessProbability(int newLevel) {
		int probability = 0;
		if (newLevel == 2) {
			probability = 60;
		} else {
			probability = 30;
		}
		return probability;
	}

	/**
	 * 取得武器的描述
	 * 
	 * @return String
	 */
	String getEquipDecription() {
		StringBuffer sf = new StringBuffer();
		sf.append("需要等级 " + getInfo(Data.IDX_EI_level));
		if (affectProperty[Data.IDX_PRO_atk] > 0) {
			sf.append("&攻击+ " + affectProperty[Data.IDX_PRO_atk]);
		}
		if (affectProperty[Data.IDX_PRO_def] > 0) {
			sf.append("&防御+ " + affectProperty[Data.IDX_PRO_def]);
		}
		if (affectProperty[Data.IDX_PRO_cri_rate] > 0) {
			sf.append("&暴击+ " + affectProperty[Data.IDX_PRO_cri_rate] + "%");
		}
		if (affectProperty[Data.IDX_PRO_maxHp] > 0) {
			sf.append("&生命+ " + affectProperty[Data.IDX_PRO_maxHp]);
		}
		if (affectProperty[Data.IDX_PRO_maxMp] > 0) {
			sf.append("&魔法+ " + affectProperty[Data.IDX_PRO_maxMp]);
		}
		return sf.toString();
	}

	/**
	 * 当前技能的下一级
	 * 
	 * @return int
	 */
	int getNextSkill() {
		if (getType() != TYPE_SKILL) {
			return -1;
		}
		return Data.SKILL_INFO[property[PRO_ID]][Data.IDX_SKI_upLvDest];
	}

	private boolean bHaveSetKey;

	/**
	 * 装备不堆叠，使用添加的到背包的次序作为key
	 */
	public void setKey(int index) {
		if (bHaveSetKey) {
			return;
		}
		bHaveSetKey = true;
		property[PRO_KEY_ID] = (short) index;
	}

}
