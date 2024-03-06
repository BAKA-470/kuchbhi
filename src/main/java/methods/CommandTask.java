package methods;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

public class CommandTask extends generic implements Runnable{

Session session;
String cmd;

public CommandTask(Session session,String cmd){
this.session=session;
this.cmd=cmd;
}

public void run()  {
try {

//path where we have check the latest folder

StringBuilder result=new StringBuilder();

//command to check latest folder

String command=cmd;  

Channel channel = session.openChannel("exec");

((ChannelExec)channel).setCommand(command);
channel.setInputStream(null);
((ChannelExec)channel).setErrStream(System.err);
((ChannelExec)channel).setPty(true);
InputStream input = channel.getInputStream();
channel.connect();

try{
InputStreamReader inputReader = new InputStreamReader(input);
BufferedReader bufferedReader = new BufferedReader(inputReader);
String line = null;

while((line = bufferedReader.readLine()) != null){
result.append(line+"\n");
}
output=result.toString();

bufferedReader.close();
inputReader.close();
}catch(Exception ex){
//logger.error("Exception occurs:----"+ex.getMessage(),ex);
ex.printStackTrace();
}
channel.disconnect();
} catch (Exception ex) {
//logger.error("Exception occurs:----"+ex.getMessage(),ex);
ex.printStackTrace();
}

}

}
