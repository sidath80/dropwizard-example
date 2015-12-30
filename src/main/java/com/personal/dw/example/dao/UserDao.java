package com.personal.dw.example.dao;

import com.personal.dw.example.model.User;

public interface UserDao {

    User getById(long id);

    User save(User user);

}
