#!/usr/bin/env bash

# Copyright 2021 Pants project contributors (see CONTRIBUTORS.md).
# Licensed under the Apache License, Version 2.0 (see LICENSE).
# script to setup new image (best to do on each intellij version bump)

USERNAME=bkardys

DOCKER_BUILDKIT=1 BUILDKIT_PROGRESS=plain docker build \
  --tag  $USERNAME/ideprobe-pants:latest \
  --file Dockerfile.deps .

docker build --tag ideprobe-pants:local .
