# Caddy Proxy und Wartungsseite

- index.html ist die Seite die bei Wartung angezeigt wird
-- sie muss im Ordner /srv/caddy/maintenance liegen

- Caddyfile ist die Konfigurationsdatei für den Caddy Reverse Proxy
-- sie muss im Ordner /srv/caddy liegen

- docker-compose.yml
-- für Caddy, muss in /srv/caddy liegen

## Script um Wartungsseite zu schalten

- maintenance.sh ist das script, es liegt logon home verzeichnis
