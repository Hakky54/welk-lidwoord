package nl.altindag.welklidwoord.presentation.proxy;

public class ProxyModel {

    private String username;
    private String password;

    private String host;
    private int port;

    public ProxyModel() {

    }

    public ProxyModel(String username, String password, String host, int port) {
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

    public ProxyModel withUsername(String username) {
        this.username = username;
        return this;
    }

    public ProxyModel withPassword(String password) {
        this.password = password;
        return this;
    }

    public ProxyModel withHost(String host) {
        this.host = host;
        return this;
    }

    public ProxyModel withPort(int port) {
        this.port = port;
        return this;
    }
}
