package com.group.auto_generating_exam.generate;

public class Question {

	public String type;

	public int score;

	public String context;

	public String answer;

	public int difficulty;

	public int identify;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Question() {
		this.type = "";
		this.context = "";
		this.difficulty = 0;
		this.identify = 0;
	}

	public void setAll(String type, String context, String answer,
					   int difficulty, int identify, int score) {
		this.type = type;
		this.context = context;
		this.difficulty = difficulty;
		this.identify = identify;
		this.score = score;
		this.answer = answer;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getIdentify() {
		return identify;
	}

	public void setIdentify(int identify) {
		this.identify = identify;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return  "  (类型：" + this.type + ",分值：" + this.score + ", 难度："
				+ this.difficulty + " )\n "+this.context ;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
