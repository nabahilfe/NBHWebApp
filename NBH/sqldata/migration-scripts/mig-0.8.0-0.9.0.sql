
drop table if exists DOCUMENTS cascade;
drop table if exists LIBARIES cascade;
drop table if exists LIBRARIES cascade;



/* Sammlung von Dokumenten */
create table if not exists LIBRARIES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    show_library_from DATE /* Starting Date for displaying Library to Public - if null, do not display */,
    show_library_to DATE /* if set, do not show after this date */,
    description VARCHAR(250) not null,
    remark VARCHAR(250) /* Anmerkung bzw. Beschreibung, wird in der Library-Ansicht angezeigt */,
    is_public BOOLEAN not null DEFAULT FALSE /* public zugänglich oder nur für Mitglieder */,
    from_member_id BIGINT /* FK id from MEMBERS(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Dokument - PDF erlauben, sonst nix */
create table if not exists DOCUMENTS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    file_name VARCHAR(250) not null,
    document_size INTEGER not null,
    document_data BYTEA not null,
    content_type VARCHAR(20) not null /* application/pdf */,
    description VARCHAR(250),
    library_id BIGINT /* FK id from LIBRARIES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);



alter table LIBRARIES
    add constraint fk_LIBRARIES_from_member_id foreign key (from_member_id) references MEMBERS(id)
;


alter table DOCUMENTS
    add constraint fk_DOCUMENTS_library_id foreign key (library_id) references LIBRARIES(id)
;




------------



ALTER TABLE GALLERIES
    ADD COLUMN from_member_id BIGINT;


alter table GALLERIES
    add constraint fk_GALLERIES_from_member_id foreign key (from_member_id) references MEMBERS(id)
;


