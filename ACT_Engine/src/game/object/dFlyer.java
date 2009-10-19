package game.object;

public interface dFlyer {
	/****************************************************
	 * 数据库索引
	 ***************************************************/
	// 1.轨迹的基础数据
	// path_base_data[][]：
	// [轨迹编号]
	// [offset_x0,offet_y0,offset_x1,offet_y1...offset_xn,offet_yn]
	// public final static int OFFSET_X = 0; //X相对偏移量
	// public final static int OFFSET_Y = 1; //Y相对偏移量

	// 2.轨迹组数据
	// path_group_data[][][]:
	// [对象编号ID]
	// [对象的轨迹编号]
	// [轨迹编号ID,轨迹循环次数];
	public final static int DATA_PATH_ID = 0; // 轨迹编号
	public final static int DATA_PATH_LOOP = 1; // 循环次数
	public final static int DATA_PATH_STATE = 2; // 循环次数

	// 3.子弹基础数据
	// bullet_base_data[][][]：
	// [子弹组编号ID]
	// [每个子弹]
	// [子弹类型（固定、锁定、跟踪、镜面、S型），对象ID, 动画ID, 攻击力, 速度角度,
	// 沿角度方向的速度, 特效ID, 加速度, 加速度角度, 子弹相对于中心点偏移X,
	// 子弹相对于中心点偏移Y, 循环播放, 子弹是否检测物理层(0:否; 1:是), 存时间, 子弹是否能抵消,
	// 是否跟随主人坐标, 子弹动画的方向数量, 永远不死]
	public final static byte DATA_INFO_BULLET_LENGTH = 14;// 子弹数据库中属性大小

	public final static int DATA_INFO_BULLET_TYPE = 0; // 子弹类型（固定、锁定、跟踪、镜面、S型）
	public final static int DATA_INFO_BULLET_ANIMATION_ID = 1; // 对象ID
	public final static int DATA_INFO_BULLET_ACTION_ID = 2; // 当前动画ID
	public final static int DATA_INFO_BULLET_POWER = 3; // 攻击力
	public final static int DATA_INFO_BULLET_ANGLE = 4; // 速度角度
	public final static int DATA_INFO_BULLET_SPEED = 5; // 沿角度方向的速度
	public final static int DATA_INFO_BULLET_DIE_ID = 6; // 子弹消亡特效ID
	public final static int DATA_INFO_BULLET_AX = 7; // 加速度
	public final static int DATA_INFO_BULLET_AY = 8; // 加速度角度
	public final static int DATA_INFO_BULLET_OFFSET_X = 9; // 子弹相对于中心点偏移x
	public final static int DATA_INFO_BULLET_OFFSET_Y = 10; // 子弹相对于中心点偏移y
	public final static int DATA_INFO_BULLET_FLAG = 11; // 子弹相对于中心点偏移y
	public final static int DATA_INFO_BULLET_LIVETIME = 12; // 生存时间 如果>0
															// 表示会消失的,为1的时候消失.如果==0
															// 就一直不消失一直存在，如果<-1
															// 就暂时不执行其他逻辑一直前进。
	public final static int DATA_INFO_BULLET_NUM_DIR = 13; // 子弹动画的方向数量

	// 子弹flag
	public final static int DATA_INFO_BULLET_FLAG_ISCOLLIDE = 1 << 5; // 子弹是否检测物理层(0:否;
																		// 1:是)
																		// NO(咱们用不上)
	public final static int DATA_INFO_BULLET_FLAG_ISCIRCLE = 1 << 4; // 循环播放
	public final static int DATA_INFO_BULLET_FLAG_ISKILL = 1 << 3; // 子弹是否能抵消
	public final static int DATA_INFO_BULLET_FLAG_ISFOLLOW = 1 << 2; // 是否跟随主人坐标
	public final static int DATA_INFO_BULLET_FLAG_NEVER_DIE = 1 << 1;// 永远不死，激光类的子弹，打到物体时不死
	public final static int DATA_INFO_BULLET_FLAG_ISBACKUP = 1 << 0;// isBackup

	// 4.子弹发射器数据
	// bullet_group_data[][][]:
	// [发射器ID]
	// [对象的子弹编号]
	// [子弹组ID, 发射此子弹频率, 剩余弹药数量(-1无限,固定数量), 发射时间];
	public final static byte DATA_BULLET_LENGTH = 18;// 子弹数据库中属性大小

	public final static int DATA_BULLET_ID = 0; // 子弹组Id
	public final static int DATA_BULLET_FRE = 1; // 发射此子弹的频率
	public final static int DATA_BULLET_NUMBER = 2; // 弹药剩余数量(-1无限,固定数量);
	public final static int DATA_BULLET_SHOOT_TIME = 3; // 发射时间

	/****************************************************
	 * 使用索引
	 ***************************************************/
	// 1.对象的轨迹使用
	// 某个对象的轨迹 actor_path[][]:
	// [轨迹编号]
	// [轨迹编号ID, 轨迹循环次数, 已经执行次数, 已经执行到哪];
	public final static int PATH_ID = 0; // 轨迹编号
	public final static int PATH_LOOP = 1; // 循环次数
	public final static int PATH_FLOOP = 2; // 已经执行次数
	public final static int PATH_INDEX = 3; // 已经执行到哪
	public final static int PATH_STATE = 4; // 路径状态

	// 2.对象的子弹使用
	// 对象的弹药 actor_bullet[][]:
	// [子弹编号]
	// [子弹组ID, 发射此子弹频率, 弹药数量(-1无限,固定数量), 子弹的发射时间];
	public final static int BULLET_ID = 0; // 子弹组Id
	public final static int BULLET_FRE = 1; // 发射此子弹的频率
	public final static int BULLET_NUMBER = 2; // 弹药剩余数量(-1无限,固定数量);
	public final static int BULLET_SHOOT_TIME = 3; // 此子弹的发射时间

	// 3.子弹属性数据
	public final static int INFO_BULLET_LENGHT = 30;

	public final static int INFO_BULLET_TYPE = 0; // 子弹类型（固定、锁定、跟踪、镜面、S型）
	public final static int INFO_BULLET_ANIMATION_ID = 1; // 对象ID
	public final static int INFO_BULLET_ACTION_ID = 2;// 子弹actionID
	public final static int INFO_BULLET_POWER = 3; // 攻击力
	public final static int INFO_BULLET_ANGLE = 4; // 速度角度
	public final static int INFO_BULLET_SPEED = 5; // 沿角度方向的速度
	public final static int INFO_BULLET_DIE_ID = 6;// 子弹死亡actionID.
	public final static int INFO_BULLET_AX = 7; // 加速度x
	public final static int INFO_BULLET_AY = 8; // 加速度y
	public final static int INFO_BULLET_X = 9; // 子弹x
	public final static int INFO_BULLET_Y = 10; // 子弹y
	public final static int INFO_BULLET_FLAG = 11; // 子弹flag
	public final static int INFO_BULLET_LIVE_TIME = 12; // 生存时间 如果>0
														// 表示会消失的,为1的时候消失.如果==0
														// 就一直不消失一直存在，如果<-1
														// 就暂时不执行其他逻辑一直前进。
	public final static int INFO_BULLET_NUM_DIR = 13;// 子弹的方向数
	public final static int INFO_BULLET_FOLLOW_HOSTID = 14; // 跟随主人的IDInObjList，-1表示没有跟随，100表示跟随主角，>0表示跟随的对象
	public final static int INFO_BULLET_DEPEND = 15; // 子弹所属(0主角,1敌人)
	public final static int INFO_BULLET_LOCKED_OBJID = 16; // 跟随子弹锁定的的对象在objList中的编号
	public final static int INFO_BULLET_SPEED_X = 17; // 子弹速度x
	public final static int INFO_BULLET_SPEED_Y = 18; // 子弹速度y
	public final static int INFO_BULLET_SQUENCE_INDEX = 19; // 子弹动画祯控制
	public final static int INFO_BULLET_FRAME_DELAY = 20; // 子弹动画祯控制
	public final static int INFO_BULLET_STATE = 21; // 子弹状态
	public final static int INFO_BULLET_DIR = 22; // 子弹方向
	public final static int INFO_BULLET_STATE_TIME = 23; // 子弹状态时间

	public final static int INFO_BULLET_CX0 = 24; // 子弹碰状
	public final static int INFO_BULLET_CY0 = 25; //
	public final static int INFO_BULLET_CX1 = 26; //
	public final static int INFO_BULLET_CY1 = 27; //
	public final static int INFO_BULLET_OFFSET_X = 28; // 子弹相对于中心点偏移x
	public final static int INFO_BULLET_OFFSET_Y = 29; // 子弹相对于中心点偏移y
	// 子弹flag
	public final static short INFO_BULLET_FLAG_ISCOLLIDE = 1 << 5; // 子弹是否检测物理层(0:否;
																	// 1:是)
																	// NO(咱们用不上)
	public final static short INFO_BULLET_FLAG_ISCIRCLE = 1 << 4; // 循环播放
	public final static short INFO_BULLET_FLAG_ISKILL = 1 << 3; // 子弹是否能抵消
	public final static short INFO_BULLET_FLAG_ISFOLLOW = 1 << 2; // 是否跟随主人坐标
	public final static short INFO_BULLET_FLAG_NEVER_DIE = 1 << 1;// 永远不死，激光类的子弹，打到物体时不死
	public final static short INFO_BULLET_FLAG_ISBACKUP = 1 << 0;// isBackup

	/***************************************
	 * 属性数据定义的定义
	 **************************************/
	// 子弹类型定义
	public final static byte BULLET_TYPE_FIXATION = 0;// 固定
	public final static byte BULLET_TYPE_LOCKED = 1;// 锁定
	public final static byte BULLET_TYPE_FOLLOW = 2;// 跟踪
	public final static byte BULLET_TYPE_MIRROR = 3;// 镜面
	public final static byte BULLET_TYPE_S = 4;// S型

	// 跟随主人的IDInObjList
	public final static byte BULLET_FOLOW_HOSTID_NO_FOLLOW = -1;// 表示没有跟随
	public final static byte BULLET_FOLOW_HOSTID_FOLLOW_HERO = -2;// 表示跟随主角

	// 子弹所属关系
	public final static byte BULLET_DEPEND_HERO = 0;
	public final static byte BULLET_DEPEND_ENEMY = 1;

	// 子弹初始位置定义
	public static final byte BULLET_Y_FROM_SCREEN_BOTTOM = -1;// 子弹从屏幕底边出y
	public static final byte BULLET_Y_RANDOM_IN_SCREEN = -2;// 子弹从屏幕随即出y
	public static final byte BULLET_X_FROM_SCREEN_RIGHT = -1;// 子弹从屏幕右边出x
	public static final byte BULLET_X_RANDOM_IN_SCREEN = -2;// 子弹从屏幕底边出x

}
