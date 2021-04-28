package com.group.auto_generating_exam.generate;


import java.util.Vector;


public class QuestionFile {
	private int question_number = 10;
	private static QuestionFile q = null;

	public  final String xuanze_qusetions_path = "questionFile/选择题";
	public  final String tiankong_qusetions_path = "questionFile/填空题";
	public  final String jianda_qusetions_path = "questionFile/简答题";
	public  final String jisuan_qusetions_path = "questionFile/计算题";

	private static Vector<Question> xuanze_questions = null;
	private static  Vector<Question> tiankong_questions = null;
	private static  Vector<Question> jianda_questions = null;
	private  static Vector<Question> jisuan_questions = null;

	public synchronized  Vector<Question> getXuanze_questions() {
		if (xuanze_questions == null) {
			xuanze_questions = xmlFileAcess.getObjects(xuanze_qusetions_path);
		}
		return xuanze_questions;
	}

	public  void setXuanze_questions(Vector<Question> xuanze_questions) {
		xuanze_questions = xuanze_questions;
	}

	public synchronized  Vector<Question> getTiankong_questions() {
		if (tiankong_questions == null) {
			tiankong_questions = xmlFileAcess
					.getObjects(tiankong_qusetions_path);
		}
		return tiankong_questions;
	}

	public static void setTiankong_questions(
			Vector<Question> tiankong_questions) {
		tiankong_questions = tiankong_questions;
	}

	public synchronized  Vector<Question> getJianda_questions() {
		if (jianda_questions == null) {
			jianda_questions = xmlFileAcess.getObjects(jianda_qusetions_path);
		}
		return jianda_questions;
	}

	public static void setJianda_questions(Vector<Question> jianda_questions) {
		jianda_questions = jianda_questions;
	}

	public synchronized  Vector<Question> getJisuan_questions() {
		if (jisuan_questions == null) {
			jisuan_questions = xmlFileAcess.getObjects(jisuan_qusetions_path);
		}
		return jisuan_questions;
	}

	public  void setJisuan_questions(Vector<Question> jisuan_questions) {
		jisuan_questions = jisuan_questions;
	}

	public synchronized static QuestionFile getQFile() {
		// TODO Auto-generated method stub
		if (q == null) {
			q = new QuestionFile();
		}
		return q;
	}

//	 public static void main(String[] args) {
//	 // TODO Auto-generated method stub
//	 XMLEncoder writer;
//	 Question question;
//	 //选择题
//	 for (int i = 0; i < 11; i++) {
//	 question=new Question();
//	 question.setAll("选择题", "1+1=","2", 1, 2, 4);
//	 writer=xmlFileAcess.getWriteOut(QuestionFile.xuanze_qusetions_path+"/"+
//	 question.hashCode() + ".xml");
//	 writer.writeObject(question);
//	 writer.flush();
//	 writer.close();
//	 }
//	 //填空题
//	 for (int i = 0; i < 11; i++) {
//	 question=new Question();
//	 question.setAll("填空题", "1+1=","2", 1, 2, 4);
//	 writer=xmlFileAcess.getWriteOut(QuestionFile.tiankong_qusetions_path+"/"+
//	 question.hashCode() + ".xml");
//	 writer.writeObject(question);
//	 writer.flush();
//	 writer.close();
//	 }
//	 //计算题
//	 for (int i = 0; i < 11; i++) {
//	 question=new Question();
//	 question.setAll("计算题", "1+1=","2", 1, 2, 10);
//	 writer=xmlFileAcess.getWriteOut(QuestionFile.jisuan_qusetions_path+"/"+
//	 question.hashCode() + ".xml");
//	 writer.writeObject(question);
//	 writer.flush();
//	 writer.close();
//	 }
//	 //简答题
//	 for (int i = 0; i < 11; i++) {
//	 question=new Question();
//	 question.setAll("简答题", "1+1=","2", 1, 2, 10);
//	 writer=xmlFileAcess.getWriteOut(QuestionFile.jianda_qusetions_path+"/"+
//	 question.hashCode() + ".xml");
//	 writer.writeObject(question);
//	 writer.flush();
//	 writer.close();
//	 }
//	 ///////////////////
//	 System.out.println("生成题库成功！");
//	 }

}
