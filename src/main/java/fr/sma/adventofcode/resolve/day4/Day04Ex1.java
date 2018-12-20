package fr.sma.adventofcode.resolve.day4;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class Day04Ex1 implements ExSolution {
	
	private static final Pattern PATTERN_LINE = Pattern.compile("\\[(.+)\\] (.+)");
	
	//private static Logger logger = LoggerFactory.getLogger(Day04Ex1.class);
	
	@Autowired
	private DataFetcher dataFetcher;
	
	
	@Override
	public void run() throws Exception {
		System.out.println("Day04Ex1");
		
		String data = dataFetcher.fetch(4);
		
		StreamEx.split(data, "\n") //all lines
				.map(PATTERN_LINE::matcher) //parsed line
				.peek(Matcher::matches) //activate parsing
				.map(m -> GuardLine.parse(m.group(1), m.group(2))) //line wrapped into an object
				.sorted(Comparator.comparing(GuardLine::getTime)) //sort line by time
				.groupRuns((g1, g2) -> !GuardShift.PATTERN_NEW_GUARD.matcher(g2.getEvent()).matches()) //group line by guard shift
				.map(GuardShift::build) // build a guard shift from lines
				.sorted(Comparator.comparing(GuardShift::getId)) // sort by id
				.collapse((gs1, gs2) -> gs1.getId() == gs2.getId(), GuardShift::merge) //group by same guard
				.sorted(Comparator.comparing(GuardShift::getTotalSleep).reversed()) //order by most sleepy guard
				.peek(System.out::println)
				.findFirst()
				.map(gs -> gs.getMaxMinute() * gs.getId()) // compute data for most sleepy guard
				.ifPresent(System.out::println);
	}
	
}
