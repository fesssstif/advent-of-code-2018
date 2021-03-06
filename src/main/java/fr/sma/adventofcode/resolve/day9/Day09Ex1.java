package fr.sma.adventofcode.resolve.day9;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.LongStreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * build a data structure to handle the marble ring
 * run the game for the number of turns
 */
@Component
public class Day09Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static Pattern LINE_PATTERN = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points");
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day09Ex1");
		
		String values = dataFetcher.fetch(9).trim();
		
		Matcher lineMatcher = LINE_PATTERN.matcher(values);
		lineMatcher.matches();
		final int nbPlayer = Integer.parseInt(lineMatcher.group(1));
		final int nbTurn = Integer.parseInt(lineMatcher.group(2));
		
		MarbleGame game = new MarbleGame(nbPlayer);
		
		game.run(nbTurn);
		
		System.out.println(LongStreamEx.of(game.getScore()).max().getAsLong());
	}
	
	
}