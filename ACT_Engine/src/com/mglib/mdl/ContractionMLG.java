//#if draw_pixel
//# import java.io.DataInputStream;
//# import javax.microedition.lcdui.Graphics;
//#
//# import com.nokia.mid.ui.DirectGraphics;
//# import com.nokia.mid.ui.DirectUtils;
//#
//# /**
//# * <p>Title: engine of RPG game</p>
//# * <p>Description: contraction mlg for a png file + modules</p>
//# * <p>Copyright: Copyright (c) 2007</p>
//# * <p>Company: mig</p>
//# * @author lianghao chen
//# * @version 1.0
//# * @version 2.0
//# * NOTE:
//# *  Format: MLG, draw_pixel
//# */
//# public class ContractionMLG
//# {
//# static{System.out.println(">>ContractionMLG format is 'draw_pixel'.");}
//# /****************************************************************************
//# * 静态量
//# ***************************************************************************/
//# /**
//# * module 的 RGBData的数据缓冲池默认大小
//# */
//# private static final int RGB_DATA_BUFFER_SIZE = 4000;
//#
//# /**
//# * module 的 RGBData　数据的默认大小的缓冲池
//# */
//# private static short[] RGBDataBufferDefault = new short[RGB_DATA_BUFFER_SIZE];
//#
//# /**
//# * module 的 RGBData　数据的缓冲池
//# */
//# private static short[] RGBDataBuffer;
//#
//# /****************************************************************************
//# * contractionMLG data
//# ***************************************************************************/
//# private boolean isNormal;
//# private short moduleCount; //module数量
//# private short[] moduleSize; //width and height
//#
//# //图的数据源
//# private byte palCount;
//# private short palLength;
//# private byte dataBitLength;
//# private short[] palData;
//# private int[] moduleOffset;
//# private byte[] moduleData;
//#
//# public short[][] moduleImage;
//#
//# /****************************************************************************
//# * current image model
//# ***************************************************************************/
//# private int curPalIndex; //当前调色板的索引
//# private int curPalOffset; //当前调色板在 palData 的位置偏移
//# private int curModuleImageOffset; //当前调色板对应的小图在 moduleImage 的位置偏移
//#
//# private int CUR_MASK_COUNT; //mask for count
//# private int CUR_MASK_DATA; //mask for data
//#
//# private boolean bAllReleased; //是否释放了所有的资源源
//#
//# /**
//# * 临时使用
//# * module width, height
//# */
//# private int width , height;
//#
//# /**
//# * 调试用
//# */
//# static public int MODULE_COUNT = 0;
//# static public int MODULE_MEMORY_SIZE = 0;
//#
//# public ContractionMLG ()
//# {
//# }
//#
//# /**
//# * 设置当前图片索引
//# * @param index int
//# */
//# public void setPallette ( int pIndex )
//# {
//# if ( pIndex == curPalIndex )
//# {
//# return;
//# }
//#
//# {
//# //	    palCount = palCounts[curImageIndex]; //pal数量
//# //	    palLength = palLengths[curImageIndex]; //pal长度
//# //	    dataBitLength = dataBitLengths[curImageIndex]; //图片索引需要的bit数
//# //	    if ( !bAllReleased )
//# //	    {
//# //		palData = palDatas[curImageIndex]; //所有pal数据
//# //		moduleOffset = moduleOffsets[curImageIndex]; //mlg数据偏移
//# //		moduleData = moduleDatas[curImageIndex]; //mlg 索引数据
//# //	    }
//#
//#
//#
//# //	    moduleImage = moduleImages[curImageIndex];
//# }
//#
//# //调色板索引
//# if ( pIndex < 0 || pIndex >= palCount )
//# {
//# throw new IllegalArgumentException ( "ContractionMLG->setImage():Pallette index is illegal!" );
//# }
//# if ( pIndex != curPalIndex )
//# {
//# curPalIndex = pIndex;
//# curPalOffset = curPalIndex * palLength;
//# curModuleImageOffset = curPalIndex * moduleCount;
//# }
//# }
//#
//# /**
//# * 获得当前的信息
//# * @return short
//# */
//# public short getCurInfo ()
//# {
//# return ( short ) curPalIndex;
//# }
//#
//# /**
//# * 设置为被mapping
//# * @param srcMlg ContractionMLG
//# * @param palIndex int
//# */
//# public void setMapping ( ContractionMLG srcMlg , int palIndex )
//# {
//# if ( !isNormal )
//# {
//# this.moduleSize = srcMlg.moduleSize;
//# this.setPallette ( palIndex );
//# }
//# }
//#
//# /**
//# * 绘制当前mlg的第 id 个　module　在x,y位置
//# * NOTE：
//# *  when use ContractionMLG on Nokia width 'drawPixels()', the 'anchor' is meaningless.
//# *
//# * @param g Graphics
//# * @param id int
//# * @param x int
//# * @param y int
//# * @param anchor int
//# * @param flip int
//# */
//# public void drawModule ( Graphics g , int id , int x , int y , int anchor , int translateFlip )
//# {
//# width = moduleSize[id << 1];
//# height = moduleSize[ ( id << 1 ) + 1];
//# if ( moduleImage[curModuleImageOffset + id] == null )
//# {
//# moduleImage[curModuleImageOffset + id] = getModuleImage ( id , width * height );
//# }
//# if ( moduleImage[curModuleImageOffset + id] != null )
//# {
//# DirectUtils.getDirectGraphics ( g ).drawPixels ( moduleImage[curModuleImageOffset + id] , true , 0 , width , x , y , width , height , CGame.COOLEDIT_2_NOKIA[translateFlip] , DirectGraphics.TYPE_USHORT_4444_ARGB );
//# }
//# }
//#
//# /**
//# * 获得所有的moduleImage 创建出所有的小图
//# *
//# * @return javax.microedition.lcdui.Image[]
//# */
//# public short[][] getAllModuleImage ( boolean release )
//# {
//# int size;
//# {
//# for ( int i = 0; i < palCount; i++ )
//# {
//# setPallette ( i );
//# for ( int id = 0; id < moduleCount; id++ )
//# {
//# if ( moduleImage[curModuleImageOffset + id] == null )
//# {
//# size = moduleSize[id << 1] * moduleSize[ ( id << 1 ) + 1];
//# moduleImage[curModuleImageOffset + id] = getModuleImage ( id , size );
//# //debug
//# if ( CDebug.showDebugInfo )
//# {
//# MODULE_COUNT++;
//# MODULE_MEMORY_SIZE += size * 2 / 3;
//# }
//# }
//# }
//# }
//# }
//# if ( CDebug.showDebugInfo )
//# {
//# System.out.println ( "\tMLG module image current total count=" + MODULE_COUNT );
//# System.out.println ( "\tMLG module image current total memory size=" + MODULE_MEMORY_SIZE );
//# }
//# //release all data after create all module image
//# bAllReleased = release;
//# if ( bAllReleased )
//# {
//# palData = null;
//# moduleOffset = null;
//# moduleData = null;
//# palData = null;
//# moduleOffset = null;
//# moduleData = null;
//# }
//# //设置默认值
//# setPallette ( 0 );
//# return moduleImage;
//# }
//#
//# /**
//# * 根据当前的调色板获取制定编号的module image
//# *
//# * @param id int
//# * @return Image
//# */
//# public short[] getModuleImage ( int id , int size )
//# {
//# stuffRGBDataBufferFromRLE8bits ( id , size );
//# short[] shortARGBData = new short[size];
//# System.arraycopy ( RGBDataBuffer , 0 , shortARGBData , 0 , shortARGBData.length );
//# return shortARGBData;
//# }
//#
//# /**
//# * 获得指定的module的宽度
//# * @param moduleID int
//# * @return int
//# */
//# public int getModuleWidth ( int moduleID )
//# {
//# return moduleSize[moduleID << 1];
//# }
//#
//# /**
//# * 获得指定的module的高度
//# * @param moduleID int
//# * @return int
//# */
//# public int getModuleHeight ( int moduleID )
//# {
//# return moduleSize[ ( moduleID << 1 ) + 1];
//# }
//#
//# /**
//# * 根据当前调色板将第 id 个的module的 RGBData 的填塞进RGBDataBuffer中，以备紧接着的Image创建
//# *
//# * @param id int
//# * @param size int
//# */
//# private void stuffRGBDataBufferFromRLE8bits ( int id , int size )
//# {
//# //检查要填塞得数据size
//# if ( size < RGB_DATA_BUFFER_SIZE )
//# {
//# RGBDataBuffer = RGBDataBufferDefault;
//# }
//# else
//# {
//# RGBDataBuffer = new short[size];
//# }
//# //get data
//# int pos = 0;
//# int count;
//# short data;
//# for ( int i = moduleOffset[id]; i < moduleOffset[id + 1]; i++ )
//# {
//# count = ( ( moduleData[i] >> dataBitLength ) & CUR_MASK_COUNT ) + 1;
//# data = palData[curPalOffset + ( moduleData[i] & CUR_MASK_DATA )];
//# while ( count-- > 0 )
//# {
//# RGBDataBuffer[pos++] = data;
//# }
//# }
//# }
//#
//# /**
//# * 从文件流中读取ContractionMLG对象并初始化
//# *
//# * @param dis DataInputStream
//# * @throws Exception
//# * @return ContractionMLG
//# */
//# public static ContractionMLG read ( DataInputStream dis ) throws Exception
//# {
//# //创建新的contractionMLG对象
//# ContractionMLG mlg = new ContractionMLG ();
//# //
//# mlg.isNormal = ( dis.readByte () == 0 );
//# mlg.moduleCount = dis.readShort ();
//# if ( mlg.isNormal )
//# {
//# mlg.moduleSize = new short[mlg.moduleCount << 1];
//# for ( int i = 0; i < mlg.moduleCount; i++ )
//# {
//# mlg.moduleSize[i << 1] = dis.readShort ();
//# mlg.moduleSize[ ( i << 1 ) + 1] = dis.readShort ();
//# }
//# }
//#
//# mlg.palCount = dis.readByte ();
//# mlg.palLength = dis.readShort ();
//# mlg.dataBitLength = dis.readByte ();
//# //数组palDatas
//# int size = mlg.palCount * mlg.palLength;
//# mlg.palData = new short[size];
//# for ( int j = 0; j < size; j++ )
//# {
//# mlg.palData[j] = dis.readShort ();
//# }
//# //数组moduleOffsets
//# size = mlg.moduleCount + 1;
//# mlg.moduleOffset = new int[size];
//# for ( int j = 0; j < size; j++ )
//# {
//# mlg.moduleOffset[j] = dis.readInt ();
//# }
//# //数组moduleDatas
//# mlg.moduleData = new byte[mlg.moduleOffset[mlg.moduleCount]];
//# dis.read ( mlg.moduleData );
//#
//# //缓冲数组moduleImages
//# mlg.moduleImage = new short[mlg.moduleCount * mlg.palCount][];
//#
//# mlg.CUR_MASK_COUNT = 0x000000ff >> mlg.dataBitLength;
//# mlg.CUR_MASK_DATA = ~ ( 0xffffffff << mlg.dataBitLength );
//# //初始化mlg
//# mlg.curPalIndex = -1;
//# mlg.setPallette ( 0 );
//#
//# return mlg;
//# }
//#
//# /**
//# * 销毁mlg
//# */
//# public void destroy ()
//# {
//# RGBDataBuffer = null;
//#
//# moduleSize = null; //width and height
//# //	palCounts = null;
//# //	palLengths = null;
//# //	dataBitLengths = null;
//# palData = null;
//# moduleOffset = null;
//# moduleData = null;
//# moduleImage = null;
//#
//# palData = null; //所有pal数据
//# moduleOffset = null; //mlg数据偏移
//# moduleData = null; //mlg 索引数据
//#
//# moduleImage = null; //所有的moduleImage, 大小为：palCount * moduleCount
//# }
//# }
//#
//#elif draw_image
//# package com.mglib.mdl;
//#
//# import java.io.*;
//# import javax.microedition.lcdui.*;
//# import com.mglib.mdl.map.MapData;
//# import game.CGame;
//# import game.CDebug;
//#
//#
//# /**
//# * <p>Title: engine of RPG game</p>
//# * <p>Description: contraction mlg for a png file + modules</p>
//# * <p>Copyright: Copyright (c) 2007</p>
//# * <p>Company: mig</p>
//# * @author lianghao chen
//# * @version 1.0
//# * @version 2.0
//# * NOTE:
//# *  Format: MLG, draw_image
//# */
//# public class ContractionMLG {
//# static {
//# System.out.println (">>ContractionMLG format is 'draw_image'.");
//# }
//#
//#
//# /****************************************************************************
//# * 静态量
//# ***************************************************************************/
//# /**
//# * module 的 RGBData的数据缓冲池默认大小
//# */
//# private static final int RGB_DATA_BUFFER_SIZE = 4000;
//#
//# /**
//# * module 的 RGBData　数据的默认大小的缓冲池
//# */
//# private static int[] RGBDataBufferDefault = new int[RGB_DATA_BUFFER_SIZE];
//#
//# /**
//# * module 的 RGBData　数据的缓冲池
//# */
//# private static int[] RGBDataBuffer;
//#
//# /****************************************************************************
//# * contractionMLG data
//# ***************************************************************************/
//# private boolean isNormal;
//# private short moduleCount; //module数量
//# private short[] moduleSize; //width and height
//#
//# //图数据源
//# private byte palCount;
//# private short palLength;
//# private byte dataBitLength;
//# private int[] palData;
//# private int[] moduleOffset;
//# private byte[] moduleData;
//# public Image[] moduleImage;
//#
//# /****************************************************************************
//# * current image model
//# ***************************************************************************/
//# private int curPalIndex; //当前调色板的索引
//# private int curPalOffset; //当前调色板在 palData 的位置偏移
//# private int curModuleImageOffset; //当前调色板对应的小图在 moduleImage 的位置偏移
//#
//# private int CUR_MASK_COUNT; //mask for count
//# private int CUR_MASK_DATA; //mask for data
//#
//# private boolean bAllReleased; //是否释放了所有的资源源
//#
//# /**
//# * 临时使用
//# * module width, height
//# */
//# private int width, height;
//#
//# /**
//# * 调试用
//# */
//# static public int MODULE_COUNT = 0;
//# static public int MODULE_MEMORY_SIZE = 0;
//#
//# public ContractionMLG () {
//# }
//#
//#
//# /**
//# * 设置当前图片索引
//# * @param index int
//# */
//# public void setPallette (int pIndex) {
//# if(pIndex == curPalIndex) {
//# return;
//# }
//# //调色板索引
//# if(pIndex < 0 || pIndex >= palCount) {
//# throw new IllegalArgumentException ("ContractionMLG->setPallette():Pallette index is illegal!");
//# }
//# if(curPalIndex != pIndex) {
//# curPalIndex = pIndex;
//#
//# curPalOffset = curPalIndex * palLength;
//# curModuleImageOffset = curPalIndex * moduleCount;
//# }
//# }
//#
//#
//# /**
//# * 获得当前的信息
//# * @return short
//# */
//# public short getCurInfo () {
//# return(short)curPalIndex;
//# }
//#
//#
//# /**
//# * 设置为被mapping
//# * @param srcMlg ContractionMLG
//# * @param palIndex int
//# */
//# public void setMapping (ContractionMLG srcMlg, int palIndex) {
//# if(!isNormal) {
//# this.moduleSize = srcMlg.moduleSize;
//# this.setPallette (palIndex);
//# }
//# }
//#
//#
//# /**
//# * 绘制当前mlg的第 id 个　module　在x,y位置
//# * NOTE：
//# *  when use ContractionMLG on Nokia width 'drawPixels()', the 'anchor' is meaningless.
//# *
//# * @param g Graphics
//# * @param id int
//# * @param x int
//# * @param y int
//# * @param anchor int
//# * @param flip int
//# */
//# public void drawModule (Graphics g, int id, int x, int y, int anchor, int translateFlip) {
//# if(MapData.mapMLGs[0] == this) {
//# System.out.println ("");
//# }
//# width = moduleSize[id << 1];
//# height = moduleSize[ (id << 1) + 1];
//# if(moduleImage[curModuleImageOffset + id] == null) {
//# moduleImage[curModuleImageOffset + id] = getModuleImage (id, width, height);
//# }
//# if(moduleImage[curModuleImageOffset + id] != null) {
//# CGame.gdraw.drawImage (g, moduleImage[curModuleImageOffset + id], 0, 0, width, height, x, y, anchor, translateFlip);
//# }
//# }
//#
//#
//# /**
//# * 获得所有的moduleImage 创建出所有的小图
//# *
//# * @return javax.microedition.lcdui.Image[]
//# */
//# public Image[] getAllModuleImage (boolean release) {
//# int w, h;
//# for(int i = 0; i < palCount; i++) {
//# setPallette (i);
//# for(int id = 0; id < moduleCount; id++) {
//# if(moduleImage[curModuleImageOffset + id] == null) {
//# w = moduleSize[id << 1];
//# h = moduleSize[ (id << 1) + 1];
//# moduleImage[curModuleImageOffset + id] = getModuleImage (id, w, h);
//# //debug
//# if(CDebug.showDebugInfo) {
//# MODULE_COUNT++;
//# MODULE_MEMORY_SIZE += w * h * 2 / 3;
//# }
//# }
//# }
//# }
//# //release all data after create all module image
//# bAllReleased = release;
//# if(bAllReleased) {
//# palData = null;
//# moduleOffset = null;
//# moduleData = null;
//# palData = null;
//# moduleOffset = null;
//# moduleData = null;
//# }
//# //设置默认值
//# setPallette (0);
//# return moduleImage;
//# }
//#
//#
//# /**
//# * 根据当前的调色板获取制定编号的module image
//# *
//# * @param id int
//# * @return Image
//# */
//# public Image getModuleImage (int id, int width, int height) {
//# stuffRGBDataBufferFromRLE8bits (id, width * height);
//# return Image.createRGBImage (RGBDataBuffer, width, height, true);
//# }
//#
//#
//# /**
//# * 获得指定的module的宽度
//# * @param moduleID int
//# * @return int
//# */
//# public int getModuleWidth (int moduleID) {
//# return moduleSize[moduleID << 1];
//# }
//#
//#
//# /**
//# * 获得指定的module的高度
//# * @param moduleID int
//# * @return int
//# */
//# public int getModuleHeight (int moduleID) {
//# return moduleSize[ (moduleID << 1) + 1];
//# }
//#
//#
//# /**
//# * 根据当前调色板将第 id 个的module的 RGBData 的填塞进RGBDataBuffer中，以备紧接着的Image创建
//# *
//# * @param id int
//# * @param size int
//# */
//# private void stuffRGBDataBufferFromRLE8bits (int id, int size) {
//# //检查要填塞得数据size
//# if(size < RGB_DATA_BUFFER_SIZE) {
//# RGBDataBuffer = RGBDataBufferDefault;
//# }
//# else {
//# RGBDataBuffer = new int[size];
//# }
//# //get data
//# int pos = 0;
//# int count;
//# int data;
//# for(int i = moduleOffset[id]; i < moduleOffset[id + 1]; i++) {
//# count = ( (moduleData[i] >> dataBitLength) & CUR_MASK_COUNT) + 1;
//# data = palData[curPalOffset + (moduleData[i] & CUR_MASK_DATA)];
//# while(count-- > 0) {
//# RGBDataBuffer[pos++] = data;
//# }
//# }
//# }
//#
//#
//# /**
//# * 从文件流中读取ContractionMLG对象并初始化
//# *
//# * @param dis DataInputStream
//# * @throws Exception
//# * @return ContractionMLG
//# */
//# public static ContractionMLG read (DataInputStream dis)
//# throws Exception {
//# //创建新的contractionMLG对象
//# ContractionMLG mlg = new ContractionMLG ();
//# //
//# mlg.isNormal = (dis.readByte () == 0);
//# mlg.moduleCount = dis.readShort ();
//# if(mlg.isNormal) {
//# mlg.moduleSize = new short[mlg.moduleCount<<1];
//# for(int i = 0; i < mlg.moduleCount; i++) {
//# mlg.moduleSize[i << 1] = dis.readShort ();
//# mlg.moduleSize[ (i << 1) + 1] = dis.readShort ();
//# }
//# }
//# mlg.palCount = dis.readByte ();
//# mlg.palLength = dis.readShort ();
//# mlg.dataBitLength = dis.readByte ();
//# //数组palDatas
//# int size = mlg.palCount * mlg.palLength;
//# mlg.palData = new int[size];
//# for(int j = 0; j < size; j++) {
//# int shortColor = dis.readShort () & 0xffff;
//# mlg.palData[j] = ( ( ( ( (shortColor & 0xF000) >>> 12) * (17 << 24)) & 0xFF000000)
//# | ( ( ( (shortColor & 0x0F00) >>> 8) * (17 << 16)) & 0x00FF0000)
//# | ( ( ( (shortColor & 0x00F0) >>> 4) * (17 << 8)) & 0x0000FF00) | ( ( ( (shortColor & 0x000F) * 17))));
//# }
//# //数组moduleOffsets
//# size = mlg.moduleCount + 1;
//# mlg.moduleOffset = new int[size];
//# for(int j = 0; j < size; j++) {
//# mlg.moduleOffset[j] = dis.readInt ();
//# }
//# //数组moduleDatas
//# mlg.moduleData = new byte[mlg.moduleOffset[mlg.moduleCount]];
//# dis.read (mlg.moduleData);
//#
//# //缓冲数组moduleImages
//# mlg.moduleImage = new Image[mlg.moduleCount * mlg.palCount];
//# mlg.CUR_MASK_COUNT = 0x000000ff >> mlg.dataBitLength;
//# mlg.CUR_MASK_DATA = ~ (0xffffffff << mlg.dataBitLength);
//# //初始化mlg
//# mlg.curPalIndex = -1;
//# mlg.setPallette (0);
//#
//# return mlg;
//# }
//#
//#
//# /**
//# * 销毁mlg
//# */
//# public void destroy () {
//# RGBDataBuffer = null;
//#
//# moduleSize = null; //width and height
//# //	palCounts = null;
//# //	palLengths = null;
//# //	dataBitLengths = null;
//# palData = null;
//# moduleOffset = null;
//# moduleData = null;
//# moduleImage = null;
//#
//# palData = null; //所有pal数据
//# moduleOffset = null; //mlg数据偏移
//# moduleData = null; //mlg 索引数据
//#
//# moduleImage = null; //所有的moduleImage, 大小为：palCount * moduleCount
//# }
//# }
//#
//#else
package com.mglib.mdl;

import game.CGame;

import java.io.DataInputStream;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * <p>
 * Title: engine of RPG game
 * </p>
 * <p>
 * Description: contraction mlg for a png file + modules
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: mig
 * </p>
 * 
 * @author lianghao chen
 * @version 1.0
 * @version 2.0
 * @version 3.0 2008-7-23 NOTE: Format: PNG this is special for ns60 old vision
 *          because of the speed, now we use the PNG directly.
 */
public class ContractionMLG {
	static {
		System.out.println(">>ContractionMLG format is 'draw_png'.");
	}

	/****************************************************************************
	 * contractionMLG data model
	 ***************************************************************************/
	private boolean isNormal;
	private short moduleCount; // module数量
	private short[] moduleSize; // width and height
	private short[] moduleXY;

	// imageCount=1个图的数据源
	private byte palCount;
	private short palLength;
	private byte[] palDatas;
	private byte[] imageData;

	private Image[] images; // [palCount]
	private Image[] moduleImages; // [palCount * moduleCount]

	/*********************
	 * current image
	 *********************/
	private int curPalIndex; // 当前调色板的索引
	private Image curImage; // 当前图的指定调色板对应的图

	private boolean bAllReleased;

	public ContractionMLG() {
	}

	/**
	 * 设置当前图片索引
	 * 
	 * @param index
	 *            int
	 */
	public void setPallette(int pIndex) {
		if (pIndex == curPalIndex) {
			return;
		}
		// 调色板索引
		if (pIndex < 0 || pIndex >= palCount) {
			throw new IllegalArgumentException(
					"ContractionMLG->setPallette():Pallette index is illegal!");
		}
		if (pIndex != curPalIndex) {
			curPalIndex = pIndex;
			curImage = null;
			if (!bAllReleased) {
				if (images[curPalIndex] != null) {
					curImage = images[curPalIndex];
				} else {
					replacePallette(imageData, palDatas, curPalIndex, palLength);
					curImage = images[curPalIndex] = Image.createImage(
							imageData, 0, imageData.length);
					// 释放资源
					for (int i = 0; i < palCount; i++) {
						if (images[i] == null) {
							return;
						}
					}
					palDatas = null;
					imageData = null;
				}
			}
		}
	}

	/**
	 * 替换当前的调色板
	 */
	private void replacePallette(byte[] imageData, byte[] pals, int palIndex,
			int palByteLen) {
		int index = 8;
		int len = 0;
		// find PLTE chunck index
		while (true) {
			len = ((imageData[index] & 0xff) << 24)
					+ ((imageData[index + 1] & 0xff) << 16)
					+ ((imageData[index + 2] & 0xff) << 8)
					+ (imageData[index + 3] & 0xff);
			if (imageData[index + 4] == 80 && imageData[index + 5] == 76
					&& imageData[index + 6] == 84 && imageData[index + 7] == 69) {
				break;
			} else {
				index += 4 + 4 + len + 4;
			}
		}
		// 此时index已经指向PLTE块的len处
		System.arraycopy(pals, palIndex * palByteLen, imageData, index + 4 + 4,
				len);
		updateCRC(imageData, index + 4, 4 + len);
	}

	/**
	 * CRC 表
	 */
	private static int[] CRCTable;

	/**
	 * 初始化crc
	 */
	private static void initCRCTable() {
		int c;
		int n, k;
		CRCTable = new int[256];
		for (n = 0; n < 256; n++) {
			c = n;
			for (k = 0; k < 8; k++) {
				if ((c & 1) == 1) {
					c = 0xedb88320 ^ (c >>> 1);
				} else {
					c = c >>> 1;
				}
			}
			CRCTable[n] = c;
		}
	}

	/**
	 * 更新data中的指定部位的crc
	 * 
	 * @param data
	 *            byte[]
	 * @param off
	 *            int
	 * @param len
	 *            int
	 * @return int
	 */
	private static void updateCRC(byte[] data, int off, int len) {
		int c = 0xffffffff;
		int crcIndex = len + off;
		if (CRCTable == null) {
			initCRCTable();
		}
		for (int n = off; n < crcIndex; n++) {
			c = CRCTable[(c ^ data[n]) & 0xff] ^ (c >>> 8);
		}
		int crc = c ^ 0xffffffff;
		data[crcIndex] = (byte) ((0xFF000000 & crc) >> 24);
		data[crcIndex + 1] = (byte) ((0x00FF0000 & crc) >> 16);
		data[crcIndex + 2] = (byte) ((0x0000FF00 & crc) >> 8);
		data[crcIndex + 3] = (byte) (0xff & crc);
	}

	/**
	 * 获得当前的信息
	 * 
	 * @return short
	 */
	public short getCurInfo() {
		return (short) curPalIndex;
	}

	public void setMapping(ContractionMLG srcMlg, int palIndex) {
		if (!isNormal) {
			this.moduleSize = srcMlg.moduleSize;
			this.moduleXY = srcMlg.moduleXY;
			this.setPallette(palIndex);
		}
	}

	/**
	 * 绘制当前mlg的第 id 个　module　在x,y位置
	 * 
	 * @param g
	 *            Graphics
	 * @param id
	 *            int
	 * @param x
	 *            int
	 * @param y
	 *            int
	 * @param anchor
	 *            int
	 * @param flip
	 *            int
	 */
	public void drawModule(Graphics g, int id, int x, int y, int anchor,
			int translateFlip) {
		// module尺寸
		int mw, mh;
		mw = moduleSize[id << 1];
		mh = moduleSize[(id << 1) + 1];
		if (curImage == null) {
			CGame.gdraw.drawImage(g, moduleImages[id + moduleCount
					* curPalIndex], 0, 0, mw, mh, x, y, anchor, translateFlip);
		} else {
			int mx, my;
			mx = moduleXY[id << 1];
			my = moduleXY[(id << 1) + 1];
			CGame.gdraw.drawImage(g, curImage, mx, my, mw, mh, x, y, anchor,
					translateFlip);
		}
	}

	/**
	 * 获得所有的module Image, 创建出所有的小图
	 * 
	 * @param release
	 *            boolean
	 * @return Image[][]
	 */
	public Image[] getAllModuleImage(boolean release) {
		int mIndex = 0;
		for (int j = 0; j < palCount; j++) {
			setPallette(j);
			// module尺寸
			int mx, my, mw, mh;
			for (int k = 0; k < moduleCount; k++) {
				mx = moduleXY[k << 1];
				my = moduleXY[(k << 1) + 1];
				mw = moduleSize[k << 1];
				mh = moduleSize[(k << 1) + 1];
				moduleImages[mIndex++] = Image.createImage(curImage, mx, my,
						mw, mh, 0);
			}
		}
		bAllReleased = release;
		// release all data after create all module image
		if (release) {
			moduleXY = null;
			palDatas = null;
			imageData = null;
			images = null;

			curImage = null;
		}
		// 设置默认值
		curPalIndex = -1;
		setPallette(0);
		return moduleImages;
	}

	/**
	 * 获得指定的module的宽度
	 * 
	 * @param moduleID
	 *            int
	 * @return int
	 */
	public int getModuleWidth(int moduleID) {
		return moduleSize[moduleID << 1];
	}

	/**
	 * 获得指定的module的高度
	 * 
	 * @param moduleID
	 *            int
	 * @return int
	 */
	public int getModuleHeight(int moduleID) {
		return moduleSize[(moduleID << 1) + 1];
	}

	/**
	 * 从文件流中读取ContractionMLG_PNG对象并初始化
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return ContractionMLG_PNG
	 */
	public static ContractionMLG read(DataInputStream dis) throws Exception {
		// 创建新的contractionMLG对象
		ContractionMLG mlg = new ContractionMLG();
		mlg.isNormal = (dis.readByte() == 0);
		mlg.moduleCount = dis.readShort();
		if (mlg.isNormal) {
			mlg.moduleSize = new short[mlg.moduleCount << 1];
			for (int i = 0; i < mlg.moduleCount; i++) {
				mlg.moduleSize[i << 1] = dis.readShort();
				mlg.moduleSize[(i << 1) + 1] = dis.readShort();
			}
			mlg.moduleXY = new short[mlg.moduleCount << 1];
			for (int i = 0; i < mlg.moduleCount; i++) {
				mlg.moduleXY[i << 1] = dis.readShort();
				mlg.moduleXY[(i << 1) + 1] = dis.readShort();
			}
		}
		mlg.palCount = dis.readByte();
		mlg.images = new Image[mlg.palCount];
		mlg.palLength = dis.readShort();
		mlg.palDatas = new byte[mlg.palCount * mlg.palLength];
		dis.read(mlg.palDatas);
		int length = dis.readInt();
		mlg.imageData = new byte[length];
		dis.read(mlg.imageData);
		// 缓冲数组moduleImages
		mlg.moduleImages = new Image[mlg.moduleCount * mlg.palCount];
		// 初始化mlg
		mlg.curPalIndex = -1;
		mlg.setPallette(0);

		return mlg;
	}

	/**
	 * 销毁mlg
	 */
	public void destroy() {
		moduleSize = null;
		moduleXY = null;

		palDatas = null;
		imageData = null;

		images = null;
		moduleImages = null;

		palDatas = null;
		imageData = null;
		curImage = null;
		moduleImages = null;
	}
}

// #endif
