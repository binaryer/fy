
import org.apache.commons.io.IOUtils

import groovy.json.JsonSlurper
import jodd.http.HttpRequest
import jodd.http.HttpResponse

class Fy { 
	
	static final def MIN_PRINTLN_MS = 250;
	static final def MAX_PRINTLN_MS = 10000;
	
	
	static final def EXITCODE_HTTP = 1;
	static final def EXITCODE_NON200 = 2;
	static final def EXITCODE_JSON = 3;

	static slowlyPrint(String str){
		
		println str;
		
		def sleepms = 30;
		if(sleepms * str.length() < MIN_PRINTLN_MS)
			sleepms = MIN_PRINTLN_MS / str.length();
		else if(sleepms * str.length() > MAX_PRINTLN_MS)
			sleepms = MAX_PRINTLN_MS / str.length();
			
		//sleepms = 100;
		sleepms = sleepms.intValue();
		
		for(def c : str.toCharArray()){
			print c;
			sleep(sleepms);
		}
		
		print '\n';
		
	}
	
	
	static main(args) { 
		
		
		//println URLEncoder.encode("aaa\nbbb", "utf-8");
		
		//println System.getProperty("os.name");
		
		if(args.length == 0){	//TODO test only
			
			//args = ["who are you"];
		}
		
		if(args.length == 0){
			
			println "minimal command line [ZHCN->ENG / ENG->ZHCN] translation tool, using fanyi.baidu.com";
			println "";
			
			println "Usage: ";
			println "\tjava -jar fy.jar i love you";
			println "\tjava -jar fy.jar 我爱你";
			println "";
			println "\talias fy='java -jar /path/to/fy.jar'";
			println "\tfy i love you";
			println "";
			println "\techo hello | fy -";
			println "\tcat file_to_trans.txt | fy -";
			println "\tfy - < file_to_trans.txt";
			println "";
			
			println "Version: ";
			println "\tv0.2";
			println "\t2016-12-XX";
			println "";
			
			println "Author: ";
			println "\t林春宇@深圳";
			println "\tchunyu_lin@163.com";
			println "\thttps://github.com/binaryer/fy";
			
			System.exit(0);
			return;
			
		}
		
		def instr = "";
		
		if(args.length == 1 && args[0].equals("-")){
			instr = IOUtils.toString(System.in, "utf-8");
			//println ("instr = " + instr);
		}
		


		def str_ = null;
		final def str = new StringBuffer();
		 
		if(instr.length() > 0){
			//instr = instr.replace("\t", " ").replace("\n", " ").replace("\r", " ");
			instr = instr.replaceAll("\t", " ");
			for(def arg : instr.split(" ")){
				str.append(arg.trim()).append(" ");
			}
				
		}else{
			for(def arg : args)
				str.append(arg.trim()).append(" ");
		}
		
		str_ = str.toString().trim().replaceAll(" ", "%20").replaceAll("\n", "\\n");
		
		//println str_;

		String from = "zh";
		String to = "en";
		
		char c = '$';
		//if(str_.matches("^[\\w%\\.-:]+"+c)){
		if(str_.matches("^[…–€\\x00-\\xff]+"+c)){
			from = "en";
			to = "zh";
		} 
	
		//println String.format("%s -> %s", from, to);
		
		final def req = HttpRequest.post("http://fanyi.baidu.com/v2transapi")
				.connectionTimeout(5*1000).timeout(15*1000)
				//.form("from", from, "to", to, "query", "aaa%0Abbb");
				.query("from", from)
				.query("to", to)
				.query("query", "aaa%0Abbb");
				
		//println str_;
				
		def res = null;
		try{
			res = req.send();
		}catch(Throwable t){
			System.err.println(String.format("ERROR: http connection"));
			System.exit(EXITCODE_HTTP);
			return;
		}
		
		if(res.statusCode() == 200){
			final def jsonstr = res.charset("utf-8").bodyText();
			try{
				final Object resultO = new JsonSlurper().parseText(jsonstr);
				slowlyPrint(resultO.trans_result.data[0].dst);
				System.exit(0);
				return;
			}catch(Throwable t){
				System.err.println(String.format("ERROR: parse json"));
				System.exit(EXITCODE_JSON);
				return;
			}
			
		}else{
			System.err.println(String.format("ERROR: http return code %d", res.statusCode()));
			System.exit(EXITCODE_NON200);
			return;
		}

	}

}
