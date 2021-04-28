package com.group.auto_generating_exam.generate;

import java.util.Random;

public class RandomNumber {
	public static int randomIntNumber(int from, int to) {
		float a = from + (to - from) * (new Random().nextFloat());
		int b = (int) a;
		return ((a - b) > 0.5 ? 1 : 0) + b;
	}
}
