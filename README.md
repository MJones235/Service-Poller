# Service-Poller

A simple service poller that keeps a list of services (defined by a URL), and periodically does a HTTP GET to each and
saves the response ("OK" or "FAIL).  This information is displayed on a web page.

To start the backend server:

```
cd service-poller
./gradlew clean run
```

To start the frontend server:
```
cd www
npm install
yarn start
```
