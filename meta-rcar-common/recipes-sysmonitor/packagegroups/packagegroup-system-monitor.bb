SUMMARY = "Packagegroup for System monitor"
LICENSE = "AGPL-3.0 & Apache-2.0"

inherit packagegroup

PR = "r0"

PACKAGES = " \
    ${PN} \
"

RDEPENDS:${PN} = " \
    grafana \
    node-exporter \
    prometheus \
"


