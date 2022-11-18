DESCRIPTION = "Grafana"
LICENSE = "AGPL-3.0"
LIC_FILES_CHKSUM = "file://${S}/git-r0/src/github.com/grafana/grafana/LICENSE;md5=eb1e647870add0502f8f010b19de32af"

PACKAGE_ARCH = "${MACHINE_ARCH}"
RDEPENDS_${PN} = "bash"

inherit go
GO_IMPORT = "github.com/grafana/grafana"
GO_INSTALL = "${GO_IMPORT}"
GOPATH="${WORKDIR}/gopath"
GOBIN="${GOPATH}/bin"
BRANCH = "release-9.2.1"
SRCREV = "34f7baebda29994f240ce8375765f73be65dca4e"
SRC_URI = "git://${GO_IMPORT}.git;branch=${BRANCH};protocol=https"
UPSTREAM_CHECK_COMMITS = "1"
#export GO111MODULE="off"
#export CGO_ENABLED="0"

inherit systemd
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_FILENAME = "grafana.service"
SYSTEMD_SERVICE_${PN} = "${SYSTEMD_SERVICE_FILENAME}"

SRC_URI_append = " \
    file://${SYSTEMD_SERVICE_FILENAME} \
"

S = "${WORKDIR}"
B = "${S}/git-r0/src/github.com/grafana/grafana"

do_compile_prepend() {
    cd ${S}
    cd ${B}
    git reset --hard
    go install github.com/google/wire/cmd/wire@latest
    #go get -d github.com/google/wire/cmd/wire
    go mod tidy -modcacherw -go=1.18
    #go mod verify  -modcacherw
}


do_compile() {
    cd ${B}
    ${GOPATH}/bin/linux_arm64/wire gen -tags oss ./pkg/cmd/grafana-cli/runner
    ${GOPATH}/bin/linux_arm64/wire gen -tags oss ./pkg/server
    go run build.go build
    # move binart to upper directory
    mv ${B}/bin/*/* -t ${B}/bin
    rmdir ${B}/bin/* || true
}

CONF_FILE = "${D}/${USRBINPATH}/grafana/conf/defaults.ini"
do_install () {
    # service file
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${SYSTEMD_SERVICE_FILENAME} ${D}/${systemd_unitdir}/system

    # binary
    install -d ${D}/${USRBINPATH}/grafana/bin
    install -m 755 ${B}/bin/* ${D}/${USRBINPATH}/grafana/bin
    ITEMS="conf plugins-bundled public scripts"
    for item in ${ITEMS}; do \
        install -d ${D}/${USRBINPATH}/grafana/${item}
        cp -rf ${B}/${item}/* -t ${D}/${USRBINPATH}/grafana/${item}
    done

    # change some config
    echo '[server]' >> ${CONF_FILE}
    echo 'http_port = 3030' >> ${CONF_FILE}
    echo '[auth.anonymous]' >> ${CONF_FILE}
    echo 'enabled = true' >> ${CONF_FILE}
    echo 'org_role = Admin' >> ${CONF_FILE}
}

