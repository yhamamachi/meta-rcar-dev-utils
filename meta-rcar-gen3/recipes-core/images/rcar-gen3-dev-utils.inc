IMAGE_INSTALL_append = " kernel-devicetree"
IMAGE_INSTALL_append = " renesas-bsp-rom-writer"
IMAGE_INSTALL_append = " ${@' flash-writer' if d.getVar('MACHINE') == 'ulcb'}"
