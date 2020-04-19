package com.odin.rnd.jjit.dflow;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.odin.rnd.jjit.ir.Register;

public class RegisterLivenessIntervals {
	private final Register register;

	private final Deque<RegisterLivenessInterval> intervals = new LinkedList<>();
	private final Set<Integer> usePositions = new TreeSet<>();
	
	private int allocRegId = -1;

	public RegisterLivenessIntervals(Register reg) {
		this.register = reg;
	}
	
	public Register getRegister() {
		return register;
	}
	
	public int getAllocatedRegisterId() {
		return allocRegId;
	}
	
	public void setAllocatedRegisterId(int regId) {
		allocRegId = regId;
	}

	public RegisterLivenessInterval addLivenessInterval(int startInstrId, int endInstrId) {
		if (!intervals.isEmpty()) {
			RegisterLivenessInterval firstInterval = intervals.getFirst();
			if (endInstrId >= firstInterval.getStartInstrId()) {
				return firstInterval;
			}
		}

		
		RegisterLivenessInterval newInterval = new RegisterLivenessInterval(startInstrId, endInstrId);
		intervals.addFirst(newInterval);
		
		return newInterval;
	}

	public Collection<RegisterLivenessInterval> getLivenessIntervals() {
		return intervals;
	}

	public void setFirstIntervalStart(int startInstrId) {
		if (intervals.isEmpty()) {
			return;
		}

		intervals.getFirst().setStartInstrId(startInstrId);
	}

	public int getFirstIntervalStart() {
		if (intervals.isEmpty()) {
			throw new IllegalStateException("empty register " + register + " liveness interval detected");
		}

		return intervals.getFirst().getStartInstrId();
	}

	public int getLastIntervalEnd() {
		if (intervals.isEmpty()) {
			throw new IllegalStateException("empty register " + register + " liveness interval detected");
		}
		
		return intervals.getLast().getEndInstrId();
	}

	public void mergeOverlapping() {
		if (intervals.size() < 2) {
			return;
		}
		
		Iterator<RegisterLivenessInterval> currIntervalIter = intervals.iterator();

		Iterator<RegisterLivenessInterval> nextIntervalIter = intervals.iterator();
		nextIntervalIter.next();
		
		while (nextIntervalIter.hasNext()) {
			RegisterLivenessInterval currInterval = currIntervalIter.next();
			RegisterLivenessInterval nextInterval = nextIntervalIter.next();
			
			if (currInterval.getEndInstrId() >= nextInterval.getStartInstrId()) {
				nextInterval.setStartInstrId(currInterval.getStartInstrId());
				currIntervalIter.remove();
			}
		}
	}

	public void addUsePosition(int instrId) {
		usePositions.add(instrId);
	}
	
	public String toString() {
		String intervalsStr = intervals.stream()
				.map(RegisterLivenessInterval::toString)
				.collect(Collectors.joining(", ", "{ ", " }"));

		return "" + register + ": " + intervalsStr + ": " + usePositions + " mapped to %t" + allocRegId;
	}
}