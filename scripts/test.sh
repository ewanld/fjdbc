#! /bin/bash
set -o nounset
set -o errexit

pushd ..
	mvn test
popd
diff -u ../target/test-classes/FjdbcTest-ref.txt ../target/test-classes/FjdbcTest-last.txt --color=auto
