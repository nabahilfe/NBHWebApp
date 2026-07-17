/* für Adress-Validierung und System-Konten */

ALTER TABLE members
    ADD COLUMN latitude DOUBLE PRECISION,
    ADD COLUMN longitude DOUBLE PRECISION,
    ADD COLUMN stair VARCHAR(20),
    ADD COLUMN door VARCHAR(20),
    ADD COLUMN is_system_account BOOLEAN NOT NULL DEFAULT FALSE;


/* WICHTIG Update des Flags is_system_account für SysAdmin und Sozialkonto */
UPDATE members
SET is_system_account = first_name IN ('Administrator', 'Sozialkonto');



