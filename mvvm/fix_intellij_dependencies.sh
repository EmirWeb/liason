#!/usr/bin/env bash

set -o pipefail
set -o errexit
set -o nounset
# set -o xtrace

__DIR__="$(cd "$(dirname "${0}")"; echo $(pwd))"
__BASE__="$(basename "${0}")"
__FILE__="${__DIR__}/${__BASE__}"

file="${__DIR__}/application.iml"
tmp_file="application.iml.tmp"
lines=$(cat ${file} | wc -l)
needed_lines=$((lines-3))
framework='<orderEntry type="jdk" jdkName="Android API 19 Platform" jdkType="Android SDK" />'

head -n ${needed_lines} ${file} | grep -v "${framework}" > ${tmp_file}

cat << EOF >> ${tmp_file}
    ${framework}
  </component>
</module>

EOF

mv ${tmp_file} ${file}