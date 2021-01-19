package com.test.db.repo;

import com.test.db.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AccountsRepo extends CrudRepository<Account, UUID> {
    Account findByPhone(@Param("phone")String phone);
    Account findByFirstNameAndLastName(@Param("firstName")String firstName,@Param("lastName")String lastName);

}
