//package com.group.auto_generating_exam.config.gene.back;
//
//import com.group.auto_generating_exam.config.exception.CustomException;
//import com.group.auto_generating_exam.config.exception.CustomExceptionType;
//import com.group.auto_generating_exam.dao.SubjectRepository;
//import com.group.auto_generating_exam.model.Subject;
//import com.group.auto_generating_exam.model.TestQuestion;
//import com.group.auto_generating_exam.util.TimeUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class GeneOP
//{
//    @Autowired
//    SubjectRepository subjectRepository;
//    @Autowired
//    TestQuestionRepository testQuestionRepository;
//
//    public class QuestionDatabase {
//        int maxNumber = 10000;  //题库最大容量
//
//        int questionNumber = 0; //题库实际容量
//
//        List<TestQuestion> questions = new ArrayList<>(); //试题数组
//
//
//        int availableClusterNumber = 0;  //试题类别数目
//        List<TestQuestion> availableCluster = new ArrayList<>(maxNumber); //试题类
//        int[] availableKindRange = new int[20]; //题型范围
//
//        Date date=new Date();
//        Random myRand = new Random(5 * (int) TimeUtils.getSecondTimestamp(date));
//
//        //--数据库操作--------------------------------------------------------
//        //根据sub_id获取试卷信息
//        public Subject GetSubject(String sub_id) {
//            Subject subject = subjectRepository.getSubjectBySubId(sub_id);
//            if (subject == null) {
//                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "题目不存在");
//            }
//            return subject;
//        }
//
//        //初始化生成questions[]
//        public void GetDatabaseForTest() {
//            questionNumber = 2000;
//            for (int i = 0; i < questionNumber; i++) {
//                TestQuestion question = new TestQuestion();
//                question.setId(i);
//
//                question.setKind(myRand.nextInt(10));
//                question.setHard(Math.abs(myRand.nextInt(10)/10.0 - 0.01));
//                question.setDiff(Math.abs(myRand.nextInt(10)/10.0 - 0.01));
//                question.setScore(myRand.nextInt(19) + 1);
//                question.setChapter(myRand.nextInt(20));
//                question.setImportance(myRand.nextInt(3));
//
//                questions.add(question);
//            }
//        }
//
//        //将从数据库中得到的question值赋给questions对象 并通过输入初始化分数
//        public void GetTestQuestionFromDatabase() {
//            List<TestQuestion> qs = testQuestionRepository.findAll();
//            questionNumber = GetMaxTestQuestion(); //初始化试题库中总题目数
//            for (TestQuestion question : qs) {
//                TestQuestion q = new TestQuestion();
//                question.setId(question.id);
//
//                q.setKind(question.kind);
//                q.setHard(question.hard);
//                q.setDiff(question.diff);
//                q.setScore(question.score);
//                q.setChapter(question.chapter);
//                q.setImportance(question.importance);
//
//                questions.add(question);
//            }
//        }
//
//        public int maxQuestionID = 0;
//
//        //从数据库中得到最大的题目号（设定：题目不能删除）
//        public int GetMaxTestQuestion() {
//            return testQuestionRepository.getMaxTestQuestionId();
//        }
//
//        //将初始化的随机题目存入数据库
//        public void GenerateQuestionDatabase() {
//            GetDatabaseForTest(); //初始化questions[]
//
//            for (int i = 0; i < questionNumber; i++) {
//                TestQuestion q = new TestQuestion();
//                q.setId(i);
//                q.setKind(questions.get(i).kind);
//                q.setScore(questions.get(i).score);
//                q.setHard(questions.get(i).hard);
//                q.setDiff(questions.get(i).diff);
//                q.setChapter(questions.get(i).chapter);
//                q.setImportance(questions.get(i).importance);
//
//                q.setContent("question " + i);
//                q.setAnswer("answer " + i);
//
//                testQuestionRepository.save(q);
//            }
//        }
//
//
//        //--遗传算法预处理-------------------------------------------------------
//        // 排序 具体的试题
//        public void SortQuestionByAttribute(){
//            for (int i = 0; i < questionNumber; i++) {
//                for (int j = 0; j < questionNumber - i - 1; j++) {
//                    if (questions.get(j).Compare(questions.get(j + 1)) == 2) {
//                        Collections.swap(questions, j, j + 1);
//                    }
//                }
//            }
//        }
//
//        // 试题预操作
//        public void PreOperation() {
//
//            SortQuestionByAttribute();
//
//            // 初始化
//            for (int i = 0; i < 20; i++) {
//                availableKindRange[i] = 0;
//            }
//
//            availableClusterNumber = 0;
//
//
//            int firstTime = 0;
//
//            for (int i = 0; i < questionNumber; i++) {
//                if (firstTime == 0) {
//                    // 如果是第一个 则初始化为一个类
//                    availableCluster.add(questions.get(i));
//                    availableCluster.get(availableClusterNumber).setStart(i);
//                    availableCluster.get(availableClusterNumber).setCount(1);
//
//                    firstTime = 1;
//                }
//                else {
//                    //比较当前的题是不是属于当前类
//                    if (availableCluster.get(availableClusterNumber).Compare(questions.get(i)) == 0) {
//                        //如果是，则更新当前类
//                        TestQuestion q = availableCluster.get(availableClusterNumber);
//                        availableCluster.get(availableClusterNumber).setCount(availableCluster.get(availableClusterNumber).count++);
//                    }
//                    else {
//                        // 如果不是，则添加新类
//                        availableClusterNumber++;
//                        availableCluster.add(questions.get(i));
//                        availableCluster.get(availableClusterNumber).setStart(i);
//                        availableCluster.get(availableClusterNumber).setCount(1);
//                    }
//                }
//
//                int nKind = availableCluster.get(availableClusterNumber).kind;
//                availableKindRange[nKind + 1] = availableClusterNumber;
//                for (int n = nKind + 1; n < 20; n++) {
//                    availableKindRange[n] = availableKindRange[nKind + 1];
//                }
//            }
//        }
//
//        //--低分辨率下的遗传算法---------------------------------------------
//        //随机生成一类题的编号
//        public int GetRandQuestionClusterOrderByKind(int kind) {
//            int t0 = availableKindRange[kind];
//            int t1 = availableKindRange[kind + 1];
//            return myRand.nextInt(t1-t0+1) + t0;
//        }
//
//        //根据编号获得分类器
//        public TestQuestion GetQuestionClusterByOrder(int order) {
//            return availableCluster.get(order);
//        }
//
//        //--高分辨率下的遗传算法------------------------------------
//        //在同一类中获取其他题目
//        public int GetRandQuestionOrderInSameCluster(int order) {
//            return myRand.nextInt(availableCluster.get(order).count) + availableCluster.get(order).start;
//        }
//
//        //根据序号获取题目
//        public TestQuestion GetQuestionByOrder(int order) {
//            return questions.get(order);
//        }
//    }
//
//
//    public class Logistic {
//        double nextValue() {
//            //返回0-1的随机数
//            return Math.random();
//        }
//    }
//
//
//    public class IntelligentTestSystem {
//
//        int population = 100;
//        int maxNumber = 200; //一套试卷最多题目数
//
//        QuestionDatabase database = new QuestionDatabase();
//
//        int[][] chromosome = new int[population][maxNumber];
//        double[] fitness = new double[population];
//
//        //--常量---------------------------
//        int score;
//        double diff;
//        int testNumber; //需要组卷的试题总数
//        int[] testKind = new int[50];
//        int[] hardDistribute = new int[50];
//        int[] chapterDistribute = new int[50];
//        int[] importanceDistribute = new int[50];
//        double[] weight = new double[5];
//
//        int[] paperOrder = new int[maxNumber];
//
//        Date date=new Date();
//        Random myRand = new Random(5 * (int) TimeUtils.getSecondTimestamp(date));
//
//        Logistic logistic1 = new Logistic();
//        Logistic logistic2 = new Logistic();
//
//        public void SetPaperAttribute(int _score, double _diff, int[] _kind,int[] _hard, int[] _chap, int[] _impo) {
//            score = _score;
//            diff = _diff;
//            testKind = (int[]) _kind.clone();
//            hardDistribute = (int[]) _hard.clone();
//            chapterDistribute = (int[]) _chap.clone();
//            importanceDistribute = (int[]) _impo.clone();
//
//            testNumber = 0;
//            for (int i = 0; i < 10; i++) { //一共只有五种题
//                testNumber += testKind[i];
//            }
//
//            weight[0] = 1;
//            weight[1] = 1;
//            weight[2] = 1;
//            weight[3] = 1;
//            weight[4] = 1;
//        }
//
////        public List GetPaperAttribute() {
////            List out = new ArrayList();
////            out.add(score);
////            out.add(diff);
////            out.add((int[])testKind.clone());
////            out.add((int[])chapterDistribute.clone());
////            out.add((int[])importanceDistribute.clone());
////            out.add((int[])paperOrder.clone());
////
////            return out;
////        }
//
//        //--低分辨率下的遗传算法------------
//        //初始化 生成试卷
//        public void initialGroup() {
//            //生成population套试卷，保存在chromosome中
//            for (int i = 0; i < population; i++) {
//                int count = 0;
//
//                //nKind为题型，testKind[nKind]为每种题型的数目
//                for (int nKind = 0; nKind < 10; nKind++) {
//                    for (int k = 0; k < testKind[nKind]; k++) {
//                        // 生成一套卷子
//                        // 低分辨率下，随机生成每道题的编号，每道题的编号为类的编号
//                        chromosome[i][count ++] = database.GetRandQuestionClusterOrderByKind(nKind);
//                    }
//                }
//            }
//        }
//
//        //罚函数
//        double punishFunction(int n) {
//            //先进行排序
//            int[] cc = new int[testNumber];
//
//            for (int i = 0; i < testNumber; i++) {
//                cc[i] = chromosome[n][i];
//            }
//
//            for (int i = 0; i < testNumber; i++) {
//                for (int j = 0; j < testNumber - i - 1; j++) {
//                    if (cc[j] > cc[j + 1]) {
//                        int t = cc[j];
//                        cc[j] = cc[j + 1];
//                        cc[j + 1] = t;
//                    }
//                }
//            }
//
//            int collision = 0;
//
//            int sameCluster = 1;
//
//            //再计算同一类的题目的数目
//            for (int i = 0; i < testNumber - 1; i++) {
//                if (cc[i] == cc[i + 1]) {
//                    sameCluster++;
//                }
//                else {
//                    TestQuestion q = database.GetQuestionClusterByOrder(cc[i]);
//                    if (q.count < sameCluster) { // 若组卷模式中想要选的题目数量大于此类中题目总数
//                        collision += 1000; // 则罚函数的值变大
//                    }
//
//                    sameCluster = 1;
//                }
//            }
//            return collision;
//        }
//
//        //计算分布式误差
//        double calculateDistributeError(int[] a, int[] b, int length) {
//            double maxError = 0;
//            for (int i = 0; i < length; i++) {
//                if (Math.abs(a[i] - b[i]) > maxError) {
//                    maxError = Math.abs(a[i] - b[i]);
//                }
//            }
//            return maxError;
//        }
//
//        //计算两个向量点乘的内积
//        int DotProduct(int[] a, int[] b, int length) {
//            int t = 0;
//            for (int i = 0; i < length; i++) {
//                t += a[i] * b[i];
//            }
//            return t;
//        }
//
//        //计算两个向量的相关度
//        double CalculateRelativity(int[] a, int[] b, int length) {
//            int t1 = DotProduct(a, a, length);
//            int t2 = DotProduct(b, b, length);
//            int t3 = DotProduct(a, b, length);
//
//            if (t1 == 0 || t2 == 0) {
//                return 0;
//            }
//
//            return t3 / (Math.sqrt(t1) * Math.sqrt(t2));
//        }
//
//        //重新规范误差 分段线性函数
//        double RefineError(double v, double v0, double v1) {
//            // 分段线性函数：[v,v0]，不变；[v,v1]，误差*2；否则 误差*4
//            if (v < v0) {
//                return v;
//            }
//            if (v < v1) {
//                return 2*v;
//            }
//            return 4*v;
//        }
//
//        //计算每一个个体的适应度
//        public double calculateSingleFitness(int n) { // 计算第n个个体适应度
//            int[] thisHardDistribute = new int[5]; //难度分布
//            int[] thisChapterDistribute = new int[20]; //章节分布
//            int[] thisImportanceDistribute = new int[5]; //重要性分布
//
//            double thisDiff = 0;
//            double thisScore = 0;
//
//            for (int i = 0; i < testNumber; i++) {
//                int order = chromosome[n][i];
//                TestQuestion q = database.GetQuestionClusterByOrder(order);
//
//
//                int nScore = q.score;
//                thisDiff += q.score * q.diff;
//
//                thisScore += nScore;
//                thisHardDistribute[q.HardN()] += nScore; //求出每个等级实际的分数
//                thisChapterDistribute[q.chapter] += nScore;
//                thisImportanceDistribute[q.importance] += nScore;
//            }
//
//            if (thisScore > 0) {
//                thisDiff /= thisScore;
//            }
//            double[] error = new double[5];
//
//            // 计算误差
//            error[0] = Math.abs((thisScore - score)/score);
//            error[1] = 0;
//            if (thisDiff < diff) {
//                error[1] = diff - thisDiff;
//            }
//
//            error[2] = 1 - CalculateRelativity(thisHardDistribute, hardDistribute, 5);
//            error[3] = 1 - CalculateRelativity(thisChapterDistribute, chapterDistribute, 20);
//            error[4] = 1 - CalculateRelativity(thisHardDistribute, importanceDistribute, 5);
//
//            // 规范误差
//            error[0] = RefineError(error[0], 0.05, 0.15);
//            error[1] = RefineError(error[1], 0.05, 0.15);
//            error[2] = RefineError(error[2], 0.05, 0.15);
//            error[3] = RefineError(error[3], 0.05, 0.15);
//            error[4] = RefineError(error[4], 0.05, 0.15);
//
//            double totalError = 0;
//            for (int i = 0; i < 5; i++) {
//                totalError += error[i]*weight[i];
//            }
//
//            // 加上罚函数
//            // 防止某一类中抽取的题目数过多
//            double punishError = punishFunction(n);
//
//            // 保存于fitness[n]中
//            fitness[n] = -(totalError + punishError);
//
//            return fitness[n];
//
//        }
//
//        //计算所有个体的适应度
//        public void calculateFitness() {
//            for (int i = 0; i < population; i++) {
//                calculateSingleFitness(i);
//            }
//        }
//
//        //交换两套试卷（即交换两个个体）
//        public void swapChromosome(int i, int j) {
//            for (int h = 0; h < testNumber; h++) {
//                int ch0 = chromosome[i][h];
//                chromosome[i][h] = chromosome[j][h];
//                chromosome[j][h] = ch0;
//            }
//        }
//
//        //排序
//        public void sort() {
//            double instead = 0;
//            double ch0 = 0;
//            double ch1 = 0;
//
//            // 将适应度低的（即试卷好的）放在前面
//            // 冒泡
//            for (int j = 0; j < population; j++) {
//                for (int i = population - 1; i > 0; i--) {
//                    if (fitness[i] > fitness[i - 1]) {
//                        instead = fitness[i - 1];
//                        fitness[i - 1] = fitness[1];
//                        fitness[1] = instead;
//                        swapChromosome(i, i - 1); //交换两个对应的试卷
//                    }
//                }
//            }
//        }
//
//        //交叉算子
//        public void intercross(int father, int mother, int son, int daughter) {
//            //随机产生一个整数，整数不超过试卷试题数目
//            int t = (int)(logistic1.nextValue()*testNumber);
//
//            // 前面部分为父亲，后面部分为母亲
//            for (int i = 0; i < t; i++) {
//                chromosome[son][i] = chromosome[father][i];
//                chromosome[daughter][i] = chromosome[mother][i];
//            }
//
//            for (int j = t; j < testNumber; j++) {
//                chromosome[son][j] = chromosome[mother][j];
//                chromosome[daughter][j] = chromosome[father][j];
//            }
//        }
//
//        //变异算子
//        public void aberrance(int father, int son) {
//            //先将父亲的基因全部复制给儿子
//            for (int i = 0; i < testNumber; i++) {
//                chromosome[son][i] = chromosome[father][i];
//            }
//
//            //随机产生整数
//            int t = (int)(logistic2.nextValue()*testNumber);
//
//            TestQuestion q = database.GetQuestionClusterByOrder(chromosome[father][t]);
//
//            //在t处改变题的难度/区分度
//            chromosome[son][t] = database.GetRandQuestionClusterOrderByKind(q.kind); //保持同一题型
//        }
//
//        //产生新的种群
//        public void generateNewGroup() {
//            Date date=new Date();
//            Random random = new Random(7 * (int) TimeUtils.getSecondTimestamp(date));
//
//            // 将排序好后的前31个个体当作优秀个体，产生更大规模的种群
//            // 交叉
//            for (int k = 31; k < 80; k += 2) {
//                int father = random.nextInt(31);
//                int mother = random.nextInt(31);
//
//                // 产生两个新的组卷方案
//                int son = k;
//                int daughter = k + 1;
//
//                intercross(father, mother, son, daughter);
//            }
//
//            //变异
//            for (int k = 81; k < 100; k++) {
//                int father = random.nextInt(31); //选出一个父亲
//                int son = k;
//                aberrance(father, son);
//            }
//        }
//
//
//
//
//        //--高分辨率下的遗传算法------------
//        int[] clusterTheme = new int[maxNumber]; // 基于类的组卷模式
//
//        public void initialGroup_highResolution() {
//            //生成population套试卷，保存在chromosome中
//            for (int i = 0; i < population; i++) {
//                int count = 0;
//
//                //nKind为题型，testKind[nKind]为每种题型的数目
//                for (int nKind = 0; nKind < 10; nKind++) {
//                    for (int k = 0; k < testKind[nKind]; k++) {
//                        // 生成一套卷子
//                        // 随机取得一道题的编号
//                        chromosome[i][count ++] = database.GetRandQuestionOrderInSameCluster(clusterTheme[count]);
//                    }
//                }
//            }
//        }
//
//        double punishFunction_highResolution(int n) {
//            //第n套题
//            //先进行排序
//            for (int i = 0; i < testNumber; i++) {
//                for (int j = 0; j < testNumber - i - 1; j++) {
//                    if (chromosome[n][j] > chromosome[n][j + 1]) {
//                        int t = chromosome[n][j];
//                        chromosome[n][j] = chromosome[n][j + 1];
//                        chromosome[n][j + 1] = t;
//                    }
//                }
//            }
//
//            int collision = 0;
//
//            //再计算同一类的题目的数目
//            for (int i = 1; i < testNumber - 1; i++) {
//                // 防止出的题目是一样的 如果一个题目出了两次，则罚函数的值增加
//                if (chromosome[n][i] == chromosome[n][i + 1]) {
//                    collision += 1000;
//                }
//            }
//            return collision;
//        }
//
//        public double calculateSingleFitness_highResolution(int n) { // 计算第n个个体适应度
//            int[] thisHardDistribute = new int[5]; //难度分布
//            int[] thisChapterDistribute = new int[20]; //章节分布
//            int[] thisImportanceDistribute = new int[5]; //重要性分布
//
//            double thisDiff = 0;
//            double thisScore = 0;
//
//            for (int i = 0; i < testNumber; i++) {
//                int order = chromosome[n][i];
//                TestQuestion q = database.GetQuestionByOrder(order);
//
//                int nScore = q.score;
//                thisDiff += q.score * q.diff;
//
//                thisScore += nScore;
//                thisHardDistribute[q.HardN()] += nScore; //求出每个等级实际的分数
//                thisChapterDistribute[q.chapter] += nScore;
//                thisImportanceDistribute[q.importance] += nScore;
//            }
//
//            if (thisScore > 0) {
//                thisDiff /= thisScore;
//            }
//            double[] error = new double[5];
//
//            // 计算误差
//            error[0] = Math.abs((thisScore - score) / score);
//            error[1] = 0;
//            if (thisDiff < diff) {
//                error[1] = diff - thisDiff;
//            }
//
//            error[2] = 1 - CalculateRelativity(thisHardDistribute, hardDistribute, 5);
//            error[3] = 1 - CalculateRelativity(thisChapterDistribute, chapterDistribute, 20);
//            error[4] = 1 - CalculateRelativity(thisHardDistribute, importanceDistribute, 5);
//
//            // 规范误差
//            error[0] = RefineError(error[0], 0.05, 0.15);
//            error[1] = RefineError(error[1], 0.05, 0.15);
//            error[2] = RefineError(error[2], 0.05, 0.15);
//            error[3] = RefineError(error[3], 0.05, 0.15);
//            error[4] = RefineError(error[4], 0.05, 0.15);
//
//            double totalError = 0;
//            for (int i = 0; i < 5; i++) {
//                totalError += error[i]*weight[i];
//            }
//
//            // 加上罚函数
//            // 防止某一类中抽取的题目数过多
//            double punishError = punishFunction_highResolution(n);
//
//            // 保存于fitness[n]中
//            fitness[n] = -(totalError + punishError);
//
//            return fitness[n];
//
//        }
//
//        public void calculateFitness_highResolution() {
//            for (int i = 0; i < population; i++) {
//                calculateSingleFitness_highResolution(i);
//            }
//        }
//
//        public void sort_highResolution() {
//            double instead = 0;
//            double ch0 = 0;
//            double ch1 = 0;
//
//            // 将适应度低的（即试卷好的）放在前面
//            // 冒泡
//            for (int j = 0; j < population; j++) {
//                for (int i = population - 1; i > 0; i--) {
//                    if (fitness[i] > fitness[i - 1]) {
//                        instead = fitness[i - 1];
//                        fitness[i - 1] = fitness[1];
//                        fitness[1] = instead;
//                        swapChromosome(i, i - 1); //交换两个对应的试卷
//                    }
//                }
//            }
//        }
//
//        public void intercross_highResolution(int father, int mother, int son, int daughter) {
//            //随机产生一个整数，整数不超过试卷试题数目
//            int t = (int)(logistic1.nextValue()*testNumber);
//
//            // 前面部分为父亲，后面部分为母亲
//            for (int i = 0; i < t; i++) {
//                chromosome[son][i] = chromosome[father][i];
//                chromosome[daughter][i] = chromosome[mother][i];
//            }
//
//            for (int j = t; j < testNumber; j++) {
//                chromosome[son][j] = chromosome[mother][j];
//                chromosome[daughter][j] = chromosome[father][j];
//            }
//        }
//
//        public void aberrance_highResolution(int father, int son) {
//            //先将父亲的基因全部复制给儿子
//            for (int i = 0; i < testNumber; i++) {
//                chromosome[son][i] = chromosome[father][i];
//            }
//
//            //随机产生整数
//            int t = (int)(logistic2.nextValue()*testNumber);
//
//            //在t处改变题的难度/区分度
//            chromosome[son][t] = database.GetRandQuestionOrderInSameCluster(clusterTheme[t]); //保持同一类中的题【clusterTheme为组卷模式，clusterTheme[t]为第t位置是为哪个类，从这个类中得到题目编号】
//        }
//
//        public void generateNewGroup_highResolution() {
//            Date date=new Date();
//            Random random = new Random(7 * (int) TimeUtils.getSecondTimestamp(date));
//
//            // 将排序好后的前31个个体当作优秀个体，产生更大规模的种群
//            // 交叉
//            for (int k = 31; k < 80; k += 2) {
//                int father = random.nextInt(31);
//                int mother = random.nextInt(31);
//
//                // 产生两个新的组卷方案
//                int son = k;
//                int daughter = k + 1;
//
//                intercross_highResolution(father, mother, son, daughter);
//            }
//
//            //变异
//            for (int k = 81; k < 100; k++) {
//                int father = random.nextInt(31); //选出一个父亲
//                int son = k;
//                aberrance_highResolution(father, son);
//            }
//        }
//
//        //--遗传算法------------
//        public double GetResult(int n) {
//            int[] thisHardDistribute = new int[5]; //实际难度分布
//            int[] thisChapterDistribute = new int[20]; //实际章节分布
//            int[] thisImportanceDistribute = new int[5]; //实际重要性分布
//
//
//            double thisDiff = 0;
//            double thisScore = 0;
//
//            for (int i = 0; i < testNumber; i++) {
//                int order = chromosome[n][i];
//                TestQuestion q = database.GetQuestionByOrder(order);
//
//                int nScore = q.score;
//
//                thisDiff += q.score * q.diff;
//
//                thisScore += nScore;
//
//                thisHardDistribute[q.HardN()] += nScore;
//                thisChapterDistribute[q.chapter] += nScore;
//                thisImportanceDistribute[q.importance] += nScore;
//
//                paperOrder[i] = q.id;
//            }
//
//            if (thisScore > 0) {
//                thisDiff /= thisScore;
//            }
//
//            score = (int)thisScore;
//            diff = thisDiff;
//            hardDistribute = (int[])thisHardDistribute.clone();
//            chapterDistribute = (int[])thisChapterDistribute.clone();
//            importanceDistribute = (int[])thisImportanceDistribute.clone();
//
//            return 1;
//        }
//
//        public int[] GeneratePaperDesign(int score, double diff, int[] kind,int[] hard, int[] chap, int[] impo) {
//            //--初始化处理------------------
//            SetPaperAttribute(score, diff, kind, hard, chap, impo); //设置参数
//            database.GetTestQuestionFromDatabase(); // 从数据库中读取全部题目
////            database.GetDatabaseForTest();
//
//
//            database.PreOperation(); // 预处理数据：将试题聚类，并且得到每个类的信息
//
//            //--低分辨率下的遗传算法------------
//            // 按照类来进行遗传算法
//            initialGroup(); // 初始化种群 随机生成组卷方案
//
//            for (int iteration = 0; iteration < 200; iteration++) {
//                calculateFitness(); // 计算适应度
//                sort(); // 按适应度排序
//                generateNewGroup(); // 产生新种群（取前面三十个当作优秀个体，来生成更大规模的种群）
//            } // 得到组卷模式
//
//            //将chromosome排序
//            for (int n = 0; n < population; n++) {
//                for (int i = 0; i < testNumber; i++) {
//                    for (int j = 0; j < testNumber - i - 1; j++) {
//                        if (chromosome[n][j] > chromosome[n][j + 1]) {
//                            int t = chromosome[n][j];
//                            chromosome[n][j] = chromosome[n][j + 1];
//                            chromosome[n][j + 1] = t;
//                        }
//                    }
//                }
//            }
//
//            int[][] theme = new int[population][maxNumber];
//            double[] themeFitness = new double[population];
//
//            theme = (int[][])chromosome.clone();
//            themeFitness = (double[])fitness.clone();
//
//
//            for (int i = 0; i < testNumber; i++) {
//                clusterTheme[i] = chromosome[0][i]; // 将产生的模式存在clusterTheme中
//            }
//
//
//            //--在高分辨率下使用遗传算法--------------------------
//            // 根据前面所选出的最好的组卷模式
//            initialGroup_highResolution();
//
//            for (int iteration = 0; iteration < 200; iteration++) {
//                calculateFitness();
//                sort_highResolution(); //排序和选择
//                generateNewGroup_highResolution();
//            }
//
//            calculateFitness_highResolution(); // 得到最好的结果的适应度
//
//            GetResult(0);
//
//            return chromosome[0];
//        }
//
//        public void generateQuestion() {
//            database.GenerateQuestionDatabase();
//        }
//
//    }
//
//    //组卷调用
//    public int[] generateTest(int score, double diff, int[] kind,int[] hard, int[] chap, int[] impo) {
//        IntelligentTestSystem intelligentTestSystem = new IntelligentTestSystem();
//
//        return intelligentTestSystem.GeneratePaperDesign(score, diff, kind, hard, chap, impo);
//    }
//
//    //为某一门科目初始化试题
//    public void generateQuestion() {
//        IntelligentTestSystem intelligentTestSystem = new IntelligentTestSystem();
//        intelligentTestSystem.generateQuestion();
//    }
//
//}
