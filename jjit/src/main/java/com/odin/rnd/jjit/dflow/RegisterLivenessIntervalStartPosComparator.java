package com.odin.rnd.jjit.dflow;

import java.util.Comparator;

public class RegisterLivenessIntervalStartPosComparator implements Comparator<RegisterLivenessIntervals> {
	@Override
	public int compare(RegisterLivenessIntervals left, RegisterLivenessIntervals right) {
		int leftIntervalStart = left.getFirstIntervalStart();
		int rightIntervalStart = right.getFirstIntervalStart();
		
		if (leftIntervalStart < rightIntervalStart) {
			return -1;
		} else if (leftIntervalStart > rightIntervalStart) {
			return 1;
		}
		
		return 0;
	}
}
