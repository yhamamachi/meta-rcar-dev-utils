DESCRIPTION = "W3C Automotive Interface Implementation - WAII"
SECTION = "examples"
HOMEPAGE = "https://github.com/w3c/automotive"

PACKAGE_ARCH = "${MACHINE_ARCH}"
RDEPENDS_${PN} = "bash"
RDEPENDS_${PN}-dev = "bash"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

BRANCH = "master"
SRC_URI = "git://${GO_IMPORT};branch=${BRANCH};protocol=https"
SRCREV = "d0d5befbc33fff60b5781b1bdde0df245f6ee492"
UPSTREAM_CHECK_COMMITS = "1"

inherit go
GO_IMPORT = "github.com/w3c/automotive-viss2"
GO_INSTALL = "${GO_IMPORT}/server"

inherit systemd
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_FILENAME = "waii.service"
SYSTEMD_SERVICE_${PN} = "${SYSTEMD_SERVICE_FILENAME}"

SRC_URI_append = " \
    file://${SYSTEMD_SERVICE_FILENAME} \
    file://start.sh \
"


do_compile_prepend() {
    cd ${S}/src/github.com/w3c/automotive-viss2/
    sed -i s'|.* => /home/.*||' ./go.mod
    go mod tidy -modcacherw
    cd ${S}/src/github.com/w3c/automotive-viss2/server
    go mod tidy -modcacherw
}

services="vissv2server agt_server"
do_compile() {
    for service in ${services}; do \
        cd ${S}/src/github.com/w3c/automotive-viss2/server/${service}
        CGO_ENABLED=0 go build -modcacherw
    done
}

FILES_${PN} = " \
    ${USRBINPATH}/waii/* \
    /var/* \
"

DBFILE_PATH = "${S}/src/github.com/w3c/automotive-viss2/server/vissv2server/serviceMgr/statestorage.db"

do_install() {
    # service file
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${SYSTEMD_SERVICE_FILENAME} ${D}/${systemd_unitdir}/system

    # binary
    install -d ${D}/${USRBINPATH}/waii
    for service in ${services} transport_sec; do \
        cp -r ${S}/src/github.com/w3c/automotive-viss2/server/${service} \
            ${D}/${USRBINPATH}/waii/
    done
    # Remove source code from directory
    find ${D}/${USRBINPATH}/waii/ -name "*.go" \
        | xargs rm -f

    # Copy script
    install -m 0755 ${WORKDIR}/start.sh ${D}/${USRBINPATH}/waii
    # Copy dbfile
    install -d ${D}/var
    install -m 0644 ${DBFILE_PATH} ${D}/var
}

inherit deploy
do_deploy() {
    # Copy Database file to deploy directory
    install -d {${DEPLOY_DIR_IMAGE}
    install -m 0644 ${DBFILE_PATH} ${DEPLOY_DIR_IMAGE}
}
addtask deploy before do_build after do_compile

