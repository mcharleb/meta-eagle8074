LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit deploy

DEPENDS += "virtual/kernel android-tools-native"

do_compile() {
}

do_install() {
}

do_deploy() {
    mkbootimg --kernel ${DEPLOY_DIR_IMAGE}/zImage \
	--dt ${DEPLOY_DIR_IMAGE}/devicetree.img \
	--base ${KERNEL_BASE} \
	--ramdisk ${DEPLOY_DIR_IMAGE}/initrd.img \
	--ramdisk_offset ${RAMDISK_OFFSET} \
	--cmdline "${KERNEL_CMDLINE}" \
	--pagesize ${PAGE_SIZE} \
	--output ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot-image.img
}

addtask do_deploy after do_install
