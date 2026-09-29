package com.vcc.bigdata.stock.application.context;

import java.util.Optional;
/**
 * class save current user id on request's header in thread local
 */
public class UserContext {
    private static final ThreadLocal<Long> CURRENT_USER_ID = new ThreadLocal<>();

    public static void setCurrentUserId(Long currentUserId){
        CURRENT_USER_ID.set(currentUserId);
    }

    public static Optional<Long> getCurrentUserId(){
        return Optional.ofNullable(CURRENT_USER_ID.get());
    }

    /**
     * clean RAM after request
     */
    public static void clear(){
        CURRENT_USER_ID.remove();
    }
}
