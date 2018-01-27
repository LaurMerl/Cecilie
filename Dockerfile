FROM hseeberger/scala-sbt


RUN apt install -y phantomjs

ENV hantomjs.binary.path = '/usr/bin/phantomjs'
