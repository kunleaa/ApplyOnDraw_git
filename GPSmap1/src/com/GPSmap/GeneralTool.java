package com.GPSmap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

public class GeneralTool{
	
	public static void saveToSDcard(float x){
		try {			
			//��׷�ӵķ�ʽ��������ٶȵ�ֵ
			File file = new File(Environment.getExternalStorageDirectory(),"OnDrawData1.txt");
		    //�ڶ�������������˵�Ƿ���append��ʽ��������  
		    BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));  
		    String info = x+"";
		    bw.write(info);
		    bw.write("\r\n"); 
		    bw.flush();  
		    System.out.println("д��ɹ�");  	
		    bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveToSDcard(float x, String filename){
		try {			
			//��׷�ӵķ�ʽ��������ٶȵ�ֵ
			File file = new File(Environment.getExternalStorageDirectory(),filename);
		    //�ڶ�������������˵�Ƿ���append��ʽ��������  
		    BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));  
		    String info = x+"";
		    bw.write(info);
		    bw.write("\r\n"); 
		    bw.flush();  
		    System.out.println("д��ɹ�");
		    bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveToSDcard(float x, float d){
		try {			
			//��׷�ӵķ�ʽ��������ٶȵ�ֵ
			File file = new File(Environment.getExternalStorageDirectory(),"OnDrawData2.txt");
		    //�ڶ�������������˵�Ƿ���append��ʽ��������  
		    BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));  
		    String info = x+"  "+d;
		    bw.write(info);
		    bw.write("\r\n"); 
		    bw.flush();  
		    System.out.println("д��ɹ�");  	
		    bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveToSDcard(float x, float d,String filename){
		try {			
			//��׷�ӵķ�ʽ��������ٶȵ�ֵ
			File file = new File(Environment.getExternalStorageDirectory(),filename);
		    //�ڶ�������������˵�Ƿ���append��ʽ��������  
		    BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));  
		    String info = x+"  "+d;
		    bw.write(info);
		    bw.write("\r\n"); 
		    bw.flush();  
		    System.out.println("д��ɹ�");  	
		    bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveToSDcard(float x, float d, float g){
		try {			
			//��׷�ӵķ�ʽ��������ٶȵ�ֵ
			File file = new File(Environment.getExternalStorageDirectory(),"OnDrawData3.txt");
		    //�ڶ�������������˵�Ƿ���append��ʽ��������  
		    BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));  
		    String info = x+"  "+d+"  "+g;
		    bw.write(info);
		    bw.write("\r\n"); 
		    bw.flush();  
		    System.out.println("д��ɹ�");
		    bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveToSDcard(float x, float d, float g, String FileName){
		try {			
			//��׷�ӵķ�ʽ��������ٶȵ�ֵ
			File file = new File(Environment.getExternalStorageDirectory(),FileName);
		    //�ڶ�������������˵�Ƿ���append��ʽ��������  
		    BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));  
		    String info = x+"  "+d+"  "+g;
		    bw.write(info);
		    bw.write("\r\n"); 
		    bw.flush();  
		    System.out.println("д��ɹ�");  
		    bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveToSDcard(float x, float d, float g, float h){
		try {			
			//��׷�ӵķ�ʽ��������ٶȵ�ֵ
			File file = new File(Environment.getExternalStorageDirectory(),"OnDrawData4.txt");
		    //�ڶ�������������˵�Ƿ���append��ʽ��������  
		    BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));  
		    String info = x+"  "+d+"  "+g+"  "+h;
		    bw.write(info);
		    bw.write("\r\n"); 
		    bw.flush();  
		    System.out.println("д��ɹ�");  	
		    bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//���ݺ�������float����
	static float[] enlarge_float(float [] array, int index)
	{
		float [] temp;
		if(index*4/3 > array.length)
		{
			temp = array;
			array = new float[2*array.length];
			for(int i = 0; i < temp.length; i++)
				array[i] = temp[i];
		}
		return array;
	}
	//���ݺ�������int����
	static int[] enlarge_int(int [] array, int index)
	{
		int [] temp;
		if(index*4/3 > array.length)
		{
			temp = array;
			array = new int[2*array.length];
			for(int i = 0; i < temp.length; i++)
				array[i] = temp[i];
		}
		return array;
	}
	
	//"config_ondraw.txt"
	public static void read2vFromSDcard_value(String filename, float value[],int count){
		int length;
		String str;
		String str_arr[];
		try {			
			File file = new File(Environment.getExternalStorageDirectory(),filename);
			FileInputStream read = new FileInputStream(file);   
			length = read.available();
			byte [] buffer = new byte[length];
			read.read(buffer); 
			str = EncodingUtils.getString(buffer, "UTF-8");   
			str_arr = str.split("\r\n");
			for(int i=0; i < count; i++)
				if(str_arr[i] != null)
			    	value[i] = Float.parseFloat(str_arr[i]);
			
		    System.out.println("read�ɹ�");  	
		    read.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Boolean removefile(String filename)
	{
		File file = new File(Environment.getExternalStorageDirectory(),filename);
        if (file.isFile() && file.exists()) {
        return file.delete();
        }
        return false;
	}
}