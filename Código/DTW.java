import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class DTW {
	private ArrayList<Movimento> teste, treino;
	
	public DTW() {
		teste = new ArrayList<Movimento>();
		treino = new ArrayList<Movimento>();
	}
	
	public void addMovimento (String path, Movimento e) {
		if(path.equals("../ArquivosEntrada/teste.txt")) {
			teste.add(e);
		} else if(path.equals("../ArquivosEntrada/treino.txt")) {
			treino.add(e);
		}
	}
	
	public void print() {
		for(Movimento movimento : teste)
			System.out.println(movimento.toString() + "\n");
	}
	
	
	public double calculaTaxaAcerto() {
		ArrayList<Double> dtws;
		int menorIndex = 0;
		double menor = 0;
		int hit = 0;
		double taxaAcerto;
		
		//pegando movimento por movimento do arquivo de testes para comparar com os do arquivo treino
		for(int i=0; i<teste.size(); i++) {
			dtws = new ArrayList<Double>();
			
			//pegando os movimentos de treino
			for(int j=0; j<treino.size(); j++) {
				
				//adicionando em um vetor o resultado do dtw realizado entre cada movimento
				dtws.add(dtwDistance(teste.get(i), treino.get(j)));
				
				//guardando sempre a menor distancia para que possamos comparar sua classe futuramente
				if(j==0) {
					menorIndex = 0;
					menor = dtws.get(0);
					continue;
				}
				
				if(dtws.get(j) <= menor) {
					menor = dtws.get(j);
					menorIndex = j;
				}
			}
			
			//verificando se a classe do movimento de treino que mais se assemelha ao movimento testando é a mesma deste
			//contabilizando um acerto caso seja
			if(teste.get(i).getClasse() == treino.get(menorIndex).getClasse()) 
				hit++;
		}
		
		taxaAcerto = (double) hit/teste.size();
		
		return taxaAcerto;
	}
	
	public double dtwDistance(Movimento a, Movimento b) {
		
		double dtw[][] = new double[a.getSerieSize()+1][b.getSerieSize()+1];
		
		// Inicializamos a primeira linha e coluna das séries "a" e "b" como Double.MAX_VALUE (equivalente ao INFINITY) 
		for(int i=0; i<=a.getSerieSize(); i++)
			dtw[i][0] = Double.MAX_VALUE;
		for(int i=0; i<=b.getSerieSize(); i++)
			dtw[0][i] = Double.MAX_VALUE;
		dtw[0][0] = 0;
		
		// Aqui calculamos a distância DTW entre as séries "a" e "b" conforme a relação
		// de recorrência passada na especificação do projeto                                   
		for(int i=1; i<=a.getSerieSize(); i++) {
			for(int j=1; j<=b.getSerieSize(); j++) {
				dtw[i][j] = d(a.get(i-1), b.get(j-1)) + Math.min(Math.min(dtw[i-1][j], dtw[i][j-1]), dtw[i-1][j-1]);
			}
		}
		
		return dtw[a.getSerieSize()][b.getSerieSize()];
	}
	
	public double d(double a, double b) {
		return (a - b)*(a - b);
	}
	
	public static void main(String[] args) {
		//ArrayList<Double> dtws = new ArrayList<Double>();
		Movimento movimento = null;
		DTW dtw = new DTW();
		boolean primeiraIteracao;;
		String path = "../ArquivosEntrada/teste.txt";
		double taxaAcerto;
		long startTime, endTime, totalTime;
		
		try {
			
			for(int k=0; k<2; k++) {
				
				for (String line : Files.readAllLines(Paths.get(path))) {
					primeiraIteracao = true; 
				    for (String part : line.split("\\s+")) {
				    	if(primeiraIteracao) {
				    		Integer classe = Integer.valueOf(part);
				    		movimento = new Movimento(classe);
				    		primeiraIteracao = false;
				    		continue;
				    	}
				        movimento.addNumber(Double.valueOf(part));
				    }
				    dtw.addMovimento(path, movimento);
				}
				
				path = "../ArquivosEntrada/treino.txt";
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		startTime = System.currentTimeMillis();
		taxaAcerto = dtw.calculaTaxaAcerto();
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;

		System.out.println("Taxa de Acerto: " + taxaAcerto);
		System.out.println("Tempo de Execucao: " + totalTime + "ms");
	}

}