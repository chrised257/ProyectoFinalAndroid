package mx.itesm.throughcode;

public class Instruccion {
	//Atributos
	private int id;
	private String comando;
	private float duracion;
	private float val1;
	private float val2;
	
	//Constructores
	public Instruccion(int id, String comando, float duracion, float val1, float val2){
		this.id = id;
		this.comando = comando;
		this.duracion = duracion;
		this.val1 = val1;
		this.val2 = val2;
	}
	
	public Instruccion(String comando, float duracion, float val1, float val2){
		this.comando = comando;
		this.duracion = duracion;
		this.val1 = val1;
		this.val2 = val2;
	}
	
	public Instruccion(int id, String comando, float duracion){
		this.id = id;
		this.comando = comando;
		this.duracion = duracion;
	}
	
	public Instruccion(String comando, float duracion){
		this.comando = comando;
		this.duracion = duracion;
	}
	
	public Instruccion(){
	}
	
	//Setters
	public void setId(int id){
		this.id = id;
	}
	
	public void setComando(String comando){
		this.comando = comando;
	}
	
	public void setDuracion(float duracion){
		this.duracion = duracion;
	}
	
	public void setVal1(float val1){
		this.val1 = val1;
	}
	
	public void setVal2(float val2){
		this.val2 = val2;
	}
	
	//Getters
	public int getId(){
		return this.id;
	}
	
	public String getComando(){
		return this.comando;
	}
	
	public float getDuracion(){
		return this.duracion;
	}
	
	public float getVal1(){
		return this.val1;
	}
	
	public float getVal2(){
		return this.val2;
	}
}
