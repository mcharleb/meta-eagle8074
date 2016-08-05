SUMMARY = "8x74 default image"

DEPENDS += "lk"

IMAGE_INSTALL = "packagegroup-core-boot ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_INSTALL += "android-tools"
IMAGE_INSTALL += "gdb"
IMAGE_INSTALL += "gcc"
IMAGE_INSTALL += "dpkg"
IMAGE_INSTALL += "adsprpc"
#IMAGE_INSTALL += "qcacld-ll"
IMAGE_INSTALL += "kernel-module-wlan"
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "hostapd"
IMAGE_INSTALL += "wpa-supplicant"
IMAGE_INSTALL += "pciutils"
IMAGE_INSTALL += "setup-softap"
IMAGE_INSTALL += "file"

IMAGE_ROOTFS_SIZE = "524288"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

inherit core-image

TOOLCHAIN_TARGET_TASK_append = " kernel-devsrc"

# delete the old image files that already exist
python do_rootfs_prepend() {
    os.system("rm -rf ${DEPLOY_DIR_IMAGE}/*${PN}*")
}

