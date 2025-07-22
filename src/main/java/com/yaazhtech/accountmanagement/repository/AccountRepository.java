package com.yaazhtech.accountmanagement.repository;

import com.yaazhtech.accountmanagement.data.PupilAccount;
import com.yaazhtech.accountmanagement.data.PupilPrimaryKey;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@EnableScan
@EnableScanCount
public interface AccountRepository extends CrudRepository<PupilAccount, PupilPrimaryKey> {

    Optional<PupilAccount> findByEmail(String email);

    boolean existsByEmail(String email);
}
