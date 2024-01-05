## Python installs
/usr/local/bin/python3.9 --version
/usr/local/bin/python3.12 --version

## Run integration tests from the command line
/usr/local/bin/python3.9 -m pipenv install
/usr/local/bin/python3.9 -m pipenv run python ./src/run_integ_test.py \
   manifests/2.12.0/opensearch-2.12.0-test.yml \
   --paths opensearch=https://ci.opensearch.org/ci/dbc/distribution-build-opensearch/1.3.5/5960/linux/x64/tar \
   --component security
