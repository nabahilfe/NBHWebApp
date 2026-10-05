## WICHTIG WICHTIG WICHTIG
## Im file application.yaml unbedingt die development settings deaktivieren und die deployment settings aktivieren!
## für development dann wieder umkehren!

gg:
    jte:
        ## development settings
        # development-mode: true
        # use-precompiled-templates: false
        
        ## deployment settings
        development-mode: false
        use-precompiled-templates: true


## Build Docker File

- start Docker Desktop Application
- open terminal at NBH directory and run command
./build-docker.sh

- this builds docker image and copies docker image to server

## Display Maintenance Page

- log in to server: ssh nbh
- set caddy proxy to display maintenence page, run: 
sudo maintenance.sh on
-- use url https://acceptance-test.nabahilfe.eu to still access nbh app


## DB-Migration

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

- container neu starten
docker compose up -d


## Backup prüfen
- open terminal and run command
ssh nbh-backup
ls -lh /home/backups/nbh/postgres
