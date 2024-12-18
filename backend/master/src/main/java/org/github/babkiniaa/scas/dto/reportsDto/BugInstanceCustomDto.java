package org.github.babkiniaa.scas.dto.reportsDto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BugInstanceCustomDto {

    private String type;

    private int priority;

    private int cachedHashCode;

    private String category;

    private String message;

    private String source;

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
