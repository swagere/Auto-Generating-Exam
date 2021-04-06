package com.group.auto_generating_exam.config.auto_generating;

import java.util.Date;
import java.util.Random;
import com.group.auto_generating_exam.util.timeUtils;

/**
 * 基于选择的基础之上
 * 在产生的解中选择性能好的解
 * 通过这些性能好的解产生更多的解
 * 然后再淘汰 直到满足条件或者到迭代次数
 *
 *
 * 问题：y=3*sin(x*x)+5*x;
 *
 */

public class BasicGene {

    public static class IntelligentTestSystem
    {
        static int population=100;           // 种群个数
        static int geneNumber = 2;          //每个个体（染色体）所用的基因个数


//        double[,] chromosome = new double[population, geneNumber];  //每个个体（染色体）的种群
        double[][] chromosome = new double[population][geneNumber];  //每个个体（染色体）的种群

        double[] fitness = new double[population];    //适应度



        double[] sampleX = new double[10];

        double[] sampleY = new double[10];

        public void InitialSample() {
            for (int i = 0; i < 10; i++)  //函数的10个坐标点 y=3*sin(x*x)+5*x;
            {
                sampleX[i] = i; // 随机选取10个点
                sampleY[i] = 3 * Math.sin(sampleX[i] * sampleX[i]) + 5 * sampleX[i];
            }
        }


        //-----------------------遗传算法------------------
        //初始化
       public void Initial() {
            InitialSample();

            Date date=new Date();
            Random rand = new Random(5 * (int) timeUtils.getSecondTimestamp(date));

            for (int i = 0; i < population; i++)  //随机生成染色体
            {
                for (int j = 0; j < 2; j++) {
                    //第i个个体 第j个基因
                    chromosome[i][j] = (rand.nextInt(200) - 100 ) / 10.0;
                }
            }
        }

        //计算个体适应度
        public double CalculateSingleFitness(int n) {
            double[] y = new double[10];

            double a = chromosome[n][0];
            double b = chromosome[n][1];

            double c = 0.0;      //保存误差平方和


            for (int i = 0; i < 10; i++)
            {
                y[i] = a * Math.sin(sampleX[i] * sampleX[i]) + b * sampleX[i];

                //将误差平方和加起来
                c = c + (sampleY[i] - y[i]) * (sampleY[i] - y[i]);
            }


            fitness[n] = -c ; // 希望误差越小，适应度越好

            return fitness[n];

        }

        //计算所有个体适应度
        public void CalculateFitness( )
        {
            for (int i = 0; i < population; i++) CalculateSingleFitness(i);
        }

        //排序
        public void Sort() {
            double instead =0;

            double ch0 = 0;

            double ch1 = 0;
  
            //冒泡排序
            for(int j=0;j<population ;j++)
            {
                for (int i = population-1; i >0; i--)
                {
                    if (fitness[i] > fitness[i - 1])
                    {
                        //交换邻近的两个

                        //交换适应度（适应度好的放在前面，适应度不好的放在后面）
                        instead = fitness[i - 1];

                        fitness[i - 1] = fitness[i];

                        fitness[i] = instead;

                        //交换染色体
                        ch0 = chromosome[i-1][0];

                        ch1 = chromosome[i-1][1];

                        chromosome[i - 1][0] = chromosome[i][0];

                        chromosome[i - 1][1] = chromosome[i][1];

                        chromosome[i][0] = ch0;

                        chromosome[i][1] = ch1;
                    }
            
                }
            }

        }

        //交叉算子
        public void Intercross(int father, int mother, int son, int daughter) {
            chromosome[son][0] = chromosome[father][0];
            chromosome[son][1] = chromosome[mother][1];

            chromosome[daughter][0] = 0.5 * chromosome[father][0] + 0.5 * chromosome[mother][0];
            chromosome[daughter][1] = 0.5 * chromosome[father][1] + 0.5 * chromosome[mother][1];
        }

        //变异算子
        public void Aberrance(int father, int son)
        {

            Date date=new Date();
            Random rand = new Random(5 * (int) timeUtils.getSecondTimestamp(date));

            chromosome[son][0] = chromosome[father][0] + (rand.nextInt(200) - 100) / 100.0;
            chromosome[son][1] = chromosome[father][1] + (rand.nextInt(200) - 100) / 100.0;
        }

        //繁衍
        public void Generate()
        {
            Date date=new Date();
            Random rand = new Random(7 * (int) timeUtils.getSecondTimestamp(date));

            //选取前30个个体为优秀种群

            for (int k = 31; k < 80; k+=2)  //30-80 交叉算子产生
            {

                int father = rand.nextInt(30);
                int mother = rand.nextInt(30);


                int son = k;
                int daughter = k+1;

                Intercross(father, mother, son, daughter);
            }

            for (int k = 81; k < 100; k ++) //80-100 交叉算子产生
            {
                int father = rand.nextInt(30);
                
                int son = k;

                Aberrance(father, son);
            }
        }
    }
}