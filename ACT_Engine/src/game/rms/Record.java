package game.rms;

import game.CGame;
import game.CTools;
import game.Goods;
import game.object.CHero;
import game.object.IObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.rms.RecordStore;

import com.mglib.script.Script;

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

public class Record {
	public Record() {
	}

	/*******************************
	 * 游戏保存
	 *******************************/
	// 游戏存贮
	public static final String DB_NAME_GAME = "EVIL_HUNTER2_GAME";
	public static final String DB_NAME_GAME_CLEAR = "EVIL_HUNTER2_CLEAR_GAME";
	public static final String DB_NAME_CONFIG = "EVIL_HUNTER2_CONFIG";
	public static final String DB_NAME_JOIN_SAVE = "EVIL_HUNTER2_JOIN";
	public static final byte NUM_RMS_RECORD = 2;
	public static final byte RECORD_ID_0 = 1; // record ID默认编号是自然数
	public static final byte RECORD_ID_1 = 2;

	public static byte[][] RMSShowData = new byte[NUM_RMS_RECORD][16 /* 根据存贮情况定!! */]; // 存贮的要在界面中显示的数据
	public static boolean[] hasRecord = new boolean[NUM_RMS_RECORD];

	// 临时存放主角数据
	public static CHero savedHeroTemp = new CHero();
	public static short savedHeroId;
	public static Goods[] savedSkills = new Goods[2];
	public static short savedWeaponId;

	/***************************************
	 * 需要根据游戏而修改的部分
	 **************************************/
	/**
	 * 根据相应的数据库名，获得要写入的字节数据数组
	 * 
	 * @param dbName
	 *            String
	 * @param recordID
	 *            int
	 * @param baos
	 *            ByteArrayOutputStream
	 * @param dos
	 *            DataOutputStream
	 * @throws IOException
	 * @return byte[]
	 */
	private static byte[] getRMSBytes(String dbName, int recordID,
			ByteArrayOutputStream baos, DataOutputStream dos) throws Exception {
		byte[] data;
		if (dbName.equals(DB_NAME_GAME)) {

			System.out.println("-------write DB_NAME_GAME--------");

			// 当前关卡
			dos.writeInt(CGame.curLevelID);
			// 全局对象
			Enumeration e = CGame.hsSaveInfo.keys();
			dos.writeShort(CGame.hsSaveInfo.size());
			short[] tempValue = null;
			String s = null;
			while (e.hasMoreElements()) {
				s = (String) e.nextElement();
				tempValue = (short[]) CGame.hsSaveInfo.get(s);
				dos.writeUTF(s);
				CTools.saveArrayShort1(tempValue, dos);
			}
			// 脚本信息：系统变量 任务属性
			CTools.saveArrayShort1(Script.systemVariates, dos);
			CTools.saveArrayShort1(Script.systemTasks, dos);
			CTools.saveArrayShort1(Script.systemTasksActorIDs, dos);
			// 主角
			// 当前主角ID
			dos
					.writeShort(CGame.m_hero.m_actorProperty[IObject.PRO_INDEX_ROLE_ID]);
			dos.writeInt(CGame.m_hero.m_x);
			dos.writeInt(CGame.m_hero.m_y);
			dos.writeInt(CHero.equipAddIndex);
			// 装备物品
			e = CGame.m_hero.hsEquipList.keys();
			dos.writeInt(CGame.m_hero.hsEquipList.size());
			while (e.hasMoreElements()) {
				Goods temp = (Goods) CGame.m_hero.hsEquipList.get(e
						.nextElement());
				CTools.saveArrayShort1(temp.property, dos);
				CTools.saveArrayShort1(temp.affectProperty, dos);
			}
			e = CGame.m_hero.hsItemList.keys();
			dos.writeInt(CGame.m_hero.hsItemList.size());
			while (e.hasMoreElements()) {
				Goods temp = (Goods) CGame.m_hero.hsItemList.get(e
						.nextElement());
				CTools.saveArrayShort1(temp.property, dos);
			}
			// 属性
			CTools.saveArrayShort1(CGame.m_hero.m_actorProperty, dos);
			// 技能
			dos.writeInt(CGame.m_hero.skills.length);
			for (int i = 0; i < CGame.m_hero.skills.length; i++) {
				CTools.saveArrayShort1(CGame.m_hero.skills[i].property, dos);
				CTools.saveArrayShort1(CGame.m_hero.skills[i].affectProperty,
						dos);
			}
			// 另一个主角的技能，武器
			dos.writeInt(savedSkills.length);
			for (int i = 0; i < savedSkills.length; i++) {
				CTools.saveArrayShort1(savedSkills[i].property, dos);
				CTools.saveArrayShort1(savedSkills[i].affectProperty, dos);
			}
			dos.writeShort(savedWeaponId);
			System.out.println("-------write rms end--------");
			// 转换为字节流
			data = baos.toByteArray();
			System.arraycopy(data, 0, RMSShowData[recordID - 1], 0,
					RMSShowData[recordID - 1].length);
		} else if (dbName.equals(DB_NAME_CONFIG)) {
			// 1.系统操作信息
			// Tools.saveArrayInt1 ( shortcutKey , dos );
			// Tools.saveArrayShort1 ( shortcutValue , dos );
			// dos.writeBoolean ( bMusicOn );
			// 2.通关数据
			// ...

			// 转换为字节流
			data = baos.toByteArray();
		} else if (dbName.equals(DB_NAME_GAME_CLEAR)) {
			// ...
			// 转换为字节流
			data = baos.toByteArray();
		} else if (dbName.equals(DB_NAME_JOIN_SAVE)) { // 为了能够衔接下部而临时加的一个RMS
			// ...
			// 最后转化为字节流
			data = baos.toByteArray();
		} else {
			data = null;
		}
		return data;
	}

	/**
	 * 根据相应的数据库名解析指定的流信息
	 * 
	 * @param dbName
	 *            String
	 * @param recordID
	 *            int
	 * @param dis
	 *            DataInputStream
	 * @throws IOException
	 * @return boolean
	 */
	private static boolean parseRMSBytes(String dbName, int recordID,
			DataInputStream dis) throws Exception {
		if (dbName.equals(DB_NAME_GAME)) {
			savedHeroTemp = new CHero();
			System.out.println("-------read DB_NAME_GAME--------");
			// 当前关卡
			CGame.curLevelID = dis.readInt();
			// 全局对象
			CGame.hsSaveInfo.clear();
			int size = dis.readShort();
			for (int i = 0; i < size; i++) {
				CGame.hsSaveInfo
						.put(dis.readUTF(), CTools.readArrayShort1(dis));
			}
			// 脚本信息：系统变量
			Script.systemVariates = CTools.readArrayShort1(dis);
			Script.systemTasks = CTools.readArrayShort1(dis);
			Script.systemTasksActorIDs = CTools.readArrayShort1(dis);
			// 2个主角的信息
			savedHeroId = dis.readShort();
			savedHeroTemp.m_x = savedHeroTemp.m_x = dis.readInt();
			savedHeroTemp.m_y = savedHeroTemp.m_y = dis.readInt();
			CHero.equipAddIndex = dis.readInt();
			// 装备、物品
			size = dis.readInt();
			savedHeroTemp.hsEquipList = new Hashtable();
			for (int i = 0; i < size; i++) {
				Goods temp = new Goods();
				temp.property = CTools.readArrayShort1(dis);
				temp.affectProperty = CTools.readArrayShort1(dis);
				savedHeroTemp.hsEquipList.put(temp.getKey() + "", temp);
			}

			size = dis.readInt();
			savedHeroTemp.hsItemList = new Hashtable();
			for (int i = 0; i < size; i++) {
				Goods temp = new Goods();
				temp.property = CTools.readArrayShort1(dis);
				savedHeroTemp.hsItemList.put(temp.getKey() + "", temp);
			}

			// 主角属性
			savedHeroTemp.m_actorProperty = CTools.readArrayShort1(dis);
			// 技能
			size = dis.readInt();
			for (int i = 0; i < size; i++) {
				savedHeroTemp.skills[i] = new Goods();
				savedHeroTemp.skills[i].property = CTools.readArrayShort1(dis);
				savedHeroTemp.skills[i].affectProperty = CTools
						.readArrayShort1(dis);
			}
			size = dis.readInt();
			for (int i = 0; i < size; i++) {
				savedSkills[i] = new Goods();
				savedSkills[i].property = CTools.readArrayShort1(dis);
				savedSkills[i].affectProperty = CTools.readArrayShort1(dis);
			}
			savedWeaponId = dis.readShort();

			System.out.println("-------read rms end--------");
		} else if (dbName.equals(DB_NAME_CONFIG)) {
			// 1.系统操作信息

		} else if (dbName.equals(DB_NAME_GAME_CLEAR)) {
		} else if (dbName.equals(DB_NAME_JOIN_SAVE)) {
		}
		return true;
	}

	/**
	 * 检测数据库:DB_NAME_GAME的记录情况
	 * 
	 * @param rmsID
	 *            int
	 * @return byte[]
	 */
	public static final boolean[] checkRMS() {
		byte[] data;
		int index;
		// 记录1
		data = readRMSBytes(DB_NAME_GAME, RECORD_ID_0);
		if (data != null) {
			index = RECORD_ID_0 - 1;
			hasRecord[index] = true;
			System.arraycopy(data, 0, RMSShowData[index], 0,
					RMSShowData[index].length);
		}

		// 记录2
		data = readRMSBytes(DB_NAME_GAME, RECORD_ID_1);
		if (data != null) {
			index = RECORD_ID_1 - 1;
			hasRecord[index] = true;
			System.arraycopy(data, 0, RMSShowData[index], 0,
					RMSShowData[index].length);
		}
		return hasRecord;
	}

	/**
	 * 检测是否存在有效的衔接数据
	 */
	// public static final boolean isHasJoinRecord ()
	// {
	// byte[] data;
	// data = readRMSBytes ( DB_NAME_JOIN_SAVE , RECORD_ID_0 );
	// if ( data != null )
	// {
	// return true;
	// }
	// else
	// {
	// return false;
	// }
	// }

	/**
	 * 玩家是否存过档
	 * 
	 * @return boolean
	 */
	public static boolean haveRecord() {
		// String[] recordList=null;
		// try{
		// recordList = RecordStore.listRecordStores();
		// }
		// catch(Exception e){
		// e.printStackTrace();
		// }
		//
		// if (recordList.length>0) {
		// return true;
		// }

		try {
			RecordStore rs = RecordStore.openRecordStore(DB_NAME_GAME, false);
			if (rs != null && rs.getNumRecords() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/***************************************
	 * 对外公开的读写数据库的接口
	 **************************************/
	/**
	 * 保存数据到指定数据库
	 * 
	 * @param dbName
	 *            String
	 * @param recordID
	 *            int
	 * @param data
	 *            byte[]
	 * @return boolean
	 */
	public static boolean saveToRMS(String dbName, int recordID) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] data = getRMSBytes(dbName, recordID, baos,
					new DataOutputStream(baos));
			saveRMSBytes(dbName, recordID, data);
			baos.close();
			baos = null;
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 保存数据到指定数据库
	 * 
	 * @param dbName
	 *            String
	 * @param recordID
	 *            int
	 * @param data
	 *            byte[]
	 * @return boolean
	 */
	private static boolean saveRMSBytes(String dbName, int recordID, byte[] data) {
		try {
			RecordStore rs = RecordStore.openRecordStore(dbName, true);
			if (rs == null) {
				return false;
			}
			while (rs.getNumRecords() < recordID) {
				rs.addRecord(new byte[] { 0 }, 0, 1);
			}
			rs.setRecord(recordID, data, 0, data.length);
			rs.closeRecordStore();
			rs = null;
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 读取指定的数据可数据并解析之
	 * 
	 * @param dbName
	 *            String
	 * @param recordID
	 *            int
	 * @return boolean
	 */
	public static boolean readFromRMS(String dbName, int recordID) {
		try {
			byte[] data = readRMSBytes(dbName, recordID);
			if (data == null) {
				return false;
			}
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
					data));
			parseRMSBytes(dbName, recordID, dis);
			dis.close();
			dis = null;
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 读取指定RMS字节数据
	 * 
	 * @param rmsID
	 *            it
	 * @return booean
	 **/
	private static byte[] readRMSBytes(String dbName, int recordID) {
		try {
			RecordStore rs = RecordStore.openRecordStore(dbName, true);
			if ((rs == null) || (rs.getNumRecords() < recordID)) {
				return null;
			}
			byte[] data = rs.getRecord(recordID);
			rs.closeRecordStore();
			rs = null;
			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
