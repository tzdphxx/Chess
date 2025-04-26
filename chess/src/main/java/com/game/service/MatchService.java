package com.game.service;

import com.game.model.User;



public interface MatchService {
    void startMatch(User user);

    boolean cancelMatch(User user);

    boolean isMatching(User user);
}
