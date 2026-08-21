drop table if exists GALLERYIES cascade;
drop table if exists IMAGE_GALLERY cascade;

drop table if exists IMAGES cascade;

drop table if exists DOCUMENT_LIBRARY cascade;
drop table if exists LIBRARIES cascade;

drop table if exists DOCUMENTS cascade;



/* Foto- oder Bildergalerie */
create table if not exists GALLERIES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    gallery_date DATE /* use it only for images from one specific date, eg. Flohmarkt */,
    show_gallery_from DATE /* Starting Date for displaying Gallery to Public - if null, do not display */,
    show_gallery_to DATE /* if set, do not show after this date */,
    description VARCHAR(250) not null,
    remark VARCHAR(250) /* Anmerkung für Editor, wird in der Gallery-Ansicht nicht angezeigt */,
    is_public BOOLEAN not null DEFAULT FALSE /* wenn nicht public dann nur für angemeldete Mitglieder sichtbar */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Bild zu einer Galerie */
create table if not exists IMAGES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    file_name VARCHAR(250) not null,
    image_width INTEGER not null,
    image_height INTEGER not null,
    image_size INTEGER not null,
    image BYTEA not null /* max 1920 x 1920 - scale down at upload */,
    thumbnail BYTEA not null /* max 400 x 400 - scale down at upload */,
    content_type VARCHAR(20) not null /* image/jpeg, image/png ... */,
    description VARCHAR(250),
    is_gallery_cover BOOLEAN not null DEFAULT FALSE /* Das repräsentative Bild für die Galerie das in der Galerieübersicht angezeigt wird */,
    gallery_id BIGINT /* FK id from GALLERIES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Sammlung von Dokumenten */
create table if not exists LIBARIES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    show_library_from DATE /* Starting Date for displaying Library to Public - if null, do not display */,
    show_library_to DATE /* if set, do not show after this date */,
    description VARCHAR(250) not null,
    remark VARCHAR(250) /* Anmerkung für Editor, wird in der Library-Ansicht nicht angezeigt */,
    is_public BOOLEAN not null DEFAULT FALSE /* public zugänglich oder nur für Mitglieder */,
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
    lobrary_id BIGINT /* FK id from LIBARIES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


alter table IMAGES
    add constraint fk_IMAGES_gallery_id foreign key (gallery_id) references GALLERIES(id)
;


alter table DOCUMENTS
    add constraint fk_DOCUMENTS_lobrary_id foreign key (lobrary_id) references LIBARIES(id)
;

