package methods;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.bson.Document;

import com.jcraft.jsch.Session;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class opco_task extends generic implements Runnable{

	
	public opco_task(String opco,String ip) {
		super();
		this.opco = opco;
		this.ip = ip;

	}



String ip;
	String opco;
	
	
	
	
	public void run() {
		// TODO Auto-generated method stub
		MongoClient mongo=null;
		String databaseName="";
		StringBuilder sbb_no=new StringBuilder();

		
		ArrayList<String>vendor=new ArrayList<String>();	
		ArrayList<String>domain=new ArrayList<String>();
		ArrayList<String>hostname=new ArrayList<String>();
		ArrayList<String>hostname_physical=new ArrayList<String>();
		ArrayList<String>version=new ArrayList<String>();
		ArrayList<String>security_name=new ArrayList<String>();
		ArrayList<String>authentication_type=new ArrayList<String>();
		ArrayList<String>authentication_passphrase=new ArrayList<String>();
		ArrayList<String>privacy_type=new ArrayList<String>();
		ArrayList<String>privacy_passphrase=new ArrayList<String>();
		ArrayList<String>nename=new ArrayList<String>();

		try {
			if(opco.toUpperCase().equals("ZAMBIA"))
			{
				databaseName="nokia_project_telco360_topology";
			}
			else
			{

				databaseName=opco+"_nokia_project_telco360_topology";
				
			}
			
			if(opco.toUpperCase().equals("ZAMBIA")||opco.toUpperCase().equals("CONGO"))
			{
				mongo=getConnection(ip);
			
			}
			else
			{
				mongo=getConnectionauth(ip);
				
			}			
		MongoDatabase database=mongo.getDatabase(databaseName);
		MongoCollection<Document>collection=database.getCollection("connectivitydetails");
		ArrayList<Document>resultSet=collection.find(Filters.eq("permissions_snmp","yes")).into(new ArrayList<Document>());

		for(Document doc:resultSet) {
		vendor.add(doc.getString("vendor"));
		domain.add(doc.getString("domain"));
		hostname.add(doc.getString("hostname(fm)"));
		hostname_physical.add(doc.getString("hostname_physical(fm)"));
		version.add(doc.getString("version(fm)"));
		security_name.add(doc.getString("community"));
		authentication_type.add(""+doc.getString("authentication_type"));
		authentication_passphrase.add(""+doc.getString("authentication_passphrase"));
		privacy_type.add(""+doc.getString("privacy_type"));
		privacy_passphrase.add(""+doc.getString("privacy_passphrase"));
		nename.add(doc.getString("nename"));
		}
		boolean boln=false;
		sbb_no.append("Opco,Domain,Vendor,Physical IP Address,Natted IP Address,Old_NEName,New_NEName"+"\n");

		Session session=getSession(ip, serverUsername, serverPassword);
		if(session.isConnected()) {
		for(int j=0;j<hostname.size();j++) {
		String sysName=localSystemInfo(collection,session,version.get(j),security_name.get(j),authentication_type.get(j),authentication_passphrase.get(j),privacy_type.get(j),privacy_passphrase.get(j), hostname.get(j));
		System.out.println(opco+"======="+hostname.get(j)+"=========="+sysName);
	
		sbb_no.append(opco+","+domain.get(j)+","+vendor.get(j)+","+hostname_physical.get(j)+","+hostname.get(j)+","+nename.get(j)+","+sysName+"\n");
	
		
		}
		}
		session.disconnect();
	
		
        File csvFile = new File(filespath+opco+"_sysname.csv");
        FileWriter fileWriter = new FileWriter(csvFile);

	
    fileWriter.write(sbb_no.toString());
    //fileWriter.write("/n");


fileWriter.close();

		closeConnection(mongo);
		} catch (Exception e) {
		//TODO Auto-generated catch block
		e.printStackTrace();
		}
	}

}
