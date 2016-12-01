#!/bin/bash
rebake() {
  bitbake -c cleanall $@ && bitbake $@
}

build-eagle8074(){
    bitbake packagegroup-qti-prebuild && \
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

# add in prebuilt layer
grep -q meta-qti-prebuilt conf/bblayers.conf || (echo 'BBLAYERS += "${TOPDIR}/../poky/meta-qti-prebuilt"' >> conf/bblayers.conf)
