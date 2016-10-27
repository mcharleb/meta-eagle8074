DESCRIPTION = "SDK Add-on dynamic libs for Hexagon"
LICENSE          = "QUALCOMM-TECHNOLOGY-Proprietary"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti/files/qcom-licenses/${LICENSE};md5=400dd647645553d955b1053bbbfcd2de"

PR = "r1"
PV = "1.0"

PACKAGES = "${PN}"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI  = "file://qcom_flight_controller_hexagon_sdk_add_on.zip;subdir=sdk_add_on-${PV}"

S = "${WORKDIR}/sdk_add_on-${PV}"

FILES_${PN} = "/usr/share/data/adsp/*"

# Prevent processing SHLIBS because these are not ARM libs
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_DEFAULT_DEPS = "1"
SKIP_FILEDEPS = "1"
EXCLUDE_FROM_SHLIBS = "1"

RDEPENDS_${PN} = ""

do_install_append() {
    dest=/usr/share/data/adsp
    install -d ${D}${dest}
    install -m 0755 ${S}/flight_controller/hexagon/libs/*.* -D ${D}${dest}
}

INSANE_SKIP_${PN} += "arch file-rdeps libdir ldflags"
