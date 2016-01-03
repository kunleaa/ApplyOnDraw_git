package com.GPSmap;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SocketService extends Service {
	Thread mServiceThread;  
    
    Socket client;
    ServerSocket server = null;
    
    static float [] StepPoints=new float[2];
    int tempflag = 0;
    public IBinder onBind(Intent intent) {  
        // TODO Auto-generated method stub  
        return null;  
    } 
    
    public void onCreate() {  
        // TODO Auto-generated method stub  
        super.onCreate();  
          
        mServiceThread = new Thread(new SocketServerThread());  
    }  
    
    public void onStart(Intent intent, int startId) {  
        // TODO Auto-generated method stub  
        super.onStart(intent, startId);  
          
        mServiceThread.start();  
    }
   
    
    public void onDestroy() {  
        // TODO Auto-generated method stub  
    	try {
    		if(server!=null)
    			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        super.onDestroy();  
    }
    
    public class SocketServerThread extends Thread {  
        private static final int PORT = 54321;   
        private SocketServerThread(){  
        }  
          
        @Override  
        public void run() {  
            try {  
            	Boolean flag = true; 
                ServerSocket server = new ServerSocket(PORT); 
                while(flag){
	                client = server.accept();
	                
	                PrintWriter writer = new PrintWriter(new BufferedWriter
	                		(new OutputStreamWriter(client.getOutputStream())));
	                
                    writer.println(StepPoints[0]+" "+StepPoints[1]+" "+(tempflag++));  
                    writer.flush();
                    //ÀØ“ª√Î÷”
                    try {
                    	Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					client.close(); 
                }   
                server.close();
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                System.err.println(e);  
            }     
        }  
          
    }  

}
