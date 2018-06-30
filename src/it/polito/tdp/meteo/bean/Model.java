package it.polito.tdp.meteo.bean;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {
	
	private MeteoDAO dao;
	
	private List<Citta> citta;
	
	private final static int C = 100; //costo ogni volta che si sposta di citta
	private final static int K = 1; // euro/% di umidità in quel giorno
	
	private List<Citta> cittaOttime;
	private int costoMin;
	private Month m;
	
	public Model() {
		dao = new MeteoDAO();	
	}

	public List<Citta> getUmiditaMediePerMese(Integer mese) {
		citta = dao.getUmiditaMediaPerMese(mese);
		return citta;
	}

	public List<Citta> getSequenzaMinimoCosto(Integer mese) {
		citta = dao.getUmiditaMediaPerMese(mese);
		for(Citta c : citta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese,c));
		}
		m = Month.of(Integer.valueOf(mese));
		int giorniTotali = m.length(false);
		cittaOttime = null;
		List<Citta> parziale = new ArrayList<Citta>();
		costoMin = Integer.MAX_VALUE;
		
		ricorsione(parziale,giorniTotali);
		return cittaOttime;
	}

	private void ricorsione(List<Citta> parziale, int giorniTotali) {
		
		if(parziale.size() == giorniTotali+1) {
			int costo = calcolaCosto(parziale,giorniTotali);
			if(costo < costoMin) {
				costoMin = costo;
				cittaOttime = new ArrayList<>(parziale);
				System.out.println(parziale);
			}
		}
		
		
		for(Citta c: citta) {
				parziale.add(c);
				c.visitata();
				if(isCorretta(parziale)) {
					ricorsione(parziale,giorniTotali);
				}
				parziale.remove(c);
				c.nonVisitata();
			
		}
		
	}

	private int calcolaCosto(List<Citta> parziale, int giorniTotali) {
		// costo + c *spostamenti + k*umidità del giorno
		
		if(parziale == null || parziale.size()<=1)
			return Integer.MAX_VALUE;
		
		int costo = 0 ;
		
		Citta cittaIeri = null ;
		for(int giorno = 1; giorno<=giorniTotali; giorno++) {
			Citta cittaOggi = parziale.get(giorno) ;

			if( cittaIeri!=null && !cittaOggi.equals(cittaIeri)) {
				costo += C ;
			}
			cittaIeri = cittaOggi ;
			
			LocalDate oggi = LocalDate.of(2013, this.m, giorno) ;
			// trovare umitida nella 'cittaOggi' nel giorno 'oggi'
			Integer um = null;
			for(Rilevamento r : cittaOggi.getRilevamenti()) {
				if(r.getData().equals(oggi)) {
					um = r.getUmidita();
				}
			}
			if(um != null)
				costo += K * um ;
			else
				costo += K * 100 ;
				// se manca il dato, ipotizzo il caso peggiore
		}
		
		return costo;
		
	}

	private boolean isCorretta(List<Citta> parziale) {
		// in nessuna città si possono spendere più di 12 giornate (anche non consecutive)
//		if(parziale.size() == 1)
//			return true;
//		
		for(Citta c : parziale) {
			if(c.getNumVisite()>12)
				return false;
		}
		return true;
	}
	
	
	

}
