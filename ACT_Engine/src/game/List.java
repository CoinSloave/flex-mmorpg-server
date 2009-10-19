package game;

import game.key.CKey;
import game.object.CHero;
import game.object.IObject;

import java.util.Enumeration;

import com.mglib.ui.Data;

/**
 * UI�е��б�
 */
public class List {
	private List() {
		items = new SimpleVector();
	}

	public static List listInstance = new List();

	// ÿ��list UI����ʾ����
	private static final byte[] SHOW_ROW = { 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 3,
			4, };
	// -----װ������
	public static final byte LIST_HERO_EQUIP = 0;// �������ϵ�װ��
	public static final byte LIST_WEAPON = 1; // ����
	public static final byte LIST_ARMOR = 2; // ����
	public static final byte LIST_JADE = 3; // ��Ʒ
	// -----��Ʒ����
	public static final byte LIST_POTION = 4; // ҩˮ
	public static final byte LIST_EQUIP = 5; // װ��
	public static final byte LIST_STONE = 6; // ��ʯ
	public static final byte LIST_SPECIAL = 7;// ����
	// -----�̵����
	public static final byte LIST_EQUIP_SHOP = 8; // װ���̵�
	public static final byte LIST_ITEM_SHOP = 9; // ��Ʒ�̵�
	// -----���ܡ�����
	public static final byte LIST_SKILL = 10;
	public static final byte LIST_QUEST = 11;
	public static final byte LIST_NUM = 12; // ����
	// �������list������
	private static byte[][] allIndex = new byte[LIST_NUM][2];

	private int listId;
	private int startIndex;
	private int cursorIndex;
	private SimpleVector items;

	/**
	 * ���������
	 * 
	 * @return boolean,�ƶ���
	 */
	public boolean navigate() {
		int starTemp = startIndex, cursorTemp = cursorIndex;
		if (CKey.isKeyPressed(CKey.GK_UP)) {
			if (cursorIndex > 0) {
				cursorIndex--;
			} else if (startIndex > 0) {
				startIndex--;
			} else {
				startIndex = Math.max(items.size() - SHOW_ROW[listId], 0);
				cursorIndex = items.size() - startIndex - 1;
			}

		} else if (CKey.isKeyPressed(CKey.GK_DOWN)) {
			if (cursorIndex < SHOW_ROW[listId] - 1 && cursorIndex < size() - 1) {
				cursorIndex++;
			} else if (startIndex < items.size() - SHOW_ROW[listId]) {
				startIndex++;
			} else {
				startIndex = cursorIndex = 0;
			}
		}
		return starTemp != startIndex || cursorTemp != cursorIndex;
	}

	/**
	 * ����list������
	 */
	public void update() {
		items.removeAllElements();
		Enumeration e = null;
		Goods g = null;
		CHero hero = CGame.m_hero;
		switch (listId) {
		// װ��������
		case LIST_HERO_EQUIP:
			Goods weapon = (Goods) hero.hsEquipList
					.get(hero.m_actorProperty[IObject.PRO_INDEX_WEAPON] + "");
			items.addElement(weapon);
			weapon = (Goods) hero.hsEquipList
					.get(hero.m_actorProperty[IObject.PRO_INDEX_LORICAE] + "");
			items.addElement(weapon);
			weapon = (Goods) hero.hsEquipList
					.get(hero.m_actorProperty[IObject.PRO_INDEX_JADE] + "");
			items.addElement(weapon);
			break;
		// ������
		case LIST_WEAPON:
			items.addElement(null);// ��һ����ж��
			e = CGame.m_hero.hsEquipList.keys();
			while (e.hasMoreElements()) {
				g = (Goods) hero.hsEquipList.get(e.nextElement());
				if (g.getDetailType() == Data.IDX_EP_weapon
				// ��������װ���Ĳ�����
						&& !hero.beenEquiped(g) && hero.canEquip(g)) {
					items.addElement(g);
				}
			}
			break;
		// ������
		case LIST_ARMOR:
			items.addElement(null);// ��һ����ж��
			e = hero.hsEquipList.keys();
			while (e.hasMoreElements()) {
				g = (Goods) hero.hsEquipList.get(e.nextElement());
				if (g.getDetailType() == Data.IDX_EP_loricae
				// ��������װ���Ĳ�����
						&& !hero.beenEquiped(g) && hero.canEquip(g)) {
					items.addElement(g);
				}
			}
			break;
		// ��Ʒ��
		case LIST_JADE:
			items.addElement(null);// ��һ����ж��
			e = CGame.m_hero.hsEquipList.keys();
			while (e.hasMoreElements()) {
				g = (Goods) hero.hsEquipList.get(e.nextElement());
				if (g.getDetailType() == Data.IDX_EP_jade
				// ��������װ���Ĳ�����
						&& !hero.beenEquiped(g) && hero.canEquip(g)) {
					items.addElement(g);
				}
			}
			break;
		// ҩˮ
		case LIST_POTION:
			e = CGame.m_hero.hsItemList.keys();
			while (e.hasMoreElements()) {
				g = (Goods) hero.hsItemList.get(e.nextElement());
				if (g.getDetailType() == Data.TYPE_GI_COMMON_ITEM) {
					items.addElement(g);
				}
			}
			break;
		// װ��
		case LIST_EQUIP:
			e = CGame.m_hero.hsEquipList.keys();
			while (e.hasMoreElements()) {
				g = (Goods) hero.hsEquipList.get(e.nextElement());
				if (!hero.beenEquiped(g)) {
					items.addElement(g);
				}
			}
			break;
		// ��ʯ
		case LIST_STONE:
			e = CGame.m_hero.hsItemList.keys();
			while (e.hasMoreElements()) {
				g = (Goods) hero.hsItemList.get(e.nextElement());
				if (g.getDetailType() == Data.TYPE_GI_OTHER_ITEM) {
					items.addElement(g);
				}
			}
			break;
		// ���⡢������Ʒ
		case LIST_SPECIAL:
			e = CGame.m_hero.hsItemList.keys();
			while (e.hasMoreElements()) {
				g = (Goods) hero.hsItemList.get(e.nextElement());
				if (g.getDetailType() == Data.TYPE_GI_QUEST_ITEM) {
					items.addElement(g);
				}
			}
			break;
		// װ���̵�
		case LIST_EQUIP_SHOP:
			Goods[] shopGood = Goods.getShopGoodsType(GameUI.curShopId(),
					Goods.TYPE_EQUIP);
			for (int i = 0; i < shopGood.length; i++) {
				items.addElement(shopGood[i]);
			}
			break;
		// ��Ʒ�̵�
		case LIST_ITEM_SHOP:
			shopGood = Goods.getShopGoodsType(GameUI.curShopId(),
					Goods.TYPE_ITEM);
			for (int i = 0; i < shopGood.length; i++) {
				items.addElement(shopGood[i]);
			}
			break;
		case LIST_SKILL:
			// ���뵱ǰ�ļ���
			for (int i = 0; i < hero.skills.length; i++) {
				// short nextSkillId= Data.SKILL_GROUP[
				// hero.skills[i].property[Goods.PRO_ID] ]
				// [Data.IDX_SKI_upLvDest];
				// Goods nextSkill=Goods.createGoods((short)2,nextSkillId);
				// if(nextSkill!=null){
				// items.addElement(nextSkill);
				// }
				items.addElement(hero.skills[i]);
			}
			break;
		case LIST_QUEST:

			break;
		}
		correctIndex();
	}

	/**
	 * �л�Ϊĳ��list
	 * 
	 * @param ListId
	 *            int
	 * @param resetIndex
	 *            boolean,��������ֵ
	 */
	public void setCurrent(int ListId, boolean resetIndex) {
		allIndex[listId][0] = (byte) startIndex;
		allIndex[listId][1] = (byte) cursorIndex;
		listId = (byte) ListId;
		if (resetIndex) {
			startIndex = cursorIndex = 0;
		} else {
			startIndex = allIndex[listId][0];
			cursorIndex = allIndex[listId][1];
		}
		update();
	}

	/**
	 * ��ֹ����Խ��
	 */
	private void correctIndex() {
		if (startIndex + cursorIndex >= items.size() - 1) {
			startIndex = Math.max(items.size() - SHOW_ROW[listId], 0);
			cursorIndex = items.size() - startIndex - 1;
		}
	}

	/**
	 * ��ǰitem
	 * 
	 * @return Goods
	 */
	public Goods curItem() {
		if (size() > 0) {
			return (Goods) items.elementAt(startIndex + cursorIndex);
		} else {
			return null;
		}
	}

	/**
	 * ʵ������
	 * 
	 * @return int
	 */
	public int index() {
		return startIndex + cursorIndex;
	}

	/**
	 * ��ʼ����
	 * 
	 * @return int
	 */
	public int startIndex() {
		return startIndex;
	}

	/**
	 * �������
	 * 
	 * @return int
	 */
	public int cursorIndex() {
		return cursorIndex;
	}

	/**
	 * ���ù��λ�ã����ڴ�����
	 * 
	 * @param cursor
	 *            int
	 * @return int
	 */
	public void setCursorIndex(int cursor) {
		if (cursor < 0 || cursor >= SHOW_ROW[listId]
				|| startIndex + cursor >= size()) {
			System.out
					.println("List.setCursorIndex(cursor),cursor outOfRange ,cursor = "
							+ cursor);
			return;
		}
		cursorIndex = cursor;
	}

	/**
	 * item����
	 * 
	 * @return int
	 */
	public int size() {
		return items.size();
	}

	/**
	 * ��������ȡ��item
	 * 
	 * @param index
	 *            int
	 * @return Goods
	 */
	public Goods elementAt(int index) {
		if (index < 0 || index >= size()) {
			System.out.println("List.elementAt(index), index illgal,index = "
					+ index);
			return null;
		} else {
			return (Goods) items.elementAt(index);
		}
	}

	/**
	 * ��ʾ����
	 * 
	 * @return int
	 */
	public int showRow() {
		return SHOW_ROW[listId];
	}

	public void removeAllElements() {
		items.removeAllElements();
	}

	// ====================================================================================

	/**************************************************************************
	 * �򻯵���������ͬ��
	 **************************************************************************/
	class SimpleVector {
		protected Object[] elementData;
		protected int elementCount;
		protected int capacityIncrement;

		SimpleVector(int initialCapacity) {
			this.elementData = new Object[initialCapacity];
			this.capacityIncrement = 10;
		}

		SimpleVector() {
			this(25);
		}

		private void ensureCapacityHelper(int minCapacity) {
			int oldCapacity = elementData.length;
			if (minCapacity > oldCapacity) {
				Object[] oldData = elementData;
				int newCapacity = (capacityIncrement > 0) ? (oldCapacity + capacityIncrement)
						: (oldCapacity * 2);
				if (newCapacity < minCapacity) {
					newCapacity = minCapacity;
				}
				elementData = new Object[newCapacity];
				System.arraycopy(oldData, 0, elementData, 0, elementCount);
			}
		}

		public int capacity() {
			return elementData.length;
		}

		public int size() {
			return elementCount;
		}

		public boolean isEmpty() {
			return elementCount == 0;
		}

		public void addElement(Object obj) {
			ensureCapacityHelper(elementCount + 1);
			elementData[elementCount++] = obj;
		}

		public boolean removeElement(Object obj) {
			int i = indexOf(obj);
			if (i >= 0) {
				removeElementAt(i);
				return true;
			}
			return false;
		}

		private int indexOf(Object elem) {
			if (elem == null) {
				for (int i = 0; i < elementCount; i++)
					if (elementData[i] == null)
						return i;
			} else {
				for (int i = 0; i < elementCount; i++)
					if (elem.equals(elementData[i]))
						return i;
			}
			return -1;
		}

		private void removeElementAt(int index) {
			if (index >= elementCount) {
				throw new ArrayIndexOutOfBoundsException(index + " >= "
						+ elementCount);
			} else if (index < 0) {
				throw new ArrayIndexOutOfBoundsException(index);
			}
			int j = elementCount - index - 1;
			if (j > 0) {
				System.arraycopy(elementData, index + 1, elementData, index, j);
			}
			elementCount--;
			elementData[elementCount] = null; /* to let gc do its work */
		}

		public void removeAllElements() {
			for (int i = 0; i < elementCount; i++)
				elementData[i] = null;
			elementCount = 0;
		}

		public boolean contains(Object elem) {
			return indexOf(elem) >= 0;
		}

		public Object elementAt(int index) {
			if (index >= elementCount) {
				throw new ArrayIndexOutOfBoundsException(index + " >= "
						+ elementCount);
			}
			return elementData[index];
		}

	}// ~~ end of SimpleVector

}
