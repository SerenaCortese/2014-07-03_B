package it.polito.tdp.meteo.bean;

import java.util.ArrayList;
import java.util.List;

public class Citta {
	
	private String citta;
	private double umiditaMedia;
	private List<Rilevamento> rilevamenti;
	private int numVisite;
	
	
	public Citta(String citta, double umiditaMedia) {
		super();
		this.citta = citta;
		this.umiditaMedia = umiditaMedia;
		this.rilevamenti = new ArrayList<>();
	}
	
	public Citta(String citta) {
		super();
		this.citta = citta;
	}

	public String getCitta() {
		return citta;
	}
	public void setCitta(String citta) {
		this.citta = citta;
	}
	public double getUmiditaMedia() {
		return umiditaMedia;
	}
	public void setUmiditaMedia(double umiditaMedia) {
		this.umiditaMedia = umiditaMedia;
	}
	
	public List<Rilevamento> getRilevamenti() {
		return rilevamenti;
	}

	public void setRilevamenti(List<Rilevamento> rilevamenti) {
		this.rilevamenti = rilevamenti;
	}
	
	
	public int getNumVisite() {
		return numVisite;
	}

	public void visitata() {
		this.numVisite ++;
	}
	
	public void nonVisitata() {
		this.numVisite--;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((citta == null) ? 0 : citta.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Citta other = (Citta) obj;
		if (citta == null) {
			if (other.citta != null)
				return false;
		} else if (!citta.equals(other.citta))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return citta;// + ", umidita Media: " + umiditaMedia;
	}
	
	

}
