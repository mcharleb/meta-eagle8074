inherit core-image

SUMMARY = "Eagle rootfs image"

DEPENDS += "lk ext2simg-native"

# rootfs partition is 8G 
IMAGE_ROOTFS_SIZE = "8388608"

IMAGE_INSTALL = "packagegroup-core-boot ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_INSTALL += "android-tools"
IMAGE_INSTALL += "libstdc++"
IMAGE_INSTALL += "adsprpc"
IMAGE_INSTALL += "kernel-module-wlan"
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "hostapd"
IMAGE_INSTALL += "wpa-supplicant"
IMAGE_INSTALL += "pciutils"
IMAGE_INSTALL += "setup-softap"
IMAGE_INSTALL += "file"

# From QRLinux
IMAGE_INSTALL += "hostapd"
IMAGE_INSTALL += "libnl"
IMAGE_INSTALL += "dnsmasq"
IMAGE_INSTALL += "ueventd"
IMAGE_INSTALL += "frameworks-av"
IMAGE_INSTALL += "post-boot"
IMAGE_INSTALL += "libhardware"
IMAGE_INSTALL += "power-hal"

IMAGE_INSTALL += "ath3k-bluez"
IMAGE_INSTALL += "qrl-scripts"

# From meta-qti qrl-binaries.bbappend
IMAGE_INSTALL += "reboot2fastboot"
IMAGE_INSTALL += "diag"
IMAGE_INSTALL += "mp-decision"
IMAGE_INSTALL += "qmi"
IMAGE_INSTALL += "qmi-framework"
IMAGE_INSTALL += "thermal-engine"
IMAGE_INSTALL += "ath6kl-utils"
IMAGE_INSTALL += "q6-admin"
IMAGE_INSTALL += "ss-restart"
IMAGE_INSTALL += "ftmdaemon"
IMAGE_INSTALL += "fastmmi"
IMAGE_INSTALL += "remote-debug-agent"
IMAGE_INSTALL += "perf-tools"

# Kernel
IMAGE_INSTALL += "kernel-module-wlan"
IMAGE_INSTALL += "depmodwrapper-cross"
IMAGE_INSTALL += "compat-wireless"
IMAGE_INSTALL += "kernel-module-rdbg"

# Missing
IMAGE_INSTALL += "openssl"
IMAGE_INSTALL += "rsyslog"
IMAGE_INSTALL += "dropbear"

IMAGE_ROOTFS_SIZE = "524288"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

# Adding the kernel source add 577M to the SDK
# Its preferable to let the developers just clone the kernel at a specific label
# A script to clone the kernel can be provided
# TOOLCHAIN_TARGET_TASK_append = " kernel-${MACHINE}-devsrc"

PACKAGECONFIG_pn-qemu-native = ""

# delete the old image files that already exist
python do_rootfs_prepend() {
    os.system("rm -rf ${DEPLOY_DIR_IMAGE}/*${PN}*")
}

python do_rootfs_append() {
    os.system("ext2simg -v ${DEPLOY_DIR_IMAGE}/eagle8074-userdata-image-${MACHINE}.ext4 ${DEPLOY_DIR_IMAGE}/eagle8074-userdata-image-${MACHINE}.img")
}
