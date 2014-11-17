package mx.itesm.throughcode;

public class Instruccion {
	//Atributos
	private int id;
	private String comando;
	private float duracion;
	private float frecuencia;
	private float intensidad;
	private boolean activacion = false;
	
	//Constructores
	public Instruccion(int id, String comando, float duracion, float frecuencia, float intensidad, boolean activacion){
		this.id = id;
		this.comando = comando;
		this.duracion = duracion;
		this.frecuencia = frecuencia;
		this.intensidad = intensidad;
		this.activacion = activacion;
	}
	
	public Instruccion(String comando, float duracion, float frecuencia, float intensidad, boolean activacion){
		this.comando = comando;
		this.duracion = duracion;
		this.frecuencia = frecuencia;
		this.intensidad = intensidad;
		this.activacion = activacion;
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
	
	public void setFrecuencia(float frecuencia){
		this.frecuencia = frecuencia;
	}
	
	public void setIntensidad(float intensidad){
		this.intensidad = intensidad;
	}
	
	public void setActivacion(boolean activacion){
		this.activacion = activacion;
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
	
	public float getFrecuencia(){
		return this.frecuencia;
	}
	
	public float getIntensidad(){
		return this.intensidad;
	}
	
	public boolean getActivacion(){
		return this.activacion;
	}
}
