package nl.altindag.welklidwoord.model;

import java.io.Serializable;

public class Proxy implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    private String host;
    private int port;

    public Proxy() {

    }

    public Proxy(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Proxy withUsername(String username) {
        this.username = username;
        return this;
    }

    public Proxy withPassword(String password) {
        this.password = password;
        return this;
    }

    public Proxy withHost(String host) {
        this.host = host;
        return this;
    }

    public Proxy withPort(int port) {
        this.port = port;
        return this;
    }
}
