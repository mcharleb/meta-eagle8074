inherit core-image

SUMMARY = "8x74 firmware image (cache partition)"

DEPENDS += "ext2simg-native"

IMAGE_INSTALL = ""

IMAGE_INSTALL += "adreno200-prebuilt-firmware"
IMAGE_INSTALL += "mm-video-prebuilt-firmware"
IMAGE_INSTALL += "sdk-add-on-firmware"
IMAGE_INSTALL += "ath6kl-firmware"

# delete the old image files that already exist
python do_rootfs_prepend() {
    os.system("rm -rf ${DEPLOY_DIR_IMAGE}/*${PN}*")
}

python do_rootfs_append() {
    os.system("ext2simg -v ${DEPLOY_DIR_IMAGE}/8x74-cache-image-${MACHINE}.ext4 ${DEPLOY_DIR_IMAGE}/out/cache-${MACHINE}.img")
}
