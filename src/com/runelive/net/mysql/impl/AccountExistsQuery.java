package com.runelive.net.mysql.impl;

import com.runelive.GameServer;
import com.runelive.net.mysql.CompletedCallback;
import com.runelive.net.mysql.SQLCallback;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountExistsQuery implements SQLCallback {

    private static final String PREPARED_QUERY = "SELECT username FROM `accounts` as acc WHERE LOWER (`username`) = LOWER('?') LIMIT 1";
    private final String query;
    private CompletedCallback completedCallback;

    public AccountExistsQuery(String name) {
        this.query = PREPARED_QUERY.replace("?", name);
    }

    public void execute() {
        GameServer.getCharacterPool().executeQuery(this.query, this);
    }

    public AccountExistsQuery setCompletedCallback(CompletedCallback completedCallback) {
        this.completedCallback = completedCallback;
        return this;
    }

    @Override
    public void queryComplete(ResultSet result) throws SQLException {
        this.completedCallback.onCompletion(result);
    }

    @Override
    public void queryError(SQLException e) {
        e.printStackTrace();
    }

}
