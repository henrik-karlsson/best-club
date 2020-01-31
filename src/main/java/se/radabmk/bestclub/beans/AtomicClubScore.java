package se.radabmk.bestclub.beans;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;

public class AtomicClubScore implements Comparable<AtomicClubScore> {
	DoubleAdder score = new DoubleAdder();
	AtomicInteger wins = new AtomicInteger();
	AtomicInteger losses = new AtomicInteger();

	public AtomicClubScore() {
	}

	public void addScore(double score, boolean win) {
		this.score.add(score);
		if (win) {
			wins.incrementAndGet();
		} else {
			losses.incrementAndGet();
		}
	}

	public String toString() {
		return score.doubleValue() + " (" + wins.intValue() + "," + losses.intValue() + ")";
	}

	@Override
	public int compareTo(AtomicClubScore that) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		// this optimization is usually worthwhile, and can
		// always be added
		if (this == that)
			return EQUAL;

		// primitive numbers follow this form
		if (this.score.doubleValue() < that.score.doubleValue())
			return BEFORE;
		if (this.score.doubleValue() > that.score.doubleValue())
			return AFTER;
		if (this.wins.intValue() < that.wins.intValue())
			return BEFORE;
		if (this.wins.intValue() > that.wins.intValue())
			return AFTER;
		if ((this.wins.intValue() + this.losses.intValue()) < (that.wins.intValue() + that.losses.intValue()))
			return BEFORE;
		if ((this.wins.intValue() + this.losses.intValue()) > (that.wins.intValue() + that.losses.intValue()))
			return AFTER;

		// all comparisons have yielded equality
		// verify that compareTo is consistent with equals (optional)
		assert this.equals(that) : "compareTo inconsistent with equals.";

		return EQUAL;
	}

	@Override
	public boolean equals(Object aThat) {
		if (this == aThat)
			return true;
		if (!(aThat instanceof AtomicClubScore))
			return false;
		AtomicClubScore that = (AtomicClubScore) aThat;
		return this.score.doubleValue() == that.score.doubleValue() &&
				this.wins.intValue() == that.wins.intValue() &&
				this.losses.intValue() == that.losses.intValue();
	}

	public double getScore() {
		return score.doubleValue();
	}

	public int getWins() {
		return wins.intValue();
	}

	public int getLosses() {
		return losses.intValue();
	}

}
