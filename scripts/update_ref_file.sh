#! /bin/bash
set -o nounset
set -o errexit

cp ../target/test-classes/FjdbcTest-last.txt ../src/test/resources/FjdbcTest-ref.txt
