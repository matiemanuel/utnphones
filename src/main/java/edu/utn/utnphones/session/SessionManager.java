package edu.utn.utnphones.session;

import edu.utn.utnphones.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import static edu.utn.utnphones.model.User.Type.employee;

@Component
public class SessionManager {

    Map<String, Session> sessionMap = new Hashtable<>();

    int sesionExpiration = 60;

    public String createSession(User user) {
        String token;

        if (user.getUser_type() == employee) {
            token = "backoffice";
            System.out.println("Logueado como empleado");
        } else {
            token = UUID.randomUUID().toString();
            System.out.println("Logueado como cliente ");
        }
        sessionMap.put(token, new Session(token, user, new Date(System.currentTimeMillis())));
        return token;
    }

    public Session getSession(String token) {

        if (StringUtils.isEmpty(token)) return null;
        Session session = sessionMap.get(token);
        if (session != null) {
            session.setLastAction(new Date(System.currentTimeMillis()));
        }
        return session;
    }

    public void removeSession(String token) {
        sessionMap.remove(token);
    }

    public void expireSessions() {
        for (String k : sessionMap.keySet()) {
            Session v = sessionMap.get(k);
            if (v.getLastAction().getTime() < System.currentTimeMillis() + (sesionExpiration * 1000)) {
                System.out.println("Expiring session " + k);
                sessionMap.remove(k);
            }
        }
    }

    public User getCurrentUser(String token) {
        return getSession(token).getLoggedUser();
    }
}