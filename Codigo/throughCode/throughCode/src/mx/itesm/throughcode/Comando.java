package mx.itesm.throughcode;

public class Comando {
		
		private int id;
		private String name;
		private int tiempo;
		private int frecuencia;
		private int rojo;
		private int verde;
		private int azul;
		private int led;
		
		public Comando(){
			this.name = "";
			this.tiempo = 0;
			this.frecuencia = 0;
			this.rojo = 0;
			this.verde = 0;
			this.azul = 0;
			this.led = 0;
		}
		public Comando(int id,String name, int tiempo, int frecuencia, int rojo, int verde, int azul, int led){
			this.name = name;
			this.tiempo = tiempo;
			this.frecuencia = frecuencia;
			this.rojo = rojo;
			this.verde = verde;
			this.azul = azul;
			this.led = led;
			this.id = id;
		}
		public Comando(String name, int tiempo, int frecuencia, int rojo, int verde, int azul, int led){
			this.name = name;
			this.tiempo = tiempo;
			this.frecuencia = frecuencia;
			this.rojo = rojo;
			this.verde = verde;
			this.azul = azul;
			this.led = led;
		}
		
		//Sección para los setters
		public void setID(int id){
			this.id = id;
		}
		void setName(String name)
		{
			this.name = name;
		}
		void setTiempo(int tiempo)
		{
			this.tiempo = tiempo;
		}
		void setFrecuencia(int frecuencia)
		{
			this.frecuencia = frecuencia;
		}
		void setRGB(int rojo, int verde, int azul) //Para valores de 0 - 255 en cada campo de color
		{
			this.rojo = rojo;
			this.verde = verde;
			this.azul = azul;
		}
		void setLED(int led)
		{
			this.led = led;
		}
		
		//Seccion de getters
		public int getID(){
			return this.id ;
		}
		String  getName()
		{
			return this.name;
		}
		int getTiempo()
		{
			return this.tiempo;
		}
		int getFrecuencia()
		{
			return this.frecuencia;
		}
		int  getR() //Para valores de 0 - 255 en cada campo de color
		{
			return this.rojo;
		}
		int  getG() //Para valores de 0 - 255 en cada campo de color
		{
			return this.verde;
		}
		int  getB() //Para valores de 0 - 255 en cada campo de color
		{
			return this.azul;
		}
		/*Byte 0000 0000
		*0001 significa LED1 prendido y los demás apagados
		*0011 significa LED1, LED2 encendidos...
		*/
		int getLED()
		{
			return this.led;
		}
}
