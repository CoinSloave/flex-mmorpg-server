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
	// ������������ʾ����Ĳ���
	public static final byte PRO_TYPE = 0; // ��Ʒ�����ࣺ0��װ�� 1����Ʒ 2������
	public static final byte PRO_ID = 1; // ���ݵ�ID����
	public static final byte PRO_COUNT = 2; // ��Ʒ������
	public static final byte PRO_KEY_ID = 3; // ��ƷList��Key

	public static final byte PRO_LEVEL = 4; // ��Ӧװ������ʾ�ȼ�����Ӧ���ܣ���ʾ������Ͷ�뼸�����ܵ�
	public static final byte PRO_MAX_LEVEL = 5; // װ���ĵȼ�����

	public short[] property = new short[Goods.PRO_LENGTH]; // ����

	// ���������Ե�Ӱ��
	public short[] affectProperty = new short[12];

	public String description;

	/***************************************
	 * ������������
	 **************************************/
	/**
	 * ȡ����Ʒ�Ĵ�����
	 * 
	 * @return short
	 */
	public short getType() {
		return property[PRO_TYPE];
	}

	/**
	 * ��ȡ����ID
	 * 
	 * @return short
	 */
	public short getDataID() {
		return property[PRO_ID];
	}

	/**
	 * ��ȡ��Ʒ����
	 * 
	 * @return short
	 */
	public short getCount() {
		return property[PRO_COUNT];
	}

	/**
	 * ȡ����Ʒ������
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
			return "δ����";
		}
	}

	/**
	 * ȡ����Ʒ����ϸ����
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
			return "������";
		}
	}

	/**
	 * ȡ����Ʒ�ļ�����
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
			return "������";
		}
	}

	/**
	 * ȡ����Ʒ�ļ۸�
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
	 * ȡ����Ʒ����ϸ����
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
	 * ȡ��UI����ID
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
	 * ��д���෽��
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
	// strType = "װ��";
	// break;
	// case Goods.TYPE_ITEM:
	// strType = "��Ʒ";
	// break;
	// case Goods.TYPE_SKILL:
	// strType = "����";
	// break;
	// default:
	// strType = "δ֪������";
	// break;
	// }
	// return "Type=" + strType + ", dataID=" + this.getDataID() + ", name=" +
	// this.getName() + ", desc=" + this.getDescParticular();
	// }

	/***************************************
	 * ������Ϣ����
	 **************************************/
	/**
	 * ȡ��ÿ��������Ʒ����info��Ϣ
	 * 
	 * @param index
	 *            int ���Ǻ�Data��������������
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
	 * rpgʹ�� ȡ����������
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
	 * ȡ����Ʒ��Ӱ����������
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
	 * ȡ����Ʒ��Ӱ��״̬����
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
	 * ��̬�ӿڶ���
	 **************************************/
	/**
	 * ����һ��Goods���󣬲���ʼ��
	 * 
	 * @param type
	 *            int ��Ʒ����
	 * @param id
	 *            int ��ƷID
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

		// ������װ������ʼ��affectProperty
		if (goods.property[PRO_TYPE] == TYPE_EQUIP) {
			System.arraycopy(
					Data.EQUIP_AFFECTED_PROPERTY[goods.property[PRO_ID]], 0,
					goods.affectProperty, 0, goods.affectProperty.length);
			goods.property[PRO_LEVEL] = 1;
			goods.property[PRO_MAX_LEVEL] = 3; // ˮ��У�װ��ֻ������2��
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
	 * �ж�2����Ʒ�ǲ���һ����
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
	 * �̵�����
	 **************************************/
	/**
	 * ����ָ���������װ��
	 * 
	 * @param shopID
	 *            byte
	 * @return Goods[]:װ�������װ��
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
	 * �����ۿ���
	 * 
	 * @param shopID
	 *            byte
	 * @return int
	 */
	public static final int getShopAgio(byte shopID) {
		return Data.DEALER_INFO[shopID][0];
	}

	/**
	 * װ������
	 * 
	 * @return int[] ���Եı仯
	 */
	public short[] levelUp() {
		if (property[PRO_LEVEL] >= property[PRO_MAX_LEVEL]) {
			return null;
		}
		property[PRO_LEVEL] += 1;
		// ����ǰ����
		short[] propertyChange = new short[12];
		System.arraycopy(affectProperty, 0, propertyChange, 0,
				affectProperty.length);
		switch (getDetailType()) {
		// ����ֻ�ӹ���
		case Data.IDX_EP_weapon:
			affectProperty[Data.IDX_PRO_atk] += 6;
			break;
		// ����ֻ�ӷ���
		case Data.IDX_EP_loricae:
			affectProperty[Data.IDX_PRO_def] += 4;
			break;
		// ��Ʒֻ�ӱ���
		case Data.IDX_EP_jade:
			affectProperty[Data.IDX_PRO_cri_rate] += 5;
			break;
		}
		// ���ر仯ֵ
		for (int i = 0; i < propertyChange.length; i++) {
			propertyChange[i] = (short) (affectProperty[i] - propertyChange[i]);
		}
		return propertyChange;
	}

	/**
	 * ����ʧ�ܣ�����1����
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
	 * ����ĳ����Ҫ�ı�ʯ����
	 * 
	 * @return int
	 */
	int getRequiredStoneNum(int newLevel) {
		return 1;
	}

	/**
	 * װ��������Ҫ�ı�ʯid
	 * 
	 * @return int
	 */
	public int getRequiredStoneId() {
		int id = 12;
		switch (getDetailType()) {
		// ����ֻ�ӹ���
		case Data.IDX_EP_weapon:
			id = 12;
			break;
		// ����ֻ�ӷ���
		case Data.IDX_EP_loricae:
			id = 13;
			break;
		// ��Ʒֻ�ӱ���
		case Data.IDX_EP_jade:
			id = 14;
			break;
		}
		return id;
	}

	/**
	 * ����ĳ���ĳɹ���
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
	 * ȡ������������
	 * 
	 * @return String
	 */
	String getEquipDecription() {
		StringBuffer sf = new StringBuffer();
		sf.append("��Ҫ�ȼ� " + getInfo(Data.IDX_EI_level));
		if (affectProperty[Data.IDX_PRO_atk] > 0) {
			sf.append("&����+ " + affectProperty[Data.IDX_PRO_atk]);
		}
		if (affectProperty[Data.IDX_PRO_def] > 0) {
			sf.append("&����+ " + affectProperty[Data.IDX_PRO_def]);
		}
		if (affectProperty[Data.IDX_PRO_cri_rate] > 0) {
			sf.append("&����+ " + affectProperty[Data.IDX_PRO_cri_rate] + "%");
		}
		if (affectProperty[Data.IDX_PRO_maxHp] > 0) {
			sf.append("&����+ " + affectProperty[Data.IDX_PRO_maxHp]);
		}
		if (affectProperty[Data.IDX_PRO_maxMp] > 0) {
			sf.append("&ħ��+ " + affectProperty[Data.IDX_PRO_maxMp]);
		}
		return sf.toString();
	}

	/**
	 * ��ǰ���ܵ���һ��
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
	 * װ�����ѵ���ʹ����ӵĵ������Ĵ�����Ϊkey
	 */
	public void setKey(int index) {
		if (bHaveSetKey) {
			return;
		}
		bHaveSetKey = true;
		property[PRO_KEY_ID] = (short) index;
	}

}
