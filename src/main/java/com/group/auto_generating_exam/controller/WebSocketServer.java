package com.group.auto_generating_exam.controller;

import com.alibaba.fastjson.JSON;
import com.group.auto_generating_exam.model.Exam;
import com.group.auto_generating_exam.service.ExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@ServerEndpoint(value = "/ws/asset")
public class WebSocketServer {

    //用来统计连接客户端的数量
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。  
    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<>();

    @Autowired
    ExamService examService;



    //--模板工具-----------------------------------------------------------------------
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        SessionSet.add(session);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1  
        log.info("有连接加入，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
//    @OnMessage
//    public void onMessage(String message, Session session) throws IOException {
//        log.info("来自客户端的消息：{}",message);
//        sendMessage(session, "Echo消息内容："+message);
//        // broadCastInfo(message); 群发消息
//    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SessionSet.remove(session);
        int cnt = OnlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 出现错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。 
     * @param session  session
     * @param map  消息
     */
    private static void sendMessage(Session session, Map map) throws IOException {
        map.put("session_id", session);
        session.getBasicRemote().sendText(map.toString());
    }

    /**
     * 群发消息
     *
     * @param message  消息
     */
    public static void broadCastInfo(Map message) throws IOException {
        for (Session session : SessionSet) {
            if(session.isOpen()){
                sendMessage(session, message);
            }
        }
    }



    //--考试实现---------------------------------------------------------
    /**
     * 接收消息
     * 开始考试
     * @param message
     * @param session
     */
    @OnMessage
    public void startExam(String message, Session session) throws IOException {
        //检验学生身份

        Integer type = Integer.valueOf(JSON.parseObject(message).get("type").toString());
        Integer exam_id = Integer.valueOf(JSON.parseObject(message).get("exam_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(message).get("user_id").toString()); //后期改成从登陆状态中获取用户user_id

        Map result = new HashMap();


        if (type == 999) {
            //--如果是请求考试剩余时间---------------------

            //返回前端
            Long last_time = examService.getLastTime(exam_id);
            Long rest_time = examService.getRestTimeByExamId(exam_id, last_time);
            result.put("type","100"); //type为10000表考试开始时返回，10002为请求失败
            result.put("message", rest_time);

            sendMessage(session, result);
        }
        else if (type == 9999) {
            //--如果是开始考试------------------------------

            //用户是否选择这门课程 即用户是否能参与这个考试
            Boolean isStuInExam = examService.isStuInExam(exam_id, user_id);
            if (!isStuInExam) {
                result.put("type","10002"); //type为10000表考试开始时返回，10002为请求失败
                result.put("message","用户没有选择该课程，不能参与考试");
            }
            //考试是否存在
            if (!examService.isExamExist(exam_id)) {
                result.put("type","10002"); //type为10000表考试开始时返回，10002为请求失败
                result.put("message","用户没有选择该课程，该考试不存在");
            }
            //检测是否超过考试时间/还未开始考试 若超过考试时间则不能考试
            Exam.ProgressStatus progress_status = examService.getExamProgressStatus(exam_id);
            if (progress_status.equals(Exam.ProgressStatus.WILL)) {
                result.put("type","10002"); //type为10000表考试开始时返回，10002为请求失败
                result.put("message","该考试还未开始");
            }
            if (progress_status.equals(Exam.ProgressStatus.DONE)) {
                result.put("type","10002"); //type为10000表考试开始时返回，10002为请求失败
                result.put("message","该考试已结束");
            }


            //检测是否已经分发给他试卷 若没有分发试卷则判定无法开始考试
            Boolean isGetExamQuestion = examService.isGetStuExamQuestion(exam_id, user_id);
            if (!isGetExamQuestion) {
                result.put("type","10002"); //type为10000表考试开始时返回，10002为请求失败
                result.put("message","该学生还未得到试卷");
            }


            //检测是否已经交卷 若已交卷则不能考试
            if (examService.isCommit(exam_id, user_id)) {
                result.put("type","10002"); //type为10000表考试开始时返回，10002为请求失败
                result.put("message","用户已交卷");
            }


            //返回前端：考试开始成功
            Long last_time = examService.getLastTime(exam_id);
            Long rest_time = examService.getRestTimeByExamId(exam_id, last_time);
            result.put("type","10001"); //type为10000表考试开始时返回，10002为请求失败
            result.put("message", rest_time);

            sendMessage(session, result);
        }
    }


    /**
     * 群发消息
     * 用于提前终止，通知所有已有连接的session，让他们停止考试
     *
     */
    public static void socketEndExam(Integer exam_id) throws IOException {
        Map result = new HashMap();
        result.put("type", "30001");
        result.put("message", 0);

        //广播出去
        broadCastInfo(result);
    }

    /**
     * 群发消息
     * 用于更改时间，通知所有已有连接的session，更改考试时间
     *
     */
    public static void socketChangExamTime(Long rest_time) throws IOException {
        Map result = new HashMap();
        result.put("type", "20001");
        result.put("message", rest_time);

        //广播出去
        broadCastInfo(result);
    }

    /**
     * 群发消息
     * 用于考试结束，通知所有已有连接的session，让他们停止考试
     *
     */
    public static void socketFinishExam(Integer exam_id) throws IOException {
    }

} 