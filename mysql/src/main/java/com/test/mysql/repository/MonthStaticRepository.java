package com.test.mysql.repository;

import com.test.mysql.entity.MonthStatisc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MonthStaticRepository extends JpaRepository<MonthStatisc, Long> {

    @Query(value = "select date_format(create_Time, '%Y%m%d') sdate,category_name as categoryName,round(sum(net_weight),2) as netWeight ,sum(peitai_num) as peitaiNum\n" +
            "  from f_garbage t where date_format(create_Time, '%Y%m') = :sdate\n" +
            "group by date_format(create_Time, '%Y%m%d'),category_name", nativeQuery = true)
    List<Object[]> selectMonth(@Param("sdate") String sdate);
}
