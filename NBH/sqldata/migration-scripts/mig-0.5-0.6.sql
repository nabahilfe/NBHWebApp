/* für Adress-Validierung und System-Konten */

ALTER TABLE members
    ADD COLUMN latitude DOUBLE PRECISION,
    ADD COLUMN longitude DOUBLE PRECISION,
    ADD COLUMN is_system_account BOOLEAN NOT NULL DEFAULT FALSE;

/* WICHTIG Update des Flags is_system_account für SysAdmin und Sozialkonto */


UPDATE members
SET is_system_account = first_name IN ('Administrator', 'Sozialkonto');



/* Foto- oder Bildergalerie */
create table if not exists IMAGE_GALLERY (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_date DATE not null,
    event_description VARCHAR(250) not null,
    event_text TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Bild zu einer Galerie */
create table if not exists IMAGES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    image_name VARCHAR(250) not null,
    image_size INTEGER not null,
    image_data BYTEA not null,
    description VARCHAR(250),
    is_gallery_cover BOOLEAN not null DEFAULT FALSE /* Das repräsentative Bild für die Gallerie das in der Gallerieübersicht angezeigt wird */,
    belongs_to_id BIGINT /* FK id from IMAGE_GALLERY(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);



/* Sammlung von Dokumenten */
create table if not exists DOCUMENT_GALLERY (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    document_description VARCHAR(250) not null,
    document_text TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* PDF Dokument */
create table if not exists PDF_DOCUMENT (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pdf_name VARCHAR(250) not null,
    pdf_size INTEGER not null,
    pdf_data BYTEA not null,
    description VARCHAR(250),
    belongs_to_id BIGINT /* FK id from DOCUMENT_GALLERY(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


alter table IMAGES
    add constraint fk_IMAGES_belongs_to_id foreign key (belongs_to_id) references IMAGE_GALLERY(id)
;


alter table PDF_DOCUMENT
    add constraint fk_PDF_DOCUMENT_belongs_to_id foreign key (belongs_to_id) references DOCUMENT_GALLERY(id)
;

