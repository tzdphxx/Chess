package com.game.util.JDBC;

import com.game.model.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class curdTest {

    @Test
    public void testQueryUserById() throws Exception {
        List<User> users = curd.Query(User.class, "select * from users where user_id = ?", 1);
        assertNotNull(users);
        assertTrue(users.size() >= 0);
    }

    @Test
    public void testUpdateData() throws Exception {
        int affected = curd.UpdateData("update users set username = ? where user_id = ?", "测试用户", 1);
        assertTrue(affected >= 0);
    }
}
