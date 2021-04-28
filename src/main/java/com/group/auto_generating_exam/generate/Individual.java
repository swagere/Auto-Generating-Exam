package com.group.auto_generating_exam.generate;

import java.util.Random;


public class Individual {

	public int[] xuanze_chrom;
	public int[] tiankong_chrom;
	public int[] jisuan_chrom;
	public int[] jianda_chrom;

	public int fitness;

	public double choice;

	public int parent1, parent2;

	@Override
	public String toString() {
		String info = "{染色体序列：[";
		for (int i = 0; i < xuanze_chrom.length; i++) {
			info = info+ xuanze_chrom[i];
		}info = info + "-" ;
		for (int i = 0; i < tiankong_chrom.length; i++) {
			info = info+ tiankong_chrom[i];
		}info = info + "-" ;
		for (int i = 0; i < jisuan_chrom.length; i++) {
			info = info+ jisuan_chrom[i];
		}info = info + "-" ;
		for (int i = 0; i < jianda_chrom.length; i++) {
			info = info+ jianda_chrom[i];
		}
		info = info + "] 、" + "个体适应度：" + fitness;
		info = info + " 、个体的父母为上代的：[" + parent1 + "," + parent2+"]}";
		return info;
	}

	private Individual(){

	}
	public Individual(int length1, int selected1, int length2, int selected2,
					  int length3, int selected3, int length4, int selected4) {
		xuanze_chrom = new int[length1];
		tiankong_chrom = new int[length2];
		jisuan_chrom = new int[length3];
		jianda_chrom = new int[length4];
		setSelected(xuanze_chrom, selected1);
		setSelected(tiankong_chrom, selected2);
		setSelected(jisuan_chrom, selected3);
		setSelected(jianda_chrom, selected4);
		parent1 = 0;
		parent2 = 0;
	}

	public void setSelected(int[] c, int selectedNum) {
		int temp = selectedNum;
		while (temp != 0) {
			int select = RandomNumber.randomIntNumber(0, c.length - 1);
			if (c[select] != 1) {
				temp--;
				c[select] = 1;
			}
		}
	}

	public void Viaration(double viaration_degree) {
		int viaration_sction = RandomNumber.randomIntNumber(1, 4);
		if (new Random().nextDouble() < viaration_degree) {
			int selectednum = 0;
			int[] temp_viaration_section;
			switch (viaration_sction) {
				case 1:
					temp_viaration_section=this.xuanze_chrom;
					break;
				case 2:
					temp_viaration_section=this.tiankong_chrom;
					break;
				case 3:

					temp_viaration_section=this.jisuan_chrom;
					break;
				default:
					temp_viaration_section=this.jianda_chrom;
					break;
			}
			for (int i = 0; i < temp_viaration_section.length; i++) {
				if(temp_viaration_section[i]==1)selectednum++;
				temp_viaration_section[i]=0;
			}
			while (selectednum != 0) {
				int select = RandomNumber.randomIntNumber(0, temp_viaration_section.length - 1);
				if (temp_viaration_section[select] != 1) {
					selectednum--;
					temp_viaration_section[select] = 1;
				}
			}
		}
	}

	public Individual copy(){
		Individual copy=new Individual();
		copy.xuanze_chrom=this.xuanze_chrom.clone();
		copy.tiankong_chrom=this.tiankong_chrom.clone();
		copy.jisuan_chrom=this.jisuan_chrom.clone();
		copy.jianda_chrom=this.jianda_chrom.clone();

		copy.fitness=this.fitness;

		copy.choice=this.choice;

		copy.parent1=this.parent1;
		copy.parent2=this.parent2;
		return copy;
	}
}
