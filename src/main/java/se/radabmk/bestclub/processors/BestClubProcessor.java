package se.radabmk.bestclub.processors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import se.radabmk.bestclub.beans.AtomicClubScore;
import se.radabmk.bestclub.beans.BestClubScore;
import se.radabmk.bestclub.beans.ClubScore;

import static java.util.stream.Collectors.*;

public class BestClubProcessor implements Processor {
	public final static double SCORE_WIN = 1.0;
	public final static double SCORE_LOSS = 0.0;
	

	@Override
	public void process(Exchange exchange) throws Exception {
		InputStream is = exchange.getIn().getBody(InputStream.class);

		Workbook wb = WorkbookFactory.create(is);
		
		Map<String, AtomicClubScore> clubMap = new HashMap<>();
		int games = 0;
		int gamesPlayed = 0;
		String tournament = null;
		
		for (Sheet sheet : wb) {
			if (tournament == null) {
				tournament = sheet.getRow(0).getCell(0).getStringCellValue();
			}
			for (Row row : sheet) {
				if (row.getRowNum() < 4) {
					continue;
				}
				games++;
				String result = row.getCell(4).getStringCellValue();
				boolean noMatch = result.equals("No Match");
				boolean notPlayed = result.isEmpty();
				if (!notPlayed) {
					gamesPlayed++;
				}
				if (!noMatch && !notPlayed) {
					boolean wo = result.equals("w.o. Won");
					String team1 = row.getCell(2).getStringCellValue();
					String team2 = row.getCell(3).getStringCellValue();
					Map<String, Double> teams = new HashMap<>();
					teams.put(team1, SCORE_WIN);
					if (!wo) {
						teams.put(team2, SCORE_LOSS);
					}
					for (Map.Entry<String, Double> team : teams.entrySet()) {
						//System.out.println(team);
						List<String> clubs = getClubs(team.getKey());
						if (clubs.size() > 0) {
							double score = team.getValue()/clubs.size();
							for (String club : clubs) {
								if (clubMap.containsKey(club)) {
									AtomicClubScore acs = clubMap.get(club);
									acs.addScore(score, team.getValue() == SCORE_WIN);
								}
								else {
									AtomicClubScore acs = new AtomicClubScore();
									acs.addScore(score, team.getValue() == SCORE_WIN);
									clubMap.put(club, acs);
								}
							}
						}
					}
				}
			}
		}
		//System.out.println(clubMap);
		
		Map<String, Double> clubScores = new HashMap<>();
		for (Map.Entry<String, AtomicClubScore> entry : clubMap.entrySet()) {
			clubScores.put(entry.getKey(), entry.getValue().getScore());
		}
		
		Map<String, AtomicClubScore> clubScoreSorted = clubMap
		        .entrySet()
		        .stream()
		        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		        .collect(
		            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
		                LinkedHashMap::new));
		
		List<ClubScore> result = new ArrayList<>();
		int position = 0;
		int incr = 1;
		AtomicClubScore prevScore = null;
		for(Map.Entry<String, AtomicClubScore> clubScore : clubScoreSorted.entrySet()) {
			String club = clubScore.getKey();
			AtomicClubScore score = clubScore.getValue();
			if (!score.equals(prevScore)) {
				position += incr;
				incr = 1;
			}
			else {
				incr++;
			}
			result.add(new ClubScore(position, club, score));
			prevScore = score;
		}
		
		wb.close();
		
		BestClubScore bcs = new BestClubScore(tournament, games, gamesPlayed, result);
		
		exchange.getMessage().setBody(bcs);

	}
	
	private List<String> getClubs(String team) {
		if (team.indexOf('(') > 0) {
			return Arrays.asList(team.substring(team.indexOf('(')+1, team.indexOf(')')).split("/"));
		}
		else {
			return new ArrayList<String>();
		}
	}

	
	
}
