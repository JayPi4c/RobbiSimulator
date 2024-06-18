module RobbiSimulator {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.media;
    requires javafx.web;

    requires eu.mihosoft.monacofx;
    requires org.controlsfx.controls;

    requires java.desktop;
    requires java.rmi;
    requires java.compiler;

    requires lombok;
    requires org.slf4j;

    requires jakarta.persistence;
    requires jakarta.xml.bind;
    requires org.hibernate.orm.core;
    requires org.apache.logging.log4j;
    requires org.apache.derby.tools;

    opens com.JayPi4c.RobbiSimulator.controller.examples to org.hibernate.orm.core;
    opens com.JayPi4c.RobbiSimulator.model to jakarta.xml.bind;

    exports com.JayPi4c.RobbiSimulator.utils.annotations;
    exports com.JayPi4c.RobbiSimulator.model;
    exports com.JayPi4c.RobbiSimulator;
}