SUMMARY = "8x74 firmware image (cache partition)"

DEPENDS += "ext2simg-native"

# Cache partition is 28M
IMAGE_ROOTFS_SIZE = "28672"

inherit image-empty

IMAGE_INSTALL += "adreno200-firmware"
IMAGE_INSTALL += "mm-video-firmware"
IMAGE_INSTALL += "ath6kl-firmware"
IMAGE_INSTALL += "mm-camera-firmware"

# Only ar3k is copied via bbappend file
IMAGE_INSTALL += "linux-firmware-ar3k"

PACKAGE_INSTALL = "${IMAGE_INSTALL}"

