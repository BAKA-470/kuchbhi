package methods;

import java.util.ArrayList;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class main extends generic{

	public static void main(String[] args) {
		
		
		
		ArrayList<String>opcos_list=new ArrayList<String>();
		opcos_list.add("zambia");
		opcos_list.add("congo");
		opcos_list.add("madagascar");
		opcos_list.add("gabon");
		opcos_list.add("uganda");
		opcos_list.add("seychelles");
		opcos_list.add("niger");
		opcos_list.add("chad");
		opcos_list.add("malawi");

		ArrayList<String>opcos_db_ips_list=new ArrayList<String>();
		opcos_db_ips_list.add("10.182.200.191");
		opcos_db_ips_list.add("10.182.200.191");
		opcos_db_ips_list.add("10.182.200.246");
		opcos_db_ips_list.add("10.182.200.246");
		opcos_db_ips_list.add("10.182.200.164");
		opcos_db_ips_list.add("10.182.200.164");
		opcos_db_ips_list.add("10.182.200.215");
		opcos_db_ips_list.add("10.182.200.215");
		opcos_db_ips_list.add("10.182.199.61");

				




		Thread thrd[]=new Thread[opcos_list.size()];

		ArrayList combine_output[]=new ArrayList[opcos_list.size()];


		for(int i=0;i<opcos_list.size();i++) {
			try {
				
				
				
				
				
				
				
				
				
				
				
				combine_output[i]=new ArrayList<String>();
				
				thrd[i]=new Thread(new opco_task(opcos_list.get(i),opcos_db_ips_list.get(i)));	

				thrd[i].start();
				
				} catch (Exception e2) {
				
				e2.printStackTrace();
			}
		}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

ArrayList<String>threadStatus=new ArrayList<String>();
while(true) {
for(int j=0;j<thrd.length;j++) {
if(!thrd[j].isAlive() && threadStatus.indexOf("THREAD-"+j)<0) {
threadStatus.add("THREAD-"+j);	
}
}

if(threadStatus.size()==thrd.length) {
System.gc();
System.out.println("All Task Completed");

break;
	
}



}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		}

		public static Session getSession(String ipaddress,String username,String password){
		try {	
		JSch jsch = new JSch();
		Session session = jsch.getSession(username, ipaddress, 22);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);;
		session.setPassword(password);
		try {
		session.connect();
		}catch(Exception ex) {
		ex.printStackTrace();
		}
		return session;
		}catch(Exception ex) {
		ex.printStackTrace();	
		}
		return null;
		}

	}


