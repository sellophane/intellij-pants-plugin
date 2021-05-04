#!/usr/bin/env bash

# Copyright 2021 Pants project contributors (see CONTRIBUTORS.md).
# Licensed under the Apache License, Version 2.0 (see LICENSE).

set -x

DOCKER_IMAGE=ideprobe-pants:local
DOCKER_DIRECTORY=/tmp/ideprobe/output
HOST_DIRECTORY=/tmp/ideprobe/output


mkdir -p "${HOST_DIRECTORY}"
docker run  \
  --mount type=bind,source="${HOST_DIRECTORY}",target="${DOCKER_DIRECTORY}" \
  "${DOCKER_IMAGE}" \
  bash scripts/run_tests.sh
