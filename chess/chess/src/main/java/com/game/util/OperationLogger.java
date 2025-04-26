package com.game.util;

import com.game.dao.OperationLogMapper;
import com.game.model.OperationLog;

public class OperationLogger {
    public static void log(String username, String operation, String detail, String ip) {
        try {
            OperationLog log = new OperationLog();
            log.setUsername(username);
            log.setOperation(operation);
            log.setDetail(detail);
            log.setIp(ip);
            new OperationLogMapper().insert(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
