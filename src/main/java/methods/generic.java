package methods;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Properties;

import org.bson.Document;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;

public class generic {
	public static String output="";		
	

	public static String dbhostname = "localhost";
//	public static String databaseName="nokia_project_telco360_topology";
	public static int dbportno = 27017;

	static String serverHostname="localhost";
	static String serverUsername="root";
	public static String serverPassword="Pind1234#";
	//public static String serverKeyFile="/home/admin/xplorg/ssh_key/id_rsa_pem";
	public static String logpath="/home/admin/xplorg/checkconnectivity/logs/";

	public static String filespath="/home/admin/xplorg/zambia/sysNames/";
	public static String timezone="GMT+3";
	public static String opco="Kenya";
	public static String dbpassword = "Telco360@123";
	public static String dbusername = "root";
	public static String dbauthentication = "admin";
	public static int dbPort = 27017;

	
	public static MongoClient getConnection(String host) {
		try {	
		//System.setProperty ("javax.net.ssl.trustStore",trustStorePath);
		//System.setProperty ("javax.net.ssl.trustStorePassword",trustStorePassword);
		//System.setProperty ("javax.net.ssl.keyStore",keyStorePath);
		//System.setProperty ("javax.net.ssl.keyStorePassword",keyStorePassword);	
		//Mongodb connection string.
		String clientUrl = "mongodb://"+host+":"+dbportno+"/?ssl=false";
		MongoClientURI uri = new MongoClientURI(clientUrl);
		//Connecting to the mongodb server using the given client uri.
		MongoClient mongo = new MongoClient(uri);
		return mongo;
		}catch(Exception ex) {
		ex.printStackTrace();	
		}
		return null;
		}

		public static MongoClient getConnectionauth(String host) {
		try {	
		//System.setProperty ("javax.net.ssl.trustStore",trustStorePath);
		//System.setProperty ("javax.net.ssl.trustStorePassword",trustStorePassword);
		//System.setProperty ("javax.net.ssl.keyStore",keyStorePath);
		//System.setProperty ("javax.net.ssl.keyStorePassword",keyStorePassword);	
		//Mongodb connection string.
			 String clientUrl = "mongodb://" + dbusername + ":" + URLEncoder.encode(dbpassword, "UTF-8") + "@" + host + ":" + dbPort + "/" + dbauthentication + "?ssl=false";
			 MongoClientURI uri = new MongoClientURI(clientUrl);
			 MongoClient mongo = new MongoClient(uri);//Connecting to the mongodb server using the given client uri.
		return mongo;
		}catch(Exception ex) {
		ex.printStackTrace();	
		}
		return null;
		}


public static void closeConnection(MongoClient mongo) {
try {	
mongo.close();
}catch(Exception ex) {
ex.printStackTrace();	
}
}

public static Session getSession(String ipaddressnatted,String username,String password) throws Exception{
JSch jsch = new JSch();
Session session = jsch.getSession(username, ipaddressnatted, 22);
Properties config = new Properties();
config.put("StrictHostKeyChecking", "no");
session.setConfig(config);;
session.setPassword(password);
session.connect();

return session;
}

public static Session getSessionKey(String ipaddress,String username,String path){
Session session = null;
try {
JSch jsch = new JSch();
jsch.addIdentity(path);	    
session = jsch.getSession(username, ipaddress,22);
java.util.Properties config = new java.util.Properties(); 
config.put("StrictHostKeyChecking", "no");
session.setConfig(config);
session.connect();
}catch (Exception ex) {
ex.printStackTrace();
}
return session;	

}



public static String localSystemInfo(MongoCollection<Document>collection,Session session,String version,String username,String authentication_type,String authentication_passphrase,String privacy_type,String privacy_passphrase,String ipaddressnatted) {
try {	
String locSysName="";
if(version.equalsIgnoreCase("V3")) {
String command1="snmpbulkwalk -v3 -l authPriv -u "+username+" -a "+authentication_type+" -A "+authentication_passphrase+" -x "+privacy_type+" -X "+privacy_passphrase+" "+ipaddressnatted+" sysName";
CommandTask task1=new CommandTask(session,command1);
task1.run();
{
BufferedReader br=new BufferedReader(new StringReader(output.replaceAll("\n"," ")));

String line="";

while((line=br.readLine())!=null) {
if(line.contains("sysName") && !line.contains("failure") && !line.contains("Timeout")) {
locSysName=line.substring(line.lastIndexOf("STRING:")+7).replace(",", " ").replace("\"", "").trim();
}
}
}
}

if(version.equalsIgnoreCase("V2C")) {
String command1="snmpbulkwalk -v2c -c "+username+" "+ipaddressnatted+" sysName";
CommandTask task1=new CommandTask(session,command1);
task1.run();
{
BufferedReader br=new BufferedReader(new StringReader(output.replaceAll("\n"," ")));

String line="";

while((line=br.readLine())!=null) {
if(line.contains("sysName") && !line.contains("failure") && !line.contains("Timeout")) {
locSysName=line.substring(line.lastIndexOf("STRING:")+7).replace(",", " ").replace("\"", "").trim();
}
}
}
}

return locSysName;
}catch(Exception ex) {
//logger.error("Exception occurs:----"+ex.getMessage(),ex);
ex.printStackTrace();	
}	
return null;
}


}
