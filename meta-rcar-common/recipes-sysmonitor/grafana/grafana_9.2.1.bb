DESCRIPTION = "Grafana"
LICENSE = "AGPL-3.0"
LIC_FILES_CHKSUM = "file://grafana-9.2.1/LICENSE;md5=eb1e647870add0502f8f010b19de32af"

PACKAGE_ARCH = "${MACHINE_ARCH}"
RDEPENDS_${PN} = "bash"

BIN_FILENAME = "grafana-9.2.1.linux-arm64.tar.gz"
BRANCH = "release-9.2.1"
SRCREV = "34f7baebda29994f240ce8375765f73be65dca4e"
SRC_URI = "git://github.com/grafana/grafana.git;branch=${BRANCH};protocol=https"
SRC_URI_append = " https://dl.grafana.com/oss/release/${BIN_FILENAME};name=bin"
SRC_URI[bin.sha256sum] = "80f56f0509cd7dea5597c7132f92cc422394877d3e24da58b3b3065ded6a2a13"

inherit systemd
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_FILENAME = "grafana.service"
SYSTEMD_SERVICE_${PN} = "${SYSTEMD_SERVICE_FILENAME}"

SRC_URI_append = " \
    file://${SYSTEMD_SERVICE_FILENAME} \
"

S = "${WORKDIR}"

# do_configure() nothing
do_configure[noexec] = "1"
# do_compile() nothing
do_compile[noexec] = "1"

CONF_FILE = "${D}/${USRBINPATH}/grafana/conf/defaults.ini"
do_install () {
    # service file
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${SYSTEMD_SERVICE_FILENAME} ${D}/${systemd_unitdir}/system

    # binary
    install -d ${D}/${USRBINPATH}/grafana/bin
    install -m 755 ${S}/grafana-9.2.1/bin/* ${D}/${USRBINPATH}/grafana/bin
    ITEMS="conf plugins-bundled public scripts"
    for item in ${ITEMS}; do \
        install -d ${D}/${USRBINPATH}/grafana/${item}
        cp -rf ${S}/grafana-9.2.1/${item}/* -t ${D}/${USRBINPATH}/grafana/${item}
    done

    # change some config
    echo '[server]' >> ${CONF_FILE}
    echo 'http_port = 3030' >> ${CONF_FILE}
    echo '[auth.anonymous]' >> ${CONF_FILE}
    echo 'enabled = true' >> ${CONF_FILE}
    echo 'org_role = Admin' >> ${CONF_FILE}
}

