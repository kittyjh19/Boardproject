package com.example.jhproject.service;

import com.example.jhproject.dao.UserDao;
import com.example.jhproject.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserDao userDao;


    @Transactional
    public User addUser(String name, String email, String password){

        User user1 = userDao.getUser(email); //이메일 중복 검사
        if(user1 != null){
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        User user = userDao.addUser(email, name, password);
        userDao.mappingUserRole(user.getUserId());
        return user;

    }

    @Transactional
    public User getUser(String email){
        return userDao.getUser(email);
    }

    @Transactional(readOnly = true)
    public List<String> getRoles(int userId) {
        return userDao.getRoles(userId);
    }
}
