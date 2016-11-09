do_install_append() {
	echo "# Disabled by meta-eagle-8074" > ${D}/lib/udev/rules.d/60-persistent-v4l.rules
}
