#!/bin/bash

set -euo pipefail

IMAGE_NAME="nbh-app"
IMAGE_FILE="${IMAGE_NAME}.tar"

echo "========================================"
echo "Deploy ${IMAGE_NAME}"
echo "========================================"

if [ ! -f "${IMAGE_FILE}" ]; then
    echo "Fehler: ${IMAGE_FILE} wurde nicht gefunden."
    exit 1
fi

echo
echo "Lade Docker-Image..."
docker load < "${IMAGE_FILE}"

echo
echo "Starte bzw. aktualisiere Container..."
cd /srv/nbhapp || {
    echo "Fehler: Verzeichnis /srv/nbhapp konnte nicht geöffnet werden." >&2
    exit 1
}

docker compose up -d --remove-orphans

echo
echo "Bereinige ungenutzte Docker-Ressourcen..."
docker image prune -af

echo
echo "Installierte Version:"
CONTAINER_ID=$(docker compose ps -q)

if [ -n "${CONTAINER_ID}" ]; then
    docker inspect \
        --format='{{ index .Config.Labels "org.opencontainers.image.version" }}' \
        "${CONTAINER_ID}"
else
    echo "Container läuft nicht!"
fi

echo
echo "Containerstatus:"
docker compose ps

echo
echo "========================================"
echo "Deployment erfolgreich abgeschlossen."
echo "========================================"