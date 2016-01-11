package com.displeyline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
	String mReceivedMsg ="0000 0000"; 
	String receiveMessage[] ;
	float [] points = {0,0};
	float radius =10;
	int tempflag = 0;
	String debug_text = "not connect";
	
	int iLastIndex = 0;
	int bufflength = 1024; 
	float[] pointsLine = new float[bufflength];
	float temp0=0;
	float temp1=0;
    //地图显示参数
	public Parameter_Map para_map;
	
	public DrawView(Context context) {
		super(context);
		para_map = new Parameter_Map();
	}

	/**
	* 这个方法会在初始化后被调用一次,invaildate()的时候会被调用
	*/
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//接收到的坐标点
		receiveMessage = mReceivedMsg.split(" ");
		points[0] = Float.parseFloat(receiveMessage[0]);
		points[1] = Float.parseFloat(receiveMessage[1]);
		//建筑距离转化到地图距离
		points[0] = para_map.convert_buildtoscreen(points[0]);
		points[1] = para_map.convert_buildtoscreen(points[1]);
		
		GetPointsLine(points);
		
		Paint paint=new Paint();//设置一个笔
		paint.setAntiAlias(true);//设置没有锯齿
		paint.setColor(Color.RED);//设置笔的颜色
		canvas.drawCircle(points[0], points[1], radius, paint);//距离画圆
		
		paint.setColor(Color.RED);//设置笔的颜色
		paint.setTextSize(50);
		canvas.drawLines(pointsLine, paint);
		
		canvas.drawText(mReceivedMsg, 10, 50, paint);
		canvas.drawText(String.valueOf(points[0]), 10, 100, paint);
		canvas.drawText(String.valueOf(points[1]), 10, 150, paint);
		canvas.drawText(debug_text +" "+ tempflag, 10, 200, paint);
		
	}
	
	public  void GetPointsLine(float[] fValues){
		if((fValues[0]!=temp0)&&(fValues[1]!=temp1)&&
				(fValues[0]!=0)&&(fValues[1]!=0)){
			if(iLastIndex <4)
			{
				pointsLine[0] = fValues[0];
				pointsLine[1] = fValues[1];
				pointsLine[2] = fValues[0];
				pointsLine[3] = fValues[1];
			}
			else
			{
				pointsLine[iLastIndex-2] = fValues[0];
				pointsLine[iLastIndex-1] = fValues[1];
				pointsLine[iLastIndex] = fValues[0];
				pointsLine[iLastIndex+1] = fValues[1];
				pointsLine[iLastIndex+2] = fValues[0];
				pointsLine[iLastIndex+3] = fValues[1];
			}
			iLastIndex = (iLastIndex+4)%bufflength;
			temp0 = fValues[0];
			temp1 = fValues[1];
		}
		
		//generalTool.saveToSDcard(drawView.points1);
		return;
	}
	
    public class Parameter_Map
    {
    	//实验楼的长和宽是分别是 4400mm 和 1500m
    	float WIDTH_BUILD = 1495;
    	float HEIGHT_BUILD = 4400;
    	float screenWidth = 0;
        float screenHeight = 0;
        //比例  画布的高（像素）/实际长度（毫米）
        float ratio = 0;
        
        void set_paramter_map(int heightPixels)
        {
        	screenHeight = heightPixels;
            screenWidth = (int) (screenHeight*0.3399);   
            ratio = screenHeight/HEIGHT_BUILD;
        }
        float convert_buildtoscreen(float length)
        {
        	return length*ratio;
        }
    }
}

