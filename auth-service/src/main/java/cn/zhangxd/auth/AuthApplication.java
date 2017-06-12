package cn.zhangxd.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.security.Principal;
import java.util.Collection;

@EnableDiscoveryClient
@SpringBootApplication
@EnableResourceServer
@RestController
@SessionAttributes("authorizationRequest")
public class AuthApplication {
    @Autowired
    FindByIndexNameSessionRepository<? extends ExpiringSession> sessions;


    @RequestMapping(value = "/user/logout")
    public Boolean logout( Principal principal ) {
        Collection<? extends ExpiringSession> usersSessions = this.sessions
                .findByIndexNameAndIndexValue(
                        FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
                        principal.getName() )
                .values();
        for( ExpiringSession expiringSession : usersSessions )  {
            sessions.delete( expiringSession.getId() );
        }
        return true;
    }


    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
