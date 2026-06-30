package com.saas.b2b.auth.application.port.in;

public record LoginCommand(String email, String password) {
}
