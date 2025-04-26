package com.game.dao;

import com.game.model.OperationLog;
import com.game.util.JDBC.curd;

import java.sql.SQLException;
import java.util.List;

public class OperationLogMapper {

    public int insert(OperationLog log) throws SQLException {
        String sql = "insert into operation_log (username, operation, detail, ip) values (?, ?, ?, ?)";
        return curd.UpdateData(sql, log.getUsername(), log.getOperation(), log.getDetail(), log.getIp());
    }

    public List<OperationLog> selectAll() throws Exception {
        String sql = "select * from operation_log order by create_time desc";
        return curd.Query(OperationLog.class, sql);
    }
}
