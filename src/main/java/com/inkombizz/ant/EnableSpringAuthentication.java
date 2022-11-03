package com.inkombizz.ant;

import com.inkombizz.security.AuthenticationSpringConfig;
import com.inkombizz.security.WebSecurityConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({WebSecurityConfig.class, AuthenticationSpringConfig.class})
public @interface EnableSpringAuthentication {

}
