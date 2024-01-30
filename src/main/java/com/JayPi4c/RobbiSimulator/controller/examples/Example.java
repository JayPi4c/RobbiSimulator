package com.JayPi4c.RobbiSimulator.controller.examples;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * This class holds all information needed for an example to be loaded. It
 * provides a method to load a new MainStage with the given Information.
 *
 * @author Jonas Pohl
 */
@Data
@Entity
@Table(name = "EXAMPLES")
public class Example {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String programName;

    @Lob
    @Column(columnDefinition = "clob")
    private String code;

    @Lob
    @Column(columnDefinition = "clob")
    private String territory;

    @ElementCollection
    @CollectionTable(name = "TAGS")
    private List<String> tags;

    /**
     * Loads a new MainStage with the given information.
     */
    public void load() {
        ProgramController.createAndShow(programName, code, territory);
    }

}
