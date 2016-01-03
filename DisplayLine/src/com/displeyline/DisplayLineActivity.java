package com.displeyline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class DisplayLineActivity extends Activity {
	private static String SERVERIP = "192.168.0.3";//服务器端的IP地址  
    private static final int SERVERPORT = 54321;//发送的端口
    private final int DEBUG = 1;
    private final int DATA = 2;
    private boolean issetipaddr = false;
    Socket socket = null;  
    //接收到的信息  
    String mReceivedMsg; 
    //ip地址
	EditText edit_ipaddr;
    //画图做准备
    private LinearLayout layout;
	DrawView drawView;
	Thread thread_recieve;
	
	private Handler handler = new Handler() {
		 
        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == DEBUG) {
            	drawView.debug_text = String.valueOf(msg.obj);
            }
            else if(msg.what == DATA)
            {
            	drawView.mReceivedMsg = String.valueOf(msg.obj);
            }
            drawView.invalidate();
            drawView.tempflag++;
            super.handleMessage(msg);
        }
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        layout = (LinearLayout)  findViewById(R.id.layout);//找到这个空间
        drawView = new DrawView(this);//创建自定义的控件
        drawView.setMinimumHeight(300);
        drawView.setMinimumWidth(500);
        layout.addView(drawView);//将自定义的控件进行添加
        edit_ipaddr = (EditText) findViewById(R.id.edit_ipaddr);
        edit_ipaddr.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        edit_ipaddr.setOnEditorActionListener(new OnEditorActionListener() {  
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//隐藏键盘
				final InputMethodManager imm = (InputMethodManager)getSystemService(
	            	      Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	            //获取ip地址
				SERVERIP = edit_ipaddr.getText().toString();
				if(true == ipCheck(SERVERIP))
				{
					//设置子线程结束标识
					issetipaddr = true;
					thread_recieve.interrupt();
			        //重启新线程，使用新的IP地址
			        thread_recieve = new Thread(new ReceiveMessage());
			        issetipaddr = false;
			        thread_recieve.start();
					Toast.makeText(DisplayLineActivity.this, "succeed to set ipaddr : "+SERVERIP, Toast.LENGTH_SHORT).show();
					return true;
				}
				else
				{
					Toast.makeText(DisplayLineActivity.this, "failed to set ipaddr: "+SERVERIP, Toast.LENGTH_SHORT).show();
					return false;
				}
			}  
        });  
        if(true == ipCheck(SERVERIP))
        {
        	thread_recieve = new Thread(new ReceiveMessage());
        	thread_recieve.start();
        	Toast.makeText(DisplayLineActivity.this, "succeed to set ipaddr : "+SERVERIP, Toast.LENGTH_SHORT).show();
        }
        else
        {
        	Toast.makeText(DisplayLineActivity.this, "failed to set ipaddr: "+SERVERIP, Toast.LENGTH_SHORT).show();
        }
        //定时器定时显示
        drawView.invalidate();
    }
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	super.onStop();
    }
    
    public class ReceiveMessage implements Runnable{
    	
		public void run() {
			 try {
	               while(issetipaddr != true)
	               {
	            	   handlersendmeg(DEBUG,"connecting");
	            	   socket = new Socket(SERVERIP, SERVERPORT); 
		        	   handlersendmeg(DEBUG,"connected");
		               BufferedReader reader = new BufferedReader(
		            		   new InputStreamReader(socket.getInputStream()));   

	            	   handlersendmeg(DEBUG,"reading");
		               mReceivedMsg = reader.readLine();
		               handlersendmeg(DEBUG,"readed");
		               if(mReceivedMsg != null){  
		            	   drawView.mReceivedMsg=mReceivedMsg;
		            	   handlersendmeg(DATA,mReceivedMsg);
		               }
		               reader.close();
		               socket.close();
	               }
	               issetipaddr = false;
	                
	           } catch (UnknownHostException e) {  
	               // TODO Auto-generated catch block  
	               e.printStackTrace();  
	           } catch (IOException e) {  
	               // TODO Auto-generated catch block  
	               e.printStackTrace();  
	           }  
			
		}
		public void handlersendmeg(int category,String str)
		{
			   Message msg = new Message();
               msg.what = category;
               msg.obj = str;
               handler.sendMessage(msg);
		}
	}
    
    public boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (text.matches(regex)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        // 返回判断信息
        return false;
    }
}