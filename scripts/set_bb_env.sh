#!/bin/bash
rebake() {
  bitbake -c cleanall $@ && bitbake $@
}

build-eagle8074(){
    bitbake cache-image    && \
    bitbake factory-image  && \
    bitbake persist-image  && \
    bitbake recovery-image && \
    bitbake userdata-image && \
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
