package fr.sma.adventofcode.resolve.day13;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * build a track system using the data, then run the simulation.
 * wait 20ms between steps and display using swing
 */
@Component
public class Day13Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day13Ex1");
		
		String values = dataFetcher.fetch(13).trim();
		
		TrackSystem trackSystem = TrackSystem.buildTrackSystem(values);
		
		TrackSystem.TrackSystemPainter painter = trackSystem.getPainter();
		
		TrackSystem.Chariot collidingChariot = StreamEx.generate(() -> trackSystem)
				.peek(trackSystem1 -> painter.repaint())
				.peek(trackSystem1 -> {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				})
				.flatCollection(TrackSystem::moveAll)
				.findFirst().get();
		
		painter.repaint();
		
		System.out.println(collidingChariot.getX() + "," + collidingChariot.getY());
	}
}