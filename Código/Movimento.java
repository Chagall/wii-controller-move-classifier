import java.util.ArrayList;

public class Movimento {
	
	private int classe;					// Tipo de movimento realizado, de 1 a 12: direita, esquerda, etc.
	private ArrayList<Double> serie;	// Lista de valores que representam a série temporal  
	
	// Construtor da Classe Movimento
	public Movimento(int classe) {
		this.classe = classe;
		this.serie = new ArrayList<Double>();
	}	

	// Insere um valor numérico da série temporal
	public void addNumber(double num) {
		this.serie.add(num);
	}	

	// Retorna o "i"ésimo valor numérico da série temporal
	public double get(int i) {
		return serie.get(i);
	}
	
	// Retorna a quantidade de valores numéricos que a série possui
	public int getSerieSize() {
		return this.serie.size();
	}
	
	// Retorna o tipo de movimento que a série representa: Um valor de 1 a 12
	public int getClasse() {
		return this.classe;
	}
	
	// Retorna uma string contendo o tipo de movimento + os valores numéricos da série
	public String toString() {
		String string = this.classe + " ";
		for(Double number : serie)
			string += number.toString() + " ";		
		return string;
	}
}
