
drop table if exists DOCUMENTS cascade;




/* Dokument - PDF erlauben, sonst nix */
create table if not exists DOCUMENTS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    file_name VARCHAR(250) not null,
    document_size INTEGER not null,
    document_data BYTEA not null,
    content_type VARCHAR(20) not null /* application/pdf */,
    description VARCHAR(250),
    library_id BIGINT /* FK id from LIBARIES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);



alter table DOCUMENTS
    add constraint fk_DOCUMENTS_library_id foreign key (library_id) references LIBARIES(id)
;

