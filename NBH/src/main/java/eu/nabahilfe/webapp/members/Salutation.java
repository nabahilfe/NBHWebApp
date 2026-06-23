/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

// see https://thorben-janssen.com/hibernate-enum-mappings/
// we use the following mapping
// @Enumerated(EnumType.STRING)
// to store the enum value as a string in the database

public enum Salutation {
    //do not change the names, they are used in the database!
    Divers,
    Herr,
    Frau
}
