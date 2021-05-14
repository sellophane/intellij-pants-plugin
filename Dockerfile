FROM bkardys/ideprobe-pants
ADD . /workspace
WORKDIR /workspace
ENV CACHE_NAME=C1.2
ENV PANTS_SHA="1.25.x-twtr"
ENV IJ_ULTIMATE=false
ENV PANTS_TEST_JUNIT_TEST_SHARD=0/2
ENV ENABLE_SCALA_PLUGIN=false
RUN ./scripts/prepare-ci-environment.sh
RUN ./scripts/setup-ci-environment.sh