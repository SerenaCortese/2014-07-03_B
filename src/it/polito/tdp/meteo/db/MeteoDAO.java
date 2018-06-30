/***************************************************************************\
 *               *                                                         *
 *    #####      *  (!) 2014 by Giovanni Squillero                         *
 *   ######      *  Politecnico di Torino - Dip. Automatica e Informatica  *
 *   ###   \     *  Cso Duca degli Abruzzi 24 / I-10129 TORINO / ITALY     *
 *    ##G  c\    *                                                         *
 *    #     _\   *  tel : +39-011-564.7092  /  Fax: +39-011-564.7099       *
 *    |   _/     *  mail: giovanni.squillero@polito.it                     *
 *    |  _/      *  www : http://www.cad.polito.it/staff/squillero/        *
 *               *                                                         *
\***************************************************************************/

package it.polito.tdp.meteo.db;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.Situazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO per l'accesso al database {@code meteo}
 * 
 * @author Fulvio
 * 
 */
public class MeteoDAO {

	/**
	 * Interroga il database e restituisce tutti i dati nella tabella
	 * {@code situazione} sotto forma di un {@link ArrayList} di
	 * {@link Situazione}, ordinati in modo crescente per data.
	 * 
	 * @return la {@link ArrayList} di {@link Situazione}
	 */
	public List<Situazione> getAllSituazioni() {

		final String sql = "SELECT * FROM situazione ORDER BY data ASC";

		List<Situazione> situazioni = new ArrayList<Situazione>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Situazione s = new Situazione(rs.getString("Localita"),
						rs.getDate("Data"), rs.getInt("Tmedia"),
						rs.getInt("Tmin"), rs.getInt("Tmax"),
						rs.getInt("Puntorugiada"), rs.getInt("Umidita"),
						rs.getInt("Visibilita"), rs.getInt("Ventomedia"),
						rs.getInt("Ventomax"), rs.getInt("Raffica"),
						rs.getInt("Pressioneslm"), rs.getInt("Pressionemedia"),
						rs.getInt("Pioggia"), rs.getString("Fenomeni"));
				situazioni.add(s);
			}
			conn.close();
			return situazioni;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

//	/**
//	 * Test method for class {@link MeteoDAO}
//	 * 
//	 * @param args
//	 */
//	public static void main(String args[]) {
//		
//		MeteoDAO dao = new MeteoDAO() ;
//		
//		List<Situazione> list = dao.getAllSituazioni() ;
//		
//		for( Situazione s : list ) {
//			System.out.format("%-10s %2td/%2$2tm/%2$4tY %3d°C-%3d°C  %3d%%  %s\n", 
//					s.getLocalita(), s.getData(), s.getTMin(), s.getTMax(), s.getUmidita(), s.getFenomeni()) ;
//		}
//
//	}

	public List<Citta> getUmiditaMediaPerMese(Integer mese) {
		String sql = "SELECT localita, avg(umidita) " + 
				"FROM situazione " + 
				"WHERE MONTH(data) = ? " + 
				"GROUP BY localita ";
		
		List<Citta> cittaUmiditaMese = new ArrayList<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, Integer.valueOf(mese));
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Citta c = new Citta(rs.getString("localita"),rs.getDouble("avg(umidita)"));
				cittaUmiditaMese.add(c);
				
			}
			conn.close();
			return cittaUmiditaMese;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(Integer mese, Citta c) {
		String sql = "SELECT data, umidita " + 
				"FROM situazione " + 
				"WHERE MONTH(data) = ? " + 
				"AND localita = ? ";
		
		List<Rilevamento> rilevamentiUmiditaMese = new ArrayList<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, Integer.valueOf(mese));
			st.setString(2, c.getCitta());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				rilevamentiUmiditaMese.add(new Rilevamento(c, rs.getDate("data").toLocalDate(), rs.getInt("umidita")));
				
			}
			conn.close();
			return rilevamentiUmiditaMese;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	
}
