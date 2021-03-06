package fr.sma.adventofcode.resolve.day5;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * progressing forward from start, try to polymerize adjacent character.
 * when found, remove them, then go back to the previous letter.
 * repeat untl the end is reached.
 */
@Component
public class Day05Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day05Ex1");
		
		String values = dataFetcher.fetch(5).trim();
		
		ArrayList<String> polymer = new ArrayList<>(Arrays.asList(values.split("")));
		
		polymerize(polymer);
		
		System.out.println(polymer.size());
	}
	
	public void polymerize(ArrayList<String> polymer) {
		int index = 0;
		while(index < polymer.size() - 1) {
			if(doReact(polymer.get(index), polymer.get(index + 1))) {
				polymer.remove(index);
				polymer.remove(index);
				if(index > 0) {
					index--;
				}
			} else {
				index++;
			}
		}
	}
	
	private boolean doReact(String left, String right) {
		return left.toUpperCase().equals(right.toUpperCase()) && !left.equals(right);
	}
}