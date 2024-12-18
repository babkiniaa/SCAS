package org.github.babkiniaa.scas.entity.reportsEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bug_instance")
@Table
public class BugInstanceCustom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;

    private int priority;

    private int cachedHashCode;

    private String category;

    private String message;

    private String Source;

//    private String oldInstanceHash;

    private String instanceHash;

//    private int instanceOccurrenceNum;
//
//    private int instanceOccurrenceMax;
//
//    private long firstVersion;
//
//    private long lastVersion;
//
//    private boolean introducedByChangeOfExistingClass;
//
//    private boolean removedByChangeOfPersistingClass;
//
//    private static final int INVALID_HASH_CODE = 0;
//
//    private static final String ELEMENT_NAME = "BugInstance";
//
//    private static boolean adjustExperimental;

}
