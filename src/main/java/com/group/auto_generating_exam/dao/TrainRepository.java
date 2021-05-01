package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.Exam;
import com.group.auto_generating_exam.model.Train;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Integer> {
    @Query("select u.train_id from Train u where u.sub_id = ?1")
    List<Integer> getTrainIdBySubId(String sub_id);
}
