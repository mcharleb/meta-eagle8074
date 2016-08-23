DESCRIPTION = "SDK Add-on Utilities"
LICENSE          = "QUALCOMM-TECHNOLOGY-Proprietary"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti/files/qcom-licenses/${LICENSE};md5=400dd647645553d955b1053bbbfcd2de"

PR = "r1"
PV = "1.0"

SRC_URI  = "file://qcom_flight_controller_hexagon_sdk_add_on.zip"

RDEPENDS_${PN} = "adsprpc libgcc glibc"

INSANE_SKIP_${PN} += "installed-vs-shipped"

FILES_${PN} += "/usr/share/data/adsp/*"
FILES_${PN} += "/usr/lib/*.so"
FILES_${PN}-staticdev += "/usr/lib/*.a"
FILES_${PN} += "/usr/tests/*"
FILES_${PN} += "/firmware/image/*"
FILES_${PN} += "/usr/include/sensor-imu/*"
FILES_${PN} += "/usr/bin/*"

FILES_${PN}-firmware += "/lib/firmware/*"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
#SKIP_FILEDEPS = "1"

PACKAGES = "${PN} ${PN}-firmware"
PROVIDES = "${PN} ${PN}-firmware"

do_install_append() {
    dest=/lib/firmware
    install -d ${D}${dest}
    install -m 0644 ${WORKDIR}/images/8074-eagle/normal/adsp_proc/obj/qdsp6v5_ReleaseG/LA/system/etc/firmware/*.* -D ${D}${dest}

    dest=/usr/lib
    install -d ${D}${dest}
    install -m 0644 ${WORKDIR}/flight_controller/krait/libs/*.* -D ${D}${dest}

#    # FIXME - these are being tested for lib deps with the wrong architecture
#    dest=/usr/share/data/adsp
#    install -d ${D}${dest}
#    install -m 0755 ${WORKDIR}/flight_controller/hexagon/libs/*.* -D ${D}${dest}
     
#    src=../../../../../../../meta-qti-flight-prebuilt/meta-eagle8074/files/  
#    dest=${DEPLOY_DIR_IMAGE}/out/sdk/
#    install -d ${dest}
#    install ${src}qcom_flight_controller_hexagon_sdk_add_on.zip ${dest}
#    install ${src}dronecontroller.zip ${dest}

# FIXME QA issue on install for rootfs, missing RDEPENDS
#    dest=/usr/tests
#    install -d ${D}${dest}
#    install -m 0755 ${WORKDIR}/flight_controller/hexagon/tests/*.* -D ${D}${dest}
#    install -m 0755 ${WORKDIR}/flight_controller/hexagon/tests/* -D ${D}${dest}

    dest=/usr/include/sensor-imu
    install -d ${D}${dest}
    install -m 0755 ${WORKDIR}/flight_controller/krait/inc/sensor_datatypes.h -D ${D}${dest}
    install -m 0755 ${WORKDIR}/flight_controller/krait/inc/SensorImu.hpp -D ${D}${dest}

# FIXME QA issue on install for rootfs, missing RDEPENDS
#    dest=/usr/bin
#    install -d ${D}${dest}
#    install -m 0755 ${WORKDIR}/flight_controller/krait/apps/* -D ${D}${dest}
}

INSANE_SKIP_${PN}-firmware += "arch"
INSANE_SKIP_${PN} += "textrel"
INSANE_SKIP_${PN} += "libdir"
INSANE_SKIP_${PN} += "ldflags"
