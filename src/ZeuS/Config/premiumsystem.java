package ZeuS.Config;

import java.util.Vector;

import com.l2jserver.Config;


public class premiumsystem{
	private int ID;
	private int exp;
	private int sp;
	private int adena;
	private int spoil;
	private int drop;
	private int epaulette;
	private int craft;
	private int mw_craft;
	private int day;
	private int cost;
	private boolean isAccount;
	private boolean isEnabled;
	private String Nombre;
	private String Icono;
	private Vector<String> InfoShow = new Vector<String>();
	
	private int getPorcen(float Valor, int porcen){
		int retorno=0;
		
		long porce = (long) ( (porcen / 100.0) * Valor);
		
		retorno = Math.round( porce + Valor );
			
		return retorno;
	}
	
	public boolean isEnabled(){
		return this.isEnabled;
	}
	
	public Vector<String> getInfoShow(){
		return InfoShow;
	}
	
	public int getIDPremium(){
		return this.ID;
	}
	
	public int get_mwCraft(){
		return this.mw_craft;
	}
	
	public int get_mwCraft(int Value){
		try{
			return getPorcen( Value , this.sp);
		}catch(Exception a){
			
		}
		return 0;
	}
	
	public int getEpaulette(){
		//KE = 9912
		return this.epaulette;
	}
	
	public int getEpaulette(int Value){
		try{
			return getPorcen( Value , this.epaulette);
		}catch(Exception a){
			
		}
		return 0;
	}
	
	public int getCraft(){
		return this.craft;
	}
	
	public int getCraft(int Value){
		try{
			return getPorcen( Value , this.craft);
		}catch(Exception a){
			
		}
		return 0;
	}
	
	public int getDrop(boolean porcen){
		if(porcen){
//			return getPorcen( Config.PREMIUM_RATE_DROP_CHANCE , this.sp);
		}
		return this.drop;		
	}

	public int getSpoil(boolean porcen){
		if(porcen){
//			return getPorcen( Config.PREMIUM_RATE_DROP_CHANCE, this.sp);
		}
		return this.spoil;		
	}
	
	
	public int getadena(boolean porcen){
		float Adena = 0;
//		if(Config.PREMIUM_RATE_DROP_AMOUNT_MULTIPLIER!=null){
//			if(Config.PREMIUM_RATE_DROP_AMOUNT_MULTIPLIER.containsKey(57)){
//				Adena = Config.PREMIUM_RATE_DROP_AMOUNT_MULTIPLIER.get(57);
//			}
//		}
		if(porcen){
			return getPorcen( Adena , this.sp);
		}
		return this.adena;		
	}
	
	public int getsp(boolean porcen){
		if(porcen){
			return getPorcen( Config.RATE_SP , this.sp);
		}
		return this.sp;		
	}
	
	public int getexp(boolean porcen){
		if(porcen){
			return getPorcen( Config.RATE_XP , this.exp);
		}
		return this.exp;
	}
	
	
	public int getDays(){
		return this.day;
	}
	
	public int getCost(){
		return this.cost;
	}
	
	public String getName(){
		return this.Nombre;
	}
	
	public String getIcono(){
		return this.Icono;
	}
	
	public boolean IsAccount(){
		return this.isAccount;
	}
	
	public String getAplicableA(){
		return this.isAccount ? "Account" : "Clan";
	}
	
	public premiumsystem(){
		
	}
	
	public premiumsystem(int _id, int _exp, int _sp, int _drop, int _adena, int _spoil, int _epaulette, int _craft, int _mw_craft, int _day, int _cost, boolean _isAcc, String _Nombre, String _Icono, Vector<String> _DataShow, boolean _isEnabled){
		this.exp = _exp;
		this.sp = _sp;
		this.adena = _adena;
		this.drop = _drop;
		this.spoil = _spoil;
		this.epaulette = _epaulette;
		this.craft = _craft;
		this.mw_craft = _mw_craft;
		this.day = _day;
		this.cost = _cost;
		this.isAccount = _isAcc;
		this.ID = _id;
		this.Nombre = _Nombre;
		this.Icono = _Icono;
		this.InfoShow = _DataShow;
		this.isEnabled = _isEnabled;
	}
}


