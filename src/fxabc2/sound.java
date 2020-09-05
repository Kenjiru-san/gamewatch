package fxabc2;

import java.io.File;

public class sound {
	
	boolean _isSound;
	String strFile; 


	File f = new File(strFile);
	soundTh th = new soundTh(f);
}

class soundTh implements Runnable{
	
	soundTh(File f){
	}
	@Override
	public void run() {
		
	}
	
}