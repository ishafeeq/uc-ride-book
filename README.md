# uc-ride-book
1. Its a Java-8 based spring boot application
2. After starting up tis application, hit `/demo` endpoint to execute demo of app
3. Demo registers 6 riders and 4 drivers in system
4. Then location is updated for all these drivers
5. Uber's H3 library is used to generate location hashes of Rider and driver
6. Currently system supports searching drivers in a grid resolution of 13 (1-15) is overall resolution limit
7. Reference to H3: https://h3geo.org/docs/api/indexing
8. After searching available drivers, app creates ride
9. Then it completes one of the ongoing ride and cancels other
