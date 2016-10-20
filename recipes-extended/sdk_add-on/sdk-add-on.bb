DESCRIPTION = "SDK Add-on Utilities"
LICENSE          = "QUALCOMM-TECHNOLOGY-Proprietary"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti/files/qcom-licenses/${LICENSE};md5=400dd647645553d955b1053bbbfcd2de"

PR = "r1"
PV = "1.0"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI  = "file://qcom_flight_controller_hexagon_sdk_add_on.zip"

INSANE_SKIP_${PN} += "installed-vs-shipped"

FILES_${PN} += "/usr/lib/*.so"
FILES_${PN}-staticdev += "/usr/lib/*.a"
FILES_${PN} += "/usr/tests/*"
FILES_${PN} += "/firmware/image/*"
FILES_${PN} += "/usr/include/sensor-imu/*"
FILES_${PN} += "/usr/bin/*"

FILES_${PN}-firmware += "/lib/firmware/*"
FILES_${PN}-adsp += "/usr/share/data/adsp/*"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

PACKAGES = "${PN}"
DEPENDS_${PN} = "adsprpc"
RDEPENDS_${PN} = "adsprpc libgcc glibc"

do_install_append() {
    dest=/lib/firmware
    install -d ${D}${dest}
    install -m 0644 ${WORKDIR}/images/firmware/*.* -D ${D}${dest}

    dest=/usr/lib
    install -d ${D}${dest}
    install -m 0644 ${WORKDIR}/flight_controller/krait/libs/*.* -D ${D}${dest}

    dest=/usr/share/data/adsp
    install -d ${D}${dest}
    install -m 0755 ${WORKDIR}/flight_controller/hexagon/libs/*.* -D ${D}${dest}
     
    dest=${DEPLOY_DIR_IMAGE}/out/sdk/
    install -d ${dest}
    install ${WORKSPACE}/qcom_flight_controller_hexagon_sdk_add_on.zip ${dest}

    dest=/usr/tests
    install -d ${D}${dest}
    install -m 0755 ${WORKDIR}/flight_controller/hexagon/tests/* -D ${D}${dest}

    dest=/usr/include/sensor-imu
    install -d ${D}${dest}
    install -m 0755 ${WORKDIR}/flight_controller/krait/inc/sensor_datatypes.h -D ${D}${dest}
    install -m 0755 ${WORKDIR}/flight_controller/krait/inc/SensorImu.hpp -D ${D}${dest}
    install -m 0755 ${WORKDIR}/flight_controller/krait/inc/sensor_imu_api.h -D ${D}${dest}

    dest=/usr/bin
    install -d ${D}${dest}
    install -m 0755 ${WORKDIR}/flight_controller/krait/apps/* -D ${D}${dest}
}

INSANE_SKIP_${PN}-firmware += "arch"
INSANE_SKIP_${PN}-adsp += "arch"
INSANE_SKIP_${PN}-adsp += "already-stripped"
INSANE_SKIP_${PN} += "textrel"
INSANE_SKIP_${PN} += "libdir"
INSANE_SKIP_${PN} += "ldflags"
INSANE_SKIP_${PN} += "dev-deps"
