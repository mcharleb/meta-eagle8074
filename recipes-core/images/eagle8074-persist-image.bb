SUMMARY = "8x74 persist partition image"

DEPENDS += "ext2simg-native"

# Cache partition is 32M
IMAGE_ROOTFS_SIZE = "32768"

inherit android-partition

PACKAGE_INSTALL = "${IMAGE_INSTALL}"

