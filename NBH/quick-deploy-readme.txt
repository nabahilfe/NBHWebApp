## WICHTIG WICHTIG WICHTIG

## Im file application.yaml unbedingt die development settings deaktivieren und die deployment settings aktivieren!

## für development dann wieder umkehren!

gg:
    jte: 
        # development settings for local development
        #development-mode: true
        #use-precompiled-templates: false

        # deployment settings for production
        development-mode: false
        use-precompiled-templates: true

## Lokal bauen und testen

- open terminal at NBH directory and run command

./mvnw clean package

- start spring app and test local in browser

./mvnw clean spring-boot:run

## Build Docker File

- start Docker Desktop Application
- open terminal at NBH directory and run command

./build-docker.sh

- this builds docker image and copies docker image to server

## Display Maintenance Page

- log in to server: ssh nbh
- set caddy proxy to display maintenence page, run:

sudo ./maintenance.sh on

- use url https://acceptance-test.nabahilfe.eu to still access nbh app

## DB-Migration (falls erforderlich)

- open terminal and run command
ssh -N nbh-pg

- connect pgadmin to server database
- execute migration script

## Deploy am Server

- open terminal and run command
ssh nbh

- falls erforderlich, updates machen!
  apt list --upgradeable
  sudo apt update
  sudo apt full-upgrade -y
  sudo apt autoremove -y
  sudo apt autoclean
  sudo reboot

- deploy docker image to container
./deploy-image.sh

- show container and logs
docker ps -a
docker logs -f nbh-app

- container neu starten (nur erforderlich wenn sich an der jaml Datei was geändert hat)
docker compose up -d

## Disable Maintenance Page

sudo ./maintenance.sh off

## Backup prüfen

- open terminal and run command
ssh nbh-backup
ls -lh /home/backups/nbh/postgres
