# This file was derived from oe-core/meta-qr-linux/meta-som8064/recipes-kernel/linux/linux-qr-som8064.bb

inherit kernel
inherit kernel-module-split
require recipes-kernel/linux/linux-yocto.inc

DEPENDS += "dtbtool-native"

FILESPATH =+ "${SOURCE}:"
S         =  "${WORKDIR}/linux"
KBUILD_DEFCONFIG = "msm8974_defconfig"
SRC_URI = "file://linux"
SRC_URI += "\
	    file://defconfig \
	    file://eagle8074.cfg \
            file://eagle8074.scc \
            file://eagle8074-user-config.cfg \
            file://0001-bluez.patch \
            file://0002-bluetooth.patch \
            file://0003-gcc4-9.patch \
           "
SRC_URI += "https://releases.linaro.org/14.09/ubuntu/ifc6410/initrd.img-3.4.0-linaro-ifc6410;downloadfilename=initrd.img;name=initrd"
SRC_URI[initrd.md5sum] = "d92fb01531698e30615f26efa2999c6c"
SRC_URI[initrd.sha256sum] = "d177ba515258df5fda6d34043261d694026c9e27f1ef8ec16674fa479c5b47fb"

# Install headers so they don't conflict with the system headers
KERNEL_SRC_PATH = "/usr/src/${MACHINE}"

COMPATIBLE_MACHINE_eagle8074 = "eagle8074"
LINUX_VERSION ?= "3.4"
LINUX_VERSION_EXTENSION ?= "-${MACHINE}"

KERNEL_BUILD_DIR = "${WORKDIR}/linux-eagle8074-standard-build"

PACKAGES_DYNAMIC += "^kernel-dtb-.*"
PACKAGES_DYNAMIC += "^kernel-dev-.*"

PACKAGES += "kernel-dtb"

PR = "r0"
PV = "${LINUX_VERSION}"

PROVIDES += "kernel-dtb kernel-dev kernel-module-cfg80211"

FILES_kernel-dtb = "/usr/share/eagle8074/devicetree.img"
FILES_kernel = "/boot/*"

do_removegit () {
   rm -rf "${S}/.git"
   rm -rf "${S}/.meta"
   rm -rf "${S}/.metadir"
}

do_install_append() {
    install -d ${D}/usr/share/eagle8074
    install -d ${D}/boot
    install ${KERNEL_BUILD_DIR}/arch/arm/boot/zImage ${D}/boot/zImage
    install ${WORKDIR}/initrd.img ${D}/boot/initrd.img
    make headers_install INSTALL_HDR_PATH="${D}/${KERNEL_SRC_PATH}"
    echo "Building device tree ${QRLINUX_KERNEL_DEVICE_TREE}..."
    oe_runmake ${QRLINUX_DTB}
    dtbtool -o "${D}/usr/share/eagle8074/devicetree.img" -p "${KERNEL_BUILD_DIR}/scripts/dtc/" -v "${KERNEL_BUILD_DIR}/arch/arm/boot/"
}

sysroot_stage_all_append() {
         sysroot_stage_dir "${D}/${KERNEL_SRC_PATH}" "${SYSROOT_DESTDIR}/${KERNEL_SRC_PATH}"
         sysroot_stage_dir "${D}/usr/share/eagle8074" "${SYSROOT_DESTDIR}/usr/share/eagle8074"
}

addtask do_removegit after do_unpack before do_kernel_checkout
