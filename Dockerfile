FROM bkardys/ideprobe-pants
ADD . /workspace
WORKDIR /workspace
RUN ./scripts/prepare-ci-environment.sh
RUN ./scripts/setup-ci-environment.sh