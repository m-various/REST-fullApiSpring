package com.test.api;

import com.test.api.dto.ResponseBase;
import com.test.api.dto.request.AddMoneyRequest;
import com.test.api.dto.request.CreateUserRequest;
import com.test.api.dto.response.CreateUserResponse;
import com.test.db.Account;
import com.test.db.repo.AccountsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("account")
public class AccountApi {

    private final AccountsRepo accountsRepo;
    private final Logger logger = LoggerFactory.getLogger(getClass());


    public AccountApi(AccountsRepo accountsRepo) {
        this.accountsRepo = accountsRepo;
    }

    @PostMapping(value="create_user",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBase> createUser (@RequestBody CreateUserRequest request) {
      Account user = null;

       try {

           user = accountsRepo.findByFirstNameAndLastName(request.getFirstName(),request.getLastName());

           if (user == null){

               user = accountsRepo.findByPhone(request.getPhone());

           }else {

               return new ResponseEntity<ResponseBase>(new CreateUserResponse(user), HttpStatus.CREATED);
           }

           if (user == null){
               user = new Account(request.getFirstName(),request.getLastName(),request.getPhone());
               accountsRepo.save(user);
           }


        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<ResponseBase>(new CreateUserResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }


        return new ResponseEntity<ResponseBase>(new CreateUserResponse(user), HttpStatus.CREATED);
    }


    @PostMapping(value="addMoney",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBase> addMoney (@RequestBody AddMoneyRequest request) {
        Account user = null;

        try {

            user = accountsRepo.findById(request.getUserId()).orElse(null);

            if (user == null){
                return new ResponseEntity<ResponseBase>(new CreateUserResponse("User not found"), HttpStatus.NOT_FOUND);
            }

            user.setMoney(user.getMoney() + request.getAmount());
            accountsRepo.save(user);



        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<ResponseBase>(new CreateUserResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }


        return new ResponseEntity<ResponseBase>(new CreateUserResponse(user), HttpStatus.OK);
    }
}
