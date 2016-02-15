import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class DTW3D {
	private ArrayList<Movimento3D> teste, treino;
	
	public DTW3D() {
		teste = new ArrayList<Movimento3D>();
		treino = new ArrayList<Movimento3D>();
	}
	
	public void addMovimento (String path, Movimento3D e) {
		if(path.equals("../ArquivosEntrada/teste3D.txt")) {
			teste.add(e);
		} else if(path.equals("../ArquivosEntrada/treino3D.txt")) {
			treino.add(e);
		}
	}
	
	public void print() {
		for(Movimento3D movimento : teste)
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
	
	public double dtwDistance(Movimento3D a, Movimento3D b) {
		
		double dtwX[][] = new double[a.getSerieXSize()+1][b.getSerieXSize()+1];
		double dtwY[][] = new double[a.getSerieYSize()+1][b.getSerieYSize()+1];
		double dtwZ[][] = new double[a.getSerieZSize()+1][b.getSerieZSize()+1];
		double dtw3DResult;

		// Inicializamos a primeira linha e coluna dos eixos X,Y e Z das séries "a" e "b" como Double.MAX_VALUE (equivalente ao INFINITY) 
		for(int i=0; i<=a.getSerieXSize(); i++){
			dtwX[i][0] = Double.MAX_VALUE;
		}
		for(int i=0; i<=a.getSerieYSize(); i++){
			dtwY[i][0] = Double.MAX_VALUE;
		}
		for(int i=0; i<=a.getSerieZSize(); i++){
			dtwZ[i][0] = Double.MAX_VALUE;
		}

		for(int i=0; i<=b.getSerieXSize(); i++){
			dtwX[0][i] = Double.MAX_VALUE;
		}
		for(int i=0; i<=b.getSerieYSize(); i++){
			dtwY[0][i] = Double.MAX_VALUE;
		}
		for(int i=0; i<=b.getSerieZSize(); i++){
			dtwZ[0][i] = Double.MAX_VALUE;
		}		

		dtwX[0][0] = 0;
		dtwY[0][0] = 0;
		dtwZ[0][0] = 0;
		
		// Aqui calculamos a distância DTW entre as séries "a" e "b" conforme a relação
		// de recorrência passada na especificação do projeto                                   
		for(int i=1; i<=a.getSerieXSize(); i++) {
			for(int j=1; j<=b.getSerieXSize(); j++) {
				dtwX[i][j] = d(a.getX(i-1), b.getX(j-1)) + Math.min(Math.min(dtwX[i-1][j], dtwX[i][j-1]), dtwX[i-1][j-1]);
			}
		}
		for(int i=1; i<=a.getSerieYSize(); i++) {
			for(int j=1; j<=b.getSerieYSize(); j++) {
				dtwY[i][j] = d(a.getY(i-1), b.getY(j-1)) + Math.min(Math.min(dtwY[i-1][j], dtwY[i][j-1]), dtwY[i-1][j-1]);
			}
		}
		for(int i=1; i<=a.getSerieZSize(); i++) {
			for(int j=1; j<=b.getSerieZSize(); j++) {
				dtwZ[i][j] = d(a.getZ(i-1), b.getZ(j-1)) + Math.min(Math.min(dtwZ[i-1][j], dtwZ[i][j-1]), dtwZ[i-1][j-1]);
			}
		}	
				
		dtw3DResult = dtwX[a.getSerieXSize()][b.getSerieXSize()] + dtwY[a.getSerieYSize()][b.getSerieYSize()] + dtwZ[a.getSerieZSize()][b.getSerieZSize()];
		
		return dtw3DResult;
	}
	
	public double d(double a, double b) {
		return (a - b)*(a - b);
	}
	
	public static void main(String[] args) {

		Movimento3D movimento = null;
		DTW3D dtw = new DTW3D();
		boolean primeiraIteracao;
		String path = "../ArquivosEntrada/teste3D.txt";
		double taxaAcerto;
		long startTime, endTime, totalTime;
		int addHelper = 0;
		
		try {
			
			for(int k=0; k<2; k++) {
				
				for (String line : Files.readAllLines(Paths.get(path))) {
					primeiraIteracao = true; 
				    for (String part : line.split("\\s+")) {
				    	if(primeiraIteracao) {
				    		Integer classe = Integer.valueOf(part);
				    		movimento = new Movimento3D(classe);
				    		primeiraIteracao = false;
				    		continue;
				    	}
				    	if(addHelper == 0){
				        	movimento.addNumberX(Double.valueOf(part));
				        	addHelper++;
				        }
				        else if(addHelper == 1){
				        	movimento.addNumberY(Double.valueOf(part));
				        	addHelper++;
				        }
				        else if(addHelper == 2){
				        	movimento.addNumberZ(Double.valueOf(part));
				        	addHelper=0;
				        }
				    }
				    dtw.addMovimento(path, movimento);
				}
				
				path = "../ArquivosEntrada/treino3D.txt";
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