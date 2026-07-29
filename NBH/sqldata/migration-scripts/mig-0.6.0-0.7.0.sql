-- in allen Tabellen muss created_by_id not null sein


-- zuerst update wenn erforderlich

UPDATE ASSOCIATIONS SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE OFFERS SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE ROLES SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE AMOUNT_DOMAIN_VALUES SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE EVENTS SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE TEXT_CONTENTS SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE REGISTRATION_CODES SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE MEMBERS SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE MEMBERSHIP_FEES SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE TIME_TRANSFERS SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE TIME_CHEQUES SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE ACCOUNTING_ENTRIES SET created_by_id = 1 WHERE created_by_id IS NULL;

UPDATE TRANSACTIONS SET created_by_id = 1 WHERE created_by_id IS NULL;



-- dann alle auf not null setzen

ALTER TABLE ASSOCIATIONS ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE OFFERS ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE ROLES ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE AMOUNT_DOMAIN_VALUES ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE EVENTS ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE TEXT_CONTENTS ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE REGISTRATION_CODES ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE MEMBERS ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE MEMBERSHIP_FEES ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE TIME_TRANSFERS ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE TIME_CHEQUES ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE ACCOUNTING_ENTRIES ALTER COLUMN created_by_id SET NOT NULL;

ALTER TABLE TRANSACTIONS ALTER COLUMN created_by_id SET NOT NULL;




-- neue Tabellen für Fotos und Dokumente

/* Foto- oder Bildergalerie */
create table IMAGE_GALLERY (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    gallery_date DATE not null,
    gallery_description VARCHAR(250) not null,
    gallery_remark VARCHAR(250) /* Anmerkung für Editor, wird in der Gallery-Ansicht nicht angezeigt */,
    is_public BOOLEAN not null DEFAULT FALSE /* wenn nicht public dann nur für angemeldete Mitglieder sichtbar */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Bild zu einer Galerie */
create table IMAGES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    image_name VARCHAR(250) not null,
    image_size INTEGER not null,
    image BYTEA not null,
    thumbnail BYTEA not null,
    content_type VARCHAR(20) not null /* image/jpeg, image/png ... */,
    description VARCHAR(250),
    is_gallery_cover BOOLEAN not null DEFAULT FALSE /* Das repräsentative Bild für die Galerie das in der Galerieübersicht angezeigt wird */,
    belongs_to_id BIGINT /* FK id from IMAGE_GALLERY(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Sammlung von Dokumenten */
create table DOCUMENT_LIBRARY (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    library_description VARCHAR(250) not null,
    library_remark VARCHAR(250) /* Anmerkung für Editor, wird in der Library-Ansicht nicht angezeigt */,
    is_public BOOLEAN not null DEFAULT FALSE /* public zugänglich oder nur für Mitglieder */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Dokument - PDF erlauben, sonst nix */
create table DOCUMENTS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    document_name VARCHAR(250) not null,
    document_size INTEGER not null,
    document_d_data BYTEA not null,
    content_type VARCHAR(20) not null /* application/pdf */,
    description VARCHAR(250),
    belongs_to_id BIGINT /* FK id from DOCUMENT_LIBRARY(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);






