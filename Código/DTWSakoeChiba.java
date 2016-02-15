import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class DTWSakoeChiba {
	private ArrayList<Movimento> teste, treino;
	
	public DTWSakoeChiba() {
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
	
	public double calculaTaxaAcerto(int window) {
		ArrayList<Double> dtws;
		int menorIndex = 0;
		double menor = 0;
		int hit = 0;
		double taxaAcerto;
		
		for(int i=0; i<teste.size(); i++) {
			dtws = new ArrayList<Double>();
			
			for(int j=0; j<treino.size(); j++) {
				
				dtws.add(dtwDistanceSakoeChiba(teste.get(i), treino.get(j),window));
				if(j==0) {
					menorIndex = 0;
					menor = dtws.get(0);
					continue;
				}
				
				if(dtws.get(j) < menor) {
					menor = dtws.get(j);
					menorIndex = j;
				}
			}
			
			if(teste.get(i).getClasse() == treino.get(menorIndex).getClasse()) 
				hit++;
		}
		
		taxaAcerto = (double) hit/teste.size();
		
		return taxaAcerto;
	}
	
	public double dtwDistanceSakoeChiba(Movimento a, Movimento b, double window) {
		double dtw[][] = new double[a.getSerieSize()+1][b.getSerieSize()+1];
		
		// Inicialmente, escolhemos o maior entre o tamanho da janela "window" e a diferença entre o tamanho das séries "a" e "b"
		// Fazemos isso, pois existe uma propriedade que afirma que: a adição da banda só faz sentido se | a - b | <= window
		window = Math.max(Math.abs(a.getSerieSize()-b.getSerieSize()),window);
		
		// Diferente do DTW normal, inicializamos todos elementos como Double.MAX_VALUE (equivalente ao INFINITY)     
		for(int i=0; i<=a.getSerieSize(); i++)
			for(int j=0; j<=b.getSerieSize(); j++)
				dtw[i][j] = Double.MAX_VALUE;
		
		dtw[0][0] = 0;

		// Aqui a única modificação é que restringimos os valores da série "b" (percorridos com "j") que serão casados 
		// com os da série "a" (percorridos com "i"). 
		// Só pegaremos valores da série "b" que estão dentro da "window" especificada                                 
		for(int i=1; i<=a.getSerieSize(); i++) {
			for(int j=(int)Math.max(1.0,i-window); j<=Math.min(b.getSerieSize(),i+window); j++) {	
				dtw[i][j] = d(a.get(i-1), b.get(j-1)) + Math.min(Math.min(dtw[i-1][j], dtw[i][j-1]), dtw[i-1][j-1]);
			}
		}
		
		return dtw[a.getSerieSize()][b.getSerieSize()];
	}
	
	public double d(double a, double b) {
		return Math.pow((a - b),2);
	}
	
	public static void main(String[] args) {
		//ArrayList<Double> dtws = new ArrayList<Double>();
		Movimento movimento = null;
		DTWSakoeChiba dtw = new DTWSakoeChiba();
		boolean primeiraIteracao;;
		String path = "../ArquivosEntrada/teste.txt";
		double taxaAcerto[] = new double[7];
		long startTime, endTime;
		long totalTime[] = new long[7];
		int bandasSakoeChiba[] = {0,1,5,10,20,50,100};

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
		
		for(int i = 0; i < 7; i++){
			startTime = System.currentTimeMillis();
			taxaAcerto[i] = dtw.calculaTaxaAcerto(bandasSakoeChiba[i]);			
			endTime   = System.currentTimeMillis();
			totalTime[i] = endTime - startTime;
		}

		for(int i = 0; i < 7; i++){
			System.out.println("Banda: " + bandasSakoeChiba[i] + "%");
			System.out.println("Taxa de Acerto: " + taxaAcerto[i]);
			System.out.println("Tempo de Execucao: " + totalTime[i] + "ms");
		}
	}

}