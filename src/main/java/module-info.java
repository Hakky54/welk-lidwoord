module nl.altindag.welklidwoord {
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.boot;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires flatter;
    requires javafx.fxml;
    requires javafx.controls;
    requires spring.beans;
    requires org.jsoup;
    requires java.net.http;
    requires org.apache.logging.log4j;
    requires io.github.hakky54.sslcontext.kickstart;

    opens nl.altindag.welklidwoord to spring.core;
    opens nl.altindag.welklidwoord.controller to javafx.fxml, spring.core;
    exports nl.altindag.welklidwoord to javafx.graphics, spring.beans, spring.context;
    exports nl.altindag.welklidwoord.controller to spring.beans;
    exports nl.altindag.welklidwoord.service to spring.beans;

}