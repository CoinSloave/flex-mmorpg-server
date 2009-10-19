package com.mglib.mdl.map;

import game.CGame;
import game.res.ResLoader;

import java.io.DataInputStream;
import java.io.InputStream;

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
public class MapData {
	private MapData() {
	}

	public static final byte PHY_NONE = -1;
	public static final byte PHY_AIR = 0;
	public static final byte PHY_BLOCK_BOX = 1; // 阻挡箱子
	public static final byte PHY_LADDER = 2; // 梯子
	public static final byte PHY_V_WALLING = 3; // 竖直踩墙
	public static final byte PHY_SLOP3_1 = 4;
	public static final byte PHY_SLOP3_2 = 5;
	public static final byte PHY_SLOP3_3 = 6;
	public static final byte PHY_SLOPE_BASE = 7;
	public static final byte PHY_SLOP2_1 = 8;
	public static final byte PHY_SLOP2_2 = 9;
	public static final byte PHY_SLOP = 12;
	public static final byte PHY_SOLID = 10; // 阻挡
	public static final byte PHY_WALL_EDGE = 11; // 高墙，趴住
	public static final byte PHY_HALF_BLOCK = 17; // 下落阻挡
	public static final byte PHY_DEAD = 14;
	public static final byte PHY_OUT = 15;
	public static final byte PHY_LAW_WALL = 16; // 矮墙，翻上去

	public static final int MASK___TILE_FLAGS = 0xf0;
	public static final byte PHY_FLAG_TILE_BOX = 0x40;
	public static final byte MASK___TILE_PHY_ENV_VALUE = 0x3f;

	public static final int TILE_MASK = 0x7F;
	public static final int TILE_FLIP_MASK = 0x80;

	public static final byte MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG = MASK___TILE_PHY_ENV_VALUE
			| PHY_FLAG_TILE_BOX;

	public int m_mapTotalWidthByPixel; // 地图宽度像素数值
	public int m_mapTotalHeightByPixel; // 地图高度像素数值

	public int m_mapWidthByTile; // 保存每一层地图数据的tile宽度 0:背景层 1:前景层 2:光影层和物理层
	public int m_mapHeightByTile; //

	public int m_mapLayerCount = 3; // 地图数据层的数量
	public byte[] m_groundData; // 整个背景地图数据信息
	public byte[] m_buildData; // 整个前景地图数据信息
	public byte[] m_phyData;

	public static final byte MAP_LAYER_PHY = 2;
	public static final byte MAP_LAYER_LIGHT = 3; // 4;
	public static final byte MAP_LAYER_BUILD = 1;
	public static final byte MAP_LAYER_GROUND = 0;

	public static final byte MAP_LAYER_BUILD_NONE_DATA = 0;
	public static final byte MAP_LAYER_DATA_NONE_DATA = 0;
	public static boolean m_isuseCreatAllImage;

	// //物理层数据的信息
	// //static int [] m_tilePerLine;//每一层总的tile数
	// static int m_prevX0, m_prevX1, m_prevY0, m_prevY1; //像素值

	private static MapData mapData;

	public static MapData getInstance() {
		if (mapData == null) {
			mapData = new MapData();
		}
		return mapData;
	}

	private static MapData[] mapDatas;

	public static MapData[] getInstance(int num) {
		if (mapDatas == null) {
			mapDatas = new MapData[num];
			for (int i = 0; i < num; i++) {
				mapDatas[i] = new MapData();
			}
		}
		return mapDatas;
	}

	public static void clearMapDatas() {
		if (mapDatas == null)
			return;

		for (int i = 0; i < mapDatas.length; i++) {
			mapDatas[i] = null;
		}

		mapDatas = null;
	}

	public static final int CAMERA_TOP = 0;
	public static final int CAMERA_LEFT = 0;

	public static int MAX_Y_INVERT_DISTANCE;

	/**************
	 * 静态MLG表
	 *************/
	public static ContractionMLG[] mapMLGs;
	public static long[] mlgFlag;

	/************
	 * mapItem:MI
	 ************/
	protected byte mi_mlgCount;
	protected short[] mi_mlg;
	protected short[][] mi_moduleID;
	protected byte[] mi_moduleData; // 线形module 数据[m_MI_moduleCount * 2]: MLGID,
									// offsetID
	protected byte[] mi_itemData; // 显示item 数据[m_MI_itemCount * 2]

	/************
	 * buildItem:BI
	 ************/
	protected byte bi_mlgCount;
	protected short[] bi_mlg;
	protected short[][] bi_moduleID;
	protected byte[] bi_moduleData; // 线形module 数据[m_BI_moduleCount * 2]: MLGID,
									// offsetID
	protected byte[] bi_itemData;

	static byte cellWidth;
	static byte cellHeight;
	public final static int TILE_WIDTH = 16; //
	public final static int TILE_HEIGHT = 16;

	/**
	 * 读取地图资源的信息 m_mapBufData m_phyBuffData m_tilePerLine m_mapLayerCount
	 * 
	 * @param mapID
	 *            int 当前地图ID号
	 */
	public void loadLevelMap(int mapID, String fileName) {
		DataInputStream dis = null;
		try {
			// mlg 标志
			if (mlgFlag == null) {
				mlgFlag = new long[ResLoader.mapMLGCount / 64 + 1];
			}
			long[] mapMlgFlag = new long[mlgFlag.length];
			long[] releasef = new long[mlgFlag.length];
			long[] loadf = new long[mlgFlag.length];

			InputStream is = "".getClass().getResourceAsStream(fileName);
			dis = new DataInputStream(is);
			int fileCount = dis.readByte();
			int[] fileOffset = new int[fileCount + 1];
			for (int i = 0; i < fileOffset.length; i++) {
				fileOffset[i] = dis.readInt();
			}
			/************
			 * 地图属性: level.bin
			 ************/
			int levelCount = dis.readByte();
			int[] levelOffset = new int[levelCount + 1];
			for (int i = 0; i < levelOffset.length; i++) {
				levelOffset[i] = dis.readInt();
			}
			dis.skip(levelOffset[mapID]);
			m_mapWidthByTile = (short) (dis.readByte() & 0xff);
			m_mapHeightByTile = (short) (dis.readByte() & 0xff);
			cellWidth = dis.readByte();
			cellHeight = dis.readByte();
			m_mapTotalWidthByPixel = (short) (m_mapWidthByTile * cellWidth);
			m_mapTotalHeightByPixel = (short) (m_mapHeightByTile * cellHeight);
			int mapItemIndex = dis.readByte();
			int buildItemIndex = dis.readByte();
			m_groundData = new byte[m_mapWidthByTile * m_mapHeightByTile];
			dis.read(m_groundData);
			m_buildData = new byte[m_mapWidthByTile * m_mapHeightByTile];
			dis.read(m_buildData);
			m_phyData = new byte[m_mapWidthByTile * m_mapHeightByTile];
			dis.read(m_phyData);

			setPhyData();

			dis.skip(levelOffset[levelCount] - levelOffset[mapID + 1]);
			/************
			 * mapItem:MI
			 ************/
			int mapItemCount = dis.readByte() & 0xff;
			int[] mapItemOffset = new int[mapItemCount + 1];
			for (int i = 0; i < mapItemOffset.length; i++) {
				mapItemOffset[i] = dis.readInt();
			}
			dis.skip(mapItemOffset[mapItemIndex]);
			mi_mlgCount = dis.readByte(); //
			mi_mlg = new short[mi_mlgCount];
			mi_moduleID = new short[mi_mlgCount][];
			for (int i = 0; i < mi_mlgCount; i++) {
				mi_mlg[i] = dis.readShort(); // ContractionMLG.read(dis);

				mapMlgFlag[mi_mlg[i] / 64] |= 1L << (mi_mlg[i] % 64);
			}
			for (int j = 0; j < mi_mlgCount; j++) {
				int mc = dis.readShort();
				mi_moduleID[j] = new short[mc];
				for (int k = 0; k < mc; k++) {
					mi_moduleID[j][k] = dis.readShort();
				}
			}

			int mapItemModuleCount = dis.readByte() & 0Xff; // add on 12.14
															// 11.50
			mi_moduleData = new byte[mapItemModuleCount << 1];
			dis.read(mi_moduleData);
			int mapItemItemCount = dis.readByte() & 0Xff; // add on 12.14 11.50
			mi_itemData = new byte[mapItemItemCount << 1];
			dis.read(mi_itemData);
			dis.skip(mapItemOffset[mapItemCount]
					- mapItemOffset[mapItemIndex + 1]);
			/************
			 * buildItem:BI
			 ************/
			int buildItemCount = dis.readByte() & 0xff;
			int[] buildItemOffset = new int[buildItemCount + 1];
			for (int i = 0; i < buildItemOffset.length; i++) {
				buildItemOffset[i] = dis.readInt();
			}
			dis.skip(buildItemOffset[buildItemIndex]);
			bi_mlgCount = dis.readByte();
			bi_mlg = new short[bi_mlgCount];
			bi_moduleID = new short[bi_mlgCount][];
			for (int i = 0; i < bi_mlgCount; i++) {
				bi_mlg[i] = dis.readShort(); // ContractionMLG.read(dis);
				// if(CGame.s_isCreatAllModuleImage){
				// bi_mlg[i].getAllModuleImage(true);
				// }
				mapMlgFlag[bi_mlg[i] / 64] |= 1L << (bi_mlg[i] % 64);
			}
			for (int j = 0; j < bi_mlgCount; j++) {
				int mc = dis.readShort();
				bi_moduleID[j] = new short[mc];
				for (int k = 0; k < mc; k++) {
					bi_moduleID[j][k] = dis.readShort();
				}
			}
			int buildItemModuleCount = dis.readByte() & 0Xff;
			bi_moduleData = new byte[buildItemModuleCount << 1];
			dis.read(bi_moduleData);
			int buildItemItemCount = dis.readByte() & 0Xff;
			bi_itemData = new byte[buildItemItemCount << 1];
			dis.read(bi_itemData);
			dis.skip(buildItemOffset[buildItemCount]
					- buildItemOffset[buildItemIndex + 1]);

			/************
			 * mlg
			 ************/
			int[] mlgOs = new int[ResLoader.mapMLGCount + 1];
			for (int i = 0; i < mlgOs.length; i++) {
				mlgOs[i] = dis.readInt();
			}
			// mlg的资源分析
			for (int i = 0; i < mlgFlag.length; i++) {
				releasef[i] = (mlgFlag[i] ^ mapMlgFlag[i]) & mlgFlag[i];
				loadf[i] = (mlgFlag[i] ^ mapMlgFlag[i]) & mapMlgFlag[i];
				mlgFlag[i] |= loadf[i];
				mlgFlag[i] &= ~releasef[i];
			}
			// release
			for (int i = 0; i < ResLoader.mapMLGCount; i++) {
				if ((releasef[i / 64] & (1L << (i % 64))) != 0) {
					mapMLGs[i].destroy();
				}
			}
			// load
			for (int i = 0; i < ResLoader.mapMLGCount; i++) {
				if ((loadf[i / 64] & (1L << (i % 64))) != 0) {
					mapMLGs[i] = ContractionMLG.read(dis);
					if (CGame.s_isCreatAllModuleImage) {
						mapMLGs[i].getAllModuleImage(true);
					}

				} else {
					dis.skip(mlgOs[i + 1] - mlgOs[i]);
				}
			}

			// close stream
			dis.close();
		} catch (Exception ex) {
			// #if mode_debug
			ex.printStackTrace();
			// #endif
		}

	}

	/********************************
	 * 设置程序使用的物理层属性
	 **********************************/
	public void setPhyData() {
		for (int i = 0; i < m_phyData.length; ++i) { // 需要封装 1.4 2008
			if (m_phyData[i] == 6) {
				m_phyData[i] = PHY_SLOP3_3;
			} else if (m_phyData[i] == 5) {
				m_phyData[i] = PHY_SLOP3_2;
			} else if (m_phyData[i] == 4) {
				m_phyData[i] = PHY_SLOP3_1;
			} else if (m_phyData[i] == 20) {
				m_phyData[i] = (byte) (PHY_SLOP3_3 | TILE_FLIP_MASK);
			} else if (m_phyData[i] == 21) {
				m_phyData[i] = (byte) (PHY_SLOP3_2 | TILE_FLIP_MASK);
			} else if (m_phyData[i] == 22) {
				m_phyData[i] = (byte) (PHY_SLOP3_1 | TILE_FLIP_MASK);
			}
			if (m_phyData[i] == 9) {
				m_phyData[i] = PHY_SLOP2_2;
			} else if (m_phyData[i] == 8) {
				m_phyData[i] = PHY_SLOP2_1;
			} else if (m_phyData[i] == 17) {
				m_phyData[i] = (byte) (PHY_SLOP2_2 | TILE_FLIP_MASK);
			} else if (m_phyData[i] == 18) {
				m_phyData[i] = (byte) (PHY_SLOP2_1 | TILE_FLIP_MASK);
			}

			else if (m_phyData[i] == 10) {
				m_phyData[i] = PHY_SLOP;
			} else if (m_phyData[i] == 19) {
				m_phyData[i] = (byte) (PHY_SLOP | TILE_FLIP_MASK);
			} else if (m_phyData[i] == 0) { // null tile
				m_phyData[i] = PHY_AIR;
			} else if (m_phyData[i] == 12) {
				m_phyData[i] = PHY_SOLID;
			} else if (m_phyData[i] == 15) {
				m_phyData[i] = PHY_BLOCK_BOX;
			} else if (m_phyData[i] == 7) {
				m_phyData[i] = PHY_SLOPE_BASE;
			} else if (m_phyData[i] == 14) {
				m_phyData[i] = PHY_HALF_BLOCK;
			}
			// else if(m_phyData[i] == 5){
			// m_phyData[i] = PHY_SLOPE_BASE;
			// }
			else if (m_phyData[i] == 3) {
				m_phyData[i] = PHY_DEAD;
			}
		}

	}

	/**
      *
      *
      */
	public void destroyLeveLMap(boolean destroyAllMapMlg) {

		m_mapWidthByTile = 0;
		m_mapHeightByTile = 0;

		m_groundData = null;
		m_buildData = null;
		m_phyData = null;

		// for(int i = 0; i < mi_mlgCount; i++){
		// mi_mlg[i] = null;
		// }
		mi_mlg = null;
		mi_moduleData = null;
		mi_itemData = null;
		mi_mlgCount = 0;

		// for(int i = 0; i < bi_mlgCount; i++){
		// bi_mlg[i] = null;
		// }
		bi_mlg = null;
		bi_moduleData = null;
		bi_itemData = null;
		bi_mlgCount = 0;
		if (destroyAllMapMlg) {
			for (int i = 0; i < mapMLGs.length; i++) {
				if (mapMLGs[i] != null) {
					mapMLGs[i].destroy();
					mapMLGs[i] = null;
				}
			}
		}
		// #if !W958C
		System.gc();
		// #endif

	}

	/**
	 * get physics attributes of a specified tile
	 * 
	 * @param tileX
	 *            int x
	 * @param tileY
	 *            int y
	 * @return byte physics
	 */
	public static int s_lastTileFlag;

	public int getTilePhyEnv(int tileX, int tileY) {
		if (tileX < 0 || tileX >= m_mapWidthByTile || tileY < 0
				|| tileY >= m_mapHeightByTile) {
			return (m_phyData == null) ? MapData.PHY_AIR : MapData.PHY_OUT;
		}
		int tile = m_phyData[m_mapWidthByTile * tileY + tileX];
		s_lastTileFlag = tile & MapData.MASK___TILE_FLAGS;
		tile &= MapData.MASK___TILE_PHY_ENV_VALUE_WITH_BOX_FLAG;
		return tile;
	}

	/**
	 * 修改地图块属性的方法
	 * 
	 * @param box
	 *            short[] 需要更改的box大小
	 * @param flags
	 *            int
	 * @param bSetFlags
	 *            boolean //是否修改地图属性
	 */
	public void modifyTileFlags(short[] box, int flags, boolean bSetFlags) {
		// CTools.shiftBox(box , CMap.TILE_WIDTH / 2 , CMap.TILE_HEIGHT / 2);
		box[0] /= TILE_WIDTH;
		box[2] /= TILE_WIDTH;
		box[1] /= TILE_HEIGHT;
		box[3] /= TILE_HEIGHT;

		int index = m_mapWidthByTile * box[1] + box[0];
		int tempIndex = index;

		for (int top = box[1]; top <= box[3]; ++top) {
			index = tempIndex;
			for (int left = box[0]; left < box[2]; ++left) {
				if (bSetFlags) {
					{
						m_phyData[index] = (byte) flags;
					}
				} else {
					m_phyData[index] = PHY_AIR;
				}

				++index;
			}
			tempIndex += m_mapWidthByTile;
		}

		// if(game.CDebug.bShowPhysicalLayer) {
		// CGame.gMap.updateMap (Camera.cameraLeft, game.pak.Camera.cameraTop,
		// true);
		// }

	}

	public static final int MAP_LAYER_MODIFY_INFO_LAYER_ID = 0;
	public static final int MAP_LAYER_MODIFY_INFO_CELL_X = 1;
	public static final int MAP_LAYER_MODIFY_INFO_CELL_Y = 2;
	public static final int MAP_LAYER_MODIFY_INFO_OLD_TILE_VALUE = 3;
	public static final int MAP_LAYER_MODIFY_INFOS_COUNT = 4;
	public static final int MAP_LAYER_MODIFY_COUNT = 250;

	// 内存问题，先屏蔽
	// public static int[] s_mapLayersModifyInfoHistory
	// = new int[MAP_LAYER_MODIFY_COUNT * MAP_LAYER_MODIFY_INFOS_COUNT];
	// public static int s_mapLayersModifyInfoPointer;
	// public static int s_mapLayersModifyInfoPointerAtCheckPoint;

	/**
	 * modify the tile value of a specified cell
	 * 
	 * @param layerID
	 *            int
	 * @param cellX
	 *            int
	 * @param cellY
	 *            int
	 * @param newTileValue
	 *            int
	 */

	public boolean modifyMapLayer(int layerID, int cellX, int cellY,
			int newTileValue) {
		int cell_index = cellY * m_mapWidthByTile + cellX;
		int old_tile_value = 0;
		/*******************************
	   *
	   ***************************/
		if (layerID == MAP_LAYER_PHY) {
			old_tile_value = m_phyData[cell_index];
		} else if (layerID == MAP_LAYER_GROUND) {
			old_tile_value = m_groundData[cell_index];
		} else if (layerID == MAP_LAYER_BUILD) {
			old_tile_value = m_buildData[cell_index];
		}

		if (old_tile_value == newTileValue) {
			return false;
		}

		// s_mapLayersModifyInfoHistory[s_mapLayersModifyInfoPointer +
		// MAP_LAYER_MODIFY_INFO_LAYER_ID] = layerID;
		// s_mapLayersModifyInfoHistory[s_mapLayersModifyInfoPointer +
		// MAP_LAYER_MODIFY_INFO_CELL_X] = cellX;
		// s_mapLayersModifyInfoHistory[s_mapLayersModifyInfoPointer +
		// MAP_LAYER_MODIFY_INFO_CELL_Y] = cellY;
		// s_mapLayersModifyInfoHistory[s_mapLayersModifyInfoPointer +
		// MAP_LAYER_MODIFY_INFO_OLD_TILE_VALUE] = old_tile_value;
		//
		// s_mapLayersModifyInfoPointer += MAP_LAYER_MODIFY_INFOS_COUNT;
		/***********************************
	   *
	   *******************************************/
		if (layerID == MAP_LAYER_PHY) {
			m_phyData[cell_index] = (byte) newTileValue;
		} else if (layerID == MAP_LAYER_GROUND) {
			m_groundData[cell_index] = (byte) newTileValue;
		} else if (layerID == MAP_LAYER_BUILD) {
			m_buildData[cell_index] = (byte) newTileValue;
		}

		if (!game.CDebug.bShowPhysicalLayer) {
			return true;
		}

		// if(m_prevX0 <= cellX && cellX <= m_prevX1 && m_prevY0 <= cellY &&
		// cellY <= m_prevY1) {
		// mappedDraw (cellX, cellY, cellX, cellY);
		// }
		return true;
	}

}
