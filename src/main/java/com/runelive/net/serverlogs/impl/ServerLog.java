package com.runelive.net.serverlogs.impl;

import java.sql.Timestamp;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/20/2017.
 *
 * @author Seba
 */
public interface ServerLog {

    String createQuery();

    default Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

}
