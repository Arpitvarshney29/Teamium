package com.teamium.config;

import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.stereotype.Component;

@Component
public class TokenStore extends InMemoryTokenStore{
}
