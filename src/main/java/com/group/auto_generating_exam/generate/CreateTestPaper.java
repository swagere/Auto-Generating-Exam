package com.group.auto_generating_exam.generate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class CreateTestPaper {
	public static int max_gen = 20;// 遗传执行次数

	public static int testSelect = 0;
	public static int difficult = 0;
	static int pop_size = 5;

	static Generate g = new Generate();

	static QuestionFile q = QuestionFile.getQFile();// 种群大小

	static ArrayList<Individual> pop;

	static StringBuffer resultContent;

	public static void main(String[] args) {
		final TestFrame show = new TestFrame("遗传算法应用于组卷");
		show.max_gen_f.setText(new Integer(max_gen).toString());
		show.pop_size_f.setText(new Integer(pop_size).toString());
		show.show();

		show.start.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent arg0) {
				switch (CreateTestPaper.testSelect) {
					case 0:
						Generate.xuanze_num = 10;
						Generate.tiankong_num = 5;
						Generate.jianda_num = 2;
						Generate.jisuan_num = 2;
						break;
					case 1:
						Generate.xuanze_num = 5;
						Generate.tiankong_num = 10;
						Generate.jianda_num = 2;
						Generate.jisuan_num = 2;
						break;
					case 2:
						Generate.xuanze_num = 10;
						Generate.tiankong_num = 5;
						Generate.jianda_num = 3;
						Generate.jisuan_num = 1;
						break;
					default:
						Generate.xuanze_num = 5;
						Generate.tiankong_num = 5;
						Generate.jianda_num = 3;
						Generate.jisuan_num = 3;
						break;
				}

				resultContent = new StringBuffer();
				if (show.max_gen_f.getText() != "")
					max_gen = Integer.parseInt(show.max_gen_f.getText());
				if (show.pop_size_f.getText() != "")
					pop_size = Integer.parseInt(show.pop_size_f.getText());
				pop = g.initial(pop_size, q);

				resultContent.append(pop.get(g.findBestIndividual(pop))
						+ ">>>>>>>>初始种群最佳个体\n\n\n");
				for (int i = 0; i < max_gen; i++) {
					pop = g.update(pop, q);
					resultContent.append(pop.get(g.findBestIndividual(pop))
							+ ">>>>>>>>第" + (i + 1) + "代最佳个体\n");
				}
				resultContent
						.append("\n\n============================生成试卷如下=========================       \n\n\n");
				int j = 1;

				Individual best = pop.get(g.findBestIndividual(pop));

				int[] c = best.xuanze_chrom;
				j = 1;
				resultContent
						.append("-----------------------【选择题】-----------------------:\n\n");
				for (int i = 0; i < c.length; i++)
					if (c[i] == 1) {
						resultContent.append("题目" + j++);
						resultContent.append(q.getXuanze_questions().get(i)
								+ "\n\n");
					}
				;

				c = best.tiankong_chrom;
				j = 1;
				resultContent
						.append("-----------------------【填空题】-----------------------:\n\n");
				for (int i = 0; i < c.length; i++)
					if (c[i] == 1) {
						resultContent.append("题目" + j++);
						resultContent.append(q.getTiankong_questions().get(i)
								+ "\n\n");
					}
				;

				c = best.jisuan_chrom;
				j = 1;
				resultContent
						.append("-----------------------【计算题】-----------------------:\n\n");
				for (int i = 0; i < c.length; i++)
					if (c[i] == 1) {
						resultContent.append("题目" + j++);
						resultContent.append(q.getJisuan_questions().get(i)
								+ "\n\n");
					}

				c = best.jianda_chrom;
				j = 1;
				resultContent
						.append("-----------------------【简答题】-----------------------:\n\n");
				for (int i = 0; i < c.length; i++)
					if (c[i] == 1) {
						resultContent.append("题目" + j++);
						resultContent.append(q.getJianda_questions().get(i)
								+ "\n\n");
					}
				;

				resultContent.append("\n\n\n\n\n\n");
				show.result.setText(resultContent.toString());
			}
		});

	}
}

class TestFrame extends JFrame {
	JPanel testcontent;

	JPanel control;

	TextField max_gen_f;

	TextField pop_size_f;

	JButton start;

	TextArea result;

	JComboBox test_ComboBox = null;

	JComboBox difficult_ComboBox = null;

	public TestFrame(String name) {
		super(name);
		setLocation(140, 41);
		setSize(1000, 700);
		// ///////////////////////////////////////////////////////////////
		String[] obj = { "选择10填空5简答2计算题2", "选择5填空10简答2计算题2", "选择10填空5简答3计算题1",
				"选择5填空5简答3计算题3" };

		test_ComboBox = new JComboBox();
		test_ComboBox.setSize(60, 25);
		test_ComboBox.setMaximumRowCount(5);// 设置最大可视行数

		for (int i = 0; i < obj.length; i++)
			test_ComboBox.addItem(obj[i]);

		// 下拉框事件处理，用匿名类实现
		test_ComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				CreateTestPaper.testSelect = test_ComboBox.getSelectedIndex();// 得到选择
			}
		});
		// ///////////////////////////////////////////////////////////////
		String[] obj1 = { "容易", "一般", "难" };

		difficult_ComboBox = new JComboBox();
		difficult_ComboBox.setSize(60, 25);
		difficult_ComboBox.setMaximumRowCount(5);// 设置最大可视行数

		for (int i = 0; i < obj1.length; i++)
			difficult_ComboBox.addItem(obj1[i]);

		// 下拉框事件处理，用匿名类实现
		difficult_ComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				CreateTestPaper.difficult = difficult_ComboBox
						.getSelectedIndex();// 得到选择
			}
		});
		// ///////////////////////////////////////////
		max_gen_f = new TextField();
		max_gen_f.setPreferredSize(new Dimension(50, 25));
		pop_size_f = new TextField();
		pop_size_f.setPreferredSize(new Dimension(50, 25));
		start = new JButton();
		start.setLabel("开始");
		start.setPreferredSize(new Dimension(80, 25));
		result = new TextArea();

		testcontent = new JPanel();
		testcontent.setPreferredSize(new Dimension(900, 540));
		testcontent.setLayout(new BorderLayout());
		testcontent.add(result);

		control = new JPanel();
		control.setPreferredSize(new Dimension(900, 60));

		control.setLayout(new FlowLayout());
		control.add(new JLabel("试卷组成："));
		control.add(test_ComboBox);
		control.add(new JLabel("试卷难度："));
		control.add(difficult_ComboBox);
		control.add(new JLabel("               "));
		control.add(new JLabel("遗传代数："));
		control.add(max_gen_f);
		control.add(new JLabel("种群数目："));
		control.add(pop_size_f);
		control.add(new JLabel("开始："));
		control.add(start);

		getContentPane().add(control);
		getContentPane().add(new JScrollPane(testcontent), BorderLayout.SOUTH);

	}
}
