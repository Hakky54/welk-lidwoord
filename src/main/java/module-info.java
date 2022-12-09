/*
 * Copyright 2017 Thunderberry.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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