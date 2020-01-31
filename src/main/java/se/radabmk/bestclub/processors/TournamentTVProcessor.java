/**
 * 
 */
package se.radabmk.bestclub.processors;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.radabmk.bestclub.beans.BestClubScore;
import se.radabmk.bestclub.beans.ClubScore;

/**
 * @author heka
 *
 */
public class TournamentTVProcessor implements Processor {
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		BestClubScore bcs = (BestClubScore) exchange.getIn().getBody();
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		
		for (int i=0; i<Math.min(10, bcs.getClubScores().size()); i++) {
			ClubScore cs = bcs.getClubScores().get(i);
			bw.write(String.format(Locale.US, "%2d. %-37s %5.1f", cs.position, cs.name, cs.points));
			bw.newLine();
		}
		bw.flush();
		bw.close();
		String output = sw.toString();
		logger.debug(output);
		exchange.getMessage().setBody(output);
	}

}
