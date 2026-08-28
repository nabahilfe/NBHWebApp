#!/bin/bash

set -e

MAINTENANCE_DIR="/srv/caddy/maintenance"
MAINTENANCE_FLAG="$MAINTENANCE_DIR/.enabled"

case "$1" in

    on)
        touch "$MAINTENANCE_FLAG"
        echo "Wartungsmodus AKTIVIERT."
        ;;

    off)
        rm -f "$MAINTENANCE_FLAG"
        echo "Wartungsmodus DEAKTIVIERT."
        ;;

    status)
        if [ -f "$MAINTENANCE_FLAG" ]; then
            echo "Wartungsmodus: AKTIV"
        else
            echo "Wartungsmodus: INAKTIV"
        fi
        ;;

    *)
        echo "Verwendung: $0 {on|off|status}"
        exit 1
        ;;

esac

