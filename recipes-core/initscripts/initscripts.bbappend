FILESEXTRAPATHS_prepend_eagle8074 := "${THISDIR}/${PN}:"

RDEPENDS_${PN} += "android_tools"

do_install_append_eagle8074() {
    ln -sf ../init.d/start_usb ${D}${sysconfdir}/rcS.d/S60start_usb
    ln -sf ../init.d/start_adbd ${D}${sysconfdir}/rcS.d/S61start_adbd
    ln -sf ../init.d/start_q6 ${D}${sysconfdir}/rcS.d/S04start_q6
}

PACKAGE_ARCH_eagle8074 = "${MACHINE_ARCH}"

