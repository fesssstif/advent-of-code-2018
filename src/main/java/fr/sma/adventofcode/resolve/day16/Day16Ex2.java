package fr.sma.adventofcode.resolve.day16;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Day16Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final Pattern INSTRUCTION_TEST_PATTERN = Pattern.compile("(Before: \\[\\d+, \\d+, \\d+, \\d+]\n\\d+ \\d+ \\d+ \\d+\nAfter:  \\[\\d+, \\d+, \\d+, \\d+])+");
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day16Ex2");
		
		String values = dataFetcher.fetch(16).trim();
		
		Matcher instructionMatcher = INSTRUCTION_TEST_PATTERN.matcher(values);
		
		//init correspondance map between code and instruction
		Map<Integer, EnumSet<InstructionSet>> correspondances =
				IntStreamEx.range(0, 16)
				.mapToEntry(i -> i, i -> EnumSet.allOf(InstructionSet.class))
				.toMap();
		
		//for each test line, remove the wrong correspondances
		StreamEx.of(instructionMatcher.results())
				.map(MatchResult::group)
				.map(InstructionTester::build)
				.mapToEntry(it -> correspondances.get(it.getInstrutction()[0]))
				.forKeyValue((it, is) -> is.removeIf(i -> !it.matchInstruction(i)));
		
		long nbInstructionRemoved = 0;
		do {
			// find known code
			List<Instruction> identifiedInstructions = StreamEx.of(correspondances.values())
					.filter(is -> is.size() <= 1)
					.map(is -> is.iterator().next())
					.collect(Collectors.toList());
			// remove the known code from available correspondances
			nbInstructionRemoved =
					StreamEx.of(correspondances.values())
					.filter(is -> is.size() > 1)
					.cross(identifiedInstructions)
					.filterKeyValue(AbstractCollection::remove).count();
		} while (nbInstructionRemoved > 0);// repeat until there are no correspondance to remove
		
		// build the instruction array mapping code
		ArrayList<InstructionSet> opCodes = EntryStream.of(correspondances).mapValues(set -> set.iterator().next()).sortedBy(Map.Entry::getKey).values().collect(Collectors.toCollection(ArrayList::new));
		
		Processor p = new Processor(new int[4], opCodes);
		
		//for each line of instruction, apply it to the register
		StreamEx.of(values.split("\n\n\n\n")[1].split("\n"))
				.map(InstructionTester.OPERATION_PATTERN::matcher)
				.flatMap(Matcher::results)
				.map(MatchResult::group)
				.map(line -> StreamEx.split(line, " ").mapToInt(Integer::valueOf).toArray())
				.forEach(p::apply);
		
		System.out.println(p.getRegister()[0]);
	}
	
}