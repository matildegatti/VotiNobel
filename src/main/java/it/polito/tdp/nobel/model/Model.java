package it.polito.tdp.nobel.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private List<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	//riempio la lista esami di partenza
	public Model() {
		EsameDAO dao=new EsameDAO();
		this.partenza= dao.getTuttiEsami();
	}
	
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		List<Esame> parziale=new ArrayList<Esame>();
		//faccio la new soluzionemigliore qui, così ogni volta che si clicca il pulsante si parte da zero
		soluzioneMigliore=new ArrayList<Esame>();
		mediaSoluzioneMigliore=0;
		
		cerca2(parziale, 0, numeroCrediti);
		return soluzioneMigliore;	
	}

	//metodo ricorsivo 
	//COMPLESSITA' N! non va bene
	private void cerca1(List<Esame> parziale, int L, int m) {
		//casi terminali
		
		//2) sommacrediti supera m
		int crediti=sommaCrediti(parziale);
		
		if(crediti>m) {
			return;
		}
		if(crediti==m) {
			double media=calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new ArrayList<>(parziale); //sovrascrivo il nuovo set
				mediaSoluzioneMigliore=media;
			}
			return;
		}
		
		//se arrivo qui ho sicuramente crediti<m
		// 1) raggiungiamo L=N, non ci sono più esami da aggiungere
		if(L == partenza.size()) {
			return;   //ho un numero di crediti minore di m ma non posso neanche aggiungerne
		}
		
		//genero i sottoproblemi
		/*for(Esame e:partenza) {    //per migliorarlo posso controllare anche qua se ho soluzioni inutili
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale,L+1,m);
				parziale.remove(e);
			}
		}*/
		
		/*for(int i=L; i<partenza.size(); i++) {  
			if(!parziale.contains(partenza.get(i)) /*&& i>=L) {   //l'indice del for deve essere maggiore del livello corrente della ricorsione, quindi non consideriamo 
													//gli elementi che vengono prima del livello della ricorsione  
				parziale.add(partenza.get(i));
				cerca1(parziale,L+1,m);
				parziale.remove(partenza.get(i));
				
			}
		}*/
		
		int lastindex=0;
		if(parziale.size()>0)
			lastindex=partenza.indexOf(parziale.get(parziale.size()-1));
		
		for(int i=lastindex;i<partenza.size();i++) {
			if(!parziale.contains(partenza.get(i)) && i>=L) {    
				parziale.add(partenza.get(i));
				cerca1(parziale,L+1,m);
				parziale.remove(partenza.get(i));
			}
		}
	}
	
	private void cerca2(List<Esame> parziale, int L, int m) {
		int crediti=sommaCrediti(parziale);
		
		if(crediti>m) {
			return;
		}
		if(crediti==m) {
			double media=calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new ArrayList<>(parziale); //sovrascrivo il nuovo set
				mediaSoluzioneMigliore=media;
			}
			return;
		}
		
		if(L == partenza.size()) {
			return;   //ho un numero di crediti minore di m ma non posso neanche aggiungerne
		}
		
		//partenza[L] è da aggiungere oppure no? provo entrambe le cose
		parziale.add(partenza.get(L));
		cerca2(parziale,L+1,m);
		
		parziale.remove(partenza.get(L));
		cerca2(parziale,L+1,m);
		
	}

	public double calcolaMedia(List<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(List<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
