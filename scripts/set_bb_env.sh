#!/bin/bash
rebake() {
  bitbake -c cleanall $@ && bitbake $@
}

build-eagle8074(){
    bitbake eagle8074-cache-image    && \
    bitbake eagle8074-factory-image  && \
    bitbake eagle8074-persist-image  && \
    bitbake eagle8074-recovery-image && \
    bitbake eagle8074-userdata-image && \
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
