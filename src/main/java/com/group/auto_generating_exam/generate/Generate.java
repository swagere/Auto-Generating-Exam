package com.group.auto_generating_exam.generate;


import java.util.ArrayList;
import java.util.Random;

public class Generate {
	public double viaration_degree = 0.005;
	public static int xuanze_num = 10;
	public static int tiankong_num = 5;
	public static int jisuan_num = 2;
	public static int jianda_num = 2;
	public static final int[][] difficult_array = {
			{ 0, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, { 0, 2, 4, 6, 8, 9, 7, 5, 3, 1 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 } };

	public ArrayList<Individual> initial(int pop_size, QuestionFile q) {
		ArrayList<Individual> init_pop = new ArrayList<Individual>();
		Individual ind;
		for (int i = 0; i < pop_size; i++) {
			ind = new Individual(q.getXuanze_questions().size(), xuanze_num, q
					.getTiankong_questions().size(), tiankong_num, q
					.getJisuan_questions().size(), jisuan_num, q
					.getJianda_questions().size(), jianda_num);
			ind.fitness = individualFunction(ind, q);
			init_pop.add(ind);
		}
		computeSelectChoice(init_pop);
		return init_pop;
	}

	public ArrayList<Individual> update(ArrayList<Individual> old_pop,
										QuestionFile q) {
		ArrayList<Individual> new_pop = new ArrayList<Individual>();
		int ind1, ind2;
		while (new_pop.size() < old_pop.size()) {// 选择交叉
			ind1 = selectIndividualUseChoice(old_pop);
			do {
				ind2 = selectIndividualUseChoice(old_pop);
			} while (ind2 == ind1);
			crossover(new_pop, old_pop, ind1, ind2);
		}

		Individual ind;
		for (int i = 0; i < new_pop.size(); i++) {
			ind = new_pop.get(i);
			ind.Viaration(viaration_degree);
			ind.fitness = individualFunction(ind, q);
		}
		computeSelectChoice(new_pop);
		return new_pop;
	}

	public void crossover(ArrayList<Individual> new_pop,
						  ArrayList<Individual> old_pop, int ind1, int ind2) {
		Individual new_ind1 = old_pop.get(ind1).copy();
		Individual new_ind2 = old_pop.get(ind2).copy();
		int[] temp;
		switch (RandomNumber.randomIntNumber(1, 4)) {
			case 1:
				temp = new_ind1.xuanze_chrom;
				new_ind1.xuanze_chrom = new_ind2.xuanze_chrom;
				new_ind2.xuanze_chrom = temp;
				break;
			case 2:
				temp = new_ind1.tiankong_chrom;
				new_ind1.tiankong_chrom = new_ind2.tiankong_chrom;
				new_ind2.tiankong_chrom = temp;
				break;
			case 3:
				temp = new_ind1.jisuan_chrom;
				new_ind1.jisuan_chrom = new_ind2.jisuan_chrom;
				new_ind2.jisuan_chrom = temp;
				break;
			default:
				temp = new_ind1.jianda_chrom;
				new_ind1.jianda_chrom = new_ind2.jianda_chrom;
				new_ind2.jianda_chrom = temp;
				break;
		}
		new_ind1.parent1 = ind1;
		new_ind1.parent2 = ind2;
		new_ind2.parent1 = ind1;
		new_ind2.parent2 = ind2;
		new_pop.add(new_ind1);
		new_pop.add(new_ind2);
	}

	public int findBestIndividual(ArrayList<Individual> new_pop) {
		int fitness = new_pop.get(0).fitness;
		int position = 0;
		for (int i = 1; i < new_pop.size(); i++) {
			if (new_pop.get(i).fitness > fitness) {
				fitness = new_pop.get(i).fitness;
				position = i;
			}
		}
		return position;
	}

	public static int individualFunction(Individual ind, QuestionFile q) {
		int adapt = 0;
		for (int i = 1; i < ind.xuanze_chrom.length; i++) {
			if (ind.xuanze_chrom[i] == 1) {
				adapt += difficult_array[CreateTestPaper.difficult][q
						.getXuanze_questions().get(i).getDifficulty()];// 难度
//				adapt += q.getXuanze_questions().get(i).getIdentify();// 区分度
			}
		}
		for (int i = 1; i < ind.tiankong_chrom.length; i++) {
			if (ind.tiankong_chrom[i] == 1) {
				adapt += difficult_array[CreateTestPaper.difficult][q
						.getTiankong_questions().get(i).getDifficulty()];// 难度
//				adapt += q.getTiankong_questions().get(i).getIdentify();// 区分度
			}
		}
		for (int i = 1; i < ind.jisuan_chrom.length; i++) {
			if (ind.jisuan_chrom[i] == 1) {
				adapt += difficult_array[CreateTestPaper.difficult][q
						.getJisuan_questions().get(i).getDifficulty()];// 难度
//				adapt += q.getJisuan_questions().get(i).getIdentify();// 区分度
			}
		}
		for (int i = 1; i < ind.jianda_chrom.length; i++) {
			if (ind.jianda_chrom[i] == 1) {
				adapt += difficult_array[CreateTestPaper.difficult][q
						.getJianda_questions().get(i).getDifficulty()];// 难度
//				adapt += q.getJianda_questions().get(i).getIdentify();// 区分度
			}
		}

		return adapt;
	}

	public void computeSelectChoice(ArrayList<Individual> new_pop) {
		double fitness_all = 0;
		double accumulation = 0;
		for (int i = 0; i < new_pop.size(); i++) {
			fitness_all += new_pop.get(i).fitness;
		}
		for (int i = 0; i < new_pop.size(); i++) {
			accumulation += new_pop.get(i).fitness;
			new_pop.get(i).choice = accumulation / fitness_all;
		}
	}

	public int selectIndividualUseChoice(ArrayList<Individual> new_pop) {
		int position = 0;
		double propability = new Random().nextDouble();
		for (int i = 0; i < new_pop.size(); i++) {
			if (propability <= new_pop.get(i).choice) {
				position = i;
				break;
			}
		}
		return position;
	}

}
