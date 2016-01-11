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
    //��ͼ��ʾ����
	public Parameter_Map para_map;
	
	public DrawView(Context context) {
		super(context);
		para_map = new Parameter_Map();
	}

	/**
	* ����������ڳ�ʼ���󱻵���һ��,invaildate()��ʱ��ᱻ����
	*/
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//���յ��������
		receiveMessage = mReceivedMsg.split(" ");
		points[0] = Float.parseFloat(receiveMessage[0]);
		points[1] = Float.parseFloat(receiveMessage[1]);
		//��������ת������ͼ����
		points[0] = para_map.convert_buildtoscreen(points[0]);
		points[1] = para_map.convert_buildtoscreen(points[1]);
		
		GetPointsLine(points);
		
		Paint paint=new Paint();//����һ����
		paint.setAntiAlias(true);//����û�о��
		paint.setColor(Color.RED);//���ñʵ���ɫ
		canvas.drawCircle(points[0], points[1], radius, paint);//���뻭Բ
		
		paint.setColor(Color.RED);//���ñʵ���ɫ
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
    	//ʵ��¥�ĳ��Ϳ��Ƿֱ��� 4400mm �� 1500m
    	float WIDTH_BUILD = 1495;
    	float HEIGHT_BUILD = 4400;
    	float screenWidth = 0;
        float screenHeight = 0;
        //����  �����ĸߣ����أ�/ʵ�ʳ��ȣ����ף�
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

