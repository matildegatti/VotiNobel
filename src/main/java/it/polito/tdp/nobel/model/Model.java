package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	//riempio la lista esami di partenza
	public Model() {
		EsameDAO dao=new EsameDAO();
		this.partenza= dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale=new HashSet<Esame>();
		//faccio la new soluzionemigliore qui, così ogni volta che si clicca il pulsante si parte da zero
		soluzioneMigliore=new HashSet<Esame>();
		mediaSoluzioneMigliore=0;
		
		cerca2(parziale, 0, numeroCrediti);
		return soluzioneMigliore;	
	}

	//metodo ricorsivo 
	//COMPLESSITA' N! non va bene
	/*private void cerca(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
		//2) sommacrediti supera m
		int crediti=sommaCrediti(parziale);
		
		if(crediti>m) {
			return;
		}
		if(crediti==m) {
			double media=calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new HashSet<>(parziale); //sovrascrivo il nuovo set
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
		for(Esame e:partenza) {    //per migliorarlo posso controllare anche qua se ho soluzioni inutili
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale,L+1,m);
				parziale.remove(e);
			}
		}
	}*/
	
	private void cerca2(Set<Esame> parziale, int L, int m) {
		int crediti=sommaCrediti(parziale);
		
		if(crediti>m) {
			return;
		}
		if(crediti==m) {
			double media=calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new HashSet<>(parziale); //sovrascrivo il nuovo set
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

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
