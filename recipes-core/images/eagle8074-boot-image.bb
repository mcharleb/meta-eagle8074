LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit deploy

DEPENDS += "kernel kernel-dtb android-tools-native"

SRC_URI += "https://releases.linaro.org/14.09/ubuntu/ifc6410/initrd.img-3.4.0-linaro-ifc6410;downloadfilename=initrd.img;name=initrd"
SRC_URI[initrd.md5sum] = "d92fb01531698e30615f26efa2999c6c"
SRC_URI[initrd.sha256sum] = "d177ba515258df5fda6d34043261d694026c9e27f1ef8ec16674fa479c5b47fb"

do_compile() {
}

do_install() {
    mkbootimg --kernel ${STAGING_DIR_TARGET}/boot/zImage \
	--dt ${STAGING_DIR_TARGET}/usr/share/${MACHINE}/devicetree.img \
	--base ${KERNEL_BASE} \
	--ramdisk ${WORKDIR}/initrd.img \
	--ramdisk_offset ${RAMDISK_OFFSET} \
	--cmdline "${KERNEL_CMDLINE}" \
	--pagesize ${PAGE_SIZE} \
	--output ${D}/${MACHINE}-boot-image.img
}

do_deploy() {
    cp ${D}/${MACHINE}-boot-image.img ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot-image.img
}

addtask do_deploy after do_install
