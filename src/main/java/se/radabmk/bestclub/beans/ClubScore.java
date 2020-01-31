/**
 * 
 */
package se.radabmk.bestclub.beans;

/**
 * @author heka
 *
 */
public class ClubScore {
	public final int position;
	public final String name;
	public final double points;
	public final int wins;
	public final int losses;

	public ClubScore(int position, String name, AtomicClubScore acs) {
		this.position = position;
		this.name = name;
		this.points = acs.getScore();
		this.wins = acs.getWins();
		this.losses = acs.getLosses();
	}

	public int getPosition() {
		return position;
	}

	public String getName() {
		return name;
	}

	public double getPoints() {
		return points;
	}

	public int getWins() {
		return wins;
	}

	public int getLosses() {
		return losses;
	}
	
}
