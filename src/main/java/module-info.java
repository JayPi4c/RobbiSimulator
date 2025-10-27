module RobbiSimulator {
    // jdk modules
    requires java.compiler;
    requires java.desktop;
    requires java.management;
    requires java.naming;
    requires java.rmi;
    requires jdk.compiler;

    // ui modules
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.media;
    requires javafx.web;

    requires eu.mihosoft.monacofx;
    requires org.controlsfx.controls;

    // logging modules
    requires org.slf4j;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.slf4j2.impl;

    // data modules
    requires jakarta.persistence;
    requires jakarta.xml.bind;
    requires org.apache.derby.tools;
    requires org.apache.derby.engine;
    requires eclipselink;
    uses jakarta.persistence.spi.PersistenceProvider;
    // requires org.eclipse.persistence.extension;

    requires static lombok;

    opens com.JayPi4c.RobbiSimulator.model to jakarta.xml.bind;
    opens com.JayPi4c.RobbiSimulator.controller.examples to eclipselink;

    exports com.JayPi4c.RobbiSimulator.utils.annotations;
    exports com.JayPi4c.RobbiSimulator.model;

    exports com.JayPi4c.RobbiSimulator to javafx.graphics;
    exports com.JayPi4c.RobbiSimulator.controller.tutor to java.rmi;
}
