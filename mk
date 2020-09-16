#
set -xe

rm -fr src
mkdir -p src
unzip -q /opt/jfx15/lib/src.zip -d src

rm -fr mod
mkdir -p mod

./mk.base
./mk.graphics
./mk.controls

./mk.jre

