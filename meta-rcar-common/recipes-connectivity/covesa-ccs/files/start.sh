#!/bin/bash

SCRIPT_DIR=$(cd `dirname $0` && pwd)
DBFILE_PATH="/var/statestorage.db" # default: serviceMgr/statestorage.db
targets="agt_server vissv2server"
killall ${targets} || true
sleep 5s
for target in ${targets}; do
    cd ${SCRIPT_DIR}/${target}
    ./${target} --dbfile=${DBFILE_PATH} &
done
wait

