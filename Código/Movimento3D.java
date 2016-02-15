import java.util.ArrayList;

public class Movimento3D {
	
	private int classe;					// Tipo de movimento realizado, de 1 a 12: direita, esquerda, etc.
	private ArrayList<Double> serieX;	// Lista de valores que representam a série temporal no eixo X 
	private ArrayList<Double> serieY;	// Lista de valores que representam a série temporal no eixo Y
	private ArrayList<Double> serieZ;	// Lista de valores que representam a série temporal no eixo Z
	
	// Construtor da Classe Movimento
	public Movimento3D(int classe) {
		this.classe = classe;
		this.serieX = new ArrayList<Double>();
		this.serieY = new ArrayList<Double>();
		this.serieZ = new ArrayList<Double>();
	}	

	// Insere um valor numérico da série temporal do eixo X
	public void addNumberX(double num) {
		this.serieX.add(num);
	}	

	// Insere um valor numérico da série temporal do eixo Y
	public void addNumberY(double num) {
		this.serieY.add(num);
	}

	// Insere um valor numérico da série temporal do eixo Z
	public void addNumberZ(double num) {
		this.serieZ.add(num);
	}

	// Retorna o "i"ésimo valor numérico da série temporal do eixo X
	public double getX(int i) {
		return serieX.get(i);
	}

	// Retorna o "i"ésimo valor numérico da série temporal do eixo Y
	public double getY(int i) {
		return serieY.get(i);
	}

	// Retorna o "i"ésimo valor numérico da série temporal do eixo Z
	public double getZ(int i) {
		return serieZ.get(i);
	}

	// Retorna a quantidade de valores numéricos que a série do eixo X possui
	public int getSerieXSize() {
		return this.serieX.size();
	}

	// Retorna a quantidade de valores numéricos que a série do eixo Y possui
	public int getSerieYSize() {
		return this.serieY.size();
	}

	// Retorna a quantidade de valores numéricos que a série do eixo Z possui
	public int getSerieZSize() {
		return this.serieZ.size();
	}

	// Retorna o tipo de movimento que a série representa: Um valor de 1 a 12
	public int getClasse() {
		return this.classe;
	}
	
	/* -------- Ver como printar os três eixos
	// Retorna uma string contendo o tipo de movimento + os valores numéricos da série
	public String toString() {
		String string = this.classe + " ";
		for(Double number : serieX)
			string += number.toString() + " ";		
		return string;
	}
	*/
}
