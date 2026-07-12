#!/bin/bash

set -e

# Projektversion aus der pom.xml lesen
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

IMAGE_NAME="nbh-app"

docker build \
    --platform linux/amd64 \
    --build-arg APP_VERSION="${VERSION}" \
    -t "${IMAGE_NAME}:v${VERSION}" \
    -t "${IMAGE_NAME}:latest" \
    .

docker build \
    --platform linux/amd64 \
    --build-arg APP_VERSION="${VERSION}" \
    -t "${IMAGE_NAME}:v${VERSION}" \
    .

echo
echo "Fertig."
echo "Erzeugtes Image:"
echo "  ${IMAGE_NAME}:v${VERSION}"