### What is this?

This is a simple tool to log the requests hitting your server.
It can be particularly useful when you're learning this new-and-fancy JS framework, and for some reason your backend endpoints aren't being hit.
Right now it only supports GET and POST HTTP requests logging.

### Building

To build the app itself:

    gradle build

To build the docker image:

    gradle buildDockerImage

### Running

From console:

    gradle run

or, if you want to run it in Docker:

    docker run -p 5050:5050 io.github.dimyurich/wirelog:latest

### Future plans

As need be, this neat tool will be extended with logging capabilities for following transports:
* WebSocket
* gRPC
* graphql
* ...

### References and kudos

* http://mrhaki.blogspot.com/search/label/Ratpacked
* [Dan Woods' Learning Ratpack @ O'Reilly](http://shop.oreilly.com/product/0636920037545.do)