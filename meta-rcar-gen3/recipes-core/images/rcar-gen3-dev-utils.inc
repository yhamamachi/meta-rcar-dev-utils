IMAGE_INSTALL_append = " kernel-devicetree"
IMAGE_INSTALL_append = " renesas-bsp-rom-writer"
IMAGE_INSTALL_append = "\
    ${@bb.utils.contains('MACHINE', 'h3ulcb', 'flash-writer', '', d)} \
    ${@bb.utils.contains('MACHINE', 'm3ulcb', 'flash-writer', '', d)} \
"

