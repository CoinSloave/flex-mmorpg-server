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
//# * ��̬��
//# ***************************************************************************/
//# /**
//# * module �� RGBData�����ݻ����Ĭ�ϴ�С
//# */
//# private static final int RGB_DATA_BUFFER_SIZE = 4000;
//#
//# /**
//# * module �� RGBData�����ݵ�Ĭ�ϴ�С�Ļ����
//# */
//# private static short[] RGBDataBufferDefault = new short[RGB_DATA_BUFFER_SIZE];
//#
//# /**
//# * module �� RGBData�����ݵĻ����
//# */
//# private static short[] RGBDataBuffer;
//#
//# /****************************************************************************
//# * contractionMLG data
//# ***************************************************************************/
//# private boolean isNormal;
//# private short moduleCount; //module����
//# private short[] moduleSize; //width and height
//#
//# //ͼ������Դ
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
//# private int curPalIndex; //��ǰ��ɫ�������
//# private int curPalOffset; //��ǰ��ɫ���� palData ��λ��ƫ��
//# private int curModuleImageOffset; //��ǰ��ɫ���Ӧ��Сͼ�� moduleImage ��λ��ƫ��
//#
//# private int CUR_MASK_COUNT; //mask for count
//# private int CUR_MASK_DATA; //mask for data
//#
//# private boolean bAllReleased; //�Ƿ��ͷ������е���ԴԴ
//#
//# /**
//# * ��ʱʹ��
//# * module width, height
//# */
//# private int width , height;
//#
//# /**
//# * ������
//# */
//# static public int MODULE_COUNT = 0;
//# static public int MODULE_MEMORY_SIZE = 0;
//#
//# public ContractionMLG ()
//# {
//# }
//#
//# /**
//# * ���õ�ǰͼƬ����
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
//# //	    palCount = palCounts[curImageIndex]; //pal����
//# //	    palLength = palLengths[curImageIndex]; //pal����
//# //	    dataBitLength = dataBitLengths[curImageIndex]; //ͼƬ������Ҫ��bit��
//# //	    if ( !bAllReleased )
//# //	    {
//# //		palData = palDatas[curImageIndex]; //����pal����
//# //		moduleOffset = moduleOffsets[curImageIndex]; //mlg����ƫ��
//# //		moduleData = moduleDatas[curImageIndex]; //mlg ��������
//# //	    }
//#
//#
//#
//# //	    moduleImage = moduleImages[curImageIndex];
//# }
//#
//# //��ɫ������
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
//# * ��õ�ǰ����Ϣ
//# * @return short
//# */
//# public short getCurInfo ()
//# {
//# return ( short ) curPalIndex;
//# }
//#
//# /**
//# * ����Ϊ��mapping
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
//# * ���Ƶ�ǰmlg�ĵ� id ����module����x,yλ��
//# * NOTE��
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
//# * ������е�moduleImage ���������е�Сͼ
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
//# //����Ĭ��ֵ
//# setPallette ( 0 );
//# return moduleImage;
//# }
//#
//# /**
//# * ���ݵ�ǰ�ĵ�ɫ���ȡ�ƶ���ŵ�module image
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
//# * ���ָ����module�Ŀ��
//# * @param moduleID int
//# * @return int
//# */
//# public int getModuleWidth ( int moduleID )
//# {
//# return moduleSize[moduleID << 1];
//# }
//#
//# /**
//# * ���ָ����module�ĸ߶�
//# * @param moduleID int
//# * @return int
//# */
//# public int getModuleHeight ( int moduleID )
//# {
//# return moduleSize[ ( moduleID << 1 ) + 1];
//# }
//#
//# /**
//# * ���ݵ�ǰ��ɫ�彫�� id ����module�� RGBData ��������RGBDataBuffer�У��Ա������ŵ�Image����
//# *
//# * @param id int
//# * @param size int
//# */
//# private void stuffRGBDataBufferFromRLE8bits ( int id , int size )
//# {
//# //���Ҫ����������size
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
//# * ���ļ����ж�ȡContractionMLG���󲢳�ʼ��
//# *
//# * @param dis DataInputStream
//# * @throws Exception
//# * @return ContractionMLG
//# */
//# public static ContractionMLG read ( DataInputStream dis ) throws Exception
//# {
//# //�����µ�contractionMLG����
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
//# //����palDatas
//# int size = mlg.palCount * mlg.palLength;
//# mlg.palData = new short[size];
//# for ( int j = 0; j < size; j++ )
//# {
//# mlg.palData[j] = dis.readShort ();
//# }
//# //����moduleOffsets
//# size = mlg.moduleCount + 1;
//# mlg.moduleOffset = new int[size];
//# for ( int j = 0; j < size; j++ )
//# {
//# mlg.moduleOffset[j] = dis.readInt ();
//# }
//# //����moduleDatas
//# mlg.moduleData = new byte[mlg.moduleOffset[mlg.moduleCount]];
//# dis.read ( mlg.moduleData );
//#
//# //��������moduleImages
//# mlg.moduleImage = new short[mlg.moduleCount * mlg.palCount][];
//#
//# mlg.CUR_MASK_COUNT = 0x000000ff >> mlg.dataBitLength;
//# mlg.CUR_MASK_DATA = ~ ( 0xffffffff << mlg.dataBitLength );
//# //��ʼ��mlg
//# mlg.curPalIndex = -1;
//# mlg.setPallette ( 0 );
//#
//# return mlg;
//# }
//#
//# /**
//# * ����mlg
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
//# palData = null; //����pal����
//# moduleOffset = null; //mlg����ƫ��
//# moduleData = null; //mlg ��������
//#
//# moduleImage = null; //���е�moduleImage, ��СΪ��palCount * moduleCount
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
//# * ��̬��
//# ***************************************************************************/
//# /**
//# * module �� RGBData�����ݻ����Ĭ�ϴ�С
//# */
//# private static final int RGB_DATA_BUFFER_SIZE = 4000;
//#
//# /**
//# * module �� RGBData�����ݵ�Ĭ�ϴ�С�Ļ����
//# */
//# private static int[] RGBDataBufferDefault = new int[RGB_DATA_BUFFER_SIZE];
//#
//# /**
//# * module �� RGBData�����ݵĻ����
//# */
//# private static int[] RGBDataBuffer;
//#
//# /****************************************************************************
//# * contractionMLG data
//# ***************************************************************************/
//# private boolean isNormal;
//# private short moduleCount; //module����
//# private short[] moduleSize; //width and height
//#
//# //ͼ����Դ
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
//# private int curPalIndex; //��ǰ��ɫ�������
//# private int curPalOffset; //��ǰ��ɫ���� palData ��λ��ƫ��
//# private int curModuleImageOffset; //��ǰ��ɫ���Ӧ��Сͼ�� moduleImage ��λ��ƫ��
//#
//# private int CUR_MASK_COUNT; //mask for count
//# private int CUR_MASK_DATA; //mask for data
//#
//# private boolean bAllReleased; //�Ƿ��ͷ������е���ԴԴ
//#
//# /**
//# * ��ʱʹ��
//# * module width, height
//# */
//# private int width, height;
//#
//# /**
//# * ������
//# */
//# static public int MODULE_COUNT = 0;
//# static public int MODULE_MEMORY_SIZE = 0;
//#
//# public ContractionMLG () {
//# }
//#
//#
//# /**
//# * ���õ�ǰͼƬ����
//# * @param index int
//# */
//# public void setPallette (int pIndex) {
//# if(pIndex == curPalIndex) {
//# return;
//# }
//# //��ɫ������
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
//# * ��õ�ǰ����Ϣ
//# * @return short
//# */
//# public short getCurInfo () {
//# return(short)curPalIndex;
//# }
//#
//#
//# /**
//# * ����Ϊ��mapping
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
//# * ���Ƶ�ǰmlg�ĵ� id ����module����x,yλ��
//# * NOTE��
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
//# * ������е�moduleImage ���������е�Сͼ
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
//# //����Ĭ��ֵ
//# setPallette (0);
//# return moduleImage;
//# }
//#
//#
//# /**
//# * ���ݵ�ǰ�ĵ�ɫ���ȡ�ƶ���ŵ�module image
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
//# * ���ָ����module�Ŀ��
//# * @param moduleID int
//# * @return int
//# */
//# public int getModuleWidth (int moduleID) {
//# return moduleSize[moduleID << 1];
//# }
//#
//#
//# /**
//# * ���ָ����module�ĸ߶�
//# * @param moduleID int
//# * @return int
//# */
//# public int getModuleHeight (int moduleID) {
//# return moduleSize[ (moduleID << 1) + 1];
//# }
//#
//#
//# /**
//# * ���ݵ�ǰ��ɫ�彫�� id ����module�� RGBData ��������RGBDataBuffer�У��Ա������ŵ�Image����
//# *
//# * @param id int
//# * @param size int
//# */
//# private void stuffRGBDataBufferFromRLE8bits (int id, int size) {
//# //���Ҫ����������size
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
//# * ���ļ����ж�ȡContractionMLG���󲢳�ʼ��
//# *
//# * @param dis DataInputStream
//# * @throws Exception
//# * @return ContractionMLG
//# */
//# public static ContractionMLG read (DataInputStream dis)
//# throws Exception {
//# //�����µ�contractionMLG����
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
//# //����palDatas
//# int size = mlg.palCount * mlg.palLength;
//# mlg.palData = new int[size];
//# for(int j = 0; j < size; j++) {
//# int shortColor = dis.readShort () & 0xffff;
//# mlg.palData[j] = ( ( ( ( (shortColor & 0xF000) >>> 12) * (17 << 24)) & 0xFF000000)
//# | ( ( ( (shortColor & 0x0F00) >>> 8) * (17 << 16)) & 0x00FF0000)
//# | ( ( ( (shortColor & 0x00F0) >>> 4) * (17 << 8)) & 0x0000FF00) | ( ( ( (shortColor & 0x000F) * 17))));
//# }
//# //����moduleOffsets
//# size = mlg.moduleCount + 1;
//# mlg.moduleOffset = new int[size];
//# for(int j = 0; j < size; j++) {
//# mlg.moduleOffset[j] = dis.readInt ();
//# }
//# //����moduleDatas
//# mlg.moduleData = new byte[mlg.moduleOffset[mlg.moduleCount]];
//# dis.read (mlg.moduleData);
//#
//# //��������moduleImages
//# mlg.moduleImage = new Image[mlg.moduleCount * mlg.palCount];
//# mlg.CUR_MASK_COUNT = 0x000000ff >> mlg.dataBitLength;
//# mlg.CUR_MASK_DATA = ~ (0xffffffff << mlg.dataBitLength);
//# //��ʼ��mlg
//# mlg.curPalIndex = -1;
//# mlg.setPallette (0);
//#
//# return mlg;
//# }
//#
//#
//# /**
//# * ����mlg
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
//# palData = null; //����pal����
//# moduleOffset = null; //mlg����ƫ��
//# moduleData = null; //mlg ��������
//#
//# moduleImage = null; //���е�moduleImage, ��СΪ��palCount * moduleCount
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
	private short moduleCount; // module����
	private short[] moduleSize; // width and height
	private short[] moduleXY;

	// imageCount=1��ͼ������Դ
	private byte palCount;
	private short palLength;
	private byte[] palDatas;
	private byte[] imageData;

	private Image[] images; // [palCount]
	private Image[] moduleImages; // [palCount * moduleCount]

	/*********************
	 * current image
	 *********************/
	private int curPalIndex; // ��ǰ��ɫ�������
	private Image curImage; // ��ǰͼ��ָ����ɫ���Ӧ��ͼ

	private boolean bAllReleased;

	public ContractionMLG() {
	}

	/**
	 * ���õ�ǰͼƬ����
	 * 
	 * @param index
	 *            int
	 */
	public void setPallette(int pIndex) {
		if (pIndex == curPalIndex) {
			return;
		}
		// ��ɫ������
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
					// �ͷ���Դ
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
	 * �滻��ǰ�ĵ�ɫ��
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
		// ��ʱindex�Ѿ�ָ��PLTE���len��
		System.arraycopy(pals, palIndex * palByteLen, imageData, index + 4 + 4,
				len);
		updateCRC(imageData, index + 4, 4 + len);
	}

	/**
	 * CRC ��
	 */
	private static int[] CRCTable;

	/**
	 * ��ʼ��crc
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
	 * ����data�е�ָ����λ��crc
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
	 * ��õ�ǰ����Ϣ
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
	 * ���Ƶ�ǰmlg�ĵ� id ����module����x,yλ��
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
		// module�ߴ�
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
	 * ������е�module Image, ���������е�Сͼ
	 * 
	 * @param release
	 *            boolean
	 * @return Image[][]
	 */
	public Image[] getAllModuleImage(boolean release) {
		int mIndex = 0;
		for (int j = 0; j < palCount; j++) {
			setPallette(j);
			// module�ߴ�
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
		// ����Ĭ��ֵ
		curPalIndex = -1;
		setPallette(0);
		return moduleImages;
	}

	/**
	 * ���ָ����module�Ŀ��
	 * 
	 * @param moduleID
	 *            int
	 * @return int
	 */
	public int getModuleWidth(int moduleID) {
		return moduleSize[moduleID << 1];
	}

	/**
	 * ���ָ����module�ĸ߶�
	 * 
	 * @param moduleID
	 *            int
	 * @return int
	 */
	public int getModuleHeight(int moduleID) {
		return moduleSize[(moduleID << 1) + 1];
	}

	/**
	 * ���ļ����ж�ȡContractionMLG_PNG���󲢳�ʼ��
	 * 
	 * @param dis
	 *            DataInputStream
	 * @throws Exception
	 * @return ContractionMLG_PNG
	 */
	public static ContractionMLG read(DataInputStream dis) throws Exception {
		// �����µ�contractionMLG����
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
		// ��������moduleImages
		mlg.moduleImages = new Image[mlg.moduleCount * mlg.palCount];
		// ��ʼ��mlg
		mlg.curPalIndex = -1;
		mlg.setPallette(0);

		return mlg;
	}

	/**
	 * ����mlg
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
