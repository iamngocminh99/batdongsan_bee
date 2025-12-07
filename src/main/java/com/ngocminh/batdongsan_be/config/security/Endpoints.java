package com.ngocminh.batdongsan_be.config.security;

public class Endpoints {
    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/auth/**",
            "/api/properties/**",
            "/api/property-image/**",
            "/api/favorites/**",
            "/api/messages/**",
            "/api/payment/vnpay/**",
            "/api/users/**",
            "/api/plan/**",

    };
    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/auth/**",
            "/api/favorites/**",
            "/api/messages/**",
            "/api/payment/vnpay/**",
    };
    public static final String[] PUBLIC_PUT_ENDPOINTS = {};
    public static final String[] PUBLIC_DELETE_ENDPOINTS = {};

    // ADMIN
    public static final String[] ADMIN_GET_ENDPOINTS = {
            "/api/admin/**",
            "/api/users/**",

            "/api/locations/**",
            "/api/agents/**",
            "/api/notifications/**",
    };
    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/api/locations/**",
            "/api/users/**",
            "/api/agents/**",
            "/api/notifications/**",
    };
    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/api/locations/**",
            "/api/users/**",
            "/api/agents/**",
    };
    public static final String[] ADMIN_DELETE_ENDPOINTS = {
            "/api/locations/**",
            "/api/users/**",
            "/api/agents/**",
    };

    // AGENT
    public static final String[] AGENT_GET_ENDPOINTS = {
            "/api/properties/agent/**",
            "/api/property-image/**",
            "/api/users/**",
    };
    public static final String[] AGENT_POST_ENDPOINTS = {
            "/api/property-image/**",

    };
    public static final String[] AGENT_PUT_ENDPOINTS = {
            "/api/properties/**"
    };
    public static final String[] AGENT_DELETE_ENDPOINTS = {
            "/api/properties/**"
    };
}
