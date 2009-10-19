package game.pak;

import game.CGame;
import game.config.dConfig;
import game.object.CObject;

import com.mglib.mdl.map.MapDraw;

public class Camera {
	// --------------------------------------------------------------------------
	/**
	 * 游戏中镜头控制的部分
	 */
	public static int cameraCenterX = dConfig.CAMERA_WIDTH / 2;
	public static int cameraCenterY = dConfig.CAMERA_HEIGHT / 2;
	// camera info
	public static int cameraLeft;
	public static int cameraTop;
	public static short[] cameraBox = new short[4]; // 记录镜头的大小

	public static short[] lockCameraBox = new short[4]; // 锁定屏幕的区域
	public static boolean isLockInArea; // 是否在一定区域内锁定屏幕锁定屏幕
	public final static byte TOP = 1;
	public final static byte LEFT = 0;
	public final static byte BOTTOM = 3;
	public final static byte RIGHT = 2;

	public static CObject cammeraObj;

	/**
	 * 更新镜头的方法
	 * 
	 * @param bSmoothly
	 *            表示是否要平滑的移动镜头
	 */
	public static void updateCamera(boolean bSmoothly) {
		// 设置镜头的中心点
		int left = cameraCenterX - dConfig.CAMERA_WIDTH / 2;
		int top = cameraCenterY - dConfig.CAMERA_HEIGHT / 2;
		int cameraLeftTemp = cameraLeft;
		if (isLockInArea) {
			if (left < lockCameraBox[LEFT]) {
				left = lockCameraBox[LEFT];
			}
			if (left >= lockCameraBox[RIGHT] - dConfig.CAMERA_WIDTH) {
				left = lockCameraBox[RIGHT] - dConfig.CAMERA_WIDTH - 1;
			}
			if (top < lockCameraBox[TOP]) {
				top = lockCameraBox[TOP];
			}
			if (top >= lockCameraBox[BOTTOM] - dConfig.CAMERA_HEIGHT) {
				top = lockCameraBox[BOTTOM] - dConfig.CAMERA_HEIGHT - 1;
			}
			// 修正锁定区域大于地图场景的问题
			if (left < 0) {
				left = 0;
			}
			if (left >= CGame.gMap.mapRes.m_mapTotalWidthByPixel
					- dConfig.CAMERA_WIDTH) {
				left = CGame.gMap.mapRes.m_mapTotalWidthByPixel
						- dConfig.CAMERA_WIDTH - 1;
			}

			if (top < 0) {
				top = 0;
			}
			if (top >= CGame.gMap.mapRes.m_mapTotalHeightByPixel
					- dConfig.CAMERA_HEIGHT) {
				top = CGame.gMap.mapRes.m_mapTotalHeightByPixel
						- dConfig.CAMERA_HEIGHT - 1;
			}

		} else {
			if (left < 0) {
				left = 0;
			}
			if (left >= CGame.gMap.mapRes.m_mapTotalWidthByPixel
					- dConfig.CAMERA_WIDTH) {
				left = CGame.gMap.mapRes.m_mapTotalWidthByPixel
						- dConfig.CAMERA_WIDTH - 1;
			}

			if (top < 0) {
				top = 0;
			}
			if (top >= CGame.gMap.mapRes.m_mapTotalHeightByPixel
					- dConfig.CAMERA_HEIGHT) {
				top = CGame.gMap.mapRes.m_mapTotalHeightByPixel
						- dConfig.CAMERA_HEIGHT - 1;
			}
		}

		if (bSmoothly) {
			int dx = left - cameraLeft;
			int dy = top - cameraTop;
			int abs_dx = dx >= 0 ? dx : -dx;
			int abs_dy = dy >= 0 ? dy : -dy;
			int k;
			if (abs_dx > abs_dy) {
				if (abs_dx > 30) {
					k = (dy << dConfig.FRACTION_BITS) / dx;
					dx = dx > 0 ? 30 : -30;
					dy = (dx * k) >> dConfig.FRACTION_BITS;
				}
			} else if (abs_dx < abs_dy) {
				if (abs_dy > 30) {
					k = (dx << dConfig.FRACTION_BITS) / dy;
					dy = dy > 0 ? 30 : -30;
					dx = (dy * k) >> dConfig.FRACTION_BITS;
				}
			}
			// 镜头速度很小就不除2 modify by lin
			if (Math.abs(dx) > 5) {
				cameraLeft += dx / 2;
			} else {
				cameraLeft += dx;
			}
			cameraTop += dy / 2;
		} else {
			cameraLeft = left;
			cameraTop = top;
			cameraCenterX = left + dConfig.CAMERA_WIDTH / 2;
			cameraCenterY = top + dConfig.CAMERA_HEIGHT / 2;
		}

		cameraBox[0] = (short) cameraLeft;
		cameraBox[1] = (short) cameraTop;
		cameraBox[2] = (short) (cameraLeft + dConfig.CAMERA_WIDTH);
		cameraBox[3] = (short) (cameraTop + dConfig.CAMERA_HEIGHT);

		// 地图中间层卷轴
		if (CGame.testSceneFlag(CGame.SCENE_FLAG_SCROLL_AT_SCENE)
				|| CGame.testSceneFlag(CGame.SCENE_FLAG_SCROLL_AT_SCREEN)) {
			int increase = (Math.abs(cameraLeftTemp - cameraLeft) < 2) ? (cameraLeftTemp - cameraLeft)
					: (cameraLeftTemp - cameraLeft) / 2;
			MapDraw.midLayerOffsetX += increase;
			MapDraw.midLayerOffsetX %= MapDraw.midLayerImg.getWidth();
		}

	}

	public static void setCameraCenter(int posX, int posY) {
		cameraCenterX = posX;
		cameraCenterY = posY;
	}

}
