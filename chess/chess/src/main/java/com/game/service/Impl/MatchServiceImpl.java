package com.game.service.Impl;

import com.game.model.User;
import com.game.service.MatchService;
import com.game.util.IdGenerator;
import com.game.webSocket.MatchEndpoint;


import java.util.Map;

import java.util.concurrent.*;

public class MatchServiceImpl implements MatchService {

    private static final MatchServiceImpl instance = new MatchServiceImpl();
    private final Map<Integer, User> matchingUsers = new ConcurrentHashMap<>();
    private final BlockingQueue<User> matchQueue = new LinkedBlockingQueue<>();


    private MatchServiceImpl() {}

    public static MatchServiceImpl getInstance() {
        return instance;
    }

    @Override
    public void startMatch(User user) {
        if (matchingUsers.containsKey(user.getUserId())) {
            return; // 已在匹配中
        }

        matchingUsers.put(user.getUserId(), user);
        matchQueue.offer(user);

        tryMatch();
    }

    private void tryMatch() {
        while (matchQueue.size() >= 2) {
            User user1 = matchQueue.poll();
            User bestMatch = null;
            int minEloDifference = Integer.MAX_VALUE;

            // 遍历队列，找到 Elo 分数最接近的玩家
            for (User potentialMatch : matchQueue) {
                int eloDifference = Math.abs(user1.getEloScore() - potentialMatch.getEloScore());
                if (eloDifference < minEloDifference) {
                    minEloDifference = eloDifference;
                    bestMatch = potentialMatch;
                }
            }

            if (bestMatch != null) {
                matchQueue.remove(bestMatch);

                String roomId = IdGenerator.generateAlphaNumericId(6);

                // 通知双方匹配成功
                MatchEndpoint.notifyMatchSuccess(user1.getUserId(), bestMatch.getUserId(), roomId);
                MatchEndpoint.notifyMatchSuccess(bestMatch.getUserId(), user1.getUserId(), roomId);

                // 清理已匹配用户
                matchingUsers.remove(user1.getUserId());
                matchingUsers.remove(bestMatch.getUserId());
            }
        }
    }

    @Override
    public boolean cancelMatch(User user) {
        if (matchingUsers.remove(user.getUserId()) != null) {
            matchQueue.remove(user); // 从队列中移除
            return true;
        }
        return false;
    }

    @Override
    public boolean isMatching(User user) {
        return matchingUsers.containsKey(user.getUserId());
    }

}
