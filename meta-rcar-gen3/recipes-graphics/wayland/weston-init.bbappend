FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

do_install_append_rcar-gen3() {
    sed -i -e '$a\idle-time=0\' ${WORKDIR}/weston.ini
}

