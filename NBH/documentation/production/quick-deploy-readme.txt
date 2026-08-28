## Build Docker File

- start Docker Desktop Application
- open terminal at NBH directory and run command
./build-docker.sh

- this builds docker image and copies docker image to server




## activate maintenance mode

- open terminal and run command
ssh nbh

- activate maintenance mode
sudo ./maintenance.sh on



## DB-Migration

- open terminal and run command
ssh -N nbh-pg

- connect pgadmin to server database
- execute migration script



## Deploy am Server im ssh terminal

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

- optional: container neu starten
docker compose up -d



## test deployment

- open browser and test deployment
https://acceptance-test.nabahilfe.eu




## de-activate maintenance mode in ssh terminal

- activate maintenance mode
sudo ./maintenance.sh off

















## Backup prüfen
- open terminal and run command
ssh nbh-backup
ls -lh /home/backups/nbh/postgres





