#!/bin/bash

set -euo pipefail

# Projektversion aus der pom.xml lesen
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

IMAGE_NAME="nbh-app"
IMAGE_FILE="${IMAGE_NAME}.tar"
DEST_DIR="/home/holu/"

echo "========================================"
echo "Baue ${IMAGE_NAME} Version ${VERSION}"
echo "========================================"

docker build \
    --platform linux/amd64 \
    --build-arg APP_VERSION="${VERSION}" \
    -t "${IMAGE_NAME}:v${VERSION}" \
    -t "${IMAGE_NAME}:latest" \
    .

echo
echo "Speichere Docker-Image..."

docker save \
    -o "${IMAGE_FILE}" \
    "${IMAGE_NAME}:latest"

echo


echo "Kopiere Docker-Image auf den Server..."

BACKUP_DATE=$(date +%F)
REMOTE_IMAGE="${DEST_DIR%/}/${IMAGE_FILE}"
REMOTE_BACKUP="${REMOTE_IMAGE}-${BACKUP_DATE}"

ssh nbh "set -e
if [ -e '${REMOTE_IMAGE}' ]; then
    if [ -e '${REMOTE_BACKUP}' ]; then
        echo 'FEHLER: Backup existiert bereits: ${REMOTE_BACKUP}'
        exit 1
    fi
    mv -- '${REMOTE_IMAGE}' '${REMOTE_BACKUP}'
    echo 'Vorhandenes Image umbenannt: ${REMOTE_IMAGE} -> ${REMOTE_BACKUP}'
else
    echo 'Kein bestehendes Image am Ziel gefunden: ${REMOTE_IMAGE}'
fi"

scp "${IMAGE_FILE}" "nbh:${DEST_DIR}"

echo
echo "========================================"
echo "Build erfolgreich"
echo "Version : ${VERSION}"
echo "Image   : ${IMAGE_NAME}:v${VERSION}"
echo "Archiv  : ${IMAGE_FILE}"
echo "Ziel    : nbh:${DEST_DIR}"
echo "========================================"