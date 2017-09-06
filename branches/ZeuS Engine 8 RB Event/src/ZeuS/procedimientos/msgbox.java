package ZeuS.procedimientos;

import java.util.logging.Logger;

public class msgbox {

	private static final Logger _log = Logger.getLogger(msgbox.class.getName());
	
	private static msgbox _instance;
	
	public static void setResp(int respuesta){
		_log.warning(String.valueOf(respuesta));
	}

	public static final msgbox getInstance()
	{
		return _instance;
	}
	
	public msgbox getInstance2(){
		return _instance;
	}

}
