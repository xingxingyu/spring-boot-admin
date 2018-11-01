package com.test.mysql.repository;


import com.test.mysql.entity.Operator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {
@Query("select t from Operator t where t.operatorName like :operatorName and t.department like :department")
    Page<Operator> findAllPage(@Param("operatorName") String operatorName,@Param("department") String department, Pageable pageable);

}
