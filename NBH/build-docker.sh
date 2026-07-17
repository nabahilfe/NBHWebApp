#!/bin/bash

set -e

# Projektversion aus der pom.xml lesen
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

IMAGE_NAME="nbh-app"
DEST_DIR="/home/holu/"

docker build \
    --platform linux/amd64 \
    --build-arg APP_VERSION="${VERSION}" \
    -t "${IMAGE_NAME}:v${VERSION}" \
    -t "${IMAGE_NAME}:latest" \
    .

echo "Speichere Docker-Image..."

docker save \
    -o "${IMAGE_NAME}-v${VERSION}.tar" \
    "${IMAGE_NAME}:v${VERSION}"

echo
echo "Erzeugtes Image:"
echo "  ${IMAGE_NAME}:v${VERSION}"
echo "Archiv:"
echo "  ${IMAGE_NAME}-v${VERSION}.tar"

echo
echo "Kopiere das Docker-Image auf den Server..."
scp "${IMAGE_NAME}-v${VERSION}.tar" nbh:${DEST_DIR}
echo "${IMAGE_NAME}-v${VERSION}.tar wurde kopiert nach nbh:${DEST_DIR}"
