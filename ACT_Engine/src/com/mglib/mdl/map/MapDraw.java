package com.mglib.mdl.map;

import game.CDebug;
import game.CGame;
import game.CTools;
import game.config.dConfig;
import game.pak.Camera;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

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
public class MapDraw {

	public MapData mapRes;

	public MapDraw(MapData mapRes) {
		this.mapRes = mapRes;
	}

	private static MapDraw mapDraw;

	// public static MapDraw getInstance () {
	// if(mapDraw == null) {
	// mapDraw = new MapDraw ();
	// }
	// return mapDraw;
	// }

	public static int m_bufWidth; // 建立地图缓冲区的宽度
	public static int m_bufHeight; // 建立地图缓冲区的高度
	static Image m_imgBuffer; // 地图缓冲的Image对象
	static Graphics m_bufGraphics; // 地图缓冲的画笔

	static int m_prevX0, m_prevX1, m_prevY0, m_prevY1; // 像素值

	public static boolean bShowMap; // 是否显示map，放技能时不显示

	public static boolean ismultilayer;
	public static int multilayer_w;
	public static int multilayer_h;
	// 各个关卡背景层大小
	public static short[] MULTILAYER_SIZE = { 0, 0, 24 * 16, 30 * 16, 0, 0,
			24 * 16, 24 * 16, 38 * 16, 20 * 16,

			24 * 16, 24 * 16, 24 * 16, 24 * 16, 0, 0, 38 * 16, 20 * 16, 0, 0,

			24 * 16, 30 * 16, 24 * 16, 24 * 16, 0, 0, 24 * 16, 30 * 16,
			24 * 16, 30 * 16, 0, 0 };
	// 中间层Y在场景中的坐标
	public static short[] MID_LAYER_Y = { 0, 25 * 16, 0, 18 * 16, 320,

	18 * 16, 18 * 16, 0, 320, 0, 35 * 16, 19 * 16, 0, 35 * 16, 25 * 16, 0, };
	// 中间层图片
	public static String[] MID_LAYER_IMG = { "", "wall", "", "grass", "tree",

	"grass", "grass", "", "tree", "",

	"wall", "tree", "", "wall", "wall", "", };

	/**
	 * 是否多层卷轴
	 * 
	 * @param levelId
	 *            int
	 */
	public static void setMapDrawMode(int levelId) {
		if (CGame.testSceneFlag(CGame.SCENE_FLAG_SCROLL_AT_SCENE)
				|| CGame.testSceneFlag(CGame.SCENE_FLAG_SCROLL_AT_SCREEN)) {
			ismultilayer = true;
			multilayer_w = MULTILAYER_SIZE[2 * levelId];
			multilayer_h = MULTILAYER_SIZE[2 * levelId + 1];
			loadMidLayImg(levelId);
			midLayerY = MID_LAYER_Y[levelId];
		} else {
			ismultilayer = false;
			midLayerImg = null;
		}
	}

	public static Image midLayerImg;
	public static short midLayerY;
	public static short midLayerX;
	public static int midLayerOffsetX;

	private static void loadMidLayImg(int mapId) {
		midLayerImg = CTools.loadImage(MID_LAYER_IMG[mapId]);
	}

	/**
	 * 中间层卷轴
	 */
	public static void drawMidLayer(Graphics g) {
		if (!CGame.testSceneFlag(CGame.SCENE_FLAG_SCROLL_AT_SCENE)
				&& !CGame.testSceneFlag(CGame.SCENE_FLAG_SCROLL_AT_SCREEN)
				|| !bShowMap) {
			return;
		}
		// if (Camera.cameraTop>midLayerY
		// ||Camera.cameraBox[3]<midLayerY-midLayerImg.getHeight()
		// ) {
		// return;
		// }
		g.setClip(0, 0, dConfig.S_WIDTH, dConfig.S_HEIGHT);
		int offset;
		// 中间层固定在屏幕中
		if (CGame.testSceneFlag(CGame.SCENE_FLAG_SCROLL_AT_SCREEN)) {
			offset = 0;
		} else {
			offset = Camera.cameraTop;
		}

		g.drawImage(midLayerImg, midLayerX + midLayerOffsetX, midLayerY
				- offset, dConfig.ANCHOR_LB);
		int dir = midLayerOffsetX < 0 ? -1 : 1;
		g
				.drawImage(midLayerImg, midLayerX + midLayerOffsetX - dir
						* midLayerImg.getWidth(), midLayerY - offset,
						dConfig.ANCHOR_LB);
	}

	/**************
	 * 静态MLG表
	 *************/
	// public static ContractionMLG[] mapMLGs;
	// public static long[] mlgFlag;

	/**
	 * //初始化地图buffer
	 */
	public static void createBufferMap() { // ( int width, int height )
		// 建立一个地图buffer
		if (dConfig.CAMERA_WIDTH % MapData.TILE_WIDTH == 0) {
			m_bufWidth = dConfig.CAMERA_WIDTH + MapData.TILE_WIDTH;
		} else {
			m_bufWidth = ((dConfig.CAMERA_WIDTH / MapData.TILE_WIDTH) + 2)
					* MapData.TILE_WIDTH;
		}

		if (dConfig.CAMERA_HEIGHT % MapData.TILE_HEIGHT == 0) {
			m_bufHeight = dConfig.CAMERA_HEIGHT + 2 * MapData.TILE_HEIGHT;
		} else {
			m_bufHeight = ((dConfig.CAMERA_HEIGHT / MapData.TILE_HEIGHT) + 2)
					* MapData.TILE_HEIGHT;
		}

		m_imgBuffer = Image.createImage(m_bufWidth, m_bufHeight); // 创建地图缓冲
		m_bufGraphics = m_imgBuffer.getGraphics(); // 取得地图缓冲的画笔

		// ---------------------------------------------------------------------
	}

	// cameraX = (cameraX * Def.CAMERA_WIDTH / m_mapTotalWidthByPixel);
	/**
	 * 根据屏幕坐标更新缓冲区
	 * 
	 * @param cameraX
	 *            int //屏幕左上点相对场景坐标系的x坐标
	 * @param cameraY
	 *            int //屏幕左上点相对场景坐标系的y坐标
	 * @param bRedraw
	 *            boolean 是否更新缓冲区
	 */

	public void updateMap(int cameraX, int cameraY, boolean bRedraw) {

		if (CDebug.bShowPhysicalLayerOnly) {
			return;
		}

		// ///////////////////////
		// update bg buffer
		// ///////////////////////

		int alignedX0; // tile
		int alignedY0; // tile
		int alignedX1; // tile
		int alignedY1; // tile
		int start; // tile
		int end; // tile

		// 多层卷轴 限制缓冲更新在地表场景范围内
		if (ismultilayer) {
			cameraX = (cameraX/** Def.CAMERA_WIDTH */
			* (multilayer_w - dConfig.CAMERA_WIDTH) / (mapRes.m_mapTotalWidthByPixel - dConfig.CAMERA_WIDTH));
			cameraY = (cameraY/** Def.CAMERA_HEIGHT */
			* (multilayer_h - dConfig.CAMERA_HEIGHT) / (mapRes.m_mapTotalHeightByPixel - dConfig.CAMERA_HEIGHT));
		}

		// if(ismultilayer) {
		// cameraX = (cameraX * dConfig.CAMERA_WIDTH /
		// mapRes.m_mapTotalWidthByPixel) / multilayer_w;
		// cameraY = (cameraY * dConfig.CAMERA_HEIGHT /
		// mapRes.m_mapTotalHeightByPixel) / multilayer_h;
		// }
		alignedX0 = cameraX / MapData.TILE_WIDTH;
		alignedX1 = alignedX0 + (dConfig.CAMERA_WIDTH + MapData.TILE_WIDTH - 1)
				/ MapData.TILE_WIDTH;
		alignedY0 = cameraY / MapData.TILE_HEIGHT;
		alignedY1 = alignedY0 + (dConfig.CAMERA_HEIGHT / MapData.TILE_HEIGHT)
				+ 1;

		if (bRedraw) {
			mappedDraw(alignedX0, alignedY0, alignedX1, alignedY1);
			m_prevX0 = alignedX0;
			m_prevY0 = alignedY0;
			m_prevX1 = alignedX1;
			m_prevY1 = alignedY1;
		}

		if (m_prevX0 != alignedX0 || m_prevX1 != alignedX1) {
			if (m_prevX0 < alignedX0) {
				start = m_prevX1 + 1;
				end = alignedX1;
			} else {
				start = alignedX0;
				end = m_prevX0 - 1;
			}

			// 只更新需要更新的缓冲区
			mappedDraw(start, alignedY0, end, alignedY1);
			m_prevX0 = alignedX0;
			m_prevX1 = alignedX1;
		}

		if (m_prevY0 != alignedY0 || m_prevY1 != alignedY1) {
			if (m_prevY0 < alignedY0) {
				start = m_prevY1 + 1;
				end = alignedY1;
			} else {
				start = alignedY0;
				end = m_prevY0 - 1;
			}
			mappedDraw(alignedX0, start, alignedX1, end);

			m_prevY0 = alignedY0;
			m_prevY1 = alignedY1;
		}

	}

	public static byte LAYER_GROUND = 0;
	public static byte LAYER_BULID = 1;
	public static byte LAYER_PHY = 2;

	public void mappedDraw(int tileX0, int tileY0, int tileX1, int tileY1) {
		int posX;
		int posY;
		int id = 0;
		// int frontId;
		int idflag = 0;

		int moduleID, flip;
		int MLGID, offsetID;

		if (tileX0 < 0) {
			tileX0 = 0;
		}
		if (tileY0 < 0) {
			tileY0 = 0;
		}
		if (tileX1 >= mapRes.m_mapWidthByTile) {
			tileX1 = mapRes.m_mapWidthByTile - 1;
		}
		if (tileY1 >= mapRes.m_mapHeightByTile) {
			tileY1 = mapRes.m_mapHeightByTile - 1;
		}

		posY = (tileY0 * MapData.TILE_HEIGHT) % m_bufHeight;
		for (int j = tileY0; j <= tileY1; j++) {
			posX = (tileX0 * MapData.TILE_WIDTH) % m_bufWidth;

			for (int i = tileX0; i <= tileX1; i++) {
				for (int layer_id = 0; layer_id < mapRes.m_mapLayerCount; layer_id++) {
					int tile_index = mapRes.m_mapWidthByTile * j + i;
					idflag = j;
					if (layer_id == LAYER_GROUND) { // 得到地表层的ID

						id = (mapRes.m_groundData[tile_index]);
						if (id < -1) {
							id = id & 0xff;
						}
					} else if (layer_id == LAYER_BULID) { // 得到建筑层的id
						id = (mapRes.m_buildData[tile_index]);
						if (id < -1) {
							id = id & 0xff;
						}
					} else if (layer_id == LAYER_PHY) {
						id = mapRes.getTilePhyEnv(i, j);
					}

					if (id >= 0) {

						if (layer_id == 0) {
							// try {
							moduleID = mapRes.mi_itemData[id << 1] & 0xff;
							flip = mapRes.mi_itemData[(id << 1) + 1];
							id = moduleID << 1;
							MLGID = mapRes.mi_moduleData[id++];
							offsetID = mapRes.mi_moduleData[id] & 0xff;
							MapData.mapMLGs[MLGID].drawModule(m_bufGraphics,
									offsetID, posX, posY, Graphics.TOP
											| Graphics.LEFT, flip);

							// }
							// catch(Exception e) {
							// e.printStackTrace ();
							// }
						} else if (layer_id == 1) {
							if (!ismultilayer) {
								moduleID = mapRes.bi_itemData[id << 1] & 0xff;
								flip = mapRes.bi_itemData[(id << 1) + 1];
								id = moduleID << 1;
								MLGID = mapRes.bi_moduleData[id++];
								offsetID = mapRes.bi_moduleData[id] & 0xff;
								MapData.mapMLGs[MLGID].drawModule(
										m_bufGraphics, offsetID, posX, posY,
										Graphics.TOP | Graphics.LEFT, flip);
							}
						}

						else if (layer_id == 2 && CDebug.bShowPhysicalLayer) {
							drawDebugTile(id, posX, posY, idflag);
						}

					}
					// 空的地图层，填充矩形
					else if (layer_id == 0) {
						// #if N73
						m_bufGraphics.setColor(0);
						// #else
						// # m_bufGraphics.setColor(0x80FFFF);
						// #endif
						m_bufGraphics.setClip(posX, posY, 16, 16);
						m_bufGraphics.fillRect(posX, posY, 16, 16);
					}
				}
				posX += MapData.TILE_WIDTH;
				if (posX >= m_bufWidth) {
					posX -= m_bufWidth;
				}
			}

			posY += MapData.TILE_HEIGHT;

			if (posY >= m_bufHeight) {
				posY -= m_bufHeight;
			}

		}
	}

	/**
	 * 显示数据层
	 */
	public void drawDebugTile(int id, int posX, int posY, int idflag) {

		// m_bufGraphics.setClip(0 , 0 , m_bufWidth , m_bufHeight);
		// m_bufGraphics.setFont(smallFont);
		m_bufGraphics.setClip(0, 0, m_bufWidth, m_bufHeight);
		switch (id) {
		case MapData.PHY_BLOCK_BOX: // 躲藏区域
			m_bufGraphics.setColor(0x123456);
			m_bufGraphics.fillRect(posX, posY, MapData.TILE_WIDTH - 1,
					MapData.TILE_HEIGHT - 1);
			break;
		case MapData.PHY_SOLID: // 阻挡区域
			m_bufGraphics.setColor(0x00ff00);
			m_bufGraphics.fillRect(posX, posY, MapData.TILE_WIDTH - 1,
					MapData.TILE_HEIGHT - 1);
			m_bufGraphics.setColor(0);
			m_bufGraphics.drawString(String.valueOf(idflag), posX, posY, 20);
			break;

		case MapData.PHY_V_WALLING: // 竖直踩墙区域
			m_bufGraphics.setColor(0x00ff);
			m_bufGraphics.fillRect(posX, posY, MapData.TILE_WIDTH - 1,
					MapData.TILE_HEIGHT - 1);
			m_bufGraphics.setColor(0);
			m_bufGraphics.drawString(String.valueOf(idflag), posX, posY, 20);
			break;

		case MapData.PHY_SLOP3_1:
		case MapData.PHY_SLOP3_2:
		case MapData.PHY_SLOP3_3:
			m_bufGraphics.setColor(0x123);
			m_bufGraphics.fillRect(posX, posY, MapData.TILE_WIDTH - 1,
					MapData.TILE_HEIGHT - 1);
			break;

		case MapData.PHY_SLOP2_1:
		case MapData.PHY_SLOP2_2:
			m_bufGraphics.setColor(0xff0e06);
			m_bufGraphics.fillRect(posX, posY, MapData.TILE_WIDTH - 1,
					MapData.TILE_HEIGHT - 1);
			break;

		case MapData.PHY_SLOP:
			m_bufGraphics.setColor(0xffff06);
			m_bufGraphics.fillRect(posX, posY, MapData.TILE_WIDTH - 1,
					MapData.TILE_HEIGHT - 1);
			break;

		case MapData.PHY_HALF_BLOCK:
			m_bufGraphics.setColor(0xff);
			m_bufGraphics.fillRect(posX, posY, MapData.TILE_WIDTH - 1,
					MapData.TILE_HEIGHT - 1);
			break;

		}

	}

	protected static final int CAMERA_TOP = 0;
	protected static final int CAMERA_LEFT = 0;

	public static boolean useMapBuf = true; // 地图是否使用缓冲

	public void paintMap(Graphics g, int cameraX, int cameraY) {

		if (CDebug.bShowPhysicalLayerOnly) {
			g.setClip(0, 0, dConfig.CAMERA_WIDTH, dConfig.CAMERA_HEIGHT);
			g.setColor(0x9f9f9f);
			g.fillRect(0, 0, dConfig.CAMERA_WIDTH, dConfig.CAMERA_HEIGHT);
			showPhyOnly(cameraX, cameraY, g);
			return;
		}

		// /**
		// * 是否显示地表层
		// */
		// if(!CGame.m_bShowMap) {
		// g.setColor (0);
		// g.setClip (CAMERA_LEFT, CAMERA_TOP, dConfig.CAMERA_WIDTH,
		// dConfig.CAMERA_HEIGHT);
		// g.fillRect (CAMERA_LEFT, CAMERA_TOP, dConfig.CAMERA_WIDTH,
		// dConfig.CAMERA_HEIGHT);
		// return;
		// }

		int modX0; // pixel
		int modX1; // pixel
		int modY0 = 0; // pixel
		int modY1 = 0; // pixel

		int realCamerY = cameraY;
		int realCamerX = cameraX;
		// 如果是多层卷轴
		if (ismultilayer) {
			int w = (cameraX * (multilayer_w - dConfig.CAMERA_WIDTH) / (mapRes.m_mapTotalWidthByPixel - dConfig.CAMERA_WIDTH));
			cameraY = (cameraY * (multilayer_h - dConfig.CAMERA_HEIGHT) / (mapRes.m_mapTotalHeightByPixel - dConfig.CAMERA_HEIGHT));
			modX0 = w % m_bufWidth;
			modX1 = (w + dConfig.CAMERA_WIDTH) % m_bufWidth;
		} else {
			modX0 = cameraX % m_bufWidth;
			modX1 = (cameraX + dConfig.CAMERA_WIDTH) % m_bufWidth;
		}

		if (ismultilayer) {
			if (cameraY >= (multilayer_h - dConfig.S_HEIGHT)) {
				modY0 = (multilayer_h - dConfig.S_HEIGHT) /* % m_bufHeight */;
				cameraY = (multilayer_h - dConfig.S_HEIGHT)/* % m_bufHeight */;
			} else {
				modY0 = cameraY /* % m_bufHeight */;
				modY1 = (cameraY + dConfig.CAMERA_HEIGHT) % m_bufHeight;
			}
		} else {
			modY0 = cameraY % m_bufHeight;
			modY1 = (cameraY + dConfig.CAMERA_HEIGHT) % m_bufHeight;
		}

		// modX1 = (cameraX + dConfig.CAMERA_WIDTH) % m_bufWidth;
		// modY1 = (cameraY + dConfig.CAMERA_HEIGHT) % m_bufHeight;

		if (bShowMap) {
			if (!useMapBuf) {
				int startGY = (short) (realCamerY / MapData.TILE_HEIGHT); // 求出开始格I
				int startGX = (short) (realCamerX / MapData.TILE_WIDTH); // 求出开始格J
				int offsetY = (byte) (realCamerY % MapData.TILE_HEIGHT); // 偏移量
				int offsetX = (byte) (realCamerX % MapData.TILE_WIDTH); // 偏移量

				drawGround(g, startGX, startGY, offsetX, offsetY);
			} else {
				if (modX1 > modX0) {
					if (modY1 > modY0) {
						copyFromBackImage(g, modX0, modY0,
								dConfig.CAMERA_WIDTH, dConfig.CAMERA_HEIGHT,
								CAMERA_LEFT, CAMERA_TOP);
					} else {
						copyFromBackImage(g, modX0, modY0,
								dConfig.CAMERA_WIDTH, dConfig.CAMERA_HEIGHT
										- modY1, CAMERA_LEFT, CAMERA_TOP);
						copyFromBackImage(g, modX0, 0, dConfig.CAMERA_WIDTH,
								modY1, CAMERA_LEFT,
								(CAMERA_TOP + dConfig.CAMERA_HEIGHT) - modY1);
					}
				} else {
					if (modY1 > modY0) {
						copyFromBackImage(g, modX0, modY0, dConfig.CAMERA_WIDTH
								- modX1, dConfig.CAMERA_HEIGHT, CAMERA_LEFT,
								CAMERA_TOP);
						copyFromBackImage(g, 0, modY0, modX1,
								dConfig.CAMERA_HEIGHT,
								(CAMERA_LEFT + dConfig.CAMERA_WIDTH) - modX1,
								CAMERA_TOP);
					} else {
						copyFromBackImage(g, modX0, modY0, dConfig.CAMERA_WIDTH
								- modX1, dConfig.CAMERA_HEIGHT - modY1,
								CAMERA_LEFT, CAMERA_TOP); // Top-Left
						copyFromBackImage(g, modX0, 0, dConfig.CAMERA_WIDTH
								- modX1, modY1, CAMERA_LEFT,
								(CAMERA_TOP + dConfig.CAMERA_HEIGHT) - modY1); // Bottom-Left
						copyFromBackImage(g, 0, modY0, modX1,
								dConfig.CAMERA_HEIGHT - modY1,
								(CAMERA_LEFT + dConfig.CAMERA_WIDTH) - modX1,
								CAMERA_TOP); // Top-Right
						copyFromBackImage(g, 0, 0, modX1, modY1,
								(CAMERA_LEFT + dConfig.CAMERA_WIDTH) - modX1,
								(CAMERA_TOP + dConfig.CAMERA_HEIGHT) - modY1); // ottom-Right
					}
				}
			}
		}
		// 绘制中间层卷轴
		drawMidLayer(g);

		// 绘制建筑层的信息
		if (ismultilayer && bShowMap) {
			int startGY = (short) (realCamerY / MapData.TILE_HEIGHT); // 求出开始格I
			int startGX = (short) (realCamerX / MapData.TILE_WIDTH); // 求出开始格J
			int offsetY = (byte) (realCamerY % MapData.TILE_HEIGHT); // 偏移量
			int offsetX = (byte) (realCamerX % MapData.TILE_WIDTH); // 偏移量
			drawGround(g, startGX, startGY, offsetX, offsetY);
		}
	}

	/*
	 * 绘制地图层
	 * 
	 * @param g Graphics
	 * 
	 * @param for1Start int
	 * 
	 * @param for1End int
	 * 
	 * @param for2Start int
	 * 
	 * @param for2End int
	 */

	private void drawGround(Graphics g, int startGX, int startGY, int offsetX,
			int offsetY) {
		int temp;
		int moduleID, flip;
		int MLGID = 0, offsetID;
		int dX, dY;

		int for1Start = startGY;
		int for1End = for1Start + (dConfig.S_HEIGHT >> 4);

		int for2Start = startGX;
		int for2End = for2Start + (dConfig.S_WIDTH >> 4);

		for (int i = for1Start; i <= for1End; i++) { // row pointer
			if (i < 0) {
				continue;
			}
			if (i >= mapRes.m_mapHeightByTile) {
				break;
			}
			for (int j = for2Start; j <= for2End; j++) { // row pointer
				if (j < 0) {
					continue;
				}
				if (j >= mapRes.m_mapWidthByTile) {
					break;
				}
				dX = ((j - startGX) * MapData.cellWidth) - offsetX; // 获取[i][j]的格子左上点坐标
				dY = ((i - startGY) * MapData.cellHeight) - offsetY;
				temp = mapRes.m_groundData[i * mapRes.m_mapWidthByTile + j];

				if (temp != -1) {
					temp = temp & 0xff;
				}
				if (!ismultilayer) {
					if (temp > -1) {
						moduleID = mapRes.mi_itemData[temp << 1]; // *2
						flip = mapRes.mi_itemData[(temp << 1) + 1];
						temp = moduleID << 1;
						MLGID = mapRes.mi_moduleData[temp++];
						offsetID = mapRes.mi_moduleData[temp];
						MapData.mapMLGs[MLGID].drawModule(g, offsetID, dX, dY,
								Graphics.TOP | Graphics.LEFT, flip);
					}
				}

				temp = mapRes.m_buildData[i * mapRes.m_mapWidthByTile + j];

				if (temp != -1) {
					temp = temp & 0xff;
				}

				if (temp > -1) {
					moduleID = mapRes.bi_itemData[temp << 1]; // *2
					// lin 09.4.13
					moduleID = moduleID & 0xff;
					flip = mapRes.bi_itemData[(temp << 1) + 1];
					temp = moduleID << 1;

					MLGID = mapRes.bi_moduleData[temp++];

					offsetID = mapRes.bi_moduleData[temp];
					// lin 09.4.13
					offsetID = offsetID & 0xff;
					if (offsetID < 0) {
						int ii = 0;
					}
					MapData.mapMLGs[MLGID].drawModule(g, offsetID, dX, dY,
							Graphics.TOP | Graphics.LEFT, flip);
				}

			}
		}

	}

	/**
	 * 
	 * @param cameraX
	 *            int
	 * @param cameraY
	 *            int
	 * @param g
	 *            Graphics
	 */
	public void showPhyOnly(int cameraX, int cameraY, Graphics g) {
		int tileX0 = cameraX / MapData.TILE_WIDTH;
		int tileX1 = (cameraX + dConfig.CAMERA_WIDTH) / MapData.TILE_WIDTH;
		int tileY0 = cameraY / MapData.TILE_HEIGHT;
		int tileY1 = (cameraY + dConfig.CAMERA_HEIGHT) / MapData.TILE_HEIGHT;

		for (int j = tileY0; j <= tileY1; j++) {
			for (int i = tileX0; i <= tileX1; i++) {
				int drawX = i * MapData.TILE_WIDTH;
				int drawY = j * MapData.TILE_HEIGHT;
				// id = getTile(i, j, 0); // -1 indicates invalid
				g.setClip(0, 0, m_bufWidth, m_bufHeight);

				int id = mapRes.getTilePhyEnv(i, j);
				switch (id) {

				case MapData.PHY_SOLID:
					// if ( CGame.TestFlag ( GetTilePhyEnv ( i, j ),
					// dLevel.PHY_FLAG_TILE_BOX ) )
					if ((CGame.gMap.mapRes.getTilePhyEnv(i, j) & MapData.PHY_FLAG_TILE_BOX) == MapData.PHY_FLAG_TILE_BOX) {
						g.setColor(0xaa00aa);
						g
								.fillRect(drawX - cameraX, drawY - cameraY,
										MapData.TILE_WIDTH - 1,
										MapData.TILE_HEIGHT - 1);
					} else {
						g.setColor(0xaa0000);
						g
								.fillRect(drawX - cameraX, drawY - cameraY,
										MapData.TILE_WIDTH - 1,
										MapData.TILE_HEIGHT - 1);
					}
					g.setColor(0);
					g.drawString(String.valueOf(i), drawX - cameraX, drawY
							- cameraY, dConfig.ANCHOR_LT);
					break;
				case MapData.PHY_SLOP3_1:
					g.setColor(0xffffe0);
					g.fillRect(drawX - cameraX, drawY - cameraY,
							MapData.TILE_WIDTH - 1, MapData.TILE_HEIGHT - 1);
					g.setColor(0);
					g.drawString(String.valueOf(MapData.PHY_SLOP3_1), drawX
							- cameraX, drawY - cameraY, dConfig.ANCHOR_LT);
					break;

				case MapData.PHY_SLOP3_2:
					g.setColor(0xfff000);
					g.fillRect(drawX - cameraX, drawY - cameraY,
							MapData.TILE_WIDTH - 1, MapData.TILE_HEIGHT - 1);
					g.setColor(0);
					g.drawString(String.valueOf(MapData.PHY_SLOP3_2), drawX
							- cameraX, drawY - cameraY, dConfig.ANCHOR_LT);
					break;

				case MapData.PHY_SLOP3_3:
					g.setColor(0xff0e0);
					g.fillRect(drawX - cameraX, drawY - cameraY,
							MapData.TILE_WIDTH - 1, MapData.TILE_HEIGHT - 1);
					g.setColor(0);
					g.drawString(String.valueOf(MapData.PHY_SLOP3_3), drawX
							- cameraX, drawY - cameraY, dConfig.ANCHOR_LT);
					break;

				case MapData.PHY_SLOP2_1:
					g.setColor(0x00ffe0);
					g.fillRect(drawX - cameraX, drawY - cameraY,
							MapData.TILE_WIDTH - 1, MapData.TILE_HEIGHT - 1);
					g.setColor(0);
					g.drawString(String.valueOf(MapData.PHY_SLOP2_1), drawX
							- cameraX, drawY - cameraY, dConfig.ANCHOR_LT);
					break;

				case MapData.PHY_SLOP2_2:
					g.setColor(0x00ffc0);
					g.fillRect(drawX - cameraX, drawY - cameraY,
							MapData.TILE_WIDTH - 1, MapData.TILE_HEIGHT - 1);
					g.setColor(0);
					g.drawString(String.valueOf(MapData.PHY_SLOP2_2), drawX
							- cameraX, drawY - cameraY, dConfig.ANCHOR_LT);
					break;

				case MapData.PHY_SLOPE_BASE:
					g.setColor(0x00f0c0);
					g.fillRect(drawX - cameraX, drawY - cameraY,
							MapData.TILE_WIDTH - 1, MapData.TILE_HEIGHT - 1);
					g.setColor(0);
					g.drawString(String.valueOf(MapData.PHY_SLOPE_BASE), drawX
							- cameraX, drawY - cameraY, dConfig.ANCHOR_LT);
					break;

				case MapData.PHY_HALF_BLOCK:
					g.setColor(0x00feee);
					g.fillRect(drawX - cameraX, drawY - cameraY,
							MapData.TILE_WIDTH - 1, MapData.TILE_HEIGHT - 1);
					break;

				}
			}
		}
	}

	/**
	 * 
	 * @param g
	 *            Graphics
	 * @param modX
	 *            int
	 * @param modY
	 *            int
	 * @param w
	 *            int
	 * @param h
	 *            int
	 * @param screenX
	 *            int
	 * @param screenY
	 *            int
	 */
	public static void copyFromBackImage(Graphics g, int modX, int modY, int w,
			int h, int screenX, int screenY) {
		g.setClip(screenX, screenY, w, h);
		g.drawImage(m_imgBuffer, screenX - modX, screenY - modY, Graphics.TOP
				| Graphics.LEFT);
	}

}
