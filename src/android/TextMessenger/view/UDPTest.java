package android.TextMessenger.view;

import android.app.Activity;
import android.os.Bundle;

public class UDPTest extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		
		Long time_init = System.currentTimeMillis();
		
		String data = "";
		//500KB
		for (int i = 0; i < 1024*500/2; i++) {
			data += "a";
		}
		
		//Segundos de execução - 1 minuto
		int total_exec = 60;
		int num_ite = 0;
		while (num_ite < total_exec) {
			if (System.currentTimeMillis() - time_init >= 1000) {
				time_init = System.currentTimeMillis();
				
				//id_pacote (1) to destination (254)!
				//node.sendData(num_ite, 254, data.getBytes());
				
				num_ite++;				
			}
		}
	}

}
