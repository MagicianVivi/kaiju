# Exercise 2

To scale our application, the first thing we can do is setup a read-replica of our database, so that we can split our write and read workload,
at the price of some consistency when reading the data.

We can then setup a caching mechanism for our application output, that will get updated asynchronously by querying the application again.

Depending on the number of requests to our service, we should scale horizontally by deploying more instances of the user facing server/application.

After that, we can either:
+ keep one cache for all servers, but it might get slow for the users if there is too much load.
+ deploy one cache per server, but then the users might get 2 different responses on 2 successive calls if the caches are not synchronised between them.
+ deploy one cache per load-balanced group of servers, which might be more complicated, but we can then setup sticky sessions for the users,
so they are served from the same load-balancer, and have more consistent responses.

As for the write side, for some database software, we can just add more nodes for writing, and let them sync up.
If our database can't handle the load, we could setup a task queue at the cost of delayed updates of the data.

We can mitigate this, by having the queue broadcast messages to the master database and the cache(s).
Though it won't avoid us refreshing the cache from the database periodically, and we could end up sending invalid/unwanted data to the user if we're not careful.

To sum up, we can optimize as much as we want for availability as long as we're ready to delay the eventual consistency of our system (and handle network partitions).
