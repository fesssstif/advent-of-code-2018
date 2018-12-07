package fr.sma.adventofcode.resolve.day5;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day05Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day05Ex1");
		
		String values = dataFetcher.fetch(5).trim(); // f***ing trim for line feed at end
		
		IntStreamEx.rangeClosed('a', 'z')
				.mapToObj(c -> StreamEx.split(values, "").filter(s -> c != s.toLowerCase().charAt(0)))
				.map(polyStream -> polyStream.collect(Collectors.toCollection(ArrayList::new)))
				.peek(this::polymerize)
				.map(List::size)
				.sorted()
				.findFirst()
				.ifPresent(System.out::println);
	}
	
	public void polymerize(ArrayList<String> polymer) {
		int index = 0;
		while(index < polymer.size() - 1) {
			//String line = (index > 0 ? polymer.get(index -1): ".") + polymer.get(index) + polymer.get(index +1)+ (index polymer.get(index +2);
			if(doReact(polymer.get(index), polymer.get(index + 1))) {
				//logger.debug(line + " match !");
				polymer.remove(index);
				polymer.remove(index);
				if(index > 0) {
					index--;
				}
			} else {
				//logger.debug(line);
				index++;
			}
		}
	}
	
	private boolean doReact(String left, String right) {
		return left.toUpperCase().equals(right.toUpperCase()) && !left.equals(right);
	}
}