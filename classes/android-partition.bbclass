LICENSE = "MIT"

IMAGE_INSTALL = ""
IMAGE_INSTALL = ""
IMAGE_LINGUAS = ""
PACKAGE_INSTALL = ""

inherit image

IMAGE_PREPROCESS_COMMAND = "remove_extra_files"

# delete the old image files that already exist
python do_rootfs_prepend() {
    import glob, os

    deploydir = d.getVar("DEPLOY_DIR_IMAGE", True)
    pn = d.getVar("PN", True)
    for f in glob.glob(deploydir+"/*"+pn+"*"):
        os.remove(f)
}

python remove_extra_files() {
    import shutil

    # Remove the RPM handling additions
    workdir = d.getVar("WORKDIR", True)
    shutil.rmtree(workdir+"/rootfs/etc")
    shutil.rmtree(workdir+"/rootfs/var")
}

python do_rootfs_append() {
    import os

    deploydir = d.getVar("DEPLOY_DIR_IMAGE", True)
    pn = d.getVar("PN", True)
    machine = d.getVar("MACHINE", True)
    rootname = deploydir+"/"+pn+"-"+machine
    os.system("ext2simg -v "+rootname+".ext4 "+rootname+".img")
}
