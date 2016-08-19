# This file was derived from oe-core/meta-qr-linux/meta-som8064/recipes-kernel/linux/linux-qr-som8064.bb

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

DEPENDS += "dtc-native dtbtool-native android-tools"
FILESPATH =+ "${SOURCE}:"
S         =  "${WORKDIR}/linux"
KBUILD_DEFCONFIG = "msm8974_defconfig"
SRC_URI = "file://linux"
SRC_URI += "\
	    file://defconfig \
	    file://eagle8074.cfg \
            file://eagle8074.scc \
            file://eagle8074-user-config.cfg \
            file://bluetooth.patch;apply=no \
           "

SRC_URI += "https://releases.linaro.org/14.09/ubuntu/ifc6410/initrd.img-3.4.0-linaro-ifc6410;downloadfilename=initrd.img;name=initrd"
SRC_URI[initrd.md5sum] = "d92fb01531698e30615f26efa2999c6c"
SRC_URI[initrd.sha256sum] = "d177ba515258df5fda6d34043261d694026c9e27f1ef8ec16674fa479c5b47fb"

#GCCVERSION="4.8%"

LINUX_VERSION ?= "3.4"
LINUX_VERSION_EXTENSION ?= "-eagle8074"
COMPATIBLE_MACHINE_eagle8074 = "eagle8074"

KERNEL_BUILD_DIR = "${WORKDIR}/linux-eagle8074-standard-build"

PR = "r0"
PV = "${LINUX_VERSION}"

FILES_${PV} += "/usr/src/${MACHINE}/*"

PROVIDES += "kernel-module-cfg80211"

do_removegit () {
   rm -rf "${S}/.git"
   rm -rf "${S}/.meta"
   rm -rf "${S}/.metadir"
}

do_deploy_append() {
    rm -f "${DEPLOYDIR}/devicetree.img" "${DEPLOYDIR}/boot-eagle8074.img"
    echo "Building device tree ${QRLINUX_KERNEL_DEVICE_TREE}..."
    oe_runmake ${QRLINUX_DTB}
    dtbtool -o "${DEPLOYDIR}/devicetree.img" -p "${KERNEL_BUILD_DIR}/scripts/dtc/" -v "${KERNEL_BUILD_DIR}/arch/arm/boot/"
    mkbootimg --kernel ${KERNEL_BUILD_DIR}/arch/arm/boot/${KERNEL_IMAGETYPE} \
	--dt ${DEPLOYDIR}/devicetree.img \
	--base ${KERNEL_BASE} \
	--ramdisk ${WORKDIR}/initrd.img \
	--ramdisk_offset ${RAMDISK_OFFSET} \
	--cmdline "${KERNEL_CMDLINE}" \
	--pagesize ${PAGE_SIZE} \
	--output ${DEPLOYDIR}/boot-eagle8074.img

# FIXME Unsupported in the Jethro version of mkbootimg
#	--dt "${DEPLOYDIR}/devicetree.img" \

#mkbootimg --kernel ${KERNEL_BUILD_DIR}/arch/arm/boot/zImage --dt ${KERNEL_BUILD_DIR}/arch/arm/boot/masterDTB --base 0x80200000 --ramdisk ${WORKDIR}/initrd.img --ramdisk_offset 0x02D00000 --cmdline console=ttyHSL0,115200,n8 root=/dev/mmcblk0p13 rw rootwait  prim_display=hdmi_msm --pagesize 2048 --output /home/mcharleb/work/OE-8074/oe-core-08-12/oe-core/build/tmp-eglibc/deploy/images/eagle8074/boot-eagle8074.img
}

do_install_append() {
     make headers_install INSTALL_HDR_PATH="${D}/usr/src/${MACHINE}"
}

sysroot_stage_all_append() {
         sysroot_stage_dir "${D}/usr/src/${MACHINE}" "${SYSROOT_DESTDIR}/usr/src/${MACHINE}"
}

addtask do_removegit after do_unpack before do_kernel_checkout
