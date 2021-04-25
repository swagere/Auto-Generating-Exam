package com.group.auto_generating_exam.config;

import com.group.auto_generating_exam.controller.WebSocketServer;
import com.group.auto_generating_exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    //添加下面配置 在socket引入socketUserService
    @Autowired
    public void examService(ExamService examService){
        WebSocketServer.examService = examService;
    }
}