package com.test.mysql.repository;


import com.test.mysql.entity.F_garbage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ElectronicDataForReportRepository extends JpaRepository<F_garbage, Long> {

    @Query("select e from f_garbage e " +
            "where e.categoryName like :categoryName and  e.department like :department  and e.up_Date >= :start and e.up_Date <= :end ")
    Page<F_garbage> findBy2Fields(@Param("categoryName") String categoryName, @Param("department") String department, @Param("start") Date start, @Param("end") Date end, Pageable pageRequest);

    @Query("select e from f_garbage e " +
            "where e.categoryName like :categoryName and  e.department like :department  and e.up_Date >= :start and e.up_Date <= :end ")
    List<F_garbage> findBy2Fields(@Param("categoryName") String categoryName, @Param("department") String department, @Param("start") Date start, @Param("end") Date end);

    Page<F_garbage> findAll(Pageable pageRequest);

    @Query(value = "select distinct category_name from f_garbage", nativeQuery = true)
    List<String> findCategory();

    @Query(value = "select distinct e.department,e.operator from f_garbage e where e.up_date BETWEEN :start and :end",nativeQuery = true)
    List<Object[]> findBy2Fields(@Param("start") Date start,@Param("end") Date end);


}
