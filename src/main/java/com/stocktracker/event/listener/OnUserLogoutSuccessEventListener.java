package com.stocktracker.event.listener;

import com.stocktracker.event.OnUserLogoutSuccessEvent;
import com.stocktracker.cache.LoggedOutJwtTokenCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OnUserLogoutSuccessEventListener implements ApplicationListener<OnUserLogoutSuccessEvent> {

    private final LoggedOutJwtTokenCache tokenCache;

    @Autowired
    public OnUserLogoutSuccessEventListener(LoggedOutJwtTokenCache tokenCache) {
        this.tokenCache = tokenCache;
    }

    public void onApplicationEvent(OnUserLogoutSuccessEvent event) {
        if (null != event) {
            log.info(String.format("Log out success event received for user [%s]", event.getUserEmail()));
            tokenCache.markLogoutEventForToken(event);
        }
    }
}
