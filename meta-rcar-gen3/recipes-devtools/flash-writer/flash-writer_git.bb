DESCRIPTION = "Flash writer"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

PACKAGE_ARCH = "${MACHINE_ARCH}"
DEPENDS = "renesas-bsp-rom-writer"

inherit deploy

S = "${WORKDIR}/git"

BRANCH = "rcar_gen3"
SRC_URI = "git://github.com/renesas-rcar/flash_writer.git;branch=${BRANCH};protocol=https"
SRCREV = "89e18eaa543e548b4a49c7731dd998c4898eacd9"

PV = "V1.0.12+renesas+git${SRCPV}"

COMPATIBLE_MACHINE = "(ulcb)|(salvator-x)|(ebisu)|(draak)"
COMPATIBLE_MACHINE_append = "|(qemuarm)"

ALLOW_EMPTY_${PN} = "1"
ALLOW_EMPTY_${PN}-dev = "1"
ALLOW_EMPTY_${PN}-staticdev = "1"

SRC_URI_append = " \
    file://0001-Fix-error-when-executing-mkdir.patch \
"
ARCH = "${@'32' if d.getVar('TARGET_ARCH') == 'arm' else '64'}"

EXTRA_OEMAKE = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${TARGET_PREFIX}gcc ${TOOLCHAIN_OPTIONS}" V=1'
EXTRA_OEMAKE_append = " AArch=${ARCH} BOARD=ULCB"

# do_install() nothing
do_install[noexec] = "1"

do_deploy() {
    # Create deploy folder
    install -d ${DEPLOYDIR}/renesas-bsp-rom-writer_${ARCH}bit/starterkit/

    # Copy IPL to deploy folder
    install -m 0644 ${B}/AArch${ARCH}_output/AArch${ARCH}_Flash_writer_SCIF_DUMMY_CERT_E6300400_ULCB.mot \
        ${DEPLOYDIR}/renesas-bsp-rom-writer_${ARCH}bit/starterkit/
}

addtask deploy before do_build after do_compile

