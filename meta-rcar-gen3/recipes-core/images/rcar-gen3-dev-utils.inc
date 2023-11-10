IMAGE_INSTALL:append = " kernel-devicetree"
IMAGE_INSTALL:append = " renesas-bsp-rom-writer"
IMAGE_INSTALL:append = "\
    ${@bb.utils.contains('MACHINE', 'h3ulcb', 'flash-writer', '', d)} \
    ${@bb.utils.contains('MACHINE', 'm3ulcb', 'flash-writer', '', d)} \
"

