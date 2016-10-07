DESCRIPTION = "Filesystem tweaks for Eagle"
LICENSE = "QUALCOMM-TECHNOLOGY-Proprietary"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti/files/qcom-licenses/${LICENSE};md5=400dd647645553d955b1053bbbfcd2de"

PR = "r0"
PV = "1.0"

inherit native

SRC_URI = "\
    file://qrlConfig.conf \
    file://qrlNetwork.conf \
"

do_install() {
    mkdir -p ${MOUNT_PATH}/copy/etc/init
    mkdir -p ${MOUNT_PATH}/copy/etc/network
    mkdir -p ${MOUNT_PATH}/copy/lib/udev/rules.d
    cp ${WORKDIR}/qrlConfig.conf ${MOUNT_PATH}/copy/etc/init/qrlConfig.conf
    cp ${WORKDIR}/qrlNetwork.conf ${MOUNT_PATH}/copy/etc/init/qrlNetwork.conf
}
