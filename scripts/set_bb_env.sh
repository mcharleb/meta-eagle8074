#!/bin/bash
rebake() {
  bitbake -c cleanall $@ && bitbake $@
}

build8x74(){
    bitbake 8x74-image && \
    bitbake lk
}

scriptdir=
if [[ -z ${BASH} ]]
then
   scriptdir="$(dirname $0)"
else
   scriptdir="$(dirname "${BASH_SOURCE}")"
fi
WS=$(readlink -f $scriptdir/../..)

export TEMPLATECONF=${WS}/meta-eagle8074/conf 
source ${WS}/oe-init-build-env
