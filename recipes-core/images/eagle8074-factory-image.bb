SUMMARY = "eagle8074 factory partition image"

DEPENDS += "ext2simg-native"

# Factory partition is 512M
IMAGE_ROOTFS_SIZE = "524288"

inherit android-partition

#IMAGE_INSTALL += "eagle8074-factory-image"

PACKAGE_INSTALL = "${IMAGE_INSTALL}"

