package com.spring.demo.dao;


import com.spring.demo.anno.RepositoryTest;
import com.spring.demo.anno.Select;
import com.spring.demo.entity.User;

public interface UserDao {
    @Select("select * from user where id = ?")
    public User getUserById(String id);
}
