package com.GPSmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.LinearLayout;

public class OnDrawActivity extends Activity {
	int time_delay = 10;
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
            	int heigth = layout.getHeight();
            	if(heigth == 0)
            	{
            		removeMessages(msg.what);
            		sendMessageDelayed(obtainMessage(msg.what),time_delay);
            	}
            	else
            	{
	    			layout.setLayoutParams(new LinearLayout.LayoutParams((int)(heigth*0.3399), heigth));
	    			drawView.para_map.set_paramter_map(heigth);
            	}
            }
            super.handleMessage(msg);
        }
    };
	
	//�������豸���
	private SensorManager manager;
	private SensorListener listener = new SensorListener();
	Data_Sensor data_sensor= new Data_Sensor();
	//��ͼ��ʾ
	private LinearLayout layout;
	DrawView drawView;
	//��ͼ�ؼ�
	Controller_View controller_view;
	//�������
	Config config = new Config();
	//����ͨ�ŷ���
	SocketService socketSc =new SocketService();
	//�������
	Selector_Model selector_model = new Selector_Model();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //����service ��ΪSOCKETͨ�ŵķ�������
        this.startService(new Intent(this, SocketService.class));
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_2);
        
        layout = (LinearLayout)  findViewById(R.id.background);//�ҵ�����ռ�
        drawView = new DrawView(this);//�����Զ���Ŀؼ�
        layout.addView(drawView);//���Զ���Ŀؼ��������
        
        controller_view = new Controller_View(this,selector_model,config,drawView);
        //��sd�������ò���ʾ������
        config.Read_SDtoView(controller_view);
        //�������������
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		//�������»���
		drawView.IsInvalidate();
		
        //���õ�ͼ�����ʵ���ֵ
        Message msg = new Message();
        msg.what = 0;
        handler.sendMessageDelayed(msg,time_delay);
    }
    
    protected void onResume() {
		//�������ٶȴ�����TYPE_ACCELEROMETER
    	Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	manager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
	
    	//�������򴫸���
    	Sensor orientation = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    	manager.registerListener(listener, orientation, SensorManager.SENSOR_DELAY_GAME);

    	super.onResume();
	}

    protected void onStop(){
    	manager.unregisterListener(listener);
    	super.onStop();
    }
    
    protected void onDestroy() {  
        super.onDestroy();   
        this.stopService(new Intent(this, SocketService.class));  
    }  
    
	private final class SensorListener implements SensorEventListener{
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
				data_sensor.set_accle(event.values);
				//������ʾ����Ļ��
				//�ֻ������µ���ֵ��values����ʾ�ܵ���A�ķ�������B��������ϵ��ʾA
				drawView.SetAcceleration_1(data_sensor.use_acc()[1], -data_sensor.use_acc()[0], -data_sensor.use_acc()[2]);
				//������У׼������
				selector_model.selectfunction(config,data_sensor,controller_view,drawView);
				drawView.trajectory.setpaintdata();
			}
			else if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
				 data_sensor.set_orien(event.values);
				 //������ʾ����Ļ��
				 drawView.SetOrientation_1(data_sensor.use_ori_trans()[0], data_sensor.use_ori_trans()[1], data_sensor.use_ori_trans()[2]);
			}
			//�������»���
			drawView.IsInvalidate();
		}
	}
}