package com.inkombizz;

public class AuthenticationConstant {
	public static final String UNAUTHORIZED_MESSAGE = "You do not have authorization";
	public static final String FORBIDDEN_MESSAGE = "You do not have access";

	public static final String USER_NAME = "username";

	public static final String TOKEN_VALIDATOR_URL_KEY = "${token.validator.url:http://localhost:8080/}";

	public static final String AUTHORIZATION = "Authorization";
	public static final String TOKEN = "token";
	public static final String EMPTY = "";
}
