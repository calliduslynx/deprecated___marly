# Mar.ly - Url Shortener

Contains two sub-projects:

- **marly-worker**: lightweight server which redirects the short urls to the mapped ones
- **marly-core**: backend of marly, responsibly of mapping and user handling, as well as statistic things

### To start
- run 1-10 marly-worker (ports 9010-9020)
- run marly-core (port 8080)
- visit localhost:8080 (or localhost:901x)
- login with google account
- use short urls on workers 
