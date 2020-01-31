/**
 * 
 */
package se.radabmk.bestclub.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author heka
 *
 */
public class BestClubScore {
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private final String tournament;
	private final int gamesTotal;
	private final int gamesPlayed;
	private final String timestamp;
	private final List<ClubScore> clubScores;
	
	public BestClubScore(String tournament, int total, int played, List<ClubScore> clubScores) {
		this.tournament = tournament;
		this.gamesTotal = total;
		this.gamesPlayed = played;
		this.timestamp = sdf.format(new Date());
		this.clubScores = clubScores;
	}

	public int getGamesTotal() {
		return gamesTotal;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getTournament() {
		return tournament;
	}

	public List<ClubScore> getClubScores() {
		return clubScores;
	}
	
}
