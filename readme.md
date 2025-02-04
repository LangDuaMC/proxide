# proxide

a data collector for proxy, but actually useful. focussed on network monitoring. alternative to 
bungeecord_prometheus_exporter

## Config

### Bungeecord

```yml
# Instance identifier
instance: "default"

# Endpoint configuration
endpoint:
  host: "0.0.0.0"
  port: 25080

# Hooks configuration
hooks:
  players:
    enabled: true
  network:
    enabled: true

# Provider configuration
provider:
  maxmind:
    enabled: false
    id: ""
    key: ""
  xcord:
    enabled: true
```

## Exported data

- Player: server, IP ASN/City, session time, ping
- Server: backend ping (1s schedule)
- Java stats
- Counter of events:
  - Sum
  - Login/Disconnect/Joined
  - Chat/Command
  - Plugin Message

## Commands
- `/proxide-version`
- `/proxide-toggle-logging`