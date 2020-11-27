package com.bdg.bank_transaction.dao;

import com.bdg.bank_transaction.exception.CustomException;
import com.bdg.bank_transaction.model.User;

import java.util.List;

public interface UserDAO {

    List<User> getAllUsers() throws CustomException;

    User getUserById(long userId) throws CustomException;

    User getUserByName(String userName) throws CustomException;

    /**
     * @param user:
     * user to be created
     * @return userId generated from insertion. return -1 on error
     */
    long insertUser(User user) throws CustomException;

    int updateUser(Long userId, User user) throws CustomException;

    int deleteUser(long userId) throws CustomException;

}
