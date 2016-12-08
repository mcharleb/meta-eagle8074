SUMMARY = "8x74 firmware image (cache partition)"

DEPENDS += "ext2simg-native"

# Cache partition is 32M
IMAGE_ROOTFS_SIZE = "32767"

inherit android-partition

IMAGE_INSTALL += "adreno200-firmware"
IMAGE_INSTALL += "mm-video-firmware"
IMAGE_INSTALL += "sdk-add-on-firmware"
IMAGE_INSTALL += "ath6kl-firmware"
IMAGE_INSTALL += "mm-camera-firmware"

# Only ar3k is copied via bbappend file
IMAGE_INSTALL += "linux-firmware-ar3k"

PACKAGE_INSTALL = "${IMAGE_INSTALL}"

IMAGE_PREPROCESS_COMMAND = "move_firmware_files"

python move_firmware_files() {
    import shutil

    # Remove the RPM handling additions
    workdir = d.get_var("WORKDIR", True)
    shutil.rmtree(workdir+"/rootfs/etc")
    shutil.rmtree(workdir+"/rootfs/var")

    # Since we are moving /lib/firmware to a new partition, fix paths
    shutil.move(workdir+"/rootfs/lib/firmware", workdir+"/rootfs/")
    shutil.rmtree(workdir+"/rootfs/lib")
}
